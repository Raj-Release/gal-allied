package com.shaic.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.criteria.Root;

import net.minidev.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.OPClaimMapper;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.claim.cashlessprocess.processicac.search.IcacRequest;
import com.shaic.claim.fvrdetails.view.ViewFVRFormDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.hospitalspocdetails.view.ViewHospSPOCDetailsDTO;
import com.shaic.claim.pcc.beans.PCCCategory;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.beans.PCCSubCategory;
import com.shaic.claim.pcc.beans.PCCSubCategoryTwo;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksDTO;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.productbenefit.view.ContinuityBenefitDTO;
import com.shaic.claim.productbenefit.view.PortablitiyPolicyDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUWisePreauthResultDto;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.Page.PAcoverTableViewDTO;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.preauth.AutoClaimAmountDetails;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.CriticalIllnessMaster;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.MasRejectSubCategory;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.domain.preauth.PccRemarks;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PortablityPolicy;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.preauth.ProcedureSpecialityMaster;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.TmpAdUserList;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;
import com.shaic.domain.preauth.ViewTmpDiagnosis;
import com.shaic.domain.preauth.ViewTmpPreauth;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
public class PreauthService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	/*@Resource
	private UserTransaction utx;*/
	
	@EJB
	private ViewFVRService viewFVRService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService calcService;
	
	////private static Window popup;
		
	private final Logger log = LoggerFactory.getLogger(PreauthService.class);


	public PreauthService() {
		super();
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
	
	
	public VB64ApprovalRequest getProcess64ById(Long cashlessKey) {
		Query query = entityManager.createNamedQuery("VB64ApprovalRequest.findByCashlessKey");
		query.setParameter("cashlessKey", cashlessKey);
		@SuppressWarnings("unchecked")
		List<VB64ApprovalRequest> singleResult = (List<VB64ApprovalRequest>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}
	
	
	public ViewTmpPreauth getViewTmpPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("ViewTmpPreauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<ViewTmpPreauth> singleResult = (List<ViewTmpPreauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByKey(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey,
			EntityManager entityManager) {
		this.entityManager = entityManager;
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		//this.entityManager = entityManager;
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}

	@SuppressWarnings("unchecked")
	public List<DiagnosisPED> getPEDDiagnosisByPEDValidationKey(Long pedValidationKey) {
		Query query = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
		query.setParameter("pedValidationKey", pedValidationKey);
		List<DiagnosisPED> singleResult = (List<DiagnosisPED>) query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()) {
			for (DiagnosisPED diagnosisPED : singleResult) {
				entityManager.refresh(diagnosisPED);
			}
		}
		return singleResult;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<DiagnosisPED> getPEDDiagnosisByPEDValidationKey(Long pedValidationKey, EntityManager entityManager) {
		this.entityManager = entityManager;
		Query query = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
		query.setParameter("pedValidationKey", pedValidationKey);
		List<DiagnosisPED> singleResult = (List<DiagnosisPED>) query.getResultList();
		if(singleResult != null && singleResult.isEmpty()) {
			for (DiagnosisPED diagnosisPED : singleResult) {
				entityManager.refresh(diagnosisPED);
			}
		}
		return singleResult;
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
		
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByClaimKeyWithClearCashless(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyWithClearCashless");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preuathList = (List<Preauth>) query.getResultList();
		if(preuathList !=  null){
			for (Preauth preauth : preuathList) {
				entityManager.refresh(preauth);
			}
		}
		return preuathList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpPreauth> getTmpPreauthByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("ViewTmpPreauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<ViewTmpPreauth> preuathList = (List<ViewTmpPreauth>) query.getResultList();
		if(preuathList !=  null){
			for (ViewTmpPreauth preauth : preuathList) {
				entityManager.refresh(preauth);
			}
		}
		return preuathList;
	}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })

	public BeanItemContainer<SelectValue> getProcedureListNames() {/*
=======
	public BeanItemContainer<SelectValue> getProcedureListNames() {
		
		
		System.out.println("---------------- Second Approach Start current time for PROCEDURE ******************" + System.currentTimeMillis());
		
>>>>>>> dbef35826fc77e256ad795f3291bfa8766dd6a72
		Query findAll = entityManager
				.createNamedQuery("ProcedureMaster.findAll");
		List<Object[]> procedureList = (List<Object[]>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for (Object[] procedureMaster : procedureList) {
			SelectValue select = new SelectValue();
			select.setId((Long)procedureMaster[0]);
			select.setValue((String)procedureMaster[1]);

			selectValuesList.add(select);
		}

		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		procedureListContainer.addAll(selectValuesList);
		
		System.out.println("---------------- Second Approach End current time for PROCEDURE ******************" + System.currentTimeMillis());
		return procedureListContainer;
	*/
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(ProcedureMaster.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.addOrder(Order.asc("procedureName"))
										.setProjection(Projections.projectionList()
										.add(Projections.property("key"), "id")
										.add(Projections.property("procedureName"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;	
	
	}
	
	@SuppressWarnings("unchecked")
	public ClaimAmountDetails getClaimAmountDetails(Long claimAmountDetailsKey) {
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByKey");
		query.setParameter("claimAmountDetailsKey", claimAmountDetailsKey);
		ClaimAmountDetails singleResult = (ClaimAmountDetails) query.getSingleResult();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClaimAmountDetails> getClaimAmountDetailsByPreauth(Long preauthKey) {
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<ClaimAmountDetails> resultList = query.getResultList();
		return resultList;
	}

	
	public Long getClaimAmountByPreauth(Long preauthKey,EntityManager entMgr) {
		this.entityManager = entMgr;
		List<ClaimAmountDetails> resultList = getClaimAmountDetailsByPreauth(preauthKey);
		Long claimedAmt = 0l;
		if(resultList != null && !resultList.isEmpty()){
			
			for(ClaimAmountDetails clmAmtDto: resultList){
				entityManager.refresh(clmAmtDto);
				if(clmAmtDto.getPaybleAmount() != null){
					claimedAmt +=clmAmtDto.getPaybleAmount().longValue();
				}
			}
			
		}
	return claimedAmt;
	}
	
	/*@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getProcedureListCodes() {
		
		
		System.out.println("---------------- Second Approach Start current time for PROCEDURE CODES ******************" + System.currentTimeMillis());
		
		Query findAll = entityManager
				.createNamedQuery("ProcedureMaster.findAll");
		List<Object[]> procedureList = (List<Object[]>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for (Object[] procedureMaster : procedureList) {
			SelectValue select = new SelectValue();
			select.setId((Long)procedureMaster[0]);
			select.setValue((String)procedureMaster[1]);

			selectValuesList.add(select);
			
		}
		

		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		procedureListContainer.addAll(selectValuesList);
		
		System.out.println("---------------- Second Approach End current time for PROCEDURE CODES ******************" + System.currentTimeMillis());
		return procedureListContainer;
		
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(ProcedureMaster.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;
	}*/

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getCriticalIllenssMasterValues(PreauthDTO preauthDTO) {
		
		Session session = (Session) entityManager.getDelegate();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		if(null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				ReferenceTable.STAR_CRITICARE_PLATINUM_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			selectValuesList = session.createCriteria(CriticalIllnessMaster.class)
					.add(Restrictions.eq("activeStatus", (long) 1))
					.add(Restrictions.eq("illnessFlag", SHAConstants.CRITICARE_PLATINUM_ILLNESS_FLAG))
					.addOrder(Order.asc("value"))
					.setProjection(Projections.projectionList()
					.add(Projections.property("key"), "id")
					.add(Projections.property("value"), "value"))
					.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		}
		else if(null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				ReferenceTable.STAR_CRITICARE_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			selectValuesList = session.createCriteria(CriticalIllnessMaster.class)
					.add(Restrictions.eq("activeStatus", (long) 1))
					.add(Restrictions.eq("illnessFlag", SHAConstants.CRITICARE_GOLD_ILLNESS_FLAG))
					.addOrder(Order.asc("value"))
					.setProjection(Projections.projectionList()
					.add(Projections.property("key"), "id")
					.add(Projections.property("value"), "value"))
					.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		}
		else if(!preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
		 selectValuesList = session.createCriteria(CriticalIllnessMaster.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(Restrictions.eq("illnessFlag", SHAConstants.GENERAL_ILLNESS_FLAG))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
										.add(Projections.property("key"), "id")
										.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		}		
		else
		{
			 selectValuesList = session.createCriteria(CriticalIllnessMaster.class)
					.add(Restrictions.eq("activeStatus", (long) 1))
					.add(Restrictions.eq("illnessFlag", SHAConstants.PAAYAS_ILLNESS_FLAG))
					.addOrder(Order.asc("value"))
					.setProjection(Projections.projectionList()
					.add(Projections.property("key"), "id")
					.add(Projections.property("value"), "value"))
					.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;	
		
	}

	/*@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getMedicalSpecialityType() {
		Query findAll = entityManager
				.createNamedQuery("SpecialityType.findByMedical");
		List<SpecialityType> medicalSpecialityList = (List<SpecialityType>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for (SpecialityType specialityType : medicalSpecialityList) {
			SelectValue select = new SelectValue();
			select.setId(specialityType.getKey());
			select.setValue(specialityType.getValue());
			selectValuesList.add(select);
		}

		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		procedureListContainer.addAll(selectValuesList);
		return procedureListContainer;
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(SpecialityType.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(Restrictions.eq("code","M"))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;
	}*/
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getSpecialistTypeList() {
/*		Query findAll = entityManager
				.createNamedQuery("SpecialityType.findAll");
		List<SpecialityType> medicalSpecialityList = (List<SpecialityType>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for (SpecialityType specialityType : medicalSpecialityList) {
			SelectValue select = new SelectValue();
			select.setId(specialityType.getKey());
			select.setValue(specialityType.getValue());
			selectValuesList.add(select);
		}

		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		procedureListContainer.addAll(selectValuesList);
		return procedureListContainer;
*/		
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(SpecialityType.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
										.add(Projections.property("key"), "id")
										.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;
		
	}

	/*@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getSurgicalSpecialityType() {
		Query findAll = entityManager
				.createNamedQuery("SpecialityType.findBySurgical");
		List<SpecialityType> medicalSpecialityList = (List<SpecialityType>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for (SpecialityType specialityType : medicalSpecialityList) {
			SelectValue select = new SelectValue();
			select.setId(specialityType.getKey());
			select.setValue(specialityType.getValue());
			selectValuesList.add(select);
		}
		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		procedureListContainer.addAll(selectValuesList);
		return procedureListContainer;
	
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(SpecialityType.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(Restrictions.eq("code","S"))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());	
		return selectValueContainer;
	}*/

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getSpecialityType(String code) {
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(SpecialityType.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(Restrictions.eq("code",code))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());	
		return selectValueContainer;
	}
	
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByKey(Long claimKey) {

		Query findByKey = entityManager.createNamedQuery("Preauth.findByKey")
				.setParameter("preauthKey", claimKey);

		List<Preauth> preauthList = (List<Preauth>) findByKey.getResultList();

		if (!preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}
	
	

	public PreauthQuery getPreauthQueryList(Long preauthKey) {
		Query findByKey = entityManager.createNamedQuery(
				"PreauthQuery.findBypreauth").setParameter("preAuthPrimaryKey",
				preauthKey);

		List<PreauthQuery> preauthList = (List<PreauthQuery>) findByKey
				.getResultList();

		if (!preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}
	
	public List<PreauthQuery> QueryList(Long preauthKey) {
		Query findByKey = entityManager.createNamedQuery(
				"PreauthQuery.findBypreauth").setParameter("preAuthPrimaryKey",
				preauthKey);

		List<PreauthQuery> preauthList = (List<PreauthQuery>) findByKey
				.getResultList();

		if (!preauthList.isEmpty()) {
			return preauthList;

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByIntimationKey(Long claimKey) {

		Query findByKey = entityManager.createNamedQuery(
				"Preauth.findByIntimationKey").setParameter("intimationKey",
				claimKey);

		List<Preauth> preauth = (List<Preauth>) findByKey.getResultList();

		if ( ! preauth.isEmpty()) {
			return preauth.get(0);
		}
		return null;
	}

	public Status getStatusByPreauth(Long key) {

		Query findByKey = entityManager.createNamedQuery("Status.findByKey")
				.setParameter("statusKey", key);

		Status status = (Status) findByKey.getSingleResult();
		if (status != null) {
			return status;
		}
		return null;
	}
	
	public Stage getStageByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery("Stage.findByKey")
				.setParameter("stageKey", key);

		Stage stage = (Stage) findByKey.getSingleResult();
		if (stage != null) {
			return stage;
		}
		return null;
	}
	
	public Status getStatusByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery("Status.findByKey")
				.setParameter("statusKey", key);

		Status status = (Status) findByKey.getSingleResult();
		if (status != null) {
			return status;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getDiagnosisList() {
//		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
//		List<Diagnosis> diagnosisList = (List<Diagnosis>) findAll
//				.getResultList();
//		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
//		for (Diagnosis diagnosis : diagnosisList) {
//			SelectValue select = new SelectValue();
//			select.setId(diagnosis.getKey());
//			select.setValue(diagnosis.getValue());
//			selectValuesList.add(select);
//		}
//
//		BeanItemContainer<SelectValue> diagnosisListContainer = new BeanItemContainer<SelectValue>(
//				SelectValue.class);
//		diagnosisListContainer.addAll(selectValuesList);
//		return diagnosisListContainer;
		
		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
		@SuppressWarnings("unchecked")
		List<Object[]> diagnosisList = (List<Object[]>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (Object[] diagnosis : diagnosisList) {
			SelectValue select = new SelectValue();
			select.setId((Long)diagnosis[0]);
			select.setValue((String)diagnosis[1]);
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);
		selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		return selectValueContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<SelectValue> getDiagnosisSelctValuesList() {
//		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
//		List<Diagnosis> diagnosisList = (List<Diagnosis>) findAll
//				.getResultList();
//		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
//		for (Diagnosis diagnosis : diagnosisList) {
//			SelectValue select = new SelectValue();
//			select.setId(diagnosis.getKey());
//			select.setValue(diagnosis.getValue());
//			selectValuesList.add(select);
//		}
//		return selectValuesList;
		
		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
		List<Object[]> diagnosisList = (List<Object[]>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		SelectValue select = null;
		for (Object[] diagnosis : diagnosisList) {
			select = new SelectValue();
			select.setId((Long)diagnosis[0]);
			select.setValue((String)diagnosis[1]);
			selectValuesList.add(select);
		}
		return selectValuesList;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Integer getSumInsured(Long productKey, Double sumInsured) {

		Query query = entityManager
				.createNativeQuery("SELECT FUN_AUTORESTORE_AMT(?1,?2) FROM DUAL");
		query.setParameter(1, productKey);
		query.setParameter(2, sumInsured);	
		BigDecimal singleResult = (BigDecimal) query.getSingleResult();
		if (null != singleResult)
			return Integer.valueOf(singleResult.toString());
		else
			return 0;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Integer getClaimAmountDetailsFromProcedure() {
		Query query = entityManager
				.createNativeQuery("SELECT PRC_HOSPITALIZATION(?1, ?2, ?3) FROM DUAL");
		query.setParameter(1, 27);
		query.setParameter(2, 200000);
		query.setParameter(3, "A");
		BigDecimal singleResult = (BigDecimal) query.getSingleResult();
		return Integer.valueOf(singleResult.toString());
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public String getRoomRentAndICUDetails() {
		Query query = entityManager.createNativeQuery(
				"select FUN_HOSPITALIZATION(?1, ?2, ?3) from dual",
				Object.class);
		query.setParameter(1, 15);
		query.setParameter(2, 200000);
		query.setParameter(3, "A");
		String result = (String) query.getSingleResult();
		return result;
	}

	public Object getClaimedAmountDetails() {

		/*Query query = entityManager.createNamedQuery("getFunc"); // if procedure
																	// then
																	// getProc
		query.setParameter("p1", 22);
		query.setParameter("p2", 200000);
		query.setParameter("p3", 63);
		query.getResultList();*/

		// Session session = (Session) entityManager.getDelegate();
		// SessionFactoryImplementor sfi = (SessionFactoryImplementor)
		// session.getSessionFactory();
		// ConnectionProvider cp = sfi.getConnectionProvider();
		// try {
		// Connection connection = cp.getConnection();
		// OracleCallableStatement cstmt = (OracleCallableStatement)
		// connection.prepareCall("{call PRC_SUB_LIMIT(?, ?, ?)}");
		// cstmt.setInt(1, 22); //Product key
		// cstmt.setInt(2, 200000); // Sum Insured
		// cstmt.setInt(3, 63); //AGE
		//
		// Object[] empObjArray = new Object[5];
		// StructDescriptor empStructDesc =
		// StructDescriptor.createDescriptor("OBJ_SUB_LIM", connection);
		// STRUCT empStruct = new STRUCT(empStructDesc, connection,
		// empObjArray);
		// cstmt.registerOutParameter(1, Types.STRUCT);
		// ResultSet rs = cstmt.executeQuery();
		//
		// while (rs.next()) {
		// STRUCT id = (STRUCT) rs.getObject(1);
		// System.out.println(id);
		// }
		//
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		// Query query =
		// entityManager.createNamedQuery("SublimitFunObject.callSublimitProcedure");
		// query.setParameter("productKey",27);
		// query.setParameter("sumInsured",300000);
		// query.setParameter("cityClass",0);
		// List resultList = query.getResultList();
		// for (Object object : resultList) {
		// System.out.println("-------------------  result " + object);
		// }

		return null;
	}

	public Coordinator findCoordinatorByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Coordinator.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List resultList = query.getResultList();
		Coordinator object = new Coordinator();
		if(!resultList.isEmpty()) {
			 object = (Coordinator) resultList.get(0);
		}
		return object;
	}

	public List<Procedure> findProcedureByPreauthKey(Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("Procedure.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Procedure> resultList = (List<Procedure>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (Procedure procedure : resultList) {
				entityManager.refresh(procedure);
			}
		}
		return resultList;
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

	@SuppressWarnings("unchecked")
	public List<Speciality> findSpecialityByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Speciality.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Speciality> resultList = (List<Speciality>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (Speciality speciality : resultList) {
				entityManager.refresh(speciality);
			}
			
		}
		return resultList;
	}
	

	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedValidationByPreauthKey(Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<PedValidation> resultList = (List<PedValidation>) query
				.getResultList();
		
		if(resultList != null && !resultList.isEmpty()) {
			for (PedValidation pedValidation : resultList) {
				entityManager.refresh(pedValidation);
			}
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedValidationByPreauthKey(Long preauthKey, EntityManager entityManage) {
		this.entityManager = entityManage;
		Query query = entityManager
				.createNamedQuery("PedValidation.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<PedValidation> resultList = (List<PedValidation>) query
				.getResultList();
		return resultList;
	}

	@SuppressWarnings("unused")
	private String getRemarksForPreauthEnhancement(Long statusKey,
			PreauthDTO bean, Preauth preauth) {
		Status status = new Status();
		Stage stage = new Stage();
		String remarks = "";
		String insuredRemarks = "";
		
		switch (statusKey.intValue()) {
		case 30:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApprovalRemarks();
			ResidualAmount residualAmt = new ResidualAmount();
			residualAmt.setKey(bean.getResidualAmountDTO().getKey());
			residualAmt.setTransactionKey(preauth.getKey());
			residualAmt.setStage(preauth.getStage());
			residualAmt.setStatus(preauth.getStatus());
			residualAmt.setApprovedAmount(bean.getResidualAmountDTO()
					.getApprovedAmount());
			residualAmt.setRemarks(bean.getResidualAmountDTO().getRemarks());
			if (residualAmt.getKey() != null) {
				entityManager.merge(residualAmt);
				entityManager.flush();
				entityManager.clear();
			}

			break;
		case 32:
			remarks = bean.getPreauthMedicalDecisionDetails().getQueryRemarks();
			PreauthQuery preAuthQuery = new PreauthQuery();
			preAuthQuery.setPreauth(preauth);
			String strUserName = bean.getStrUserName();
			preAuthQuery.setCreatedBy(strUserName);
			preAuthQuery.setQueryRemarks(remarks);
			status.setKey(bean.getStatusKey());
			stage.setKey(bean.getStageKey());
			preAuthQuery.setStatus(status);
			preAuthQuery.setStage(stage);
			preAuthQuery.setDoctorNote(bean.getPreauthMedicalDecisionDetails().getDoctorNote());
			//R1295
			if(bean.getQueryType() != null){
				MastersValue qryTyp = new MastersValue();
				qryTyp.setKey(bean.getQueryType().getId());
				qryTyp.setValue(bean.getQueryType().getValue());
				preAuthQuery.setQueryType(qryTyp);
			}
			
			entityManager.persist(preAuthQuery);
			entityManager.flush();
			entityManager.clear();
			break;
		case 31:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			preauth.setInsuredRemarks(bean.getPreauthMedicalDecisionDetails()
					.getRemarksForInsured());
			break;
		case 35:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getEscalationRemarks();
			if(bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
				if(bean.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					createSpecialist(bean, preauth, status, stage, remarks);
					
				}else{
					createEscalate(bean, preauth, status, stage, remarks);
				}
			}
			break;
		case 36:
			createCoordinator(bean, preauth,status,stage);
			break;
			
		case 38 : //same as 63  so no need to //break
		case 63 : 	
			remarks = bean.getPreauthMedicalDecisionDetails().getDownsizeRemarks();
			preauth.setDownsizeInsuredRemarks(bean.getPreauthMedicalDecisionDetails()
					.getDownsizeInsuredRemarks());
			break;

		case 64:
			remarks = bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks();
			preauth.setInsuredRemarks(bean.getPreauthMedicalDecisionDetails()
					.getWithDrawRemarksForInsured());
			break;
		case 40:
			remarks = bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks();
			preauth.setInsuredRemarks(bean.getPreauthMedicalDecisionDetails()
					.getRemarksForInsured());
			break;

		default:
			break;
		}
		return remarks;
	}
	
   public HashMap<String,String> diagnosisNameAndIcdCode(List<PedValidation> pedValidationList){

	    StringBuffer diagnosisName = new StringBuffer();
	    StringBuffer icdCodeDesc = new StringBuffer();
	    
	    HashMap<String, String> mapAsList = new HashMap<String, String>();
		
		for (PedValidation pedValidation : pedValidationList) {
			
			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if(masters != null){
			diagnosisName.append(masters.getValue()).append(",");
			}
			IcdCode icdCode = getIcdCode(pedValidation.getIcdCodeId());
			if(icdCode != null){
				icdCodeDesc.append(icdCode.getValue()).append(", ");
			}

		}
		
		mapAsList.put("diagnosis", diagnosisName.toString());
		mapAsList.put("icdCode", icdCodeDesc.toString());
	
		return mapAsList;
   }
   
   public IcdCode getIcdCode(Long icdCodeKey){
	   
		Query query = entityManager.createNamedQuery("IcdCode.findByKey")
				.setParameter("primaryKey", icdCodeKey);

		List<IcdCode> icdCodeList =  query.getResultList();
		if(null != icdCodeList && !icdCodeList.isEmpty())
		{
			//IcdCode icdCodeList = (IcdCode) query.getSingleResult();
			return icdCodeList.get(0);
		}
		
		return null;
   }
   
   public IcdChapter getIcdChapter(Long icdChapter){
	   
		Query query = entityManager.createNamedQuery("IcdChapter.findByKey")
				.setParameter("primaryKey", icdChapter);

		IcdChapter icdChapter1 = (IcdChapter) query.getSingleResult();
		
		return icdChapter1;
  }
   
   public IcdBlock getIcdBlock(Long icdBlock){
	   
 		Query query = entityManager.createNamedQuery("IcdBlock.findByKey")
 				.setParameter("primaryKey", icdBlock);

 		IcdBlock icdBlock1 = (IcdBlock) query.getSingleResult();
 		
 		return icdBlock1;
   }
   
	public BeanItemContainer<SelectValue> getProcedureforSpeciality(Long specialityKey){
		   
		   BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
		   Query query = entityManager.createNamedQuery("ProcedureSpecialityMaster.findBySpecialistKey")
				   .setParameter("specialityKey", specialityKey);
		   List<ProcedureSpecialityMaster> procedureMaster = (List<ProcedureSpecialityMaster>) query.getResultList();
		   if(procedureMaster != null && !procedureMaster.isEmpty()) {
			   List<SelectValue> listProcedures = new ArrayList<SelectValue>();
			   for (ProcedureSpecialityMaster procedureSpecialityMaster : procedureMaster) {
				SelectValue selectSpaciality = new SelectValue();
				selectSpaciality.setId(procedureSpecialityMaster.getKey().longValue());
				selectSpaciality.setValue(procedureSpecialityMaster.getProcedureName());
				listProcedures.add(selectSpaciality);
				selectSpaciality =null;
			}
			   procedureListContainer.addAll(listProcedures);
		   }
		   
		   
		   return procedureListContainer; 
	   }
	@SuppressWarnings("unused")
	private String getRemarksForPreauth(Long statusKey, PreauthDTO bean,
			Preauth preauth) {
		Status status = new Status();
		Stage stage = new Stage();
		String remarks = "";
		String strUserName = bean.getStrUserName();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		preauth.getKey();
		switch (statusKey.intValue()) {
		case 22:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApprovalRemarks();
//			ResidualAmount residualAmt = new ResidualAmount();
//			residualAmt.setPreauth(preauth);
//			residualAmt.setStage(preauth.getStage());
//			residualAmt.setStatus(preauth.getStatus());
//			residualAmt.setApprovedAmount(bean.getResidualAmountDTO()
//					.getApprovedAmount());
//			residualAmt.setRemarks(bean.getResidualAmountDTO().getRemarks());
//			entityManager.persist(residualAmt);
//			entityManager.flush();
			break;
		case 24:
			remarks = bean.getPreauthMedicalDecisionDetails().getQueryRemarks();
			/*PreauthQuery preAuthQuery = new PreauthQuery();
			preAuthQuery.setPreauth(preauth);
			preAuthQuery.setQueryRemarks(remarks);
			preAuthQuery.setCreatedBy(strUserName);
			status.setKey(bean.getStatusKey());
			stage.setKey(bean.getStageKey());
			preAuthQuery.setStatus(status);
			preAuthQuery.setStage(stage);
			preAuthQuery.setCreatedBy(strUserName);
			preAuthQuery.setDoctorNote(bean.getPreauthMedicalDecisionDetails().getDoctorNote());
			//R1295
			MastersValue qryTyp = new MastersValue();
			qryTyp.setKey(bean.getQueryType().getId());
			qryTyp.setValue(bean.getQueryType().getValue());
			preAuthQuery.setQueryType(qryTyp);
			
			entityManager.persist(preAuthQuery);
			entityManager.flush();
			entityManager.clear();*/
			break;
		case 23:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			break;
		case 26:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getDenialRemarks();
			// CR R2019065
			//IMSSUPPOR-29249
			if(bean.getPreauthMedicalDecisionDetails().getUserClickAction()!=null && bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("clsNotReq")
					&& !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				
				remarks = bean.getPreauthMedicalDecisionDetails()
						.getRemarksForInsured();
			}
			break;
		case 27:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getEscalationRemarks();			
			break;
		case 28:
			createCoordinator(bean, preauth,status,stage);
			break;

		default:
			break;
		}
		return remarks;
	}

	private void createCoordinator(PreauthDTO bean, Preauth preauth,Status status, Stage stage) {
		Coordinator coordinator = new Coordinator();
		MastersValue master = null;
		/*if(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest() != null) {
			masterValue.setKey(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest().getId());
			masterValue.setValue(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest().getValue());
		}*/
		if(bean.getCoordinatorDetails().getTypeofCoordinatorRequest() != null) {
		    master = getMaster(bean.getCoordinatorDetails().getTypeofCoordinatorRequest().getId());
//			masterValue.setKey(bean.getCoordinatorDetails().getTypeofCoordinatorRequest().getId());
//			masterValue.setValue(bean.getCoordinatorDetails().getTypeofCoordinatorRequest().getValue());
		}else if(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest() != null && 
				bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest().getId() != null){
			master =  getMaster(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest().getId());
		}
		
		
		
		coordinator.setCoordinatorRequestType(master);
		// coordinator.setCoordinatorRemarks(bean.getPreauthMedicalDecisionDetails().getReasonForRefering());
		/**
		 * Remarks given in the pre auth submission page should be set in
		 * requestor remarks column. Hence commenting above code and adding
		 * below one.
		 * */
		/*coordinator.setRequestorRemarks(bean.getPreauthMedicalDecisionDetails()
				.getReasonForRefering());*/
		String strUserName = bean.getStrUserName();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		if(bean.getCoordinatorDetails().getReasonForRefering() != null){
			coordinator.setRequestorRemarks(bean.getCoordinatorDetails()
					.getReasonForRefering());
		}else if(bean.getPreauthMedicalDecisionDetails().getReasonForRefering() != null){
			coordinator.setRequestorRemarks(bean.getPreauthMedicalDecisionDetails().getReasonForRefering());
		}
		coordinator.setActiveStatus(1l);
		stage.setKey(bean.getStageKey());
		status.setKey(bean.getStatusKey());
		coordinator.setStage(stage);
		coordinator.setStatus(status);
		coordinator.setClaim(preauth.getClaim());
		coordinator.setPolicy(preauth.getPolicy());
		coordinator.setIntimation(preauth.getIntimation());
		coordinator.setTransactionFlag("C");
		coordinator.setTransactionKey(preauth.getKey());
		coordinator.setCreatedBy(strUserName);
		entityManager.persist(coordinator);
		entityManager.flush();
		entityManager.clear();
		bean.getCoordinatorDetails().setKey(coordinator.getKey());
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
			}

		return a_mastersValue;
	}

	public Claim searchByClaimKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
	}
	private void createEscalate(PreauthDTO bean, Preauth preauth,
			Status status, Stage stage, String remarks) {
		PreauthEscalate preauthEscalte = new PreauthEscalate();
		//preauthEscalte.setPreauth(preauth);
		preauthEscalte.setClaim(preauth.getClaim());
		
		String strUserName = bean.getStrUserName();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		
		preauthEscalte.setEscalateRemarks(remarks);
		status.setKey(bean.getStatusKey());
		stage.setKey(bean.getStageKey());
		preauthEscalte.setStatus(status);
		preauthEscalte.setStage(stage);
		preauthEscalte.setTransactionFlag("C");
		preauthEscalte.setTransactionKey(preauth.getKey());
		preauthEscalte.setCreatedBy(strUserName);
		
		SelectValue escalateTo = bean.getPreauthMedicalDecisionDetails()
				.getEscalateTo();
		if (escalateTo != null) {
			MastersValue value = new MastersValue();
			value.setKey(escalateTo.getId());
			value.setValue(escalateTo.getValue());
			preauthEscalte.setEscalateTo(value);
		}
		entityManager.persist(preauthEscalte);
		
		entityManager.flush();
		entityManager.clear();
	}
	
	private void createSpecialist(PreauthDTO bean, Preauth preauth,Status status, Stage stage, String remarks){
		
		
		Specialist specialist = new Specialist();
		
		specialist.setClaim(preauth.getClaim());
		specialist.setReasonForReferring(remarks);
//		specialist.setSpecialistRemarks(remarks);
		status.setKey(bean.getStatusKey());
		stage.setKey(bean.getStageKey());
		
		specialist.setStatus(status);
		specialist.setStage(stage);
		specialist.setTransactionKey(preauth.getKey());
		specialist.setTransactionFlag("C");
		
		String strUserName = bean.getStrUserName();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		specialist.setCreatedBy(strUserName);
		
		SelectValue specialistType = bean.getPreauthMedicalDecisionDetails().getSpecialistValue();
		
		if (specialistType != null) {
			MastersValue value = new MastersValue();
			value.setKey(specialistType.getId());
			value.setValue(specialistType.getValue());
			specialist.setSpcialistType(value);
		}
		specialist.setFileName(bean.getFileName());
		specialist.setFileToken(bean.getTokenName());
		
		entityManager.persist(specialist);
		entityManager.flush();
		entityManager.clear();
	    
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	

	public Preauth submitPreAuth(PreauthDTO preauthDTO, Boolean isPreauthEnhancement) throws Exception {
		
		
	/*	if(null != preauthDTO.getExistingStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS)){
			preauthDTO.setStatusKey( preauthDTO.getExistingStatusKey());
		}*/
		
//		Calendar instance = Calendar.getInstance();
		Date date1 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%% SUBMIT SERVICE STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date1);
		/*try
		{*/
			//utx.begin();
		//Boolean isReferToFLP = false;
		/*if(preauthDTO.getStatusKey() != null && ReferenceTable.getReferToFLPKeys().containsKey(preauthDTO.getStatusKey())) {
			//isReferToFLP = true;
		}*/
		if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks() 
				&& !("").equals(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks()))
		{
			preauthDTO.getPreauthDataExtractionDetails().setTreatmentRemarks(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks());

		}
		log.info("**** SUBMIT PREAUTH ****------> " + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NO INTIMATIION"));
		PreauthMapper prauthMapper = PreauthMapper.getInstance();
		Preauth preauth = prauthMapper.getPreauth(preauthDTO);
		if (preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory() !=null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue() != null) {
			preauth.setCategory(preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue());
		}
		if(null != preauthDTO.getSfxMatchedQDate())
			preauth.setSfxMatchedQDate(preauthDTO.getSfxMatchedQDate());
		if(null != preauthDTO.getSfxRegisteredQDate())
			preauth.setSfxRegisteredQDate(preauthDTO.getSfxRegisteredQDate());
		
		if(preauthDTO.getProcessType() != null) {
			preauth.setProcessType(preauthDTO.getProcessType());
		}
		if(null != preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss()){
			preauth.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		}
		if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			preauth.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
		}
		if(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			preauth.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
		}
		String strUserName = preauthDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		
		Claim currentClaim = null;
		
		if(preauth.getClaim() != null) {
			
			
			currentClaim = searchByClaimKey(preauth.getClaim().getKey());
			
//			currentClaim.setStatus(preauth.getStatus());
//			currentClaim.setStage(preauth.getStage());
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
				currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
			}
			currentClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			currentClaim.setDataOfAdmission(preauth.getDataOfAdmission());
			currentClaim.setDataOfDischarge(preauth.getDateOfDischarge());
			// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
			if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
				currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
				currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
				currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
			}
			if (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeathFlag() != null){
				currentClaim.setIncidenceFlag(preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeathFlag());
			}
			if (preauthDTO.getPreauthDataExtractionDetails().getDateOfDeathAcc() != null){
				currentClaim.setIncidenceDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeathAcc());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident())
			{
				currentClaim.setAccidentDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath())
			{
				currentClaim.setDeathDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement())
			{
				currentClaim.setDisablementDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement());
			}
			
			if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
				 || preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
				|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
				 || preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
				
				currentClaim.setPaHospExpenseAmt(0d);
				preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			}
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null){
					String bufferFlag = preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() ? "Y":"N";
					currentClaim.setGmcCorpBufferFlag(bufferFlag);
					if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
						currentClaim.setGmcCorpBufferLmt(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim().doubleValue());
					}
				}
			}
					
			
			if(null != currentClaim.getIntimation().getPolicy().getProduct().getKey() &&
					(ReferenceTable.getGPAProducts().containsKey(currentClaim.getIntimation().getPolicy().getProduct().getKey())))
			{
				
				currentClaim.setGpaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
				currentClaim.setGpaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());				
				currentClaim.setGpaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
				currentClaim.setGpaRiskName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
				currentClaim.setGpaRiskDOB(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskDOB());
				currentClaim.setGpaRiskAge(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskAge());				
				currentClaim.setGpaSection(preauthDTO.getPreauthDataExtractionDetails().getGpaSection());
				
				SelectValue gpaCategory = new SelectValue();
				if(null != preauthDTO.getPreauthDataExtractionDetails().getGpaCategory()){
					gpaCategory = preauthDTO.getPreauthDataExtractionDetails().getGpaCategory();
					
					if(null != gpaCategory.getValue()){
						
						String[] splitCategory = gpaCategory.getValue().split("-");
						String category = splitCategory[0];
						if(null != category && !("null").equalsIgnoreCase(category)){
							currentClaim.setGpaCategory(category);
						}
					}
					
				}
			}
			
			entityManager.merge(currentClaim);
			entityManager.flush();
			entityManager.clear();
			
			preauth.setClaim(currentClaim);
		}

		// Diff Amount is used for Accumulator ...
		if(preauth.getTotalApprovalAmount() == null) {
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt());
		}
		
		Double diffAmt = 0d;
		if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)  && preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null) {
			diffAmt = preauth.getTotalApprovalAmount() - preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt();
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
		}
		preauth.setDiffAmount(diffAmt);
		if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)) {
			Double diffAmount =  preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() -  preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt();
			preauth.setDiffAmount(diffAmount);
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt());
			
			//CR R1313
			if(!ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				preauth.setRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getDownsizeRemarks());
				preauth.setDownsizeInsuredRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getDownsizeInsuredRemarks());
			}
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			
		}
		
		if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
			
			Double diffAmount =  preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() -  preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
			preauth.setDiffAmount(diffAmount);
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
		}
		
		if(preauthDTO.getProcessType() != null) {
			preauth.setProcessType(preauthDTO.getProcessType());
		}
		if(preauth.getStatus() != null && ReferenceTable.WITHDRAW_KEYS.containsKey(preauth.getStatus().getKey())) {
			preauth.setProcessType("W");
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt());
			preauth.setInsuredRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRemarksForInsured());
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
		}
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
				|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
			preauth.setProcessType("W");
			preauth.setTotalApprovalAmount(0d);
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			//IMSSUPPOR-23037 & IMSSUPPOR-23349
			Double diffAmount =  0d - (preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() : 0d);
			preauth.setDiffAmount(diffAmount < 0 ? diffAmount : (diffAmount * -1));
		
			//CR R1313
			if(!ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				preauth.setWithdrawRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawRemarks());
				preauth.setInsuredWithdrawRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured());
				
				if(preauthDTO.getPreauthMedicalDecisionDetails().getPolicyConditionNoReject() != null) {
					preauth.setConditionNo(preauthDTO.getPreauthMedicalDecisionDetails().getPolicyConditionNoReject());
				}
			}
		
		}
		
		if(preauthDTO.getPreauthMedicalDecisionDetails().getIcdExclusionReason()!=null){
			preauth.setReasonForIcdExclusion(preauthDTO.getPreauthMedicalDecisionDetails().getIcdExclusionReason());
		}
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
			preauth.setProcessType("T");
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt());
			preauth.setInsuredRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRemarksForInsured());
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
				preauth.setTotalApprovalAmount(0d);
			}
			
			if(preauthDTO.getPreauthMedicalDecisionDetails().getRejSubCategory() != null) {
				MasRejectSubCategory rejSubCategory = new MasRejectSubCategory();
				rejSubCategory.setKey(preauthDTO.getPreauthMedicalDecisionDetails().getRejSubCategory().getId());
				preauth.setRejSubCategory(rejSubCategory);
			}	
			
		}
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
			preauth.setProcessType("L"); 
			preauth.setTotalApprovalAmount(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt());
			preauth.setInsuredRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRemarksForInsured());
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			
			if(preauthDTO.getPreauthMedicalDecisionDetails().getUserClickAction() != null && preauthDTO.getPreauthMedicalDecisionDetails().getUserClickAction().equals("clsNotReq")
					&& !ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				preauth.setInsuredRemarks(null);
			}
		}
	
		preauth.setActiveStatus(1l);
		preauth.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		
		if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("n/a")) {
				preauth.setAutoRestoration("O");
			} else {
				preauth.setAutoRestoration(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("not") ? "N" : "Y");
			}
		}
		
		if (!isPreauthEnhancement) {
			preauth.setRemarks(getRemarksForPreauth(preauth.getStatus()
					.getKey(), preauthDTO, preauth));

		} else {
			preauth.setRemarks(getRemarksForPreauthEnhancement(preauth
					.getStatus().getKey(), preauthDTO, preauth));
			
		}
		
		if(preauthDTO.getStatusKey() != null && ReferenceTable.getReferToFLPKeys().containsKey(preauthDTO.getStatusKey())) {
			preauth.setReferToFLPRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getReferToFLPremarks());
		}
		
		if(preauthDTO.getStatusKey() != null) {
			if(ReferenceTable.ENHANCEMENT_REFER_64_VB_COMPLIANCE.equals(preauthDTO.getStatusKey()) || ReferenceTable.PREAUTH_REFER_64_VB_COMPLIANCE.equals(preauthDTO.getStatusKey())){
				preauth.setRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRefer64VBRemarks());
			}
			if(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT.equals(preauthDTO.getStatusKey()) || ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS.equals(preauthDTO.getStatusKey())){
				preauth.setWithdrawRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawRemarks());
				preauth.setRejectRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRejectionRemarks());
				
				preauth.setInsuredRemarks(preauthDTO.getPreauthMedicalDecisionDetails()
						.getRemarksForInsured());
			}
		}
		
		if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag()) {
			preauth.setWorkPlace(preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag());
		}
		
		if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequest()) {
			preauth.setFvrAlertFlag(SHAConstants.YES_FLAG);
		}
		
		if(preauth.getStatus().getKey() != null 
				&& ((ReferenceTable.PREAUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()) || (ReferenceTable.ENHANCEMENT_QUERY_STATUS).equals(preauth.getStatus().getKey())))
		{
			preauth.setLetterVerified(preauthDTO.isLetterContentValidated() ? "Y" : "N");
			//IMSSUPPOR-31100
			preauth.setTotalApprovalAmount(null);
		}
		
		/**
		 * GLX2020021
		 * */
		
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null
				&& preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue() != null) {
			if( preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.ONLY_PRE_AUTH)) {
			
					preauth.setStpProcessLevel(ReferenceTable.ONLY_PRE_AUTH_KEY);
					if(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate() !=null){
						preauth.setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
					}

				
			}else if(preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL)) {
					preauth.setStpProcessLevel(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL_KEY);
					if(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate() !=null){
						preauth.setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
					}
			}
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails() != null 
				&& preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable() !=null){
			preauth.setImplantFlag(preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable() ? "Y" : "N");
		}
		
		//Newly add field for auto allocation submit
		if (preauthDTO.getIsPreauthAutoAllocationQ()){
			preauth.setAaUserId(userNameForDB);
			preauth.setAaSubmitDate(new Timestamp(System.currentTimeMillis()));
		}
		if(preauthDTO.getPreauthMedicalDecisionDetails() != null 
				&& preauthDTO.getPreauthMedicalDecisionDetails().getBehaviourHosp1chkbox() !=null){
			preauth.setHospCollectOvrAmtChkBx(preauthDTO.getPreauthMedicalDecisionDetails().getBehaviourHosp1chkbox() ? "Y" : "N");
		}
		if(preauthDTO.getPreauthMedicalDecisionDetails() != null 
				&& preauthDTO.getPreauthMedicalDecisionDetails().getBehaviourHospMbagreedchkbox() !=null){
			preauth.setMbAgrdDisNtAplyChkBx(preauthDTO.getPreauthMedicalDecisionDetails().getBehaviourHospMbagreedchkbox() ? "Y" : "N");
		}
		
		//added for cr2019184 policy isntalment premium to save in cashles table
		if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			preauth.setPremiumAmt(preauthDTO.getPolicyInstalmentPremiumAmt() != null ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0d);
		}
		
