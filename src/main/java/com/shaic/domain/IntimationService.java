package com.shaic.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Column;
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
import org.apache.ws.security.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.resteasy.util.GetRestful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.aadhar.pages.UpdateAadharDetailsDTO;
import com.shaic.claim.bedphoto.UpdateBedPhotoFilePage;
import com.shaic.claim.bedphoto.UploadBedPhotoDTO;
import com.shaic.claim.enhacement.table.PreviousPreAuthMapper;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.fileUpload.ReferenceDocument;
import com.shaic.claim.intimatino.view.ViewIntimationDTO;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.pancard.page.UploadedPanCardDocumentsDTO;
import com.shaic.claim.policy.search.ui.BancsAdjustPolicyInstallemtResponse;
import com.shaic.claim.policy.search.ui.InsertHospitalToPMS;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremInsuredBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.SearchClaimRegisterationFormDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.reimbursement.talktalktalk.DialerCallRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerEndCallRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerEndCallResponse;
import com.shaic.claim.reimbursement.talktalktalk.DialerHoldCallRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerHoldCallResponse;
import com.shaic.claim.reimbursement.talktalktalk.DialerLoginLogoutResponse;
import com.shaic.claim.reimbursement.talktalktalk.DialerLoginRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerLogoutRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerPlaceCallRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerPlaceCallResponse;
import com.shaic.claim.reimbursement.talktalktalk.DialerUnholdRequest;
import com.shaic.claim.reimbursement.talktalktalk.DialerUnholdResponse;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.reports.intimatedRiskDetailsReport.IntimatedRiskDetailsReportDto;
import com.shaic.claim.reports.intimationAlternateCPUReport.IntimationAlternateCPUwiseReportDto;
import com.shaic.claim.reports.plannedAdmissionReport.PlannedAdmissionReportDto;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.domain.preauth.MasCmdOffsetConfiguration;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PremiaPreviousClaim;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages.ClaimWiseApprovalDto;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkService;

@Stateless
public class IntimationService {
	
	 private final Logger log = LoggerFactory.getLogger(IntimationService.class);

	 @EJB
	 private SearchGenerateRemainderBulkService bulkReminderSerachSevice;
	 
	@PersistenceContext
	protected EntityManager entityManager;
	
