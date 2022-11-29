package com.shaic.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.opsearch.OPSearchIntimationDTO;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.policy.search.ui.premia.PremPolicySearchDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.preauth.PortablityPolicy;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.restservices.bancs.claimprovision.PolicySummaryResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class PolicyService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	InsuredService insured;

	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	

	//@Inject
	//private NewIntimationDto newIntimationDTO;

	//@Inject
	//private ClaimService claimService;

	//@Inject
	//private PreauthService preAuthService;

	//@Inject
	//private NewIntimationService newIntimationService;

	public PolicyService() {
		super();
	}

	public Policy getPolicyByKey(Long policyKey) {

		if (policyKey != null) {
			return entityManager.find(Policy.class, policyKey);
		}
		return null;

	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		if(query.getResultList().size()!=0){
		Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}
	
	public Policy getKeyByPolicyNumber(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findKeyByPolicyNo");
		query.setParameter("policyNumber", policyNumber);
		if(query.getResultList().size()!=0){
			Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}
	
	public Policy getViewTmpClaim(){
		Query query = entityManager.createNamedQuery("ViewTmpClaim.findAll");
		if(query.getResultList().size()!=0){
		      System.out.println("Claim count ********"+query.getResultList().size());
		}
		return null;
	}
	
	public Policy getPolicyByRenewalPolicyNumber(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByRenewalPolicyNumber");
		query.setParameter("renewalPolicyNumber", policyNumber);
		if(query.getResultList().size()!=0){
		Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}

//	@SuppressWarnings({ "unchecked", "unused" })
//	public BeanItemContainer<TmpPolicy> getSearchPolicyDetails() {
//		Query findAll = entityManager.createNamedQuery("TmpPolicy.findAll");
//		List<TmpPolicy> policyList = (List<TmpPolicy>) findAll.getResultList();
//		// BeanContainer<String, TmpPolicy> beans = new BeanContainer<String,
//		// TmpPolicy>(TmpPolicy.class);
//		// beans.setBeanIdProperty("polSysId");
//		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
//				TmpPolicy.class);
//		policyListContainer.addAll(policyList);
//		return policyListContainer;
//	}

//	public List<TmpInsured> getByHealthCard(String healthCard) {
//		Query findAll = entityManager.createNamedQuery(
//				"TmpInsured.findByHealthcard").setParameter("healthCard",
//				"%" + healthCard + "%");
//		List<TmpInsured> policyList = (List<TmpInsured>) findAll.getResultList();
//		return policyList;
//	}
//	
//	public List<TmpInsured> getByRMN(Long mobileNumber) {
//		List<TmpInsured> policyList = null;
//		if(mobileNumber != null){
//		Query findAll = entityManager.createNamedQuery(
//				"TmpInsured.findByRmn").setParameter("rmn",
//				 mobileNumber );
//		policyList = (List<TmpInsured>) findAll.getResultList();
//		}
//		return policyList;
//	}
//	
//
//	public List<TmpPolicy> getByProposerName(String proposerName) {
//		Query findAll = entityManager.createNamedQuery(
//				"TmpPolicy.findByProposerName").setParameter("proposerName",
//				"%" + proposerName + "%");
//		List<TmpPolicy> policyList = (List<TmpPolicy>) findAll.getResultList();
//		return policyList;
//	}
//
//	public BeanItemContainer<TmpPolicy> getByInsuredName(String insuredName) {
//		Query findAll = entityManager.createNamedQuery(
//				"TmpInsured.findByInsuredName").setParameter("insuredName",
//				"%" + insuredName + "%");
//		List<TmpInsured> insuredList = (List<TmpInsured>) findAll
//				.getResultList();
//		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
//				TmpPolicy.class);
//		List<TmpPolicy> policyList = new ArrayList<TmpPolicy>();
//
//		for (TmpInsured tmpInsured : insuredList) {
//			policyList.add(tmpInsured.getPolicy());
//		}
//		policyListContainer.addAll(policyList);
//		return policyListContainer;
//	}
//
//	public List<TmpPolicy> getByInsuredNameAndDob(String insuredName, Date dob, String productType, String mobileNo) {
//		List<TmpPolicy> policyList = new ArrayList<TmpPolicy>();
//		List<TmpInsured> insuredList = new ArrayList<TmpInsured>();
//		if(mobileNo == null){
//		@SuppressWarnings("unused")
//		Query findAll = entityManager
//				.createNamedQuery("TmpInsured.findByInsuredNameAndDOB")
//				.setParameter("insuredName",
//						"%" + insuredName.toLowerCase() + "%")
//				.setParameter("dob", dob);
//		insuredList = (List<TmpInsured>) findAll.getResultList();
//	}
//	else{
//		try{
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<TmpInsured> createQuery = cb
//				.createQuery(TmpInsured.class);
//		Root<TmpInsured> insuredRoot = createQuery.from(TmpInsured.class);
//
//		createQuery.select(insuredRoot);
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		List<TmpInsured> insured = null;
//		
//		if(insuredName != null &&  !insuredName.equals("") && dob != null){
//		
//		if (insuredName != null) {
//			Expression<String> expression = cb.upper(insuredRoot
//					.<String> get("insuredName"));
//			predicates.add(cb.like(expression, "%" + StringUtils.trim(insuredName).toUpperCase() + "%"));
//		}
//		if (dob != null) {
//			Expression<Date> expression = insuredRoot
//					.<Date> get("insuredDateOfBirth");
//			predicates.add(cb.equal(expression, dob));
//		}
//		}
//		if (mobileNo != null) {
//			mobileNo = StringUtils.trim(mobileNo);
//			Expression<String> expression = insuredRoot
//					.<String> get("registerdMobileNumber");
//			predicates.add(cb.like(expression, "%"+mobileNo+"%"));
//		}
//
//		if (!predicates.isEmpty()) {
//			createQuery
//					.where(cb.and(predicates.toArray(new Predicate[] {})));
//			Query filterQuery = entityManager.createQuery(createQuery);
//			insuredList = (List<TmpInsured>) filterQuery.getResultList();
//
//		}
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	}
//		
////		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
////				TmpPolicy.class);
//
//		if(!insuredList.isEmpty()){
//			for (TmpInsured tmpInsured : insuredList) {
//				TmpPolicy policy;
//				if(productType == null){
//					policy = getTmpPolicy(tmpInsured.getPolicyNo());	
//					
//				}
//				else{
//					policy = getPolicyByProductType(tmpInsured.getPolicyNo(),productType);
//				}
//			
//			
//			if(policy != null){
//				if (!policyList.contains(policy)) {
//					policy.setPolSumInsured(tmpInsured.getInsuredSumInsured());
//					policyList.add(policy);
//				}
//			 }
//			}
//		}
//		return policyList;
//	}
//
//	@SuppressWarnings("unchecked")
//	public BeanItemContainer<TmpPolicy> getByProposerNameAndDob(
//			String proposerName, Date dob, String productType,String mobile)
//			throws ParseException {
//		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
//				TmpPolicy.class);
//		List<TmpPolicy> policyList = new ArrayList<TmpPolicy>();
//		try {
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TmpPolicy> createQuery = cb
//					.createQuery(TmpPolicy.class);
//			Root<TmpPolicy> policyRoot = createQuery.from(TmpPolicy.class);
//
//			createQuery.select(policyRoot);
//			List<Predicate> predicates = new ArrayList<Predicate>();
//			List<TmpInsured> insured = null;
//
//			if (proposerName != null) {
//				proposerName = StringUtils.trim(proposerName.toUpperCase());
//				Expression<String> expression = policyRoot
//						.<String> get("polAssrName");
//				predicates.add(cb.like(cb.upper(expression), "%" + proposerName + "%"));
//			}
//			if (dob != null) {
//				Expression<Date> expression = policyRoot
//						.<Date> get("polAssrDOB");
//				predicates.add(cb.equal(expression, dob));
//			}
//			if (productType != null && StringUtils.trim(productType) != null) {
//				Expression<String> expression = policyRoot
//						.<String> get("productType");
//				predicates.add(cb.like(cb.upper(expression), "%" + productType.toUpperCase() + "%"));
//			}
//			if (mobile != null) {
//				Long mobileNo = Long.parseLong(StringUtils.trim(mobile));
//				Expression<String> expression = policyRoot
//						.<String> get("registerdMobileNumber");
//				predicates.add(cb.equal(expression, mobileNo));
//			}
//
//			if (!predicates.isEmpty()) {
//				createQuery
//						.where(cb.and(predicates.toArray(new Predicate[] {})));
//				Query filterQuery = entityManager.createQuery(createQuery);
//				policyList = (List<TmpPolicy>) filterQuery.getResultList();
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		policyListContainer.addAll(policyList);
//		return policyListContainer;
//
//		// BeanItemContainer<TmpPolicy> policyListContainer = new
//		// BeanItemContainer<TmpPolicy>(TmpPolicy.class);
//		// List<TmpPolicy> policyList = new ArrayList<TmpPolicy>();
//		// Query findPolicy =
//		// entityManager.createNamedQuery("TmpPolicy.findByProposerNameAndDob").setParameter(
//		// "proposerName", "%"+ proposerName.toLowerCase() + "%").setParameter(
//		// "dob", dob);
//		// Object singleResult = findPolicy.getSingleResult();
//		// if(singleResult != null){
//		// policyList.add((TmpPolicy) singleResult);
//		// }
//		// policyListContainer.addAll(policyList);
//		// return policyListContainer;
//	}

//	@SuppressWarnings({ "unchecked", "unused" })
//	public BeanItemContainer<PolicyCoditions> getPolicyConditions(
//			String ProductName) {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<PolicyCoditions> createQuery = cb
//				.createQuery(PolicyCoditions.class);
//		Root<PolicyCoditions> fromQuery = createQuery
//				.from(PolicyCoditions.class);
//		createQuery.select(fromQuery);
//		Expression<String> expression = fromQuery.get("polProdCode");
//		createQuery.where(cb.like(expression, "%" + ProductName + "%"));
//		Query filterQuery = entityManager.createQuery(createQuery);
//		List<PolicyCoditions> policyConditionsList = (List<PolicyCoditions>) filterQuery
//				.getResultList();
//		BeanItemContainer<PolicyCoditions> policyConditionsListContainer = new BeanItemContainer<PolicyCoditions>(
//				PolicyCoditions.class);
//		policyConditionsListContainer.addAll(policyConditionsList);
//		return policyConditionsListContainer;
//	}

//	public List<TmpInsured> getInsured(String key, String value) {
//		// Form Where Condition.
//		List<TmpInsured> insuredList = new ArrayList<TmpInsured>();
//		try {
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TmpInsured> createQuery = cb
//					.createQuery(TmpInsured.class);
//			Root<TmpInsured> fromQuery = createQuery.from(TmpInsured.class);
//			createQuery.select(fromQuery);
//			Expression<String> expression = fromQuery.get(key);
//			createQuery.where(cb.equal(expression, value));
//			Query filterQuery = entityManager.createQuery(createQuery);
//
//			insuredList = (List<TmpInsured>) filterQuery.getResultList();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return insuredList;
//	}

	@SuppressWarnings("static-access")
	public BeanItemContainer<PremPolicy> filterPolicyDetailsPremia(
			HashMap<String, String> values) {
		BeanItemContainer<PremPolicy> policyListContainer = new BeanItemContainer<PremPolicy>(
				PremPolicy.class);
		
		if (values.get("healthCardNumber") != null) {

		}
		// Testing "P/111117/01/2015/001655"
		PremPolicySearchDTO policy = new PremPolicySearchDTO();
		policy.setHealthCardNo(values.get("healthCardNumber"));
		policy.setInsuredDOB(values.get("insuredDateOfBirth"));
		policy.setInsuredName(values.get("insuredName"));
		policy.setMobileNo(values.get("registerdMobileNumber"));
		policy.setPolicyNo(values.get("polNo"));
		policy.setProposerDOB(values.get("insuredDateOfBirth"));
		policy.setProposerName(values.get("polAssrName"));
		policy.setReceiptNo(values.get("polReceiptNo"));
		//Builder builder = PremiaService.getInstance().getBuilder();
		
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		PolicySource policySource = null;
		HashMap<String, Object> policyObjmap = new HashMap<String, Object>();
		List<PremPolicy> post = null;
		policyObjmap.putAll(values);
		String source = null;
		
		if (policy.getPolicyNo() != null) {
			policyObj = getByPolicyNumber(policy.getPolicyNo());
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					post = ClaimProvisionService.getInstance().callPolicySummaryService(policyObjmap);		
					source = SHAConstants.BANCS_POLICY;
				}else{
					builder = PremiaService.getInstance().getBuilder();
					post = builder.post(new GenericType<List<PremPolicy>>() {}, policy);
					source = SHAConstants.PREMIA_POLICY;
				}
			}else{
				//First time policy entered in galaxy
				policySource = getByPolicySource(policy.getPolicyNo());
				if (policySource != null) {
					if (policySource.getPolicySourceFrom() != null && policySource.getPolicySourceFrom().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						post = ClaimProvisionService.getInstance().callPolicySummaryService(policyObjmap);
						source = SHAConstants.BANCS_POLICY;
					}else{
						builder = PremiaService.getInstance().getBuilder();
						post = builder.post(new GenericType<List<PremPolicy>>() {}, policy);
						source = SHAConstants.PREMIA_POLICY;
					}
				}
			}
		}
		
		//Bancs Changes End
		//List<PremPolicy> post = builder.post(new GenericType<List<PremPolicy>>() {}, policy);

		if(post != null){
			for (PremPolicy premPolicy : post) {
				premPolicy.setPolicySource(source);
			}
			policyListContainer.addAll(post);
		}
		return policyListContainer;

	}
	
	@SuppressWarnings("static-access")
	public BeanItemContainer<PremPolicy> filterPolicyDetailsPremiaforOP(
			HashMap<String, String> values) {
		BeanItemContainer<PremPolicy> policyListContainer = new BeanItemContainer<PremPolicy>(
				PremPolicy.class);
		
		if (values.get("healthCardNumber") != null) {

		}
		// Testing "P/111117/01/2015/001655"
		PremPolicySearchDTO policy = new PremPolicySearchDTO();
		policy.setHealthCardNo(values.get("healthCardNumber"));
		policy.setInsuredDOB(values.get("insuredDateOfBirth"));
		policy.setInsuredName(values.get("insuredName"));
		policy.setMobileNo(values.get("registerdMobileNumber"));
		policy.setPolicyNo(values.get("polNo"));
		policy.setProposerDOB(values.get("insuredDateOfBirth"));
		policy.setProposerName(values.get("polAssrName"));
		policy.setReceiptNo(values.get("polReceiptNo"));
		//Builder builder = PremiaService.getInstance().getBuilder();
		
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		PolicySource policySource = null;
		HashMap<String, Object> policyObjmap = new HashMap<String, Object>();
		List<PremPolicy> post = null;
		policyObjmap.putAll(values);
		String source = null;
		
		if (policy != null) {
			/*policyObj = getByPolicyNumber(policy.getPolicyNo());
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					post = ClaimProvisionService.getInstance().callPolicySummaryService(policyObjmap);		
					source = SHAConstants.BANCS_POLICY;
				}else{
					builder = PremiaService.getInstance().getBuilder();
					post = builder.post(new GenericType<List<PremPolicy>>() {}, policy);
					source = SHAConstants.PREMIA_POLICY;
				}
			}else{*/
				//First time policy entered in galaxy
			/*	As Per Raja Instruction Policy Source Commented for OP
				policySource = getByPolicySource(policy.getPolicyNo());
				if (policySource != null) {
					if (policySource.getPolicySourceFrom() != null && policySource.getPolicySourceFrom().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						post = ClaimProvisionService.getInstance().callPolicySummaryService(policyObjmap);
						source = SHAConstants.BANCS_POLICY;
					}else{*/
						builder = PremiaService.getInstance().getBuilder();
						post = builder.post(new GenericType<List<PremPolicy>>() {}, policy);
						source = SHAConstants.PREMIA_POLICY;
//					}
//				}
//			}
		}
		
		if(post != null){
			for (PremPolicy premPolicy : post) {
				premPolicy.setPolicySource(source);
			}
			policyListContainer.addAll(post);
		}
		return policyListContainer;

	}
	
	public BeanItemContainer<OPSearchIntimationDTO> getOPIntimationData(HashMap<String, String> argEnteredValues){

		String  intimationNo = argEnteredValues.get("intimationNo");
		String  claimNo = argEnteredValues.get("claimNo");
		String  polNo = argEnteredValues.get("polNo");
		String  healthCardNumber = argEnteredValues.get("healthCardNumber");
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		CriteriaQuery<OPHealthCheckup> OPCriteriaQuery = builder.createQuery(OPHealthCheckup.class);
		Root<OPHealthCheckup> searchRoot = OPCriteriaQuery.from(OPHealthCheckup.class);
		
		List<OPHealthCheckup> opSearchResultList = new ArrayList<OPHealthCheckup>();	
		List<OPSearchIntimationDTO> OPSearchResult = new ArrayList<OPSearchIntimationDTO>();
		
	
		if(StringUtils.isNotBlank(intimationNo)){
			Predicate searchByIntimation = builder.like(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
			predicates.add(searchByIntimation);
			System.out.println("Intimation No : "+intimationNo);
		}
		if(StringUtils.isNotBlank(claimNo)){
			Predicate searchByClaimNo = builder.like(searchRoot.<OPClaim>get("claim").<String>get("claimId"), "%"+claimNo+"%");
			predicates.add(searchByClaimNo);
			System.out.println("Claim No : "+claimNo);
		}
		if(StringUtils.isNotBlank(polNo)){
			Predicate searchByPolicy = builder.equal(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), polNo);
			predicates.add(searchByPolicy);
			System.out.println("Policy No : "+polNo);
		}
		if(StringUtils.isNotBlank(healthCardNumber)){
			Predicate searchByHealthCardNo = builder.equal(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Insured>get("insured").<String>get("healthCardNumber"), healthCardNumber);
			predicates.add(searchByHealthCardNo);
			System.out.println("Health Card Number : "+healthCardNumber);
		}
		
		OPCriteriaQuery.select(searchRoot).where(builder.and(predicates.toArray(new Predicate[] {}))).orderBy(builder.desc(searchRoot.<Long>get("key")));
//		OPCriteriaQuery.select(searchRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		TypedQuery<OPHealthCheckup> opquery = entityManager.createQuery(OPCriteriaQuery);
		opSearchResultList = opquery.getResultList();
		
		if(opSearchResultList != null && opSearchResultList.size() > 0){
			//lumenSearchResult = LumenRequestMapper.getInstance().getLumenDetails(lumenSearchResultList);
			OPSearchIntimationDTO newRec = null;
			for(OPHealthCheckup rec : opSearchResultList){
				newRec = new OPSearchIntimationDTO();
				newRec.setOpIntimationNo(rec.getClaim().getIntimation().getIntimationId());
				newRec.setOpClaimNo(rec.getClaim().getClaimId());
				newRec.setPolicyNo(rec.getClaim().getIntimation().getPolicyNumber());
				newRec.setHealthCardNo(rec.getClaim().getIntimation().getInsured().getHealthCardNumber());
				newRec.setProductName(rec.getClaim().getIntimation().getPolicy().getProductName());
				newRec.setInsuredPatientName(rec.getClaim().getIntimation().getInsured().getInsuredName());
				if(rec.getClaim().getClaimType() != null){
					newRec.setClaimType(rec.getClaim().getClaimType().getValue());
				}
				newRec.setOpHealthCheckupDate(rec.getOpHealthCheckupDate());
				newRec.setOpVisitReason((rec.getClaim().getIntimation().getAdmissionReason() == null)?"":rec.getClaim().getIntimation().getAdmissionReason());	
				
				newRec.setClaim(rec.getClaim());
				newRec.setIntimation(rec.getClaim().getIntimation());
				newRec.setPolicy(rec.getClaim().getIntimation().getPolicy());
				OPSearchResult.add(newRec);
			}
		}
		
		BeanItemContainer<OPSearchIntimationDTO> page = new BeanItemContainer<OPSearchIntimationDTO>(OPSearchIntimationDTO.class);
		page.addAll(OPSearchResult);
		return page;
	}
	
	
	/**
	 * Method for fetching policy details from premia.
	 * */
	
	public PremPolicyDetails fetchPolicyDetailsFromPremia(String values)
	{
		PremPolicyDetails policyDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(values);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			
			Policy policyObj = null;
			Builder builder = null;
			
			if(policy.getPolicyNo() != null){
				policyObj = getByPolicyNumber(policy.getPolicyNo());
				if (policyObj != null) {
					 if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						//builder = BancsSevice.getInstance().getPolicyDetail();
					}else{
						builder = PremiaService.getInstance().getPolicyDetail();
						policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
					}
				}
				/*Below else condition added for OP*/
				else {
					builder = PremiaService.getInstance().getPolicyDetail();
					policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
				}
			}
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getPolicyDetail();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		System.out.println("----List policy Details----"+policyDetails);
	
		return policyDetails;
	
	}

	
	public BeanItemContainer<OPSearchIntimationDTO> getOPExpiredPolicyData(HashMap<String, String> argEnteredValues){

		String  polNo = argEnteredValues.get("polNo");
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		CriteriaQuery<OPHealthCheckup> OPCriteriaQuery = builder.createQuery(OPHealthCheckup.class);
		Root<OPHealthCheckup> searchRoot = OPCriteriaQuery.from(OPHealthCheckup.class);
		
		List<OPHealthCheckup> opSearchResultList = new ArrayList<OPHealthCheckup>();	
		List<OPSearchIntimationDTO> OPSearchResult = new ArrayList<OPSearchIntimationDTO>();
	
		if(StringUtils.isNotBlank(polNo)){
			Predicate searchByPolicy = builder.equal(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), polNo);
			predicates.add(searchByPolicy);
			System.out.println("Policy No : "+polNo);
		}
		OPCriteriaQuery.select(searchRoot).where(builder.and(predicates.toArray(new Predicate[] {}))).orderBy(builder.desc(searchRoot.<Long>get("key")));