//		Portal Flag updated in cashless table
		if(preauthDTO.getNhpUpdKey() != null){
			preauth.setNhpUpdDocKey(preauthDTO.getNhpUpdKey());
		}
		
		if (preauthDTO.getKey() != null) {
			preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			preauth.setModifiedBy(userNameForDB);
			entityManager.merge(preauth);
			log.info("------PreauthKey------>"+preauth.getKey()+"<------------");

		} else {
			preauth.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			preauth.setCreatedBy(userNameForDB);
			entityManager.persist(preauth);
		}
		entityManager.flush();
		entityManager.clear();
		
		preauthDTO.setKey(preauth.getKey());
//		if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
//				|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_STATUS)){
//			if(preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason() != null 
//					&& preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)){
//				
//				preauth.setTotalApprovalAmount(0d);
//				
//				
//			}else{
//				Double cpuProvisionAmt = preauthDTO.getCpuProvisionAmt();
//				if(cpuProvisionAmt != null){
//					preauth.setTotalApprovalAmount(cpuProvisionAmt);
//				}
//			}
//		}
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(! (preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
					preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID))){
				preauth.setInjuryCauseId(null);
			}
			
			if(isPreauthEnhancement){
				if(preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null){
					preauth.setOtherBenefitApprovedAmt(preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount());
				}
				
			}
			
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null && ! preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt()){
			preauth.setOtherBenefitApprovedAmt(0d);
		}
		
		/*if(preauthDTO.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue() != null && ! preauthDTO.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue().isEmpty()){
			PccRemarks pccRemarks = new PccRemarks();
			Status status = new Status();
			Stage stage = new Stage();
			pccRemarks.setIntimation(preauth.getIntimation());
			pccRemarks.setClaimType(preauth.getClaim().getClaimType());
			pccRemarks.setPccRemarks(preauthDTO.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue());
			status.setKey(preauthDTO.getStatusKey());
			stage.setKey(preauthDTO.getStageKey());
			pccRemarks.setStatus(status);
			pccRemarks.setStage(stage);
			pccRemarks.setClaim(preauth.getClaim());
			pccRemarks.setActiveStatus(1L);
			pccRemarks.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			pccRemarks.setCreatedBy(userNameForDB);
			pccRemarks.setCashlessKey(preauth.getKey());
			
			entityManager.persist(pccRemarks);
			entityManager.flush();
			entityManager.clear();
			
		}*/
		
		entityManager.merge(preauth);
		entityManager.flush();
		entityManager.clear();
		log.info("------Preath Key------>"+preauth+"<------------");
		
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REFER_64_VB_COMPLIANCE)){
			insertToVB64Approval(preauth, "PRE AUTH");
		}
		if( preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REFER_64_VB_COMPLIANCE)){
			insertToVB64Approval(preauth, "ENHANCEMENT");
		}
		
		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE
		 * during policy refractoring activity. Below code is added for inserting
		 * value in the insured key column.
		 * */
		if(null != preauth && null != preauth.getStatus() && (ReferenceTable.PREAUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()))
		{
			PreauthQuery preAuthQuery = new PreauthQuery();
			Status status = new Status();
			Stage stage = new Stage();			
			preAuthQuery.setPreauth(preauth);
			preAuthQuery.setQueryRemarks(preauth.getRemarks());
			preAuthQuery.setCreatedBy(strUserName);
			status.setKey(preauthDTO.getStatusKey());
			stage.setKey(preauthDTO.getStageKey());
			preAuthQuery.setStatus(status);
			preAuthQuery.setStage(stage);
			preAuthQuery.setCreatedBy(strUserName);
			preAuthQuery.setDoctorNote(preauthDTO.getPreauthMedicalDecisionDetails().getDoctorNote());
			
			//R1295
			if(preauthDTO.getQueryType() != null){
				MastersValue qryTyp = new MastersValue();
				qryTyp.setKey(preauthDTO.getQueryType().getId());
				qryTyp.setValue(preauthDTO.getQueryType().getValue());
				preAuthQuery.setQueryType(qryTyp);
			}
			
			entityManager.persist(preAuthQuery);
			entityManager.flush();
			entityManager.clear();
		}
		
		if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
			Status status = new Status();
			Stage stage = new Stage();	
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
				createSpecialist(preauthDTO, preauth, status, stage, preauth.getRemarks());
				
			}else{
				
				createEscalate(preauthDTO, preauth, status, stage, preauth.getRemarks());
			}
		}
		
		ResidualAmount residualAmt = new ResidualAmount();
		residualAmt.setKey(null);
		if(preauthDTO.getResidualAmountDTO().getKey() != null) {
			residualAmt.setKey(preauthDTO.getResidualAmountDTO().getKey());
		}
		residualAmt.setTransactionKey(preauth.getKey());
		residualAmt.setStage(preauth.getStage());
		residualAmt.setStatus(preauth.getStatus());
		residualAmt.setRemarks(preauthDTO.getResidualAmountDTO().getRemarks());
		residualAmt.setAmountConsideredAmount(preauthDTO.getResidualAmountDTO().getAmountConsideredAmount());
		residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO().getMinimumAmount());
		residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO().getCopayAmount());
		residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO().getApprovedAmount());
		residualAmt.setCopayPercentage(preauthDTO.getResidualAmountDTO().getCopayPercentage());
		residualAmt.setRemarks(preauthDTO.getResidualAmountDTO().getRemarks());
		residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO().getNetAmount() != null ? preauthDTO.getResidualAmountDTO().getNetAmount() : 0d );
		residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d );
		
		if(null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
			if(null != preauthDTO.getResidualAmountDTO().getCoPayTypeId()){		
				MastersValue copayTypeValue = new MastersValue();
				copayTypeValue.setValue(preauthDTO.getResidualAmountDTO().getCoPayTypeId().getValue());
				copayTypeValue.setKey(preauthDTO.getResidualAmountDTO().getCoPayTypeId().getId());
				residualAmt.setCoPayTypeId(copayTypeValue);
			}
		}
		
		if (residualAmt.getKey() == null) {
			entityManager.persist(residualAmt);
			
		} else {
			entityManager.merge(residualAmt);
		}
		entityManager.flush();
		entityManager.clear();
		preauthDTO.setKey(preauth.getKey());

		preauthDTO.getCoordinatorDetails().setPreauthKey(preauth.getKey());
		preauthDTO.getCoordinatorDetails().setIntimationKey(
				preauth.getIntimation().getKey());
		preauthDTO.getCoordinatorDetails().setPolicyKey(
				preauth.getPolicy().getKey());

		if (preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag()
				.toLowerCase().equalsIgnoreCase("y")) {
			Coordinator coordinator = prauthMapper.getCoordinator(preauthDTO
					.getCoordinatorDetails());

			coordinator.setActiveStatus(1l);
			
			coordinator.setCreatedBy(userNameForDB);
			coordinator.setStage(preauth.getStage());
			coordinator.setStatus(preauth.getStatus());
			coordinator.setClaim(preauth.getClaim());
			coordinator.setTransactionKey(preauth.getKey());
			coordinator.setTransactionFlag("C");

			if (coordinator.getKey() != null) {
				coordinator.setModifiedBy(userNameForDB);
				coordinator.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(coordinator);
			} else {
				entityManager.persist(coordinator);
			}
			entityManager.flush();
			entityManager.clear();
			preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
			log.info("------Coordinator------>"+coordinator+"<------------");
		}

		List<SpecialityDTO> specialityDTOList = preauthDTO
				.getPreauthDataExtractionDetails().getSpecialityList();
		if (!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = prauthMapper
						.getSpeciality(specialityDTO);
				//speciality.setPreauth(preauth);
				speciality.setClaim(preauth.getClaim());
				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
				entityManager.flush();
				entityManager.clear();
			}
			
			//entityManager.flush();
		}

		//entityManager.clear();
		
	   List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = preauthDTO.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
			
			DiagnosisDetailsTableDTO diagnosisDetailsDTO = diagnosisProcedureTableDTO.getDiagnosisDetailsDTO();
			diagnosisDetailsDTO.setAmountConsideredAmount(diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
			diagnosisDetailsDTO.setNetAmount(diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue()  : 0d);
			diagnosisDetailsDTO.setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
			diagnosisDetailsDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString(diagnosisProcedureTableDTO.getCoPayPercentage().getValue()));
			diagnosisDetailsDTO.setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ? diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
			diagnosisDetailsDTO.setApprovedAmount(diagnosisProcedureTableDTO.getMinimumAmount() != null ?  diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() : 0d);
			diagnosisDetailsDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
			diagnosisDetailsDTO.setApproveRemarks(diagnosisProcedureTableDTO.getRemarks());
			
			diagnosisDetailsDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
			diagnosisDetailsDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
			diagnosisDetailsDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());
			
			PedValidation pedValidation = prauthMapper.getPedValidation(diagnosisDetailsDTO);

			if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getPrimaryDiagnosis()!=null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getPrimaryDiagnosis())
			{
				pedValidation.setPrimaryDiagnosis("Y");
			}
			else
			{
				pedValidation.setPrimaryDiagnosis("N");
			}
			if(null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
				diagnosisDetailsDTO.setCoPayTypeId(diagnosisProcedureTableDTO.getCoPayType());
			}
			
			if(isPreauthEnhancement) {
//				diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getApprovedAmount() != null ? diagnosisDetailsDTO.getApprovedAmount() : 0d) - (diagnosisDetailsDTO.getOldApprovedAmount() != null ? diagnosisDetailsDTO.getOldApprovedAmount() : 0d ));
				diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getOldApprovedAmount() != null ? diagnosisDetailsDTO.getOldApprovedAmount() : 0d) - (diagnosisDetailsDTO.getNetApprovedAmount() != null ? diagnosisDetailsDTO.getNetApprovedAmount() : 0d ));
			}
			
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
				diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getNetApprovedAmount() != null ? diagnosisDetailsDTO.getNetApprovedAmount() : 0d ));
			}
			
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
				diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getOldApprovedAmount() != null ? diagnosisDetailsDTO.getOldApprovedAmount() : 0d ));
			}
			
		} else if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
			ProcedureDTO procedureDTO = diagnosisProcedureTableDTO.getProcedureDTO();
			procedureDTO.setAmountConsideredAmount(diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
			procedureDTO.setNetAmount(diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue()  : 0d);
			procedureDTO.setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
			procedureDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString(diagnosisProcedureTableDTO.getCoPayPercentage().getValue()));
			procedureDTO.setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ? diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
			procedureDTO.setApprovedAmount(diagnosisProcedureTableDTO.getMinimumAmount() != null ?  diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() : 0d);
			procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO.getRemarks());
			procedureDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
			
			procedureDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
			procedureDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
			procedureDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());
			
			if(null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
				procedureDTO.setCoPayTypeId(diagnosisProcedureTableDTO.getCoPayType());
			}
			
			if(isPreauthEnhancement) {
//				procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount() != null ? procedureDTO.getApprovedAmount() : 0d) - (procedureDTO.getOldApprovedAmount() != null ? procedureDTO.getOldApprovedAmount() : 0d ));
				procedureDTO.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO.getOldApprovedAmount() : 0d) - (procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount() : 0d ));
			}
			
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
				procedureDTO.setDiffAmount((procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount() : 0d ));
			}
			
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || ReferenceTable.WITHDRAW_KEYS.containsKey(preauth.getStatus().getKey())) {
				procedureDTO.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO.getOldApprovedAmount() : 0d ));
			}
			//GLX2020047
			if(diagnosisProcedureTableDTO.getAgreedPackageAmt() != null && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getAgreedPackageAmt())){
				Double rate = Double.valueOf(diagnosisProcedureTableDTO.getAgreedPackageAmt());
				procedureDTO.setAgreedPackageRate(rate.longValue());
			}
			if(diagnosisProcedureTableDTO.getPackageAmt() != null && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getPackageAmt())){
				Double rate = Double.valueOf(diagnosisProcedureTableDTO.getPackageAmt());
				procedureDTO.setPackageRate(rate.longValue());
			}
			procedureDTO.setPkgReasonForChge(diagnosisProcedureTableDTO.getReasonForPkgChange());
			
		}
		}
		
		List<ProcedureDTO> procedureList = preauthDTO
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		if (!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = prauthMapper.getProcedure(procedureDTO);
				procedure.setDeleteFlag(1l);
				
				//Boolean isWithdrawOrDownsize = false;
				if(preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.DOWNSIZE_STAGE)) {
					procedure.setProcessFlag("E");
					procedure.setAction("A");
					if(procedure.getDiffAmount() != null && procedure.getDiffAmount() > 0) {
						procedure.setAction("R");
					}
				}
				
				if(procedure.getDiffAmount() != null && procedure.getDiffAmount() == 0) {
					procedure.setAction("0");
				}
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					if(procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double.valueOf(procedureDTO.getCopay().getValue()));
					}
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
					procedure.setProcessFlag("W");
				}
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)) {
					procedure.setProcessFlag("D");
				}
				
				if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					procedure.setProcessFlag("D");
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
					procedure.setProcessFlag("T");
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
					procedure.setProcessFlag("L");
				}
				
				procedure.setTransactionKey(preauth.getKey());
				procedure.setStage(preauth.getStage());
				procedure.setStatus(preauth.getStatus());
				procedure.setRecTypeFlag("A");
				if (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
					procedure.setApprovedAmount(procedureDTO
							.getNetApprovedAmount());
					procedure.setNetApprovedAmount(procedureDTO
							.getNetApprovedAmount());
					procedure.setApprovedRemarks(procedureDTO
							.getApprovedRemarks());
					
				}
				if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
					procedure.setRecTypeFlag("C");
				}
				
				if(null == procedure.getProcedureID()){
						procedure.setProcedureID(0l);
				}

				if (procedure.getKey() != null) {
					procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					procedure.setModifiedBy(preauthDTO.getStrUserName());
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
					log.info("------Procedure------>"+ procedure.getKey() +"<------------");
				}
				
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
				
				DiagnosisDetailsTableDTO pedValidationDTOWithCodes = setIcdChapterBlock(pedValidationDTO);
				PedValidation pedValidation = prauthMapper.getPedValidation(pedValidationDTOWithCodes);
				
				pedValidation.setDeleteFlag(1l);
				
				if(pedValidationDTO.getPrimaryDiagnosis()!=null && pedValidationDTO.getPrimaryDiagnosis())
				{
					pedValidation.setPrimaryDiagnosis("Y");
				}
				else
				{
					pedValidation.setPrimaryDiagnosis("N");
				}
				if(preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)
						|| preauth.getStage().getKey().equals(ReferenceTable.DOWNSIZE_STAGE)) {
					pedValidation.setProcessFlag("E");
					pedValidation.setAction("A");
					if(pedValidation.getDiffAmount() != null && pedValidation.getDiffAmount() > 0) {
						pedValidation.setAction("R");
					}
				}
				
				if(pedValidation.getDiffAmount() != null && pedValidation.getDiffAmount() == 0) {
					pedValidation.setAction("0");
				}
				
				if(ReferenceTable.WITHDRAW_KEYS.containsKey(preauth.getStatus().getKey())) {
					pedValidation.setProcessFlag("W");
				}
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)) {
					pedValidation.setProcessFlag("D");
				}
				
				if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					pedValidation.setProcessFlag("D");
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
					pedValidation.setProcessFlag("T");
				}
				
				if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
					pedValidation.setProcessFlag("L");
				}
				
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if(pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double.valueOf(pedDetailsTableDTO.getCopay().getValue()));
							break;
						}
					}
				}
				pedValidation.setTransactionKey(preauth.getKey());
				pedValidation.setStage(preauth.getStage());
				pedValidation.setStatus(preauth.getStatus());
				pedValidation.setIntimation(preauth.getIntimation());
				pedValidation.setPolicy(preauth.getPolicy());
				pedValidation.setRecTypeFlag("A");
				if (preauth.getStatus().getKey() == ReferenceTable.PREAUTH_APPROVE_STATUS) {
					pedValidation.setApproveAmount(pedValidationDTO
							.getNetApprovedAmount());
					pedValidation.setNetApprovedAmount(pedValidationDTO
							.getNetApprovedAmount());
					pedValidation.setApprovedRemarks(pedValidationDTO
							.getApproveRemarks());
					
				}
				
				if(preauth.getStatus().getKey() == ReferenceTable.PREAUTH_APPROVE_STATUS || preauth.getStatus().getKey()== ReferenceTable.ENHANCEMENT_APPROVE_STATUS){
					pedValidation.setRecTypeFlag("C");
				}
				
				if (pedValidation.getKey() != null) {
					pedValidation.setModifiedBy(userNameForDB);
					pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				log.info("------PedValidation------>"+pedValidation+"<------------");
				entityManager.flush();
				entityManager.clear();
				
				List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
				if(!pedList.isEmpty()) {
					MastersValue value = null;
					ExclusionDetails exclusionValue = null;
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						DiagnosisPED diagnosisPED = prauthMapper.getDiagnosisPED(pedDetailsTableDTO);
						diagnosisPED.setPedValidation(pedValidation);
						if(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
							value = new MastersValue();
							value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);
							value.setValue(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() : null);
							diagnosisPED.setDiagonsisImpact(value);
							
						}
						if(pedDetailsTableDTO.getExclusionDetails() != null) {
							exclusionValue = new ExclusionDetails();
							exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);
							exclusionValue.setExclusion(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() : null);
							diagnosisPED.setExclusionDetails(exclusionValue);
						}
						diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO.getRemarks());
						if (diagnosisPED.getKey() != null) {
							entityManager.merge(diagnosisPED);
						} else {
							entityManager.persist(diagnosisPED);
							pedDetailsTableDTO.setKey(diagnosisPED.getKey());
						}
						log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
						entityManager.flush();
						entityManager.clear();
				}
				}
				
			}
			
			List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO.getDeletedDiagnosis();
			
			if(deletedDiagnosis != null && ! deletedDiagnosis.isEmpty()){
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					PedValidation pedValidation = prauthMapper.getPedValidation(diagnosisDetailsTableDTO);
					pedValidation.setDeleteFlag(0l);
					pedValidation.setTransactionKey(preauth.getKey());
					if(pedValidation.getKey() != null){
						pedValidation.setModifiedBy(userNameForDB);
						pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(pedValidation);
						log.info("------DeletedDiagnosis------>"+pedValidation+"<------------");
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
			if(deletedProcedure != null && ! deletedProcedure.isEmpty()){
				for (ProcedureDTO procedureDTO : deletedProcedure) {
					Procedure procedure = prauthMapper.getProcedure(procedureDTO);
					procedure.setDeleteFlag(0l);
					procedure.setTransactionKey(preauth.getKey());
					if(procedure.getKey() != null){
						procedure.setModifiedBy(userNameForDB);
						procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(procedure);
						log.info("------DeletedProcedure------>"+procedure+"<------------");
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
		}

		List<NoOfDaysCell> claimedDetailsList = preauthDTO
				.getPreauthDataExtractionDetails().getClaimedDetailsList();
		if (!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
				ClaimAmountDetails claimedAmountDetails = prauthMapper
						.getClaimedAmountDetails(noOfDaysCell);
				claimedAmountDetails.setPreauth(preauth);
				claimedAmountDetails.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
				claimedAmountDetails.setActiveStatus(1l);
				claimedAmountDetails.setStage(preauth.getStage());
				claimedAmountDetails.setStatus(preauth.getStatus());
				if (claimedAmountDetails.getKey() != null) {
					entityManager.merge(claimedAmountDetails);
					log.info("------ClaimAmountDetails------>"+claimedAmountDetails+"<------------");
				} else {
					entityManager.persist(claimedAmountDetails);
					noOfDaysCell.setKey(claimedAmountDetails.getKey());
				}
				entityManager.flush();
				entityManager.clear();
			}
			//entityManager.flush();
		}
		
		// set the Active Status as 0 for identifying the deleted column.
		if(preauthDTO.getDeletedClaimedAmountIds() != null && !preauthDTO.getDeletedClaimedAmountIds().isEmpty()) {
			for (Long claimAmountKey : preauthDTO.getDeletedClaimedAmountIds()) {
				ClaimAmountDetails claimAmountDetails = getClaimAmountDetails(claimAmountKey);
				claimAmountDetails.setActiveStatus(0l);
				entityManager.merge(claimAmountDetails);
				log.info("------ClaimAmountDetails------>"+claimAmountDetails+"<------------");
				entityManager.flush();
				entityManager.clear();
			}
			//entityManager.flush();
		}
		
		
		boolean fvrRadioOption = preauthDTO.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag() : Boolean.FALSE;
		if (fvrRadioOption) {
			boolean fvrpending = getFVRStatusIdByClaimKey(preauthDTO.getClaimDTO().getKey());
			if(fvrRadioOption){
				if(!fvrpending){		
					FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(preauthDTO.getClaimDTO().getKey());
					
					if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
					
						if(fvrobjByRodKey  != null ){
							DBCalculationService dbCalculationService = new DBCalculationService();
							dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
							fvrService.autoSkipFirstFVR(fvrobjByRodKey);
						}
					}
				}
			}
			FieldVisitRequest fvrRequest = new FieldVisitRequest();
			
			Claim claim = preauth.getClaim();
			
			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", preauth.getIntimation().getKey());
			
			claim = (Claim) findByIntimationKey.getSingleResult();
			
			if(claim.getIntimation() != null){
				Long hospital = claim.getIntimation().getHospital();
				Hospitals hospitalByKey = getHospitalByKey(hospital);
				Long cpuId = hospitalByKey.getCpuId();
				if(cpuId != null){
				TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
				fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}
				
			}
			
		//	fvrRequest.setPreauth(preauth);
			fvrRequest.setClaim(claim);
			fvrRequest.setFvrTriggerPoints(preauthDTO
					.getPreauthMedicalDecisionDetails().getFvrTriggerPoints());
			SelectValue allocationTo = preauthDTO
					.getPreauthMedicalDecisionDetails().getAllocationTo();
			
			MastersValue value = new MastersValue();
			value.setKey(allocationTo.getId());
			value.setValue(allocationTo.getValue());
			
			SelectValue assignTo = preauthDTO.getPreauthMedicalDecisionDetails().getAssignTo();
			if(assignTo != null){
				MastersValue assignToValue = new MastersValue();
				assignToValue.setKey(assignTo.getId());
				assignToValue.setValue(assignTo.getValue());
				fvrRequest.setAssignTo(assignToValue);
			}
			
			SelectValue priority = preauthDTO.getPreauthMedicalDecisionDetails().getPriority();
			if(priority != null){
				MastersValue priorityToValue = new MastersValue();
				priorityToValue.setKey(priority.getId());
				priorityToValue.setValue(priority.getValue());
				fvrRequest.setPriority(priorityToValue);
			}
			
			fvrRequest.setClaim(claim);
			fvrRequest.setIntimation(preauth.getIntimation());

			fvrRequest.setPolicy(preauth.getPolicy());
//			fvrRequest.setRepresentativeName(preauthDTO.getPreauthMedicalDecisionDetails().getre);
			fvrRequest.setFvrTriggerPoints(preauthDTO
					.getPreauthMedicalDecisionDetails().getFvrTriggerPoints());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(preauth.getActiveStatus());
			fvrRequest.setOfficeCode(preauth.getOfficeCode());
			fvrRequest.setCreatedBy(userNameForDB);
			fvrRequest.setActiveStatus(claim.getActiveStatus());
			//fvrRequest.setDocumentReceivedFlag();
			//fvrRequest.setActiveStatusDate(preauth.getActiveStatusDate());
			//fvrRequest.setDocumentReceivedFlag();
			//fvrRequest.setVersion(preauth.getVersion());
			//fvrRequest.setRepresentativeCode(preauth.);
			//fvrRequest.setAssignedDate(preauth.);
			fvrRequest.setStage(preauth.getStage());

			fvrRequest.setTransactionKey(preauth.getKey());
			fvrRequest.setTransactionFlag("C");
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
			fvrRequest.setStatus(fvrStatus);

			entityManager.persist(fvrRequest);
			entityManager.flush();
			entityManager.clear();
			
			viewFVRService.saveTriggerPoints(fvrRequest, preauthDTO
					.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());			
			
			log.info("------FieldVisitRequest------>"+fvrRequest+"<------------");
			ViewFVRFormDTO bean = new ViewFVRFormDTO();
			//bean.setUsername("zsma1");
			//bean.setPassword("welcome1");
			bean.setUsername(preauthDTO.getStrUserName());
			bean.setPassword(preauthDTO.getStrPassword());
			//TODO:
			//Preauth fvrPreAuth = getPreauthById(preauth.getKey());
			//viewFVRService.initiateBPM(fvrRequest, claim, bean,preauthDTO.getStageKey());
			viewFVRService.initiateDB(fvrRequest, claim, bean,preauthDTO.getStageKey());
			
			}

		Investigation checkInitiateInvestigation = checkInitiateInvestigation(preauth
				.getKey());
		if (checkInitiateInvestigation != null) {
			checkInitiateInvestigation
					.setInvestigatorName(preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorName() != null ? preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorName().getInvestigatorName() : null);
			;
			checkInitiateInvestigation.setIntimation(preauth.getIntimation());
			checkInitiateInvestigation
					.setInvestigatorCode(preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorName() != null ? preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorName().getInvestigatorCode() : null);
			checkInitiateInvestigation.setRemarks(preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getInvestigationReviewRemarks());
//			checkInitiateInvestigation.setPreAuth(preauth);
			checkInitiateInvestigation.setStage(preauth.getStage());
			checkInitiateInvestigation.setStatus(preauth.getStatus());
			checkInitiateInvestigation.setPolicy(preauth.getPolicy());
			entityManager.merge(checkInitiateInvestigation);
			entityManager.flush();
			entityManager.clear();
			log.info("------Investigation------>"+checkInitiateInvestigation+"<------------");
		}
		
		if(preauthDTO.getPreauthMedicalDecisionDetails().getFvrGradingDTO() != null){
			List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
				List<FVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2.getFvrGradingDTO();
				FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2.getKey());
				for (FVRGradingDTO fvrGradingDTO2 : fvrGradingDTO) {
					
					switch (Integer.valueOf(String.valueOf(fvrGradingDTO2.getKey()))) {
					case 8 : 
						fvrByKey.setPatientVerified(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 9: 
						fvrByKey.setDiagnosisVerfied(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 10: 
						fvrByKey.setRoomCategoryVerfied(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 11: 
						fvrByKey.setTriggerPointsFocused(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 12: 
						fvrByKey.setPedVerified(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 13: 
						fvrByKey.setPatientDischarged(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 14: 
						fvrByKey.setPatientNotAdmitted(fvrGradingDTO2.getStatusFlag());
					 	break;
					case 15: 
						fvrByKey.setOutstandingFvr(fvrGradingDTO2.getStatusFlag());
					 	break;
					default:
						break;
					}
				}
				fvrByKey.setFvrGradingDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(fvrByKey);
				entityManager.flush();
				entityManager.clear();
				log.info("------FieldVisitRequest------>"+fvrByKey+"<------------");
				
				
			
			}
		}
		
		
		
		Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
		if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
			
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = preauthDTO.getUpdateOtherClaimDetailDTO();
			if(!updateOtherClaimDetailDTO.isEmpty()){
				List<UpdateOtherClaimDetails> updateOtherClaimDetails = PreauthMapper.getInstance().getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
				for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
					updateOtherClaimDetails2.setCashlessKey(preauth.getKey());
					updateOtherClaimDetails2.setClaimKey(currentClaim.getKey());
					updateOtherClaimDetails2.setStage(preauth.getStage());
//					updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
					updateOtherClaimDetails2.setIntimationKey(currentClaim.getIntimation().getKey());
					updateOtherClaimDetails2.setStatus(preauth.getStatus());
					updateOtherClaimDetails2.setClaimType(currentClaim.getClaimType().getValue());
					//updateOtherClaimDetails2.setKey(null);
					if(updateOtherClaimDetails2.getKey() != null){
						entityManager.merge(updateOtherClaimDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(updateOtherClaimDetails2);
						entityManager.flush();
					}
				}
			}
		}
		
		/*Negotiation Details - CR20181286*/
		if((preauth.getStatus().getKey() == ReferenceTable.PREAUTH_APPROVE_STATUS || preauth.getStatus().getKey()== ReferenceTable.ENHANCEMENT_APPROVE_STATUS) 
				&& preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationAmount() != null
				&& preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationMade()){
			NegotiationAmountDetails negAmtDtls = new NegotiationAmountDetails();
			negAmtDtls.setKey(null);
			negAmtDtls.setIntimationKey(preauthDTO.getNewIntimationDTO().getKey());
			negAmtDtls.setIntimationNo(preauthDTO.getNewIntimationDTO().getIntimationId());
			negAmtDtls.setTransactionKey(preauth.getKey());
			negAmtDtls.setTransactionFlag("C");
			negAmtDtls.setActiveStatus(1l);
			negAmtDtls.setCpuKey(preauthDTO.getNewIntimationDTO().getCpuId());
			negAmtDtls.setNegotiatedAmt(preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationAmount());
			negAmtDtls.setSavedAmt(preauthDTO.getPreauthMedicalDecisionDetails().getSavedAmt());
			negAmtDtls.setClaimAppAmt(preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
			negAmtDtls.setStage(preauth.getStage());
			negAmtDtls.setStatus(preauth.getStatus());
			negAmtDtls.setCreatedBy(preauthDTO.getStrUserName());
			negAmtDtls.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			negAmtDtls.setModifiedBy(preauthDTO.getStrUserName());
			negAmtDtls.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			negAmtDtls.setNegotiationWith(preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationWith());
			negAmtDtls.setTotalNegotiationSaved(preauthDTO.getPreauthMedicalDecisionDetails().getTotalNegotiationSaved());
			negAmtDtls.setHstCLTrans(preauthDTO.getPreauthMedicalDecisionDetails().getHigestCLTrans());
			negAmtDtls.setClaimedAmt(Double.parseDouble(preauthDTO.getPreauthDataExtractionDetails().getAmtClaimed()));
			entityManager.persist(negAmtDtls);
			entityManager.flush();

		}
		
		/*Treating Doctor Details - CR2019211 */
		List<TreatingDoctorDTO> treatingDoctorLists = preauthDTO.getPreauthDataExtractionDetails().getTreatingDoctorDTOs();
		if(!treatingDoctorLists.isEmpty()) {
			for (TreatingDoctorDTO treatingDoctorDTO: treatingDoctorLists) {		
				TreatingDoctorDetails doctorDetails = prauthMapper.gettreatingDoctor(treatingDoctorDTO);	

				if (doctorDetails.getKey() != null) {
					doctorDetails.setModifiedBy(userNameForDB);
					doctorDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(doctorDetails);
				} else {
					doctorDetails.setClaimKey(preauth.getClaim().getKey());
					doctorDetails.setTransactionKey(preauth.getKey());
					doctorDetails.setActiveStatus(1L);	
					doctorDetails.setCreatedBy(userNameForDB);
					doctorDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(doctorDetails);
					treatingDoctorDTO.setKey(doctorDetails.getKey());
				}
				log.info("------TreatingDoctorDetails------>"+doctorDetails+"<------------");
				entityManager.flush();
				entityManager.clear();		
			}
		}
		
		/*Implant Details - GLX2020057 */
		if(preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable()){
			List<ImplantDetailsDTO> implantDetailsDTOs = preauthDTO.getPreauthDataExtractionDetails().getImplantDetailsDTOs();
			if(implantDetailsDTOs !=null && !implantDetailsDTOs.isEmpty()) {
				for (ImplantDetailsDTO implantDetailsDTO: implantDetailsDTOs) {		
					ImplantDetails implantDetails = prauthMapper.getimplantDetails(implantDetailsDTO);

					if (implantDetails.getKey() != null) {
						implantDetails.setModifiedBy(userNameForDB);
						implantDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(implantDetails);
					} else {
						implantDetails.setClaimKey(preauth.getClaim().getKey());
						implantDetails.setTransactionKey(preauth.getKey());
						implantDetails.setActiveStatus(1L);	
						implantDetails.setCreatedBy(userNameForDB);
						implantDetails.setCreateDate(new Timestamp(System.currentTimeMillis()));
						entityManager.persist(implantDetails);
						implantDetailsDTO.setKey(implantDetails.getKey());
					}
					log.info("------ImplantDetails------>"+implantDetails+"<------------");
					entityManager.flush();
					entityManager.clear();		
				}
			}
		}
		
		/*List<TreatingDoctorDTO> deletedTreatingDoctor = preauthDTO.getDeletedDoctorDetails();
		
		if(deletedTreatingDoctor != null && ! deletedTreatingDoctor.isEmpty()){
			for (TreatingDoctorDTO treatingDoctorDTO : deletedTreatingDoctor) {
				TreatingDoctorDetails doctorDetails = prauthMapper.gettreatingDoctor(treatingDoctorDTO);
				doctorDetails.setActiveStatus(0L);
				doctorDetails.setTransactionKey(preauth.getKey());
				if(doctorDetails.getKey() != null){
					doctorDetails.setModifiedBy(userNameForDB);
					doctorDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(doctorDetails);
					log.info("------DeleteddoctorDetails------>"+doctorDetails+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
		}*/
		
		
	/*	if(null != preauthDTO && null != preauthDTO.getNewIntimationDTO().getKey()){
			
			Intimation intimationObj = getIntimationByKey(preauthDTO.getNewIntimationDTO().getKey());
			if(null != intimationObj){
				
				intimationObj.setPaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
				intimationObj.setPaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());
				intimationObj.setPaPatientName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
				intimationObj.setPaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
				
				if(null != intimationObj.getKey()){
				
					entityManager.merge(intimationObj);
					entityManager.flush();
				}else{
					entityManager.persist(intimationObj);
					entityManager.flush();
				}					
				
			}
			
			
		}*/
		


		Date date2 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%% DB UPDATED SUCCESSFULLY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date2);

		log.info("%%%%%%%%%%%%%%%%%%% TOTAL TIME OF DATA SAVING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+SHAUtils.getDurationFromTwoDate(date1, date2));
		
		
		date1 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%% DOCUMENT UPLOADED STARTING IN DMS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date1);
		
			if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
			{	
				WeakHashMap dataMap = new WeakHashMap();
				dataMap.put("intimationNumber",currentClaim.getIntimation().getIntimationId());
				dataMap.put("claimNumber",currentClaim.getClaimId());
				if(null != currentClaim.getClaimType())
				{
//				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(currentClaim.getClaimType().getKey()))
//					{
					/*Preauth preauthObj = getLatestPreauthByClaimKey( currentClaim.getKey());
						if(null != preauthObj)*/
					dataMap.put("cashlessNumber", preauth.getPreauthId());
//					}
				}
				dataMap.put("filePath", preauthDTO.getDocFilePath());
				dataMap.put("docType", preauthDTO.getDocType());
				if(null != preauthDTO.getDocumentSource() && (SHAConstants.ENHANCEMENT).equalsIgnoreCase(preauthDTO.getDocumentSource()))
				{
					dataMap.put("docSources",  preauthDTO.getDocumentSource());
					/*if(null != currentClaim)
				{
					Preauth preauthObj = getLatestPreauthByClaimKey( currentClaim.getKey());
					if(null != preauthObj)
						dataMap.put("cashlessNumber", preauthObj.getPreauthId());
				}*/
					
				}
				else
				{
					dataMap.put("docSources", SHAConstants.PRE_AUTH);
				}
				dataMap.put("createdBy", preauthDTO.getStrUserName());
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				//String docToken = uploadGeneratedLetterToDMS(dataMap);
				
				if(null != preauthDTO.getStatusKey() && (preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) || 
						(null !=preauthDTO.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() &&  !preauthDTO.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() && preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))))
				{
					if(preauthDTO.getBillAssessmentDocFilePath() != null) {
						dataMap.put("docSources", SHAConstants.ENHANCEMENT);
						dataMap.put("docType", SHAConstants.CASHLESS_ENHANCEMENT_BILL_ASSESSMENT);
						dataMap.put("filePath", preauthDTO.getBillAssessmentDocFilePath());
						//bean.setDocSource(SHAConstants.ENHANCEMENT);
						//String docToken1 = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
						SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
						
						SHAUtils.setClearReferenceData(dataMap);
					}
					
					//String docToken1 = uploadGeneratedLetterToDMS(dataMap);
				}
				
				
				/**
				 * The below code was added to update the document token column
				 * in query table, if an query has been raised at preauth level.
				 * */
				
				if(null != preauth && null != preauth.getStatus() && ((ReferenceTable.PREAUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()) || 
						(ReferenceTable.ENHANCEMENT_QUERY_STATUS).equals(preauth.getStatus().getKey())))
				{
					PreauthQuery preauthQuery = getPreauthQueryList(preauth.getKey());
					if(null != preauthQuery && null != docToken)
					{
						preauthQuery.setDocumentToken(Long.valueOf(docToken));
						entityManager.merge(preauthQuery);
						entityManager.flush();
						entityManager.clear();
						log.info("------PreauthQuery------>"+preauthQuery+"<------------");
					}
				}
				
				
			}
		
		Object doctorNameList = preauthDTO.getPreauthMedicalDecisionDetails().getDoctorName();
		if(null != doctorNameList ){
			List<String> docList = getListFromMultiSelectComponent(doctorNameList);
			if(null != docList && !docList.isEmpty()){
				for (String docName : docList) {
					OpinionValidation opinionValidation = new OpinionValidation();
					opinionValidation.setIntimationNumber(preauthDTO.getNewIntimationDTO().getIntimationId());		
					opinionValidation.setStage(preauth.getStage());
					opinionValidation.setStatus(preauth.getStatus());
					Stage stageByKey = getStageByKey(preauth.getStage().getKey());
					opinionValidation.setClaimStage(null != stageByKey ? stageByKey.getStageName() : null);
					opinionValidation.setAssignedDocName(docName);
					opinionValidation.setAssignedDate(new Timestamp(System.currentTimeMillis()));
					opinionValidation.setCpuCode(Long.valueOf(preauthDTO.getNewIntimationDTO().getCpuCode()));
					opinionValidation.setOpinionStatus(ReferenceTable.OPINION_VALIDATION_PENDING_KEY);
					opinionValidation.setOpinionStatusDate(new Timestamp(System.currentTimeMillis()));
					opinionValidation.setCreatedBy(preauthDTO.getStrUserName());
					opinionValidation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					opinionValidation.setUpdatedRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRemarksFromDeptHead());
					opinionValidation.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
					opinionValidation.setUpdatedBy(preauthDTO.getStrUserName());
					if(preauthDTO.getPreauthMedicalDecisionDetails().getDoctorContainer() != null){
					BeanItemContainer<SpecialSelectValue> doctorContainer = preauthDTO.getPreauthMedicalDecisionDetails().getDoctorContainer();
					List<SpecialSelectValue> doctorsWithRole = doctorContainer.getItemIds();
						for (SpecialSelectValue specialSelectValue : doctorsWithRole) {
							if(docName.equals(specialSelectValue.getValue())){
								opinionValidation.setAssignedRoleBy(specialSelectValue.getSpecialValue());
							}
						}
					}
					
					entityManager.persist(opinionValidation);
				}
			}
		}
		
		
		date2 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%% DOCUMENT UPLOADED ENDED IN DMS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date2);
		
		log.info("%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR UPLOADING DOCUEMNT %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+SHAUtils.getDurationFromTwoDate(date1, date2));

		if(preauthDTO.getStatusKey() != null && !(ReferenceTable.getReferToFLPKeys().containsKey(preauthDTO.getStatusKey()))) {
			submitFVRGradingDetail(preauthDTO);
		}
		
		if((preauthDTO.getStatusKey().intValue() == (ReferenceTable.PREAUTH_REJECT_STATUS).intValue()) || (preauthDTO.getStatusKey().intValue() == (ReferenceTable.ENHANCEMENT_REJECT_STATUS).intValue())){
			updateRejectReason(preauthDTO);
		}
		setDBOutcome(preauthDTO, preauth, isPreauthEnhancement);
				
		return preauth;

		//}
		/*catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}*/
		
		
	}
	
	private void insertToVB64Approval(Preauth preauth, String processType) {
		
		VB64ApprovalRequest vb64ApprovalRequest = new VB64ApprovalRequest();
		vb64ApprovalRequest.setIntimationKey(preauth.getIntimation());
		vb64ApprovalRequest.setCashlessKey(preauth);
		//vb64ApprovalRequest.setReimbursementKey(preauth.getR);
		vb64ApprovalRequest.setRequestedBy(preauth.getModifiedBy());
		vb64ApprovalRequest.setClaimKey(preauth.getClaim());
		vb64ApprovalRequest.setProcessType(processType);
		vb64ApprovalRequest.setRequestorRemarks(preauth.getRemarks());
		vb64ApprovalRequest.setPaymentStatus("Pending");
		vb64ApprovalRequest.setStage(preauth.getStage());
		vb64ApprovalRequest.setStatus(preauth.getStatus());
		vb64ApprovalRequest.setActiveStatus("Y");
		vb64ApprovalRequest.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		vb64ApprovalRequest.setCreatedBy(preauth.getModifiedBy());
		entityManager.persist(vb64ApprovalRequest);
		entityManager.flush();
		entityManager.clear();
	}

	public TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}
	

	
	public Preauth getLatestPreauthByClaimKey(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}
	
	public List<Preauth> getPreauthListByClaimKey(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			
			return preauthList;
		}
		return null;
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
	
	public Hospitals getHospitalObject(Long hospitalKey) {
		TypedQuery<Hospitals> query = entityManager.createNamedQuery("Hospitals.findByKey", Hospitals.class);
		query.setParameter("key", hospitalKey);
		List<Hospitals>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
	}

	public FieldVisitRequest getFVRByKey(Long fvrKey) {
		Query query = entityManager.createNamedQuery("FieldVisitRequest.findByKey");
		query.setParameter("primaryKey", fvrKey);
		FieldVisitRequest singleResult = (FieldVisitRequest) query.getSingleResult();
		
		return singleResult;
	}
	
	@SuppressWarnings("unused")
	public void setBPMOutcome(PreauthDTO preauthDTO, Preauth preauth,
			Boolean isPreauthEnhancement) {/*
		
		Calendar instance = Calendar.getInstance();
		Date date1 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%% BPMN CALL STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date1);

			PreAuthReqType preauthRequest  = preauthDTO.getRodHumanTask().getPayloadCashless().getPreAuthReq();
			
			ClaimRequestType claimRequest = preauthDTO.getRodHumanTask().getPayloadCashless().getClaimRequest();
			ClassificationType classification = preauthDTO.getRodHumanTask().getPayloadCashless().getClassification();
			if(classification == null) {
				classification = new ClassificationType();
				
			}
			
			CustomerType customer = preauthDTO.getRodHumanTask().getPayloadCashless().getCustomer();
			
		try {
			preauthRequest.setKey(preauth.getKey());
			

			String outCome = "";
			if (isPreauthEnhancement) {
				outCome = getOutcomeForPreauthEnhancement(preauth, preauthRequest,
						outCome, preauthDTO);
			} else {
				outCome = getOutcomeForPreauth(preauth, preauthRequest, outCome,
						preauthDTO);
			}
			preauthRequest.setResult(outCome);
			preauthRequest.setOutcome(outCome);
			preauthDTO.getRodHumanTask().setOutcome(outCome);
			
			if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_ESCALATION_STATUS)){
				  preauthRequest.setOutcome(null);	
				
			}
			
			PedReqType pedReq = new PedReqType();
			
			if(preauthDTO.getStrUserName() != null){
				TmpEmployee employeeByName = getEmployeeByName(preauthDTO.getStrUserName());
				if(employeeByName != null){
					String employeeWithNames = employeeByName.getEmployeeWithNames();
					pedReq.setReferredBy(employeeWithNames);
				}else{
					pedReq.setReferredBy(preauthDTO.getStrUserName());
				}
			}
			
			if(outCome.equalsIgnoreCase("SPECIALIST") || outCome.equalsIgnoreCase("SPECIALISTENH")){
				if(customer == null){
					customer = new CustomerType();
				}
				
				if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null && 
						preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue() != null){
					customer.setSpeciality(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
					preauthDTO.getRodHumanTask().getPayloadCashless().setCustomer(customer);
				}
				
				claimRequest.setReferTo(preauthDTO.getStrUserName());
				preauthDTO.getRodHumanTask().getPayloadCashless().setClaimRequest(claimRequest);
			}
			
//			if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null){
//				
//				
//				
//				pedReq.setReferredBy(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
//				
//			}
			
			Stage stage = entityManager.find(Stage.class, preauth.getStage().getKey());
			
			if(preauth.getStage() != null){
				
				
				claimRequest.setOption(stage != null ? stage.getStageName() : "");
				preauthDTO.getRodHumanTask().getPayloadCashless().setClaimRequest(claimRequest);
			}
			
			preauthDTO.getRodHumanTask().getPayloadCashless().setPedReq(pedReq);	
			preauthDTO.getRodHumanTask().getPayloadCashless().setPreAuthReq(preauthRequest);
			
			preauthDTO.getRodHumanTask().getPayloadCashless().getClassification().setSource(stage.getStageName());
			
			ClaimType claimType = preauthDTO.getRodHumanTask().getPayloadCashless().getClaim();
			if(claimType != null){
				Long claimKey = preauth.getClaim().getKey();
				if(claimKey != null){
					Claim claim = getClaimByKey(claimKey);
					if(claimType.getKey() == null && claim != null){
						claimType.setKey(claim.getKey());
					}
					if(claimType.getClaimId() == null && claim != null){
						claimType.setClaimId(claim.getClaimId());
					}
				}
			}
			preauthDTO.getRodHumanTask().getPayloadCashless().setClaim(claimType);
			
			if(preauthDTO.getStatusKey() != null && ReferenceTable.getReferToFLPKeys().containsKey(preauthDTO.getStatusKey())) {
				preauthRequest.setKey(preauthDTO.getClaimDTO().getKey());
				if(classification != null) {
					classification.setSource(isPreauthEnhancement ? "FLE" : "FLP");
					preauthDTO.getRodHumanTask().getPayloadCashless().setClassification(classification);
					
				}
			}
			
			if(isPreauthEnhancement) {
				SubmitPreAuthEnhTask task = BPMClientContext.getPreauthEnhancementTask(preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
				task.execute(preauthDTO.getStrUserName(), preauthDTO.getRodHumanTask());
			} else {
				SubmitPreAuthTask task = BPMClientContext.getPreauthTask(preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
				try{
				task.execute(preauthDTO.getStrUserName(), preauthDTO.getRodHumanTask());
				}catch(Exception e){
					e.printStackTrace();
					log.error(e.toString());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		

		Date date2 = new Date();
		
		log.info("%%%%%%%%%%%%%%%%%%%  BPMN CALL ENDED  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ date2);
		
		log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME FOR BPMN CALL  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date1, date2));
		
	*/}
	
	public void setBPMOutComeForWithdraw(PreauthDTO preauthDTO, Preauth preauth, String outCome){/*
=======
	public void setBPMOutComeForWithdraw(PreauthDTO preauthDTO, Preauth preauth, String outCome, Boolean isPAWithdraw){
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		
		if(preauth.getKey()!=null){
			IntimationType intMsg = new IntimationType();
			ClaimType claimType = new ClaimType();
			HospitalInfoType hospitalType = new HospitalInfoType();
			if(preauth.getStatus()!=null){
				intMsg.setStatus(preauth.getStatus().getProcessValue());
			}
			if(preauth.getClaim()!=null) {
				
				if(preauth.getClaim().getClaimType()!=null){
					claimType.setClaimType(preauth.getClaim().getClaimType().getValue());
				}
				if(preauth.getClaim().getKey() != null){
					claimType.setKey(preauth.getClaim().getKey());
				}
				if(preauth.getClaim().getClaimId() != null){
					claimType.setClaimId(preauth.getClaim().getClaimId());
				}
			}	
			if(preauth.getIntimation()!=null){
				intMsg.setIntimationNumber(preauth.getIntimation().getIntimationId());
			if(preauth.getIntimation().getHospitalType()!=null){
				hospitalType.setHospitalType(preauth.getIntimation().getHospitalType().getValue());
			}
				intMsg.setKey(preauth.getIntimation().getKey());
			}
			
			PreAuthReqType preAuthReq = new PreAuthReqType();
			preAuthReq.setOutcome(outCome);
			preAuthReq.setPatientStatus(outCome);
			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(preauth.getClaim().getIntimation().getPolicy().getPolicyNumber());
			preAuthReq.setKey(preauth.getKey());
			if(outCome.equalsIgnoreCase("SUBMIT")){
				preAuthReq.setKey(preauth.getClaim().getKey());
			}
			
			ClaimRequestType claimRequestType = new ClaimRequestType();
			claimRequestType.setCpuCode(String.valueOf(preauth.getIntimation().getCpuCode().getCpuCode()));
			
			Insured insured = preauth.getIntimation().getInsured();
			Claim claim = preauth.getClaim();
			
			ClassificationType classificationType = new ClassificationType();
			if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
				
				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			}else{
				classificationType.setPriority(SHAConstants.NORMAL);
			}
		
			classificationType.setType(SHAConstants.TYPE_FRESH);
			
			Stage statgeByKey = getStageByKey(ReferenceTable.STANDALONE_WITHDRAW_STAGE); 
			
			classificationType.setSource(statgeByKey.getStageName());
			
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob(SHAConstants.HEALTH_LOB);
			productInfo.setLobType(SHAConstants.HEALTH_LOB_TYPE);
			
			if(isPAWithdraw) {
				productInfo.setLob(SHAConstants.PA_LOB);
				productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			}
			
			PayloadBOType payloadBo = new PayloadBOType();
			payloadBo.setPreAuthReq(preAuthReq);
			payloadBo.setHospitalInfo(hospitalType);
			payloadBo.setClaimRequest(claimRequestType);
			payloadBo.setPolicy(policyType);
			payloadBo.setIntimation(intMsg);
			payloadBo.setClaim(claimType);
			payloadBo.setClassification(classificationType);
			payloadBo.setProductInfo(productInfo);
			
			BPMClientContext.submitWithdrawTask(payloadBo, outCome);
		}
	*/}
	public void setBPMOutComeForDownsize(PreauthDTO preauthDTO, Preauth preauth){/*

		String outCome = getOutComeForDownsize(preauthDTO,preauth);
		if(preauth.getKey()!=null){
			IntimationType intMsg = new IntimationType();
			ClaimType claimType = new ClaimType();
			HospitalInfoType hospitalType = new HospitalInfoType();
			if(preauth.getStatus()!=null){
			intMsg.setStatus(preauth.getStatus().getProcessValue());
			}
			if(preauth.getClaim()!=null){
				if(preauth.getClaim().getClaimType()!=null){
					claimType.setClaimType(preauth.getClaim().getClaimType().getValue());
				}
			}	
			if(preauth.getIntimation()!=null){
				intMsg.setIntimationNumber(preauth.getIntimation().getIntimationId());
			if(preauth.getIntimation().getHospitalType()!=null){
				hospitalType.setHospitalType(preauth.getIntimation().getHospitalType().getValue());
			}
				intMsg.setKey(preauth.getIntimation().getKey());
			}
			
			PreAuthReqType preAuthReq = new PreAuthReqType();
			preAuthReq.setOutcome(outCome);
			preAuthReq.setPreAuthAmt(preauth.getTotalApprovalAmount());
			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(preauth.getIntimation().getPolicy().getPolicyNumber());
			preAuthReq.setKey(preauth.getKey());
			preAuthReq.setApproveDownsize(outCome);
			preAuthReq.setResult(outCome);
			
			CustomerType customer = new CustomerType();
			
			if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null){
				customer.setSpeciality(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
			}
			
			
			PedReqType pedReq = new PedReqType();
			CustomerType customerType = new CustomerType();
			if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null){
				
				pedReq.setReferredBy(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
				customerType.setSpeciality(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
			}
			
			if(outCome != null && outCome.equalsIgnoreCase("SPECIALIST")){
				preAuthReq.setOutcome("DOWNSIZE");
			}else{
				preAuthReq.setOutcome(SHAConstants.ESCALATE_DOWNSIZE);
			}
			
			String userName = preauthDTO.getStrUserName();
			
			Insured insured = preauth.getIntimation().getInsured();
			Claim claim = preauth.getClaim();
			
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob((claim.getLobId() != null  && claim.getLobId().equals(ReferenceTable.HEALTH_LOB_KEY)) ? SHAConstants.HEALTH_LOB : SHAConstants.PA_LOB);
			productInfo.setLobType(claim.getProcessClaimType());
			
			ClassificationType classificationType = new ClassificationType();
			if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
				
				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			}else{
				classificationType.setPriority(SHAConstants.NORMAL);
			}
		
			classificationType.setType(SHAConstants.TYPE_FRESH);
			
			classificationType.setSource(preauth.getStatus().getProcessValue());
			if(outCome != null && outCome.equalsIgnoreCase("SPECIALIST")){
				Stage stage = entityManager.find(Stage.class, preauth.getStage().getKey());
				classificationType.setSource(stage.getStageName());
			}
			

			ClaimRequestType claimRequestType = new ClaimRequestType();
			if(null != preauth && null != preauth.getClaim() && null != preauth.getClaim().getIntimation() && null != preauth.getClaim().getIntimation().getCpuCode())
			{
				claimRequestType.setCpuCode(String.valueOf(preauth.getClaim().getIntimation().getCpuCode().getCpuCode()));
			}
			
			PayloadBOType payloadBo = new PayloadBOType();
			payloadBo.setPedReq(pedReq);
			payloadBo.setPreAuthReq(preAuthReq);
			payloadBo.setHospitalInfo(hospitalType);
			payloadBo.setPolicy(policyType);
			payloadBo.setIntimation(intMsg);
			payloadBo.setClaim(claimType);
			payloadBo.setClassification(classificationType);
			payloadBo.setCustomer(customer);
			payloadBo.setClaimRequest(claimRequestType);
			payloadBo.setCustomer(customerType);
			payloadBo.setProductInfo(productInfo);
			
			
			BPMClientContext.executeforDownSize(payloadBo ,outCome,userName);
		}
	*/}
	
	public String getOutComeForDownsize(PreauthDTO preauthDTO,Preauth preauth){
		
		String outCome = "";
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			outCome = "DOWNSIZE";
		}
		else if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
				if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					outCome = "SPECIALIST";
					createSpecialist(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA1)){
					outCome = "ZSMA";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA2)){
					outCome = "ZMH";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA3)){
					outCome = "CLCMO";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else{
					outCome = "SUBMIT";
				}
			}
		}
		
		return outCome;
		
	}
	
	public void setBPMOutComeForDownsizePreauthRequest(PreauthDTO preauthDTO, Preauth preauth){/*
		
		String outCome = "";
		
		outCome = getOutComeForDownsizePreauthRequest(preauthDTO,preauth);

		PayloadBOType payload = preauthDTO.getRodHumanTask().getPayloadCashless();
		if(payload == null){
			payload = new PayloadBOType();
			preauthDTO.getRodHumanTask().setPayloadCashless(payload);
		}
		try {
			PreAuthReqType preAuthReq = payload.getPreAuthReq();
			
			if(preAuthReq == null){
				preAuthReq = new PreAuthReqType();
			}
			
			preAuthReq.setKey(preauth.getKey());
			preAuthReq.setApproveDownsize(outCome);
			preAuthReq.setResult(outCome);
			
			if(outCome != null && outCome.equalsIgnoreCase("SPECIALIST")){
				preAuthReq.setOutcome("DOWNSIZE");
			}else{
				preAuthReq.setOutcome(SHAConstants.ESCALATE_DOWNSIZE);
			}
			
			ClassificationType classification = payload.getClassification();
			
			Stage stage = entityManager.find(Stage.class, preauth.getStage().getKey());
			classification.setSource(stage.getStageName());
			
			Claim claim = preauth.getClaim();
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob((claim.getLobId() != null  && claim.getLobId().equals(ReferenceTable.HEALTH_LOB_KEY)) ? SHAConstants.HEALTH_LOB : SHAConstants.PA_LOB);
			productInfo.setLobType(claim.getProcessClaimType());
			
			payload.setPreAuthReq(preAuthReq);
			payload.setProductInfo(productInfo);
			preauthDTO.getRodHumanTask().setOutcome(outCome);
			preauthDTO.getRodHumanTask().setPayloadCashless(payload);
			
			SubmitDownsizePreAuthTask downsizePreauthTask = BPMClientContext.getDownsizePreauthTask(preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
			downsizePreauthTask.execute(preauthDTO.getStrUserName(), preauthDTO.getRodHumanTask());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		
	*/}
	
	public String getOutComeForDownsizePreauthRequest(PreauthDTO preauthDTO,Preauth preauth){
		
		String outCome = "";
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			outCome = "DOWNSIZE";
		}
		else if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
				if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					outCome = "SPECIALIST";
					createSpecialist(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA1)){
					outCome = "ZSMA";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA2)){
					outCome = "ZMH";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA3)){
					outCome = "CLCMO";
					createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
				else{
					outCome = "SUBMIT";
				}
			}
		}
		
		return outCome;
		
	}
	
	

	/*private String getOutcomeForPreauth(Preauth preauth,
			PreAuthReqType preauthRequest, String outCome, PreauthDTO bean) {*/
		/*private String getOutcomeForPreauth(Preauth preauth,
				 String outCome, PreauthDTO bean) {
		if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
			outCome = "APPROVE";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
			outCome = "REJECT";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
//			outCome = "DENIAL";
			outCome = "REJECT";
		//	preauthRequest.setKey(preauth.getClaim().getKey());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS)) {
			outCome = "TRANSLATE";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_QUERY_STATUS)) {
			outCome = "QUERY";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS)) {
			outCome = "FLP";
		}  else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
			if (bean != null
					&& bean.getPreauthMedicalDecisionDetails() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
							.getId() != null) {
				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
						.getId().equals(ReferenceTable.CMA1)) {
					outCome = "CMA1";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA2)) {
					outCome = "CMA2";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA3)) {
					outCome = "CMA3";
				} 
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA4)) {
					outCome = "CMA4";
				} 
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA5)) {
					outCome = "CMA5";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA6)) {
					outCome = "CMA6";
				}else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue()
						==(ReferenceTable.SPECIALIST_DOCTOR)) {
					outCome = "SPECIALIST";
				}
			}
		}
		return outCome;
	}*/