	PolicyService policyService;
	
	
	public IntimationService() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RegistrationBean> searchbyIntimationNo(
			List<RegistrationBean> registrationList) {
		List validRegistrationList = new ArrayList<RegistrationBean>();
		for (RegistrationBean instance : registrationList) {
			Query findByIntimNo = entityManager.createNamedQuery(
					"Intimation.findByIntimationNumber").setParameter(
					"intimationNo", instance.getIntimationNo());
			try {
				List resultList = findByIntimNo.getResultList();

				if (!resultList.isEmpty()) {
					Intimation intimationInstance = (Intimation) resultList
							.get(0);
					instance.setIntimationDate(intimationInstance
							.getCreatedDate());
					instance.setHospitalType(intimationInstance
							.getHospitalType() != null ? intimationInstance
							.getHospitalType().getValue() : null);
					instance.setIntimation(intimationInstance);
					validRegistrationList.add(instance);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return validRegistrationList;
	}

	public List<SearchClaimRegistrationTableDto> searchIntimationToRegister(
			List<SearchClaimRegistrationTableDto> searchClaimRegistrationDtoList) {
		
		List<SearchClaimRegistrationTableDto> validRegistrationList = new ArrayList<SearchClaimRegistrationTableDto>();
		List<Intimation> resultList;
		Intimation intimationInstance;
		Query findByIntimNo = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber");
		for (SearchClaimRegistrationTableDto searchDto : searchClaimRegistrationDtoList) {
			findByIntimNo.setParameter(
					"intimationNo", searchDto.getIntimationNumber());
			try {
				resultList = (List<Intimation>)findByIntimNo.getResultList();				

				if (!resultList.isEmpty()) {
					intimationInstance = (Intimation) resultList
							.get(0);
					searchDto.setIntimationDate(intimationInstance
							.getCreatedDate());
					searchDto.setHospitalType(intimationInstance
							.getHospitalType() != null ? intimationInstance
							.getHospitalType().getValue() : null);
					searchDto.setNewIntimationDto(NewIntimationMapper.getInstance()
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
	public List<Intimation> searchAll() {
		Query findAll = entityManager.createNamedQuery("Intimation.findAll");
		List<Intimation> intimationList = (List<Intimation>) findAll
				.getResultList();
		return intimationList;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Intimation getIntimationByKey(Long intimationKey) {

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
	
	@SuppressWarnings({ "unchecked" })
	public OPIntimation getOPIntimationByKey(Long intimationKey) {
		Query findByKey = entityManager.createNamedQuery("OPIntimation.findByKey").setParameter("intiationKey", intimationKey);
		List<OPIntimation> intimationList = (List<OPIntimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			return intimationList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public GalaxyIntimation getGalaxyIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("GalaxyIntimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<GalaxyIntimation> intimationList = (List<GalaxyIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	public Intimation getIntimationByKey(Long intimationKey,EntityManager em) {
		entityManager = em;
	  return getIntimationByKey(intimationKey);
	}	
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public ViewTmpIntimation getTmpIntimationByKey(Long intimationKey) {

//		intimationKey = 40021544L;
		Query findByKey = entityManager
				.createNamedQuery("ViewTmpIntimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<ViewTmpIntimation> intimationList = (List<ViewTmpIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		try{
			Query findByKey = entityManager.createNamedQuery(
					"Intimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
	
			List<Intimation> intimationList = (List<Intimation>) findByKey
					.getResultList();
	
			if (!intimationList.isEmpty()) {
				entityManager.refresh(intimationList.get(0));
				return intimationList.get(0);
	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	@SuppressWarnings("unchecked")
	public OPIntimation getOPIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"OPIntimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<OPIntimation> intimationList = (List<OPIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public OPIntimation getOPIntimationByNumber(String intimationNo) {
		 Query findByKey = entityManager.createNamedQuery(
					"OPIntimation.findByIntimationId").setParameter(
							"intimationNo",  "%"+intimationNo.toLowerCase()+"%");


		List<OPIntimation> intimationList = (List<OPIntimation>) findByKey.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public GalaxyIntimation getGalaxyIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"GalaxyIntimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<GalaxyIntimation> intimationList = (List<GalaxyIntimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	public boolean isGhiIntimation(String intimationNo)
	{
		
		boolean result = false;
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationId").setParameter(
						"intimationNo", "%"+intimationNo.toLowerCase()+"%");
//				"intimationNo", "%"+intimationNo.toLowerCase()+"%");

		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			
			if(intimationList.get(0).getPolicy() != null && intimationList.get(0).getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimationList.get(0).getPolicy().getProduct().getKey()))
			 {
				result = true;
			 }
		}
		return result;
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
	 * BeanItemContainer<IntimationDTO>( IntimationDTO.class); List<Intimation>
	 * searchResultList = new ArrayList<Intimation>();
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
	 * .getCriteriaBuilder(); final CriteriaQuery<Intimation> criteriaQuery =
	 * builder .createQuery(Intimation.class);
	 * 
	 * Root<Intimation> intimationRoot = criteriaQuery .from(Intimation.class);
	 * Join<Intimation, Policy> policyJoin = intimationRoot.join( "policy",
	 * JoinType.INNER);
	 * 
	 * List<Predicate> predicates = new ArrayList<Predicate>();
	 * 
	 * List<Intimation> resultList = new ArrayList<Intimation>();
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
	 * final TypedQuery<Intimation> intimationquery = entityManager
	 * .createQuery(criteriaQuery); resultList =
	 * intimationquery.getResultList(); } else if (claimNumber != null) { final
	 * CriteriaQuery<Claim> claimCriteriaQuery = builder
	 * .createQuery(Claim.class);
	 * 
	 * Root<Claim> claimRoot = claimCriteriaQuery .from(Claim.class);
	 * Join<Claim, Intimation> claimJoin = claimRoot.join( "intimation",
	 * JoinType.INNER);
	 * 
	 * Predicate claimNumberPredicate = builder.like( claimRoot.<String>
	 * get("claimId"), claimNumber); predicates.add(claimNumberPredicate);
	 * 
	 * if (intimationStatus != null) {
	 * 
	 * Predicate statusPredicate = builder.like( claimRoot.<Intimation>
	 * get("intimation") .<String> get("status"), intimationStatus);
	 * predicates.add(statusPredicate); }
	 * 
	 * if (policyNumber != null) { Predicate policyPredicate = builder.like(
	 * claimRoot.<Intimation> get("intimation") .<Policy> get("policy")
	 * .<String> get("policyNumber"), policyNumber);
	 * predicates.add(policyPredicate); }
	 * 
	 * if (intimationNumber != null) { Predicate intimationNoPredicate =
	 * builder.like( claimRoot.<Intimation> get("intimation") .<String>
	 * get("intimationId"), intimationNumber);
	 * predicates.add(intimationNoPredicate); }
	 * 
	 * if (insuredName != null) { Predicate insuredNamePredicate = builder.like(
	 * claimRoot.<Intimation> get("intimation") .<Policy> get("policy")
	 * .<String> get("insuredFirstName"), insuredName);
	 * predicates.add(insuredNamePredicate); }
	 * 
	 * if (healthCardNumber != null) { Predicate healthCardNumberPredicate =
	 * builder.like( claimRoot.<Intimation> get("intimation") .<Policy>
	 * get("policy") .<String> get("healthCardNumber"), healthCardNumber);
	 * predicates.add(healthCardNumberPredicate); }
	 * 
	 * // if(hospitalName != null) // { // Predicate healthCardNumberPredicate =
	 * // builder.like(hospitalJoin.<String>get("name"), // hospitalName); //
	 * predicates.add(healthCardNumberPredicate); // }
	 * 
	 * if (fromDate != null && toDate != null) { Predicate createdDatePredicate
	 * = builder.between( claimRoot.<Intimation> get("intimation") .<Date>
	 * get("createdDate"), fromDate, toDate);
	 * predicates.add(createdDatePredicate); }
	 * claimCriteriaQuery.select(claimRoot) .where(builder.and(predicates
	 * .toArray(new Predicate[] {})));
	 * 
	 * final TypedQuery<Claim> claimQuery = entityManager
	 * .createQuery(claimCriteriaQuery); List<Claim> claimList = (List<Claim>)
	 * claimQuery .getResultList(); resultList = new ArrayList<Intimation>(); if
	 * (claimList != null && !claimList.isEmpty()) { for (Claim claim :
	 * claimList) { resultList.add(claim.getIntimation()); } } } if (resultList
	 * != null && !resultList.isEmpty()) { for (Intimation a_intimation :
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
	
	public boolean isOPintimation(Long intimationKey){
		
		Claim clmObj = getClaimforIntimation(intimationKey);
		
		if(clmObj != null && (ReferenceTable.OP_STAGE).equals(clmObj.getStage().getKey())){
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Page<NewIntimationDto> searchCombinationforViewAndEditReport(SearchIntimationFormDto searchDto, EntityManager entityManager ) {
		this.entityManager = entityManager;
		return searchCombinationforViewAndEdit(searchDto);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Page<NewIntimationDto> searchCombinationforViewAndEdit(SearchIntimationFormDto searchDto) {
		Map<String, Object> params = searchDto.getFilters();

		Page<NewIntimationDto> pagedIntimationListContainer = new Page<NewIntimationDto>();
		List<Intimation> resultList = new ArrayList<Intimation>();

		Pageable apageable = params.containsKey("pageable") ? (Pageable)params.get("pageable") : null;
		
		int pageNumber = apageable != null ? apageable.getPageNumber() : 1; 
		
		List<NewIntimationDto> intimationDtoList = new ArrayList<NewIntimationDto>();
		
		resultList = searchIntimationByFilters(params,apageable);

				if (resultList != null && !resultList.isEmpty()) {
					Claim clmObj;
					NewIntimationDto newIntimationDto;
					Hospitals hospital = null;
					SelectValue claimType;
					SelectValue hospitalType;
					CityTownVillage city = null;
					State state = null;
					MastersValue admissionType;
					SelectValue admitionTypeMast;
					MastersValue intimatinMode;
					SelectValue intimatoinModeMast;
					for (Intimation a_intimation : resultList) {
						
						clmObj = getClaimforIntimation(a_intimation.getKey());
//						Claim  clmObj = entityManager.find(Claim.class,  a_intimation.getKey());
						
						if(clmObj != null && (ReferenceTable.OP_STAGE).equals(clmObj.getStage().getKey())){
							continue;
						}
						else{
						newIntimationDto = NewIntimationMapper.getInstance()
								.getNewIntimationDto(a_intimation);
						newIntimationDto.setPolicy(a_intimation.getPolicy());
						
						if(ReferenceTable.getGMCProductList().containsKey(newIntimationDto.getPolicy().getProduct().getKey()) ||
								ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(newIntimationDto.getPolicy().getProduct().getKey())){
							
							boolean isjiopolicy = getJioPolicyDetails(newIntimationDto.getPolicy().getPolicyNumber());
							  
							  newIntimationDto.setIsJioPolicy(isjiopolicy);
						      Insured insuredByKey = getInsuredByKey(newIntimationDto.getInsuredPatient().getKey());
						      Insured MainMemberInsured = null;
						      
						      if(insuredByKey.getDependentRiskId() == null){
						    	  MainMemberInsured = insuredByKey;
						      }else{
						    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredNameForDefault(newIntimationDto.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
						    	  MainMemberInsured = insuredByPolicyAndInsuredId;
						      }
						      
						      if(MainMemberInsured != null){
						    	  newIntimationDto.setGmcMainMemberName(MainMemberInsured.getInsuredName());
						    	  newIntimationDto.setGmcMainMemberId(MainMemberInsured.getInsuredId());
						    	  newIntimationDto.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
						    	  if(MainMemberInsured.getInsuredAge() != null){
						    		  newIntimationDto.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
						    	  }
						    	  /**
						    	   * Part of CR R1186
						    	   */
						    	  
						    	  a_intimation.getInsured().setAddress1(MainMemberInsured.getAddress1());
						    	  a_intimation.getInsured().setAddress2(MainMemberInsured.getAddress2());
						    	  a_intimation.getInsured().setAddress3(MainMemberInsured.getAddress3());
						    	  a_intimation.getInsured().setCity(MainMemberInsured.getCity());
						    	  a_intimation.getInsured().setInsuredPinCode(MainMemberInsured.getInsuredPinCode());
						    	  a_intimation.getInsured().setInsuredState(MainMemberInsured.getInsuredState());
						    	  
						      }		
						}

						newIntimationDto.setInsuredPatient(a_intimation.getInsured());
											
						
						//TmpHospital tmpHospital = null;
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
							hospitalType = new SelectValue();
							hospitalType.setId(hospital.getHospitalType()
									.getKey());
							hospitalType.setValue(hospital.getHospitalType()
									.getValue());
							newIntimationDto.setHospitalType(hospitalType);
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());
							
							claimType = getClaimType(hospital.getHospitalType().getKey());
						
							newIntimationDto.setClaimType(claimType);	
							
							newIntimationDto.getHospitalDto().setCity(
									hospital.getCity());
							newIntimationDto.getHospitalDto().setState(
									hospital.getState());
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());

							
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

						/*if (tmpHospital != null) {
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
						}*/
						
						admissionType = a_intimation.getAdmissionType();
						
						admitionTypeMast = new SelectValue();
						
						if(admissionType != null){
							admitionTypeMast.setId(admissionType.getKey());
							admitionTypeMast.setValue(admissionType.getValue());						
						}
						newIntimationDto.setAdmissionType(admitionTypeMast);						

						
						intimatinMode = a_intimation.getIntimationMode();
						intimatoinModeMast = new SelectValue();
						
						if(intimatinMode != null){
							intimatoinModeMast.setId(intimatinMode.getKey());
							intimatoinModeMast.setValue(intimatinMode.getValue());						
						}
									
						newIntimationDto.setModeOfIntimation(intimatoinModeMast);
						
						
						String patientName = "";
						if (a_intimation.getInsuredPatientName() != null) {
							patientName = a_intimation.getInsuredPatientName();
						}
						newIntimationDto.setInsuredPatientName(patientName);
						
						if(null != a_intimation && null != a_intimation.getPolicy() && 
								null != a_intimation.getPolicy().getProduct() && 
								null != a_intimation.getPolicy().getProduct().getKey() &&
								(ReferenceTable.getGPAProducts().containsKey(a_intimation.getPolicy().getProduct().getKey()))){
							
							newIntimationDto.setInsuredPatientName(a_intimation.getPaPatientName());
						}
						
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
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Page<NewIntimationDto> searchCombinationforView(SearchIntimationFormDto searchDto) {
		Map<String, Object> params = searchDto.getFilters();

		Page<NewIntimationDto> pagedIntimationListContainer = new Page<NewIntimationDto>();
		List<Intimation> resultList = new ArrayList<Intimation>();

		Pageable apageable = params.containsKey("pageable") ? (Pageable)params.get("pageable") : null;
		
		int pageNumber = apageable != null ? apageable.getPageNumber() : 1; 
		
		List<NewIntimationDto> intimationDtoList = new ArrayList<NewIntimationDto>();
		
		resultList = searchIntimationByFiltersForClaimWiseReport(params,apageable);

				if (resultList != null && !resultList.isEmpty()) {
					Claim clmObj;
					NewIntimationDto newIntimationDto;
					Hospitals hospital = null;
					SelectValue claimType;
					SelectValue hospitalType;
					CityTownVillage city = null;
					State state = null;
					MastersValue admissionType;
					SelectValue admitionTypeMast;
					MastersValue intimatinMode;
					SelectValue intimatoinModeMast;
					for (Intimation a_intimation : resultList) {
						
						clmObj = getClaimforIntimation(a_intimation.getKey());
//						Claim  clmObj = entityManager.find(Claim.class,  a_intimation.getKey());
						
						if(clmObj != null && (ReferenceTable.OP_STAGE).equals(clmObj.getStage().getKey())){
							continue;
						}
						else{
						newIntimationDto = NewIntimationMapper.getInstance()
								.getNewIntimationDto(a_intimation);
						newIntimationDto.setPolicy(a_intimation.getPolicy());
						newIntimationDto.setInsuredPatient(a_intimation
								.getInsured());
						
						if(ReferenceTable.getGMCProductList().containsKey(newIntimationDto.getPolicy().getProduct().getKey()) ||
								ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(newIntimationDto.getPolicy().getProduct().getKey())){
							
							boolean isjiopolicy = getJioPolicyDetails(newIntimationDto.getPolicy().getPolicyNumber());
							  
							  newIntimationDto.setIsJioPolicy(isjiopolicy);
						      Insured insuredByKey = getInsuredByKey(newIntimationDto.getInsuredPatient().getKey());
						      Insured MainMemberInsured = null;
						      
						      if(insuredByKey.getDependentRiskId() == null){
						    	  MainMemberInsured = insuredByKey;
						      }else{
						    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredId(newIntimationDto.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB);
						    	  MainMemberInsured = insuredByPolicyAndInsuredId;
						      }
						      
						      if(MainMemberInsured != null){
						    	  newIntimationDto.setGmcMainMemberName(MainMemberInsured.getInsuredName());
						    	  newIntimationDto.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
						    	  if(MainMemberInsured.getInsuredAge() != null){
						    		  newIntimationDto.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
						    	  }
						      }		
						}
											
						
						//TmpHospital tmpHospital = null;
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
							hospitalType = new SelectValue();
							hospitalType.setId(hospital.getHospitalType()
									.getKey());
							hospitalType.setValue(hospital.getHospitalType()
									.getValue());
							newIntimationDto.setHospitalType(hospitalType);
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());
							
							claimType = getClaimType(hospital.getHospitalType().getKey());
						
							newIntimationDto.setClaimType(claimType);	
							
							newIntimationDto.getHospitalDto().setCity(
									hospital.getCity());
							newIntimationDto.getHospitalDto().setState(
									hospital.getState());
							newIntimationDto.setHospitalTypeValue(hospital
									.getHospitalType().getValue());

							
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

						/*if (tmpHospital != null) {
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
						}*/
						
						admissionType = a_intimation.getAdmissionType();
						
						admitionTypeMast = new SelectValue();
						
						if(admissionType != null){
							admitionTypeMast.setId(admissionType.getKey());
							admitionTypeMast.setValue(admissionType.getValue());						
						}
						newIntimationDto.setAdmissionType(admitionTypeMast);						

						
						intimatinMode = a_intimation.getIntimationMode();
						intimatoinModeMast = new SelectValue();
						
						if(intimatinMode != null){
							intimatoinModeMast.setId(intimatinMode.getKey());
							intimatoinModeMast.setValue(intimatinMode.getValue());						
						}
									
						newIntimationDto.setModeOfIntimation(intimatoinModeMast);
						
						
						String patientName = "";
						if (a_intimation.getInsuredPatientName() != null) {
							patientName = a_intimation.getInsuredPatientName();
						}
						newIntimationDto.setInsuredPatientName(patientName);
						
						if(null != a_intimation && null != a_intimation.getPolicy() && 
								null != a_intimation.getPolicy().getProduct() && 
								null != a_intimation.getPolicy().getProduct().getKey() &&
								(ReferenceTable.GPA_PRODUCT_KEY.equals(a_intimation.getPolicy().getProduct().getKey()))){
							
							newIntimationDto.setInsuredPatientName(a_intimation.getPaPatientName());
						}
						
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
	
	//@SuppressWarnings({ "unchecked", "unused" })
	public List<Intimation> searchClaimDocumentsForView(Long policyKey) {
		
		getIntimationByPolicyKey(policyKey);
		return null;
	}
	
	
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			entityManager.refresh(hospitals);
			return hospitals;
		}
		return null;
	}
	
	public List<Intimation> searchIntimationByFilters(Map<String, Object> params,Pageable apageable,EntityManager em){
		this.entityManager = em;
		return searchIntimationByFiltersForClaimWiseReport(params,apageable);
	}
	
	private List<Intimation> searchIntimationByFilters(Map<String, Object> params,Pageable apageable){
	
		List<Intimation> resultList = new ArrayList<Intimation>();
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
			/*intimationNumber = params.containsKey("intimationNumber") ? "%"
					+ StringUtils.trim(params.get("intimationNumber")
							.toString()) + "%" : null;*/
			intimationNumber = params.containsKey("intimationNumber") ?
					StringUtils.trim(params.get("intimationNumber")
							.toString()) : null;
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
			
			intimationYear = params.containsKey("intimationYear") ?  StringUtils.trim(params.get("intimationYear").toString())
					: null;
			
			/*intimationSeqNumber = params.containsKey("intimationSequenceNumber") ?  StringUtils.trim(params.get("intimationSequenceNumber").toString())
					: null;*/
			
			List<Long> hospitalIdList = new ArrayList<Long>();

			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
				/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				Join<Intimation, Insured> insuredJoin = intimationRoot.join(
						"insured", JoinType.INNER);*/

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
						/*if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									intimationRoot.<Status> get("status")
											.<String> get("processValue"),
									intimationStatus);
							predicates.add(statusPredicate);
						}*/

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									intimationRoot.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							/*Predicate intimationNoPredicate = builder
									.like(intimationRoot
											.<String> get("intimationId"),
											intimationNumber);*/
							Predicate intimationNoPredicate = null;
							if(StringUtils.isNumeric(intimationNumber)){
								if(intimationYear != null){
									intimationNumber = params.containsKey("intimationNumber") ?  "C%/"+intimationYear+"/%"
											+ StringUtils.trim(params.get("intimationNumber")
												.toString())  : null;
									intimationNoPredicate = builder
										.like(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
								} else {
									intimationNoPredicate =  builder
											.equal(intimationRoot
													.<String> get("intimationId"),
													intimationNumber);
								}
							}else {
								intimationNoPredicate =  builder
										.equal(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
							}
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
						
						if(intimationYear != null){
							/*Predicate intimationNoPredicate = builder
									.like(intimationRoot
											.<String> get("intimationId"),
											"%/"+intimationYear+"/%");*/
							Predicate intimationNoPredicate= builder
									.equal(intimationRoot
										.<Long> get("intimationYear"),intimationYear);
							predicates.add(intimationNoPredicate);
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

						final TypedQuery<Intimation> intimationquery = entityManager
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
						final CriteriaQuery<Claim> claimCriteriaQuery = builder
								.createQuery(Claim.class);

						Root<Claim> claimRoot = claimCriteriaQuery
								.from(Claim.class);
						/*Join<Claim, Intimation> claimJoin = claimRoot.join(
								"intimation", JoinType.INNER);*/

						Predicate claimNumberPredicate = builder.like(
								claimRoot.<String> get("claimId"), claimNumber);
						predicates.add(claimNumberPredicate);

						/*if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Status> get("status")
											.<String> get("processValue"),
									intimationStatus);
							predicates.add(statusPredicate);
						}*/

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							/*Predicate intimationNoPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<String> get("intimationId"),
									intimationNumber);*/
							Predicate intimationNoPredicate = null;
							if(StringUtils.isNumeric(intimationNumber)){
								if(intimationYear != null){
									intimationNumber = params.containsKey("intimationNumber") ?  "C%/"+intimationYear+"/%"
											+ StringUtils.trim(params.get("intimationNumber")
												.toString())  : null;
									intimationNoPredicate = builder
										.like(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
								} else {
									intimationNoPredicate =  builder
											.equal(intimationRoot
													.<String> get("intimationId"),
													intimationNumber);
								}
							}else {
								intimationNoPredicate =  builder
										.equal(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
							}
							predicates.add(intimationNoPredicate);
						}
						
						if(intimationYear != null){
							/*Predicate intimationNoPredicate = builder
									.like(claimRoot
											.<String> get("claimId"),
											"%/"+intimationYear+"/%");*/
							Predicate intimationNoPredicate= builder
									.equal(intimationRoot
										.<Long> get("intimationYear"),intimationYear);
							predicates.add(intimationNoPredicate);
						}
						if (insuredName != null) {
							Predicate insuredNamePredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Insured> get("insured")
											.<String> get("insuredName"),
									insuredName);
							predicates.add(insuredNamePredicate);
						}

						if (healthCardNumber != null) {
							Predicate healthCardNumberPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("healthCardNumber"),
									healthCardNumber);
							predicates.add(healthCardNumberPredicate);
						}

						if (fromDate != null && toDate != null) {
							Predicate createdDatePredicate = builder.between(
									claimRoot.<Intimation> get("intimation")
											.<Date> get("createdDate"),
									fromDate, toDate);
							predicates.add(createdDatePredicate);
						}

						if (!hospitalIdList.isEmpty()) {
							Predicate hospitalPredicate = claimRoot
									.<Intimation> get("intimation")
									.<Long> get("hospital").in(hospitalIdList);
							predicates.add(hospitalPredicate);
						}

						claimCriteriaQuery.select(claimRoot).where(
								builder.and(predicates
										.toArray(new Predicate[] {})));

						final TypedQuery<Claim> claimQuery = entityManager
								.createQuery(claimCriteriaQuery);
						List<Claim> claimList = new ArrayList<Claim>();
						
						
						if(apageable ==null){
							
							claimList = (List<Claim>) claimQuery
									.getResultList();
						}	
						
						else if (apageable != null){
							
							if(claimQuery.getResultList() != null && !claimQuery.getResultList().isEmpty() && claimQuery.getResultList().size() < 10){
									claimList = (List<Claim>) claimQuery.getResultList();
							}
							else if(claimQuery.getResultList() != null && claimQuery.getResultList().size() >= 10){
								claimList = claimQuery.setFirstResult(firstResult).setMaxResults(10).getResultList();
							}
						}
						
						resultList = new ArrayList<Intimation>();
						
						if (claimList != null && !claimList.isEmpty()) {
							for (Claim claim : claimList) {
								Intimation IntimationByNo = getIntimationByNo(claim.getIntimation().getIntimationId());
								resultList.add(IntimationByNo);
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
	
	private List<Intimation> searchIntimationByFiltersForClaimWiseReport(Map<String, Object> params,Pageable apageable){
		
		List<Intimation> resultList = new ArrayList<Intimation>();
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
			String intmNo;

			intimationStatus = params.containsKey("intimationStatus") ? StringUtils
					.trim(params.get("intimationStatus").toString()) : null;

			/*Below Code For IMSSUPPOR-28837 - Qry Tn*/
			intmNo = params.containsKey("intimationNumber") ?
					StringUtils.trim(params.get("intimationNumber")
						.toString()) : null;
					if(intmNo != null && intmNo.contains("CIR")  || intmNo != null && intmNo.contains("CIG") || intmNo != null && intmNo.contains("CLI")){
						intimationNumber = params.containsKey("intimationNumber") ?
								StringUtils.trim(params.get("intimationNumber")
									.toString()) : null;
					}
					else {
						/*intimationNumber = params.containsKey("intimationNumber") ?  "%"
							+ StringUtils.trim(params.get("intimationNumber")
								.toString()) + "%" : null;*/
						intimationNumber = params.containsKey("intimationNumber") ?
								StringUtils.trim(params.get("intimationNumber")
									.toString()) : null;
					}
					//commented since we are facing issue if intimation is searched only through six digits means done by noufel/narasima
//			if(intmNo != null && intmNo.contains("CIR")  || intmNo != null && intmNo.contains("CIG")){
//				intimationNumber = params.containsKey("intimationNumber") ?
//						StringUtils.trim(params.get("intimationNumber")
//								.toString()) : null;
//			}

			/*END Code For IMSSUPPOR-28837 - Qry Tn*/
					
			/*intimationNumber = params.containsKey("intimationNumber") ? "%"
					+ StringUtils.trim(params.get("intimationNumber")
							.toString()) + "%" : null;*/
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
			
			intimationYear = params.containsKey("intimationYear") ?  StringUtils.trim(params.get("intimationYear").toString())
					: null;
			
			intimationSeqNumber = params.containsKey("intimationSequenceNumber") ?  StringUtils.trim(params.get("intimationSequenceNumber").toString())
					: null;
			
			List<Long> hospitalIdList = new ArrayList<Long>();

			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
				
				//As per Rajesh-Sathish Sir suggestion commenting below code for performance issue - 06-Aug-2019
				/*Join<Intimation, Status> statusJoin = intimationRoot.join(
						"status", JoinType.INNER);*/
				
				/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				Join<Intimation, Insured> insuredJoin = intimationRoot.join(
						"insured", JoinType.INNER);*/

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
						//As per Rajesh-Sathish Sir suggestion commenting below code for performance issue - 06-Aug-2019
						/*if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									statusJoin.<String> get("processValue"),
									intimationStatus);
							Predicate statusPredicate1 = builder.like(
									statusJoin.<String> get("processValue"),
									SHAConstants.CLAIM_WISE_APPROVAL);
							Predicate condition18 = builder.or(statusPredicate,statusPredicate1);
							predicates.add(condition18);
							
						}*/

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									intimationRoot.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							/*Below Code For IMSSUPPOR-28837 - Qry Tn*/
							Predicate intimationNoPredicate = null;
//							String[]  splitIntm = intimationNumber.split("/");
							if(StringUtils.isNumeric(intimationNumber)){
								if(intimationYear != null){
									intimationNumber = params.containsKey("intimationNumber") ?  "C%/"+intimationYear+"/%"
											+ StringUtils.trim(params.get("intimationNumber")
												.toString())  : null;
									intimationNoPredicate = builder
										.like(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
								} else {
									intimationNoPredicate =  builder
											.equal(intimationRoot
													.<String> get("intimationId"),
													intimationNumber);
								}
							} else {
								intimationNoPredicate = intimationNoPredicate = builder
										.equal(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
							}
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
						
						if( intimationYear != null){
							/*Predicate intimationNoPredicate = builder
									.like(intimationRoot
											.<String> get("intimationId"),
											"%/"+intimationYear+"/%");*/
							/*Below Code For IMSSUPPOR-28837 - Qry Tn*/
//							Expression<String> intimationYr = intimationRoot.<String>get("intimationId");
//							Expression<String> year = builder.substring(intimationYr,5,4);
//							Expression<String> tataClaimYear = builder.substring(intimationYr,10,4);
//							Predicate claimYr = builder.equal(year,intimationYear);
//							Predicate tataYr = builder.equal(tataClaimYear,intimationYear);
//							Predicate intimationNoPredicate = builder.or(claimYr,tataYr);
//							predicates.add(intimationNoPredicate);
							//added for search slowness performance issue on 08-02-2020
							Predicate intimationNoPredicate= builder
									.equal(intimationRoot
										.<Long> get("intimationYear"),intimationYear);
										predicates.add(intimationNoPredicate);
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

						final TypedQuery<Intimation> intimationquery = entityManager
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
						final CriteriaQuery<Claim> claimCriteriaQuery = builder
								.createQuery(Claim.class);

						Root<Claim> claimRoot = claimCriteriaQuery
								.from(Claim.class);
						/*Join<Claim, Intimation> claimJoin = claimRoot.join(
								"intimation", JoinType.INNER);*/
						Join<Claim, Status> claimStatusJoin = claimRoot.join(
								"status", JoinType.INNER);

						Predicate claimNumberPredicate = builder.like(
								claimRoot.<String> get("claimId"), claimNumber);
						predicates.add(claimNumberPredicate);

						/*if (intimationStatus != null) {

							Predicate statusPredicate = builder.like(
									claimStatusJoin.<String> get("processValue"),
									intimationStatus);
							predicates.add(statusPredicate);
						}*/

						if (policyNumber != null) {
							Predicate policyPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("policyNumber"),
									policyNumber);
							predicates.add(policyPredicate);
						}

						if (intimationNumber != null) {
							/*Below Code For IMSSUPPOR-28837 - Qry Tn*/
							Predicate intimationNoPredicate = null;
//							String[]  splitIntm = intimationNumber.split("/");
							if(StringUtils.isNumeric(intimationNumber)){
								if(intimationYear != null){
									intimationNumber = params.containsKey("intimationNumber") ?  "C%/"+intimationYear+"/%"
											+ StringUtils.trim(params.get("intimationNumber")
												.toString())  : null;
									intimationNoPredicate = builder
										.like(intimationRoot
												.<String> get("intimationId"),
												intimationNumber);
								} else {
									intimationNoPredicate =  builder
											.equal(intimationRoot
													.<String> get("intimationId"),
													intimationNumber);
								}
							} else {
								intimationNoPredicate = builder
										.equal(claimRoot.<Intimation> get("intimation")
												.<String> get("intimationId"),
												intimationNumber);
							}
							predicates.add(intimationNoPredicate);
						}
						
						if(intimationYear != null){
							/*Predicate intimationNoPredicate = builder
									.like(claimRoot
											.<String> get("claimId"),
											"%/"+intimationYear+"/%");*/
							/*Below Code For IMSSUPPOR-28837 - Qry Tn*/
//							Expression<String> intimationYr = claimRoot.<String>get("claimId");
//							Expression<String> year = builder.substring(intimationYr,6,4);
//							Predicate intimationNoPredicate = builder.equal(year,intimationYear);
//							predicates.add(intimationNoPredicate);
							//added for search slowness performance issue on 08-02-2020
							Predicate intimationNoPredicate = builder.equal(
									claimRoot.<Intimation> get("intimation")
										.<Long> get("intimationYear"),intimationYear);
											predicates.add(intimationNoPredicate);
						}


						if (insuredName != null) {
							Predicate insuredNamePredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Insured> get("insured")
											.<String> get("insuredName"),
									insuredName);
							predicates.add(insuredNamePredicate);
						}

						if (healthCardNumber != null) {
							Predicate healthCardNumberPredicate = builder.like(
									claimRoot.<Intimation> get("intimation")
											.<Policy> get("policy")
											.<String> get("healthCardNumber"),
									healthCardNumber);
							predicates.add(healthCardNumberPredicate);
						}

						if (fromDate != null && toDate != null) {
							Predicate createdDatePredicate = builder.between(
									claimRoot.<Intimation> get("intimation")
											.<Date> get("createdDate"),
									fromDate, toDate);
							predicates.add(createdDatePredicate);
						}

						if (!hospitalIdList.isEmpty()) {
							Predicate hospitalPredicate = claimRoot
									.<Intimation> get("intimation")
									.<Long> get("hospital").in(hospitalIdList);
							predicates.add(hospitalPredicate);
						}

						claimCriteriaQuery.select(claimRoot).where(
								builder.and(predicates
										.toArray(new Predicate[] {})));

						final TypedQuery<Claim> claimQuery = entityManager
								.createQuery(claimCriteriaQuery);
						List<Claim> claimList = new ArrayList<Claim>();
						
						
						if(apageable ==null){
							
							claimList = (List<Claim>) claimQuery
									.getResultList();
						}	
						
						else if (apageable != null){
							
							if(claimQuery.getResultList() != null && !claimQuery.getResultList().isEmpty() && claimQuery.getResultList().size() < 10){
									claimList = (List<Claim>) claimQuery.getResultList();
							}
							else if(claimQuery.getResultList() != null && claimQuery.getResultList().size() >= 10){
								claimList = claimQuery.setFirstResult(firstResult).setMaxResults(10).getResultList();
							}
						}
						
						resultList = new ArrayList<Intimation>();
						
						if (claimList != null && !claimList.isEmpty()) {
							for (Claim claim : claimList) {
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
		
	
private List<Intimation> searchIntimationByFiltersForClaimDocuments(Map<String, Object> params,Pageable apageable){
		
		List<Intimation> resultList = new ArrayList<Intimation>();
		if (!params.isEmpty()) {
			String intimationNumber;
			String policyNumber;
			String intmNo;

			intmNo = params.containsKey("intimationNumber") ?
					StringUtils.trim(params.get("intimationNumber")
						.toString()) : null;
					if(intmNo != null && intmNo.contains("CIR")  || intmNo != null && intmNo.contains("CIG") || intmNo != null && intmNo.contains("CLI")){
						intimationNumber = params.containsKey("intimationNumber") ?
								StringUtils.trim(params.get("intimationNumber")
									.toString()) : null;
					}
					else {
						intimationNumber = params.containsKey("intimationNumber") ?  "%"
							+ StringUtils.trim(params.get("intimationNumber")
								.toString()) + "%" : null;
					}
					//commented since we are facing issue if intimation is searched only through six digits means done by noufel/narasima
//			if(intmNo != null && intmNo.contains("CIR")  || intmNo != null && intmNo.contains("CIG")){
//				intimationNumber = params.containsKey("intimationNumber") ?
//						StringUtils.trim(params.get("intimationNumber")
//								.toString()) : null;
//			}
			
			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
			policyNumber = params.containsKey("policyNumber") ? "%"
					+ StringUtils.trim(params.get("policyNumber").toString())
					+ "%" : null;
			
					final TypedQuery<Intimation> intimationquery = entityManager
					.createQuery(criteriaQuery);
						resultList = new ArrayList<Intimation>();
						
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
	public Intimation searchIntimationByParams(Map<String, Object> params) {

		Intimation intimationResult = new Intimation();

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
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
				/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);*/
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

				final TypedQuery<Intimation> query = entityManager
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
	public void create(Policy policy, Intimation a_intimation) {

		if (a_intimation != null) {
			entityManager.persist(policy);
			a_intimation.setPolicy(policy);
			entityManager.persist(a_intimation);
		}
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRED) public void
	 * submit(IntimationDTO intimationDTO, Intimation intimation) { //
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
	public void submitIntimation(NewIntimationDto newIntimationDto, GalaxyIntimation intimation, Long statusKey) {
		Stage intiamtionStage = entityManager.find(Stage.class, ReferenceTable.INTIMATION_STAGE_KEY);
		intimation.setStage(intiamtionStage);
		
		Status intimationStatus = null;
		intimationStatus = entityManager.find(Status.class, statusKey);
		intimation.setStatus(intimationStatus);
		
		Boolean premiaIntimation = isPremiaIntimation(newIntimationDto.getPolicy().getPolicyNumber());
		if(premiaIntimation != null && premiaIntimation){
			intimation.setApplicationFlag("P");
		}else{
			intimation.setApplicationFlag("G");
		}
		
		MastersValue intimationSource = entityManager.find(MastersValue.class, ReferenceTable.CALL_CENTRE_SOURCE);
		intimation.setIntimationSource(intimationSource);
		intimation.setHospitalReqFlag(("true".equals(intimation.getHospitalReqFlag()) ? "Y" : "N"));
		intimation.setDummy(("Y".equals(intimation.getDummy()) ? "Y" : "N"));
		
		if(intimation.getIncidenceFlag() != null) {
			intimation.setIncidenceFlag(intimation.getIncidenceFlag().charAt(0) + "");	
			if(intimation.getIncidenceFlag().equalsIgnoreCase("A")){
				intimation.setIncidenceFlag("Y");
			}else{
				intimation.setIncidenceFlag("N");
			}
		}
		
		
		
		if((intimation.getIncidenceFlag() == null) || (intimation.getIncidenceFlag() != null && intimation.getIncidenceFlag().isEmpty())){
			intimation.setProcessClaimType("H");
			intimation.setPaCategory("H");
		}else{
			intimation.setProcessClaimType("P");
			intimation.setPaCategory("P");
		}
		//persisting Student patient name as paPatientName for intimations created by galaxy
		if(ReferenceTable.getGPAProducts().containsKey(newIntimationDto.getPolicy().getProduct().getKey())){
			if(null != newIntimationDto.getStudentPatientName()) {
			intimation.setPaPatientName(newIntimationDto.getStudentPatientName());
		}
		}
		
		if(intimation.getKey() == null) {
			intimation.setRegistrationType("MANUAL");
			intimation.setRegistrationStatus("REGISTERED");
			intimation.setActiveStatus("1");
			if(newIntimationDto.getUsername() != null)
			intimation.setCreatedBy(SHAUtils.getUserNameForDB(newIntimationDto.getUsername().toUpperCase()));
			intimation.setCreatedDate(new Date());
			entityManager.persist(intimation);			
		} else {
			if(newIntimationDto.getUsername() != null)
			intimation.setModifiedBy(SHAUtils.getUserNameForDB(newIntimationDto.getUsername().toUpperCase()));
			intimation.setModifiedDate(new Date());
			entityManager.merge(intimation);
		}
		entityManager.flush();
		entityManager.refresh(intimation);
		newIntimationDto.setKey(intimation.getKey() != null ? intimation.getKey().longValue() : null);
		
		
		/***
		 * update dummy hospital to pms
		 */
		
		if(intimation.getDummy() != null && intimation.getDummy().equals("Y")){
			insertDummyHospitalToPMS(newIntimationDto, intimation);
		}
		
	}

	/*
	 * public IntimationDTO read(Long key) { Long tmpKey = new Long(key);
	 * Intimation intimation = entityManager.find(Intimation.class, tmpKey);
	 * return new IntimationMapper().getIntimationDTO(intimation); }
	 */
	public NewIntimationDto readNewIntimation(Long key) {
		Long tmpKey = new Long(key);
		Intimation intimation = entityManager.find(Intimation.class, tmpKey);
		NewIntimationDto intimatinDto = NewIntimationMapper.getInstance()
				.getNewIntimationDto(intimation);
		intimatinDto.setPolicy(intimation.getPolicy());
		return intimatinDto;
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRED) public void
	 * save(IntimationDTO intimationDTO, Intimation intimation) { //
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

	public void saveIntimation(NewIntimationDto newIntimationDto, GalaxyIntimation intimation) {
		Status intimationStatus = entityManager.find(Status.class, ReferenceTable.INTIMATION_SAVE_STATUS_KEY);
		intimation.setStatus(intimationStatus);

		Stage intiamtionStage = entityManager.find(Stage.class, ReferenceTable.INTIMATION_STAGE_KEY);
		intimation.setStage(intiamtionStage);

		MastersValue intimationSource = entityManager.find(MastersValue.class, 131l);
		intimation.setIntimationSource(intimationSource);

		if(newIntimationDto != null) {
			if(intimation.getKey() == null) {
				intimation.setHospitalReqFlag(("true".equals(intimation.getHospitalReqFlag()) ? "Y" : "N"));
				intimation.setDummy(("true".equals(intimation.getDummy()) ? "Y" : "N"));
				intimation.setActiveStatus("1");
//				intimation.setCreatedBy(SHAUtils.getUserNameForDB(newIntimationDto.getUsername().toUpperCase()));
				intimation.setCreatedDate(new Date());
				entityManager.persist(intimation);
			} else {
//				intimation.setModifiedBy(SHAUtils.getUserNameForDB(newIntimationDto.getUsername().toUpperCase()));
				intimation.setModifiedDate(new Date());
				entityManager.merge(intimation);				
			}
			entityManager.flush();
			newIntimationDto.setKey(intimation.getKey() != null ? intimation.getKey().longValue() : null);
		}
		
	}
	
	public void insertDummyHospitalToPMS(NewIntimationDto newIntimationDto, GalaxyIntimation intimation){
		InsertHospitalToPMS hospitalDto = new InsertHospitalToPMS();
		if(newIntimationDto.getHospitalDto() != null && newIntimationDto.getHospitalDto().getNotRegisteredHospitals() != null){
			hospitalDto.setHospName(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getHospitalName());
			hospitalDto.setHospitalAddress(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getAddress());
			hospitalDto.setHospitalCity(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getCity());
			if(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getState() != null){
				hospitalDto.setHospitalState(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getState());
			}
			//hospitalDto.setHospitalState(newIntimationDto.get
			hospitalDto.setHospitalTelephoneNo(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getContactNumber());
			hospitalDto.setHospitalFaxNo(newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getFaxNumber() != null ? newIntimationDto.getHospitalDto().getNotRegisteredHospitals().getFaxNumber().toString() : "");
			hospitalDto.setHospitalIntimationId(intimation.getKey().toString());
			hospitalDto.setHospitalIntimationNo(intimation.getIntimationId());
			hospitalDto.setHospitalFlagYn("0");
			hospitalDto.setCreatedBy(newIntimationDto.getUsername());
			
			//hospitalDto.setCreatedOn(SHAUtils.formatIntimationDateForEdit(new Date()));
			String format = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
			hospitalDto.setCreatedOn(format);
			PremiaService.insertNonNetworkHospital(hospitalDto);
		}
	}

	// private void setMasterValueToPolicy(IntimationDTO intimationDTO,
	// Intimation intimation) {
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
	public void create(Intimation a_intimation) {

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

	public Intimation searchByKey(Long a_key) {
		Intimation find = entityManager.find(Intimation.class, a_key);
		entityManager.refresh(find);
		
		
		
		return find;
	}
	
	public GalaxyIntimation galaxyIntimationsearchByKey(Long a_key) {
		GalaxyIntimation find = entityManager.find(GalaxyIntimation.class, a_key);
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

	public Claim getClaimforIntimation(Long intimationKey) {
		Claim a_claim = null;
		if (intimationKey != null) {
			
			Query findByIntimationKey = entityManager.createNamedQuery(
					"Claim.findByIntimationKey").setParameter("intimationKey",
					intimationKey);

			try {
				List<Claim> claimList = (List<Claim>) findByIntimationKey.getResultList();
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
	public List<Intimation> getClaimforIntimationWithClaimNo(
			Long intimationKey, String claimNumber) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		List<Intimation> a_intimationList;
		if (intimationKey != null) {
			Query findByIntimationKeyAndClaimNumber = entityManager
					.createNamedQuery("Claim.findByIntimationKeyAndClaimNumber")
					.setParameter("intimationKey", intimationKey);
			findByIntimationKeyAndClaimNumber.setParameter("claimNumber",
					claimNumber);
			try {
				a_claimList = (List<Claim>) findByIntimationKeyAndClaimNumber
						.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Query findByClaimNumber = entityManager
					.createNamedQuery("Claim.findByClaimNumber");
			findByClaimNumber.setParameter("claimNumber", claimNumber);
			try {
				a_claimList = (List<Claim>) findByClaimNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// intimationKey null
		}
		a_intimationList = new ArrayList<Intimation>();
		if (a_claimList != null && !a_claimList.isEmpty()) {
			for (Claim claim : a_claimList) {
				a_intimationList.add((Intimation) entityManager.find(
						Intimation.class, claim.getIntimation().getKey()));
			}
		}
		if (!a_intimationList.isEmpty()) {
			return a_intimationList;
		}

		return null;
	}

	public List<Intimation> findDuplicateInitmation(Date admissionDate1, Long hospitalKey,String policyNumber,String insuredId) {
		String admissionDate = new SimpleDateFormat("dd-MM-yyyy").format(admissionDate1);

		/*Query query = entityManager
				.createNamedQuery("GalaxyIntimation.findDuplicateInitmation");*/
		Query query = entityManager
				.createNamedQuery("Intimation.findDuplicateInitmation");
		query = query.setParameter("hospital", hospitalKey);
		query = query.setParameter("policy", policyNumber);
		query = query.setParameter("admissionDate",admissionDate);
		query = query.setParameter("insuredId", Long.valueOf(insuredId));
		List<Intimation> insuredList = query.getResultList();

		return insuredList;
	}
	
	public PremiaPreviousClaim findPremiaIntimationDuplicate(Date admissionDate1, String hospitalCode,String policyNumber,String insuredId) {
		String admissionDate = new SimpleDateFormat("dd/MM/yyyy").format(admissionDate1);

		Query query = entityManager
				.createNamedQuery("PremiaPreviousClaim.findForDuplicate");
		query = query.setParameter("hospitalCode", hospitalCode.toLowerCase());
		query = query.setParameter("policyNumber", policyNumber);
		query = query.setParameter("admittedDate",admissionDate);
		query = query.setParameter("riskId", Long.valueOf(insuredId));
		List<PremiaPreviousClaim> insuredList = (List<PremiaPreviousClaim>)query.getResultList();
		if(insuredList != null && ! insuredList.isEmpty()){
			return insuredList.get(0);
		}

		return null;
	}
	
	

	public Intimation searchbyIntimationNo(String intimationNo) {
		Intimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"Intimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<Intimation> intimationQueryResultList = (List<Intimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (Intimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
					
					intimation.getPolicy().setInsured(getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public OPIntimation searchbyOPIntimationNo(String intimationNo) {
		OPIntimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"OPIntimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<OPIntimation> intimationQueryResultList = (List<OPIntimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (OPIntimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
					
					intimation.getPolicy().setInsured(getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public GalaxyIntimation searchbyGalaxyIntimationNo(String intimationNo) {
		GalaxyIntimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"GalaxyIntimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<GalaxyIntimation> intimationQueryResultList = (List<GalaxyIntimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (GalaxyIntimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
					
					intimation.getPolicy().setInsured(getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber()));
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
	
	public ViewTmpOPIntimation searchbyOPIntimationNoFromViewIntimation(String intimationNo) {
		ViewTmpOPIntimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"ViewTmpOPIntimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<ViewTmpOPIntimation> intimationQueryResultList = (List<ViewTmpOPIntimation>) findByIntimNo
						.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (ViewTmpOPIntimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public  Intimation getNotRegisteredIntimation(String intimationNo){
		 Intimation intimation = searchbyIntimationNo(intimationNo);
		 
		 try{
			 if(intimation != null){
					Query claimByIntimationQuery = entityManager.createNamedQuery(
							"Claim.findByIntimationKey").setParameter(
									"intimationKey", intimation.getKey());
					List<Claim> claimList = claimByIntimationQuery.getResultList();
					
					if(claimList != null && !claimList.isEmpty()){
						//intimation=null;
					}				
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			
			return intimation;
		 
	}

	public Intimation searchbyIntimationKey(Long intimationKey) {
		Intimation intimation = null;

		Query findByIntimNo = entityManager.createNamedQuery(
				"Intimation.findByKey").setParameter("intiationKey",
				intimationKey);
		try {
			intimation = (Intimation) findByIntimNo.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return intimation;
	}

	@SuppressWarnings("unchecked")
	public TmpCPUCode getTmpCpuCode(Long cpuKey) {
		return entityManager.find(TmpCPUCode.class, cpuKey);
	}

	public Page<SearchClaimRegistrationTableDto> getIntimationToRegister(
			SearchClaimRegisterationFormDto regQf) {

		List<SearchClaimRegistrationTableDto> resultList = new ArrayList<SearchClaimRegistrationTableDto>();
		
//		ClaimRegTask registrationTask = BPMClientContext.getManualRegisterClaimTask(regQf.getUserId(),regQf.getPassword());
		
//		ClaimRegROTask manualRegisterClaimTaskForRO = BPMClientContext.getManualRegisterClaimTaskForRO(regQf.getUserId(), regQf.getPassword());
		
		List<Map<String, Object>> taskProcedure = null ;
		
		String strIntimationNo = "";
		String strPolicyNo = "";
		
		String priority = regQf.getPriority() != null ? regQf.getPriority().getValue() != null ? regQf.getPriority().getValue() : null : null;
		String source = regQf.getSource() != null ? regQf.getSource().getValue() != null ? regQf.getSource().getValue(): null : null;
		String type = regQf.getType() != null ? regQf.getType().getValue() != null ? regQf.getType().getValue(): null : null;
		
		String hospType = regQf.getHospitalType() != null && regQf.getHospitalType().getValue() != null ? (("network").equalsIgnoreCase(regQf.getHospitalType().getValue()) ? SHAConstants.NETWORK_HOSPITAL_TYPE : SHAConstants.NON_NETWORK_HOSPITAL_TYPE ) : null;
		String accDeath = regQf.getAccDeath() != null ? regQf.getAccDeath().getValue() : null ;
		char[] accType = accDeath != null ? accDeath.toCharArray() : new char[1];
		
		String lob = regQf.getLob(); 
	    String lobType = regQf.getLobType();			
		
//		GetHumanTasks instance = new GetHumanTasks();

//		RegistrationQF qf = null;
		
		//int pageNumber = regQf.getPageable().getPageNumber();
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		List<String> keys = new ArrayList<String>(); 
		
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.MANUAL_REGISTRATION_CURRENT_QUEUE);
		
		mapValues.put(SHAConstants.USER_ID, regQf.getUserId());
		
		if(null !=lob && (SHAConstants.PA_LOB).equals(lob))
		{
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		}
		else
		{
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
		}
		//mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
		
		
		//Pageable apage = regQf.getPageable();
		//PayloadBOType payloadBO = null;

	/*	PayloadBOType payloadBO = new PayloadBOType();*/

//		if(regQf.getHumanTask() != null){
//			payloadBO = regQf.getHumanTask().getPayloadCashless();	
//		}
//		else{
//			payloadBO = new PayloadBOType(); 
//		}
		
		if (regQf != null && (regQf.getHospitalType() != null || regQf.getIntimatedDate() != null || 

				regQf.getIntimationNumber() != null)){
			
			/*if(payloadBO == null){
				payloadBO = new PayloadBOType();
			}
				*/		
//			PolicyType policyInfo = new PolicyType();
		
			if(regQf.getPolicyNumber() != null){
//				policyInfo.setPolicyId(regQf.getPolicyNumber());
//				payloadBO.setPolicy(policyInfo);
				
				strPolicyNo= regQf.getPolicyNumber();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
			}

			if(regQf.getIntimationNumber() != null || regQf.getPolicyNumber() != null || regQf.getCpucode() != null || regQf.getAccDeath() != null ){
					
						
//			PolicyType policyInfo = new PolicyType();
//		
//			if(regQf.getPolicyNumber() != null && !regQf.getPolicyNumber().isEmpty()){
//				policyInfo.setPolicyId(regQf.getPolicyNumber());
//				payloadBO.setPolicy(policyInfo);
//
//				
//			}			

			
			if(regQf.getIntimationNumber() != null && !regQf.getIntimationNumber().isEmpty()){
				
//				intimationType.setIntimationNumber(regQf.getIntimationNumber());
//				payloadBO.setIntimation(intimationType);
				
				strIntimationNo = regQf.getIntimationNumber();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				
			}

			if(regQf.getIntimatedDate() != null){
				
//				String intimDate = SHAUtils.formatIntimationDateValue(regQf.getIntimatedDate());
//				intimationType.setIntDate(new Date(intimDate));
//				payloadBO.setIntimation(intimationType);
				
				
				mapValues.put(SHAConstants.INTIMATION_DATE, regQf.getIntimatedDate());
			}

		//	HospitalInfoType hospitalInfo = new HospitalInfoType();
			if(regQf.getHospitalType() != null){				
//				hospitalInfo.setHospitalType(regQf.getHospitalType().getValue().toUpperCase());
//				payloadBO.setHospitalInfo(hospitalInfo);
				
				mapValues.put(SHAConstants.HOSPITAL_TYPE, regQf.getHospitalType().getValue());
			}

			
//			if(regQf.getCpucode() != null && regQf.getCpucode().getValue() != null){
//				String cpuCode = regQf.getCpucode().getValue();
//				String cpuCodeValue[] = regQf.getCpucode().getValue().split("-");
//				ClaimRequestType calimReqBO = new ClaimRequestType();
//				if(cpuCodeValue.length >0){
//					calimReqBO.setCpuCode(cpuCodeValue[0].trim());
//					payloadBO.setClaimRequest(calimReqBO);
//				}				
//			}
			
//			if(accDeath != null && accDeath.length() > 0){
//				intimationType.setReason(String.valueOf(accType[0]));
//				payloadBO.setIntimation(intimationType);
//			}
			
//			HospitalInfoType hospitalInfo = new HospitalInfoType();
//			if(hospType != null){				
//				hospitalInfo.setHospitalType(hospType);
//				payloadBO.setHospitalInfo(hospitalInfo);
//
//			}			

		     }
				
		
//		ClassificationType classification = null;
		
	    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
					|| type != null && ! type.isEmpty()){
				
				if(priority != null && ! priority.isEmpty())
					if(! priority.equalsIgnoreCase(SHAConstants.ALL)){
						mapValues.put(SHAConstants.PRIORITY, priority);
					}
				if(source != null && ! source.isEmpty()){
					
					mapValues.put(SHAConstants.STAGE_SOURCE, source);
				}
				
				if(type != null && ! type.isEmpty()){
					if(! type.equalsIgnoreCase(SHAConstants.ALL)){
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}
				}

		}
		
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

//				 payloadBO.setClassification(classification);
		}
	    
//	    ProductInfoType prodTypeBO = new ProductInfoType();
//	    if(lob != null){
//		    prodTypeBO.setLob(lob);
//		    payloadBO.setProductInfo(prodTypeBO);	    	
//	    }
//	    
//	     if(lobType != null){
//		    prodTypeBO.setLobType(lobType);
//		    payloadBO.setProductInfo(prodTypeBO);	    	
//	    }	     


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
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
//		taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);	
		
		taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);

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
		
		Page<SearchClaimRegistrationTableDto> page = new Page<SearchClaimRegistrationTableDto>();

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
				
				SearchClaimRegistrationTableDto resultBean = new SearchClaimRegistrationTableDto();
//				resultBean.setHumanTask(item);
//				String intimationNumberFromBPMTask = getIntimationNo(item);
//				String intimationNumberFromBPMTask = "CLI/2015/120000/0002166";
				if (null != intimationNumber && !("").equalsIgnoreCase(intimationNumber)) {
					resultBean.setIntimationNumber(intimationNumber);
					Intimation intimation = getNotRegisteredIntimation(resultBean.getIntimationNumber());
					if (intimation != null) {
						NewIntimationDto intimationToRegister = getIntimationDto(intimation);
						if (null != intimationToRegister) {
							resultBean
									.setNewIntimationDto(intimationToRegister);
							resultBean.setPolicyNo(intimationToRegister.getPolicy().getPolicyNumber());
							resultBean.setCpuCode(intimationToRegister.getCpuCode());
							resultBean.setInsuredPatientName(intimationToRegister.getInsuredPatient().getInsuredName());
							if(null != intimationToRegister.getPolicy().getProduct().getKey() &&
									(ReferenceTable.GPA_PRODUCT_KEY.equals(intimationToRegister.getPolicy().getProduct().getKey()))
									&& intimationToRegister.getInsuredPatient().getInsuredName() == null){
								
								resultBean.setInsuredPatientName(intimationToRegister.getPaPatientName());
							}
							
							if(intimationToRegister.getIntimationId() != null){
								Intimation intimationByNo = getIntimationByNo(intimationToRegister.getIntimationId());
								if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
									String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
									intimationToRegister.setColorCode(colorCodeForGMC);
								}
							}
							
							resultBean.setHospitalName(intimationToRegister.getHospitalDto().getRegistedHospitals().getName());
							resultBean.setAccidentOrDeath(("A").equalsIgnoreCase(intimationToRegister.getIncidenceFlag()) ? "Accident" : "Death");
							if(intimationToRegister.getHospitalType() != null){
							resultBean.setHospitalType(intimationToRegister.getHospitalDto().getRegistedHospitals().getHospitalType().getValue());
							}
							resultBean.setIntimationDate(intimationToRegister
									.getCreatedDate());
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

		

//			}


		page.setPageItems(resultList);
//		page.setPageNumber(registrationTaskList.getCurrentPage());
//		page.setTotalRecords(taskList.size());
//		page.setHasNext(true);
//		page.setPageItems(resultList);
//		page.setPageNumber(registrationTaskList.getCurrentPage());
		page.setTotalRecords(totalRecords);


		return page;
	}

	@SuppressWarnings("unchecked")
	public NewIntimationDto getIntimationDto(Intimation intimation) {
		NewIntimationDto intimationToRegister = null;
		if (null != intimation) {
			NewIntimationMapper newIntimationMapper = NewIntimationMapper.getInstance();
			intimationToRegister = newIntimationMapper
					.getNewIntimationDto(intimation);
			intimationToRegister.setIntimationSource(intimation
					.getIntimationSource());
			if (null != intimation.getPolicy()) {
				DBCalculationService dbCalcService = new DBCalculationService();
				if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){
					
					if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
						List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());
						
						if(null != insuredList && !insuredList.isEmpty()){
							intimation.getPolicy().setInsured(insuredList);
							
						}
					}
					if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()) ||
							ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(intimation.getPolicy().getProduct().getKey())){
				      Insured insuredByKey = getInsuredByKey(intimation.getInsured().getKey());
				      Insured MainMemberInsured = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredId(intimation.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				      }
				      
				      if(MainMemberInsured != null){
				    	  intimationToRegister.setGmcMainMember(MainMemberInsured);
				    	  intimationToRegister.setGmcMainMemberName(MainMemberInsured.getInsuredName());
				    	  intimationToRegister.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
				    	  if(MainMemberInsured.getInsuredAge() != null){
				    		  intimationToRegister.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
				    	  }
				    	  
				    	  //IMSSUPPOR-27098
				    	  /**
				    	   * Part of CR R1186
				    	   */
				    	  
				    	  intimation.getInsured().setAddress1(MainMemberInsured.getAddress1());
				    	  intimation.getInsured().setAddress2(MainMemberInsured.getAddress2());
				    	  intimation.getInsured().setAddress3(MainMemberInsured.getAddress3());
				    	  intimation.getInsured().setCity(MainMemberInsured.getCity());
				    	  intimation.getInsured().setInsuredPinCode(MainMemberInsured.getInsuredPinCode());
				    	  intimation.getInsured().setInsuredState(MainMemberInsured.getInsuredState());
				      }	
						
						String colorCodeForGMC = getColorCodeForGMC(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId().toString());
					      intimationToRegister.setColorCode(colorCodeForGMC);
					      IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(intimation.getPolicy().getPolicyNumber());
					      if(incurredClaimRatio != null && incurredClaimRatio.getEarnedPremium() != null){
					    	  intimationToRegister.setIcrEarnedPremium(incurredClaimRatio.getClaimRatio().toString());
					      }
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isPaayasPlicy = getPaayasPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isPaayasPlicy){
									intimationToRegister.setIsPaayasPolicy(Boolean.TRUE);
								}
						}
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isJioPlicy = getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isJioPlicy){
									intimationToRegister.setIsJioPolicy(Boolean.TRUE);
								}
						}
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isTataPolicy = getTataPolicy(intimation.getPolicy().getPolicyNumber());
								if(isTataPolicy){
									intimationToRegister.setIsTataPolicy(Boolean.TRUE);
								}
						}
					      
					      
							String isClaimManuallyProcessed = dbCalcService.isClaimManuallyProcessed(intimationToRegister.getInsuredPatient().getHealthCardNumber());
							if (null != isClaimManuallyProcessed && SHAConstants.YES_FLAG.equalsIgnoreCase(isClaimManuallyProcessed)) {
								intimationToRegister.setIsClaimManuallyProcessed(Boolean.TRUE);
							}
				}
					
				}
				
				if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())
						|| ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
					/*Below Query commented as per premia revised table
					Boolean gmcInsuredDeleted = isGmcInsuredDeleted(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId());*/
					Boolean gmcInsuredDeleted = dbCalcService.getGMCInsuredDeleted(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId());

					intimationToRegister.setIsDeletedRisk(gmcInsuredDeleted);
				}
				intimationToRegister.setInsuredKey(intimation.getInsured().getKey());
				String address = (intimation.getPolicy().getProposerAddress1() != null ? intimation.getPolicy().getProposerAddress1() : "") + 
						(intimation.getPolicy().getProposerAddress2() != null ? intimation.getPolicy().getProposerAddress2() : "") + 
						(intimation.getPolicy().getProposerAddress3() != null ? intimation.getPolicy().getProposerAddress3() : "" );

				intimation.getPolicy().setProposerAddress(address);				
				
				intimationToRegister.setPolicy(intimation.getPolicy());	
				
				if(intimation.getPolicy() != null && intimation.getPolicy().getPolicyNumber() != null){
				    Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
				    intimationToRegister.setPolicy(policy);
				}
				
				OrganaizationUnit orgUnit = getOrganizationUnit(intimation.getPolicy().getHomeOfficeCode());
				if(orgUnit != null){
					intimationToRegister
					.setOrganizationUnit(orgUnit);
					if(orgUnit.getParentOrgUnitKey() != null){
						OrganaizationUnit parentUnit = 	getOrganizationUnit(orgUnit.getParentOrgUnitKey().toString());	
						intimationToRegister.setParentOrgUnit(parentUnit);
					}					
				}
				
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
				intimationToRegister.setInsuredPatient(intimation.getInsured());
				intimationToRegister.setInsuredAge(intimationToRegister.getInsuredCalculatedAge());
				
//				NomineeDetails nomineeObj = entityManager.find(
//						NomineeDetails.class, intimation.getInsured().getKey());
				List<NomineeDetails> nomineeListObj =  getNomineeListForInsured(intimation.getInsured().getKey());
				
				if(nomineeListObj != null && !nomineeListObj.isEmpty()){
					intimationToRegister.setNomineeName(nomineeListObj.get(0).getNomineeName());
				}
			}
		
			Hospitals registeredHospital = null;
			TmpHospital tempHospital = null;

			if (intimation.getHospital() != null
					&& intimation.getHospitalType() != null
					&& StringUtils.contains(intimation.getHospitalType()
							.getValue().toLowerCase(), "network")) {
				registeredHospital = getHospitalDetailsByKey(intimation.getHospital());
				if (null != registeredHospital) {
					HospitalDto hospitaldto = new HospitalDto(
							registeredHospital);
					hospitaldto.setRegistedHospitals(registeredHospital);
					intimationToRegister.setHospitalType(hospitaldto.getHospitalType());
					intimationToRegister.setHospitalTypeValue(hospitaldto.getHospitalType() != null ?hospitaldto.getHospitalType().getValue() : "");
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
				
			}
			
			/**
			 * For PA the claim type should be populated based on intimation table data.
			 * For Health, it should be populated based on hospital type.
			 * Hence if the claim type is PA, then we skip the below hospital type
			 * based validation.
			 * */
			if(intimation.getIncidenceFlag() == null || (intimation.getIncidenceFlag() != null && (SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(intimation.getIncidenceFlag())))
			{
				if (intimation.getHospitalType() != null){
					SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
					intimationToRegister.setClaimType(claimType);
				}
			}
			else {
				SelectValue claimType = getClaimType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
				intimationToRegister.setClaimType(claimType);
			}
			
			
			TmpCPUCode cpuObject = intimation.getCpuCode();

			if (null != cpuObject) {
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				intimationToRegister.setCpuAddress(cpuObject.getAddress());
				intimationToRegister.setReimbCpuAddress(cpuObject.getReimbAddress());
				intimationToRegister.setTollNumber(cpuObject.getTollNumber());
                intimationToRegister.setWhatsupNumber(cpuObject.getWhatsupNumber());
                intimationToRegister.setPageFooter(cpuObject.getPageFooter());
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

			} 
			/*else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
					.getValue(), "not-registered")) {
//				if (tempHospital.getPincode() != null) {
//					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//							.toString());
//				}
			}*/
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

			if (intimation.getRoomCategory() != null) {
				SelectValue roomCategory = new SelectValue();
				roomCategory.setId(intimation.getRoomCategory().getKey());
				roomCategory.setValue(intimation.getRoomCategory().getValue());
				intimationToRegister.setRoomCategory(roomCategory);
			}
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
				
			}
			intimationToRegister.setStatus(intimation.getStatus());
			intimationToRegister.setStage(intimation.getStage());
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		if(intimation.getInsured() != null && intimation.getPolicy() != null){
			if(intimation.getPolicy() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsuredForGMC(intimation.getPolicy().getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}else if(intimation.getPolicy() != null && ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()) ){
				Double insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}else{
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}
		
		}
		
		Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
		if (portablityStatus != null) {
			intimationToRegister.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
			intimationToRegister.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}
		
		//IMSSUPPOR-27698
				if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()) ||
						ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(intimation.getPolicy().getProduct().getKey())){
				      Insured insuredByKey = getInsuredByKey(intimation.getInsured().getKey());
				      Insured MainMemberInsured = null;
				      List<NomineeDetailsDto> nomineeListByPolicyKey = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				    	  nomineeListByPolicyKey = getProposerNomineeDetailsForInsured(MainMemberInsured.getKey());
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredIdForGMC(intimation.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
				    	  if (insuredByPolicyAndInsuredId !=null){
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				    	  nomineeListByPolicyKey = getProposerNomineeDetailsForInsured(MainMemberInsured.getKey());
				    	  }
				      	}
				      if(nomineeListByPolicyKey!=null){
				    	  intimationToRegister.setNomineeList(nomineeListByPolicyKey);
				      }
				      //IMSSUPPOR-28537
					}else if((SHAConstants.PA_TRAUMA_CARE_GROUP_PRODUCT_CODE.equals(intimation.getPolicy().getProduct().getCode())) 
							|| (SHAConstants.PA_ACCIDENT_CARE_GROUP_PRODUCT_CODE.equals(intimation.getPolicy().getProduct().getCode()))){
	                    Insured insuredByKey = getInsuredByKey(intimation.getInsured().getKey());
	                    Insured MainMemberInsured = null;
	                    List<NomineeDetailsDto> nomineeListByPolicyKey = null;
	                    if(insuredByKey.getKey()!= null){
	                            MainMemberInsured = insuredByKey;
	                        nomineeListByPolicyKey = getProposerNomineeDetailsForInsured(MainMemberInsured.getKey());
	                        if(nomineeListByPolicyKey!=null){
	                                intimationToRegister.setNomineeList(nomineeListByPolicyKey);
	                        }
	                     }
					}else{
					List<NomineeDetailsDto> nomineeListByPolicyKey = getNomineeForPolicyKey(intimation.getPolicy().getKey());
					intimationToRegister.setNomineeList(nomineeListByPolicyKey);
					}
				//IMSSUPPOR-27698
		
		return intimationToRegister;
	}
	
	@SuppressWarnings("unchecked")
	public NewIntimationDto getGalaxyIntimationDto(GalaxyIntimation intimation) {
		NewIntimationDto intimationToRegister = null;
		if (null != intimation) {
			NewIntimationMapper newIntimationMapper = NewIntimationMapper.getInstance();
			intimationToRegister = newIntimationMapper
					.getStageNewIntimationDto(intimation);
			intimationToRegister.setIntimationSource(intimation
					.getIntimationSource());
			if (null != intimation.getPolicy()) {
				if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){
					List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());
					
					if(null != insuredList && !insuredList.isEmpty()){
						intimation.getPolicy().setInsured(insuredList);
						
					}
					if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()) ||
							ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(intimation.getPolicy().getProduct().getKey())	){
				      Insured insuredByKey = getInsuredByKey(intimation.getInsured().getKey());
				      Insured MainMemberInsured = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredNameForDefault(intimation.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				      }
				      
				      if(MainMemberInsured != null){
				    	  intimationToRegister.setGmcMainMemberId(MainMemberInsured.getInsuredId());
				    	  intimationToRegister.setGmcMainMemberName(MainMemberInsured.getInsuredName());
				    	  intimationToRegister.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
				    	  intimationToRegister.setInsuredKey(intimation.getInsured().getKey());
				    	  if(MainMemberInsured.getInsuredAge() != null){
				    		  intimationToRegister.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
				    	  }
				    	  
				    	  /**
				    	   * Part of CR R1186
				    	   */
				    	  
				    	  intimation.getInsured().setAddress1(MainMemberInsured.getAddress1());
				    	  intimation.getInsured().setAddress2(MainMemberInsured.getAddress2());
				    	  intimation.getInsured().setAddress3(MainMemberInsured.getAddress3());
				    	  intimation.getInsured().setCity(MainMemberInsured.getCity());
				    	  intimation.getInsured().setInsuredPinCode(MainMemberInsured.getInsuredPinCode());
				    	  intimation.getInsured().setInsuredState(MainMemberInsured.getInsuredState());
				    	  
				      }	
						
						String colorCodeForGMC = getColorCodeForGMC(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId().toString());
					      intimationToRegister.setColorCode(colorCodeForGMC);
					      IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(intimation.getPolicy().getPolicyNumber());
					      if(incurredClaimRatio != null && incurredClaimRatio.getEarnedPremium() != null){
					    	  intimationToRegister.setIcrEarnedPremium(incurredClaimRatio.getClaimRatio().toString());
					      }
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isPaayasPlicy = getPaayasPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isPaayasPlicy){
									intimationToRegister.setIsPaayasPolicy(Boolean.TRUE);
								}
						}
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isJioPlicy = getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isJioPlicy){
									intimationToRegister.setIsJioPolicy(Boolean.TRUE);
								}
						}
					      
					      if(null != intimation.getPolicy().getPolicyNumber()){
								
								Boolean isTataPolicy = getTataPolicy(intimation.getPolicy().getPolicyNumber());
								if(isTataPolicy){
									intimationToRegister.setIsTataPolicy(Boolean.TRUE);
								}
						}
					      
					      DBCalculationService dbCalcService = new DBCalculationService();
							String isClaimManuallyProcessed = dbCalcService.isClaimManuallyProcessed(intimationToRegister.getInsuredPatient().getHealthCardNumber());
							if (null != isClaimManuallyProcessed && SHAConstants.YES_FLAG.equalsIgnoreCase(isClaimManuallyProcessed)) {
								intimationToRegister.setIsClaimManuallyProcessed(Boolean.TRUE);
							}
				}
					
				}
				
				if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())
						|| ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
					Boolean gmcInsuredDeleted = isGmcInsuredDeleted(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId());
					intimationToRegister.setIsDeletedRisk(gmcInsuredDeleted);
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
				
				OrganaizationUnit orgUnit = getOrganizationUnit(intimation.getPolicy().getHomeOfficeCode());
				if(orgUnit != null){
					intimationToRegister
					.setOrganizationUnit(orgUnit);
					if(orgUnit.getParentOrgUnitKey() != null){
						OrganaizationUnit parentUnit = 	getOrganizationUnit(orgUnit.getParentOrgUnitKey().toString());	
						intimationToRegister.setParentOrgUnit(parentUnit);
					}					
				}
				
				if (null != intimation.getLobId() && null != intimation.getLobId().getKey()) {
					MastersValue lineofBusiness = entityManager.find(
							MastersValue.class, intimation.getLobId().getKey());
					intimationToRegister.setLineofBusiness(lineofBusiness
							.getValue() != null ? lineofBusiness.getValue()
							: "");
					intimationToRegister.setLobId(new SelectValue(lineofBusiness.getKey(),lineofBusiness.getValue()));
				}
			}

			if (null != intimation.getInsured()) {
				intimationToRegister.setInsuredPatient(intimation.getInsured());
				intimationToRegister.setInsuredAge(intimationToRegister.getInsuredCalculatedAge());
				
				NomineeDetails nomineeObj = entityManager.find(
						NomineeDetails.class, intimation.getInsured().getKey());
				
				if(nomineeObj != null){
				intimationToRegister.setNomineeName(nomineeObj.getNomineeName());
				}
			}
		
			Hospitals registeredHospital = null;
			TmpHospital tempHospital = null;

			if (intimation.getHospital() != null
					&& intimation.getHospitalType() != null
					&& StringUtils.contains(intimation.getHospitalType()
							.getValue().toLowerCase(), "network")) {
				registeredHospital = getHospitalDetailsByKey(intimation.getHospital());
				if (null != registeredHospital) {
					HospitalDto hospitaldto = new HospitalDto(registeredHospital);
					hospitaldto.setRegistedHospitals(registeredHospital);
					intimationToRegister.setHospitalType(hospitaldto.getHospitalType());
					intimationToRegister.setHospitalTypeValue(hospitaldto.getHospitalType() != null ?hospitaldto.getHospitalType().getValue() : "");
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
				
			}
			
			/**
			 * For PA the claim type should be populated based on intimation table data.
			 * For Health, it should be populated based on hospital type.
			 * Hence if the claim type is PA, then we skip the below hospital type
			 * based validation.
			 * */
			if(intimation.getIncidenceFlag() == null || (intimation.getIncidenceFlag() != null && (SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(intimation.getIncidenceFlag())))
			{
				if (intimation.getHospitalType() != null){
					SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
					intimationToRegister.setClaimType(claimType);
				}
			}
			else {
				SelectValue claimType = getClaimType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
				intimationToRegister.setClaimType(claimType);
			}
			
			
			TmpCPUCode cpuObject = intimation.getCpuCode();

			if (null != cpuObject) {
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				intimationToRegister.setCpuAddress(cpuObject.getAddress());
				intimationToRegister.setReimbCpuAddress(cpuObject.getReimbAddress());
				intimationToRegister.setTollNumber(cpuObject.getTollNumber());
                intimationToRegister.setWhatsupNumber(cpuObject.getWhatsupNumber());
                intimationToRegister.setPageFooter(cpuObject.getPageFooter());
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

			} 
			/*else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
					.getValue(), "not-registered")) {
//				if (tempHospital.getPincode() != null) {
//					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//							.toString());
//				}
			}*/
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

			if (intimation.getRoomCategory() != null) {
				SelectValue roomCategory = new SelectValue();
				roomCategory.setId(intimation.getRoomCategory().getKey());
				roomCategory.setValue(intimation.getRoomCategory().getValue());
				intimationToRegister.setRoomCategory(roomCategory);
			}
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
				
			}
			intimationToRegister.setStatus(intimation.getStatus());
			intimationToRegister.setStage(intimation.getStage());
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		if(intimation.getInsured() != null && intimation.getPolicy() != null){
			if(intimation.getPolicy() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsuredForGMC(intimation.getPolicy().getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}else if(intimation.getPolicy() != null && ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())){
				Double insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}else{
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag());
				intimationToRegister.setOrginalSI(insuredSumInsured);
			}
		
		}
		
		Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
		if (portablityStatus != null) {
			intimationToRegister.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
			intimationToRegister.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}
		
		
		return intimationToRegister;
	}
	
	
	@SuppressWarnings("unchecked")
	public NewIntimationDto getIntimationDtoFromViewIntimation(ViewTmpIntimation intimation) {
		NewIntimationDto intimationToRegister = null;
		if (null != intimation) {
			NewIntimationMapper newIntimationMapper = new NewIntimationMapper();
			intimationToRegister = newIntimationMapper
					.getNewIntimationDto(intimation);
			intimationToRegister.setIntimationSource(intimation
					.getIntimationSource());
			if (null != intimation.getPolicy()) {
				Policy policyByPolicyNubember = getPolicyByPolicyNubember(intimation.getPolicyNumber());
				if(null == policyByPolicyNubember.getInsured() || policyByPolicyNubember.getInsured().isEmpty()){
					List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicyNumber());
					if(null != insuredList && !insuredList.isEmpty()){
						policyByPolicyNubember.setInsured(insuredList);
					}
				}
				
				
				String address = (policyByPolicyNubember.getProposerAddress1() != null ? policyByPolicyNubember.getProposerAddress1() : "") + 
						(policyByPolicyNubember.getProposerAddress2() != null ? policyByPolicyNubember.getProposerAddress2() : "") + 
						(policyByPolicyNubember.getProposerAddress3() != null ? policyByPolicyNubember.getProposerAddress3() : "" );

				policyByPolicyNubember.setProposerAddress(address);				
				
				intimationToRegister.setPolicy(policyByPolicyNubember);	
				
				intimationToRegister
						.setOrganizationUnit(getOrganizationUnit(policyByPolicyNubember.getHomeOfficeCode()));
				if (null != policyByPolicyNubember.getLobId()) {
					MastersValue lineofBusiness = entityManager.find(
							MastersValue.class, policyByPolicyNubember
									.getLobId());
					intimationToRegister.setLineofBusiness(lineofBusiness
							.getValue() != null ? lineofBusiness.getValue()
							: "");
				}
			}

			if (null != intimation.getInsured()) {
				intimationToRegister.setInsuredPatient(intimation.getInsured());
			}
		
			Hospitals registeredHospital = null;
			TmpHospital tempHospital = null;

			if (intimation.getHospital() != null
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
					String[] hospAddress = StringUtil.split(registeredHospital.getAddress(),',');
					int length;
					if(hospAddress.length != 0 ){
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
			} else {
				if(intimation.getHospital() != null) {
					tempHospital = entityManager.find(TmpHospital.class,
							intimation.getHospital());
					HospitalDto hospitalDto = new HospitalDto(tempHospital,
							intimation.getHospitalType().getValue());
					hospitalDto.setNotRegisteredHospitals(tempHospital);
				}
				
			}
			

			if (intimation.getHospitalType() != null){
				SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
				intimationToRegister.setClaimType(claimType);
			}
			
			
			TmpCPUCode cpuObject = intimation.getCpuCode();

			if (null != cpuObject) {
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				intimationToRegister.setCpuAddress(cpuObject.getAddress());
				intimationToRegister.setReimbCpuAddress(cpuObject.getReimbAddress());
				intimationToRegister.setTollNumber(cpuObject.getTollNumber());
                intimationToRegister.setWhatsupNumber(cpuObject.getWhatsupNumber());
                intimationToRegister.setPageFooter(cpuObject.getPageFooter());
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
				if (tempHospital.getPincode() != null) {
					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
							.toString());
				}
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

			if (intimation.getRoomCategory() != null) {
				SelectValue roomCategory = new SelectValue();
				roomCategory.setId(intimation.getRoomCategory().getKey());
				roomCategory.setValue(intimation.getRoomCategory().getValue());
				intimationToRegister.setRoomCategory(roomCategory);
			}
			if (intimation.getPolicy() != null) {
				Policy policyByPolicyNubember = getPolicyByPolicyNubember(intimation.getPolicyNumber());
				intimationToRegister.setAgentBrokerCode(policyByPolicyNubember
						.getAgentCode() != null ? policyByPolicyNubember
						.getAgentCode() : "");
				intimationToRegister.setAgentBrokerName(policyByPolicyNubember
						.getAgentName() != null ? policyByPolicyNubember
						.getAgentName() : "");
				if(policyByPolicyNubember.getProduct()!=null){
					intimationToRegister.setProductName(policyByPolicyNubember
							.getProduct().getValue() != null ? policyByPolicyNubember.getProduct().getValue() : "");
				}
				
			}
			intimationToRegister.setStatus(intimation.getStatus());
			intimationToRegister.setStage(intimation.getStage());
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		if(intimation.getInsured() != null && intimation.getPolicy() != null){
		Policy policyByPolicyNubember = getPolicyByPolicyNubember(intimation.getPolicyNumber());
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), policyByPolicyNubember.getKey(),intimation.getInsured().getLopFlag());
		intimationToRegister.setOrginalSI(insuredSumInsured);
		}
		
		Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
		if (portablityStatus != null) {
			intimationToRegister.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
			intimationToRegister.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}
		return intimationToRegister;
	}
	
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
	
	public NewIntimationDto getIntimationDto(Intimation intimation,
			EntityManager entityManager) {
		this.entityManager = entityManager;
		return getIntimationDto(intimation);
	}
	
	public NewIntimationDto getIntimationDto(ViewTmpIntimation intimation,
			EntityManager entityManager) {
		this.entityManager = entityManager;
		return getIntimationDtoFromViewIntimation(intimation);
	}




	public TmpCPUCode getCpuObjectByPincode(String pincode) {
		TmpCPUCode cpuObject = null;
		if (pincode != null) {
			cpuObject = entityManager.find(TmpCPUCode.class,
					Long.valueOf(StringUtils.trim(pincode)));
		}
		return cpuObject;
	}

	/*private String getIntimationNo(HumanTask item) {

//		Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(
//				item.getPayload(), "RegIntDetails");
//		String keyValue = (String) valuesFromBPM.get("intimationNumber");
		return item.getPayloadCashless().getIntimation().getIntimationNumber();
	}*/

	/**
	 * Method to retreive viewIntimation DTO.
	 * */
	public ViewIntimationDTO viewIntimatinDTO(Intimation intimation,
			Hospitals hospital) {
		if (intimation == null) {
			return null;
		}

		OrganaizationUnit organization = getOrganizationUnit(intimation
				.getPolicy().getHomeOfficeCode());

		ViewIntimationDTO viewIntimationDTO = new ViewIntimationDTO();

		viewIntimationDTO
				.setKey((Long) (intimation.getKey() != null ? intimation
						.getKey() : ""));
		viewIntimationDTO
				.setIntimationNo(intimation.getIntimationId() != null ? intimation
						.getIntimationId() : "");
		viewIntimationDTO.setDateOfIntimation(intimation.getCreatedDate()
				.toString() != null ? intimation.getCreatedDate().toString()
				: "");
		viewIntimationDTO.setPolicyNumber(intimation.getPolicy()
				.getPolicyNumber() != null ? intimation.getPolicy()
				.getPolicyNumber() : "");
		viewIntimationDTO
				.setPolicyIssuingOffice(null != organization ? organization
						.getOrganizationUnitName() : "");
		viewIntimationDTO.setProductName(intimation.getPolicy()
				.getProductName() != null ? intimation.getPolicy()
				.getProductName() : "");
		viewIntimationDTO
				.setInsuredName(intimation.getIntimatedBy().getValue() != null ? intimation
						.getIntimatedBy().getValue() : "");
		viewIntimationDTO
				.setPatientName(intimation.getInsuredPatientName() != null ? intimation
						.getInsuredPatientName() : "");
		viewIntimationDTO.setHospitalName(hospital.getName() != null ? hospital
				.getName() : "");
		viewIntimationDTO
				.setCityOfHospital(hospital.getCity() != null ? hospital
						.getCity() : "");
		viewIntimationDTO
				.setHospitalType(hospital.getHospitalType().getValue() != null ? hospital
						.getHospitalType().getValue() : "");
		viewIntimationDTO.setDateOfAdmission(intimation.getAdmissionDate()
				.toString() != null ? intimation.getAdmissionDate().toString()
				: "");
		viewIntimationDTO
				.setReasonForAdmission(intimation.getAdmissionReason() != null ? intimation
						.getAdmissionReason() : "");
		viewIntimationDTO.setCpuCode(intimation.getCpuCode().getCpuCode()
				.toString() != null ? intimation.getCpuCode().getCpuCode()
				.toString() : "");
		viewIntimationDTO
				.setSmCode(intimation.getPolicy().getSmCode() != null ? intimation
						.getPolicy().getSmCode() : "");
		viewIntimationDTO
				.setSmName(intimation.getPolicy().getSmName() != null ? intimation
						.getPolicy().getSmName() : "");
		viewIntimationDTO.setAgentBrokerCode(intimation.getPolicy()
				.getAgentCode() != null ? intimation.getPolicy().getAgentCode()
				: "");
		viewIntimationDTO.setAgentBrokerName(intimation.getPolicy()
				.getAgentName() != null ? intimation.getPolicy().getAgentName()
				: "");
		viewIntimationDTO
				.setHospitalCode(hospital.getHospitalCode() != null ? hospital
						.getHospitalCode() : "");
		viewIntimationDTO.setIdCardNo(intimation.getInsured()
				.getHealthCardNumber() != null ? intimation.getInsured()
				.getHealthCardNumber() : "");
		return viewIntimationDTO;
	}

	public OrganaizationUnit getOrganizationUnit(String strPolOfficeCode) {

		return getPolicyServiceInstance().getInsuredOfficeNameByDivisionCode(
				entityManager, strPolOfficeCode);
	}

	public PolicyService getPolicyServiceInstance() {
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
	
	public Intimation getIntimationByHealthcardNo(String intimationId, String heathCardNo){
		Query query = entityManager.createNamedQuery("Intimation.findByHealthCardNo");
		query.setParameter("intimationId", intimationId);
		query.setParameter("healthCardNo", heathCardNo);
		List<Intimation> resultList = query.getResultList();
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
	
	
	//RM-STUB
	/*public Claim doAutoRegistratrionProcessForPremia(EntityManager entityManager ,Intimation objIntimation , StarFaxSimulatorService starFaxservice)
	{
		this.entityManager = entityManager;
		return doAutoRegistrationProcess(objIntimation, starFaxservice);
	}
	
	public Claim doAutoRegistrationProcess(Intimation objIntimation , StarFaxSimulatorService starFaxservice)
	{
		//Hospitals objHosp = starFaxservice.getHospitalObject(objIntimation.getHospital());
		String strClaimTypeRequest = "";
		if(null != objIntimation.getClaimType())
		{
			strClaimTypeRequest = objIntimation.getClaimType().getValue();
		}
		Claim objClaim = starFaxservice.populateClaimObject(objIntimation, strClaimTypeRequest);
		    log.info("before save claim --->" + objIntimation.getIntimationId());
		try {
			entityManager.persist(objClaim);
			entityManager.flush();
		} catch(Exception e) {
			e.printStackTrace();
			log.error("******************Not Pulled from Premia***************** -----> " + objIntimation.getIntimationId());
		}
		
//		entityManager.refresh(objClaim);
		log.info("after save claim --->" + objIntimation.getIntimationId() + "Claim No --- >" + objClaim.getClaimId());
		
		objIntimation = getIntimationByKey(objIntimation.getKey());
		objIntimation.setRegistrationStatus("REGISTERED");
		entityManager.merge(objIntimation);
		entityManager.flush();
		return objClaim;
	}*/
	
	public List<PlannedAdmissionReportDto> getPlannedIntimationDetails(Map<String,Object> searchFilter){
		List<PlannedAdmissionReportDto> resultList = new ArrayList<PlannedAdmissionReportDto>();
		if(searchFilter != null && !searchFilter.isEmpty()){
			
			Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
			Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
				
			try{	
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
				/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				Join<Intimation, Insured> insuredJoin = intimationRoot.join(
						"insured", JoinType.INNER);*/

				List<Predicate> predicates = new ArrayList<Predicate>();
				Expression<Date> dateExpression = intimationRoot
						.<Date> get("createdDate");
				if(fromDate != null){					
					Predicate fromDatePredicate = builder
							.greaterThanOrEqualTo(dateExpression,
									fromDate);
					predicates.add(fromDatePredicate);					
				}
				if(toDate != null){

				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(dateExpression, toDate);
				predicates.add(toDatePredicate);
				
				}
				
				Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
				Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
				predicates.add(statusPredicate);
				
				Predicate plannedAdminPredicate = builder.equal(intimationRoot.<MastersValue>get("admissionType").<Long>get("key"),ReferenceTable.PLANNED_ADMISSION_KEY);
				predicates.add(plannedAdminPredicate);
				
				criteriaQuery.select(intimationRoot).where(
						builder.and(predicates
								.toArray(new Predicate[] {})));

				final TypedQuery<Intimation> intimationquery = entityManager
						.createQuery(criteriaQuery);
				SHAUtils.popinReportLog(entityManager, "", "PlannedAdmissionReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				List<Intimation> intimationList = intimationquery.getResultList();
				
				if(intimationList != null && !intimationList.isEmpty()){
					NewIntimationDto intimationDto;
					PlannedAdmissionReportDto plannedDto;
					for(Intimation intimation : intimationList){
						if(!isOPintimation(intimation.getKey())){
							intimationDto = getIntimationDto(intimation);
							plannedDto = new PlannedAdmissionReportDto(intimationDto);
							plannedDto.setSno(intimationList.indexOf(intimation)+1);
							resultList.add(plannedDto);
							plannedDto = null;
						}
					}					
				}
				SHAUtils.popinReportLog(entityManager, "", "PlannedAdmissionReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			}catch(Exception e){
				SHAUtils.popinReportLog(entityManager, "", "PlannedAdmissionReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				e.printStackTrace();
			}
			
		}
		return resultList;
	}
	
	public List<IntimatedRiskDetailsReportDto> getIntimatedRiskDetails(Map<String,Object> searchFilter){
		List<IntimatedRiskDetailsReportDto> resultList = new ArrayList<IntimatedRiskDetailsReportDto>();
		if(searchFilter != null && !searchFilter.isEmpty()){
			
			Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
			Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
			String intimationNo = searchFilter.containsKey("intimationNo") && searchFilter.get("intimationNo") != null ? (String) searchFilter.get("intimationNo") : null;
				
			try{	
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = builder
						.createQuery(Intimation.class);

				Root<Intimation> intimationRoot = criteriaQuery
						.from(Intimation.class);
				/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);
				Join<Intimation, Insured> insuredJoin = intimationRoot.join(
						"insured", JoinType.INNER);*/

				List<Predicate> predicates = new ArrayList<Predicate>();
				Expression<Date> dateExpression = intimationRoot
						.<Date> get("createdDate");
				if(fromDate != null){
					Predicate fromDatePredicate = builder
							.greaterThanOrEqualTo(dateExpression,
									fromDate);
					predicates.add(fromDatePredicate);
				}
				 if(toDate != null){
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
					Predicate toDatePredicate = builder
							.lessThanOrEqualTo(dateExpression, toDate);
					predicates.add(toDatePredicate);
				
				}
				if(intimationNo != null && !("").equalsIgnoreCase(intimationNo)){
					
					Predicate intimationNoPredicate = builder
							.like(intimationRoot.<String>get("intimationId"), intimationNo);
					predicates.add(intimationNoPredicate);
				}
				
				Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
				Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
				predicates.add(statusPredicate);
				
				criteriaQuery.select(intimationRoot).where(
						builder.and(predicates
								.toArray(new Predicate[] {})));

				final TypedQuery<Intimation> intimationquery = entityManager
						.createQuery(criteriaQuery);
				
				SHAUtils.popinReportLog(entityManager, "", "IntimatedRiskDetailsReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				List<Intimation> intimationList = intimationquery.getResultList();
				
				if(intimationList != null && !intimationList.isEmpty()){
					Hospitals hospObj;
					String hospName = "";
					IntimatedRiskDetailsReportDto intimatedRiskDto;
					for(Intimation intimation : intimationList){	
						if(!isOPintimation(intimation.getKey())){
//							NewIntimationDto intimationDto = getIntimationDto(intimation);
//							IntimatedRiskDetailsReportDto intimatedRiskDto = new IntimatedRiskDetailsReportDto(intimationDto);
							hospObj = getHospitalDetailsByKey(intimation.getHospital());
							hospName = hospObj.getName() != null ? hospObj.getName() : "";
							intimatedRiskDto = new IntimatedRiskDetailsReportDto(intimation,hospName);
							resultList.add(intimatedRiskDto);
							intimatedRiskDto = null;
						}		
					}					
				}
				SHAUtils.popinReportLog(entityManager, "", "IntimatedRiskDetailsReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				
			}
			catch(Exception e){
				SHAUtils.popinReportLog(entityManager, "", "IntimatedRiskDetailsReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				e.printStackTrace();
			}
		}
		return resultList;	
	}
	
	
	public void updateDocumentDetails(MultipleUploadDocumentDTO dto, String intimationId){
		
		try{
	DocumentDetails referenceDocument = new DocumentDetails();
	referenceDocument.setFileName(dto.getFileName());
	referenceDocument.setDocumentToken(Long.valueOf(dto.getFileToken()));
	referenceDocument.setDocumentSource("Update Pan Details");
	referenceDocument.setDocumentType("Pan Card Details");
	if(intimationId!=null){
		referenceDocument.setIntimationNumber(intimationId);
	}
//	referenceDocument.setClaimNumber(investigation.getClaim().getClaimId());
	//Reimbursement reimbursementByKey = getReimbursementByKey(bean.getTransactionKey());
	
	/*if(reimbursementByKey != null){
		referenceDocument.setReimbursementNumber(reimbursementByKey.getRodNumber());
	}*/
	referenceDocument.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
	
	referenceDocument.setDeletedFlag("N");
	referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	referenceDocument.setCreatedBy(dto.getUsername());
	entityManager.persist(referenceDocument);
	entityManager.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	public void updateAadharDocumentDetails(MultipleUploadDocumentDTO dto, String intimationId){
		
		try{
			DocumentDetails referenceDocument = new DocumentDetails();
			referenceDocument.setFileName(dto.getFileName());
			referenceDocument.setDocumentToken(Long.valueOf(dto.getFileToken()));
			referenceDocument.setDocumentSource("Update Aadhar Details");
			referenceDocument.setDocumentType("Aadhar Card Details");
			if(intimationId!=null){
				referenceDocument.setIntimationNumber(intimationId);
			}

			referenceDocument.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
		
			referenceDocument.setDeletedFlag("N");
			referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			referenceDocument.setCreatedBy(dto.getUsername());
			entityManager.persist(referenceDocument);
			entityManager.flush();
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	
	public List<IntimationAlternateCPUwiseReportDto> getAlternateCPUwiseIntimationDetails(Map<String,Object> searchFilter){
		List<IntimationAlternateCPUwiseReportDto> resultListDto = new ArrayList<IntimationAlternateCPUwiseReportDto>();
		
		if(searchFilter != null && !searchFilter.isEmpty()){
			try{
				
				Date fromDate = searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null ? (Date) searchFilter.get("fromDate") : null;
				Date toDate = searchFilter.containsKey("endDate") && searchFilter.get("endDate") != null ? (Date) searchFilter.get("endDate") : null;
					
					final CriteriaBuilder builder = entityManager
							.getCriteriaBuilder();
					final CriteriaQuery<Intimation> criteriaQuery = builder
							.createQuery(Intimation.class);

					Root<Intimation> intimationRoot = criteriaQuery
							.from(Intimation.class);
					/*Join<Intimation, Policy> policyJoin = intimationRoot.join(
							"policy", JoinType.INNER);
					Join<Intimation, Insured> insuredJoin = intimationRoot.join(
							"insured", JoinType.INNER);*/

					List<Predicate> predicates = new ArrayList<Predicate>();
				
					if(fromDate != null && toDate != null){
					
						Expression<Date> dateExpression = intimationRoot
								.<Date> get("createdDate");
						Predicate fromDatePredicate = builder
								.greaterThanOrEqualTo(dateExpression,
										fromDate);
						predicates.add(fromDatePredicate);

						Calendar c = Calendar.getInstance();
						c.setTime(toDate);
						c.add(Calendar.DATE, 1);
						toDate = c.getTime();
						Predicate toDatePredicate = builder
								.lessThanOrEqualTo(dateExpression, toDate);
						predicates.add(toDatePredicate);
					
					}
						
//						Predicate cpuPredicate = builder  
//								.notLike(intimationRoot.<TmpCPUCode>get("cpuCode").<String>get("cpuCode"), intimationRoot.<Policy>get("policy").<String>get("homeOfficeCode"));
//						predicates.add(cpuPredicate);
					
					Expression<Long> stusExp = intimationRoot.<Status>get("status").<Long>get("key");
					Predicate statusPredicate = builder.equal(stusExp, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
					predicates.add(statusPredicate);
					
					criteriaQuery.select(intimationRoot).where(
							builder.and(predicates
									.toArray(new Predicate[] {})));

					final TypedQuery<Intimation> intimationquery = entityManager
							.createQuery(criteriaQuery);
					SHAUtils.popinReportLog(entityManager, "", "IntimationAlternateCPUwiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
					
					List<Intimation> intimationList = intimationquery.getResultList();
					
					if(intimationList != null && !intimationList.isEmpty()){
						Hospitals hospObj;
						String hospName = "";
						Claim claimObj;
						OrganaizationUnit orgUnitObj;
						IntimationAlternateCPUwiseReportDto intimationAlternateCPUDto;
						for(Intimation intimation : intimationList){
							entityManager.refresh(intimation);
							if(!isOPintimation(intimation.getKey())){
//								NewIntimationDto intimationDto = getIntimationDto(intimation);
								orgUnitObj = getOrganizationUnit(intimation
										.getPolicy().getHomeOfficeCode());
								if(intimation.getCpuCode().getCpuCode() != null && orgUnitObj != null && orgUnitObj.getCpuCode() != null && !(orgUnitObj.getCpuCode()).equalsIgnoreCase(String.valueOf(intimation.getCpuCode().getCpuCode()))){
//									IntimationAlternateCPUwiseReportDto intimationAlternateCPUDto = new IntimationAlternateCPUwiseReportDto(intimationDto);
									hospObj = getHospitalDetailsByKey(intimation.getHospital());
									hospName = hospObj != null ? hospObj.getName() : "";
									intimationAlternateCPUDto = new IntimationAlternateCPUwiseReportDto(intimation,hospName);
									claimObj = getClaimforIntimation(intimation.getKey());
									if(claimObj != null){
										entityManager.refresh(claimObj);
										intimationAlternateCPUDto.setClaimNo(claimObj.getClaimId());
									}
									resultListDto.add(intimationAlternateCPUDto);
									intimationAlternateCPUDto = null;
								}								
							}		
						}
						SHAUtils.popinReportLog(entityManager, "", "IntimationAlternateCPUwiseReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
					}
			}
			catch(Exception e){
				SHAUtils.popinReportLog(entityManager, "", "IntimationAlternateCPUwiseReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				e.printStackTrace();
			}
		}
			
		return resultListDto;
	
	}


	
	
	@SuppressWarnings("unchecked")
	public List<UploadedPanCardDocumentsDTO> getUploadDocumentList(Long intimationKey){
		
		
		List<MultipleUploadDocumentDTO> updateDocumentDetails = getUpdateDocumentDetails(intimationKey);
		Integer sno = 1;
		List<UploadedPanCardDocumentsDTO> list = new ArrayList<UploadedPanCardDocumentsDTO>();
		for (MultipleUploadDocumentDTO documentDto : updateDocumentDetails) {
//			entityManager.refresh(investigation);
			if(documentDto.getFileName() != null){
				UploadedPanCardDocumentsDTO dto = new UploadedPanCardDocumentsDTO();
				dto.setSno(sno);
				dto.setFileName(documentDto.getFileName());
				dto.setToken(documentDto.getFileToken());
				dto.setFileType("Pan Card Details");
				dto.setUploadBy(documentDto.getUsername());
				dto.setUploadDate(documentDto.getUploadedDate());
				list.add(dto);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<UpdateAadharDetailsDTO> getUploadAadharDocumentList(Long intimationKey){
		
		
		List<MultipleUploadDocumentDTO> updateDocumentDetails = getUpdateDocumentDetails(intimationKey);
		Integer sno = 1;
		List<UpdateAadharDetailsDTO> list = new ArrayList<UpdateAadharDetailsDTO>();
		for (MultipleUploadDocumentDTO documentDto : updateDocumentDetails) {
			if(documentDto.getFileName() != null){
				UpdateAadharDetailsDTO dto = new UpdateAadharDetailsDTO();
				dto.setSno(sno);
				dto.setFileName(documentDto.getFileName());
				dto.setToken(documentDto.getFileToken());
				dto.setFileType("Aadhar Details");
				dto.setUploadBy(documentDto.getUsername());
				dto.setUploadDate(documentDto.getUploadedDate());
				list.add(dto);
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<UploadedPanCardDocumentsDTO> getUploadDocumentList(MultipleUploadDocumentDTO documentDto1){
		
		
		List<MultipleUploadDocumentDTO> updateDocumentDetails = new ArrayList<MultipleUploadDocumentDTO>();
		updateDocumentDetails.add(documentDto1);
		Integer sno = 1;
		List<UploadedPanCardDocumentsDTO> list = new ArrayList<UploadedPanCardDocumentsDTO>();
		for (MultipleUploadDocumentDTO documentDto : updateDocumentDetails) {
//			entityManager.refresh(investigation);
			if(documentDto.getFileName() != null){
				UploadedPanCardDocumentsDTO dto = new UploadedPanCardDocumentsDTO();
				dto.setSno(documentDto1.getsNo());
				dto.setFileName(documentDto.getFileName());
				dto.setToken(documentDto.getFileToken());
				dto.setFileType("Pan Card Details");
				dto.setUploadBy(documentDto.getUsername());
				dto.setUploadDate(documentDto.getUploadedDate());
				list.add(dto);
			}
		}
		return list;
	}

	public List<MultipleUploadDocumentDTO> getUpdateDocumentDetails(Long key){
		
		Query query = entityManager.createNamedQuery("ReferenceDocument.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
		List<MultipleUploadDocumentDTO> documentList = new ArrayList<MultipleUploadDocumentDTO>();
		
		List<ReferenceDocument> resultList = (List<ReferenceDocument>) query.getResultList();
		for (ReferenceDocument referenceDocument : resultList) {
			MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
			dto.setTransactionKey(referenceDocument.getTransactionKey());
			dto.setFileName(referenceDocument.getFileName());
			dto.setFileToken(referenceDocument.getDocumentToken());
			dto.setUsername(referenceDocument.getCreatedBy());
			dto.setUploadedDate(referenceDocument.getCreatedDate());
			documentList.add(dto);
		}
		
		return documentList;
	}
	
	
	public void setInactiveUpdateDocumentDetails(String documentToken){
		
		Query query = entityManager.createNamedQuery("ReferenceDocument.findByToken");
		query.setParameter("documentToken", documentToken);
		List<ReferenceDocument> resultList = (List<ReferenceDocument>) query.getResultList();
		if(resultList!=null && !resultList.isEmpty()){
			ReferenceDocument referenceDocument = resultList.get(0);
			referenceDocument.setDeletedFlag("Y");
			entityManager.merge(referenceDocument);
			entityManager.flush();
		}
		
	}
	
	public List<Long> getIntimationByPolicyLikeNo(String policyNo){
		if(policyNo != null){
			Query query = entityManager.createNamedQuery("Intimation.findByMaxByPolicy");
			query.setParameter("policyNo", policyNo);
	
			List<Long> resultList =  (List<Long>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
//				entityManager.refresh(resultList);
				return resultList;
			}
		}
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
	
	public Insured getInsuredByPolicyAndInsuredId(String policyNo , Long insuredId, String lobFlag) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredId)
		query = query.setParameter("insuredId", insuredId);
		query = query.setParameter("lobFlag", lobFlag);
		Insured insuredResult = null;
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			for (Insured insured : insuredList) {
				if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
					insuredResult = insured;
					break;
				}
				else if(SHAConstants.PA_CLAIM_TYPE.equalsIgnoreCase(insured.getLopFlag())){
					insuredResult = insured;
					break;					
				}
			}
		}
		return insuredResult;
	}
	
	public Insured getInsuredByPolicyAndInsuredNameForDefault(String policyNo , Long insuredId) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNoForDefault");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredId)
		query = query.setParameter("insuredId", insuredId);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			
			if(insuredList.size()>1){
				if(insuredList.get(0).getLopFlag() != null && SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insuredList.get(0).getLopFlag())){
					return insuredList.get(0);
				}
				else {
					return insuredList.get(1);
				}
			}
			else{			
			
				return insuredList.get(0);
			}
		}
		
		return null;
	}
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		//query.setParameter("insuredNumber", insuredNumber);
		List<IncurredClaimRatio> result = (List<IncurredClaimRatio>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}

	public String getColorCodeForGMC(String policyNumber, String insuredNumber){
		IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(policyNumber);
		if(incurredClaimRatio != null){
			return incurredClaimRatio.getClaimColour();
		}
		return null;
	}
	public Boolean getPaayasPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("PaayasPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<PaayasPolicy> resultList = (List<PaayasPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
	}
	public Boolean getJioPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("StarJioPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarJioPolicy> resultList = (List<StarJioPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
	}
	
	public Boolean isPremiaIntimation(String policyNo){
//		entityManager.merge(claim);
//		entityManager.flush();
		
		String query = "SELECT POL_NO FROM  CLMV_POLICY_INTM  WHERE POL_NO ='" + policyNo+"'";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<String> policyNumber = (List<String>)nativeQuery.getResultList();
		
		if(policyNumber != null && ! policyNumber.isEmpty()){
			return true;
		}
		return false;

	}
	
public List<GmcMainMemberList> getGMCInsuredBasedOnMemberId(GmcMainMemberList insured){
		
		/*Query query = entityManager
				.createNamedQuery("GmcMainMemberList.findByMemberId");
		query.setParameter("memberId", memberId);
		//query.setParameter("insuredNumber", insuredNumber);
		List<GmcMainMemberList> result = (List<GmcMainMemberList>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result;
		}
		return null;*/
	
	Session session = (Session) entityManager.getDelegate();
	@SuppressWarnings("unchecked")
	List<GmcMainMemberList> selectValuesList = session.createCriteria(GmcMainMemberList.class)
	.add(Restrictions.eq("endorsementNumber", insured.getEndorsementNumber()))
	.add(Restrictions.eq("memberId", insured.getMemberId()))
	.add(Restrictions.eq("policyNumber", insured.getPolicyNumber()))
	.setProjection(Projections.projectionList()
			.add(Projections.property("riskId"), "riskId")
			.add(Projections.property("mainMemberName"), "mainMemberName")
			.add(Projections.property("insuredName"), "insuredName"))
			.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(GmcMainMemberList.class)).list();	
	return selectValuesList;
}

public Boolean isGmcInsuredDeleted(String policyNumber,Long insuredId){
	
	/*Query query = entityManager
			.createNamedQuery("GmcMainMemberList.findByMemberId");
	query.setParameter("memberId", memberId);
	//query.setParameter("insuredNumber", insuredNumber);
	List<GmcMainMemberList> result = (List<GmcMainMemberList>) query.getResultList();
	if(result != null && ! result.isEmpty()){
		return result;
	}
	return null;*/

Session session = (Session) entityManager.getDelegate();
@SuppressWarnings("unchecked")
List<GmcMainMemberList> selectValuesList = session.createCriteria(GmcMainMemberList.class)
.add(Restrictions.eq("memberId", insuredId))
.add(Restrictions.eq("policyNumber", policyNumber))
.add(Restrictions.eq("recType","D"))
.setProjection(Projections.projectionList()
		.add(Projections.property("riskId"), "riskId")
		.add(Projections.property("mainMemberName"), "mainMemberName")
		.add(Projections.property("insuredName"), "insuredName"))
		.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(GmcMainMemberList.class)).list();
if(selectValuesList != null && ! selectValuesList.isEmpty()){
	return true;
}
return false;
}

	public void updateCancelledPolicyApproval(ClaimWiseApprovalDto approvalDto, String userName){
		try{
			if(approvalDto.getIntimationNo() != null){
				
				
				Intimation intimationDetails = getIntimationByNo(approvalDto.getIntimationNo());
				if(intimationDetails.getKey() !=null){
					intimationDetails.setAllowApprovalFlag(approvalDto.getAllowedApproval());
					intimationDetails.setAllowApprovalComments(approvalDto.getApprovalComments());
					intimationDetails.setModifiedBy(userName);
					Status status = new Status();
					status.setKey(ReferenceTable.CLAIM_WISE_ALLOW_APPROVAL);
					intimationDetails.setStatus(status);
					intimationDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(intimationDetails);
					entityManager.flush();
					log.info("------Cancelled Policy Claim Allowed Flag----"+intimationDetails+"----------");
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public Boolean isGMCPolicyByPolicyNubember(String policyNumber){
		if(policyNumber != null){
			Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
			query.setParameter("policyNumber", policyNumber);
	
			List<Policy> resultList =  (List<Policy>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
				Policy policy = (Policy)resultList.get(0);
				if(policy != null && policy.getProduct() != null && ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey())){
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	public String getTopUpPolicyDetails(String policyNumber,SearchClaimRegistrationTableDto dto){
		   
		   DBCalculationService dbCalculationService = new DBCalculationService();
		   Map<String, Object> getTopUpPolicy = dbCalculationService.getTopUpPolicyDetails(policyNumber);
			String alertFlag = "";
			if(getTopUpPolicy != null){
				if(getTopUpPolicy.containsKey("alertFlag")){
					alertFlag = String.valueOf(getTopUpPolicy.get("alertFlag"));
					dto.setTopUpPolicyAlertFlag(alertFlag);
					if(null != alertFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(alertFlag)){
						String alertMsg = String.valueOf(getTopUpPolicy.get("alertMsg"));
						dto.setTopUpPolicyAlertMessage(alertMsg);
				}
			}
	  }
			return alertFlag;
	}
	
	
	public void updateCumulativeBonusFromWebService(PremBonusDetails details){
		
		//code for bonuslogicIntegration
	try{
		if(details != null && details.getPolicyNo()!= null)
		{
			Policy policydetails = getPolicyByPolicyNubember(details.getPolicyNo());
			if(policydetails.getKey()!= null)
			{
				if(details.getCumulativeBonus()!= null && ! details.getCumulativeBonus().isEmpty()){
				policydetails.setCummulativeBonus(Double.valueOf(details.getCumulativeBonus()));
				}
				else{
				if(details.getCumulativeBonus()== null || details.getCumulativeBonus().isEmpty()){
					policydetails.setCummulativeBonus(0d);
				}
				}
				if(details.getChequeStatus()!= null && !details.getChequeStatus().isEmpty()){
				policydetails.setChequeStatus(details.getChequeStatus());
				}
				entityManager.merge(policydetails);
				entityManager.flush();
			}
			if(details.getInsuredBonus()!= null && ! details.getInsuredBonus().isEmpty()){
			List<PremInsuredBonusDetails> insuredBonus = details.getInsuredBonus();
				for (PremInsuredBonusDetails premInsuredBonusDetails : insuredBonus) {
					String insuredNumber = premInsuredBonusDetails.getInsuredNumber();
					if(insuredNumber!= null && ! insuredNumber.isEmpty()){
					Insured insured= getInsuredByPolicyAndInsuredNameForDefault(details.getPolicyNo(),Long.valueOf(insuredNumber));
					if(premInsuredBonusDetails.getCumulativeBonus() != null && ! premInsuredBonusDetails.getCumulativeBonus().isEmpty()){
					insured.setCummulativeBonus(Double.valueOf(premInsuredBonusDetails.getCumulativeBonus()));
					entityManager.merge(insured);
					entityManager.flush();
					}
					else{
						if(premInsuredBonusDetails.getCumulativeBonus()== null || premInsuredBonusDetails.getCumulativeBonus().isEmpty()){
							insured.setCummulativeBonus(0d);
							entityManager.merge(insured);
							entityManager.flush();
						}
					}
				}
			  }
			}
		}
	}
		 catch(Exception e){
				e.printStackTrace();
	  }
	}
	public Boolean getTataPolicy(String policyNumber){
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return true;
		    }
		    return false;
	}
	public void updateBedPhotoDetails(MultipleUploadDocumentDTO dto, String intimationId){
		
		try{
			DocumentDetails referenceDocument = new DocumentDetails();
			referenceDocument.setFileName(dto.getFileName());
			referenceDocument.setDocumentToken(Long.valueOf(dto.getFileToken()));
			referenceDocument.setDocumentSource("BED PHOTO");
			referenceDocument.setDocumentType("BED Photo Details");
			if(intimationId!=null){
				referenceDocument.setIntimationNumber(intimationId);
			}

			referenceDocument.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
		
			referenceDocument.setDeletedFlag("N");
			referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			referenceDocument.setCreatedBy(dto.getUsername());
			entityManager.persist(referenceDocument);
			entityManager.flush();
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	public List<UploadBedPhotoDTO> getBedPhotoDetails(Long intimationKey){
		
		
		List<MultipleUploadDocumentDTO> updateDocumentDetails = getUpdateDocumentDetails(intimationKey);
		Integer sno = 1;
		List<UploadBedPhotoDTO> list = new ArrayList<UploadBedPhotoDTO>();
		for (MultipleUploadDocumentDTO documentDto : updateDocumentDetails) {
			if(documentDto.getFileName() != null){
				UploadBedPhotoDTO dto = new UploadBedPhotoDTO();
				dto.setSno(sno);
				dto.setFileName(documentDto.getFileName());
				dto.setToken(documentDto.getFileToken());
				dto.setFileType("");
				dto.setUploadBy(documentDto.getUsername());
				dto.setUploadDate(documentDto.getUploadedDate());
				list.add(dto);
			}
		}
		return list;
	}
	
	public NomineeDetails getNomineeDetailsByInsuredId(Long insuredKey){
		
		NomineeDetails result = null;	
		List<NomineeDetails> resultList = getNomineeListForInsured(insuredKey);
		
		if(resultList != null 
				&& !resultList.isEmpty() 
//				&& resultList.get(0).getNomineeAge() > 17) {
				&& resultList.get(0).getNomineeName() != null
				&& !resultList.get(0).getNomineeName().isEmpty()) {
			result = resultList.get(0);
		}
		else {
			
			//TODO Appointee / Gaurdian Details to be Fetched
		}
		
		
		return result;
	}

	private List<NomineeDetails> getNomineeListForInsured(Long insuredKey) {
		
		List<NomineeDetails> resultList = new ArrayList<NomineeDetails>();
		
		try{
			Query query = entityManager.createNamedQuery("NomineeDetails.findByInsuredKey");
			query = query.setParameter("insuredKey", insuredKey);
			
			resultList = (List<NomineeDetails>) query.getResultList();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resultList;
	}

	
	public NomineeDetails getNomineeDetailsByInsuredId(Long insuredKey, EntityManager em) {
		this.entityManager = em;
		return getNomineeDetailsByInsuredId(insuredKey);
	}

	
public void getDBTaskForSTPProcess(String currentQ){
		
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
		    
			for (Map<String, Object> outPutArray : taskProcedure) {
				
				Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
				String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
				processSTPRecord(keyValue, Double.valueOf(claimedAmount));
				String outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_APPROVE;
				outPutArray.put(SHAConstants.OUTCOME,outCome);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(outPutArray);
				db.revisedInitiateTaskProcedure(objArrayForSubmit);
				
			}
		} 
		
	}
	
	public void processSTPRecord(Long cashlessKey, Double approvedAmt){
		
		List<PedValidation> diagnosisByTransactionKey = getDiagnosisByTransactionKey(cashlessKey);
		if(diagnosisByTransactionKey != null){
			for (PedValidation pedValidation : diagnosisByTransactionKey) {
				pedValidation.setAmountConsideredAmount(approvedAmt);
				pedValidation.setMinimumAmount(approvedAmt);
				pedValidation.setNetAmount(approvedAmt);
				pedValidation.setNetApprovedAmount(approvedAmt);
				pedValidation.setApproveAmount(approvedAmt);
				Status status = new Status();
				status.setKey(ReferenceTable.PREAUTH_APPROVE_STATUS);
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.PREAUTH_STAGE);
				pedValidation.setStage(stage);
				pedValidation.setStatus(status);
				pedValidation.setModifiedBy("SYSTEM");
				pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(pedValidation);
				entityManager.flush();
				
				
				Preauth preauthById = getPreauthById(cashlessKey);
				if(preauthById != null){
					preauthById.setTotalApprovalAmount(approvedAmt);
					preauthById.setStage(stage);
					preauthById.setStatus(status);
					preauthById.setModifiedBy("SYSTEM");
					preauthById.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					preauthById.setInitiateFvr(0);
					preauthById.setSpecialistOpinionTaken(0);
					preauthById.setFvrNotRequiredRemarks(null);
				    entityManager.merge(preauthById);
				    entityManager.flush();
				    
				    ResidualAmount residualAmt = new ResidualAmount();
					residualAmt.setTransactionKey(cashlessKey);
					residualAmt.setAmountConsideredAmount(0d);
					residualAmt.setMinimumAmount(0d);
					residualAmt.setCopayPercentage(0d);
					residualAmt.setCopayAmount(0d);
					residualAmt.setNetAmount(0d);
					residualAmt.setApprovedAmount(0d);
					Stage stageId = new Stage();
					stageId.setKey(ReferenceTable.PREAUTH_STAGE);
					residualAmt.setStage(stageId);
					Status statusId = new Status();
					statusId.setKey(ReferenceTable.PREAUTH_APPROVE_STATUS);
					residualAmt.setStatus(statusId);
					residualAmt.setCreatedBy("SYSTEM");
					residualAmt.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					residualAmt.setNetApprovedAmount(0d);
										
					entityManager.persist(residualAmt);
					entityManager.flush();
					
					letterGenationOfSTPProcess(preauthById,pedValidation);
				}
				
			}
		}
		
	}
	
	public void letterGenationOfSTPProcess(Preauth preauth,PedValidation pedValidation)
	{
		PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
		setClaimValuesToDTO(preauthDTO, preauth.getClaim());
		NewIntimationDto newIntimationDto = getIntimationDto(preauth.getClaim().getIntimation());
		ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(preauth.getClaim());
		preauthDTO.setNewIntimationDTO(newIntimationDto);
		preauthDTO.setClaimDTO(claimDTO);
		if(null != preauth && null != preauth.getTotalApprovalAmount()){
			preauthDTO.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(preauth.getTotalApprovalAmount());
			String amtInwords = SHAUtils.getParsedAmount(preauth.getTotalApprovalAmount());	
			preauthDTO.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			preauthDTO.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(Boolean.TRUE);
			if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)){
				amtInwords = SHAUtils.getParsedAmount(preauthDTO.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
				preauthDTO.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
				preauthDTO.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
			}
		}
		
		if(preauth.getOtherBenefitFlag() != null && preauth.getOtherBenefitFlag().equalsIgnoreCase("Y") && (null != preauth.getOtherBenefitApprovedAmt()))
		{
			String amtInwords = SHAUtils.getParsedAmount(preauth.getTotalApprovalAmount() + preauth.getOtherBenefitApprovedAmt());	
			preauthDTO.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
		
		if(preauthDTO.getNewIntimationDTO().getHospitalDto() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null)
		{
			if(preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getName() != null){
				String hospitalName = preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getName();
				/*hospitalName = StringUtils.replace(hospitalName,"&","-");
				preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().setName(hospitalName);
				*//*hospitalName = hospitalName.replaceAll("&", "_");
				preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().setName(hospitalName);*/
			}
		}
		
		if(null != pedValidation){
			Query diagnosis = entityManager
					.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey",
					pedValidation.getDiagnosisId());
			List<Diagnosis> masters = (List<Diagnosis>) diagnosis.getResultList();
			if (masters != null && !masters.isEmpty()) {
				preauthDTO.getPreauthDataExtractionDetails().setDiagnosis(masters.get(0).getValue());
			}
			
			//STP ADDed by pavithran 15/05/2021
			if(pedValidation != null && pedValidation.getIcdCodeId() != null ){
				
				preauthDTO.getNewIntimationDTO().setIcdLetterStatus(2);
				/*String insuranceDiagnosisCode = pedValidation.getIcdCodeId().toString();
				System.out.println("insuranceDiagnosisCode.."+insuranceDiagnosisCode);

				if(ReferenceTable.getCovidInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
					if(preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getId() != null &&
						preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getId().equals(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL_KEY))
				{
					preauthDTO.getNewIntimationDTO().setIcdLetterStatus(1);
				}
				else 
					if(preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getId() != null &&
							preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getId().equals(ReferenceTable.ONLY_PRE_AUTH_KEY)){
						preauthDTO.getNewIntimationDTO().setIcdLetterStatus(2);
					}else
					{
						preauthDTO.getNewIntimationDTO().setIcdLetterStatus(0);
					}
				}*/
			}
			else
			{
				preauthDTO.getNewIntimationDTO().setIcdLetterStatus(0);
			}
			
		}
		
		//CR2019134
		/*List<PreviousPreAuthTableDTO> searchPrevPreAuthReport = searchPrevPreAuthReport(preauthDTO.getIntimationKey());
		preauthDTO.setPreviousPreauthTableDTOReportList(searchPrevPreAuthReport);*/
		
		Double approvedAmount =0d;
		List<PreviousPreAuthTableDTO> reportLit = new ArrayList<PreviousPreAuthTableDTO>();
		//if(preauthDTO.getPreviousPreauthTableDTOReportList().isEmpty()){
			PreviousPreAuthTableDTO previousPreAuthTableDTOReport = new PreviousPreAuthTableDTO();			
			previousPreAuthTableDTOReport.setModifiedDate(new Date());
			if(preauthDTO.getPreauthMedicalDecisionDetails() != null && preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
				approvedAmount = preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();	
				if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)){
					approvedAmount = preauthDTO.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium();	
				}
			}
			
			if(preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null && preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt() && preauthDTO.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null){
				approvedAmount = preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + preauthDTO.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt();	
			}
			previousPreAuthTableDTOReport.setApprovedAmt(String.valueOf(approvedAmount));
			previousPreAuthTableDTOReport.setStatus("Approved (Pre Auth)");
			previousPreAuthTableDTOReport.setReferenceNo(preauthDTO.getClaimNumber() + "/001");
			reportLit.add(previousPreAuthTableDTOReport);
			preauthDTO.setPreviousPreauthTableDTOReportList(reportLit);
			
		//}
		
		int totalApprovedAmount = 0;
		 List<PreviousPreAuthTableDTO> previousPreAuthTableDTO = preauthDTO.getPreviousPreauthTableDTOReportList();
			for(PreviousPreAuthTableDTO deduction :previousPreAuthTableDTO){
				double amount = Double.valueOf(deduction.getApprovedAmt());
				totalApprovedAmount = totalApprovedAmount + Double.valueOf(deduction.getApprovedAmt()).intValue();		
			}
			preauthDTO.setTotalApprovedAmount(totalApprovedAmount);
			String amtInwords = SHAUtils.getParsedAmount(totalApprovedAmount);	
			preauthDTO.setTotalApprovedAmountInWords(amtInwords);
			
			
		// CR2019187 - Commented as per CR2019187
			
		/*List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = findClaimAmountDetailsByPreauthKey(preauthDTO.getKey());
		preauthDTO.getPreauthDataExtractionDetails().setClaimedDetailsList(premedicalMapper.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));	
		
		List<NoOfDaysCell> copyClaimeDetailsList = SHAUtils.copyClaimeDetailsList(preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList());
		preauthDTO.getPreauthDataExtractionDetails().setClaimedDetailsListForBenefitSheet(copyClaimeDetailsList);*/
			
		preauthDTO.setStpApplicable(Boolean.TRUE);	
		preauthDTO.getPreauthDataExtractionDetails().setPreAuthType(new SelectValue(preauth.getStpProcessLevel(),""));
		
		int otherDeduction = 0;
		if(!preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
			 List<NoOfDaysCell> claimedDetailsListForBenefitSheet = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet();
			for(NoOfDaysCell deduction :claimedDetailsListForBenefitSheet){
				otherDeduction = otherDeduction + deduction.getNonPayableAmount() + deduction.getDeductibleAmount();		
			}
			preauthDTO.setOtherDeductionAmount(otherDeduction);
		}
		//STP ADDed by pavithran 15/05/2021
		//added for cr GLX2020124 by noufel
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(),0l,0l);
		Long versionNumber =1l;
		if(getproductUINvalues != null){
			if(getproductUINvalues.containsKey("productversionNumber")){
				versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
				preauthDTO.setProductVersionNumber(versionNumber);
			}
			if(getproductUINvalues.containsKey("productUINnumber")){
				preauthDTO.setProductUinNumber((String) getproductUINvalues.get("productUINnumber")); 
			}
		}
		String existingIrda = preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getIrdaCode();
		//added for UIN number change in letter template by noufel 
		if(preauthDTO.getProductUinNumber() != null && ! preauthDTO.getProductUinNumber().isEmpty()){
			preauthDTO.getNewIntimationDTO().getPolicy().getProduct().setIrdaCode(preauthDTO.getProductUinNumber());
		}
		
		
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(preauthDTO);		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(preauthDTO.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
		String filePath = "";
		
		if(ReferenceTable.PREAUTH_APPROVE_STATUS.equals(preauthDTO.getStatusKey())){
			String templateName = "PreauthInitialApprovalLetter";
			/*if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
				templateName = "PreauthInitialApprovalLetterPaayas";
			}*/
			
			//STP ADDed by pavithran 15/05/2021
			MasHospitals hospitalDetails = getHrmDetails(preauthDTO.getNewIntimationDTO().getHospitalDto().getHospitalCode());
			
			if(hospitalDetails != null)
			{
				preauthDTO.getNewIntimationDTO().setHrmUserName(hospitalDetails.getHrmUserName());
				preauthDTO.getNewIntimationDTO().setHrmContactNo(hospitalDetails.getHrmContactNo());
				preauthDTO.getNewIntimationDTO().setHrmHeadName(hospitalDetails.getHrmHeadName());
				preauthDTO.getNewIntimationDTO().setHrmHeadContactNo(hospitalDetails.getHrmHeadContactNo());
			}
			
			preauthDTO.getNewIntimationDTO().setCscMemberNameContactNo(getCSCMemberDetails(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode())));
						
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			preauthDTO.setDocType(SHAConstants.PREAUTH_INTIAL_APPROVAL_LETTER);
		}
		
		final String finalFilePath = filePath;
		preauthDTO.setDocFilePath(finalFilePath);
		preauthDTO.setDocSource(SHAConstants.PRE_AUTH);
		
		WeakHashMap dataMap = new WeakHashMap();
		dataMap.put("intimationNumber",preauth.getClaim().getIntimation().getIntimationId());
		dataMap.put("claimNumber",preauth.getClaim().getClaimId());
		dataMap.put("cashlessNumber", preauth.getPreauthId());
		dataMap.put("filePath", preauthDTO.getDocFilePath());
		dataMap.put("docType", preauthDTO.getDocType());
		dataMap.put("docSources", SHAConstants.PRE_AUTH);
		dataMap.put("createdBy", "SYSTEM");
		preauthDTO.getNewIntimationDTO().getPolicy().getProduct().setIrdaCode(existingIrda);
		String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
	}
	
	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = getHospitalByKey(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
		preauthDTO.setClaimDTO(ClaimMapper.getInstance().getClaimDto(claimByKey));
	}
	
	 public List<PedValidation> getDiagnosisByTransactionKey(Long transactionKey) {	
	 		

	  		List<PedValidation> resultList = new ArrayList<PedValidation>();
	  		
	  		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
	  		query.setParameter("transactionKey", transactionKey);
	  		
	  		resultList = (List<PedValidation>)query.getResultList();
	  	    
	  		return resultList;

	  	}
	 
		public Preauth getPreauthById(Long preauthKey) {
			Query query = entityManager.createNamedQuery("Preauth.findByKey");
			query.setParameter("preauthKey", preauthKey);
			@SuppressWarnings("unchecked")
			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
			if(singleResult != null && ! singleResult.isEmpty()) {
				
			entityManager.refresh(singleResult.get(0));
		
				return singleResult.get(0);
			}
			
			return null;
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

		public NewIntimationDto getOPIntimationDto(OPIntimation intimation) {
			NewIntimationDto intimationToRegister = null;
			if (null != intimation) {
				NewIntimationMapper newIntimationMapper = NewIntimationMapper.getInstance();
				intimationToRegister = newIntimationMapper.getNewIntimationDto(intimation);
				intimationToRegister.setIntimationSource(intimation
						.getIntimationSource());
				if (null != intimation.getPolicy()) {
					DBCalculationService dbCalcService = new DBCalculationService();
					if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){

						if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
							List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());

							if(null != insuredList && !insuredList.isEmpty()){
								intimation.getPolicy().setInsured(insuredList);

							}
						}
						if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
							Insured insuredByKey = getInsuredByKey(intimation.getInsured().getKey());
							Insured MainMemberInsured = null;

							if(insuredByKey.getDependentRiskId() == null){
								MainMemberInsured = insuredByKey;
							}else{
								Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredId(intimation.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
								MainMemberInsured = insuredByPolicyAndInsuredId;
							}

							if(MainMemberInsured != null){
								intimationToRegister.setGmcMainMemberName(MainMemberInsured.getInsuredName());
								intimationToRegister.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
								if(MainMemberInsured.getInsuredAge() != null){
									intimationToRegister.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
								}

								//IMSSUPPOR-27098
								/**
								 * Part of CR R1186
								 */

								 intimation.getInsured().setAddress1(MainMemberInsured.getAddress1());
								 intimation.getInsured().setAddress2(MainMemberInsured.getAddress2());
								 intimation.getInsured().setAddress3(MainMemberInsured.getAddress3());
								 intimation.getInsured().setCity(MainMemberInsured.getCity());
								 intimation.getInsured().setInsuredPinCode(MainMemberInsured.getInsuredPinCode());
								 intimation.getInsured().setInsuredState(MainMemberInsured.getInsuredState());
							}	

							String colorCodeForGMC = getColorCodeForGMC(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId().toString());
							intimationToRegister.setColorCode(colorCodeForGMC);
							IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(intimation.getPolicy().getPolicyNumber());
							if(incurredClaimRatio != null && incurredClaimRatio.getEarnedPremium() != null){
								intimationToRegister.setIcrEarnedPremium(incurredClaimRatio.getClaimRatio().toString());
							}

							if(null != intimation.getPolicy().getPolicyNumber()){

								Boolean isPaayasPlicy = getPaayasPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isPaayasPlicy){
									intimationToRegister.setIsPaayasPolicy(Boolean.TRUE);
								}
							}

							if(null != intimation.getPolicy().getPolicyNumber()){

								Boolean isJioPlicy = getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
								if(isJioPlicy){
									intimationToRegister.setIsJioPolicy(Boolean.TRUE);
								}
							}

							if(null != intimation.getPolicy().getPolicyNumber()){

								Boolean isTataPolicy = getTataPolicy(intimation.getPolicy().getPolicyNumber());
								if(isTataPolicy){
									intimationToRegister.setIsTataPolicy(Boolean.TRUE);
								}
							}


							String isClaimManuallyProcessed = dbCalcService.isClaimManuallyProcessed(intimationToRegister.getInsuredPatient().getHealthCardNumber());
							if (null != isClaimManuallyProcessed && SHAConstants.YES_FLAG.equalsIgnoreCase(isClaimManuallyProcessed)) {
								intimationToRegister.setIsClaimManuallyProcessed(Boolean.TRUE);
							}
						}

					}

					if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())
							|| ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
						/*Below Query commented as per premia revised table
						Boolean gmcInsuredDeleted = isGmcInsuredDeleted(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId());*/
						Boolean gmcInsuredDeleted = dbCalcService.getGMCInsuredDeleted(intimation.getPolicy().getPolicyNumber(), intimation.getInsured().getInsuredId());

						intimationToRegister.setIsDeletedRisk(gmcInsuredDeleted);
					}
					intimationToRegister.setInsuredKey(intimation.getInsured().getKey());
					String address = (intimation.getPolicy().getProposerAddress1() != null ? intimation.getPolicy().getProposerAddress1() : "") + 
							(intimation.getPolicy().getProposerAddress2() != null ? intimation.getPolicy().getProposerAddress2() : "") + 
							(intimation.getPolicy().getProposerAddress3() != null ? intimation.getPolicy().getProposerAddress3() : "" );

					intimation.getPolicy().setProposerAddress(address);				

					intimationToRegister.setPolicy(intimation.getPolicy());	

					if(intimation.getPolicy() != null && intimation.getPolicy().getPolicyNumber() != null){
						Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
						intimationToRegister.setPolicy(policy);
					}

					OrganaizationUnit orgUnit = getOrganizationUnit(intimation.getPolicy().getHomeOfficeCode());
					if(orgUnit != null){
						intimationToRegister
						.setOrganizationUnit(orgUnit);
						if(orgUnit.getParentOrgUnitKey() != null){
							OrganaizationUnit parentUnit = 	getOrganizationUnit(orgUnit.getParentOrgUnitKey().toString());	
							intimationToRegister.setParentOrgUnit(parentUnit);
						}					
					}

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
					intimationToRegister.setInsuredPatient(intimation.getInsured());
					intimationToRegister.setInsuredAge(intimationToRegister.getInsuredCalculatedAge());

					NomineeDetails nomineeObj = entityManager.find(
							NomineeDetails.class, intimation.getInsured().getKey());

					if(nomineeObj != null){
						intimationToRegister.setNomineeName(nomineeObj.getNomineeName());
					}
				}

				Hospitals registeredHospital = null;
				TmpHospital tempHospital = null;

				if (intimation.getHospital() != null
						&& intimation.getHospitalType() != null
						&& StringUtils.contains(intimation.getHospitalType()
								.getValue().toLowerCase(), "network")) {
					registeredHospital = getHospitalDetailsByKey(intimation.getHospital());
					if (null != registeredHospital) {
						HospitalDto hospitaldto = new HospitalDto(
								registeredHospital);
						hospitaldto.setRegistedHospitals(registeredHospital);
						intimationToRegister.setHospitalType(hospitaldto.getHospitalType());
						intimationToRegister.setHospitalTypeValue(hospitaldto.getHospitalType() != null ?hospitaldto.getHospitalType().getValue() : "");
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

				}

				/**
				 * For PA the claim type should be populated based on intimation table data.
				 * For Health, it should be populated based on hospital type.
				 * Hence if the claim type is PA, then we skip the below hospital type
				 * based validation.
				 * */
				if(intimation.getIncidenceFlag() == null || (intimation.getIncidenceFlag() != null && (SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(intimation.getIncidenceFlag())))
				{
					if (intimation.getHospitalType() != null){
						SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
						intimationToRegister.setClaimType(claimType);
					}
				}
				else {
					SelectValue claimType = getClaimType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
					intimationToRegister.setClaimType(claimType);
				}


				TmpCPUCode cpuObject = intimation.getCpuCode();

				if (null != cpuObject) {
					intimationToRegister.setCpuCode(cpuObject.getCpuCode());
					intimationToRegister.setCpuId(cpuObject.getKey());
					intimationToRegister.setCpuAddress(cpuObject.getAddress());
					intimationToRegister.setReimbCpuAddress(cpuObject.getReimbAddress());
					intimationToRegister.setTollNumber(cpuObject.getTollNumber());
	                intimationToRegister.setWhatsupNumber(cpuObject.getWhatsupNumber());
	                intimationToRegister.setPageFooter(cpuObject.getPageFooter());
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

						} 
						/*else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
						.getValue(), "not-registered")) {
//					if (tempHospital.getPincode() != null) {
//						cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//								.toString());
//					}
				}*/
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

						if (intimation.getRoomCategory() != null) {
							SelectValue roomCategory = new SelectValue();
							roomCategory.setId(intimation.getRoomCategory().getKey());
							roomCategory.setValue(intimation.getRoomCategory().getValue());
							intimationToRegister.setRoomCategory(roomCategory);
						}
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

						}
						intimationToRegister.setStatus(intimation.getStatus());
						intimationToRegister.setStage(intimation.getStage());
			}

			DBCalculationService dbCalculationService = new DBCalculationService();
			if(intimation.getInsured() != null && intimation.getPolicy() != null){
				if(intimation.getPolicy() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
					Double insuredSumInsured = dbCalculationService.getInsuredSumInsuredForGMC(intimation.getPolicy().getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode());
					intimationToRegister.setOrginalSI(insuredSumInsured);
				}else if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.GPA_PRODUCT_KEY)){
					Double insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey());
					intimationToRegister.setOrginalSI(insuredSumInsured);
				}else{
					Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag());
					intimationToRegister.setOrginalSI(insuredSumInsured);
				}

			}

			Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
			if (portablityStatus != null) {
				intimationToRegister.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
				intimationToRegister.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
			}


			return intimationToRegister;
		}
		
		public List<NomineeDetailsDto> getNomineeForPolicyKey(Long policyKey, EntityManager em){
			this.entityManager = em;
			return getNomineeForPolicyKey(policyKey);
		}
		
		public List<NomineeDetailsDto> getNomineeForPolicyKey(Long policyKey){
			
			List<NomineeDetailsDto> nomineeDetailsDtoList = new ArrayList<NomineeDetailsDto>();
			

//			List<ProposerNominee> proposerNomineeList = getProposerNomineeList(policyKey);
			
//			if(proposerNomineeList != null && !proposerNomineeList.isEmpty()) {
//				NomineeDetailsDto nomineeDetailsDto = null;
//				for (ProposerNominee nomineeDetails : proposerNomineeList) {
//					nomineeDetailsDto = new NomineeDetailsDto(nomineeDetails);					
//					nomineeDetailsDto.setSno(String.valueOf(proposerNomineeList.indexOf(nomineeDetails)+1));
//					nomineeDetailsDtoList.add(nomineeDetailsDto);
//				}				
//			}
			
			
			List<PolicyNominee> policyNomineeList = getPolicyNomineeList(policyKey);
			nomineeDetailsDtoList = getNomineeDetailsListDto(policyNomineeList);
			
			return nomineeDetailsDtoList;		
			
		}

		private List<NomineeDetailsDto> getNomineeDetailsListDto(List<PolicyNominee> policyNomineeList) {
			
			List<NomineeDetailsDto> nomineeDetailsDtoList = new ArrayList<NomineeDetailsDto>();
			if(policyNomineeList != null && !policyNomineeList.isEmpty()) {
				NomineeDetailsDto nomineeDetailsDto = null;
				BankMaster bankMaster=null;
				for (PolicyNominee nomineeDetails : policyNomineeList) {
					nomineeDetailsDto = new NomineeDetailsDto(nomineeDetails);
					if(nomineeDetailsDto.getIfscCode() != null){
						
						bankMaster=getBankDetails(nomineeDetailsDto.getIfscCode());
						if(bankMaster != null){
							nomineeDetailsDto.setBankName(bankMaster.getBankName());
							nomineeDetailsDto.setBankBranchName(bankMaster.getBranchName());
						}
					}
					nomineeDetailsDto.setSno(String.valueOf(policyNomineeList.indexOf(nomineeDetails)+1));
					nomineeDetailsDtoList.add(nomineeDetailsDto);
				}				
			}
			
			return nomineeDetailsDtoList;
		}
		
		private List<ProposerNominee> getProposerNomineeList(Long policyKey) {
			
			List<ProposerNominee> resultList = new ArrayList<ProposerNominee>();
			
			try{
				Query query = entityManager.createNamedQuery("ProposerNominee.findByPolicy");
				query = query.setParameter("policyKey", policyKey);
				
				resultList = (List<ProposerNominee>) query.getResultList();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
		
		public List<PolicyNominee> getPolicyNomineeList(Long policyKey) {
			
			List<PolicyNominee> resultList = new ArrayList<PolicyNominee>();
			
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByPolicy");
				query = query.setParameter("policyKey", policyKey);
				
				resultList = (List<PolicyNominee>) query.getResultList();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
		
		public List<PolicyNominee> getPolicyNomineeListForInsured(Long insuredKey) {
			
			List<PolicyNominee> resultList = new ArrayList<PolicyNominee>();
			
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByInsuredKey");
				query = query.setParameter("insuredKey", insuredKey);
				
				resultList = (List<PolicyNominee>) query.getResultList();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
		
		public List<ProposerNominee> getNomineeListByTransactionKey(Long transacKey) {
			
			List<ProposerNominee> resultList = new ArrayList<ProposerNominee>();
			
			try{
				Query query = entityManager.createNamedQuery("ProposerNominee.findByTransacKey");
				query = query.setParameter("transacKey", transacKey);
				
				resultList = (List<ProposerNominee>) query.getResultList();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
		
		public List<NomineeDetailsDto> getNomineeDetailsListByTransactionKey(Long transacKey, EntityManager em) {
			this.entityManager = em;
			
			return getNomineeDetailsListByTransactionKey(transacKey);
		}
		
		public List<NomineeDetailsDto> getNomineeDetailsListByTransactionKey(Long transacKey) {
			
			List<ProposerNominee> proposerNomineeList = getNomineeListByTransactionKey(transacKey);
			
			return getProposerNomineeDetailsListDto(proposerNomineeList);
		}
		
		private List<NomineeDetailsDto> getProposerNomineeDetailsListDto(List<ProposerNominee> proposerNomineeList) {
			
			List<NomineeDetailsDto> nomineeDetailsDtoList = new ArrayList<NomineeDetailsDto>();
			if(proposerNomineeList != null && !proposerNomineeList.isEmpty()) {
				NomineeDetailsDto nomineeDetailsDto = null;
				BankMaster bankMaster=null;
				for (ProposerNominee nomineeDetails : proposerNomineeList) {
					nomineeDetailsDto = new NomineeDetailsDto(nomineeDetails);
					
					if(nomineeDetailsDto.getIfscCode() != null){
						
						bankMaster=getBankDetails(nomineeDetailsDto.getIfscCode());
						if(bankMaster != null){
							nomineeDetailsDto.setBankName(bankMaster.getBankName());
							nomineeDetailsDto.setBankBranchName(bankMaster.getBranchName());
						}
					}
					nomineeDetailsDto.setSno(String.valueOf(proposerNomineeList.indexOf(nomineeDetails)+1));
					nomineeDetailsDtoList.add(nomineeDetailsDto);
				}				
			}
			return nomineeDetailsDtoList;
		}
		

		public List<NomineeDetailsDto> getProposerNomineeDetailsForInsured(Long insuredKey){
				List<NomineeDetailsDto> nomineeDetailsDtoList = new ArrayList<NomineeDetailsDto>();
				List<PolicyNominee> insuredNomineeList = getPolicyInsuredNomineeList(insuredKey);
				nomineeDetailsDtoList = getNomineeDetailsListDto(insuredNomineeList);
				if(nomineeDetailsDtoList != null && !nomineeDetailsDtoList.isEmpty())
				{
					return nomineeDetailsDtoList;	
				}		
				return null;
		}
	private List<PolicyNominee> getPolicyInsuredNomineeList(Long insuredKey) {
			
			List<PolicyNominee> resultList = new ArrayList<PolicyNominee>();
			
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByInsuredKey");
				query = query.setParameter("insuredKey", insuredKey);
				
				resultList = (List<PolicyNominee>) query.getResultList();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
	
	public Insured getInsuredByPolicyAndInsuredIdForGMC(String policyNo , Long insuredId) {
		
		Insured insuredResult = null;
		try{
			Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNoForDefault");
			query = query.setParameter("policyNo", policyNo);
			if(null != insuredId){
				query = query.setParameter("insuredId", insuredId);
				List<Insured> insuredList = query.getResultList();
				insuredList = query.getResultList();
				if(null != insuredList && !insuredList.isEmpty()) {					
					
					if(insuredList.size()>1){
						for (Insured insured : insuredList) {
							if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
								insuredResult = insured;
								break;
							}
							else if(insured.getLopFlag() == null || !SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(insured.getLopFlag())){
								insuredResult = insured;
								break;					
							}
						}
						if(insuredResult == null){
							insuredResult = insuredList.get(0);
						}
					}
					else{
						insuredResult = insuredList.get(0);
					}					
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return insuredResult;
	}
	
	
	public List<Insured> getInsuredGMC(Long policyKey){
		if(policyKey != null){
			Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
			query.setParameter("policykey", policyKey);
	
			List<Insured> resultList =  (List<Insured>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
//				entityManager.refresh(resultList);
				return resultList;
			}
		}
		return null;
	}
	public GmcMainMemberList getMemberId(Long riskId,String policyNumber){
		if(riskId != null){
			Query query = entityManager.createNamedQuery("GmcMainMemberList.findByRiskId");
			query.setParameter("riskId", riskId);
			query.setParameter("policyNumber", policyNumber);
	
			List<GmcMainMemberList> resultList =  (List<GmcMainMemberList>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()){
				return resultList.get(0);
			}
		}
		return null;
	}
	
	public List<GmcMainMemberList> getListByMemberId(Long memberId,String policyNumber){
		if(memberId != null){
			Query query = entityManager.createNamedQuery("GmcMainMemberList.findByMemberId");
			query.setParameter("memberId", memberId);
			query.setParameter("policyNumber", policyNumber);
			List<GmcMainMemberList> resultList =  (List<GmcMainMemberList>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
				return resultList;
			}
		}
		return null;
	}
	
	public List<PreviousPreAuthTableDTO> searchPrevPreAuthReport(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByStatusAndIntimation");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		List<PreviousPreAuthTableDTO> processEnhancementTableDTOList = PreviousPreAuthMapper.getInstance()
				.getProcessEnhacementTableDTO(resultList);
		return processEnhancementTableDTOList;
		
	}
	
	public List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey(
			Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<ClaimAmountDetails> resultList = (List<ClaimAmountDetails>) query
				.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (ClaimAmountDetails claimAmountDetails : resultList) {
				entityManager.refresh(claimAmountDetails);
			}
			
		}
		return resultList;
	}
	
	public List<NomineeDetailsDto> getNomineeForInsured(Long insuredKey){
		
		List<NomineeDetailsDto> nomineeDetailsDtoList = new ArrayList<NomineeDetailsDto>();
		
		Insured insuredByKey = getInsuredByKey(insuredKey);
	      Insured MainMemberInsured = null;
	      
	      if(insuredByKey.getDependentRiskId() == null){
	    	  MainMemberInsured = insuredByKey;
	      }else{
	    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredNameForDefault(insuredByKey.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
	    	  MainMemberInsured = insuredByPolicyAndInsuredId;
	      }
		
		List<PolicyNominee> policyNomineeList = getPolicyNomineeListForInsured(MainMemberInsured.getKey());
		nomineeDetailsDtoList = getNomineeDetailsListDto(policyNomineeList);
		
		return nomineeDetailsDtoList;		
		
	}
	
	public BankMaster getBankDetails(String ifscCode)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> masBank = (List<BankMaster>)query.getResultList();
		if(masBank != null && ! masBank.isEmpty()){
			return masBank.get(0);
		}
		return null;
	}
	
	public String createJWTTokenForClaimStatusPages(Map<String, String> userMap) throws NoSuchAlgorithmException, ParseException{	
		String token = "";	
		try {	
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");	
			keyGenerator.initialize(1024);	
		
			KeyPair kp = keyGenerator.genKeyPair();	
			RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();	
			RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();	
		
		
			JWSSigner signer = new RSASSASigner(privateKey);	
		
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();	
			claimsSet.issuer("galaxy");	
			if(userMap.get("intimationNo") != null){
				claimsSet.claim("intimationNo", userMap.get("intimationNo"));	
			}
			if(userMap.get("reimbursementkey") != null){
				claimsSet.claim("reimbursementkey", userMap.get("reimbursementkey"));
			}
			if(userMap.get("acknowledgementNo") !=null){
				claimsSet.claim("acknowledgementNo", userMap.get("acknowledgementNo"));
			}
			if(userMap.get("ompdoc") != null){
				claimsSet.claim("ompdoc", userMap.get("ompdoc"));
			}
			if(userMap.get("cashlessDoc") != null){
				claimsSet.claim("cashlessDoc", userMap.get("cashlessDoc"));
			}
			if(userMap.get("lumenKey") != null){
				claimsSet.claim("lumenKey", userMap.get("lumenKey"));
			}
			claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));	
		
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());	
		
			signedJWT.sign(signer);	
			token = signedJWT.serialize();	
			signedJWT = SignedJWT.parse(token);	
		
			JWSVerifier verifier = new RSASSAVerifier(publicKey);	
				
			return token;	
		} catch (JOSEException ex) {	
				
		}	
		return null;	
		
	 }

	public Page<PaymentProcessCpuPageDTO> searchIntimationForUPRView(SearchIntimationFormDto searchDto) {
		
		Map<String, Object> params = searchDto.getFilters();
		Pageable apageable = params.containsKey("pageable") ? (Pageable)params.get("pageable") : null;
		
		int pageNumber = apageable != null ? apageable.getPageNumber() : 1;
		List<Intimation> resultList = new ArrayList<Intimation>();
		Page<PaymentProcessCpuPageDTO> pagedIntimationListContainer = new Page<PaymentProcessCpuPageDTO>();
		List<PaymentProcessCpuPageDTO> intimationUPRDtoList = new ArrayList<PaymentProcessCpuPageDTO>();
		NewIntimationDto newIntimationDto = null;
		PaymentProcessCpuPageDTO paymentDto = null;
		int firstResult = 1;
		
		if (!params.isEmpty()) {
		
			List<ClaimPayment> paymentList = searchPaymentforIntimationUprView(params);
			
			if(paymentList != null && !paymentList.isEmpty()) {
				for (ClaimPayment clmPayment : paymentList) {
					
					Intimation intimationObj = getIntimationByNo(clmPayment.getIntimationNumber());
					newIntimationDto = getIntimationDto(intimationObj);
					
					PaymentProcessCpuTableDTO paymentProcessDto = new PaymentProcessCpuTableDTO(); 
					paymentProcessDto.setAccountNumber(clmPayment.getAccountNumber());
					paymentProcessDto.setAmount(clmPayment.getApprovedAmount());
					paymentProcessDto.setBankName(clmPayment.getBankName());
					paymentProcessDto.setBranchName(clmPayment.getBranchName());
					paymentProcessDto.setChequeDate(clmPayment.getChequeDDDate());
					paymentProcessDto.setChequeDateValue(clmPayment.getChequeDDDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmPayment.getChequeDDDate()) : "");
					paymentProcessDto.setChequeNo(clmPayment.getChequeDDNumber());
					paymentProcessDto.setClaimPaymentKey(clmPayment.getKey());
					paymentProcessDto.setClaimType(clmPayment.getClaimType());
					paymentProcessDto.setCpuCode(clmPayment.getCpuCode());
					paymentProcessDto.setDocReceivedFrom(clmPayment.getDocumentReceivedFrom());
					paymentProcessDto.setFaApprovalDate(clmPayment.getFaApprovalDate());
					paymentProcessDto.setFaApprovalDateValue(clmPayment.getFaApprovalDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmPayment.getFaApprovalDate()) : "");
//					paymentProcessDto.setFinancialRemarks(clmPayment.get);
					paymentProcessDto.setIfscCode(clmPayment.getIfscCode());
					paymentProcessDto.setInterestAmount(clmPayment.getInterestAmount());
					paymentProcessDto.setIntimationNo(clmPayment.getIntimationNumber());
					paymentProcessDto.setKey(clmPayment.getKey());
					paymentProcessDto.setNetAmount(clmPayment.getNetAmount());
					paymentProcessDto.setPayeeName(clmPayment.getPayeeName());
					paymentProcessDto.setPaymentDate(clmPayment.getPaymentDate());
					paymentProcessDto.setRodNo(clmPayment.getRodNumber());
					paymentProcessDto.setTdsAmount(clmPayment.getTdsAmount());
					
					Reimbursement rodObj = getReimbursementObject(clmPayment.getRodNumber());
					ClaimDto clmDto = ClaimMapper.getInstance().getClaimDto(rodObj.getClaim());
					paymentProcessDto.setRodKey(rodObj.getKey());
					clmDto.setNewIntimationDto(newIntimationDto);
					paymentDto = new PaymentProcessCpuPageDTO(paymentProcessDto,clmDto,rodObj);
					paymentDto.setClaimDto(clmDto);
					paymentDto.setReimbursementObj(rodObj);
					paymentDto.setBatchNo(clmPayment.getBatchNumber());
					paymentDto.setPayableAt(clmPayment.getPayableAt());
					paymentDto.setDdDateValue(clmPayment.getChequeDDDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmPayment.getChequeDDDate()) : "");
					paymentDto.setPaymentCpu(clmPayment.getPaymentCpuCode().toString());
					
					TmpCPUCode tmpCpuCode = getCpuByCode(Long.valueOf(clmPayment.getPaymentCpuCode().toString()));
					if(tmpCpuCode != null){
						paymentDto.setCpuCodeWithValue(tmpCpuCode.getCpuCode()+"-"+tmpCpuCode.getDescription());
					}
					paymentDto.setPaymentType(clmPayment.getPaymentType());
					paymentDto.setStatus(clmPayment.getStatusId().getProcessValue());
					paymentDto.setDocReceivedFrom(clmPayment.getDocumentReceivedFrom());
					
					paymentDto.setRodType((rodObj.getReconsiderationRequest() != null 
							&& !rodObj.getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.ORIGINAL : (SHAConstants.RECONSIDERATION) + (rodObj.getVersion() != null && rodObj.getVersion().intValue() > 1 ? rodObj.getVersion().intValue() - 1 : ""));
					
					if(rodObj.getReconsiderationRequest() == null){
						paymentDto.setRodType(SHAConstants.ORIGINAL);
					}
					
					paymentDto.setBillClassification((rodObj.getDocAcknowLedgement() != null 
							&& rodObj.getDocAcknowLedgement().getHospitalisationFlag() != null 
							&& rodObj.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.HOSPITALIZATION : SHAConstants.PARTIAL);
					
					if(rodObj.getDocAcknowLedgement() != null){
						String billClassificationValue = getBillClassificationValue(rodObj.getDocAcknowLedgement());
						if(billClassificationValue != null && rodObj.getDocAcknowLedgement().getBenifitFlag() != null){
							billClassificationValue += ","+rodObj.getDocAcknowLedgement().getBenifitFlag();
						}else if(rodObj.getDocAcknowLedgement().getBenifitFlag() != null){
							billClassificationValue = rodObj.getDocAcknowLedgement().getBenifitFlag();
						}
						paymentDto.setBillClassification(billClassificationValue);
					}
					//code added for seting payment party code in create and batch lot screen
					if(rodObj != null && rodObj.getClaim() !=null && rodObj.getClaim().getIntimation() != null && rodObj.getClaim().getIntimation().getPolicy().getPaymentParty() != null){
						paymentDto.setPaymentPartyMode(rodObj.getClaim().getIntimation().getPolicy().getPaymentParty());
					}
					newIntimationDto.setPaymentDto(paymentDto);
					intimationUPRDtoList.add(paymentDto);
				}			
			}
			
			if(apageable != null){					
				searchDto.getPageable().setPageNumber(pageNumber+1);
				if(intimationUPRDtoList == null || intimationUPRDtoList.isEmpty()){
					searchDto.getPageable().setPageNumber(1);
				}
				pagedIntimationListContainer.setHasNext(true);
				pagedIntimationListContainer.setPageNumber(pageNumber);
				pagedIntimationListContainer.setPageItems(intimationUPRDtoList);
			}
			else{
				pagedIntimationListContainer.setPageItems(intimationUPRDtoList);
			}
		}			
		
		return pagedIntimationListContainer;
	}
	
	public List<ClaimPayment> searchPaymentforIntimationUprView(Map<String, Object> searchFilter) {
		
		String intmNo = searchFilter.containsKey("intimationNumber") ?
				StringUtils.trim(searchFilter.get("intimationNumber")
						.toString()) : null;
				
		String uprId = searchFilter.containsKey("uprNumber") ? 
				StringUtils.trim(searchFilter.get("uprNumber")
						.toString()) : null;
		
		List<ClaimPayment> resultList = new ArrayList<ClaimPayment>();
		
		int firstResult = 1;
		if (!searchFilter.isEmpty()) {
			
			Query namedQuery = null;
			if(intmNo != null && !intmNo.isEmpty()
					&& uprId != null && !uprId.isEmpty()) {
				namedQuery = entityManager.createNamedQuery("ClaimPayment.findByintimationNumberWithUPR");
				namedQuery.setParameter("intimationNumber", intmNo);
				namedQuery.setParameter("uprId", uprId);
			}
			else if((intmNo == null || intmNo.isEmpty())
					&& uprId != null && !uprId.isEmpty()){
				namedQuery = entityManager.createNamedQuery("ClaimPayment.findByUprForIntimationUPR");
				namedQuery.setParameter("uprId", uprId);
			}
			else if(intmNo != null && !intmNo.isEmpty()
					&& (uprId == null || uprId.isEmpty())) {
				namedQuery = entityManager.createNamedQuery("ClaimPayment.findByintimationNumberForUPR");
				namedQuery.setParameter("intimationNumber", intmNo);
			}
				
			if(namedQuery != null) {
				resultList = namedQuery.getResultList();
			}
		}
		return resultList;	
	}
	
	public Reimbursement getReimbursementObject(String rodNo) {
		try{
			Query query = entityManager
					.createNamedQuery("Reimbursement.findRodByNumberWise");
			query = query.setParameter("rodNumber", rodNo);
			List<Reimbursement> reimbursementObjectList = query.getResultList();
			if (null != reimbursementObjectList
					&& !reimbursementObjectList.isEmpty()) {
				entityManager.refresh(reimbursementObjectList.get(0));
				return reimbursementObjectList.get(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ClaimPaymentCancel> getPremiaPaymentCancelDetailList(PaymentProcessCpuPageDTO paymentDto, String paymode){
		List<ClaimPaymentCancel> paymentCancelDetail = new ArrayList<ClaimPaymentCancel>();
		Query query = entityManager.createNamedQuery(
					"ClaimPaymentCancel.findByRodNoPaymentType");
			query = query.setParameter("paymentType", paymode);
			query = query.setParameter("rodNumber", paymentDto.getReimbursementObj().getRodNumber());
		try{
			if(query != null)
				paymentCancelDetail = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return paymentCancelDetail;
	}
	
	public List<BancsPaymentCancel> getBancsPaymentCancelDetailList(PaymentProcessCpuPageDTO paymentDto, String paymode){
		List<BancsPaymentCancel> paymentCancelDetail = new ArrayList<BancsPaymentCancel>();
		Query query = entityManager.createNamedQuery(
					"BancsPaymentCancel.findByPaymentKeyPaymentType");
			query = query.setParameter("paymentType", paymode);
			query = query.setParameter("paymentKey", paymentDto.getKey());
	
		try{
			if(query != null)
				paymentCancelDetail = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return paymentCancelDetail;
	}
	
	private String getBillClassificationValue(DocAcknowledgement docAck) {
		StringBuilder strBuilder = new StringBuilder();
		// StringBuilder amtBuilder = new StringBuilder();
		// Double total = 0d;
		try {
			if (("Y").equals(docAck.getHospitalisationFlag())) {
				strBuilder.append("Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPreHospitalisationFlag())) {
				strBuilder.append("Pre-Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPostHospitalisationFlag())) {
				strBuilder.append("Post-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
				strBuilder.append("Partial-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getLumpsumAmountFlag())) {
				strBuilder.append("Lumpsum Amount");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getHospitalCashFlag())) {
				strBuilder.append("Add on Benefits (Hospital cash)");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getPatientCareFlag())) {
				strBuilder.append("Add on Benefits (Patient Care)");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
				strBuilder.append("Hospitalization Repeat");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getCompassionateTravel())) {
				strBuilder.append("Compassionate Travel");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
				strBuilder.append("Repatriation Of Mortal Remains");
				strBuilder.append(",");
			}
			
			 if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
					 (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
							 || ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
				 if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					 strBuilder.append("Valuable Service Provider (Hospital)");
					 strBuilder.append(",");
				 }
			 }
			 else{ 
				 if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					 strBuilder.append("Preferred Network Hospital");
					 strBuilder.append(",");
				 }
			 }
			
			if (("Y").equals(docAck.getSharedAccomodation())) {
				strBuilder.append("Shared Accomodation");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
				strBuilder.append("Emergency Medical Evacuation");
				strBuilder.append(",");
			}
			
			//added for new product076
			if (("Y").equals(docAck.getProdHospBenefitFlag())) {	
			strBuilder.append("Hospital Cash");
			}
			
			if (("Y").equals(docAck.getStarWomenCare())) {
				strBuilder.append("Star Mother Cover");
				strBuilder.append(",");
			}
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}

	public ClaimPayment getSettlementDetailsByPaymode(Long pyamentKey, String paymode)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(pyamentKey != null && paymode != null){
		Query findByPaymentKey = entityManager.createNamedQuery("ClaimPayment.findByKeyPayType");
		findByPaymentKey = findByPaymentKey.setParameter("primaryKey", pyamentKey); 
		findByPaymentKey.setParameter("paymode", paymode);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail: paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
				return paymentDetailsList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return null;
	}
	
	public TmpCPUCode getCpuByCode(Long cpuCode) {
		 Query list = entityManager.createNamedQuery("TmpCPUCode.findByCode").setParameter("cpuCode", cpuCode) ;
		 List<TmpCPUCode> cpuCodeList = list.getResultList();
		 
		 if(cpuCodeList != null && !cpuCodeList.isEmpty()){
				return cpuCodeList.get(0);
			}
			 return null;
	 }
	public MasRoomRentLimit getMasRoomRentLimit(Long policyKey) {
		Query query = entityManager
				.createNamedQuery("MasRoomRentLimit.findByPolicyKey");
		query = query.setParameter("policyKey", policyKey);
		List<MasRoomRentLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasRoomRentLimit getMasRoomRentLimit(Long policyKey, Double sumInsuredTo, String roomType) {
		Query query = entityManager
				.createNamedQuery("MasRoomRentLimit.findBasedOnSITo");
		query = query.setParameter("policyKey", policyKey);
		//query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("roomType", roomType);
		List<MasRoomRentLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasRoomRentLimit getMasRoomRentLimitbySuminsured(Long policyKey, Double sumInsuredTo) {
		Query query = entityManager
				.createNamedQuery("MasRoomRentLimit.findBasedOnSI");
		query = query.setParameter("policyKey", policyKey);
		//query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		//query = query.setParameter("roomType", roomType);
		List<MasRoomRentLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public PhysicalDocumentVerification getReimbursementByKeyForPhysicalVerifcation(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerification.findByRodKey")
				.setParameter("primaryKey", key);

		List<PhysicalDocumentVerification> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	public boolean getCRMDummyHospitalIntimationAvaialble(String intimationNo) {
		Boolean isDummyHospIntm = false;
		GalaxyCRMIntimation glxIntmObj = null;
		Query findByKey1 = entityManager.createNamedQuery("GalaxyCRMIntimation.findByIntimationNoWithLike").setParameter("intimationNo",  "%"+intimationNo+"%");
		List<GalaxyCRMIntimation> intimationList1 = (List<GalaxyCRMIntimation>) findByKey1.getResultList();
		if (!intimationList1.isEmpty()) {
			glxIntmObj = intimationList1.get(0);
			if(glxIntmObj != null && glxIntmObj.getDummyHospFlag() != null && glxIntmObj.getDummyHospFlag().equalsIgnoreCase("Y")){
				isDummyHospIntm = true;
			}
		}
		return isDummyHospIntm;
	}
	
	public String createDMSToken(String policyNo){
		
		String apiKey = BPMClientContext.DMS_SECRET_KEY;
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
			System.out.println("Token : "+signedJWT.serialize());
			return signedJWT.serialize();
		}catch (JOSEException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	public Date getCMDClubMemberTypeForEnhancement(Claim claim,Date pfdUpFfaxSubmitDate){
		Date clubmembertype = pfdUpFfaxSubmitDate;
		String priorityOffsetString  = null;
		try{
		if(pfdUpFfaxSubmitDate != null && claim != null && ((claim.getClaimClubMember() != null && !claim.getClaimClubMember().isEmpty())
				|| (claim.getPriorityEvent() != null && !claim.getPriorityEvent().isEmpty()))){
			 priorityOffsetString = claim.getClaimClubMember();
			if(claim.getPriorityEvent() != null && !claim.getPriorityEvent().isEmpty() && !claim.getPriorityEvent().trim().isEmpty()){
				priorityOffsetString = claim.getPriorityEvent().trim();
				String[] parts = priorityOffsetString.split(" ");
				for(int i = 0; i < parts.length; i++) {
					if(parts[i].equalsIgnoreCase("COVID")) {
						priorityOffsetString = SHAConstants.COVID;
					}
				}
			}
			if(priorityOffsetString != null && !priorityOffsetString.isEmpty() && !priorityOffsetString.trim().isEmpty()){
				MasCmdOffsetConfiguration getOffsetTime = getOffsetTimeByClaimPriority(priorityOffsetString);
				if(getOffsetTime != null && getOffsetTime.getTimeLapse() != null){
					Integer ofSetNum = (int) -getOffsetTime.getTimeLapse();
					System.out.println("Offset Time Value :" + ofSetNum + "Offest Priority value  :" + getOffsetTime.getClubMember());
						Calendar cal = Calendar.getInstance();
						cal.setTime(pfdUpFfaxSubmitDate);
						cal.add(Calendar.MINUTE, (ofSetNum));
						Date oneHourBack = cal.getTime();
						clubmembertype = oneHourBack;
					System.out.println("clubMember Time differnece : " + "Actual Time :- " + pfdUpFfaxSubmitDate + "  Difference Time :- " + clubmembertype + "  Memeber Type :- " + claim.getClaimClubMember());
				}
			}
		}
	}catch (Exception e){
		e.printStackTrace();
		clubmembertype = pfdUpFfaxSubmitDate;
	}
		return clubmembertype;
	}
	
	public Date getCMDClubMemberTypeForPreauth(Claim claim,Date pfdUpFfaxSubmitDate){
		Date clubmembertype = pfdUpFfaxSubmitDate;
		String priorityOffsetString = null;
		try{
		if(pfdUpFfaxSubmitDate != null && claim != null && (claim.getClaimClubMember() != null && !claim.getClaimClubMember().isEmpty())){
			 priorityOffsetString = claim.getClaimClubMember();
			if(priorityOffsetString != null && !priorityOffsetString.isEmpty()){
				MasCmdOffsetConfiguration getOffsetTime = getOffsetTimeByClaimPriority(priorityOffsetString);
				if(getOffsetTime != null && getOffsetTime.getTimeLapse() != null){
					Integer ofSetNum = (int) -getOffsetTime.getTimeLapse();
					System.out.println("Offset Time Value :" + ofSetNum + "Offest Priority value  :" + getOffsetTime.getClubMember());
						Calendar cal = Calendar.getInstance();
						cal.setTime(pfdUpFfaxSubmitDate);
						cal.add(Calendar.MINUTE, (ofSetNum));
						Date oneHourBack = cal.getTime();
						clubmembertype = oneHourBack;
					System.out.println("clubMember Time differnece : " + "Actual Time :- " + pfdUpFfaxSubmitDate + "  Difference Time :- " + clubmembertype + "  Memeber Type :- " + claim.getClaimClubMember());
				}
			}
		}
		}catch (Exception e){
			e.printStackTrace();
			clubmembertype = pfdUpFfaxSubmitDate;
		}
		return clubmembertype;
	}
	
	public MasCmdOffsetConfiguration getOffsetTimeByClaimPriority(String offsetPriorityValue) {
		Query query = entityManager.createNamedQuery("MasCmdOffsetConfiguration.findOffsetByPriority")
				.setParameter("clubMemeberType", offsetPriorityValue);

		List<MasCmdOffsetConfiguration> cmdOffsetPriorityList =  query.getResultList();
		if(!cmdOffsetPriorityList.isEmpty() && cmdOffsetPriorityList != null) {
			return cmdOffsetPriorityList.get(0);
		}
		return null;
	}
	public PhysicalDocumentVerification getPhysicalVerifcationByClaimKey(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerification.findByClaimKey")
				.setParameter("claimKey", key);

		List<PhysicalDocumentVerification> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	public MasHospitals getHrmDetails(String hospitalCode) {
		Query query = entityManager.createNamedQuery(
				"MasHospitals.findByCode").setParameter("hospitalCode", hospitalCode);
		MasHospitals hospitalDetails = (MasHospitals) query.getSingleResult();
		if (hospitalDetails != null) {
			return hospitalDetails;
		}
		return null;
	}
	
	public String getCSCMemberDetails(Long cpuCode) {

		String memberDetail = "";
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT CSC_MOBILE_NO FROM MAS_CPU_CODE WHERE CPU_CODE=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, cpuCode);
				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							memberDetail = rs.getString(1);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberDetail;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getInsuredCoverByInsuredKey(Long insuredKey,String coverCode){
		Query query = entityManager
				.createNamedQuery("InsuredCover.findHospitalExpnByInsured");
		query = query.setParameter("insuredKey", insuredKey);
		query = query.setParameter("coverCode", coverCode);
		List<InsuredCover> insuredList  = (List<InsuredCover>) query.getResultList();	
		if(insuredList != null && !insuredList.isEmpty()) {
			return false;
		}else {
			return true;		
		}
	}
	
	public DialerLoginLogoutResponse dialerLogin(InitiateTalkTalkTalkDTO dto){
		
		DialerLoginLogoutResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
			
			DialerLoginRequest request = new DialerLoginRequest();
			request.setAction("LOGINUSER");
			request.setUser_type("Agent");
			request.setUsername(dto.getStrUserName().toUpperCase());
//			request.setUsername("TALK");
			request.setPassword(dialerPwd);
			request.setStation(dto.getExtnCode());
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer Login request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			
			if (urlCon.getResponseCode() != 200) {

			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer Login response: "+output);
					response = gson.fromJson(output,DialerLoginLogoutResponse.class);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}
	
	public DialerPlaceCallResponse dialerPlaceCall(InitiateTalkTalkTalkDTO dto){
		
		DialerPlaceCallResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
			
			DialerPlaceCallRequest request = new DialerPlaceCallRequest();
			request.setAction("CALL");
			request.setUser(dto.getStrUserName().toUpperCase());
//			request.setUser("TALK");
			request.setPhone_number(dto.getTalkMobto());
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer Place Call request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			dto.setAction("CALL");
			saveDialerCallRequest(dto);
			if (urlCon.getResponseCode() != 200) {

			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer Place Call response: "+output);
					response = gson.fromJson(output,DialerPlaceCallResponse.class);
					saveDialerCallResponse(response, dto);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}
	
	public DialerEndCallResponse dialerEndCall(InitiateTalkTalkTalkDTO dto){
		
		DialerEndCallResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
			
			DialerEndCallRequest request = new DialerEndCallRequest();
			request.setAction("CLOSE");
			request.setEndcall_type("CLOSE");
			request.setConvoxid(dto.getConvoxid());
			request.setDisposition("TEST");
			request.setAgent_id(dto.getStrUserName().toUpperCase());
//			request.setAgent_id("TALK");
			request.setProcess_crm_id(dto.getExtnCode());
			request.setMobile_number(dto.getTalkMobto());
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer End Call request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			
			if (urlCon.getResponseCode() != 200) {

			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer End Call response: "+output);
					response = gson.fromJson(output,DialerEndCallResponse.class);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}
	
	public DialerLoginLogoutResponse dialerLogOut(InitiateTalkTalkTalkDTO dto){
		
		DialerLoginLogoutResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
//			urlCon.setRequestProperty("access_token", "PROD_MIAGCSqGSIb3DQEHAqCAMIACAQExC");
			
			DialerLogoutRequest request = new DialerLogoutRequest();
			request.setAction("LOGOUTUSER");
			request.setUser(dto.getStrUserName().toUpperCase());
//			request.setUser("TALK");
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer Logout request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			
			if (urlCon.getResponseCode() != 200) {

			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer Logout response: "+output);
					response = gson.fromJson(output,DialerLoginLogoutResponse.class);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}
	
	public DialerHoldCallResponse dialerHoldCall(InitiateTalkTalkTalkDTO dto){
		
		DialerHoldCallResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
			
			DialerHoldCallRequest request = new DialerHoldCallRequest();
			request.setAction("HOLD");
			request.setUser_id(dto.getStrUserName().toUpperCase());
//			request.setUser_id("TALK");
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer Hold request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			
			if (urlCon.getResponseCode() != 200) {

			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer Hold response: "+output);
					response = gson.fromJson(output,DialerHoldCallResponse.class);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}

	public DialerUnholdResponse dialerUnHold(InitiateTalkTalkTalkDTO dto){
		
		DialerUnholdResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.DIALER_URL;
			String accessToken = BPMClientContext.DIALER_ACCESS_TOKEN;
			String dialerPwd = BPMClientContext.DIALER_PASSWORD;
			System.out.println("Dialer - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("access_token", accessToken);
	//		urlCon.setRequestProperty("access_token", "PROD_MIAGCSqGSIb3DQEHAqCAMIACAQExC");
			
			DialerUnholdRequest request = new DialerUnholdRequest();
			request.setAction("UNHOLD");
			request.setUser_id(dto.getStrUserName().toUpperCase());
//			request.setUser_id("TALK");
			request.setRefno(dto.getReferenceId());
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request); 
			System.out.println("Dialer UnHold request:"+requestString);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(requestString.toString().getBytes());
			os.flush();
			os.close();
			
			if (urlCon.getResponseCode() != 200) {
	
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Dialer UnHold response: "+output);
					response = gson.fromJson(output,DialerUnholdResponse.class);
					return response;
				}
			}
		} catch(Exception e){
			
		}
		return response;
	}
	
	
	public void saveDialerCallRequest(InitiateTalkTalkTalkDTO dto){
		
		try{
			DialerCallRequest callDetails = new DialerCallRequest();
			callDetails.setIntimationNo(dto.getIntimationNumber());
			callDetails.setRequestAction(dto.getAction());
			callDetails.setRequestUser(dto.getStrUserName());
			callDetails.setRequestPhoneNo(dto.getTalkMobto());
			callDetails.setRequestRefNo(dto.getReferenceId());
			callDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			callDetails.setCreatedBy(dto.getUsername());
			callDetails.setActiveStatus("1");
			entityManager.persist(callDetails);
			entityManager.flush();
 
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	public void saveDialerCallResponse(DialerPlaceCallResponse response,InitiateTalkTalkTalkDTO dto){
		DialerCallRequest callDetails = new DialerCallRequest();
		callDetails.setIntimationNo(dto.getIntimationNumber());
		callDetails.setResponseStatus(response.getSTATUS());
		callDetails.setResponseMessage(response.getMESSAGE());
		callDetails.setResponsePhoneNo(response.getPHONE_NUMBER());
		callDetails.setResponseProcess(response.getPROCESS());
		callDetails.setResponseRefNo(response.getRefno());
		callDetails.setResponseLeadId(response.getLEAD_ID());
		callDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		callDetails.setCreatedBy(dto.getUsername());
		callDetails.setActiveStatus("1");
		entityManager.persist(callDetails);
		entityManager.flush();
	}
}