//		OPCriteriaQuery.select(searchRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		TypedQuery<OPHealthCheckup> opquery = entityManager.createQuery(OPCriteriaQuery);
		opSearchResultList = opquery.getResultList();
		
		if(opSearchResultList != null && opSearchResultList.size() > 0){
			//lumenSearchResult = LumenRequestMapper.getInstance().getLumenDetails(lumenSearchResultList);
			OPSearchIntimationDTO newRec = null;
			for(OPHealthCheckup rec : opSearchResultList){
				newRec = new OPSearchIntimationDTO();
				newRec.setOpIntimationNo(rec.getClaim().getIntimation().getIntimationId());
				newRec.setOpClaimNo(rec.getClaim().getClaimId());
				newRec.setPolicyNo(rec.getClaim().getIntimation().getPolicyNumber());
				newRec.setHealthCardNo(rec.getClaim().getIntimation().getInsured().getHealthCardNumber());
				newRec.setProductName(rec.getClaim().getIntimation().getPolicy().getProductName());
				newRec.setInsuredPatientName(rec.getClaim().getIntimation().getInsured().getInsuredName());
				if(rec.getClaim().getClaimType() != null){
					newRec.setClaimType(rec.getClaim().getClaimType().getValue());
				}
				newRec.setOpHealthCheckupDate(rec.getOpHealthCheckupDate());
				newRec.setOpVisitReason((rec.getClaim().getIntimation().getAdmissionReason() == null)?"":rec.getClaim().getIntimation().getAdmissionReason());	
				
				newRec.setClaim(rec.getClaim());
				newRec.setIntimation(rec.getClaim().getIntimation());
				newRec.setPolicy(rec.getClaim().getIntimation().getPolicy());
				OPSearchResult.add(newRec);
			}
		}
		
		BeanItemContainer<OPSearchIntimationDTO> page = new BeanItemContainer<OPSearchIntimationDTO>(OPSearchIntimationDTO.class);
		page.addAll(OPSearchResult);
		return page;
	}
	
	/*public String get64VBChequeStatus(String values){
		
		Builder builder = PremiaService.get64VBChequeStatusDetails();
		
		PremPolicyDetails chequeStatus = builder.post(new GenericType<PremPolicyDetails>() {}, "\""+values + "\"");
		
		String chequeStatusValue = SHAConstants.VB64_STATUS_PENDING;
		
		
		return chequeStatusValue;
		
	}*/
	
		