/*	private String getOutcomeForPreauthEnhancement(Preauth preauth,
			PreAuthReqType preauthRequest, String outCome, PreauthDTO bean) {*/
		/*private String getOutcomeForPreauthEnhancement(Preauth preauth,
				 String outCome, PreauthDTO bean) {
		if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
			outCome = "APPROVEENH";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
			outCome = "REJECTENH";
		} else if (ReferenceTable.WITHDRAW_KEYS.containsKey(preauth.getStatus().getKey())) {
			outCome = "WITHDRAWENH";
			if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null 
					&& bean.getPreauthMedicalDecisionDetails().getWithdrawReason().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)){
				outCome = "NOTADMITTED";
			}
			//preauthRequest.setKey(preauth.getClaim().getKey());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
			outCome = "DOWNSIZEENH";
		//	preauthRequest.setKey(preauth.getClaim().getKey());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS)) {
			outCome = "TRANSLATEENH";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS)) {
			outCome = "FLE";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)) {
			outCome = "QUERYENH";
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)) {
			if (bean != null
					&& bean.getPreauthMedicalDecisionDetails() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
							.getId() != null) {
//				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo().getId()
//						.equals(ReferenceTable.CMA1)) {
//					outCome = "CMA1ENH";
//				} else if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//						.getId().equals(ReferenceTable.CMA2)) {
//					outCome = "ZMHENH";
//				} else if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//						.getId()
//						.equals(ReferenceTable.CMA3)) {
//					outCome = "CLCMOENH";
//				}else if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//						.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)) {
//					outCome = "SPECIALISTENH";
//				}
				
				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
						.getId().equals(ReferenceTable.CMA1)) {
					outCome = "CMA1ENH";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA2)) {
					outCome = "CMA2ENH";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA3)) {
					outCome = "CMA3ENH";
				} 
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA4)) {
					outCome = "CMA4ENH";
				} 
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA5)) {
					outCome = "CMA5ENH";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().equals(ReferenceTable.CMA6)) {
					outCome = "CMA6ENH";
				}else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue()
						==(ReferenceTable.SPECIALIST_DOCTOR)) {
					outCome = "SPECIALISTENH";
				}
			
				
			}
		}
		return outCome;
	}*/

	@SuppressWarnings("unchecked")
	public Investigation checkInitiateInvestigation(Long preauthKey) {
		Query findAll = entityManager
				.createNamedQuery("Investigation.findByClaimKey");
		findAll.setParameter("claimKey", preauthKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		//Boolean isAvailable = false;
		if (!investigationList.isEmpty() && investigationList.size() > 0) {
			return investigationList.get(0);
		}
		return null;
	}
	
	public Boolean checkInvestigationPending(Long clmKey) {
		
		Boolean isInvesInitiated = Boolean.FALSE;
		Query findAll = entityManager
				.createNamedQuery("Investigation.findByClaimKey");
		findAll.setParameter("claimKey", clmKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		if(investigationList != null && !investigationList.isEmpty()){
			
			for (Investigation investigation : investigationList) {
			
				if((ReferenceTable.INITIATE_INVESTIGATION_APPROVED.equals(investigation.getStatus().getKey())) ||
					(ReferenceTable.ASSIGN_INVESTIGATION.equals(investigation.getStatus().getKey())) ||
					(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey())) ||
					(ReferenceTable.INVESTIGATION_REPLY_RECEIVED.equals(investigation.getStatus().getKey())) 
					){
					   
					   isInvesInitiated = Boolean.TRUE;	
					   break;
				}
			}
		}
		return isInvesInitiated;
	}

	@SuppressWarnings("unchecked")
	public ResidualAmount getResidualAmtByPreauthKey(Long preauthKey) {
		Query findAll = entityManager
				.createNamedQuery("ResidualAmount.findByPreauthKey");
		findAll.setParameter("preauthKey", preauthKey);
		List<ResidualAmount> residualAmtList = (List<ResidualAmount>) findAll
				.getResultList();
		//Boolean isAvailable = false;
		if (!residualAmtList.isEmpty() && residualAmtList.size() > 0) {
			entityManager.refresh(residualAmtList.get(0));
			return residualAmtList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ResidualAmount getLatestResidualAmtByPreauthKey(Long preauthKey) {
		Query findAll = entityManager
				.createNamedQuery("ResidualAmount.findByPreauthKey");
		findAll.setParameter("preauthKey", preauthKey);
		List<ResidualAmount> residualAmtList = (List<ResidualAmount>) findAll
				.getResultList();
		
		List<Long> keys = new ArrayList<Long>();
		
       for (ResidualAmount residualAmount : residualAmtList) {
			keys.add(residualAmount.getKey());
		}
       Long maxKey = 0l;
       
       if(! keys.isEmpty()){
    	   maxKey = Collections.max(keys);
       }
       
       Query find = entityManager
				.createNamedQuery("ResidualAmount.findByKey");
       find.setParameter("primarykey", maxKey);
		List<ResidualAmount> residualAmt = (List<ResidualAmount>) find
				.getResultList();
		
		if(residualAmt != null && !residualAmt.isEmpty()){
			return residualAmt.get(0);
		}
		
		return null;
	}
	
//	public void refereshObj(Preauth preauth) {
//		entityManager.refresh(preauth);
//	}
//	
	public String get(Long preAuthKey)
	{
		float lAmt= 0f;
		Query findAll = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		findAll.setParameter("preauthKey", preAuthKey);
		List<ClaimAmountDetails> claimAmtDetails = (List<ClaimAmountDetails>)findAll.getResultList();
		if(null !=claimAmtDetails )
		{
			//for(int i = 0 ; i<claimAmtDetails.size() ; i++)
			for(ClaimAmountDetails objClaim :claimAmtDetails )
			{
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

	public String getPreauthReqAmt(Long preAuthKey, Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		Preauth currentPreauth = new Preauth();
		for (Preauth preauth : resultList) {
			
			entityManager.refresh(preauth);
			if(preauth.getKey().equals(preAuthKey)) {
				currentPreauth = preauth;
				break;
			}
		}
		String calculatePreRequestedAmt = calculatePreRequestedAmt(preAuthKey, claimKey);
		if(currentPreauth != null) {
			String[] split = currentPreauth.getPreauthId().split("/");
			String string = split[split.length - 1];
			if(SHAUtils.getIntegerFromString(string) != 1) {
				for (Preauth preauthDO : resultList) {
					String[] splitedStr = preauthDO.getPreauthId().split("/");
					String number = splitedStr[splitedStr.length - 1];
					Integer previousNumber = SHAUtils.getIntegerFromString(string) - 1;
					if((previousNumber).equals(SHAUtils.getIntegerFromString(number))) {
						String calculatePreRequestedAmt2 = calculatePreRequestedAmt(preauthDO.getKey(), claimKey);
						Double value =  SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt) - SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt2);
						calculatePreRequestedAmt = String.valueOf(value.intValue());
						break;
					}
				}
			}
		}
		return calculatePreRequestedAmt;
	}
	
	public String getPreauthReqAmtFromTmpPreauth(Long preAuthKey, Long claimKey) {
		Query query = entityManager.createNamedQuery("ViewTmpPreauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<ViewTmpPreauth> resultList = (List<ViewTmpPreauth>) query.getResultList();
		ViewTmpPreauth currentPreauth = new ViewTmpPreauth();
		for (ViewTmpPreauth preauth : resultList) {
			
			entityManager.refresh(preauth);
			if(preauth.getKey().equals(preAuthKey)) {
				currentPreauth = preauth;
			}
		}
		String calculatePreRequestedAmt = calculatePreRequestedAmt(preAuthKey, claimKey);
		if(currentPreauth != null) {
			String[] split = currentPreauth.getPreauthId().split("/");
			String string = split[split.length - 1];
			if(SHAUtils.getIntegerFromString(string) != 1) {
				for (ViewTmpPreauth preauthDO : resultList) {
					String[] splitedStr = preauthDO.getPreauthId().split("/");
					String number = splitedStr[splitedStr.length - 1];
					Integer previousNumber = SHAUtils.getIntegerFromString(string) - 1;
					if((previousNumber).equals(SHAUtils.getIntegerFromString(number))) {
						String calculatePreRequestedAmt2 = calculatePreRequestedAmt(preauthDO.getKey(), claimKey);
						Double value =  SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt) - SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt2);
						calculatePreRequestedAmt = String.valueOf(value.intValue());
						break;
					}
				}
			}
		}
		return calculatePreRequestedAmt;
	}
	
	
	
	
	public String calculatePreRequestedAmt(Long preAuthKey, Long claimKey)
	{
		String requestedAmt = "0";
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
			//return String.valueOf(lAmt);
			requestedAmt = String.valueOf(lAmt);
		}
		/*else
		{
			return "0";
		}*/
		return requestedAmt;
	}
	
	/**
	 * This method is used by the search screen service file. 
	 * For example, for preAuthSearch screens, this method is required
	 * for preAuthRequestedAmt Field displayed in the search result table.
	 * In this scneario , if @Inject is used in the search related service file,
	 * then it leads to persistence context error realted with entity manager.
	 * Hence this below overridden method is used , where entityManager will be passed
	 * as a arguement from the search service file.
	 * 
	 * */
	public String calculatePreRequestedAmt(EntityManager entityManager , Long preAuthKey)
	{
		float lAmt= 0f;
		Query findAll = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		findAll.setParameter("preauthKey", preAuthKey);
		List<ClaimAmountDetails> claimAmtDetails = (List<ClaimAmountDetails>)findAll.getResultList();
		if(null !=claimAmtDetails )
		{
			//for(int i = 0 ; i<claimAmtDetails.size() ; i++)
			for(ClaimAmountDetails objClaim :claimAmtDetails )
			{
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
	
	/**
	 * Method to retreive preauth from claim key
	 * 
	 * */
	
	public Preauth getPreauthFromClaimKey(Long claimKey)
	{
		Preauth preauth = null;
		List<Preauth> preauthList = getPreauthByClaimKey(claimKey);
		if (null != preauthList && !preauthList.isEmpty())
		{
			int size = preauthList.size();
			preauth = preauthList.get(size-1);
		}
		
		return preauth;
		
	}
	
	
	public List<PreviousClaimsTableDTO> getPreviousClaims(
			List<ViewTmpClaim> claimList, String strClaimNumber) {
		List<ViewTmpClaim> objClaimList = new ArrayList<ViewTmpClaim>();
		List<PreviousClaimsTableDTO> previousClaimList;
		
		for (ViewTmpClaim objClaim : claimList) {
			if (!(strClaimNumber.equalsIgnoreCase(objClaim.getClaimId()))) {
				objClaimList.add(objClaim);
			}
		}
		previousClaimList = PreviousClaimMapper.getInstance()
				.getPreviousClaimDTOList(objClaimList);
		
		for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
			
			if(previousClaimsTableDTO.getCustomerID() != null){
				//String pedDetails = "";
				StringBuffer pedDetails = new StringBuffer();
				List<InsuredPedDetails> pedByInsured = getPEDByInsured(Long.valueOf(previousClaimsTableDTO.getCustomerID()));
				for (InsuredPedDetails insuredPedDetails : pedByInsured) {
					if(insuredPedDetails.getPedDescription() != null){
						//pedDetails += insuredPedDetails.getPedDescription() +", ";
						pedDetails.append(insuredPedDetails.getPedDescription()).append(", ");
					}
				}
				//previousClaimsTableDTO.setPedName(pedDetails);
				previousClaimsTableDTO.setPedName(pedDetails.toString());
			}
			
			
			Preauth previousPreauth = getPreviousPreauth(previousClaimsTableDTO.getKey());
			if(previousPreauth != null){
//				previousClaimsTableDTO.setClaimAmount(getPreauthReqAmt(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
				previousClaimsTableDTO.setClaimAmount(calculatePreRequestedAmt(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
			}
			
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
				
				if(previousClaimsTableDTO.getDiagnosisForPreviousClaim() != null){
					previousClaimsTableDTO.setDiagnosis(previousClaimsTableDTO.getDiagnosisForPreviousClaim());
				}
			}
			
			List<ViewTmpReimbursement> reimbursement = getTmpReimbursment(previousClaimsTableDTO.getKey());
			
			if(reimbursement != null){
				List<Double> copayValues = new ArrayList<Double>();
				Boolean isPA = false;
				String benefitcover = "";
				String coverValue = "";
				Double paApprovedAmt = 0d;
				Double paClaimedAmt = 0d;
				StringBuffer icdCodes = new StringBuffer();
				
				
				List<Reimbursement> reimbObjList = getReimbursementByClaimKey(previousClaimsTableDTO.getKey());
				if(reimbObjList != null && !reimbObjList.isEmpty()){
					
					for (Reimbursement reimbObj : reimbObjList) {
						paApprovedAmt = paApprovedAmt + getBenefitAddOnOptionalApprovedAmt(reimbObj);
					}
				}	
				
			for (ViewTmpReimbursement viewTmpReimbursement : reimbursement) {
					
					Reimbursement reimbursementObjectByKey = getReimbursementObjectByKey(viewTmpReimbursement.getKey());
					if(reimbursementObjectByKey != null && reimbursementObjectByKey.getAmtConsCopayPercentage() != null){
						copayValues.add(reimbursementObjectByKey.getAmtConsCopayPercentage().doubleValue());
					}
					
//				    copayValues.add(reimbursementCalCulationDetails2.getCopayAmount().longValue());

					List<ViewTmpDiagnosis> pedValidationForTmp = getPedValidationForTmp(viewTmpReimbursement.getKey());
					if(pedValidationForTmp != null){
						for (ViewTmpDiagnosis viewTmpDiagnosis : pedValidationForTmp) {
							if(viewTmpDiagnosis.getCopayPercentage() != null){
								copayValues.add(viewTmpDiagnosis.getCopayPercentage().doubleValue());
							}
							
							Long icdCodeId = viewTmpDiagnosis.getIcdCodeId();
							if(icdCodeId != null){
								IcdCode icdCode = getIcdCode(icdCodeId);
								if(icdCode != null && icdCode.getDescription() != null){
									icdCodes.append(icdCode.getDescription()).append(", ");
								}
							}
						}
					}
					
					if(viewTmpReimbursement.getBenefitsId() != null)
					{
						MastersValue masterValue = viewTmpReimbursement.getBenefitsId();
						
						if(masterValue.getValue() != null)
						{
							benefitcover += masterValue.getValue() + ", ";
								
						}
					}
					
					previousClaimsTableDTO.setIcdCodes(icdCodes.toString());
					
					List<ViewTmpProcedure> tmpProcedureByTransactionKey = getTmpProcedureByTransactionKey(viewTmpReimbursement.getKey());
					if(tmpProcedureByTransactionKey != null){
						for (ViewTmpProcedure viewTmpProcedure : tmpProcedureByTransactionKey) {
							if(viewTmpProcedure.getCopayPercentage() != null){
								copayValues.add(viewTmpProcedure.getCopayPercentage().doubleValue());
							}
						}
					}
				
					coverValue = getCoverValueForViewBasedOnrodKey(viewTmpReimbursement.getKey());
					
				if(reimbursementObjectByKey != null){
						Claim claim = reimbursementObjectByKey.getClaim();
						
					if(claim != null && claim.getLobId()!= null && (ReferenceTable.PA_LOB_KEY).equals(claim.getLobId())){
						isPA = true;
						
						/*List<Reimbursement> reimbObjList = getReimbursementByClaimKey(claim.getKey());
						if(reimbObjList != null && !reimbObjList.isEmpty()){
							
							for (Reimbursement reimbObj : reimbObjList) {
								paApprovedAmt = paApprovedAmt + getBenefitAddOnOptionalApprovedAmt(reimbObj);
							}
						}	*/					
//						paApprovedAmt = paApprovedAmt + getBenefitAddOnOptionalApprovedAmt(reimbursementObjectByKey);	
						if(null != claim && null != claim.getIntimation() && null != claim.getIntimation().getPolicy() && 
								null != claim.getIntimation().getPolicy().getProduct() && null != claim.getIntimation().getPolicy().getProduct() &&
								(ReferenceTable.getGPAProducts().containsKey(claim.getIntimation().getPolicy().getProduct().getKey()))){
							previousClaimsTableDTO.setInsuredPatientName(claim.getIntimation().getPaPatientName());
							previousClaimsTableDTO.setParentName(claim.getGpaParentName());
							previousClaimsTableDTO.setParentDOB(claim.getGpaParentDOB());
							previousClaimsTableDTO.setPatientName(claim.getGpaRiskName());
							previousClaimsTableDTO.setPatientDOB(claim.getGpaRiskDOB());
							previousClaimsTableDTO.setCategory(claim.getGpaCategory());
						}						
						
						DocAcknowledgement docAcknowledgement = reimbursementObjectByKey.getDocAcknowLedgement();
						
						if(docAcknowledgement != null && docAcknowledgement.getProcessClaimType() != null){
							if(docAcknowledgement.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE) && docAcknowledgement.getBenifitClaimedAmount() != null){
								paClaimedAmt = paClaimedAmt + docAcknowledgement.getBenifitClaimedAmount();
							}else if(docAcknowledgement.getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE)){
								paClaimedAmt = paClaimedAmt + getClaimedAmountValueForView(docAcknowledgement);
							}
						}
					}
				}
					
			}
				
				if(isPA){
					previousClaimsTableDTO.setApprovedAmount(String.valueOf(paApprovedAmt));
					previousClaimsTableDTO.setClaimAmount(String.valueOf(paClaimedAmt));						
				}
				
				if(benefitcover.isEmpty())
				{
					previousClaimsTableDTO.setBenefits(coverValue);
				}else
				{
					if(coverValue.isEmpty()){
						previousClaimsTableDTO.setBenefits(benefitcover);
					}else{
						previousClaimsTableDTO.setBenefits(benefitcover+", "+coverValue);	
					}
				}
				
				if(! copayValues.isEmpty()){
					Double maximumCopay = Collections.max(copayValues);		
					previousClaimsTableDTO.setCopayPercentage(maximumCopay);
				}

			}else{
				if(previousPreauth != null ){
					List<Double> copayValues = new ArrayList<Double>();
					StringBuffer icdCodes = new StringBuffer();
					
					if(previousPreauth.getCoPay() != null){
						copayValues.add(previousPreauth.getCoPay().doubleValue());
					}
					
					List<ViewTmpDiagnosis> pedValidationForTmp = getPedValidationForTmp(previousPreauth.getKey());
					if(pedValidationForTmp != null){
						for (ViewTmpDiagnosis viewTmpDiagnosis : pedValidationForTmp) {
							if(viewTmpDiagnosis.getCopayPercentage() != null){
								copayValues.add(viewTmpDiagnosis.getCopayPercentage().doubleValue());
							}
							
							Long icdCodeId = viewTmpDiagnosis.getIcdCodeId();
							if(icdCodeId != null){
								IcdCode icdCode = getIcdCode(icdCodeId);
								if(icdCode != null && icdCode.getDescription() != null){
									icdCodes.append(icdCode.getDescription()).append(", ");
								}
							}
						}
					}
					
					previousClaimsTableDTO.setIcdCodes(icdCodes.toString());
					
					List<ViewTmpProcedure> tmpProcedureByTransactionKey = getTmpProcedureByTransactionKey(previousPreauth.getKey());
					if(tmpProcedureByTransactionKey != null){
						for (ViewTmpProcedure viewTmpProcedure : tmpProcedureByTransactionKey) {
							if(viewTmpProcedure.getCopayPercentage() != null){
								copayValues.add(viewTmpProcedure.getCopayPercentage().doubleValue());
							}
						}
					}
					
					if(! copayValues.isEmpty()){
						Double maximumCopay = Collections.max(copayValues);
						previousClaimsTableDTO.setCopayPercentage(maximumCopay);
					}
				}
			}

		}
		try{
			if (objClaimList != null && previousClaimList != null && !objClaimList.isEmpty() && !previousClaimList.isEmpty()) {
				for (int i = 0; i < objClaimList.size(); i++) {
					if(previousClaimList.get(i).getAdmissionDate() != null){
                         previousClaimList.get(i).setAdmissionDate(SHAUtils.formatDate(objClaimList.get(i).getIntimation().getAdmissionDate()));
					}
					ViewTmpIntimation intimation = objClaimList.get(i).getIntimation();
					if(intimation != null && intimation.getHospital() != null){
						Hospitals hospitals = getHospitalByKey(intimation.getHospital());
						if(hospitals != null){
							previousClaimList.get(i).setHospitalName(hospitals.getName());
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		
		
		
		if(!previousClaimList.isEmpty()) {
			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
				String diagnosisStr = " ";
				List<PedValidation> pedValidationsList = getDiagnosis(previousClaimsTableDTO.getIntimationKey());
			    List<Long> keyList = new ArrayList<Long>();
				if (!pedValidationsList.isEmpty()) {
					for (PedValidation pedValidation : pedValidationsList) {
						
//						map.put(pedValidation.getDiagnosisId(), masterService.getDiagnosis(pedValidation
//										.getDiagnosisId()));
						if(! keyList.contains(pedValidation.getDiagnosisId())){
						diagnosisStr = (diagnosisStr == " " ? ""
								: diagnosisStr + ", ")
								+ (pedValidation.getDiagnosisId() != null ? getDiagnosisByKey(pedValidation
										.getDiagnosisId()) : "");
						keyList.add(pedValidation.getDiagnosisId());
						}
						
					}
					
					previousClaimsTableDTO.setDiagnosis(diagnosisStr);
					
				}
				
			}
			
		}
		
		return previousClaimList;
	}

	@SuppressWarnings("unchecked")
	public List<InsuredPedDetails> getPEDByInsured(Long insuredKey){
		 Query query = entityManager.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		 query = query.setParameter("insuredKey", insuredKey);		        
		 List<InsuredPedDetails> insuredList  = query.getResultList();			     
		return insuredList;

	}
	
	public Reimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", key);
		
		List<Reimbursement> reimbursementList = query.getResultList();
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
	
	public List<ViewTmpReimbursement> getTmpReimbursment(Long claimKey) {
		
		List<ViewTmpReimbursement> reimbursmentList = new ArrayList<ViewTmpReimbursement>();
		
		List<ViewTmpReimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (ViewTmpReimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
//						&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					reimbursmentList.add(reimbursement);	
				}
			}
		}

		return reimbursmentList;
	}
	
	
	public List<ViewTmpReimbursement> getPreviousRODByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<ViewTmpReimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (ViewTmpReimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
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
	
	public  List<PreviousClaimsTableDTO> getPreviousClaimByForRegistration(NewIntimationDto newIntimationDto) {
//		List<ViewTmpIntimation> intimationlist = getPolicyWiseClaimList(intimation);
//		List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);
		
		//Claim currentClaim = null;
		
		/*List<Claim> claimByIntimation = claimService.getClaimByIntimation(newIntimationDto.getKey());
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			currentClaim = claimByIntimation.get(0);
			
		}*/
		Intimation intimation = claimService.getIntimationByKey(newIntimationDto.getKey());
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(0L, intimation.getPolicy().getKey(), 
				intimation.getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		/*ViewTmpIntimation intimation = getTmpIntimationByKey(newIntimationDto.getKey());
		
		List<ViewTmpClaim> claimsByPolicyNumber = getViewTmpClaimsByPolicyNumber(intimation.getPolicyNumber());
		
		List<ViewTmpClaim> claimByIntimation = getTmpClaimByIntimation(intimation.getKey());
		List<ViewTmpClaim> filterClaim = new ArrayList<ViewTmpClaim>();
		
		for (ViewTmpClaim claim : claimsByPolicyNumber) {
			
			filterClaim.add(claim);
			
			if(claimByIntimation != null && !claimByIntimation.isEmpty()){
				if(claim.getKey().equals(claimByIntimation.get(0).getKey())){
					filterClaim.remove(claim);
				}
			}
			
		}
		
		List<PreviousClaimsTableDTO> claimList = getPreviousClaims(filterClaim, intimation.getIntimationId());*/
        return previousClaimDTOList;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public ViewTmpIntimation getTmpIntimationByKey(Long intimationKey) {

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
	
	public List<ViewTmpDiagnosis> getPedValidationForTmp(Long key){
		
		Query query = entityManager.createNamedQuery("ViewTmpDiagnosis.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
        List<ViewTmpDiagnosis> resultList = (List<ViewTmpDiagnosis>)query.getResultList();
        
        if(resultList != null && ! resultList.isEmpty()){
        	return resultList;
        }
        
        return null;
	}
	
	   public List<PedValidation> getDiagnosisByTransactionKey(Long transactionKey) {	
	 		

	  		List<PedValidation> resultList = new ArrayList<PedValidation>();
	  		
	  		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
	  		query.setParameter("transactionKey", transactionKey);
	  		
	  		resultList = (List<PedValidation>)query.getResultList();
	  	    
	  		return resultList;

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
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpClaim> getTmpClaimByIntimation(Long intimationKey) {
		List<ViewTmpClaim> a_claimList = new ArrayList<ViewTmpClaim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("ViewTmpClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<ViewTmpClaim>) findByIntimationKey.getResultList();
				
				for (ViewTmpClaim claim : a_claimList) {
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
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpClaim> getViewTmpClaimsByPolicyNumber(String policyNumber) {
		
		List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
		if (policyNumber != null) {

			Query findByPolicyNumber = entityManager.createNamedQuery(
					"ViewTmpClaim.findByPolicyNumber").setParameter("policyNumber",
					policyNumber);

			try {
				resultList = findByPolicyNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			for (ViewTmpClaim claim : resultList) {
				entityManager.refresh(claim);
			}
		}
		return resultList;
	}
	
	
	public List<PreviousClaimsTableDTO> getPreviousClaimForOP(
			List<OPClaim> claimList) {
//		List<Claim> objClaimList = new ArrayList<Claim>();
		List<PreviousClaimsTableDTO> previousClaimList;
		
		previousClaimList = OPClaimMapper.getInstance()
				.getPreviousClaimDTOListOP(claimList);
		
		for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
			/*Preauth previousPreauth = getPreviousPreauth(previousClaimsTableDTO.getKey());
			if(previousPreauth != null){
				previousClaimsTableDTO.setClaimAmount(getPreauthReqAmt(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
			}*/
			
			/*Long claimedAmountForROD = getClaimedAmountForROD(previousClaimsTableDTO.getKey());
			if(claimedAmountForROD != null && claimedAmountForROD > 0){
				previousClaimsTableDTO.setClaimAmount(String.valueOf(claimedAmountForROD));
			}*/
			
		}
		try{
			if (claimList != null && previousClaimList != null && !claimList.isEmpty() && !previousClaimList.isEmpty()) {
				for (int i = 0; i < claimList.size(); i++) {
//					if(previousClaimList.get(i).getAdmissionDate() != null){
                         previousClaimList.get(i).setAdmissionDate(SHAUtils.formatDate(claimList.get(i).getDataOfAdmission()));
                         previousClaimList.get(i).setIntimationNumber(claimList.get(i).getIntimation().getIntimationId());
                         previousClaimList.get(i).setIntimationKey(claimList.get(i).getIntimation().getKey());
                         previousClaimList.get(i).setPolicyNumber(claimList.get(i).getIntimation().getPolicy().getPolicyNumber());
                         previousClaimList.get(i).setPolicyYear(claimList.get(i).getIntimation().getPolicy().getPolicyYear().toString());
                         previousClaimList.get(i).setClaimNumber(claimList.get(i).getClaimId());
                         previousClaimList.get(i).setInsuredPatientName(claimList.get(i).getIntimation().getInsured().getInsuredName());
                         previousClaimList.get(i).setClaimStatus(claimList.get(i).getStatus().getProcessValue());
                         previousClaimList.get(i).setClaimAmount(String.valueOf(claimList.get(i).getClaimedAmount()));
                         Double approvedAmt = 0d;
                         if(claimList.get(i).getKey() != null){
                        	 OPHealthCheckup opHealthCheckUp = getOpHealthByClaimKey(claimList.get(i).getKey());
                        	 approvedAmt = opHealthCheckUp.getAmountPayable() != null ? opHealthCheckUp.getAmountPayable() : 0d;
                         }
                         previousClaimList.get(i).setApprovedAmount(String.valueOf(approvedAmt));
//					}
				}
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		
		
		
		if(!previousClaimList.isEmpty()) {
			/*for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
				String diagnosisStr = " ";
				List<PedValidation> pedValidationsList = getDiagnosis(previousClaimsTableDTO.getIntimationKey());
			    List<Long> keyList = new ArrayList<Long>();
				if (!pedValidationsList.isEmpty()) {
					for (PedValidation pedValidation : pedValidationsList) {
						
//						map.put(pedValidation.getDiagnosisId(), masterService.getDiagnosis(pedValidation
//										.getDiagnosisId()));
						if(! keyList.contains(pedValidation.getDiagnosisId())){
						diagnosisStr = (diagnosisStr == " " ? ""
								: diagnosisStr + ", ")
								+ getDiagnosisByKey(pedValidation
										.getDiagnosisId());
						keyList.add(pedValidation.getDiagnosisId());
						}
						
					}
					
				}
				previousClaimsTableDTO.setDiagnosis(diagnosisStr);
			}*/
			
		}
		
		return previousClaimList;
	}
	
	public Preauth getPreviousPreauth(Long claimKey) {
		List<Preauth> preauthByClaimKey =getPreauthByClaimKey(claimKey);
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);
			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			//Integer nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId()
							.split("/");
					String number = splitNumber[splitNumber.length - 1];
					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						previousPreauth = preauth;
						//nextReferenceNo = Integer.valueOf(number);
					}
				}
			}
		}
		return previousPreauth;
	}
	
	public ViewTmpPreauth getPreviousPreauthFromTmpPreauth(Long claimKey) {
		List<ViewTmpPreauth> preauthByClaimKey =getTmpPreauthByClaimKey(claimKey);
		ViewTmpPreauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);
			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			//Integer nextReferenceNo = Integer.valueOf(defaultNumber);
			for (ViewTmpPreauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId()
							.split("/");
					String number = splitNumber[splitNumber.length - 1];
					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						previousPreauth = preauth;
						//nextReferenceNo = Integer.valueOf(number);
					}
				}
			}
		}
		return previousPreauth;
	}
	
	public Long getClaimedAmountForROD(Long claimKey){
		
		Double totalClaimedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<Reimbursement> rodList = query.getResultList();
		
		for (Reimbursement reimbursement : rodList) {
			
			Double claimedAmount = 0d;
			if(reimbursement.getDocAcknowLedgement() != null){
				DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
				
				if(docAcknowledgement.getHospitalizationClaimedAmount() != null){
					claimedAmount += docAcknowledgement.getHospitalizationClaimedAmount();
				}
				if(docAcknowledgement.getPreHospitalizationClaimedAmount() != null){
					claimedAmount += docAcknowledgement.getPreHospitalizationClaimedAmount();
				}
				if(docAcknowledgement.getPostHospitalizationClaimedAmount() != null){
					claimedAmount += docAcknowledgement.getPostHospitalizationClaimedAmount();
				}
			}
			totalClaimedAmount += claimedAmount;
		}
		
		Long amount =totalClaimedAmount.longValue();
		
		return amount;
	}
	
	public Double getReimbursementApprovedAmount(Long claimKey){
		
		Double approvedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<Reimbursement> rodList = query.getResultList();
		
		for (Reimbursement reimbursement : rodList) {
			if(reimbursement.getFinancialApprovedAmount() != null){
				approvedAmount += reimbursement.getFinancialApprovedAmount();
			}
		}
		
		return approvedAmount;
		
	}
	
	public Long getClaimedAmountFromTmpReimbursement(Long claimKey){
		
		Double totalClaimedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<ViewTmpReimbursement> rodList = query.getResultList();
		
		for (ViewTmpReimbursement reimbursement : rodList) {
			
			Double claimedAmt = 0d;
			
			if(reimbursement.getDocAcknowLedgement() != null){
				DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
				
				if(docAcknowledgement.getHospitalizationClaimedAmount() != null){
					claimedAmt += docAcknowledgement.getHospitalizationClaimedAmount();
				}
				if(docAcknowledgement.getPreHospitalizationClaimedAmount() != null){
					claimedAmt += docAcknowledgement.getPreHospitalizationClaimedAmount();
				}
				if(docAcknowledgement.getPostHospitalizationClaimedAmount() != null){
					claimedAmt += docAcknowledgement.getPostHospitalizationClaimedAmount();
				}	
			}
			totalClaimedAmount += claimedAmt;
		}
		
		Long amount =totalClaimedAmount.longValue();
		
		return amount;
	}
	
	public Long getClaimedAmountForRODByClaimKey(Long claimKey,EntityManager entityMgr){
		
	this.entityManager = entityMgr;
	return getClaimedAmountForROD(claimKey);		
		
	}
	
	
	
	/**
	 * Added for fetching date of discharge for latest
	 * pre auth id. 
	 * */
	
	/*public Preauth getLatestPreauthByClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}
			if(ReferenceTable.getWithdrawnKeys().containsKey(preauth.getStatus().getKey())) {
				return null;
			}
			
			return preauth;
		}
		
		return null;
	
	}*/
	
	public Preauth getLatestPreauthByClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaimAndRejectedKey");
		query.setParameter("claimkey", claimKey);
		query.setParameter("preauthKey", ReferenceTable.getRejectedPreauthKeysList());
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			/*for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}*/
			if(ReferenceTable.getWithdrawnKeys().containsKey(preauth.getStatus().getKey())) {
				return null;
			}
			
			return preauth;
		}
		
		return null;
	
	}
	
	
	
	
	public Preauth getLatestPreauthByClaimForWithdrawReject(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}
			if(ReferenceTable.getWithdrawnKeysExceptWithdrawReject().containsKey(preauth.getStatus().getKey())) {
				return null;
			}
			
			
			return preauth;
		}
		
		return null;
	
	}
	
	public Preauth getLatestPreauthDetails(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}
			
			return preauth;
		}
		
		return null;
	
	}
	
	/**
	 * Do not modify below method. This is specific to 
	 * acknowledgement screen.
	 * 
	 * **/
	
	public Preauth getLatestPreauthByClaimForAck(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		Preauth preauth = null;
		if(null != singleResult && !singleResult.isEmpty())
		{
			preauth = singleResult.get(0);
			entityManager.refresh(preauth);
			return preauth;
		}
		return null;
	}
	
	public List<Preauth> getPreauthListByDescendingOrder(Long claimKey){
		
		
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		
		for (Preauth preauth : resultList) {
			entityManager.refresh(preauth);
		}
		
		return resultList;
	}
	
  
	
	public List<Preauth> getPreauthListByDescendingOrder(Long claimKey,EntityManager em){
		this.entityManager = em;
		
		List<Preauth> resultList = getPreauthListByDescendingOrder(claimKey);
		
		return resultList;
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
	
	public TmpEmployee getEmployeeByName(String userName){
		
		TmpEmployee tmpEmployee = null;
		
		userName = userName.toLowerCase();
		
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpName");
		query.setParameter("empName", userName);
		
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
			tmpEmployee = tmpEmployee2;
		}
		
		return tmpEmployee;
		
		
	}
	
	public String getDiagnosisByKey(Long key) {
		Diagnosis diagnosis = entityManager.find(Diagnosis.class, key);
		return diagnosis != null ? diagnosis.getValue() : "";
	}


	public CPUWisePreauthResultDto getCpuWisePreauth(Map<String,Object> queryFilter,UsertoCPUMappingService userCPUMapService,DBCalculationService dbclaService){
		CPUWisePreauthResultDto resultDto = null;
		
//		List<CPUwisePreauthReportDto> resultListDto = new ArrayList<CPUwisePreauthReportDto>();		
//		Query findClaimByCPUQuery = entityManager.createNamedQuery("Claim.findByCPUKey");
//		findClaimByCPUQuery = findClaimByCPUQuery.setParameter("cpuKey", cpuKey);		
//		List<Claim> claimList = findClaimByCPUQuery.getResultList();
		
		Date fromDate = null;
		Date toDate = null;
		Long cpuKey = null;
		String userId = "";
		try{
		
/*		final CriteriaBuilder builder = entityManager
				.getCriteriaBuilder();
		
		final CriteriaQuery<Preauth> criteriaPreauthQuery = builder
				.createQuery(Preauth.class);

		Root<Preauth> preauthRoot = criteriaPreauthQuery
				.from(Preauth.class);		
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		criteriaPreauthQuery.select(preauthRoot);
		
		if(queryFilter.containsKey("cpuKey") && queryFilter.get("cpuKey") != null){
			cpuKey = (Long)queryFilter.get("cpuKey");
			Predicate cpuPredicate = builder.equal(preauthRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuKey);	
		predicates.add(cpuPredicate);
		}
		else{
			userId = (String)queryFilter.get(BPMClientContext.USERID);
			List<Long> cpuKeyList = (List<Long>)userCPUMapService.getCPUCodeList(userId, entityManager);
			Predicate cpuKeyListPredicate = preauthRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);	
			predicates.add(cpuKeyListPredicate);
		}
		
		if(queryFilter.containsKey("fromDate") && queryFilter.get("fromDate") != null && queryFilter.containsKey("toDate") && queryFilter.get("toDate") != null){
			
			fromDate = (Date)queryFilter.get("fromDate");
			
			toDate = (Date)queryFilter.get("toDate");
			Predicate fromDatePredicate = builder.greaterThanOrEqualTo(preauthRoot.<Date>get("createdDate"), fromDate);
			predicates.add(fromDatePredicate);
			
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();
			
			Predicate toDatePredicate = builder.lessThanOrEqualTo(preauthRoot.<Date>get("createdDate"), toDate);
			predicates.add(toDatePredicate);
		}
		
		
		criteriaPreauthQuery.where(builder.and(predicates
				.toArray(new Predicate[] {})));

		final TypedQuery<Preauth> prauthQuery = entityManager
				.createQuery(criteriaPreauthQuery);
		
		SHAUtils.popinReportLog(entityManager, userId, "PreauthCPUWiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
		List<Preauth> preauthResultList = (List<Preauth>) prauthQuery
				.getResultList();
		
		if(preauthResultList != null && !preauthResultList.isEmpty()){
			PreauthDTO preauthDto;
			ClaimDto clmDto;
			NewIntimationDto intimationDto;
			Long claimedAmt;
			CPUwisePreauthReportDto cpuResultDto;
			for(Preauth preauth : preauthResultList){
				entityManager.refresh(preauth);
				preauthDto = PreauthMapper.getInstance().getPreauthDTO(preauth);
				preauthDto.getPreauthMedicalDecisionDetails().setApprovalRemarks(preauth.getRemarks());
				claimedAmt = getClaimAmountByPreauth(preauth.getKey(), entityManager);
					preauthDto.setAmountConsidered(claimedAmt != null ? claimedAmt.toString() : "");
						
				clmDto =  ClaimMapper.getInstance().getClaimDto(preauth.getClaim());
				
				intimationDto = (new IntimationService()).getIntimationDto(preauth.getIntimation(),entityManager);
				
				preauthDto.setNewIntimationDTO(intimationDto);
				clmDto.setNewIntimationDto(intimationDto);
				preauthDto.setClaimDTO(clmDto);
				
				String diagnosis = getDiagnosisForPreauthByKey(preauth.getKey());
					preauthDto.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);								
				
				cpuResultDto = new CPUwisePreauthReportDto(preauthDto); 
				BeanItemContainer<SelectValue> emlSelectValue = (new MasterService()).getEmployeeLoginContainer(cpuResultDto.getDoctor(),entityManager);
				if (emlSelectValue.getItemIds() != null && !emlSelectValue.getItemIds().isEmpty()) {
					cpuResultDto.setDoctor(emlSelectValue.getItemIds().get(0).getValue());
				}
				cpuResultDto.setSno(preauthResultList.indexOf(preauth)+1);
				cpuResultDto.setDiagnosis(preauthDto.getPreauthDataExtractionDetails().getDiagnosis());
				resultListDto.add(cpuResultDto);
				cpuResultDto = null;
			}
			
		}
		
		resultDto = new CPUWisePreauthResultDto();
		resultDto.setCpuwisePreauthProcessedList(resultListDto);
		
		
		Query preauthSummaryQ = entityManager.createNamedQuery("Preauth.findCashlessEstimation");
		preauthSummaryQ.setParameter("fromDate", fromDate);
		preauthSummaryQ.setParameter("endDate", toDate);
		preauthSummaryQ.setParameter("cpuKey", cpuKey);
	
		Long estimationCount = (Long)preauthSummaryQ.getSingleResult(); 
		
		resultDto.setEstimation(estimationCount != null ? estimationCount : 0l);
		
		Query preauthApprovedQ = entityManager.createNamedQuery("Preauth.findPreauthSummaryByStageNStaus");
		preauthApprovedQ.setParameter("fromDate", fromDate);
		preauthApprovedQ.setParameter("endDate", toDate);
		preauthApprovedQ.setParameter("cpuKey", cpuKey);
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PREAUTH_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PREAUTH_APPROVE_STATUS);
		
		Long preauthApproveCount = (Long)preauthApprovedQ.getSingleResult();
		
		resultDto.setPreauthApproved(preauthApproveCount != null ?preauthApproveCount : 0l);
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
		
		Long premedicalQCount = (Long)preauthApprovedQ.getSingleResult();
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PREAUTH_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PREAUTH_QUERY_STATUS);
		Long preauthQCount = (Long)preauthApprovedQ.getSingleResult();
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
		//Long preMedEnhanceQCount = (Long)preauthApprovedQ.getSingleResult();
		
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.ENHANCEMENT_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.ENHANCEMENT_QUERY_STATUS);
		Long enhanceQCount = (Long)preauthApprovedQ.getSingleResult();
		
		Long totalQueryCount = (premedicalQCount != null  ? premedicalQCount : 0l) + (preauthQCount != null ? preauthQCount : 0l) + (enhanceQCount != null ? enhanceQCount : 0l);	
		resultDto.setQueryRaised(totalQueryCount);
		
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PREAUTH_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
		Long denialCount = (Long)preauthApprovedQ.getSingleResult();
		resultDto.setDocGiven(denialCount);
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.PREAUTH_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.PREAUTH_REJECT_STATUS);
		Long rejectCount = (Long)preauthApprovedQ.getSingleResult();
		resultDto.setRejected(rejectCount);
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.ENHANCEMENT_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.WITHDRAW_APPROVED_STATUS);
		//Long enhancewithDrawCount = (Long)preauthApprovedQ.getSingleResult();
		
		
		preauthApprovedQ.setParameter("preauthStageKey",ReferenceTable.WITHDRAW_STAGE);
		preauthApprovedQ.setParameter("approvedStatusKey",ReferenceTable.STANDALONE_WITHDRAW_STATUS);
		//Long preauthWithDrawCount = (Long)preauthApprovedQ.getSingleResult();
		
		//Long totalWithDraw = (enhancewithDrawCount != null ? enhancewithDrawCount : 0l) + (preauthWithDrawCount != null ? preauthWithDrawCount : 0l);
		
				
		Query enhancementSummaryQ = entityManager.createNamedQuery("Preauth.findEnhancementApprovedSummary");
		enhancementSummaryQ.setParameter("enhanceStageKey", ReferenceTable.ENHANCEMENT_STAGE);
		enhancementSummaryQ.setParameter("approvedStatusKey", ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
		enhancementSummaryQ.setParameter("fromDate", fromDate);
		enhancementSummaryQ.setParameter("endDate", toDate);
		enhancementSummaryQ.setParameter("cpuKey", cpuKey);
		Long enhanceApproveCount = (Long)enhancementSummaryQ.getSingleResult();
		resultDto.setEnhancementApproved(enhanceApproveCount != null ? enhanceApproveCount :0l);*/
			
		if(queryFilter.containsKey("fromDate") && queryFilter.get("fromDate") != null && queryFilter.containsKey("toDate") && queryFilter.get("toDate") != null){
				
			fromDate = (Date)queryFilter.get("fromDate");
			toDate = (Date)queryFilter.get("toDate");
		}
		else{
			fromDate = null;
			toDate = null;
		}
		
		String cpuStr[] = null;
		
		if(queryFilter.containsKey("cpuCode") && queryFilter.get("cpuCode") != null){
			SelectValue cpuCodeSelect = (SelectValue)queryFilter.get("cpuCode");
			cpuKey = cpuCodeSelect.getId();
			cpuStr =  cpuCodeSelect != null ? cpuCodeSelect.getValue().split(" - ") : null;		
		}	
			
		 
		String strCPUcode = cpuStr != null && cpuStr.length > 0 ? cpuStr[0] : null;
		userId = (String)queryFilter.get(BPMClientContext.USERID);
		SHAUtils.popinReportLog(entityManager, userId, "PreauthCPUWiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
		resultDto = dbclaService.getPreauthCpuwiseReport(new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()), (strCPUcode != null ? Long.valueOf(strCPUcode) : null ), userId);
		SHAUtils.popinReportLog(entityManager, userId, "Preauth CPU wise Report",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
		
		}
		catch(Exception e){
			SHAUtils.popinReportLog(entityManager, userId, "Preauth CPU wise Report",new Date(),new Date(),SHAConstants.RPT_ERROR);
			e.printStackTrace();
			log.error(e.toString());
		}
		
		
		return resultDto;
	}
	
	public String getDiagnosisForPreauthByKey(Long preauthKey){
		String diagnosis="";
		try{
		List<PedValidation> findPedValidationByPreauthKey = findPedValidationByPreauthKey(preauthKey);
		List<DiagnosisDetailsTableDTO> diagDtoList = (PreMedicalMapper.getInstance()).getNewPedValidationTableListDto(findPedValidationByPreauthKey);
		
		if(diagDtoList != null && !diagDtoList.isEmpty()){
			
			for(DiagnosisDetailsTableDTO  diagDto : diagDtoList){
				Long diagKey =diagDto.getDiagnosisId();
				if(diagKey != null){
					Diagnosis diagObj = entityManager.find(Diagnosis.class, diagKey);	
					if(diagObj != null){
						diagnosis = ("").equals(diagnosis) ? diagObj.getValue() : diagnosis + " , " + diagObj.getValue(); 
					}
				}
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return diagnosis;
	}
	
	public Preauth getLatestPreauthByIntimationKey(Long intimationKey){
		Query findByLatestIntimationKey = entityManager
				.createNamedQuery("Preauth.findByLatestIntimationKey");
    	findByLatestIntimationKey.setParameter("intimationKey",intimationKey);
    	
    	List<Preauth> result = (List<Preauth>) findByLatestIntimationKey.getResultList();
    	if(result != null && ! result.isEmpty()){
    		
    		entityManager.refresh(result.get(0));
    		return result.get(0);
    	}
    	
    	return null;
	}
	
	public List<ViewDoctorRemarksDTO> getDoctorRemarksDetails(Long claimKey){
		List<ViewDoctorRemarksDTO> resultList = new ArrayList<ViewDoctorRemarksDTO>();
		List<Preauth> preauthByClaimKey = getPreauthByClaimKey(claimKey);
		if(preauthByClaimKey != null){
			for (Preauth preauth : preauthByClaimKey) {
				if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)
						||  preauth.getStage().getKey().equals(ReferenceTable.WITHDRAW_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.DOWNSIZE_STAGE)){
					ViewDoctorRemarksDTO dto = new ViewDoctorRemarksDTO();
					if(preauth.getModifiedDate() != null){
						dto.setStrNoteDate(SHAUtils.formatDate(preauth.getModifiedDate()));
					}else{
						dto.setStrNoteDate(SHAUtils.formatDate(preauth.getCreatedDate()));
					}
					dto.setUserId(preauth.getModifiedBy());
					dto.setTransactionType(preauth.getStage().getStageName());
					dto.setRemarks(preauth.getDoctorNote());
					
					List<PreauthQuery> preauthQueryList = QueryList(preauth.getKey());
					
					if(! (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS))){
				
						if(preauthQueryList != null){
							for (PreauthQuery preauthQuery : preauthQueryList) {
								ViewDoctorRemarksDTO dto1 = new ViewDoctorRemarksDTO();
								if(preauthQuery.getCreatedBy() != null){
									dto1.setUserId(preauthQuery.getCreatedBy());	
								}
								dto1.setTransaction(preauth.getPreauthId());
								dto1.setRemarks(preauthQuery.getDoctorNote());
								if(preauth.getModifiedDate() != null){
									dto1.setStrNoteDate(SHAUtils.formatDate(preauth.getModifiedDate()));
								}
								resultList.add(dto1);
							}
							
						}
					}
					
					dto.setTransaction(preauth.getPreauthId());
					resultList.add(dto);
				}
			}
		}
		
		List<Reimbursement> reimbursementList = getReimbursementByClaimKey(claimKey);
		for (Reimbursement reimbursement : reimbursementList) {
			ViewDoctorRemarksDTO reimbursementDto = new ViewDoctorRemarksDTO();
			if(reimbursement.getModifiedDate() != null){
				reimbursementDto.setStrNoteDate(SHAUtils.formatDate(reimbursement.getModifiedDate()));
			}
			reimbursementDto.setUserId(reimbursement.getModifiedBy());
			
			StageInformation stageInformationDetailsByStatus = getStageInformationDetailsByStatus(reimbursement.getKey());
			if(stageInformationDetailsByStatus != null){
				reimbursementDto.setUserId(stageInformationDetailsByStatus.getCreatedBy());
			}
			reimbursementDto.setTransactionType("ROD Process");
			reimbursementDto.setRemarks(reimbursement.getDoctorNote());
			reimbursementDto.setTransaction(reimbursement.getRodNumber());
			resultList.add(reimbursementDto);
		}
		
		return resultList;
		
	}
	
	public StageInformation getStageInformationDetailsByStatus(Long reimbursementKey){
		
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
		statusList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
		
		Query query = entityManager.createNamedQuery("StageInformation.findByStatusListAndReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementKey);
		query.setParameter("statusList", statusList);
		
		List<StageInformation> resultList = (List<StageInformation>) query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		
		return null;

	}
	
	

	
	public List<ViewDoctorRemarksDTO> getDoctorRemarksDetailsForPreviousClaims(Long claimKey){
		List<ViewDoctorRemarksDTO> resultList = new ArrayList<ViewDoctorRemarksDTO>();
		List<ViewTmpPreauth> preauthByClaimKey = getTmpPreauthByClaimKey(claimKey);
		if(preauthByClaimKey != null){
			for (ViewTmpPreauth preauth : preauthByClaimKey) {
				if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)
						||  preauth.getStage().getKey().equals(ReferenceTable.WITHDRAW_STAGE) || preauth.getStage().getKey().equals(ReferenceTable.DOWNSIZE_STAGE)){
					ViewDoctorRemarksDTO dto = new ViewDoctorRemarksDTO();
					if(preauth.getModifiedDate() != null){
						dto.setStrNoteDate(SHAUtils.formatDate(preauth.getModifiedDate()));
					}else{
						dto.setStrNoteDate(SHAUtils.formatDate(preauth.getCreatedDate()));
					}
					dto.setUserId(preauth.getModifiedBy());
					dto.setTransactionType(preauth.getStage().getStageName());
					dto.setRemarks(preauth.getDoctorNote());
					dto.setTransaction(preauth.getPreauthId());
					resultList.add(dto);
				}
			}
		}
		
		List<ViewTmpReimbursement> reimbursementList = getViewTmpRimbursementDetails(claimKey);
		for (ViewTmpReimbursement reimbursement : reimbursementList) {
			ViewDoctorRemarksDTO reimbursementDto = new ViewDoctorRemarksDTO();
			if(reimbursement.getModifiedDate() != null){
				reimbursementDto.setStrNoteDate(SHAUtils.formatDate(reimbursement.getModifiedDate()));
			}
			reimbursementDto.setUserId(reimbursement.getModifiedBy());
			reimbursementDto.setTransactionType("ROD Process");
			reimbursementDto.setRemarks(reimbursement.getDoctorNote());
			reimbursementDto.setTransaction(reimbursement.getRodNumber());
			resultList.add(reimbursementDto);
		}
		
		return resultList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpReimbursement> getViewTmpRimbursementDetails(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();
		
		for (ViewTmpReimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
		}

		return reimbursementList;

	}
	
	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		
		List<Reimbursement> rodList = null;
		try{
			Query query = entityManager.createNamedQuery(
					"Reimbursement.findByClaimKey").setParameter("claimKey",
					claimKey);
			rodList = query.getResultList();
			
			if(rodList != null && !rodList.isEmpty()){
				for(Reimbursement rodObj : rodList){	
					entityManager.refresh(rodObj);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return rodList;
	}
	
	public String getDiagnosisByPreauthKey(Long preauthKey,EntityManager entMgr){
		this.entityManager = entMgr;
		String diagnosis = getDiagnosisForPreauthByKey(preauthKey);
		return diagnosis;
	}
	
	public void callBPMRemainderTask(PreauthDTO preauthDTO, Preauth preauth, String userName, String password) {/*
		CLReminder initiateRemainderTask = BPMClientContext
				.getCashlessReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payload = new PayloadBOType();
		PreAuthReqType preauthRequest  = new PreAuthReqType();
		
		PolicyType policyBO = new PolicyType();
		policyBO.setPolicyId(preauthDTO.getPolicyDto().getPolicyNumber());
		payload.setPolicy(policyBO);
		
		IntimationType intimationBo = new IntimationType();
		intimationBo.setIntimationNumber(preauthDTO.getNewIntimationDTO().getIntimationId());
		payload.setIntimation(intimationBo);
		
		ProductInfoType productBO = new ProductInfoType();
		
		if(ReferenceTable.HEALTH_LOB_KEY.equals(preauth.getClaim().getIntimation().getPolicy().getLobId())){
			productBO.setLob(SHAConstants.HEALTH_LOB);
		}
		else{
			productBO.setLob(SHAConstants.PA_LOB);
		}
		
		payload.setProductInfo(productBO);
		
		ClaimRequestType claimRequest = new ClaimRequestType();
		
	try {
		preauthRequest.setKey(preauth.getKey());

//		String outCome = "SUBMIT";
//		
//		preauthRequest.setResult(outCome);
//		preauthRequest.setOutcome(outCome);
		
		ClaimType calimBo = new ClaimType();
		
		calimBo.setClaimId(preauth.getClaim().getClaimId());
		calimBo.setKey(preauth.getClaim().getKey());
		calimBo.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
		payload.setClaim(calimBo);
		
		
		PedReqType pedReq = new PedReqType();
		
		if(userName != null){
			TmpEmployee employeeByName = getEmployeeByName(userName);
			if(employeeByName != null){
				String employeeWithNames = employeeByName.getEmployeeWithNames();
				pedReq.setReferredBy(employeeWithNames);
			}else{
				pedReq.setReferredBy(userName);
			}
		}
		
		Stage stage = entityManager.find(Stage.class, preauth.getStage().getKey());
		
		if(null != preauth && null != preauth.getClaim() && null != preauth.getClaim().getIntimation() && null != preauth.getClaim().getIntimation().getCpuCode())
		{
			claimRequest.setCpuCode(String.valueOf(preauth.getClaim().getIntimation().getCpuCode().getCpuCode()));
			claimRequest.setClientType(SHAConstants.CASHLESS_CLAIM_TYPE);
			payload.setClaimRequest(claimRequest);
		}
		
		if(preauth.getStage() != null){
			claimRequest.setOption(stage != null ? stage.getStageName() : "");
			payload.setClaimRequest(claimRequest);
		}
		claimRequest.setOption(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
		payload.setClaimRequest(claimRequest);
		payload.setPedReq(pedReq);	
		payload.setPreAuthReq(preauthRequest);
		
		QueryType queryBo = new QueryType();
		payload.setQuery(queryBo);
		
		ClassificationType classificationType = new ClassificationType();
		classificationType.setSource(stage.getStageName());
		payload.setClassification(classificationType);
		
		initiateRemainderTask.initiate(BPMClientContext.BPMN_TASK_USER, payload);
		
		
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.toString());
	}
	*/}
	
		public void callReminderTaskForDB(Object[] inputArray){
		
//		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
//		
//		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		//remainder category - shaconstants - preauth bills not received (cashless_key), bills not received(claim_key), query(reimbursement_key)
		
		
		
		
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
//		dbCalculationService.initiateTaskProcedure(parameter);
		DBCalculationService dbCalculationService = new DBCalculationService();
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
	}
	
	public Boolean isPreauthApprovedStatus(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && (preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_HOSPTIAL_STATUS)))){
				return true;
			}
		}
		return false;
		
	}
	
	public Boolean isEnhancementApprovedOrDownsize(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))){
				return true;
			}
		}
		return false;
	}


	public  Procedure getProcedureDetails(Long preAuthkey) {
		Procedure procedure = null;
					
			Query findByPreauthKey = entityManager.createNamedQuery(
					"Procedure.findByPreauthKey").setParameter("preauthKey", preAuthkey);
			try{
				List<Procedure> procedureList = (List<Procedure>) findByPreauthKey.getResultList();
				if(null != procedureList && !procedureList.isEmpty())
				{
					procedure = procedureList.get(0);
				}
				return procedure;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

    }


	public  TmpEmployee getLoginDetails(String loginId) {
		TmpEmployee employeeDetails = null;
					
		if(loginId != null){
			Query findByPreauthKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", loginId.toLowerCase());
			try{
				List<TmpEmployee> empList = (List<TmpEmployee>) findByPreauthKey.getResultList();
				if(null != empList && !empList.isEmpty())
				{
					employeeDetails = empList.get(0);
				}
				return employeeDetails;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
	}
	return employeeDetails;
	}

	@SuppressWarnings("unchecked")
	public List<ReimbursementCalCulationDetails> getReimbursementCalculationDetails(Long reimbursementKey){
		
		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
		query = query.setParameter("reimbursementKey", reimbursementKey);
		
		List<ReimbursementCalCulationDetails> reimbursmentCalculationDetails = (List<ReimbursementCalCulationDetails>) query.getResultList();
		
		return reimbursmentCalculationDetails;
		
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		Claim resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);
			List<Claim> claimResultList = findByIntimationNum.getResultList();
			try {
				if(null != claimResultList && !claimResultList.isEmpty())
				{
					resultClaim = claimResultList.get(0);
					entityManager.refresh(resultClaim);
					return resultClaim;
					//resultClaim = (Claim) findByIntimationNum.getSingleResult();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultClaim;
	}
	
	public void updateCloseClaim(Claim claim, Date closedDate){
		
		claim.setCloseDate(closedDate);
		entityManager.merge(claim);
		entityManager.flush();
		entityManager.clear();
	}
	
	public void updateStageInformation(Claim claim,Date closeDate){
		
		Query rodObjectQuery = entityManager
				.createNamedQuery("StageInformation.findClaimByStatusKey");
		rodObjectQuery.setParameter("claimkey",
				claim.getKey());

		rodObjectQuery.setParameter("statusKey",
				ReferenceTable.PREAUTH_CLOSED_STATUS);
		
		List<StageInformation> resultList = (List<StageInformation>) rodObjectQuery.getResultList();
		if(!resultList.isEmpty()){
			StageInformation stageInformation = resultList.get(0);
			stageInformation.setCreatedDate(closeDate);
			entityManager.merge(stageInformation);
			entityManager.flush();
			entityManager.clear();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public List<PortablitiyPolicyDTO> getPortablityDetails(String policyNumber){
		
		Query findByKey = entityManager.createNamedQuery(
				"PortablityPolicy.findByPolicyNumber").setParameter(
				"policyNumber", policyNumber);
		
		List<PortablityPolicy> resultList = (List<PortablityPolicy>) findByKey.getResultList();
		
		List<PortablitiyPolicyDTO> portablityDetails = EarlierRodMapper.getInstance().getPortablityDetails(resultList);
		
		return portablityDetails;
	}
	
	/**
	 * Below code was Added as part of CR R1080
	 */

	public List<PortablitiyPolicyDTO> getPortablityDetails(String policyNumber, String insuredName){
		List<PortablitiyPolicyDTO> portablityDetails = new ArrayList<PortablitiyPolicyDTO>();
		if(policyNumber != null && insuredName != null){
			Query findByKey = entityManager.createNamedQuery(
					"PortablityPolicy.findByPolicyNumberNInsuredName").setParameter(
							"insuredName", insuredName != null ? insuredName.toLowerCase() : "");
			findByKey.setParameter("policyNumber", policyNumber);
			
			List<PortablityPolicy> resultList = (List<PortablityPolicy>) findByKey.getResultList();
			
			portablityDetails = EarlierRodMapper.getInstance().getPortablityDetails(resultList);
		}
		else if(policyNumber != null){
			portablityDetails = getPortablityDetails(policyNumber);
		}		
		
		return portablityDetails;
	}
	
	//for continuity Benefit
	public List<ContinuityBenefitDTO> getContinuityBenefitDetails(Long policyKey, Long insuredKey){
		List<ContinuityBenefitDTO> portablityDetails = new ArrayList<ContinuityBenefitDTO>();
		if(policyKey != null && insuredKey != null){
			Query findByKey = entityManager.createNamedQuery(
					"GmcContinuityBenefit.findByPolicyKeyAndInsuredKey").setParameter(
							"insuredKey", insuredKey);
			findByKey.setParameter("policyKey", policyKey);
			
			List<GmcContinuityBenefit> resultList = (List<GmcContinuityBenefit>) findByKey.getResultList();
			
			portablityDetails = EarlierRodMapper.getInstance().getContinuityBenefitDetails(resultList);
		}
		/*else if(policyKey != null){
			portablityDetails = getPortablityDetails(policyKey);
		}*/		
		
		return portablityDetails;
	}
	
	
	/**
	 * Below code was Added as part of CR R1080
	 */
	public List<PortablitiyPolicyDTO> getPortablityPolicyDetails(String policyNumber,String insuredName){
		
DBCalculationService db = new DBCalculationService();
		
		List<PortablitiyPolicyDTO> resultset = db.getPreviousPortabilityPolicy(policyNumber, insuredName);
		
		return resultset;
		
	/*	Query findByKey = entityManager.createNamedQuery(
				"PortabilityPreviousPolicy.findByCurPolicyNumberNInsuredName").setParameter(
				"currentPolicyNumber", policyNumber);
		findByKey.setParameter("insuredName", insuredName != null ? insuredName.toLowerCase(): null);
		List<PortabilityPreviousPolicy> resultList = (List<PortabilityPreviousPolicy>) findByKey.getResultList();
		
		List<PortablitiyPolicyDTO> portablityDetails = EarlierRodMapper.getInstance().getPortablityPrevPolicyDetails(resultList);
		
		return portablityDetails;*/
	}
//	public  Procedure getProcedureDetails(Long preAuthkey) {
//		Procedure procedure = null;
//					
//			Query findByPreauthKey = entityManager.createNamedQuery(
//					"Procedure.findByPreauthKey").setParameter("preauthKey", preAuthkey);
//			try{
//				List<Procedure> procedureList = (List<Procedure>) findByPreauthKey.getResultList();
//				if(null != procedureList && !procedureList.isEmpty())
//				{
//					procedure = procedureList.get(0);
//				}
//				return procedure;
//				
//			}catch(Exception e)
//			{
//				e.printStackTrace();
//				return null;
//			}
//
//}


	
	
	public  List<HRMTableDTO> getEarlierHrmDetails(String intimationNo) {
		//HrmDetails employeeDetails = null;
		List<HRMTableDTO> hrmtableDtoList = new ArrayList<HRMTableDTO>();
			Query findByIntimationNo = entityManager.createNamedQuery(
					"HrmDetails.findByIntimationNo").setParameter("intimationNO", intimationNo);
			try{
				List<HrmDetails> hrmDetails = (List<HrmDetails>) findByIntimationNo.getResultList();
			
				
				if(null != hrmDetails && !hrmDetails.isEmpty())
				{
					
					for (HrmDetails hrmDetails1 : hrmDetails) {
						HRMTableDTO hrmtableDto = new HRMTableDTO();
						hrmtableDto.setAnhOrNanh(hrmDetails1.getPchdAnhNanh());
						hrmtableDto.setHrmId(hrmDetails1.getPchdHrmId());
						hrmtableDto.setSurgicalProcedure(hrmDetails1.getPchdSurgicalProcedure());
						hrmtableDto.setClaimedAmt(hrmDetails1.getPchdClaimAmount());
						hrmtableDto.setPackageAmt(null != hrmDetails1.getPchdPackageAmount() ? hrmDetails1.getPchdPackageAmount().longValue() : null);
						hrmtableDto.setDocRemarks(hrmDetails1.getPchdRepresentativeRemarks());
						hrmtableDto.setAssigneeDateAndTime(hrmDetails1.getPchdAssigneeDate());
						hrmtableDto.setDocUserId(hrmDetails1.getDoctorId());
						hrmtableDto.setDocName(hrmDetails1.getDoctorName());
						hrmtableDto.setRequestTypeValue(hrmDetails1.getRequestType());
						hrmtableDto.setHrmReplyRemarks(hrmDetails1.getPchdRequestRemarks());
						hrmtableDto.setReplyDateAndTime(hrmDetails1.getPchdRequestDate());
			
						
						
						hrmtableDtoList.add(hrmtableDto);
					}
					
				}
				
				
				return hrmtableDtoList;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

}
	
	@SuppressWarnings("null")
	public HrmDetails submitHrmRequestDetails(List<HRMTableDTO> hrmDto)
	{
	
		if(null != hrmDto && !hrmDto.isEmpty()){
			
			
			for (HRMTableDTO hrmTableDTO : hrmDto) {				
				
				HrmDetails hrmDetails = new HrmDetails();
				if(null != hrmTableDTO.getAnhOrNanh() && hrmTableDTO.getAnhOrNanh().equalsIgnoreCase(ReferenceTable.AGREED_NETWORK_HOSPITAL_TYPE)){
					hrmDetails.setPchdAnhNanh("ANH");
				}
				else
				{
					hrmDetails.setPchdAnhNanh("NANH");
				}
				
				hrmDetails.setPchdSurgicalProcedure(hrmTableDTO.getSurgicalProcedure());
				hrmDetails.setPchdClaimAmount(hrmTableDTO.getClaimedAmt());
				hrmDetails.setPchdPackageAmount(null != hrmTableDTO.getPackageAmt() ? hrmTableDTO.getPackageAmt().doubleValue() : null);
				hrmDetails.setPchdClaimIntimationNo(hrmTableDTO.getIntimationNO());
				hrmDetails.setPchdReadFlag(0);
				hrmDetails.setPchdRepresentativeRemarks(hrmTableDTO.getDocRemarks());
				hrmDetails.setPchdAssigneeDate(hrmTableDTO.getAssigneeDateAndTime());
				//hrmDetails.setPchdAssigneeId(hrmTableDTO.getDocUserId());
				hrmDetails.setPchdHrmId(hrmTableDTO.getHrmId());
				hrmDetails.setPchdClaimIntimationNo(hrmTableDTO.getIntimationNO());
				hrmDetails.setPchdProviderCode(hrmTableDTO.getHospitalCode());
				hrmDetails.setPchdAssigneeDate(hrmTableDTO.getAssigneeDateAndTime());
				hrmDetails.setGalTransactionId(hrmTableDTO.getCashlessKey());
				if(null != hrmTableDTO.getRequestType() && null != hrmTableDTO.getRequestType().getValue())
				{
				hrmDetails.setRequestType(hrmTableDTO.getRequestType().getValue());
				}
				hrmDetails.setDoctorId(hrmTableDTO.getDocUserId());
				hrmDetails.setDoctorName(hrmTableDTO.getDocName());
								
			
				entityManager.persist(hrmDetails);
				entityManager.flush();
				entityManager.clear();
				
				/*if(null != hrmTableDTO.getCashlessKey())
				{
					entityManager.merge(hrmDetails);
					entityManager.flush();
				}
				else
				{
				entityManager.persist(hrmDetails);
				entityManager.flush();
				}
				*/
				

			}
			
						
		}
		
	return null;
		}
	
	public String validate(String intimationNo,List<HRMTableDTO> hrmDto)
	{
		//String err = "";
		
		//HrmDetails employeeDetails = null;
		//List<HRMTableDTO> hrmtableDtoList = new ArrayList<HRMTableDTO>();
			Query findByIntimationNo = entityManager.createNamedQuery(
					"HrmDetails.findByIntimationNo").setParameter("intimationNO", intimationNo);
			try{
				List<HrmDetails> hrmDetails = (List<HrmDetails>) findByIntimationNo.getResultList();
			
				String requestType = null;
				String docRemarks = null;
				if(null != hrmDto && !hrmDto.isEmpty()){
					
					
					for (HRMTableDTO hrmTableDTO : hrmDto) {	
						
						if(null != hrmTableDTO.getRequestType() && null != hrmTableDTO.getRequestType().getValue())
						{
						requestType = hrmTableDTO.getRequestType().getValue();
						docRemarks = hrmTableDTO.getDocRemarks();
						}
					}
					
					if(null == requestType )
					{
						return "Please Select Request Type";
					}
					if(null == docRemarks)
					{
						return "Please Select Doctor Remarks";
					}
				}
				
				if(null != hrmDetails && !hrmDetails.isEmpty())
				{
					for (HrmDetails hrmDetails1 : hrmDetails) {
						
					if(null == hrmDetails1.getPchdRequestRemarks() && null == hrmDetails1.getPchdRequestId() 
							&& ((hrmDetails1.getRequestType().equals(SHAConstants.HRM_PRE_AUTH)) ||(hrmDetails1.getRequestType().equals(SHAConstants.HRM_ENHANCEMENT)) )
							&& ((requestType.equals(SHAConstants.HRM_PRE_AUTH)) || requestType.equals(SHAConstants.HRM_ENHANCEMENT)))
					{
					return "You cannot raise request against this intimation untill receive reply for the prevoius request";
					}
				}
			}
				

			
				
			}		

	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
			return null;
	
	}
	
	public Long getClaimCount(Long policyKey){
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY WHERE "
//				+ "B.STATUS_ID NOT IN("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") AND"
//				+ "A.INSURED_KEY  = "+insuredKey;
		
//		String query = "select count(1) from ims_cls_claim where status_id not in ("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") and insured_key = "+insuredKey;
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
//				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
//				+ "WHERE B.STATUS_ID NOT IN(7) AND A.INSURED_KEY  = "+insuredKey;
		
		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
				+ "WHERE B.STATUS_ID NOT IN(7) AND A.POLICY_KEY  = "+policyKey;
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		
		
		List<Object> objList = nativeQuery.getResultList();
		
		if(null != objList && !objList.isEmpty())
		{
//			for (Iterator it = objList.iterator(); it.hasNext();)
//			{
//				 Object[] myResult = (Object[]) it.next();
//				 Long count = (Long) myResult[0];
//				 return count;
//			}
			
			BigDecimal count = (BigDecimal)objList.get(0);
			return count.longValue();
		}
		return null;
	}
	
	public void setDBOutcome(PreauthDTO bean, Preauth preauth,
			Boolean isPreauthEnhancement) throws Exception {

		
		if(bean.getDbOutArray() != null){
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();

		if(preauth.getTreatmentType() != null){
			wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE,preauth.getTreatmentType().getValue());
		}
//		wrkFlowMap.put(SHAConstants.CASHLESS_NO,preauth.getPreauthId());
//		wrkFlowMap.put(SHAConstants.CASHLESS_KEY,preauth.getKey());
		if(preauth.getSpecialistType() != null){
			wrkFlowMap.put(SHAConstants.SPECIALITY_NAME,preauth.getSpecialistType().getValue());
		}
		if(null != preauth.getKey()){
			wrkFlowMap.put(SHAConstants.CASHLESS_KEY,preauth.getKey());
		}
//		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(),preauth.getClaim().getKey());
//		
//		Double parseDouble = Double.parseDouble(preauthReqAmt);
		
		Double parseDouble = 0d;
		
		if(bean.getAmountConsidered() != null){
			parseDouble = Double.valueOf(bean.getAmountConsidered());
		}
		
//		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,0l);

//		if (parseDouble != null ){
//			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,parseDouble.intValue());
//		}
		
		wrkFlowMap.put(SHAConstants.USER_ID,bean.getStrUserName());
//		wrkFlowMap.put(SHAConstants.ADMISSION_DATE,preauth.getDataOfAdmission());
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE,preauth.getStatus().getProcessValue());
		
		if(isPreauthEnhancement){
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_ENHANCEMENT_PROCESS);
		}else{
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_PREAUTH_PROCESS);
		}
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS)){
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_REFERED_TO_FLP);
		}else if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS)){
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_REFERED_TO_FLE);
		}
		
		wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 0l);
		
		String outCome = "";
		if (isPreauthEnhancement) {
			outCome = getDBOutcomeForPreauthEnhancement(bean, preauth);
			if (outCome
					.equalsIgnoreCase(SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_ESCALATE)) {
				if (bean != null
						&& bean.getPreauthMedicalDecisionDetails() != null
						&& bean.getPreauthMedicalDecisionDetails()
								.getEscalateTo() != null
						&& bean.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId() != null) {

					if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
							.getId().equals(ReferenceTable.CMA1)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 1l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId()
							.equals(ReferenceTable.CMA2)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 2l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId()
							.equals(ReferenceTable.CMA3)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 3l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId()
							.equals(ReferenceTable.CMA4)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 4l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId()
							.equals(ReferenceTable.CMA5)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 5l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId()
							.equals(ReferenceTable.CMA6)) {
						wrkFlowMap
								.put(SHAConstants.ESCALATE_ROLE_ID, 6l);
					}
				}
				
				wrkFlowMap.put(SHAConstants.ESCALATE_USER_ID, bean.getStrUserName());
			}

		} else {
			outCome = getDBOutcomeForPreauth(bean,preauth);
			if (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_PROCESS_PREAUTH_ESCALATE)){
				
				if (bean != null
						&& bean.getPreauthMedicalDecisionDetails() != null
						&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
						&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
								.getId() != null) {
					if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
							.getId().equals(ReferenceTable.CMA1)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,1l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId().equals(ReferenceTable.CMA2)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,2l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId().equals(ReferenceTable.CMA3)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,3l);
					} 
					else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId().equals(ReferenceTable.CMA4)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,4l);
					} 
					else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId().equals(ReferenceTable.CMA5)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,5l);
					} else if (bean.getPreauthMedicalDecisionDetails()
							.getEscalateTo().getId().equals(ReferenceTable.CMA6)) {
						wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,6l);
					}
					
				}
				
			}
			
			//outCome = SHAConstants.OUTCOME_REFER_TO_CPU;
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getSpecialistValue() != null && 
				bean.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue() != null){
			wrkFlowMap.put(SHAConstants.SPECIALITY_NAME,bean.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
		}
		
		if(bean.getStrUserName() != null){
			TmpEmployee employeeByName = getEmployeeByName(bean.getStrUserName());
			if(employeeByName != null){
				wrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
			}
		}
		if (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_REFER_TO_CPU) || outCome.equalsIgnoreCase(SHAConstants.OUTCOME_REFER_TO_CPU_ENHN)){
			wrkFlowMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS, SHAConstants.YES_FLAG);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, SHAConstants.YES_FLAG);
		}
		
		wrkFlowMap.put(SHAConstants.PAYLOAD_ALLOCATED_USER, null);
		wrkFlowMap.put(SHAConstants.PAYLOAD_ALLOCATED_DATE, null);
		
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
		wrkFlowMap.put(SHAConstants.ALLOCATED_USER,null);
		wrkFlowMap.put(SHAConstants.ALLOCATED_DATE,null);
		
		//added for hold release in manual submit on 28-02-2020
		if (bean.getIsPreauthAutoAllocationQ() != null
				&& !bean.getIsPreauthAutoAllocationQ()) {
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,null);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE,null);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,null);
		}
		/*if(null != bean.getPreauthHoldStatusKey() && bean.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)){
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,bean.getStrUserName());
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,SHAConstants.HOLD_FLAG);
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PREAUTH_HOLD_OUTCOME);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE,bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
		}*/
		
		//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	

			if (bean.getIsPreauthAutoAllocationQ() != null
					&& bean.getIsPreauthAutoAllocationQ()) {
				
				String strUserName = bean.getStrUserName();
				String intimationNo = preauth.getClaim().getIntimation()
						.getIntimationId();

				if (strUserName != null && intimationNo != null) {
					AutoAllocationDetails allocationDetail = getIntimationStatus(
							intimationNo, strUserName);
					if (allocationDetail != null) {
						if (allocationDetail.getStatusFlag() != null
								&& allocationDetail.getStatusFlag()
										.equalsIgnoreCase(SHAConstants.PENDING)) {
							allocationDetail
									.setStatusFlag(SHAConstants.REALLOCATION_COMPLETED_STATUS);
							allocationDetail.setCompletedDate(new Timestamp(
									System.currentTimeMillis()));
							allocationDetail.setCompletedUser(strUserName);
							allocationDetail.setModifiedBy(strUserName);
							allocationDetail.setModifiedDate(new Timestamp(
									System.currentTimeMillis()));
							
							entityManager.merge(allocationDetail);
							entityManager.flush();
							entityManager.clear();
						}
					}
				}

			}
			
			if (bean.getIsPreauthAutoAllocationQ() != null && bean.getIsPreauthAutoAllocationQ()) {
				/*if(null !=bean.getPreauthHoldStatusKey() && bean.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)){
					updateStageInformation(preauth,bean);
				}
				else{*/
					if(null != wrkFlowMap){
						dbCalService.releaseHoldClaim(Long.parseLong(String.valueOf(wrkFlowMap.get(SHAConstants.WK_KEY))));
					}
				//}
			}
		}

	}
	
	private String getDBOutcomeForPreauthEnhancement(PreauthDTO bean, Preauth preauth) {
		
		String outCome = "";
		if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_APPROVE;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_REJECT;
		} else if (ReferenceTable.WITHDRAW_KEYS.containsKey(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW;
			if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null 
					&& ! bean.getPreauthMedicalDecisionDetails().getWithdrawReason().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)){
				outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW_REJECT_OTHERS;
			}
			if (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) ||
					preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
				outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW_REJECT;
			}
