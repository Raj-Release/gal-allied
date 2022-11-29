package com.shaic.domain.omp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.ReportDto;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.claimhistory.view.ompView.OMPPreviousClaimMapper;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.omp.registration.OMPPreviousClaimTableDTO;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationFormDto;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewBabyIntimation;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpProcedure;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.ViewTmpDiagnosis;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.OMPNewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class OMPIntimationService {
	

	
	 private final Logger log = LoggerFactory.getLogger(OMPIntimationService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	PolicyService policyService;		
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompClaimService;

	public OMPIntimationService() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RegistrationBean> searchbyIntimationNo(
			List<RegistrationBean> registrationList) {
		List validRegistrationList = new ArrayList<RegistrationBean>();
		for (RegistrationBean instance : registrationList) {
			Query findByIntimNo = entityManager.createNamedQuery(
					"OMPIntimation.findByIntimationNumber").setParameter(
					"intimationNo", instance.getIntimationNo());
			try {
				List resultList = findByIntimNo.getResultList();

				if (!resultList.isEmpty()) {
					OMPIntimation intimationInstance = (OMPIntimation) resultList
							.get(0);
					instance.setIntimationDate(intimationInstance
							.getCreatedDate());
					instance.setHospitalType(intimationInstance
							.getHospitalType() != null ? intimationInstance
							.getHospitalType().getValue() : null);
					instance.setOmpIntimation(intimationInstance);
					validRegistrationList.add(instance);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return validRegistrationList;
	}

	public List<OMPSearchClaimRegistrationTableDTO> searchOmpIntimationToRegister(
			List<OMPSearchClaimRegistrationTableDTO> searchClaimRegistrationDtoList) {
		
		List<OMPSearchClaimRegistrationTableDTO> validRegistrationList = new ArrayList<OMPSearchClaimRegistrationTableDTO>();
		for (OMPSearchClaimRegistrationTableDTO searchDto : searchClaimRegistrationDtoList) {
			Query findByIntimNo = entityManager.createNamedQuery(
					"OMPIntimation.findByOMPIntimationNo").setParameter(
					"intimationNo", searchDto.getIntimationNo());
			try {
				findByIntimNo.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
				List resultList = findByIntimNo.getResultList();
				
				

				if (!resultList.isEmpty()) {
					OMPIntimation intimationInstance = (OMPIntimation) resultList
							.get(0);
					searchDto.setIntimationDate(intimationInstance
							.getCreatedDate());
//					searchDto.setHospitalType(intimationInstance
//							.getHospitalType() != null ? intimationInstance
//							.getHospitalType().getValue() : null);
					searchDto.setNewIntimationDto(OMPNewIntimationMapper.getInstance()
							.getNewIntimationDto(intimationInstance));
					searchDto.getNewIntimationDto().setPolicy(
							intimationInstance.getPolicy());
					validRegistrationList.add(searchDto);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		

		return validRegistrationList;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<OMPIntimation> searchAll() {
		Query findAll = entityManager.createNamedQuery("OMPIntimation.findAll");
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findAll
				.getResultList();
		return intimationList;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public OMPIntimation getIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("OMPIntimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<OMPIntimation> intimationList = (List<OMPIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public OMPIntimation getTmpIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("OMPIntimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<OMPIntimation> intimationList = (List<OMPIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public OMPIntimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"OMPIntimation.findByOMPIntimationNo").setParameter(
				"intimationNo", intimationNo);
		findByKey.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public NewBabyIntimation getNewBabyByIntimationKey(Long intimationKey) {
		try{
		Query findByKey = entityManager.createNamedQuery(
				"NewBabyIntimation.findByIntimation").setParameter(
				"intimationKey", intimationKey);

		List<NewBabyIntimation> intimationList = (List<NewBabyIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList.get(0);

		}
		}catch(Exception e){
		
		}
		return null;
	}

	/*
	 * @SuppressWarnings({ "unchecked", "unused" }) public
	 * BeanItemContainer<IntimationDTO> searchCombination( Map<String, Object>
	 * params) {
	 * 
	 * BeanItemContainer<IntimationDTO> intimationListContainer = new
	 * BeanItemContainer<IntimationDTO>( IntimationDTO.class); List<OMPIntimation>
	 * searchResultList = new ArrayList<OMPIntimation>();
	 * 
	 * if (!params.isEmpty()) { String intimationStatus; String
	 * intimationNumber; String claimNumber; String policyNumber; String
	 * insuredName; String healthCardNumber; String intimationYear; String
	 * intimationSeqNumber; String hospitalName; Date fromDate; Date toDate;
	 * 
	 * intimationStatus = params.containsKey("intimationStatus") ? StringUtils
	 * .trim(params.get("intimationStatus").toString()) : null; intimationNumber
	 * = params.containsKey("intimationNumber") ? "%" +
	 * StringUtils.trim(params.get("intimationNumber") .toString()) + "%" :
	 * null; policyNumber = params.containsKey("policyNumber") ? "%" +
	 * StringUtils.trim(params.get("policyNumber").toString()) + "%" : null;
	 * insuredName = params.containsKey("insuredName") ? "%" +
	 * StringUtils.trim(params.get("insuredName").toString() .toUpperCase()) +
	 * "%" : null; healthCardNumber = params.containsKey("healthCardNumber") ?
	 * "%" + StringUtils.trim(params.get("healthCardNumber") .toString()) + "%"
	 * : null; hospitalName = params.containsKey("hospitalName") ? "%" +
	 * StringUtils.trim(params.get("hospitalName").toString()) + "%" : null;
	 * fromDate = params.containsKey("fromDate") ? (Date) params
	 * .get("fromDate") : null; toDate = params.containsKey("toDate") ? (Date)
	 * params.get("toDate") : null; claimNumber =
	 * params.containsKey("claimNumber") ? "%" +
	 * StringUtils.trim(params.get("claimNumber").toString()) + "%" : null;
	 * 
	 * try { final CriteriaBuilder builder = entityManager
	 * .getCriteriaBuilder(); final CriteriaQuery<OMPIntimation> criteriaQuery =
	 * builder .createQuery(OMPIntimation.class);
	 * 
	 * Root<OMPIntimation> intimationRoot = criteriaQuery .from(OMPIntimation.class);
	 * Join<OMPIntimation, Policy> policyJoin = intimationRoot.join( "policy",
	 * JoinType.INNER);
	 * 
	 * List<Predicate> predicates = new ArrayList<Predicate>();
	 * 
	 * List<OMPIntimation> resultList = new ArrayList<OMPIntimation>();
	 * 
	 * if (claimNumber == null) { if (intimationStatus != null) {
	 * 
	 * Predicate statusPredicate = builder.like( intimationRoot.<String>
	 * get("status"), intimationStatus); predicates.add(statusPredicate); }
	 * 
	 * if (policyNumber != null) { Predicate policyPredicate = builder.like(
	 * intimationRoot.<Policy> get("policy") .<String> get("policyNumber"),
	 * policyNumber); predicates.add(policyPredicate); }
	 * 
	 * if (intimationNumber != null) { Predicate intimationNoPredicate =
	 * builder.like( intimationRoot.<String> get("intimationId"),
	 * intimationNumber); predicates.add(intimationNoPredicate); }
	 * 
	 * if (insuredName != null) { Predicate insuredNamePredicate = builder.like(
	 * builder.upper(intimationRoot.<Policy> get( "policy").<String> get(
	 * "insuredFirstName")), insuredName); predicates.add(insuredNamePredicate);
	 * }
	 * 
	 * if (healthCardNumber != null) { Predicate healthCardNumberPredicate =
	 * builder.like( intimationRoot.<Policy> get("policy") .<String>
	 * get("healthCardNumber"), healthCardNumber);
	 * predicates.add(healthCardNumberPredicate); }
	 * 
	 * // if(hospitalName != null) // { // Predicate healthCardNumberPredicate =
	 * // builder.like(hospitalJoin.<String>get("name"), // hospitalName); //
	 * predicates.add(healthCardNumberPredicate); // }
	 * 
	 * if (fromDate != null && toDate != null) { Expression<Date>
	 * fromDateExpression = intimationRoot .<Date> get("createdDate"); Predicate
	 * fromDatePredicate = builder .greaterThanOrEqualTo(fromDateExpression,
	 * fromDate); predicates.add(fromDatePredicate);
	 * 
	 * Expression<Date> toDateExpression = intimationRoot .<Date>
	 * get("createdDate"); Calendar c = Calendar.getInstance();
	 * c.setTime(toDate); c.add(Calendar.DATE, 1); toDate = c.getTime();
	 * Predicate toDatePredicate = builder.lessThanOrEqualTo( toDateExpression,
	 * toDate); predicates.add(toDatePredicate); }
	 * criteriaQuery.select(intimationRoot) .where(builder.and(predicates
	 * .toArray(new Predicate[] {})));
	 * 
	 * final TypedQuery<OMPIntimation> intimationquery = entityManager
	 * .createQuery(criteriaQuery); resultList =
	 * intimationquery.getResultList(); } else if (claimNumber != null) { final
	 * CriteriaQuery<Claim> claimCriteriaQuery = builder
	 * .createQuery(Claim.class);
	 * 
	 * Root<Claim> claimRoot = claimCriteriaQuery .from(Claim.class);
	 * Join<Claim, OMPIntimation> claimJoin = claimRoot.join( "intimation",
	 * JoinType.INNER);
	 * 
	 * Predicate claimNumberPredicate = builder.like( claimRoot.<String>
	 * get("claimId"), claimNumber); predicates.add(claimNumberPredicate);
	 * 
	 * if (intimationStatus != null) {
	 * 
	 * Predicate statusPredicate = builder.like( claimRoot.<OMPIntimation>
	 * get("intimation") .<String> get("status"), intimationStatus);
	 * predicates.add(statusPredicate); }
	 * 
	 * if (policyNumber != null) { Predicate policyPredicate = builder.like(
	 * claimRoot.<OMPIntimation> get("intimation") .<Policy> get("policy")
	 * .<String> get("policyNumber"), policyNumber);
	 * predicates.add(policyPredicate); }
	 * 
	 * if (intimationNumber != null) { Predicate intimationNoPredicate =
	 * builder.like( claimRoot.<OMPIntimation> get("intimation") .<String>
	 * get("intimationId"), intimationNumber);
	 * predicates.add(intimationNoPredicate); }
	 * 
	 * if (insuredName != null) { Predicate insuredNamePredicate = builder.like(
	 * claimRoot.<OMPIntimation> get("intimation") .<Policy> get("policy")
	 * .<String> get("insuredFirstName"), insuredName);
	 * predicates.add(insuredNamePredicate); }
	 * 
	 * if (healthCardNumber != null) { Predicate healthCardNumberPredicate =
	 * builder.like( claimRoot.<OMPIntimation> get("intimation") .<Policy>
	 * get("policy") .<String> get("healthCardNumber"), healthCardNumber);
	 * predicates.add(healthCardNumberPredicate); }
	 * 
	 * // if(hospitalName != null) // { // Predicate healthCardNumberPredicate =
	 * // builder.like(hospitalJoin.<String>get("name"), // hospitalName); //
	 * predicates.add(healthCardNumberPredicate); // }
	 * 
	 * if (fromDate != null && toDate != null) { Predicate createdDatePredicate
	 * = builder.between( claimRoot.<OMPIntimation> get("intimation") .<Date>
	 * get("createdDate"), fromDate, toDate);
	 * predicates.add(createdDatePredicate); }
	 * claimCriteriaQuery.select(claimRoot) .where(builder.and(predicates
	 * .toArray(new Predicate[] {})));
	 * 
	 * final TypedQuery<Claim> claimQuery = entityManager
	 * .createQuery(claimCriteriaQuery); List<Claim> claimList = (List<Claim>)
	 * claimQuery .getResultList(); resultList = new ArrayList<OMPIntimation>(); if
	 * (claimList != null && !claimList.isEmpty()) { for (Claim claim :
	 * claimList) { resultList.add(claim.getIntimation()); } } } if (resultList
	 * != null && !resultList.isEmpty()) { for (OMPIntimation a_intimation :
	 * resultList) {
	 * 
	 * IntimationDTO intimationDetailsDTO = new IntimationMapper()
	 * .getIntimationDTO(a_intimation); Hospitals hospital = null; TmpHospital
	 * tmpHospital = null; if
	 * (!ValidatorUtils.isNull(a_intimation.getHospital())) { if
	 * (a_intimation.getHospitalType().getValue()
	 * .toLowerCase().contains("network")) { hospital =
	 * entityManager.find(Hospitals.class, a_intimation.getHospital()); }
	 * 
	 * }
	 * 
	 * if (hospital != null) {
	 * 
	 * intimationDetailsDTO.getHospitalDetails()
	 * .getHospital().setName(hospital.getName()); intimationDetailsDTO
	 * .getHospitalDetails() .getHospitalType() .setValue(
	 * hospital.getHospitalType() .getValue());
	 * 
	 * //
	 * intimationDetailsDTO.getHospitalDetails().getCity().setValue(hospitalObj
	 * .getCity()); //
	 * intimationDetailsDTO.getHospitalDetails().getState().setValue
	 * (hospitalObj.getState());
	 * 
	 * }
	 * 
	 * String patientName = ""; if (a_intimation.getInsuredPatientName() !=
	 * null) { patientName = a_intimation.getInsuredPatientName(); }
	 * intimationDetailsDTO.getIntimationDetails()
	 * .setInsuredPatientName(patientName); String healthcardNum = ""; if
	 * (a_intimation.getPolicy().getHealthCardNumber() != null) { healthcardNum
	 * = a_intimation.getPolicy() .getHealthCardNumber(); }
	 * 
	 * intimationDetailsDTO.getIntimationDetails() .getInsuredPatientId()
	 * .setHealthCardNumber(healthcardNum);
	 * 
	 * if (a_intimation.getPolicy().getInsuredId() != null) {
	 * intimationDetailsDTO .getIntimationDetails() .getInsuredPatientId()
	 * .setKey(Long.valueOf(a_intimation .getPolicy().getInsuredId())); }
	 * 
	 * intimationListContainer.addBean(intimationDetailsDTO); } return
	 * intimationListContainer; } else { // No results Found Query result list
	 * empty } } catch (Exception E) {
	 * System.out.println("Exception while query Execution on DB");
	 * E.printStackTrace();
	 * 
	 * } finally {
	 * 
	 * } }
	 * 
	 * else { // Param empty System.out.println("Params  empty"); } return null;
	 * }
	 */
	
//	public boolean isOPintimation(Long intimationKey){
//		
//		OMPClaim clmObj = getClaimforIntimation(intimationKey);
//		
//		if(clmObj != null && (ReferenceTable.OP_STAGE).equals(clmObj.getStage().getKey())){
//			return true;
//		}
//		
//		return false;
//	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Page<NewIntimationDto> searchCombinationforViewAndEditReport(SearchIntimationFormDto searchDto, EntityManager entityManager ) {
		this.entityManager = entityManager;
		return searchCombinationforViewAndEdit(searchDto);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Page<NewIntimationDto> searchCombinationforViewAndEdit(SearchIntimationFormDto searchDto) {
		Map<String, Object> params = searchDto.getFilters();

		Page<NewIntimationDto> pagedIntimationListContainer = new Page<NewIntimationDto>();
		List<OMPIntimation> resultList = new ArrayList<OMPIntimation>();

		Pageable apageable = params.containsKey("pageable") ? (Pageable)params.get("pageable") : null;
		
		int pageNumber = apageable != null ? apageable.getPageNumber() : 1; 
		
		List<NewIntimationDto> intimationDtoList = new ArrayList<NewIntimationDto>();
		
		resultList = searchIntimationByFilters(params,apageable);

				if (resultList != null && !resultList.isEmpty()) {
					for (OMPIntimation a_intimation : resultList) {
						
						OMPClaim clmObj = getClaimforIntimation(a_intimation.getKey());
//						Claim  clmObj = entityManager.find(Claim.class,  a_intimation.getKey());
						
						if(clmObj != null && (ReferenceTable.OP_STAGE).equals(clmObj.getStage().getKey())){
							continue;
						}
						else{
						NewIntimationDto newIntimationDto = OMPNewIntimationMapper.getInstance()
								.getNewIntimationDto(a_intimation);
						newIntimationDto.setPolicy(a_intimation.getPolicy());
						newIntimationDto.setInsuredPatient(a_intimation
								.getInsured());
						Hospitals hospital = null;
						TmpHospital tmpHospital = null;
						if (!ValidatorUtils.isNull(a_intimation.getHospital())) {
							if (a_intimation.getHospitalType().getValue()
									.toLowerCase().contains("network")) {
//								hospital = entityManager.find(Hospitals.class,
//										a_intimation.getHospital());
								if(a_intimation.getHospital() != null){
									hospital = getHospitalDetailsByKey(a_intimation.getHospital());
								}
							} else {
//								tmpHospital = entityManager.find(
//										TmpHospital.class,
//										a_intimation.getHospital());
							}

						}

						if (hospital != null) {
							newIntimationDto.getHospitalDto()
									.setRegistedHospitals(hospital);
							newIntimationDto.getHospitalDto().setName(
									hospital.getName());
							newIntimationDto
									.getHospitalDto()
									.getHospitalType()
									.setValue(
											hospital.getHospitalType()
													.getValue());
							newIntimationDto.getHospitalDto().getHospitalType()
									.setId(hospital.getHospitalType().getKey());
							SelectValue hospitalType = new SelectValue();
							hospitalType.setId(hospital.getHospitalType()
									.getKey());
							hospitalType.setValue(hospital.getHospitalType()
									.getValue());
							newIntimationDto.setHospitalType(hospitalType);
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());
							
							SelectValue claimType = getClaimType(hospital.getHospitalType().getKey());
						
							newIntimationDto.setClaimType(claimType);	
							
							newIntimationDto.getHospitalDto().setCity(
									hospital.getCity());
							newIntimationDto.getHospitalDto().setState(
									hospital.getState());
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());

							CityTownVillage city = null;
							if(hospital.getCityId() != null){
								city = entityManager
										.find(CityTownVillage.class,
												hospital.getCityId());
							}
							if (city != null) {

								newIntimationDto.getHospitalDto().setCity(
										city.getValue());
								newIntimationDto.setCity(city);
							}
							
							State state = null;
							if(hospital.getStateId() != null){
								state = entityManager.find(State.class,
										hospital.getStateId());
							}
							

							if (state != null) {
								newIntimationDto.setState(state);
								newIntimationDto.getHospitalDto().setState(
										state.getValue());
							}

							newIntimationDto.getHospitalDto().setCity(
									hospital.getCity());
							newIntimationDto.getHospitalDto().setState(
									hospital.getState());

						}

						if (tmpHospital != null) {
							newIntimationDto.getHospitalDto()
									.setNotRegisteredHospitals(tmpHospital);
							newIntimationDto.getHospitalDto().setName(
									tmpHospital.getHospitalName());
							newIntimationDto
									.getHospitalDto()
									.getHospitalType()
									.setValue(
											a_intimation.getHospitalType()
													.getValue());
							newIntimationDto
									.getHospitalDto()
									.getHospitalType()
									.setId(a_intimation.getHospitalType()
											.getKey());
							SelectValue hospitalType = new SelectValue();
							hospitalType.setId(a_intimation.getHospitalType()
									.getKey());
							hospitalType.setValue(a_intimation
									.getHospitalType().getValue());
							newIntimationDto.setHospitalType(hospitalType);
							newIntimationDto.setHospitalTypeValue(hospitalType
									.getValue());

							CityTownVillage city = entityManager.find(
									CityTownVillage.class,
									tmpHospital.getCityId());
							if (city != null) {

								newIntimationDto.getHospitalDto().setCity(
										city.getValue());
								newIntimationDto.setCity(city);
							}
							State state = entityManager.find(State.class,
									tmpHospital.getStateId());

							if (state != null) {
								newIntimationDto.setState(state);
								newIntimationDto.getHospitalDto().setState(
										state.getValue());
							}
						}
						
//						MastersValue admissionType = a_intimation.getAdmissionType();
//						
//						SelectValue admitionTypeMast = new SelectValue();
//						
//						if(admissionType != null){
//							admitionTypeMast.setId(admissionType.getKey());
//							admitionTypeMast.setValue(admissionType.getValue());						
//						}
//						newIntimationDto.setAdmissionType(admitionTypeMast);						

						
						MastersValue intimatinMode = a_intimation.getIntimationMode();
						SelectValue intimatoinModeMast = new SelectValue();
						
						if(intimatinMode != null){
							intimatoinModeMast.setId(intimatinMode.getKey());
							intimatoinModeMast.setValue(intimatinMode.getValue());						
						}
									
						newIntimationDto.setModeOfIntimation(intimatoinModeMast);
						
						
						String patientName = "";
						if (a_intimation.getInsuredName() != null) {
							patientName = a_intimation.getInsuredName();
						}
						newIntimationDto.setInsuredPatientName(patientName);
						
						newIntimationDto.setIntimationSource(a_intimation
								.getIntimationSource());
						intimationDtoList.add(newIntimationDto);						
					}
						
				}
					if(apageable != null){					
						searchDto.getPageable().setPageNumber(pageNumber+1);
						if(intimationDtoList == null || intimationDtoList.isEmpty()){
							searchDto.getPageable().setPageNumber(1);
						}
						pagedIntimationListContainer.setHasNext(true);
						pagedIntimationListContainer.setPageNumber(pageNumber);
						pagedIntimationListContainer.setPageItems(intimationDtoList);
					}
					else{
						pagedIntimationListContainer.setPageItems(intimationDtoList);
					}
					
					return pagedIntimationListContainer;
				} else {
					// No results Found Query result list empty
				}
		
		return null;
	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	public List<OMPIntimation> searchIntimationByFilters(Map<String, Object> params,Pageable apageable,EntityManager em){
		this.entityManager = em;
		return searchIntimationByFilters(params,apageable);
	}
	
	private List<OMPIntimation> searchIntimationByFilters(Map<String, Object> params,Pageable apageable){
	
		List<OMPIntimation> resultList = new ArrayList<OMPIntimation>();
		int pageNumber = 1;
		int firstResult = 1;
		if (!params.isEmpty()) {
			String intimationStatus;
			String intimationNumber;
			String claimNumber;
			String policyNumber;
			String insuredName;
			String healthCardNumber;
			String intimationYear;
			String intimationSeqNumber;
			String hospitalName;
			Date fromDate;
			Date toDate;			

			intimationStatus = params.containsKey("intimationStatus") ? StringUtils
					.trim(params.get("intimationStatus").toString()) : null;
			intimationNumber = params.containsKey("intimationNumber") ? "%"
					+ StringUtils.trim(params.get("intimationNumber")
							.toString()) + "%" : null;
			policyNumber = params.containsKey("policyNumber") ? "%"
					+ StringUtils.trim(params.get("policyNumber").toString())
					+ "%" : null;
			insuredName = params.containsKey("insuredName") ? "%"
					+ StringUtils.trim(params.get("insuredName").toString()
							.toUpperCase()) + "%" : null;
			healthCardNumber = params.containsKey("healthCardNumber") ? "%"
					+ StringUtils.trim(params.get("healthCardNumber")
							.toString()) + "%" : null;
			hospitalName = params.containsKey("hospitalName") ? "%"
					+ StringUtils.trim(params.get("hospitalName").toString()
							.toLowerCase()) + "%" : null;
			fromDate = params.containsKey("fromDate") ? (Date) params
					.get("fromDate") : null;
			toDate = params.containsKey("toDate") ? (Date) params.get("toDate")
					: null;
			claimNumber = params.containsKey("claimNumber") ? "%"
					+ StringUtils.trim(params.get("claimNumber").toString())
					+ "%" : null;
			
			List<Long> hospitalIdList = new ArrayList<Long>();

			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<OMPIntimation> criteriaQuery = builder
						.createQuery(OMPIntimation.class);

				Root<OMPIntimation> intimationRoot = criteriaQuery
						.from(OMPIntimation.class);
				Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				Join<OMPIntimation, Insured> insuredJoin = intimationRoot.join(
						"insured", JoinType.INNER);

				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (hospitalName != null) {
					Query query = entityManager
							.createNamedQuery("Hospitals.findByName");
					query = query.setParameter("name",
							hospitalName.toLowerCase());
					List<Hospitals> hospitalList = query.getResultList();
					if (hospitalList != null && !hospitalList.isEmpty()) {
						for (Hospitals hospital : hospitalList) {
							hospitalIdList.add(hospital.getKey());
						}
					}
				}
				if (hospitalName != null && hospitalIdList.isEmpty()) {
					resultList = null;
				} else {
					
					if(apageable != null){
						
						pageNumber = apageable.getPageNumber() ;
						
						if(pageNumber > 1){
							firstResult = (pageNumber-1) *10;
						}else{
							firstResult = 1;
						}
					}
					
					
					if (claimNumber == null) {
						if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									intimationRoot.<Status> get("status")
											.<String> get("processValue"),
									intimationStatus);
							predicates.add(statusPredicate);
						}

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									intimationRoot.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							Predicate intimationNoPredicate = builder
									.like(intimationRoot
											.<String> get("intimationId"),
											intimationNumber);
							predicates.add(intimationNoPredicate);
						}

						if (insuredName != null) {
							Predicate insuredNamePredicate = builder.like(
									builder.upper(intimationRoot.<Insured> get(
											"insured").<String> get(
											"insuredName")), insuredName);
							predicates.add(insuredNamePredicate);
						}

						if (healthCardNumber != null) {
							Predicate healthCardNumberPredicate = builder.like(
									intimationRoot.<Insured> get("insured")
											.<String> get("healthCardNumber"),
									healthCardNumber);
							predicates.add(healthCardNumberPredicate);
						}

						if (!hospitalIdList.isEmpty()) {
							Predicate hospitalPredicate = intimationRoot
									.<Long> get("hospital").in(hospitalIdList);
							predicates.add(hospitalPredicate);
						}

						if (fromDate != null && toDate != null) {
							Expression<Date> fromDateExpression = intimationRoot
									.<Date> get("createdDate");
							Predicate fromDatePredicate = builder
									.greaterThanOrEqualTo(fromDateExpression,
											fromDate);
							predicates.add(fromDatePredicate);

							Expression<Date> toDateExpression = intimationRoot
									.<Date> get("createdDate");
							Calendar c = Calendar.getInstance();
							c.setTime(toDate);
							c.add(Calendar.DATE, 1);
							toDate = c.getTime();
							Predicate toDatePredicate = builder
									.lessThanOrEqualTo(toDateExpression, toDate);
							predicates.add(toDatePredicate);
						}
						criteriaQuery.select(intimationRoot).where(
								builder.and(predicates
										.toArray(new Predicate[] {})));

						final TypedQuery<OMPIntimation> intimationquery = entityManager
								.createQuery(criteriaQuery);
						
						if(apageable != null){
							
								if(intimationquery.getResultList() != null && !intimationquery.getResultList().isEmpty() && intimationquery.getResultList().size() < 10){
									resultList = intimationquery.getResultList();
								}
								else if(intimationquery.getResultList() != null && intimationquery.getResultList().size() >= 10){
									resultList = intimationquery.setFirstResult(firstResult).setMaxResults(10).getResultList();
								}
						}
					
					if(apageable == null){
						resultList = intimationquery.getResultList();
						return resultList;
					}	
						
					} else if (claimNumber != null) {
						final CriteriaQuery<OMPClaim> claimCriteriaQuery = builder
								.createQuery(OMPClaim.class);

						Root<OMPClaim> claimRoot = claimCriteriaQuery
								.from(OMPClaim.class);
						Join<OMPClaim, OMPIntimation> claimJoin = claimRoot.join(
								"intimation", JoinType.INNER);

						Predicate claimNumberPredicate = builder.like(
								claimRoot.<String> get("claimId"), claimNumber);
						predicates.add(claimNumberPredicate);

						if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									claimRoot.<OMPIntimation> get("intimation")
											.<Status> get("status")
											.<String> get("processValue"),
									intimationStatus);
							predicates.add(statusPredicate);
						}

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									claimRoot.<OMPIntimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							Predicate intimationNoPredicate = builder.like(
									claimRoot.<OMPIntimation> get("intimation")
											.<String> get("intimationId"),
									intimationNumber);
							predicates.add(intimationNoPredicate);
						}

						if (insuredName != null) {
							Predicate insuredNamePredicate = builder.like(
									claimRoot.<OMPIntimation> get("intimation")
											.<Insured> get("insured")
											.<String> get("insuredName"),
									insuredName);
							predicates.add(insuredNamePredicate);
						}

						if (healthCardNumber != null) {
							Predicate healthCardNumberPredicate = builder.like(
									claimRoot.<OMPIntimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("healthCardNumber"),
									healthCardNumber);
							predicates.add(healthCardNumberPredicate);
						}

						if (fromDate != null && toDate != null) {
							Predicate createdDatePredicate = builder.between(
									claimRoot.<OMPIntimation> get("intimation")
											.<Date> get("createdDate"),
									fromDate, toDate);
							predicates.add(createdDatePredicate);
						}

						if (!hospitalIdList.isEmpty()) {
							Predicate hospitalPredicate = claimRoot
									.<OMPIntimation> get("intimation")
									.<Long> get("hospital").in(hospitalIdList);
							predicates.add(hospitalPredicate);
						}

						claimCriteriaQuery.select(claimRoot).where(
								builder.and(predicates
										.toArray(new Predicate[] {})));

						final TypedQuery<OMPClaim> claimQuery = entityManager
								.createQuery(claimCriteriaQuery);
						List<OMPClaim> claimList = new ArrayList<OMPClaim>();
						
						
						if(apageable ==null){
							
							claimList = (List<OMPClaim>) claimQuery
									.getResultList();
						}	
						
						else if (apageable != null){
							
							if(claimQuery.getResultList() != null && !claimQuery.getResultList().isEmpty() && claimQuery.getResultList().size() < 10){
									claimList = (List<OMPClaim>) claimQuery.getResultList();
							}
							else if(claimQuery.getResultList() != null && claimQuery.getResultList().size() >= 10){
								claimList = claimQuery.setFirstResult(firstResult).setMaxResults(10).getResultList();
							}
						}
						
						resultList = new ArrayList<OMPIntimation>();
						
						if (claimList != null && !claimList.isEmpty()) {
							for (OMPClaim claim : claimList) {
								resultList.add(claim.getIntimation());
							}
						}
					}
					return resultList;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return resultList;
	}
		

	private SelectValue getClaimType(Long hospitalTypeKey) {
		SelectValue claimType = new SelectValue();
		
		try{	
			MastersValue claimTypeMaser; 
		if( hospitalTypeKey != null && hospitalTypeKey == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			
			claimTypeMaser = entityManager.find(MastersValue.class, ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			
		}
		else{
			claimTypeMaser = entityManager.find(MastersValue.class,ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}
		
		if(claimTypeMaser != null){
			claimType.setId(claimTypeMaser.getKey());
			claimType.setValue(claimTypeMaser.getValue());
	
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return claimType;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public OMPIntimation searchIntimationByParams(Map<String, Object> params) {

		OMPIntimation intimationResult = new OMPIntimation();

		if (!params.isEmpty()) {
			String intimationNumber;
			String policyNumber;
			String cpuCode;

			intimationNumber = params.containsKey("intimationNumber") ? "%"
					+ StringUtils.trim(params.get("intimationNumber")
							.toString()) + "%" : null;
			policyNumber = params.containsKey("policyNumber") ? "%"
					+ StringUtils.trim(params.get("policyNumber").toString())
					+ "%" : null;
			cpuCode = params.containsKey("cpuCode") ? "%"
					+ StringUtils.trim(params.get("cpuCode").toString()) + "%"
					: null;

			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<OMPIntimation> criteriaQuery = builder
						.createQuery(OMPIntimation.class);

				Root<OMPIntimation> intimationRoot = criteriaQuery
						.from(OMPIntimation.class);
				Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (policyNumber != null) {
					Predicate policyPredicate = builder.like(intimationRoot
							.<Policy> get("policy")
							.<String> get("policyNumber"), policyNumber);
					predicates.add(policyPredicate);
				}

				if (intimationNumber != null) {
					Predicate intimationNoPredicate = builder.like(
							intimationRoot.<String> get("intimationId"),
							intimationNumber);
					predicates.add(intimationNoPredicate);
				}

				if (cpuCode != null) {
					Predicate insuredNamePredicate = builder.like(
							intimationRoot.<String> get("cpuCode"), cpuCode);
					predicates.add(insuredNamePredicate);
				}

				criteriaQuery.select(intimationRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<OMPIntimation> query = entityManager
						.createQuery(criteriaQuery);
				intimationResult = query.getSingleResult();

				if (intimationResult != null) {
					return intimationResult;
				} else {
					// No results Found Query result list empty
				}
			} catch (Exception E) {
				System.out.println("Exception while query Execution on DB");
				E.printStackTrace();

			} finally {

			}
		}

		else {
			// Param empty
			System.out.println("Params  empty");
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(Policy policy, OMPIntimation a_intimation) {

		if (a_intimation != null) {
			entityManager.persist(policy);
			a_intimation.setPolicy(policy);
			entityManager.persist(a_intimation);
		}
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRED) public void
	 * submit(IntimationDTO intimationDTO, OMPIntimation intimation) { //
	 * setMasterValueToPolicy(intimationDTO, intimation);
	 * intimation.setStatus(intimation.getHospitalType().getValue()
	 * .toLowerCase().contains("network") ? "Submitted" : "Pending"); if
	 * (intimationDTO != null) { if (intimation.getKey() != null) {
	 * entityManager.merge(intimation); } else {
	 * entityManager.persist(intimation);
	 * 
	 * } entityManager.flush();
	 * 
	 * intimationDTO.setKey(intimation.getKey() != null ? intimation
	 * .getKey().longValue() : null); } }
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitIntimation(NewIntimationDto newIntimationDto,
			OMPIntimation intimation) {

		Stage intiamtionStage = entityManager.find(Stage.class,
				ReferenceTable.INTIMATION_STAGE_KEY);

		intimation.setStage(intiamtionStage);
		Status intimationStatus = null;
		if (intimation.getHospitalType().getValue().toLowerCase()
				.contains("network")) {
			intimationStatus = entityManager.find(Status.class,
					ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
		} else {
			intimationStatus = entityManager.find(Status.class,
					ReferenceTable.INTIMATION_PENDING_STATUS_KEY);
		}
		intimation.setStatus(intimationStatus);

		MastersValue intimationSource = entityManager.find(MastersValue.class,
				ReferenceTable.CALL_CENTRE_SOURCE);
		intimation.setIntimationSource(intimationSource);
		
		intimation.setCreatedBy(SHAUtils.getUserNameForDB(newIntimationDto.getUsername()));

		if (intimation.getKey() != null) {
			intimation.setModifiedDate(new Date());
			entityManager.merge(intimation);
		} else {
			entityManager.persist(intimation);

		}
		entityManager.flush();

		newIntimationDto.setKey(intimation.getKey() != null ? intimation
				.getKey().longValue() : null);
	}

	/*
	 * public IntimationDTO read(Long key) { Long tmpKey = new Long(key);
	 * OMPIntimation intimation = entityManager.find(OMPIntimation.class, tmpKey);
	 * return new IntimationMapper().getIntimationDTO(intimation); }
	 */
	public NewIntimationDto readNewIntimation(Long key) {
		Long tmpKey = new Long(key);
		OMPIntimation intimation = entityManager.find(OMPIntimation.class, tmpKey);
		NewIntimationDto intimatinDto = OMPNewIntimationMapper.getInstance()
				.getNewIntimationDto(intimation);
		intimatinDto.setPolicy(intimation.getPolicy());
		return intimatinDto;
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRED) public void
	 * save(IntimationDTO intimationDTO, OMPIntimation intimation) { //
	 * setMasterValueToPolicy(intimationDTO, intimation);
	 * intimation.setStatus("Draft"); if (intimationDTO != null) { if
	 * (intimation.getKey() != null) { entityManager.merge(intimation); } else {
	 * entityManager.persist(intimation);
	 * 
	 * } entityManager.flush();
	 * 
	 * intimationDTO.setKey(intimation.getKey() != null ? intimation
	 * .getKey().longValue() : null); } }
	 */

	public void saveIntimation(NewIntimationDto newIntimationDto,
			OMPIntimation intimation) {
		Status intimationStatus = entityManager.find(Status.class,
				ReferenceTable.INTIMATION_SAVE_STATUS_KEY);
		intimation.setStatus(intimationStatus);

		Stage intiamtionStage = entityManager.find(Stage.class,
				ReferenceTable.INTIMATION_STAGE_KEY);
		intimation.setStage(intiamtionStage);

		MastersValue intimationSource = entityManager.find(MastersValue.class,
				131l);
		intimation.setIntimationSource(intimationSource);

		if (newIntimationDto != null) {
			if (intimation.getKey() != null) {
				intimation.setModifiedDate(new Date());
				entityManager.merge(intimation);
			} else {
				entityManager.persist(intimation);
			}

			entityManager.flush();

			newIntimationDto.setKey(intimation.getKey() != null ? intimation
					.getKey().longValue() : null);
		}
	}

	// private void setMasterValueToPolicy(IntimationDTO intimationDTO,
	// OMPIntimation intimation) {
	// Policy policy = intimation.getPolicy();
	// policy.setGenderId(getKeyByValue(intimationDTO.getIntimationDetails()
	// .getInsuredPatientId().getInsuredGender()));
	// policy.setRelationshipWithInsuredId(getKeyByValue((intimationDTO
	// .getIntimationDetails().getInsuredPatientId().getPraiCode02())));
	// policy.setPolicyType(getKeyByValue((intimationDTO.getTmpPolicy()
	// .getPolType())));
	// policy.setProductType(getKeyByValue((intimationDTO.getTmpPolicy()
	// .getProductType())));
	// policy.setInsuredFirstName(intimationDTO.getIntimationDetails()
	// .getInsuredPatientId().getInsuredName());
	// policy.setInsuredMiddleName(intimationDTO.getIntimationDetails()
	// .getInsuredPatientId().getInsuredName());
	// policy.setInsuredLastName(intimationDTO.getIntimationDetails()
	// .getInsuredPatientId().getInsuredName());
	// }

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getKeyByValue(String a_value) {
		MastersValue masterValue = new MastersValue();
		String searchValue = null;

		if (a_value != null) {
			if (a_value.equals("M") || a_value.equals("m"))
				searchValue = "Male";
			else if (a_value.equals("F") || a_value.equals("f"))
				searchValue = "Female";
			else
				searchValue = a_value;
		}
		Query query = entityManager
				.createNamedQuery("MastersValue.findByValue");
		query = query.setParameter("parentKey", searchValue);
		List<MastersValue> mastersValueList = query.getResultList();

		for (MastersValue a_mastersValue : mastersValueList)
			masterValue = a_mastersValue;

		return masterValue;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(OMPIntimation a_intimation) {

		if (a_intimation != null) {
			
			if(null == a_intimation.getKey()){
				entityManager.persist(a_intimation);
				entityManager.flush();
			}
			else{
				entityManager.merge(a_intimation);
				entityManager.flush();
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createNewBabyIntimation(List<NewBabyIntimation> newbaby,
			List<NewBabyIntimation> deletedBabyList) {
		List<NewBabyIntimation> OldBabyList = new ArrayList<NewBabyIntimation>();
		if (newbaby != null && !newbaby.isEmpty()) {
			Query q = entityManager
					.createNamedQuery("NewBabyIntimation.findByIntimation");
			if (newbaby.get(0) != null
					&& newbaby.get(0).getIntimation() != null
					&& newbaby.get(0).getIntimation().getKey() != null) {
				q.setParameter("intimationKey", newbaby.get(0).getIntimation()
						.getKey());
				OldBabyList = q.getResultList();

				if (OldBabyList != null && !OldBabyList.isEmpty()
						&& deletedBabyList != null
						&& !deletedBabyList.isEmpty()) {
					for (NewBabyIntimation deletedBaby : deletedBabyList) {
						if (OldBabyList.contains(deletedBaby)) {

							deletedBaby = entityManager.find(
									NewBabyIntimation.class,
									deletedBaby.getKey());
							deletedBaby.setActiveStatus(false);
							deletedBaby.setModifiedDate(new Date());
							entityManager.merge(deletedBaby);
							entityManager.flush();
							entityManager.refresh(deletedBaby);
						}
					}
				}

				for (NewBabyIntimation newBabyIntimation : newbaby) {
					newBabyIntimation.setActiveStatus(true);
					if (newBabyIntimation != null) {
						if (newBabyIntimation.getKey() == null) {
							entityManager.persist(newBabyIntimation);
						} else {
							newBabyIntimation = entityManager.find(
									NewBabyIntimation.class,
									newBabyIntimation.getKey());
							newBabyIntimation.setActiveStatus(true);
							newBabyIntimation.setModifiedDate(new Date());
							entityManager.merge(newBabyIntimation);
						}
					}
					entityManager.flush();
				}
			}
		}
	}

	public OMPIntimation searchByKey(Long a_key) {
		OMPIntimation find = entityManager.find(OMPIntimation.class, a_key);
		entityManager.refresh(find);
		
		
		
		return find;
	}

	public List<NewBabyIntimation> getListOfNewBabyByIntimation(
			Long intimationKey) {
		Query findByKey = entityManager.createNamedQuery(
				"NewBabyIntimation.findByIntimation").setParameter(
				"intimationKey", intimationKey);

		List<NewBabyIntimation> actualNewBornBabyList = (List<NewBabyIntimation>) findByKey
				.getResultList();

		List<NewBabyIntimation> finalNewBornBabyList = new ArrayList<NewBabyIntimation>();

		if (actualNewBornBabyList != null && !actualNewBornBabyList.isEmpty()) {
			for (NewBabyIntimation newBaby : actualNewBornBabyList) {
				if (newBaby.getActiveStatus() != null
						&& newBaby.getActiveStatus()) {
					finalNewBornBabyList.add(newBaby);
				}
			}
		}

		return finalNewBornBabyList;
	}

	public OMPClaim getClaimforIntimation(Long intimationKey) {
		OMPClaim a_claim = null;
		if (intimationKey != null) {
			
			Query findByIntimationKey = entityManager.createNamedQuery(
					"OMPClaim.findByOMPIntimationKey").setParameter("intimationKey",
					intimationKey);
			findByIntimationKey.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);

			try {
				List<OMPClaim> claimList = (List<OMPClaim>) findByIntimationKey.getResultList();
				if(claimList != null && !claimList.isEmpty()){
					a_claim = claimList.get(0);
					entityManager.refresh(a_claim);	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// intimationKey null
		}

		return a_claim;
	}

	@SuppressWarnings("unchecked")
	public List<OMPIntimation> getClaimforIntimationWithClaimNo(
			Long intimationKey, String claimNumber) {
		List<OMPClaim> a_claimList = new ArrayList<OMPClaim>();
		List<OMPIntimation> a_intimationList;
		if (intimationKey != null) {
			Query findByIntimationKeyAndClaimNumber = entityManager
					.createNamedQuery("OMPClaim.findByOMPIntimationKey")
					.setParameter("intimationKey", intimationKey);
			findByIntimationKeyAndClaimNumber.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
			try {
				a_claimList = (List<OMPClaim>) findByIntimationKeyAndClaimNumber
						.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Query findByClaimNumber = entityManager
					.createNamedQuery("OMPClaim.findByClaimNumber");
			findByClaimNumber.setParameter("claimNumber", claimNumber);
			try {
				a_claimList = (List<OMPClaim>) findByClaimNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// intimationKey null
		}
		a_intimationList = new ArrayList<OMPIntimation>();
		if (a_claimList != null && !a_claimList.isEmpty()) {
			for (OMPClaim claim : a_claimList) {
				a_intimationList.add((OMPIntimation) entityManager.find(
						OMPIntimation.class, claim.getIntimation().getKey()));
			}
		}
		if (!a_intimationList.isEmpty()) {
			return a_intimationList;
		}

		return null;
	}

	public List<OMPIntimation> findDuplicateInitmation(OMPIntimation a_intimation) {

		Query query = entityManager
				.createNamedQuery("OMPIntimation.findDuplicateInitmation");
		query = query.setParameter("hospital", a_intimation.getHospital());
		query = query.setParameter("policy", a_intimation.getPolicy()
				.getPolicyNumber());
		query = query.setParameter("admissionDate",
				a_intimation.getAdmissionDate());
		query = query.setParameter("insuredId", a_intimation.getInsured()
				.getInsuredId());
		List<OMPIntimation> insuredList = query.getResultList();

		return insuredList;
	}

	@SuppressWarnings("unchecked")
	public OMPIntimation searchbyIntimationNo(String intimationNo) {
		OMPIntimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"OMPIntimation.findByOMPIntimationNo").setParameter(
					"intimationNo", intimationNo);
			try {
				findByIntimNo.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
				List<OMPIntimation> intimationQueryResultList = (List<OMPIntimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (OMPIntimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public ViewTmpIntimation searchbyIntimationNoFromViewIntimation(String intimationNo) {
		ViewTmpIntimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"ViewTmpIntimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<ViewTmpIntimation> intimationQueryResultList = (List<ViewTmpIntimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (ViewTmpIntimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public  OMPIntimation getNotRegisteredIntimation(String intimationNo){
		 OMPIntimation intimation = searchbyIntimationNo(intimationNo);
		 
		 try{
			 if(intimation != null){
					Query claimByIntimationQuery = entityManager.createNamedQuery(
							"OMPClaim.findByOMPIntimationKey").setParameter(
									"intimationKey", intimation.getKey());
					claimByIntimationQuery.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
					List<OMPClaim> claimList = claimByIntimationQuery.getResultList();
					
					if(claimList != null && !claimList.isEmpty()){
						intimation=null;
					}				
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			
			return intimation;
		 
	}
	
	public  OMPIntimation getNewNotRegisteredIntimation(String intimationNo){
		 OMPIntimation intimation = searchbyIntimationNo(intimationNo);
		/* try{
			 if(intimation != null){
					Query claimByIntimationQuery = entityManager.createNamedQuery(
							"OMPClaim.findByOMPIntimationKey").setParameter(
									"intimationKey", intimation.getKey());
					claimByIntimationQuery.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
					List<OMPClaim> claimList = claimByIntimationQuery.getResultList();
					if(claimList != null && !claimList.isEmpty()){
						intimation=null;
					}				
					}

				} catch (Exception e) {
					e.printStackTrace();
				}*/
			
			return intimation;
		 
	}

	public OMPIntimation searchbyIntimationKey(Long intimationKey) {
		OMPIntimation intimation = null;

		Query findByIntimNo = entityManager.createNamedQuery(
				"OMPIntimation.findByKey").setParameter("intiationKey",
				intimationKey);
		try {
			intimation = (OMPIntimation) findByIntimNo.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return intimation;
	}

	@SuppressWarnings("unchecked")
	public TmpCPUCode getTmpCpuCode(Long cpuKey) {
		return entityManager.find(TmpCPUCode.class, cpuKey);
	}

	public Page<OMPSearchClaimRegistrationTableDTO> getOmpIntimationToRegister(
			OMPSearchClaimRegistrationFormDto regQf) {

		List<OMPSearchClaimRegistrationTableDTO> resultList = new ArrayList<OMPSearchClaimRegistrationTableDTO>();
		
//		ClaimRegTask registrationTask = BPMClientContext.getManualRegisterClaimTask(regQf.getUserId(),regQf.getPassword());
		
//		ClaimRegROTask manualRegisterClaimTaskForRO = BPMClientContext.getManualRegisterClaimTaskForRO(regQf.getUserId(), regQf.getPassword());
		
		List<Map<String, Object>> taskProcedure = null ;
		
		String strIntimationNo = "";
		String strPolicyNo = "";
		String streventCode = regQf.getType() != null ? regQf.getType().getValue() != null ? regQf.getType().getValue() : null : null;
		String strProductCode = regQf.getProductCode() != null ? regQf.getProductCode().getValue() != null ? regQf.getProductCode().getValue() : null : null;
		Date strIntimationDate = regQf.getIntimationDate();
		Date strLossDate = regQf.getLossnDate();
//		String priority = regQf.getPriority() != null ? regQf.getPriority().getValue() != null ? regQf.getPriority().getValue() : null : null;
//		String source = regQf.getSource() != null ? regQf.getSource().getValue() != null ? regQf.getSource().getValue(): null : null;
//		String type = regQf.getType() != null ? regQf.getType().getValue() != null ? regQf.getType().getValue(): null : null;
				
//		GetHumanTasks instance = new GetHumanTasks();

//		RegistrationQF qf = null;
		
		int pageNumber = regQf.getPageable().getPageNumber();
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		List<String> keys = new ArrayList<String>(); 
		
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.MANUAL_REGISTRATION_CURRENT_QUEUE);
		
		mapValues.put(SHAConstants.USER_ID, regQf.getUserId());
		
		
		
		Pageable apage = regQf.getPageable();
//		if(regQf.getHumanTask() != null){
//			payloadBO = regQf.getHumanTask().getPayloadCashless();	
//		}
//		else{
//			payloadBO = new PayloadBOType(); 
//		}
		
//		if (regQf != null && (regQf.getHospitalType() != null || regQf.getIntimatedDate() != null || 
//				regQf.getIntimationNumber() != null)){
//			
//			if(payloadBO == null){
//				payloadBO = new PayloadBOType();
//			}
						
//			PolicyType policyInfo = new PolicyType();
		
			if(regQf.getPolicyNo() != null){
//				policyInfo.setPolicyId(regQf.getPolicyNumber());
//				payloadBO.setPolicy(policyInfo);
				
				strPolicyNo= regQf.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
				
			}			

			
			if(regQf.getIntimationNo() != null){
				
//				intimationType.setIntimationNumber(regQf.getIntimationNumber());
//				payloadBO.setIntimation(intimationType);
				
				strIntimationNo = regQf.getIntimationNo();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				
			}

			if(strIntimationDate != null){
				
//				String intimDate = SHAUtils.formatIntimationDateValue(regQf.getIntimationDate());
//				intimationType.setIntDate(new Date(intimDate));
//				payloadBO.setIntimation(intimationType);
			 mapValues.put(SHAConstants.INTIMATION_DATE, strIntimationDate);
			}
			
			if(strLossDate != null){
//				String lossDate = SHAUtils.formatIntimationDateValue(regQf.getLossnDate());
//				intimationType.setIntDate(new Date(lossDate));
//				payloadBO.setIntimation(intimationType);
				mapValues.put(SHAConstants.LOSS_DATE, strLossDate);
			}
			
			
			if(strProductCode != null){
//				productType.setProductName(productName);
			
				
				mapValues.put(SHAConstants.PRODUCT_CODE,strProductCode);
			}
			
			
			if(streventCode != null){
			
				mapValues.put(SHAConstants.EVENT_CODE,streventCode);
			}
			
//	if(strIntimationDate != null){
//				
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				String intimDate = SHAUtils.formatIntimationDateValue(strIntimationDate);
//			    intimationType.setStatus(intimDate);
//			    
//			    payloadBO.setIntimation(intimationType);
//			}
//			HospitalInfoType hospitalInfo = new HospitalInfoType();
//			if(regQf.getHospitalType() != null){				
////				hospitalInfo.setHospitalType(regQf.getHospitalType().getValue().toUpperCase());
////				payloadBO.setHospitalInfo(hospitalInfo);
//				
//				mapValues.put(SHAConstants.HOSPITAL_TYPE, regQf.getHospitalType().getValue());
//			}			

		
		
//		ClassificationType classification = null;
//		
//	    if(streventCode != null && ! streventCode.isEmpty() || strProductCode != null && ! strProductCode.isEmpty())
//					{
//				
//				if(streventCode != null && ! streventCode.isEmpty())
//					if(! streventCode.equalsIgnoreCase(masterService.getOMPEventCodes())){
//						mapValues.put(SHAConstants.PRIORITY, priority);
//					}
//				if(source != null && ! source.isEmpty()){
//					
//					mapValues.put(SHAConstants.STAGE_SOURCE, source);
//				}
//				
//				if(type != null && ! type.isEmpty()){
//					if(! type.equalsIgnoreCase(SHAConstants.ALL)){
//						mapValues.put(SHAConstants.RECORD_TYPE, type);
//					}
//				}
//				
//		}
		
//	    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
//					|| type != null && ! type.isEmpty()){
//				classification = new ClassificationType();
//				
//				if(priority != null && ! priority.isEmpty())
//					if(priority.equalsIgnoreCase(SHAConstants.ALL)){
//						priority = null;
//					}
//				classification.setPriority(priority);
//				if(source != null && ! source.isEmpty()){
//					classification.setSource(source);
//				}
//				
//				if(type != null && ! type.isEmpty()){
//					if(type.equalsIgnoreCase(SHAConstants.ALL)){
//						type = null;
//					}
//					classification.setType(type);
//				}
//				
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				
//				 payloadBO.setClassification(classification);
//		}

//		if (regQf != null
//				&& (regQf.getHospitalType() != null
//						|| regQf.getIntimatedDate() != null || regQf
//						.getIntimationNumber() != null)) {
//			qf = new RegistrationQF();
//			qf.setHospitalType(regQf.getHospitalType() != null ? regQf
//					.getHospitalType().getValue().toUpperCase() : null);
//			qf.setIntDate(Utilities
//					.changeDatetoString(regQf.getIntimatedDate() != null ? regQf
//							.getIntimatedDate() : null));
//			qf.setIntimationNumber(regQf.getIntimationNumber() != null ? regQf
//					.getIntimationNumber() : null);
//			qf.setPolicyId(regQf.getPolicyNumber() != null ? regQf
//					.getPolicyNumber() : null);
//
//		}

		// if(regQf != null){
		// qf = new RegistrationQF();
		// qf.setHospitalType(regQf.getHospitalType() != null ?
		// regQf.getHospitalType().getValue():null);
		// qf.setIntDate(regQf.getIntimatedDate() != null ?
		// regQf.getIntimatedDate().toString(): null);
		// qf.setIntimationNumber(regQf.getIntimationNumber() != null ?
		// regQf.getIntimationNumber():null);
		// // apage.setPageSize(100);
		// }
		
		Pageable pageable = regQf.getPageable();
		
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		
		pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer.valueOf(BPMClientContext.PAGE_SIZE) : 10);
		pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer.valueOf(BPMClientContext.PAGE_NUMBER) : 1);
		
		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
//		Map<Long, Object> hospObjMap = new WeakHashMap<Long, Object>();
		Object[] setMapValues = SHAUtils.setOMPObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		dbCalculationService.callOMPUnlockProcedure(236599l);													//Testing purpose remove surly
		taskProcedure = dbCalculationService.getOMPTaskProcedure(setMapValues);	

//		PagedTaskList registrationTaskList = registrationTask.getTasks(regQf.getUserId(), pageable, payloadBO);
		
	
//		List<HumanTask> taskList = registrationTaskList.getHumanTasks();
		
		pageable.setPageSize(0);
		
//		if(taskList != null && taskList.size() <= Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE)){
//			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) - taskList.size() : 10);
//		}
//		pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer.valueOf(BPMClientContext.PAGE_SIZE) : 10);
//		pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer.valueOf(BPMClientContext.PAGE_NUMBER) : 1);
//		//PagedTaskList tasks = manualRegisterClaimTaskForRO.getTasks(regQf.getUserId(), null, payloadBO);
//		PagedTaskList tasks = manualRegisterClaimTaskForRO.getTasks(regQf.getUserId(), pageable, payloadBO);
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
		
//		if(humanTasks != null){
//			taskList.addAll(humanTasks);
//		}
//		List<HumanTask> taskList = instance.getRegistrationTask(
//				regQf.getUserId(), regQf.getPassword(), qf, null);

//		if (taskList != null && !taskList.isEmpty()) {
//			for (HumanTask item : taskList) {
		
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
					String intimationValue = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
					keys.add(intimationValue);
					Long keyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
			
					workFlowMap.put(keyValue,outPutArray);
					 totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
	
				}

			}	
			
			for (String intimationNumber : keys) {
				
				OMPSearchClaimRegistrationTableDTO resultBean = new OMPSearchClaimRegistrationTableDTO();
//				resultBean.setHumanTask(item);
//				String intimationNumberFromBPMTask = getIntimationNo(item);
//				String intimationNumberFromBPMTask = "CLI/2015/120000/0002166";
				
				
				if (null != intimationNumber && !("").equalsIgnoreCase(intimationNumber)) {
					
					resultBean.setIntimationNo(intimationNumber);
					OMPIntimation intimation = getNotRegisteredIntimation(resultBean.getIntimationNo());
					if (intimation != null) {
						resultBean.setDbOutArray(workFlowMap.get(intimation.getKey()));
									
						NewIntimationDto intimationToRegister = getIntimationDto(intimation);
						if (null != intimationToRegister) {
							resultBean
									.setNewIntimationDto(intimationToRegister);
							if(intimationToRegister.getHospitalType() != null){
							resultBean.setHospitalType(intimationToRegister
									.getHospitalType().getValue());
							}
							if(intimationToRegister.getHospitalDto() != null){
								resultBean.setHospitalCity(intimation.getCityName());
								resultBean.setHospitalName(intimationToRegister.getHospitalName());
							}
							resultBean.setHospitalCity(intimation.getCityName());
							resultBean.setPolicyno(intimationToRegister.getPolicy() != null && intimationToRegister.getPolicy().getPolicyNumber() != null ? intimationToRegister.getPolicy().getPolicyNumber() : "");
							
							if(intimationToRegister.getPolicy().getProduct()!= null  && intimationToRegister.getPolicy().getProduct().getKey()!= null){
								
								resultBean.setProductcode(intimationToRegister.getPolicy().getProduct().getCode()!= null && intimationToRegister.getPolicy().getProduct().getValue() != null ? (intimationToRegister.getPolicy().getProduct().getValue() + intimationToRegister.getPolicy().getProduct().getCode()): "");
								resultBean.setProductName(intimationToRegister.getPolicy().getProduct().getValue());
							}
								if(intimationToRegister.getDollarInitProvisionAmt()== null || intimationToRegister.getDollarInitProvisionAmt().equals("")){
									resultBean.setInitlProvisionAmt(0d);
								}else{
							resultBean.setInitlProvisionAmt(intimationToRegister.getDollarInitProvisionAmt());
								}
								if(intimationToRegister.getInrConversionRate()== null || intimationToRegister.getInrConversionRate().equals("")){
									resultBean.setInrConversionRate(0d);
								}else{
							resultBean.setInrConversionRate(intimationToRegister.getInrConversionRate());
								}
								if(intimationToRegister.getInrTotalAmount()== null || intimationToRegister.getInrTotalAmount().equals("")){
									resultBean.setInrValue(0d);
								}else{
							resultBean.setInrValue(intimationToRegister.getInrTotalAmount());
								}
								
								if(intimationToRegister.getDollarInitProvisionAmt() != null && intimationToRegister.getInrConversionRate() != null){
									Double amt= (intimationToRegister.getDollarInitProvisionAmt() * intimationToRegister.getInrConversionRate());
									resultBean.setInrValue(amt );
								}else{
									resultBean.setInrValue(0d);
								}
								resultBean.setCmbclaimType(intimationToRegister.getClaimType());
							resultBean.setLoss(intimationToRegister.getAilmentLoss());
							resultBean.setLossdate(intimationToRegister.getLossDateTime());
							MastersEvents eventMaseter = null;
//							if(intimationToRegister.getEvent() != null && intimationToRegister.getEvent().getKey() != null){
//								resultBean.setType(intimationToRegister.getEvent().getEventType() != null && intimationToRegister.getEvent().getDescription() != null ? (intimationToRegister.getEvent().getEventType() + intimationToRegister.getEvent().getDescription()): "");
//							}
							
							if(intimationToRegister.getEventCodeValue() != null ){
//								SelectValue event;
//								event = masterService.getOMPEventCodes();
//								resultBean.setType(event);
								//resultBean.setEventCode(intimationToRegister.getEventCodeValue());
								resultBean.setType(intimationToRegister.getEventCodeValue());
//								resultBean.setType(intimationToRegister.getEvent().getEventCode() != null && intimationToRegister.getEvent().getDescription() != null ? (intimationToRegister.getEvent().getEventCode() + intimationToRegister.getEvent().getDescription()): "");
							}
							intimationToRegister.setHospitalName(intimation.getHospitalName());
							intimationToRegister.setHospitalCity(intimation.getCityName());
							intimationToRegister.setHospitalizationFlag(intimation.getHospitalizationFlag());
							intimationToRegister.setNonHospitalizationFlag(intimation.getNonHospitalizationFlag());
							/*Hospitals hospital = null;
										//hospital = getHospitalDetailsByKey(intimation.getHospital());
									if (hospital!=null && hospital.getName() != null && hospital.getHospitalType() != null ){
										intimationToRegister.setHospitalName(intimation.getHospitalName());
										intimationToRegister.setHospitalCity(hospital.getCity());
										resultBean.setHospitalName(intimationToRegister.getHospitalName());
										resultBean.setHospitalCity(intimationToRegister.getHospitalCity());
										resultBean.setHospitalisationFlag(intimationToRegister.getHospitalizationFlag());
										resultBean.setNonHospitalisationFlag(intimationToRegister.getNonHospitalizationFlag());
										SelectValue country = new SelectValue();
												country = masterService.getCountryValueByValue(hospital.getCountry());
//										country.setValue(hospital.getCountry());
										resultBean.setHospitalCountry(country);
										intimationToRegister.setCountryId(country.getId());
//										resultBean.setCountryId(intimationToRegister.getCountryId());
								}
//							}
*///						}
							resultBean.setIntimationDate(intimationToRegister
									.getCreatedDate());
							resultBean.setAdmissiondate(intimationToRegister
									.getAdmissionDate());
							
							resultBean.setUserId(regQf.getUserId());
							resultBean.setPassword(regQf.getPassword());
							Object workflowKey = workFlowMap.get(intimation.getKey());
							resultBean.setDbOutArray(workflowKey);
//							resultBean.setTaskNumber(item.getNumber());
							resultList.add(resultBean);
							if(resultList.size() == 10) {
								break;
							}
						}
					}

				}
				
			}
//			} call hospital details
//			intimation = getHospitalDetailsByKey();

		Page<OMPSearchClaimRegistrationTableDTO> page = new Page<OMPSearchClaimRegistrationTableDTO>();
//		page.setHasNext(true);
		page.setPageItems(resultList);
//		page.setPageNumber(registrationTaskList.getCurrentPage());
		page.setTotalRecords(totalRecords);


		return page;
	}

	@SuppressWarnings("unchecked")
	public NewIntimationDto getIntimationDto(OMPIntimation intimation) {
		NewIntimationDto intimationToRegister = null;	
		if (null != intimation) {
			OMPNewIntimationMapper newIntimationMapper = OMPNewIntimationMapper.getInstance();
			intimationToRegister = newIntimationMapper
					.getNewIntimationDto(intimation);
			intimationToRegister.setIntimationSource(intimation
					.getIntimationSource());
			if (null != intimation.getPolicy()) {
				if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){
					List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());
					if(null != insuredList && !insuredList.isEmpty()){
						intimation.getPolicy().setInsured(insuredList);
					}
				}
				intimationToRegister.setAdmissionDate(intimation.getAdmissionDate());
//				intimationToRegister.setAdmissionDateForCarousel(intimation.getAdmissionDate().toString());
				if(intimation.getAdmissionDate()!=null){
					intimationToRegister.setAdmissionDateForCarousel(intimation.getAdmissionDate().toString());
				}

				
				String address = (intimation.getPolicy().getProposerAddress1() != null ? intimation.getPolicy().getProposerAddress1() : "") + 
						(intimation.getPolicy().getProposerAddress2() != null ? intimation.getPolicy().getProposerAddress2() : "") + 
						(intimation.getPolicy().getProposerAddress3() != null ? intimation.getPolicy().getProposerAddress3() : "" );

				intimation.getPolicy().setProposerAddress(address);				
				
				intimationToRegister.setPolicy(intimation.getPolicy());	
				
				if(intimation.getPolicy() != null && intimation.getPolicy().getPolicyNumber() != null){
				    Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
				    intimationToRegister.setPolicy(policy);
				}
				
				intimationToRegister
						.setOrganizationUnit(getOrganizationUnit(intimation
								.getPolicy().getHomeOfficeCode()));
				if (null != intimation.getPolicy().getLobId()) {
					MastersValue lineofBusiness = entityManager.find(
							MastersValue.class, intimation.getPolicy()
									.getLobId());
					intimationToRegister.setLineofBusiness(lineofBusiness
							.getValue() != null ? lineofBusiness.getValue()
							: "");
				}
			}

			if (null != intimation.getInsured()) {
				intimationToRegister.setInsuredKey(intimation.getInsured().getKey());
				intimationToRegister.setInsuredPatient(intimation.getInsured());
				intimationToRegister.setInsuredAge(intimationToRegister.getInsuredCalculatedAge());
				intimationToRegister.setPlaceVisit(intimation.getInsured().getPlaceOfvisit());
				intimationToRegister.setPlan(intimation.getInsured().getPlan());
				intimationToRegister.setPassportExpiryDate(intimation.getInsured().getPassPortExpiryDate() );
				intimationToRegister.setInsuredPatientName(intimation.getInsured().getInsuredName());
				intimationToRegister.setLossDateTime(intimation.getLossDateTime());
				intimationToRegister.setSponsorName(intimation.getSponsorName());
				intimationToRegister.setPassportNumber(intimation.getInsured().getPassportNo());
				intimationToRegister.setPlaceLossDelay(intimation.getPlaceLossDelay());
				intimationToRegister.setTpaIntimationNumber(intimation.getTpaIntimationNumber());
				intimationToRegister.setIntimationDate(intimation.getIntimationDate());
				intimationToRegister.setInsuredPatient(intimation.getInsured());
				if(intimation.getIntimatedBy() != null){
					SelectValue intimatedby = new SelectValue();
					intimatedby.setId(intimation.getIntimatedBy().getKey());
					intimatedby.setValue(intimation.getIntimatedBy().getValue());
//					intimationToRegister.setModeOfIntimation(intimatedby);
					intimationToRegister.setIntimatedBy(intimatedby);
					if(intimation.getIntimationMode() != null){
						SelectValue modeofintimation = new SelectValue();
						modeofintimation.setId(intimation.getIntimationMode().getKey());
						modeofintimation.setValue(intimation.getIntimationMode().getValue());
						intimationToRegister.setModeOfIntimation(modeofintimation);
					}
					
			}
				
				
				
			}
		
			Hospitals registeredHospital = null;
			TmpHospital tempHospital = null;

			/*if (intimation.getHospital() != null
					&& intimation.getHospitalType() != null
					&& StringUtils.contains(intimation.getHospitalType()
							.getValue().toLowerCase(), "network")) {
				registeredHospital = entityManager.find(Hospitals.class,
						intimation.getHospital());
				if (null != registeredHospital) {
					HospitalDto hospitaldto = new HospitalDto(
							registeredHospital);
					hospitaldto.setRegistedHospitals(registeredHospital);

					intimationToRegister.setHospitalDto(hospitaldto);
					if(registeredHospital.getAddress() != null){
						String[] hospAddress = StringUtil.split(registeredHospital.getAddress(),',');
						int length;
						if(hospAddress != null && hospAddress.length != 0 ){
							length=hospAddress.length;			
							intimationToRegister.getHospitalDto().setHospAddr1(hospAddress[0]);
							if(length >2){
								intimationToRegister.getHospitalDto().setHospAddr2(hospAddress[1]);
							}
							if(length >3){
								intimationToRegister.getHospitalDto().setHospAddr3(hospAddress[2]);
							}
							if(length >4){
								intimationToRegister.getHospitalDto().setHospAddr4(hospAddress[3]);
							}

						}
					}
					
					
				}
			} else {
				if(intimation.getHospital() != null) {
					tempHospital = entityManager.find(TmpHospital.class,
							intimation.getHospital());
					HospitalDto hospitalDto = new HospitalDto(tempHospital,
							intimation.getHospitalType().getValue());
					hospitalDto.setNotRegisteredHospitals(tempHospital);
				}
				
			}*/
			

//			if (intimation.getHospitalType() != null){
//				SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
//				intimationToRegister.setClaimType(claimType);
//			}
			
			MastersValue claimType = intimation.getClaimType();
			SelectValue claimTypeMast = new SelectValue();
			if(claimType != null){
				/*System.out.println("ClType key "+claimType.getKey());
				System.out.println("ClType value "+claimType.getValue());*/
				claimTypeMast.setId(claimType.getKey());
				claimTypeMast.setValue(claimType.getValue());						
		}
			intimationToRegister.setClaimType(claimTypeMast);
			
			TmpCPUCode cpuObject = intimation.getCpuCode();

			if (null != cpuObject) {
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				intimationToRegister.setCpuAddress(cpuObject.getAddress());
			}
			
			String hospitalType = intimation.getHospitalType() != null ? intimation
					.getHospitalType().getValue().toString().toLowerCase()
					: null;
			
					
			if (hospitalType != null
					&& StringUtils.containsIgnoreCase(hospitalType, "network")) {
				if (registeredHospital != null) {

					if (null == cpuObject) {
						cpuObject = getCpuObjectByPincode(registeredHospital
								.getPincode());
					}
				}

			} else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
					.getValue(), "not-registered")) {
//				if (tempHospital.getPincode() != null) {
//					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//							.toString());
//				}
			}
			if(null == intimation.getCpuCode() && null != cpuObject )
			{
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				
			}
			intimationToRegister
					.setIntimaterName(intimation.getIntimaterName() != null ? intimation
							.getIntimaterName() : "");
			if (intimation.getAdmissionDate() != null) {
				intimationToRegister.setAdmissionDate(intimation
						.getAdmissionDate());
			}

			if (intimation.getCreatedDate() != null) {
				intimationToRegister.setDateOfIntimation(intimation
						.getCreatedDate().toString());
			}

//			if (intimation.getRoomCategory() != null) {
//				SelectValue roomCategory = new SelectValue();
//				roomCategory.setId(intimation.getRoomCategory().getKey());
//				roomCategory.setValue(intimation.getRoomCategory().getValue());
//				intimationToRegister.setRoomCategory(roomCategory);
//			}
			if (intimation.getPolicy() != null) {
				intimationToRegister.setAgentBrokerCode(intimation.getPolicy()
						.getAgentCode() != null ? intimation.getPolicy()
						.getAgentCode() : "");
				intimationToRegister.setAgentBrokerName(intimation.getPolicy()
						.getAgentName() != null ? intimation.getPolicy()
						.getAgentName() : "");
				if(intimation.getPolicy().getProduct()!=null){
					intimationToRegister.setProductName(intimation.getPolicy()
							.getProduct().getValue() != null ? intimation
							.getPolicy().getProduct().getValue() : "");
				}
				MastersEvents master = null;
				if(intimation.getEvent() != null && intimation.getEvent().getKey() != null){
//					intimationToRegister.setEvent(intimation.getEvent().getKey());
					//intimationToRegister.setEventName(intimation.getEvent().getEventType() != null && intimation.getEvent().getDescription() != null ? (intimation.getEvent().getEventType() + intimation.getEvent().getDescription()): "");
					SelectValue event = new SelectValue();
					event.setId(intimation.getEvent().getKey());
					event.setValue(intimation.getEvent().getEventType() + intimation.getEvent().getEventDescription());
					intimationToRegister.setEventCodeValue(event);
					intimationToRegister.setEventCode(intimation.getEvent().getEventCode());
//					intimationToRegister.setEventName(intimation.getEvent().getDescription() != null ? intimation.getEvent().getDescription() : "");
					
				}
				
			}
			if(intimation.getInrConversionRate()!= null){
				intimationToRegister.setInrConversionRate(intimation.getInrConversionRate());
			}
			if(intimation.getInrTotalAmount()!=null){
				intimationToRegister.setInrTotalAmount(intimation.getInrTotalAmount());
			}
			if(intimation.getDollarInitProvisionAmt()!= null){
				intimationToRegister.setDollarInitProvisionAmt(intimation.getDollarInitProvisionAmt());
			}
			intimationToRegister.setPlaceLossDelay(intimation.getPlaceLossDelay());
			intimationToRegister.setStatus(intimation.getStatus());
			intimationToRegister.setStage(intimation.getStage());
		
			DBCalculationService dbCalculationService = new DBCalculationService();
			if(intimation.getInsured() != null && intimation.getPolicy() != null){
				Double insuredSumInsured = dbCalculationService.getOMPInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}
		}
		
		return intimationToRegister;
	}

	
	
//	@SuppressWarnings("unchecked")
//	public NewIntimationDto getIntimationDtoFromViewIntimation(ViewTmpIntimation intimation) {
//		NewIntimationDto intimationToRegister = null;
//		if (null != intimation) {
//			NewIntimationMapper newIntimationMapper = new NewIntimationMapper();
//			intimationToRegister = newIntimationMapper
//					.getNewIntimationDto(intimation);
//			intimationToRegister.setIntimationSource(intimation
//					.getIntimationSource());
//			if (null != intimation.getPolicy()) {
//				if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){
//					List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());
//					if(null != insuredList && !insuredList.isEmpty()){
//						intimation.getPolicy().setInsured(insuredList);
//					}
//				}
//				
//				
//				String address = (intimation.getPolicy().getProposerAddress1() != null ? intimation.getPolicy().getProposerAddress1() : "") + 
//						(intimation.getPolicy().getProposerAddress2() != null ? intimation.getPolicy().getProposerAddress2() : "") + 
//						(intimation.getPolicy().getProposerAddress3() != null ? intimation.getPolicy().getProposerAddress3() : "" );
//
//				intimation.getPolicy().setProposerAddress(address);				
//				
//				intimationToRegister.setPolicy(intimation.getPolicy());	
//				
//				if(intimation.getPolicy() != null && intimation.getPolicy().getPolicyNumber() != null){
//				    Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
//				    intimationToRegister.setPolicy(policy);
//				}
//				
//				intimationToRegister
//						.setOrganizationUnit(getOrganizationUnit(intimation
//								.getPolicy().getHomeOfficeCode()));
//				if (null != intimation.getPolicy().getLobId()) {
//					MastersValue lineofBusiness = entityManager.find(
//							MastersValue.class, intimation.getPolicy()
//									.getLobId());
//					intimationToRegister.setLineofBusiness(lineofBusiness
//							.getValue() != null ? lineofBusiness.getValue()
//							: "");
//				}
//			}
//
//			if (null != intimation.getInsured()) {
//				intimationToRegister.setInsuredPatient(intimation.getInsured());
//			}
//		
//			Hospitals registeredHospital = null;
//			TmpHospital tempHospital = null;
//
//			if (intimation.getHospital() != null
//					&& intimation.getHospitalType() != null
//					&& StringUtils.contains(intimation.getHospitalType()
//							.getValue().toLowerCase(), "network")) {
//				registeredHospital = entityManager.find(Hospitals.class,
//						intimation.getHospital());
//				if (null != registeredHospital) {
//					HospitalDto hospitaldto = new HospitalDto(
//							registeredHospital);
//					hospitaldto.setRegistedHospitals(registeredHospital);
//					intimationToRegister.setHospitalDto(hospitaldto);
//					String[] hospAddress = StringUtil.split(registeredHospital.getAddress(),',');
//					int length;
//					if(hospAddress.length != 0 ){
//						length=hospAddress.length;			
//						intimationToRegister.getHospitalDto().setHospAddr1(hospAddress[0]);
//						if(length >2){
//							intimationToRegister.getHospitalDto().setHospAddr2(hospAddress[1]);
//						}
//						if(length >3){
//							intimationToRegister.getHospitalDto().setHospAddr3(hospAddress[2]);
//						}
//						if(length >4){
//							intimationToRegister.getHospitalDto().setHospAddr4(hospAddress[3]);
//						}
//					}
//					
//					
//				}
//			} else {
//				if(intimation.getHospital() != null) {
//					tempHospital = entityManager.find(TmpHospital.class,
//							intimation.getHospital());
//					HospitalDto hospitalDto = new HospitalDto(tempHospital,
//							intimation.getHospitalType().getValue());
//					hospitalDto.setNotRegisteredHospitals(tempHospital);
//				}
//				
//			}
//			
//
//			if (intimation.getHospitalType() != null){
//				SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
//				intimationToRegister.setClaimType(claimType);
//			}
//			
//			
//			TmpCPUCode cpuObject = intimation.getCpuCode();
//
//			if (null != cpuObject) {
//				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
//				intimationToRegister.setCpuId(cpuObject.getKey());
//				intimationToRegister.setCpuAddress(cpuObject.getAddress());
//			}
//			
//			String hospitalType = intimation.getHospitalType() != null ? intimation
//					.getHospitalType().getValue().toString().toLowerCase()
//					: null;
//			
//					
//			if (hospitalType != null
//					&& StringUtils.containsIgnoreCase(hospitalType, "network")) {
//				if (registeredHospital != null) {
//
//					if (null == cpuObject) {
//						cpuObject = getCpuObjectByPincode(registeredHospital
//								.getPincode());
//					}
//				}
//
//			} else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
//					.getValue(), "not-registered")) {
//				if (tempHospital.getPincode() != null) {
//					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//							.toString());
//				}
//			}
//			if(null == intimation.getCpuCode() && null != cpuObject )
//			{
//				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
//				intimationToRegister.setCpuId(cpuObject.getKey());
//				
//			}
//			intimationToRegister
//					.setIntimaterName(intimation.getIntimaterName() != null ? intimation
//							.getIntimaterName() : "");
//			if (intimation.getAdmissionDate() != null) {
//				intimationToRegister.setAdmissionDate(intimation
//						.getAdmissionDate());
//			}
//
//			if (intimation.getCreatedDate() != null) {
//				intimationToRegister.setDateOfIntimation(intimation
//						.getCreatedDate().toString());
//			}
//
//			if (intimation.getRoomCategory() != null) {
//				SelectValue roomCategory = new SelectValue();
//				roomCategory.setId(intimation.getRoomCategory().getKey());
//				roomCategory.setValue(intimation.getRoomCategory().getValue());
//				intimationToRegister.setRoomCategory(roomCategory);
//			}
//			if (intimation.getPolicy() != null) {
//				intimationToRegister.setAgentBrokerCode(intimation.getPolicy()
//						.getAgentCode() != null ? intimation.getPolicy()
//						.getAgentCode() : "");
//				intimationToRegister.setAgentBrokerName(intimation.getPolicy()
//						.getAgentName() != null ? intimation.getPolicy()
//						.getAgentName() : "");
//				if(intimation.getPolicy().getProduct()!=null){
//					intimationToRegister.setProductName(intimation.getPolicy()
//							.getProduct().getValue() != null ? intimation
//							.getPolicy().getProduct().getValue() : "");
//				}
//				
//			}
//			intimationToRegister.setStatus(intimation.getStatus());
//			intimationToRegister.setStage(intimation.getStage());
//		}
//		
//		DBCalculationService dbCalculationService = new DBCalculationService();
//		if(intimation.getInsured() != null && intimation.getPolicy() != null){
//		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey());
//		intimationToRegister.setOrginalSI(insuredSumInsured);
//		}
//		return intimationToRegister;
//	}
	
	private List<Insured> getInsuredListByPolicyNumber(String policyNumber){
		PolicyService policyService = new PolicyService();
		List<Insured> insuredList = null;
		if(null != policyNumber){
			insuredList = policyService.getInsuredList(policyNumber, entityManager);	
		}
		
		return insuredList;
	}

	/**
	 * The below overridden method is used, in case injecting of intimation
	 * service is not possible in source code. Without injecting, entity manager
	 * available in this class cannot be used. Therefore, in below method
	 * provision is provided where entity manager can be passed for the caller
	 * itself. If injection of intimation service is possible, then above method
	 * can be used.
	 * **/
	
	public NewIntimationDto getIntimationDto(OMPIntimation intimation,
			EntityManager entityManager) {
		this.entityManager = entityManager;
		return getIntimationDto(intimation);
	}
	
//	public NewIntimationDto getIntimationDto(ViewTmpIntimation intimation,
//			EntityManager entityManager) {
//		this.entityManager = entityManager;
//		return getIntimationDtoFromViewIntimation(intimation);
//	}

	public TmpCPUCode getCpuObjectByPincode(String pincode) {
		TmpCPUCode cpuObject = null;
		if (pincode != null) {
			cpuObject = entityManager.find(TmpCPUCode.class,
					Long.valueOf(StringUtils.trim(pincode)));
		}
		return cpuObject;
	}

//	private String getIntimationNo(HumanTask item) {
//
////		Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(
////				item.getPayload(), "RegIntDetails");
////		String keyValue = (String) valuesFromBPM.get("intimationNumber");
//		return item.getPayloadCashless().getIntimation().getIntimationNumber();
//	}

	/**
	 * Method to retreive viewIntimation DTO.
	 * */
//	public ViewIntimationDTO viewIntimatinDTO(OMPIntimation intimation, Hospitals hospital) {
//		if (intimation == null) {
//			return null;
//		}
//
//		OrganaizationUnit organization = getOrganizationUnit(intimation
//				.getPolicy().getHomeOfficeCode());
//
//		ViewIntimationDTO viewIntimationDTO = new ViewIntimationDTO();
//
//		viewIntimationDTO
//				.setKey((Long) (intimation.getKey() != null ? intimation
//						.getKey() : ""));
//		viewIntimationDTO
//				.setIntimationNo(intimation.getIntimationId() != null ? intimation
//						.getIntimationId() : "");
//		viewIntimationDTO.setDateOfIntimation(intimation.getCreatedDate()
//				.toString() != null ? intimation.getCreatedDate().toString()
//				: "");
//		viewIntimationDTO.setPolicyNumber(intimation.getPolicy()
//				.getPolicyNumber() != null ? intimation.getPolicy()
//				.getPolicyNumber() : "");
//		viewIntimationDTO
//				.setPolicyIssuingOffice(null != organization ? organization
//						.getOrganizationUnitName() : "");
//		viewIntimationDTO.setProductName(intimation.getPolicy()
//				.getProductName() != null ? intimation.getPolicy()
//				.getProductName() : "");
//		viewIntimationDTO
//				.setInsuredName(intimation.getIntimatedBy().getValue() != null ? intimation
//						.getIntimatedBy().getValue() : "");
//		viewIntimationDTO
//				.setPatientName(intimation.getInsuredPatientName() != null ? intimation
//						.getInsuredPatientName() : "");
//		viewIntimationDTO.setHospitalName(hospital.getName() != null ? hospital
//				.getName() : "");
//		viewIntimationDTO
//				.setCityOfHospital(hospital.getCity() != null ? hospital
//						.getCity() : "");
//		viewIntimationDTO
//				.setHospitalType(hospital.getHospitalType().getValue() != null ? hospital
//						.getHospitalType().getValue() : "");
//		viewIntimationDTO.setDateOfAdmission(intimation.getAdmissionDate()
//				.toString() != null ? intimation.getAdmissionDate().toString()
//				: "");
//		viewIntimationDTO
//				.setReasonForAdmission(intimation.getAdmissionReason() != null ? intimation
//						.getAdmissionReason() : "");
//		viewIntimationDTO.setCpuCode(intimation.getCpuCode().getCpuCode()
//				.toString() != null ? intimation.getCpuCode().getCpuCode()
//				.toString() : "");
//		viewIntimationDTO
//				.setSmCode(intimation.getPolicy().getSmCode() != null ? intimation
//						.getPolicy().getSmCode() : "");
//		viewIntimationDTO
//				.setSmName(intimation.getPolicy().getSmName() != null ? intimation
//						.getPolicy().getSmName() : "");
//		viewIntimationDTO.setAgentBrokerCode(intimation.getPolicy()
//				.getAgentCode() != null ? intimation.getPolicy().getAgentCode()
//				: "");
//		viewIntimationDTO.setAgentBrokerName(intimation.getPolicy()
//				.getAgentName() != null ? intimation.getPolicy().getAgentName()
//				: "");
//		viewIntimationDTO
//				.setHospitalCode(hospital.getHospitalCode() != null ? hospital
//						.getHospitalCode() : "");
//		viewIntimationDTO.setIdCardNo(intimation.getInsured()
//				.getHealthCardNumber() != null ? intimation.getInsured()
//				.getHealthCardNumber() : "");
//		return viewIntimationDTO;
//	}

	private OrganaizationUnit getOrganizationUnit(String strPolOfficeCode) {

		return getPolicyServiceInstance().getInsuredOfficeNameByDivisionCode(
				entityManager, strPolOfficeCode);
	}

	private PolicyService getPolicyServiceInstance() {
		if (null == this.policyService) {
			this.policyService = new PolicyService();
		}
		return this.policyService;
	}
	
	public Integer getFVRCount(Long intimationKey) {
		Query query = entityManager.createNamedQuery("FieldVisitRequest.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<FieldVisitRequest> singleResult = query.getResultList();
		if(!singleResult.isEmpty()) {
			return singleResult.size();
		}
		return 0;
	}
	
	public OMPIntimation getIntimationByHealthcardNo(String intimationId, String heathCardNo){
		Query query = entityManager.createNamedQuery("OMPIntimation.findByHealthCardNo");
		query.setParameter("intimationId", intimationId);
		query.setParameter("healthCardNo", heathCardNo);
		List<OMPIntimation> resultList = query.getResultList();
		if(!resultList.isEmpty()){
		return resultList.get(0);
		}
		return null;
	
	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		if(policyNumber != null){
			Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
			query.setParameter("policyNumber", policyNumber);
	
			List<Policy> resultList =  (List<Policy>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
				entityManager.refresh(resultList.get(0));
				return resultList.get(0);
			}
		}
		return null;
	}
	
	public List<ViewTmpIntimation> getIntimationByPolicyKey(Long policyKey){
		if(policyKey != null){
			Query query = entityManager.createNamedQuery("ViewTmpIntimation.findByPolicyKey");
			query.setParameter("policyKey", policyKey);
	
			List<ViewTmpIntimation> resultList =  (List<ViewTmpIntimation>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
//				entityManager.refresh(resultList);
				return resultList;
			}
		}
		return null;
	}
	
	
	
//	public OMPClaim doAutoRegistratrionProcessForPremia(EntityManager entityManager ,OMPIntimation objIntimation , StarFaxSimulatorService starFaxservice)	{
//		this.entityManager = entityManager;
//		return doAutoRegistrationProcess(objIntimation, starFaxservice);
//	}
	
//	public OMPClaim doAutoRegistrationProcess(OMPIntimation objIntimation , StarFaxSimulatorService starFaxservice){
//		//Hospitals objHosp = starFaxservice.getHospitalObject(objIntimation.getHospital());
//		String strClaimTypeRequest = "";
//		if(null != objIntimation.getClaimType())
//		{
//			strClaimTypeRequest = objIntimation.getClaimType().getValue();
//		}
//		OMPClaim objClaim = starFaxservice.populateClaimObject(objIntimation, strClaimTypeRequest);
//		    log.info("before save claim --->" + objIntimation.getIntimationId());
//		try {
//			entityManager.persist(objClaim);
//			entityManager.flush();
//		} catch(Exception e) {
//			e.printStackTrace();
//			log.error("******************Not Pulled from Premia***************** -----> " + objIntimation.getIntimationId());
//		}
//		
////		entityManager.refresh(objClaim);
//		log.info("after save claim --->" + objIntimation.getIntimationId() + "OMPClaim No --- >" + objClaim.getClaimId());
//		
//		objIntimation = getIntimationByKey(objIntimation.getKey());
//		objIntimation.setRegistrationStatus("REGISTERED");
//		entityManager.merge(objIntimation);
//		entityManager.flush();
//		return objClaim;
//	}
	
//	public List<PlannedAdmissionReportDto> getPlannedIntimationDetails(Map<String,Object> searchFilter){
//		List<PlannedAdmissionReportDto> resultList = new ArrayList<PlannedAdmissionReportDto>();
//		if(searchFilter != null && !searchFilter.isEmpty()){
//			
//			Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
//			Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
//				
//			try{	
//				final CriteriaBuilder builder = entityManager
//						.getCriteriaBuilder();
//				final CriteriaQuery<OMPIntimation> criteriaQuery = builder
//						.createQuery(OMPIntimation.class);
//
//				Root<OMPIntimation> intimationRoot = criteriaQuery
//						.from(OMPIntimation.class);
//				Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
//						"policy", JoinType.INNER);
//				Join<OMPIntimation, Insured> insuredJoin = intimationRoot.join(
//						"insured", JoinType.INNER);
//
//				List<Predicate> predicates = new ArrayList<Predicate>();
//				Expression<Date> dateExpression = intimationRoot
//						.<Date> get("createdDate");
//				if(fromDate != null){					
//					Predicate fromDatePredicate = builder
//							.greaterThanOrEqualTo(dateExpression,
//									fromDate);
//					predicates.add(fromDatePredicate);					
//				}
//				if(toDate != null){
//
//				Calendar c = Calendar.getInstance();
//				c.setTime(toDate);
//				c.add(Calendar.DATE, 1);
//				toDate = c.getTime();
//				Predicate toDatePredicate = builder
//						.lessThanOrEqualTo(dateExpression, toDate);
//				predicates.add(toDatePredicate);
//				
//				}
//				
//				Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
//				Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
//				predicates.add(statusPredicate);
//				
//				Predicate plannedAdminPredicate = builder.equal(intimationRoot.<MastersValue>get("admissionType").<Long>get("key"),ReferenceTable.PLANNED_ADMISSION_KEY);
//				predicates.add(plannedAdminPredicate);
//				
//				criteriaQuery.select(intimationRoot).where(
//						builder.and(predicates
//								.toArray(new Predicate[] {})));
//
//				final TypedQuery<OMPIntimation> intimationquery = entityManager
//						.createQuery(criteriaQuery);
//				
//				List<OMPIntimation> intimationList = intimationquery.getResultList();
//				
//				if(intimationList != null && !intimationList.isEmpty()){
//					
//					for(OMPIntimation intimation : intimationList){
//						if(!isOPintimation(intimation.getKey())){
//							NewIntimationDto intimationDto = getIntimationDto(intimation);
//							PlannedAdmissionReportDto plannedDto = new PlannedAdmissionReportDto(intimationDto);
//							plannedDto.setSno(intimationList.indexOf(intimation)+1);
//							resultList.add(plannedDto);		
//						}
//					}					
//				}				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//		}
//		return resultList;
//	}
	
//	public List<IntimatedRiskDetailsReportDto> getIntimatedRiskDetails(Map<String,Object> searchFilter){
//		List<IntimatedRiskDetailsReportDto> resultList = new ArrayList<IntimatedRiskDetailsReportDto>();
//		if(searchFilter != null && !searchFilter.isEmpty()){
//			
//			Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
//			Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
//			String intimationNo = searchFilter.containsKey("intimationNo") && searchFilter.get("intimationNo") != null ? (String) searchFilter.get("intimationNo") : null;
//				
//			try{	
//				final CriteriaBuilder builder = entityManager
//						.getCriteriaBuilder();
//				final CriteriaQuery<OMPIntimation> criteriaQuery = builder
//						.createQuery(OMPIntimation.class);
//
//				Root<OMPIntimation> intimationRoot = criteriaQuery
//						.from(OMPIntimation.class);
//				Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
//						"policy", JoinType.INNER);
//				Join<OMPIntimation, Insured> insuredJoin = intimationRoot.join(
//						"insured", JoinType.INNER);
//
//				List<Predicate> predicates = new ArrayList<Predicate>();
//				Expression<Date> dateExpression = intimationRoot
//						.<Date> get("createdDate");
//				if(fromDate != null){
//					Predicate fromDatePredicate = builder
//							.greaterThanOrEqualTo(dateExpression,
//									fromDate);
//					predicates.add(fromDatePredicate);
//				}
//				 if(toDate != null){
//					Calendar c = Calendar.getInstance();
//					c.setTime(toDate);
//					c.add(Calendar.DATE, 1);
//					toDate = c.getTime();
//					Predicate toDatePredicate = builder
//							.lessThanOrEqualTo(dateExpression, toDate);
//					predicates.add(toDatePredicate);
//				
//				}
//				if(intimationNo != null && !("").equalsIgnoreCase(intimationNo)){
//					
//					Predicate intimationNoPredicate = builder
//							.like(intimationRoot.<String>get("intimationId"), intimationNo);
//					predicates.add(intimationNoPredicate);
//				}
//				
//				Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
//				Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
//				predicates.add(statusPredicate);
//				
//				criteriaQuery.select(intimationRoot).where(
//						builder.and(predicates
//								.toArray(new Predicate[] {})));
//
//				final TypedQuery<OMPIntimation> intimationquery = entityManager
//						.createQuery(criteriaQuery);
//				
//				List<OMPIntimation> intimationList = intimationquery.getResultList();
//				
//				if(intimationList != null && !intimationList.isEmpty()){
//					
//					for(OMPIntimation intimation : intimationList){	
////						if(!isOPintimation(intimation.getKey())){
//							NewIntimationDto intimationDto = getIntimationDto(intimation);
//							IntimatedRiskDetailsReportDto intimatedRiskDto = new IntimatedRiskDetailsReportDto(intimationDto);
//							resultList.add(intimatedRiskDto);
////						}		
//					}					
//				}				
//				
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		return resultList;	
//	}
	
//	public List<IntimationAlternateCPUwiseReportDto> getAlternateCPUwiseIntimationDetails(Map<String,Object> searchFilter){
//		List<IntimationAlternateCPUwiseReportDto> resultListDto = new ArrayList<IntimationAlternateCPUwiseReportDto>();
//		
//		if(searchFilter != null && !searchFilter.isEmpty()){
//			try{
//				
//				Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
//				Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
//					
//					final CriteriaBuilder builder = entityManager
//							.getCriteriaBuilder();
//					final CriteriaQuery<OMPIntimation> criteriaQuery = builder
//							.createQuery(OMPIntimation.class);
//
//					Root<OMPIntimation> intimationRoot = criteriaQuery
//							.from(OMPIntimation.class);
//					Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
//							"policy", JoinType.INNER);
//					Join<OMPIntimation, Insured> insuredJoin = intimationRoot.join(
//							"insured", JoinType.INNER);
//
//					List<Predicate> predicates = new ArrayList<Predicate>();
//				
//					if(fromDate != null && toDate != null){
//					
//						Expression<Date> dateExpression = intimationRoot
//								.<Date> get("createdDate");
//						Predicate fromDatePredicate = builder
//								.greaterThanOrEqualTo(dateExpression,
//										fromDate);
//						predicates.add(fromDatePredicate);
//
//						Calendar c = Calendar.getInstance();
//						c.setTime(toDate);
//						c.add(Calendar.DATE, 1);
//						toDate = c.getTime();
//						Predicate toDatePredicate = builder
//								.lessThanOrEqualTo(dateExpression, toDate);
//						predicates.add(toDatePredicate);
//					
//					}
//						
////						Predicate cpuPredicate = builder  
////								.notLike(intimationRoot.<TmpCPUCode>get("cpuCode").<String>get("cpuCode"), intimationRoot.<Policy>get("policy").<String>get("homeOfficeCode"));
////						predicates.add(cpuPredicate);
//					
//					Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
//					Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
//					predicates.add(statusPredicate);
//					
//					criteriaQuery.select(intimationRoot).where(
//							builder.and(predicates
//									.toArray(new Predicate[] {})));
//
//					final TypedQuery<OMPIntimation> intimationquery = entityManager
//							.createQuery(criteriaQuery);
//					
//					List<OMPIntimation> intimationList = intimationquery.getResultList();
//					
//					if(intimationList != null && !intimationList.isEmpty()){
//						
//						for(OMPIntimation intimation : intimationList){
//							entityManager.refresh(intimation);
//							if(!isOPintimation(intimation.getKey())){
//								NewIntimationDto intimationDto = getIntimationDto(intimation);
//								if(intimationDto.getCpuCode() != null && intimationDto.getOrganizationUnit() != null && intimationDto.getOrganizationUnit().getCpuCode() != null && !(intimationDto.getOrganizationUnit().getCpuCode()).equalsIgnoreCase(intimationDto.getCpuCode())){
//									IntimationAlternateCPUwiseReportDto intimationAlternateCPUDto = new IntimationAlternateCPUwiseReportDto(intimationDto);
//									OMPClaim claimObj = getClaimforIntimation(intimation.getKey());
//									if(claimObj != null){
//										entityManager.refresh(claimObj);
//										intimationAlternateCPUDto.setClaimNo(claimObj.getClaimId());
//									}
//									resultListDto.add(intimationAlternateCPUDto);	
//								}								
//							}		
//						}					
//					}
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//			
//		return resultListDto;
//	
//	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<OMPClaim> getViewTmpClaimsByPolicyNumber(String policyNumber) {
		
		List<OMPClaim> resultList = new ArrayList<OMPClaim>();
		if (policyNumber != null) {

			Query findByPolicyNumber = entityManager.createNamedQuery(
					"OMPClaim.findByPolicyNumber").setParameter("policyNumber",
					policyNumber);
			findByPolicyNumber.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
			try {
				resultList = findByPolicyNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			for (OMPClaim claim : resultList) {
				entityManager.refresh(claim);
			}
		}
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<OMPClaim> getTmpClaimByIntimation(Long intimationKey) {
		List<OMPClaim> a_claimList = new ArrayList<OMPClaim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("OMPClaim.findByOMPIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			findByIntimationKey.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
			try {

				a_claimList = (List<OMPClaim>) findByIntimationKey.getResultList();
				
				for (OMPClaim claim : a_claimList) {
					entityManager.refresh(claim);
				}

				System.out.println("size++++++++++++++" + a_claimList.size());

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			} finally {

			}

		} else {
			// intimationKey null
		}
		return a_claimList;

	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public BeanItemContainer<SelectValue> getAllHospitalCodeAndValue() {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		Query query = entityManager.createNamedQuery("OMPHospitals.findAll");
		List<OMPHospitals> hospitalValueList = query.getResultList();
		SelectValue selected = null;
		for (OMPHospitals hosRecord :hospitalValueList){
			selected = new SelectValue();
			selected.setId(hosRecord.getKey());
			selected.setValue(hosRecord.getName());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(selectValueList);
		return selectValueContainer;
	}
	
	@SuppressWarnings({ "unchecked"})
	public OMPHospitals getHospitalDetails(String nameOfHospital) {
		Query query = entityManager.createNamedQuery("OMPHospitals.findByHospitalName");
		query = query.setParameter("hospitalName", nameOfHospital);
		List<OMPHospitals> hospitalValueList = query.getResultList();
		if(hospitalValueList!=null && !hospitalValueList.isEmpty()){
			return hospitalValueList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked"})
	public OMPHospitals getHospitalDetailsBykey(Long key) {
		Query query = entityManager.createNamedQuery("OMPHospitals.findbyKey");
		query = query.setParameter("key", key);
		List<OMPHospitals> hospitalValueList = query.getResultList();
		if(hospitalValueList!=null && !hospitalValueList.isEmpty()){
			return hospitalValueList.get(0);
		}
		return null;
	}
	
	
	public void doInsertNewOMPHospital(OMPHospitals hosObj){
		entityManager.persist(hosObj);
		entityManager.flush();
	}
	
	public String getMaxOMPHospitalCode(){
		Query query = entityManager.createNamedQuery("OMPHospitals.findMaxHospitalCode");
		String maxHospitalCode = (String) query.getSingleResult();
		return maxHospitalCode;
	}
	
	public  List<OMPPreviousClaimTableDTO> getPreviousClaimByForRegistration(NewIntimationDto newIntimationDto) {
//		List<ViewTmpIntimation> intimationlist = getPolicyWiseClaimList(intimation);
//		List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);
		List<OMPPreviousClaimTableDTO> claimList = new ArrayList<OMPPreviousClaimTableDTO>();
		OMPIntimation intimation = getTmpIntimationByKey(newIntimationDto.getKey());
		if(intimation != null){
		List<OMPClaim> claimsByPolicyNumber = getViewTmpClaimsByPolicyNumber(intimation.getPolicy().getPolicyNumber());
		
		List<OMPClaim> claimByIntimation = getTmpClaimByIntimation(intimation.getKey());
		List<OMPClaim> filterClaim = new ArrayList<OMPClaim>();
		
		for (OMPClaim claim : claimsByPolicyNumber) {
			
			filterClaim.add(claim);
			
			if(claimByIntimation != null && !claimByIntimation.isEmpty()){
				if(claim.getKey().equals(claimByIntimation.get(0).getKey())){
					filterClaim.remove(claim);
				
				}
			}
			
		}
		
		
		 claimList = getPreviousClaims(filterClaim, intimation.getIntimationId());
		}
        return claimList;
		
	}
	
	
	
	public List<OMPPreviousClaimTableDTO> getPreviousClaims(
			List<OMPClaim> claimList, String strClaimNumber) {
		List<OMPClaim> objClaimList = new ArrayList<OMPClaim>();
		List<OMPPreviousClaimTableDTO> previousClaimList;// = new ArrayList<OMPPreviousClaimTableDTO>(); // for test
		
		
		for (OMPClaim objClaim : claimList) {
			if (!(strClaimNumber.equalsIgnoreCase(objClaim.getClaimId()))) {
				objClaimList.add(objClaim);
			}
		}
		previousClaimList = OMPPreviousClaimMapper.getInstance()// we hav to do
				.getPreviousClaimDTOList(objClaimList);
		
		for (OMPPreviousClaimTableDTO previousClaimsTableDTO : previousClaimList) {
			
//			if(previousClaimsTableDTO.getCustomerID() != null){
//				String pedDetails = "";
//				List<InsuredPedDetails> pedByInsured = getPEDByInsured(Long.valueOf(previousClaimsTableDTO.getCustomerID()));
//				for (InsuredPedDetails insuredPedDetails : pedByInsured) {
//					if(insuredPedDetails.getPedDescription() != null){
//						pedDetails += insuredPedDetails.getPedDescription() +", ";
//					}
//				}
//				previousClaimsTableDTO.setPedName(pedDetails);
//			}
			
			
//			Preauth previousPreauth = getPreviousPreauth(previousClaimsTableDTO.getKey());
//			if(previousPreauth != null){
////				previousClaimsTableDTO.setClaimAmount(getPreauthReqAmt(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
//				previousClaimsTableDTO.setClaimAmount(calculatePreRequestedAmt(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
//			}
			
			Long claimedAmountForROD = getClaimedAmountForROD(previousClaimsTableDTO.getKey());
			if(claimedAmountForROD != null && claimedAmountForROD > 0){
				previousClaimsTableDTO.setClaimAmount(String.valueOf(claimedAmountForROD));
			}
			
			Double reimbursementApprovedAmount = getReimbursementApprovedAmount(previousClaimsTableDTO.getKey());
			if(reimbursementApprovedAmount != null && reimbursementApprovedAmount > 0){
				previousClaimsTableDTO.setApprovedAmount(reimbursementApprovedAmount.toString());
			}
			
			if(previousClaimsTableDTO != null && previousClaimsTableDTO.getRecordFlag() != null && previousClaimsTableDTO.getRecordFlag().equalsIgnoreCase("P")){
				
				if(previousClaimsTableDTO.getSettledAmountForPreviousClaim() != null){
					previousClaimsTableDTO.setApprovedAmount(previousClaimsTableDTO.getSettledAmountForPreviousClaim().toString());
					
				}
				
//				if(previousClaimsTableDTO.getDiagnosisForPreviousClaim() != null){
//					previousClaimsTableDTO.setDiagnosis(previousClaimsTableDTO.getDiagnosisForPreviousClaim());
//				}
			}
			
			List<OMPReimbursement> reimbursement = getTmpReimbursment(previousClaimsTableDTO.getKey());
			
			if(reimbursement != null){
				List<Long> copayValues = new ArrayList<Long>();
				
				String icdCodes = "";
				
				for (OMPReimbursement viewTmpReimbursement : reimbursement) {
					
					OMPReimbursement reimbursementObjectByKey = getReimbursementObjectByKey(viewTmpReimbursement.getKey());
					if(reimbursementObjectByKey != null && reimbursementObjectByKey.getAmtConsCopayPercentage() != null){
						copayValues.add(reimbursementObjectByKey.getAmtConsCopayPercentage().longValue());
					}
					
//				    copayValues.add(reimbursementCalCulationDetails2.getCopayAmount().longValue());

//					List<ViewTmpDiagnosis> pedValidationForTmp = getPedValidationForTmp(viewTmpReimbursement.getKey());
//					if(pedValidationForTmp != null){
//						for (ViewTmpDiagnosis viewTmpDiagnosis : pedValidationForTmp) {
//							if(viewTmpDiagnosis.getCopayPercentage() != null){
//								copayValues.add(viewTmpDiagnosis.getCopayPercentage().longValue());
//							}
//							
//							Long icdCodeId = viewTmpDiagnosis.getIcdCodeId();
//							if(icdCodeId != null){
//								IcdCode icdCode = getIcdCode(icdCodeId);
//								if(icdCode != null && icdCode.getDescription() != null){
//									icdCodes += icdCode.getDescription() +", ";
//								}
//							}
//						}
//					}
					
//					previousClaimsTableDTO.setIcdCodes(icdCodes);
					
//					List<ViewTmpProcedure> tmpProcedureByTransactionKey = getTmpProcedureByTransactionKey(viewTmpReimbursement.getKey());
//					if(tmpProcedureByTransactionKey != null){
//						for (ViewTmpProcedure viewTmpProcedure : tmpProcedureByTransactionKey) {
//							if(viewTmpProcedure.getCopayPercentage() != null){
//								copayValues.add(viewTmpProcedure.getCopayPercentage().longValue());
//							}
//						}
//					}
					
				}
				
//				if(! copayValues.isEmpty()){
//					Long maximumCopay = Collections.max(copayValues);		
//					previousClaimsTableDTO.setCopayPercentage(maximumCopay);
//				}

//			}else{
//				if(previousPreauth != null ){
//					List<Long> copayValues = new ArrayList<Long>();
//					String icdCodes = "";
//					
//					if(previousPreauth.getCoPay() != null){
//						copayValues.add(previousPreauth.getCoPay().longValue());
//					}
//					
//					List<ViewTmpDiagnosis> pedValidationForTmp = getPedValidationForTmp(previousPreauth.getKey());
//					if(pedValidationForTmp != null){
//						for (ViewTmpDiagnosis viewTmpDiagnosis : pedValidationForTmp) {
//							if(viewTmpDiagnosis.getCopayPercentage() != null){
//								copayValues.add(viewTmpDiagnosis.getCopayPercentage().longValue());
//							}
//							
//							Long icdCodeId = viewTmpDiagnosis.getIcdCodeId();
//							if(icdCodeId != null){
//								IcdCode icdCode = getIcdCode(icdCodeId);
//								if(icdCode != null && icdCode.getDescription() != null){
//									icdCodes += icdCode.getDescription() +", ";
//								}
//							}
//						}
//					}
//					
//					previousClaimsTableDTO.setIcdCodes(icdCodes);
//					
//					List<ViewTmpProcedure> tmpProcedureByTransactionKey = getTmpProcedureByTransactionKey(previousPreauth.getKey());
//					if(tmpProcedureByTransactionKey != null){
//						for (ViewTmpProcedure viewTmpProcedure : tmpProcedureByTransactionKey) {
//							if(viewTmpProcedure.getCopayPercentage() != null){
//								copayValues.add(viewTmpProcedure.getCopayPercentage().longValue());
//							}
//						}
//					}
//					
//					if(! copayValues.isEmpty()){
//						Long maximumCopay = Collections.max(copayValues);
//						previousClaimsTableDTO.setCopayPercentage(maximumCopay);
//					}
//				}
//			}

		}
		try{
			if (objClaimList != null && previousClaimList != null && !objClaimList.isEmpty() && !previousClaimList.isEmpty()) {
				for (int i = 0; i < objClaimList.size(); i++) {
					if(previousClaimList.get(i).getAdmissiondate() != null){
                         previousClaimList.get(i).setAdmissiondate(objClaimList.get(i).getIntimation().getAdmissionDate());
					}
					OMPIntimation intimation = objClaimList.get(i).getIntimation();
					if(intimation != null && intimation.getHospitalName() != null){
//						Hospitals hospitals = getHospitalByKey(intimation.getHospital());
//						if(hospitals != null){
							previousClaimList.get(i).setHospitalname(intimation.getHospitalName());
//						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		
		
		
//		if(!previousClaimList.isEmpty()) {
//			for (OMPPreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
//				String diagnosisStr = " ";
//				List<PedValidation> pedValidationsList = getDiagnosis(previousClaimsTableDTO.getIntimationKey());
//			    List<Long> keyList = new ArrayList<Long>();
//				if (!pedValidationsList.isEmpty()) {
//					for (PedValidation pedValidation : pedValidationsList) {
//						
////						map.put(pedValidation.getDiagnosisId(), masterService.getDiagnosis(pedValidation
////										.getDiagnosisId()));
//						if(! keyList.contains(pedValidation.getDiagnosisId())){
//						diagnosisStr = (diagnosisStr == " " ? ""
//								: diagnosisStr + ", ")
//								+ getDiagnosisByKey(pedValidation
//										.getDiagnosisId());
//						keyList.add(pedValidation.getDiagnosisId());
//						}
//						
//					}
//					
//					previousClaimsTableDTO.setDiagnosis(diagnosisStr);
//					
//				}
//				
//			}
//			
//		}
		}
		
		return previousClaimList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PedValidation> getDiagnosis(Long intimationkey){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	  	CriteriaQuery<PedValidation> createQuery = cb.createQuery(PedValidation.class);
	  	Root<PedValidation> fromQuery = createQuery.from(PedValidation.class);
	  	createQuery.select(fromQuery);
	  	Expression<String> expression = fromQuery.get("intimation");
	  	createQuery.where(cb.equal(expression, intimationkey));
	  	Query filterQuery = entityManager.createQuery(createQuery);
		List<PedValidation> policyConditionsList = (List<PedValidation>) filterQuery.getResultList();
		
		
		return policyConditionsList;
	}
	
	public String getDiagnosisByKey(Long key) {
		Diagnosis diagnosis = entityManager.find(Diagnosis.class, key);
		return diagnosis != null ? diagnosis.getValue() : "";
	}

	public IcdCode getIcdCode(Long icdCodeKey){
		   
		Query query = entityManager.createNamedQuery("IcdCode.findByKey")
				.setParameter("primaryKey", icdCodeKey);

		IcdCode icdCodeList = (IcdCode) query.getSingleResult();
		
		return icdCodeList;
	}


	public Hospitals getHospitalByKey(Long hospitalKey){
		
		Query findHospitalElement = entityManager
				.createNamedQuery("Hospitals.findByKey").setParameter("key", hospitalKey);
		
		List<Hospitals> hospital  = (List<Hospitals>) findHospitalElement.getResultList();
		if(hospital != null && ! hospital.isEmpty()){
			return hospital.get(0);
		}
		return null;
	}

	
	public List<ViewTmpProcedure> getTmpProcedureByTransactionKey(Long key){
		
		Query query = entityManager.createNamedQuery("ViewTmpProcedure.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
		List<ViewTmpProcedure> resultList = (List<ViewTmpProcedure>)query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
	    	return resultList;
	    }
	    
	    return null;
		
	}

	public List<ViewTmpDiagnosis> getPedValidationForTmp(Long key){
		
		Query query = entityManager.createNamedQuery("ViewTmpDiagnosis.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
	    List<ViewTmpDiagnosis> resultList = (List<ViewTmpDiagnosis>)query.getResultList();
	    
	    if(resultList != null && ! resultList.isEmpty()){
	    	return resultList;
	    }
	    
	    return null;
	}
	
	public List<OMPReimbursement> getTmpReimbursment(Long claimKey) {
		
		List<OMPReimbursement> reimbursmentList = new ArrayList<OMPReimbursement>();
		
		List<OMPReimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (OMPReimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
//						&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					reimbursmentList.add(reimbursement);	
				}
			}
		}

		return reimbursmentList;
	}
	
	
	public List<OMPReimbursement> getPreviousRODByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<OMPReimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (OMPReimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
	}

	
	public OMPReimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey")
				.setParameter("primaryKey", key);
		
		List<OMPReimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
		/*
		Reimbursement reimbursement = (Reimbursement) query.getSingleResult();
		entityManager.refresh(reimbursement);
		return reimbursement;*/
	}

	@SuppressWarnings("unchecked")
	public List<InsuredPedDetails> getPEDByInsured(Long insuredKey){
		 Query query = entityManager.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		 query = query.setParameter("insuredKey", insuredKey);		        
		 List<InsuredPedDetails> insuredList  = query.getResultList();			     
		return insuredList;

	}
	
	public Preauth getPreviousPreauth(Long claimKey) {
		List<Preauth> preauthByClaimKey =getPreauthByClaimKey(claimKey);
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);
			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			Integer nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId()
							.split("/");
					String number = splitNumber[splitNumber.length - 1];
					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						previousPreauth = preauth;
						nextReferenceNo = Integer.valueOf(number);
					}
				}
			}
		}
		return previousPreauth;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preuathList = (List<Preauth>) query.getResultList();
		if(preuathList !=  null){
			for (Preauth preauth : preuathList) {
				entityManager.refresh(preauth);
			}
		}
		return preuathList;
	}
	

	public String calculatePreRequestedAmt(Long preAuthKey, Long claimKey)
	{
		float lAmt= 0f;
		Query findAll = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		findAll.setParameter("preauthKey", preAuthKey);		
		List<ClaimAmountDetails> claimAmtDetails = (List<ClaimAmountDetails>)findAll.getResultList();
		for (ClaimAmountDetails claimAmountDetails : claimAmtDetails) {
			entityManager.refresh(claimAmountDetails);			
		}
		if(null !=claimAmtDetails )
		{
			//for(int i = 0 ; i<claimAmtDetails.size() ; i++)
			for(ClaimAmountDetails objClaim :claimAmtDetails )
			{
				entityManager.refresh(objClaim);
				if(null!= objClaim.getClaimedBillAmount())
				{
					lAmt = lAmt+ objClaim.getClaimedBillAmount();
				}
			}
			return String.valueOf(lAmt);
		}
		else
		{
			return "0";
		}
	}
	
	public Long getClaimedAmountForROD(Long claimKey){
		
		Double claimedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKey").setParameter(
						"claimKey", claimKey);
		List<OMPReimbursement> rodList = query.getResultList();
		
		for (OMPReimbursement reimbursement : rodList) {
			if(reimbursement.getDocAcknowLedgement() != null){
				OMPDocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
				
					claimedAmount = ( docAcknowledgement.getHospitalizationClaimedAmount() != null ? claimedAmount + docAcknowledgement.getHospitalizationClaimedAmount() : 0d )
					+ ( docAcknowledgement.getPreHospitalizationClaimedAmount() != null ? claimedAmount + docAcknowledgement.getPreHospitalizationClaimedAmount() : 0d )
					+ ( docAcknowledgement.getPostHospitalizationClaimedAmount() != null ? claimedAmount + docAcknowledgement.getPostHospitalizationClaimedAmount() : 0d );
			}
		}
		
		Long amount =claimedAmount.longValue();
		
		return amount;
		
	}
	
	public Double getReimbursementApprovedAmount(Long claimKey){
		
		Double approvedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKey").setParameter(
						"claimKey", claimKey);
		List<OMPReimbursement> rodList = query.getResultList();
		
		for (OMPReimbursement reimbursement : rodList) {
			if(reimbursement.getFinancialApprovedAmount() != null){
				approvedAmount += reimbursement.getFinancialApprovedAmount();
			}
		}
		
		return approvedAmount;
		
	}
	
	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<OMPReimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (OMPReimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursementByNumber(String rodNumber) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findOMPRodByNumber")
				.setParameter("rodNumber", rodNumber);
		List<OMPReimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (OMPReimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<OMPReimbursement> getRembursementDetailsByClaimKey(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKey");
		query.setParameter("claimKey", claimKey);

		List<OMPReimbursement> reimbursementList = null;
		try {
			reimbursementList = (List<OMPReimbursement>) query.getResultList();
			
			for (OMPReimbursement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reimbursementList;

	}
	
	@SuppressWarnings("unchecked")
	public OMPClaimPayment getClaimPaymentByRODNo(String rodNo)
	{
		
		OMPClaimPayment claimPayment = null;
		Query query = entityManager.createNamedQuery("OMPClaimPayment.findByRodNo");
		query = query.setParameter("rodNumber", rodNo);
		//query = query.setParameter("statusId", ReferenceTable.PAYMENT_SETTLED);
		List<OMPClaimPayment> claimPaymentList = query.getResultList();
		if(null != claimPaymentList && !claimPaymentList.isEmpty())
		{
			claimPayment = claimPaymentList.get(0);
			entityManager.refresh(claimPaymentList.get(0));
			return claimPayment;
			
		}
		return claimPayment;
		
	}
	
	
	
//	@SuppressWarnings("unchecked")
//	public OMPClaimPayment getClaimPaymentByIntimationNumber(String intimationNumber) {
//		List<OMPClaimPayment> resultClaim = null;
//		if (intimationNumber != null) {
//
//			Query findByIntimationNum = entityManager.createNamedQuery(
//					"OMPClaimPayment.findByOMPIntimationNumber").setParameter(
//					"intimationNumber", intimationNumber);
//		//	findByIntimationNum.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
//
//			try {
//				resultClaim = (List<OMPClaimPayment>) findByIntimationNum.getResultList();
//				
//				if(resultClaim != null && !resultClaim.isEmpty()){
//					return resultClaim.get(0);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	
	@SuppressWarnings("unchecked")
	public List<OMPReimbursement> getDocumentRecDetailsByClaimKey(Long claimKey, String value) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKeyDocRec");
		query.setParameter("claimKey", claimKey);
		query.setParameter("value", value);
		

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();
		
		for (OMPReimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
		}

		return reimbursementList;

	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getClaimbyClassification(String value) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKeyClass");
		query.setParameter("value", value);
		

		List<Long> claimKey = (List<Long>) query
				.getResultList();
		
		return claimKey;

	}
	
	public OMPClaimPayment getRODPaymentStatus(String argRodNumber){
		Query query = entityManager.createNamedQuery("OMPClaimPayment.findByRodNo");
		query.setParameter("rodNumber", argRodNumber);
		List<OMPClaimPayment> result = (List<OMPClaimPayment>) query.getResultList();
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<OMPClaimPayment> getPaymetForLetterGeneration(Long status,
			String letterFlag, Long paymentStatusId) {

		Query query = entityManager
				.createNamedQuery("OMPClaimPayment.findByOMPPaymentByStatus");
		query.setParameter("status", status);
		query.setParameter("letterFlag", letterFlag);
		query.setParameter("paymentStatusId", paymentStatusId);


		List<OMPClaimPayment> ompPayment = (List<OMPClaimPayment>) query
				.getResultList();

		return ompPayment;

	}

	@SuppressWarnings("unchecked")
	public OMPIntimation getOMPIntimationByPolicy(String policyNumber) {
		if (policyNumber != null) {

			Query query = entityManager
					.createNamedQuery("OMPIntimation.findByPolicy");
			query.setParameter("policyNumber", policyNumber);

			List<OMPIntimation> resultList = (List<OMPIntimation>) query
					.getResultList();

			if (resultList != null && resultList.size() != 0) {
				entityManager.refresh(resultList.get(0));
				return resultList.get(0);
			}
		}
		return null;
	}
	
	//CR2019034
	
	public void ompPaymentLetterGenerationProcess(){
		
		OMPClaimProcessorDTO ompProcessorDTO = null;
		NewIntimationDto newIntimationDto = null;
		OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO = null;
		OMPPaymentDetailsTableDTO ompPaymentDetailsTableDTO= null;
		
		List<OMPClaimPayment> paymetForLetterGeneration = getPaymetForLetterGeneration(ReferenceTable.PAYMENT_SETTLED, "Y",ReferenceTable.OMPPAYTO_INSURED);
		
		if (!(paymetForLetterGeneration.isEmpty())) {
			for (OMPClaimPayment payment : paymetForLetterGeneration) {
				ompProcessorDTO = new OMPClaimProcessorDTO();
				ompClaimCalculationViewTableDTO = new OMPClaimCalculationViewTableDTO();
				ompPaymentDetailsTableDTO = new OMPPaymentDetailsTableDTO();
				newIntimationDto = getIntimationDto(getOMPIntimationByPolicy(payment.getPolicyNo()));
				ompPaymentDetailsTableDTO.setChequeDate(payment.getChequeDdDate());
				ompPaymentDetailsTableDTO.setTransectionChequeNo(payment.getChequeDdNumber()!=null?payment.getChequeDdNumber():"");
				OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(payment.getIntimationNumber());

				MastersEvents event = claimsByIntimationNumber.getEvent();
				if (event != null) {
					ompProcessorDTO.setEventdescription(event.getEventDescription()+ " " + event.getEventCode());
				}

				//newIntimationDto.setClaimNumber(payment.getClaimNumber());
				newIntimationDto.setIntimationId(payment.getIntimationNumber()!=null?payment.getIntimationNumber():"");
				ompProcessorDTO.setNewIntimationDto(newIntimationDto);
                Double settledAmountDouble = null;
				if (payment.getClmSecCode() != null && payment.getClmSecCode().equalsIgnoreCase("OMP Claim Related")) {
					settledAmountDouble = payment.getTotApprovedAmt();
				} else if (payment.getClmSecCode() != null && payment.getClmSecCode().equalsIgnoreCase("OMP Other Expenses")) {
					settledAmountDouble = payment.getTotAmtINr();
				} else if (payment.getClmSecCode() != null && payment.getClmSecCode().equalsIgnoreCase("Negotiator Fee")) {
					settledAmountDouble = payment.getApprovedAmt();
				}
				ompClaimCalculationViewTableDTO.setFinalApprovedAmtInr(payment.getTotApprovedAmt());
				ompPaymentDetailsTableDTO.setSettleAmt(settledAmountDouble != null ? settledAmountDouble.toString() : "");
				ompClaimCalculationViewTableDTO.setClaimProcessorDTO(ompProcessorDTO);
				ompPaymentDetailsTableDTO.setOmpClaimCalculationViewTableDTO(ompClaimCalculationViewTableDTO);

				List<OMPPaymentDetailsTableDTO> ompProcessorDTOList = new ArrayList<OMPPaymentDetailsTableDTO>();

				ompProcessorDTOList.add(ompPaymentDetailsTableDTO);
				DocumentGenerator docGen = new DocumentGenerator();
				ReportDto reportDto = new ReportDto();
				// reportDto.setClaimId(ompProcessorDTO.getClaimDto().getClaimId());
				reportDto.setBeanList(ompProcessorDTOList);
				String filePath = "";
				String templateName = "OMPPaymentLetter";
				filePath = docGen.generatePdfDocument(templateName, reportDto);
				ompProcessorDTO.setDocumentType(SHAConstants.OMP_PAYMENT_LETTER);
				final String finalFilePath = filePath;
				ompProcessorDTO.setDocumentFilePath(finalFilePath);
				ompProcessorDTO.setDocumentSource(SHAConstants.OMP_PAYMENT);

				HashMap dataMap = new HashMap();
				dataMap.put("intimationNumber", payment.getIntimationNumber());
				dataMap.put("claimNumber", payment.getClaimNumber());
				// dataMap.put("cashlessNumber", preauth.getPreauthId());
				dataMap.put("filePath", ompProcessorDTO.getDocumentFilePath());
				dataMap.put("docType", SHAConstants.OMP_PAYMENT_LETTER);
				dataMap.put("docSources", SHAConstants.OMP_PAYMENT);
				dataMap.put("createdBy", "SYSTEM");
				// String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				WeakHashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(finalFilePath);

				if(inParameters !=null){
					OMPDocumentDetails ompDocument = new OMPDocumentDetails();
					ompDocument.setIntimationNumber(payment.getIntimationNumber());
					ompDocument.setClaimNumber(payment.getClaimNumber());
					ompDocument.setReimbursementNumber(payment.getRodNumber());
					ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
					ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
					ompDocument.setDocumentSource(SHAConstants.OMP_PAYMENT);
					ompDocument.setDocumentType(SHAConstants.OMP_PAYMENT_LETTER);
					ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					ompDocument.setCreatedBy(ompProcessorDTO.getUserName());
					entityManager.persist(ompDocument);

					payment.setLetterFlag("Y");
					payment.setDocToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
					entityManager.merge(payment);
				}

			}

		}
}
	
	@SuppressWarnings("unchecked")
	public List<OMPDocAcknowledgement> getAcknowledgetDetailsByClaimKey(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findAckByClaim");
		query.setParameter("claimkey", claimKey);

		List<OMPDocAcknowledgement> reimbursementList = null;
		try {
			reimbursementList = (List<OMPDocAcknowledgement>) query.getResultList();
			
			for (OMPDocAcknowledgement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reimbursementList;

	}
	
	public OMPDocAcknowledgement getAcknowledgementByRodKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByReimbursement");
		query = query.setParameter("rodKey", rodKey);
		
		List<OMPDocAcknowledgement> docAcknowledgementList = query.getResultList();
		
		if(docAcknowledgementList != null && !docAcknowledgementList.isEmpty())
		return docAcknowledgementList.get(0);
		
		return null;
	}
	
	public Insured getInsuredByKey(Long key) {

		Query query = entityManager
				.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		List<Insured> insuredList = (List<Insured>) query.getResultList();
		if (insuredList != null && ! insuredList.isEmpty())
			return insuredList.get(0);
		return null;

	}
	
public String createDMSToken(String policyNo){
		
		String apiKey = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
		try{
			Date now = new Date();
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
	//		claimsSet.issueTime(now);
			claimsSet.claim("application","GALAXY");
    		claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));
    		claimsSet.claim("PolNo", policyNo);
			JWSSigner signer = new MACSigner(apiKey);
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet.build());
			signedJWT.sign(signer);
			System.out.println("Token : "+signedJWT);
			return signedJWT.serialize();
		}catch (JOSEException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
}