//	@SuppressWarnings({ "unchecked", "unused" })
//	public BeanItemContainer<TmpPolicy> filterPolicyDetails(
//			HashMap<String, String> values) {
//		// Form Where Condition.
//		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
//				TmpPolicy.class);
//		try {
//			// entityManager.clear();
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TmpPolicy> createQuery = cb
//					.createQuery(TmpPolicy.class);
//			Root<TmpPolicy> policyRoot = createQuery.from(TmpPolicy.class);
//
//			createQuery.select(policyRoot);
//			List<Predicate> predicates = new ArrayList<Predicate>();
//			List<TmpInsured> tmpInsuredList = null;
//
//			for (Map.Entry<String, String> entry : values.entrySet()) {
//				if (entry.getValue() != null) {
//					if (entry.getKey().equalsIgnoreCase("healthCardNumber")) {
//						tmpInsuredList = getByHealthCard(entry.getValue());
//					} 
//					if(entry.getKey().equalsIgnoreCase("registerdMobileNumber") && entry.getValue() != null){
//						
//						predicates.add(cb.equal(policyRoot.<String> get(entry.getKey()),
//								entry.getValue()));
//						
//					}
//					if(! entry.getKey().equalsIgnoreCase("healthCardNumber") && !entry.getKey().equalsIgnoreCase("registerdMobileNumber")){
//						Expression<String> expression = cb.lower(policyRoot
//								.<String> get(entry.getKey()));
//						predicates.add(cb.like(expression,
//								"%" + entry.getValue().toLowerCase() + "%"));
//					}
//				}
//			}
//			List<TmpPolicy> policyList = null;
//			if (!predicates.isEmpty()) {
//				createQuery
//						.where(cb.and(predicates.toArray(new Predicate[] {})));
//				Query filterQuery = entityManager.createQuery(createQuery);
//				policyList = (List<TmpPolicy>) filterQuery.getResultList();
//
//			}
//			List<TmpPolicy> policiesList = new ArrayList<TmpPolicy>();
//			if (tmpInsuredList != null) {
//				if (policyList == null) {
//					policyList = new ArrayList<TmpPolicy>();
//				}
//				for (TmpInsured tmpInsured : tmpInsuredList) {
//
//					TmpPolicy policy = getTmpPolicy(tmpInsured.getPolicyNo());
//					if (!policyList.isEmpty() && policyList.contains(policy)) {
//						policiesList.add(policy);
//					} else if (predicates.isEmpty()) {
//						policiesList.add(policy);
//					}
//				}
//			} else {
//				policiesList = policyList;
//			}
//
//			// Refresh the entity manager to get new updates.
//			for (TmpPolicy tmpPolicy : policiesList) {
//				entityManager.refresh(tmpPolicy);
//			}
//			policyListContainer.addAll(policiesList);
//
//		} catch (Exception e) {
//			e.getMessage();
//		}
//		return policyListContainer;
//	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<OrganaizationUnit> getPolicyCodeOrName() {
		BeanItemContainer<OrganaizationUnit> organizationContainer = new BeanItemContainer<OrganaizationUnit>(
				OrganaizationUnit.class);
		Query findAll = entityManager
				.createNamedQuery("OrganaizationUnit.findAll");
		List<OrganaizationUnit> organizationList = (List<OrganaizationUnit>) findAll
				.getResultList();
		organizationContainer.addAll(organizationList);
		return organizationContainer;
	}
	
	/**
	 * Method added for retreiving select value container for
	 * product. Specific to Policy Search.
	 * */
	public BeanItemContainer<SelectValue> getSelectValueContainerForOrganization() {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		
		Query findAll = entityManager
				.createNamedQuery("OrganaizationUnit.findAll");
		List<OrganaizationUnit> organizationList = (List<OrganaizationUnit>) findAll.getResultList();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!organizationList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (OrganaizationUnit organizationUnit : organizationList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(organizationUnit.getKey().longValue());
				selectValue.setValue(organizationUnit.getOrganizationUnitName());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	

	@SuppressWarnings({ "unchecked", "unused" })
	public List<OrganaizationUnit> getInsuredOfficeNameByDivisionCode(
			String polDivnCode) {
		List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
		if(polDivnCode != null){
			Query findAll = entityManager.createNamedQuery(
					"OrganaizationUnit.findByUnitId").setParameter("officeCode",
							polDivnCode);
			organizationList = (List<OrganaizationUnit>) findAll
				.getResultList();
		}
		
		return organizationList;
	}
	
	/**
	 * Method to retreive policy issuing office name , by passing
	 * issuing code.
	 * */
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(EntityManager entityManager,
			String polDivnCode) {
		List<OrganaizationUnit> organizationUnit = null;
		if(polDivnCode != null){			
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode", polDivnCode);
			organizationUnit = (List<OrganaizationUnit>)findAll.getResultList();
		}
		
		if(organizationUnit != null && ! organizationUnit.isEmpty()){
			return organizationUnit.get(0);
		}
		
		
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<Intimation> getPolicyIntimationDetails(
			String policyNumber) {

		String status = "Submitted";

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = builder
				.createQuery(Intimation.class);

		Root<Intimation> searchRoot = criteriaQuery.from(Intimation.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Intimation> resultList = new ArrayList<Intimation>();

		if (policyNumber != null) {
			Predicate policyPredicate = builder.like(
					searchRoot.<Policy> get("policy").<String> get(
							"policyNumber"), "%" + policyNumber + "%");
			predicates.add(policyPredicate);
			predicates.add(policyPredicate);
		}
		if (status != null) {
			Predicate statusPredicate = builder.like(
					searchRoot.<Status> get("status").<String> get("processValue"), "%" + status + "%");
			predicates.add(statusPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		criteriaQuery.orderBy(builder.asc(searchRoot.get("createdDate")));
		final TypedQuery<Intimation> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();

		List<Intimation> pageItemList = resultList;

		BeanItemContainer<Intimation> intimationContainer = new BeanItemContainer<Intimation>(
				Intimation.class);
		intimationContainer.addAll(pageItemList);
		return intimationContainer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Policy getPolicy(String policyNumber) {
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);
		List<Policy> policyList = query.getResultList();
		for (Policy policy : policyList) {
			entityManager.refresh(policy);
		}
		Policy policy = null;
		if(policyList != null && !policyList.isEmpty())
		{
			/**
			 * Will IMS_CLS_POLICY will hold multiple
			 * entries for a single policy number? ---
			 * Needs to clarified with DBA team.
			 * */
			for (Policy resultPolicy : policyList) {
				policy = resultPolicy;
				break;
			}
			if(policy != null){
				List<Insured> insuredList = getInsuredList(policyNumber,entityManager);
				policy.setInsured(insuredList);
			}
		}
		return policy;
	}
	
	
	/**
	 * Added for premia data copy service
	 * */
	
	public Policy getPolicyForPremia(String policyNumber , EntityManager entityManager)
	{
		this.entityManager = entityManager;
		return getPolicy(policyNumber);
	}
	
	public List<Insured> getInsuredList(String policyNumber,EntityManager entityManager){
		InsuredService insuredService = new InsuredService();
		List<Insured> insuredList = insuredService.getInsuredListByPolicyNo(policyNumber, entityManager);
		return insuredList;
	}
	
	public List<NomineeDetails> getNomineeDetails(Long insuredKey){
		
		Query query = entityManager.createNamedQuery("NomineeDetails.findByInsuredKey");
		query = query.setParameter("insuredKey", insuredKey);
		
		List<NomineeDetails> nomineeDetails = (List<NomineeDetails>) query.getResultList();
		return nomineeDetails;
		
	}
	
	
//	public TmpPolicy getTmpPolicy(String policyNumber) {
//		// Query findAll =
//		// entityManager.createNamedQuery("CityTownVillage.findAll");
//		Query query = entityManager.createNamedQuery("TmpPolicy.findByPolicy");
//		query = query.setParameter("parentKey", policyNumber);
//
//		List<TmpPolicy> policyList = query.getResultList();
//		TmpPolicy tmpPolicy = new TmpPolicy();
//
//		if(policyList != null && !policyList.isEmpty())
//		{
//
//			for (TmpPolicy resultPolicy : policyList) {
//				tmpPolicy = resultPolicy;
//			}
//
//			List<TmpInsured> tmpTmpInsured = insured.getInsuredList(policyNumber)
//					.getItemIds();
//			tmpPolicy.setTmpInsured(tmpTmpInsured);
//		}
//		return tmpPolicy;
//	}


//	public TmpPolicy getPolicyByProductType(String policyNumber,String productType) {
//		// Query findAll =
//		// entityManager.createNamedQuery("CityTownVillage.findAll");
//		Query query = entityManager.createNamedQuery("TmpPolicy.findByPolicyByPolType");
//		query = query.setParameter("parentKey", policyNumber);
//		query.setParameter("polType", StringUtils.trim(productType.toLowerCase()));		
//
//		List<TmpPolicy> tmpPolicyList = query.getResultList();
//		TmpPolicy a_TmpPolicy = new TmpPolicy();
//
//		if(tmpPolicyList != null && !tmpPolicyList.isEmpty())
//		{
//			for (TmpPolicy tmpPolicy : tmpPolicyList) {
//				a_TmpPolicy = tmpPolicy;
//			}
//
//			List<TmpInsured> tmpTmpInsured = insured.getInsuredList(policyNumber)
//					.getItemIds();
//			a_TmpPolicy.setTmpInsured(tmpTmpInsured);
//		}
//		return a_TmpPolicy;
//	}
	
	
	
	
//	@SuppressWarnings({ "unchecked", "unused" })
//	public TmpPolicy getPolicyById(Long tmpPolicyId) {
//		// Query findAll =
//		// entityManager.createNamedQuery("CityTownVillage.findAll");
//		Query query = entityManager.createNamedQuery("TmpPolicy.findById");
//		query = query.setParameter("primaryKey", tmpPolicyId);
//
//		List<TmpPolicy> tmpPolicyList = query.getResultList();
//		TmpPolicy a_TmpPolicy = new TmpPolicy();
//
//		if (!tmpPolicyList.isEmpty()) {
//			a_TmpPolicy = tmpPolicyList.get(0);
//		}
//		return a_TmpPolicy;
//	}

//	public TmpPolicy findTmppolicyById(Long key) {
//		return (TmpPolicy) entityManager.find(TmpPolicy.class, key);
//	}
//	
//	public TmpPolicy findTmppolicyByPolicyNo(String parentKey) {
//		return (TmpPolicy) entityManager.find(TmpPolicy.class, parentKey);
//	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<Intimation> getIntimationByPolicy(
			String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"Intimation.findByPolicy").setParameter("policyNo",
				"'%" + policyNumber + "%'");
		List<Intimation> intimationList = (List<Intimation>) findIntimOfPolicy
				.getResultList();
		BeanItemContainer<Intimation> intimationContainer = new BeanItemContainer<Intimation>(
				Intimation.class);
		intimationContainer.addAll(intimationList);
		return intimationContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Hospitals getVWHospitalByKey(Long hospitalKey) {
		if (hospitalKey != null) {
			return entityManager.find(Hospitals.class, hospitalKey);
		}
		return null;
	}

//	@SuppressWarnings({ "unchecked", "unused" })
//	public TmpInsured findByInsuredKey(Long insuredKey) {
//		if (insuredKey != null) {
//			return entityManager.find(TmpInsured.class, insuredKey);
//		}
//		return null;
//	}
	
	public Insured findByClsInsuredKey(Long insuredKey) {
		if (insuredKey != null) {
			return entityManager.find(Insured.class, insuredKey);
		}
		return null;
	}

	public TmpCPUCode getTmpCpuCode(Long cpuKey) {
		if (!ValidatorUtils.isNull(cpuKey))
			return entityManager.find(TmpCPUCode.class, cpuKey);
		else
			return null;
	}
	
	
	public TmpCPUCode getMasCpuCode(Long cpuCode)
	{
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty())
		{
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	public  TmpCPUCode getCpuDetails(Long cpuId) {
		TmpCPUCode ack = null;
					
			Query findByReimbursementKey = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			try{
				List resultList = findByReimbursementKey.getResultList();
				if(resultList != null && !resultList.isEmpty()) {
					ack = (TmpCPUCode) resultList.get(0);
				}
				
				return ack;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<TmpCPUCode> getTmpCpuCodes() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
				.getResultList();

		System.out
				.println("description of sample CPU ++++++++++++++++++++++ : "
						+ resultCPUCodeList.get(0).getDescription());

		BeanItemContainer<TmpCPUCode> cpuCodeContainer = new BeanItemContainer<TmpCPUCode>(
				TmpCPUCode.class);
		cpuCodeContainer.addAll(resultCPUCodeList);
			
		cpuCodeContainer.sort(new Object[] {"description"}, new boolean[] {true});

		return cpuCodeContainer;

	}

	/*public IntimationsDto intimationToIntimationDTO(Intimation intimation) {
		if (intimation == null) {
			return null;
		}

		IntimationsDto intimationDto = new IntimationsDto();

		intimationDto.setKey(intimation.getKey());
		intimationDto
				.setPolicyNumber(intimation.getPolicy().getPolicyNumber() != null ? intimation
						.getPolicy().getPolicyNumber() : "");
		// intimationDto.setActiveStatus( (intimation.getActiveStatus() < 0 ) ?
		// intimation.getActiveStatus() : "" );
		// intimationDto.setActiveStatusDate( intimation.getActiveStatusDate()
		// != null ? intimation.getActiveStatusDate():"" );
		intimationDto
				.setAdmissionDate(intimation.getAdmissionDate() != null ? intimation
						.getAdmissionDate() : null);
		intimationDto
				.setAdmissionReason(intimation.getAdmissionReason() != null ? intimation
						.getAdmissionReason() : "");
		// intimationDto.setAdmissionTypeId( intimation.getAdmissionTypeId() < 0
		// ? intimation.getAdmissionTypeId() : "" );
		TmpCPUCode tmpCpuCode = intimation.getCpuCode();
		intimationDto.setCpuCode(tmpCpuCode != null ? tmpCpuCode.getCpuCode()
				.toString() : null);
		intimationDto.setCpuId(intimation.getCpuCode() != null ? intimation
				.getCpuCode().getKey() : null);
		intimationDto.setCreatedBy(intimation.getCreatedBy());
		intimationDto.setCreatedDate(intimation.getCreatedDate());
		intimationDto
				.setDoctorFirstName(intimation.getDoctorName() != null ? intimation
						.getDoctorName() : "");
		intimationDto
				.setHospitalComments(intimation.getHospitalComments() != null ? intimation
						.getHospitalComments() : "");
		// intimationDto.setHospitalType(
		// intimation.getHospital().getHospitalsType() != null ?
		// intimation.getHospital().getHospitalsType() : "");
		// intimationDto.setInpatientNumber( intimation.getInpatientNumber() );
		if (!ValidatorUtils.isNull(intimation.getIntimatedBy())) {
			intimationDto.setIntimatedByValue(intimation.getIntimatedBy()
					.getValue() != null ? intimation.getIntimatedBy()
					.getValue() : "");
		}
		intimationDto
				.setIntimatorName(intimation.getIntimaterName() != null ? intimation
						.getIntimaterName() : "");
		intimationDto
				.setIntimationId(intimation.getIntimationId() != null ? intimation
						.getIntimationId() : "");
		if (!ValidatorUtils.isNull(intimation.getIntimationMode())) {
			intimationDto.setIntimationModeValue(intimation.getIntimationMode()
					.getValue() != null ? intimation.getIntimationMode()
					.getValue() : "");
		}
		intimationDto.setLateintimationReason(intimation
				.getLateintimationReason() != null ? intimation
				.getLateintimationReason() : intimation
				.getLateintimationReason());
		intimationDto.setModifiedBy(intimation.getModifiedBy());
		intimationDto.setModifiedDate(intimation.getModifiedDate());

		intimationDto
				.setOfficeCode(intimation.getOfficeCode() != null ? intimation
						.getOfficeCode() : "----------");
		intimationDto.setSmCode("");
		intimationDto.setSmName("");
		intimationDto.setAgentBrokerCode("");
		intimationDto.setAgentBrokerName("");
		List<OrganaizationUnit> insuredOfficeNameByDivisionCode = getInsuredOfficeNameByDivisionCode(intimation
				.getPolicy().getOfficeCode());

		if (!insuredOfficeNameByDivisionCode.isEmpty()) {
			intimationDto.setOfficeCode(insuredOfficeNameByDivisionCode.get(0)
					.getOrganizationUnitName());
		}

		// intimationDto.setPatientNotCovered( intimation.getPatientNotCovered()
		// );
		if (!ValidatorUtils.isNull(intimation.getRoomCategory())) {
			intimationDto.setRoomCategoryValue(intimation.getRoomCategory()
					.getValue() != null ? intimation.getRoomCategory()
					.getValue() : "<(----------)>");
		}
		intimationDto.setStatus(intimation.getStatus() != null ? intimation
				.getStatus() : "----------");
		intimationDto
				.setStatusDate(intimation.getStatusDate() != null ? intimation
						.getStatusDate() : null);
		// intimationDto.setVersion( intimation.getVersion() );
		intimationDto
				.setCallerContactNumber(intimation.getCallerMobileNumber());
		intimationDto
				.setPolicyStatus(intimation.getPolicy().getStatus() != null ? intimation
						.getPolicy().getStatus() : "----------");

		intimationDto
				.setPolicyType(intimation.getPolicy().getPolicyType() != null ? intimation
						.getPolicy().getPolicyType().getValue()
						: "");
		intimationDto.setCumulativeBonus(intimation.getPolicy()
				.getCummulativeBonus());
		intimationDto
				.setProductType(intimation.getPolicy().getProductType() != null ? intimation
						.getPolicy().getProductType().getValue()
						: "");
		intimationDto
				.setCustomerId(intimation.getPolicy().getInsuredId() != null ? intimation
						.getPolicy().getInsuredId() : "");

		intimationDto
				.setProductName(intimation.getPolicy().getProduct() != null ? intimation
						.getPolicy().getProduct().getValue()
						: "----------");

		if (!ValidatorUtils.isNull(intimation.getHospital())) {
			Hospitals hospital = getVWHospitalByKey(intimation.getHospital());

			intimationDto.setHospitalName(hospital.getName());
			intimationDto.setHospitalCode(hospital.getHospitalCode());
			intimationDto.setHospitalTypeName(hospital.getHospitalType()
					.getValue());

			intimationDto.setHospitalDoorAptNumber(hospital
					.getDoorApartmentNumber() != null ? hospital
					.getDoorApartmentNumber() : "");
			intimationDto
					.setHospitalBuildingName(hospital.getBuildingName() != null ? hospital
							.getBuildingName() : "");
			intimationDto
					.setHospitalStreetName(hospital.getStreetName() != null ? hospital
							.getStreetName() : "");
			intimationDto.setHospitalCity(hospital.getCity() != null ? hospital
					.getCity() : "");
			intimationDto.setState(hospital.getState() != null ? hospital
					.getState() : "");
			intimationDto.setDistrict(hospital.getDistrict() != null ? hospital
					.getDistrict() : "");
			intimationDto
					.setHospitalTypeName(hospital.getHospitalType() != null ? hospital
							.getHospitalType().getValue() : "");
			intimationDto.setHospitalAddress(intimationDto
					.getHospitalDoorAptNumber()
					+ ","
					+ intimationDto.getHospitalBuildingName()
					+ ","
					+ intimationDto.getHospitalStreetName()
					+ ","
					+ intimationDto.getHospitalCity()
					+ ","
					+ intimationDto.getDistrict()
					+ ","
					+ intimationDto.getState());
			intimationDto
					.setHospitalPhnNo(hospital.getPhoneNumber() != null ? hospital
							.getPhoneNumber() : "");
			intimationDto.setHospitalFaxNo(hospital.getFax() != null ? hospital
					.getFax() : "");
		}

		// intimationDto.setHospitalCode(intimation.getHospital().getHospitalCode()
		// != null ? intimation.getHospital().getHospitalCode() : "----------");
		intimationDto
				.setHospitalTypeId(intimation.getHospitalType() != null ? intimation
						.getHospitalType().getKey() : null);

		if (!ValidatorUtils.isNull(intimation.getIntimationMode()))
			intimationDto.setIntimationModeValue(intimation.getIntimationMode()
					.getValue() != null ? intimation.getIntimationMode()
					.getValue() : "----------");
		else
			intimationDto.setIntimationModeValue("");

		if (!ValidatorUtils.isNull(intimation.getRoomCategory()))
			intimationDto.setRoomCategoryValue(intimation.getRoomCategory()
					.getValue() != null ? intimation.getRoomCategory()
					.getValue() : "----------");
		else
			intimationDto.setRoomCategoryValue("");

		if (!ValidatorUtils.isNull(intimation.getIntimatedBy()))
			intimationDto.setIntimatedByValue(intimation.getIntimatedBy()
					.getValue() != null ? intimation.getIntimatedBy()
					.getValue() : "----------");
		else
			intimationDto.setIntimatedByValue("");

		if (!ValidatorUtils.isNull(intimation.getPolicy())) {
			intimationDto.setPolicy(intimation.getPolicy());
			intimationDto
					.setProposerName((intimation.getPolicy()
							.getProposerFirstName() != null ? intimation
							.getPolicy().getProposerFirstName() : "")
							+ " ");
			intimationDto
					.setInsuredName((intimation.getPolicy()
							.getInsuredFirstName() != null ? intimation
							.getPolicy().getInsuredFirstName() : "")
							+ " "
							+ (intimation.getPolicy().getInsuredMiddleName() != null ? intimation
									.getPolicy().getInsuredMiddleName() : "")
							+ " "
							+ ((intimation.getPolicy().getInsuredLastName() != null ? intimation
									.getPolicy().getInsuredLastName() : "")));
			intimationDto.setInsuredPatientName(intimationDto.getInsuredName());
			// intimationDto.setPolicyDto(DtoConverter.policyToPolicyDTO(intimation.getPolicy()));
			DtoConverter dtoConverter = new DtoConverter();
			intimationDto.setPolicyDto(dtoConverter
					.policyToPolicyDTO(intimation.getPolicy()));
			intimationDto.setHealthCardNumber(intimation.getPolicy()
					.getHealthCardNumber() != null ? intimation.getPolicy()
					.getHealthCardNumber() : "----------");
		} else {
			intimationDto.setInsuredName("------------------------");
			intimationDto.setHealthCardNumber("-----------------------");
		}

		if (!ValidatorUtils.isNull(intimation.getPolicy().getProduct()))
			intimationDto.setProductName(intimation.getPolicy().getProduct()
					.getValue() != null ? intimation.getPolicy().getProduct()
					.getValue() : "--------");
		else
			intimationDto.setProductName("----------");

		return intimationDto;
	}*/

	/*public NewIntimationDto newIntimationToIntimationDTO(Intimation intimation) {
		if (intimation == null) {
			return null;
		}

		Hospitals hospital = newIntimationService
				.getHospitalDetailsByKey(intimation.getHospital());

		Claim claim = newIntimationService.getClaimByIntimationKey(intimation
				.getKey());

		List<Preauth> preAuth = newIntimationService
				.getPreauthListByIntimationKey(intimation.getKey());

		newIntimationDTO.setKey(intimation.getKey());

		newIntimationDTO
				.setIntimationId(intimation.getIntimationId() != null ? intimation
						.getIntimationId() : "");

		newIntimationDTO.setDateOfIntimation(intimation.getCreatedDate() != null ? intimation.getCreatedDate().toString()
				: "");

		newIntimationDTO.setPolicyNumber(intimation.getPolicy()
				.getPolicyNumber() != null ? intimation.getPolicy()
				.getPolicyNumber() : "");

		newIntimationDTO.setPolicyIssueOffice(intimation.getPolicy()
				.getHomeOfficeName() != null ? intimation.getPolicy()
				.getHomeOfficeName() : "");

		newIntimationDTO.setProductName(intimation.getPolicy().getProduct()
				.getValue().toString() != null ? intimation.getPolicy()
				.getProduct().getValue().toString() : "");

		newIntimationDTO.setInsuredPatientName(intimation.getPolicy()
				.getInsuredFirstName() != null ? intimation.getPolicy()
				.getInsuredFirstName() : "");

		newIntimationDTO
				.setPatientName(intimation.getInsuredPatientName() != null ? intimation
						.getInsuredPatientName() : "");

		newIntimationDTO.setHospitalName(hospital.getName() != null ? hospital
				.getName() : "");

		newIntimationDTO.setHospitalCity(hospital.getCity() != null ? hospital
				.getCity() : "");

		newIntimationDTO.setHospitalNetwork(hospital.getHospitalType()
				.getValue() != null ? hospital.getHospitalType().getValue()
				: "");

		if (intimation.getAdmissionDate() != null) {
			newIntimationDTO.setAdmissionDate(intimation.getAdmissionDate());
		}

		newIntimationDTO
				.setReasonForAdmission(intimation.getAdmissionReason() != null ? intimation
						.getAdmissionReason() : "");

		if (intimation.getCpuCode() != null) {
			newIntimationDTO.setCpuCode(intimation.getCpuCode().getCpuCode());
		}

		newIntimationDTO
				.setSmCode(intimation.getPolicy().getSmCode() != null ? intimation
						.getPolicy().getSmCode() : "");

		newIntimationDTO
				.setSmName(intimation.getPolicy().getSmName() != null ? intimation
						.getPolicy().getSmName() : "");

		newIntimationDTO.setAgentBrokerCode(intimation.getPolicy()
				.getAgentCode() != null ? intimation.getPolicy().getAgentCode()
				: "");

		newIntimationDTO.setAgentBrokerName(intimation.getPolicy()
				.getAgentName() != null ? intimation.getPolicy().getAgentName()
				: "");

		newIntimationDTO
				.setHospitalCode(hospital.getHospitalCode() != null ? hospital
						.getHospitalCode() : "");

		newIntimationDTO.setIntimationMode(intimation.getIntimationMode()
				.getValue() != null ? intimation.getIntimationMode().getValue()
				: "");

		newIntimationDTO
				.setIntimatdBy(intimation.getIntimatedBy().getValue() != null ? intimation
						.getIntimatedBy().getValue() : "");

		newIntimationDTO
				.setIntimatorName(intimation.getIntimaterName() != null ? intimation
						.getIntimaterName() : "");

		if (hospital.getKey() != null) {
			newIntimationDTO.getHospitalDto().setKey(hospital.getKey());
		}

		newIntimationDTO
				.setHospitalPhnNo(hospital.getPhoneNumber() != null ? hospital
						.getPhoneNumber() : "");

		newIntimationDTO
				.setHospitalFaxNo(hospital.getFax().toString() != null ? hospital
						.getFax().toString() : "");

		newIntimationDTO
				.setHospitalAddress(hospital.getAddress().toString() != null ? hospital
						.getAddress().toString() : "");

		newIntimationDTO
				.setRoomCatgory(intimation.getRoomCategory() != null ? intimation
						.getRoomCategory().getValue() : "");

		newIntimationDTO
				.setDoctorName(intimation.getDoctorName() != null ? intimation
						.getDoctorName() : "");

		newIntimationDTO
				.setCallerContactNum(intimation.getCallerMobileNumber() != null ? intimation
						.getCallerMobileNumber() : "");

		newIntimationDTO.setIdCardNo(intimation.getPolicy()
				.getHealthCardNumber() != null ? intimation.getPolicy()
				.getHealthCardNumber() : "");
		if (intimation.getCpuCode() != null) {
			newIntimationDTO.setClaimCpuCode(intimation.getCpuCode()
					.getCpuCode().toString());
		}

		if (claim != null) {
			newIntimationDTO.setClaimNo(claim.getClaimId() != null ? claim
					.getClaimId() : "");

			newIntimationDTO.setRegistrationStatus(claim.getStatus()
					.getProcessValue() != null ? claim.getStatus()
					.getProcessValue() : "");

			newIntimationDTO
					.setProvisionAmt(claim.getProvisionAmount() != null ? claim
							.getProvisionAmount().toString() : "");

			newIntimationDTO.setCashLessOrReimbersement(claim.getClaimType()
					.getValue() != null ? claim.getClaimType().getValue() : "");

			newIntimationDTO
					.setCloseRemarks(claim.getRegistrationRemarks() != null ? claim
							.getRegistrationRemarks() : "");
		}
		
		List<Long> maxSewquence = new ArrayList<Long>();

		for (Preauth preAuthAprveAmt : preAuth) {
			if (preAuthAprveAmt.getSequenceNo() != null) {
				maxSewquence.add(preAuthAprveAmt.getSequenceNo());
			}
		}

		if(!preAuth.isEmpty()){
			newIntimationDTO
			.setTotalAuthAmt(preAuth.get(preAuth.size()-1).getApprovedAmount().toString());
		}

		for (Preauth preAuthAprveAmt : preAuth) {
			if (maxSewquence != null && maxSewquence.size() != 0) {
				if (preAuthAprveAmt.getSequenceNo().equals(
						Collections.max(maxSewquence))) {
					newIntimationDTO
							.setStatusOfCashLess(preAuthAprveAmt.getStatus()
									.getProcessValue() != null ? preAuthAprveAmt
									.getStatus().getProcessValue() : "");
				} else {
					newIntimationDTO.setStatusOfCashLess("");
				}
			}
		}

		
		 * NewIntimationDto newIntimationDTO = new NewIntimationDto();
		 * 
		 * newIntimationDTO.setKey(intimation.getKey());
		 * 
		 * newIntimationDTO .setIntimationId(intimation.getIntimationId() !=
		 * null ? intimation .getIntimationId() : "");
		 * 
		 * 
		 * 
		 * newIntimationDTO .setAdmissionDate(intimation.getAdmissionDate() !=
		 * null ? intimation .getAdmissionDate() : null);
		 * 
		 * newIntimationDTO.setPolicyNumber(intimation.getPolicy().getPolicyNumber
		 * ());
		 * 
		 * if (intimation.getPolicy().getHomeOfficeName() != null) {
		 * newIntimationDTO.setPolicyIssueOffice(intimation.getPolicy()
		 * .getHomeOfficeName()); }
		 * if(intimation.getPolicy().getProductType()!=null){ selectValue = new
		 * SelectValue();
		 * selectValue.setId(intimation.getPolicy().getProductType().getKey());
		 * selectValue
		 * .setValue(intimation.getPolicy().getProductType().getValue());
		 * newIntimationDTO.setProductType(selectValue); }
		 * if(intimation.getInsuredPatientName()!=null){
		 * newIntimationDTO.setInsuredPatientName
		 * (intimation.getInsuredPatientName()); }
		 * if(intimation.getDoctorName()!=null){
		 * newIntimationDTO.setDoctorName(intimation.getDoctorName()); }
		 * 
		 * if(intimation.getAdmissionReason()!=null){
		 * newIntimationDTO.setReasonForAdmission
		 * (intimation.getAdmissionReason()); }
		 * 
		 * TmpCPUCode tmpCpuCode = intimation.getCpuCode();
		 * 
		 * newIntimationDTO.setCpuCode(tmpCpuCode != null ? tmpCpuCode
		 * .getCpuCode() : null);
		 * newIntimationDTO.setCpuId(intimation.getCpuCode() != null ?
		 * intimation .getCpuCode().getKey() : null); //
		 * newIntimationDTO.setCreatedBy(intimation.getCreatedBy());
		 * newIntimationDTO.setCreatedDate(intimation.getCreatedDate());
		 * newIntimationDTO .setDoctorName(intimation.getDoctorName() != null ?
		 * intimation .getDoctorName() : ""); newIntimationDTO
		 * .setComments(intimation.getHospitalComments() != null ? intimation
		 * .getHospitalComments() : ""); if
		 * (!ValidatorUtils.isNull(intimation.getIntimatedBy())) { selectValue =
		 * new SelectValue();
		 * selectValue.setId(intimation.getIntimatedBy().getKey());
		 * selectValue.setValue(intimation.getIntimatedBy().getValue());
		 * newIntimationDTO.setIntimatedBy(selectValue); } newIntimationDTO
		 * .setIntimaterName(intimation.getIntimaterName() != null ? intimation
		 * .getIntimaterName() : "");
		 * 
		 * if (!ValidatorUtils.isNull(intimation.getIntimationMode())) {
		 * selectValue = new SelectValue();
		 * selectValue.setId(intimation.getIntimationMode().getKey());
		 * selectValue.setValue(intimation.getIntimationMode().getValue());
		 * newIntimationDTO.setModeOfIntimation(selectValue); } if
		 * (!ValidatorUtils.isNull(intimation.getLateintimationReason())) {
		 * selectValue = new SelectValue();
		 * selectValue.setId(intimation.getLateintimationReason().getKey());
		 * selectValue.setValue(intimation.getLateintimationReason()
		 * .getValue()); newIntimationDTO.setLateIntimationReason(selectValue);
		 * } List<OrganaizationUnit> insuredOfficeNameByDivisionCode =
		 * getInsuredOfficeNameByDivisionCode(intimation
		 * .getPolicy().getOfficeCode()); if
		 * (!ValidatorUtils.isNull(intimation.getRoomCategory())) { selectValue
		 * = new SelectValue();
		 * selectValue.setId(intimation.getRoomCategory().getKey());
		 * selectValue.setValue(intimation.getRoomCategory().getValue());
		 * newIntimationDTO.setRoomCategory(selectValue); }
		 * newIntimationDTO.setStatus(intimation.getStatus() != null ?
		 * intimation .getStatus() : "----------"); newIntimationDTO
		 * .setCallerContactNum(intimation.getCallerMobileNumber());
		 * newIntimationDTO.setPolicy(intimation.getPolicy());
		 * 
		 * if (!ValidatorUtils.isNull(intimation.getPolicy())) { selectValue =
		 * new SelectValue();
		 * selectValue.setId(intimation.getPolicy().getPolicyType().getKey());
		 * selectValue.setValue(intimation.getPolicy().getPolicyType()
		 * .getValue()); newIntimationDTO.setPolicyType(selectValue); }
		 * 
		 * if (!ValidatorUtils.isNull(intimation.getPolicy())) { selectValue =
		 * new SelectValue();
		 * selectValue.setId(intimation.getPolicy().getProductType().getKey());
		 * selectValue.setValue(intimation.getPolicy().getProductType()
		 * .getValue()); newIntimationDTO.setProductType(selectValue); }
		 * 
		 * if (!ValidatorUtils.isNull(intimation.getPolicy().getProduct())) {
		 * newIntimationDTO.setProduct(intimation.getPolicy().getProduct()); }
		 * 
		 * if (!ValidatorUtils.isNull(intimation.getHospital())) { Hospitals
		 * hospital = getVWHospitalByKey(intimation.getHospital());
		 * newIntimationDTO.getHospitalDto().setKey(hospital.getKey());
		 * newIntimationDTO.getHospitalDto().setAddress(hospital.getAddress());
		 * newIntimationDTO.getHospitalDto().setHospitalCode(
		 * hospital.getHospitalCode());
		 * newIntimationDTO.getHospitalDto().setCity(hospital.getCity());
		 * newIntimationDTO.getHospitalDto().setName(hospital.getName());
		 * if(hospital.getHospitalType()!=null){
		 * //newIntimationDTO.getHospitalDto().setNetworkHospitalType(); } }
		 

		return newIntimationDTO;
	}*/

	@SuppressWarnings({ "unchecked", })
	public OrganaizationUnit getOrgUnitName(String a_organizationUnitId) {

		Query query = entityManager
				.createNamedQuery("OrganaizationUnit.findByOrgUnitId");
		query = query.setParameter("parentKey", a_organizationUnitId);

		List<OrganaizationUnit> OrganaizationUnit = (List<OrganaizationUnit>) query
				.getResultList();
		OrganaizationUnit orgunit = new OrganaizationUnit();
		for (OrganaizationUnit org : OrganaizationUnit) {
			orgunit = org;
		}

		return orgunit;
	}
	
	/**
	 * The below is nowhere reffered in code. Hence commented the same.
	 * 
	 * */

//	@SuppressWarnings({ "unchecked"})
//	public BeanItemContainer<TmpProductCondition> getProductByType(
//			String a_productName, String a_productType) {
//
//		Query query = entityManager
//				.createNamedQuery("TmpProductCondition.findByCodeAndType");
//		query = query.setParameter("productCode", a_productName);
//		query = query.setParameter("productType", a_productType);
//
//		List<TmpProductCondition> productConditionsList = query.getResultList();
//
//		BeanItemContainer<TmpProductCondition> productConditionsContainer = new BeanItemContainer<TmpProductCondition>(
//				TmpProductCondition.class);
//		productConditionsContainer.addAll(productConditionsList);
//
//		return productConditionsContainer;
//	}

	/*public ClaimDtoOld claimToClaimDTO(Claim claim, Hospitals hospital) {
		ClaimDtoOld claimDto = new ClaimDtoOld();
		claimDto.setKey(claim.getKey());
		claimDto.setClaimId(claim.getClaimId());
		claimDto.setCreatedDate(claim.getCreatedDate());
		claimDto.setClaimedAmount(claim.getClaimedAmount());
		claimDto.setProvisionAmount(claim.getProvisionAmount());
		claimDto.setClaimType(claim.getClaimType().getValue());
		claimDto.setClaimStatus(claim.getStatus().getProcessValue());
		claimDto.setCurrencyId(claim.getCurrencyId());

		NewIntimationDto intimationDto = newIntimationToIntimationDTO(claim
				.getIntimation());
		// claimDto.setIntimation(intimationDto);
		claimDto.setNewIntimationDTO(intimationDto);

		return claimDto;
	}*/

	@SuppressWarnings({ "unchecked" })
	public List<Intimation> getIntimationListByPolicy(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"Intimation.findByPolicy").setParameter("policyNo",
				policyNumber);
		List<Intimation> intimationList = (List<Intimation>) findIntimOfPolicy
				.getResultList();
		for (Intimation intimation : intimationList) {
			entityManager.refresh(intimation);
		}
		return intimationList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<ViewTmpIntimation> getTmpIntimationListByPolicy(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"ViewTmpIntimation.findByPolicy").setParameter("policyNo",
				policyNumber);
		List<ViewTmpIntimation> intimationList = (List<ViewTmpIntimation>) findIntimOfPolicy
				.getResultList();
		for (ViewTmpIntimation intimation : intimationList) {
			entityManager.refresh(intimation);
		}
		return intimationList;
	}

	@SuppressWarnings({ "unchecked" })
	public List<OMPIntimation> getOMPIntimationListByPolicy(String policyNumber) {
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"OMPIntimation.findByPolicy").setParameter("policyNumber",
				policyNumber);
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findIntimOfPolicy
				.getResultList();
		for (OMPIntimation intimation : intimationList) {
			entityManager.refresh(intimation);
		}
		return intimationList;
	}

	
	@SuppressWarnings({ "unchecked", })
	public List<Intimation> getIntimationListByInsured(String insuredId) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"Intimation.findByInsuredId").setParameter("insuredId",
				Long.valueOf(insuredId));
		List<Intimation> intimationList = (List<Intimation>) findIntimOfPolicy
				.getResultList();
		return intimationList;
	}
	
	@SuppressWarnings({ "unchecked", })
	public List<ViewTmpIntimation> getViewTmpIntimationListByInsured(String insuredId) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"ViewTmpIntimation.findByInsuredId").setParameter("insuredId",
				Long.valueOf(insuredId));
		List<ViewTmpIntimation> intimationList = (List<ViewTmpIntimation>) findIntimOfPolicy
				.getResultList();
		return intimationList;
	}

	@SuppressWarnings({ "unchecked", })
	public List<OMPIntimation> getOmpIntimationListByInsured(String insuredId) {
		Query findIntimOfPolicy = entityManager.createNamedQuery(
				"OMPIntimation.findByInsuredId").setParameter("insuredNumber",
				Long.valueOf(insuredId));
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findIntimOfPolicy
				.getResultList();
		return intimationList;
	}
	
//	public Address getPolicyAddress(BigDecimal objectKey) {
//		// Query findAll =
//		// entityManager.createNamedQuery("CityTownVillage.findAll");
//		Query findAll = entityManager.createNamedQuery("Address.findByKey")
//				.setParameter("objectKey", objectKey)
//				.setParameter("objectType", "OrganizationUnit");
//
//		if (findAll.getResultList().size() > 0) {
//			return (Address) findAll.getSingleResult();
//		}
//
//		return null;
//	}
	


	public Policy getByPolicyNumber(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findAll = entityManager.createNamedQuery(
				"Policy.findByPolicyNumber").setParameter("policyNumber",
				policyNumber);

		if (findAll.getResultList().size() > 0) {
			return (Policy) findAll.getSingleResult();
		}
		return null;
	}
	
	public PolicySource getByPolicySource(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findAll = entityManager.createNamedQuery(
				"PolicySource.findByPolicyNumber").setParameter("policyNumber",
				policyNumber);

		if (findAll.getResultList().size() > 0) {
			return (PolicySource) findAll.getSingleResult();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<PolicyCondition> getProductConditionByType(
			String productCode) {

		Query query = entityManager
				.createNamedQuery("PolicyCondition.findByProductCode");
		query = query.setParameter("productCode", productCode);
		

		List<PolicyCondition> productConditionsList = query.getResultList();

		BeanItemContainer<PolicyCondition> productConditionsContainer = new BeanItemContainer<PolicyCondition>(
				PolicyCondition.class);
		productConditionsContainer.addAll(productConditionsList);

		return productConditionsContainer;
	}
	
	
	public BeanItemContainer<PolicyCondition> getProductConditionByKey(
			Long productKey) {

		Query query = entityManager
				.createNamedQuery("PolicyCondition.findByProduct");
		query = query.setParameter("productKey", productKey);
		

		List<PolicyCondition> productConditionsList = query.getResultList();

		BeanItemContainer<PolicyCondition> productConditionsContainer = new BeanItemContainer<PolicyCondition>(
				PolicyCondition.class);
		productConditionsContainer.addAll(productConditionsList);

		return productConditionsContainer;
	}
	
	public BeanItemContainer<PolicyCondition> getProductConditionByVersion(
			Long productKey,Long versionNumber) {

		Query query = entityManager
				.createNamedQuery("PolicyCondition.findByProductCodeWithVersion");
		query = query.setParameter("productkey", productKey);
		query.setParameter("productversionNumber", versionNumber);

		List<PolicyCondition> productConditionsList = query.getResultList();

		BeanItemContainer<PolicyCondition> productConditionsContainer = new BeanItemContainer<PolicyCondition>(
				PolicyCondition.class);
		productConditionsContainer.addAll(productConditionsList);

		return productConditionsContainer;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<PolicyCondition> getProductConditionByTypeWithVersion(
			Long productKey,Long versionNumber) {

		Query query = entityManager
				.createNamedQuery("PolicyCondition.findByVersion");
		query = query.setParameter("productKey", productKey);
		query.setParameter("productversionNumber", versionNumber);
		

		List<PolicyCondition> productConditionsList = query.getResultList();

		BeanItemContainer<PolicyCondition> productConditionsContainer = new BeanItemContainer<PolicyCondition>(
				PolicyCondition.class);
		productConditionsContainer.addAll(productConditionsList);

		return productConditionsContainer;
	}
	
	public Product getProductByKey(Long productKey) {

		if (productKey != null) {
			return entityManager.find(Product.class, productKey);
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<InsuredPedDetails> getPEDByInsured(Long insuredKey){
		 Query query = entityManager.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		 query = query.setParameter("insuredKey", insuredKey);		        
		 List<InsuredPedDetails> insuredList  = query.getResultList();			     
		return insuredList;

	}
	
//	public List<TmpPED> getTmpPED(Long insuredKey){
//		 Query query = entityManager.createNamedQuery("TmpPED.findByinsuredKey");
//		 query = query.setParameter("insuredKey", insuredKey);		        
//		 List<TmpPED> insuredPEdList  = query.getResultList();			     
//		return insuredPEdList;
//
//	}
	
//	public List<TmpEndorsementDetails> getTmpEndorsementList(String strPolicyNo){
//		 Query query = entityManager.createNamedQuery("TmpEndorsementDetails.findByPolicyNo");
//		 query = query.setParameter("policyNumber", strPolicyNo);		        
//		 List<TmpEndorsementDetails> tmpEndorsementDetailsList  = query.getResultList();			     
//		return tmpEndorsementDetailsList;
//
//	}
	
	public List<PolicyEndorsementDetails> getEndorsementList(String strPolicyNo) {
		 Query query = entityManager.createNamedQuery("PolicyEndorsementDetails.findByPolicyNo");
		 query = query.setParameter("policyNumber", strPolicyNo);
		 List<PolicyEndorsementDetails> endorsmentList = new ArrayList<PolicyEndorsementDetails>();
		 
		 List<PolicyEndorsementDetails> endorsementDetailsList  = query.getResultList();
		 for (PolicyEndorsementDetails policyEndorsementDetails : endorsementDetailsList) {
			if(policyEndorsementDetails.getEndorsementNumber() != null && ! policyEndorsementDetails.getEndorsementNumber().equals("0")){
				endorsmentList.add(policyEndorsementDetails);
			}
		}
		return endorsmentList;

	}
	
	public List<PolicyEndorsementDetails> getPolicyEndorsementList(String strPolicyNo) {
		 Query query = entityManager.createNamedQuery("PolicyEndorsementDetails.findByPolicyNo");
		 query = query.setParameter("policyNumber", strPolicyNo);
		 List<PolicyEndorsementDetails> endorsmentList = new ArrayList<PolicyEndorsementDetails>();
		 
		 List<PolicyEndorsementDetails> endorsementDetailsList  = query.getResultList();
		 for (PolicyEndorsementDetails policyEndorsementDetails : endorsementDetailsList) {
			if(policyEndorsementDetails.getEndorsementNumber() != null && ! policyEndorsementDetails.getEndorsementNumber().equals("0")){
				endorsmentList.add(policyEndorsementDetails);
			}
		}
		return endorsmentList;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(Policy policy,List<PreviousPolicy> previousPolicyList, List<PolicyEndorsementDetails> policyEndorsementList)
	{
		List<Insured> insuredDetails = policy.getInsured();
		if (null != policy && null == policy.getKey()) 
		{
			entityManager.persist(policy);
			entityManager.flush();
		}	
		Policy policy2 = getPolicy(policy.getPolicyNumber());
			if(null != insuredDetails && !insuredDetails.isEmpty())
			{
				
				Insured jetMainMember = null;
				
				for (Insured  objInsured : insuredDetails)  {
					objInsured.setPolicy(policy2);
					/*if(policy2.getDeductibleAmount() != null){
						objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
					}*/
					
					//IMSSUPPOR-27387 && Key added for IMSSUPPOR-29029 - 17-07-2019
					if((policy2.getProductType() == null) || (policy2.getProductType() != null && policy2.getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY))){
						if(policy2.getDeductibleAmount() != null){
							objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
						}
					}
					
					objInsured.setLopFlag("H");
					
					/*Below Code for product jet group - MED-PRD-073*/
					if(policy.getProduct().getCode() != null && (policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
							|| policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
							|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE))){
						if(jetMainMember == null) {
							jetMainMember = getJetMainMemberInsured(policy);
						}
						if(objInsured.getMainMember() != null && objInsured.getMainMember().equalsIgnoreCase(SHAConstants.N_FLAG)){
							if(jetMainMember != null) {
								objInsured.setDependentRiskId(jetMainMember.getInsuredId() != null ? jetMainMember.getInsuredId() : null);
							}
						}
					}
					
					if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
						objInsured.setInsuredPinCode(null);
					}
					if(objInsured != null && objInsured.getKey() != null){
						entityManager.merge(objInsured);
						entityManager.flush();
					}else{
						entityManager.persist(objInsured);
						entityManager.flush();
					}
					if(objInsured.getInsuredPedList() != null && !objInsured.getInsuredPedList().isEmpty()) {
						for(InsuredPedDetails insuredPed : objInsured.getInsuredPedList()){
							insuredPed.setInsuredKey(objInsured.getInsuredId());
							String pedDescription = insuredPed.getPedDescription();
							if(pedDescription != null && pedDescription.length() >300) {
								pedDescription = pedDescription.substring(0, 299);
								insuredPed.setPedDescription(pedDescription);
							}
							if(insuredPed.getKey() == null){
								entityManager.persist(insuredPed);
								entityManager.flush();
							}
						}
						
					}
					
					if(objInsured.getNomineeDetails() != null && ! objInsured.getNomineeDetails().isEmpty()) {
						for (NomineeDetails nomineeDetail : objInsured.getNomineeDetails()) {
							nomineeDetail.setInsured(objInsured);
							if(nomineeDetail.getKey() == null){
								entityManager.persist(nomineeDetail);
								entityManager.flush();
							} else {
								entityManager.merge(nomineeDetail);
								entityManager.flush();
							}
						}
					}
					
					if(objInsured.getProposerInsuredNomineeDetails() != null && ! objInsured.getProposerInsuredNomineeDetails().isEmpty()){
						for (PolicyNominee nomineeDetail : objInsured.getProposerInsuredNomineeDetails()) {
							nomineeDetail.setPolicy(objInsured.getPolicy());
							nomineeDetail.setInsured(objInsured);
							if(nomineeDetail.getKey() == null){
								nomineeDetail.setActiveStatus(1);
								nomineeDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
								nomineeDetail.setCreatedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.persist(nomineeDetail);
								entityManager.flush();
							}else{
								nomineeDetail.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
								nomineeDetail.setModifiedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.merge(nomineeDetail);
								entityManager.flush();
							}
						}
					}
					
					if(objInsured.getPortablityPolicy() != null && ! objInsured.getPortablityPolicy().isEmpty()){
						for (PortablityPolicy portablityPolicy : objInsured.getPortablityPolicy()) {
								portablityPolicy.setActiveStatus(1l);
								portablityPolicy.setInsuredName(objInsured.getInsuredName());
							if(portablityPolicy.getKey() == null && portablityPolicy.getPolicyNumber() != null){
								portablityPolicy.setCurrentPolicyNumber(policy2.getPolicyNumber());
								entityManager.persist(portablityPolicy);
								entityManager.flush();
							}else if(portablityPolicy.getKey() != null && portablityPolicy.getPolicyNumber() != null) {
								entityManager.merge(portablityPolicy);
								entityManager.flush();
							}
						}
					}
					
					/*JetContinuityBenefits*/
					if(objInsured.getGmcContBenefitDtls() != null && !objInsured.getGmcContBenefitDtls().isEmpty()){
						for (GmcContinuityBenefit continuityBenefit : objInsured.getGmcContBenefitDtls()) {
							continuityBenefit.setPolicy(policy2);
							continuityBenefit.setInsured(objInsured);
							if(continuityBenefit.getKey() == null){
								continuityBenefit.setActiveFlag(SHAConstants.YES_FLAG);
								continuityBenefit.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
								continuityBenefit.setCreatedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.persist(continuityBenefit);
								entityManager.flush();
							} else {
								continuityBenefit.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
								continuityBenefit.setModifiedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.merge(continuityBenefit);
								entityManager.flush();
							}
						
						}
					}
					
					/*For JET GOLD POLICY
					List<InsuredCover> coverDetailsForPA = objInsured.getCoverDetailsForPA();
					
					if(null != coverDetailsForPA && ! coverDetailsForPA.isEmpty()){
						for (InsuredCover insuredCover : coverDetailsForPA) {
							insuredCover.setInsuredKey(objInsured.getKey());
							if(insuredCover.getKey() != null){
								entityManager.merge(insuredCover);
								entityManager.flush();
							}else{
								entityManager.persist(insuredCover);
								entityManager.flush();
							}
						}
					}*/
				}
			}
			
			if(policy.getInsuredPA() != null && ! policy.getInsuredPA().isEmpty()){
				for (Insured insuredPA : policy.getInsuredPA()) {
					insuredPA.setPolicy(policy2);
					if(policy2.getDeductibleAmount() != null){
						insuredPA.setDeductibleAmount(policy2.getDeductibleAmount());
					}
					if(insuredPA != null){
						entityManager.persist(insuredPA);
						entityManager.flush();
						
						entityManager.refresh(insuredPA);
					}
					
					
					if(insuredPA.getCoverDetailsForPA() != null && ! insuredPA.getCoverDetailsForPA().isEmpty()){
						for (InsuredCover insuredCoverPA : insuredPA.getCoverDetailsForPA()) {
							insuredCoverPA.setInsuredKey(insuredPA.getKey());
							if(insuredCoverPA.getKey() == null){
								entityManager.persist(insuredCoverPA);
								entityManager.flush();
							} else {
								entityManager.merge(insuredCoverPA);
								entityManager.flush();
							}
						}
					}
					
					if(insuredPA.getNomineeDetails() != null && ! insuredPA.getNomineeDetails().isEmpty()) {
						for (NomineeDetails nomineeDetail : insuredPA.getNomineeDetails()) {
							nomineeDetail.setInsured(insuredPA);
							if(nomineeDetail.getKey() == null){
								entityManager.persist(nomineeDetail);
								entityManager.flush();
							} else {
								entityManager.merge(nomineeDetail);
								entityManager.flush();
							}
						}
					}
					
					if(insuredPA.getProposerInsuredNomineeDetails() != null && ! insuredPA.getProposerInsuredNomineeDetails().isEmpty()){
						for (PolicyNominee nomineeDetail : insuredPA.getProposerInsuredNomineeDetails()) {
							nomineeDetail.setPolicy(insuredPA.getPolicy());
							nomineeDetail.setInsured(insuredPA);
							if(nomineeDetail.getKey() == null){
								nomineeDetail.setActiveStatus(1);
								nomineeDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
								nomineeDetail.setCreatedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.persist(nomineeDetail);
								entityManager.flush();
							}else{
								nomineeDetail.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
								nomineeDetail.setModifiedDate(new Timestamp(System
										.currentTimeMillis()));
								entityManager.merge(nomineeDetail);
								entityManager.flush();
							}
						}
					}
					
				}
				

			}
			// BankDetails Added for OP
//			Policy policy2 = getPolicy(policy.getPolicyNumber());
			List<PolicyBankDetails> policyBankDetails = policy.getPolicyBankDetails();
			if(policyBankDetails != null && !policyBankDetails.isEmpty()){
				for (PolicyBankDetails policyBankDetails2 : policyBankDetails) {
					policyBankDetails2.setPolicyKey(policy2.getKey());
					if(policyBankDetails2.getKey() == null){
						entityManager.persist(policyBankDetails2);
						entityManager.flush();
					} else {
						entityManager.merge(policyBankDetails2);
						entityManager.flush();
					}
				}
			}
			
			List<PolicyRiskCover> policyRiskCoverDetails = policy.getPolicyRiskCoverDetails();
			
			if(policyRiskCoverDetails != null && !policyRiskCoverDetails.isEmpty()){
				for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
					policyRiskCover.setPolicyKey(policy2.getKey());
					if(policyRiskCover.getKey() == null){
						entityManager.persist(policyRiskCover);
						entityManager.flush();
					} else {
						entityManager.merge(policyRiskCover);
						entityManager.flush();
					}
				}
			}
			
			List<PolicyCoverDetails> policyCoverDetails = policy.getPolicyCoverDetails();
			
			if(policyCoverDetails != null && !policyCoverDetails.isEmpty()){
				for (PolicyCoverDetails policyRiskCover : policyCoverDetails) {
					policyRiskCover.setPolicyKey(policy2.getKey());
					if(policyRiskCover.getKey() == null){
						entityManager.persist(policyRiskCover);
						entityManager.flush();
					} else {
						entityManager.merge(policyRiskCover);
						entityManager.flush();
					}
				}
			}
			
			/*Below code for MED-PRD-073*/
			if(policy.getProduct().getCode() != null && (policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
					|| policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
					|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE))){
				List<MasAilmentLimit> ailmentDetails = policy.getAilmentDetails();
				if(ailmentDetails != null){
					for (MasAilmentLimit masAilmentLimit : ailmentDetails) {
						masAilmentLimit.setPolicyKey(policy2.getKey());
						if(masAilmentLimit.getKey() == null){
							entityManager.persist(masAilmentLimit);
							entityManager.flush();
						}else{
							entityManager.merge(masAilmentLimit);
							entityManager.flush();
						}
					}
				}
				List<MasCopayLimit> copayLimit = policy.getCopayLimit();
				if(copayLimit != null){
					for (MasCopayLimit copayLimit1 : copayLimit) {
						copayLimit1.setPolicyKey(policy2.getKey());
						if(copayLimit1.getKey() == null){
							entityManager.persist(copayLimit1);
							entityManager.flush();
						}else{
							entityManager.merge(copayLimit);
							entityManager.flush();
						}
					}
				}
				List<MasDeliveryExpLimit> deliveryExpLimit = policy.getDeliveryExpLimit();
				if(deliveryExpLimit != null){
					for (MasDeliveryExpLimit masDeliveryLimit : deliveryExpLimit) {
						masDeliveryLimit.setPolicyKey(policy2.getKey());
						if(masDeliveryLimit.getKey() == null){
							entityManager.persist(masDeliveryLimit);
							entityManager.flush();
						}else{
							entityManager.merge(masDeliveryLimit);
							entityManager.flush();
						}
					}
				}
				List<MasPrePostHospLimit> prePostLimit = policy.getPrePostLimit();
				if(prePostLimit != null){
					for (MasPrePostHospLimit masPrepostLimt : prePostLimit) {
						masPrepostLimt.setPolicyKey(policy2.getKey());
						if(masPrepostLimt.getKey() == null){
							entityManager.persist(masPrepostLimt);
							entityManager.flush();
						}else{
							entityManager.merge(masPrepostLimt);
							entityManager.flush();
						}
					}
				}
				List<MasRoomRentLimit> roomRentLimit = policy.getRoomRentLimit();
				
				if(roomRentLimit != null){
					for (MasRoomRentLimit masRoomRentLimit : roomRentLimit) {
						masRoomRentLimit.setPolicyKey(policy2.getKey());
						if(masRoomRentLimit.getKey() == null){
							entityManager.persist(masRoomRentLimit);
							entityManager.flush();
						}else{
							entityManager.merge(masRoomRentLimit);
							entityManager.flush();
						}
					}
				}
				
				List<GpaBenefitDetails> gpaBenefitDetails = policy.getGpaBenefitDetails();
				if(gpaBenefitDetails != null && !gpaBenefitDetails.isEmpty()){
					for (GpaBenefitDetails gpaBenefitDetls : gpaBenefitDetails) {
						gpaBenefitDetls.setPolicyKey(policy2.getKey());
						if(gpaBenefitDetls.getKey() == null){
							entityManager.persist(gpaBenefitDetls);
							entityManager.flush();
						} else {
							entityManager.merge(gpaBenefitDetls);
							entityManager.flush();
						}
					}
				}
			}
			
			List<PolicyNominee> policyProposerDetails = policy.getProposerNomineeDetails();
			if(policyProposerDetails != null && !policyProposerDetails.isEmpty()){
				for (PolicyNominee policyProposerDetail : policyProposerDetails) {
					policyProposerDetail.setPolicy(policy2);
					if(policyProposerDetail.getKey() == null){
						policyProposerDetail.setActiveStatus(1);
						policyProposerDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						policyProposerDetail.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.persist(policyProposerDetail);
						entityManager.flush();
					} else {
						policyProposerDetail.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						policyProposerDetail.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(policyProposerDetail);
						entityManager.flush();
					}
				}
			}
			
			if(null != previousPolicyList && !previousPolicyList.isEmpty()){
				
				//Query query = entityManager.createNamedQuery("PreviousPolicy.findByCurrentPolicy").setParameter("policyNumber", policy.getPolicyNumber());
				//List<PreviousPolicy> existingList = query.getResultList();
				for(PreviousPolicy prevPolicy : previousPolicyList)
				{
					 
					/**
					 * Combination of previous policy no and current policy number
					 * existence in previous policy table is validated. If this 
					 * combination is not present, then record is inserted in 
					 * previous policy table. If present, then this
					 * previous policy record is skipped.
					 * */
					
					if(!isPreviousPolicyExisting(prevPolicy.getPolicyNumber(),policy.getPolicyNumber()))
					{
					
					 if(policy.getPolicyNumber() != null){
						 prevPolicy.setCurrentPolicyNumber(policy.getPolicyNumber());
					 }
					 
					 if(prevPolicy.getProposerAddress() != null && prevPolicy.getProposerAddress().length() > 200) {
						 prevPolicy.setProposerAddress(prevPolicy.getProposerAddress().substring(0, 200));
					 }
					 
					 entityManager.persist(prevPolicy); 
					 entityManager.flush();	 
					 
				}
					 
					 
					 
					 /*  if(( existingList == null || existingList.isEmpty() ) && prevPolicy.getKey() == null){
						 entityManager.persist(prevPolicy); 
						 entityManager.flush();	 
					 }
					if(existingList != null && !existingList.isEmpty() && previousPolicyList.size() > existingList.size()){
						 
						 if(!existingList.contains(prevPolicy) && prevPolicy.getKey() == null ){
							 entityManager.persist(prevPolicy); 
							 entityManager.flush();							 
						 } 
						 
					 }*/
					
				}
			}
			if(null !=policyEndorsementList && !policyEndorsementList.isEmpty()){
				for (PolicyEndorsementDetails policyEndorsementDetails : policyEndorsementList) {
					//String endorsementText = policyEndorsementDetails.getEndorsementText();
//					if(endorsementText != null){
//						endorsementText = endorsementText.substring(0, 900);
//						policyEndorsementDetails.setEndorsementText(endorsementText);
//					}
					policyEndorsementDetails.setPolicy(policy);
					entityManager.persist(policyEndorsementDetails);
					entityManager.flush();
				}
			}
			
			
		}
	
	
	/***
	 * Added as a fix for production issue. Duplicate entries 
	 * are present in previous policy table. To avoid this, 
	 * combination of current policy and previous policy no
	 * existence needs to be validated. 
	 */
	
	private Boolean isPreviousPolicyExisting(String policyNumber,String currentPolicyNo)
	{
		Query query = entityManager.createNamedQuery("PreviousPolicy.findByPreviousPolicyAndCurrentPolicyNo");
		query = query.setParameter("policyNumber", policyNumber);
		query = query.setParameter("currentPolicyNumber", currentPolicyNo);
		List<PreviousPolicy> prevPolicyList = query.getResultList();
		if(null != prevPolicyList && !prevPolicyList.isEmpty())
		{
			return true;
		}
		return false;
	}
	
	
	public void createPolicyFromPremia(Policy policy,List<PreviousPolicy> previousPolicyList, List<PolicyEndorsementDetails> policyEndorsementList,EntityManager entityManager)
	{
		this.entityManager = entityManager;
		create(policy, previousPolicyList,policyEndorsementList);
	}

	
	public Product getProductByProductCode(String productCode){
			
		 Product product = null;
		 Query query = entityManager.createNamedQuery("Product.findByCode");
		 query = query.setParameter("productCode", productCode);	
		 List resultList = query.getResultList();
		 if(resultList != null && !resultList.isEmpty()) {
			 product = (Product) query.getResultList().get(0);	
		 } else {
			 System.out.println("This product is not available in Galaxy------------->" + productCode);
		 }
				
		 return product;
	}
	
	public Product getProductByProductCodeForPremia(String productCode, EntityManager entityManager)
	{
		this.entityManager = entityManager;
		return getProductByProductCode(productCode);
	}
	
	public Intimation getIntimationByKey(Long intimationKey)
	{
		Query query = entityManager.createNamedQuery("Intimation.findByKey");
		query = query.setParameter("intiationKey", intimationKey);
		List<Intimation> intimationList = query.getResultList();
		if(null != intimationList && !intimationList.isEmpty())
		{
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	public List<Insured> getInsuredListForPolicy(Long policyKey,Long insuredId)
	{
		Query query = entityManager.createNamedQuery("Insured.findInsuredByPolicyKey");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("insuredId", insuredId);
		List<Insured> insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty())
		{
			return insuredList;
		}
		return null;
	}
	
	public MASClaimAdvancedProvision  getClaimAdvProvision(Long branchCode) {
		
		Query query = entityManager.createNamedQuery("MASClaimAdvancedProvision.findByBranchCode");
    	query = query.setParameter("branchCode", branchCode);
    	
    	List<MASClaimAdvancedProvision> claimAdvProvsionList = (List<MASClaimAdvancedProvision>) query.getResultList();
    	if (claimAdvProvsionList != null && !claimAdvProvsionList.isEmpty()){
    		return claimAdvProvsionList.get(0);
    	}
		return null;
	}

	public void updatePanCardDetails(Policy byPolicyNumber) {
		entityManager.merge(byPolicyNumber);
		entityManager.flush();
		
	}
	
	public void updateOPAllowIntimation(Policy byPolicyNumber) {
		entityManager.merge(byPolicyNumber);
		entityManager.flush();
		
	}
	
	public PremPolicySchedule fetchPolicyScheduleFromPremia(String values, int endIdx)
	{
		PremPolicySchedule policyDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(values);
			//System.out.println("--the policy nu---"+policy.getPolicyNo());
//			Builder builder = PremiaService.getInstance().getPolicyScheduleDetail();
//			policyDetails= builder.post(new GenericType<PremPolicySchedule>() {}, "\""+policy.getPolicyNo()+ "\"");
//			policyDetails= builder.post(new GenericType<PremPolicySchedule>() {}, "{\"PolicyNumber\":\""+policy.getPolicyNo()+"\", \"EndIdx\":\""+endIdx+"\"}");
			
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;

			if (policy.getPolicyNo() != null) {
				policyObj = getByPolicyNumber(policy.getPolicyNo());
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						policyDetails = new PremPolicySchedule();
						String url = BPMClientContext.BANCS_POLICY_SCHEDULE_VIEW_URL;
						url = url.replace("POLICY_NUMBER", values);		
						url = url.replace("MEMBER", "");
						if(endIdx <= 0){
							url = url.replace("DOC_TYPE", "POLICY_SCHEDULE");
							url = url.replace("VERSION", String.valueOf(endIdx));
							url = url.replace("ENO", "");
							policyDetails.setResultUrl(url);

						}else{
							url = url.replace("DOC_TYPE", "ENDORSEMENT_SCHEDULE");
							url = url.replace("VERSION", "");
							url = url.replace("ENO", String.valueOf(endIdx));
							policyDetails.setResultUrl(url);
						}							
					}else{
						builder = PremiaService.getInstance().getPolicyScheduleDetail();
						policyDetails= builder.post(new GenericType<PremPolicySchedule>() {}, "{\"PolicyNumber\":\""+policy.getPolicyNo()+"\", \"EndIdx\":\""+endIdx+"\"}");
					}
				}
			}
			
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getPolicyScheduleDetail();
//			policyDetails= builder.post(new GenericType<PremPolicySchedule>() {}, "\""+policy.getPolicyNo()+ "\"");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return policyDetails;
	}

	public Policy getByPolicyLikeNumber(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findAll = entityManager.createNamedQuery(
				"Policy.findByLikePolicyNumber").setParameter("policyNumber",
				policyNumber);

		if (findAll.getResultList().size() > 0) {
			return (Policy) findAll.getSingleResult();
		}
		return null;
	}
	
	public List<PolicyRiskCover> getRiskCoverByPolicy(Long policyKey)
	{
		Query query = entityManager.createNamedQuery("PolicyRiskCover.findByPolicy");
		query = query.setParameter("policyKey", policyKey);
		List<PolicyRiskCover> riskCoverList = query.getResultList();
		if(null != riskCoverList && !riskCoverList.isEmpty())
		{
			return riskCoverList;
		}
		return null;
	}
	
	public void updatePolicyBonusDetails(String policyNumber){
		PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(policyNumber);
		Policy policyByPolicyNubember = getPolicyByPolicyNubember(policyNumber);
		if(policyByPolicyNubember != null && null != policyDetails && policyDetails.getCumulativeBonus() != null && ! policyDetails.getCumulativeBonus().isEmpty()){
			policyByPolicyNubember.setCummulativeBonus(Double.valueOf(policyDetails.getCumulativeBonus()));
			entityManager.merge(policyByPolicyNubember);
			entityManager.flush();
		}
	}
	
	public Policy getPolicyByMasterPolicyNumber(String masterPolicyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("masterPolicyNumber", masterPolicyNumber);
		if(query.getResultList().size()!=0){
		Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}
	
	public Boolean getPolicyValidationObject(String policyNumber) {
		
		Boolean isValidationAvailable = Boolean.FALSE;
		Query query = entityManager
				.createNamedQuery("PolicyValidation.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);
		List<PolicyValidation> policyValidationObjectList = query.getResultList();
		
		if (null != policyValidationObjectList	&& !policyValidationObjectList.isEmpty()) {
			
			isValidationAvailable = Boolean.TRUE;
		}
		return isValidationAvailable;
	}
	
	
	public List<ZUAQueryHistoryTable> getZUAQueryHistoryDetails(String policyNumber){	
		
		Query query = entityManager
				.createNamedQuery("ZUAQueryHistoryTable.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);	
		List<ZUAQueryHistoryTable> zuaQueryList = query.getResultList();
		
		if(zuaQueryList != null && !zuaQueryList.isEmpty()){
			return zuaQueryList;
		}	
				
		return zuaQueryList;
	}
	
	
	public List<ZUASendQueryTable> getZUAQueryDetails(String policyNumber){
		
		Query query = entityManager
				.createNamedQuery("ZUASendQueryTable.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);	
		List<ZUASendQueryTable> zuaQueryList = query.getResultList();
		
		if(zuaQueryList != null && !zuaQueryList.isEmpty()){
			/*for (ZUASendQueryTable zuaQueryTable : zuaQueryList) {
				
				Query zuaMasterquery = entityManager
						.createNamedQuery("ZUAMasterQueryDetails.findByQueryCode");
				zuaMasterquery = zuaMasterquery.setParameter("queryCode", zuaQueryTable.getQueryCode());	
				List<ZUAMasterQueryDetails> zuaMasterQueryList = zuaMasterquery.getResultList();
				
				if(null != zuaMasterQueryList && !zuaMasterQueryList.isEmpty()){*/
					
					return zuaQueryList;
				}
			//}
			
		//}
		return null;
	}
	
	public List<Object[]> zuaQueryHistoryDetails(String policyNo){
//		entityManager.merge(claim);
//		entityManager.flush();
		
		String query = "SELECT * FROM  ZUC_QUERY_DETAILS  WHERE ZUQ_POLICY_NO =" + "'"+policyNo+"'" + "ORDER BY ZUQ_SR_NO ASC" ;
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<Object[]> resultList = (List<Object[]>)nativeQuery.getResultList();
		
		return resultList;
		 

	}
	
	public List<Insured> getInsuredListForGMC(Long dependentRiskID){
		Query query = entityManager.createNamedQuery("Insured.findByInsuredDependentRiskID");
		query = query.setParameter("dependentRiskId", dependentRiskID);
		List<Insured> insuredList = new ArrayList<Insured>();
		insuredList = query.getResultList();
		
		return insuredList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PreExistingDisease> getPedList(Long insuredKey) {
		
		List<PreExistingDisease> pedDescList = null;
		try{
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.PED_APPROVED);
		statusList.add(ReferenceTable.ENDORSEMENT_PROCESSING);

		
		Query query = entityManager.createNamedQuery("NewInitiatePedEndorsement.findByInsuredKey");
		query = query.setParameter("insuredKey", insuredKey);
		query = query.setParameter("statusList", statusList);
		List<String> pedList = query.getResultList();
		pedList.removeAll(Collections.singleton(null));
		
		if(null != pedList && !pedList.isEmpty())
		{
			Query masQuery = entityManager.createNamedQuery("PreExistingDisease.findKeyList");
			masQuery = masQuery.setParameter("codeList", pedList);
			pedDescList = masQuery.getResultList();
			
		}}catch(Exception e){
			e.printStackTrace();
		}
		return pedDescList;
	}
	public List<PolicyCoverDetails> getPolicyCoverDetails(Long policyKey) {
		Query query = entityManager
				.createNamedQuery("PolicyCoverDetails.findByPolicy");
		query = query.setParameter("policyKey", policyKey);
		List<PolicyCoverDetails> insuredList = query.getResultList();
		/*for (Insured insured : insuredList) {
=======
		 Query query = this.entityManager.createNamedQuery("Insured.findByPolicyNo");
		 query = query.setParameter("policyNumber", policyNo);		        
		 List<Insured> insuredList  = query.getResultList();
		 for (Insured insured : insuredList) {
			entityManager.refresh(insured);
		}*/
		return insuredList;

	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public UnFreezHospitals getUnFreezVWHospitalByKey(Long hospitalKey) {
		if (hospitalKey != null) {
			return entityManager.find(UnFreezHospitals.class, hospitalKey);
		}
		return null;
	}
	public Insured getInsuredByPolicyAndInsuredName(String policyNo , Long insuredId) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredId");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredId)
		query = query.setParameter("insuredId", insuredId);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			return insuredList.get(0);
		}
		
		return null;
	}

	public PremPolicySchedule fetchPolicyScheduleWithoutRiskFromPremia(String values, int endIdx, String printRiskNo)
	{
		PremPolicySchedule policyDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(values);
			Policy policyObj = null;
			Builder builder = null;

			if (policy.getPolicyNo() != null) {
				policyObj = getByPolicyNumber(policy.getPolicyNo());
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						policyDetails = new PremPolicySchedule();
						String url = BPMClientContext.BANCS_POLICY_SCHEDULE_VIEW_URL;
						url = url.replace("POLICY_NUMBER", values);		
						url = url.replace("MEMBER", "");
						if(endIdx <= 0){
							url = url.replace("DOC_TYPE", "POLICY_SCHEDULE");
							url = url.replace("VERSION", String.valueOf(endIdx));
							url = url.replace("ENO", "");
							policyDetails.setResultUrl(url);

						}else{
							url = url.replace("DOC_TYPE", "ENDORSEMENT_SCHEDULE");
							url = url.replace("VERSION", "");
							url = url.replace("ENO", String.valueOf(endIdx));
							policyDetails.setResultUrl(url);
						}							
					}else{
						builder = PremiaService.getInstance().getPolicyScheduleWithoutRiskDetail();
						policyDetails= builder.post(new GenericType<PremPolicySchedule>() {}, "{\"PolicyNumber\":\""+policy.getPolicyNo()+"\", \"EndIdx\":\""+endIdx+"\", \"PrintRiskYesNo\":\""+printRiskNo+"\"}");
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return policyDetails;
	}

	public LegalBillingDTO getLegalBillingDetails(PreauthDTO bean){
		
		LegalBillingDTO legalBillingDTO = new LegalBillingDTO();
		String intimationNo = bean.getNewIntimationDTO().getIntimationId();
		legalBillingDTO.setInterestApplicable(isinterestApplicable(intimationNo));
		if(bean.getPanNumber() !=null){
			legalBillingDTO.setPanDetails(true);
			legalBillingDTO.setPanNo(bean.getPanNumber());
			legalBillingDTO = dbCalculationService.getInterestOtherClaimForTaxDeductions(legalBillingDTO,bean.getKey());
		}else{
			legalBillingDTO.setPanDetails(false);
		}
		
		return legalBillingDTO;		
	}
	
	public Boolean isinterestApplicable(String intimationNo){
		
		Query query = entityManager.createNamedQuery("LegalConsumer.findByIntimationNumber");
		query = query.setParameter("intimationNumber", intimationNo);
		List<LegalConsumer> consumer = query.getResultList();
		if(consumer !=null
				&& !consumer.isEmpty()){
			return true;
		}else{
			Query query1 = entityManager.createNamedQuery("LegalOmbudsman.findByIntimationNumber");
			query1 = query1.setParameter("intimationNumber", intimationNo);
			List<LegalOmbudsman> ombudsman = query1.getResultList();
			if(ombudsman !=null
					&& !ombudsman.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public Insured getJetMainMemberInsured(Policy policy){
			
			if(policy.getInsured() != null && !policy.getInsured().isEmpty()){
				List<Insured> insuredList = policy.getInsured();
				Insured mainInsured = null;
				for (Insured insured : insuredList) {
					if(insured.getMainMember() != null && (insured.getMainMember().equalsIgnoreCase(SHAConstants.YES) || insured.getMainMember().equalsIgnoreCase(SHAConstants.YES_FLAG))){
						mainInsured = insured;
					}
				}
				return mainInsured;
			}
			return null;
		}
	
	public TPolicyMaster getTPolicyMaster(String policyNumber) {

		Query findAll = entityManager.createNamedQuery(
				"TPolicyMaster.findByPolicyNumber").setParameter("policyNumber",
				policyNumber);
		List<TPolicyMaster> resultList = findAll.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
	
	public TPolicyDetails getTPolicyDetails(String partyCode) {

		Query findAll = entityManager.createNamedQuery(
				"TPolicyDetails.findByPartyCode").setParameter("partyCode",
						partyCode);
		List<TPolicyDetails> resultList = findAll.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("static-access")
	public BeanItemContainer<PremPolicy> getBancsPolicySummaryDtls(
			HashMap<String, String> values) {
		BeanItemContainer<PremPolicy> policyListContainer = new BeanItemContainer<PremPolicy>(
				PremPolicy.class);
		
		if (values.get("healthCardNumber") != null) {

		}
		PremPolicySearchDTO policy = new PremPolicySearchDTO();
		policy.setHealthCardNo(values.get("healthCardNumber"));
		policy.setInsuredDOB(values.get("insuredDateOfBirth"));
		policy.setInsuredName(values.get("insuredName"));
		policy.setMobileNo(values.get("registerdMobileNumber"));
		policy.setPolicyNo(values.get("polNo"));
		policy.setProposerDOB(values.get("insuredDateOfBirth"));
		policy.setProposerName(values.get("polAssrName"));
		policy.setReceiptNo(values.get("polReceiptNo"));
		
		Policy policyObj = null;
		Builder builder = null;
		PolicySource policySource = null;
		HashMap<String, Object> policyObjmap = new HashMap<String, Object>();
		List<PremPolicy> post = null;
		policyObjmap.putAll(values);
		String source = null;
		
		if (policy.getPolicyNo() != null || policy.getHealthCardNo() !=null) {
			post = ClaimProvisionService.getInstance().callPolicySummaryService(policyObjmap);
			source = SHAConstants.BANCS_POLICY;
		}

		if(post != null){
			for (PremPolicy premPolicy : post) {
				premPolicy.setPolicySource(source);
			}
			policyListContainer.addAll(post);
		}
		return policyListContainer;

	}
}