//			preauthRequest.setKey(preauth.getClaim().getKey());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_DOWNSIZE;
//			preauthRequest.setKey(preauth.getClaim().getKey());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_REFER_TO_COORDINATOR;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS)) {
			outCome = SHAConstants.OUTCOME_REFER_TO_FLE;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_QUERY;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_REFER_64_VB_COMPLIANCE)) {
			outCome = SHAConstants.ENHANCEMENT_PROCESS_VB_OUTCOME;
		}else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_ESCALATE;
			
			if (bean != null
					&& bean.getPreauthMedicalDecisionDetails() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo().getId() != null) {
				if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue()
						==(ReferenceTable.SPECIALIST_DOCTOR)) {
					outCome =SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_ESCALATE_SPL;
				}
			}
			
		}
		
		else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHN_REFER_TO_CPU)) {
			outCome = SHAConstants.OUTCOME_REFER_TO_CPU_ENHN;
		}
//			if (bean != null
//					&& bean.getPreauthMedicalDecisionDetails() != null
//					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
//					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//							.getId() != null) {
//				
//				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//						.getId().equals(ReferenceTable.CMA1)) {
//					outCome = "CMA1ENH";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA2)) {
//					outCome = "CMA2ENH";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA3)) {
//					outCome = "CMA3ENH";
//				} 
//				else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA4)) {
//					outCome = "CMA4ENH";
//				} 
//				else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA5)) {
//					outCome = "CMA5ENH";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA6)) {
//					outCome = "CMA6ENH";
//				}else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().intValue()
//						==(ReferenceTable.SPECIALIST_DOCTOR)) {
//					outCome = "SPECIALISTENH";
//				}
//			
//				
//			}
//		}
		return outCome;
	}
	
	private String getDBOutcomeForPreauth(PreauthDTO bean, Preauth preauth) {
		
		String outCome = "" ;
		if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_APPROVE;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_REJECT;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
//			outCome = "DENIAL";
			outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_DENIEL_OF_CASHLESS;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_REFER_TO_COORDINATOR;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_QUERY_STATUS)) {
			outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_QUERY;
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS)) {
			outCome = SHAConstants.OUTCOME_REFER_TO_FLP;
		}   else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REFER_64_VB_COMPLIANCE)) {
			outCome = SHAConstants.PREAUTH_PROCESS_VB_OUTCOME;
		}else if (preauth.getStatus().getKey()
					.equals(ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
				outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_ESCALATE;
				if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue()
						==(ReferenceTable.SPECIALIST_DOCTOR)) {
					outCome = SHAConstants.OUTCOME_PROCESS_PREAUTH_ESCALATE_SPL;
				}
			}
		 else if (preauth.getStatus().getKey()
					.equals(ReferenceTable.PREAUTH_REFER_TO_CPU)) {
				outCome = SHAConstants.OUTCOME_REFER_TO_CPU;
			}
//			if (bean != null
//					&& bean.getPreauthMedicalDecisionDetails() != null
//					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
//					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//							.getId() != null) {
//				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
//						.getId().equals(ReferenceTable.CMA1)) {
//					outCome = "CMA1";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA2)) {
//					outCome = "CMA2";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA3)) {
//					outCome = "CMA3";
//				} 
//				else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA4)) {
//					outCome = "CMA4";
//				} 
//				else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA5)) {
//					outCome = "CMA5";
//				} else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().equals(ReferenceTable.CMA6)) {
//					outCome = "CMA6";
//				}else if (bean.getPreauthMedicalDecisionDetails()
//						.getEscalateTo().getId().intValue()
//						==(ReferenceTable.SPECIALIST_DOCTOR)) {
//					outCome = "SPECIALIST";
//				}
//			}
		
		return outCome;
	}
	
	public void submitDBProcedureForStadWithraw(Claim claim, Preauth preauth ,String outCome){
		
		Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
		
		//Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claim, hospitalById);
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		Insured insured = preauth.getIntimation().getInsured();
	
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		inputArray[SHAConstants.INDEX_OUT_COME] = outCome;
		inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
		inputArray[SHAConstants.INDEX_CASHLESS_NO] = preauth.getPreauthId();
		inputArray[SHAConstants.INDEX_CPU_CODE] = String.valueOf(preauth.getIntimation().getCpuCode().getCpuCode());
		
		if(claim.getPriorityEvent() != null && !claim.getPriorityEvent().isEmpty()){
			inputArray[SHAConstants.INDEX_PRIORITY] = claim.getPriorityEvent() != null ? claim.getPriorityEvent() : SHAConstants.NORMAL ;
    	}
		else if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
			
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		}else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		} else if (claim.getClaimPriorityLabel() != null && claim.getClaimPriorityLabel().equals("Y")) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
		}else{
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
		}
		
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_WITHDRAW_PROCESS;
		
//		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(), preauth
//				.getClaim().getKey());
//		if (preauthReqAmt != null) {
//			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = Double.valueOf(preauthReqAmt);
//		} else {
//			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = 0d;
//		}
		
		if(preauth.getTreatmentType() != null){
			inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = preauth.getTreatmentType().getValue();
		}

		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		//dbCalculationService.initiateTaskProcedure(parameter);
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
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

public void submitDBProcedureForStadDownsize(PreauthDTO preauthDTO, Preauth preauth){
	
	
		Claim claim = preauth.getClaim();
		
		Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
		
		//Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claim, hospitalById);
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		Insured insured = preauth.getIntimation().getInsured();
		
		String outCome = getDBOutComeForDownsize(preauthDTO, preauth);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		inputArray[SHAConstants.INDEX_OUT_COME] = outCome;
		inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
		inputArray[SHAConstants.INDEX_CASHLESS_NO] = preauth.getPreauthId();
		inputArray[SHAConstants.INDEX_CPU_CODE] = String.valueOf(preauth.getIntimation().getCpuCode().getCpuCode());
		
		if(claim.getPriorityEvent() != null && !claim.getPriorityEvent().isEmpty()){
			inputArray[SHAConstants.INDEX_PRIORITY] = claim.getPriorityEvent() != null ? claim.getPriorityEvent() : SHAConstants.NORMAL ;
    	}
		else if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
			
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		}else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		} else if (claim.getClaimPriorityLabel() != null && claim.getClaimPriorityLabel().equals("Y")) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
		}else{
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
		}
		
		if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null){
			inputArray[SHAConstants.INDEX_SPECIALITY_NAME]=preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue();
		}
		
		if (outCome.equals(SHAConstants.OUTCOME_FOR_ESCALATE)){
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA1)){
				inputArray[SHAConstants.INDEX_ESCALATE_ROLE_ID] = "ZSMA";
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
			else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA2)){
				inputArray[SHAConstants.INDEX_ESCALATE_ROLE_ID] = "ZMH";
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
			else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA3)){
				inputArray[SHAConstants.INDEX_ESCALATE_ROLE_ID] = "CLCMO";
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
		}
		
		if(preauthDTO.getStrUserName() != null){
			TmpEmployee employeeByName = getEmployeeByName(preauthDTO.getStrUserName());
			if(employeeByName != null){
				inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] = employeeByName.getEmpId();
			}
		}
		
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = preauth.getTotalApprovalAmount();
		
		if(preauth.getTreatmentType() != null){
			inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = preauth.getTreatmentType().getValue();
		}

		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		//dbCalculationService.initiateTaskProcedure(parameter);
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
	}

	public String getDBOutComeForDownsize(PreauthDTO preauthDTO,Preauth preauth){
		
		String outCome = "";
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			outCome = SHAConstants.OUTCOME_FOR_DOWNSIZE_PREAUTH;
		}
		else if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
				outCome = SHAConstants.OUTCOME_FOR_ESCALATE;
				if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					outCome = SHAConstants.OUTCOME_FOR_ESCALATE_SPECIALIST;
					createSpecialist(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
				}
			}
		}
		
		return outCome;
		
	}
	
	public void setDBOutComeForDownsizePreauthRequest(PreauthDTO preauthDTO, Preauth preauth){
		
		Claim claim = preauth.getClaim();
		
		Insured insured = preauth.getIntimation().getInsured();
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
		
		String outCome = getDBOutComeForDownsizeRequest(preauthDTO, preauth);
		
		
		if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
			
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.VIP_CUSTOMER);
		}else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.SENIOR_CITIZEN);
		}else{
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.NORMAL);
		}
		
		
		if(preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue() != null){
			wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, preauthDTO.getPreauthMedicalDecisionDetails().getSpecialistValue().getValue());
		}
		
		wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 0l);
		
		if (outCome.equals(SHAConstants.OUTCOME_FOR_ESCALATE_DOWNSIZE_REQUEST)){
			if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA1)){
				wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 1l);
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
			else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA2)){
				wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 2l);
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
			else if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.CMA3)){
				wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 3l);
				createEscalate(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
			}
			wrkFlowMap.put(SHAConstants.ESCALATE_USER_ID, preauthDTO.getStrUserName());
		}
		
		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,  preauth.getTotalApprovalAmount() < 0 ? 0 : preauth.getTotalApprovalAmount() );
		
		if(preauth.getTreatmentType() != null){
			wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE,  preauth.getTreatmentType().getValue());
		}
		
		if(preauthDTO.getStrUserName() != null){
			TmpEmployee employeeByName = getEmployeeByName(preauthDTO.getStrUserName());
			if(employeeByName != null){
				wrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
			}
		}
		
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
		
		//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
	}
	
	public String getDBOutComeForDownsizeRequest(PreauthDTO preauthDTO,Preauth preauth){
			
			String outCome = "";
			
			if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
				outCome = SHAConstants.OUTCOME_FOR_DOWNSIZE_PREAUTH_REQUEST;
			}
			else if(preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
				if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
					outCome = SHAConstants.OUTCOME_FOR_ESCALATE_DOWNSIZE_REQUEST;
					if(preauthDTO.getPreauthMedicalDecisionDetails().getEscalateTo().getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
						outCome = SHAConstants.OUTCOME_FOR_ESCALATE_SPECIALIST_DOWNSIZE_REQUEST;
						createSpecialist(preauthDTO, preauth, preauth.getStatus(), preauth.getStage(), preauthDTO.getPreauthMedicalDecisionDetails().getEscalateRemarks());
					}
				}
			}
			
			return outCome;
			
		}
	
	public void updateUserList(String loginId, String userName, String RoleId){
		TmpAdUserList userList = new TmpAdUserList();
		userList.setUserId(loginId);
		userList.setUserName(userName);
		userList.setRoleId(RoleId);
		entityManager.persist(userList);
		entityManager.flush();
		entityManager.clear();
	}
	
 	public Boolean getDBTaskForPreauth(String intimation,String currentQ){
		
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, intimation);
	mapValues.put(SHAConstants.CURRENT_Q, currentQ);
	
//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
	
	Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

	DBCalculationService db = new DBCalculationService();
//	 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
	
	 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
	if (taskProcedure != null && !taskProcedure.isEmpty()){
		return true;
	} 
	return false;
}
 	
	public List<Map<String, Object>> getDBTaskForCloseClaim(String intimation,String currentQ){
		
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, intimation);
	mapValues.put(SHAConstants.CURRENT_Q, currentQ);
	
//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
	
	Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

	DBCalculationService db = new DBCalculationService();
//	 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
	
	 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForReOpen(setMapValues);
	if (taskProcedure != null && !taskProcedure.isEmpty()){
		return taskProcedure;
	} 
	return taskProcedure;
}
	
	
	
	public AutoAllocationDetails getIntimationStatus(String intimationNo,
			String doctor) {

		String doctorId = doctor.toLowerCase();

		AutoAllocationDetails userDetail;
		Query findByTransactionKey = entityManager
				.createNamedQuery(
						"AutoAllocationDetails.findIntimationStatus")
				.setParameter("intimationNo", intimationNo)
				.setParameter("doctorId", doctorId);
		try {
			userDetail = (AutoAllocationDetails) findByTransactionKey
					.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetails(Long policyKey,PreauthDTO preauthDTO){
		
		Boolean isCashless = Boolean.TRUE;
		
		
		/*List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.FINANCIAL_SETTLED);
		statusList.add(ReferenceTable.FINANCIAL_APPROVE_STATUS);
		statusList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
		statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
		statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);*/
		List<Claim> claimList = null;
		if(preauthDTO.getNewIntimationDTO() != null && preauthDTO.getNewIntimationDTO().getPolicy() != null &&
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GROUP_TOPUP_PROD_KEY)){
			if(preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode() != null && preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_A)){
				Long insuredId = preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId() :0l;
				Query findByInsuredId = entityManager.createNamedQuery(
						"Claim.findByInsuredID").setParameter("insuredId",
								insuredId);
				if(findByInsuredId != null){
					claimList = (List<Claim>) findByInsuredId.getResultList();
				}
			}else if(preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode() != null && preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_C)){
				Long insuredId = preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId() :0l;
				List<Insured> insuredList = preauthDTO.getNewIntimationDTO().getPolicy().getInsured();
				List <Long> insuredKeyList = new ArrayList<Long>();
				if(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId() != null){
					if(preauthDTO.getNewIntimationDTO().getInsuredPatient().getDependentRiskId() != null){
						insuredList = getInsuredListForGMC(preauthDTO.getNewIntimationDTO().getInsuredPatient().getDependentRiskId());	
					}else{
						insuredList = getInsuredListForGMC(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());	
					}
				}else{
					insuredList = null;
				}
				if(insuredList != null && !insuredList.isEmpty()){

					for (Insured insured : insuredList) {
						insuredKeyList.add(insured.getKey());
					}				
				}
				Query findByInsuredId = null;
				if(insuredKeyList != null ){

					findByInsuredId = entityManager.createNamedQuery(
							"Claim.findClaimByInsuredList").setParameter("insuredList",
									insuredKeyList);
				}
				if(findByInsuredId != null){
					claimList = (List<Claim>) findByInsuredId.getResultList();
				}
			}
		}
		else {
		Query findByKey = entityManager.createNamedQuery("Claim.findByPolicyKey")
				.setParameter("policyKey", policyKey);
		//findByKey.setParameter("statusList", statusList);
		
		 claimList = (List<Claim>) findByKey.getResultList();
		}
		
		List<UpdateOtherClaimDetailDTO> dtoList = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		for (Claim claim : claimList) {
			Boolean isRodSettled = Boolean.FALSE;
			isCashless = Boolean.TRUE;
			UpdateOtherClaimDetailDTO dto = new UpdateOtherClaimDetailDTO();
			dto.setIntimationNo(claim.getIntimation().getIntimationId());
			dto.setInsurerName(SHAConstants.SUPER_SURPLUS_INSURER_NAME);
			dto.setEnableOrDisable(false);
			dto.setEditFlag(false);
			
			List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
			List<Long> reimbursementKey = new ArrayList<Long>();
			 reimbursementList = getReimbursementByClaimKey(claim.getKey());
			if(null != claim && (ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.equals(claim.getClaimType().getKey()))){
				if(null != reimbursementList && reimbursementList.isEmpty()){
					isCashless = Boolean.FALSE;
				}
				
			}

			if(isCashless)
			{
				
			for (Reimbursement reimbursement : reimbursementList) {
				if(null != reimbursement && null != reimbursement.getStatus() && null != reimbursement.getStatus().getKey() && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED) ||
						(((reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS )) || 
								(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))) &&
								(ReferenceTable.DEFINE_LIMIT_REJECTION_CATEGORY.equals(reimbursement.getRejectionCategoryId()))))){
					
					List<Long> icdChapterList = new ArrayList<Long>();
					List<Long> icdCodeList = new ArrayList<Long>();
					List<Long> icdBlockList = new ArrayList<Long>();
					
					reimbursementKey.add(reimbursement.getKey());

				   List<PedValidation> diagnosisByTransactionKey = getDiagnosisByTransactionKey(reimbursement.getKey());
				   for (PedValidation pedValidation : diagnosisByTransactionKey) {
					 if(pedValidation.getIcdChpterId() != null){
						 icdChapterList.add(pedValidation.getIcdChpterId());
					 }
					 if(pedValidation.getIcdBlockId() != null){
						 icdBlockList.add(pedValidation.getIcdBlockId());
					 }
					 if(pedValidation.getIcdCodeId() != null){
						 icdCodeList.add(pedValidation.getIcdCodeId());
					 }
				}
				   
				   if(! icdChapterList.isEmpty()){
					   String icdChapterName = getIcdChapterName(icdChapterList);
					   dto.setIcdChaper(icdChapterName);
				   }
				   
				   if(! icdCodeList.isEmpty()){
					   String icdCodeName = getIcdCodeName(icdCodeList);
					   dto.setIcdCode(icdCodeName);
				   }
				   
				   if(! icdBlockList.isEmpty()){
					   String icdBlockName = getIcdBlockName(icdBlockList);
					   dto.setIcdBlock(icdBlockName);
				   }

				   String diagnosis = getDiagnosis(diagnosisByTransactionKey);
				   
				   List<Procedure> procedure = getProcedure(reimbursement.getKey());
				   for (Procedure procedure2 : procedure) {
					diagnosis += procedure2.getProcedureName()+", ";
				   }
				   dto.setPrimaryProcedure(diagnosis);
				   
				   isRodSettled = true;
				}
				
				if(reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
					Double claimedAmount = 0d;
					Double admissibleAmt = 0d;
					List<Hospitalisation> hospitalisationList = getHospitalisationList(reimbursement.getKey());
					for (Hospitalisation hospitalisation : hospitalisationList) {
						if(hospitalisation.getClaimedAmount() != null){
							claimedAmount += hospitalisation.getClaimedAmount();
						}
						if(hospitalisation.getNetAmount() != null){
							admissibleAmt += hospitalisation.getNetAmount();
						}
					}
					
					Double deductAmt =  0d;
					
					dto.setClaimAmount(claimedAmount.longValue());
					if(reimbursement.getAmtConsAftCopayAmount() != null){
						dto.setAdmissibleAmount(reimbursement.getAmtConsAftCopayAmount().longValue());
						deductAmt = claimedAmount - reimbursement.getAmtConsAftCopayAmount() > 0 ? claimedAmount - reimbursement.getAmtConsAftCopayAmount() : 0d;
					}else{
						dto.setAdmissibleAmount(0l);
					}
					
					dto.setDeductibles(deductAmt.longValue());
					
					
				}else if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")){
					Double claimedAmount = 0d;
					Double admissibleAmt = 0d;
					List<Hospitalisation> hospitalisationList = getHospitalisationList(reimbursement.getKey());
					for (Hospitalisation hospitalisation : hospitalisationList) {
						if(hospitalisation.getClaimedAmount() != null){
							claimedAmount += hospitalisation.getClaimedAmount();
						}
						if(hospitalisation.getNetAmount() != null){
							admissibleAmt += hospitalisation.getNetAmount();
						}
					}
					
					Double deductAmt = claimedAmount - admissibleAmt > 0 ? claimedAmount - admissibleAmt : 0d;
					
					
					dto.setClaimAmount(claimedAmount.longValue());
					if(reimbursement.getAmtConsAftCopayAmount() != null){
						dto.setAdmissibleAmount(reimbursement.getAmtConsAftCopayAmount().longValue());
						deductAmt = claimedAmount - reimbursement.getAmtConsAftCopayAmount() > 0 ? claimedAmount - reimbursement.getAmtConsAftCopayAmount() : 0d;
					}else{
						dto.setAdmissibleAmount(0l);
					}
					dto.setDeductibles(deductAmt.longValue());
				}
				
				/*Long claimedAmountForROD = getClaimedAmountForROD(reimbursement.getKey());
				dto.setClaimAmount(claimedAmountForROD);
				
				Double reimbursementApprovedAmount = getReimbursementApprovedAmount(reimbursement.getKey());
				dto.setAdmissibleAmount(reimbursementApprovedAmount.longValue());*/
				
			}
		}
			else{
				
				Preauth preauth = getPreauthFromClaimKey(claim.getKey());
				if(null != preauth && null != preauth.getRejectionCategorId() && (ReferenceTable.PREAUTH_REJECT_STATUS).equals(preauth.getStatus().getKey()) &&
						(ReferenceTable.DEFINE_LIMIT_REJECTION_CATEGORY).equals(preauth.getRejectionCategorId().getKey())){
				
				List<Long> icdChapterList = new ArrayList<Long>();
				List<Long> icdCodeList = new ArrayList<Long>();
				List<Long> icdBlockList = new ArrayList<Long>();
				
				List<PedValidation> diagnosisByTransactionKey = getDiagnosisByTransactionKey(preauth.getKey());
				   for (PedValidation pedValidation : diagnosisByTransactionKey) {
					 if(pedValidation.getIcdChpterId() != null){
						 icdChapterList.add(pedValidation.getIcdChpterId());
					 }
					 if(pedValidation.getIcdBlockId() != null){
						 icdBlockList.add(pedValidation.getIcdBlockId());
					 }
					 if(pedValidation.getIcdCodeId() != null){
						 icdCodeList.add(pedValidation.getIcdCodeId());
					 }
				}
				   
				   if(! icdChapterList.isEmpty()){
					   String icdChapterName = getIcdChapterName(icdChapterList);
					   dto.setIcdChaper(icdChapterName);
				   }
				   
				   if(! icdCodeList.isEmpty()){
					   String icdCodeName = getIcdCodeName(icdCodeList);
					   dto.setIcdCode(icdCodeName);
				   }
				   
				   if(! icdBlockList.isEmpty()){
					   String icdBlockName = getIcdBlockName(icdBlockList);
					   dto.setIcdBlock(icdBlockName);
				   }
				   
				   
				   String diagnosis = getDiagnosis(diagnosisByTransactionKey);
				   
				   List<Procedure> procedure = getProcedure(preauth.getKey());
				   for (Procedure procedure2 : procedure) {
					diagnosis += procedure2.getProcedureName()+", ";
				   }
				   dto.setPrimaryProcedure(diagnosis);
				   
				   String claimedAmountForROD = getPreauthReqAmt(preauth.getKey(),claim.getKey());
				   if(null != claimedAmountForROD && ! claimedAmountForROD.isEmpty()){
					   BigDecimal b = new BigDecimal(claimedAmountForROD);
				       long longClaimedAmountForROD = b.longValue();
				       dto.setClaimAmount(longClaimedAmountForROD);
				       dto.setAdmissibleAmount(longClaimedAmountForROD);
				       
				   }
				   
				   isRodSettled = true;
				}
				
			}	//Long claimedAmountForROD = getClaimedAmountForROD(reimbursement.getKey());
			if(isRodSettled)
				dtoList.add(dto);
			if(! reimbursementKey.isEmpty()){
			    List<UpdateOtherClaimDetailDTO> otherClaimDetailsBasedOnClaimKey = getOtherClaimDetailsBasedOnClaimKey(claim.getKey(),reimbursementKey);
			    if(otherClaimDetailsBasedOnClaimKey != null && ! otherClaimDetailsBasedOnClaimKey.isEmpty()){
			    	dtoList.addAll(otherClaimDetailsBasedOnClaimKey);
			    }
			  }
			}
			
			
		return dtoList;
		}
		
	

	public List<PAAdditionalCovers> getAdditionalCoversForRodAndBillEntry(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
		query = query.setParameter("rodKey",rodKey );
		List<PAAdditionalCovers> additionalCovers = query.getResultList();
		if(null != additionalCovers && !additionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return additionalCovers;
		}
		return null;
	}
	
	public List<PAOptionalCover> getOptionalCoversForRodAndBillEntry(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
		query = query.setParameter("rodKey",rodKey );
		List<PAOptionalCover> optionalCovers = query.getResultList();
		if(null != optionalCovers && !optionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return optionalCovers;
		}
		return null;
	}
	
	private Double getClaimedAmountValueForView(DocAcknowledgement docAck)
	{
		Double totalAmt = 0d;
		Double addOnAmt = 0d;
		Double optionalAmt = 0d;
		Double claimedAmt = 0d;
		
		if(docAck != null)
		{
			if(docAck.getRodKey()!= null)
			{
				List<PAAdditionalCovers> addOnCoversList = getAdditionalCoversForRodAndBillEntry(docAck.getRodKey());
				List<PAOptionalCover> optionalCoversList = getOptionalCoversForRodAndBillEntry(docAck.getRodKey());
				
				if(addOnCoversList != null  && !addOnCoversList.isEmpty())
				{
					for(PAAdditionalCovers paAdditionalCovers : addOnCoversList)
					{
						if(paAdditionalCovers.getClaimedAmount() != null)
						{
						addOnAmt = addOnAmt + paAdditionalCovers.getClaimedAmount();
						}
					}
				}
				
				if(optionalCoversList != null && !optionalCoversList.isEmpty())
				{
					for(PAOptionalCover paOptionalCovers : optionalCoversList)
					{
						if(paOptionalCovers.getClaimedAmount() != null)
						{
							optionalAmt = optionalAmt + paOptionalCovers.getClaimedAmount();
						}
					}
				}
			}
			
			if(docAck.getBenifitClaimedAmount() != null)
			{
				claimedAmt = docAck.getBenifitClaimedAmount();
			}
			
			totalAmt = addOnAmt + optionalAmt + claimedAmt;
		}
		return totalAmt;
	}
	
	public String getCoverValueForViewBasedOnrodKey(Long rodKey)
	{
		String optCover = "";
		String addOnCover = "";
		String coverValues = "";
		
		if(rodKey != null)
		{
		List<PAcoverTableViewDTO> addOnCoversList = getAdditionalCoversValueBasedOnRODForView(rodKey);
		List<PAcoverTableViewDTO> optionalCoversList = getOptionalCoversValueBasedOnRODForView(rodKey);
		
		if(addOnCoversList != null)
		{
		for(PAcoverTableViewDTO addOn: addOnCoversList)
		{
			if(addOn.getCover() != null){
				addOnCover += addOn.getCover()+", ";	
			}
		}
		}
		if(optionalCoversList != null)
		{
		for(PAcoverTableViewDTO optional: optionalCoversList)
		{
			if(optional.getCover() != null){
				optCover += optional.getCover()+", ";	
			}
		}
		}
		coverValues = optCover + addOnCover;
		}
		
		return coverValues;
	}
	
	public List<PAcoverTableViewDTO> getAdditionalCoversValueBasedOnRODForView(Long rodKey)
    {
    	List<PAcoverTableViewDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAAdditionalCovers> paAddCoversList = query.getResultList();
    	if(null != paAddCoversList && !paAddCoversList.isEmpty())
    	{
    		addOnCoversList = new ArrayList<PAcoverTableViewDTO>();
    		for (PAAdditionalCovers paAdditionalCovers : paAddCoversList) {
    			
    			PAcoverTableViewDTO addOnCoversDTO = new PAcoverTableViewDTO();
    			Query query1 = entityManager.createNamedQuery("MasPAClaimCover.findByKey");
    	    	query1 = query1.setParameter("coverKey", paAdditionalCovers.getCoverId());
    	    	List<MasPAClaimCover> masPAClaimCoverList = query1.getResultList();
    	    	addOnCoversDTO.setCover(masPAClaimCoverList.get(0).getCoverDesc());
    			addOnCoversDTO.setClaimedAmt(paAdditionalCovers.getClaimedAmount());
    			addOnCoversDTO.setRemarks(paAdditionalCovers.getRemarks());
    			addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }
    
    
    public List<PAcoverTableViewDTO> getOptionalCoversValueBasedOnRODForView(Long rodKey)
    {
    	List<PAcoverTableViewDTO> optionalCoversList = null;
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAOptionalCover> paOptionalCoversList = query.getResultList();
    	if(null != paOptionalCoversList && !paOptionalCoversList.isEmpty())
    	{
    		optionalCoversList = new ArrayList<PAcoverTableViewDTO>();
    		for (PAOptionalCover paOptionalCovers : paOptionalCoversList) {
    			
    			PAcoverTableViewDTO optionalCoversDTO = new PAcoverTableViewDTO();
    			
    			Query query1 = entityManager.createNamedQuery("MasPAClaimCover.findByKey");
    	    	query1 = query1.setParameter("coverKey", paOptionalCovers.getCoverId());
    	    	List<MasPAClaimCover> masPAClaimCoverList = query1.getResultList();
    	    	optionalCoversDTO.setCover(masPAClaimCoverList.get(0).getCoverDesc());
    	    	optionalCoversDTO.setClaimedAmt(paOptionalCovers.getClaimedAmount());
    	    	optionalCoversDTO.setRemarks(paOptionalCovers.getRemarks());
				optionalCoversList.add(optionalCoversDTO);
				
			}
    	}
    	return optionalCoversList;
    }
    
    private Double getBenefitAddOnOptionalApprovedAmt(Reimbursement reimbursementByKey)
	{
		Double totalAmt = 0d;
		Double addApprovedAmt = 0d;
		Double optApprovedAmt = 0d;
		Double benApprovedAmt = 0d;
		
		if(reimbursementByKey != null)
		{
			if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementByKey.getStage().getKey()) && (ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursementByKey.getStatus().getKey()) || ReferenceTable.FINANCIAL_SETTLED.equals(reimbursementByKey.getStatus().getKey())))
			{
				if((SHAConstants.HOSP).equalsIgnoreCase(reimbursementByKey.getDocAcknowLedgement().getBenifitFlag()) || ((SHAConstants.PART).equalsIgnoreCase(reimbursementByKey.getDocAcknowLedgement().getBenifitFlag()))
						)
				{	
									
					List<ClaimPayment> claimPayment = getClaimPaymentByRodNumber(reimbursementByKey.getRodNumber()); 
					if(null != claimPayment && !claimPayment.isEmpty())
					{
						for (ClaimPayment claimPayment2 : claimPayment) {							
						
						if(SHAConstants.YES_FLAG.equalsIgnoreCase(claimPayment2.getReconisderFlag()))
						{
							totalAmt = reimbursementByKey.getFinancialApprovedAmount()+claimPayment2.getTotalApprovedAmount();
						}
						else
						{
							totalAmt = reimbursementByKey.getFinancialApprovedAmount();
						}
						}
													
					}
				}				
				
				else
				{
					if(reimbursementByKey.getBenApprovedAmt() != null)
					{
						benApprovedAmt = reimbursementByKey.getBenApprovedAmt();
					}
					if(reimbursementByKey.getOptionalApprovedAmount() != null)
					{
						optApprovedAmt = reimbursementByKey.getOptionalApprovedAmount();
					}
					if(reimbursementByKey.getAddOnCoversApprovedAmount() != null)
					{
						addApprovedAmt = reimbursementByKey.getAddOnCoversApprovedAmount();
					}
					
					totalAmt = benApprovedAmt + optApprovedAmt + addApprovedAmt;
				}
			}
		}
		return totalAmt;
	}

    @SuppressWarnings("unchecked")
	public List<ClaimPayment> getClaimPaymentByRodNumber(String rodNo) {
		
		List<ClaimPayment> paymentList = null;
		try{
			Query query = entityManager.createNamedQuery(
					"ClaimPayment.findByRodNo").setParameter("rodNumber",
							rodNo);
			paymentList = query.getResultList();
			
			if(paymentList != null && !paymentList.isEmpty()){
				for(ClaimPayment rodObj : paymentList){	
					entityManager.refresh(rodObj);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}


		return paymentList;
	}
    
    private ClaimPayment getPaymentDetails(String rodNo)
	{
    	ClaimPayment fvrDetail; 
		Query findByTransactionKey = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNo").setParameter("rodNumber", rodNo);
		try{
			fvrDetail = (ClaimPayment) findByTransactionKey.getSingleResult();
			return fvrDetail;
		}
		catch(Exception e)
		{
			return null;
		}
		
		
			
	}
    
	public Boolean getPADBTaskForPreauth(String intimation,String currentQ){
		
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, intimation);
	mapValues.put(SHAConstants.CURRENT_Q, currentQ);
	
//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
	
	Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

	DBCalculationService db = new DBCalculationService();
//	 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
	
	 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
	if (taskProcedure != null && !taskProcedure.isEmpty()){
		return true;
	} 
	return false;
}
	
	public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetailsList(Long cashlessKey){
		
		Query query = entityManager.createNamedQuery("UpdateOtherClaimDetails.findByCashlessKey");
		query.setParameter("cashlessKey", cashlessKey);
		
		List<UpdateOtherClaimDetails> resultList = (List<UpdateOtherClaimDetails>) query.getResultList();
		
		return resultList;
		
	}

	
	public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetailsDTO(Long cashlessKey){
		
		List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsList(cashlessKey);
		List<UpdateOtherClaimDetailDTO> resultList = PreauthMapper.getInstance().getUpdateOtherClaimDetailsDTO(updateOtherClaimDetailsList);
		
		return resultList;
	}
	
	
/*	public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetailsDTO(Long cashlessKey,Long policyKey){
		
		List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsList(cashlessKey,policyKey);
		List<UpdateOtherClaimDetailDTO> resultList = PreauthMapper.getInstance().getUpdateOtherClaimDetailsDTO(updateOtherClaimDetailsList);
		
		return resultList;
	}
	
	public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetailsList(Long cashlessKey,Long policyKey){
		
		Query query = entityManager.createNamedQuery("UpdateOtherClaimDetails.findByCashlessKey");
		query.setParameter("cashlessKey", cashlessKey);
		
		List<UpdateOtherClaimDetails> resultList = (List<UpdateOtherClaimDetails>) query.getResultList();
		List<Reimbursement> reimbList = new ArrayList<Reimbursement>();	
		
		
		
	    List<Intimation> intimation = getIntimationByPolicyKey(policyKey);
	    if(null != intimation && !intimation.isEmpty()){
	    	
	    	for (Intimation intimation2 : intimation) {
				
	    		List<Claim> claim = getClaimByIntimation(intimation2.getKey());
	    		
	    		if(null != claim && !claim.isEmpty())
	    		{
	    			for (Claim claim2 : claim) {
	    				List<Reimbursement> reimbursement = getReimbursementObjByClaimKey(claim2.getKey());
	    				
	    				if(null != reimbursement && !reimbursement.isEmpty()){
	    					
	    					for (Reimbursement reimbursement2 : reimbursement) {
	    						
	    						UpdateOtherClaimDetails newResultList = new UpdateOtherClaimDetails();
	    						
	    						resultList.add(e)
								
	    						if((ReferenceTable.FINANCIAL_SETTLED).equals(reimbursement2.getStatus().getKey())){
	    							
	    							reimbList.add(reimbursement2);
	    						}
							}
	    				}
					
	    			}
	    		}
			}
	    	
	    	
	    	
	    }
		
		return resultList;
		
	}*/
	
	public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetailsDTOForReimbursement(Long reimbursementKey){
		
		List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsListForReimbursement(reimbursementKey);
		List<UpdateOtherClaimDetailDTO> resultList = PreauthMapper.getInstance().getUpdateOtherClaimDetailsDTO(updateOtherClaimDetailsList);
		
		return resultList;
	}
	
	public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetailsListForReimbursement(Long reimbursementKey){
		
		Query query = entityManager.createNamedQuery("UpdateOtherClaimDetails.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementKey);
		
		List<UpdateOtherClaimDetails> resultList = (List<UpdateOtherClaimDetails>) query.getResultList();
		
		return resultList;
		
	}
	
	 public String getDiagnosis(List<PedValidation> pedValidationList){

		    String diagnosisName = "";
			
			for (PedValidation pedValidation : pedValidationList) {
				
				Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
				diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
				Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
				if(masters != null){
				diagnosisName += masters.getValue()+",";
				}

			}
		
			return diagnosisName;
	   }
	 
	 
	 
	 @SuppressWarnings("unchecked")
	 public List<Procedure> getProcedure(Long transactionKey){
	 	
	 		Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
	 		query.setParameter("transactionKey", transactionKey);
	 		
	 		List<Procedure> resultList = (List<Procedure>)query.getResultList();
	 		
	 		return resultList;
	 }
	 
	 public String getIcdChapterName(List<Long> icdChapterList){
		 
		 Query query = entityManager.createNamedQuery("IcdChapter.findByKeyList");
	 		query.setParameter("keyList", icdChapterList);
	 		
	 		List<IcdChapter> resultList = (List<IcdChapter>)query.getResultList();
	 		String strIcdChapter = "";
	 		for (IcdChapter icdChapter : resultList) {
				strIcdChapter += icdChapter.getDescription() +",";
			}
	 		
	 		return strIcdChapter;
		 
	 }
	 
	 public String getIcdCodeName(List<Long> icdCodeList){
		 
		 Query query = entityManager.createNamedQuery("IcdCode.findByKeyList");
	 		query.setParameter("keyList", icdCodeList);
	 		
	 		List<IcdCode> resultList = (List<IcdCode>)query.getResultList();
	 		String strIcdCode = "";
	 		for (IcdCode icdCode : resultList) {
	 			strIcdCode += icdCode.getDescription() +",";
			}
	 		
	 		return strIcdCode;
		 
	 }
	 
 public String getIcdBlockName(List<Long> icdBlockList){
		 
		 Query query = entityManager.createNamedQuery("IcdBlock.findByKeyList");
	 		query.setParameter("keyList", icdBlockList);
	 		
	 		List<IcdBlock> resultList = (List<IcdBlock>)query.getResultList();
	 		String strIcdBlock = "";
	 		for (IcdBlock icdBlock : resultList) {
	 			strIcdBlock += icdBlock.getDescription() +",";
			}
	 		
	 		return strIcdBlock;
		 
	 }
 
 @SuppressWarnings({ "unchecked", "unused" })
	public Intimation getIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList.get(0);

		}
		return null;
	}
 
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByClaimnKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}

	public void submitFVRGradingDetail(PreauthDTO preauthDTO){
		
			List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO
					.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
			
			if(fvrGradingDetailsDTO != null && !fvrGradingDetailsDTO.isEmpty()){
				for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
					
					if(fvrGradingDetailsDTO2
							.getNewFvrGradingDTO() != null){
						List<NewFVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2
								.getNewFvrGradingDTO();
						FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2
								.getKey());
						for (NewFVRGradingDTO fvrGradingDTO2 : fvrGradingDTO) {
							
							if(fvrGradingDTO2.getKey() == null){
								FVRGradingDetail gradingDetail = new FVRGradingDetail();
								gradingDetail.setFvrKey(fvrByKey.getKey());
								
								//if(fvrByKey.getStatus() != null && fvrByKey.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED)){
									if((fvrGradingDetailsDTO2.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentANotEdit()) && (fvrGradingDetailsDTO2.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentBNotEdit()))
									{
										if(fvrGradingDTO2.getStatusFlag() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
											gradingDetail.setSegment(fvrGradingDTO2.getSegment());
											gradingDetail.setGrading(fvrGradingDTO2.getStatusFlag());
											if(fvrGradingDTO2.getFvrSeqNo() != null){
												gradingDetail.setSeqNo(fvrGradingDTO2.getFvrSeqNo());	
											}
										}else if(fvrGradingDTO2.getStatusFlagSegmentA() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A)){
											gradingDetail.setSegment(fvrGradingDTO2.getSegment());
											if(fvrGradingDTO2.getFvrSeqNo() != null){
												gradingDetail.setSeqNo(fvrGradingDTO2.getFvrSeqNo());	
											}
											gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentA());
										}
									}else if(fvrGradingDetailsDTO2.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentCNotEdit()){
										if(fvrGradingDTO2.getStatusFlagSegmentC() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C)){
											if(fvrGradingDTO2.getFvrSeqNo() != null){
												gradingDetail.setSeqNo(fvrGradingDTO2.getFvrSeqNo());	
											}
											gradingDetail.setSegment(fvrGradingDTO2.getSegment());
											gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentC());
										}
									}
									/*}else if(fvrByKey.getStatus() != null && fvrByKey.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR))
								{
									if(fvrGradingDTO2.getCategory() != null && fvrGradingDTO2.getCategory().equalsIgnoreCase(SHAConstants.FVR_NOT_RECEIVED)){
										if(fvrGradingDTO2.getStatusFlagSegmentC() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C)){
											gradingDetail.setSegment(fvrGradingDTO2.getSegment());
											gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentC());
										}	
									}
								}*/
								
								gradingDetail.setRemarks(fvrGradingDTO2.getCategory());
								gradingDetail.setCreatedBy(SHAUtils.getUserNameForDB(preauthDTO.getStrUserName()));
								gradingDetail.setGradingDate(new Timestamp(System
										.currentTimeMillis()));
								gradingDetail.setCreatedDate(new Timestamp(System
										.currentTimeMillis()));
							
								if(gradingDetail.getSegment() != null && gradingDetail.getGrading() != null){
										entityManager.persist(gradingDetail);
										entityManager.flush();
									}
								
							}else{
								
								FVRGradingDetail gradingDetail = getFVRGradingDetailByKey(fvrGradingDTO2.getKey());
								
								if(gradingDetail != null){
									
									//if(fvrByKey.getStatus() != null && (fvrByKey.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR) || fvrByKey.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED))){
								
										if((fvrGradingDetailsDTO2.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentANotEdit()) && (fvrGradingDetailsDTO2.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentBNotEdit()))
										{
											if(fvrGradingDTO2.getStatusFlag() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
												gradingDetail.setGrading(fvrGradingDTO2.getStatusFlag());
											}else if(fvrGradingDTO2.getStatusFlagSegmentA() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A)){
												gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentA());
											}else{
												if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C)){
													gradingDetail.setGrading(null);
												}
											}
										}else if(fvrGradingDetailsDTO2.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO2.getIsSegmentCNotEdit()){
											if(fvrGradingDTO2.getStatusFlagSegmentC() != null && fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C)){
												if( fvrGradingDTO2.getFvrSeqNo()!= null && fvrGradingDTO2.getFvrSeqNo().intValue() == 22 && fvrGradingDTO2.getIsFVRReceived() != null && fvrGradingDTO2.getIsFVRReceived()){
													gradingDetail.setGrading(SHAConstants.N_FLAG);
												}
												else{
													gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentC());
												}
											}else{
												if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
													gradingDetail.setGrading(null);
												}else if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A)){
													gradingDetail.setGrading(null);
												}else if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C) && fvrGradingDTO2.getFvrSeqNo()!= null && fvrGradingDTO2.getFvrSeqNo().intValue() == 22 && fvrGradingDTO2.getIsFVRReceived() != null && fvrGradingDTO2.getIsFVRReceived()){
													if(gradingDetail.getSeqNo() != null && gradingDetail.getSeqNo().equals(fvrGradingDTO2.getFvrSeqNo()) && gradingDetail.getGrading() != null && gradingDetail.getGrading().equalsIgnoreCase(SHAConstants.YES_FLAG)){
														gradingDetail.setGrading(SHAConstants.N_FLAG);	
													}
												}	
											}
											
										}
										gradingDetail.setModifiedBy(SHAUtils.getUserNameForDB(preauthDTO.getStrUserName()));
										gradingDetail.setGradingDate(new Timestamp(System
												.currentTimeMillis()));
										gradingDetail.setModifiedDate(new Timestamp(System
												.currentTimeMillis()));
									
										if(gradingDetail.getGrading() != null){
											entityManager.merge(gradingDetail);
											entityManager.flush();
										}
									//}
								}
								
							}
						}
						if(fvrGradingDetailsDTO2.getIsFVRReceived() != null && fvrGradingDetailsDTO2.getIsFVRReceived()){
							
							if(fvrGradingDetailsDTO2.getGradingRemarks() != null){
								fvrByKey.setGradingRmrks(fvrGradingDetailsDTO2.getGradingRemarks());
							}
														
							Status status = new Status();
							status.setKey(ReferenceTable.FVR_GRADING_STATUS);
							fvrByKey.setModifiedBy(SHAUtils.getUserNameForDB(preauthDTO.getStrUserName()));
							fvrByKey.setModifiedDate(new Timestamp(System
									.currentTimeMillis()));
							fvrByKey.setStatus(status);
							fvrByKey.setFvrGradingDate(new Timestamp(System.currentTimeMillis()));
							
							entityManager.merge(fvrByKey);
							entityManager.flush();	
						}
					}
					
					
				}
			}
			
		
		
	}
 
 public FVRGradingDetail getFVRGradingDetailByKey(Long key) {
		Query query = entityManager
				.createNamedQuery("FVRGradingDetail.findByKey").setParameter("key", key);
		FVRGradingDetail singleResult = (FVRGradingDetail) query.getSingleResult();
		return singleResult;
	}


public Boolean getFVRStatusIdByClaimKey(Long claimkey){
	
	List<Long> statusList = new ArrayList<Long>();

	//statusList.add(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
	statusList.add(ReferenceTable.ASSIGNFVR);
	//statusList.add(ReferenceTable.FVR_GRADING_STATUS);

	
	Query query = entityManager.createNamedQuery("FieldVisitRequest.findByStatusKey");
	query.setParameter("claimKey", claimkey);
	query.setParameter("statusList", statusList);
	
	List<FieldVisitRequest> resultList = query.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		//return resultList.get(0);
		return true;
	}
	
	return false;

}

public FieldVisitRequest getPendingFVRByClaimKey(Long claimkey){
	
	List<Long> statusList = new ArrayList<Long>();

	statusList.add(ReferenceTable.ASSIGNFVR);
	
	try{
		Query query = entityManager.createNamedQuery("FieldVisitRequest.findByStatusKey");
		query.setParameter("claimKey", claimkey);
		query.setParameter("statusList", statusList);
		
		List<FieldVisitRequest> resultList = query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(resultList.size()-1);			
		}
	}
	catch(Exception e){
			e.printStackTrace();
        }
	
	return null;

}

private Policy  getPolicyByKey(Long policyKey) {
	
	Query query = entityManager.createNamedQuery("Policy.findByKey");
	query = query.setParameter("policyKey", policyKey);
	Policy policyList = (Policy) query.getSingleResult();
	if (policyList != null){
		return policyList;
	}
	return null;
}

public List<Intimation> getIntimationByPolicyKey(Long policyKey){
	if(policyKey != null){
		Query query = entityManager.createNamedQuery("Intimation.findByPolicyKey");
		query.setParameter("policyKey", policyKey);

		List<Intimation> resultList =  (List<Intimation>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()){
//			entityManager.refresh(resultList);
			return resultList;
		}
	}
	return null;
}



@SuppressWarnings("unchecked")
public List<Claim> getClaimByIntimation(Long intimationKey) {
	List<Claim> a_claimList = new ArrayList<Claim>();
	if (intimationKey != null) {

		Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
		findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
		try {

			a_claimList = (List<Claim>) findByIntimationKey.getResultList();
			
			for (Claim claim : a_claimList) {
				entityManager.refresh(claim);
			}

			System.out.println("size++++++++++++++" + a_claimList.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	} else {
		// intimationKey null
	}
	return a_claimList;

}



@SuppressWarnings("unchecked")
public List<Reimbursement> getReimbursementObjByClaimKey(Long claimKey) {
	Query query = entityManager.createNamedQuery(
			"Reimbursement.findByClaimKey").setParameter("claimKey",
			claimKey);
	List<Reimbursement> rodList = query.getResultList();

	return rodList;
}


@SuppressWarnings("unchecked")
public List<Hospitalisation> getHospitalisationList(Long rodKey) {

	Query query = entityManager
			.createNamedQuery("Hospitalisation.findByReimbursement");
	query = query.setParameter("reimbursementKey", rodKey);

	List<Hospitalisation> billDetails = (List<Hospitalisation>) query
			.getResultList();

	for (Hospitalisation hospitalisation : billDetails) {
		entityManager.refresh(hospitalisation);

	}
	return billDetails;

}

public List<UpdateOtherClaimDetailDTO> getOtherClaimDetailsBasedOnClaimKey(Long claimKey,List<Long> reimbursementList){
	
	List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsListForClaim(claimKey,reimbursementList);
	List<UpdateOtherClaimDetailDTO> resultList = PreauthMapper.getInstance().getUpdateOtherClaimDetailsDTO(updateOtherClaimDetailsList);
	for (UpdateOtherClaimDetailDTO updateOtherClaimDetailDTO : resultList) {
		updateOtherClaimDetailDTO.setEnableOrDisable(false);
		updateOtherClaimDetailDTO.setEditFlag(false);
	}
	
	return resultList;
}

public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetailsListForClaim(Long claimKey,List<Long> reimbursementList){
	
	Query query = entityManager.createNamedQuery("UpdateOtherClaimDetails.findByClaimKey");
	query.setParameter("claimKey", claimKey);
	query.setParameter("reimbursementKey", reimbursementList);
	
	List<UpdateOtherClaimDetails> resultList = (List<UpdateOtherClaimDetails>) query.getResultList();
	
	return resultList;
	
}

public MasAilmentLimit getAilmentLimitGMC(Long sublimitId){
	Query claimLimitQuery = entityManager.createNamedQuery("MasAilmentLimit.findByKey");
	claimLimitQuery.setParameter("limitKey",sublimitId);
	
	MasAilmentLimit claimLimit = (MasAilmentLimit)claimLimitQuery.getSingleResult();
	
	return claimLimit;
}
public JSONObject validateToken(String token) {
	JSONObject jSONObject = null;
	try {
		SignedJWT signedJWTnew = SignedJWT.parse(token);

		BigInteger modulus = new BigInteger(BPMClientContext.RSA_PUBLIC);
		BigInteger exponent = new BigInteger(SHAConstants.RSA_EXPONENT);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
		KeyFactory fact = KeyFactory.getInstance(SHAConstants.RSA_INSTANCE_NAME);
		Key pubKey = fact.generatePublic(keySpec);

		JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) pubKey);
		/*if(signedJWTnew.verify(verifier)) {
			Assert.assertTrue(signedJWTnew.verify(verifier));*/
			Date date = (Date)signedJWTnew.getJWTClaimsSet().getClaim("exp");
			long timeInMilliseconds = date.getTime();

			jSONObject = signedJWTnew.getPayload().toJSONObject();
			jSONObject.put("exp", timeInMilliseconds);
//		}
	} catch (Exception e) {
		e.getMessage();
	}
	return jSONObject;
}

public String createJWTToken(String intimationNo) throws NoSuchAlgorithmException, ParseException{	
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
		claimsSet.claim("intimationNO", intimationNo);	
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

public MasClaimPortal getPortalViewByApplicationName(String source){

	Query query = entityManager
			.createNamedQuery("MasClaimPortal.findByApplicationName");
	query.setParameter("portal", source.toUpperCase());
	List<MasClaimPortal> singleResult = (List<MasClaimPortal>) query.getResultList();
	if(singleResult !=null && !singleResult.isEmpty()){
		return singleResult.get(0);
	}
	return null;	
}

public Boolean getFVRStatusIdMAByClaimKey(Long claimkey){
	
	List<Long> statusList = new ArrayList<Long>();

	//statusList.add(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
	statusList.add(ReferenceTable.INITITATE_FVR);
	//statusList.add(ReferenceTable.FVR_GRADING_STATUS);
	
	List<Long> stageList = new ArrayList<Long>();

	//statusList.add(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
	stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
	

	Query query = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKeyStageStatus");
	query.setParameter("claimKey", claimkey);
	query.setParameter("stageList", stageList);
	query.setParameter("statusList", statusList);
	
	/*List<Long> statusList1 = new ArrayList<Long>();
	statusList1.add(ReferenceTable.ASSIGNFVR);
	statusList1.add(ReferenceTable.INITITATE_FVR);*/
	
	//Query query1 = entityManager.createNamedQuery("FieldVisitRequest.findByStatusKey");
	//query.setParameter("claimKey", claimkey);
	//query.setParameter("statusList", statusList1);
	
	List<FieldVisitRequest> resultList = query.getResultList();
	//List<FieldVisitRequest> resultList1 = query1.getResultList();
	
	if((resultList != null && ! resultList.isEmpty())){
		//return resultList.get(0);
		return false;
	}
	
	return true;

}

	public List<ViewDoctorRemarksDTO> getDoctorInternalNotesHistoryDetails(Long claimKey){
		List<ViewDoctorRemarksDTO> resultList = new ArrayList<ViewDoctorRemarksDTO>();
		List<InternalNotesHistory> notesHistoryByClaimKey = getInternalNotesHistoryByClaimKey(claimKey);
		if(notesHistoryByClaimKey != null){
			ViewDoctorRemarksDTO dto = null;
			for (InternalNotesHistory internalNotes : notesHistoryByClaimKey) {
				dto = new ViewDoctorRemarksDTO();
				dto.setStrNoteDate(internalNotes.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm aa").format(internalNotes.getCreatedDate()) : "");
				dto.setTransaction(internalNotes.getReferenceNo());
				dto.setTransactionType(internalNotes.getTransacType());
				dto.setRemarks(internalNotes.getStatusRemarks());
				if(internalNotes.getCreatedBy() != null){
					TmpEmployee embObj = getLoginDetails(internalNotes.getCreatedBy());
					
					if(embObj != null){
						dto.setUserId((embObj.getEmpFirstName() != null ? (embObj.getEmpFirstName() + " / ") : "" ) + embObj.getLoginId() );
					}
				}
				resultList.add(dto);
			}
		}
		return resultList;
	}
	
	public List<InternalNotesHistory> getInternalNotesHistoryByClaimKey(Long claimKey) {
	
		List<InternalNotesHistory> internalNotesList = new ArrayList<InternalNotesHistory>();
		
		try{
			Query query = entityManager.createNamedQuery("InternalNotesHistory.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			internalNotesList = (List<InternalNotesHistory>) query.getResultList();
			if(internalNotesList !=  null){
				for (InternalNotesHistory internalNotes : internalNotesList) {
					entityManager.refresh(internalNotes);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return internalNotesList;
	}

	public Boolean getFVRStatusByRodKey(Long rodKey){
	
	List<Long> statusList = new ArrayList<Long>();

	statusList.add(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
	statusList.add(ReferenceTable.INITITATE_FVR);
	statusList.add(ReferenceTable.SKIPFVR);
	statusList.add(ReferenceTable.FVR_GRADING_STATUS);
	statusList.add(ReferenceTable.FVRCANCELLED);
	//IMSSUPPOR-24514
	statusList.add(ReferenceTable.FVR_NOT_REQUIRED);
	statusList.add(ReferenceTable.CORP_LEVEL_CLOSE);

	
	Query query = entityManager.createNamedQuery("FieldVisitRequest.findByRodKeyAndStatus");
	query.setParameter("rodKey", rodKey);
	query.setParameter("statusList", statusList);
	
	List<FieldVisitRequest> resultList = query.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		//return resultList.get(0);
		return true;
	}
	
	return false;

}
	
	public Boolean getFVRStatusByClaimKey(Long claimkey,String rodNumber){
		
		/*List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
		statusList.add(ReferenceTable.ASSIGNFVR);
		statusList.add(ReferenceTable.FVR_GRADING_STATUS);
		statusList.add(ReferenceTable.SKIPFVR);
		statusList.add(ReferenceTable.FVRCANCELLED);
		statusList.add(ReferenceTable.INITITATE_FVR);*/
		List<Long> stageList = new ArrayList<Long>();

		stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		

		Query query = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKeyAndStage");
		query.setParameter("claimKey", claimkey);
		query.setParameter("stageList", stageList);
		/*query.setParameter("statusList", statusList);	*/	
		
		List<FieldVisitRequest> resultList = query.getResultList();
		Boolean firstRod = Boolean.FALSE;
		if(rodNumber != null ) {
			String[] split = rodNumber.split("/");
			String string = split[split.length - 1];
			if(string != null && Integer.valueOf(string).equals(1)) {
				firstRod = Boolean.TRUE;
			} 
		}
		
		if((resultList != null && resultList.isEmpty())){
			return false;
		}
		else{
			for (FieldVisitRequest fieldVisitRequest : resultList) {
				if(firstRod){
					if((ReferenceTable.ZONAL_REVIEW_STAGE.equals(fieldVisitRequest.getStage().getKey()) ||
							ReferenceTable.CLAIM_REQUEST_STAGE.equals(fieldVisitRequest.getStage().getKey())) &&
							ReferenceTable.INITITATE_FVR.equals(fieldVisitRequest.getStatus().getKey())){
						return false;
					}
				}
			}
		}
		
		return true;

	}
	public Preauth searchByKey(Long key)
	{
		if(key != null){
			Query findByPreauthKey = entityManager.createNamedQuery(
					"Preauth.findByKey").setParameter("preauthKey", key);
			try {
				List<Preauth> preauth = (List<Preauth>) findByPreauthKey.getResultList();
				if(!preauth.isEmpty()){
					return preauth.get(0);
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
		
		//return null;
	}
	
public Boolean isInvsInitiated(Long claimKey) {
		
		Boolean isInvesInitiated = Boolean.FALSE;
		
		List<Long> statusList = new ArrayList<Long>();

		statusList.add(ReferenceTable.INVESTIGATION_REPLY_RECEIVED);
		statusList.add(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED);
		statusList.add(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		statusList.add(ReferenceTable.INVESTIGATION_GRADING);
		statusList.add(ReferenceTable.PARALLEL_INVES_CANCELLED);
		
		Query findAll = entityManager
				.createNamedQuery("Investigation.findLatestByClaimKeyAndStatus");
		findAll.setParameter("claimKey", claimKey);
		findAll.setParameter("statusList", statusList);
		
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		if(investigationList != null && !investigationList.isEmpty()){
			for (Investigation investigation : investigationList) {
				if(null != investigation.getTransactionFlag() && SHAConstants.TRANSACTION_FLAG_CASHLESS.equalsIgnoreCase(investigation.getTransactionFlag())){
					isInvesInitiated = Boolean.TRUE;
				}
			}
		}
		return isInvesInitiated;
	}

	public Boolean isFVRReplyReceived(Long claimkey){
		
		List<Long> statusList = new ArrayList<Long>();	
		Query query = entityManager.createNamedQuery("FieldVisitRequest.findFvrByClaimKey");
		query.setParameter("claimKey", claimkey);
		
		List<FieldVisitRequest> resultList = query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			if(null != resultList.get(0).getStatus().getKey() && 
					((ReferenceTable.ASSIGNFVR.equals(resultList.get(0).getStatus().getKey())) ||
					(ReferenceTable.INITITATE_FVR.equals(resultList.get(0).getStatus().getKey())))){
				return false;
			}
		}
		return true;
	}
	
	public List<String> getListFromMultiSelectComponent(Object object){
		
		String userRole = object.toString();
		if(!userRole.equals("[]")){
			String temp[] = userRole.split(",");
			List<String> listOfUserRole = new ArrayList<String>();
			listOfUserRole.clear();
			for (int i = 0; i < temp.length; i++) {
			//	String val = temp[i].replaceAll("\\[]", "");
				String val = temp[i].replaceAll("\\[", "").replaceAll("\\]", "");
				listOfUserRole.add(val.trim());
			}
			return listOfUserRole;
		}
		return null;
		
	}
	
	public void updateStageInformation(Preauth preauth,PreauthDTO bean){
		
		StageInformation stgInformation = new StageInformation();
		
		stgInformation.setIntimation(preauth.getIntimation());				
		stgInformation.setClaimType(preauth.getClaim().getClaimType());
		stgInformation.setStage(preauth.getStage());
		Status status = new Status();
		status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
		status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
		stgInformation.setPreauth(preauth);
		stgInformation.setStatus(status);
		stgInformation.setClaim(preauth.getClaim());
		String strUserName = bean.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		stgInformation.setCreatedBy(userNameForDB);
		//stgInformation.setCreatedBy(preauth.getModifiedBy());
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		stgInformation.setStatusRemarks(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
		
		entityManager.persist(stgInformation);
		entityManager.flush();
}
	
	public MasUserAutoAllocation searchUserType(String userName) {

		MasUserAutoAllocation doctor = null;

		userName = userName.toLowerCase();

		Query query = entityManager
				.createNamedQuery("MasUserAutoAllocation.findByDoctor");
		query.setParameter("doctorId", userName);

		List<MasUserAutoAllocation> doctorList = query.getResultList();
		if (doctorList != null && !doctorList.isEmpty()) {
			doctor = doctorList.get(0);
		}

		return doctor;

	}
	
	public void updateHoldRemarksForAutoAllocation(Long wkKey, String remarks){
		try{
			
		CashlessWorkFlow wkFlow = getWorkFlowValues(wkKey);	
			if(null != wkFlow){
				wkFlow.setPedRequesterRole(remarks);
				entityManager.merge(wkFlow);
				entityManager.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateEsclateIfCompleted(RawInvsDetails rawDetailsObj){
		if(null != rawDetailsObj){
			rawDetailsObj.setRecordType(SHAConstants.COMPLETED_CASE);
			entityManager.merge(rawDetailsObj);
			entityManager.flush();
		}
	}
	public void updateInvsReviewDetails(List<AssignedInvestigatiorDetails> invsReviewRemarksObj){
	    Boolean isExist = false;
		if(null != invsReviewRemarksObj && !invsReviewRemarksObj.isEmpty()){
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : invsReviewRemarksObj) {
				if(!isExist){
					isExist = true;
					entityManager.merge(assignedInvestigatiorDetails);
					entityManager.flush();
				}
			}
		}
	}
	
	public void updateCancelRemarks(Preauth preauth, String remarks,PreauthDTO bean){
		if(null != preauth){
			preauth.setAutoCancelRemarks(remarks);
			if(null != preauth.getKey()){
				entityManager.merge(preauth);
				entityManager.flush();
			}
		}
		AutoAllocationCancelRemarks autoAllocCancelRemarks = new AutoAllocationCancelRemarks();
		autoAllocCancelRemarks.setTransactionKey(bean.getKey());
		autoAllocCancelRemarks.setIntimationKey(null != bean.getNewIntimationDTO() ? bean.getNewIntimationDTO().getKey():null);
		autoAllocCancelRemarks.setClaimKey(bean.getClaimDTO().getKey());
		autoAllocCancelRemarks.setTransactionType(SHAConstants.CASHLESS_CHAR);
		autoAllocCancelRemarks.setCancelRemarks(remarks);
		autoAllocCancelRemarks.setCancelledBy(bean.getStrUserName());
		autoAllocCancelRemarks.setCancelledDate(new Timestamp(System.currentTimeMillis()));
		autoAllocCancelRemarks.setStageId(null !=bean.getStageKey()?bean.getStageKey():null);
		entityManager.persist(autoAllocCancelRemarks);
		entityManager.flush();
	}
	
	public CashlessWorkFlow getWorkFlowValues(Long wfKey){
		 Query wkQuery = entityManager.createNamedQuery("CashlessWorkFlow.findByKey");
		    wkQuery.setParameter("workFlowKey", wfKey);
		    List<CashlessWorkFlow> wk_list = (List<CashlessWorkFlow>)wkQuery.getResultList();
		    if(wk_list != null && ! wk_list.isEmpty()){
		    	return wk_list.get(0);
		    }
		    return null;
	}
	
	public Boolean getPreauthSubmitDecisionAlert(Long claimkey,PreauthDTO bean){
		Query query = entityManager.createNamedQuery("AutoClaimAmountDetails.findByCashlessKey");
		query.setParameter("claimKey", claimkey);	
		
		List<AutoClaimAmountDetails> resultList = query.getResultList();		
		if(resultList != null && ! resultList.isEmpty()){
			for (AutoClaimAmountDetails autoClaimAmountDetails : resultList) {
				if(null != bean.getStatusKey() && !autoClaimAmountDetails.getStatusId().equals(bean.getStatusKey())){
					return true;
				}
			}
			
		}
		
		return false;

	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public String getSTPALert(Long cashlessKey,Double approvedAmnt,Long decisionId) {

		Query query = entityManager
				.createNativeQuery("SELECT FUN_STP_COMPARE(?1,?2,?3) FROM DUAL");
		query.setParameter(1, cashlessKey);
		query.setParameter(2, approvedAmnt);	
		query.setParameter(3, decisionId);
		String isMandatory = (String) query.getSingleResult();
		if (null != isMandatory)
			return "Y";
		else
			return isMandatory;
	}
	
	public void updateSTPRemarks(Preauth preauth,PreauthDTO preauthDto){
		if(null != preauth){
			preauth.setStpRemarks(preauthDto.getStpRemarks());
			if(null != preauth.getKey()){
				entityManager.merge(preauth);
				entityManager.flush();
			}
		}
	}
	
	//R1256
	private void updateRejectReason(PreauthDTO preauthDto){
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		RejectReason rejReason = new RejectReason();
		rejReason.setTransactionKey(preauthDto.getKey());
		rejReason.setTransactionType("C");
		rejReason.setSubmittedDocFlag((preauthDto.getPreauthMedicalDecisionDetails().getChkSubmittedDoc())?"Y":"N");
		rejReason.setFieldVisitReportFlag((preauthDto.getPreauthMedicalDecisionDetails().getChkFieldVisitReport())?"Y":"N");
		rejReason.setInvestigationFlag((preauthDto.getPreauthMedicalDecisionDetails().getChkInvestigationReport())?"Y":"N");
		rejReason.setOthersFlag((preauthDto.getPreauthMedicalDecisionDetails().getChkOthers())?"Y":"N");
		rejReason.setOthersRemarks(preauthDto.getPreauthMedicalDecisionDetails().getTxtaOthersRemarks());
		rejReason.setCreatedBy(loginUserId);
		rejReason.setCreatedDate((new Timestamp(System.currentTimeMillis())));
		entityManager.persist(rejReason);
		entityManager.flush();
	}
	
	public List<NegotiationAmountDetails> getNegotiationDetails(Long intimationKey){
		Query query = entityManager.createNamedQuery("NegotiationAmountDetails.findByIntimationKeyDesc");
		query.setParameter("intmnKey", intimationKey);
		List<NegotiationAmountDetails> listNegDtls = (List<NegotiationAmountDetails>)query.getResultList();
		if(listNegDtls != null){
			return listNegDtls;
		}
		return null;
	}

	private DiagnosisDetailsTableDTO  setIcdChapterBlock(DiagnosisDetailsTableDTO pedValidationDto){
		
		if(pedValidationDto != null){
			Long icdCodeKey = pedValidationDto.getIcdCode().getId();
			IcdCode icdCodeValues = getIcdCode(icdCodeKey);
			if(icdCodeValues != null){
				SelectValue icdBlockDtls = new SelectValue();
				icdBlockDtls.setId(icdCodeValues.getIcdBlock().getKey());
				SelectValue icdChapterDtls = new SelectValue();
				icdChapterDtls.setId(icdCodeValues.getIcdChapter().getKey());
				
				pedValidationDto.setIcdBlock(icdBlockDtls);
				pedValidationDto.setIcdChapter(icdChapterDtls);
			}
		}
		return pedValidationDto;
		
	}
	public void insertNegotiationDetails(Preauth preauth,PreauthDTO bean){
		
		NegotiationDetails negotiationObj = getNegotiationPendingDetails(bean.getClaimDTO().getKey());
		if(null == negotiationObj){
			negotiationObj = new NegotiationDetails();
			negotiationObj.setIntimation(preauth.getIntimation());				
			negotiationObj.setClaim(preauth.getClaim());
			negotiationObj.setCashlessKey(preauth.getKey());
			negotiationObj.setNegotiationFlag(SHAConstants.YES_FLAG);
			negotiationObj.setNegotiationRemarks(bean.getPreauthMedicalDecisionDetails().getNegotiatePoints());
			negotiationObj.setNegotiationAmt(Double.valueOf(bean.getPreauthMedicalDecisionDetails().getAmtToNegotiated()));
			negotiationObj.setStageId(preauth.getStage());
			Status status = new Status();
			status.setKey(ReferenceTable.NEGOTIATION_INITIATED);
			status.setProcessValue(ReferenceTable.NEGOTIATION_INITIATED_VALUE);
			negotiationObj.setStatusId(status);
			negotiationObj.setCreatedBy(preauth.getModifiedBy());
			negotiationObj.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			
			entityManager.persist(negotiationObj);
			entityManager.flush();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public NegotiationDetails getNegotiationPending(Long claimKey) {
		Query query = entityManager.createNamedQuery("NegotiationDetails.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<NegotiationDetails> negatiationList = (List<NegotiationDetails>) query.getResultList();
		if(negatiationList !=  null && ! negatiationList.isEmpty()){
			for (NegotiationDetails negotiationDetails : negatiationList) {
				if(negotiationDetails.getStatusId() != null && (negotiationDetails.getStatusId().getKey().equals(ReferenceTable.NEGOTIATION_INITIATED))){
					return negotiationDetails;
				}
			}
		}
		return null;
	}
	
	public void updateNegotiationDetails(NegotiationDetails negotiationObj){
		
		negotiationObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(negotiationObj);
		entityManager.flush();
	}
	
	public MastersValue getMasterValue(String masterCode) {

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", masterCode);
		List<MastersValue> mastersValueList = query.getResultList();
		if(null != mastersValueList && !mastersValueList.isEmpty()){
			return mastersValueList.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NegotiationDetails getNegotiationByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("NegotiationDetails.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<NegotiationDetails> negatiationList = (List<NegotiationDetails>) query.getResultList();
		if(negatiationList !=  null && ! negatiationList.isEmpty()){
			return negatiationList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public NegotiationDetails getNegotiationPendingDetails(Long claimKey) {
		Query query = entityManager.createNamedQuery("NegotiationDetails.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<NegotiationDetails> negatiationList = (List<NegotiationDetails>) query.getResultList();
		if(negatiationList !=  null && ! negatiationList.isEmpty()){
			for (NegotiationDetails negotiationDetails : negatiationList) {
				if(negotiationDetails.getStatusId() != null && (negotiationDetails.getStatusId().getKey().equals(ReferenceTable.NEGOTIATION_INITIATED))){
					return negotiationDetails;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void cancellNegotiation(PreauthDTO bean) {
		NegotiationDetails negotiation= getNegotiationPending(bean.getClaimDTO().getKey());
	    if(negotiation != null){
	    	Status status = new Status();
	    	status.setKey(ReferenceTable.NEGOTIATION_CANCELLED);
	    	negotiation.setStatusId(status);
	    	negotiation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	    	negotiation.setModifiedBy(bean.getStrUserName());
	    	entityManager.merge(negotiation);
			entityManager.flush();
	    }
	
	}
	
	public List<Preauth> getPreauthListByClaimKeyInDesc(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescorder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			
			return preauthList;
		}
		return null;
	}
	
	public TataPolicy getTataPolicy(String policyNumber){
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return tataPolicy.get(0);
		    }
		    return null;
	}
	public void saveStandaloneNegotionAmt(ViewNegotiationDetailsDTO negDtls,String userName){
		NegotiationAmountDetails negAmtDtls = new NegotiationAmountDetails();
		negAmtDtls.setKey(null);
		negAmtDtls.setIntimationKey(negDtls.getIntimationKey());
		negAmtDtls.setIntimationNo(negDtls.getIntimationNo());
		negAmtDtls.setTransactionKey(negDtls.getLatestTransKey());
		negAmtDtls.setTransactionFlag(negDtls.getTransactionFlag());
		negAmtDtls.setActiveStatus(1l);
		negAmtDtls.setCpuKey(negDtls.getCpuKey());
		negAmtDtls.setNegotiatedAmt(negDtls.getNegotiatedAmt());
		negAmtDtls.setSavedAmt(negDtls.getSavedAmt());
		negAmtDtls.setClaimAppAmt(negDtls.getClaimApprovedAmt());
		Stage stage = new Stage();
		stage.setKey(ReferenceTable.STANDALONE_NEGOTIATION_UPDATE_STAGE_KEY);
		negAmtDtls.setStage(stage);
		Status status = new Status();
		status.setKey(ReferenceTable.STANDALONE_NEGOTIATION_UPDATE_STATUS_KEY);
		negAmtDtls.setStatus(status);
		negAmtDtls.setCreatedBy(userName);
		negAmtDtls.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		negAmtDtls.setModifiedBy(userName);
		negAmtDtls.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		entityManager.persist(negAmtDtls);
		entityManager.flush();
	}
	
	public boolean getScoringDetails(Long intimationKey){
		Query query = entityManager.createNamedQuery("HospitalScoring.findBySD");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("sdKey", 7L);
		query.setParameter("mdKey", 8L);
		List<HospitalScoring> listHSDtls = (List<HospitalScoring>)query.getResultList();
		if(listHSDtls != null && !listHSDtls.isEmpty()){
			HospitalScoring sdObj = listHSDtls.get(0);
			HospitalScoring mdObj = listHSDtls.get(1);
			if(sdObj.getGradeScore().equals("Y") && mdObj.getGradeScore().equals("N")){
				return true;
			}else if(sdObj.getGradeScore().equals("N") && mdObj.getGradeScore().equals("N")){
				return false;
			}else{
				System.out.println("else");
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean getCashlessEnhStatus(Long intimationKey){
		boolean validationStatus = false;
		Map<Long, Preauth> statusMap = new HashMap<Long, Preauth>();
		Query query = entityManager.createNamedQuery("Preauth.findByCashlessIntimationKey");
		query.setParameter("iKey", intimationKey);
		List<Preauth> listCLSDtls = (List<Preauth>)query.getResultList();
		if(listCLSDtls != null && !listCLSDtls.isEmpty()){
			for(Preauth rec : listCLSDtls){
				statusMap.put(rec.getStatus().getKey(), rec);
			}
			if(statusMap.get(ReferenceTable.ENHANCEMENT_REJECT_STATUS) != null){
				if(statusMap.get(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) != null){
					System.out.println("Having both the status");
					validationStatus = true;
				}else{
					System.out.println("Only reject status");
					validationStatus = false;
				}
			}else{
				System.out.println("Not rejected since creation, no validation req....");
				validationStatus = false;
			}
		}else{
			System.out.println("should not come here");
			validationStatus = false;
		}
		return validationStatus;
	}
	
	public boolean getReimbursementStatus(Long intimationKey, String argRodNo){
		System.out.println(intimationKey+"<------------>"+argRodNo);
		boolean validationStatus = false;
//		Map<Long, Reimbursement> statusMap = new HashMap<Long, Reimbursement>();
		Query query = entityManager.createNamedQuery("Reimbursement.findByIntimationRodNumber");
		query.setParameter("iKey", intimationKey);
		query.setParameter("rodNumber", argRodNo);
		List<Reimbursement> listReimDtls = (List<Reimbursement>)query.getResultList();
		if(listReimDtls != null && !listReimDtls.isEmpty()){
			for(Reimbursement rec : listReimDtls){
				if(rec.getReconsiderationRequest() != null && rec.getReconsiderationRequest().equals("Y")){
					validationStatus = true;
					break;
				}else{
					validationStatus = false;
				}
//				statusMap.put(rec.getStatus().getKey(), rec);
			}
			/*if((statusMap.get(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS) != null) || (statusMap.get(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS) != null)){
				System.out.println("Reject Approved / Settled....");
				validationStatus = true;
			}else{
				System.out.println("Not rejected since creation, no validation req....");
				validationStatus = false;
			}*/
		}else{
			System.out.println("should not come here");
			validationStatus = false;
		}
		return validationStatus;
	}
	
	public Insured getTopUpPolicy(Long policyKey){
		 Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
		 query.setParameter("policykey", policyKey);
		    List<Insured> topUpPolicy = (List<Insured>)query.getResultList();
		    if(topUpPolicy != null && ! topUpPolicy.isEmpty()){
		    	return topUpPolicy.get(0);
		    }
		    return null;
	}
	
	public Preauth getPreviousPreauthList(Long claimKey){
		try{
		 Query query = entityManager.createNamedQuery("Preauth.findPreauthApprovedDateByClaimKey");
		 query.setParameter("claimkey", claimKey);
		    List<Preauth> previousPreuathList = (List<Preauth>)query.getResultList();
		    if(previousPreuathList != null && ! previousPreuathList.isEmpty()){
		    	return previousPreuathList.get(0);
		    }
		    return null;
		}catch(Exception e){
			return null;
		}
	}
	
	//For Gmc Limit
		public List<MasAilmentLimit> getAilmentLimitGMCByPolicyKey(Long policyKey){
			Query claimLimitQuery = entityManager.createNamedQuery("MasAilmentLimit.findByPolicyKey");
			claimLimitQuery.setParameter("policyKey",policyKey);
		List<MasAilmentLimit> claimLimit = (List<MasAilmentLimit>) claimLimitQuery.getResultList();
			
			if(claimLimit != null && ! claimLimit.isEmpty()){
				
				return claimLimit;
			}
			return null;
		}
		public List<MasDeliveryExpLimit> getDeliveryLimitGMCByPolicyKey(Long policyKey){
			Query claimLimitQuery = entityManager.createNamedQuery("MasDeliveryExpLimit.findByPolicyKey");
			claimLimitQuery.setParameter("policyKey",policyKey);
		List<MasDeliveryExpLimit> claimLimit = (List<MasDeliveryExpLimit>) claimLimitQuery.getResultList();
			
			if(claimLimit != null && ! claimLimit.isEmpty()){
				
				return claimLimit;
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		public List<TreatingDoctorDetails> findTreatingDoctorDetailsByTransactionKey(Long transactionKey) {
			
			Query query = entityManager.createNamedQuery("TreatingDoctorDetails.findByTransactionKey");
			query.setParameter("transactionKey", transactionKey);
			List<TreatingDoctorDetails> resultList = (List<TreatingDoctorDetails>) query.getResultList();
			
			if(resultList != null && !resultList.isEmpty()) {
				for (TreatingDoctorDetails treatingDoctorDetails : resultList) {
					entityManager.refresh(treatingDoctorDetails);
				}
			}
			return resultList;
		}
		
		@SuppressWarnings("unchecked")
		public List<TreatingDoctorDetails> findTreatingDoctorDetailsByClaimKey(Long claimKey) {
			Query query = entityManager
					.createNamedQuery("TreatingDoctorDetails.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<TreatingDoctorDetails> resultList = (List<TreatingDoctorDetails>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				for (TreatingDoctorDetails treatingDoctorDetails : resultList) {
					entityManager.refresh(treatingDoctorDetails);
				}
				
			}
			return resultList;
		}
		
		@SuppressWarnings("unchecked")
		public List<TreatingDoctorDetails> findTreatingDoctorDetails() {
			Query query = entityManager
					.createNamedQuery("TreatingDoctorDetails.findAll");
			List<TreatingDoctorDetails> resultList = (List<TreatingDoctorDetails>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				for (TreatingDoctorDetails treatingDoctorDetails : resultList) {
					entityManager.refresh(treatingDoctorDetails);
				}		
			}
			return resultList;
		}
		
		@SuppressWarnings("unchecked")
		public List<ViewPccRemarksDTO> getPccRemarksDTOHistoryDetails(Long claimKey){
			List<ViewPccRemarksDTO> resultList = new ArrayList<ViewPccRemarksDTO>();
			List<PccRemarks> PccHistoryByClaimKey = getPccRemarksHistoryByClaimKey(claimKey);
			if(PccHistoryByClaimKey != null){
				ViewPccRemarksDTO dto = null;
				for (PccRemarks remarks : PccHistoryByClaimKey) {
					dto = new ViewPccRemarksDTO();
					dto.setStrNoteDate(remarks.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm aa").format(remarks.getCreatedDate()) : "");
					if(remarks.getStage() !=null && remarks.getStage().getStageName()!=null){
						dto.setTransactionType(remarks.getStage().getStageName());
					}	
					dto.setRemarks(remarks.getPccRemarks());
					if(remarks.getCreatedBy() != null){
						TmpEmployee embObj = getLoginDetails(remarks.getCreatedBy());
						if(embObj != null){
							dto.setUserId((embObj.getEmpFirstName() != null ? (embObj.getEmpFirstName() + " / ") : "" ) + embObj.getLoginId() );
						}
					}
					resultList.add(dto);
				}
			}
			return resultList;
		}
				
		public List<PccRemarks> getPccRemarksHistoryByClaimKey(Long claimKey) {
			
			List<PccRemarks> pccRemarks = new ArrayList<PccRemarks>();
			
			try{
				Query query = entityManager.createNamedQuery("PccRemarks.findByClaimKey");
				query.setParameter("claimKey", claimKey);
				pccRemarks = (List<PccRemarks>) query.getResultList();
				if(pccRemarks !=  null){
					for (PccRemarks pccRemark : pccRemarks) {
						entityManager.refresh(pccRemark);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return pccRemarks;
		}

		public PccRemarks getEscalateRemarks(Long intimationKey) {
			Query pccRemarksQuery = entityManager.createNamedQuery("PccRemarks.findByKey");
			pccRemarksQuery.setParameter("intimationKey", intimationKey);
			List<PccRemarks>	resultList = pccRemarksQuery.getResultList();
			if (null != resultList && 0 != resultList.size())
			{
				return resultList.get(0);
			}
			else
			{
				return null;
			}
		}
		
		public Preauth getlastPreauthbyintimationkey(Long intimationKey) {
			Query query = entityManager.createNamedQuery("Preauth.findByStatusAndIntimationDesc");
			query.setParameter("intimationKey", intimationKey);
			@SuppressWarnings("unchecked")
			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
			if(singleResult != null && ! singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				return singleResult.get(0);
			}
			return null;
		}
	
		@SuppressWarnings("unchecked")
		public List<ViewHospSPOCDetailsDTO> getHospitalSPOCDetails(Long key){
			List<ViewHospSPOCDetailsDTO> resultList = new ArrayList<ViewHospSPOCDetailsDTO>();
			Hospitals getHospitalSPOCList = getHospitalSPOCDetailsByKey(key);
			ViewHospSPOCDetailsDTO hospitalSPOC = new ViewHospSPOCDetailsDTO();
			hospitalSPOC.setHospitalSPOC("Medical");
			resultList.add(hospitalSPOC);
			ViewHospSPOCDetailsDTO hospitalSPOC1 = new ViewHospSPOCDetailsDTO();
			hospitalSPOC1.setHospitalSPOC("Non-Medical");
			resultList.add(hospitalSPOC1);
			if(getHospitalSPOCList != null){
				for (ViewHospSPOCDetailsDTO spocDetails : resultList) {
						if(spocDetails.getHospitalSPOC() != null && spocDetails.getHospitalSPOC().equalsIgnoreCase("Medical")){
								spocDetails.setLandLineNumber(getHospitalSPOCList.getMedicalSpocLL());
								spocDetails.setMobileNumber(getHospitalSPOCList.getMedicalSpocMobileNo());
								spocDetails.setName(getHospitalSPOCList.getMedicalSpocName());
								spocDetails.setEmailId(getHospitalSPOCList.getMedicalSpocEmail());
						} 
						if(spocDetails.getHospitalSPOC() != null && spocDetails.getHospitalSPOC().equalsIgnoreCase("Non-Medical")){
								spocDetails.setLandLineNumber(getHospitalSPOCList.getNonMedicalSpocLL());
								spocDetails.setMobileNumber(getHospitalSPOCList.getNonMedicalSpocMobileNo());
								spocDetails.setName(getHospitalSPOCList.getNonMedicalSpocName());
								spocDetails.setEmailId(getHospitalSPOCList.getNonMedicalSpocEmail());
						}
					}
				
				}
			return resultList;
		}
		
		public Hospitals getHospitalSPOCDetailsByKey(Long key){
			
			Query query = entityManager.createNamedQuery("Hospitals.findByKey");
			query.setParameter("key", key);
			
			Hospitals resultList = (Hospitals) query.getSingleResult();
			
			if(resultList != null){
				return resultList;
			}
			
			return null;
			
		}
		
		public Long getHigestClaimamtbyintimationkey(Long intimationKey) {
			Query query = entityManager.createNamedQuery("Preauth.findByIntimationByAmtDesc");
			query.setParameter("intimationKey", intimationKey);
			@SuppressWarnings("unchecked")
			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
			if(singleResult != null && ! singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				return singleResult.get(0).getClaimedAmt().longValue();
			}
			return 0L;
		}

		public List<NegotiationAmountDetailsDTO> getNegotiationAmountDetailsByIntimationKey(Long intimationKey) {

			List<NegotiationAmountDetails> negotiationAmountDetails=null;
			List<NegotiationAmountDetailsDTO> negotiationAmountDetailsDTOs =null;

			try{
				Query query = entityManager.createNamedQuery("NegotiationAmountDetails.findByIntimationKeyDesc");
				query.setParameter("intmnKey", intimationKey);
				negotiationAmountDetails = (List<NegotiationAmountDetails>) query.getResultList();
				if(negotiationAmountDetails !=  null
						&& !negotiationAmountDetails.isEmpty()){
					PreauthMapper prauthMapper = PreauthMapper.getInstance();
					negotiationAmountDetailsDTOs = prauthMapper.getnegotiationDetailsMapDTOList(negotiationAmountDetails);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return negotiationAmountDetailsDTOs;
		}
		
		public NegotiationAmountDetailsDTO getLatestNegotiationDetails(Long intimationKey){
			
			List<NegotiationAmountDetailsDTO> negotiationAmountDetailsDTOs =getNegotiationAmountDetailsByIntimationKey(intimationKey);
			if(negotiationAmountDetailsDTOs !=  null
					&& !negotiationAmountDetailsDTOs.isEmpty()){
				return negotiationAmountDetailsDTOs.get(0);
			}
			return null;
		}
		
		public NegotiationAmountDetails getNegotiationAmountDetailsByTransactionKey(Long transactionKey) {
			List<NegotiationAmountDetails> negotiationAmountDetails=null;
			try{
				Query query = entityManager.createNamedQuery("NegotiationAmountDetails.findByTransactionKey");
				query.setParameter("transactionKey", transactionKey);
				negotiationAmountDetails = (List<NegotiationAmountDetails>) query.getResultList();
				if(negotiationAmountDetails !=  null
						&& !negotiationAmountDetails.isEmpty()){
					return negotiationAmountDetails.get(0);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		public List<Insured> getInsuredListForGMC(Long dependentRiskID){
			Query query = entityManager.createNamedQuery("Insured.findByInsuredDependentRiskID");
			query = query.setParameter("dependentRiskId", dependentRiskID);
			List<Insured> insuredList = new ArrayList<Insured>();
			insuredList = query.getResultList();
			
			return insuredList;
		}

		public OPHealthCheckup getOpHealthByClaimKey(Long claimKey) {
			Query query = entityManager.createNamedQuery("OPHealthCheckup.findByClaim");
			query.setParameter("claimKey", claimKey);
			List<OPHealthCheckup> singleResult =  query.getResultList();
			if(singleResult != null && !singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				return singleResult.get(0);
			}
			return null;
		}

		public MasUser getUserName(String initiatorId)
		{
			List<MasUser> tmpEmployeeDetails;
			Query findByTransactionKey = entityManager.createNamedQuery("MasUser.getById")
					.setParameter("userId", initiatorId.toLowerCase());
			try{
				tmpEmployeeDetails =(List<MasUser>) findByTransactionKey.getResultList();
				if(tmpEmployeeDetails != null && ! tmpEmployeeDetails.isEmpty()){
					return tmpEmployeeDetails.get(0);
				}
				return null;
			}
			catch(Exception e)
			{
				return null;
			}								
		}

		public void updateStageInformationHoldByPassFLP(Claim claim,PreauthDTO bean){

			StageInformation stgInformation = new StageInformation();

			stgInformation.setIntimation(claim.getIntimation());				
			stgInformation.setClaimType(claim.getClaimType());
			Stage stage = getStageByKey(ReferenceTable.PROCESS_PREAUTH);
			stgInformation.setStage(stage);
			Status status = new Status();
			status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
			stgInformation.setStatus(status);
			stgInformation.setClaim(claim);
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			stgInformation.setCreatedBy(userNameForDB);
			//stgInformation.setCreatedBy(preauth.getModifiedBy());
			stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			stgInformation.setStatusRemarks(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());

			entityManager.persist(stgInformation);
			entityManager.flush();
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
				if(userMap.get("cashlessKey") != null){
					claimsSet.claim("cashlessKey", userMap.get("cashlessKey"));
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

		public void deleteImplantDetails(Long implantKey,String userName){

			Query query = entityManager.createNamedQuery("ImplantDetails.findByKey");
			query.setParameter("primarykey", implantKey);
			List<ImplantDetails> singleResult =  query.getResultList();
			if(singleResult != null && !singleResult.isEmpty()) {
				ImplantDetails details = singleResult.get(0);
				details.setActiveStatus(0L);
				details.setModifiedBy(userName);
				details.setModifiedDate(new Date());
				entityManager.merge(details);
				entityManager.flush();
				entityManager.clear();
			}
		}
		
		@SuppressWarnings("unchecked")
		public List<ImplantDetails> findImplantDetailsByClaimKey(Long claimKey) {
			Query query = entityManager.createNamedQuery("ImplantDetails.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<ImplantDetails> resultList = (List<ImplantDetails>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				for (ImplantDetails implantDetails : resultList) {
					entityManager.refresh(implantDetails);
				}
			}
			return resultList;
		}
		
		@SuppressWarnings({ "unchecked", "unused" })
		public BeanItemContainer<SelectValue> getAllSpecialityType() {
			Session session = (Session) entityManager.getDelegate();
			@SuppressWarnings("unchecked")
			List<SelectValue> selectValuesList = session.createCriteria(SpecialityType.class)
											.add(Restrictions.eq("activeStatus", (long) 1))
											.addOrder(Order.asc("value"))
											.setProjection(Projections.projectionList()
															.add(Projections.property("key"), "id")
															.add(Projections.property("value"), "value"))
											.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			selectValueContainer.addAll(selectValuesList);
			//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
			System.out.println("---------------- Second Approach End current time " + new Date());	
			return selectValueContainer;
		}

		public void submitICACProcess(PreauthDTO preauthDTO){
			IcacRequest icacRequest = new IcacRequest();
			if(null != preauthDTO){
				String strUserName = preauthDTO.getStrUserName();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);

				Claim currentClaim = searchByClaimKeyValues(preauthDTO.getClaimKey());
				Preauth preauth =searchByKey(preauthDTO.getKey());

				if(preauthDTO.getKey() != null){
					if(preauth != null && null != preauth.getKey()){
						preauth.setIcacFlag(SHAConstants.YES_FLAG);
						entityManager.merge(preauth);
						entityManager.flush();
						entityManager.clear();
					}
				}

				if(preauthDTO.getClaimKey() != null) {	
					if(currentClaim != null && currentClaim.getKey()!= null ){
						currentClaim.setIcacFlag(SHAConstants.YES_FLAG);
						entityManager.merge(currentClaim);
						entityManager.flush();
						entityManager.clear();
					}
				}
				
				if(icacRequest != null){
					icacRequest.setIntimationNum(preauthDTO.getNewIntimationDTO().getIntimationId());
					icacRequest.setPolicyNumber(currentClaim.getIntimation().getPolicyNumber());
					Status status = null;
					Stage stage = null;
					String srcStage = null;
					if(preauthDTO.getIcacStatusId() !=null 
							&& preauthDTO.getIcacStatusId().equals(ReferenceTable.PROCESS_PREAUTH)){
						status = entityManager.find(Status.class,
								ReferenceTable.PREAUTH_ICAC_REQUEST_INITIATED);
						stage = entityManager.find(Stage.class,
								ReferenceTable.PROCESS_PREAUTH);
						srcStage = SHAConstants.PRE_AUTH;
					}else if(preauthDTO.getIcacStatusId() !=null 
							&& preauthDTO.getIcacStatusId().equals(ReferenceTable.ENHANCEMENT_STAGE)){
						 status = entityManager.find(Status.class,
								ReferenceTable.ENHANCEMENT_ICAC_REQUEST_INITIATED);
						 stage = entityManager.find(Stage.class,
								ReferenceTable.ENHANCEMENT_STAGE);
						 srcStage = SHAConstants.PRE_AUTH_ENHANCEMENT;
					}
					if(status != null){
						icacRequest.setClaimStage(stage);
					}
					if(stage != null){
						icacRequest.setStatusId(status);
					}
					if(srcStage != null && !srcStage.isEmpty()){
						icacRequest.setIcacRequestSource(srcStage);
					}
					icacRequest.setClaimType(currentClaim.getClaimType());
					icacRequest.setRequestRemark(preauthDTO.getIcacProcessRemark());
					icacRequest.setFinalDecFlag("N");
					icacRequest.setCreatedBy(userNameForDB);
					icacRequest.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.persist(icacRequest);
					entityManager.flush();

				}

			}
		}
		
		public Claim searchByClaimKeyValues(Long a_key) {
			Claim find = entityManager.find(Claim.class, a_key);
			entityManager.refresh(find);
			return find;
		}
		
		@SuppressWarnings("unchecked")
		public BeanItemContainer<SelectValue> getPCCCategorys() {

			Session session = (Session) entityManager.getDelegate();		
			List<SelectValue> selectValuesList = session.createCriteria(PCCCategory.class)
					.add(Restrictions.eq("activeStatus", "Y"))
					.addOrder(Order.asc("pccDesc"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("key"), "id")
							.add(Projections.property("pccDesc"), "value"))
							.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			selectValueContainer.addAll(selectValuesList);
			System.out.println("---------------- PCCCategorys" + new Date());
			return selectValueContainer;	
		}
		
		@SuppressWarnings("unchecked")
		public BeanItemContainer<SelectValue> getPCCSubCategorys(Long pccCategorykey) {

			Session session = (Session) entityManager.getDelegate();		
			List<SelectValue> selectValuesList = session.createCriteria(PCCSubCategory.class)
					.add(Restrictions.eq("activeStatus", "Y"))
					.add(Restrictions.eq("pccCategorykey",pccCategorykey))
					.addOrder(Order.asc("pccSubDesc"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("key"), "id")
							.add(Projections.property("pccSubDesc"), "value"))
							.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			selectValueContainer.addAll(selectValuesList);
			System.out.println("---------------- PCCSubCategory" + new Date());
			return selectValueContainer;	
		}
		
		@SuppressWarnings("unchecked")
		public BeanItemContainer<SelectValue> getPCCSubCategorystwo(Long pccSubCategorykey) {

			BeanItemContainer<SelectValue> selectValueContainer = null;
			Session session = (Session) entityManager.getDelegate();		
			List<SelectValue> selectValuesList = session.createCriteria(PCCSubCategoryTwo.class)
					.add(Restrictions.eq("activeStatus", "Y"))
					.add(Restrictions.eq("pccSubCategorykey",pccSubCategorykey))
					.addOrder(Order.asc("pccSubDesc"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("key"), "id")
							.add(Projections.property("pccSubDesc"), "value"))
							.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
			if(selectValuesList !=null && !selectValuesList.isEmpty()){
				selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				selectValueContainer.addAll(selectValuesList);
				System.out.println("---------------- PCCSubCategoryTwo" + new Date());
			}		
			return selectValueContainer;	
		}
		
		@SuppressWarnings("unchecked")
		public PCCRequest findByintimationNo(String intimationNo) {
			Query query = entityManager.createNamedQuery("PCCRequest.findByintimationNo");
			query.setParameter("intimationNo", intimationNo);
			List<PCCRequest> resultList = (List<PCCRequest>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				entityManager.refresh(resultList.get(0));
				return resultList.get(0);
			}
			return null;
		}
		
		public void savePCCRequest(PreauthDTO bean,PccDetailsTableDTO pccDetailsTableDTO,String screenName){
			
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			
			PCCRequest pccRequest= new PCCRequest();
			Status status = new Status();
			PCCCategory pccCategory = new PCCCategory();
			PCCSubCategory pccSubCategory = new PCCSubCategory();
			PCCSubCategoryTwo subcategoryTwo = new PCCSubCategoryTwo();
			Intimation intimation = new Intimation();
			MastersValue mastersValue = new MastersValue();
		
			pccCategory.setKey(pccDetailsTableDTO.getPccCategory().getId());
			pccSubCategory.setKey(pccDetailsTableDTO.getPccSubCategory1().getId());
			if(pccDetailsTableDTO.getPccSubCategory2() !=null && pccDetailsTableDTO.getPccSubCategory2().getId() !=null){
				subcategoryTwo.setKey(pccDetailsTableDTO.getPccSubCategory2().getId());
				pccRequest.setSubCategoryTwo(subcategoryTwo);
			}
			if(pccDetailsTableDTO.getPccCategory().getId().equals(SHAConstants.PCC_CATAGORY_ANH)){
				status.setKey(SHAConstants.PCC_COORDINATOR_INITIATED_STATUS);
				pccRequest.setStatus(status);
			}else if(pccDetailsTableDTO.getPccCategory().getId().equals(SHAConstants.PCC_CATAGORY_OTHERS)){
				status.setKey(SHAConstants.PCC_PROCESSOR_INITIATED_STATUS);
				pccRequest.setStatus(status);
			}
			
			if(screenName !=null && screenName.equals("Process Pre-Auth")){
				mastersValue.setKey(SHAConstants.PCC_PROCESS_PRE_AUTH_SOURCE);
				pccRequest.setPccSource(mastersValue);
			}else if(screenName !=null && screenName.equals("Process Enhancements")){
				mastersValue.setKey(SHAConstants.PCC_PROCESS_ENHANCEMENT_SOURCE);
				pccRequest.setPccSource(mastersValue);
			}
			
			intimation.setKey(bean.getNewIntimationDTO().getKey());
			pccRequest.setIntimationNo(bean.getNewIntimationDTO().getIntimationId());
			pccRequest.setPolicyNo(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			pccRequest.setClaimType(bean.getClaimDTO().getClaimTypeValue());
			pccRequest.setClaimStage(bean.getClaimDTO().getStageName());
			pccRequest.setPccDoctorRemarks(pccDetailsTableDTO.getEscalatePccRemarks());
			pccRequest.setPccCategory(pccCategory);
			pccRequest.setSubCategory(pccSubCategory);		
			pccRequest.setInitiateDate(new Timestamp(System.currentTimeMillis()));
			pccRequest.setInitiateBy(userNameForDB);
			pccRequest.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			pccRequest.setCreatedBy(userNameForDB);	
			pccRequest.setIntimation(intimation);			
			entityManager.persist(pccRequest);
			entityManager.flush();
			entityManager.clear();
			
		}
		
		@SuppressWarnings("unchecked")
		public Reimbursement getRembiObj(Long claimKey) {
			Reimbursement resultClaim = null;
			if (claimKey != null) {

				Query query = entityManager.createNamedQuery(
						"Reimbursement.findByClaimKey").setParameter("claimKey",
						claimKey);
				List<Reimbursement> claimResultList = query.getResultList();
				try {
					if(null != claimResultList && !claimResultList.isEmpty())
					{
						resultClaim = claimResultList.get(0);
						entityManager.refresh(resultClaim);
						return resultClaim;
						//resultClaim = (Claim) findByIntimationNum.getSingleResult();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return resultClaim;
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
		
		public void saveHospitalisationPropDedValue(Hospitalisation hospValue){
			if(hospValue.getKey() != null){
			entityManager.merge(hospValue);
			entityManager.flush();
			}
		}
		
		public void updateStageInformationForBilling(Reimbursement preauth,PreauthDTO bean){
			
			StageInformation stgInformation = new StageInformation();
			
			stgInformation.setIntimation(preauth.getClaim().getIntimation());				
			stgInformation.setClaimType(preauth.getClaim().getClaimType());
			Stage stage = new Stage();
			if(bean.getStageKey() !=null && bean.getStageKey().equals(ReferenceTable.BILLING_STAGE)){
				stage.setKey(ReferenceTable.BILLING_STAGE);
			}else{
				stage.setKey(ReferenceTable.FINANCIAL_STAGE);
			}			
			stgInformation.setStage(stage);
			Status status = new Status();
			status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
			stgInformation.setReimbursement(preauth);
			stgInformation.setStatus(status);
			stgInformation.setClaim(preauth.getClaim());
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			stgInformation.setCreatedBy(userNameForDB);
			//stgInformation.setCreatedBy(preauth.getModifiedBy());
			stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			stgInformation.setStatusRemarks(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
			
			entityManager.persist(stgInformation);
			entityManager.flush();
	}
		// Function to merge two arrays of same type 
	    public static <T> Object[] concatenate(T[] a, T[] b) 
	    { 
	        Object[] n=new Object[a.length + b.length]; 
	          
	        System.arraycopy(a, 0, n, 0, a.length); 
	          
	        System.arraycopy(b, 0, n, a.length, b.length); 
	          
	        return n; 
	    }
	    
public void updateStageInformationForMAautoAllocation(Reimbursement preauth,PreauthDTO bean){
			
			StageInformation stgInformation = new StageInformation();
			
			stgInformation.setIntimation(preauth.getClaim().getIntimation());				
			stgInformation.setClaimType(preauth.getClaim().getClaimType());
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			stgInformation.setStage(stage);
			Status status = new Status();
			status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
			stgInformation.setReimbursement(preauth);
			stgInformation.setStatus(status);
			stgInformation.setClaim(preauth.getClaim());
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			stgInformation.setCreatedBy(userNameForDB);
			//stgInformation.setCreatedBy(preauth.getModifiedBy());
			stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			stgInformation.setStatusRemarks(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
			
			entityManager.persist(stgInformation);
			entityManager.flush();
	}
public String getProvisionBehanviourChangesFlag(PreauthDTO bean){
	String provisionBehaviourFlag = null;
	if(bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getIntimationId() != null  && bean.getNewIntimationDTO().getPolicy() != null ){
		provisionBehaviourFlag = calcService.ProvisionBehaviourValueFlag(bean.getNewIntimationDTO().getIntimationId(),bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
				bean.getNewIntimationDTO().getPolicy().getKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId() );
	}
	return provisionBehaviourFlag;
}

public String getUnlockProvisionAmtFlag(PreauthDTO bean){
	String provisionUnlockFlag = null;
	String strUserName = bean.getStrUserName();
	String userID = SHAUtils.getUserNameForDB(strUserName);
	if(bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getIntimationId() != null && bean.getNewIntimationDTO().getPolicy() != null){
		provisionUnlockFlag = calcService.getUnlockProvisionFlag(bean.getNewIntimationDTO().getIntimationId(),bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
				bean.getNewIntimationDTO().getPolicy().getKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(),userID);
	}
	return provisionUnlockFlag;
}

public BeanItemContainer<SelectValue> getProcedureCodeList() {
	
	
	Session session = (Session) entityManager.getDelegate();
	@SuppressWarnings("unchecked")
	List<SelectValue> selectValuesList = session.createCriteria(ProcedureMaster.class)
									.add(Restrictions.eq("activeStatus", (long) 1))
									.addOrder(Order.asc("procedureCode"))
									.setProjection(Projections.projectionList()
									.add(Projections.property("key"), "id")
									.add(Projections.property("procedureCode"), "value"))
									.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
	BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
			SelectValue.class);
	for (SelectValue selectValue : selectValuesList) {
		String value = selectValue.getValue();
		
		if(value != null ){
			String tmpValue = selectValue.getValue();
			value = value   ;
			selectValue.setValue(value);
		}
	}
	selectValueContainer.addAll(selectValuesList);
	System.out.println("---------------- Second Approach End current time " + new Date());
	return selectValueContainer;	

}
	
}
