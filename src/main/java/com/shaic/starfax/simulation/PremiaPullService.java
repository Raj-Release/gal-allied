
/**
 * 
 */
package com.shaic.starfax.simulation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.cms.TimeStampAndCRL;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.aria.client.SelectedValue;
import com.shaic.arch.PremiaConstants;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.rule.IntimationRule;
import com.shaic.claim.policy.search.ui.BancsRenewedPolicyInformationResponse;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremCoverDetailsForPA;
import com.shaic.claim.policy.search.ui.PremDependentInsuredDetails;
import com.shaic.claim.policy.search.ui.PremGMCContinuityBenefits;
import com.shaic.claim.policy.search.ui.PremGmcBenefitDetails;
import com.shaic.claim.policy.search.ui.PremGmcRoomRentLimit;
import com.shaic.claim.policy.search.ui.PremGpaBenefitDetails;
import com.shaic.claim.policy.search.ui.PremInsuredDetails;
import com.shaic.claim.policy.search.ui.PremInsuredNomineeDetails;
import com.shaic.claim.policy.search.ui.PremInsuredOMPDetails;
import com.shaic.claim.policy.search.ui.PremPEDDetails;
import com.shaic.claim.policy.search.ui.PremPolicyCoverDetails;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPolicyRiskCover;
import com.shaic.claim.policy.search.ui.PremPortability;
import com.shaic.claim.policy.search.ui.PremPortabilityPrevPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPreviousPolicyDetails;
import com.shaic.claim.policy.search.ui.PremRelatedPolicies;
import com.shaic.claim.policy.search.ui.PremiaInsuredPA;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.PremiaToPolicyMapper;
import com.shaic.claim.policy.search.ui.RenewedPolicyDetails;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.policy.search.ui.premia.PremPolicySearchDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.GalaxyCRMIntimation;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.GalaxyIntimationTable;
import com.shaic.domain.GmcContinuityBenefit;
import com.shaic.domain.GmcCoorporateBufferLimit;
import com.shaic.domain.GpaBenefitDetails;
import com.shaic.domain.GpaPolicy;
import com.shaic.domain.HospCashIntimation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.IncurredClaimRatioBatchMonitor;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredCover;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationImportValidation;
import com.shaic.domain.MASClaimAdvancedProvision;
import com.shaic.domain.MasAilmentLimit;
import com.shaic.domain.MasCopayLimit;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasDeliveryExpLimit;
import com.shaic.domain.MasPrePostHospLimit;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OPPremiaIntimationTable;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PaayasPolicy;
import com.shaic.domain.Pellucid;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyBankDetails;
import com.shaic.domain.PolicyCoverDetails;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyRiskCover;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PolicySource;
import com.shaic.domain.PremiaEndorsementTable;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.Product;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.RelatedPolicies;
import com.shaic.domain.StarJioPolicy;
import com.shaic.domain.StarKotakPolicy;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TataPolicy;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ZUASendQueryTable;
import com.shaic.domain.bancs.BaNCSIntimationTable;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PortabilityPreviousPolicy;
import com.shaic.domain.preauth.PortablityPolicy;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PremiaPreviousClaim;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateReminderTableDTO;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.restservices.bancs.consumerDetails.BancsConsumerDetailService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author yosuva.a
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PremiaPullService {
	
	private final Logger log = LoggerFactory.getLogger(PremiaPullService.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbService;
	
	
	public void updatePremiaIntimationTable(PremiaIntimationTable premiaIntimationTable, String message) throws IllegalStateException, SecurityException, SystemException {
		if(null != premiaIntimationTable && null != premiaIntimationTable.getGiIntimationId()){
			try {
				utx.begin();
				premiaIntimationTable.setGiSavedType(message);
				entityManager.merge(premiaIntimationTable);
				entityManager.flush();
				utx.commit();
			} catch(Exception e) {
				utx.rollback();
			}
		}
	}
	
	public void updatePremiaEndorsementTable(PremiaEndorsementTable premiaIntimationTable, String message) throws IllegalStateException, SecurityException, SystemException {
		if(null != premiaIntimationTable && null != premiaIntimationTable.getPolicySysId()){
			try {
				utx.begin();
				if(premiaIntimationTable.getRiskId() != null){
					updatePremiaEndorsementTableWithRiskId(premiaIntimationTable, message);
				}else{
					/*premiaIntimationTable.setReadFlag(message);
					entityManager.merge(premiaIntimationTable);
					entityManager.flush();*/
					updatePremiaEndorsementTableWithoutRiskId(premiaIntimationTable, message);
				}
				utx.commit();
				log.info("@@@@@@@@@ENDORSEMENT BATCH END@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getPolicyNumber()+"-----> " +premiaIntimationTable.getRiskId()+"-----> "+ System.currentTimeMillis());
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING ENDORSEMENT PREMIA TABLE **********************" + e.getMessage());
				if(utx != null){
					utx.rollback();
				}
			}
		}
	}
	
	public Boolean isIntimationExist(String strIntimationNo) {
		Query query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo",strIntimationNo);
		List<Intimation>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()){
			return true;
		}
		return false;
	}

	public List<PremiaIntimationTable> processPremiaIntimationData(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<PremiaIntimationTable> premiaIntimationList = fetchRecFromPremiaIntimationTable(batchSize);
			return premiaIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE **********************" + e.getMessage());
			return null;
		}
	}
	
	public List<PremiaEndorsementTable> processEndorsementData(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<PremiaEndorsementTable> premiaIntimationList = fetchRecFromPremiaEndorsementTable(batchSize);
			return premiaIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE **********************" + e.getMessage());
			return null;
		}
	}
	
	public List<PremiaIntimationTable> processPremiaIntimationDataForGmc(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR GMC PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<PremiaIntimationTable> premiaIntimationList = fetchRecFromPremiaIntimationTableForGMC(batchSize);
			return premiaIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE **********************" + e.getMessage());
			return null;
		}
	}
	
	public List<PremiaIntimationTable> processPremiaIntimationDataForNonNetwork(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<PremiaIntimationTable> premiaIntimationList = fetchRecFromPremiaIntimationTableForNonNetwork(batchSize);
			return premiaIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE **********************" + e.getMessage());
			return null;
		}
	}


	private List<PremiaIntimationTable> fetchRecFromPremiaIntimationTable(String batchSize) {
		Query query = entityManager.createNamedQuery("PremiaIntimationTable.findAll");
		query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
		/**
		 * Will remove once the final integration of batch is over.
		 * */
	//	query = query.setParameter("claimType", "P");

		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<PremiaIntimationTable> premiaIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR PREMIA PULL FOR NON GMC NETWORK ******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return premiaIntimationTableList;
	}
	
	private List<PremiaEndorsementTable> fetchRecFromPremiaEndorsementTable(String batchSize) {
		Query query = entityManager.createNamedQuery("PremiaEndorsementTable.findAll");
		/**
		 * Will remove once the final integration of batch is over.
		 * */
	//	query = query.setParameter("claimType", "P");

		if(batchSize != null) {
			
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<PremiaEndorsementTable> premiaIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR PREMIA PULL FOR NON GMC NETWORK ******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return premiaIntimationTableList;
	}
	
	private List<PremiaIntimationTable> fetchRecFromPremiaIntimationTableForGMC(String batchSize) {
		Query query = entityManager.createNamedQuery("PremiaIntimationTable.findAllGmc");
		query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);

		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<PremiaIntimationTable> premiaIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR PREMIA PULL FOR GMC NETWORK ******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return premiaIntimationTableList;
	}
	
	private List<PremiaIntimationTable> fetchRecFromPremiaIntimationTableForNonNetwork(String batchSize) {
		Query query = entityManager.createNamedQuery("PremiaIntimationTable.findByNonNetwork");
		query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
		/**
		 * Will remove once the final integration of batch is over.
		 * */
	//	query = query.setParameter("claimType", "P");

		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<PremiaIntimationTable> premiaIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR PREMIA PULL FOR NON NETWORK******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return premiaIntimationTableList;
	}
	
	public MastersValue getMaster(Long masterKey) {
		MastersValue a_mastersValue = new MastersValue();
		if (masterKey != null) {
			Query query = entityManager.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", masterKey);
			List<MastersValue> mastersValueList = query.getResultList();
			if(mastersValueList != null && !mastersValueList.isEmpty()) {
				a_mastersValue = mastersValueList.get(0);
				return a_mastersValue;
			}
		}
		return null;
	}
	
	public MastersValue getMastersValue(String masterValue, String masterCode) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKeyByCodeAndValue");
		query = query.setParameter("parentKey", masterCode);
		query = query.setParameter("value", masterValue);
		
		List<MastersValue> masterValueList = new ArrayList<MastersValue>();
		masterValueList = query.getResultList();
		if(null != masterValueList && !masterValueList.isEmpty()) {
			return masterValueList.get(0);
		}
		return null;
	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		List resultList = query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			Policy singleResult =  (Policy) resultList.get(0);
			return singleResult;
		}
		return null;
	}
	
	public Hospitals getHospitalByHospNo(String hospCode) {
		Query query = entityManager.createNamedQuery("Hospitals.findByCode");
		query = query.setParameter("hospitalCode", hospCode.toUpperCase());
		List<Hospitals> hospitalList = query.getResultList();
		if(null != hospitalList && !hospitalList.isEmpty()) {
			return hospitalList.get(0);
		}
		return null;
	}
	
	public  TmpCPUCode getCpuDetails(Long cpuId) {
		TmpCPUCode ack = null;
		Query findByReimbursementKey = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		try {
			List resultList = findByReimbursementKey.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				ack = (TmpCPUCode) resultList.get(0);
			}
			return ack;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String issuingOfficeCode) {
		List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
		if(issuingOfficeCode != null){
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode",issuingOfficeCode);
			organizationList = (List<OrganaizationUnit>) findAll.getResultList();
			if(organizationList != null && ! organizationList.isEmpty()){
				return organizationList.get(0);
			}
		}
		return null;
	}
	
	public Insured getInsuredByPolicyAndInsuredName(String policyNo , String insuredName,String lobFlag) {
		if(lobFlag == null){
			lobFlag = "H";
		}
		Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredName)
		query = query.setParameter("insuredId", Long.valueOf(insuredName));
		query = query.setParameter("lobFlag", lobFlag);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			return insuredList.get(0);
		}  
		
		return null;
	}
	
	public Insured getInsuredByPolicyAndInsuredNameForDefault(String policyNo , String insuredName) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNoForDefault");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredName)
		query = query.setParameter("insuredId", Long.valueOf(insuredName));
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			return insuredList.get(0);
		}
		
		return null;
	}
	
	public Status getStatus(Long key){
		Query query = entityManager.createNamedQuery("Status.findByKey");
		query = query.setParameter("statusKey", key);
		List<Status> statusList = query.getResultList();
		if(null != statusList && !statusList.isEmpty()) {
			return statusList.get(0);
		}
		return null;
	}
	
	public Stage getStage(Long stageKey) {
		Query query = entityManager.createNamedQuery("Stage.findByKey");
		query = query.setParameter("stageKey", stageKey);
		List<Stage> stageList = query.getResultList();
		if(null != stageList && !stageList.isEmpty()) {
			return stageList.get(0);
		}
			
		return null;
	}
	
	public PremPolicyDetails fetchPolicyDetailsFromPremia(String policyNumber) {
		PremPolicyDetails policyDetails = null;
		try {
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNumber);
			System.out.println("****POLICY NUMBER IS *****--->" + policy.getPolicyNo());
			log.debug("****POLICY NUMBER IS *****--->" + policy.getPolicyNo());
			@SuppressWarnings("static-access")
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			PolicySource policySource = null;
			if(policy.getPolicyNo() != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNumber);
				if (policyObj != null) {
				   if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						policyDetails = BancsSevice.getInstance().getPolicyDetailsMethod(policyNumber);
						Product product = getProductBySource(policyDetails.getProductCode());
						if(product != null){
							policyDetails.setProductCode(product.getCode());
						}
					}else{
						builder = PremiaService.getInstance().getPolicyDetail();
						policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
					}
				}else{
//					//First time policy entered in galaxy
//					policySource = getByPolicySource(policy.getPolicyNo());
//					if (policySource != null) {
//						if (policySource.getPolicySourceFrom() != null && policySource.getPolicySourceFrom().equalsIgnoreCase(SHAConstants.PREMIA_POLICY)) {
//							builder = PremiaService.getInstance().getPolicyDetail();
//							policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
//						} else if (policySource.getPolicySourceFrom() != null && policySource.getPolicySourceFrom().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
//							policyDetails = BancsSevice.getInstance().getPolicyDetailsMethod(policyNumber);
//							Product product = getProductBySource(policyDetails.getProductCode());
//							if(product != null){
//								policyDetails.setProductCode(product.getCode());
//							}
//						
//						}
//					}
					log.info("Batch web service call for premia intimation" + policyNumber);
					builder = PremiaService.getInstance().getPolicyDetail();
					policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
			}
			}
			
			
			//Bancs Changes End
		//	Builder builder = PremiaService.getInstance().getPolicyDetail();
		} catch(Exception e) {
			log.error("ERROR WHILE FETCHING POLCIY DETAILS FROM PREMIA ---" + policyNumber);
			e.printStackTrace();
		}
	
		return policyDetails;
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
	
	
	public Policy getPolicy(String policyNumber) {
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
 		query = query.setParameter("policyNumber", policyNumber);
		List<Policy> policyList = query.getResultList();
		for (Policy policy : policyList) {
			entityManager.refresh(policy);
		}
		Policy policy = null;
		if(policyList != null && !policyList.isEmpty()){
			/**
			 * Will IMS_CLS_POLICY will hold multiple
			 * entries for a single policy number? ---
			 * Needs to clarified with DBA team.
			 * */
			for (Policy resultPolicy : policyList) {
				policy = resultPolicy;
			}
			List<Insured> insuredList = getInsuredListByPolicyNo(policyNumber);
			policy.setInsured(insuredList);
		}
		return policy;
	}
	
	public List<Insured> getInsuredListByPolicyNo(String policyNo) {
		Query query = entityManager
				.createNamedQuery("Insured.findByPolicyNo");
		query = query.setParameter("policyNumber", policyNo);
		List<Insured> insuredList = query.getResultList();
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
	public MastersValue getMasterByValue(String a_key) {
		a_key = a_key.toLowerCase();
		MastersValue a_mastersValue = null;
		if (a_key != null) {
			Query query = entityManager.createNamedQuery("MastersValue.findByValue");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMasterByValueAndMasList(String value,String masListKey) {
		MastersValue a_mastersValue = null;
		if (value != null) {
			value = value.toLowerCase();
			Query query = entityManager
					.createNamedQuery("MastersValue.findValueByMasListKey");
			query = query.setParameter("parentKey", value);
			query = query.setParameter("masListKey", masListKey);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}
	
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
				searchValue = StringUtils.trim(a_value);
		}
		if(null != searchValue){
		Query query = entityManager
				.createNamedQuery("MastersValue.findByValue");
		query = query.setParameter("parentKey", searchValue.toLowerCase());
		List<MastersValue> mastersValueList = query.getResultList();

		if(mastersValueList != null && !mastersValueList.isEmpty() )
		{
			for (MastersValue a_mastersValue : mastersValueList){
				masterValue = a_mastersValue;
			}
		}
			
		}
		return masterValue;
	}
	
	public Product getProductByCodeAndType(String productCode, String productType){
		productType = productType.toLowerCase();
		Query query = entityManager
				.createNamedQuery("Product.findByProductType");
		query = query.setParameter("productCode", productCode);
		query = query.setParameter("productType", productType);
		
		@SuppressWarnings("unchecked")
		List<Product> productList = (List<Product>) query.getResultList();
		
		if(productList != null && ! productList.isEmpty()){
			return productList.get(0);
		}
		return null;
		
	}
	
	public Product getProductByCodeAndTypeAndInceptionDate(String productCode, String productType,Date policyStartDate){
		productType = productType.toLowerCase();
		Query query = entityManager
				.createNamedQuery("Product.findByProductTypeAndDate");
		query = query.setParameter("productCode", productCode);
		query = query.setParameter("productType", productType);
		query = query.setParameter("inceptionDate", policyStartDate);
		
		@SuppressWarnings("unchecked")
		List<Product> productList = (List<Product>) query.getResultList();
		
		if(productList != null && ! productList.isEmpty()){
			return productList.get(0);
		}
		return null;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMasterValue(Long a_key) {
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
	
	public List<Intimation> getIntimationByPolicyKey(Long policyKey){
		if(policyKey != null){
			Query query = entityManager.createNamedQuery("Intimation.findByPolicyKey");
			query.setParameter("policyKey", policyKey);
	
			List<Intimation> resultList =  (List<Intimation>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()){
//				entityManager.refresh(resultList);
				return resultList;
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Intimation insertIntimationDataRevised(PremiaIntimationTable premiaIntimationTable,String lobFlag) throws NotSupportedException, SystemException {
		try {
			utx.begin();
			Boolean isHospTypeEmpty = false;
			Intimation intimation  = new Intimation();
			MastersValue intimationModeValue = null;
			MastersValue intimationIntimatedBy = null;
			Policy policy = null;
 			Hospitals hospitals = null;
			MastersValue admissionType = null;
			MastersValue managementType = null;
			MastersValue roomCategory = null;
			TmpCPUCode cpuCode = null;
			MastersValue intimationSource = null;
			MastersValue hospitalType = new MastersValue();
			MastersValue claimType = new MastersValue();
			Insured insured = null;
			Boolean isHospTopUpIntmAvail = false;
			
			/*//MED-PRD-076
			if(premiaIntimationTable.getProductCode() != null && premiaIntimationTable.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| premiaIntimationTable.getProductCode() != null && premiaIntimationTable.getProductCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
				premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
			}*/
			if(isHospCashTopUpAvail(premiaIntimationTable.getGiIntimationNo())) {
				isHospTopUpIntmAvail =true;
			}
			
			if(null != premiaIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimationMode()))
				intimationModeValue = getMastersValue(premiaIntimationTable.getGiIntimationMode(),ReferenceTable.MODE_OF_INTIMATION);
			else
				intimationModeValue = getMastersValue(SHAConstants.INTIMATION_MODE_PHONE,ReferenceTable.MODE_OF_INTIMATION);
			
			if(null != premiaIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimationMode())){
				if(premiaIntimationTable.getGiIntimationMode().equalsIgnoreCase(SHAConstants.INTIMATION_MODE_WEBSERVICE)){
					intimationModeValue = getMaster(ReferenceTable.INTIMATION_ONLINE_MODE);
				}
			}

			if(null != premiaIntimationTable.getGiIntimatedBy() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimatedBy()))
				intimationIntimatedBy = getMastersValue(premiaIntimationTable.getGiIntimatedBy(),ReferenceTable.INTIMATED_BY);
			else
				intimationIntimatedBy = getMastersValue(SHAConstants.INTIMATION_INTIMATED_BY,ReferenceTable.INTIMATED_BY);
			
			if(null != premiaIntimationTable.getGiPolNo() && !("").equalsIgnoreCase(premiaIntimationTable.getGiPolNo()))
				policy = getPolicyByPolicyNubember(premiaIntimationTable.getGiPolNo());
			
			/* For the product star criticare platinum and gold,cashless process is not applicable and it should be routed to reimbursement
			  even the user create intimation for network hospital.the below code is added for this scenario */
			if(null != premiaIntimationTable.getProductCode() &&
				(ReferenceTable.getDirectReimbursementProducts().containsKey(premiaIntimationTable.getProductCode()))) {
				premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
			}
			
			// GLX2020054 add for star novel grp corona
			if(policy != null && (policy.getProduct().getCode().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE) /*||
					policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_COVID_PROD_CODE)*/)&& null !=  policy.getPolicyPlan()&& (policy.getPolicyPlan().equals(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))) {
					premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
			}
			
			if(policy != null && (SHAConstants.N_FLAG).equals(policy.getProduct().getCashlessElgFlag())){
				premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
			}
			
			if(null != premiaIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiHospCode())) {
				
				String hospCode =  premiaIntimationTable.getGiHospCode();
				hospitals = getHospitalByHospNo(hospCode.toUpperCase());
				
				if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) ||
						isHospTopUpIntmAvail) {
					
					if(policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
					
				}else if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
					if(null != hospCode) {
						log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
						if(null != hospitals)
							cpuCode =  getCpuDetails(hospitals.getCpuId());
					}
				}
			}
			else
			{
				String hospCode =  SHAConstants.DEFAULT_HOSP_CODE;
				isHospTypeEmpty = true;
				if(null != hospCode) {
					log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
					hospitals = getHospitalByHospNo(hospCode.toUpperCase());
					if(null != hospitals)
						cpuCode =  getCpuDetails(hospitals.getCpuId());
				}
			}
			
			//added foe CR GMC CPU Routing GLX2020075
			if(policy != null && policy.getProduct().getKey() != null){
				MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(policy.getProduct().getKey());
				if(gmcRoutingProduct != null){
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {	
						if(null != premiaIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiHospCode())) {
							String hospCode =  premiaIntimationTable.getGiHospCode();
							hospitals = getHospitalByHospNo(hospCode.toUpperCase());
							log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
							if(null != hospitals)
								cpuCode =  getCpuDetails(hospitals.getCpuId());
							if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
								cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
							}

						}
					}else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
									if(masCpuLimit != null){
										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
									}else {
										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
										if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
											cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
										}
									}
								}
							}
						}

					}
					intimation.setCpuCode(cpuCode);
				}
			}
			//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//			if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
//				cpuCode =  getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//				if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
//					if(policy.getHomeOfficeCode() != null) {
//						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
//						if(branchOffice != null){
//							String officeCpuCode = branchOffice.getCpuCode();
//							if(officeCpuCode != null) {
//								MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
//								if(masCpuLimit != null){
//									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
//								}
//							}
//						}
//					}
//			   }
//				intimation.setCpuCode(cpuCode);
//			}
			
			if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
				String baayasCpuCodeByPolicy = getPaayasCpuCodeByPolicy(policy.getPolicyNumber());
				if(baayasCpuCodeByPolicy != null){
					cpuCode = getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
					if(cpuCode != null){
						intimation.setCpuCode(cpuCode);
					}
				}
				
				String jioCpuCodeByPolicy = getJioPolicyCpuCode(policy.getPolicyNumber());
				if(jioCpuCodeByPolicy != null){
					cpuCode = getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
					if(cpuCode != null){
						intimation.setCpuCode(cpuCode);
					}
				}
				
				Long tataPolicy = getTataPolicy(policy.getPolicyNumber());
				if(tataPolicy != null){
					cpuCode = getMasCpuCode(tataPolicy);
					if(cpuCode != null){
						intimation.setCpuCode(cpuCode);
					}
				}
				
				String kotakCpuCodeByPolicy = getKotakPolicyCpuCode(policy.getPolicyNumber());
				if(kotakCpuCodeByPolicy != null){
					cpuCode = getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
					if(cpuCode != null){
						intimation.setCpuCode(cpuCode);
					}
				}
				
				
			}
			
			if(policy != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
				cpuCode =  getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
				intimation.setCpuCode(cpuCode);
			}
			
			//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//			//added for CPU routing
//			if(policy != null && policy.getProduct().getKey() != null){
//				String CpuCode= getMasProductCpu(policy.getProduct().getKey());
//				if(CpuCode != null){
//					cpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//					intimation.setCpuCode(cpuCode);
//				}
//			}
//			//added for CPU routing
			
			String gpaPolicyDetails = getGpaPolicyDetails(policy.getPolicyNumber());
			if(gpaPolicyDetails != null){
				TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
				if(masCpuCode != null){
					intimation.setCpuCode(masCpuCode);
					cpuCode = masCpuCode;
				}
			}
			
			if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) 
					{
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
					}
			else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
				hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
			}
//			if(null != premiaIntimationTable.getGiClmProcDivn()){
//				cpuCode = getMasCpuCode(Long.parseLong(premiaIntimationTable.getGiClmProcDivn()));
//			}
			
			/***
			 * Added for pa claims .
			 * */
			
			setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory(),isHospTypeEmpty,isHospTopUpIntmAvail);
			/*	if(SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(premiaIntimationTable.getGiPACategory())) 
			{
				if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) && 
						SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
				{
					
					
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				}
				else
				{
					setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					if(policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
			}*/
			/**
			 * The below else block will be called other than PA Claims. 
			 * */
		/*	else {
					
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
					} else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
			}*/
			if(null != premiaIntimationTable.getGiAdmissionType() && !("").equalsIgnoreCase(premiaIntimationTable.getGiAdmissionType()))
				admissionType = getMastersValue(premiaIntimationTable.getGiAdmissionType(), ReferenceTable.ADMISSION_TYPE);
			
			if(null != premiaIntimationTable.getGiManagementType() && !("").equalsIgnoreCase(premiaIntimationTable.getGiManagementType()))
				managementType = getMastersValue(premiaIntimationTable.getGiManagementType(),ReferenceTable.TREATMENT_MANAGEMENT);
			else
				managementType = getMastersValue(SHAConstants.MGMT_SURGICAL,ReferenceTable.TREATMENT_MANAGEMENT);
			if(null != premiaIntimationTable.getGiRoomCategory() && !("").equalsIgnoreCase(premiaIntimationTable.getGiRoomCategory()))
				roomCategory = getMastersValue(premiaIntimationTable.getGiRoomCategory(), ReferenceTable.ROOM_CATEGORY);
			
			/*else
				roomCategory = getMastersValue(SHAConstants.DELUXE, ReferenceTable.ROOM_CATEGORY);*/

			if(null != premiaIntimationTable.getGiInsuredName() && !("").equalsIgnoreCase(premiaIntimationTable.getGiInsuredName())){
				insured = getInsuredByPolicyAndInsuredName(premiaIntimationTable.getGiPolNo(), premiaIntimationTable.getGiInsuredName(),premiaIntimationTable.getGiPACategory());
			}
			
			if(insured == null){
				insured = getInsuredByPolicyAndInsuredNameForDefault(premiaIntimationTable.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
			}
			/*
			 * Intimation source - Currently in premia , there is no column available 
			 * in premia table. As of now,  hardcoding 131. Later this needs to be removed.
			 * */
			intimationSource = new MastersValue();
			
			if(null != premiaIntimationTable.getGiSavedType() && (SHAConstants.PREMIA_INTIMATION_HOSP_SAVE).equalsIgnoreCase(premiaIntimationTable.getGiSavedType())) {
				intimationSource.setKey(ReferenceTable.HOSPITAL_PORTAL);
			} else {
				intimationSource.setKey(ReferenceTable.CALL_CENTRE_SOURCE);
			}
			
			intimation.setInsured(insured);
			intimation.setIntimationMode(intimationModeValue);
			intimation.setIntimatedBy(intimationIntimatedBy);
			intimation.setIntimaterName(premiaIntimationTable.getGiIntimatorName());
			if(null != premiaIntimationTable.getGiIntimatorContactNo()) {
				String userNameForDB = SHAUtils.getUserNameForDB(premiaIntimationTable.getGiIntimatorContactNo());
				intimation.setCallerLandlineNumber(userNameForDB);
				intimation.setCallerMobileNumber(userNameForDB);
			}

			intimation.setPolicy(policy);
			intimation.setIntimationId(premiaIntimationTable.getGiIntimationNo());
			if(null != hospitals) {
				intimation.setHospital(hospitals.getKey());
			}
			
			/**
			 * new column added for policy year
			 */
			if(premiaIntimationTable.getGiPolicyYear() != null){
				intimation.setPolicyYear(premiaIntimationTable.getGiPolicyYear());
			}
			
			if(premiaIntimationTable.getGiAttenderMobileNumber() != null){
				// As per Satish and Saran instruction used substring for attender mobile
//				intimation.setAttendersMobileNumber(premiaIntimationTable.getGiAttenderMobileNumber());
				String attenderMobNo = SHAUtils.getUserNameForDB(premiaIntimationTable.getGiAttenderMobileNumber());
				intimation.setAttendersMobileNumber(attenderMobNo);
			}
			
			intimation.setHospitalType(hospitalType);
			intimation.setHospitalComments(premiaIntimationTable.getGiSHospComments());
			intimation.setCallerAddress(premiaIntimationTable.getCallerAddress());
			intimation.setAdmissionType(admissionType);
			intimation.setManagementType(managementType);
			intimation.setAdmissionReason(premiaIntimationTable.getGiReasonForAdmission());

			/**
			 * Commented on 25/10/2019 and the batch scheduled process comment as per Raja A. instruction
			 **/
			
			intimation.setAdmissionDate(premiaIntimationTable.getGiAdmitted());
			/*//Added as per Sathish Sir comment - 14-SEP-2019
			if(premiaIntimationTable.getGiAdmitted() != null && premiaIntimationTable.getGiAdmittedTime() != null){
				String dateTimeAdmitted = SHAUtils.formatDateForAdmissionDischarge(premiaIntimationTable.getGiAdmitted())+ " "+ premiaIntimationTable.getGiAdmittedTime();
				Date admissionDateTime = SHAUtils.combineDateTime(dateTimeAdmitted);
				intimation.setAdmissionDate(admissionDateTime);
			}else{
				intimation.setAdmissionDate(premiaIntimationTable.getGiAdmitted());
			}

			if(premiaIntimationTable.getGiDischargeDate() != null && premiaIntimationTable.getGiDischargeTime() != null){
				String dateTimeDischarged = SHAUtils.formatDateForAdmissionDischarge(premiaIntimationTable.getGiDischargeDate())+ " "+ premiaIntimationTable.getGiDischargeTime();
				Date dischargeDateTime = SHAUtils.combineDateTime(dateTimeDischarged);
				intimation.setDateOfDischarge(dischargeDateTime);
			}else{
				intimation.setDateOfDischarge(premiaIntimationTable.getGiDischargeDate());
			}*/
			
			intimation.setRoomCategory(roomCategory);
			intimation.setInsuredPatientName(premiaIntimationTable.getGiPatientNameYn());
			intimation.setInpatientNumber(premiaIntimationTable.getGiInpatientNo());
			intimation.setCpuCode(cpuCode);
			intimation.setIntimationSource(intimationSource);
			intimation.setCreatedBy(premiaIntimationTable.getGiCreatedBy());
			intimation.setCreatedDate(premiaIntimationTable.getGiCreatedOn());
			intimation.setClaimType(claimType);
			intimation.setStatus(getStatus(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY));
			intimation.setStage(getStage(ReferenceTable.INTIMATION_STAGE_KEY));
			if(premiaIntimationTable.getGiEmail() !=null && !premiaIntimationTable.getGiEmail().isEmpty())
			{
			intimation.setPortalEmailId(premiaIntimationTable.getGiEmail());
			//As per Saran/Satish Sir comments added below line - Customer APP portal
			intimation.setCallerEmail(premiaIntimationTable.getGiEmail());
			}
			if(cpuCode != null){
				intimation.setOriginalCpuCode(cpuCode.getCpuCode());
			}
			
	
			/**
			 * Added for PA.
			 * 
			 * **/
			if(SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
					intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
			else 
				intimation.setIncidenceFlag(SHAConstants.DEATH_FLAG);
			
			//intimation.setIncidenceFlag(premiaIntimationTable.getGiAccidentDeathFlag());
			intimation.setProcessClaimType(premiaIntimationTable.getGiPACategory());
			if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiPACategory()))
			{
				intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
			}
			intimation.setHospitalReqFlag(premiaIntimationTable.getGiHospitalRequiredFlag());
			if(null != policy.getLobId())
			{
				MastersValue masLob = getMaster(policy.getLobId());
				intimation.setLobId(masLob);
			}			
			/*if((policy.getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_37_PRODUCT)) || (policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT))
					|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER)) || (policy.getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL)) 
<<<<<<< HEAD
					|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE))){*/			
/*			=======
					|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE))
					|| (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()))
					|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_NEW))){
>>>>>>> mayrelease_04_05_2018_EAP_7_CR*/
			
			if(null != intimation.getPolicy().getProduct().getKey() && 
					(ReferenceTable.getCombinationOfHealthAndPA().containsKey(intimation.getPolicy().getProduct().getKey()))){

				if(premiaIntimationTable.getGiAccidentDeathFlag() != null){
					MastersValue masLob = getMaster(ReferenceTable.PA_LOB_KEY);
					intimation.setLobId(masLob);
				}
			}

			if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())){
				intimation.setPaParentName(premiaIntimationTable.getGiPAParentName());
				if(premiaIntimationTable.getGiPAParentAge() != null){
					intimation.setPaParentAge(Double.valueOf(premiaIntimationTable.getGiPAParentAge()));
				}
				if(premiaIntimationTable.getGiPAParentDOB() != null){
//					intimation.setPaParentDOB(new Date(premiaIntimationTable.getGiPAParentDOB()));
					Date parentDOB = SHAUtils.formatTimeFromString(premiaIntimationTable.getGiPAParentDOB());
					intimation.setPaParentDOB(parentDOB);
				}
				intimation.setPaPatientName(premiaIntimationTable.getGiPAPatientName());
				intimation.setPaCategory(premiaIntimationTable.getGiPACategory());
				
				//CR2018085
				if(premiaIntimationTable.getGiPAPatientDOB() != null){
					Date patientDOB = SHAUtils.formatTimeFromString(premiaIntimationTable.getGiPAPatientDOB());
					intimation.setPaPatientDOB(patientDOB);
				}
				if(premiaIntimationTable.getGiPAPatientAge() != null){
					intimation.setPaPatientAge(Integer.valueOf(premiaIntimationTable.getGiPAPatientAge()));
				}
			}
			
			if(intimation.getInsured() == null || intimation.getHospital() == null){
				log.info(" &&&&&&&&& INTIMATION IS NOT INSERTED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! >"+ premiaIntimationTable.getGiIntimationNo() +"<------------");
				utx.rollback();
				return null;
			}
			
			// PA added fix ends up here.
			if(null != intimation.getHospital()){
				entityManager.persist(intimation);
				entityManager.flush();
				log.info(" &&&&&&&&& INTIMATION INSERTED SUCESSFULLY------>"+ intimation.getIntimationId() +"<------------");
			}
			
			
			
			if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()) && intimation.getPolicy().getGpaPolicyType() != null 
					&& intimation.getPolicy().getGpaPolicyType().equalsIgnoreCase("Un Named")){
				DBCalculationService dbCalculationService = new DBCalculationService();
				dbCalculationService.invokeProcedureForUnnamedPolicy(intimation.getIntimationId());
			}
			utx.commit();
			
			return intimation;
		
		} catch(Exception e) {
			utx.rollback();
		}
		return null;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean populatePolicyFromTmpPolicy(PremPolicyDetails premPolicyDetails,String riskSysID,Boolean isEndorsement) {
		Boolean isAllowed = true;
		try {
		//tmpPolicy = policyService.findTmppolicyByPolicyNo(tmpPolicy.getPolNo());
		 Policy policy = null;
		
		 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) {
    		 isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.FLOATER_POLICY);
 	     } else {
 	    	isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
 	     }
		 
		 if(!isAllowed) {
			 try {
				 utx.begin();
				 Policy existingPolicy = getPolicy(premPolicyDetails.getPolicyNo());
				 if (existingPolicy != null){
					 List<Intimation> intimationByPolicyKey = getIntimationByPolicyKey(existingPolicy.getKey());
					 if(intimationByPolicyKey != null && !intimationByPolicyKey.isEmpty()){
						 log.info("############# Intrgration Policy Part If intimation is existing for this policy then we will allow further ------>>>>>"+premPolicyDetails.getPolicyNo());
						 isAllowed = true;
					 }
				 }
				 utx.commit();
			 } catch (Exception e){
				 log.error("############# Error while processing the integration part ----->>>"+premPolicyDetails.getPolicyNo());
				 utx.rollback();
			 }
		 }
		
		if(isAllowed) {
			if(! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE) && 
					!ReferenceTable.getGMCProductCodeList().containsKey(premPolicyDetails.getProductCode())
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.JET_PRIVILEGE_PRODUCT_CODE)
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
					&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)){
					 policy = populatePolicyData(premPolicyDetails, true,isEndorsement);

					if(policy == null) {
						log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->" + premPolicyDetails.getPolicyNo());
						return !isAllowed;
					}
					if(premPolicyDetails.getPolicyType() == null || (premPolicyDetails.getPolicyType() != null && !premPolicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))){
						List<PremPreviousPolicyDetails> previousPolicyDetails = premPolicyDetails.getPreviousPolicyDetails();
						if(previousPolicyDetails != null){
						for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
							try {
								Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
								if(previousPolicyObj == null) {
									PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(previousPolicy.getPolicyNo());
									if(policyDetails != null){
										populatePolicyData(policyDetails, false,isEndorsement);
									}

								}
							} catch(Exception e) {
								log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->" + previousPolicy.getPolicyNo());
							}
							
						}
					 }
					}
					
					if (premPolicyDetails.getBasePolicy() != null
							&& !premPolicyDetails.getBasePolicy().isEmpty()) {
						try {
							Policy previousPolicyObj = getPolicy(premPolicyDetails
									.getBasePolicy());
							if (previousPolicyObj == null) {
								PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(premPolicyDetails
										.getBasePolicy());
								if (policyDetails != null) {
									populatePolicyData(policyDetails, false,
											isEndorsement);
								}

							}
						} catch (Exception e) {
							log.error("$$$$$$$$$$$$$$ BASE POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->"
									+ premPolicyDetails.getBasePolicy());
						}

					}
					
					//added for saving topup policy 
			        if(null != premPolicyDetails.getTopUpPolicyNo() && !premPolicyDetails.getTopUpPolicyNo().isEmpty()){
			        		PremPolicyDetails premTopUpPolicyDetails = fetchPolicyDetailsFromPremia(premPolicyDetails.getTopUpPolicyNo());
			        		try {
			        			
			        			if(premTopUpPolicyDetails !=null){
			        				populatePolicyData(premTopUpPolicyDetails, false,isEndorsement);
			        			}
			        		}
			        		catch(Exception e) {
			        			log.error("___________________  POLICY ERROR WHILE SAVING THE TOPUP DATA ___________________" + premPolicyDetails.getTopUpPolicyNo());
			        		}
			        	}
					
				}else if(premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.JET_PRIVILEGE_PRODUCT_CODE)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)){

					 policy = populatePolicyData(premPolicyDetails, true,isEndorsement);
						if(policy == null) {
							log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->" + premPolicyDetails.getPolicyNo());
							return !isAllowed;
						}
						/*if(premPolicyDetails.getPolicyType() == null || (premPolicyDetails.getPolicyType() != null && !premPolicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))){
							List<PremPreviousPolicyDetails> previousPolicyDetails = premPolicyDetails.getPreviousPolicyDetails();
							for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
								try {
									Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
									if(previousPolicyObj == null) {
										PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(previousPolicy.getPolicyNo());
										if(policyDetails != null){
											populatePolicyData(policyDetails, false);
										}

									}
								} catch(Exception e) {
									log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->" + previousPolicy.getPolicyNo());
								}
								
							}
						}*/
					
				}
					else if(ReferenceTable.getGMCProductCodeList().containsKey(premPolicyDetails.getProductCode())){
					PremPolicyDetails gmcPolicyDetails = fetchGmcPolicyDetailsFromPremia(premPolicyDetails.getPolicyNo(),riskSysID);
					policy = populatePolicyData(gmcPolicyDetails, true,isEndorsement);
					if(policy == null) {
						log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->" + premPolicyDetails.getPolicyNo());
						return !isAllowed;
					}
					if(gmcPolicyDetails.getPolicyType() == null || (gmcPolicyDetails.getPolicyType() != null && !gmcPolicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))){
						List<PremPreviousPolicyDetails> previousPolicyDetails = gmcPolicyDetails.getPreviousPolicyDetails();
						if(previousPolicyDetails != null){
							for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
								try {
									Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
									if(previousPolicyObj == null) {
										PremPolicyDetails policyDetails = fetchGmcPolicyDetailsFromPremia(premPolicyDetails.getPolicyNo(),riskSysID);
										if(policyDetails != null){
											populatePolicyData(policyDetails, false,isEndorsement);
										}
									}
								} catch(Exception e) {
									log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->" + previousPolicy.getPolicyNo());
								}
								
							}
						}
					
					}
				}else if(premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE) ||
						premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)){
					PremPolicyDetails gpaPlicyDetails = fetchGpaPolicyDetailsFromPremia(premPolicyDetails.getPolicyNo(),riskSysID);
					gpaPlicyDetails.setRiskSysId(riskSysID);
					policy = populatePolicyData(gpaPlicyDetails, true,isEndorsement);
					if(policy == null) {
						log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->" + premPolicyDetails.getPolicyNo());
						return !isAllowed;
					}
					if(gpaPlicyDetails.getPolicyType() == null || (gpaPlicyDetails.getPolicyType() != null && !gpaPlicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))){
						List<PremPreviousPolicyDetails> previousPolicyDetails = gpaPlicyDetails.getPreviousPolicyDetails();
						if(previousPolicyDetails != null){
							for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
								try {
									Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
									if(previousPolicyObj == null) {
										PremPolicyDetails policyDetails = fetchGpaPolicyDetailsFromPremia(premPolicyDetails.getPolicyNo(),riskSysID);
										if(policyDetails != null){
											populatePolicyData(policyDetails, false,isEndorsement);
										}
									}
								} catch(Exception e) {
									log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->" + previousPolicy.getPolicyNo());
								}
								
							}
						}
					
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(null != premPolicyDetails)
				log.error("#*#*#*#*#*#*#*#*#*#ERROR OCCURED IN populatePolicyFromTmpPolicy METHOD *#*#*#*#*#*#*#*#*#*#*#--------->" + premPolicyDetails.getPolicyNo());
		}
		return isAllowed;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean populateGMCandGPAPolicy(PremPolicyDetails gmcPolicyDetails,String riskSysID,Boolean isEndorsement) {
		
		//PremPolicyDetails gmcPolicyDetails = fetchGmcPolicyDetailsFromPremia(policyNumber,riskSysID);
		try {
			Policy policy = populatePolicyData(gmcPolicyDetails, true,isEndorsement);
		} catch (IllegalStateException | SecurityException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;

	}
	
	/*@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean saveJIOPreviousPolicy(PremPolicyDetails gmcPolicyDetails,String riskSysID) {
		
		//PremPolicyDetails gmcPolicyDetails = fetchGmcPolicyDetailsFromPremia(policyNumber,riskSysID);
		try {
			Policy policy = populatePolicyData(gmcPolicyDetails, false);
		} catch (IllegalStateException | SecurityException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;

	}*/
	
	
	
	@SuppressWarnings("deprecation")
	public void getInsuredformPolicy(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		utx.begin();
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
			PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		 List<Insured> insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
			
		 //set insured Date
		
		 
		 List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
		 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
			MastersValue genderMaster =  getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
			 insured.get(index).setInsuredGender(genderMaster);
			
			 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
				 
				 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
				 //Added for insured age caluculation.
				 Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));
				 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
				 if(formatPremiaDate != null) {
					 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);
				 }
			 }
			 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
	 		 
	 		 if(insuredAge != null){
	 			 insured.get(index).setInsuredAge(insuredAge);
	 		 } 
	 		 insured.get(index).setLopFlag("H");
		 }
		 
		 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
				 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
		 }
			 
		List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();				 
		int i=0;
		if(null != insured && !insured.isEmpty()) {
			for (Insured insured2 : insured) {
				if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
					Double totalSumInsured = policy.getTotalSumInsured();
					Double size = Double.valueOf(insured.size());
					Double sumInsured = 0d;
					if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_82)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
						if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
							sumInsured = totalSumInsured/size;
						}
						Long roundOfSI = Math.round(sumInsured);
						insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
					}else{
						insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
					}
					
//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
					
				} else {
//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
				}
				
				if (premiaInsured.get(i).getSelfDeclaredPed() != null
						&& premiaInsured.get(i).getSelfDeclaredPed()
								.length() > 0) {
					InsuredPedDetails selfDeclaredPed = new InsuredPedDetails();
					selfDeclaredPed.setInsuredKey(insured2
							.getInsuredId());
					selfDeclaredPed.setPedDescription(premiaInsured
							.get(i).getSelfDeclaredPed());
					List<InsuredPedDetails> pedList = new ArrayList<InsuredPedDetails>();
					pedList.add(selfDeclaredPed);
					try{
						if(insured2.getInsuredPedList() != null){
							insured2.getInsuredPedList().addAll(pedList);
						}else{
							insured2.setInsuredPedList(pedList);
						}
					}catch(Exception e){
						System.out.println("Insured Ped list exception");
						e.printStackTrace();
					}
					
				}
				i++;
//				 if(!isCurrentPolicy) {
//						insured2.setKey(insured2.getInsuredId()); 
//				 }		
			}
		    policy.setInsured(insured);
		}
		
		List<Insured> list = getInsuredListByPolicyNo(premPolicyDetails.getPolicyNo());
		
		
		if(null != policy.getInsured() && !policy.getInsured().isEmpty()) {
			
			for (Insured  objInsured : policy.getInsured())  {
				
				Boolean isAlreadyExist = false;
				for (Insured insured3 : list) {
					if((insured3.getInsuredId().equals(objInsured.getInsuredId()) && (insured3.getLopFlag() != null && insured3.getLopFlag().equalsIgnoreCase(objInsured.getLopFlag())))){
						isAlreadyExist = true;
						break;
					}
				}
				
				if(!isAlreadyExist){
					objInsured.setPolicy(policy);
					if(objInsured != null){
						entityManager.merge(objInsured);
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
							//added for CR ped effective date save in PED table GLX0132
							if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//								Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
//								if(fromDate != null){
								insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));
//								}
							}
							if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//								Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
//								if(fromDate != null){
								insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));	
//								}
							}
							if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
								insuredPed.setPedType(insuredPed.getPedType());
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
				}
				
			}
			
		}
		utx.commit();
		
	}
	
	public void saveInsuredCoverDetails(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		
		utx.begin();
		
		PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		
		List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
        List<Insured> insuredPAFromPremia = new ArrayList<Insured>();
        if(premiaInsuredPAdetails != null){
        	insuredPAFromPremia = premiaPolicyMapper.getInsuredPAFromPremia(premiaInsuredPAdetails);
        }
        
        List<Insured> insuredPA = insuredPAFromPremia;
        
       
        
		if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
				if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
					for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(coverDetails.getCoverCode());
							insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
						if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
							insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
						}
						insuredCoverDetailsForPA.add(insuredCover);
						
						
					}
					
					if(insuredPA != null){
						for (Insured insured2 : insuredPA) {
							
							MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
							if (genderMaster != null &&  genderMaster.getKey() != null) {
								
								insured2.setInsuredGender(genderMaster);
							}
							
							
							if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
								insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
								break;
							}
						}
					}
				}
			}
        	
        	 for (Insured insured : insuredPA) {
        		 
        		 if(insured.getCoverDetailsForPA() != null && ! insured.getCoverDetailsForPA().isEmpty()){
         			for (InsuredCover insuredCoverPA : insured.getCoverDetailsForPA()) {
         				insuredCoverPA.setInsuredKey(insured.getKey());
         				if(insuredCoverPA.getKey() == null){
         					entityManager.persist(insuredCoverPA);
         					entityManager.flush();
         				} else {
         					entityManager.merge(insuredCoverPA);
         					entityManager.flush();
         				}
         			}
         		}
     		}
        	
        	
        }
		
		utx.commit();
		
	
		
		
	}
	
	public void savePortabilityPolicy(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		
		utx.begin();
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
		PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		 List<Insured> insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
			
		 //set insured Date
		
		 
		 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
			 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
			 
			 List<PremPortability> portablitityDetails = premPolicyDetails.getInsuredDetails().get(index).getPortablitityDetails();
			 
			 if(portablitityDetails != null){
				 List<PortablityPolicy> portablityList = premiaPolicyMapper.getPortablityList(portablitityDetails);
				 for (PortablityPolicy portablityPolicy : portablityList) {
					
					 try{
					 if(portablityPolicy.getStrDateOfBirth() != null && ! portablityPolicy.getStrDateOfBirth().equalsIgnoreCase("")){
						 portablityPolicy.setDateOfBirth(new Date(portablityPolicy.getStrDateOfBirth()));
					 }
					 if(portablityPolicy.getStrMemberEntryDate() != null && ! portablityPolicy.getStrMemberEntryDate().equalsIgnoreCase("")){
						 portablityPolicy.setMemberEntryDate(new Date(portablityPolicy.getStrMemberEntryDate()));
					 }
					 
					 if(portablityPolicy.getPolicyStrStartDate() != null && ! portablityPolicy.getPolicyStrStartDate().equalsIgnoreCase("")){
						 portablityPolicy.setPolicyStartDate(new Date(portablityPolicy.getPolicyStrStartDate()));
					 }
							portablityPolicy.setActiveStatus(1l);
							portablityPolicy.setInsuredName(insured.get(index).getInsuredName());
					 if(portablityPolicy.getKey() == null && portablityPolicy.getPolicyNumber() != null){
							portablityPolicy.setCurrentPolicyNumber(policy.getPolicyNumber());
							entityManager.persist(portablityPolicy);
							entityManager.flush();
					 }else{
							entityManager.merge(portablityPolicy);
							entityManager.flush();
					 }
					
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				}
//				 insured.get(index).setPortablityPolicy(portablityList);
				
			 }
		 }	
		utx.commit();
		
	}
	
	 /**
	  * Below code was added as part of CR R1080
	  */
public void savePrevPortabilityPolicy(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		
		
		PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		List<PremInsuredDetails> premiaInsuredList = premPolicyDetails.getInsuredDetails();
		if(premiaInsuredList != null && !premiaInsuredList.isEmpty()){
		
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
		 
		for (PremInsuredDetails premInsuredDetails : premiaInsuredList){
		
			List<PremPortabilityPrevPolicyDetails> premPortablitiPrevPolicyDetails = premInsuredDetails.getPortabilityPrevPolicyDetails();
			 
			 if(premPortablitiPrevPolicyDetails != null){
				 List<PortabilityPreviousPolicy> portablityPrevPolicyList = premiaPolicyMapper.getPortablityPrevPolicyList(premPortablitiPrevPolicyDetails);
				 for (PortabilityPreviousPolicy portablityPrevPolicy : portablityPrevPolicyList) {
					
					 try{
						 
						 if(portablityPrevPolicy.getPolicyStrFmDt() != null && ! portablityPrevPolicy.getPolicyStrFmDt().isEmpty()){
							 portablityPrevPolicy.setPolicyFmDt(SHAUtils.formatTimeFromString(portablityPrevPolicy.getPolicyStrFmDt()));
						 }
						 if(portablityPrevPolicy.getPolicyStrToDt() != null && ! portablityPrevPolicy.getPolicyStrToDt().isEmpty()){
							 portablityPrevPolicy.setPolicyToDt(SHAUtils.formatTimeFromString(portablityPrevPolicy.getPolicyStrToDt()));
						 }
						 portablityPrevPolicy.setCurrentPolicyNumber(policy.getPolicyNumber());
						 portablityPrevPolicy.setInsuredName(premInsuredDetails.getInsuredName());
						 portablityPrevPolicy.setActiveStatus(1l);
						 
//						 Boolean isAvailable = checkDataAvailable(portablityPrevPolicy, premInsuredDetails.getInsuredName());	 
//						 
//						 if(portablityPrevPolicy.getPolicyNumber() != null && !isAvailable){
							
							portablityPrevPolicy.setCreatedDate(new Date()); 
							entityManager.persist(portablityPrevPolicy);
							entityManager.flush();
//						 }
//						 else if(portablityPrevPolicy.getKey() != null && portablityPrevPolicy.getPolicyNumber() != null && isAvailable){
//							
//							 portablityPrevPolicy.setModifiedDate(new Date());
//							entityManager.merge(portablityPrevPolicy);
//							entityManager.flush();
//						 }
					}
					catch(Exception e){
						 e.printStackTrace();
					}
				}
				
			}			 
		  }	
		}		
	}
	
/**
 * 
 * Below code was Added as part of CR R1080 Portability Prev. Policy for each insured
 *  
 */
//	private Boolean checkDataAvailable(PortabilityPreviousPolicy portablityPrevPolicy, String premInsuredName){
//		
//	Boolean exists = Boolean.TRUE;	
//	
//	String portablityPrevPolicyNumber = portablityPrevPolicy.getPolicyNumber();
//	
//	Query portabilityPolQuery = entityManager.createNamedQuery("PortabilityPreviousPolicy.findByPolicyNumberNInsuredName");
//	portabilityPolQuery.setParameter("policyNumber", portablityPrevPolicyNumber);
//	portabilityPolQuery.setParameter("insuredName", premInsuredName != null ? premInsuredName.toLowerCase():"");
//	
//	List<PortabilityPreviousPolicy> resutlList = portabilityPolQuery.getResultList();
//	if(resutlList != null && !resutlList.isEmpty()){
//	
//		portablityPrevPolicy.setKey(resutlList.get(0).getKey());
//		return exists;
//	}
//	return !exists;
//		
//	}
	
	private Boolean isIntegrated(PremPolicyDetails premPolicyDetails, Long productTypeKey) {
		Boolean isAllowed = true;
		try {
			MastersValue productType = getMasterValue(productTypeKey);
			Product product = getProductByCodeAndType(premPolicyDetails.getProductCode(), productType.getValue());
			if(product != null && product.getIsIntegrated() != null && product.getIsIntegrated().equalsIgnoreCase("N")){
				isAllowed = false;
			}
		}
		catch(Exception e) {
			log.error("*************ERROR OCCURED IN ISINTEGRATED METHOD****************************PRODUCT KEY------------>"+productTypeKey);
		}
		 return isAllowed;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Boolean isPolicyAllowed(String policyNumber) {
		Query query = entityManager
				.createNamedQuery("IntimationImportValidation.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);
		List<IntimationImportValidation> resultList = query.getResultList();
		Boolean isAllowed = true;
		if(resultList != null && !resultList.isEmpty()) {
			IntimationImportValidation intimationImportValidation = resultList.get(0);
			if(intimationImportValidation != null && intimationImportValidation.getPremiaIntimationCount() != null && intimationImportValidation.getPremiaIntimationCount() > 1) {
				isAllowed = false;
			}
		}
		return isAllowed;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	private Policy populatePolicyData(PremPolicyDetails premPolicyDetails, Boolean isCurrentPolicy,Boolean isEndorsement) throws IllegalStateException, SecurityException, SystemException {
		try {
			utx.begin();
			Boolean isBaNCS = Boolean.FALSE;
			if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
				isBaNCS = Boolean.TRUE;
			}
			
			Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
			/*if (null == policy || ReferenceTable.getGMCProductCodeList().containsKey(premPolicyDetails.getProductCode()) ||  
					premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)||
					premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)) {*/

			Policy existingPolicy = null;
			if(policy != null && isEndorsement){
				existingPolicy =  policy;
			}
			
				 PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
				 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
				 Double totalAmount = 0d;
				 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
				 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
				 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
				 policy.setTotalPremium(totalAmount);
				 
				 //FIX FOR OVERRIDING OF CHECQUE STATUS DURING ENDORSEMENT BATCH
				 if(existingPolicy != null && isEndorsement){
					 if(existingPolicy.getChequeStatus() != null){
						 policy.setChequeStatus(existingPolicy.getChequeStatus());
					 }
					 if(existingPolicy.getRestoredSI() != null){
						 policy.setRestoredSI(existingPolicy.getRestoredSI());
					 }
					 if(existingPolicy.getRechargeSI() != null){
						 policy.setRechargeSI(existingPolicy.getRechargeSI());
					 }
				 }
				 
				 
				 try{
					 if(premPolicyDetails.getDeductiableAmt() != null && ! premPolicyDetails.getDeductiableAmt().isEmpty()){
						 policy.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getDeductiableAmt()).doubleValue());
					 }
				 }catch(Exception e){
					 e.printStackTrace();
				 }
				 
				 if(premPolicyDetails.getPolSysId() != null){
					 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
			
					 policy.setPolicySystemId(polSysId);
				 }
				 
				 if(premPolicyDetails.getProposerGender() != null){
					 if(premPolicyDetails.getProposerGender().equalsIgnoreCase("M")){
						 MastersValue master = getMaster(ReferenceTable.MALE_GENDER);
						 policy.setProposerGender(master);
					 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("F")){
						 MastersValue master = getMaster(ReferenceTable.FEMALE_GENDER);
						 policy.setProposerGender(master);
					 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("TG")){
						 MastersValue master = getMaster(ReferenceTable.TRANS_GENDER);
						 policy.setProposerGender(master);
					 }
				 }
				 if(premPolicyDetails.getPortedYN() != null){
					 policy.setPortedPolicy(premPolicyDetails.getPortedYN());
				 }
				 
				 if(!isCurrentPolicy) {
					 //IMSSUPPOR-31281 - Sequence mismatch with SysId and policyKey
					 Policy clsPolicy = getPolicyDetails(policy.getPolicySystemId());
					    if(clsPolicy == null){
					    	policy.setKey(policy.getPolicySystemId()); 
					    }
				 }
				 
				 if(premPolicyDetails.getPolicyPlan() != null){
 					 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
						 policy.setPolicyPlan("A");
					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
						 policy.setPolicyPlan("B");
 					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
 							 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
						 policy.setPolicyPlan("G");
					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
						 policy.setPolicyPlan("S");
					 }//Added for MED-PRD-072
					 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
						 policy.setPolicyPlan("B");
					 }
 					 
 					 //MED-PRD-076
 					if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
 						if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
 							 policy.setPolicyPlan("B");
 						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
 							 policy.setPolicyPlan("E");
 	 					 }
 					}
 					 
				 }
				 
				 if(premPolicyDetails.getSectionCode() != null && premPolicyDetails.getSectionCode().equalsIgnoreCase(SHAConstants.JET_PRODUCT_SECTION_CODE)){
					 policy.setPolicyPlan("A");
				 }
				 
				 if(premPolicyDetails.getPolicyTerm() != null) {
					 // Below Cond for SCRC - MED-PRD-070 - R201811302
					//below code added fro PA saral sureksha product.
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.SARAL_SURAKSHA_CARE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(1l);
					 }
					 if(premPolicyDetails.getProductCode() != null && (ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED.equals(premPolicyDetails.getProductCode())
							 || SHAConstants.PRODUCT_CODE_87.equals(premPolicyDetails.getProductCode()) || premPolicyDetails.getProductCode().equals(ReferenceTable.SUPER_SURPLUS_REVISED_INDIVIDUAL_CODE)
									 || premPolicyDetails.getProductCode().equals(ReferenceTable.SUPER_SURPLUS_REVISED_INDIVIDUAL_CODE) ||
									 premPolicyDetails.getProductCode().equals(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM_CODE))){
						 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
							 String policyTermYear[] = premPolicyDetails.getPolicyTerm().split(" ");
							 String policyTerm = policyTermYear[0];
							 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTerm));
						 }
					 }
					 else if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
						 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
						 policy.setPolicyTerm(policyTerm);
					 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(2l);
					 }
					 //added for new product076
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
						 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) { 
							 String policyTermYear = SHAUtils.getTruncateWord(premPolicyDetails.getPolicyTerm(), 0, 1);
							 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTermYear));
						 }
					 }
						//Added for MED-PRD-083
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
					 policy.setPolicyTerm(1l);
					 }
					 
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(1l);
					 }
					 
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())
							 && premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()){
						 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
						 policy.setPolicyTerm(policyTerm);
					 }
					 
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())
							 && premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()){
						 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
						 policy.setPolicyTerm(policyTerm);
					 }
					 
					 if(premPolicyDetails.getProductCode() != null && ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_CODE.equals(premPolicyDetails.getProductCode())) {
						 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
							 String policyTermYear = SHAUtils.getTruncateWord(premPolicyDetails.getPolicyTerm(), 0, 1);
							 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTermYear));
						 }else{
							 policy.setPolicyTerm(1l);
						 }
					 }
					 if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_CANCER_PLATINUM_IND_PRODUCT)){
						 policy.setPolicyTerm(1l);
					 }
				 }
				 
				//Added for BaNCS
				 if(premPolicyDetails.getPolicySource() == null || !premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
					 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
					 if(policyStrYear != null){
						 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
					 }
				 }
				 
				 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)){
					 if(isBaNCS){
						 policy.setCreatedDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
					 }else{
						 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 }
				 }
				 
				 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true){
					 if(isBaNCS){
						 policy.setPolicyFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
					 }else{
						 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 }
				 }
				 
				 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true){
					 if(isBaNCS){
						 policy.setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyEndDate()));
					 }else{
						 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
					 }
				 }
				 
				 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true){
					 if(isBaNCS){
						 policy.setReceiptDate(SHAUtils.formatTimeFromString(premPolicyDetails.getReceiptDate()));
					 }else{
						 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
					 }
				 }
				 
				 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true){
					 if(isBaNCS){
						 policy.setProposerDob(SHAUtils.formatTimeFromString(premPolicyDetails.getProposerDOB()));
					 }else{
						 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
					 }
				 }
				 
				 if(null != premPolicyDetails.getLob() && null != getMasterByValue(premPolicyDetails.getLob()))
					 policy.setLobId(getMasterByValue((premPolicyDetails.getLob())).getKey());
				 
				 
				 if(null != getMasterByValue(premPolicyDetails.getPolicyType()))
				 policy.setPolicyType(getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
				 
 				 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
					 MastersValue schemeId = getMasterByValue(premPolicyDetails.getSchemeType());
					 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
				 }
 				 
				//TODO:Get product type from premia 
//				 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//				 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
				 
				 Product productByProductCodeForPremia = getProductByProductCode(premPolicyDetails.getProductCode(),new Date(premPolicyDetails.getPolicyStartDate()));
				 if(productByProductCodeForPremia == null) {
					 return null;
				 }
				 
				 //Added for BaNCS - Commented the code as per satish sir request and setted the policySource default to P.
				/* if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
					 policy.setLobId(Long.parseLong(productByProductCodeForPremia.getLobId()));
					 
					 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
						 List<MastersValue> schmList = findByMasterListKeyList("PRDSCHM");
							for(int k=0;k<schmList.size();k++) {
								if(schmList.get(k).getValue().replace("+", "").equalsIgnoreCase(premPolicyDetails.getSchemeType())) {
									policy.setSchemeId(schmList.get(k).getKey());
									break;
								}
							}
					 }
					 
					 policy.setProposerSubDist(premPolicyDetails.getSubDistrict());
					 policy.setOfficeTelephone(premPolicyDetails.getBaNCSofficeTelPhone());
					 
					 policy.setPremiumTax(Double.valueOf(premPolicyDetails.getBaNCSPremiumTax()));
					 policy.setStampDuty(Double.valueOf(premPolicyDetails.getBaNCSStampDuty()));
					 policy.setTotalPremium(Double.valueOf(premPolicyDetails.getBaNCSTotalPremium()));
					 
					 policy.setOfficeEmailId(premPolicyDetails.getBaNCSOfficeEmailId());
					 policy.setOfficeFax(premPolicyDetails.getBaNCSOfficeFax());
					 policy.setPolicySource(SHAConstants.BANCS_POLICY);
					 
				 }*/
				 policy.setPolicySource(SHAConstants.PREMIA_POLICY);
				 policy.setProduct(productByProductCodeForPremia);
				 
				 //Added for MED-PRD-072
				 if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_72) || premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87) || premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_012))
						 && policy.getPolicyPlan() != null){
					 Product productByProductKeyForPremia = null;
					 
					 /**
					  * As per Satish Sirs Suggestion Product is defaulted to Basic
					  * 
					  */
					 if(premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87)){
						 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY_98);
					 }else{
					 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY);
					 }
					 // BELOW  Code is commented because Policy Plan was changed from policy level to insured level for this Product SHAConstants.PRODUCT_CODE_72
					 /*if(policy.getPolicyPlan().equals("B")){
						 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY);
					 }
					 else if(policy.getPolicyPlan().equals("G")){
						 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY);
					 }*/
					 
					 
					 
					 if(productByProductKeyForPremia != null){
						 policy.setProduct(productByProductKeyForPremia);
					 }
				 }
				 
				 policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
				 
				 List<PolicyEndorsementDetails> endorsementDetailsList = new ArrayList<PolicyEndorsementDetails>();
				 if(premPolicyDetails.getEndorsementDetails() != null){
				 endorsementDetailsList = premiaPolicyMapper .getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
				 }
				 
				 List<PreviousPolicy> previousPolicyList = new ArrayList<PreviousPolicy>();
			     if(premPolicyDetails.getPreviousPolicyDetails() != null){
				 previousPolicyList = premiaPolicyMapper.getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
			     }
				 //set Endrosement Date
			     if(premPolicyDetails.getEndorsementDetails() != null){
				 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
					 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
					if(isBaNCS){
						endorsementDetailsList.get(index).setEffectiveFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						endorsementDetailsList.get(index).setEndoresementDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
					}else{
						endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
					}
					 }
					 //Not null handled for BANCS
					 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt() != null){
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
							 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
						 }
					 }
				 }
			     }
				 //Set previous policy Date
			     if(premPolicyDetails.getPreviousPolicyDetails() != null){
				 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
					 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
						 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
					 }
			     }
				 //Set previous policy Date
				 if(premPolicyDetails.getPreviousPolicyDetails() != null){
					 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
						 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
							 if(isBaNCS){
								 previousPolicyList.get(index).setPolicyFrmDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
							 }else{
								 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
							 }
						 }
						 if(null == premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()||premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().isEmpty() ? false : true){
							 if(isBaNCS){
								 previousPolicyList.get(index).setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
							 }else{
								 previousPolicyList.get(index).setPolicyToDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
							 }
						 }
					 }
				  }
			     }
			     
			 	
				// ADD FOR MED_PRD_84 YOUNG STAR FLT POLICY
				if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84) ||
						premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)){
					
					 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)
							 && (premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
 							 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW))) { 
			     	    	
		        			 policy.setHospitalCashBenefits(SHAConstants.OTHR_BEN_HOS_CASH_FLAG);
		     	    }
					
				}
			     
			     List<Insured> insured = new ArrayList<Insured>();
				 if(premPolicyDetails.getInsuredDetails() != null){
					 
					 List<PremInsuredDetails> premiaInsuredList = premPolicyDetails.getInsuredDetails();
					 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
					 
					 if(premiaInsuredList != null && !premiaInsuredList.isEmpty()) {
						 for (PremInsuredDetails premInsuredDetails : premiaInsuredList) {
							
							 for (Insured insuredObj : insured) {
								 if(insuredObj.getInsuredId() != null && !StringUtils.isBlank(premInsuredDetails.getRiskSysId())){
									 if(insuredObj.getInsuredId().equals(Long.valueOf(premInsuredDetails.getRiskSysId()))){
										 
										 if(premInsuredDetails.getPolicyPlan() != null){
											 if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
												 insuredObj.setPolicyPlan("B");
											 }
											// Added For MED-PRD-83
											 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) 
													 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
												 insuredObj.setPolicyPlan("S");
											 }
											 else if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
													 || premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
												 insuredObj.setPolicyPlan("G");
											 }
										 }
									 
									 // ADD FOR MED_PRD_84 YOUNG STAR IND POLICY
										if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84) ||
												premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)){
											
											 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)
													 && (premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
						 							 || premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW))) { 
									     	    	
												 insuredObj.setHospitalCashBenefits(SHAConstants.OTHR_BEN_HOS_CASH_FLAG);
								     	    }
											
										}
									 //
										Boolean isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
										if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
											if(isAllowed){
												if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
													insuredObj.setPlan("B");
													insuredObj.setPolicyPlan("B");
												}else if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
													insuredObj.setPlan("E");
													insuredObj.setPolicyPlan("E");
												}
											}
										}
										
										//comment by noufel since code already presient above
//										Boolean isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
										if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
											if(isAllowed && premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)
													|| premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)
													&& !premInsuredDetails.getHcpDays().isEmpty()){
												insuredObj.setHcpDays(Integer.valueOf(premInsuredDetails.getHcpDays()));
											}
										}
										
										Boolean isFloater = isIntegrated(premPolicyDetails, ReferenceTable.FLOATER_POLICY);
										if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
											if(isFloater){
												if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
													insuredObj.setPlan("B");
													insuredObj.setPolicyPlan("B");
												}else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
													insuredObj.setPlan("E");
													insuredObj.setPolicyPlan("E");
												}
											}
										}
										
									//  add for comphersive product 078
										if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
											
											 if(premInsuredDetails.getBuyBackPed() != null && ! premInsuredDetails.getBuyBackPed().isEmpty()  && premInsuredDetails.getBuyBackPed().equalsIgnoreCase(SHAConstants.YES)) { 
									     	    	
												 insuredObj.setBuyBackPed(SHAConstants.YES_FLAG);
								     	    }else{
								     	    	insuredObj.setBuyBackPed(SHAConstants.N_FLAG);
								     	    }
											
										}
									//  add for Revised comphersive product 088
										if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
											
											 if(premInsuredDetails.getBuyBackPed() != null && ! premInsuredDetails.getBuyBackPed().isEmpty()  && premInsuredDetails.getBuyBackPed().equalsIgnoreCase(SHAConstants.YES)) { 
									     	    	
												 insuredObj.setBuyBackPed(SHAConstants.YES_FLAG);
								     	    }else{
								     	    	insuredObj.setBuyBackPed(SHAConstants.N_FLAG);
								     	    }
											
										}
										// addd for product code Star cancre 
										if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_CANCER_PLATINUM_IND_PRODUCT)){
											
											if(premInsuredDetails.getLumbSum() != null && ! premInsuredDetails.getLumbSum().isEmpty() &&  premInsuredDetails.getLumbSum().equalsIgnoreCase("1")) { 
												insuredObj.setLumpSumFlg(SHAConstants.YES_FLAG);
											}else{
												insuredObj.setLumpSumFlg(SHAConstants.N_FLAG);
											}
											
											List<PremPolicyCoverDetails> policyLumpSumCover = premPolicyDetails.getPremPolicyCoverDetails();
											if(policyLumpSumCover != null && ! policyLumpSumCover.isEmpty()){
												for (PremPolicyCoverDetails premPolicyRiskCover2 : policyLumpSumCover) {
													if(insuredObj.getLumpSumFlg() != null && insuredObj.getLumpSumFlg().equalsIgnoreCase(SHAConstants.YES_FLAG) 
															&& insuredObj.getInsuredId() != null && insuredObj.getInsuredId().equals(Long.valueOf(premPolicyRiskCover2.getRiskId())) 
															&& premPolicyRiskCover2.getCoverCode() != null && premPolicyRiskCover2.getCoverCode().equalsIgnoreCase(SHAConstants.STAR_CANCER_PLATINUM_LUMP_COVER_CODE)){
														if(premPolicyRiskCover2.getSumInsured() != null){
															insuredObj.setSumInsured1(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
														}

													}
													if(insuredObj.getInsuredId() != null && insuredObj.getInsuredId().equals(Long.valueOf(premPolicyRiskCover2.getRiskId())) 
															&& premPolicyRiskCover2.getCoverCode() != null && premPolicyRiskCover2.getCoverCode().equalsIgnoreCase(SHAConstants.STAR_CANCER_PLATINUM_INID_COVER_CODE)){
														if(premPolicyRiskCover2.getSumInsured() != null){
															insuredObj.setSumInsured2(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
														}

													}
												}
											}
										}
									 }
								 }
							 }
						}
						 
					 }
					 
					 
					 
				 }else if(premPolicyDetails.getJpInsuredDetails() != null){
					 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getJpInsuredDetails());
					 for (Insured insured2 : insured) {
						 insured2.setLopFlag("H");
						 
						 if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
								MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
								if(genderMaster != null && genderMaster.getKey() != null){
									insured2.setInsuredGender(genderMaster);
								}
							}else
							{
								MastersValue genderMaster =  getKeyByValue("MALE");
								if(genderMaster != null){
									insured2.setInsuredGender(genderMaster);
								}
							}
						 
						if(insured2.getStrInsuredAge() != null && ! insured2.getStrInsuredAge().isEmpty()){
							Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
							insured2.setInsuredAge(insuredAge);
						}
						if(insured2.getStrDateOfBirth() != null && ! insured2.getStrDateOfBirth().isEmpty()){
							 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
							 if(insuredDOB != null) {
								 insured2.setInsuredDateOfBirth(insuredDOB);
							 }
						}
						
						/**This code added for CR20181258 - Criticare Platinum since nominee details insertion is not handled for Health claim **/
						List<PremInsuredDetails> premiaInsureObj = premPolicyDetails.getJpInsuredDetails();
						if(premiaInsureObj != null && ! premiaInsureObj.isEmpty()){
							for (PremInsuredDetails premiaInsured : premiaInsureObj) {
								List<NomineeDetails> nomineeDetailsForHealth = new ArrayList<NomineeDetails>();
								
								List<PolicyNominee> proposerNomineeDetailsForHealth = new ArrayList<PolicyNominee>();
								
								List<GmcContinuityBenefit> continuityBenefits = new ArrayList<GmcContinuityBenefit>();
								
								List<InsuredPedDetails> pedDetails = new ArrayList<InsuredPedDetails>();
								
								if(premiaInsured.getNomineeDetails() != null && ! premiaInsured.getNomineeDetails().isEmpty()){
									for (PremInsuredNomineeDetails nomineeObj : premiaInsured.getNomineeDetails()) {
										NomineeDetails nomineeDetail = new NomineeDetails();
										PolicyNominee proposerNomineeDetail = new PolicyNominee();
										
										if(nomineeObj.getNomineeAge() != null && ! nomineeObj.getNomineeAge().isEmpty()){
											nomineeDetail.setNomineeAge(Long.valueOf(nomineeObj.getNomineeAge()));
											proposerNomineeDetail.setNomineeAge(Integer.valueOf(nomineeObj.getNomineeAge()));
										}
										nomineeDetail.setNomineeName(nomineeObj.getNomineeName());
										proposerNomineeDetail.setNomineeName(nomineeObj.getNomineeName());
										//TODO
										/*proposerNomineeDetail.setAccountNumber(nomineeObj.getAccountNumber());
										proposerNomineeDetail.setNameAsPerBank(nomineeObj.getBeneficiaryName());
										proposerNomineeDetail.setIFSCcode(nomineeObj.getIfscCode());*/
										nomineeDetailsForHealth.add(nomineeDetail);
										proposerNomineeDetailsForHealth.add(proposerNomineeDetail);
									}
									if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
										insured2.setNomineeDetails(nomineeDetailsForHealth);
										insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForHealth);
									}
								}
								
								/*Below Code for MED-PRD-074*/
								if(premiaInsured.getContinuityBenefits() != null && ! premiaInsured.getContinuityBenefits().isEmpty()){
									if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
										continuityBenefits = premiaPolicyMapper.getGMCInsuredContBenDetails(premiaInsured.getContinuityBenefits());
										insured2.setGmcContBenefitDtls(continuityBenefits);
									}
								}
								
								if(premiaInsured.getPedDetails() != null && !premiaInsured.getPedDetails().isEmpty()){
									if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
										pedDetails = premiaPolicyMapper.getInsuredPedFromPremia(premiaInsured.getPedDetails());
										insured2.setInsuredPedList(pedDetails);
									}
								}
								
								/*if(policy.getProduct() != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
								    List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

								        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
								        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
								        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
								        		InsuredCover riskCover = new InsuredCover();
								        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
								        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
								        		if(premPolicyRiskCover2.getSumInsured() != null){
								        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
								        		}
								        		
								        		insuredCoverList.add(riskCover);
											}
								        	if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
								        		insured2.setCoverDetailsForPA(insuredCoverList);
								        	}
									}
								}*/
								
								 try{
									 if(premiaInsured.getDeductiableAmt() != null  && !premiaInsured.getDeductiableAmt().isEmpty()){
										 insured2.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premiaInsured.getDeductiableAmt()).doubleValue());
									 }
									 if(premiaInsured.getDefinedLimit() != null && !premiaInsured.getDefinedLimit().isEmpty()){
										 insured2.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premiaInsured.getDefinedLimit()).doubleValue());
									 }
								 }catch(Exception e){
									 e.printStackTrace();
								 }
							}
						}
				 	//  End of  CR CR20181258
					}
				 }

				 //set insured Date
				 List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
				 if(premPolicyDetails.getInsuredDetails() != null){
	 				 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
						MastersValue genderMaster =  getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
						 insured.get(index).setInsuredGender(genderMaster);
						
						 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
							 
							//Added for BaNCS
							 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
								 Date formatBaNCSDate = SHAUtils.formatTimeFromString(premPolicyDetails.getInsuredDetails().get(index).getDob());
								 if(formatBaNCSDate != null) {
									 insured.get(index).setInsuredDateOfBirth(formatBaNCSDate);
								 }
								 insured.get(index).setSourceRiskId(premPolicyDetails.getInsuredDetails().get(index).getBaNCSSourceRiskID());
							 }else{
								 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
								 //Added for insured age caluculation.
								 Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));
								 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
								 if(formatPremiaDate != null) {
									 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);
								 }
								 
							 }
							 
							 
							 
							 
						 }
						 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
				 		 
				 		 if(insuredAge != null){
				 			 insured.get(index).setInsuredAge(insuredAge);
				 		 } 
				 		 
				 		 insured.get(index).setLopFlag("H");
				 		 
				 		 if(insured.get(index).getSumInsured1() != null){
				 			insured.get(index).setSumInsured1Flag("Y");
				 		 }
				 		 else
				 		 {
				 			insured.get(index).setSumInsured1Flag("N");
				 		 }
				 		 
				 		if(insured.get(index).getSumInsured2() != null){
				 			insured.get(index).setSumInsured2Flag("Y");
				 		 }
				 		else
				 		{
				 			insured.get(index).setSumInsured2Flag("N");
				 		}
				 		
				 		if(insured.get(index).getSumInsured3() != null){
				 			insured.get(index).setSumInsured3Flag("Y");
				 		 }
				 		else
				 		{
				 			insured.get(index).setSumInsured3Flag("N");
				 		}
				 		
				 		//IMSSUPPOR-27387
				 		if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
				 			 try{
								 if(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt() != null){
									 insured.get(index).setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt()).doubleValue());
								 }
							 }catch(Exception e){
								 e.printStackTrace();
							 }
				 		}
					 }

				 }
				 

				 if(premPolicyDetails.getInsuredDetails() != null){
					 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
						 List<PremPEDDetails> pedDetailsList = premPolicyDetails.getInsuredDetails().get(index).getPedDetails();
						 if(pedDetailsList != null && !pedDetailsList.isEmpty()){
							 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(pedDetailsList));
						 }
							 
							 List<PremPortability> portablitityDetails = premPolicyDetails.getInsuredDetails().get(index).getPortablitityDetails();
							 
							 if(portablitityDetails != null){
								 List<PortablityPolicy> portablityList = premiaPolicyMapper.getPortablityList(portablitityDetails);
								 for (PortablityPolicy portablityPolicy : portablityList) {
									
									 try{
									 if(portablityPolicy.getStrDateOfBirth() != null && ! portablityPolicy.getStrDateOfBirth().equalsIgnoreCase("")){
										 portablityPolicy.setDateOfBirth(new Date(portablityPolicy.getStrDateOfBirth()));
									 }
									 if(portablityPolicy.getStrMemberEntryDate() != null && ! portablityPolicy.getStrMemberEntryDate().equalsIgnoreCase("")){
										 portablityPolicy.setMemberEntryDate(new Date(portablityPolicy.getStrMemberEntryDate()));
									 }
									 
									 if(portablityPolicy.getPolicyStrStartDate() != null && ! portablityPolicy.getPolicyStrStartDate().equalsIgnoreCase("")){
										 portablityPolicy.setPolicyStartDate(new Date(portablityPolicy.getPolicyStrStartDate()));
									 }
									 }catch(Exception e){
										 e.printStackTrace();
									 }
								}
								 insured.get(index).setPortablityPolicy(portablityList);
							 }
					 }
				 }
					 
				List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();				 
				int i=0;
 				if(null != insured && !insured.isEmpty()) {
					for (Insured insured2 : insured) {
						if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
							Double totalSumInsured = policy.getTotalSumInsured();
							Double size = Double.valueOf(insured.size());
							Double sumInsured = 0d;
							if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
									|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
									|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
									|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
									|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_82)
									|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
								if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
									sumInsured = totalSumInsured/size;
								}
								Long roundOfSI = Math.round(sumInsured);
								insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
							}else{
								if(premPolicyDetails.getProductCode() == null){
									insured2.setInsuredSumInsured(policy.getTotalSumInsured());
								}
								else if(premPolicyDetails.getProductCode() != null && !premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
										&& !policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
										&& !policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
										&& !policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)) {
									insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
								}
							}
							
		//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
							
						} else {
		//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
						}
						
						if(premiaInsured != null){
						
							if (premiaInsured.get(i).getSelfDeclaredPed() != null
									&& premiaInsured.get(i).getSelfDeclaredPed()
											.length() > 0) {
								InsuredPedDetails selfDeclaredPed = new InsuredPedDetails();
								selfDeclaredPed.setInsuredKey(insured2
										.getInsuredId());
								selfDeclaredPed.setPedDescription(premiaInsured
										.get(i).getSelfDeclaredPed());
								List<InsuredPedDetails> pedList = new ArrayList<InsuredPedDetails>();
								pedList.add(selfDeclaredPed);
								try{
									if(insured2.getInsuredPedList() != null){
										insured2.getInsuredPedList().addAll(pedList);
									}else{
										insured2.setInsuredPedList(pedList);
									}
								}catch(Exception e){
									System.out.println("Insured Ped list exception");
									e.printStackTrace();
								}
								
							}
						}
						i++;
						 if(!isCurrentPolicy) {
							    Insured clsInsured = getCLSInsured(insured2.getInsuredId());
							    if(clsInsured == null){
							    	insured2.setKey(insured2.getInsuredId()); 
							    }
						 }
						 
						 //Added for BaNCS
						 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
							 Query query = entityManager.createNativeQuery("select SEQ_B_INSURED_NO.nextval from dual");
							 insured2.setInsuredId(Long.parseLong(query.getResultList().get(0).toString()));		
						 }
					}
				    policy.setInsured(insured);
				}
			    
 		        if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
 		        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_37)//Added for comprehensive ind 507
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE) 
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_83)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)
 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
		        	
		        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) { 
		     	    	
		        		 MastersValue productType = getMaster(ReferenceTable.FLOATER_POLICY);
		        		 policy.setProductType(productType);
		        		 
		        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
		        		 if(product != null){
		        			 policy.setProduct(product);
		        		 }
		     	    } else {
		     	    	
		     	    	 MastersValue productType = getMasterValue(ReferenceTable.INDIVIDUAL_POLICY);
		        		 policy.setProductType(productType);
		        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
		        		 if(product != null) {
		        			 policy.setProduct(product);
		        		 }
		     	    }
		        }
		        List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

		        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
		        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
		        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
		        		PolicyRiskCover riskCover = new PolicyRiskCover();
		        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
		        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
		        		if(premPolicyRiskCover2.getSumInsured() != null){
		        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
		        		}
		        		
		        		policyRiskCoverList.add(riskCover);
					}
		        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
		        }
		        
		       /* if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
		        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
		        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
						if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
							policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
//							policyBankDetails.setEffectiveFrom(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveFrom()));
						}
 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
							policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
//							policyBankDetails.setEffectiveTo(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveTo()));
						}
					}
		        	policy.setPolicyBankDetails(bankDetailsFromPremia);
		        }*/
		        
		        //Bancs Changes Start
				Policy policyObj = null;
				Builder builder = null;
				List<ZUAViewQueryHistoryTableDTO> listOfQueryHistory = new ArrayList<ZUAViewQueryHistoryTableDTO>();

				if (premPolicyDetails.getPolicyNo() != null) {
					//Commented as per SATHISH SIR in Batch
					/*DBCalculationService dbService = new DBCalculationService();
					policyObj = dbService.getPolicyObject(premPolicyDetails.getPolicyNo());
<<<<<<< HEAD
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							ClaimProvisionService.getInstance().callBankDetailsService(null, null);
						}else{
								 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
							        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
							        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
											if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
												policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
											}
					 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
												policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
											}
										}
							        	policy.setPolicyBankDetails(bankDetailsFromPremia);
							        }
						}
					}
=======
					if (policyObj != null) {*/
						//Commented as per SATHISH SIR in Batch
						//if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.PREMIA_POLICY)) {
							 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
						        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
						        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
										if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("") && !policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("null") ){
											policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
										}
				 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("") && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("null")){
											policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
										}
									}
						        	policy.setPolicyBankDetails(bankDetailsFromPremia);
						        }
							
						/*} else if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							ClaimProvisionService.getInstance().callBankDetailsService();
						}*/
							 //}
				}
				//Bancs Changes End
  		        List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
		        List<Insured> insuredPAFromPremia = new ArrayList<Insured>();
		        if(premiaInsuredPAdetails != null){
		        	insuredPAFromPremia = premiaPolicyMapper.getInsuredPAFromPremia(premiaInsuredPAdetails);
		        }
		        for (Insured insured2 : insuredPAFromPremia) {
					if(insured2.getStrInsuredAge() != null && !insured2.getStrInsuredAge().isEmpty()){
						Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());     
						insured2.setInsuredAge(insuredAge);
					}
					if(insured2.getStrDateOfBirth() != null && !insured2.getStrDateOfBirth().isEmpty()){
					
					 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
					 if(insuredDOB != null) {
						 insured2.setInsuredDateOfBirth(insuredDOB);
					 }
					}
					if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
						MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
						if(genderMaster != null && genderMaster.getKey() != null){
							insured2.setInsuredGender(genderMaster);
						}
					}
					/**
					 * As per Sathish ,if gender is blank  from premia, then
					 *  default it to MALE.
					 * */
					else
					{
						MastersValue genderMaster =  getKeyByValue("MALE");
						if(genderMaster != null){
							insured2.setInsuredGender(genderMaster);
						}
					}
					insured2.setLopFlag("P");
					
				}
		        
		        if(insuredPAFromPremia != null){
		        	policy.setInsuredPA(insuredPAFromPremia);
		        }

		        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
		        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
		        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
						if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
							for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
								InsuredCover insuredCover = new InsuredCover();
								insuredCover.setCoverCode(coverDetails.getCoverCode());
 								insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
								if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
									insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
								}
								insuredCoverDetailsForPA.add(insuredCover);
								
								
							}
							List<Insured> insuredPA = policy.getInsuredPA();
							if(insuredPA != null){
								for (Insured insured2 : insuredPA) {
									
									MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
									if (genderMaster != null &&  genderMaster.getKey() != null) {
										
										insured2.setInsuredGender(genderMaster);
									}
									
									
									if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
										insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
										break;
									}
								}
							}
						}
						
						if(policy.getProduct() != null && (policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)
								|| policy.getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY)
								|| policy.getProduct().getKey().equals(ReferenceTable.GROUP_TOPUP_PROD_KEY))){
						    List<PremPolicyRiskCover> premPolicyCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

						        if(premPolicyCover != null && ! premPolicyCover.isEmpty()){
						        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
						        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyCover) {
						        		InsuredCover riskCover = new InsuredCover();
						        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
						        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
						        		if(premPolicyRiskCover2.getSumInsured() != null){
						        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
						        		}
						        		
						        		insuredCoverList.add(riskCover);
									}
						        	
						        	List<Insured> insuredPA = policy.getInsuredPA();
									if(insuredPA != null){
										for (Insured insured2 : insuredPA) {		
											if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
												insured2.setCoverDetailsForPA(insuredCoverList);
											}
										}
									}
						        
							}
						}
					}
		        }
      
		        
		        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
		        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
		        		 List<NomineeDetails> nomineeDetailsForPA = new ArrayList<NomineeDetails>();
		        		 List<PolicyNominee> proposerNomineeDetailsForPA = new ArrayList<PolicyNominee>();
		        		 
						if(premiaInsured1.getNomineeDetails() != null && ! premiaInsured1.getNomineeDetails().isEmpty()){
							for (PremInsuredNomineeDetails coverDetails : premiaInsured1.getNomineeDetails()) {
								NomineeDetails nomineeDetail = new NomineeDetails();
								PolicyNominee proposerNomineeDetail = new PolicyNominee();
								if(coverDetails.getNomineeAge() != null && ! coverDetails.getNomineeAge().isEmpty()){
									nomineeDetail.setNomineeAge(Long.valueOf(coverDetails.getNomineeAge()));
									proposerNomineeDetail.setNomineeAge(Integer.valueOf(coverDetails.getNomineeAge()));
								}
								nomineeDetail.setNomineeName(coverDetails.getNomineeName());
								proposerNomineeDetail.setNomineeName(coverDetails.getNomineeName());
								proposerNomineeDetail.setRelationshipWithProposer(coverDetails.getNomineeRelation());
								//TODO
								/*proposerNomineeDetail.setAccountNumber(coverDetails.getAccountNumber());
								proposerNomineeDetail.setNameAsPerBank(coverDetails.getBeneficiaryName());
								proposerNomineeDetail.setIFSCcode(coverDetails.getIfscCode());*/
								nomineeDetailsForPA.add(nomineeDetail);
								proposerNomineeDetailsForPA.add(proposerNomineeDetail);
								
								
							}
							List<Insured> insuredPA = policy.getInsuredPA();
							if(insuredPA != null){
								for (Insured insured2 : insuredPA) {
									if(insured2.getHealthCardNumber().equalsIgnoreCase(premiaInsured1.getIdCardNumber())){
										insured2.setNomineeDetails(nomineeDetailsForPA);
										insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForPA);
									}
								}
							}
						}
					}
		        }

		        /*if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
		        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
		        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
		        		PolicyRiskCover riskCover = new PolicyRiskCover();
		        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
		        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
		        		policyRiskCoverList.add(riskCover);
					}
		        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
		        }*/
		        
		        /**
		         * Get Policy Cover details
		         */
		        List<PremPolicyCoverDetails> premPolicyCoverDetails = premPolicyDetails.getPremPolicyCoverDetails();

		        if(premPolicyCoverDetails != null && ! premPolicyCoverDetails.isEmpty()){
		        	List<PolicyCoverDetails> policyRiskCoverList = new ArrayList<PolicyCoverDetails>();
		        	for (PremPolicyCoverDetails premPolicyRiskCover2 : premPolicyCoverDetails) {
		        		PolicyCoverDetails riskCover = new PolicyCoverDetails();
		        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
		        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
		        		if(premPolicyRiskCover2.getSumInsured() != null){
		        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
		        		}
		        		if(premPolicyRiskCover2.getRiskId() != null){
		        			riskCover.setRiskId(Long.valueOf(premPolicyRiskCover2.getRiskId()));
		        		}
		        		policyRiskCoverList.add(riskCover);
					}
		        	policy.setPolicyCoverDetails(policyRiskCoverList);
		        }
		        
		        if(premPolicyDetails.getPolicyZone() != null){
					 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
				}
		        
		        List<PremInsuredNomineeDetails> premProposerNomineeDetails = premPolicyDetails.getProperNomineeDetails();
		        
		        if(premProposerNomineeDetails != null && !premProposerNomineeDetails.isEmpty()){
		        	List<PolicyNominee> proposerNomineeDetails = premiaPolicyMapper.getProposerInsuredNomineeDetails(premProposerNomineeDetails);
		        	for (PolicyNominee proposerNominee : proposerNomineeDetails) {
						if(proposerNominee.getStrNomineeDOB() != null && !proposerNominee.getStrNomineeDOB().isEmpty()){
							if(isBaNCS){
								proposerNominee.setNomineeDob(SHAUtils.formatTimeFromString(proposerNominee.getStrNomineeDOB()));
							}else{
								proposerNominee.setNomineeDob(new Date(proposerNominee.getStrNomineeDOB()));
							}
						}
					}
		        	
		        	policy.setProposerNomineeDetails(proposerNomineeDetails);
		        	
		        }
		        /* Below code for product code - MED-PRD-073*/
		        if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
		        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
		        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
		        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))) {
		        
				        
				        if(premPolicyDetails.getGmcAilmentLimit() != null){
							List<MasAilmentLimit> ailmentLimit = premiaPolicyMapper.getAilmentLimit(premPolicyDetails.getGmcAilmentLimit());
							policy.setAilmentDetails(ailmentLimit);
						}
						
						if(premPolicyDetails.getGmcDeliveryLimit() != null){
							List<MasDeliveryExpLimit> deliveryLimit = premiaPolicyMapper.getDeliveryExpLimits(premPolicyDetails.getGmcDeliveryLimit());
							policy.setDeliveryExpLimit(deliveryLimit);
						}
						
						if(premPolicyDetails.getGmcCopayLimit() != null){
							List<MasCopayLimit> copayLimit = premiaPolicyMapper.getCopayLimit(premPolicyDetails.getGmcCopayLimit());
							policy.setCopayLimit(copayLimit);
						}
						if(premPolicyDetails.getGmcPrePostHospLimit() != null){
							List<MasPrePostHospLimit> prePostLimit = premiaPolicyMapper.getPrepostLimit(premPolicyDetails.getGmcPrePostHospLimit());
							policy.setPrePostLimit(prePostLimit);
						}
						
						if(premPolicyDetails.getGmcRoomRentLimit() != null){
							List<MasRoomRentLimit> roomRentList = premiaPolicyMapper.getRoomRentLimit(premPolicyDetails.getGmcRoomRentLimit());
							policy.setRoomRentLimit(roomRentList);
						}
						
						List<GpaBenefitDetails> benefitDetailsList = new ArrayList<GpaBenefitDetails>();
						
						List<PremGmcBenefitDetails> gmcPolicyConditions = premPolicyDetails.getGmcPolicyConditions();
						if(gmcPolicyConditions != null){
							for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
								GpaBenefitDetails benefitDetails = new GpaBenefitDetails();
								benefitDetails.setBenefitCode(premGmcBenefitDetails.getConditionCode());
								benefitDetails.setBenefitDescription(premGmcBenefitDetails.getConditionDesc());
								benefitDetails.setBenefitLongDescription(premGmcBenefitDetails.getConditionLongDesc());
								benefitDetailsList.add(benefitDetails);
							}
						}
						policy.setGpaBenefitDetails(benefitDetailsList);
						
					if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty()){
					
						if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
							policy.setSectionCode(SHAConstants.GMC_SECTION_A);
							policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
						}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
							policy.setSectionCode(SHAConstants.GMC_SECTION_C);
							policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
						}
					}
						
						
		        }
		      
		        
		        //MED-PRD-076
				if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
					Product product = null;
						if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty() && policy.getPolicyPlan() != null){
							if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("B")){
								product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
							}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("E")){
								//HOSPITAL_CASH_POLICY_IND_Bproduct = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_E);
								product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
							}
							else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("B")){
								product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_B);
							}
							else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("E")){
								product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_E);
							}
						}
						if(product != null){
							policy.setProduct(product);
						}
						
						if(premPolicyDetails.getPhcDays() != null && !premPolicyDetails.getPhcDays().isEmpty()){
							policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getPhcDays()));
						}
						policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
					}
				//added for GHC hospital cash product CR
				if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
					if(premPolicyDetails.getGhcDays() != null && !premPolicyDetails.getGhcDays().isEmpty()){
						policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getGhcDays()));
					}
				}
				//added for installment payment status flag and instalment method update at policy level changes done
				if(premPolicyDetails.getInstalmentFlag() != null && !premPolicyDetails.getInstalmentFlag().isEmpty() &&
						premPolicyDetails.getInstalmentFlag().equals("1")){
					policy.setPolicyInstalmentFlag(SHAConstants.YES_FLAG);
				}else {
					policy.setPolicyInstalmentFlag(SHAConstants.N_FLAG);	
				}
				
				if(premPolicyDetails.getBasePolicy() != null && !premPolicyDetails.getBasePolicy().isEmpty()){
					policy.setBasePolicyNo(premPolicyDetails.getBasePolicy());
				}
				
				if(premPolicyDetails.getInstallementMethod() != null && !premPolicyDetails.getInstallementMethod().isEmpty()){
					policy.setPolicyInstalmentType(premPolicyDetails.getInstallementMethod());
				}
				
				// ADD for PA ACC-PRD-020 as flt cover details adding 
				if(policy.getProduct() != null && policy.getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY)){
					
					List<PremPolicyCoverDetails> premPolicyCover = premPolicyDetails.getPremPolicyCoverDetails();

					if(premPolicyCover != null && ! premPolicyCover.isEmpty()){
						List<Insured> insuredPA = policy.getInsuredPA();
						if(insuredPA != null && premPolicyCover.size() >= 1){
							for (Insured insured2 : insuredPA) {	
								List<InsuredCover> insuredCovers = new ArrayList<InsuredCover>();
								PremPolicyCoverDetails premPolicyRiskCover2 = premPolicyCover.get(0);
								InsuredCover riskCover = new InsuredCover();
								riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
								riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
								if(premPolicyRiskCover2.getSumInsured() != null){
									riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
								}/*
								if(premPolicyRiskCover2.getRiskId() != null){
									riskCover.setInsuredKey(Long.valueOf(insured2.getInsuredId()));
								}*/
								insuredCovers.add(riskCover);
								insured2.setCoverDetailsForPA(insuredCovers);
							}
						}
					}
				}
				  
		        // GLX2020054 add for star novel grp product
		        if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())){
		        	Product product = null;
		        	if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_A);
						policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
						product = getCoronaGrpForProduct(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM);
						policy.setPolicyPlan("L");
					}else if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_INDEMNITY)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_C);
						policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
						product = getCoronaGrpForProduct(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY);
						policy.setPolicyPlan("I");
					}
		        	if(product != null){
						policy.setProduct(product);
					}
		        	policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
		        	policy.setPolicyTerm(1l);
				}
		        
		        if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_GRP_COVID_PROD_CODE.equals(premPolicyDetails.getProductCode())){
		        	Product product = null;
		        	if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_A);
						policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
						product = getCoronaGrpForProduct(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM);
						policy.setPolicyPlan("L");
					}else if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_INDEMNITY)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_C);
						policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
						product = getCoronaGrpForProduct(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI);
						policy.setPolicyPlan("I");
					}
		        	if(product != null){
						policy.setProduct(product);
					}
		        	policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
		        	if(premPolicyDetails != null && premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()){
		        		Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
		        		policy.setPolicyTerm(policyTerm);
		        	}
					 
				}
		        //added for saving UIN in policy table
		        if(premPolicyDetails.getPremiaUINCode() != null && !premPolicyDetails.getPremiaUINCode().isEmpty()) {
		        	policy.setPremiaUINCode(premPolicyDetails.getPremiaUINCode());
		        }
				
		      //added for corpBufferLimit New column insert only gmc
				if(premPolicyDetails.getGmcCorpBufferLimit() != null){
					List<GmcCoorporateBufferLimit> corpBufferLimitList = premiaPolicyMapper.getCorpBufferLimit(premPolicyDetails.getGmcCorpBufferLimit());
					policy.setGmcCorpBufferLimit(corpBufferLimitList);
				}
				if(premPolicyDetails.getNacbBuffer() != null && !premPolicyDetails.getNacbBuffer().isEmpty()) {
					policy.setNacbBufferAmt(Double.valueOf(premPolicyDetails.getNacbBuffer()));
				}
				if(premPolicyDetails.getVintageBuffer() != null && !premPolicyDetails.getVintageBuffer().isEmpty()) {
					policy.setWintageBufferAmt(Double.valueOf(premPolicyDetails.getVintageBuffer()));
				}
				//added for topup policy data saving process
				if(premPolicyDetails.getTopUpPolicyNo() != null && !premPolicyDetails.getTopUpPolicyNo().isEmpty()) {
					policy.setTopupPolicyNo(premPolicyDetails.getTopUpPolicyNo());
				}
		        createGMCproduct(premPolicyDetails, policy, premiaPolicyMapper,isEndorsement);
		        // gpa cover details
		        createGPAproduct(premPolicyDetails, policy, premiaPolicyMapper,isEndorsement);
		        
		        if(premPolicyDetails.getProductCode() != null && ! ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(premPolicyDetails.getProductCode()) 
		        		&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
		        		&&	! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)){
		        	if(isEndorsement){
		        		updatePolicyDetailsAfterEndorsement(policy, previousPolicyList, endorsementDetailsList);
		        	}else{
		        		Policy policy2 = getPolicy(policy.getPolicyNumber());
			        	if(policy2 == null){
			        		createPolicy(policy, previousPolicyList, endorsementDetailsList);
			        	}else{
			        		//insert or update insured details
			        		
			        		saveInsuredDetails(policy, policy2);
			        	}
		        	}
		        	
		        }
		        
		        if(ReferenceTable.getMasterPolicyAvailableProducts().containsKey(premPolicyDetails.getProductCode()) || 
		        		((premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())
		        		|| premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_GRP_COVID_PROD_CODE.equals(premPolicyDetails.getProductCode()))
		        		&& premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM))){
		        	if(null != premPolicyDetails.getMasterPolicyNumber() && !premPolicyDetails.getMasterPolicyNumber().isEmpty()){
						PremPolicyDetails masterPremPolicyDetails = fetchJetPrivillagePolicyDetailsFromPremia(premPolicyDetails.getMasterPolicyNumber());
						saveMasterPolicyForJetPrivillage(masterPremPolicyDetails);
		        	}
				}
		        
		        savePrevPortabilityPolicy(premPolicyDetails);		
		        
				policy = getPolicy(premPolicyDetails.getPolicyNo());
			//}

			utx.commit();
			return policy;
		}
		
		
		
		catch(Exception e) {
			log.error("___________________  POLICY ERROR WHILE SAVING THE DATA ___________________" + premPolicyDetails.getPolicyNo());
			e.printStackTrace();
			utx.rollback();
			log.error("**************ERROR IN POPULATE POLICY DATA METHOD*********" + e.getMessage());
			return null;
		}
	}

	private void createGPAproduct(PremPolicyDetails premPolicyDetails,
			Policy policy, PremiaToPolicyMapper premiaPolicyMapper, Boolean isEndorsement)
			throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		if(premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE) ||
				premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)){
	        List<PremPolicyRiskCover> premPolicyGpaCoverdetails = premPolicyDetails.getGpaCoverDetails();

	        if(premPolicyGpaCoverdetails != null && ! premPolicyGpaCoverdetails.isEmpty()){
	        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
	        	for (PremPolicyRiskCover gpaCoverDetail : premPolicyGpaCoverdetails) {
	        		PolicyRiskCover riskCover = new PolicyRiskCover();
	        		riskCover.setCoverCode(gpaCoverDetail.getCoverCode());
	        		riskCover.setCoverCodeDescription(gpaCoverDetail.getCoverDescription());
	        		if(gpaCoverDetail.getSumInsured() != null){
	        			riskCover.setSumInsured(Double.valueOf(gpaCoverDetail.getSumInsured()));
	        		}
	        		policyRiskCoverList.add(riskCover);
				}
	        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
	        }
	        
	        Map<String, String> gpaType = ReferenceTable.getGpaType();
	        String sectionCode = premPolicyDetails.getSectionCode();
	        if(sectionCode != null){
	        	String gpatypeValue = gpaType.get(sectionCode);
	        	policy.setGpaPolicyType(gpatypeValue);
	        }
	        
	        List<Insured> gpaInsured = new ArrayList<Insured>();
	        
	        if(premPolicyDetails.getGpaInsuredDetails() != null){
	        	 
		        gpaInsured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getGpaInsuredDetails());
		        for (Insured insured2 : gpaInsured) {
		        	
		        	if(insured2.getStrInsuredAge() != null && !(insured2.getStrInsuredAge().isEmpty())){
		        		insured2.setInsuredAge(Double.valueOf(insured2.getStrInsuredAge()));
		        	}
		        	
		        	if(insured2.getStrGender() != null && ! insured2.getStrGender().isEmpty()) {
		        		MastersValue genderMaster =  getKeyByValue(insured2.getStrGender().trim());
		        		if(genderMaster != null){
		        		insured2.setInsuredGender(genderMaster);
		        		}
		        	}
		        	
		        	if(insured2.getStrDateOfBirth() != null && !(insured2.getStrDateOfBirth().isEmpty())) {
		        		Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
		        		insured2.setInsuredDateOfBirth(insuredDOB);
		        	}
		        	List<InsuredCover> insuredCoverDetails = new ArrayList<InsuredCover>();
					if(insured2.getStrEffectivedFromDate() != null){
						insured2.setEffectiveFromDate(new Date(insured2.getStrEffectivedFromDate()));
					}
					if(insured2.getStrEffectiveToDate() != null){
						insured2.setEffectiveToDate(new Date(insured2.getStrEffectiveToDate()));
					}
					
					if(insured2.getTable1() != null && ! insured2.getTable1().isEmpty() && ! insured2.getTable1().equalsIgnoreCase("0")){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TABLE_1);
						insuredCover.setCoverCodeDescription(SHAConstants.TABLE_1_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTable1()));
						insuredCoverDetails.add(insuredCover);
						
					}
					if(insured2.getTable2() != null && ! insured2.getTable2().isEmpty() && ! insured2.getTable2().equalsIgnoreCase("0")){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TABLE_2);
						insuredCover.setCoverCodeDescription(SHAConstants.TABLE_2_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTable2()));
						insuredCoverDetails.add(insuredCover);
					}
					if(insured2.getTable3() != null && ! insured2.getTable3().isEmpty() && ! insured2.getTable3().equalsIgnoreCase("0")){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TABLE_3);
						insuredCover.setCoverCodeDescription(SHAConstants.TABLE_3_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTable3()));
						insuredCoverDetails.add(insuredCover);
					}
					if(insured2.getTable4() != null && ! insured2.getTable4().isEmpty() && ! insured2.getTable4().equalsIgnoreCase("0")){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TABLE_4);
						insuredCover.setCoverCodeDescription(SHAConstants.TABLE_4_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTable4()));
						insuredCoverDetails.add(insuredCover);
					}
					
					if(insured2.getHospitalExpensive() != null && ! insured2.getHospitalExpensive().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.HOSP_EXPENSES_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.HOSP_EXPENSES_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getHospitalExpensive()));
						insuredCoverDetails.add(insuredCover);
						
					}
					
					if(insured2.getInPatient() != null && ! insured2.getInPatient().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.INPATIENT_HOSP_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.INPATIENT_HOSP_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getInPatient()));
						insuredCoverDetails.add(insuredCover);
						
					}else if(insured2.getMedicalExtensionOtherPaClaim() != null && ! insured2.getMedicalExtensionOtherPaClaim().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.INPATIENT_HOSP_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.INPATIENT_HOSP_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getMedicalExtensionOtherPaClaim()));
						insuredCoverDetails.add(insuredCover);
						
					}
					
					if(insured2.getMedicalExtension() != null && ! insured2.getMedicalExtension().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.MEDICAL_EXTENSION_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.MEDICAL_EXTENSION_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getMedicalExtension()));
						insuredCoverDetails.add(insuredCover);
						
					} else if(insured2.getMedicalExtensionOtherPaClaim() != null && ! insured2.getMedicalExtensionOtherPaClaim().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.MEDICAL_EXTENSION_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.MEDICAL_EXTENSION_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getMedicalExtensionOtherPaClaim()));
						insuredCoverDetails.add(insuredCover);
						
					}
					
					if(insured2.getOutPatient() != null && ! insured2.getOutPatient().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.OUT_PATIENT_EXPENCES_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.OUT_PATIENT_EXPENCES_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getOutPatient()));
						insuredCoverDetails.add(insuredCover);
						
					}
					
					if(insured2.getTransMortRem() != null && ! insured2.getTransMortRem().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TRANS_MORT_REMS_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.TRANS_MORT_REMS_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTransMortRem()));
						insuredCoverDetails.add(insuredCover);
						
					}
					
					if(insured2.getTutionFees() != null && ! insured2.getTutionFees().isEmpty()){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.TUTION_FEES_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.TUTION_FEES_COVER_DESC);
						insuredCover.setSumInsured(Double.valueOf(insured2.getTutionFees()));
						insuredCoverDetails.add(insuredCover);
						
					}							
				

					if(insured2.getEarningParentSI() != null &&  insured2.getEarningParentSI() != 0){
						InsuredCover insuredCover = new InsuredCover();
						insuredCover.setCoverCode(SHAConstants.EARNING_PARENT_SI_COVER_CODE);
						insuredCover.setCoverCodeDescription(SHAConstants.EARNING_PARENT_SI_COVER_DESC);
						insuredCover.setSumInsured(insured2.getEarningParentSI());
						insuredCoverDetails.add(insuredCover);
						
					}	
					insured2.setCoverDetailsForPA(insuredCoverDetails);
				}
	        }
	        
	        
	        List<PremGpaBenefitDetails> premGpaBenefitDetails = fetchGpaPolicyBenefitFromPremia(premPolicyDetails.getPolicyNo());
	        if(premGpaBenefitDetails != null){
	        	  List<GpaBenefitDetails> gpaBenefitDetails = premiaPolicyMapper.getGpaBenefitDetails(premGpaBenefitDetails);
			      policy.setGpaBenefitDetails(gpaBenefitDetails);

	        }
	        if(policy.getGpaPolicyType() != null && policy.getGpaPolicyType().equalsIgnoreCase(SHAConstants.GPA_UN_NAMED_POLICY_TYPE)){
	        	if(premPolicyDetails.getRiskSysId() != null){
	        		policy.setUnNamedInsuredId(Long.valueOf(premPolicyDetails.getRiskSysId()));
	        	}
	        }
	        if(isEndorsement){
	        	updateGPAPolicyEndorsement(policy, gpaInsured);
			}else{
				createGpaPolicy(policy, gpaInsured);
			}
        }
	}

	private void createGMCproduct(PremPolicyDetails premPolicyDetails,
			Policy policy, PremiaToPolicyMapper premiaPolicyMapper,Boolean isEndorsement)
			throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		if(premPolicyDetails.getProductCode() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(premPolicyDetails.getProductCode())){
			List<Insured> gmcInsured = new ArrayList<Insured>();
			if(premPolicyDetails.getGmcInsuredDetails() != null){
				gmcInsured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getGmcInsuredDetails());
				
				List<PremInsuredDetails> gmcInsuredDetails = premPolicyDetails.getGmcInsuredDetails();
				try{
				if(gmcInsuredDetails != null && ! gmcInsuredDetails.isEmpty()){
					for (PremInsuredDetails premInsuredDetails : gmcInsuredDetails) {
						for (Insured insured : gmcInsured) {
							if(insured.getInsuredId().equals(Long.valueOf(premInsuredDetails.getRiskSysId()))){
								
								log.info("Employee ID is updated for this insured number ------>" + insured.getInsuredEmailId());
								
								//Date formatPremiaDate = SHAUtils.formatPremiaDate(premInsuredDetails.getDob());
//								Date formatPremiaDate = SHAUtils.formatPremiaDate(new Date(premInsuredDetails.getDob()).toString());
								 if(premInsuredDetails.getInsuredAge() != null){
									 Double insuredAge = SHAUtils.getDoubleValueFromString(premInsuredDetails.getInsuredAge());
							 		 if(insuredAge != null){
							 			 insured.setInsuredAge(insuredAge);
							 		 } 
								 }
								 
								 if(premInsuredDetails.getInsuredEmail() != null){
									 insured.setInsuredEmailId(premInsuredDetails.getInsuredEmail());
								 }
								 
								if(premInsuredDetails.getDob() != null && ! premInsuredDetails.getDob().isEmpty()){
									 Date formatPremiaDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premInsuredDetails.getDob())));
									 insured.setInsuredDateOfBirth(formatPremiaDate);
								}
								
								if(insured.getStrGender() != null && !insured.getStrGender().equalsIgnoreCase("")){
									MastersValue genderMaster =  getKeyByValue(insured.getStrGender());
									if(genderMaster != null && genderMaster.getKey() != null){
										insured.setInsuredGender(genderMaster);
									}
								}
								/**
								 * As per Sathish ,if gender is blank  from premia, then
								 *  default it to MALE.
								 * */
								/*else
								{
									MastersValue genderMaster =  getKeyByValue("MALE");
									if(genderMaster != null){
										insured.setInsuredGender(genderMaster);
									}
								}*/
								List<GmcContinuityBenefit> gmcContBenefits = new ArrayList<GmcContinuityBenefit>();
								if(premInsuredDetails.getContinuityBenefits() != null && !premInsuredDetails.getContinuityBenefits().isEmpty()){
									gmcContBenefits = premiaPolicyMapper.getGMCInsuredContBenDetails(premInsuredDetails.getContinuityBenefits());
									insured.setGmcContBenefitDtls(gmcContBenefits);
								}
								
								if(premInsuredDetails.getDependantSIFlag() != null && premInsuredDetails.getDependantSIFlag().isEmpty()){
									insured.setDependentSIFlag(SHAConstants.N_FLAG);
								}
								
								if(premInsuredDetails.getGmcNomineeDetails() != null && !premInsuredDetails.getGmcNomineeDetails().isEmpty()){
									insured.setProposerInsuredNomineeDetails(premiaPolicyMapper.getProposerInsuredNomineeDetails(premInsuredDetails.getGmcNomineeDetails()));
								}
								
								if(premInsuredDetails.getPedDetails() != null && !premInsuredDetails.getPedDetails().isEmpty()){
									List<InsuredPedDetails> gmcInsuredPedList = new ArrayList<InsuredPedDetails>();
									for (PremPEDDetails premPedDetail : premInsuredDetails.getPedDetails()) {
										InsuredPedDetails insuredPed = new InsuredPedDetails();
										insuredPed.setPedCode(premPedDetail.getGmcPedCode());
										insuredPed.setPedDescription(premPedDetail.getGmcPedDescription());
										gmcInsuredPedList.add(insuredPed);
									}
									insured.setInsuredPedList(gmcInsuredPedList);
								}
								
								//VIP - CR2019114
								if(premInsuredDetails.getVipPolicy() != null && premInsuredDetails.getVipPolicy().equals("1")){
									insured.setVipFlag(SHAConstants.YES_FLAG);
								}
								
								//added for GHC hospital cash product
								
								if(premInsuredDetails.getGhcPolicyType() !=null && !premInsuredDetails.getGhcPolicyType().isEmpty())
								{
									insured.setGhcPolicyType(premInsuredDetails.getGhcPolicyType());
								}
								
								if(premInsuredDetails.getGhcScheme() !=null && !premInsuredDetails.getGhcScheme().isEmpty())
								{
									insured.setGhcScheme(premInsuredDetails.getGhcScheme());
								}
								
								
								 break;
							}
						}
					}
				}
			 }catch(Exception e){
				 
			 }
			}
			if(premPolicyDetails.getGmcAilmentLimit() != null){
				List<MasAilmentLimit> ailmentLimit = premiaPolicyMapper.getAilmentLimit(premPolicyDetails.getGmcAilmentLimit());
				policy.setAilmentDetails(ailmentLimit);
			}
			
			if(premPolicyDetails.getGmcDeliveryLimit() != null){
				List<MasDeliveryExpLimit> deliveryLimit = premiaPolicyMapper.getDeliveryExpLimits(premPolicyDetails.getGmcDeliveryLimit());
				policy.setDeliveryExpLimit(deliveryLimit);
			}
			
			if(premPolicyDetails.getGmcCopayLimit() != null){
				List<MasCopayLimit> copayLimit = premiaPolicyMapper.getCopayLimit(premPolicyDetails.getGmcCopayLimit());
				policy.setCopayLimit(copayLimit);
			}
			if(premPolicyDetails.getGmcPrePostHospLimit() != null){
				List<MasPrePostHospLimit> prePostLimit = premiaPolicyMapper.getPrepostLimit(premPolicyDetails.getGmcPrePostHospLimit());
				policy.setPrePostLimit(prePostLimit);
			}
			
			if(premPolicyDetails.getGmcRoomRentLimit() != null){
				List<MasRoomRentLimit> roomRentList = premiaPolicyMapper.getRoomRentLimit(premPolicyDetails.getGmcRoomRentLimit());
				policy.setRoomRentLimit(roomRentList);
			}
			
			if(premPolicyDetails.getGmcInsuredDetails() != null){
				List<PremInsuredDetails> insuredDetails = premPolicyDetails.getGmcInsuredDetails();
				if(insuredDetails != null){
					for (PremInsuredDetails premInsuredDetails : insuredDetails) {
						List<PremDependentInsuredDetails> dependentDetailsList = premInsuredDetails.getDependentDetailsList();
						List<Insured> insuredFromPremiaDepenedent = premiaPolicyMapper.getInsuredFromPremiaDepenedent(dependentDetailsList);
						for (Insured insuredGmc : gmcInsured) {
							if(premInsuredDetails.getRiskSysId().equalsIgnoreCase(insuredGmc.getInsuredId().toString())){
								for (Insured insured2 : insuredFromPremiaDepenedent) {
									insured2.setDependentRiskId(insuredGmc.getInsuredId());
									if(insured2.getStrInsuredAge() != null){
										insured2.setInsuredAge(SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge()));
									}
									
									if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
										MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
										if(genderMaster != null && genderMaster.getKey() != null){
											insured2.setInsuredGender(genderMaster);
										}
									}
									/**
									 * As per Sathish ,if gender is blank  from premia, then
									 *  default it to MALE.
									 * */
									/*else
									{
										MastersValue genderMaster =  getKeyByValue("MALE");
										if(genderMaster != null){
											insured2.setInsuredGender(genderMaster);
										}
									}*/
									
									if(dependentDetailsList != null && !dependentDetailsList.isEmpty()){
										for (PremDependentInsuredDetails dependentInsured : dependentDetailsList) {
											if(dependentInsured.getRiskSysId().equalsIgnoreCase(insured2.getInsuredId().toString())){
												List<GmcContinuityBenefit> gmcContBenefits = new ArrayList<GmcContinuityBenefit>();
												if(dependentInsured.getContinuityBenefits() != null && !dependentInsured.getContinuityBenefits().isEmpty()){
													gmcContBenefits = premiaPolicyMapper.getGMCInsuredContBenDetails(dependentInsured.getContinuityBenefits());
													insured2.setGmcContBenefitDtls(gmcContBenefits);
												}
												
												if(dependentInsured.getPedDetails() != null && !dependentInsured.getPedDetails().isEmpty()){
													List<InsuredPedDetails> gmcInsuredPedList = new ArrayList<InsuredPedDetails>();
													for (PremPEDDetails premPedDetail : dependentInsured.getPedDetails()) {
														InsuredPedDetails insuredPed = new InsuredPedDetails();
														insuredPed.setPedCode(premPedDetail.getGmcPedCode());
														insuredPed.setPedDescription(premPedDetail.getGmcPedDescription());
														gmcInsuredPedList.add(insuredPed);
													}
													insured2.setInsuredPedList(gmcInsuredPedList);
												}
												
											}
										}
									}
									
									//VIP - CR2019114
									if(insuredGmc.getVipFlag() != null && insuredGmc.getVipFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
										insured2.setVipFlag(SHAConstants.YES_FLAG);
									}
									
								}
							}
						}
						policy.setDependentInsuredId(insuredFromPremiaDepenedent);
					}
				}
				
			}
			
			List<GpaBenefitDetails> benefitDetailsList = new ArrayList<GpaBenefitDetails>();
			
			List<PremGmcBenefitDetails> gmcPolicyConditions = premPolicyDetails.getGmcPolicyConditions();
			if(gmcPolicyConditions != null){
				for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
					GpaBenefitDetails benefitDetails = new GpaBenefitDetails();
					benefitDetails.setBenefitCode(premGmcBenefitDetails.getConditionCode());
					benefitDetails.setBenefitDescription(premGmcBenefitDetails.getConditionDesc());
					benefitDetails.setBenefitLongDescription(premGmcBenefitDetails.getConditionLongDesc());
					benefitDetailsList.add(benefitDetails);
				}
			}
			policy.setGpaBenefitDetails(benefitDetailsList);
			
			//CR2019054 - TOP-UP POLICY
			List<PremRelatedPolicies> premRelatedPolicyDetailsList = premPolicyDetails.getRelatedPolicies();
			if(premRelatedPolicyDetailsList != null && !premRelatedPolicyDetailsList.isEmpty()){
				List<RelatedPolicies> relatedPolicyDetailsList = new ArrayList<RelatedPolicies>();
				for (PremRelatedPolicies premRelatedPolicyDetail : premRelatedPolicyDetailsList) {
					RelatedPolicies relatedPolicy = new RelatedPolicies();
					relatedPolicy.setPolicyNo(premPolicyDetails.getPolicyNo());
					relatedPolicy.setLinkPolicyNo(premRelatedPolicyDetail.getPolicyNo());
					relatedPolicy.setLinkType(premRelatedPolicyDetail.getPolicyType());
					relatedPolicy.setActiveStatus(1);
					relatedPolicyDetailsList.add(relatedPolicy);
				}
				policy.setRelatedPolicies(relatedPolicyDetailsList);
			}
			//added for corpBufferLimit New column insert
			if(premPolicyDetails.getGmcCorpBufferLimit() != null){
				List<GmcCoorporateBufferLimit> corpBufferLimitList = premiaPolicyMapper.getCorpBufferLimit(premPolicyDetails.getGmcCorpBufferLimit());
				policy.setGmcCorpBufferLimit(corpBufferLimitList);
			}
			if(premPolicyDetails.getNacbBuffer() != null && !premPolicyDetails.getNacbBuffer().isEmpty()) {
				policy.setNacbBufferAmt(Double.valueOf(premPolicyDetails.getNacbBuffer()));
			}
			if(premPolicyDetails.getVintageBuffer() != null && !premPolicyDetails.getVintageBuffer().isEmpty()) {
				policy.setWintageBufferAmt(Double.valueOf(premPolicyDetails.getVintageBuffer()));
			}
			//added for topup policy data saving process
			if(premPolicyDetails.getTopUpPolicyNo() != null && !premPolicyDetails.getTopUpPolicyNo().isEmpty()) {
				policy.setTopupPolicyNo(premPolicyDetails.getTopUpPolicyNo());
			}
			if(isEndorsement){
				updateGMCPolicyEndorsement(policy, gmcInsured);
			}else{
				createGMCPolicy(policy, gmcInsured);
			}
			
			
		}
	}

	@SuppressWarnings("deprecation")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createPolicy(Policy policy,List<PreviousPolicy> previousPolicyList, List<PolicyEndorsementDetails> policyEndorsementList) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
	{
		if (null != policy) {
			entityManager.merge(policy);
			entityManager.flush();
		}	
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		if(null != policy.getInsured() && !policy.getInsured().isEmpty()) {
			
			Insured jetMainMember = null;
			
			for (Insured  objInsured : policy.getInsured())  {
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
						|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
						|| policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))){
					if(jetMainMember == null) {
						jetMainMember = getJetMainMemberInsured(policy);
					}
					if(objInsured.getMainMember() != null && objInsured.getMainMember().equalsIgnoreCase(SHAConstants.N_FLAG)){
						if(jetMainMember != null) {
							objInsured.setDependentRiskId(jetMainMember.getInsuredId() != null ? jetMainMember.getInsuredId() : null);
						}
					}
					if(objInsured.getDeductibleAmount() != null){
						objInsured.setDeductibleAmount(objInsured.getDeductibleAmount());
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
						//added for CR ped effective date save in PED table GLX0132
						if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//							Date fromDate = SHAUtils.baNCSDateTime(insuredPed.getStrEffectivedFromDate());
//							if(fromDate != null){
								insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));	
//							}
						}
						if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//							Date fromDate = SHAUtils.baNCSDateTime(insuredPed.getStrEffectiveToDate());
//							if(fromDate != null){
								insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));	
//							}
						}
						if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
							insuredPed.setPedType(insuredPed.getPedType());
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
		
		if (null != previousPolicyList && !previousPolicyList.isEmpty()) {

			// Query query =
			// entityManager.createNamedQuery("PreviousPolicy.findByCurrentPolicy").setParameter("policyNumber",
			// policy.getPolicyNumber());
			// List<PreviousPolicy> existingList = query.getResultList();
			for (PreviousPolicy prevPolicy : previousPolicyList) {
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
				|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
				|| policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))){
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
					
					//added for CR GLX2020066 by noufel
					if(masRoomRentLimit.getProportionateFlag() != null && !masRoomRentLimit.getProportionateFlag().isEmpty()){
						if(masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES) ||
								masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) || 
								masRoomRentLimit.getProportionateFlag().equalsIgnoreCase("A")){
							masRoomRentLimit.setProportionateFlag(SHAConstants.YES_FLAG);
						}else {
							masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
						}
					}
					else{
						masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
					}
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
				String endorsementText = policyEndorsementDetails.getEndorsementText();
//					if(endorsementText != null){
//						endorsementText = endorsementText.substring(0, 900);
//						policyEndorsementDetails.setEndorsementText(endorsementText);
//					}
				policyEndorsementDetails.setPolicy(policy2);
				entityManager.persist(policyEndorsementDetails);
				entityManager.flush();
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
		
		//added for GMC corpBufferLimit Value save CR by noufel
				List<GmcCoorporateBufferLimit> corpBufferLimitList = policy.getGmcCorpBufferLimit();
				
				if(corpBufferLimitList != null){
					for (GmcCoorporateBufferLimit corpBufferLimit : corpBufferLimitList) {
						corpBufferLimit.setPolicyKey(policy2.getKey());
						corpBufferLimit.setActiveStatus(1l);
						corpBufferLimit.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						corpBufferLimit.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
						if(corpBufferLimit.getKey() == null){
							entityManager.persist(corpBufferLimit);
							entityManager.flush();
						}else{
							corpBufferLimit.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
							corpBufferLimit.setModifiedDate(new Timestamp(System
									.currentTimeMillis()));
							corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
							entityManager.merge(corpBufferLimit);
							entityManager.flush();
						}
					}
				}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createGpaPolicy(Policy policy,List<Insured> insured) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
	{
		
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		if (null == policy2) {
			entityManager.persist(policy);
			entityManager.flush();
			
			
			List<PolicyRiskCover> policyRiskCoverDetails = policy.getPolicyRiskCoverDetails();
			
			if(policyRiskCoverDetails != null && !policyRiskCoverDetails.isEmpty()){
				for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
					policyRiskCover.setPolicyKey(policy.getKey());
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
					policyRiskCover.setPolicyKey(policy.getKey());
					if(policyRiskCover.getKey() == null){
						entityManager.persist(policyRiskCover);
						entityManager.flush();
					} else {
						entityManager.merge(policyRiskCover);
						entityManager.flush();
					}
				}
			}
			
			policy2 = policy;
		}
		
		if(policy.getGpaPolicyType() != null && policy.getGpaPolicyType().equalsIgnoreCase(SHAConstants.GPA_UN_NAMED_POLICY_TYPE)){
			
			if(insured.size() == 2){
				List<String> riskNumber = new ArrayList<String>();
				for (Insured insured2 : insured) {
					if(riskNumber.contains(insured2.getInsuredId().toString())){
						Long insuredId = insured2.getInsuredId();
						String risk1 = insuredId.toString();
						risk1 = risk1+"00";
						insuredId = Long.valueOf(risk1);
						insured2.setInsuredId(insuredId);
						break;
					}else{
						riskNumber.add(insured2.getInsuredId().toString());
					}
				}
			}
		}

		List<Insured> ListOfInsured = getInsuredListByPolicyNo(policy2.getPolicyNumber());
		List<Long> riskNumberList = new ArrayList<Long>();
		for (Insured insured2 : ListOfInsured) {
			riskNumberList.add(insured2.getInsuredId());
		}
		
		if(null != insured && !insured.isEmpty()) {
			List<String> riskNumber = new ArrayList<String>();
			for (Insured  objInsured : insured)  {
				if(!riskNumberList.contains(objInsured.getInsuredId())){
				objInsured.setPolicy(policy2);
				objInsured.setLopFlag("P");
				if(policy2.getGpaPolicyType() != null && policy2.getGpaPolicyType().equalsIgnoreCase(SHAConstants.GPA_UN_NAMED_POLICY_TYPE)
						&& policy.getUnNamedInsuredId() != null && riskNumber.contains(policy.getUnNamedInsuredId().toString())){
					
					objInsured.setInsuredId(policy.getUnNamedInsuredId());
					riskNumber.add(policy.getUnNamedInsuredId().toString());
					
				}
				if(policy2.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
				}
				if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
					objInsured.setInsuredPinCode(null);
				}
				if(objInsured != null){
					entityManager.persist(objInsured);
					entityManager.flush();
				}
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
				}
			  }
			}
		}

		
		
		List<GpaBenefitDetails> gpaBenefitDetails = policy.getGpaBenefitDetails();
		if(gpaBenefitDetails != null && !gpaBenefitDetails.isEmpty()){
			List<GpaBenefitDetails> policyBenefitDetails = getPolicyBenefitDetails(policy2.getKey());
			List<Integer> benefitSerialNoList = new ArrayList<Integer>();
			if(policyBenefitDetails != null && ! policyBenefitDetails.isEmpty()){
				for (GpaBenefitDetails existingBenefitDetail : policyBenefitDetails) {
					benefitSerialNoList.add(existingBenefitDetail.getSrNumber());
				}
			}
			
			for (GpaBenefitDetails gpaBenefitDetails2 : gpaBenefitDetails) {
				gpaBenefitDetails2.setPolicyKey(policy2.getKey());
				if(! benefitSerialNoList.contains(gpaBenefitDetails2.getSrNumber())){
					if(gpaBenefitDetails2.getKey() != null){
						entityManager.merge(gpaBenefitDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(gpaBenefitDetails2);
						entityManager.flush();
					}
				}
				
				
			}
		}
		
	}
	
	public Boolean processSavedIntimationRevised(Intimation intimationObj, PremiaIntimationTable premiaIntimation) throws IllegalStateException, SecurityException, SystemException {
		try {
			utx.begin();
			Intimation intimation = getIntimationByKey(intimationObj.getKey());
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> "
					+ intimation != null ? intimation.getIntimationId()
					: (intimation != null ? intimation.getIntimationId()
							+ "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
							: "NO INTIMATIONS"));
			log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> " + intimation != null ? intimation.getIntimationId() : (intimation != null ? intimation.getIntimationId() + "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US" : "NO INTIMATIONS") );

			IntimationRule intimationRule = new IntimationRule();
			/*IntimationType a_message = new IntimationType();
			ClaimRequestType claimReqType = new ClaimRequestType();
<<<<<<< HEAD
			ClaimType claimType = new ClaimType();*/

			String strClaimType = "";
			Boolean isHospTopUpIntmAvail = false;
			/**
			 * All PA claims goes to manual registration only. Hence
			 * below condition was added.
			 * */
		
			//added by noufel for HC topup intimatin creation
			if(isHospCashTopUpAvail(intimation.getIntimationId())) {
				isHospTopUpIntmAvail =true;
			}
			if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
				strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
			}
			else if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
				strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
			}
			if(isHospTopUpIntmAvail){
				strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
			}
			//claimType.setClaimType(strClaimType);
			if (null != intimation.getCpuCode()
					&& null != intimation.getCpuCode().getCpuCode())
				/*claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode()
						.getCpuCode()));
			a_message.setKey(intimation.getKey());*/
			if (null != intimation.getAdmissionDate()) {
				String date = String.valueOf(intimation.getAdmissionDate());
				String date1 = date.replaceAll("-", "/");
				//a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
//				a_message.setIntDate(new Timestamp(System.currentTimeMillis()));
//				Timestamp timestamp = new Timestamp(
//						System.currentTimeMillis() + 60 * 60 * 1000);
//				a_message.setIntDate(timestamp);
			}
			/*a_message.setIntimationNumber(intimation.getIntimationId());
			a_message
					.setIntimationSource(intimation.getIntimationSource() != null ? (intimation
							.getIntimationSource().getValue() != null ? intimation
							.getIntimationSource().getValue() : "")
							: "");
			a_message.setIsClaimPending(!intimationRule
					.isClaimExist(intimation));
			a_message
					.setIsPolicyValid(intimationRule.isPolicyValid(intimation));*/
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double balsceSI = 0d;
			if(null != intimation && null != intimation.getPolicy() && 
					null != intimation.getPolicy().getProduct() && 
					null !=intimation.getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
				
				if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
					balsceSI = dbCalculationService.getBalanceSIForGMC(intimation.getPolicy().getKey() ,intimation.getInsured().getKey(),0l);
				}else{
					balsceSI = dbCalculationService.getBalanceSI(
							intimation.getPolicy().getKey(),
							intimation.getInsured().getKey(), 0l,
							intimation.getInsured().getInsuredSumInsured(),
							intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
						
				}
			}
			else
			{
				balsceSI = dbCalculationService.getGPABalanceSI(
						intimation.getPolicy().getKey(),
						intimation.getInsured().getKey(), 0l,
						intimation.getInsured().getInsuredSumInsured(),
						intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}
			

			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
					.equalsIgnoreCase(strClaimType)) {
				if (null != balsceSI
						&& balsceSI > 0
						&& null != intimation.getPolicy()
						&& (SHAConstants.NEW_POLICY)
								.equalsIgnoreCase(intimation.getPolicy()
										.getPolicyStatus())
						&& null != strPremiaFlag
						&& ("true").equalsIgnoreCase(strPremiaFlag)) {
					//a_message.setIsBalanceSIAvailable(true);
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
					String get64vbStatus = PremiaService.getInstance()
							.get64VBStatus(
									intimation.getPolicy().getPolicyNumber(), intimation.getIntimationId());
					
					/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
						get64vbStatus = "R";
					}*/
					
					if (get64vbStatus != null
							&& (SHAConstants.DISHONOURED
									.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
									.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
									.equalsIgnoreCase(get64vbStatus))) {
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");//Changed N to Y due to GLX2021067 
					}
				} else {
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");//Changed N to Y due to GLX2021067
					if (null != intimation.getPolicy()
							&& !(SHAConstants.NEW_POLICY)
									.equalsIgnoreCase(intimation.getPolicy()
											.getPolicyStatus())) {
						Boolean endorsedPolicyStatus = PremiaService
								.getInstance().getEndorsedPolicyStatus(
										intimation.getPolicy()
												.getPolicyNumber());
						/*
						if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
							endorsedPolicyStatus = true;
						}
						*/

						if (endorsedPolicyStatus) {
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
							String get64vbStatus = PremiaService.getInstance()
									.get64VBStatus(
											intimation.getPolicy()
													.getPolicyNumber(), intimation.getIntimationId());
							/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
								get64vbStatus = "R";
							}*/
							if (get64vbStatus != null
									&& (SHAConstants.DISHONOURED
											.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
											.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
											.equalsIgnoreCase(get64vbStatus))) {
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");//Changed N to Y due to GLX2021067 

							}
						}
						
					}
				}
			} else {
				if (null != intimation.getPolicy()
						&& !(SHAConstants.NEW_POLICY)
								.equalsIgnoreCase(intimation.getPolicy()
										.getPolicyStatus())) {
					Boolean endorsedPolicyStatus = PremiaService.getInstance()
							.getEndorsedPolicyStatus(
									intimation.getPolicy().getPolicyNumber());
					/**
					 * As per satish sir, Endorsement status is not consider for GMC
					 */
					/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
						endorsedPolicyStatus = true;
					}*/
					if (endorsedPolicyStatus)
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
					else
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
				} else {
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
				}
			}
			if(null != intimation.getLobId())
				{
					if(!(ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey())) 
							&& (!((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey())
									&& (intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)))))
					{
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"N");
					}
	            }
			
			
			if(intimation.getPolicy().getProduct() != null && !(ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(intimation.getPolicy().getProduct().getKey())) && ReferenceTable.getSuperSurplusKeys().containsKey(intimation.getPolicy().getProduct().getKey())
					&& intimation.getPolicy().getPolicyPlan() != null && intimation.getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
				mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"Y");
			}
			
			Claim claimObject = null;
			
			Policy policy = intimation.getPolicy();
			if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(policy.getProduct().getKey())){
				Date policyFromDate = policy.getPolicyFromDate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(policyFromDate);
				calendar.add(Calendar.YEAR, 1);
			    Date afterOneYear = calendar.getTime();
			    
			    log.info("*********After One year For unique policy ********  " +afterOneYear);
			    
			    Date policyToDate = policy.getPolicyToDate();
			    
			    log.info("*********Policy To Date For unique policy ********  " +policyToDate);
			    
			    if(intimation.getAdmissionDate() != null && intimation.getAdmissionDate().after(afterOneYear) && intimation.getAdmissionDate().before(policyToDate)){
			    	Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(policy.getPolicyNumber());
			    	if(uniqueInstallmentAmount > 0){
			    		mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
			    	}
			    }
			}
			 //added for CR2019184 to allow manual registration process if installment premium is pending 
		    if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getPolicyInstalmentFlag() != null &&
		    		intimation.getPolicy().getProduct().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
		    	Integer policyInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(policy.getPolicyNumber());
		    	if(policyInstallmentAmount != null && policyInstallmentAmount > 0){
		    		mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");//Changed N to Y due to GLX2021067 
		    	}
		    }
		    
			if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
				log.info("&&&&&&&&&&&&&THIS INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
						+ intimation.getIntimationId());
				claimObject = doAutoRegistrationProcessRevised(intimation);
			}
			else {
				log.info("%%%%%***MANUAL**** *THIS INTIMATION IS  GOING TO MANUAL REGISTRATION  -------->"
						+ ((balsceSI != null && balsceSI > 0) ? "BALANCE IS IS "
								+ String.valueOf(balsceSI) + ""
								: "64VB or ENDORSED ")
						+ "   INTIMATION NUMBER------>"
						+ intimation.getIntimationId());
			}
			
			Hospitals hospital = getHospitalByHospNo(premiaIntimation
					.getGiHospCode());
			
/*
=======
				else
				{
					if(null != intimation.getPolicy() && !(SHAConstants.NEW_POLICY).equalsIgnoreCase(intimation.getPolicy().getPolicyStatus())) 
					{
						Boolean endorsedPolicyStatus = PremiaService.getInstance().getEndorsedPolicyStatus(intimation.getPolicy().getPolicyNumber());
						if(endorsedPolicyStatus) 
							a_message.setIsBalanceSIAvailable(true);
						else
							a_message.setIsBalanceSIAvailable(false);
					}  else {
						a_message.setIsBalanceSIAvailable(true);
					}
				}
			
				/**
				 * Other than PA, rest of all claims had to undergo validation
				 * before determining it as PA or health claims.
				 * */
			
			/**
			 * need to implement lob type for PA 
			 */
//				if(null != intimation.getLobId())
//				{
//					if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
//					{
//						a_message.setReason(intimation.getIncidenceFlag()); // will be A or D. A-accident and D-Death.
//						a_message.setIsBalanceSIAvailable(false);
//						productInfoType.setLob(SHAConstants.PA_LOB);
//						productInfoType.setLobType(SHAConstants.PA_LOB_TYPE);
//						
//						if((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(intimation.getIncidenceFlag()) && null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
//							strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
//						}
//						else{ //if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
//							strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
//						}
//						claimType.setClaimType(strClaimType);	
//					}
//					if((ReferenceTable.HEALTH_LOB_KEY).equals(intimation.getLobId().getKey()))
//					{
//						productInfoType.setLob(SHAConstants.HEALTH_LOB);
//						productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
//					}
//					if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey()))
//							{
//								/**
//								 * The below code is added as per sathish sir suggestion.
//								 * If in case of package product, the lob will set 
//								 * as health for the flow to proceed to health screens.
//								 * */
//						
//						
//						        if(intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
//						        	productInfoType.setLob(SHAConstants.HEALTH_LOB);
//									productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
//									/**
//									 * For package health product, the flow should go to
//									 * auto registration. Since this is a package lob 
//									 * the above if case where we check not equal to health lob,
//									 * we set isBalanceAvailable as false. That needs to set to true
//									 * for package product. Hence below code is added.
//									 * 
//									 * */
//									if(null != balsceSI && balsceSI > 0 && null != intimation.getPolicy() && (SHAConstants.NEW_POLICY).equalsIgnoreCase(intimation.getPolicy().getPolicyStatus())
//											&& null != strPremiaFlag && ("true").equalsIgnoreCase(strPremiaFlag)) {
//										a_message.setIsBalanceSIAvailable(true);
//										String get64vbStatus = PremiaService.getInstance().get64VBStatus(intimation.getPolicy().getPolicyNumber());
//										if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
//											a_message.setIsBalanceSIAvailable(false);
//										}
//									}
//						        }else{
//						        	productInfoType.setLob(SHAConstants.PACKAGE_LOB_TYPE);
//									productInfoType.setLobType(SHAConstants.PACKAGE_LOB_FLAG);
//						        }
//							}
//				}
//			
//>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
//			if(intimation.getHospitalType() != null) {
//			 
//			
//			Hospitals hospital = null;
//			
//			
//			if (null != intimation.getHospitalType().getKey()) {
//				if (intimation.getHospitalType().getKey()
//						.equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)) {
//					hospital = getHospitalByHospNo(premiaIntimation
//							.getGiHospCode());
//					if (hospital != null) {
//						if (hospital.getNetworkHospitalTypeId() != null) {
//							MastersValue networkHospitalType = getMaster(hospital
//									.getNetworkHospitalTypeId());
//							if (networkHospitalType != null) {
////								hospitalInfoType
////										.setNetworkHospitalType(networkHospitalType
////												.getValue() != null ? networkHospitalType
////												.getValue() : "");
//
//							}
//						}
//					}
//				}
//			}
			
			/*if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey()))
				{
				if(intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
					
					if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
						log.info("&&&&&&&&&&&&&THIS INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
								+ intimation.getIntimationId());
						claimObject = doAutoRegistrationProcessRevised(intimation);
					}
				 }
				
			  }*/
				
			
			if(hospital == null){
				hospital = getHospitalByHospNo(premiaIntimation
						.getGiHospCode());
			}

			// }


			String userId = BPMClientContext.BPMN_TASK_USER;
			String password = BPMClientContext.BPMN_PASSWORD;
	

			
			if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI)) && claimObject != null){
				
				/**
				 *    auto registration for cashless claim and reimbursement claim
				 */
				
				if(claimObject.getClaimId() == null && claimObject.getKey() != null){
					claimObject = getClaimByClaimKey(claimObject.getKey());
				}
				//Added as per Satish Sir Suggestion while do auto registration of claim
				if(claimObject != null && hospital != null){
					PremiaService.getInstance().getPolicyLock(claimObject, hospital.getHospitalCode());
				}
				
//				Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObject, hospital);
				
				Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];
				
				if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType)){
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
				}else{
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME;
				}
				
				if(hospital.getFspFlag() != null && hospital.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					 MastersValue master = getMaster(ReferenceTable.FSP);
					 if(master != null){
						 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
					 }
		    	}
				
				/*if(null != intimation.getLobId())
				{
						if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
						{
							inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
							inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
						}
						
				}*/
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
//				dbCalculationService.initiateTaskProcedure(parameter);
				dbCalculationService.revisedInitiateTaskProcedure(parameter);
				
			}else{
				
				/**
				 *    Manual registration for cashless claim and reimbursement claim
				 */
				
//				Object[] arrayListForDBCall = SHAUtils.getArrayListForManualRegDBCall(intimation, hospital);
				
				Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForManualRegistrationDBCall(intimation, hospital);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];
				
				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
				
				if(null != intimation.getLobId())
				{
						if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
						{
							inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
							inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
						}
				}
				
				if(hospital.getFspFlag() != null && hospital.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					 MastersValue master = getMaster(ReferenceTable.FSP);
					 if(master != null){
						 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
					 }
		    	}
				
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
//				dbCalculationService.initiateTaskProcedure(parameter);
				dbCalculationService.revisedInitiateTaskProcedure(parameter);
				
			}
			
			if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType) && null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
				
				/**
				 * Initiate for Reimbursement claim for auto Registration Only.
				 */
//
//				IntMsg initiateInitmationTask = BPMClientContext
//						.getIntimationIntiationTask(
//								BPMClientContext.BPMN_TASK_USER,
//								BPMClientContext.BPMN_PASSWORD);
//	
//				initiateInitmationTask.initiate(BPMClientContext.BPMN_TASK_USER,
//						payloadBO);
			}

			if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))
					&& (SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
							.equalsIgnoreCase(strClaimType)
					&& claimObject != null) {
                log.info("****************&&&&&&& REMAINDER_INITIATED FOR AUTO REGISTRATION REIMBURSEMENT" + intimation.getIntimationId());
				
				Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];
				
				inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.BILLS_NOT_RECEIVED;
				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS;

				callReminderTaskForDB(inputArray);
				
//				autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
				
				log.info("*******&&&&&&&&& FVR INITIATED FOR REIMBURSEMENT CLAIM **-->"
						+ intimation.getIntimationId());
			}
			
			if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
				autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
			}
			
			if(claimObject != null){

			if (claimObject != null) {
				Claim claim = getClaimByIntimationNo(intimationObj
						.getIntimationId());
				if (claim != null) {
					
					updateProvisionAmountToPremia(claim);
					}
				}

			}

			utx.commit();
			updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			utx.rollback();
//			updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_FAILURE_STATUS);
			log.error("***************************ERROR OCCURED WHILE CLAIM GENERATION **************************" + (intimationObj != null ? intimationObj.getIntimationId() : "NO INTIMATION NUMBER"));
			return false;
		}
				
	}
	
	
	public Boolean autoRegisterFVR(Intimation objIntimation, String userName)
	{
		try
		{
			FieldVisitRequest fvrRequest = new FieldVisitRequest();
			
			IntimationRule intimationRule = new IntimationRule();

			Claim claim = findClaimByKey(objIntimation);
			
			
			Stage objStage = new Stage();
			objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
			
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
			
			if(claim != null && claim.getIntimation() != null){
				Intimation intimation = claim.getIntimation();
				Long hospital = intimation.getHospital();
				
				Hospitals hospitalById = getHospitalById(hospital);
				
				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
				if(tmpCPUCode != null){
					fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}
				
				
			}
			
			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
			value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
			fvrRequest.setAllocationTo(value);
			fvrRequest.setIntimation(objIntimation);
			fvrRequest.setClaim(claim);
			fvrRequest.setCreatedBy(userName);
			fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM_NEW);
			fvrRequest.setPolicy(objIntimation.getPolicy());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(1L);
			fvrRequest.setOfficeCode(objIntimation.getPolicy().getHomeOfficeCode());	
			fvrRequest.setTransactionFlag("R");
			fvrRequest.setStatus(fvrStatus);
			fvrRequest.setStage(objStage);
			entityManager.persist(fvrRequest);
			entityManager.flush();
			// this.fvrRequest = fvrRequest;
//			callReimbursmentFVRTask(fvrRequest, objIntimation, claim, userName);
			initiateDBForFVR(fvrRequest, claim);

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void initiateDBForFVR(FieldVisitRequest fieldVisitRequestDO,
			Claim claim) {
		if (fieldVisitRequestDO != null && claim != null
				&& fieldVisitRequestDO.getKey() != null) {

			Hospitals hospitals = getHospitalById(claim.getIntimation().getHospital());
			
			

			
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitals);
			
			Object[] inputArray = (Object[])arrayListForDBCall[0];
			
			Object[] parameter = new Object[1];
			parameter[0] = inputArray;			
			
			inputArray[SHAConstants.INDEX_FVR_KEY] = fieldVisitRequestDO.getKey();
			inputArray[SHAConstants.INDEX_FVR_NUMBER] = fieldVisitRequestDO.getFvrId();
			inputArray[SHAConstants.INDEX_FVR_CPU_CODE] = String.valueOf(fieldVisitRequestDO.getFvrCpuId());
			
			inputArray[SHAConstants.INDEX_OUT_COME] =  SHAConstants.OUTCOME_REG_INITIATE_FVR; 
		
			Intimation intimation = claim.getIntimation();

            Insured insured = intimation.getInsured();
			
            if(claim != null && claim.getPriorityEvent() != null && !claim.getPriorityEvent().trim().isEmpty()){
    			inputArray[SHAConstants.INDEX_PRIORITY] = claim.getPriorityEvent() != null ? claim.getPriorityEvent() : SHAConstants.NORMAL ;
        	}
            else if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
				
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
			}
			else
			{
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			}
		
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.TYPE_FRESH;
			
			//classificationType.setSource(SHAConstants.NORMAL);
			inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.NORMAL;
//			inputArray[SHAConstants.INDEX_ESCALATE_ROLE_ID] = "";
//			inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = bean.getUsername();
			
		  
			DBCalculationService dbCalculationService = new DBCalculationService();
			String successMsg = dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
		}
		 
			
		} 
		


	private Claim findClaimByKey(Intimation objIntimation) {
		Query findByIntimationKey = entityManager
				.createNamedQuery("Claim.findByIntimationKey");
		findByIntimationKey = findByIntimationKey.setParameter(
				"intimationKey", objIntimation.getKey());
//			Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
		Claim claim = (Claim) findByIntimationKey.getSingleResult();
		return claim;
	}
	
/*	public void callReimbursmentFVRTask(FieldVisitRequest fiedvisitRequest,Intimation objIntimation,Claim claim, String userName){
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
		
		policyBo.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
		reimbursementPayload.setPolicy(policyBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
		intimationBo.setIntimationNumber(objIntimation.getIntimationId());
		intimationBo.setKey(objIntimation.getKey());
		intimationBo.setStatus("TOFVR");
		reimbursementPayload.setIntimation(intimationBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
		if(null != objIntimation.getCpuCode())
		{
			claimReq.setCpuCode(String.valueOf(objIntimation.getCpuCode().getCpuCode()));
		}	
		claimReq.setKey(claim.getKey());
		claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED); 
		
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
		claimBo.setClaimId(claim.getClaimId());
		claimBo.setKey(claim.getKey());
		claimBo.setClaimType(claim.getClaimType() != null ? (claim.getClaimType().getValue() != null ? claim.getClaimType().getValue().toUpperCase(): "") : "");		
		reimbursementPayload.setClaim(claimBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
		reimbursementPayload.setQuery(queryType);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
		
		callsificationBo.setPriority("");
		callsificationBo.setSource("");
		callsificationBo.setType("");
		reimbursementPayload.setClassification(callsificationBo);
		
		ProcessActorInfoType processActor = new ProcessActorInfoType();
		processActor.setEscalatedByRole("");
		processActor.setEscalatedByUser(BPMClientContext.BPMN_TASK_USER);
		reimbursementPayload.setProcessActorInfo(processActor);
		
		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");
		
		if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct() != null && objIntimation.getPolicy().getProduct().getKey() != null){
			productInfo.setProductId(objIntimation.getPolicy().getProduct().getKey().toString());
			productInfo.setProductName(objIntimation.getPolicy().getProduct().getValue());
			reimbursementPayload.setProductInfo(productInfo);
		}
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType hospitalInfoType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType();
		
		Long hospital = objIntimation.getHospital();
		
		Hospitals hospitalById = getHospitalById(hospital);
		hospitalInfoType.setKey(hospital);
		hospitalInfoType.setHospitalType(hospitalById.getHospitalType().getValue());
		hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType());
		reimbursementPayload.setHospitalInfo(hospitalInfoType);
		
		
		if(objIntimation.getAdmissionDate() != null){
			String intimDate = SHAUtils.formatIntimationDateValue(objIntimation.getAdmissionDate());
			RRCType rrcType = new RRCType();
			rrcType.setFromDate(intimDate);
			reimbursementPayload.setRrc(rrcType);
		}
		
		FieldVisitType fieldVisitType = new FieldVisitType();
		fieldVisitType.setKey(fiedvisitRequest.getKey());
		
		Long cpuId = hospitalById.getCpuId();
		if(cpuId != null){
		TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
		if(tmpCPUCode != null){
			fieldVisitType.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
			claimReq.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
			}
		}
		
		reimbursementPayload.setClaimRequest(claimReq);	
		
		reimbursementPayload.setFieldVisit(fieldVisitType);
		
        FVR reimbursementFVR = BPMClientContext.getReimbursementFVR(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
        try {
			reimbursementFVR.initiate(userName, reimbursementPayload);
		} catch (Exception e) {
			e.printStackTrace();
		}
<<<<<<< HEAD

		// need to be implemented
	}*/

	private TmpCPUCode getTmpCPUCode(Long cpuId) {
		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			List<TmpCPUCode> tmpCPUCode = (List<TmpCPUCode>) findCpuCode
					.getResultList();

			if (tmpCPUCode != null && !tmpCPUCode.isEmpty()) {
				return tmpCPUCode.get(0);
			}

			return null;
		} catch (Exception e) {

		}
		return null;
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
	
	public Claim doAutoRegistrationProcessRevised(Intimation objIntimation) {
		//Hospitals objHosp = starFaxservice.getHospitalObject(objIntimation.getHospital());
		String strClaimTypeRequest = "";
		if(null != objIntimation.getClaimType())
		{
			strClaimTypeRequest = objIntimation.getClaimType().getValue();
		}
		Claim objClaim = populateClaimObject(objIntimation, strClaimTypeRequest);
		    log.info("before save claim --->" + objIntimation.getIntimationId());
		try {
			entityManager.persist(objClaim);
			entityManager.flush();
			entityManager.refresh(objClaim);
		} catch (Exception e) {

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
	}
	
	/**
	 * Method to populate claim object from intimation object. 
	 * This claim object will be later inserted into IMS_CLS_CLAIM
	 * table for futher processing
	 * 
	 * */
	
	public Claim populateClaimObject(Intimation objIntimation,String strClaimTypeRequest) {

		Claim claimObj = new Claim();
		Status objStatus = new Status();
		Stage objStage = new Stage();
		//Intializing hospital object for given intimation number.
		MastersValue masValue = new MastersValue();
		MastersValue masForClaim = new MastersValue();
		//objStatus.setKey(11l);
		objStatus.setKey(ReferenceTable.INTIMATION_REGISTERED_STATUS);
		objStage.setKey(9l);
		DBCalculationService dbCalculationService = new DBCalculationService();
		/**
		 * If the hospital type is network ,
		 * then the claim type will cashless. Else, 
		 * it would be Re-imbursement.
		 * */
		//if(("NETWORK").equalsIgnoreCase(objHosp.getHospitalTypeName()))
		if(("CASHLESS").equalsIgnoreCase(strClaimTypeRequest))
		{
			masForClaim.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		}
		/*else
		{
			masForClaim.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}*/
		
		else if(("REIMBURSEMENT").equalsIgnoreCase(strClaimTypeRequest))
		{
			masForClaim.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}
		
		masValue.setKey(ReferenceTable.INR_CURRENCY_CODE);
		claimObj.setIntimation(objIntimation);
		//Setting status as Registered for time being. This needs to be changed later.
		claimObj.setStatus(objStatus);
		claimObj.setStage(objStage);
		claimObj.setClaimedamountCurrencyId(ReferenceTable.INR_CURRENCY_CODE);
		claimObj.setOfficeCode(objIntimation.getOfficeCode());
		//claimObj.setCreatedBy(objIntimation.getCreatedBy());
		claimObj.setCreatedBy(SHAConstants.BATCH_USER_ID);
		claimObj.setCreatedDate(objIntimation.getCreatedDate());
//		claimObj.setStatusDate(objIntimation.getStatusDate());
		claimObj.setConversionLetter(0l);
		claimObj.setRejectionLetterflag(null);
		claimObj.setConversionFlag(0l);
		claimObj.setIsVipCustomer(0l);
//		claimObj.setVersion(objIntimation.getVersion());
		claimObj.setCurrencyId(masValue);
		claimObj.setClaimType(masForClaim);
		if(objIntimation.getAdmissionDate() != null){
			claimObj.setDataOfAdmission(objIntimation.getAdmissionDate());
		}
		claimObj.setConversionReason(null);
		claimObj.setClaimLink(null);
		claimObj.setNormalClaimFlag("O");
		if(objIntimation.getLobId() != null && objIntimation.getLobId().getKey() != null){
			claimObj.setLobId(objIntimation.getLobId().getKey());
		}
		
		if(objIntimation.getCpuCode() != null){
			claimObj.setOriginalCpuCode(objIntimation.getCpuCode().getCpuCode());
		}

		Policy policyByKey = getPolicyByKey(objIntimation.getPolicy().getKey());
		
		MASClaimAdvancedProvision claimAdvProvision = getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));
		
		/**
		 * Function for calculating Balance SI will be changed as per the new table
		 * structure. Once that is done, the same needs to be changed in this file.
		 * */

//		Double amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
//				 , objIntimation.getCpuCode() != null ? objIntimation.getCpuCode().getProvisionAmount() : 0d,objIntimation.getKey());
		
		Double amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
				 , claimAdvProvision != null ? claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d : 0d,objIntimation.getKey(),claimObj);
		
//		claimObj.setClaimedAmount(amt);
		/**
		 * added as per Sathish Sir, claimed amount is set to be zero
		 */
//		claimObj.setClaimedAmount(0d);
//		claimObj.setProvisionAmount(0d);
		
		
		claimObj.setClaimedAmount(amt);
		claimObj.setProvisionAmount(amt);

		//claimObj.setClaimedHomeAmount(objIntimation.getCpuCode().getProvisionAmount());
		//claimObj.setProvisionHomeAmount(objIntimation.getCpuCode().getProvisionAmount());
		claimObj.setRegistrationRemarks("Auto Registered");
		claimObj.setCurrentProvisionAmount(amt);
//		claimObj.setActiveStatusDate(objIntimation.getActiveStatusDate());
		
		//code added by noufel for combining all the priority and pass as a single String Variable
		List<String> combinedPriorityList = new ArrayList<String>();
		StringBuilder combinedPriorityValues = new StringBuilder();
		if(objIntimation.getAdmissionReason() != null && !objIntimation.getAdmissionReason().isEmpty()){
			combinedPriorityList.add(objIntimation.getAdmissionReason());
		}
		String memberType = dbCalculationService.getCMDMemberType(policyByKey.getKey());
		if(null != memberType && !memberType.isEmpty()){
			combinedPriorityList.add(memberType);
			claimObj.setClaimClubMember(memberType);
		}
		Integer activeStatus = dbCalculationService.getGmcATOSActiveFlag(policyByKey.getPolicyNumber());
		if(activeStatus != null && activeStatus == 1){
			combinedPriorityList.add(SHAConstants.ATOS);
			claimObj.setClaimPriorityLabel("Y");
		}else {
			claimObj.setClaimPriorityLabel("N");
		}
		if(objIntimation.getHospital() != null){
		Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(objIntimation.getHospital());
		if(null != hospitalDetailsByKey && hospitalDetailsByKey.getFspFlag() != null && hospitalDetailsByKey.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			combinedPriorityList.add(SHAConstants.VSP_HOSPITALS);
		}
		}
		
		if(combinedPriorityList !=null && !combinedPriorityList.isEmpty()){
			combinedPriorityList.forEach(prio -> combinedPriorityValues.append(prio+" "));
	    	 System.out.println(combinedPriorityValues.toString());
		}
		if(combinedPriorityValues != null && !combinedPriorityValues.toString().isEmpty()){
			System.out.println("Combined priority Values :-" + combinedPriorityValues.toString());
			Map<String, Object> getPriorityEvent =	dbCalculationService.getPriorityFlag(0l,combinedPriorityValues.toString());
         if(getPriorityEvent != null){
        	 if(getPriorityEvent.containsKey("priorityWeightage")){
        		 claimObj.setPriorityWeightage((Long) getPriorityEvent.get("priorityWeightage")); 
        	 }
        	 if(getPriorityEvent.containsKey("priorityEvent") && ((String) getPriorityEvent.get("priorityEvent")) != null && !((String) getPriorityEvent.get("priorityEvent")).isEmpty()){
        		 claimObj.setPriorityEvent((String) getPriorityEvent.get("priorityEvent")); 
        	 }
         }
		}
		return claimObj;
	}
	
	public void updateProvisionAmountToPremia(Claim claim){
		
		if(claim != null && claim.getIntimation() != null && claim.getIntimation().getIntimationId() != null){
		
		log.info("@@@@@@@@@ PROVISION UPDATED STARTING TIME FOR INTIMATION PULL @@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
		}
		
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			try {
				Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
				updateProvisionAmountToPremia(provisionAmtInput);
			} catch(Exception e) {
				
			}
		}
		
		if(claim != null && claim.getIntimation() != null && claim.getIntimation().getIntimationId() != null){
			
			log.info("@@@@@@@@@ PROVISION UPDATED END TIME FOR INTIMATION PULL @@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
			
		}
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
	
	public String updateProvisionAmountToPremia(String input) {
		try {
			
			//Bancs Changes Start
			JSONObject jsonObject = new JSONObject(input);
			String policyNo = jsonObject.getString("PolicyNo");
			String intimationNo = jsonObject.getString("IntimationNo");
			String currentProvisionAmount = jsonObject.getString("ProvisionAmount");
			Policy policyObj = null;
			Builder builder = null;
			String output = null;
			
			if(policyNo != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNo);
				//policyObj = policyService.getByPolicyNumber(policyNo);
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						output = BancsSevice.getInstance().provisionAmountBancsUpdate(policyNo,intimationNo, currentProvisionAmount, input);
					}else{
							builder = PremiaService.getInstance().getBuilderForProvison();
							output = builder.post(new GenericType<String>() {}, input);
					}
				}
			}
			
			//Bancs Changes End
		//	Builder builder = PremiaService.getInstance().getBuilderForProvison();
			//String output = builder.post(new GenericType<String>() {}, input);
			log.info("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END -->" + output);
			return output;
		} catch(Exception e) {
			log.error("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END --> Exception is --- > "  + e.getMessage());
			e.printStackTrace();
		}
		
		return "";

	}
	
	public Double calculateAmtBasedOnBalanceSI(Long policyKey , Long insuredId, Long insuredKey, Double provisionAmout,Long intimationKey,Claim claimObj)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		//Double amount = 0d;
//		TODO under Discussion to get balance SumInsured
		Double insuredSumInsured = 0d;
		if(null != claimObj.getIntimation() && null != claimObj.getIntimation().getPolicy() && 
				null != claimObj.getIntimation().getPolicy().getProduct() && 
				null !=claimObj.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(claimObj.getIntimation().getPolicy().getProduct().getKey()))){	
		 insuredSumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,"H");
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(insuredId.toString(), policyKey);
		}
		//amount = Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey ,insuredSumInsured) , provisionAmout);
		System.out.println("--policy key---"+policyKey+"----insuredSumInsured----"+insuredSumInsured+"----insured key----"+insuredKey);
		if(null != claimObj.getIntimation() && null != claimObj.getIntimation().getPolicy() && 
				null != claimObj.getIntimation().getPolicy().getProduct() && 
				null != claimObj.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(claimObj.getIntimation().getPolicy().getProduct().getKey()))){	
		return Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey, 0l, 
				insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
		
		}
		else
		{
			return Math.min(dbCalculationService.getGPABalanceSI(policyKey, insuredKey, 0l, 
					insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
		}
		
		//return amount;
	}
	
	private Coordinator getCoordinator() {
		Query query = entityManager.createNamedQuery("Coordinator.findByKey");
		query = query.setParameter("primaryKey",40000261l);
		List<Coordinator>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	
	public List<InsuredPedDetails> getInsuredKeyListByInsuredkey(Long insuredKey){
		 Query query = entityManager.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		 query = query.setParameter("insuredKey", insuredKey);		        
		 @SuppressWarnings("unchecked")
		List<InsuredPedDetails> insuredList  = query.getResultList();	
		 if(insuredList != null && !insuredList.isEmpty()){
			 return insuredList;
		 }
		return null;
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public void saveInsuredPEDDetailsAlone(String policyNumber) throws IllegalStateException, SecurityException, SystemException {
		PremPolicyDetails premPolicyDetails = fetchPolicyDetailsFromPremia(policyNumber);
		try {
			utx.begin();
			if(premPolicyDetails != null) {
				
				PremiaToPolicyMapper premiaPolicyMapper =  PremiaToPolicyMapper.getInstance();
				
				 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
					 String riskSysId = premPolicyDetails.getInsuredDetails().get(index).getRiskSysId();
					 if(!premPolicyDetails.getInsuredDetails().get(index).getPedDetails().isEmpty()) {
						 List<InsuredPedDetails> insuredKeyListByInsuredkey = getInsuredKeyListByInsuredkey(new Long(riskSysId));
						 Insured insured = getInsuredByPolicyAndInsuredName(policyNumber, riskSysId,"H");
						 insured.setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
						 if(insured != null && insured.getInsuredPedList() != null && !insured.getInsuredPedList().isEmpty()) {
								for(InsuredPedDetails insuredPed : insured.getInsuredPedList()){
									if(!SHAUtils.isPEDAvailable(insuredKeyListByInsuredkey, insuredPed.getPedCode())) {
										insuredPed.setInsuredKey(insured.getInsuredId());
										String pedDescription = insuredPed.getPedDescription();
										if(pedDescription != null && pedDescription.length() >300) {
											pedDescription = pedDescription.substring(0, 299);
											insuredPed.setPedDescription(pedDescription);
										}
										//added for CR ped effective date save in PED table GLX0132
										if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//											Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
//											if(fromDate != null){
											insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));
//											}
										}
										if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//											Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
//											if(fromDate != null){
											insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));
//											}
										}
										if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
											insuredPed.setPedType(insuredPed.getPedType());
										}
										if(insuredPed.getKey() == null){
											entityManager.persist(insuredPed);
											entityManager.flush();
										}
									}
							
								}
									
							}
					 }
				
				 }
			}
			utx.commit();
		} catch(Exception e) {
			utx.rollback();
		}
		
		 
		 
			
	}
	
	@SuppressWarnings("static-access")
	public void savePolicyDetails(String policyNo) throws IllegalStateException, SecurityException, SystemException {
		
		String updateStringValue = null;
		
		PremPolicyDetails premPolicyDetails = fetchPolicyDetailsFromPremia(policyNo);
		try {
			
			if(premPolicyDetails !=null){
				populatePolicyData(premPolicyDetails, false,false);
				
			}
		}
		catch(Exception e) {
			
		}
		 
	}
	
	
	@SuppressWarnings("static-access")
	public void updatePolicyDetails(String policyNo)
			throws IllegalStateException, SecurityException, SystemException {

		String updateStringValue = null;

		PremPolicyDetails premPolicyDetails = fetchPolicyDetailsFromPremia(policyNo);
		try {

			if (premPolicyDetails != null) {
//				populatePolicyData(premPolicyDetails, false);
				
//				updatePolicyDetails(premPolicyDetails);
				
				updatePreviousPolicyDetails(premPolicyDetails);
				
				

			}
		} catch (Exception e) {

		}

	}
	
	private void updatePolicyDetails(PremPolicyDetails premPolicyDetails){
		
		try {
			utx.begin();
			
			Policy policyByPolicyNubember = getPolicyByPolicyNubember(premPolicyDetails.getPolicyNo());
			
			if(policyByPolicyNubember.getPolicyFromDate() == null){
			
				policyByPolicyNubember.setPolicyFromDate(new Date(premPolicyDetails
				.getPolicyStartDate()));
				
				policyByPolicyNubember.setPolicyToDate(new Date(premPolicyDetails
				.getPolicyEndDate()));
				
				entityManager.merge(policyByPolicyNubember);
				entityManager.flush();
				
				utx.commit();
			}

			
		}catch(Exception e){
			
		}
		
	}
	
private void updatePreviousPolicyDetails(PremPolicyDetails premPolicyDetails){
		
		try {
			
			utx.begin();
            List<PremPreviousPolicyDetails> previousPolicyDetails = premPolicyDetails.getPreviousPolicyDetails();
            for (PremPreviousPolicyDetails premPreviousPolicyDetails : previousPolicyDetails) {
				PreviousPolicy previousPolicy = getPreviousPolicy(premPreviousPolicyDetails.getPolicyNo(), premPolicyDetails.getPolicyNo());
				if(previousPolicy != null){
					if(previousPolicy.getPolicyFrmDate() == null){
						previousPolicy.setPolicyFrmDate(new Date(premPreviousPolicyDetails.getPolicyFromDate()));
						previousPolicy.setPolicyToDate(new Date(premPreviousPolicyDetails.getPolicyEndDate()));
						entityManager.merge(previousPolicy);
						entityManager.flush();
						utx.commit();
						
						log.info("Policy from date and To date is updated ---->"+previousPolicy.getPolicyNumber());
					}
				}
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

private PreviousPolicy getPreviousPolicy(String policyNumber,
		String currentPolicyNo) {
	Query query = entityManager
			.createNamedQuery("PreviousPolicy.findByPreviousPolicyAndCurrentPolicyNo");
	query = query.setParameter("policyNumber", policyNumber);
	query = query.setParameter("currentPolicyNumber", currentPolicyNo);
	List<PreviousPolicy> prevPolicyList = query.getResultList();
	if (null != prevPolicyList && !prevPolicyList.isEmpty()) {
		return prevPolicyList.get(0);
	}
	return null;
}
	
	


	public PremiaIntimationTable getPremiaIntimationByIntimationNo(String intimationNumber){
		
		PremiaIntimationTable intimation =null;
		
		Query query = entityManager.createNamedQuery("PremiaIntimationTable.findByIntimationNumber");
		query = query.setParameter("intimationNumber", intimationNumber);
		intimation = (PremiaIntimationTable) query.getSingleResult();
		if(intimation != null)	{
			
			return intimation;
			
		}
		return null;
	}
	


	public void doPremiaPull(PremiaIntimationTable processPremiaIntimationData) {
		if (processPremiaIntimationData != null) {
			try {

				if (!isIntimationExist(processPremiaIntimationData
						.getGiIntimationNo())) {
					if ((null != processPremiaIntimationData.getGiHospCode() || !("")
							.equalsIgnoreCase(processPremiaIntimationData
									.getGiHospCode()))
							&& isPolicyAllowed(processPremiaIntimationData
									.getGiPolNo())) {
						String policyServiceType = "OTHERS";
						Boolean isIntegratedPolicy = true;
						if(processPremiaIntimationData.getProductCode() != null){
							Product productByProductCode = getProductByProductCode(processPremiaIntimationData.getProductCode());
							if(productByProductCode != null && productByProductCode.getProductService() != null){
								policyServiceType = productByProductCode.getProductService();
							}
						}
						PremPolicyDetails policyDetails = null;
						if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
							log.info("***** GMC POLICY WEB SERVICE CALLING ********************-----> ");
							policyDetails = fetchGmcPolicyDetailsFromPremia(processPremiaIntimationData
									.getGiPolNo(), processPremiaIntimationData.getGiInsuredName());
							isIntegratedPolicy = populateGMCandGPAPolicy(policyDetails, processPremiaIntimationData.getGiInsuredName(),false);
							
						}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
							log.info("***** GPA POLICY WEB SERVICE CALLING ********************-----> ");
							policyDetails = fetchGpaPolicyDetailsFromPremia(processPremiaIntimationData
									.getGiPolNo(), processPremiaIntimationData.getGiInsuredName());
							policyDetails.setRiskSysId(processPremiaIntimationData.getGiInsuredName());
							isIntegratedPolicy = populateGMCandGPAPolicy(policyDetails, processPremiaIntimationData.getGiInsuredName(),false);
						}else{
							policyDetails = fetchPolicyDetailsFromPremia(processPremiaIntimationData
									.getGiPolNo());
							isIntegratedPolicy = populatePolicyFromTmpPolicy(policyDetails,processPremiaIntimationData.getGiInsuredName(),false);
						}
						/*Boolean isIntegratedPolicy = populateGMCPolicy(processPremiaIntimationData
								.getGiPolNo(), processPremiaIntimationData.getGiInsuredName());*/
						if (isIntegratedPolicy) {
							Intimation intimation = insertIntimationDataRevised(processPremiaIntimationData,policyDetails.getLob());
							//Intimation intimation = insertIntimationDataRevised(processPremiaIntimationData,"H");
							log.info("***** SUCCESSFULLY INITMATION CREATED ********************-----> "
									+ intimation.getIntimationId());
							if (null != intimation
									&& null != intimation.getHospital()
									&& null != intimation.getCpuCode()) {
								processSavedIntimationRevised(intimation,
										processPremiaIntimationData);

							}
						} else {
							log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + processPremiaIntimationData.getGiIntimationNo() + "  HOSP CODE" + processPremiaIntimationData.getGiHospCode());
						}
					} else {
						log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + processPremiaIntimationData.getGiIntimationNo());
						updatePremiaIntimationTable(processPremiaIntimationData, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
					}
				}
					
				} catch(Exception e) {
					
				}
			
		}
	}
	
	public void uploadReminderLetterToDMS(SearchGenerateReminderTableDTO reminderLetterDto){
		try
		{
			utx.begin();
			ReportDto reportDto = new ReportDto();
			DocumentGenerator docGenarator = new DocumentGenerator();
			String fileUrl = null;
			
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			if(null != reminderLetterDto.getPresenterString() && !("").equalsIgnoreCase(reminderLetterDto.getPresenterString()))
				reportDto.setPresenterString(reminderLetterDto.getPresenterString());
			
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseClaimReminderLetter", reportDto);
			Claim objClaim = getClaimByClaimKey(reminderLetterDto.getClaimDto().getKey());
			if(null != fileUrl && !("").equalsIgnoreCase(fileUrl))
			{
				WeakHashMap dataMap = new WeakHashMap();
				dataMap.put("intimationNumber",reminderLetterDto.getClaimDto().getNewIntimationDto().getIntimationId());
				if(null != objClaim)
				{
					dataMap.put("claimNumber",objClaim.getClaimId());
					if(null != objClaim.getClaimType())
					{
						if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
							{
								Preauth preauth = SHAUtils.getPreauthClaimKey(entityManager , objClaim.getKey());
								if(null != preauth)
									dataMap.put("cashless", preauth.getPreauthId());
							}
						
						else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()) && dataMap != null)
						{
							Reimbursement reimbursement = getReimbursementByClaimKey(objClaim.getKey());
							if(null != reimbursement)
								dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
						}
					}
				}
				dataMap.put("filePath", fileUrl);
				dataMap.put("docType",SHAConstants.REMINDER_LETTER);
				dataMap.put("docSources", SHAConstants.GENERATE_REMINDER_LETTER);
				dataMap.put("createdBy", reminderLetterDto.getUsername());
				
				//For testing Purpose commented Need to uncomment below line
				SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				
				objClaim.setReminderCount(claimDto.getReminderCount().longValue());
				objClaim.setFirstReminderDate(claimDto.getFirstReminderDate());
				objClaim.setSecondReminderDate(claimDto.getSecondReminderDate());
				objClaim.setThirdReminderDate(claimDto.getThirdReminderDate());
				//For testing Purpose commented Need to uncomment below lines
				entityManager.merge(objClaim);
				entityManager.flush();
				utx.commit();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			try {
				utx.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}	





	public void processXRayRecord(String batchSize)
	{
		
		List<Pellucid> pellucideTableList = fetchRecordsFromPellucidTable(batchSize);
		try
		{
			utx.begin();
			if(null != pellucideTableList && !pellucideTableList.isEmpty())
			{
				for (Pellucid pellucid : pellucideTableList) {
					Boolean isIntimationExist = isIntimationExist(pellucid.getIntNumber());
					if(null != isIntimationExist && isIntimationExist)
					{
						log.info("*****  INITMATION EXIST IN GALAXY ********************-----> " + pellucid.getIntNumber());
						if(null != pellucid.getDocId())
						{
							HashMap documentMap = SHAFileUtils.uploadDocumentByUrlWebService(pellucid.getDmrUrl(),pellucid.getFileName(),0l);
							if(null != documentMap)
							{
								String tokenId = (String)documentMap.get("fileKey");
								if(null != tokenId && !tokenId.isEmpty())
								{
									log.info("*****  DMS DOCUMENT TOKEN ********************-----> " + tokenId);
									DocumentDetails documentDetails  = saveDocumentDetails(pellucid,Long.valueOf(tokenId));
									if(null != documentDetails)
									{
										log.info("***** DOCUMENT DETAILS SAVED SUCCESSFULLY. DOCUMENT KEY************"+documentDetails.getKey());
										pellucid.setDocGlxReadFlag(SHAConstants.X_RAY_YES_FLAG);
										updatePellucidTable(pellucid);
										log.info("***** PELLUCID DETAILS SAVED SUCCESSFULLY FOR INTIMATION NUMBER************"+pellucid.getIntNumber());
									}
								}
								else
								{
									log.info("***** FILE DOESN'T EXIST IN DMS FOR TOKEN************"+pellucid.getDocId());
									pellucid.setDocGlxReadFlag("FAIL");
									updatePellucidTable(pellucid);
								}
							}
						}
						
					}
					else 
					{
						log.info("*****  INITMATION DOESNT EXIST IN GALAXY ********************-----> " + pellucid.getIntNumber());
						pellucid.setDocGlxReadFlag("FAIL");
						updatePellucidTable(pellucid);
						// to update failure flag in pellucid table.
					}
				}
			}
			utx.commit();
	}
	catch(Exception e)
	{
		e.printStackTrace();
		try {
			utx.rollback();
		}  catch (IllegalStateException | SecurityException
				| SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
		}
	}
	
}


	public List<Pellucid> fetchRecordsFromPellucidTable(String batchSize)
	{
		List<Pellucid> pellucidIntimationTableList  = null;
		Query query = entityManager.createNamedQuery("Pellucid.findAll");
		query = query.setParameter("docGlxReadFlag", SHAConstants.X_RAY_RECORD_STATUS);
		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		pellucidIntimationTableList	= query.getResultList();
		if(null != pellucidIntimationTableList && !pellucidIntimationTableList.isEmpty())
		{
			for (Pellucid pellucid : pellucidIntimationTableList) {
//				entityManager.refresh(pellucid);
			}
		}
		log.info("********* COUNT FOR XRAY PULL ******------> " + (pellucidIntimationTableList != null ? String.valueOf(pellucidIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return pellucidIntimationTableList;
	}

	
	public DocumentDetails saveDocumentDetails(Pellucid pellucid,Long tokenId)
	{

		/*EntityTransaction tx = entityManager.getTransaction();
		tx.begin();*/
//		entityManager.getTransaction().begin();
		DocumentDetails docDetails = new DocumentDetails();
		log.info("********* SAVE DOCUMENT DETAILS METHOD ******------>INTIMATION NUMBER ***************------> " + pellucid.getIntNumber());
		docDetails.setIntimationNumber(pellucid.getIntNumber());
		if(null != pellucid.getIntNumber())
		{
			Claim claimObj = getClaimByIntimationNo(pellucid.getIntNumber());
			if(null != claimObj)
				docDetails.setClaimNumber(claimObj.getClaimId());
		}
		docDetails.setFileName(pellucid.getFileName());
		docDetails.setDocumentType(SHAConstants.X_RAY_REPORT_DOC_TYPE);
		docDetails.setDocumentUrl(pellucid.getDmrUrl());
		docDetails.setDocumentToken(tokenId);
		docDetails.setDocumentSource(SHAConstants.X_RAY_BATCH_DOCUMENT_SOURCE);
		docDetails.setSfFileName(pellucid.getFileName());
		docDetails.setDocSubmittedDate((new Timestamp(System
				.currentTimeMillis())));
		docDetails.setDocAcknowledgementDate((new Timestamp(System
				.currentTimeMillis())));
		docDetails.setCreatedDate((new Timestamp(System
				.currentTimeMillis())));
		docDetails.setCreatedBy(SHAConstants.X_RAY_BATCH_CREATED_BY);
		docDetails.setDeletedFlag("N");
		
		//tx.commit();
		entityManager.persist(docDetails);
		entityManager.flush();
		log.info("*********  DOCUMENT DETAILS SAVED SUCCESSFULLY ******------>DOCUMENT DETAILS KEY ***************------> " + docDetails.getKey());
		return docDetails;
	}
	
	public void updatePellucidTable(Pellucid pellucid)
	{
		if(null != pellucid)
		{
			entityManager.merge(pellucid);
			entityManager.flush();
			log.info("*********  PELLUCID TABLE UPDATED SUCCESSFULLY ******------>PELLUCID INT ID ***************------> " + pellucid.getIntId()+"***************************************"
					+ "INTIMATION NO----->"+pellucid.getIntNumber()+"***************************** PELLUCID GLX READ FLAG ************"
					+ "------->"+pellucid.getDocGlxReadFlag());
		}
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByIntimationNo(String intimationNo) {
		List<Claim> claimList = null;
			Query query = entityManager.createNamedQuery("Claim.findByIntimationNumber");
			query.setParameter("intimationNumber", intimationNo);
	
			claimList = (List<Claim>) query.getResultList();
	
			if (null != claimList && !claimList.isEmpty()) {
				entityManager.refresh(claimList.get(0));
				return claimList.get(0);
	
			}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Claim claim = null;
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claimList = (List<Claim>)query.getResultList();
		
		if(claimList != null && ! claimList.isEmpty()){
			for (Claim claimObj : claimList) {
				entityManager.refresh(claimObj);
				claim = claimObj;
			}			
		}
		return claim;
	}
	
	
	public Reimbursement getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
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
	



	private MASClaimAdvancedProvision getClaimAdvProvision(Long branchCode) {

		Query query = entityManager
				.createNamedQuery("MASClaimAdvancedProvision.findByBranchCode");
		query = query.setParameter("branchCode", branchCode);
		
		List<MASClaimAdvancedProvision> claimAdvProvsionList = (List<MASClaimAdvancedProvision>) query
				.getResultList();
		if(claimAdvProvsionList != null && ! claimAdvProvsionList.isEmpty()){
			return claimAdvProvsionList.get(0);
		}
		return null;
	}
	 
	
	private void setClaimTypeAndHospitalType(PremiaIntimationTable premiaIntimationTable,MastersValue hospitalType ,MastersValue claimType,Policy policy ,TmpCPUCode cpuCode,String lobType,Boolean isHospTypeEmpty,Boolean isHospTopUpIntmAvail)
	{
		if(SHAConstants.PA_LOB_TYPE.equals(lobType))
		{
			if(isHospTypeEmpty)
			{
				hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
				claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				if(policy.getHomeOfficeCode() != null) {
					OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
					if(branchOffice != null){
						String officeCpuCode = branchOffice.getCpuCode();
						if(officeCpuCode != null) {
							cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
						}
					}
				}
			}
			else{
				if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) && 
						SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
				{
					//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				}
				else {
					//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					if(policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
			}
		}
		else
		{
			if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
				hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
				claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			}
			else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
				hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
				claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				if(policy.getHomeOfficeCode() != null) {
					OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
					if(branchOffice != null){
						String officeCpuCode = branchOffice.getCpuCode();
						if(officeCpuCode != null) {
							cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
						}
					}
				}
			} 
			if(isHospTopUpIntmAvail) {
				claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
			}
		}
	}
	
	public void processPremiaWSForUniquePremium(){
		
		List<ClaimPayment> paymentList = getUniquePremiumPremiaUpdationFromPayment();
		try
		{
			utx.begin();
			if(null != paymentList && !paymentList.isEmpty()) {
				log.info("&&&&&&&&&&&&&& Settled cases premium deduction happening -----> "   );
				for (ClaimPayment claimPayment : paymentList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put(PremiaConstants.POLICY_NUMBER, claimPayment.getPolicyNumber());
					jsonObj.put(PremiaConstants.INTIMATION_NUMBER, claimPayment.getIntimationNumber());
					jsonObj.put(PremiaConstants.CPU_CODE, claimPayment.getCpuCode() != null ? String.valueOf(claimPayment.getCpuCode()) : "");
					Double amt = (claimPayment.getApprovedAmount() != null ? claimPayment.getApprovedAmount() : 0d) - (claimPayment.getTotalApprovedAmount() != null ?claimPayment.getTotalApprovedAmount() : 0d) ;
					log.info("&&&&&&&&&&&&&& Settled cases premium deduction happening---> Adjustment Amount -----> " + amt  );
					jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(amt));
					if(claimPayment.getProductCode() != null &&
							claimPayment.getProductCode().equalsIgnoreCase(SHAConstants.STAR_UNIQUE_PRODUCT_CODE)){
					PremiaService.getInstance().updateAdjustmentAmount(jsonObj.toString());
					}
					else{
						//code added for to adjust policy instalment premium to premia/bancs 
						PremiaService.getInstance().updateAdjustPolicyInstallment(jsonObj.toString());	
					}
					claimPayment.setPremiaWSFlag(SHAConstants.YES_FLAG);
					entityManager.merge(claimPayment);
					entityManager.flush();
				}
			}
			utx.commit();
	}
	catch(Exception e)
		{
			e.printStackTrace();
			try {
				utx.rollback();
			}  catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
			}
		}
	}
	
	
public void reminderLetterGeneratedForPreauthApproved(){
		
	try {
		
		Query query = entityManager.createNamedQuery("CashlessWorkFlow.findPreauthApproved");
		query.setParameter("outcome", SHAConstants.OUTCOME_PROCESS_PREAUTH_APPROVE);
		query.setParameter("previous",SHAConstants.PP_CURRENT_QUEUE);
		query.setParameter("current", SHAConstants.PREAUTH_END_Q);

		List<CashlessWorkFlow> cashlessWorkFlowList = (List<CashlessWorkFlow>)query.getResultList();
		
		log.info("Starting REMINDER FOR PREAUTH CASES------> count = "+ cashlessWorkFlowList.size());
		
		for (CashlessWorkFlow cashlessWorkFlow : cashlessWorkFlowList) {
			
			Long cashlessKey = cashlessWorkFlow.getCashlessKey();
			utx.begin();
			Preauth preauthById = getPreauthById(cashlessKey);
			if(preauthById != null){
//				callBPMRemainderTask(preauthById, BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
				cashlessWorkFlow.setRemarks(SHAConstants.REMINDER_REMARKS);
				entityManager.merge(cashlessWorkFlow);
				entityManager.flush();
				
			}
			utx.commit();
			
		}
		
		log.info("Completed REMINDER FOR PREAUTH CASES------> count = "+ cashlessWorkFlowList.size());
	
		
		
	}catch(Exception e){
		e.printStackTrace();
		try {
			utx.rollback();
		} catch (IllegalStateException | SecurityException
				| SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
		}
	}
		
		
		
	}
	
//public void reminderLetterGeneratedForEhancementApproved(){
//		
//        try{
//		Query query = entityManager.createNamedQuery("CashlessWorkFlow.findPreauthApproved");
//		query.setParameter("outcome", SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_APPROVE);
//		query.setParameter("previous",SHAConstants.PE_CURRENT_QUEUE);
//		query.setParameter("current", SHAConstants.ENHANCEMENT_Q);
//
//		List<CashlessWorkFlow> cashlessWorkFlowList = (List<CashlessWorkFlow>)query.getResultList();
//		
//		log.info("Starting REMINDER FOR ENHANCEMENT CASES------> count = "+ cashlessWorkFlowList.size());
//		
//		for (CashlessWorkFlow cashlessWorkFlow : cashlessWorkFlowList) {
//			
//			Long cashlessKey = cashlessWorkFlow.getCashlessKey();
//			utx.begin();
//			Preauth preauthById = getPreauthById(cashlessKey);
//			if(preauthById != null){
//				
//				
//				stopCashlessReminderLetter(preauthById.getClaim().getKey(), BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//				stopCashlessReminderProcess(preauthById.getClaim().getKey(), BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//				
////				callBPMRemainderTask(preauthById, BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//				cashlessWorkFlow.setRemarks(SHAConstants.REMINDER_REMARKS);
//				entityManager.merge(cashlessWorkFlow);
//				entityManager.flush();
//				
//			}
//			utx.commit();
//			
//		}
//		
//		log.info("Completed REMINDER FOR ENHANCEMENT CASES------> count = "+ cashlessWorkFlowList.size());
//		
//        }catch(Exception e){
//    		e.printStackTrace();
//    		try {
//    			utx.rollback();
//    		} catch (IllegalStateException | SecurityException
//    				| SystemException e1) {
//    			// TODO Auto-generated catch block
//    			e1.printStackTrace();
//    			log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
//    		}
//    	}
//		
//		
//		
//	}

//public void reminderLetterGeneratedForWithdrawApproved(){
//	
//    try{
//    	
//    
//    List<String> outCome = new ArrayList<String>();
//    outCome.add(SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW);
//    outCome.add(SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW_REJECT_OTHERS);
//    outCome.add(SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW_REJECT);
//    outCome.add(SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_WITHDRAW_REJECT_OTHERS);
//    outCome.add(SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_PATIENT_NOT_ADMITTED);
//    outCome.add(SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_OTHERS);
//    
//    List<String> previousQ = new ArrayList<String>();
//    previousQ.add(SHAConstants.PE_CURRENT_QUEUE);
//    previousQ.add(SHAConstants.CURRENT_Q_WITHDRAW);
//    
//    List<String> currentQ = new ArrayList<String>();
//    currentQ.add("WDEN");
//    currentQ.add("CNVR");
//    currentQ.add("CNVR");
//    currentQ.add("WREN");
//
//	Query query = entityManager.createNamedQuery("CashlessWorkFlow.findWithdrawApproved");
//	query.setParameter("outcome", outCome);
//	query.setParameter("previous",previousQ);
//	query.setParameter("current", currentQ);
//
//	List<CashlessWorkFlow> cashlessWorkFlowList = (List<CashlessWorkFlow>)query.getResultList();
//	
//	log.info("Starting STOPPED REMINDER FOR WITHDRAW CASES------> count = "+ cashlessWorkFlowList.size());
//	
//	for (CashlessWorkFlow cashlessWorkFlow : cashlessWorkFlowList) {
//		
//		Long cashlessKey = cashlessWorkFlow.getCashlessKey();
//		utx.begin();
//		Preauth preauthById = getPreauthById(cashlessKey);
//		if(preauthById != null){
//			
//			
//			stopCashlessReminderLetter(preauthById.getClaim().getKey(), BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//			stopCashlessReminderProcess(preauthById.getClaim().getKey(), BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//
//			cashlessWorkFlow.setRemarks(SHAConstants.REMINDER_REMARKS);
//			entityManager.merge(cashlessWorkFlow);
//			entityManager.flush();
//			
//		}
//		utx.commit();
//		
//	}
//	
//	log.info("completed STOPPED REMINDER FOR WITHDRAW CASES------> count = "+ cashlessWorkFlowList.size());
//	
//	
//    }catch(Exception e){
//		e.printStackTrace();
//		try {
//			utx.rollback();
//		} catch (IllegalStateException | SecurityException
//				| SystemException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
//		}
//	}
//	
//	
//	
//}
	
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
	
//	public void callBPMRemainderTask(Preauth preauth, String userName, String password) {
//		CLReminder initiateRemainderTask = BPMClientContext
//				.getCashlessReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER,
//						BPMClientContext.BPMN_PASSWORD);
//		PayloadBOType payload = new PayloadBOType();
//		PreAuthReqType preauthRequest  = new PreAuthReqType();
//		
//		PolicyType policyBO = new PolicyType();
//		policyBO.setPolicyId(preauth.getClaim().getIntimation().getPolicy().getPolicyNumber());
//		payload.setPolicy(policyBO);
//		
//		IntimationType intimationBo = new IntimationType();
//		intimationBo.setIntimationNumber(preauth.getClaim().getIntimation().getIntimationId());
//		payload.setIntimation(intimationBo);
//		
//		ClaimRequestType claimRequest = new ClaimRequestType();
//		
//	try {
//		preauthRequest.setKey(preauth.getKey());
//
////		String outCome = "SUBMIT";
////		
////		preauthRequest.setResult(outCome);
////		preauthRequest.setOutcome(outCome);
//		
//		ClaimType calimBo = new ClaimType();
//		
//		calimBo.setClaimId(preauth.getClaim().getClaimId());
//		calimBo.setKey(preauth.getClaim().getKey());
//		calimBo.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
//		payload.setClaim(calimBo);
//		
//		
//		PedReqType pedReq = new PedReqType();
//		
//		if(userName != null){
//				pedReq.setReferredBy(userName);
//		}
//		
//		Stage stage = entityManager.find(Stage.class, preauth.getStage().getKey());
//		
//		if(null != preauth && null != preauth.getClaim() && null != preauth.getClaim().getIntimation() && null != preauth.getClaim().getIntimation().getCpuCode())
//		{
//			claimRequest.setCpuCode(String.valueOf(preauth.getClaim().getIntimation().getCpuCode().getCpuCode()));
//			claimRequest.setClientType(SHAConstants.CASHLESS_CLAIM_TYPE);
//			payload.setClaimRequest(claimRequest);
//		}
//		
//		if(preauth.getStage() != null){
//			claimRequest.setOption(stage != null ? stage.getStageName() : "");
//			payload.setClaimRequest(claimRequest);
//		}
//		claimRequest.setOption(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
//		payload.setClaimRequest(claimRequest);
//		payload.setPedReq(pedReq);	
//		payload.setPreAuthReq(preauthRequest);
//		
//		QueryType queryBo = new QueryType();
//		payload.setQuery(queryBo);
//		
//		ClassificationType classificationType = new ClassificationType();
//		classificationType.setSource(stage.getStageName());
//		payload.setClassification(classificationType);
//		
//		initiateRemainderTask.initiate(BPMClientContext.BPMN_TASK_USER, payload);
//		
//		
//	} catch (Exception e) {
//		e.printStackTrace();
//		log.error(e.toString());
//	}
//	}
	
//	public void stopCashlessReminderLetter(Long claimKey,String userName,String password)
//	{
//		
//		try{
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationBo = new 	com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
//		
////		intimationBo.setIntimationNumber(intimationNo);
////		caPayloadBO.setIntimation(intimationBo);
//
//		claimBo.setKey(claimKey);
//		caPayloadBO.setClaim(claimBo);
//		
//			CLGenerateLetterTask generateBillsNotReceivedReminderLetterTask = BPMClientContext
//					.getCashlessRemainderLetterTask(userName, password);
//			PagedTaskList billsNotReceivedCLTaskList = generateBillsNotReceivedReminderLetterTask
//					.getTasks(userName, new Pageable(),
//							caPayloadBO);
//			
//			if(null != billsNotReceivedCLTaskList)
//			{
//				 List<HumanTask> humanTaskList  = billsNotReceivedCLTaskList.getHumanTasks();
//				 if(null != humanTaskList && !humanTaskList.isEmpty())
//				 {
//					 for(HumanTask clTask : humanTaskList){
//						 clTask.setOutcome("SUBMIT");
//							SubmitCLGenerateLetterTask   submitCLRemLetterTask = BPMClientContext.getSubmitCLReimnderLetterTask(userName, password);
//							submitCLRemLetterTask.execute(userName, clTask);
//					 }
//					 
//					 
//				 }
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error(e.toString());
//		}
//		
//	}
//	
//	public void stopCashlessReminderProcess(Long claimKey,String userName,String password)
//	{
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
////		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationBo = new 	com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
//		
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
//
//		claimBo.setKey(claimKey);
//		caPayloadBO.setClaim(claimBo);
//		
////		intimationBo.setIntimationNumber(intimationNo);
////		caPayloadBO.setIntimation(intimationBo);
//		CLReminderTask clReminder = BPMClientContext.getGenerateCashlessRemainderLetterTask(userName,password);
//		PagedTaskList pagedHumanTaskList = clReminder.getTasks(userName,new Pageable(),caPayloadBO);
//		if(null != pagedHumanTaskList)
//		{
//			 List<HumanTask> humanTaskList  = pagedHumanTaskList.getHumanTasks();
//			 if(null != humanTaskList && !humanTaskList.isEmpty())
//			 {
//				 humanTaskList.get(0).setOutcome("APPROVE");
//				 SubmitCLReminderTask submitTask =  BPMClientContext.getSubmitCLReimnderTask(userName,password);
//				 try {
//					submitTask.execute(userName , humanTaskList.get(0));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					log.error(e.toString());
//				}
//			 }
//		}
//	}


	public List<ClaimPayment> getUniquePremiumPremiaUpdationFromPayment() {
		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByUniqueAndPremiaWS");
		@SuppressWarnings("unchecked")
		List<ClaimPayment> paymentList = query.getResultList();
		return paymentList;
	}
	public void setEndrosementForPolicy(PremPolicyDetails premPolicyDetails)
			throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException {
		utx.begin();
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
		PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper
				.getInstance();
		List<PolicyEndorsementDetails> policyEndorsementList = premiaPolicyMapper
				.getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
		
		if (null != policyEndorsementList && !policyEndorsementList.isEmpty()) {
			for (PolicyEndorsementDetails policyEndorsementDetails : policyEndorsementList) {
				String endorsementText = policyEndorsementDetails
						.getEndorsementText();
				// if(endorsementText != null){
				// endorsementText = endorsementText.substring(0, 900);
				// policyEndorsementDetails.setEndorsementText(endorsementText);
				// }
				policyEndorsementDetails.setPolicy(policy);
				entityManager.persist(policyEndorsementDetails);
				entityManager.flush();
			}
		}

		

		
		utx.commit();

	}
	
	public void callReminderTaskForDB(Object[] inputArray){
		
//		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
//		
//		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		//remainder category - shaconstants - preauth bills not received (cashless_key), bills not received(claim_key), query(reimbursement_key)
		
		inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS;
		
		
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
//		dbCalculationService.initiateTaskProcedure(parameter);
		DBCalculationService dbCalculationService = new DBCalculationService();
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
	}	
	public void getPAInsuredFromPolicy(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException {
		
		utx.begin();
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
			PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		
		 List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
	        List<Insured> insuredPAFromPremia = premiaPolicyMapper.getInsuredPAFromPremia(premiaInsuredPAdetails);
	        for (Insured insured2 : insuredPAFromPremia) {
				if(insured2.getStrInsuredAge() != null){
					Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
					insured2.setInsuredAge(insuredAge);
				}
				if(insured2.getStrDateOfBirth() != null){
				
				 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
				 if(insuredDOB != null) {
					 insured2.setInsuredDateOfBirth(insuredDOB);
				 }
				}
				if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
					MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
					if(genderMaster != null && genderMaster.getKey() != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				/**
				 * As per Sathish ,if gender is blank  from premia, then
				 *  default it to MALE.
				 * */
				else
				{
					MastersValue genderMaster =  getKeyByValue("MALE");
					if(genderMaster != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				insured2.setLopFlag("P");
				
			}
	        
	        if(insuredPAFromPremia != null){
	        	policy.setInsuredPA(insuredPAFromPremia);
	        }
	        
	       
	        
	        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
	        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
	        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
					if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
						for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
							InsuredCover insuredCover = new InsuredCover();
							insuredCover.setCoverCode(coverDetails.getCoverCode());
							insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
							if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
								insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
							}
							insuredCoverDetailsForPA.add(insuredCover);
							
							
						}
						List<Insured> insuredPA = policy.getInsuredPA();
						if(insuredPA != null){
							for (Insured insured2 : insuredPA) {
								
								MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
								if (genderMaster != null &&  genderMaster.getKey() != null) {
									
									insured2.setInsuredGender(genderMaster);
								}
								
								
								if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
									insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
									break;
								}
							}
						}
					}
				}
	        }

	        
	        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
	        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
	        		 List<NomineeDetails> nomineeDetailsForPA = new ArrayList<NomineeDetails>();
	        		 
	        		 List<PolicyNominee> proposerNomineeDetailsForPA = new ArrayList<PolicyNominee>();
	        		 
					if(premiaInsured1.getNomineeDetails() != null && ! premiaInsured1.getNomineeDetails().isEmpty()){
						for (PremInsuredNomineeDetails coverDetails : premiaInsured1.getNomineeDetails()) {
							NomineeDetails nomineeDetail = new NomineeDetails();
							PolicyNominee proposerNominee = new PolicyNominee();
							if(coverDetails.getNomineeAge() != null && !coverDetails.getNomineeAge().isEmpty()){
								nomineeDetail.setNomineeAge(Long.valueOf(coverDetails.getNomineeAge()));
								proposerNominee.setNomineeAge(Integer.valueOf(coverDetails.getNomineeAge()));
							}
							nomineeDetail.setNomineeName(coverDetails.getNomineeName());
							proposerNominee.setRelationshipWithProposer(coverDetails.getNomineeRelation());
							proposerNominee.setNomineeName(coverDetails.getNomineeName());
							//TODO
							/*proposerNominee.setAccountNumber(coverDetails.getAccountNumber());
							proposerNominee.setNameAsPerBank(coverDetails.getBeneficiaryName());
							proposerNominee.setIFSCcode(coverDetails.getIfscCode());*/
							nomineeDetailsForPA.add(nomineeDetail);
							proposerNomineeDetailsForPA.add(proposerNominee);
							
							
						}
						List<Insured> insuredPA = policy.getInsuredPA();
						if(insuredPA != null){
							for (Insured insured2 : insuredPA) {
								if(insured2.getHealthCardNumber().equalsIgnoreCase(premiaInsured1.getIdCardNumber())){
									insured2.setNomineeDetails(nomineeDetailsForPA);
									insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForPA);
								}
							}
						}
					}
				}
	        }
	        savePAInsuredData(policy);
	        try {
				utx.commit();
			} catch (SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException
					| HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createGMCPolicy(Policy policy,List<Insured> insured) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
	{
		
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		if (null == policy2) {
			entityManager.persist(policy);
			entityManager.flush();
			
			
			List<PolicyRiskCover> policyRiskCoverDetails = policy.getPolicyRiskCoverDetails();
			
			if(policyRiskCoverDetails != null && !policyRiskCoverDetails.isEmpty()){
				for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
					policyRiskCover.setPolicyKey(policy.getKey());
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
					policyRiskCover.setPolicyKey(policy.getKey());
					if(policyRiskCover.getKey() == null){
						entityManager.persist(policyRiskCover);
						entityManager.flush();
					} else {
						entityManager.merge(policyRiskCover);
						entityManager.flush();
					}
				}
			}
			
			List<MasAilmentLimit> ailmentDetails = policy.getAilmentDetails();
			if(ailmentDetails != null){
				for (MasAilmentLimit masAilmentLimit : ailmentDetails) {
					masAilmentLimit.setPolicyKey(policy.getKey());
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
					if(copayLimit1.getCopayPercentage() != null && !copayLimit1.getCopayPercentage().equals(0.0d)){
						copayLimit1.setPolicyKey(policy.getKey());
						if(copayLimit1.getKey() == null){
							entityManager.persist(copayLimit1);
							entityManager.flush();
						}else{
							entityManager.merge(copayLimit);
							entityManager.flush();
						}
					}
				}
			}
			List<MasDeliveryExpLimit> deliveryExpLimit = policy.getDeliveryExpLimit();
			if(deliveryExpLimit != null){
				for (MasDeliveryExpLimit masDeliveryLimit : deliveryExpLimit) {
					masDeliveryLimit.setPolicyKey(policy.getKey());
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
					masPrepostLimt.setPolicyKey(policy.getKey());
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
					masRoomRentLimit.setPolicyKey(policy.getKey());
					//added for CR GLX2020066 by noufel
					if(masRoomRentLimit.getProportionateFlag() != null && !masRoomRentLimit.getProportionateFlag().isEmpty()){
						if(masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES) ||
								masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) || 
								masRoomRentLimit.getProportionateFlag().equalsIgnoreCase("A")){
							masRoomRentLimit.setProportionateFlag(SHAConstants.YES_FLAG);
						}else {
							masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
						}
					}
					else{
						masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
					}
					if(masRoomRentLimit.getKey() == null){
						entityManager.persist(masRoomRentLimit);
						entityManager.flush();
					}else{
						entityManager.merge(masRoomRentLimit);
						entityManager.flush();
					}
				}
			}
			
			
			//CR2019054 - TOP-UP POLICY
			List<RelatedPolicies> relatedPolicies = policy.getRelatedPolicies();
			if(relatedPolicies != null){
				for (RelatedPolicies relatedPolicy : relatedPolicies) {
					relatedPolicy.setPolicyKey(policy.getKey());
					if(relatedPolicy.getKey() == null){
						relatedPolicy.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						relatedPolicy.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.persist(relatedPolicy);
						entityManager.flush();
					}else{
						relatedPolicy.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						relatedPolicy.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(relatedPolicy);
						entityManager.flush();
					}
				}
			}
			
			List<PolicyBankDetails> policyBankDetails = policy.getPolicyBankDetails();
			if(policyBankDetails != null && !policyBankDetails.isEmpty()){
				for (PolicyBankDetails policyBankDetails2 : policyBankDetails) {
					policyBankDetails2.setPolicyKey(policy.getKey());
					if(policyBankDetails2.getKey() == null){
						entityManager.persist(policyBankDetails2);
						entityManager.flush();
					} else {
						entityManager.merge(policyBankDetails2);
						entityManager.flush();
					}
				}
			}
			
			//added for GMC corpBufferLimit Value save CR by noufel
			List<GmcCoorporateBufferLimit> corpBufferLimitList = policy.getGmcCorpBufferLimit();
			
			if(corpBufferLimitList != null){
				for (GmcCoorporateBufferLimit corpBufferLimit : corpBufferLimitList) {
					corpBufferLimit.setPolicyKey(policy.getKey());
					corpBufferLimit.setActiveStatus(1l);
					corpBufferLimit.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
					corpBufferLimit.setCreatedDate(new Timestamp(System
							.currentTimeMillis()));
					corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
					if(corpBufferLimit.getKey() == null){
						entityManager.persist(corpBufferLimit);
						entityManager.flush();
					}else{
						corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
						corpBufferLimit.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						corpBufferLimit.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(corpBufferLimit);
						entityManager.flush();
					}
				}
			}
			
			
			policy2 = policy;
		}
		
		
//		List<Insured> ListOfInsured = getInsuredListByPolicyNo(policy2.getPolicyNumber());
//		List<Long> riskNumberList = new ArrayList<Long>();
//		for (Insured insured2 : ListOfInsured) {
//			if(insured2.getLopFlag() == null || (insured2.getLopFlag() != null && insured2.getLopFlag().equalsIgnoreCase("H")))
//			riskNumberList.add(insured2.getInsuredId());
//		}
		
		if(null != insured && !insured.isEmpty()) {
			
			for (Insured  objInsured : insured)  {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), objInsured.getInsuredId());
				for (Insured insured2 : existingInsured) {
					if(insured2.getLopFlag() == null || (insured2.getLopFlag() != null && insured2.getLopFlag().equalsIgnoreCase("H")))
					riskNumberList.add(insured2.getInsuredId());
			     }
				
				if(!riskNumberList.contains(objInsured.getInsuredId())){
					
				objInsured.setPolicy(policy2);
				objInsured.setLopFlag("H");
				
				if(objInsured.getStrEffectivedFromDate() != null && !objInsured.getStrEffectivedFromDate().isEmpty()){
					Date fromDate = SHAUtils.formatTimeFromString(objInsured.getStrEffectivedFromDate());
					if(fromDate != null){
						objInsured.setEffectiveFromDate(fromDate);	
					}
				}
				if(objInsured.getStrEffectiveToDate() != null && !objInsured.getStrEffectiveToDate().isEmpty()){
					Date toDate = SHAUtils.formatTimeFromString(objInsured.getStrEffectiveToDate());
					if(toDate != null){
						objInsured.setEffectiveToDate(toDate);	
					}
				}
				
				if(policy2.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
				}
				if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
					objInsured.setInsuredPinCode(null);
				}
				//GMC Hospital CASH
				if(policy2.getPhcBenefitDays() != null){
					objInsured.setGhcDays(policy2.getPhcBenefitDays());
				}
				
				if(objInsured.getGhcPolicyType() !=null && !objInsured.getGhcPolicyType().isEmpty())
				{
					String ghcType = objInsured.getGhcPolicyType();
					objInsured.setGhcPolicyType(ghcType);
				}
				
				if(objInsured.getGhcScheme() !=null && !objInsured.getGhcScheme().isEmpty())
				{
					String ghcScheme = objInsured.getGhcScheme();
					objInsured.setGhcScheme(ghcScheme);
				}
				
				Boolean tataPolicy = isTataPolicy(policy.getPolicyNumber());
				if(tataPolicy != null && tataPolicy){
					Long insuredId = objInsured.getInsuredId();
					if(insuredId != null){
					objInsured.setHealthCardNumber(insuredId.toString());
					}
				}
				
				if(objInsured != null){
					entityManager.persist(objInsured);
					entityManager.flush();
				}
				
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
				
				if(objInsured.getInsuredPedList() != null && !objInsured.getInsuredPedList().isEmpty()) {
					for(InsuredPedDetails insuredPed : objInsured.getInsuredPedList()){
						insuredPed.setInsuredKey(objInsured.getInsuredId());
						String pedDescription = insuredPed.getPedDescription();
						if(pedDescription != null && pedDescription.length() >300) {
							pedDescription = pedDescription.substring(0, 299);
							insuredPed.setPedDescription(pedDescription);
						}
						if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//							Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
//							if(fromDate != null){
							insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));
//							}
						}
						if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//							Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
//							if(fromDate != null){
							insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));
//							}
						}
						if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
							insuredPed.setPedType(insuredPed.getPedType());
						}
						if(insuredPed.getKey() == null){
							entityManager.persist(insuredPed);
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
				
				
			  }
			}
		}
		
		List<Insured> dependentInsuredId = policy.getDependentInsuredId();
		if(dependentInsuredId != null && ! dependentInsuredId.isEmpty()){
			for (Insured insured2 : dependentInsuredId) {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), insured2.getInsuredId());
				for (Insured depInsured : existingInsured) {
					if(depInsured.getLopFlag() == null || (depInsured.getLopFlag() != null && depInsured.getLopFlag().equalsIgnoreCase("H")))
					riskNumberList.add(depInsured.getInsuredId());
			     }
				
				if(!riskNumberList.contains(insured2.getInsuredId())){
					insured2.setPolicy(policy2);
					insured2.setLopFlag("H");
					if(insured2.getStrEffectivedFromDate() != null && !insured2.getStrEffectivedFromDate().isEmpty()){
						Date fromDate = SHAUtils.formatTimeFromString(insured2.getStrEffectivedFromDate());
						if(fromDate != null){
							insured2.setEffectiveFromDate(fromDate);	
						}
					}
					if(insured2.getStrEffectiveToDate() != null && !insured2.getStrEffectiveToDate().isEmpty()){
						Date toDate = SHAUtils.formatTimeFromString(insured2.getStrEffectiveToDate());
						if(toDate != null){
							insured2.setEffectiveToDate(toDate);	
						}
					}
					
					if(insured2.getInsuredPinCode() != null && ! StringUtils.isNumeric(insured2.getInsuredPinCode())){
						insured2.setInsuredPinCode(null);
					}
					Boolean tataPolicy = isTataPolicy(policy.getPolicyNumber());
					if(tataPolicy != null && tataPolicy){
						Long insuredId = insured2.getInsuredId();
						if(insuredId != null){
							insured2.setHealthCardNumber(insuredId.toString());
						}
					}
					if(insured2 != null){
						entityManager.persist(insured2);
						entityManager.flush();
					}
					
					if(insured2.getGmcContBenefitDtls() != null && !insured2.getGmcContBenefitDtls().isEmpty()){
						for (GmcContinuityBenefit continuityBenefit : insured2.getGmcContBenefitDtls()) {
							continuityBenefit.setPolicy(policy2);
							continuityBenefit.setInsured(insured2);
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
					
					if(insured2.getInsuredPedList() != null && !insured2.getInsuredPedList().isEmpty()) {
						for(InsuredPedDetails insuredPed : insured2.getInsuredPedList()){
							insuredPed.setInsuredKey(insured2.getInsuredId());
							String pedDescription = insuredPed.getPedDescription();
							if(pedDescription != null && pedDescription.length() >300) {
								pedDescription = pedDescription.substring(0, 299);
								insuredPed.setPedDescription(pedDescription);
							}
							//added for CR ped effective date save in PED table GLX0132
							if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
								Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
								if(fromDate != null){
									insuredPed.setPedEffectiveFromDate(fromDate);	
								}
							}
							if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
								Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
								if(fromDate != null){
									insuredPed.setPedEffectiveToDate(fromDate);	
								}
							}
							if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
								insuredPed.setPedType(insuredPed.getPedType());
							}
							if(insuredPed.getKey() == null){
								entityManager.persist(insuredPed);
								entityManager.flush();
							}
						}

					}
					
				}
			}
		}
		
		if(policy.getInsuredPA() != null && ! policy.getInsuredPA().isEmpty()){
			for (Insured insuredPA : policy.getInsuredPA()) {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), insuredPA.getInsuredId());
				for (Insured depInsured : existingInsured) {
					if((depInsured.getLopFlag() != null && depInsured.getLopFlag().equalsIgnoreCase("P")))
					riskNumberList.add(depInsured.getInsuredId());
			     }
				if(!riskNumberList.contains(insuredPA.getInsuredId())){
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
			

		}
		
		List<GpaBenefitDetails> gpaBenefitDetails = policy.getGpaBenefitDetails();
		if(gpaBenefitDetails != null && !gpaBenefitDetails.isEmpty()){
			List<GpaBenefitDetails> policyBenefitDetails = getPolicyBenefitDetails(policy2.getKey());
			List<String> conditionCodeList = new ArrayList<String>();
			if(policyBenefitDetails != null && ! policyBenefitDetails.isEmpty()){
				for (GpaBenefitDetails existingBenefitDetail : policyBenefitDetails) {
					conditionCodeList.add(existingBenefitDetail.getBenefitCode());
				}
			}
			
			for (GpaBenefitDetails gpaBenefitDetails2 : gpaBenefitDetails) {
				gpaBenefitDetails2.setPolicyKey(policy2.getKey());
				if(! conditionCodeList.contains(gpaBenefitDetails2.getBenefitCode())){
					if(gpaBenefitDetails2.getKey() != null){
						entityManager.merge(gpaBenefitDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(gpaBenefitDetails2);
						entityManager.flush();
					}
				}
				
				
			}
		}
		
		
		
	}
	public void savePAInsuredData(Policy policy) {
		
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		
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
		
		
	}
	
	
	public PremPolicyDetails fetchGmcPolicyDetailsFromPremia(String policyNumber,String riskSysId)
	{
		PremPolicyDetails policyDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNumber);
			policy.setRiskSysId(riskSysId);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			
			if (policy.getPolicyNo() != null) {
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNumber);
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						//builder = BancsSevice.getInstance().getGmcPolicyDetails();
					}else{
						builder = PremiaService.getInstance().getGmcPolicyDetails();
						policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, policy);
					}
				}else{
					builder = PremiaService.getInstance().getGmcPolicyDetails();
					policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, policy);
				}
			}
			
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getGmcPolicyDetails();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("----List policy Details----"+policyDetails);
	
		return policyDetails;
	
	}
	
	public List<PremGpaBenefitDetails> fetchGpaPolicyBenefitFromPremia(String policyNumber)
	{
		List<PremGpaBenefitDetails> benefitDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNumber);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			
			if(policyNumber != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNumber);
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						//builder = BancsSevice.getInstance().getGpaBenefitDetails();
					}else{
						builder = PremiaService.getInstance().getGpaBenefitDetails();
						benefitDetails= builder.post(new GenericType<List<PremGpaBenefitDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
					}
				}else{
					builder = PremiaService.getInstance().getGpaBenefitDetails();
					benefitDetails= builder.post(new GenericType<List<PremGpaBenefitDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
				}
			}
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getGpaBenefitDetails();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("----List policy Details----"+benefitDetails);
	
		return benefitDetails;
	}
	
	public BeanItemContainer<RenewedPolicyDetails> fetchRenewedPolicyDetails(String policyNo) {
		List<RenewedPolicyDetails> renewedPolicyDtls = new ArrayList<RenewedPolicyDetails>();
		BeanItemContainer<RenewedPolicyDetails> policyListContainer =
	            new BeanItemContainer<RenewedPolicyDetails>(RenewedPolicyDetails.class);
		try {
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNo);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			
			if (policy.getPolicyNo() != null) {
				String bancsrenewedFlag = "N";
	            BPMClientContext bpmClientContext = new BPMClientContext();
	             bancsrenewedFlag = bpmClientContext.getBancsRenewalFlag();
				List<BancsRenewedPolicyInformationResponse> response = null;
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNo);
				if (policyObj != null) {
					 if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
							 && bancsrenewedFlag != null && bancsrenewedFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						 response = BancsSevice.getInstance().getRenewedPolicyDetails(policyNo);
						 if(response != null && !response.isEmpty()){
							for(BancsRenewedPolicyInformationResponse bancsResponse : response){
								RenewedPolicyDetails renewedList = new RenewedPolicyDetails();
								renewedList.setFromDate(bancsResponse.getPolicyInceptionDate());
								renewedList.setToDate(bancsResponse.getPolicyExpiryDate());
								renewedList.setSumInsured(bancsResponse.getSumInsured());
								renewedList.setGrossPremium(bancsResponse.getGrossPremium());
								renewedList.setPolicyNumber(bancsResponse.getPolicyNumber());
								renewedList.setProductCode(bancsResponse.getProductCode());
								renewedList.setProductName(bancsResponse.getProductName());
								renewedList.setUnderWriteYear(bancsResponse.getUnderwritingYear());
								renewedPolicyDtls.add(renewedList);
							}
						 }
						
					}else{
						builder = PremiaService.getInstance().getRenewedPolicyDetails();
						renewedPolicyDtls= builder.post(new GenericType<List<RenewedPolicyDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
					}
				}else{
					builder = PremiaService.getInstance().getRenewedPolicyDetails();
					renewedPolicyDtls= builder.post(new GenericType<List<RenewedPolicyDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
				}
			}
			
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getRenewedPolicyDetails();
			if(renewedPolicyDtls != null && !renewedPolicyDtls.isEmpty()){
				for (RenewedPolicyDetails renewedPolicyDetails : renewedPolicyDtls) {
					if(renewedPolicyDetails.getPolicyNumber() != null && !renewedPolicyDetails.getPolicyNumber().isEmpty()){
						policyListContainer.addBean(renewedPolicyDetails);
					} else {
						policyListContainer.addBean(null);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("--------RenewalPolicyList------"+policyListContainer);
		return policyListContainer;
	}
	
	public List<RenewedPolicyDetails> getRenewedPolicyDetails(String policyNo) {
		List<RenewedPolicyDetails> renewedPolicyDtls = new ArrayList<RenewedPolicyDetails>();
		
		try {
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNo);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			
			if (policy.getPolicyNo() != null) {
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNo);
				if (policyObj != null) {
					String bancsrenewedFlag = "N";
		            BPMClientContext bpmClientContext = new BPMClientContext();
		             bancsrenewedFlag = bpmClientContext.getBancsRenewalFlag();
					List<BancsRenewedPolicyInformationResponse> response = null;
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
							&& bancsrenewedFlag != null && bancsrenewedFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						 response = BancsSevice.getInstance().getRenewedPolicyDetails(policyNo);
						 if(response != null && !response.isEmpty()){
							for(BancsRenewedPolicyInformationResponse bancsResponse : response){
								RenewedPolicyDetails renewedList = new RenewedPolicyDetails();
								renewedList.setFromDate(bancsResponse.getPolicyInceptionDate());
								renewedList.setToDate(bancsResponse.getPolicyExpiryDate());
								renewedList.setSumInsured(bancsResponse.getSumInsured());
								renewedList.setGrossPremium(bancsResponse.getGrossPremium());
								renewedList.setPolicyNumber(bancsResponse.getPolicyNumber());
								renewedList.setProductCode(bancsResponse.getProductCode());
								renewedList.setProductName(bancsResponse.getProductName());
								renewedList.setUnderWriteYear(bancsResponse.getUnderwritingYear());
								renewedPolicyDtls.add(renewedList);
							}
						 }
						
					}else{
						builder = PremiaService.getInstance().getRenewedPolicyDetails();
						renewedPolicyDtls= builder.post(new GenericType<List<RenewedPolicyDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
					}
				}else{
					builder = PremiaService.getInstance().getRenewedPolicyDetails();
					renewedPolicyDtls= builder.post(new GenericType<List<RenewedPolicyDetails>>() {}, "\""+policy.getPolicyNo()+ "\"");
				}
			}
			
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getRenewedPolicyDetails();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("--------RenewalPolicyList------"+renewedPolicyDtls);
		return renewedPolicyDtls;
	}
	
	// Method duplicated from populatePolicyFromTmpPolicy to return policy record object. 
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public Policy populatePolicyToAddOMPIntimation(PremPolicyDetails premPolicyDetails) {
			Boolean isAllowed = true;
			Policy policy = null;
			try {

				if (premPolicyDetails.getPolType() != null&& premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) {
					isAllowed = isIntegrated(premPolicyDetails,ReferenceTable.FLOATER_POLICY);
				} else {
					isAllowed = isIntegrated(premPolicyDetails,ReferenceTable.INDIVIDUAL_POLICY);
				}

				if (!isAllowed) {
					try {
						utx.begin();
						Policy existingPolicy = getPolicy(premPolicyDetails.getPolicyNo());
						if (existingPolicy != null) {
							List<Intimation> intimationByPolicyKey = getIntimationByPolicyKey(existingPolicy.getKey());
							if (intimationByPolicyKey != null && !intimationByPolicyKey.isEmpty()) {
								log.info("############# Intrgration Policy Part If intimation is existing for this policy then we will allow further ------>>>>>"+ premPolicyDetails.getPolicyNo());
								isAllowed = true;
							}
						}
						utx.commit();
					} catch (Exception e) {
						log.error("############# Error while processing the integration part ----->>>"+ premPolicyDetails.getPolicyNo());
						utx.rollback();
					}

				}

				if (isAllowed) {
					policy = populateOMPPolicyData(premPolicyDetails, true, true);
					if (policy == null) {
						log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->"+ premPolicyDetails.getPolicyNo());
					}
					if (premPolicyDetails.getPolicyType() == null || (premPolicyDetails.getPolicyType() != null 
							&& !premPolicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))) {
						List<PremPreviousPolicyDetails> previousPolicyDetails = premPolicyDetails.getPreviousPolicyDetails();
						if(previousPolicyDetails!=null){
						for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
							try {
								Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
								if (previousPolicyObj == null) {
									PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(previousPolicy.getPolicyNo());
									if (policyDetails != null) {
										populatePolicyData(policyDetails, false, true);
									}
								}
							} catch (Exception e) {
								log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->"+ previousPolicy.getPolicyNo());
							}

						}}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (null != premPolicyDetails)
					log.error("#*#*#*#*#*#*#*#*#*#ERROR OCCURED IN populatePolicyToAddOMPIntimation METHOD *#*#*#*#*#*#*#*#*#*#*#--------->"+ premPolicyDetails.getPolicyNo());
			}
			return policy;
		}

		private Policy populateOMPPolicyData(PremPolicyDetails premPolicyDetails,
				Boolean isCurrentPolicy, Boolean isAlreadyHittedDBForPolicy) throws IllegalStateException,
				SecurityException, SystemException {

			try {
				utx.begin();
				Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
				if (null == policy) {
					 PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
					 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
					 Double totalAmount = 0d;
					 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
					 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
					 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
					 policy.setTotalPremium(totalAmount);
					 
					 try{
						 if(premPolicyDetails.getDeductiableAmt() != null){
							 policy.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getDeductiableAmt()).doubleValue());
						 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
					 
					 if(premPolicyDetails.getPolSysId() != null){
						 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
				
						 policy.setPolicySystemId(polSysId);
					 }
					 
					 if(!isCurrentPolicy) {
						policy.setKey(policy.getPolicySystemId()); 
					 }
					 
					 if(premPolicyDetails.getPolicyPlan() != null){
	 					 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
							 policy.setPolicyPlan("A");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
							 policy.setPolicyPlan("B");
	 					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)){
							 policy.setPolicyPlan("G");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
							 policy.setPolicyPlan("S");
						 }
						 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
							 policy.setPolicyPlan("B");
						 }
					 }
//					 Below Cond for SCRC - MED-PRD-070 - R201811302
					 if(premPolicyDetails.getPolicyTerm() != null) {
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED.equals(premPolicyDetails.getProductCode())){
							 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
								 String policyTermYear[] = premPolicyDetails.getPolicyTerm().split(" ");
								 String policyTerm = policyTermYear[0];
								 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTerm));
							 }
						 }
						 else if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
							 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
							 policy.setPolicyTerm(policyTerm);
						 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(2l);
						 }
						 //added for new product076
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
							 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) { 
								 String policyTermYear = SHAUtils.getTruncateWord(premPolicyDetails.getPolicyTerm(), 0, 1);
								 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTermYear));
							 }
						 }
							//Added for MED-PRD-083
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(1l);
					 }
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(1l);
						 }
					 }
					 
					 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
					 if(policyStrYear != null){
						 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
					 }
					 
					 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true))
					 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 
					 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)
						 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 
					 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true)
					 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
					 
					 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true)
					 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
					 
					 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true)
						 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
					 
					 if(null != getMasterByValue(premPolicyDetails.getLob()))
						 policy.setLobId(getMasterByValue((premPolicyDetails.getLob())).getKey());
					 
					 if(null != getMasterByValue(premPolicyDetails.getPolicyType()))
					 policy.setPolicyType(getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
					 
	 				 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
						 MastersValue schemeId = getMasterByValue(premPolicyDetails.getSchemeType());
						 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
					 }
					 
					//TODO:Get product type from premia 
//					 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//					 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
					 
					 Product productByProductCodeForPremia = getProductByProductCode(premPolicyDetails.getProductCode(),new Date(premPolicyDetails.getPolicyStartDate()));
					 if(productByProductCodeForPremia == null) {
						 return null;
					 }
					 policy.setProduct(productByProductCodeForPremia);
					 policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
					 
					 List<PolicyEndorsementDetails> endorsementDetailsList = premiaPolicyMapper.getInstance().getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
					 List<PreviousPolicy> previousPolicyList = null;
					 if(premPolicyDetails.getPreviousPolicyDetails()!=null){
						 
						 previousPolicyList = premiaPolicyMapper.getInstance().getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
						//Set previous policy Date
						 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
							 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
								 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
							 }
							 if(null == premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()||premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().isEmpty() ? false : true){
								 previousPolicyList.get(index).setPolicyToDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
							 }
						 }
					 }
					 //set Endrosement Date
					 
					 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
						 endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						 endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						 }
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
							 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
							 }
					 }
					 if(premPolicyDetails.getInsuredDetails()!=null){
						 List<Insured> insured = premiaPolicyMapper.getInstance().getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
					
					 //set insured Date
					
					 
					 List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
	 				 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
						MastersValue genderMaster =  getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
						 insured.get(index).setInsuredGender(genderMaster);
						
						 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
							 
							 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
							 //Added for insured age caluculation.
							 Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));
							 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
							 if(formatPremiaDate != null) {
								 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);
							 }
						 }
						 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
				 		 
				 		 if(insuredAge != null){
				 			 insured.get(index).setInsuredAge(insuredAge);
				 		 } 
				 		 
				 		 insured.get(index).setLopFlag("H");

					 }
					 
					 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
							 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInstance().getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
							 
							 List<PremPortability> portablitityDetails = premPolicyDetails.getInsuredDetails().get(index).getPortablitityDetails();
							 
							 if(portablitityDetails != null){
								 List<PortablityPolicy> portablityList = premiaPolicyMapper.getInstance().getPortablityList(portablitityDetails);
								 for (PortablityPolicy portablityPolicy : portablityList) {
									
									 try{
									 if(portablityPolicy.getStrDateOfBirth() != null && ! portablityPolicy.getStrDateOfBirth().equalsIgnoreCase("")){
										 portablityPolicy.setDateOfBirth(new Date(portablityPolicy.getStrDateOfBirth()));
									 }
									 if(portablityPolicy.getStrMemberEntryDate() != null && ! portablityPolicy.getStrMemberEntryDate().equalsIgnoreCase("")){
										 portablityPolicy.setMemberEntryDate(new Date(portablityPolicy.getStrMemberEntryDate()));
									 }
									 
									 if(portablityPolicy.getPolicyStrStartDate() != null && ! portablityPolicy.getPolicyStrStartDate().equalsIgnoreCase("")){
										 portablityPolicy.setPolicyStartDate(new Date(portablityPolicy.getPolicyStrStartDate()));
									 }
									 }catch(Exception e){
										 e.printStackTrace();
									 }
								}
								 insured.get(index).setPortablityPolicy(portablityList);
							 }
							 
							 /**
							  * Below code was added as part of CR R1080
							  */
							 List<PremPortabilityPrevPolicyDetails> premPortablitityPrevPolDetails = premPolicyDetails.getInsuredDetails().get(index).getPortabilityPrevPolicyDetails();
							 
							 if(premPortablitityPrevPolDetails != null){
								 List<PortabilityPreviousPolicy> portablityPrevPolList = premiaPolicyMapper.getInstance().getPortablityPrevPolicyList(premPortablitityPrevPolDetails);
								 for (PortabilityPreviousPolicy portablityPrevPolicy : portablityPrevPolList) {
									
									 try{
									 if(portablityPrevPolicy.getPolicyStrFmDt() != null && ! portablityPrevPolicy.getPolicyStrFmDt().isEmpty()){
										 portablityPrevPolicy.setPolicyFmDt(SHAUtils.getFromDate(portablityPrevPolicy.getPolicyStrFmDt()));
									 }
									 if(portablityPrevPolicy.getPolicyStrToDt() != null && ! portablityPrevPolicy.getPolicyStrToDt().isEmpty()){
										 portablityPrevPolicy.setPolicyToDt(SHAUtils.getFromDate(portablityPrevPolicy.getPolicyStrToDt()));
									 }
									 portablityPrevPolicy.setCurrentPolicyNumber(policy.getPolicyNumber());
									 portablityPrevPolicy.setInsuredName(insured.get(index).getInsuredName());
									 }catch(Exception e){
										 e.printStackTrace();
									 }
								}
								 insured.get(index).setPortablityPrevPolicy(portablityPrevPolList);
							 }
							 
							 //  End of  CR R1080
					 }
						 
					 
					List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();				 
					int i=0;
	 				if(null != insured && !insured.isEmpty()) {
						for (Insured insured2 : insured) {
							if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
								Double totalSumInsured = policy.getTotalSumInsured();
								Double size = Double.valueOf(insured.size());
								Double sumInsured = 0d;
								if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)){
									if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
										sumInsured = totalSumInsured/size;
									}
									Long roundOfSI = Math.round(sumInsured);
									insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
								}else{
									insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
								}
								
			//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
								
							} else {
			//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
							}
							
							if(premiaInsured.get(i).getSelfDeclaredPed() != null && premiaInsured.get(i).getSelfDeclaredPed().length() >0){
								InsuredPedDetails selfDeclaredPed = new InsuredPedDetails();
								selfDeclaredPed.setInsuredKey(insured2.getInsuredId());
								selfDeclaredPed.setPedDescription(premiaInsured.get(i).getSelfDeclaredPed());
								List<InsuredPedDetails> pedList = new ArrayList<InsuredPedDetails>();
								pedList.add(selfDeclaredPed);
								insured2.setInsuredPedList(pedList);
							}
							i++;
							 if(!isCurrentPolicy) {
									insured2.setKey(insured2.getInsuredId()); 
							 }		
						}
					    policy.setInsured(insured);
					}
				}
	 		        if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
			        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
	 		        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)){
			        	
			        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) { 
			     	    	
			        		 MastersValue productType = getMaster(ReferenceTable.FLOATER_POLICY);
			        		 policy.setProductType(productType);
			        		 
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null){
			        			 policy.setProduct(product);
			        		 }
			     	    } else {
			     	    	
			     	    	 MastersValue productType = getMasterValue(ReferenceTable.INDIVIDUAL_POLICY);
			        		 policy.setProductType(productType);
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null) {
			        			 policy.setProduct(product);
			        		 }
			     	    }
			        }
	 		        List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

			        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
			        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
			        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
			        		PolicyRiskCover riskCover = new PolicyRiskCover();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		if(premPolicyRiskCover2.getSumInsured() != null && ! premPolicyRiskCover2.getSumInsured().equals("")){
			        			//riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
							}
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
			        }
			        
			        if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
			        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getInstance().getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
			        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
							if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
							}
	 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
							}
						}
			        	policy.setPolicyBankDetails(bankDetailsFromPremia);
			        }
			        List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
			        if(premiaInsuredPAdetails!=null){
			        	List<Insured> insuredPAFromPremia = premiaPolicyMapper.getInstance().getInsuredPAFromPremia(premiaInsuredPAdetails);
			        	for (Insured insured2 : insuredPAFromPremia) {
							if(insured2.getStrInsuredAge() != null){
								Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
								insured2.setInsuredAge(insuredAge);
							}
							if(insured2.getStrDateOfBirth() != null){
							
							 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
							 if(insuredDOB != null) {
								 insured2.setInsuredDateOfBirth(insuredDOB);
							 }
							}
							if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
								MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
								if(genderMaster != null && genderMaster.getKey() != null){
									insured2.setInsuredGender(genderMaster);
								}
							}
							/**
							 * As per Sathish ,if gender is blank  from premia, then
							 *  default it to MALE.
							 * */
							else
							{
								MastersValue genderMaster =  getKeyByValue("MALE");
								if(genderMaster != null){
									insured2.setInsuredGender(genderMaster);
								}
							}
							insured2.setLopFlag("P");
							
						}
				        
				        if(insuredPAFromPremia != null){
				        	policy.setInsuredPA(insuredPAFromPremia);
				        }
			        }
			        
			        
			        PremInsuredOMPDetails premiaInsuredOmpdetails = premPolicyDetails.getPremiaInsuredOmpdetails();
			        if(premiaInsuredOmpdetails!=null){
			        	Insured insured = setOMPInsured(premiaPolicyMapper,premiaInsuredOmpdetails);
			        	String policyStartDate = premPolicyDetails.getPolicyStartDate();
			        	if(policyStartDate!=null && policyStartDate.length()>1){
			        		Date depatureDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyStartDate)));
			        		insured.setDepartureDate(depatureDate);
			        		
			        	}
			        	String policyEndDate = premPolicyDetails.getPolicyEndDate();
			        	if(policyEndDate != null && policyEndDate.length()>1){
			        		Date returnDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyEndDate)));
			        		insured.setReturnDate(returnDate);
			        		
			        	}
			        	List<Insured> insuredList = new ArrayList<Insured>();
			        	insuredList.add(insured);
			        	policy.setInsured(insuredList);
				       
			        }
			        
			        PremInsuredOMPDetails premiaInsuredOmpCorpdetails = premPolicyDetails.getPremiaInsuredOmpCorpdetails();
			        if(premiaInsuredOmpCorpdetails!=null){
			        	Insured insured = setOMPInsured(premiaPolicyMapper,premiaInsuredOmpCorpdetails);
			        	String policyStartDate = premPolicyDetails.getPolicyStartDate();
			        	if(policyStartDate!=null && policyStartDate.length()>1){
			        		Date depatureDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyStartDate)));
			        		insured.setDepartureDate(depatureDate);
			        		
			        	}
			        	String policyEndDate = premPolicyDetails.getPolicyEndDate();
			        	if(policyEndDate != null && policyEndDate.length()>1){
			        		Date returnDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyEndDate)));
			        		insured.setReturnDate(returnDate);
			        		
			        	}
			        	List<Insured> insuredList = new ArrayList<Insured>();
			        	insuredList.add(insured);
			        	policy.setInsured(insuredList);
				       
			        }
			        
			        PremInsuredOMPDetails premiaInsuredOmpStudentdetails = premPolicyDetails.getPremiaInsuredOmpStudentdetails();
			        if(premiaInsuredOmpStudentdetails!=null){
			        	Insured insured = setOMPInsured(premiaPolicyMapper,premiaInsuredOmpStudentdetails);
			        	String policyStartDate = premPolicyDetails.getPolicyStartDate();
			        	if(policyStartDate!=null && policyStartDate.length()>1){
			        		Date depatureDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyStartDate)));
			        		insured.setDepartureDate(depatureDate);
			        		
			        	}
			        	String policyEndDate = premPolicyDetails.getPolicyEndDate();
			        	if(policyEndDate != null && policyEndDate.length()>1){
			        		Date returnDate = SHAUtils.formatPremiaDate(SHAUtils
			        				.formatPremiaDateAsString(new Date(policyEndDate)));
			        		insured.setReturnDate(returnDate);
			        		
			        	}
			        	List<Insured> insuredList = new ArrayList<Insured>();
			        	insuredList.add(insured);
			        	policy.setInsured(insuredList);
				       
			        }
			        
			        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
			        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
			        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
							if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
								for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
									InsuredCover insuredCover = new InsuredCover();
									insuredCover.setCoverCode(coverDetails.getCoverCode());
	 								insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
									if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
										insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
									}
									insuredCoverDetailsForPA.add(insuredCover);
									
									
								}
								List<Insured> insuredPA = policy.getInsuredPA();
								if(insuredPA != null){
									for (Insured insured2 : insuredPA) {
										
										MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
										if (genderMaster != null &&  genderMaster.getKey() != null) {
											
											insured2.setInsuredGender(genderMaster);
										}
										
										
										if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
											insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
											break;
										}
									}
								}
							}
						}
			        }
	      
			        
			        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
			        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
			        		 List<NomineeDetails> nomineeDetailsForPA = new ArrayList<NomineeDetails>();
			        		 List<PolicyNominee> proposerNominees = new ArrayList<PolicyNominee>();
			        		 
							if(premiaInsured1.getNomineeDetails() != null && ! premiaInsured1.getNomineeDetails().isEmpty()){
								for (PremInsuredNomineeDetails coverDetails : premiaInsured1.getNomineeDetails()) {
									NomineeDetails nomineeDetail = new NomineeDetails();
									PolicyNominee proposerNominee = new PolicyNominee();
									if(coverDetails.getNomineeAge() != null){
										nomineeDetail.setNomineeAge(Long.valueOf(coverDetails.getNomineeAge()));
										proposerNominee.setNomineeAge(Integer.valueOf(coverDetails.getNomineeAge()));
									}
									nomineeDetail.setNomineeName(coverDetails.getNomineeName());
									proposerNominee.setNomineeName(coverDetails.getNomineeName());
									//TODO
									/*proposerNominee.setAccountNumber(coverDetails.getAccountNumber());
									proposerNominee.setNameAsPerBank(coverDetails.getBeneficiaryName());
									proposerNominee.setIFSCcode(coverDetails.getIfscCode());*/
									nomineeDetailsForPA.add(nomineeDetail);
									proposerNominees.add(proposerNominee);
									
									
								}
								List<Insured> insuredPA = policy.getInsuredPA();
								if(insuredPA != null){
									for (Insured insured2 : insuredPA) {
										if(insured2.getHealthCardNumber().equalsIgnoreCase(premiaInsured1.getIdCardNumber())){
											insured2.setNomineeDetails(nomineeDetailsForPA);
											insured2.setProposerInsuredNomineeDetails(proposerNominees);
										}
									}
								}
							}
						}
			        }
			        Double sumInsured =0d;
			        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
			        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
			        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
			        		PolicyRiskCover riskCover = new PolicyRiskCover();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		riskCover.setSumInsured(Double.parseDouble(premPolicyRiskCover2.getSumInsured()));
			        		if("OMP-CVR-001".equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
			        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
			        		}else if("STP-CVR-001".equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
			        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
			        		}else if("CFT-CVR-001".equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
			        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
			        		}
			        		//Suganesh
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
			        	policy.setTotalSumInsured(sumInsured);
			        }
			      //added for saving UIN in policy table
			        if(premPolicyDetails.getPremiaUINCode() != null && !premPolicyDetails.getPremiaUINCode().isEmpty()) {
			        	policy.setPremiaUINCode(premPolicyDetails.getPremiaUINCode());
			        }
					createPolicy(policy, previousPolicyList, endorsementDetailsList);
					policy = getPolicy(premPolicyDetails.getPolicyNo());
				} 
				utx.commit();
				return policy;
			}
			catch(Exception e) {
				log.error("___________________  POLICY ERROR WHILE SAVING THE DATA ___________________" + premPolicyDetails.getPolicyNo());
				e.printStackTrace();
				utx.rollback();
				log.error("**************ERROR IN POPULATE POLICY DATA METHOD*********" + e.getMessage());
				return null;
			}
		
	}
	
	public PremPolicyDetails fetchGpaPolicyDetailsFromPremia(String policyNumber,String riskSysId)
	{
		PremPolicyDetails policyDetails = null;
		try
		{
			PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
			policy.setPolicyNo(policyNumber);
			policy.setRiskSysId(riskSysId);
			System.out.println("--the policy nu---"+policy.getPolicyNo());
			//Bancs Changes Start
			
			Policy policyObj = null;
			Builder builder = null;
			
			if(policy.getPolicyNo() != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policy.getPolicyNo());
				if (policyObj != null) {
					 if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						//builder = BancsSevice.getInstance().getGpaPolicyDetails();
					}else{
							builder = PremiaService.getInstance().getGpaPolicyDetails();
							policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, policy);

					}
				}else{
					builder = PremiaService.getInstance().getGpaPolicyDetails();
					policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, policy);

				}
			}
			//Bancs Changes End
			//Builder builder = PremiaService.getInstance().getGpaPolicyDetails();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("----List policy Details----"+policyDetails);
	
		return policyDetails;
	
	}
	
	private List<GpaBenefitDetails>  getPolicyBenefitDetails(Long policyKey) {
		
		Query query = entityManager.createNamedQuery("GpaBenefitDetails.findByPolicy");
    	query = query.setParameter("policyKey", policyKey);
    	List<GpaBenefitDetails> benefitList = (List<GpaBenefitDetails>) query.getResultList();
    	if (benefitList != null){
    		return benefitList;
    	}
		return null;
	}

		public Insured setOMPInsured(PremiaToPolicyMapper premiaPolicyMapper,PremInsuredOMPDetails premiaInsuredOmpdetails) {
			Insured insured2 =premiaPolicyMapper.getInstance().getOMPInsuredFromPremia(premiaInsuredOmpdetails);
				insured2.setInsuredId(Long.valueOf(premiaInsuredOmpdetails.getRiskSysID()));
				
				if(premiaInsuredOmpdetails.getDateOfExpiry() != null){
					Date dateFormatDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premiaInsuredOmpdetails.getDateOfExpiry())));
					insured2.setPassPortExpiryDate(dateFormatDate);
				}
				if(insured2.getStrInsuredAge() != null){
					Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
					insured2.setInsuredAge(insuredAge);
				}
				if(insured2.getStrDateOfBirth() != null){
				
				 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
				 if(insuredDOB != null) {
					 insured2.setInsuredDateOfBirth(insuredDOB);
				 }
				}
				//me
				if(premiaInsuredOmpdetails.getPurposeofVisit() != null){
					insured2.setPurposeOfvisit(premiaInsuredOmpdetails.getPurposeofVisit());
				}
				
				if(premiaInsuredOmpdetails.getPlaceOfIssue() != null && premiaInsuredOmpdetails.getPlaceOfIssue().length()>1){
					insured2.setPlaceOfIssue(premiaInsuredOmpdetails.getPlaceOfIssue());
				}
				
				if(premiaInsuredOmpdetails.getPlaceofTravel1() != null && premiaInsuredOmpdetails.getPlaceofTravel1().length()>1){
					insured2.setPlaceOfvisit(premiaInsuredOmpdetails.getPlaceofTravel1());
				}
				
				if(premiaInsuredOmpdetails.getCountryOfVisit() != null && premiaInsuredOmpdetails.getCountryOfVisit().length()>1){
					insured2.setPlaceOfvisit(premiaInsuredOmpdetails.getCountryOfVisit());
				}
				if(premiaInsuredOmpdetails.getPurposeOfTravel() != null && premiaInsuredOmpdetails.getPurposeOfTravel().length()>1){
					insured2.setPurposeOfvisit(premiaInsuredOmpdetails.getPurposeOfTravel());
				}
//				if(premiaInsuredOmpdetails.getDaysOfTravel() != null ){
//					Long numberofDays = SHAUtils.getLongFromString(premiaInsuredOmpdetails.getDaysOfTravel());
//					insured2.setNoOfDays(numberofDays);
//				}
//				if(premiaInsuredOmpdetails.getMedicalReportAttached()!= null){
//					insured2.setMedicalRptAttached(premiaInsuredOmpdetails.getMedicalReportAttached());
//				}
//				if(premiaInsuredOmpdetails.getI20No() != null){
//					insured2.setI20no(premiaInsuredOmpdetails.getI20No());
//				}
//				if(premiaInsuredOmpdetails.getNameOfInstitution() != null && premiaInsuredOmpdetails.getNameOfInstitution().length()>1){
//					insured2.setInstituteName(premiaInsuredOmpdetails.getNameOfInstitution());
//				}
//				if(premiaInsuredOmpdetails.getGender()!= null){
//					insured2.setSex(premiaInsuredOmpdetails.getGender());
//				}
//				if(premiaInsuredOmpdetails.getSumInsured() != null ){
//					Double suminsured = SHAUtils.getDoubleValueFromString(premiaInsuredOmpdetails.getSumInsured());
//					insured2.setInsuredSumInsured(suminsured);
//				}
//				if(premiaInsuredOmpdetails.getTripIncludeUSAandCanada() != null){
//					insured2.setTripIncludeUsaCanada(premiaInsuredOmpdetails.getTripIncludeUSAandCanada() );
//				}
				if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
					MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
					if(genderMaster != null && genderMaster.getKey() != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				/**
				 * As per Sathish ,if gender is blank  from premia, then
				 *  default it to MALE.
				 * */
				else
				{
					MastersValue genderMaster =  getKeyByValue("MALE");
					if(genderMaster != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				
				//CR_R1276_R1332
				if(premiaInsuredOmpdetails.getAssigneename() != null && !premiaInsuredOmpdetails.getAssigneename().isEmpty() && premiaInsuredOmpdetails.getRelation() != null && !premiaInsuredOmpdetails.getRelation().isEmpty()){
					List<PolicyNominee> proposerNomineeDetailsForHealth = new ArrayList<PolicyNominee>();
					PolicyNominee proposerNomineeDetail = new PolicyNominee();
					proposerNomineeDetail.setNomineeName(premiaInsuredOmpdetails.getAssigneename());
					proposerNomineeDetail.setRelationshipWithProposer(premiaInsuredOmpdetails.getRelation());
					/*proposerNomineeDetail.setAccountNumber(coverDetails.getAccountNumber());
					proposerNomineeDetail.setNameAsPerBank(coverDetails.getBeneficiaryName());
					proposerNomineeDetail.setIFSCcode(coverDetails.getIfscCode());*/
					proposerNomineeDetailsForHealth.add(proposerNomineeDetail);
					insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForHealth);
				}
				
				//insured2.setLopFlag("TRAVEL");
				return insured2;
				
				
		}
		
		public PremPolicyDetails fetchOMPPolicyDetailsFromPremia(String policyNumber) {
			PremPolicyDetails policyDetails = null;
			try {
				PremPolicySearchDTO policy = new PremPolicySearchDTO();
				policy.setPolicyNo(policyNumber);
				log.debug("****POLICY NUMBER IS *****--->" + policy.getPolicyNo());
				@SuppressWarnings("static-access")
				//Bancs Changes Start
				Policy policyObj = null;
				Builder builder = null;
				
				if (policy.getPolicyNo() != null) {
					DBCalculationService dbService = new DBCalculationService();
					policyObj = dbService.getPolicyObject(policyNumber);
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							//builder = BancsSevice.getInstance().getOMPPolicyDetail();
						}else{
							builder = PremiaService.getInstance().getOMPPolicyDetail();
							policyDetails = builder.post(new GenericType<PremPolicyDetails>() {
							}, "\"" + policy.getPolicyNo() + "\"");
						}
					}else{
						builder = PremiaService.getInstance().getOMPPolicyDetail();
						policyDetails = builder.post(new GenericType<PremPolicyDetails>() {
						}, "\"" + policy.getPolicyNo() + "\"");
					}
				}
				
				//Bancs Changes End
			//	Builder builder = PremiaService.getInstance().getOMPPolicyDetail();
				
			} catch (Exception e) {
				log.error("ERROR WHILE FETCHING POLCIY DETAILS FROM PREMIA ---"
						+ policyNumber);
				e.printStackTrace();
			}

			return policyDetails;
		}
		
		public PremPolicyDetails fetchJetPrivillagePolicyDetailsFromPremia(String policyNumber) {
			PremPolicyDetails policyDetails = null;
			try {
				PremPolicySearchDTO policy = new PremPolicySearchDTO(); 
				policy.setPolicyNo(policyNumber);
				log.debug("****POLICY NUMBER IS *****--->" + policy.getPolicyNo());
				@SuppressWarnings("static-access")
				//Bancs Changes Start
				
				Policy policyObj = null;
				Builder builder = null;
				
				if(policy.getPolicyNo() != null){
					DBCalculationService dbService = new DBCalculationService();
					policyObj = dbService.getPolicyObject(policy.getPolicyNo());
					if (policyObj != null) {
						if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
							builder = BancsSevice.getInstance().getJetPrivillagePolicyDetail();
						}else{
							builder = PremiaService.getInstance().getJetPrivillagePolicyDetail();
						}
					}else{
						builder = PremiaService.getInstance().getJetPrivillagePolicyDetail();
					}
				}
				//Bancs Changes End
				//Builder builder = PremiaService.getInstance().getJetPrivillagePolicyDetail();
				policyDetails= builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policy.getPolicyNo()+ "\"");
			} catch(Exception e) {
				log.error("ERROR WHILE FETCHING POLCIY DETAILS FROM PREMIA ---" + policyNumber);
				e.printStackTrace();
			}
		
			return policyDetails;
		}
		
		@SuppressWarnings({ "static-access", "deprecation" })
		private Policy saveMasterPolicyForJetPrivillage(PremPolicyDetails premPolicyDetails) throws IllegalStateException, SecurityException, SystemException {
			try {
				Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
				if (null == policy) {

					 PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
					 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
					 Double totalAmount = 0d;
					 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
					 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
					 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
					 policy.setTotalPremium(totalAmount);
					 
					 try{
						 if(premPolicyDetails.getDeductiableAmt() != null){
							 policy.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getDeductiableAmt()).doubleValue());
						 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
					 
					 if(premPolicyDetails.getPolSysId() != null){
						 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
				
						 policy.setPolicySystemId(polSysId);
					 }

					 if(premPolicyDetails.getPolicyPlan() != null){
	 					 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
							 policy.setPolicyPlan("A");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
							 policy.setPolicyPlan("B");
	 					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)){
							 policy.setPolicyPlan("G");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
							 policy.setPolicyPlan("S");
						 }
						 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
							 policy.setPolicyPlan("B");
						 }						 
					 }
	
					 if(premPolicyDetails.getPolicyTerm() != null) {
	 					 if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
							 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
							 policy.setPolicyTerm(policyTerm);
						 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(2l);
						 }
							//Added for MED-PRD-083
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(1l);
					 }
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(1l);
						 }
					 }
					 
					 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
					 if(policyStrYear != null){
						 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
					 }
					 
					 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true))
					 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 
					 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)
						 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
					 
					 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true)
					 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
					 
					 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true)
					 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
					 
					 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true)
						 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
					 
					 if(null != getMasterByValue(premPolicyDetails.getLob()))
						 policy.setLobId(getMasterByValue((premPolicyDetails.getLob())).getKey());
					 
					 if(null != getMasterByValue(premPolicyDetails.getPolicyType()))
					 policy.setPolicyType(getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
					 
	 				 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
						 MastersValue schemeId = getMasterByValue(premPolicyDetails.getSchemeType());
						 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
					 }
					 
					//TODO:Get product type from premia 
//					 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//					 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
					 
					 Product productByProductCodeForPremia = getProductByProductCode(premPolicyDetails.getProductCode(),new Date(premPolicyDetails.getPolicyStartDate()));
					 if(productByProductCodeForPremia == null) {
						 return null;
					 }
					 policy.setProduct(productByProductCodeForPremia);
					 policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
					 
					 
					 List<PolicyEndorsementDetails> endorsementDetailsList = new ArrayList<PolicyEndorsementDetails>();
					 if(premPolicyDetails.getEndorsementDetails() != null){
					 endorsementDetailsList = premiaPolicyMapper .getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
					 }
					 
					 List<PreviousPolicy> previousPolicyList = new ArrayList<PreviousPolicy>();
				     if(premPolicyDetails.getPreviousPolicyDetails() != null){
					 previousPolicyList = premiaPolicyMapper.getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
				     }
					 //set Endrosement Date
				     if(premPolicyDetails.getEndorsementDetails() != null){
					 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
						 endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						 endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						 }
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
							 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
							 }
					 }
				     }
					 //Set previous policy Date
				     
				     
	 		        if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
			        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
	 		        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)){
			        	
			        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) { 
			     	    	
			        		 MastersValue productType = getMaster(ReferenceTable.FLOATER_POLICY);
			        		 policy.setProductType(productType);
			        		 
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null){
			        			 policy.setProduct(product);
			        		 }
			     	    } else {
			     	    	
			     	    	 MastersValue productType = getMasterValue(ReferenceTable.INDIVIDUAL_POLICY);
			        		 policy.setProductType(productType);
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null) {
			        			 policy.setProduct(product);
			        		 }
			     	    }
			        }
			        
			        if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
			        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
			        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
							if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
//								policyBankDetails.setEffectiveFrom(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveFrom()));
							}
	 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
//								policyBankDetails.setEffectiveTo(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveTo()));
							}
						}
			        	policy.setPolicyBankDetails(bankDetailsFromPremia);
			        }

			        /*if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
			        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
			        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
			        		PolicyRiskCover riskCover = new PolicyRiskCover();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
			        }*/
			        
			       /* Below code for product code - MED-PRD-073*/
			        if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))) {
			        List<PremPolicyCoverDetails> premPolicyCoverDetails = premPolicyDetails.getPremPolicyCoverDetails();

					        if(premPolicyCoverDetails != null && ! premPolicyCoverDetails.isEmpty()){
					        	List<PolicyCoverDetails> policyRiskCoverList = new ArrayList<PolicyCoverDetails>();
					        	for (PremPolicyCoverDetails premPolicyRiskCover2 : premPolicyCoverDetails) {
					        		PolicyCoverDetails riskCover = new PolicyCoverDetails();
					        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
					        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
					        		if(premPolicyRiskCover2.getSumInsured() != null){
					        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
					        		}
					        		if(premPolicyRiskCover2.getRiskId() != null){
					        			riskCover.setRiskId(Long.valueOf(premPolicyRiskCover2.getRiskId()));
					        		}
					        		policyRiskCoverList.add(riskCover);
								}
					        	policy.setPolicyCoverDetails(policyRiskCoverList);
					        }
					        
					        if(premPolicyDetails.getGmcAilmentLimit() != null){
								List<MasAilmentLimit> ailmentLimit = premiaPolicyMapper.getAilmentLimit(premPolicyDetails.getGmcAilmentLimit());
								policy.setAilmentDetails(ailmentLimit);
							}
							
							if(premPolicyDetails.getGmcDeliveryLimit() != null){
								List<MasDeliveryExpLimit> deliveryLimit = premiaPolicyMapper.getDeliveryExpLimits(premPolicyDetails.getGmcDeliveryLimit());
								policy.setDeliveryExpLimit(deliveryLimit);
							}
							
							if(premPolicyDetails.getGmcCopayLimit() != null){
								List<MasCopayLimit> copayLimit = premiaPolicyMapper.getCopayLimit(premPolicyDetails.getGmcCopayLimit());
								policy.setCopayLimit(copayLimit);
							}
							if(premPolicyDetails.getGmcPrePostHospLimit() != null){
								List<MasPrePostHospLimit> prePostLimit = premiaPolicyMapper.getPrepostLimit(premPolicyDetails.getGmcPrePostHospLimit());
								policy.setPrePostLimit(prePostLimit);
							}
							
							if(premPolicyDetails.getGmcRoomRentLimit() != null){
								List<MasRoomRentLimit> roomRentList = premiaPolicyMapper.getRoomRentLimit(premPolicyDetails.getGmcRoomRentLimit());
								policy.setRoomRentLimit(roomRentList);
							}
							
							List<GpaBenefitDetails> benefitDetailsList = new ArrayList<GpaBenefitDetails>();
							
							List<PremGmcBenefitDetails> gmcPolicyConditions = premPolicyDetails.getGmcPolicyConditions();
							if(gmcPolicyConditions != null){
								for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
									GpaBenefitDetails benefitDetails = new GpaBenefitDetails();
									benefitDetails.setBenefitCode(premGmcBenefitDetails.getConditionCode());
									benefitDetails.setBenefitDescription(premGmcBenefitDetails.getConditionDesc());
									benefitDetails.setBenefitLongDescription(premGmcBenefitDetails.getConditionLongDesc());
									benefitDetailsList.add(benefitDetails);
								}
							}
							policy.setGpaBenefitDetails(benefitDetailsList);
			        }
			        
			        if(premPolicyDetails.getPolicyZone() != null){
						 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
					}
			        
			      //added for saving UIN in policy table
			        if(premPolicyDetails.getPremiaUINCode() != null && !premPolicyDetails.getPremiaUINCode().isEmpty()) {
			        	policy.setPremiaUINCode(premPolicyDetails.getPremiaUINCode());
			        }
			      //added for corpBufferLimit New column insert
					if(premPolicyDetails.getGmcCorpBufferLimit() != null){
						List<GmcCoorporateBufferLimit> corpBufferLimitList = premiaPolicyMapper.getCorpBufferLimit(premPolicyDetails.getGmcCorpBufferLimit());
						policy.setGmcCorpBufferLimit(corpBufferLimitList);
					}
					
					if(premPolicyDetails.getNacbBuffer() != null && !premPolicyDetails.getNacbBuffer().isEmpty()) {
						policy.setNacbBufferAmt(Double.valueOf(premPolicyDetails.getNacbBuffer()));
					}
					if(premPolicyDetails.getVintageBuffer() != null && !premPolicyDetails.getVintageBuffer().isEmpty()) {
						policy.setWintageBufferAmt(Double.valueOf(premPolicyDetails.getVintageBuffer()));
					}
					//added for topup policy data saving process
					if(premPolicyDetails.getTopUpPolicyNo() != null && !premPolicyDetails.getTopUpPolicyNo().isEmpty()) {
						policy.setTopupPolicyNo(premPolicyDetails.getTopUpPolicyNo());
					}
			        createGMCproduct(premPolicyDetails, policy, premiaPolicyMapper,false);
			        // gpa cover details
			        createGPAproduct(premPolicyDetails, policy, premiaPolicyMapper,false);
			        
			        if(premPolicyDetails.getProductCode() != null && ! ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(premPolicyDetails.getProductCode()) && 
			        		! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE) &&
			        		! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)){
			        	
			        	
			        	createPolicy(policy, previousPolicyList, endorsementDetailsList);
			        }

					policy = getPolicy(premPolicyDetails.getPolicyNo());
				} 
				return policy;
			}
			catch(Exception e) {
				log.error("___________________  POLICY ERROR WHILE SAVING THE DATA ___________________" + premPolicyDetails.getPolicyNo());
				e.printStackTrace();
				utx.rollback();
				log.error("**************ERROR IN POPULATE POLICY DATA METHOD*********" + e.getMessage());
				return null;
			}
		}
		
   public void saveEffectiveFromDate(PremPolicyDetails policyDetails){
	   try {
		utx.begin();
	} catch (NotSupportedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (SystemException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   List<PremInsuredDetails> insuredDetails = policyDetails.getGmcInsuredDetails();
	   
	   for (PremInsuredDetails premInsuredDetails : insuredDetails) {
		   
		   List<PremDependentInsuredDetails> dependentDetailsList = premInsuredDetails.getDependentDetailsList();
		   for (PremDependentInsuredDetails premDependentInsuredDetails : dependentDetailsList) {
			   Insured insuredByPolicyAndInsuredName = getInsuredByPolicyAndInsuredName(policyDetails.getPolicyNo(),Long.valueOf(premDependentInsuredDetails.getRiskSysId()));
			   /*if(premDependentInsuredDetails.getEffectiveFromDate() != null && ! premDependentInsuredDetails.getEffectiveFromDate().isEmpty()){
				   //insuredByPolicyAndInsuredName.setEffectiveFromDate(new Date(premDependentInsuredDetails.getEffectiveFromDate()));
				   Date formatPremiaDate = SHAUtils.formatTimeFromString(premDependentInsuredDetails.getEffectiveFromDate());
				   insuredByPolicyAndInsuredName.setEffectiveFromDate(formatPremiaDate);
				}
				if(premDependentInsuredDetails.getEffectiveToDate() != null && ! premDependentInsuredDetails.getEffectiveToDate().isEmpty()){
					//insuredByPolicyAndInsuredName.setEffectiveToDate(new Date(premDependentInsuredDetails.getEffectiveToDate()));
					   Date formatPremiaDate = SHAUtils.formatTimeFromString(premDependentInsuredDetails.getEffectiveToDate());
					   insuredByPolicyAndInsuredName.setEffectiveToDate(formatPremiaDate);
				}*/
			   if(insuredByPolicyAndInsuredName != null){
				   insuredByPolicyAndInsuredName.setInsuredEmailId(premDependentInsuredDetails.getMailId());
			   }

				entityManager.merge(insuredByPolicyAndInsuredName);
				entityManager.flush();
		   }
		   
		   Insured mainInsured = getInsuredByPolicyAndInsuredName(policyDetails.getPolicyNo(),Long.valueOf(premInsuredDetails.getRiskSysId()));
		   
		   /*if(premInsuredDetails.getEffectiveFromDate() != null && !premInsuredDetails.getEffectiveFromDate().isEmpty()){
			   //insuredByPolicyAndInsuredName.setEffectiveFromDate(new Date(premDependentInsuredDetails.getEffectiveFromDate()));
			   Date formatPremiaDate = SHAUtils.formatTimeFromString(premInsuredDetails.getEffectiveFromDate());
			   mainInsured.setEffectiveFromDate(formatPremiaDate);
			}
			if(premInsuredDetails.getEffectiveToDate() != null && !premInsuredDetails.getEffectiveToDate().isEmpty()){
//				mainInsured.setEffectiveToDate(new Date(premInsuredDetails.getEffectiveToDate()));
				  Date formatPremiaDate = SHAUtils.formatTimeFromString(premInsuredDetails.getEffectiveToDate());
				   mainInsured.setEffectiveToDate(formatPremiaDate);
			}*/
		   if(premInsuredDetails.getEmployeeId() != null){
		   
			   if(premInsuredDetails.getAddress1()!= null){
				   mainInsured.setAddress1(premInsuredDetails.getAddress1());
			   }
			   if(premInsuredDetails.getAddress2()!= null){
				   mainInsured.setAddress2(premInsuredDetails.getAddress2());
			   }
			   if(premInsuredDetails.getAddress3()!= null){
				   mainInsured.setAddress3(premInsuredDetails.getAddress3());
			   }
			   mainInsured.setInsuredEmployeeId(premInsuredDetails.getEmployeeId());
			   mainInsured.setInsuredEmailId(premInsuredDetails.getMailId());
			   if(premInsuredDetails.getInsuredEmail() != null && 
					   (premInsuredDetails.getMailId() == null || premInsuredDetails.getMailId() != null && premInsuredDetails.getMailId().isEmpty())){
				   mainInsured.setInsuredEmailId(premInsuredDetails.getInsuredEmail());
			   }
				entityManager.merge(mainInsured);
				entityManager.flush();
		   }else{
			   if(premInsuredDetails.getAddress1()!= null){
				   mainInsured.setAddress1(premInsuredDetails.getAddress1());
			   }
			   if(premInsuredDetails.getAddress2()!= null){
				   mainInsured.setAddress2(premInsuredDetails.getAddress2());
			   }
			   if(premInsuredDetails.getAddress3()!= null){
				   mainInsured.setAddress3(premInsuredDetails.getAddress3());
			   }
			   entityManager.merge(mainInsured);
			   entityManager.flush();
		   }
	   }
	   
	   try {
		utx.commit();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicMixedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicRollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
   
   public void savePortedPolicy(PremPolicyDetails premPolicyDetails){
	   
	   try{
		   utx.begin();
		   Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
		   if(policy != null){
			   policy.setPortedPolicy(premPolicyDetails.getPortedYN());
			   entityManager.merge(policy);
			   entityManager.flush();
		   }
		   utx.commit();
	   }catch(Exception e){
		   
	   }
	   
   }
   
   private Insured getInsuredByPolicyAndInsuredName(String policyNo , Long insuredId) {
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
   
   public void updateGrossPremiumAmtForPolicyNumber(){
	   log.info("@@@@@@@@@ ICR BATCH GMC_CLAIM_RATIO_COLOR STARTED@@@@@@@@@@-----> "+ System.currentTimeMillis());
		List<IncurredClaimRatio> getpolicyNumberForIncurredClaimRatio = getpolicyNumberForIncurredClaimRatio();
		log.info("@@@@@@@@@ ICR BATCH GMC_CLAIM_RATIO_COLOR ENDED@@@@@@@@@@-----> " + System.currentTimeMillis());
		for (IncurredClaimRatio incurredClaimRatio : getpolicyNumberForIncurredClaimRatio) {
			try{
			log.info("@@@@@@@@@ ICR BATCH GROSS PREMIUM WEB SERVICE CALL STARTED@@@@@@@@@@-----> "+ System.currentTimeMillis());
			utx.begin();
			PremPolicyDetails fetchGmcPolicyDetailsFromPremia = fetchGmcPolicyDetailsFromPremia(incurredClaimRatio.getPolicyNumber(), "");
			//PremPolicyDetails fetchGmcPolicyDetailsFromPremia = fetchPolicyDetailsFromPremia(incurredClaimRatio.getPolicyNumber());
			
			if(fetchGmcPolicyDetailsFromPremia != null){
				log.info("@@@@@@@@@ ICR BATCH GROSS PREMIUM UPDATE@@@@@@@@@@-----> "+fetchGmcPolicyDetailsFromPremia.getGrossPremium());
				//Commented for gross premium as decimal
				//incurredClaimRatio.setGrossPremium(Long.valueOf(fetchGmcPolicyDetailsFromPremia.getGrossPremium()));
				incurredClaimRatio.setGrossPremium(Math.round(Double.valueOf(fetchGmcPolicyDetailsFromPremia.getGrossPremium())));
				entityManager.merge(incurredClaimRatio);
				entityManager.flush();
				
				
				Policy policy = getPolicy(fetchGmcPolicyDetailsFromPremia.getPolicyNo());
				policy.setGrossPremium(Double.valueOf(fetchGmcPolicyDetailsFromPremia.getGrossPremium()));
				entityManager.merge(policy);
				entityManager.flush();
				utx.commit();
			 }
			log.info("@@@@@@@@@ ICR BATCH GROSS PREMIUM WEB SERVICE CALL ENDED@@@@@@@@@@-----> "+ System.currentTimeMillis());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				try {
					log.info("@@@@@@@@@ ICR BATCH GROSS PREMIUM CALL ROLL BACKED@@@@@@@@@-----> ");
					utx.rollback();
				}  catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
				}
			}
			
		}
		log.info("@@@@@@@@@ ICR BATCH GMC_CLAIM_RATIO_COLOR SKIPPED@@@@@@@@@@-----> " + getpolicyNumberForIncurredClaimRatio.size());
	}
	
	public List<IncurredClaimRatio> getpolicyNumberForIncurredClaimRatio(){
		 Query query = entityManager.createNamedQuery("IncurredClaimRatio.findAll");
		    List<IncurredClaimRatio> listOfIncurredRatio = (List<IncurredClaimRatio>)query.getResultList();
		    log.info("@@@@@@@@@ ICR BATCH GMC_CLAIM_RATIO_COLOR LIST OF INCURRED RATIO @@@@@@@@@@-----> " + (listOfIncurredRatio != null ? listOfIncurredRatio.size() : "LIST NULL"));
		    return listOfIncurredRatio;
	}
	
	public MasCpuLimit getMasCpuLimit(Long cpuId,String polType){
		try{
		Query findCpuCode = entityManager.createNamedQuery("MasCpuLimit.findByCode").setParameter("cpuCode", cpuId);
		findCpuCode.setParameter("polType", polType);
		//findCpuCode.setParameter("activeStatus", 1l);
		List<MasCpuLimit> cpuLimit = (List<MasCpuLimit>)findCpuCode.getResultList();
		
		if(cpuLimit != null && ! cpuLimit.isEmpty()){
			return cpuLimit.get(0);
		}
		
		}catch(Exception e){
				
		}
		return null;
}
	
   public Product getProductByProductCode(String productCode,Date policyStartDate){
		
		 Product product = null;
		 Query query = entityManager.createNamedQuery("Product.findByCodeAndDate");
		 query = query.setParameter("productCode", productCode);
		 query = query.setParameter("inceptionDate", policyStartDate);
		 List resultList = query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 product = (Product) query.getResultList().get(0);	
		 } else {
			 System.out.println("This product is not available in Galaxy------------->" + productCode + query);
		 }
				
		 return product;
	}
   
   public String getPaayasCpuCodeByPolicy(String policyNumber){
		
		 PaayasPolicy paayasPolicy = null;
		 Query query = entityManager.createNamedQuery("PaayasPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<PaayasPolicy> resultList = (List<PaayasPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
   
   
   public String getJioPolicyCpuCode(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("StarJioPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarJioPolicy> resultList = (List<StarJioPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
   
   public String getGpaPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("GpaPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<GpaPolicy> resultList = (List<GpaPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
   
   public GalaxyIntimation insertIntimationFromwebService(GalaxyIntimation intimation){
	   try {
		utx.begin();
		 entityManager.persist(intimation);
		  entityManager.flush();
		  entityManager.refresh(intimation);
	} catch (NotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		utx.commit();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicMixedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicRollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   return intimation;
	  
   }
   
   public TmpHospital insertTmpHospital(TmpHospital tmpHospital){
	   try {
		utx.begin();
		 entityManager.persist(tmpHospital);
		  entityManager.flush();
		  entityManager.refresh(tmpHospital);
	} catch (NotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		utx.commit();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicMixedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicRollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   return tmpHospital;
	  
   }
   
   @SuppressWarnings("unchecked")
	public State getState(String argState) {
		Query query = entityManager.createNamedQuery("State.findByName");
		query.setParameter("stateName", "%" + argState.toLowerCase() + "%");
		List<State> stList = query.getResultList();
		if(null != stList && !stList.isEmpty()) {
			return stList.get(0);
		}
		return null;
	}
   
   @SuppressWarnings("unchecked")
  	public CityTownVillage getCity(String argCity) {
  		Query query = entityManager.createNamedQuery("CityTownVillage.findByCity");
  		query.setParameter("cityName", "%" + argCity.toLowerCase() + "%");
  		List<CityTownVillage> stList = query.getResultList();
  		if(null != stList && !stList.isEmpty()) {
  			return stList.get(0);
  		}
  		return null;
  	}
   
   
   
   public void updateIntimationFromwebService(GalaxyIntimation intimation){
	   try {
		utx.begin();
		entityManager.merge(intimation);
		entityManager.flush();
	} catch (NotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		utx.commit();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicMixedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (HeuristicRollbackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
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
   
   private List<Insured> getInsuredByPolicyAndRiskId(String policyNo , Long insuredId) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredId");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredId)
		query = query.setParameter("insuredId", insuredId);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		return insuredList;
	}
  
   public void saveManualPrevPortabilityPolicy(PremPolicyDetails premPolicyDetails) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		
		utx.begin();
		PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		List<PremInsuredDetails> premiaInsuredList = premPolicyDetails.getInsuredDetails();
		if(premiaInsuredList != null && !premiaInsuredList.isEmpty()){
		
		Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
		 
		for (PremInsuredDetails premInsuredDetails : premiaInsuredList){
		
			List<PremPortabilityPrevPolicyDetails> premPortablitiPrevPolicyDetails = premInsuredDetails.getPortabilityPrevPolicyDetails();
			 
			 if(premPortablitiPrevPolicyDetails != null){
				 List<PortabilityPreviousPolicy> portablityPrevPolicyList = premiaPolicyMapper.getPortablityPrevPolicyList(premPortablitiPrevPolicyDetails);
				 for (PortabilityPreviousPolicy portablityPrevPolicy : portablityPrevPolicyList) {
					
					 try{
						 
						 if(portablityPrevPolicy.getPolicyStrFmDt() != null && ! portablityPrevPolicy.getPolicyStrFmDt().isEmpty()){
							 portablityPrevPolicy.setPolicyFmDt(SHAUtils.formatTimeFromString(portablityPrevPolicy.getPolicyStrFmDt()));
						 }
						 if(portablityPrevPolicy.getPolicyStrToDt() != null && ! portablityPrevPolicy.getPolicyStrToDt().isEmpty()){
							 portablityPrevPolicy.setPolicyToDt(SHAUtils.formatTimeFromString(portablityPrevPolicy.getPolicyStrToDt()));
						 }
						 portablityPrevPolicy.setCurrentPolicyNumber(policy.getPolicyNumber());
						 portablityPrevPolicy.setInsuredName(premInsuredDetails.getInsuredName());
						 portablityPrevPolicy.setActiveStatus(1l);
						 
//						 Boolean isAvailable = checkDataAvailable(portablityPrevPolicy, premInsuredDetails.getInsuredName());	 
//						 
//						 if(portablityPrevPolicy.getPolicyNumber() != null && !isAvailable){
							
							portablityPrevPolicy.setCreatedDate(new Date()); 
							entityManager.persist(portablityPrevPolicy);
							entityManager.flush();
//						 }
//						 else if(portablityPrevPolicy.getKey() != null && portablityPrevPolicy.getPolicyNumber() != null && isAvailable){
//							
//							 portablityPrevPolicy.setModifiedDate(new Date());
//							entityManager.merge(portablityPrevPolicy);
//							entityManager.flush();
//						 }
					}
					catch(Exception e){
						 e.printStackTrace();
					}
				}
				
			}			 
		  }	
		}	
		utx.commit();
   }
   
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void saveInsuredDetails(Policy policy, Policy existingPolicy){
		
		if(null != policy.getInsured() && !policy.getInsured().isEmpty()) {
			
			for (Insured  objInsured : policy.getInsured())  {
				
				Insured jetMainMember = null;
				
				Insured insuredByPolicyAndRiskId = null;
				
				if(policy.getPolicySource() != null && policy.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
					insuredByPolicyAndRiskId = getBaNCSInsuredByPolicyAndRiskIdWithLobFlag(policy.getPolicyNumber(), objInsured.getSourceRiskId(),objInsured.getLopFlag());
				}else{
					insuredByPolicyAndRiskId = getInsuredByPolicyAndRiskIdWithLobFlag(policy.getPolicyNumber(), objInsured.getInsuredId(),objInsured.getLopFlag());
				}
				
				if(insuredByPolicyAndRiskId == null){
				
				objInsured.setPolicy(existingPolicy);
				/*if(existingPolicy.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(existingPolicy.getDeductibleAmount());
				}*/
				
				//IMSSUPPOR-27387 - Key added for IMSSUPPOR-29029 - 17-07-2019
				if((existingPolicy.getProductType() == null) || (existingPolicy.getProductType() != null && existingPolicy.getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY))){
					if(existingPolicy.getDeductibleAmount() != null){
						objInsured.setDeductibleAmount(existingPolicy.getDeductibleAmount());
					}
				}
				
				objInsured.setLopFlag("H");
				
				if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
					objInsured.setInsuredPinCode(null);
				}
				//GMC Hospital Cash
				if(objInsured.getGhcPolicyType() !=null && !objInsured.getGhcPolicyType().isEmpty())
				{
					String ghcType = objInsured.getGhcPolicyType();
					objInsured.setGhcPolicyType(ghcType);
				}
				
				if(objInsured.getGhcScheme() !=null && !objInsured.getGhcScheme().isEmpty())
				{
					String ghcScheme = objInsured.getGhcScheme();
					objInsured.setGhcScheme(ghcScheme);
				}
				/*Below Code for product jet group - MED-PRD-073*/
				if(policy.getProduct().getCode() != null && (policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
						|| policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
						|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
						|| policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))) {
					if(jetMainMember == null) {
						jetMainMember = getJetMainMemberInsured(policy);
					}
					if(objInsured.getMainMember() != null && objInsured.getMainMember().equalsIgnoreCase(SHAConstants.N_FLAG)){
						if(jetMainMember != null) {
							objInsured.setDependentRiskId(jetMainMember.getInsuredId() != null ? jetMainMember.getInsuredId() : null);
						}
					}
					if(objInsured.getDeductibleAmount() != null){
						objInsured.setDeductibleAmount(objInsured.getDeductibleAmount());
					}
				}
				
				if(objInsured != null){
					entityManager.merge(objInsured);
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
						//added for CR ped effective date save in PED table GLX0132
						if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//							Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
//							if(fromDate != null){
							insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));
//							}
						}
						if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//							Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
//							if(fromDate != null){
							insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));
//							}
						}
						if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
							insuredPed.setPedType(insuredPed.getPedType());
						}
						if(insuredPed.getKey() == null){
							entityManager.persist(insuredPed);
							entityManager.flush();
						}
					}
					
				}
				
				/*JetContinuityBenefits*/
				if(objInsured.getGmcContBenefitDtls() != null && !objInsured.getGmcContBenefitDtls().isEmpty()){
					for (GmcContinuityBenefit continuityBenefit : objInsured.getGmcContBenefitDtls()) {
						continuityBenefit.setPolicy(objInsured.getPolicy());
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
				
				if(policy.getProduct().getCode() != null && !policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT) && !policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
						&& !policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)){
					
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
				}
				
				if(objInsured.getPortablityPolicy() != null && ! objInsured.getPortablityPolicy().isEmpty()){
					for (PortablityPolicy portablityPolicy : objInsured.getPortablityPolicy()) {
							portablityPolicy.setActiveStatus(1l);
							portablityPolicy.setInsuredName(objInsured.getInsuredName());
						if(portablityPolicy.getKey() == null && portablityPolicy.getPolicyNumber() != null){
							portablityPolicy.setCurrentPolicyNumber(existingPolicy.getPolicyNumber());
							entityManager.persist(portablityPolicy);
							entityManager.flush();
						}else if(portablityPolicy.getKey() != null && portablityPolicy.getPolicyNumber() != null) {
							entityManager.merge(portablityPolicy);
							entityManager.flush();
						}
					}
				}
			  }
			}
		}
		
		if(policy.getInsuredPA() != null && ! policy.getInsuredPA().isEmpty()){
			for (Insured insuredPA : policy.getInsuredPA()) {
				
				
				Insured insuredByPolicyAndRiskId = null;
				
				if(policy.getPolicySource() != null && policy.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
					insuredByPolicyAndRiskId = getBaNCSInsuredByPolicyAndRiskIdWithLobFlag(policy.getPolicyNumber(), insuredPA.getSourceRiskId(),insuredPA.getLopFlag());
				}else{
					insuredByPolicyAndRiskId = getInsuredByPolicyAndRiskIdWithLobFlag(policy.getPolicyNumber(), insuredPA.getInsuredId(),insuredPA.getLopFlag());
				}
				if(insuredByPolicyAndRiskId == null){
				insuredPA.setPolicy(existingPolicy);
				if(existingPolicy.getDeductibleAmount() != null){
					insuredPA.setDeductibleAmount(existingPolicy.getDeductibleAmount());
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
			

		}
		
	}
   
   private Insured getInsuredByPolicyAndRiskIdWithLobFlag(String policyNo , Long insuredId,String lobFlag) {
	   Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		query = query.setParameter("insuredId", insuredId);
		query = query.setParameter("lobFlag", lobFlag);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			return insuredList.get(0);
		} 
		
		return null;
	}
   
	/**
	 * The below method is used to fetch Insured details
	 * from IMS_CLS_INSURED Table. This table is a new table
	 * which was introduced during policy table change requirement.
	 * 
	 * */
	public Insured getCLSInsured(Long key) {

		Query query = entityManager
				.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		List<Insured> insuredList = (List<Insured>)query.getResultList();
		//List<Insured> insuredList = new ArrayList<Insured>(); - INSURED KEY MISMATCH ISSUE FIXED - 16-MAY-2019 - DINESH.M
		if(insuredList != null && ! insuredList.isEmpty()){
			return insuredList.get(0);
		}
		return null;

	}
	
	public Long getTataPolicy(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return tataPolicy.get(0).getCpuCode();
		    }
		    return null;
	}
	
	public Boolean isTataPolicy(String policyNumber){
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return true;
		    }
		    return false;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updatePolicyDetailsAfterEndorsement(Policy policy,List<PreviousPolicy> previousPolicyList, List<PolicyEndorsementDetails> policyEndorsementList) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
	{
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		if (null != policy) {
			if(policy2 != null){
				policy.setKey(policy2.getKey());
			}
			entityManager.merge(policy);
			entityManager.flush();
			policy2 = policy;
		}	
		List<Insured> existingInsuredList = getInsuredListByPolicyNo(policy2.getPolicyNumber());
		Map<Long,Insured> healthInsuredMap = new WeakHashMap<Long, Insured>();
		Map<Long,Insured> paInsuredMap = new WeakHashMap<Long, Insured>();
		for (Insured insured : existingInsuredList) {
			if(insured.getLopFlag() == null || SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
				healthInsuredMap.put(insured.getInsuredId(), insured);
			}else if(insured.getLopFlag() != null && SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(insured.getLopFlag())){
				paInsuredMap.put(insured.getInsuredId(), insured);
			}
		}
		if(null != policy.getInsured() && !policy.getInsured().isEmpty()) {
			updateInsuredDetailsForEndorsement(policy, policy2, healthInsuredMap);
		}
		
		if (null != previousPolicyList && !previousPolicyList.isEmpty()) {

			// Query query =
			// entityManager.createNamedQuery("PreviousPolicy.findByCurrentPolicy").setParameter("policyNumber",
			// policy.getPolicyNumber());
			// List<PreviousPolicy> existingList = query.getResultList();
			for (PreviousPolicy prevPolicy : previousPolicyList) {
			}				
		}
		
		if(policy.getInsuredPA() != null && ! policy.getInsuredPA().isEmpty()){
			updatePAInsuredDetailsForEndorsement(policy, policy2,paInsuredMap);
		 }
		
		if(healthInsuredMap != null && ! healthInsuredMap.isEmpty()){
			Set<Entry<Long, Insured>> entrySet = healthInsuredMap.entrySet();
			for (Map.Entry<Long, Insured> entry : entrySet)  {
	               Insured insured = (Insured)entry.getValue();
	               insured.setDeletedFlag("Y");
	               entityManager.merge(insured);
	               entityManager.flush();
		      }
		}
		
		if(paInsuredMap != null && ! paInsuredMap.isEmpty()){
			Set<Entry<Long, Insured>> entrySet = paInsuredMap.entrySet();
			for (Map.Entry<Long, Insured> entry : entrySet)  {
	               Insured insured = (Insured)entry.getValue();
	               insured.setDeletedFlag("Y");
	               entityManager.merge(insured);
	               entityManager.flush();
		      }
		}
		
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
			List<PolicyRiskCover> riskCoverByPolicy = getRiskCoverByPolicy(policy.getKey());
			Map<String, PolicyRiskCover> riskCoverMap = new WeakHashMap<String, PolicyRiskCover>();
			for (PolicyRiskCover policyRiskCover : riskCoverByPolicy) {
				riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
			}
			for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
				PolicyRiskCover policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
				if(policyRiskCover2 != null){
					policyRiskCover.setKey(policyRiskCover2.getKey());
				}
				policyRiskCover.setPolicyKey(policy.getKey());
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
			List<PolicyCoverDetails> policyCoverDetails2 = getPolicyCoverDetails(policy.getKey());
			Map<String, PolicyCoverDetails> riskCoverMap = new WeakHashMap<String, PolicyCoverDetails>();
			for (PolicyCoverDetails policyRiskCover : policyCoverDetails2) {
				riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
			}
			for (PolicyCoverDetails policyRiskCover : policyCoverDetails) {
				PolicyCoverDetails policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
				if(policyRiskCover2 != null){
					policyRiskCover.setKey(policyRiskCover2.getKey());
				}
				policyRiskCover.setPolicyKey(policy.getKey());
				if(policyRiskCover.getKey() == null){
					entityManager.persist(policyRiskCover);
					entityManager.flush();
				} else {
					entityManager.merge(policyRiskCover);
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
			List<PolicyEndorsementDetails> policyEndorsementList2 = getPolicyEndorsementList(policy2.getPolicyNumber());
			Map<String, PolicyEndorsementDetails> endorsementMap = new WeakHashMap<String, PolicyEndorsementDetails>();
			for (PolicyEndorsementDetails policyEndorsementDetails : policyEndorsementList2) {
				endorsementMap.put(policyEndorsementDetails.getEndorsementNumber(), policyEndorsementDetails);
			}
			for (PolicyEndorsementDetails policyEndorsementDetails : policyEndorsementList) {
				PolicyEndorsementDetails policyEndorsementDetails2 = endorsementMap.get(policyEndorsementDetails.getEndorsementNumber());
				if(policyEndorsementDetails2 != null){
					policyEndorsementDetails.setEndorsementKey(policyEndorsementDetails2.getEndorsementKey());
					endorsementMap.remove(policyEndorsementDetails2);
				}
				String endorsementText = policyEndorsementDetails.getEndorsementText();
//					if(endorsementText != null){
//						endorsementText = endorsementText.substring(0, 900);
//						policyEndorsementDetails.setEndorsementText(endorsementText);
//					}
				policyEndorsementDetails.setPolicy(policy2);
				if(policyEndorsementDetails.getEndorsementKey() != null){
					entityManager.merge(policyEndorsementDetails);
				}else{
					entityManager.persist(policyEndorsementDetails);
				}
				entityManager.flush();
			}
			
			if(endorsementMap != null && ! endorsementMap.isEmpty()){
				Set<Entry<String, PolicyEndorsementDetails>> entrySet = endorsementMap.entrySet();
				for (Map.Entry<String, PolicyEndorsementDetails> entry : entrySet)  {
					PolicyEndorsementDetails endorsement = (PolicyEndorsementDetails)entry.getValue();
					endorsement.setDeletedFlag("Y");
		            entityManager.merge(endorsement);
		            entityManager.flush();
			      }
			}
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateGMCPolicyEndorsement(Policy policy,List<Insured> insured) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
	{
		
		Policy policy2 = getPolicy(policy.getPolicyNumber());
		if (null != policy2) {
			if(policy2.getKey() != null){
				policy.setKey(policy2.getKey());
			}
			entityManager.merge(policy);
			entityManager.flush();
			
			List<PolicyRiskCover> policyRiskCoverDetails = policy.getPolicyRiskCoverDetails();
			if(policyRiskCoverDetails != null && !policyRiskCoverDetails.isEmpty()){
				List<PolicyRiskCover> riskCoverByPolicy = getRiskCoverByPolicy(policy.getKey());
				Map<String, PolicyRiskCover> riskCoverMap = new WeakHashMap<String, PolicyRiskCover>();
				for (PolicyRiskCover policyRiskCover : riskCoverByPolicy) {
					riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
				}
				for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
					PolicyRiskCover policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
					if(policyRiskCover2 != null){
						policyRiskCover.setKey(policyRiskCover2.getKey());
					}
					policyRiskCover.setPolicyKey(policy.getKey());
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
				List<PolicyCoverDetails> policyCoverDetails2 = getPolicyCoverDetails(policy.getKey());
				Map<String, PolicyCoverDetails> riskCoverMap = new WeakHashMap<String, PolicyCoverDetails>();
				for (PolicyCoverDetails policyRiskCover : policyCoverDetails2) {
					riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
				}
				for (PolicyCoverDetails policyRiskCover : policyCoverDetails) {
					PolicyCoverDetails policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
					if(policyRiskCover2 != null){
						policyRiskCover.setKey(policyRiskCover2.getKey());
					}
					policyRiskCover.setPolicyKey(policy.getKey());
					if(policyRiskCover.getKey() == null){
						entityManager.persist(policyRiskCover);
						entityManager.flush();
					} else {
						entityManager.merge(policyRiskCover);
						entityManager.flush();
					}
				}
			}
			
			List<MasAilmentLimit> ailmentDetails = policy.getAilmentDetails();
			if(ailmentDetails != null){
				for (MasAilmentLimit masAilmentLimit : ailmentDetails) {
					MasAilmentLimit masAilmentLimit2 = getMasAilmentLimit(policy.getKey(), masAilmentLimit.getSumInsuredFrom(), masAilmentLimit.getSumInsuredTo(), masAilmentLimit.getAilment());
					if(masAilmentLimit2 != null){
						masAilmentLimit.setKey(masAilmentLimit2.getKey());
					}
					masAilmentLimit.setPolicyKey(policy.getKey());
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
					if(copayLimit1.getCopayPercentage() != null && !copayLimit1.getCopayPercentage().equals(0.0d)){
						MasCopayLimit masCopayLimit = getMasCopayLimit(policy.getKey(), copayLimit1.getSumInsuredFrom(), copayLimit1.getSumInsuredTo(), copayLimit1.getClaimType());
						if(masCopayLimit != null){
							copayLimit1.setKey(masCopayLimit.getKey());
						}
						copayLimit1.setPolicyKey(policy.getKey());
						if(copayLimit1.getKey() == null){
							entityManager.persist(copayLimit1);
							entityManager.flush();
						}else{
							entityManager.merge(copayLimit1);
							entityManager.flush();
						}
					}
				}
			}
			List<MasDeliveryExpLimit> deliveryExpLimit = policy.getDeliveryExpLimit();
			if(deliveryExpLimit != null){
				for (MasDeliveryExpLimit masDeliveryLimit : deliveryExpLimit) {
					MasDeliveryExpLimit masDeliveryLimit2 = getMasDeliveryLimit(policy.getKey(), masDeliveryLimit.getSumInsuredFrom(), masDeliveryLimit.getSumInsuredTo(), masDeliveryLimit.getDeliveryType());
					if(masDeliveryLimit2 != null){
						masDeliveryLimit.setKey(masDeliveryLimit2.getKey());
					}
					masDeliveryLimit.setPolicyKey(policy.getKey());
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
					MasPrePostHospLimit masPrePostHospLimit = getMasPrePostHospLimit(policy.getKey(), masPrepostLimt.getSumInsuredFrom(), masPrepostLimt.getSumInsuredTo(), masPrepostLimt.getHospitalType());
					if(masPrePostHospLimit != null){
						masPrepostLimt.setKey(masPrePostHospLimit.getKey());
					}
					masPrepostLimt.setPolicyKey(policy.getKey());
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
					MasRoomRentLimit masRoomRentLimit2 = getMasRoomRentLimit(policy.getKey(), masRoomRentLimit.getSumInsuredFrom(), masRoomRentLimit.getSumInsuredTo(), masRoomRentLimit.getRoomType());
					if(masRoomRentLimit2 != null){
						masRoomRentLimit.setKey(masRoomRentLimit2.getKey());
					}
					//added for CR GLX2020069 by noufel
					if(masRoomRentLimit.getProportionateFlag() != null && !masRoomRentLimit.getProportionateFlag().isEmpty()){
						if(masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES) ||
								masRoomRentLimit.getProportionateFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
							masRoomRentLimit.setProportionateFlag(SHAConstants.YES_FLAG);
						}else {
							masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
						}
					}
					else{
						masRoomRentLimit.setProportionateFlag(SHAConstants.N_FLAG);
					}
					masRoomRentLimit.setPolicyKey(policy.getKey());
					if(masRoomRentLimit.getKey() == null){
						entityManager.persist(masRoomRentLimit);
						entityManager.flush();
					}else{
						entityManager.merge(masRoomRentLimit);
						entityManager.flush();
					}
				}
			}
			
			//added for GMC corpBufferUpdate through endorsement
          List<GmcCoorporateBufferLimit> corpBufferLimitList = policy.getGmcCorpBufferLimit();
			
			if(corpBufferLimitList != null){
				for (GmcCoorporateBufferLimit corpBufferLimit : corpBufferLimitList) {
					GmcCoorporateBufferLimit isCorpBufferLimit = getcorpBufferLimit(policy.getKey(), corpBufferLimit.getSumInsuredFrom(), corpBufferLimit.getSumInsuredTo());
					if(isCorpBufferLimit != null){
						corpBufferLimit.setKey(isCorpBufferLimit.getKey());
					}
					corpBufferLimit.setPolicyKey(policy.getKey());
					if(corpBufferLimit.getKey() == null){
						corpBufferLimit.setActiveStatus(1l);
						corpBufferLimit.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						corpBufferLimit.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
						entityManager.persist(corpBufferLimit);
						entityManager.flush();
					}else{
						corpBufferLimit.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
						corpBufferLimit.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						corpBufferLimit.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(corpBufferLimit);
						entityManager.flush();
					}
				}
			}
			policy2 = policy;
		
		
		
//		List<Insured> ListOfInsured = getInsuredListByPolicyNo(policy2.getPolicyNumber());
//		List<Long> riskNumberList = new ArrayList<Long>();
//		for (Insured insured2 : ListOfInsured) {
//			if(insured2.getLopFlag() == null || (insured2.getLopFlag() != null && insured2.getLopFlag().equalsIgnoreCase("H")))
//			riskNumberList.add(insured2.getInsuredId());
//		}
		
		if(null != insured && !insured.isEmpty()) {
			
			for (Insured  objInsured : insured)  {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), objInsured.getInsuredId());
				for (Insured insured2 : existingInsured) {
					if(insured2.getLopFlag() == null || (insured2.getLopFlag() != null && insured2.getLopFlag().equalsIgnoreCase("H"))){
						riskNumberList.add(insured2.getInsuredId());
						objInsured.setKey(insured2.getKey());
					}
			     }
				
				//if(!riskNumberList.contains(objInsured.getInsuredId())){
				
				objInsured.setPolicy(policy2);
				objInsured.setLopFlag("H");
				
				if(objInsured.getStrEffectivedFromDate() != null && !objInsured.getStrEffectivedFromDate().isEmpty()){
					Date fromDate = SHAUtils.formatTimeFromString(objInsured.getStrEffectivedFromDate());
					if(fromDate != null){
						objInsured.setEffectiveFromDate(fromDate);	
					}
				}
				if(objInsured.getStrEffectiveToDate() != null && !objInsured.getStrEffectiveToDate().isEmpty()){
					Date toDate = SHAUtils.formatTimeFromString(objInsured.getStrEffectiveToDate());
					if(toDate != null){
						objInsured.setEffectiveToDate(toDate);	
					}
				}
				
				if(policy2.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
				}
				if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
					objInsured.setInsuredPinCode(null);
				}
				//GMC Hospital Cash
				if(policy2.getPhcBenefitDays() != null){
					objInsured.setGhcDays(policy2.getPhcBenefitDays());
				}
				
				if(objInsured.getGhcPolicyType() !=null && !objInsured.getGhcPolicyType().isEmpty())
				{
					String ghcType = objInsured.getGhcPolicyType();
					objInsured.setGhcPolicyType(ghcType);
				}
				
				if(objInsured.getGhcScheme() !=null && !objInsured.getGhcScheme().isEmpty())
				{
					String ghcScheme = objInsured.getGhcScheme();
					objInsured.setGhcScheme(ghcScheme);
				}
				
				if(objInsured != null){
					if(objInsured.getKey() != null){
						entityManager.merge(objInsured);
						entityManager.flush();
					}else{
						entityManager.persist(objInsured);
						entityManager.flush();
					}
					
				}
			  }
			//}
		}
		
		List<Insured> dependentInsuredId = policy.getDependentInsuredId();
		if(dependentInsuredId != null && ! dependentInsuredId.isEmpty()){
			for (Insured insured2 : dependentInsuredId) {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), insured2.getInsuredId());
				for (Insured depInsured : existingInsured) {
					if(depInsured.getLopFlag() == null || (depInsured.getLopFlag() != null && depInsured.getLopFlag().equalsIgnoreCase("H"))){
						riskNumberList.add(depInsured.getInsuredId());
						insured2.setKey(depInsured.getKey());
					}
			     }
				
				//if(!riskNumberList.contains(insured2.getInsuredId())){
					insured2.setPolicy(policy2);
					insured2.setLopFlag("H");
					if(insured2.getStrEffectivedFromDate() != null && !insured2.getStrEffectivedFromDate().isEmpty()){
						Date fromDate = SHAUtils.formatTimeFromString(insured2.getStrEffectivedFromDate());
						if(fromDate != null){
							insured2.setEffectiveFromDate(fromDate);	
						}
					}
					if(insured2.getStrEffectiveToDate() != null && !insured2.getStrEffectiveToDate().isEmpty()){
						Date toDate = SHAUtils.formatTimeFromString(insured2.getStrEffectiveToDate());
						if(toDate != null){
							insured2.setEffectiveToDate(toDate);	
						}
					}
					
					if(insured2.getInsuredPinCode() != null && ! StringUtils.isNumeric(insured2.getInsuredPinCode())){
						insured2.setInsuredPinCode(null);
					}
					if(insured2 != null){
						if(insured2.getKey() != null){
							entityManager.merge(insured2);
							entityManager.flush();
						}else{
							entityManager.persist(insured2);
							entityManager.flush();
						}
					}
				//}
			}
		}
		
		if(policy.getInsuredPA() != null && ! policy.getInsuredPA().isEmpty()){
			for (Insured insuredPA : policy.getInsuredPA()) {
				
				List<Long> riskNumberList = new ArrayList<Long>();
				List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), insuredPA.getInsuredId());
				for (Insured depInsured : existingInsured) {
					if((depInsured.getLopFlag() != null && depInsured.getLopFlag().equalsIgnoreCase("P"))){
						riskNumberList.add(depInsured.getInsuredId());
						insuredPA.setKey(depInsured.getKey());
					}
			     }
					insuredPA.setPolicy(policy2);
					if(policy2.getDeductibleAmount() != null){
						insuredPA.setDeductibleAmount(policy2.getDeductibleAmount());
					}
					if(insuredPA != null){
						if(insuredPA.getKey() != null){
							entityManager.merge(insuredPA);
							entityManager.flush();
							
						}else{
							entityManager.persist(insuredPA);
							entityManager.flush();
							
						}
						
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
		
		List<GpaBenefitDetails> gpaBenefitDetails = policy.getGpaBenefitDetails();
		if(gpaBenefitDetails != null && !gpaBenefitDetails.isEmpty()){
			List<GpaBenefitDetails> policyBenefitDetails = getPolicyBenefitDetails(policy2.getKey());
			List<String> conditionCodeList = new ArrayList<String>();
			if(policyBenefitDetails != null && ! policyBenefitDetails.isEmpty()){
				for (GpaBenefitDetails existingBenefitDetail : policyBenefitDetails) {
					conditionCodeList.add(existingBenefitDetail.getBenefitCode());
				}
			}
			
			for (GpaBenefitDetails gpaBenefitDetails2 : gpaBenefitDetails) {
				gpaBenefitDetails2.setPolicyKey(policy2.getKey());
				if(! conditionCodeList.contains(gpaBenefitDetails2.getBenefitCode())){
					if(gpaBenefitDetails2.getKey() != null){
						entityManager.merge(gpaBenefitDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(gpaBenefitDetails2);
						entityManager.flush();
					}
				}
				
				
			}
		}
		
		
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
		}
		
	}

	private void updateInsuredDetailsForEndorsement(Policy policy,
			Policy policy2,Map<Long,Insured> insuredMap) {
		
		
		for (Insured  objInsured : policy.getInsured())  {
			Insured insured = insuredMap.get(objInsured.getInsuredId());
			//If insured details is already exist in DB, The below code is excuted.
			if(insured != null){
				objInsured.setKey(insured.getKey());
				//FIX FOR OVERRIDING DURING ENDORSEMENT BATCH
				if(insured.getInsuredRestoredSI() != null){
					objInsured.setInsuredRestoredSI(insured.getInsuredRestoredSI());
				}
				insuredMap.remove(objInsured.getInsuredId());
			}
			
			Insured jetMainMember = null;
			
			objInsured.setPolicy(policy2);
			/*if(policy2.getDeductibleAmount() != null){
				objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
			}*/
			
			//IMSSUPPOR-27387 - key added for IMSSUPPOR-29029 - 17-07-2019
			if((policy2.getProductType() == null) || (policy2.getProductType() != null && policy2.getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY))){
				if(policy2.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(policy2.getDeductibleAmount());
				}
			}
			
			objInsured.setLopFlag("H");
			
			if(objInsured.getInsuredPinCode() != null && ! StringUtils.isNumeric(objInsured.getInsuredPinCode())){
				objInsured.setInsuredPinCode(null);
			}
			//GMC Hospital Cash
			if(objInsured.getGhcPolicyType() !=null && !objInsured.getGhcPolicyType().isEmpty())
			{
				String ghcType = objInsured.getGhcPolicyType();
				objInsured.setGhcPolicyType(ghcType);
			}
			
			if(objInsured.getGhcScheme() !=null && !objInsured.getGhcScheme().isEmpty())
			{
				String ghcScheme = objInsured.getGhcScheme();
				objInsured.setGhcScheme(ghcScheme);
			}
			
			/*Below Code for product jet group - MED-PRD-073*/
			if(policy.getProduct().getCode() != null && (policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
					|| policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
					|| policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
					|| policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))){
				if(jetMainMember == null) {
					jetMainMember = getJetMainMemberInsured(policy);
				}
				if(objInsured.getMainMember() != null && objInsured.getMainMember().equalsIgnoreCase(SHAConstants.N_FLAG)){
					if(jetMainMember != null) {
						objInsured.setDependentRiskId(jetMainMember.getInsuredId() != null ? jetMainMember.getInsuredId() : null);
					}
				}
				if(objInsured.getDeductibleAmount() != null){
					objInsured.setDeductibleAmount(objInsured.getDeductibleAmount());
				}
			}
			
			if(objInsured != null){
				entityManager.merge(objInsured);
				entityManager.flush();
			}
			List<InsuredPedDetails> insuredKeyListByInsuredkey = getInsuredKeyListByInsuredkey(objInsured.getInsuredId());
			
			if(objInsured.getInsuredPedList() != null && !objInsured.getInsuredPedList().isEmpty()) {
				
				for(InsuredPedDetails insuredPed : objInsured.getInsuredPedList()){
					if(!SHAUtils.isPEDAvailable(insuredKeyListByInsuredkey, insuredPed.getPedCode())) {
					insuredPed.setInsuredKey(objInsured.getInsuredId());
					String pedDescription = insuredPed.getPedDescription();
					if(pedDescription != null && pedDescription.length() >300) {
						pedDescription = pedDescription.substring(0, 299);
						insuredPed.setPedDescription(pedDescription);
					}
					//added for CR ped effective date save in PED table GLX0132
					if(insuredPed.getStrEffectivedFromDate() != null && !insuredPed.getStrEffectivedFromDate().isEmpty()){
//						Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectivedFromDate());
//						if(fromDate != null){
						insuredPed.setPedEffectiveFromDate(new Date(insuredPed.getStrEffectivedFromDate()));	
//						}
					}
					if(insuredPed.getStrEffectiveToDate() != null && !insuredPed.getStrEffectiveToDate().isEmpty()){
//						Date fromDate = SHAUtils.formatTimeFromString(insuredPed.getStrEffectiveToDate());
//						if(fromDate != null){
						insuredPed.setPedEffectiveToDate(new Date(insuredPed.getStrEffectiveToDate()));
//						}
					}
					if(insuredPed.getPedType() != null && !insuredPed.getPedType().isEmpty()){
						insuredPed.setPedType(insuredPed.getPedType());
					}
					if(insuredPed.getKey() == null){
						entityManager.persist(insuredPed);
						entityManager.flush();
					}
					}else{
						//Update Insured Ped details
						InsuredPedDetails availableInsuredPed = SHAUtils.getAvailableInsuredPed(insuredKeyListByInsuredkey, insuredPed.getPedCode());
						if(availableInsuredPed != null){
							insuredPed.setKey(availableInsuredPed.getKey());
							entityManager.merge(insuredPed);
							entityManager.flush();
						}
					}
				}
				
			}else{
				//Delete insured ped details List
				for (InsuredPedDetails insuredPedDetails : insuredKeyListByInsuredkey) {
					if(insuredPedDetails.getPedCode() != null){
						insuredPedDetails.setDeletedFlag("Y");
						entityManager.merge(insuredPedDetails);
						entityManager.flush();
					}
				}
			}
			
			if(objInsured.getNomineeDetails() != null && ! objInsured.getNomineeDetails().isEmpty()) {
				List<NomineeDetails> nomineeDetails = getNomineeDetails(insured.getKey());
				Map<String, NomineeDetails> nomineeMap = new WeakHashMap<String, NomineeDetails>();
				for (NomineeDetails nomineeDetails2 : nomineeDetails) {
					nomineeMap.put(nomineeDetails2.getNomineeName(), nomineeDetails2);
				}
				for (NomineeDetails nomineeDetail : objInsured.getNomineeDetails()) {
					NomineeDetails nomineeDetails2 = nomineeMap.get(nomineeDetail);
					if(nomineeDetails2 != null){
						nomineeDetail.setKey(nomineeDetails2.getKey());
						nomineeMap.remove(nomineeDetails2);
					}
					nomineeDetail.setInsured(objInsured);
					if(nomineeDetail.getKey() == null){
						entityManager.persist(nomineeDetail);
						entityManager.flush();
					} else {
						entityManager.merge(nomineeDetail);
						entityManager.flush();
					}
				}
				
				if(!nomineeMap.isEmpty()){
					if(nomineeMap != null && ! nomineeMap.isEmpty()){
						Set<Entry<String, NomineeDetails>> entrySet = nomineeMap.entrySet();
						for (Map.Entry<String, NomineeDetails> entry : entrySet)  {
							NomineeDetails nomineeDetails1 = (NomineeDetails)entry.getValue();
							nomineeDetails1.setDeletedFlag("Y");
				            entityManager.merge(nomineeDetails1);
				            entityManager.flush();
					      }
					}
				}
				
			}
			
			if(policy.getProposerNomineeDetails() != null && ! policy.getProposerNomineeDetails().isEmpty()) {
				List<PolicyNominee> nomineeDetails = getPolicyNomineeList(policy.getKey());
				Map<String, PolicyNominee> nomineeMap = new WeakHashMap<String, PolicyNominee>();
				for (PolicyNominee nomineeDetails2 : nomineeDetails) {
					nomineeMap.put(nomineeDetails2.getNomineeName(), nomineeDetails2);
				}
				for (PolicyNominee nomineeDetail : policy.getProposerNomineeDetails()) {
					PolicyNominee nomineeDetails2 = nomineeMap.get(nomineeDetail.getNomineeName());
					if(nomineeDetails2 != null){
						nomineeDetail.setKey(nomineeDetails2.getKey());
						nomineeMap.remove(nomineeDetails2);
					}
					nomineeDetail.setPolicy(objInsured.getPolicy());
					nomineeDetail.setInsured(objInsured);
					if(nomineeDetail.getKey() == null){
						nomineeDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						nomineeDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						nomineeDetail.setActiveStatus(1);
						entityManager.persist(nomineeDetail);
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
			
			if(objInsured.getProposerInsuredNomineeDetails() != null && ! objInsured.getProposerInsuredNomineeDetails().isEmpty()) {
				List<PolicyNominee> nomineeDetails = getProposerNomineeDetails(insured.getKey());
				Map<String, PolicyNominee> nomineeMap = new WeakHashMap<String, PolicyNominee>();
				for (PolicyNominee nomineeDetails2 : nomineeDetails) {
					nomineeMap.put(nomineeDetails2.getNomineeName(), nomineeDetails2);
				}
				for (PolicyNominee nomineeDetail : objInsured.getProposerInsuredNomineeDetails()) {
					PolicyNominee nomineeDetails2 = nomineeMap.get(nomineeDetail.getNomineeName());
					if(nomineeDetails2 != null){
						nomineeDetail.setKey(nomineeDetails2.getKey());
						nomineeMap.remove(nomineeDetails2);
					}
					nomineeDetail.setPolicy(objInsured.getPolicy());
					nomineeDetail.setInsured(objInsured);
					if(nomineeDetail.getKey() == null){
						nomineeDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						nomineeDetail.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						nomineeDetail.setActiveStatus(1);
						entityManager.persist(nomineeDetail);
						entityManager.flush();
					} else {
						nomineeDetail.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						nomineeDetail.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(nomineeDetail);
						entityManager.flush();
					}
				}
				
				if(!nomineeMap.isEmpty()){
					if(nomineeMap != null && ! nomineeMap.isEmpty()){
						Set<Entry<String, PolicyNominee>> entrySet = nomineeMap.entrySet();
						for (Map.Entry<String, PolicyNominee> entry : entrySet)  {
							PolicyNominee nomineeDetails1 = (PolicyNominee)entry.getValue();
							nomineeDetails1.setActiveStatus(0);;
							nomineeDetails1.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
							nomineeDetails1.setModifiedDate(new Timestamp(System
									.currentTimeMillis()));
				            entityManager.merge(nomineeDetails1);
				            entityManager.flush();
					      }
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
		}
		
	}
	

	private void updatePAInsuredDetailsForEndorsement(Policy policy,
			Policy policy2,Map<Long,Insured> insuredMap) {
		//Update Endorsement Details
		for (Insured insuredPA : policy.getInsuredPA()) {
			Insured insured = insuredMap.get(insuredPA.getInsuredId());
			if(insured != null){
				insuredPA.setKey(insured.getKey());
				insuredMap.remove(insuredPA.getInsuredId());
			}
			
			insuredPA.setPolicy(policy2);
			if(policy2.getDeductibleAmount() != null){
				insuredPA.setDeductibleAmount(policy2.getDeductibleAmount());
			}
			if(insuredPA != null){
				if(insuredPA.getKey() != null){
					entityManager.merge(insuredPA);
					entityManager.flush();
				}else{
					entityManager.persist(insuredPA);
					entityManager.flush();
				}
			}
			
			
			if(insuredPA.getCoverDetailsForPA() != null && ! insuredPA.getCoverDetailsForPA().isEmpty()){
				List<InsuredCover> insuredCoverByInsuredKey = getInsuredCoverByInsuredKey(insured.getKey());
				Map<String, InsuredCover> insuredCoverMap = new WeakHashMap<String, InsuredCover>();
				for (InsuredCover insuredCover : insuredCoverByInsuredKey) {
					insuredCoverMap.put(insuredCover.getCoverCode(), insuredCover);
				}
				for (InsuredCover insuredCoverPA : insuredPA.getCoverDetailsForPA()) {
					InsuredCover insuredCover = insuredCoverMap.get(insuredCoverPA.getCoverCode());
					if(insuredCover != null){
						insuredCoverPA.setKey(insuredCover.getKey());
						insuredCoverMap.remove(insuredCover);
					}
					insuredCoverPA.setInsuredKey(insuredPA.getKey());
					if(insuredCoverPA.getKey() == null){
						entityManager.persist(insuredCoverPA);
						entityManager.flush();
					} else {
						entityManager.merge(insuredCoverPA);
						entityManager.flush();
					}
				}
				if(!insuredCoverMap.isEmpty()){
					if(insuredCoverMap != null && ! insuredCoverMap.isEmpty()){
						Set<Entry<String, InsuredCover>> entrySet = insuredCoverMap.entrySet();
						for (Map.Entry<String, InsuredCover> entry : entrySet)  {
							InsuredCover insuredCover = (InsuredCover)entry.getValue();
							insuredCover.setDeletedFlag("Y");
				            entityManager.merge(insuredCover);
				            entityManager.flush();
					      }
					}
				}
			}
			
			if(insuredPA.getNomineeDetails() != null && ! insuredPA.getNomineeDetails().isEmpty()) {
				List<NomineeDetails> nomineeDetails = getNomineeDetails(insured.getKey());
				Map<String, NomineeDetails> nomineeMap = new WeakHashMap<String, NomineeDetails>();
				for (NomineeDetails nomineeDetails2 : nomineeDetails) {
					nomineeMap.put(nomineeDetails2.getNomineeName(), nomineeDetails2);
				}
				for (NomineeDetails nomineeDetail : insuredPA.getNomineeDetails()) {
					NomineeDetails nomineeDetails2 = nomineeMap.get(nomineeDetail);
					if(nomineeDetails2 != null){
						nomineeDetail.setKey(nomineeDetails2.getKey());
						nomineeMap.remove(nomineeDetails2);
					}
					nomineeDetail.setInsured(insuredPA);
					if(nomineeDetail.getKey() == null){
						entityManager.persist(nomineeDetail);
						entityManager.flush();
					} else {
						entityManager.merge(nomineeDetail);
						entityManager.flush();
					}
				}
				
				if(!nomineeMap.isEmpty()){
					if(nomineeMap != null && ! nomineeMap.isEmpty()){
						Set<Entry<String, NomineeDetails>> entrySet = nomineeMap.entrySet();
						for (Map.Entry<String, NomineeDetails> entry : entrySet)  {
							NomineeDetails nomineeDetails1 = (NomineeDetails)entry.getValue();
							nomineeDetails1.setDeletedFlag("Y");
				            entityManager.merge(nomineeDetails1);
				            entityManager.flush();
					      }
					}
				}
				
			}
			
			if(insuredPA.getNomineeDetails() != null && ! insuredPA.getNomineeDetails().isEmpty()) {
				List<PolicyNominee> nomineeDetails = getProposerNomineeDetails(insured.getKey());
				Map<String, PolicyNominee> nomineeMap = new WeakHashMap<String, PolicyNominee>();
				for (PolicyNominee nomineeDetails2 : nomineeDetails) {
					nomineeMap.put(nomineeDetails2.getNomineeName(), nomineeDetails2);
				}
				for (PolicyNominee nomineeDetail : insuredPA.getProposerInsuredNomineeDetails()) {
					PolicyNominee nomineeDetails2 = nomineeMap.get(nomineeDetail.getNomineeName());
					if(nomineeDetails2 != null){
						nomineeDetail.setKey(nomineeDetails2.getKey());
						nomineeMap.remove(nomineeDetails2);
					}
					nomineeDetail.setInsured(insuredPA);
					nomineeDetail.setPolicy(insuredPA.getPolicy());
					if(nomineeDetail.getKey() == null){
						nomineeDetail.setCreatedBy(SHAConstants.USER_ID_SYSTEM);
						nomineeDetail.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						nomineeDetail.setActiveStatus(1);
						entityManager.persist(nomineeDetail);
						entityManager.flush();
					} else {
						nomineeDetail.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
						nomineeDetail.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(nomineeDetail);
						entityManager.flush();
					}
				}
				
				if(!nomineeMap.isEmpty()){
					if(nomineeMap != null && ! nomineeMap.isEmpty()){
						Set<Entry<String, PolicyNominee>> entrySet = nomineeMap.entrySet();
						for (Map.Entry<String, PolicyNominee> entry : entrySet)  {
							PolicyNominee nomineeDetails1 = (PolicyNominee)entry.getValue();
							nomineeDetails1.setActiveStatus(0);
				            entityManager.merge(nomineeDetails1);
				            entityManager.flush();
					      }
					}
				}
				
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<InsuredCover> getInsuredCoverByInsuredKey(Long insuredKey){
		Query query = entityManager.createNamedQuery("InsuredCover.findByInsured").setParameter("insuredKey", insuredKey);
		 List<InsuredCover> insuredList  = (List<InsuredCover>) query.getResultList();			     
		return insuredList;		
	}
	
	public List<NomineeDetails> getNomineeDetails(Long insuredKey){
		
		Query query = entityManager.createNamedQuery("NomineeDetails.findByInsuredKey");
		query = query.setParameter("insuredKey", insuredKey);
		List<NomineeDetails> nomineeDetails = (List<NomineeDetails>) query.getResultList();
		return nomineeDetails;
		
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
	
	public List<PolicyCoverDetails> getPolicyCoverDetails(Long policyKey) {
		Query query = entityManager
				.createNamedQuery("PolicyCoverDetails.findByPolicy");
		query = query.setParameter("policyKey", policyKey);
		List<PolicyCoverDetails> insuredList = query.getResultList();
		
		return insuredList;

	}
	public MasAilmentLimit getMasAilmentLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo, String ailment) {
		Query query = entityManager
				.createNamedQuery("MasAilmentLimit.findBasedOnSIFromTo");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("ailment", ailment);
		List<MasAilmentLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasCopayLimit getMasCopayLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo, String claimType) {
		Query query = entityManager
				.createNamedQuery("MasCopayLimit.findBasedOnSIFromTo");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("claimType", claimType);
		List<MasCopayLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasDeliveryExpLimit getMasDeliveryLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo, String delType) {
		Query query = entityManager
				.createNamedQuery("MasDeliveryExpLimit.findBasedOnSIFromTo");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("delType", delType);
		List<MasDeliveryExpLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasPrePostHospLimit getMasPrePostHospLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo, String hospitalType) {
		Query query = entityManager
				.createNamedQuery("MasPrePostHospLimit.findBasedOnSIFromTo");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("hospitalType", hospitalType);
		List<MasPrePostHospLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public MasRoomRentLimit getMasRoomRentLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo, String roomType) {
		Query query = entityManager
				.createNamedQuery("MasRoomRentLimit.findBasedOnSIFromTo");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
		query = query.setParameter("sumInsuredTo", sumInsuredTo);
		query = query.setParameter("roomType", roomType);
		List<MasRoomRentLimit> ailmentLimit = query.getResultList();
		if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
			return ailmentLimit.get(0);
		}
		return null;
	}
	
	public void updatePremiaEndorsementTableWithRiskId(PremiaEndorsementTable endorsement, String message){
		
		String query = "UPDATE PGIT_END_APPR_DTLS SET AED_READ_FLG = '"+message+ "' WHERE AED_POL_SYS_ID ="+endorsement.getPolicySysId()+""
				+ " and AED_POL_END_NO_IDX="+endorsement.getEndorsementIndex()
				+" and AED_POL_NO='"+endorsement.getPolicyNumber()+"' and AED_RISK_ID='"+endorsement.getRiskId()+"'";
		//System.out.println(query);
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();

	}	
	
	public List<PremiaEndorsementTable> fetchRecFromPremiaEndorsementTableDTO(String batchSize) {
		List<PremiaEndorsementTable> endorsementList = null;
		try{
			String qry = "SELECT * from PGIT_END_APPR_DTLS p where p.AED_READ_FLG is null and AED_PROD_CODE not  in ('MED-PRD-047','MED-PRD-068','ACC-PRD-011','ACC-PRD-019') order by p.AED_CR_DT asc";
			Query nativeQuery = entityManager.createNativeQuery(qry);
			nativeQuery.setMaxResults(SHAUtils.getIntegerFromString("20"));
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			endorsementList = new ArrayList<PremiaEndorsementTable>();
			PremiaEndorsementTable premEnd = null;
			for (Object[] objects : objList) {
				premEnd = new PremiaEndorsementTable();
				
				BigDecimal decPolSys = (BigDecimal)objects[0];
				premEnd.setPolicySysId(decPolSys.longValue());
				
				BigDecimal index = (BigDecimal)objects[1];
				premEnd.setEndorsementIndex(index.longValue());
				
				premEnd.setPolicyNumber((String)objects[2]);
				BigDecimal riskId = (BigDecimal)objects[3];
				premEnd.setRiskId(riskId != null ? riskId.toString() : null);
				premEnd.setProductCode((String)objects[4]);
//				premEnd.setReadFlag((String)objects[5]);
				
				Timestamp cretedOn = (Timestamp) objects[6];
				premEnd.setGiCreatedOn(cretedOn);
				
				endorsementList.add(premEnd);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return endorsementList;
	}
	
	public List<PremiaEndorsementTable> fetchRecFromPremiaEndorsementTableForGMC(String batchSize) {
		List<PremiaEndorsementTable> endorsementList = null;
		try{
			String qry = "SELECT * from PGIT_END_APPR_DTLS p where p.AED_READ_FLG is null and AED_PROD_CODE in  ('MED-PRD-047','MED-PRD-068') order by p.AED_CR_DT asc";
			Query nativeQuery = entityManager.createNativeQuery(qry);
			nativeQuery.setMaxResults(SHAUtils.getIntegerFromString("20"));
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			endorsementList = new ArrayList<PremiaEndorsementTable>();
			PremiaEndorsementTable premEnd = null;
			for (Object[] objects : objList) {
				premEnd = new PremiaEndorsementTable();
				
				BigDecimal decPolSys = (BigDecimal)objects[0];
				premEnd.setPolicySysId(decPolSys.longValue());
				
				BigDecimal index = (BigDecimal)objects[1];
				premEnd.setEndorsementIndex(index.longValue());
				
				premEnd.setPolicyNumber((String)objects[2]);
				BigDecimal riskId = (BigDecimal)objects[3];
				premEnd.setRiskId(riskId != null ? riskId.toString() : null);
				premEnd.setProductCode((String)objects[4]);
//				premEnd.setReadFlag((String)objects[5]);
				
				Timestamp cretedOn = (Timestamp) objects[6];
				premEnd.setGiCreatedOn(cretedOn);
				
				endorsementList.add(premEnd);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return endorsementList;
	}
	
	public List<PremiaEndorsementTable> fetchRecFromPremiaEndorsementTableForGPA(String batchSize) {
		

		List<PremiaEndorsementTable> endorsementList = null;
		try{
			String qry = "SELECT * from PGIT_END_APPR_DTLS p where p.AED_READ_FLG is null and AED_PROD_CODE in ('ACC-PRD-011','ACC-PRD-019') order by p.AED_CR_DT asc";
			Query nativeQuery = entityManager.createNativeQuery(qry);
			nativeQuery.setMaxResults(SHAUtils.getIntegerFromString("20"));
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			endorsementList = new ArrayList<PremiaEndorsementTable>();
			PremiaEndorsementTable premEnd = null;
			for (Object[] objects : objList) {
				premEnd = new PremiaEndorsementTable();
				
				BigDecimal decPolSys = (BigDecimal)objects[0];
				premEnd.setPolicySysId(decPolSys.longValue());
				
				BigDecimal index = (BigDecimal)objects[1];
				premEnd.setEndorsementIndex(index.longValue());
				
				premEnd.setPolicyNumber((String)objects[2]);
				BigDecimal riskId = (BigDecimal)objects[3];
				premEnd.setRiskId(riskId != null ? riskId.toString() : null);
				premEnd.setProductCode((String)objects[4]);
//				premEnd.setReadFlag((String)objects[5]);
				
				Timestamp cretedOn = (Timestamp) objects[6];
				premEnd.setGiCreatedOn(cretedOn);
				
				endorsementList.add(premEnd);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return endorsementList;
	
	}
	
	public List<PolicyNominee> getProposerNomineeDetails(Long insuredKey){
		
		Query query = entityManager.createNamedQuery("PolicyNominee.findByInsuredKey");
		query = query.setParameter("insuredKey", insuredKey);
		List<PolicyNominee> nomineeDetails = (List<PolicyNominee>) query.getResultList();
		return nomineeDetails;
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
	
	public void updatePremiaEndorsementTableWithoutRiskId(PremiaEndorsementTable endorsement, String message){
		
		String query = "UPDATE PGIT_END_APPR_DTLS SET AED_READ_FLG = '"+message+ "' WHERE AED_POL_SYS_ID ="+endorsement.getPolicySysId()+""
				+ " and AED_POL_END_NO_IDX="+endorsement.getEndorsementIndex()
				+" and AED_POL_NO='"+endorsement.getPolicyNumber()+"'";
		//System.out.println(query);
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();

	}
	
	 public String getKotakPolicyCpuCode(String policyNumber){
			
		 Query query = entityManager.createNamedQuery("StarKotakPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarKotakPolicy> resultList = (List<StarKotakPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}

	 public String getMasProductCpu(Long key){
			
		 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		 query = query.setParameter("key", key);
		 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}

	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void insertICRbatchData(){
		  try{
				utx.begin();
				log.info("*********  ICR Batch Data Insert begin ********************------> ");
		        List<IncurredClaimRatio> getpolicyNumberForIncurredClaimRatio = getpolicyNumberForIncurredClaimRatio();
			    if(getpolicyNumberForIncurredClaimRatio != null && ! getpolicyNumberForIncurredClaimRatio.isEmpty()){
				IncurredClaimRatioBatchMonitor ICRupdate = new IncurredClaimRatioBatchMonitor();
				ICRupdate.setAppBatchFlag("1");
				ICRupdate.setDbBatchFlag("0");
				ICRupdate.setBatchDate(new Timestamp(System.currentTimeMillis()));
				ICRupdate.setCreatedBy("SYSTEM");
				ICRupdate.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(ICRupdate);
				entityManager.flush();
				log.info("*********  ICR Batch Data Inserted SuccessFully ********************------> ");
				utx.commit();
			}
		  }
		  catch(Exception e) {
				log.error("___________________  ICR Data Not Inserted  ___________________" );
				e.printStackTrace();
	  }
    }
	 
	@SuppressWarnings({ "unchecked", "unused" }) 
	public List<MastersValue> findByMasterListKeyList(String value) { 
		MastersValue a_mastersValue = null;
		List<MastersValue> mastersValueList= new ArrayList<MastersValue>(); 
		if (value!= null) { 
			Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKey"); 
			query = query.setParameter("parentKey", value); 
			mastersValueList = query.getResultList();
		}

		return mastersValueList; 
	}
	 
	 public Product getProductBySource(String productCode){

			Product product = null;
			Query query = entityManager.createNamedQuery("Product.findBySourceCode");
			query = query.setParameter("productCode", productCode);	
			List resultList = query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				product = (Product) query.getResultList().get(0);	
			} else {
				System.out.println("This product is not available in Galaxy------------->" + productCode);
			}

			return product;
		}
	 
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
		public Intimation insertIntimationDataRevisedBaNCS(BaNCSIntimationTable bancsIntimationTable,String lobFlag) throws NotSupportedException, SystemException {
			try {
				utx.begin();
				Boolean isHospTypeEmpty = false;
				Intimation intimation  = new Intimation();
				MastersValue intimationModeValue = null;
				MastersValue intimationIntimatedBy = null;
				Policy policy = null;
	 			Hospitals hospitals = null;
				MastersValue admissionType = null;
				MastersValue managementType = null;
				MastersValue roomCategory = null;
				TmpCPUCode cpuCode = null;
				MastersValue intimationSource = null;
				MastersValue hospitalType = new MastersValue();
				MastersValue claimType = new MastersValue();
				Insured insured = null;
				if(null != bancsIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(bancsIntimationTable.getGiIntimationMode()))
					intimationModeValue = getMastersValue(bancsIntimationTable.getGiIntimationMode(),ReferenceTable.MODE_OF_INTIMATION);
				else
					intimationModeValue = getMastersValue(SHAConstants.INTIMATION_MODE_PHONE,ReferenceTable.MODE_OF_INTIMATION);
				
				if(null != bancsIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(bancsIntimationTable.getGiIntimationMode())){
					if(bancsIntimationTable.getGiIntimationMode().equalsIgnoreCase(SHAConstants.INTIMATION_MODE_WEBSERVICE)){
						intimationModeValue = getMaster(ReferenceTable.INTIMATION_ONLINE_MODE);
					}
				}

				if(null != bancsIntimationTable.getGiIntimatedBy() && !("").equalsIgnoreCase(bancsIntimationTable.getGiIntimatedBy()))
					intimationIntimatedBy = getMastersValue(bancsIntimationTable.getGiIntimatedBy(),ReferenceTable.INTIMATED_BY);
				else
					intimationIntimatedBy = getMastersValue(SHAConstants.INTIMATION_INTIMATED_BY,ReferenceTable.INTIMATED_BY);
				
				if(null != bancsIntimationTable.getGiPolNo() && !("").equalsIgnoreCase(bancsIntimationTable.getGiPolNo()))
					policy = getPolicyByPolicyNubember(bancsIntimationTable.getGiPolNo());
				
				/* For the product star criticare platinum and gold,cashless process is not applicable and it should be routed to reimbursement
				  even the user create intimation for network hospital.the below code is added for this scenario */
				if(null != bancsIntimationTable.getProductCode() &&
					(ReferenceTable.getDirectReimbursementProducts().containsKey(bancsIntimationTable.getProductCode()))) {
					bancsIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
				}
				// GLX2020054 add for star novel grp corona
				if(policy != null && (policy.getProduct().getCode().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE) /*||
						policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_COVID_PROD_CODE)*/)&& null !=  policy.getPolicyPlan()&& (policy.getPolicyPlan().equals(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))) {
					bancsIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
				}
				
				if(null != bancsIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(bancsIntimationTable.getGiHospCode())) {
					
					String hospCode =  bancsIntimationTable.getGiHospCode();
					hospitals = getHospitalByHospNo(hospCode.toUpperCase());
					
					if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {
						
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
						
					}else if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {
						if(null != hospCode) {
							log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
							if(null != hospitals)
								cpuCode =  getCpuDetails(hospitals.getCpuId());
						}
					}
				}
				else
				{
					String hospCode =  SHAConstants.DEFAULT_HOSP_CODE;
					isHospTypeEmpty = true;
					if(null != hospCode) {
						log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
						hospitals = getHospitalByHospNo(hospCode.toUpperCase());
						if(null != hospitals)
							cpuCode =  getCpuDetails(hospitals.getCpuId());
					}
				}
				
				//added foe CR GMC CPU Routing GLX2020075
				if(policy != null && policy.getProduct().getKey() != null){
					MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(policy.getProduct().getKey());
					if(gmcRoutingProduct != null){
						if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {	
							if(null != bancsIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(bancsIntimationTable.getGiHospCode())) {
								String hospCode =  bancsIntimationTable.getGiHospCode();
								hospitals = getHospitalByHospNo(hospCode.toUpperCase());
								log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
								if(null != hospitals)
									cpuCode =  getCpuDetails(hospitals.getCpuId());
								if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
									cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
								}

							}
						}else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {
							if(policy.getHomeOfficeCode() != null) {
								OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
								if(branchOffice != null){
									String officeCpuCode = branchOffice.getCpuCode();
									if(officeCpuCode != null) {
										MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
										if(masCpuLimit != null){
											cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
										}else {
											cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
											if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
												cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
											}
										}
									}
								}
							}

						}
						intimation.setCpuCode(cpuCode);
					}
				}
				//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//				if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
//					cpuCode =  getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//					if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {
//						if(policy.getHomeOfficeCode() != null) {
//							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
//							if(branchOffice != null){
//								String officeCpuCode = branchOffice.getCpuCode();
//								if(officeCpuCode != null) {
//									MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
//									if(masCpuLimit != null){
//										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
//									}
//								}
//							}
//						}
//				   }
//					intimation.setCpuCode(cpuCode);
//				}
				
				if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
					String baayasCpuCodeByPolicy = getPaayasCpuCodeByPolicy(policy.getPolicyNumber());
					if(baayasCpuCodeByPolicy != null){
						cpuCode = getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
						if(cpuCode != null){
							intimation.setCpuCode(cpuCode);
						}
					}
					
					String jioCpuCodeByPolicy = getJioPolicyCpuCode(policy.getPolicyNumber());
					if(jioCpuCodeByPolicy != null){
						cpuCode = getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
						if(cpuCode != null){
							intimation.setCpuCode(cpuCode);
						}
					}
					
					Long tataPolicy = getTataPolicy(policy.getPolicyNumber());
					if(tataPolicy != null){
						cpuCode = getMasCpuCode(tataPolicy);
						if(cpuCode != null){
							intimation.setCpuCode(cpuCode);
						}
					}
					
					String kotakCpuCodeByPolicy = getKotakPolicyCpuCode(policy.getPolicyNumber());
					if(kotakCpuCodeByPolicy != null){
						cpuCode = getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
						if(cpuCode != null){
							intimation.setCpuCode(cpuCode);
						}
					}
					
					
				}
				
				if(policy != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
					cpuCode =  getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
					intimation.setCpuCode(cpuCode);
				}
				//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
				//added for CPU routing
//				if(policy != null && policy.getProduct().getKey() != null){
//					String CpuCode= getMasProductCpu(policy.getProduct().getKey());
//					if(CpuCode != null){
//						cpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//						intimation.setCpuCode(cpuCode);
//					}
//				}
				//added for CPU routing
				
				String gpaPolicyDetails = getGpaPolicyDetails(policy.getPolicyNumber());
				if(gpaPolicyDetails != null){
					TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
					if(masCpuCode != null){
						intimation.setCpuCode(masCpuCode);
						cpuCode = masCpuCode;
					}
				}
				
				if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) 
						{
							hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
						}
				else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(bancsIntimationTable.getGiHospitalTypeYn())) {
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
				}
//				if(null != premiaIntimationTable.getGiClmProcDivn()){
//					cpuCode = getMasCpuCode(Long.parseLong(premiaIntimationTable.getGiClmProcDivn()));
//				}
				
				/***
				 * Added for pa claims .
				 * */
				setClaimTypeAndHospitalTypeBaNCS(bancsIntimationTable,hospitalType,claimType,policy,cpuCode,bancsIntimationTable.getGiPACategory(),isHospTypeEmpty);
			/*	if(SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(premiaIntimationTable.getGiPACategory())) 
				{
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) && 
							SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
					{
						
						
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
					}
					else
					{
						setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
				}*/
				/**
				 * The below else block will be called other than PA Claims. 
				 * */
			/*	else {
						
						if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
							setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
							hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
							claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
						} else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
							setClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory());
							hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
							claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
							if(policy.getHomeOfficeCode() != null) {
								OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
								if(branchOffice != null){
									String officeCpuCode = branchOffice.getCpuCode();
									if(officeCpuCode != null) {
										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
									}
								}
							}
						}
				}*/
				if(null != bancsIntimationTable.getGiAdmissionType() && !("").equalsIgnoreCase(bancsIntimationTable.getGiAdmissionType()))
					admissionType = getMastersValue(bancsIntimationTable.getGiAdmissionType(), ReferenceTable.ADMISSION_TYPE);
				
				if(null != bancsIntimationTable.getGiManagementType() && !("").equalsIgnoreCase(bancsIntimationTable.getGiManagementType()))
					managementType = getMastersValue(bancsIntimationTable.getGiManagementType(),ReferenceTable.TREATMENT_MANAGEMENT);
				else
					managementType = getMastersValue(SHAConstants.MGMT_SURGICAL,ReferenceTable.TREATMENT_MANAGEMENT);
				if(null != bancsIntimationTable.getGiRoomCategory() && !("").equalsIgnoreCase(bancsIntimationTable.getGiRoomCategory()))
					roomCategory = getMastersValue(bancsIntimationTable.getGiRoomCategory(), ReferenceTable.ROOM_CATEGORY);
				
				/*else
					roomCategory = getMastersValue(SHAConstants.DELUXE, ReferenceTable.ROOM_CATEGORY);*/

				if(null != bancsIntimationTable.getGiInsuredName() && !("").equalsIgnoreCase(bancsIntimationTable.getGiInsuredName())){
					insured = getInsuredByPolicyAndInsuredName(bancsIntimationTable.getGiPolNo(), bancsIntimationTable.getGiInsuredName(),bancsIntimationTable.getGiPACategory());
				}
				
				if(insured == null){
					insured = getInsuredByPolicyAndInsuredNameForDefault(bancsIntimationTable.getGiPolNo(), bancsIntimationTable.getGiInsuredName());
				}
				/*
				 * Intimation source - Currently in premia , there is no column available 
				 * in premia table. As of now,  hardcoding 131. Later this needs to be removed.
				 * */
				intimationSource = new MastersValue();
				
				if(null != bancsIntimationTable.getGiSavedType() && (SHAConstants.PREMIA_INTIMATION_HOSP_SAVE).equalsIgnoreCase(bancsIntimationTable.getGiSavedType())) {
					intimationSource.setKey(ReferenceTable.HOSPITAL_PORTAL);
				} else {
					intimationSource.setKey(ReferenceTable.CALL_CENTRE_SOURCE);
				}
				
				intimation.setInsured(insured);
				intimation.setIntimationMode(intimationModeValue);
				intimation.setIntimatedBy(intimationIntimatedBy);
				intimation.setIntimaterName(bancsIntimationTable.getGiIntimatorName());
				if(null != bancsIntimationTable.getGiIntimatorContactNo()) {
					String userNameForDB = SHAUtils.getUserNameForDB(bancsIntimationTable.getGiIntimatorContactNo());
					intimation.setCallerLandlineNumber(userNameForDB);
					intimation.setCallerMobileNumber(userNameForDB);
				}

				intimation.setPolicy(policy);
				intimation.setIntimationId(bancsIntimationTable.getGiIntimationNo());
				if(null != hospitals) {
					intimation.setHospital(hospitals.getKey());
				}
				
				/**
				 * new column added for policy year
				 */
				if(bancsIntimationTable.getGiPolicyYear() != null){
					intimation.setPolicyYear(bancsIntimationTable.getGiPolicyYear());
				}
				
				if(bancsIntimationTable.getGiAttenderMobileNumber() != null){
					// As per Satish and Saran instruction used substring for attender mobile
//					intimation.setAttendersMobileNumber(bancsIntimationTable.getGiAttenderMobileNumber());
					String attenderMobNo = SHAUtils.getUserNameForDB(bancsIntimationTable.getGiAttenderMobileNumber());
					intimation.setAttendersMobileNumber(attenderMobNo);
				}
				
				intimation.setHospitalType(hospitalType);
				intimation.setHospitalComments(bancsIntimationTable.getGiSHospComments());
				intimation.setCallerAddress(bancsIntimationTable.getCallerAddress());
				intimation.setAdmissionType(admissionType);
				intimation.setManagementType(managementType);
				intimation.setAdmissionReason(bancsIntimationTable.getGiReasonForAdmission());
				intimation.setAdmissionDate(bancsIntimationTable.getGiAdmitted());
				intimation.setRoomCategory(roomCategory);
				intimation.setInsuredPatientName(bancsIntimationTable.getGiPatientNameYn());
				intimation.setInpatientNumber(bancsIntimationTable.getGiInpatientNo());
				intimation.setCpuCode(cpuCode);
				intimation.setIntimationSource(intimationSource);
				intimation.setCreatedBy(bancsIntimationTable.getGiCreatedBy());
				intimation.setCreatedDate(bancsIntimationTable.getGiCreatedOn());
				intimation.setClaimType(claimType);
				intimation.setStatus(getStatus(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY));
				intimation.setStage(getStage(ReferenceTable.INTIMATION_STAGE_KEY));
				if(cpuCode != null){
					intimation.setOriginalCpuCode(cpuCode.getCpuCode());
				}
				
		
				/**
				 * Added for PA.
				 * 
				 * **/
				if(SHAConstants.YES_FLAG.equalsIgnoreCase(bancsIntimationTable.getGiAccidentDeathFlag()))
						intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
				else 
					intimation.setIncidenceFlag(SHAConstants.DEATH_FLAG);
				
				//intimation.setIncidenceFlag(premiaIntimationTable.getGiAccidentDeathFlag());
				intimation.setProcessClaimType(bancsIntimationTable.getGiPACategory());
				if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(bancsIntimationTable.getGiPACategory()))
				{
					intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
				}
				intimation.setHospitalReqFlag(bancsIntimationTable.getGiHospitalRequiredFlag());
				if(null != policy.getLobId())
				{
					MastersValue masLob = getMaster(policy.getLobId());
					intimation.setLobId(masLob);
				}			
				/*if((policy.getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_37_PRODUCT)) || (policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT))
						|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER)) || (policy.getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL)) 
	<<<<<<< HEAD
						|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE))){*/			
	/*			=======
						|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE))
						|| (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()))
						|| (policy.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_NEW))){
	>>>>>>> mayrelease_04_05_2018_EAP_7_CR*/
				
				if(null != intimation.getPolicy().getProduct().getKey() && 
						(ReferenceTable.getCombinationOfHealthAndPA().containsKey(intimation.getPolicy().getProduct().getKey()))){

					if(bancsIntimationTable.getGiAccidentDeathFlag() != null){
						MastersValue masLob = getMaster(ReferenceTable.PA_LOB_KEY);
						intimation.setLobId(masLob);
					}
				}

				if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())){
					intimation.setPaParentName(bancsIntimationTable.getGiPAParentName());
					if(bancsIntimationTable.getGiPAParentAge() != null){
						intimation.setPaParentAge(Double.valueOf(bancsIntimationTable.getGiPAParentAge()));
					}
					if(bancsIntimationTable.getGiPAParentDOB() != null){
						intimation.setPaParentDOB(new Date(bancsIntimationTable.getGiPAParentDOB()));
					}
					intimation.setPaPatientName(bancsIntimationTable.getGiPAPatientName());
					intimation.setPaCategory(bancsIntimationTable.getGiPACategory());
				}
				
				if(intimation.getInsured() == null || intimation.getHospital() == null){
					log.info(" &&&&&&&&& INTIMATION IS NOT INSERTED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! >"+ bancsIntimationTable.getGiIntimationNo() +"<------------");
					return null;
				}
				
				// PA added fix ends up here.
				if(null != intimation.getHospital()){
					entityManager.persist(intimation);
					entityManager.flush();
					log.info(" &&&&&&&&& INTIMATION INSERTED SUCESSFULLY------>"+ intimation.getIntimationId() +"<------------");
				}
				
				
				
				if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()) && intimation.getPolicy().getGpaPolicyType() != null 
						&& intimation.getPolicy().getGpaPolicyType().equalsIgnoreCase("Un Named")){
					DBCalculationService dbCalculationService = new DBCalculationService();
					dbCalculationService.invokeProcedureForUnnamedPolicy(intimation.getIntimationId());
				}
				utx.commit();
				
				return intimation;
			
			} catch(Exception e) {
				utx.rollback();
			}
			return null;
		}
	 
	 private void setClaimTypeAndHospitalTypeBaNCS(BaNCSIntimationTable premiaIntimationTable,MastersValue hospitalType ,MastersValue claimType,Policy policy ,TmpCPUCode cpuCode,String lobType,Boolean isHospTypeEmpty)
		{
			if(SHAConstants.PA_LOB_TYPE.equals(lobType))
			{
				if(isHospTypeEmpty)
				{
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					if(policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
				else{
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) && 
							SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
					{
						//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
					}
					else {
						//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
				}
			}
			else
			{
				if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				}
				else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
					hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
					claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					if(policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
			}
		}
	 
	 public Boolean processSavedIntimationRevisedBaNCS(Intimation intimationObj, BaNCSIntimationTable bancsIntimation) throws IllegalStateException, SecurityException, SystemException {
			try {
				utx.begin();
				Intimation intimation = getIntimationByKey(intimationObj.getKey());
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> "
						+ intimation != null ? intimation.getIntimationId()
						: (intimation != null ? intimation.getIntimationId()
								+ "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
								: "NO INTIMATIONS"));
				log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> " + intimation != null ? intimation.getIntimationId() : (intimation != null ? intimation.getIntimationId() + "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US" : "NO INTIMATIONS") );

				IntimationRule intimationRule = new IntimationRule();
				/*IntimationType a_message = new IntimationType();
				ClaimRequestType claimReqType = new ClaimRequestType();
	<<<<<<< HEAD
				ClaimType claimType = new ClaimType();*/

				String strClaimType = "";
				
				/**
				 * All PA claims goes to manual registration only. Hence
				 * below condition was added.
				 * */
			
				
				if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
					strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
				}
				else if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
					strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
				}
				//claimType.setClaimType(strClaimType);
				if (null != intimation.getCpuCode()
						&& null != intimation.getCpuCode().getCpuCode())
					/*claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode()
							.getCpuCode()));
				a_message.setKey(intimation.getKey());*/
				if (null != intimation.getAdmissionDate()) {
					String date = String.valueOf(intimation.getAdmissionDate());
					String date1 = date.replaceAll("-", "/");
					//a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
//					a_message.setIntDate(new Timestamp(System.currentTimeMillis()));
//					Timestamp timestamp = new Timestamp(
//							System.currentTimeMillis() + 60 * 60 * 1000);
//					a_message.setIntDate(timestamp);
				}
				/*a_message.setIntimationNumber(intimation.getIntimationId());
				a_message
						.setIntimationSource(intimation.getIntimationSource() != null ? (intimation
								.getIntimationSource().getValue() != null ? intimation
								.getIntimationSource().getValue() : "")
								: "");
				a_message.setIsClaimPending(!intimationRule
						.isClaimExist(intimation));
				a_message
						.setIsPolicyValid(intimationRule.isPolicyValid(intimation));*/
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				Double balsceSI = 0d;
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null !=intimation.getPolicy().getProduct().getKey() &&
						!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
					
					if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
						balsceSI = dbCalculationService.getBalanceSIForGMC(intimation.getPolicy().getKey() ,intimation.getInsured().getKey(),0l);
					}else{
						balsceSI = dbCalculationService.getBalanceSI(
								intimation.getPolicy().getKey(),
								intimation.getInsured().getKey(), 0l,
								intimation.getInsured().getInsuredSumInsured(),
								intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
							
					}
				}
				else
				{
					balsceSI = dbCalculationService.getGPABalanceSI(
							intimation.getPolicy().getKey(),
							intimation.getInsured().getKey(), 0l,
							intimation.getInsured().getInsuredSumInsured(),
							intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				

				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
						.equalsIgnoreCase(strClaimType)) {
					if (null != balsceSI
							&& balsceSI > 0
							&& null != intimation.getPolicy()
							&& (SHAConstants.NEW_POLICY)
									.equalsIgnoreCase(intimation.getPolicy()
											.getPolicyStatus())
							&& null != strPremiaFlag
							&& ("true").equalsIgnoreCase(strPremiaFlag)) {
						//a_message.setIsBalanceSIAvailable(true);
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
						String get64vbStatus = PremiaService.getInstance()
								.get64VBStatus(
										intimation.getPolicy().getPolicyNumber(), intimation.getIntimationId());
						
						/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
							get64vbStatus = "R";
						}*/
						
						if (get64vbStatus != null
								&& (SHAConstants.DISHONOURED
										.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
										.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
										.equalsIgnoreCase(get64vbStatus))) {
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
						}
					} else {
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
						if (null != intimation.getPolicy()
								&& !(SHAConstants.NEW_POLICY)
										.equalsIgnoreCase(intimation.getPolicy()
												.getPolicyStatus())) {
							Boolean endorsedPolicyStatus = PremiaService
									.getInstance().getEndorsedPolicyStatus(
											intimation.getPolicy()
													.getPolicyNumber());
							/*
							if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
								endorsedPolicyStatus = true;
							}
							*/

							if (endorsedPolicyStatus) {
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
								String get64vbStatus = PremiaService.getInstance()
										.get64VBStatus(
												intimation.getPolicy()
														.getPolicyNumber(), intimation.getIntimationId());
								/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
									get64vbStatus = "R";
								}*/
								if (get64vbStatus != null
										&& (SHAConstants.DISHONOURED
												.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
												.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
												.equalsIgnoreCase(get64vbStatus))) {
									mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");

								}
							}
							
						}
					}
				} else {
					if (null != intimation.getPolicy()
							&& !(SHAConstants.NEW_POLICY)
									.equalsIgnoreCase(intimation.getPolicy()
											.getPolicyStatus())) {
						Boolean endorsedPolicyStatus = PremiaService.getInstance()
								.getEndorsedPolicyStatus(
										intimation.getPolicy().getPolicyNumber());
						/**
						 * As per satish sir, Endorsement status is not consider for GMC
						 */
						/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
							endorsedPolicyStatus = true;
						}*/
						if (endorsedPolicyStatus)
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
						else
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
					} else {
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
					}
				}
				if(null != intimation.getLobId())
					{
						if(!(ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey())) 
								&& (!((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey())
										&& (intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)))))
						{
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"N");
						}
		            }
				
				
				if(intimation.getPolicy().getProduct() != null && !(ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(intimation.getPolicy().getProduct().getKey())) && ReferenceTable.getSuperSurplusKeys().containsKey(intimation.getPolicy().getProduct().getKey())
						&& intimation.getPolicy().getPolicyPlan() != null && intimation.getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"N");
				}
				
				Claim claimObject = null;
				
				Policy policy = intimation.getPolicy();
				if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(policy.getProduct().getKey())){
					Date policyFromDate = policy.getPolicyFromDate();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(policyFromDate);
					calendar.add(Calendar.YEAR, 1);
				    Date afterOneYear = calendar.getTime();
				    
				    log.info("*********After One year For unique policy ********  " +afterOneYear);
				    
				    Date policyToDate = policy.getPolicyToDate();
				    
				    log.info("*********Policy To Date For unique policy ********  " +policyToDate);
				    
				    if(intimation.getAdmissionDate() != null && intimation.getAdmissionDate().after(afterOneYear) && intimation.getAdmissionDate().before(policyToDate)){
				    	Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(policy.getPolicyNumber());
				    	if(uniqueInstallmentAmount > 0){
				    		mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
				    	}
				    }
				}
				
				if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
					log.info("&&&&&&&&&&&&&THIS INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
							+ intimation.getIntimationId());
					claimObject = doAutoRegistrationProcessRevised(intimation);
				}
				else {
					log.info("%%%%%***MANUAL**** *THIS INTIMATION IS  GOING TO MANUAL REGISTRATION  -------->"
							+ ((balsceSI != null && balsceSI > 0) ? "BALANCE IS IS "
									+ String.valueOf(balsceSI) + ""
									: "64VB or ENDORSED ")
							+ "   INTIMATION NUMBER------>"
							+ intimation.getIntimationId());
				}
				
				Hospitals hospital = getHospitalByHospNo(bancsIntimation
						.getGiHospCode());
				
	
					
				
				if(hospital == null){
					hospital = getHospitalByHospNo(bancsIntimation
							.getGiHospCode());
				}

				// }


				String userId = BPMClientContext.BPMN_TASK_USER;
				String password = BPMClientContext.BPMN_PASSWORD;
		

				
				if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI)) && claimObject != null){
					
					/**
					 *    auto registration for cashless claim and reimbursement claim
					 */
					
					if(claimObject.getClaimId() == null && claimObject.getKey() != null){
						claimObject = getClaimByClaimKey(claimObject.getKey());
					}
					
//					Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObject, hospital);
					
					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
					
					Object[] inputArray = (Object[])arrayListForDBCall[0];
					
					if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType)){
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
					}else{
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME;
					}
					/*if(null != intimation.getLobId())
					{
							if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
							{
								inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
								inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
								inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
							}
							
					}*/
					Object[] parameter = new Object[1];
					parameter[0] = inputArray;
//					dbCalculationService.initiateTaskProcedure(parameter);
					dbCalculationService.revisedInitiateTaskProcedure(parameter);
					
				}else{
					
					/**
					 *    Manual registration for cashless claim and reimbursement claim
					 */
					
//					Object[] arrayListForDBCall = SHAUtils.getArrayListForManualRegDBCall(intimation, hospital);
					
					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForManualRegistrationDBCall(intimation, hospital);
					
					Object[] inputArray = (Object[])arrayListForDBCall[0];
					
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
					
					if(null != intimation.getLobId())
					{
							if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
							{
								inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
								inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
							}
					}
					Object[] parameter = new Object[1];
					parameter[0] = inputArray;
//					dbCalculationService.initiateTaskProcedure(parameter);
					dbCalculationService.revisedInitiateTaskProcedure(parameter);
					
				}
				
				if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType) && null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
					
					/**
					 * Initiate for Reimbursement claim for auto Registration Only.
					 */
	//
//					IntMsg initiateInitmationTask = BPMClientContext
//							.getIntimationIntiationTask(
//									BPMClientContext.BPMN_TASK_USER,
//									BPMClientContext.BPMN_PASSWORD);
	//	
//					initiateInitmationTask.initiate(BPMClientContext.BPMN_TASK_USER,
//							payloadBO);
				}

				if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))
						&& (SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
								.equalsIgnoreCase(strClaimType)
						&& claimObject != null) {
	                log.info("****************&&&&&&& REMAINDER_INITIATED FOR AUTO REGISTRATION REIMBURSEMENT" + intimation.getIntimationId());
					
					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
					
					Object[] inputArray = (Object[])arrayListForDBCall[0];
					
					inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.BILLS_NOT_RECEIVED;
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS;

					callReminderTaskForDB(inputArray);
					
//					autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
					
					log.info("*******&&&&&&&&& FVR INITIATED FOR REIMBURSEMENT CLAIM **-->"
							+ intimation.getIntimationId());
				}
				
				if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
					autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
				}
				
				if(claimObject != null){

				if (claimObject != null) {
					Claim claim = getClaimByIntimationNo(intimationObj
							.getIntimationId());
					if (claim != null) {
						
						updateProvisionAmountToPremia(claim);
						}
					}

				}

				utx.commit();
				updateBaNCSIntimationTable(bancsIntimation, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
				return true;
			}
			catch(Exception e) {
				e.printStackTrace();
				utx.rollback();
//				updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_FAILURE_STATUS);
				log.error("***************************ERROR OCCURED WHILE CLAIM GENERATION **************************" + (intimationObj != null ? intimationObj.getIntimationId() : "NO INTIMATION NUMBER"));
				return false;
			}
					
		}
	 
	 public void updateBaNCSIntimationTable(BaNCSIntimationTable bancsIntimationTable, String message) throws IllegalStateException, SecurityException, SystemException {
			if(null != bancsIntimationTable && null != bancsIntimationTable.getGiIntimationId()){
				try {
					utx.begin();
					bancsIntimationTable.setGiSavedType(message);
					entityManager.merge(bancsIntimationTable);
					entityManager.flush();
					utx.commit();
				} catch(Exception e) {
					utx.rollback();
				}
			}
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
	 
	 private Insured getBaNCSInsuredByPolicyAndRiskIdWithLobFlag(String policyNo , String insuredId,String lobFlag) {
		   Query query = entityManager.createNamedQuery("Insured.findBaNCSInsuredIdAndPolicyNo");
			query = query.setParameter("policyNo", policyNo);
			query = query.setParameter("sourceRiskId", insuredId);
			query = query.setParameter("lobFlag", lobFlag);
			List<Insured> insuredList = query.getResultList();
			insuredList = query.getResultList();
			if(null != insuredList && !insuredList.isEmpty()) {
				return insuredList.get(0);
			} 
			
			return null;
		}
	 

	 public List<PremiaEndorsementTable> fetchRecFromPremiaEndorsementTableForGMCDesc(String batchSize) {
			List<PremiaEndorsementTable> endorsementList = null;
			try{
				String qry = "SELECT * from PGIT_END_APPR_DTLS p where p.AED_READ_FLG is null and AED_PROD_CODE in  ('MED-PRD-047','MED-PRD-068') order by p.AED_CR_DT desc";
				Query nativeQuery = entityManager.createNativeQuery(qry);
				nativeQuery.setMaxResults(SHAUtils.getIntegerFromString("20"));
				List<Object[]> objList = (List<Object[]>) nativeQuery
						.getResultList();
				endorsementList = new ArrayList<PremiaEndorsementTable>();
				PremiaEndorsementTable premEnd = null;
				for (Object[] objects : objList) {
					premEnd = new PremiaEndorsementTable();
					
					BigDecimal decPolSys = (BigDecimal)objects[0];
					premEnd.setPolicySysId(decPolSys.longValue());
					
					BigDecimal index = (BigDecimal)objects[1];
					premEnd.setEndorsementIndex(index.longValue());
					
					premEnd.setPolicyNumber((String)objects[2]);
					BigDecimal riskId = (BigDecimal)objects[3];
					premEnd.setRiskId(riskId != null ? riskId.toString() : null);
					premEnd.setProductCode((String)objects[4]);
//					premEnd.setReadFlag((String)objects[5]);
					
					Timestamp cretedOn = (Timestamp) objects[6];
					premEnd.setGiCreatedOn(cretedOn);
					
					endorsementList.add(premEnd);
					
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			return endorsementList;
		}
	 
	 public Long fetchCountPremiaEndorsementTableForGMC() {
		 Long pendingCount = 0L;
			try{
				String qry = "SELECT count(*) from PGIT_END_APPR_DTLS p where p.AED_READ_FLG is null and AED_PROD_CODE in  ('MED-PRD-047','MED-PRD-068') order by p.AED_CR_DT asc";
				Query nativeQuery = entityManager.createNativeQuery(qry);
				BigDecimal pendingCountDeci = (BigDecimal) nativeQuery.getSingleResult();
				pendingCount = pendingCountDeci.longValue();
			} catch(Exception e){
				e.printStackTrace();
			}
			return pendingCount;
		}
	 
	 public Product getProrataForProduct(Long productId)
		{
		//	Product a_mastersValue = new MastersValue();
			//if (a_key != null) {
		 Product product = null;
		 try{
			 Query query = entityManager
					 .createNamedQuery("Product.findByKey");
			 query = query.setParameter("key", productId);
			 List<Product> productValList = query.getResultList();
			 if(null != productValList && !productValList.isEmpty())
			 {
				 product = productValList.get(0);
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return product;	
		}
	 
	 public List<GalaxyIntimation> findDuplicateInitmation(String admissionDate, Long hospitalKey, String policyNumber, String insuredId) {
		 Query query = entityManager.createNamedQuery("GalaxyIntimation.findDuplicateInitmation");
		 query = query.setParameter("hospital", hospitalKey);
		 query = query.setParameter("policy", policyNumber);
		 query = query.setParameter("admissionDate",admissionDate);
		 query = query.setParameter("insuredId", Long.valueOf(insuredId));
		 List<GalaxyIntimation> insuredList = query.getResultList();
		 return insuredList;
	 }

	 public List<PremiaPreviousClaim> findPremiaIntimationDuplicate(String admissionDate, String hospitalCode, String policyNumber,String insuredId) {
		 Query query = entityManager.createNamedQuery("PremiaPreviousClaim.findForDuplicate");
		 query = query.setParameter("hospitalCode", hospitalCode.toLowerCase());
		 query = query.setParameter("policyNumber", policyNumber);
		 query = query.setParameter("admittedDate",admissionDate);
		 query = query.setParameter("riskId", Long.valueOf(insuredId));
		 List<PremiaPreviousClaim> insuredList = (List<PremiaPreviousClaim>)query.getResultList();
		 return insuredList;
	 }
	 public GalaxyCRMIntimation saveCRMIntimation(GalaxyCRMIntimation crmIntimation) {
		 try {
			 utx.begin();
			 entityManager.persist(crmIntimation);
			 entityManager.flush();
			 entityManager.refresh(crmIntimation);
			 utx.commit();
		 }catch(Exception e) {
			 try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException | SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
		 return crmIntimation;
	 }
	 
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
		public void updateGPAPolicyEndorsement(Policy policy,List<Insured> insuredList) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException
		{
			Policy policy2 = getPolicy(policy.getPolicyNumber());
			if (null != policy2) {
				if(policy2.getKey() != null){
					policy.setKey(policy2.getKey());
				}
				entityManager.merge(policy);
				entityManager.flush();
				
				List<PolicyRiskCover> policyRiskCoverDetails = policy.getPolicyRiskCoverDetails();
				if(policyRiskCoverDetails != null && !policyRiskCoverDetails.isEmpty()){
					List<PolicyRiskCover> riskCoverByPolicy = getRiskCoverByPolicy(policy.getKey());
					Map<String, PolicyRiskCover> riskCoverMap = new WeakHashMap<String, PolicyRiskCover>();
					for (PolicyRiskCover policyRiskCover : riskCoverByPolicy) {
						riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
					}
					for (PolicyRiskCover policyRiskCover : policyRiskCoverDetails) {
						PolicyRiskCover policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
						if(policyRiskCover2 != null){
							policyRiskCover.setKey(policyRiskCover2.getKey());
						}
						policyRiskCover.setPolicyKey(policy.getKey());
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
					List<PolicyCoverDetails> policyCoverDetails2 = getPolicyCoverDetails(policy.getKey());
					Map<String, PolicyCoverDetails> riskCoverMap = new WeakHashMap<String, PolicyCoverDetails>();
					for (PolicyCoverDetails policyRiskCover : policyCoverDetails2) {
						riskCoverMap.put(policyRiskCover.getCoverCode(), policyRiskCover);
					}
					for (PolicyCoverDetails policyRiskCover : policyCoverDetails) {
						PolicyCoverDetails policyRiskCover2 = riskCoverMap.get(policyRiskCover.getCoverCode());
						if(policyRiskCover2 != null){
							policyRiskCover.setKey(policyRiskCover2.getKey());
						}
						policyRiskCover.setPolicyKey(policy.getKey());
						if(policyRiskCover.getKey() == null){
							entityManager.persist(policyRiskCover);
							entityManager.flush();
						} else {
							entityManager.merge(policyRiskCover);
							entityManager.flush();
						}
					}
				}
				
			List<GpaBenefitDetails> gpaBenefitDetails = policy.getGpaBenefitDetails();
			if(gpaBenefitDetails != null && !gpaBenefitDetails.isEmpty()){
				List<GpaBenefitDetails> policyBenefitDetails = getPolicyBenefitDetails(policy2.getKey());
				Map<String, GpaBenefitDetails> riskCoverMap = new WeakHashMap<String, GpaBenefitDetails>();
				if(policyBenefitDetails != null && ! policyBenefitDetails.isEmpty()){
					for (GpaBenefitDetails existingBenefitDetail : policyBenefitDetails) {
						riskCoverMap.put(existingBenefitDetail.getBenefitCode(),existingBenefitDetail);
					}
				}
				for (GpaBenefitDetails gpaBenefitDetails2 : gpaBenefitDetails) {
					GpaBenefitDetails gpaBenefitDetails3 = riskCoverMap.get(gpaBenefitDetails2.getBenefitCode());
					if(gpaBenefitDetails3 !=null){
					gpaBenefitDetails2.setKey(gpaBenefitDetails3.getKey());
					}
					gpaBenefitDetails2.setPolicyKey(policy2.getKey());
						if(gpaBenefitDetails2.getKey() == null){
							entityManager.persist(gpaBenefitDetails2);
							entityManager.flush();
						}else{
							entityManager.merge(gpaBenefitDetails2);
							entityManager.flush();
						}
					}
				}
			
			policy2 = policy;
			
			if(policy.getGpaPolicyType() != null && !policy.getGpaPolicyType().equalsIgnoreCase(SHAConstants.GPA_UN_NAMED_POLICY_TYPE)){
				if(insuredList != null && ! insuredList.isEmpty()){
					for (Insured insuredPA : insuredList) {
						
						List<Insured> existingInsured = getInsuredByPolicyAndRiskId(policy2.getPolicyNumber(), insuredPA.getInsuredId());
						for (Insured depInsured : existingInsured) {
							if((depInsured.getLopFlag() != null && depInsured.getLopFlag().equalsIgnoreCase("P"))){
								insuredPA.setKey(depInsured.getKey());
							}
					     }
							insuredPA.setPolicy(policy2);
							if(policy2.getDeductibleAmount() != null){
								insuredPA.setDeductibleAmount(policy2.getDeductibleAmount());
							}
							if(insuredPA != null){
								if(insuredPA.getKey() != null){
									entityManager.merge(insuredPA);
									entityManager.flush();
									
								}else{
									entityManager.persist(insuredPA);
									entityManager.flush();
									
								}
								
								entityManager.refresh(insuredPA);
							}
							
							
							if(insuredPA.getCoverDetailsForPA() != null && ! insuredPA.getCoverDetailsForPA().isEmpty()){
								List<InsuredCover> insuredCoverByInsuredKey = getInsuredCoverByInsuredKey(insuredPA.getKey());
								Map<String, InsuredCover> insuredCoverMap = new WeakHashMap<String, InsuredCover>();
								for (InsuredCover insuredCover : insuredCoverByInsuredKey) {
									insuredCoverMap.put(insuredCover.getCoverCode(), insuredCover);
								}
								for (InsuredCover insuredCoverPA : insuredPA.getCoverDetailsForPA()) {
									InsuredCover insuredCover = insuredCoverMap.get(insuredCoverPA.getCoverCode());
									if(insuredCover != null){
										insuredCoverPA.setKey(insuredCover.getKey());
										insuredCoverMap.remove(insuredCover);
									}
									insuredCoverPA.setInsuredKey(insuredPA.getKey());
									if(insuredCoverPA.getKey() == null){
										entityManager.persist(insuredCoverPA);
										entityManager.flush();
									} else {
										entityManager.merge(insuredCoverPA);
										entityManager.flush();
									}
								}
								if(!insuredCoverMap.isEmpty()){
									if(insuredCoverMap != null && ! insuredCoverMap.isEmpty()){
										Set<Entry<String, InsuredCover>> entrySet = insuredCoverMap.entrySet();
										for (Map.Entry<String, InsuredCover> entry : entrySet)  {
											InsuredCover insuredCover = (InsuredCover)entry.getValue();
											insuredCover.setDeletedFlag("Y");
								            entityManager.merge(insuredCover);
								            entityManager.flush();
									      }
									}
								}
							}
						}
					}
				}
			}
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
	 
	 public Boolean isICRBatchProcessed(){
		 Boolean isDone = Boolean.FALSE;
		 try{
				Query query = entityManager.createNamedQuery("IncurredClaimRatioBatchMonitor.findBySysDate");
				
				List<IncurredClaimRatioBatchMonitor> resultList = (List<IncurredClaimRatioBatchMonitor>) query.getResultList();
				
				if(resultList != null && !resultList.isEmpty()){
					isDone = Boolean.TRUE;
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return isDone;
		 
	 }
	 
	//Added For Accident Trauma care Group product		
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public Boolean populatePolicyFromPAPolicy(PremPolicyDetails premPolicyDetails,String riskSysID,Boolean isEndorsement) {
			Boolean isAllowed = true;
			try {
			//tmpPolicy = policyService.findTmppolicyByPolicyNo(tmpPolicy.getPolNo());
			 Policy policy = null;
			
			 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) {
	    		 isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.FLOATER_POLICY);
	 	     } else {
	 	    	isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
	 	     }
			 
			 if(!isAllowed) {
				 try {
					 utx.begin();
					 Policy existingPolicy = getPolicy(premPolicyDetails.getPolicyNo());
					 if (existingPolicy != null){
						 List<Intimation> intimationByPolicyKey = getIntimationByPolicyKey(existingPolicy.getKey());
						 if(intimationByPolicyKey != null && !intimationByPolicyKey.isEmpty()){
							 log.info("############# Intrgration Policy Part If intimation is existing for this policy then we will allow further ------>>>>>"+premPolicyDetails.getPolicyNo());
							 isAllowed = true;
						 }
					 }
					 utx.commit();
				 } catch (Exception e){
					 log.error("############# Error while processing the integration part ----->>>"+premPolicyDetails.getPolicyNo());
					 utx.rollback();
				 }
			 }
			
			if(isAllowed) {
				if(! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE) && 
						!ReferenceTable.getGMCProductCodeList().containsKey(premPolicyDetails.getProductCode())
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.JET_PRIVILEGE_PRODUCT_CODE)
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
						&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)){
						 policy = populatePolicyDataForPA(premPolicyDetails, true,isEndorsement);

						if(policy == null) {
							log.error("############## POLICY NOT SAVED DUE TO PRODUCT CODE IS NOT AVAILABLE IN OUR DB ##############----->" + premPolicyDetails.getPolicyNo());
							return !isAllowed;
						}
						if(premPolicyDetails.getPolicyType() == null || (premPolicyDetails.getPolicyType() != null && !premPolicyDetails.getPolicyType().equalsIgnoreCase(SHAConstants.PORTABILITY_POLICY))){
							List<PremPreviousPolicyDetails> previousPolicyDetails = premPolicyDetails.getPreviousPolicyDetails();
							if(previousPolicyDetails != null){
							for (PremPreviousPolicyDetails previousPolicy : previousPolicyDetails) {
								try {
									Policy previousPolicyObj = getPolicy(previousPolicy.getPolicyNo());
									if(previousPolicyObj == null) {
										PremPolicyDetails policyDetails = fetchPolicyDetailsFromPremia(previousPolicy.getPolicyNo());
										if(policyDetails != null){
											populatePolicyDataForPA(policyDetails, false,isEndorsement);
										}

									}
								} catch(Exception e) {
									log.error("$$$$$$$$$$$$$$ PREVIOUS POLICY DETAILS ERROR------MIGHT BE PRODUCT CODE--------->" + previousPolicy.getPolicyNo());
								}
								
							}
						 }
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(null != premPolicyDetails)
					log.error("#*#*#*#*#*#*#*#*#*#ERROR OCCURED IN populatePolicyFromTmpPolicy METHOD *#*#*#*#*#*#*#*#*#*#*#--------->" + premPolicyDetails.getPolicyNo());
			}
			return isAllowed;
		}
		
		//Added For Accident Trauma care Group product		 
	 @SuppressWarnings({ "static-access", "deprecation" })
		private Policy populatePolicyDataForPA(PremPolicyDetails premPolicyDetails, Boolean isCurrentPolicy,Boolean isEndorsement) throws IllegalStateException, SecurityException, SystemException {
			try {
				utx.setTransactionTimeout(36000);
				utx.begin();
				Boolean isBaNCS = Boolean.FALSE;
				if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
					isBaNCS = Boolean.TRUE;
				}
				
				Policy policy = getPolicy(premPolicyDetails.getPolicyNo());
				/*if (null == policy || ReferenceTable.getGMCProductCodeList().containsKey(premPolicyDetails.getProductCode()) ||  
						premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)||
						premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)) {*/

				Policy existingPolicy = null;
				if(policy != null && isEndorsement){
					existingPolicy =  policy;
				}
				
					 PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
					 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
					 Double totalAmount = 0d;
					 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
					 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
					 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
					 policy.setTotalPremium(totalAmount);
					 
					 //FIX FOR OVERRIDING OF CHECQUE STATUS DURING ENDORSEMENT BATCH
					 if(existingPolicy != null && isEndorsement){
						 if(existingPolicy.getChequeStatus() != null){
							 policy.setChequeStatus(existingPolicy.getChequeStatus());
						 }
						 if(existingPolicy.getRestoredSI() != null){
							 policy.setRestoredSI(existingPolicy.getRestoredSI());
						 }
						 if(existingPolicy.getRechargeSI() != null){
							 policy.setRechargeSI(existingPolicy.getRechargeSI());
						 }
					 }
					 
					 
					 try{
						 if(premPolicyDetails.getDeductiableAmt() != null){
							 policy.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getDeductiableAmt()).doubleValue());
						 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
					 
					 if(premPolicyDetails.getPolSysId() != null){
						 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
				
						 policy.setPolicySystemId(polSysId);
					 }
					 
					 if(premPolicyDetails.getProposerGender() != null){
						 if(premPolicyDetails.getProposerGender().equalsIgnoreCase("M")){
							 MastersValue master = getMaster(ReferenceTable.MALE_GENDER);
							 policy.setProposerGender(master);
						 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("F")){
							 MastersValue master = getMaster(ReferenceTable.FEMALE_GENDER);
							 policy.setProposerGender(master);
						 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("TG")){
							 MastersValue master = getMaster(ReferenceTable.TRANS_GENDER);
							 policy.setProposerGender(master);
						 }
					 }
					 if(premPolicyDetails.getPortedYN() != null){
						 policy.setPortedPolicy(premPolicyDetails.getPortedYN());
					 }
					 
					 if(!isCurrentPolicy) {
						//IMSSUPPOR-31281 - Sequence mismatch with SysId and policyKey
						Policy clsPolicy = getPolicyDetails(policy.getPolicySystemId());
						if(clsPolicy == null){
							policy.setKey(policy.getPolicySystemId()); 
						}
					 }
					 
					 if(premPolicyDetails.getPolicyPlan() != null){
	 					 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
							 policy.setPolicyPlan("A");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
							 policy.setPolicyPlan("B");
	 					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
	 							 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
							 policy.setPolicyPlan("G");
						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
							 policy.setPolicyPlan("S");
						 }//Added for MED-PRD-072
						 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
							 policy.setPolicyPlan("B");
						 }
	 					 
	 					 //MED-PRD-076
	 					if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
	 						if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
	 							 policy.setPolicyPlan("B");
	 						 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
	 							 policy.setPolicyPlan("E");
	 	 					 }
	 					}
	 					 
					 }
					 
					 if(premPolicyDetails.getSectionCode() != null && premPolicyDetails.getSectionCode().equalsIgnoreCase(SHAConstants.JET_PRODUCT_SECTION_CODE)){
						 policy.setPolicyPlan("A");
					 }
					 
					 if(premPolicyDetails.getPolicyTerm() != null) {
						 // Below Cond for SCRC - MED-PRD-070 - R201811302
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED.equals(premPolicyDetails.getProductCode())){
							 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
								 String policyTermYear[] = premPolicyDetails.getPolicyTerm().split(" ");
								 String policyTerm = policyTermYear[0];
								 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTerm));
							 }
						 }
						 else if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
							 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
							 policy.setPolicyTerm(policyTerm);
						 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(2l);
						 }
						 //added for new product076
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
							 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) { 
								 String policyTermYear = SHAUtils.getTruncateWord(premPolicyDetails.getPolicyTerm(), 0, 1);
								 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTermYear));
							 }
						 }
							//Added for MED-PRD-083
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
						 policy.setPolicyTerm(1l);
					 }
						 if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96.equals(premPolicyDetails.getProductCode())) {
							 policy.setPolicyTerm(1l);
						 }
						 
					 }
					 
					//Added for BaNCS
					 if(premPolicyDetails.getPolicySource() == null || !premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
						 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
						 if(policyStrYear != null){
							 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
						 }
					 }
					 
					 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)){
						 if(isBaNCS){
							 policy.setCreatedDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
						 }else{
							 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
						 }
					 }
					 
					 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true){
						 if(isBaNCS){
							 policy.setPolicyFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
						 }else{
							 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
						 }
					 }
					 
					 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true){
						 if(isBaNCS){
							 policy.setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyEndDate()));
						 }else{
							 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
						 }
					 }
					 
					 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true){
						 if(isBaNCS){
							 policy.setReceiptDate(SHAUtils.formatTimeFromString(premPolicyDetails.getReceiptDate()));
						 }else{
							 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
						 }
					 }
					 
					 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true){
						 if(isBaNCS){
							 policy.setProposerDob(SHAUtils.formatTimeFromString(premPolicyDetails.getProposerDOB()));
						 }else{
							 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
						 }
					 }
					 
					 if(null != premPolicyDetails.getLob() && null != getMasterByValue(premPolicyDetails.getLob()))
						 policy.setLobId(getMasterByValue((premPolicyDetails.getLob())).getKey());
					 
					 
					 if(null != getMasterByValue(premPolicyDetails.getPolicyType()))
					 policy.setPolicyType(getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
					 
	 				 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
						 MastersValue schemeId = getMasterByValue(premPolicyDetails.getSchemeType());
						 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
					 }
	 				 
					//TODO:Get product type from premia 
//					 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//					 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
					 
					 Product productByProductCodeForPremia = getProductByProductCode(premPolicyDetails.getProductCode(),new Date(premPolicyDetails.getPolicyStartDate()));
					 if(productByProductCodeForPremia == null) {
						 return null;
					 }
					 
					 //Added for BaNCS - Commented the code as per satish sir request and setted the policySource default to P.
					/* if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
						 policy.setLobId(Long.parseLong(productByProductCodeForPremia.getLobId()));
						 
						 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
							 List<MastersValue> schmList = findByMasterListKeyList("PRDSCHM");
								for(int k=0;k<schmList.size();k++) {
									if(schmList.get(k).getValue().replace("+", "").equalsIgnoreCase(premPolicyDetails.getSchemeType())) {
										policy.setSchemeId(schmList.get(k).getKey());
										break;
									}
								}
						 }
						 
						 policy.setProposerSubDist(premPolicyDetails.getSubDistrict());
						 policy.setOfficeTelephone(premPolicyDetails.getBaNCSofficeTelPhone());
						 
						 policy.setPremiumTax(Double.valueOf(premPolicyDetails.getBaNCSPremiumTax()));
						 policy.setStampDuty(Double.valueOf(premPolicyDetails.getBaNCSStampDuty()));
						 policy.setTotalPremium(Double.valueOf(premPolicyDetails.getBaNCSTotalPremium()));
						 
						 policy.setOfficeEmailId(premPolicyDetails.getBaNCSOfficeEmailId());
						 policy.setOfficeFax(premPolicyDetails.getBaNCSOfficeFax());
						 policy.setPolicySource(SHAConstants.BANCS_POLICY);
						 
					 }*/
					 policy.setPolicySource(SHAConstants.PREMIA_POLICY);
					 policy.setProduct(productByProductCodeForPremia);
					 
					 //Added for MED-PRD-072
					 if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_72) || premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87) || premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_012)) && 
							 policy.getPolicyPlan() != null){
						 Product productByProductKeyForPremia = null;
						 
						 /**
						  * As per Satish Sirs Suggestion Product is defaulted to Basic
						  * 
						  */
						 if(premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87)){
							 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY_98);
						 }else{
						 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY);
						 }
						 // BELOW  Code is commented because Policy Plan was changed from policy level to insured level for this Product SHAConstants.PRODUCT_CODE_72
						 /*if(policy.getPolicyPlan().equals("B")){
							 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY);
						 }
						 else if(policy.getPolicyPlan().equals("G")){
							 productByProductKeyForPremia = getProrataForProduct(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY);
						 }*/
						 
						 
						 
						 if(productByProductKeyForPremia != null){
							 policy.setProduct(productByProductKeyForPremia);
						 }
					 }
					 
					 policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
					 
					 List<PolicyEndorsementDetails> endorsementDetailsList = new ArrayList<PolicyEndorsementDetails>();
					 if(premPolicyDetails.getEndorsementDetails() != null){
					 endorsementDetailsList = premiaPolicyMapper .getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
					 }
					 
					 List<PreviousPolicy> previousPolicyList = new ArrayList<PreviousPolicy>();
				     if(premPolicyDetails.getPreviousPolicyDetails() != null){
					 previousPolicyList = premiaPolicyMapper.getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
				     }
					 //set Endrosement Date
				     if(premPolicyDetails.getEndorsementDetails() != null){
					 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
						if(isBaNCS){
							endorsementDetailsList.get(index).setEffectiveFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
							endorsementDetailsList.get(index).setEndoresementDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						}else{
							endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
							endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						}
						 }
						 //Not null handled for BANCS
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt() != null){
							 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
								 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
							 }
						 }
					 }
				     }
					 //Set previous policy Date
				     if(premPolicyDetails.getPreviousPolicyDetails() != null){
					 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
						 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
							 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
						 }
				     }
					 //Set previous policy Date
					 if(premPolicyDetails.getPreviousPolicyDetails() != null){
						 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
							 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
								 if(isBaNCS){
									 previousPolicyList.get(index).setPolicyFrmDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
								 }else{
									 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
								 }
							 }
							 if(null == premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()||premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().isEmpty() ? false : true){
								 if(isBaNCS){
									 previousPolicyList.get(index).setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
								 }else{
									 previousPolicyList.get(index).setPolicyToDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
								 }
							 }
						 }
					  }
				     }
				     
				     List<Insured> insured = new ArrayList<Insured>();
					 if(premPolicyDetails.getInsuredDetails() != null){
						 
						 List<PremInsuredDetails> premiaInsuredList = premPolicyDetails.getInsuredDetails();
						 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
						 
						 if(premiaInsuredList != null && !premiaInsuredList.isEmpty()) {
							 for (PremInsuredDetails premInsuredDetails : premiaInsuredList) {
								
								 for (Insured insuredObj : insured) {
									 if(insuredObj.getInsuredId() != null && !StringUtils.isBlank(premInsuredDetails.getRiskSysId())){
										 if(insuredObj.getInsuredId().equals(Long.valueOf(premInsuredDetails.getRiskSysId()))){
											 
											 if(premInsuredDetails.getPolicyPlan() != null){
												 if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
													 insuredObj.setPolicyPlan("B");
												 }
												 // Added For MED-PRD-83
												 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) 
														 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
													 insuredObj.setPolicyPlan("S");
												 }
												 else if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
														 || premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
													 insuredObj.setPolicyPlan("G");
												 }
											 }
										 }
									 }
								 }
							}
							 
						 }
						 
						 
						 
					 }else if(premPolicyDetails.getJpInsuredDetails() != null){
						 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getJpInsuredDetails());
						 for (Insured insured2 : insured) {
							 insured2.setLopFlag("H");
							 
							 if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
									MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
									if(genderMaster != null && genderMaster.getKey() != null){
										insured2.setInsuredGender(genderMaster);
									}
								}else
								{
									MastersValue genderMaster =  getKeyByValue("MALE");
									if(genderMaster != null){
										insured2.setInsuredGender(genderMaster);
									}
								}
							 
							if(insured2.getStrInsuredAge() != null && ! insured2.getStrInsuredAge().isEmpty()){
								Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
								insured2.setInsuredAge(insuredAge);
							}
							if(insured2.getStrDateOfBirth() != null && ! insured2.getStrDateOfBirth().isEmpty()){
								 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
								 if(insuredDOB != null) {
									 insured2.setInsuredDateOfBirth(insuredDOB);
								 }
							}
							
							/**This code added for CR20181258 - Criticare Platinum since nominee details insertion is not handled for Health claim **/
							List<PremInsuredDetails> premiaInsureObj = premPolicyDetails.getJpInsuredDetails();
							if(premiaInsureObj != null && ! premiaInsureObj.isEmpty()){
								for (PremInsuredDetails premiaInsured : premiaInsureObj) {
									List<NomineeDetails> nomineeDetailsForHealth = new ArrayList<NomineeDetails>();
									
									List<PolicyNominee> proposerNomineeDetailsForHealth = new ArrayList<PolicyNominee>();
									
									List<GmcContinuityBenefit> continuityBenefits = new ArrayList<GmcContinuityBenefit>();
									
									List<InsuredPedDetails> pedDetails = new ArrayList<InsuredPedDetails>();
									
									if(premiaInsured.getNomineeDetails() != null && ! premiaInsured.getNomineeDetails().isEmpty()){
										for (PremInsuredNomineeDetails nomineeObj : premiaInsured.getNomineeDetails()) {
											NomineeDetails nomineeDetail = new NomineeDetails();
											PolicyNominee proposerNomineeDetail = new PolicyNominee();
											
											if(nomineeObj.getNomineeAge() != null && ! nomineeObj.getNomineeAge().isEmpty()){
												nomineeDetail.setNomineeAge(Long.valueOf(nomineeObj.getNomineeAge()));
												proposerNomineeDetail.setNomineeAge(Integer.valueOf(nomineeObj.getNomineeAge()));
											}
											nomineeDetail.setNomineeName(nomineeObj.getNomineeName());
											proposerNomineeDetail.setNomineeName(nomineeObj.getNomineeName());
											//TODO
											/*proposerNomineeDetail.setAccountNumber(nomineeObj.getAccountNumber());
											proposerNomineeDetail.setNameAsPerBank(nomineeObj.getBeneficiaryName());
											proposerNomineeDetail.setIFSCcode(nomineeObj.getIfscCode());*/
											nomineeDetailsForHealth.add(nomineeDetail);
											proposerNomineeDetailsForHealth.add(proposerNomineeDetail);
										}
										if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
											insured2.setNomineeDetails(nomineeDetailsForHealth);
											insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForHealth);
										}
									}
									
									/*Below Code for MED-PRD-074*/
									if(premiaInsured.getContinuityBenefits() != null && ! premiaInsured.getContinuityBenefits().isEmpty()){
										if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
											continuityBenefits = premiaPolicyMapper.getGMCInsuredContBenDetails(premiaInsured.getContinuityBenefits());
											insured2.setGmcContBenefitDtls(continuityBenefits);
										}
									}
									
									if(premiaInsured.getPedDetails() != null && !premiaInsured.getPedDetails().isEmpty()){
										if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
											pedDetails = premiaPolicyMapper.getInsuredPedFromPremia(premiaInsured.getPedDetails());
											insured2.setInsuredPedList(pedDetails);
										}
									}
									
									/*if(policy.getProduct() != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
									    List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

									        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
									        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
									        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
									        		InsuredCover riskCover = new InsuredCover();
									        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
									        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
									        		if(premPolicyRiskCover2.getSumInsured() != null){
									        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
									        		}
									        		
									        		insuredCoverList.add(riskCover);
												}
									        	if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
									        		insured2.setCoverDetailsForPA(insuredCoverList);
									        	}
										}
									}*/
								}
							}
					 	//  End of  CR CR20181258
						}
					 }

					 //set insured Date
					 List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
					 if(premPolicyDetails.getInsuredDetails() != null){
		 				 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
							MastersValue genderMaster =  getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
							 insured.get(index).setInsuredGender(genderMaster);
							
							 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
								 
								//Added for BaNCS
								 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
									 Date formatBaNCSDate = SHAUtils.formatTimeFromString(premPolicyDetails.getInsuredDetails().get(index).getDob());
									 if(formatBaNCSDate != null) {
										 insured.get(index).setInsuredDateOfBirth(formatBaNCSDate);
									 }
									 insured.get(index).setSourceRiskId(premPolicyDetails.getInsuredDetails().get(index).getBaNCSSourceRiskID());
								 }else{
									 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
									 //Added for insured age caluculation.
									 Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));
									 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
									 if(formatPremiaDate != null) {
										 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);
									 }
									 
								 }
								 
								 
								 
								 
							 }
							 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
					 		 
					 		 if(insuredAge != null){
					 			 insured.get(index).setInsuredAge(insuredAge);
					 		 } 
					 		 
					 		 insured.get(index).setLopFlag("H");
					 		 
					 		 if(insured.get(index).getSumInsured1() != null){
					 			insured.get(index).setSumInsured1Flag("Y");
					 		 }
					 		 else
					 		 {
					 			insured.get(index).setSumInsured1Flag("N");
					 		 }
					 		 
					 		if(insured.get(index).getSumInsured2() != null){
					 			insured.get(index).setSumInsured2Flag("Y");
					 		 }
					 		else
					 		{
					 			insured.get(index).setSumInsured2Flag("N");
					 		}
					 		
					 		if(insured.get(index).getSumInsured3() != null){
					 			insured.get(index).setSumInsured3Flag("Y");
					 		 }
					 		else
					 		{
					 			insured.get(index).setSumInsured3Flag("N");
					 		}
					 		
					 		//IMSSUPPOR-27387
					 		if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
					 			 try{
									 if(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt() != null){
										 insured.get(index).setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt()).doubleValue());
									 }
								 }catch(Exception e){
									 e.printStackTrace();
								 }
					 		}
						 }

					 }
					 

					 if(premPolicyDetails.getInsuredDetails() != null){
						 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
							 List<PremPEDDetails> pedDetailsList = premPolicyDetails.getInsuredDetails().get(index).getPedDetails();
							 if(pedDetailsList != null && !pedDetailsList.isEmpty()){
								 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(pedDetailsList));
							 }
								 
								 List<PremPortability> portablitityDetails = premPolicyDetails.getInsuredDetails().get(index).getPortablitityDetails();
								 
								 if(portablitityDetails != null){
									 List<PortablityPolicy> portablityList = premiaPolicyMapper.getPortablityList(portablitityDetails);
									 for (PortablityPolicy portablityPolicy : portablityList) {
										
										 try{
										 if(portablityPolicy.getStrDateOfBirth() != null && ! portablityPolicy.getStrDateOfBirth().equalsIgnoreCase("")){
											 portablityPolicy.setDateOfBirth(new Date(portablityPolicy.getStrDateOfBirth()));
										 }
										 if(portablityPolicy.getStrMemberEntryDate() != null && ! portablityPolicy.getStrMemberEntryDate().equalsIgnoreCase("")){
											 portablityPolicy.setMemberEntryDate(new Date(portablityPolicy.getStrMemberEntryDate()));
										 }
										 
										 if(portablityPolicy.getPolicyStrStartDate() != null && ! portablityPolicy.getPolicyStrStartDate().equalsIgnoreCase("")){
											 portablityPolicy.setPolicyStartDate(new Date(portablityPolicy.getPolicyStrStartDate()));
										 }
										 }catch(Exception e){
											 e.printStackTrace();
										 }
									}
									 insured.get(index).setPortablityPolicy(portablityList);
								 }
						 }
					 }
						 
					List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();				 
					int i=0;
	 				if(null != insured && !insured.isEmpty()) {
						for (Insured insured2 : insured) {
							if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
								Double totalSumInsured = policy.getTotalSumInsured();
								Double size = Double.valueOf(insured.size());
								Double sumInsured = 0d;
								if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_82)
										|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
									if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
										sumInsured = totalSumInsured/size;
									}
									Long roundOfSI = Math.round(sumInsured);
									insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
								}else{
									if(premPolicyDetails.getProductCode() == null){
										insured2.setInsuredSumInsured(policy.getTotalSumInsured());
									}
									else if(premPolicyDetails.getProductCode() != null && !premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
											&& !policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
											&& !policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
											&& !policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)) {
										insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
									}
								}
								
			//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
								
							} else {
			//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
							}
							
							if(premiaInsured != null){
							
								if (premiaInsured.get(i).getSelfDeclaredPed() != null
										&& premiaInsured.get(i).getSelfDeclaredPed()
												.length() > 0) {
									InsuredPedDetails selfDeclaredPed = new InsuredPedDetails();
									selfDeclaredPed.setInsuredKey(insured2
											.getInsuredId());
									selfDeclaredPed.setPedDescription(premiaInsured
											.get(i).getSelfDeclaredPed());
									List<InsuredPedDetails> pedList = new ArrayList<InsuredPedDetails>();
									pedList.add(selfDeclaredPed);
									try{
										if(insured2.getInsuredPedList() != null){
											insured2.getInsuredPedList().addAll(pedList);
										}else{
											insured2.setInsuredPedList(pedList);
										}
									}catch(Exception e){
										System.out.println("Insured Ped list exception");
										e.printStackTrace();
									}
									
								}
							}
							i++;
							 if(!isCurrentPolicy) {
								    Insured clsInsured = getCLSInsured(insured2.getInsuredId());
								    if(clsInsured == null){
								    	insured2.setKey(insured2.getInsuredId()); 
								    }
							 }
							 
							 //Added for BaNCS
							 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
								 Query query = entityManager.createNativeQuery("select SEQ_B_INSURED_NO.nextval from dual");
								 insured2.setInsuredId(Long.parseLong(query.getResultList().get(0).toString()));		
							 }
						}
					    policy.setInsured(insured);
					}
				    
	 		        if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
			        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
	 		        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE) 
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_83)
	 		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			        	
			        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) { 
			     	    	
			        		 MastersValue productType = getMaster(ReferenceTable.FLOATER_POLICY);
			        		 policy.setProductType(productType);
			        		 
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null){
			        			 policy.setProduct(product);
			        		 }
			     	    } else {
			     	    	
			     	    	 MastersValue productType = getMasterValue(ReferenceTable.INDIVIDUAL_POLICY);
			        		 policy.setProductType(productType);
			        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
			        		 if(product != null) {
			        			 policy.setProduct(product);
			        		 }
			     	    }
			        }
			        List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

			        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
			        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
			        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
			        		PolicyRiskCover riskCover = new PolicyRiskCover();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		if(premPolicyRiskCover2.getSumInsured() != null){
			        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
			        		}
			        		
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
			        }
			        
			       /* if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
			        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
			        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
							if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
//								policyBankDetails.setEffectiveFrom(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveFrom()));
							}
	 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
								policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
//								policyBankDetails.setEffectiveTo(SHAUtils.formatDateWithoutTime(policyBankDetails.getStrEffectiveTo()));
							}
						}
			        	policy.setPolicyBankDetails(bankDetailsFromPremia);
			        }*/
			        
			        //Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					List<ZUAViewQueryHistoryTableDTO> listOfQueryHistory = new ArrayList<ZUAViewQueryHistoryTableDTO>();

					if (premPolicyDetails.getPolicyNo() != null) {
						//Commented as per SATHISH SIR in Batch
						/*DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(premPolicyDetails.getPolicyNo());
	<<<<<<< HEAD
						if (policyObj != null) {
							if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								ClaimProvisionService.getInstance().callBankDetailsService(null, null);
							}else{
									 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
								        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
								        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
												if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("")){
													policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
												}
						 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("")){
													policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
												}
											}
								        	policy.setPolicyBankDetails(bankDetailsFromPremia);
								        }
							}
						}
	=======
						if (policyObj != null) {*/
							//Commented as per SATHISH SIR in Batch
							//if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.PREMIA_POLICY)) {
								 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
							        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
							        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
											if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("") && !policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("null") ){
												policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
											}
					 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("") && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("null")){
												policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
											}
										}
							        	policy.setPolicyBankDetails(bankDetailsFromPremia);
							        }
								
							/*} else if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								ClaimProvisionService.getInstance().callBankDetailsService();
							}*/
								 //}
					}
					//Bancs Changes End
	  		        List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
			        List<Insured> insuredPAFromPremia = new ArrayList<Insured>();
			        if(premiaInsuredPAdetails != null){
			        	insuredPAFromPremia = premiaPolicyMapper.getInsuredPAFromPremia(premiaInsuredPAdetails);
			        }
			        for (Insured insured2 : insuredPAFromPremia) {
						if(insured2.getStrInsuredAge() != null){
							Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());     
							insured2.setInsuredAge(insuredAge);
						}
						if(insured2.getStrDateOfBirth() != null){
						
						 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
						 if(insuredDOB != null) {
							 insured2.setInsuredDateOfBirth(insuredDOB);
						 }
						}
						if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
							MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
							if(genderMaster != null && genderMaster.getKey() != null){
								insured2.setInsuredGender(genderMaster);
							}
						}
						/**
						 * As per Sathish ,if gender is blank  from premia, then
						 *  default it to MALE.
						 * */
						else
						{
							MastersValue genderMaster =  getKeyByValue("MALE");
							if(genderMaster != null){
								insured2.setInsuredGender(genderMaster);
							}
						}
						insured2.setLopFlag("P");
						
					}
			        
			        if(insuredPAFromPremia != null){
			        	policy.setInsuredPA(insuredPAFromPremia);
			        }

			        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
			        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
			        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
							if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
								for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
									InsuredCover insuredCover = new InsuredCover();
									insuredCover.setCoverCode(coverDetails.getCoverCode());
	 								insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
									if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
										insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
									}
									insuredCoverDetailsForPA.add(insuredCover);
									
									
								}
								List<Insured> insuredPA = policy.getInsuredPA();
								if(insuredPA != null){
									for (Insured insured2 : insuredPA) {
										
										MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
										if (genderMaster != null &&  genderMaster.getKey() != null) {
											
											insured2.setInsuredGender(genderMaster);
										}
										
										
										if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
											insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
											break;
										}
									}
								}
							}
							
							if(policy.getProduct() != null && (policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)
									|| policy.getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY))){
							    List<PremPolicyRiskCover> premPolicyCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

							        if(premPolicyCover != null && ! premPolicyCover.isEmpty()){
							        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
							        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyCover) {
							        		InsuredCover riskCover = new InsuredCover();
							        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
							        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
							        		if(premPolicyRiskCover2.getSumInsured() != null){
							        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
							        		}
							        		
							        		insuredCoverList.add(riskCover);
										}
							        	
							        	List<Insured> insuredPA = policy.getInsuredPA();
										if(insuredPA != null){
											for (Insured insured2 : insuredPA) {		
												if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
													insured2.setCoverDetailsForPA(insuredCoverList);
												}
											}
										}
							        
								}
							}
						}
			        }
	      
			        
			        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
			        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
			        		 List<NomineeDetails> nomineeDetailsForPA = new ArrayList<NomineeDetails>();
			        		 List<PolicyNominee> proposerNomineeDetailsForPA = new ArrayList<PolicyNominee>();
			        		 
							if(premiaInsured1.getNomineeDetails() != null && ! premiaInsured1.getNomineeDetails().isEmpty()){
								for (PremInsuredNomineeDetails coverDetails : premiaInsured1.getNomineeDetails()) {
									NomineeDetails nomineeDetail = new NomineeDetails();
									PolicyNominee proposerNomineeDetail = new PolicyNominee();
									if(coverDetails.getNomineeAge() != null && ! coverDetails.getNomineeAge().isEmpty()){
										nomineeDetail.setNomineeAge(Long.valueOf(coverDetails.getNomineeAge()));
										proposerNomineeDetail.setNomineeAge(Integer.valueOf(coverDetails.getNomineeAge()));
									}
									nomineeDetail.setNomineeName(coverDetails.getNomineeName());
									proposerNomineeDetail.setNomineeName(coverDetails.getNomineeName());
									proposerNomineeDetail.setRelationshipWithProposer(coverDetails.getNomineeRelation());
									//TODO
									/*proposerNomineeDetail.setAccountNumber(coverDetails.getAccountNumber());
									proposerNomineeDetail.setNameAsPerBank(coverDetails.getBeneficiaryName());
									proposerNomineeDetail.setIFSCcode(coverDetails.getIfscCode());*/
									nomineeDetailsForPA.add(nomineeDetail);
									proposerNomineeDetailsForPA.add(proposerNomineeDetail);
									
									
								}
								List<Insured> insuredPA = policy.getInsuredPA();
								if(insuredPA != null){
									for (Insured insured2 : insuredPA) {
										if(insured2.getHealthCardNumber().equalsIgnoreCase(premiaInsured1.getIdCardNumber())){
											insured2.setNomineeDetails(nomineeDetailsForPA);
											insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForPA);
										}
									}
								}
							}
						}
			        }

			        /*if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
			        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
			        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
			        		PolicyRiskCover riskCover = new PolicyRiskCover();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
			        }*/
			        
			        /**
			         * Get Policy Cover details
			         */
			        List<PremPolicyCoverDetails> premPolicyCoverDetails = premPolicyDetails.getPremPolicyCoverDetails();

			        if(premPolicyCoverDetails != null && ! premPolicyCoverDetails.isEmpty()){
			        	List<PolicyCoverDetails> policyRiskCoverList = new ArrayList<PolicyCoverDetails>();
			        	for (PremPolicyCoverDetails premPolicyRiskCover2 : premPolicyCoverDetails) {
			        		PolicyCoverDetails riskCover = new PolicyCoverDetails();
			        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
			        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
			        		if(premPolicyRiskCover2.getSumInsured() != null){
			        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
			        		}
			        		if(premPolicyRiskCover2.getRiskId() != null){
			        			riskCover.setRiskId(Long.valueOf(premPolicyRiskCover2.getRiskId()));
			        		}
			        		policyRiskCoverList.add(riskCover);
						}
			        	policy.setPolicyCoverDetails(policyRiskCoverList);
			        }
			        
			        if(premPolicyDetails.getPolicyZone() != null){
						 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
					}
			        
			        List<PremInsuredNomineeDetails> premProposerNomineeDetails = premPolicyDetails.getProperNomineeDetails();
			        
			        if(premProposerNomineeDetails != null && !premProposerNomineeDetails.isEmpty()){
			        	List<PolicyNominee> proposerNomineeDetails = premiaPolicyMapper.getProposerInsuredNomineeDetails(premProposerNomineeDetails);
			        	for (PolicyNominee proposerNominee : proposerNomineeDetails) {
							if(proposerNominee.getStrNomineeDOB() != null && !proposerNominee.getStrNomineeDOB().isEmpty()){
								if(isBaNCS){
									proposerNominee.setNomineeDob(SHAUtils.formatTimeFromString(proposerNominee.getStrNomineeDOB()));
								}else{
									proposerNominee.setNomineeDob(new Date(proposerNominee.getStrNomineeDOB()));
								}
							}
						}
			        	
			        	policy.setProposerNomineeDetails(proposerNomineeDetails);
			        	
			        }
			        /* Below code for product code - MED-PRD-073*/
			        if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
			        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))) {
			        
					        
					        if(premPolicyDetails.getGmcAilmentLimit() != null){
								List<MasAilmentLimit> ailmentLimit = premiaPolicyMapper.getAilmentLimit(premPolicyDetails.getGmcAilmentLimit());
								policy.setAilmentDetails(ailmentLimit);
							}
							
							if(premPolicyDetails.getGmcDeliveryLimit() != null){
								List<MasDeliveryExpLimit> deliveryLimit = premiaPolicyMapper.getDeliveryExpLimits(premPolicyDetails.getGmcDeliveryLimit());
								policy.setDeliveryExpLimit(deliveryLimit);
							}
							
							if(premPolicyDetails.getGmcCopayLimit() != null){
								List<MasCopayLimit> copayLimit = premiaPolicyMapper.getCopayLimit(premPolicyDetails.getGmcCopayLimit());
								policy.setCopayLimit(copayLimit);
							}
							if(premPolicyDetails.getGmcPrePostHospLimit() != null){
								List<MasPrePostHospLimit> prePostLimit = premiaPolicyMapper.getPrepostLimit(premPolicyDetails.getGmcPrePostHospLimit());
								policy.setPrePostLimit(prePostLimit);
							}
							
							if(premPolicyDetails.getGmcRoomRentLimit() != null){
								List<MasRoomRentLimit> roomRentList = premiaPolicyMapper.getRoomRentLimit(premPolicyDetails.getGmcRoomRentLimit());
								policy.setRoomRentLimit(roomRentList);
							}
							
							List<GpaBenefitDetails> benefitDetailsList = new ArrayList<GpaBenefitDetails>();
							
							List<PremGmcBenefitDetails> gmcPolicyConditions = premPolicyDetails.getGmcPolicyConditions();
							if(gmcPolicyConditions != null){
								for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
									GpaBenefitDetails benefitDetails = new GpaBenefitDetails();
									benefitDetails.setBenefitCode(premGmcBenefitDetails.getConditionCode());
									benefitDetails.setBenefitDescription(premGmcBenefitDetails.getConditionDesc());
									benefitDetails.setBenefitLongDescription(premGmcBenefitDetails.getConditionLongDesc());
									benefitDetailsList.add(benefitDetails);
								}
							}
							policy.setGpaBenefitDetails(benefitDetailsList);
							
						if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty()){
						
							if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
								policy.setSectionCode(SHAConstants.GMC_SECTION_A);
								policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
							}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
								policy.setSectionCode(SHAConstants.GMC_SECTION_C);
								policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
							}
						}
							
							
			        }
			        
			        //MED-PRD-076
					if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
						Product product = null;
							if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty() && policy.getPolicyPlan() != null){
								if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("B")){
									product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
								}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("E")){
									//product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_E);
									product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
								}
								else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("B")){
									product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_B);
								}
								else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("E")){
									product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_E);
								}
							}
							if(product != null){
								policy.setProduct(product);
							}
							
							if(premPolicyDetails.getPhcDays() != null && !premPolicyDetails.getPhcDays().isEmpty()){
								policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getPhcDays()));
							}
							policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
						}
					//added for GHC hospital cash product CR
					if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
						if(premPolicyDetails.getGhcDays() != null && !premPolicyDetails.getGhcDays().isEmpty()){
							policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getGhcDays()));
						}
					}
					//added for installment payment status flag and instalment method update at policy level changes done
					if(premPolicyDetails.getInstalmentFlag() != null && !premPolicyDetails.getInstalmentFlag().isEmpty() &&
							premPolicyDetails.getInstalmentFlag().equals("1")){
						policy.setPolicyInstalmentFlag(SHAConstants.YES_FLAG);
					}else {
						policy.setPolicyInstalmentFlag(SHAConstants.N_FLAG);	
					}

					if(premPolicyDetails.getInstallementMethod() != null && !premPolicyDetails.getInstallementMethod().isEmpty()){
						policy.setPolicyInstalmentType(premPolicyDetails.getInstallementMethod());
					}

					//added for saving UIN in policy table
			        if(premPolicyDetails.getPremiaUINCode() != null && !premPolicyDetails.getPremiaUINCode().isEmpty()) {
			        	policy.setPremiaUINCode(premPolicyDetails.getPremiaUINCode());
			        }
					createGMCproduct(premPolicyDetails, policy, premiaPolicyMapper,isEndorsement);
					
					//added for corpBufferLimit New column insert
					if(premPolicyDetails.getGmcCorpBufferLimit() != null){
						List<GmcCoorporateBufferLimit> roomRentList = premiaPolicyMapper.getCorpBufferLimit(premPolicyDetails.getGmcCorpBufferLimit());
						policy.setGmcCorpBufferLimit(roomRentList);
					}
					if(premPolicyDetails.getNacbBuffer() != null && !premPolicyDetails.getNacbBuffer().isEmpty()) {
						policy.setNacbBufferAmt(Double.valueOf(premPolicyDetails.getNacbBuffer()));
					}
					if(premPolicyDetails.getVintageBuffer() != null && !premPolicyDetails.getVintageBuffer().isEmpty()) {
						policy.setWintageBufferAmt(Double.valueOf(premPolicyDetails.getVintageBuffer()));
					}
					//added for topup policy data saving process
					if(premPolicyDetails.getTopUpPolicyNo() != null && !premPolicyDetails.getTopUpPolicyNo().isEmpty()) {
						policy.setTopupPolicyNo(premPolicyDetails.getTopUpPolicyNo());
					}
			        createGMCproduct(premPolicyDetails, policy, premiaPolicyMapper,isEndorsement);
			        // gpa cover details
			        createGPAproduct(premPolicyDetails, policy, premiaPolicyMapper,isEndorsement);
			        
			        if(premPolicyDetails.getProductCode() != null && ! ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(premPolicyDetails.getProductCode()) 
			        		&& ! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
			        		&&	! premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)){
			        	if(isEndorsement){
			        		updatePolicyDetailsAfterEndorsement(policy, previousPolicyList, endorsementDetailsList);
			        	}else{
			        		Policy policy2 = getPolicy(policy.getPolicyNumber());
				        	if(policy2 == null){
				        		createPolicy(policy, previousPolicyList, endorsementDetailsList);
				        	}else{
				        		//insert or update insured details
				        		
				        		saveInsuredDetails(policy, policy2);
				        	}
			        	}
			        	
			        }
			        
			        if(ReferenceTable.getMasterPolicyAvailableProducts().containsKey(premPolicyDetails.getProductCode()) ||
			        		((premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())
			        		|| premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_GRP_COVID_PROD_CODE.equals(premPolicyDetails.getProductCode()))
			        		&& premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM))){
			        	if(null != premPolicyDetails.getMasterPolicyNumber() && !premPolicyDetails.getMasterPolicyNumber().isEmpty()){
							PremPolicyDetails masterPremPolicyDetails = fetchJetPrivillagePolicyDetailsFromPremia(premPolicyDetails.getMasterPolicyNumber());
							saveMasterPolicyForJetPrivillage(masterPremPolicyDetails);
			        	}
					}
			        
			        savePrevPortabilityPolicy(premPolicyDetails);		
			        
					policy = getPolicy(premPolicyDetails.getPolicyNo());
				//}

				utx.commit();
				return policy;
			}
			catch(Exception e) {
				log.error("___________________  POLICY ERROR WHILE SAVING THE DATA ___________________" + premPolicyDetails.getPolicyNo());
				e.printStackTrace();
				utx.rollback();
				log.error("**************ERROR IN POPULATE POLICY DATA METHOD*********" + e.getMessage());
				return null;
			}
		}
	//Added For Accident Trauma care Group product		
		public List<PremiaIntimationTable> processPremiaIntimationDataForPA(String batchSize) {
			try {
				log.info("********* BATCH SIZE FOR PREMIA PULL ACC-PRD-008******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
				List<PremiaIntimationTable> premiaIntimationList = fetchRecFromPremiaIntimationTableForPA(batchSize);
				return premiaIntimationList;
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE ACC-PRD-008**********************" + e.getMessage());
				return null;
			}
		}
		//Added For Accident Trauma care Group product		
		private List<PremiaIntimationTable> fetchRecFromPremiaIntimationTableForPA(String batchSize) {
			Query query = entityManager.createNamedQuery("PremiaIntimationTable.findAllPa");
			query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
			/**
			 * Will remove once the final integration of batch is over.
			 * */
			if(batchSize != null) {
				query.setMaxResults(SHAUtils.getIntegerFromString("20"));
			}
			List<PremiaIntimationTable> premiaIntimationTableList = query.getResultList();
			log.info("********* COUNT FOR PREMIA PULL FOR GROUP PA ACC-PRD-008******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
			return premiaIntimationTableList;
		}
		
	 
	 public List<OPPremiaIntimationTable> processOPPremiaIntimationData(String batchSize) {
			try {
				log.info("********* BATCH SIZE FOR OP PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
				List<OPPremiaIntimationTable> oppremiaIntimationList = fetchRecFromOPPremiaIntimationTable(batchSize);
				return oppremiaIntimationList;
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING FETCH FROM OP PREMIA TABLE **********************" + e.getMessage());
				return null;
			}
		}
	 
	 private List<OPPremiaIntimationTable> fetchRecFromOPPremiaIntimationTable(String batchSize) {
			Query query = entityManager.createNamedQuery("OPPremiaIntimationTable.findByAll");
//			query = query.setParameter("claimTypeKey",ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			if(batchSize != null) {
				query.setMaxResults(SHAUtils.getIntegerFromString("20"));
			}
			List<OPPremiaIntimationTable> oppremiaIntimationTableList = query.getResultList();
			log.info("********* COUNT FOR PREMIA PULL FOR OP INTIMATIONS ******------> " + (oppremiaIntimationTableList != null ? String.valueOf(oppremiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
			return oppremiaIntimationTableList;
		}
	 
	 public Boolean populateOPPolicyFromTmpPolicy(PremPolicyDetails premPolicyDetails) {
			//tmpPolicy = policyService.findTmppolicyByPolicyNo(tmpPolicy.getPolNo());
//		 Boolean isAllowed = false;
		 Boolean isBaNCS = Boolean.FALSE;
		 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
				isBaNCS = Boolean.TRUE;
		}
		 PremiaToPolicyMapper premiaPolicyMapper = PremiaToPolicyMapper.getInstance();
		 premiaPolicyMapper.getAllMapValues();
			Policy policy = policyService.getPolicy(premPolicyDetails.getPolicyNo());
	/*
			List<PreviousPolicy> previousPolicyList = previousPolicyService
					.getPrevPolicy(premPolicyDetails.getProposerCode());
			List<PolicyEndorsementDetails> endorsementDetailsList = policyService
					.getEndorsementList(premPolicyDetails.getPolicyNo());

			List<TmpPrevPolicy> tmpPrevPolicyList = new ArrayList<TmpPrevPolicy>();
			List<TmpEndorsementDetails> tmpEndorsementDetails = new ArrayList<TmpEndorsementDetails>();

			if ((null == previousPolicyList || previousPolicyList.isEmpty())
					&& (null == endorsementDetailsList || endorsementDetailsList
							.isEmpty())) {
				tmpPrevPolicyList = previousPolicyService
						.findTmppolicyByPolicyNo((premPolicyDetails.getProposerCode()));

				tmpEndorsementDetails = policyService
						.getTmpEndorsementList(premPolicyDetails.getPolicyNo());

				previousPolicyList = populatePreviousPolicy(tmpPrevPolicyList);
				endorsementDetailsList = populatePolicyEndorsementDetails(tmpEndorsementDetails);
			}*/

			if (null == policy) {
			 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
			 
			 Double totalAmount = 0d;
			 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
			 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
			 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
			 policy.setTotalPremium(totalAmount);
			 
			 try{
				 if(premPolicyDetails.getDeductiableAmt() != null && ! premPolicyDetails.getDeductiableAmt().isEmpty()){
					 policy.setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getDeductiableAmt()).doubleValue());
				 }
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 
			 if(premPolicyDetails.getPolSysId() != null){
				 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
		
				 policy.setPolicySystemId(polSysId);
			 }
			 if(premPolicyDetails.getProposerGender() != null){
				 if(premPolicyDetails.getProposerGender().equalsIgnoreCase("M")){
					 MastersValue master = getMaster(ReferenceTable.MALE_GENDER);
					 policy.setProposerGender(master);
				 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("F")){
					 MastersValue master = getMaster(ReferenceTable.FEMALE_GENDER);
					 policy.setProposerGender(master);
				 }else  if(premPolicyDetails.getProposerGender().equalsIgnoreCase("TG")){
					 MastersValue master = getMaster(ReferenceTable.TRANS_GENDER);
					 policy.setProposerGender(master);
				 }
			 }
			 
			 if(premPolicyDetails.getPortedYN() != null){
				 policy.setPortedPolicy(premPolicyDetails.getPortedYN());
			 }
			 
			 /*if(!isCurrentPolicy) {
				 //IMSSUPPOR-31281 - Sequence mismatch with SysId and policyKey
				 Policy clsPolicy = getPolicyDetails(policy.getPolicySystemId());
				    if(clsPolicy == null){
				    	policy.setKey(policy.getPolicySystemId()); 
				    }
			 }*/
			 
			 if(premPolicyDetails.getPolicyPlan() != null){
				 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
					 policy.setPolicyPlan("A");
				 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
					 policy.setPolicyPlan("B");
				 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
						 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
					 policy.setPolicyPlan("G");
				 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
					 policy.setPolicyPlan("S");
				 } //Added for MED-PRD-072
				 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
					 policy.setPolicyPlan("B");
				 }
			 }
			 
			//MED-PRD-076
				if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
					if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
						 policy.setPolicyPlan("B");
					 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
						 policy.setPolicyPlan("E");
					 }
				}
			 
			 if(premPolicyDetails.getPolicyZone() != null){
				 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
			 }
			 
			 /*String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
			 if(policyStrYear != null){
				 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
			 }*/
			 
			 if(premPolicyDetails.getPolicyTerm() != null) {
				 // Below Cond for SCRC - MED-PRD-070 - R201811302
				 if(premPolicyDetails.getProductCode() != null && ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED.equals(premPolicyDetails.getProductCode())){
					 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
						 String policyTermYear[] = premPolicyDetails.getPolicyTerm().split(" ");
						 String policyTerm = policyTermYear[0];
						 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTerm));
					 }
				 }
				 else  if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
					 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
					 policy.setPolicyTerm(policyTerm);
				 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
					 policy.setPolicyTerm(2l);
				 }
					//Added for MED-PRD-083
				 if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
				 policy.setPolicyTerm(1l);
			 }
				 if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96.equals(premPolicyDetails.getProductCode())) {
					 policy.setPolicyTerm(1l);
				 }
			 }
			 
			 /*if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true))
			 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
			 
			 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)
				 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
			 
			 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true)
			 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
			 
			 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true)
			 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
			 
			 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true)
				 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));*/
			 
			 if(premPolicyDetails.getPolicySource() == null || !premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
				 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
				 if(policyStrYear != null){
					 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
				 }
			 }
			 
			 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)){
				 if(isBaNCS){
					 policy.setCreatedDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
				 }else{
					 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
				 }
			 }
			 
			 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true){
				 if(isBaNCS){
					 policy.setPolicyFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyStartDate()));
				 }else{
					 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
				 }
			 }
			 
			 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true){
				 if(isBaNCS){
					 policy.setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPolicyEndDate()));
				 }else{
					 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
				 }
			 }
			 
			 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true){
				 if(isBaNCS){
					 policy.setReceiptDate(SHAUtils.formatTimeFromString(premPolicyDetails.getReceiptDate()));
				 }else{
					 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
				 }
			 }
			 
			 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true){
				 if(isBaNCS){
					 policy.setProposerDob(SHAUtils.formatTimeFromString(premPolicyDetails.getProposerDOB()));
				 }else{
					 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
				 }
			 }
			 
			 if(null != masterService.getMasterByValue(premPolicyDetails.getLob()))
			 policy.setLobId(masterService.getMasterByValue(premPolicyDetails.getLob()).getKey());
			 
			 if(null != masterService.getMasterByValue(premPolicyDetails.getPolicyType()))
			 policy.setPolicyType(masterService.getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
			 
			 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
				 
				 MastersValue schemeId = masterService.getMasterByValue(premPolicyDetails.getSchemeType());
			
				 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
			 }
			 
			//TODO:Get product type from premia 
//			 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//			 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
			 

			 policy.setProduct((policyService.getProductByProductCode(premPolicyDetails.getProductCode())));
			 policy.setProductType(masterService.getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
			 
			 List<PolicyEndorsementDetails> endorsementDetailsList = premiaPolicyMapper .getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
		
			 List<PreviousPolicy> previousPolicyList = premiaPolicyMapper.getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
			 //set Endrosement Date
			 if(premPolicyDetails.getEndorsementDetails() != null){
				 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
					 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
					if(isBaNCS){
						endorsementDetailsList.get(index).setEffectiveFromDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						endorsementDetailsList.get(index).setEndoresementDate(SHAUtils.baNCSDateTime(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
					}else{
						endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
						endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
					}
					 }
					 //Not null handled for BANCS
					 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt() != null){
						 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
							 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
						 }
					 }
				 }
			  }
			 //Set previous policy Date
			 if(premPolicyDetails.getPreviousPolicyDetails() != null){
				 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
					 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
						 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
					 }
			     }
				 //Set previous policy Date
				 if(premPolicyDetails.getPreviousPolicyDetails() != null){
					 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
						 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
							 if(isBaNCS){
								 previousPolicyList.get(index).setPolicyFrmDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
							 }else{
								 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
							 }
						 }
						 if(null == premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()||premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().isEmpty() ? false : true){
							 if(isBaNCS){
								 previousPolicyList.get(index).setPolicyToDate(SHAUtils.baNCSDateTime(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
							 }else{
								 previousPolicyList.get(index).setPolicyToDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
							 }
						 }
					 }
				  }
			     }
			 
			// ADD FOR MED_PRD_84 YOUNG STAR FLT POLICY
				if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84) ||
						premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)){
					
					 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)
							 && (premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
							 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW))) { 
			     	    	
		        			 policy.setHospitalCashBenefits(SHAConstants.OTHR_BEN_HOS_CASH_FLAG);
		     	    }
					
				}
			 
//			 List<Insured> insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
			 
			 List<Insured> insured = new ArrayList<Insured>();
			 if(premPolicyDetails.getInsuredDetails() != null){
				 
				 List<PremInsuredDetails> premiaInsuredList = premPolicyDetails.getInsuredDetails();
				 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
				 
				 if(premiaInsuredList != null && !premiaInsuredList.isEmpty()) {
					 for (PremInsuredDetails premInsuredDetails : premiaInsuredList) {
						
						 for (Insured insuredObj : insured) {
							 if(insuredObj.getInsuredId() != null && !StringUtils.isBlank(premInsuredDetails.getRiskSysId())){
								 if(insuredObj.getInsuredId().equals(Long.valueOf(premInsuredDetails.getRiskSysId()))){
									 
									 if(premInsuredDetails.getPolicyPlan() != null){
										 if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_BASE)){
											 insuredObj.setPolicyPlan("B");
										 }
										// Added For MED-PRD-83
										 else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) 
												 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
											 insuredObj.setPolicyPlan("S");
										 }
										 else if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
												 || premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
											 insuredObj.setPolicyPlan("G");
										 }
									 }
								 
								 // ADD FOR MED_PRD_84 YOUNG STAR IND POLICY
									if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84) ||
											premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)){
										
										 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)
												 && (premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
					 							 || premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW))) { 
								     	    	
											 insuredObj.setHospitalCashBenefits(SHAConstants.OTHR_BEN_HOS_CASH_FLAG);
							     	    }
										
									}
								 //
									Boolean isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
									if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
										if(isAllowed){
											if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
												insuredObj.setPlan("B");
												insuredObj.setPolicyPlan("B");
											}else if(premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
												insuredObj.setPlan("E");
												insuredObj.setPolicyPlan("E");
											}
										}
									}
									
									//comment by noufel since code already presient above
//									Boolean isAllowed = isIntegrated(premPolicyDetails, ReferenceTable.INDIVIDUAL_POLICY);
									if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
										if(isAllowed && premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)
												|| premInsuredDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)
												&& !premInsuredDetails.getHcpDays().isEmpty()){
											insuredObj.setHcpDays(Integer.valueOf(premInsuredDetails.getHcpDays()));
										}
									}
									
									Boolean isFloater = isIntegrated(premPolicyDetails, ReferenceTable.FLOATER_POLICY);
									if(premPolicyDetails.getProductCode().equals(SHAConstants.HOSPITAL_CASH_POLICY)){
										if(isFloater){
											if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN)){
												insuredObj.setPlan("B");
												insuredObj.setPolicyPlan("B");
											}else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN)){
												insuredObj.setPlan("E");
												insuredObj.setPolicyPlan("E");
											}
										}
									}
									
								//  add for comphersive product 078
									if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
										
										 if(premInsuredDetails.getBuyBackPed() != null && ! premInsuredDetails.getBuyBackPed().isEmpty()  && premInsuredDetails.getBuyBackPed().equalsIgnoreCase(SHAConstants.YES)) { 
								     	    	
											 insuredObj.setBuyBackPed(SHAConstants.YES_FLAG);
							     	    }else{
							     	    	insuredObj.setBuyBackPed(SHAConstants.N_FLAG);
							     	    }
										
									}
								//  add for comphersive product 088
									if(premPolicyDetails.getProductCode() != null &&  premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
										
										 if(premInsuredDetails.getBuyBackPed() != null && ! premInsuredDetails.getBuyBackPed().isEmpty()  && premInsuredDetails.getBuyBackPed().equalsIgnoreCase(SHAConstants.YES)) { 
								     	    	
											 insuredObj.setBuyBackPed(SHAConstants.YES_FLAG);
							     	    }else{
							     	    	insuredObj.setBuyBackPed(SHAConstants.N_FLAG);
							     	    }
										
									}
								 
								 }
							 }
						 }
					}
					 
				 } 
			 } else if(premPolicyDetails.getJpInsuredDetails() != null){
				 insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getJpInsuredDetails());
				 for (Insured insured2 : insured) {
					 insured2.setLopFlag("H");
					 
					 if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
							MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
							if(genderMaster != null && genderMaster.getKey() != null){
								insured2.setInsuredGender(genderMaster);
							}
						}else
						{
							MastersValue genderMaster =  getKeyByValue("MALE");
							if(genderMaster != null){
								insured2.setInsuredGender(genderMaster);
							}
						}
					 
					if(insured2.getStrInsuredAge() != null && ! insured2.getStrInsuredAge().isEmpty()){
						Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());
						insured2.setInsuredAge(insuredAge);
					}
					if(insured2.getStrDateOfBirth() != null && ! insured2.getStrDateOfBirth().isEmpty()){
						 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
						 if(insuredDOB != null) {
							 insured2.setInsuredDateOfBirth(insuredDOB);
						 }
					}
					
					/**This code added for CR20181258 - Criticare Platinum since nominee details insertion is not handled for Health claim **/
					List<PremInsuredDetails> premiaInsureObj = premPolicyDetails.getJpInsuredDetails();
					if(premiaInsureObj != null && ! premiaInsureObj.isEmpty()){
						for (PremInsuredDetails premiaInsured : premiaInsureObj) {
							List<NomineeDetails> nomineeDetailsForHealth = new ArrayList<NomineeDetails>();
							
							List<PolicyNominee> proposerNomineeDetailsForHealth = new ArrayList<PolicyNominee>();
							
							List<GmcContinuityBenefit> continuityBenefits = new ArrayList<GmcContinuityBenefit>();
							
							List<InsuredPedDetails> pedDetails = new ArrayList<InsuredPedDetails>();
							
							if(premiaInsured.getNomineeDetails() != null && ! premiaInsured.getNomineeDetails().isEmpty()){
								for (PremInsuredNomineeDetails nomineeObj : premiaInsured.getNomineeDetails()) {
									NomineeDetails nomineeDetail = new NomineeDetails();
									PolicyNominee proposerNomineeDetail = new PolicyNominee();
									
									if(nomineeObj.getNomineeAge() != null && ! nomineeObj.getNomineeAge().isEmpty()){
										nomineeDetail.setNomineeAge(Long.valueOf(nomineeObj.getNomineeAge()));
										proposerNomineeDetail.setNomineeAge(Integer.valueOf(nomineeObj.getNomineeAge()));
									}
									nomineeDetail.setNomineeName(nomineeObj.getNomineeName());
									proposerNomineeDetail.setNomineeName(nomineeObj.getNomineeName());
									//TODO
									/*proposerNomineeDetail.setAccountNumber(nomineeObj.getAccountNumber());
									proposerNomineeDetail.setNameAsPerBank(nomineeObj.getBeneficiaryName());
									proposerNomineeDetail.setIFSCcode(nomineeObj.getIfscCode());*/
									nomineeDetailsForHealth.add(nomineeDetail);
									proposerNomineeDetailsForHealth.add(proposerNomineeDetail);
								}
								if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
									insured2.setNomineeDetails(nomineeDetailsForHealth);
									insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForHealth);
								}
							}
							
							/*Below Code for MED-PRD-074*/
							if(premiaInsured.getContinuityBenefits() != null && ! premiaInsured.getContinuityBenefits().isEmpty()){
								if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
									continuityBenefits = premiaPolicyMapper.getGMCInsuredContBenDetails(premiaInsured.getContinuityBenefits());
									insured2.setGmcContBenefitDtls(continuityBenefits);
								}
							}
							
							if(premiaInsured.getPedDetails() != null && !premiaInsured.getPedDetails().isEmpty()){
								if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
									pedDetails = premiaPolicyMapper.getInsuredPedFromPremia(premiaInsured.getPedDetails());
									insured2.setInsuredPedList(pedDetails);
								}
							}
							
							/*if(policy.getProduct() != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
							    List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

							        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
							        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
							        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
							        		InsuredCover riskCover = new InsuredCover();
							        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
							        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
							        		if(premPolicyRiskCover2.getSumInsured() != null){
							        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
							        		}
							        		
							        		insuredCoverList.add(riskCover);
										}
							        	if(insured2.getInsuredId().equals(Long.valueOf(premiaInsured.getRiskSysId()))){
							        		insured2.setCoverDetailsForPA(insuredCoverList);
							        	}
								}
							}*/
						}
					}
			 	//  End of  CR CR20181258
				}
			 }
			
			 //set insured Date
			
			 //Comment Block Start
			 //List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();

			 /*for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++){
				MastersValue genderMaster =  masterService.getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
				 insured.get(index).setInsuredGender(genderMaster);
				
				 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
//					 insured.get(index).setInsuredDateOfBirth(new Date(premPolicyDetails.getInsuredDetails().get(index).getDob()));
//					 Date now = new Date();
//					 Date Dob = new Date(premPolicyDetails.getInsuredDetails().get(index).getDob());
//					 Double age = (double) (now.getYear() - Dob.getYear());
//					 if ((Dob.getMonth() > now.getMonth())  || (Dob.getMonth() == now.getMonth() && Dob.getMonth() > now.getMonth())) {
//						 age--;
//						    }
//					 insured.get(index).setInsuredAge(age);
					 
					 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
					 //Added for insured age caluculation.
					 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));

					 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
					 if(formatPremiaDate != null) {
						 
						 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);

//						 Integer age = SHAUtils.calculateInsuredAge(formatPolicyStartDate,formatPremiaDate);
//						 insured.get(index).setInsuredAge(Double.valueOf(age));
					 }
					 
				 }
				 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
		 		 
		 		 if(insuredAge != null){
		 			 insured.get(index).setInsuredAge(insuredAge);
		 			 } 
		 		insured.get(index).setLopFlag("H");

			 }
			 
				 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
					 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
					 insured.get(index).setNomineeDetails(premiaPolicyMapper.getInsuredNomineeDetails(premPolicyDetails.getInsuredDetails().get(index).getNomineeDetails()));
					 
					 insured.get(index).setProposerInsuredNomineeDetails(premiaPolicyMapper.getProposerInsuredNomineeDetails(premPolicyDetails.getInsuredDetails().get(index).getNomineeDetails()));
			 }
				 
				   List<PremInsuredNomineeDetails> premProposerNomineeDetails = premPolicyDetails.getProperNomineeDetails();
			        
			        if(premProposerNomineeDetails != null && !premProposerNomineeDetails.isEmpty()){
			        	List<PolicyNominee> proposerNomineeDetails = premiaPolicyMapper.getProposerInsuredNomineeDetails(premProposerNomineeDetails);
			        	for (PolicyNominee proposerNominee : proposerNomineeDetails) {
							if(proposerNominee.getStrNomineeDOB() != null && !proposerNominee.getStrNomineeDOB().isEmpty()){
								proposerNominee.setNomineeDob(new Date(proposerNominee.getStrNomineeDOB()));
							}
						}
			        	
			        	policy.setProposerNomineeDetails(proposerNomineeDetails);
			        	
			        }*/ //Comment block end
			 
			 List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
			 if(premPolicyDetails.getInsuredDetails() != null){
 				 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++) {
					MastersValue genderMaster =  getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
					 insured.get(index).setInsuredGender(genderMaster);
					
					 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
						 
						//Added for BaNCS
						 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
							 Date formatBaNCSDate = SHAUtils.formatTimeFromString(premPolicyDetails.getInsuredDetails().get(index).getDob());
							 if(formatBaNCSDate != null) {
								 insured.get(index).setInsuredDateOfBirth(formatBaNCSDate);
							 }
							 insured.get(index).setSourceRiskId(premPolicyDetails.getInsuredDetails().get(index).getBaNCSSourceRiskID());
						 }else{
							 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
							 //Added for insured age caluculation.
							 Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(premPolicyDetails.getPolicyStartDate())));
							 //Date formatPolicyStartDate = SHAUtils.formatPremiaDate(new Date(premPolicyDetails.getPolicyStartDate()).toString());
							 if(formatPremiaDate != null) {
								 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);
							 }
							 
						 }
						 
						 
						 
						 
					 }
					 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
			 		 
			 		 if(insuredAge != null){
			 			 insured.get(index).setInsuredAge(insuredAge);
			 		 } 
			 		 
			 		 insured.get(index).setLopFlag("H");
			 		 
			 		 if(insured.get(index).getSumInsured1() != null){
			 			insured.get(index).setSumInsured1Flag("Y");
			 		 }
			 		 else
			 		 {
			 			insured.get(index).setSumInsured1Flag("N");
			 		 }
			 		 
			 		if(insured.get(index).getSumInsured2() != null){
			 			insured.get(index).setSumInsured2Flag("Y");
			 		 }
			 		else
			 		{
			 			insured.get(index).setSumInsured2Flag("N");
			 		}
			 		
			 		if(insured.get(index).getSumInsured3() != null){
			 			insured.get(index).setSumInsured3Flag("Y");
			 		 }
			 		else
			 		{
			 			insured.get(index).setSumInsured3Flag("N");
			 		}
			 		
			 		//IMSSUPPOR-27387
			 		if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
			 			 try{
							 if(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt() != null){
								 insured.get(index).setDeductibleAmount(SHAUtils.getIntegerFromStringWithComma(premPolicyDetails.getInsuredDetails().get(index).getDeductiableAmt()).doubleValue());
							 }
						 }catch(Exception e){
							 e.printStackTrace();
						 }
			 		}
				 }

			 }
			 
			 if(premPolicyDetails.getInsuredDetails() != null){
				 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
					 List<PremPEDDetails> pedDetailsList = premPolicyDetails.getInsuredDetails().get(index).getPedDetails();
					 if(pedDetailsList != null && !pedDetailsList.isEmpty()){
						 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(pedDetailsList));
					 }
						 
						 List<PremPortability> portablitityDetails = premPolicyDetails.getInsuredDetails().get(index).getPortablitityDetails();
						 
						 if(portablitityDetails != null){
							 List<PortablityPolicy> portablityList = premiaPolicyMapper.getPortablityList(portablitityDetails);
							 for (PortablityPolicy portablityPolicy : portablityList) {
								
								 try{
								 if(portablityPolicy.getStrDateOfBirth() != null && ! portablityPolicy.getStrDateOfBirth().equalsIgnoreCase("")){
									 portablityPolicy.setDateOfBirth(new Date(portablityPolicy.getStrDateOfBirth()));
								 }
								 if(portablityPolicy.getStrMemberEntryDate() != null && ! portablityPolicy.getStrMemberEntryDate().equalsIgnoreCase("")){
									 portablityPolicy.setMemberEntryDate(new Date(portablityPolicy.getStrMemberEntryDate()));
								 }
								 
								 if(portablityPolicy.getPolicyStrStartDate() != null && ! portablityPolicy.getPolicyStrStartDate().equalsIgnoreCase("")){
									 portablityPolicy.setPolicyStartDate(new Date(portablityPolicy.getPolicyStrStartDate()));
								 }
								 }catch(Exception e){
									 e.printStackTrace();
								 }
							}
							 insured.get(index).setPortablityPolicy(portablityList);
						 }
				 }
			 }
				 
				 
				 
			List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();
				 
			int i=0;
			if(null != insured && !insured.isEmpty()) {
				for (Insured insured2 : insured) {
					if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
						Double totalSumInsured = policy.getTotalSumInsured();
						Double size = Double.valueOf(insured.size());
						Double sumInsured = 0d;
						if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
								|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
								|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
								|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
								|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_82)
								|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
							if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
								sumInsured = totalSumInsured/size;
							}
							Long roundOfSI = Math.round(sumInsured);
							insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
						}else{
							if(premPolicyDetails.getProductCode() == null){
								insured2.setInsuredSumInsured(policy.getTotalSumInsured());
							}
							else if(premPolicyDetails.getProductCode() != null && !premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
									&& !policy.getProduct().getCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
									&& !policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
									&& !policy.getProduct().getCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)) {
								insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
							}
						}
						
	//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
						
					} else {
	//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
					}
					
					if(premiaInsured != null){
					
						if (premiaInsured.get(i).getSelfDeclaredPed() != null
								&& premiaInsured.get(i).getSelfDeclaredPed()
										.length() > 0) {
							InsuredPedDetails selfDeclaredPed = new InsuredPedDetails();
							selfDeclaredPed.setInsuredKey(insured2
									.getInsuredId());
							selfDeclaredPed.setPedDescription(premiaInsured
									.get(i).getSelfDeclaredPed());
							List<InsuredPedDetails> pedList = new ArrayList<InsuredPedDetails>();
							pedList.add(selfDeclaredPed);
							try{
								if(insured2.getInsuredPedList() != null){
									insured2.getInsuredPedList().addAll(pedList);
								}else{
									insured2.setInsuredPedList(pedList);
								}
							}catch(Exception e){
								System.out.println("Insured Ped list exception");
								e.printStackTrace();
							}
							
						}
					}
					i++;
					 /*if(!isCurrentPolicy) {
						    Insured clsInsured = getCLSInsured(insured2.getInsuredId());
						    if(clsInsured == null){
						    	insured2.setKey(insured2.getInsuredId()); 
						    }
					 }*/
					 
					 //Added for BaNCS
					 if(premPolicyDetails.getPolicySource() != null && premPolicyDetails.getPolicySource().equalsIgnoreCase("B")){
						 Query query = entityManager.createNativeQuery("select SEQ_B_INSURED_NO.nextval from dual");
						 insured2.setInsuredId(Long.parseLong(query.getResultList().get(0).toString()));		
					 }
				}
			    policy.setInsured(insured);
			}
			/*InsuredPedDetails selfDeclaredPed = null;
			List<InsuredPedDetails> pedList = null ;
			for (Insured insured2 : insured) {
				
				if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
					Double totalSumInsured = policy.getTotalSumInsured();
					Double size = Double.valueOf(insured.size());
					Double sumInsured = 0d;
					if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
							|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
						if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
							sumInsured = totalSumInsured/size;
						}
						Long roundOfSI = Math.round(sumInsured);
						insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
					}else{
						insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
					}
					
//					policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
					
				}else{
//					policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
				}
				
				Below code commented - record inserting two times 
				if(premiaInsured.get(i).getSelfDeclaredPed() != null && premiaInsured.get(i).getSelfDeclaredPed().length() >0){
					selfDeclaredPed = new InsuredPedDetails();
					selfDeclaredPed.setInsuredKey(insured2.getInsuredId());
					selfDeclaredPed.setPedDescription(premiaInsured.get(i).getSelfDeclaredPed());
					pedList = new ArrayList<InsuredPedDetails>();
					pedList.add(selfDeclaredPed);
					insured2.setInsuredPedList(pedList);
				}
				i++;
						
			}
			
			

		    policy.setInsured(insured);*/
			
			if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
	        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
		        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_37)//Added for comprehensive ind 507
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE) 
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_83)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)
		        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
	        	
	        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)) { 
	     	    	
	        		 MastersValue productType = getMaster(ReferenceTable.FLOATER_POLICY);
	        		 policy.setProductType(productType);
	        		 
	        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
	        		 if(product != null){
	        			 policy.setProduct(product);
	        		 }
	     	    } else {
	     	    	
	     	    	 MastersValue productType = getMasterValue(ReferenceTable.INDIVIDUAL_POLICY);
	        		 policy.setProductType(productType);
	        		 Product product = getProductByCodeAndTypeAndInceptionDate(premPolicyDetails.getProductCode(), productType.getValue(),new Date(premPolicyDetails.getPolicyStartDate()));
	        		 if(product != null) {
	        			 policy.setProduct(product);
	        		 }
	     	    }
	        }
			
			List<PremPolicyRiskCover> premPolicyRiskCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

	        if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
	        	List<PolicyRiskCover> policyRiskCoverList = new ArrayList<PolicyRiskCover>();
	        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
	        		PolicyRiskCover riskCover = new PolicyRiskCover();
	        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
	        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
	        		if(premPolicyRiskCover2.getSumInsured() != null){
	        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
	        		}
	        		
	        		policyRiskCoverList.add(riskCover);
				}
	        	policy.setPolicyRiskCoverDetails(policyRiskCoverList);
	        }
		    
			 /*if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
		        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
		        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
						if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("") && !policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("null") ){
							policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
						}
						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("") && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("null")){
							policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
						}
					}
		        	policy.setPolicyBankDetails(bankDetailsFromPremia);
		        }*/
	        if (premPolicyDetails.getPolicyNo() != null) {
	        	 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
			        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
			        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
							if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("") && !policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("null") ){
								policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
							}
	 						if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("") && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("null")){
								policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
							}
						}
			        	policy.setPolicyBankDetails(bankDetailsFromPremia);
			        }
	        }
	        
	        List<PremiaInsuredPA> premiaInsuredPAdetails = premPolicyDetails.getPremiaInsuredPAdetails();
	        List<Insured> insuredPAFromPremia = new ArrayList<Insured>();
	        if(premiaInsuredPAdetails != null){
	        	insuredPAFromPremia = premiaPolicyMapper.getInsuredPAFromPremia(premiaInsuredPAdetails);
	        }
	        for (Insured insured2 : insuredPAFromPremia) {
				if(insured2.getStrInsuredAge() != null && !insured2.getStrInsuredAge().isEmpty()){
					Double insuredAge = SHAUtils.getDoubleValueFromString(insured2.getStrInsuredAge());     
					insured2.setInsuredAge(insuredAge);
				}
				if(insured2.getStrDateOfBirth() != null && !insured2.getStrDateOfBirth().isEmpty()){
				
				 Date insuredDOB = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(insured2.getStrDateOfBirth())));
				 if(insuredDOB != null) {
					 insured2.setInsuredDateOfBirth(insuredDOB);
				 }
				}
				if(insured2.getStrGender() != null && !insured2.getStrGender().equalsIgnoreCase("")){
					MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
					if(genderMaster != null && genderMaster.getKey() != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				/**
				 * As per Sathish ,if gender is blank  from premia, then
				 *  default it to MALE.
				 * */
				else
				{
					MastersValue genderMaster =  getKeyByValue("MALE");
					if(genderMaster != null){
						insured2.setInsuredGender(genderMaster);
					}
				}
				insured2.setLopFlag("P");
				
			}
	        
	        if(insuredPAFromPremia != null){
	        	policy.setInsuredPA(insuredPAFromPremia);
	        }
	        
	        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
	        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
	        		 List<InsuredCover> insuredCoverDetailsForPA = new ArrayList<InsuredCover>();
					if(premiaInsured1.getPremInsuredRiskCoverDetails() != null && ! premiaInsured1.getPremInsuredRiskCoverDetails().isEmpty()){
						for (PremCoverDetailsForPA coverDetails : premiaInsured1.getPremInsuredRiskCoverDetails()) {
							InsuredCover insuredCover = new InsuredCover();
							insuredCover.setCoverCode(coverDetails.getCoverCode());
								insuredCover.setCoverCodeDescription(coverDetails.getCoverDescription());
							if(coverDetails.getSumInsured() != null && ! coverDetails.getSumInsured().equals("")){
								insuredCover.setSumInsured(Double.valueOf(coverDetails.getSumInsured()));
							}
							insuredCoverDetailsForPA.add(insuredCover);
							
							
						}
						List<Insured> insuredPA = policy.getInsuredPA();
						if(insuredPA != null){
							for (Insured insured2 : insuredPA) {
								
								MastersValue genderMaster =  getKeyByValue(insured2.getStrGender());
								if (genderMaster != null &&  genderMaster.getKey() != null) {
									
									insured2.setInsuredGender(genderMaster);
								}
								
								
								if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
									insured2.setCoverDetailsForPA(insuredCoverDetailsForPA);
									break;
								}
							}
						}
					}
					
					if(policy.getProduct() != null && (policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)
							|| policy.getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY))){
					    List<PremPolicyRiskCover> premPolicyCover = premPolicyDetails.getPremInsuredRiskCoverDetails();

					        if(premPolicyCover != null && ! premPolicyCover.isEmpty()){
					        	List<InsuredCover> insuredCoverList = new ArrayList<InsuredCover>();
					        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyCover) {
					        		InsuredCover riskCover = new InsuredCover();
					        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
					        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
					        		if(premPolicyRiskCover2.getSumInsured() != null){
					        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
					        		}
					        		
					        		insuredCoverList.add(riskCover);
								}
					        	
					        	List<Insured> insuredPA = policy.getInsuredPA();
								if(insuredPA != null){
									for (Insured insured2 : insuredPA) {		
										if(insured2.getInsuredId() != null && insured2.getInsuredId().toString().equalsIgnoreCase(premiaInsured1.getRiskSysId())){
											insured2.setCoverDetailsForPA(insuredCoverList);
										}
									}
								}
					        
						}
					}
				}
	        }
	        
	        if(premiaInsuredPAdetails != null && ! premiaInsuredPAdetails.isEmpty()){
	        	for (PremiaInsuredPA premiaInsured1 : premiaInsuredPAdetails) {
	        		 List<NomineeDetails> nomineeDetailsForPA = new ArrayList<NomineeDetails>();
	        		 List<PolicyNominee> proposerNomineeDetailsForPA = new ArrayList<PolicyNominee>();
	        		 
					if(premiaInsured1.getNomineeDetails() != null && ! premiaInsured1.getNomineeDetails().isEmpty()){
						for (PremInsuredNomineeDetails coverDetails : premiaInsured1.getNomineeDetails()) {
							NomineeDetails nomineeDetail = new NomineeDetails();
							PolicyNominee proposerNomineeDetail = new PolicyNominee();
							if(coverDetails.getNomineeAge() != null && ! coverDetails.getNomineeAge().isEmpty()){
								nomineeDetail.setNomineeAge(Long.valueOf(coverDetails.getNomineeAge()));
								proposerNomineeDetail.setNomineeAge(Integer.valueOf(coverDetails.getNomineeAge()));
							}
							nomineeDetail.setNomineeName(coverDetails.getNomineeName());
							proposerNomineeDetail.setNomineeName(coverDetails.getNomineeName());
							proposerNomineeDetail.setRelationshipWithProposer(coverDetails.getNomineeRelation());
							//TODO
							/*proposerNomineeDetail.setAccountNumber(coverDetails.getAccountNumber());
							proposerNomineeDetail.setNameAsPerBank(coverDetails.getBeneficiaryName());
							proposerNomineeDetail.setIFSCcode(coverDetails.getIfscCode());*/
							nomineeDetailsForPA.add(nomineeDetail);
							proposerNomineeDetailsForPA.add(proposerNomineeDetail);
							
							
						}
						List<Insured> insuredPA = policy.getInsuredPA();
						if(insuredPA != null){
							for (Insured insured2 : insuredPA) {
								if(insured2.getHealthCardNumber().equalsIgnoreCase(premiaInsured1.getIdCardNumber())){
									insured2.setNomineeDetails(nomineeDetailsForPA);
									insured2.setProposerInsuredNomineeDetails(proposerNomineeDetailsForPA);
								}
							}
						}
					}
				}
	        }
	        
	        List<PremPolicyCoverDetails> premPolicyCoverDetails = premPolicyDetails.getPremPolicyCoverDetails();

	        if(premPolicyCoverDetails != null && ! premPolicyCoverDetails.isEmpty()){
	        	List<PolicyCoverDetails> policyRiskCoverList = new ArrayList<PolicyCoverDetails>();
	        	for (PremPolicyCoverDetails premPolicyRiskCover2 : premPolicyCoverDetails) {
	        		PolicyCoverDetails riskCover = new PolicyCoverDetails();
	        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
	        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
	        		if(premPolicyRiskCover2.getSumInsured() != null){
	        			riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
	        		}
	        		if(premPolicyRiskCover2.getRiskId() != null){
	        			riskCover.setRiskId(Long.valueOf(premPolicyRiskCover2.getRiskId()));
	        		}
	        		policyRiskCoverList.add(riskCover);
				}
	        	policy.setPolicyCoverDetails(policyRiskCoverList);
	        }
	        
	        if(premPolicyDetails.getPolicyZone() != null){
				 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
			}
	        
	        List<PremInsuredNomineeDetails> premProposerNomineeDetails = premPolicyDetails.getProperNomineeDetails();
	        
	        if(premProposerNomineeDetails != null && !premProposerNomineeDetails.isEmpty()){
	        	List<PolicyNominee> proposerNomineeDetails = premiaPolicyMapper.getProposerInsuredNomineeDetails(premProposerNomineeDetails);
	        	for (PolicyNominee proposerNominee : proposerNomineeDetails) {
					if(proposerNominee.getStrNomineeDOB() != null && !proposerNominee.getStrNomineeDOB().isEmpty()){
						if(isBaNCS){
							proposerNominee.setNomineeDob(SHAUtils.formatTimeFromString(proposerNominee.getStrNomineeDOB()));
						}else{
							proposerNominee.setNomineeDob(new Date(proposerNominee.getStrNomineeDOB()));
						}
					}
				}
	        	
	        	policy.setProposerNomineeDetails(proposerNomineeDetails);
	        	
	        }
	        
	        /* Below code for product code - MED-PRD-073*/
	        if(premPolicyDetails.getProductCode() != null && (premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GROUP_PRODUCT)
	        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
	        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE)
	        		|| premPolicyDetails.getProductCode().equals(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))) {
	        
			        
			        if(premPolicyDetails.getGmcAilmentLimit() != null){
						List<MasAilmentLimit> ailmentLimit = premiaPolicyMapper.getAilmentLimit(premPolicyDetails.getGmcAilmentLimit());
						policy.setAilmentDetails(ailmentLimit);
					}
					
					if(premPolicyDetails.getGmcDeliveryLimit() != null){
						List<MasDeliveryExpLimit> deliveryLimit = premiaPolicyMapper.getDeliveryExpLimits(premPolicyDetails.getGmcDeliveryLimit());
						policy.setDeliveryExpLimit(deliveryLimit);
					}
					
					if(premPolicyDetails.getGmcCopayLimit() != null){
						List<MasCopayLimit> copayLimit = premiaPolicyMapper.getCopayLimit(premPolicyDetails.getGmcCopayLimit());
						policy.setCopayLimit(copayLimit);
					}
					if(premPolicyDetails.getGmcPrePostHospLimit() != null){
						List<MasPrePostHospLimit> prePostLimit = premiaPolicyMapper.getPrepostLimit(premPolicyDetails.getGmcPrePostHospLimit());
						policy.setPrePostLimit(prePostLimit);
					}
					
					if(premPolicyDetails.getGmcRoomRentLimit() != null){
						List<MasRoomRentLimit> roomRentList = premiaPolicyMapper.getRoomRentLimit(premPolicyDetails.getGmcRoomRentLimit());
						policy.setRoomRentLimit(roomRentList);
					}
					
					List<GpaBenefitDetails> benefitDetailsList = new ArrayList<GpaBenefitDetails>();
					
					List<PremGmcBenefitDetails> gmcPolicyConditions = premPolicyDetails.getGmcPolicyConditions();
					if(gmcPolicyConditions != null){
						for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
							GpaBenefitDetails benefitDetails = new GpaBenefitDetails();
							benefitDetails.setBenefitCode(premGmcBenefitDetails.getConditionCode());
							benefitDetails.setBenefitDescription(premGmcBenefitDetails.getConditionDesc());
							benefitDetails.setBenefitLongDescription(premGmcBenefitDetails.getConditionLongDesc());
							benefitDetailsList.add(benefitDetails);
						}
					}
					policy.setGpaBenefitDetails(benefitDetailsList);
					
				if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty()){
				
					if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_A);
						policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
					}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
						policy.setSectionCode(SHAConstants.GMC_SECTION_C);
						policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
					}
				}
					
					
	        }
	        
	      //MED-PRD-076
			if(premPolicyDetails.getProductCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
				Product product = null;
					if(premPolicyDetails.getPolType() != null && !premPolicyDetails.getPolType().isEmpty() && policy.getPolicyPlan() != null){
						if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("B")){
							product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
						}else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL) && policy.getPolicyPlan().equalsIgnoreCase("E")){
							//HOSPITAL_CASH_POLICY_IND_Bproduct = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_E);
							product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_IND_B);
						}
						else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("B")){
							product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_B);
						}
						else if(premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER) && policy.getPolicyPlan().equalsIgnoreCase("E")){
							product = getProrataForProduct(ReferenceTable.HOSPITAL_CASH_POLICY_FLT_E);
						}
					}
					if(product != null){
						policy.setProduct(product);
					}
					
					if(premPolicyDetails.getPhcDays() != null && !premPolicyDetails.getPhcDays().isEmpty()){
						policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getPhcDays()));
					}
					policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
				}
			
			//added for GHC hospital cash product CR
			if(premPolicyDetails.getProductCode() != null && ReferenceTable.GROUP_HOSPITAL_CASH_POLICY.equals(premPolicyDetails.getProductCode())){
				if(premPolicyDetails.getGhcDays() != null && !premPolicyDetails.getGhcDays().isEmpty()){
					policy.setPhcBenefitDays(Integer.valueOf(premPolicyDetails.getGhcDays()));
				}
			}
			//added for installment payment status flag and instalment method update at policy level changes done
			if(premPolicyDetails.getInstalmentFlag() != null && !premPolicyDetails.getInstalmentFlag().isEmpty() &&
					premPolicyDetails.getInstalmentFlag().equals("1")){
				policy.setPolicyInstalmentFlag(SHAConstants.YES_FLAG);
			}else {
				policy.setPolicyInstalmentFlag(SHAConstants.N_FLAG);	
			}
			
			if(premPolicyDetails.getBasePolicy() != null && !premPolicyDetails.getBasePolicy().isEmpty()){
				policy.setBasePolicyNo(premPolicyDetails.getBasePolicy());
			}
			
			if(premPolicyDetails.getInstallementMethod() != null && !premPolicyDetails.getInstallementMethod().isEmpty()){
				policy.setPolicyInstalmentType(premPolicyDetails.getInstallementMethod());
			}
			
			// ADD for PA ACC-PRD-020 as flt cover details adding 
			if(policy.getProduct() != null && policy.getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY)){
				
				List<PremPolicyCoverDetails> premPolicyCover = premPolicyDetails.getPremPolicyCoverDetails();

				if(premPolicyCover != null && ! premPolicyCover.isEmpty()){
					List<Insured> insuredPA = policy.getInsuredPA();
					if(insuredPA != null && premPolicyCover.size() >= 1){
						for (Insured insured2 : insuredPA) {	
							List<InsuredCover> insuredCovers = new ArrayList<InsuredCover>();
							PremPolicyCoverDetails premPolicyRiskCover2 = premPolicyCover.get(0);
							InsuredCover riskCover = new InsuredCover();
							riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
							riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
							if(premPolicyRiskCover2.getSumInsured() != null){
								riskCover.setSumInsured(Double.valueOf(premPolicyRiskCover2.getSumInsured()));
							}/*
							if(premPolicyRiskCover2.getRiskId() != null){
								riskCover.setInsuredKey(Long.valueOf(insured2.getInsuredId()));
							}*/
							insuredCovers.add(riskCover);
							insured2.setCoverDetailsForPA(insuredCovers);
						}
					}
				}
			}
			
			// GLX2020054 add for star novel grp product
	        if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())){
	        	Product product = null;
	        	if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM)){
					policy.setSectionCode(SHAConstants.GMC_SECTION_A);
					policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
					product = getCoronaGrpForProduct(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM);
					policy.setPolicyPlan("L");
				}else if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_INDEMNITY)){
					policy.setSectionCode(SHAConstants.GMC_SECTION_C);
					policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
					product = getCoronaGrpForProduct(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY);
					policy.setPolicyPlan("I");
				}
	        	if(product != null){
					policy.setProduct(product);
				}
	        	policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
	        	policy.setPolicyTerm(1l);
			}
	        
	        if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_GRP_COVID_PROD_CODE.equals(premPolicyDetails.getProductCode())){
	        	Product product = null;
	        	if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_LUMPSUM)){
					policy.setSectionCode(SHAConstants.GMC_SECTION_A);
					policy.setSectionDescription(SHAConstants.GMC_SECTION__DESC_A);
					product = getCoronaGrpForProduct(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM);
					policy.setPolicyPlan("L");
				}else if(premPolicyDetails.getCovidPlan().equalsIgnoreCase(SHAConstants.STAR_COVID_PLAN_INDEMNITY)){
					policy.setSectionCode(SHAConstants.GMC_SECTION_C);
					policy.setSectionDescription(SHAConstants.GMC_SECTION_DESC_C);
					product = getCoronaGrpForProduct(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI);
					policy.setPolicyPlan("I");
				}
	        	if(product != null){
					policy.setProduct(product);
				}
	        	policy.setProductType(getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
	        	if(premPolicyDetails != null && premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()){
	        		Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
	        		policy.setPolicyTerm(policyTerm);
	        	}
			}
		    
	        /*if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
	        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
	        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
	        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
	        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
		        	|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)
		        	|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)){
	        	
	        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
	     	    	
	        		 MastersValue productType = masterService.getMaster(ReferenceTable.FLOATER_POLICY);
	        		 policy.setProductType(productType);
	        		 
	        		 Product product = masterService.getProductByCodeAndType(premPolicyDetails.getProductCode(), productType.getValue());
	        		 if(product != null){
	        			 policy.setProduct(product);
	        		 }
	        		 
	     	    }else{
	     	    	
	     	    	 MastersValue productType = masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY);
	        		 policy.setProductType(productType);
	        		 
	        		 Product product = masterService.getProductByCodeAndType(premPolicyDetails.getProductCode(), productType.getValue());
	        		 if(product != null){
	        			 policy.setProduct(product);
	        		 }
	     	    	
	     	    }
	        	
	        }*/
		   
			policyService.create(policy, previousPolicyList, endorsementDetailsList);
			
			 policy = policyService.getPolicy(premPolicyDetails.getPolicyNo());
			}
			if(policy != null){
				return true;
			}
				return false;
		}
	 
	 public Boolean isOPIntimationExist(String strIntimationNo) {
			Query query = entityManager.createNamedQuery("OPIntimation.findByIntimationNumber");
			query = query.setParameter("intimationNo",strIntimationNo);
			List<Intimation>	resultList = query.getResultList();
			if (null != resultList && !resultList.isEmpty()){
				return true;
			}
			return false;
		}
	 
	 public void updateOPPremiaIntimationTable(OPPremiaIntimationTable oppremiaIntimationTable, String message,String flag) throws IllegalStateException, SecurityException, SystemException {
			if(null != oppremiaIntimationTable && null != oppremiaIntimationTable.getIntimationNumber()){
				try {
					utx.begin();
					oppremiaIntimationTable.setReadFlag(flag);
					oppremiaIntimationTable.setReadStatus(message);
					entityManager.merge(oppremiaIntimationTable);
					entityManager.flush();
					utx.commit();
				} catch(Exception e) {
					utx.rollback();
				}
			}
		}
	 
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
		public OPIntimation insertOPIntimationData(OPPremiaIntimationTable oppremiaIntimationTable,String lobFlag) throws NotSupportedException, SystemException {
			try {
				utx.begin();
				Boolean isHospTypeEmpty = false;
				OPIntimation opintimation  = new OPIntimation();
				MastersValue intimationModeValue = null;
				MastersValue intimationIntimatedBy = null;
				Policy policy = null;
	 			Hospitals hospitals = null;
				MastersValue admissionType = null;
				MastersValue managementType = null;
				MastersValue roomCategory = null;
				TmpCPUCode cpuCode = null;
				MastersValue intimationSource = null;
				MastersValue hospitalType = new MastersValue();
				Insured insured = null;

				
				if(null != oppremiaIntimationTable.getIntimationMode() && !("").equalsIgnoreCase(oppremiaIntimationTable.getIntimationMode())){
					intimationModeValue = getMastersValue(oppremiaIntimationTable.getIntimationMode(),ReferenceTable.MODE_OF_INTIMATION);
				}
				else {
					intimationModeValue = getMastersValue(SHAConstants.INTIMATION_MODE_PHONE,ReferenceTable.MODE_OF_INTIMATION);
				}
				

				/*if(null != oppremiaIntimationTable.getIntimati() && !("").equalsIgnoreCase(oppremiaIntimationTable.getGiIntimatedBy())){
					intimationIntimatedBy = getMastersValue(oppremiaIntimationTable.getGiIntimatedBy(),ReferenceTable.INTIMATED_BY);
				}
				else {
					intimationIntimatedBy = getMastersValue(SHAConstants.INTIMATION_INTIMATED_BY,ReferenceTable.INTIMATED_BY);
				}*/
				
				if(null != oppremiaIntimationTable.getPolicyNumber() && !("").equalsIgnoreCase(oppremiaIntimationTable.getPolicyNumber())){
					policy = getPolicyByPolicyNubember(oppremiaIntimationTable.getPolicyNumber());
				}


				if(null != oppremiaIntimationTable.getInsuredId() && !("").equalsIgnoreCase(oppremiaIntimationTable.getInsuredId())){
					insured = getInsuredByPolicyAndInsuredName(oppremiaIntimationTable.getPolicyNumber(), oppremiaIntimationTable.getInsuredId(),null);
				}
				
				if(insured == null){
					insured = getInsuredByPolicyAndInsuredNameForDefault(oppremiaIntimationTable.getPolicyNumber(), oppremiaIntimationTable.getInsuredId());
				}
//				intimationSource = new MastersValue();
				
				opintimation.setInsured(insured);
				opintimation.setIntimationMode(intimationModeValue);
				MastersValue modeOfReceipt = new MastersValue();
				modeOfReceipt.setKey(10203l);
				modeOfReceipt.setValue("Online");
				opintimation.setModeOfReceiptId(modeOfReceipt);
				MastersValue claimType = new MastersValue();
				claimType.setKey(ReferenceTable.OUT_PATIENT);
				claimType.setValue("Out Patient");
				opintimation.setClaimType(claimType);
				opintimation.setIntimationId(oppremiaIntimationTable.getIntimationNumber());
				opintimation.setIntimatedBy(intimationIntimatedBy);
				opintimation.setIntimaterName(oppremiaIntimationTable.getIntimatorName());
				if(null != oppremiaIntimationTable.getIntimatorContactNumber()) {
					String userNameForDB = SHAUtils.getUserNameForDB(oppremiaIntimationTable.getIntimatorContactNumber());
					opintimation.setCallerLandlineNumber(userNameForDB);
					opintimation.setCallerMobileNumber(userNameForDB);
				}

				opintimation.setPolicy(policy);
				opintimation.setIntimationId(oppremiaIntimationTable.getIntimationNumber());
				opintimation.setAdmissionReason(oppremiaIntimationTable.getReasonForConsultation());
				opintimation.setDoctorName(oppremiaIntimationTable.getIntimatorName());
				opintimation.setCallerEmail(oppremiaIntimationTable.getEmailID());
				if(oppremiaIntimationTable.getTreatmentType() != null && oppremiaIntimationTable.getTreatmentType().equalsIgnoreCase("Allopathic")){
					MastersValue treatmentTypeValue = new MastersValue();
					treatmentTypeValue.setKey(10221l);
					treatmentTypeValue.setValue("Allopathic");
					opintimation.setTreatmentTypeId(treatmentTypeValue);
				} else if(oppremiaIntimationTable.getTreatmentType() != null && oppremiaIntimationTable.getTreatmentType().equalsIgnoreCase("Non-Allopathic")){
					MastersValue treatmentTypeValue = new MastersValue();
					treatmentTypeValue.setKey(10222l);
					treatmentTypeValue.setValue("Non-Allopathic");
					opintimation.setTreatmentTypeId(treatmentTypeValue);
				}
				if(oppremiaIntimationTable.getEmergencyFlag() != null){
					opintimation.setEmergencyFlag(oppremiaIntimationTable.getEmergencyFlag());
				}
				if(oppremiaIntimationTable.getAccidentFlag() != null){
					opintimation.setAccidentFlag(oppremiaIntimationTable.getAccidentFlag());
				}
				if(oppremiaIntimationTable.getEmergencyAccidentRemarks() != null){
					opintimation.setRemarksForEmergencyAccident(oppremiaIntimationTable.getEmergencyAccidentRemarks());
				}
				opintimation.setInpatientNumber(oppremiaIntimationTable.getIntimatorContactNumber());
				opintimation.setDoctorNo(Long.valueOf(oppremiaIntimationTable.getIntimatorContactNumber()));
				OrganaizationUnit PIOCpuCode = getOrgUnitByCode(policy.getHomeOfficeCode());
				if(PIOCpuCode != null){
					TmpCPUCode cpuObjectByPincode = getMasCpuCode(Long.parseLong(PIOCpuCode.getCpuCode())); // discussed and entered the policy issuing office CPU Code.
					opintimation.setCpuCode(cpuObjectByPincode);
				}else{
					opintimation.setCpuCode(null);
				}
//				opintimation.setCpuCode(cpuCode);
				opintimation.setIntimationSource(intimationSource);
				MastersValue opLOB = masterService.getMaster(policy.getLobId());
				opintimation.setLobId(opLOB);
				opintimation.setCreatedBy(oppremiaIntimationTable.getCreatedBy());
				opintimation.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				opintimation.setStatus(getStatus(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY));
				opintimation.setStage(getStage(ReferenceTable.INTIMATION_STAGE_KEY));
				if(oppremiaIntimationTable.getOphcConsultationDate() != null){
					opintimation.setAdmissionDate(oppremiaIntimationTable.getOphcConsultationDate());
				}
				if(oppremiaIntimationTable.getEmailID() !=null && !oppremiaIntimationTable.getEmailID().isEmpty())
				{
					opintimation.setCallerEmail(oppremiaIntimationTable.getEmailID());
				}
				if(oppremiaIntimationTable.getAggregatorCode() != null){
					opintimation.setAggregatorCode(oppremiaIntimationTable.getAggregatorCode());
				}
				if(oppremiaIntimationTable.getServiceType() != null){
					opintimation.setServiceType(oppremiaIntimationTable.getServiceType());
				}
				if(oppremiaIntimationTable.getClaimTypeKey() != null){
					opintimation.setClaimTypeKey(oppremiaIntimationTable.getClaimTypeKey());
				} else {
					opintimation.setClaimTypeKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				}
				if(oppremiaIntimationTable.getAggregatorBookingId() != null){
					opintimation.setAggregatorBookingId(oppremiaIntimationTable.getAggregatorBookingId());
				}
				if(oppremiaIntimationTable.getRequestedAmount() !=null){
					opintimation.setRequestedAmount(oppremiaIntimationTable.getRequestedAmount());
				}
				if(oppremiaIntimationTable.getServiceCategory() != null){
					opintimation.setServiceCategory(oppremiaIntimationTable.getServiceCategory());
				}
				
				if(oppremiaIntimationTable.getBookingDate() != null){
					opintimation.setBookingDate(oppremiaIntimationTable.getBookingDate());
				}
				
				if(oppremiaIntimationTable.getServiceProvideName() != null){
					opintimation.setServiceProviderName(oppremiaIntimationTable.getServiceProvideName());
				}
				
				if(oppremiaIntimationTable.getServiceProviderAddress() != null){
					opintimation.setServiceProviderAddress(oppremiaIntimationTable.getServiceProviderAddress());
				}

				
				/*if(opintimation.getInsured() == null){
					log.info(" &&&&&&&&& INTIMATION IS NOT INSERTED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! >"+ oppremiaIntimationTable.getIntimationNumber() +"<------------");
					return null;
				}*/
				if(null != opintimation && opintimation.getInsured() !=null){
					entityManager.persist(opintimation);
					entityManager.flush();
					log.info(" &&&&&&&&& INTIMATION OP INSERTED SUCESSFULLY------>"+ opintimation.getIntimationId() +"<------------");
				}
				utx.commit();
				
				return opintimation;
			
			} catch(Exception e) {
				e.printStackTrace();
				utx.rollback();
			}
			return null;
		}
	 
	 public Boolean processSavedOPIntimationRevised(OPIntimation intimationObj, OPPremiaIntimationTable oppremiaIntimation) throws IllegalStateException, SecurityException, SystemException {
			try {
				utx.begin();
				OPIntimation intimation = getOPIntimationByKey(intimationObj.getKey());
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				log.info("**************WHILE PROCESSING SAVED OP INTIMATION ***********-----> "
						+ intimation != null ? intimation.getIntimationId()
						: (intimation != null ? intimation.getIntimationId()
								+ "--->THIS OP INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
								: "NO OP INTIMATIONS"));
				log.info("**************WHILE PROCESSING SAVED OP INTIMATION ***********-----> " + intimation != null ? intimation.getIntimationId() : (intimation != null ? intimation.getIntimationId() + "--->THIS OP INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US" : "NO OP INTIMATIONS") );

				IntimationRule intimationRule = new IntimationRule();

				if (null != intimation.getAdmissionDate()) {
					String date = String.valueOf(intimation.getAdmissionDate());
					String date1 = date.replaceAll("-", "/");
				}

				OPClaim claimObject = null;
				
				Policy policy = intimation.getPolicy();
				
				log.info("&&&&&&&&&&&&&THIS OP INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
							+ intimation.getIntimationId());
				claimObject = doAutoRegistrationForOP(intimation,oppremiaIntimation);
				
				if(claimObject != null){

				if (claimObject != null) {
					Claim claim = getClaimByIntimationNo(intimationObj
							.getIntimationId());
					if (claim != null) {
						
						updateProvisionAmountToPremia(claim);
						}
					}

				}

				utx.commit();
				dbService.invokeAccumulatorForOP(policy.getPolicyNumber(), intimation.getInsured().getHealthCardNumber(), "0");
				updateOPPremiaIntimationTable(oppremiaIntimation,SHAConstants.SUCCESS_FLAG, SHAConstants.YES_FLAG);
				return true;
			}
			catch(Exception e) {
				e.printStackTrace();
				utx.rollback();
//				updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_FAILURE_STATUS);
				log.error("***************************ERROR OCCURED WHILE OP CLAIM GENERATION **************************" + (intimationObj != null ? intimationObj.getIntimationId() : "NO INTIMATION NUMBER"));
				return false;
			}
					
		}
			
			public OPClaim doAutoRegistrationForOP(OPIntimation objIntimation,OPPremiaIntimationTable oppremiaIntimation) {

				String strClaimTypeRequest = "";
				if(null != objIntimation.getClaimType())
				{
					strClaimTypeRequest = objIntimation.getClaimType().getValue();
				}
				OPClaim objClaim = populateOPClaimObject(objIntimation,oppremiaIntimation, strClaimTypeRequest);
				OPHealthCheckup objOPHealthClaim = populateOPHealthCheckUp(objClaim,objIntimation,oppremiaIntimation, strClaimTypeRequest);
				    log.info("before save claim --->" + objIntimation.getIntimationId());
				try {
					entityManager.persist(objClaim);
					entityManager.flush();
					entityManager.refresh(objClaim);
					
					entityManager.persist(objOPHealthClaim);
					entityManager.flush();
					entityManager.refresh(objOPHealthClaim);
				} catch (Exception e) {

					e.printStackTrace();
					log.error("******************Not Pulled from Premia***************** -----> " + objIntimation.getIntimationId());
				}

				log.info("after save claim --->" + objIntimation.getIntimationId() + "Claim No --- >" + objClaim.getClaimId());
				
				objIntimation = getOPIntimationByKey(objIntimation.getKey());
				entityManager.merge(objIntimation);
				entityManager.flush();
				return objClaim;
			}
			
			public OPIntimation getOPIntimationByKey(Long intimationKey)
			{
				Query query = entityManager.createNamedQuery("OPIntimation.findByKey");
				query = query.setParameter("intiationKey", intimationKey);
				List<OPIntimation> intimationList = query.getResultList();
				if(null != intimationList && !intimationList.isEmpty())
				{
					entityManager.refresh(intimationList.get(0));
					return intimationList.get(0);
				}
				return null;
			}
			
			public OPClaim populateOPClaimObject(OPIntimation objIntimation,OPPremiaIntimationTable oppremiaIntimation,String strClaimTypeRequest) {

				OPClaim claimObj = new OPClaim();
				Status objStatus = new Status();
				Stage objStage = new Stage();
				MastersValue masValue = new MastersValue();
				MastersValue masForClaim = new MastersValue();
				//objStatus.setKey(11l);
				objStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				objStage.setKey(9l);
				claimObj.setIntimation(objIntimation);
				claimObj.setStatus(objStatus);
				claimObj.setStage(objStage);
				claimObj.setOfficeCode(objIntimation.getOfficeCode());
				claimObj.setCreatedBy(objIntimation.getCreatedBy());
				claimObj.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				claimObj.setAccidentFlag(objIntimation.getAccidentFlag());
				claimObj.setEmergencyFlag(objIntimation.getEmergencyFlag());
				claimObj.setDoctorNo(Long.valueOf(oppremiaIntimation.getIntimatorContactNumber()));
				claimObj.setDocumentReceivedDate(oppremiaIntimation.getBillReceivedDate());
				if(objIntimation.getRemarksForEmergencyAccident() != null){
					claimObj.setRemarksForEmergencyAccident(objIntimation.getRemarksForEmergencyAccident());
				}
				if(objIntimation.getAdmissionDate() != null){
					claimObj.setDataOfAdmission(objIntimation.getAdmissionDate());
				}
				if(objIntimation.getLobId() != null && objIntimation.getLobId().getKey() != null){
					claimObj.setLobId(objIntimation.getLobId().getKey());
				}
				if(objIntimation.getModeOfReceiptId() != null){
					claimObj.setModeOfReceiptId(objIntimation.getModeOfReceiptId().getKey());
				}
				if(objIntimation.getTreatmentTypeId() != null){
					claimObj.setTreatmentTypeId(objIntimation.getTreatmentTypeId().getKey());
				}
				
				if(objIntimation.getCpuCode() != null){
					claimObj.setOriginalCpuCode(objIntimation.getCpuCode().getCpuCode());
				}
				if(oppremiaIntimation.getSectionCode() != null
						&& oppremiaIntimation.getSectionCode().equals("SEC-OP-COV-01")){
					claimObj.setConsulationTypeId(101l);
					claimObj.setOpcoverSection("SEC-OP-COV-01");
				} else if(oppremiaIntimation.getSectionCode() != null
						&& oppremiaIntimation.getSectionCode().equals("SEC-OP-COV-02")){
					claimObj.setConsulationTypeId(102l);
					claimObj.setOpcoverSection("SEC-OP-COV-02");
				} else if(oppremiaIntimation.getSectionCode() != null
						&& oppremiaIntimation.getSectionCode().equals("SEC-OP-COV-03")){
					claimObj.setConsulationTypeId(103l);
					claimObj.setOpcoverSection("SEC-OP-COV-03");
				}
				MastersValue value = new MastersValue();
				value.setKey(ReferenceTable.OUT_PATIENT);
				value.setValue("Out Patient");
				claimObj.setClaimType(value);

				Policy policyByKey = getPolicyByKey(objIntimation.getPolicy().getKey());
				
				MASClaimAdvancedProvision claimAdvProvision = getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));

//				Double amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
//						 , objIntimation.getCpuCode() != null ? objIntimation.getCpuCode().getProvisionAmount() : 0d,objIntimation.getKey());
				
				Double amt = 0d;
				/*amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
						 , claimAdvProvision != null ? claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d : 0d,objIntimation.getKey(),claimObj);*/
				
				claimObj.setClaimedAmount(oppremiaIntimation.getAmountClaimed());
//				claimObj.setProvisionAmount(oppremiaIntimation.getAmountClaimed());
				Long claimKey= 0l;
				Map<String, Integer> amountList = dbService.getOPAvailableAmount(objIntimation.getInsured().getKey(),claimKey, claimObj.getClaimType().getKey() != null ? claimObj.getClaimType().getKey() : 0l,
						claimObj.getOpcoverSection() != null ? claimObj.getOpcoverSection() : "0");
				Integer opAvailableAmount = 0;
				Integer perClaimLimit = 0;
				Integer perPolicyLimit = 0;
				
				if(amountList != null && !amountList.isEmpty()){
					opAvailableAmount = amountList.get(SHAConstants.CURRENT_BALANCE_SI);
					perClaimLimit = amountList.get(SHAConstants.OP_CLAIM_LIMIT);
					perPolicyLimit = amountList.get(SHAConstants.OP_POLICY_LIMIT);
				}
				Double min = 0d;
				Double minclaimLimit = 0d;
				Double minSi = 0d;
				Double amountClaimed = 0d;
				if(oppremiaIntimation.getAmountClaimed() != null){
					amountClaimed = oppremiaIntimation.getAmountClaimed();
				}
				if(perClaimLimit != null &&  perClaimLimit > 0){
					minclaimLimit = Math.min(Double.valueOf(perClaimLimit.toString()), opAvailableAmount);
					min = Math.min(oppremiaIntimation.getAmountClaimed(),minclaimLimit);
				} else if(perPolicyLimit != null && perPolicyLimit > 0){
					minclaimLimit = Math.min(Double.valueOf(perPolicyLimit.toString()), opAvailableAmount);
					min = Math.min(amountClaimed,minclaimLimit);
				} else {
					min= Math.min(amountClaimed, opAvailableAmount);
				}
				claimObj.setProvisionAmount(min);
				claimObj.setCurrentProvisionAmount(min);

				claimObj.setRegistrationRemarks("Auto Registered");
				claimObj.setCurrentProvisionAmount(amt);
				
				return claimObj;
			}
			
			public OPHealthCheckup populateOPHealthCheckUp(OPClaim objClaim,OPIntimation objIntimation,OPPremiaIntimationTable oppremiaIntimation,String strClaimTypeRequest) {

				OPHealthCheckup opHealthclaimObj = new OPHealthCheckup();
				/*Status objStatus = new Status();
				Stage objStage = new Stage();*/
				MastersValue masValue = new MastersValue();
				MastersValue masForClaim = new MastersValue();
				//objStatus.setKey(11l);
				/*objStatus.setKey(ReferenceTable.INTIMATION_REGISTERED_STATUS);
				objStage.setKey(9l);*/
				opHealthclaimObj.setClaim(objClaim);
				opHealthclaimObj.setIntimation(objIntimation);
				Status status = new Status();
				status.setKey(ReferenceTable.OP_REGISTER_CLAIM_STATUS);
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.OP_STAGE);
				opHealthclaimObj.setStatus(status);
				opHealthclaimObj.setStage(stage);
				opHealthclaimObj.setDocumentReceivedFromId(objIntimation.getModeOfReceiptId());
				opHealthclaimObj.setOpHealthCheckupDate(oppremiaIntimation.getOphcConsultationDate());
				opHealthclaimObj.setBillReceivedDate(oppremiaIntimation.getBillReceivedDate());
				opHealthclaimObj.setOpHealthRemarks(oppremiaIntimation.getReasonForConsultation());
				opHealthclaimObj.setCreatedBy(objIntimation.getCreatedBy());
				opHealthclaimObj.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				return opHealthclaimObj;
			}
			
			public OrganaizationUnit getOrgUnitByCode(String polDivnCode) {
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
			
			public Policy getPolicyDetails(Long policyKey) {
				Query query = entityManager.createNamedQuery("Policy.findByKey");
				query = query.setParameter("policyKey", policyKey);
				List<Policy> resultList = (List<Policy>)query.getResultList();
				if(resultList != null && !resultList.isEmpty()) {
					return resultList.get(0);
				}
				return null;
			}
	

			public List<DocAcknowledgement> loadAcknowledgementDocForTopup() {
				try {
					
					Query query = entityManager
							.createNamedQuery("DocAcknowledgement.findByFlag");
							query = query.setParameter("ackLetterFlag", "N");
					List<DocAcknowledgement> resultList = query.getResultList();
					
					if(resultList != null && ! resultList.isEmpty()){
						return resultList;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			public void adjustPolicyInstallmentProcess(){
				
				List<ClaimPayment> paymentList = getUniquePremiumPremiaUpdationFromPayment();
				try
				{
					utx.begin();
					if(null != paymentList && !paymentList.isEmpty()) {
						log.info("&&&&&&&&&&&&&& Settled cases premium deduction happening -----> "   );
						for (ClaimPayment claimPayment : paymentList) {
							JSONObject jsonObj = new JSONObject();
							jsonObj.put(PremiaConstants.POLICY_NUMBER, claimPayment.getPolicyNumber());
							jsonObj.put(PremiaConstants.INTIMATION_NUMBER, claimPayment.getIntimationNumber());
							jsonObj.put(PremiaConstants.CPU_CODE, claimPayment.getCpuCode() != null ? String.valueOf(claimPayment.getCpuCode()) : "");
							Double amt = (claimPayment.getApprovedAmount() != null ? claimPayment.getApprovedAmount() : 0d) - (claimPayment.getTotalApprovedAmount() != null ?claimPayment.getTotalApprovedAmount() : 0d) ;
							log.info("&&&&&&&&&&&&&& Settled cases premium deduction happening---> Adjustment Amount -----> " + amt  );
							jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(amt));
							PremiaService.getInstance().updateAdjustPolicyInstallment(jsonObj.toString());
							
							claimPayment.setPremiaWSFlag(SHAConstants.YES_FLAG);
							entityManager.merge(claimPayment);
							entityManager.flush();
						}
					}
					utx.commit();
			}
			catch(Exception e)
				{
					e.printStackTrace();
					try {
						utx.rollback();
					}  catch (IllegalStateException | SecurityException
							| SystemException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.info("***** EXCEPTION OCCURED IN X RAY PULL BATCH ********************-----> ");
					}
				}
			}

			public Policy  getPolicyByPolicyKey(Long policyKey) {
				
				Query query = entityManager.createNamedQuery("Policy.findByKey");
		    	query = query.setParameter("policyKey", policyKey);
		    	Policy policyList = (Policy) query.getSingleResult();
		    	if (policyList != null){
		    		return policyList;
		    	}
				return null;
			}
			
			@SuppressWarnings({ "unchecked", "unused" })
			public List<DocumentCheckListDTO> getDocumentList(Long ackKey) {
				List<RODDocumentCheckList> documentChkLst = null;
				Query query = entityManager
						.createNamedQuery("RODDocumentCheckList.findByDocKey");
				query.setParameter("docKey", ackKey);
				if (null != query.getResultList()) {
					documentChkLst = (List<RODDocumentCheckList>) query.getResultList();
				}
				List<DocumentCheckListDTO> documentDetailList = new ArrayList<DocumentCheckListDTO>();
				if (documentChkLst != null) {
					for (int i = 0; i < documentChkLst.size(); i++) {
						DocumentCheckListDTO dto = new DocumentCheckListDTO();
						if (null != documentChkLst.get(i).getReceivedStatusId()) {
							SelectValue selected = new SelectValue();
							selected.setId(documentChkLst.get(i).getReceivedStatusId()
									.getKey());
							selected.setValue(documentChkLst.get(i)
									.getReceivedStatusId().getValue());
							dto.setReceivedStatus(selected);
						}
						dto.setNoOfDocuments(documentChkLst.get(i).getNoOfDocuments());
						dto.setRemarks(documentChkLst.get(i).getRemarks());
						if (null != documentChkLst.get(i).getDocumentTypeId()) {
							Query documentQuery = entityManager
									.createNamedQuery("DocumentCheckListMaster.findByKey");
							documentQuery.setParameter("primaryKey", documentChkLst
									.get(i).getDocumentTypeId());
							DocumentCheckListMaster masterValue = (DocumentCheckListMaster) documentQuery
									.getSingleResult();
							dto.setValue(masterValue.getValue());
							dto.setMandatoryDocFlag(masterValue.getMandatoryDocFlag());
							dto.setRequiredDocType(masterValue.getRequiredDocType());
						}
						documentDetailList.add(dto);
					}
				}
				return documentDetailList;
			}

	public void updateAcknowledgementLetterGenFlag(
			DocAcknowledgement docAcknowledgement)
			throws IllegalStateException, SecurityException, SystemException {

		try {
			utx.begin();
			entityManager.merge(docAcknowledgement);
			entityManager.flush();
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
		}
	}

	public MasProductCpuRouting getMasProductForGMCRouting(Long key){
			
		 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		 query = query.setParameter("key", key);
		 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0);
		 } 
				
		 return null;
	}
	
	//Added For Accident Trauma care Group product		
			public List<GalaxyIntimationTable> processGalaxyIntimationDataForPA(String batchSize) {
				try {
					log.info("********* BATCH SIZE FOR GALAXY PULL ACC-PRD-008******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
					List<GalaxyIntimationTable> premiaIntimationList = fetchRecFromGalaxyIntimationTableForPA(batchSize);
					return premiaIntimationList;
				} catch(Exception e) {
					log.error("********* EXCEPTION OCCURED DURING FETCH FROM GALAXY TABLE ACC-PRD-008**********************" + e.getMessage());
					return null;
				}
			}
			
			//Added For Accident Trauma care Group product		
			private List<GalaxyIntimationTable> fetchRecFromGalaxyIntimationTableForPA(String batchSize) {
				Query query = entityManager.createNamedQuery("GalaxyIntimationTable.findAllPa");
				query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
				/**
				 * Will remove once the final integration of batch is over.
				 * */
				if(batchSize != null) {
					query.setMaxResults(SHAUtils.getIntegerFromString("20"));
				}
				List<GalaxyIntimationTable> premiaIntimationTableList = query.getResultList();
				log.info("********* COUNT FOR GALAXY PULL FOR GROUP PA ACC-PRD-008******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
				return premiaIntimationTableList;
			}
			
			public Boolean processSavedIntimationRevisedGalaxy(Intimation intimationObj, GalaxyIntimationTable premiaIntimation) throws IllegalStateException, SecurityException, SystemException {
				try {
					utx.begin();
					Intimation intimation = getIntimationByKey(intimationObj.getKey());
					Map<String, Object> mapValues = new WeakHashMap<String, Object>();
					log.info("**************WHILE PROCESSING SAVED GLX INTIMATION ***********-----> "
							+ intimation != null ? intimation.getIntimationId()
							: (intimation != null ? intimation.getIntimationId()
									+ "--->THIS GLX INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
									: "NO INTIMATIONS"));
					log.info("**************WHILE PROCESSING SAVED GLX INTIMATION ***********-----> " + intimation != null ? intimation.getIntimationId() : (intimation != null ? intimation.getIntimationId() + "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US" : "NO INTIMATIONS") );

					IntimationRule intimationRule = new IntimationRule();
					/*IntimationType a_message = new IntimationType();
					ClaimRequestType claimReqType = new ClaimRequestType();
		<<<<<<< HEAD
					ClaimType claimType = new ClaimType();*/

					String strClaimType = "";
					Boolean isHospTopUpIntmAvail = false;
					/**
					 * All PA claims goes to manual registration only. Hence
					 * below condition was added.
					 * */
				
					//added by noufel for HC topup intimatin creation
					if(isHospCashTopUpAvail(intimation.getIntimationId())) {
						isHospTopUpIntmAvail =true;
					}
					if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
						strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
					}
					else if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
						strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
					}
					if(isHospTopUpIntmAvail){
						strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
					}
					//claimType.setClaimType(strClaimType);
					if (null != intimation.getCpuCode()
							&& null != intimation.getCpuCode().getCpuCode())
						/*claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode()
								.getCpuCode()));
					a_message.setKey(intimation.getKey());*/
					if (null != intimation.getAdmissionDate()) {
						String date = String.valueOf(intimation.getAdmissionDate());
						String date1 = date.replaceAll("-", "/");
						//a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
//						a_message.setIntDate(new Timestamp(System.currentTimeMillis()));
//						Timestamp timestamp = new Timestamp(
//								System.currentTimeMillis() + 60 * 60 * 1000);
//						a_message.setIntDate(timestamp);
					}
					/*a_message.setIntimationNumber(intimation.getIntimationId());
					a_message
							.setIntimationSource(intimation.getIntimationSource() != null ? (intimation
									.getIntimationSource().getValue() != null ? intimation
									.getIntimationSource().getValue() : "")
									: "");
					a_message.setIsClaimPending(!intimationRule
							.isClaimExist(intimation));
					a_message
							.setIsPolicyValid(intimationRule.isPolicyValid(intimation));*/
					DBCalculationService dbCalculationService = new DBCalculationService();
					
					Double balsceSI = 0d;
					if(null != intimation && null != intimation.getPolicy() && 
							null != intimation.getPolicy().getProduct() && 
							null !=intimation.getPolicy().getProduct().getKey() &&
							!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
						
						if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
							balsceSI = dbCalculationService.getBalanceSIForGMC(intimation.getPolicy().getKey() ,intimation.getInsured().getKey(),0l);
						}else{
							balsceSI = dbCalculationService.getBalanceSI(
									intimation.getPolicy().getKey(),
									intimation.getInsured().getKey(), 0l,
									intimation.getInsured().getInsuredSumInsured(),
									intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
								
						}
					}
					else
					{
						balsceSI = dbCalculationService.getGPABalanceSI(
								intimation.getPolicy().getKey(),
								intimation.getInsured().getKey(), 0l,
								intimation.getInsured().getInsuredSumInsured(),
								intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					}
					

					String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
					if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
							.equalsIgnoreCase(strClaimType)) {
						if (null != balsceSI
								&& balsceSI > 0
								&& null != intimation.getPolicy()
								&& (SHAConstants.NEW_POLICY)
										.equalsIgnoreCase(intimation.getPolicy()
												.getPolicyStatus())
								&& null != strPremiaFlag
								&& ("true").equalsIgnoreCase(strPremiaFlag)) {
							//a_message.setIsBalanceSIAvailable(true);
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
							String get64vbStatus = PremiaService.getInstance()
									.get64VBStatus(
											intimation.getPolicy().getPolicyNumber(), intimation.getIntimationId());
							
							/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
								get64vbStatus = "R";
							}*/
							
							if (get64vbStatus != null
									&& (SHAConstants.DISHONOURED
											.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
											.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
											.equalsIgnoreCase(get64vbStatus))) {
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
							}
						} else {
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
							if (null != intimation.getPolicy()
									&& !(SHAConstants.NEW_POLICY)
											.equalsIgnoreCase(intimation.getPolicy()
													.getPolicyStatus())) {
								Boolean endorsedPolicyStatus = PremiaService
										.getInstance().getEndorsedPolicyStatus(
												intimation.getPolicy()
														.getPolicyNumber());
								/*
								if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
									endorsedPolicyStatus = true;
								}
								*/

								if (endorsedPolicyStatus) {
									mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
									String get64vbStatus = PremiaService.getInstance()
											.get64VBStatus(
													intimation.getPolicy()
															.getPolicyNumber(), intimation.getIntimationId());
									/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
										get64vbStatus = "R";
									}*/
									if (get64vbStatus != null
											&& (SHAConstants.DISHONOURED
													.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING
													.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE
													.equalsIgnoreCase(get64vbStatus))) {
										mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");

									}
								}
								
							}
						}
					} else {
						if (null != intimation.getPolicy()
								&& !(SHAConstants.NEW_POLICY)
										.equalsIgnoreCase(intimation.getPolicy()
												.getPolicyStatus())) {
							Boolean endorsedPolicyStatus = PremiaService.getInstance()
									.getEndorsedPolicyStatus(
											intimation.getPolicy().getPolicyNumber());
							/**
							 * As per satish sir, Endorsement status is not consider for GMC
							 */
							/*if(intimation.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE)){
								endorsedPolicyStatus = true;
							}*/
							if (endorsedPolicyStatus)
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
							else
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
						} else {
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
						}
					}
					if(null != intimation.getLobId())
						{
							if(!(ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey())) 
									&& (!((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey())
											&& (intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)))))
							{
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"N");
							}
			            }
					
					
					if(intimation.getPolicy().getProduct() != null && !(ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(intimation.getPolicy().getProduct().getKey())) && ReferenceTable.getSuperSurplusKeys().containsKey(intimation.getPolicy().getProduct().getKey())
							&& intimation.getPolicy().getPolicyPlan() != null && intimation.getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI,"N");
					}
					
					Claim claimObject = null;
					
					Policy policy = intimation.getPolicy();
					if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(policy.getProduct().getKey())){
						Date policyFromDate = policy.getPolicyFromDate();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(policyFromDate);
						calendar.add(Calendar.YEAR, 1);
					    Date afterOneYear = calendar.getTime();
					    
					    log.info("*********After One year For unique policy ********  " +afterOneYear);
					    
					    Date policyToDate = policy.getPolicyToDate();
					    
					    log.info("*********Policy To Date For unique policy ********  " +policyToDate);
					    
					    if(intimation.getAdmissionDate() != null && intimation.getAdmissionDate().after(afterOneYear) && intimation.getAdmissionDate().before(policyToDate)){
					    	Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(policy.getPolicyNumber());
					    	if(uniqueInstallmentAmount > 0){
					    		mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
					    	}
					    }
					}
					 //added for CR2019184 to allow manual registration process if installment premium is pending 
				    if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getPolicyInstalmentFlag() != null &&
				    		intimation.getPolicy().getProduct().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				    	Integer policyInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(policy.getPolicyNumber());
				    	if(policyInstallmentAmount != null && policyInstallmentAmount > 0){
				    		mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
				    	}
				    }
				    
					if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
						log.info("&&&&&&&&&&&&&THIS GLX INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
								+ intimation.getIntimationId());
						claimObject = doAutoRegistrationProcessRevised(intimation);
					}
					else {
						log.info("%%%%%***MANUAL**** *THIS GLX INTIMATION IS  GOING TO MANUAL REGISTRATION  -------->"
								+ ((balsceSI != null && balsceSI > 0) ? "BALANCE IS IS "
										+ String.valueOf(balsceSI) + ""
										: "64VB or ENDORSED ")
								+ "   INTIMATION NUMBER------>"
								+ intimation.getIntimationId());
					}
					
					Hospitals hospital = getHospitalByHospNo(premiaIntimation
							.getGiHospCode());
					
						
					if(hospital == null){
						hospital = getHospitalByHospNo(premiaIntimation
								.getGiHospCode());
					}

					// }


					String userId = BPMClientContext.BPMN_TASK_USER;
					String password = BPMClientContext.BPMN_PASSWORD;
			

					
					if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI)) && claimObject != null){
						
						/**
						 *    auto registration for cashless claim and reimbursement claim
						 */
						
						if(claimObject.getClaimId() == null && claimObject.getKey() != null){
							claimObject = getClaimByClaimKey(claimObject.getKey());
						}
						
						//Added as per Satish Sir Suggestion while do auto registration of galaxy claim
						if(claimObject != null && hospital != null){
							PremiaService.getInstance().getPolicyLock(claimObject, hospital.getHospitalCode());
						}
						
//						Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObject, hospital);
						
						Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
						
						Object[] inputArray = (Object[])arrayListForDBCall[0];
						
						if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType)){
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
						}else{
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME;
						}
						
						if(hospital.getFspFlag() != null && hospital.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
							 MastersValue master = getMaster(ReferenceTable.FSP);
							 if(master != null){
								 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
							 }
				    	}
						
						/*if(null != intimation.getLobId())
						{
								if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
								{
									inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
									inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
									inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
								}
								
						}*/
						Object[] parameter = new Object[1];
						parameter[0] = inputArray;
//						dbCalculationService.initiateTaskProcedure(parameter);
						dbCalculationService.revisedInitiateTaskProcedure(parameter);
						
					}else{
						
						/**
						 *    Manual registration for cashless claim and reimbursement claim
						 */
						
//						Object[] arrayListForDBCall = SHAUtils.getArrayListForManualRegDBCall(intimation, hospital);
						
						Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForManualRegistrationDBCall(intimation, hospital);
						
						Object[] inputArray = (Object[])arrayListForDBCall[0];
						
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
						
						if(null != intimation.getLobId())
						{
								if(! ReferenceTable.HEALTH_LOB_KEY.equals(intimation.getLobId().getKey()))
								{
									inputArray[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;
									inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
								}
						}
						
						if(hospital.getFspFlag() != null && hospital.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
							 MastersValue master = getMaster(ReferenceTable.FSP);
							 if(master != null){
								 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
							 }
				    	}
						
						Object[] parameter = new Object[1];
						parameter[0] = inputArray;
//						dbCalculationService.initiateTaskProcedure(parameter);
						dbCalculationService.revisedInitiateTaskProcedure(parameter);
						
					}
					
					if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType) && null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
						
						/**
						 * Initiate for Reimbursement claim for auto Registration Only.
						 */

					}

					if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))
							&& (SHAConstants.REIMBURSEMENT_CLAIM_TYPE)
									.equalsIgnoreCase(strClaimType)
							&& claimObject != null) {
		                log.info("****************&&&&&&& REMAINDER_INITIATED FOR AUTO REGISTRATION REIMBURSEMENT" + intimation.getIntimationId());
						
						Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
						
						Object[] inputArray = (Object[])arrayListForDBCall[0];
						
						inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.BILLS_NOT_RECEIVED;
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS;

						callReminderTaskForDB(inputArray);
						
//						autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
						
						log.info("*******&&&&&&&&& FVR INITIATED FOR REIMBURSEMENT CLAIM **-->"
								+ intimation.getIntimationId());
					}
					
					if (null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI))){
						autoRegisterFVR(intimationObj, BPMClientContext.BPMN_TASK_USER);
					}
					
					if(claimObject != null){

					if (claimObject != null) {
						Claim claim = getClaimByIntimationNo(intimationObj
								.getIntimationId());
						if (claim != null) {
							
							updateProvisionAmountToPremia(claim);
							}
						}

					}

					utx.commit();
					updateGalaxyIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
					return true;
				}
				catch(Exception e) {
					e.printStackTrace();
					utx.rollback();
//					updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_FAILURE_STATUS);
					log.error("***************************ERROR OCCURED WHILE GLX CLAIM GENERATION **************************" + (intimationObj != null ? intimationObj.getIntimationId() : "NO INTIMATION NUMBER"));
					return false;
				}
						
			}
			
			public void updateGalaxyIntimationTable(GalaxyIntimationTable premiaIntimationTable, String message) throws IllegalStateException, SecurityException, SystemException {
				if(null != premiaIntimationTable && null != premiaIntimationTable.getGiIntimationId()){
					try {
						utx.begin();
						premiaIntimationTable.setGiSavedType(message);
						entityManager.merge(premiaIntimationTable);
						entityManager.flush();
						utx.commit();
					} catch(Exception e) {
						utx.rollback();
					}
				}
			}
			
			@TransactionAttribute(TransactionAttributeType.REQUIRED)
			public Intimation insertGalaxyIntimationDataRevised(GalaxyIntimationTable premiaIntimationTable,String lobFlag) throws NotSupportedException, SystemException {
				try {
					utx.begin();
					Boolean isHospTypeEmpty = false;
					Intimation intimation  = new Intimation();
					MastersValue intimationModeValue = null;
					MastersValue intimationIntimatedBy = null;
					Policy policy = null;
		 			Hospitals hospitals = null;
					MastersValue admissionType = null;
					MastersValue managementType = null;
					MastersValue roomCategory = null;
					TmpCPUCode cpuCode = null;
					MastersValue intimationSource = null;
					MastersValue hospitalType = new MastersValue();
					MastersValue claimType = new MastersValue();
					Insured insured = null;
					Boolean isHospTopUpIntmAvail = false;
					
					/*//MED-PRD-076
					if(premiaIntimationTable.getProductCode() != null && premiaIntimationTable.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
							|| premiaIntimationTable.getProductCode() != null && premiaIntimationTable.getProductCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
						premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
					}*/
					//added by noufel for HC topup intimatin creation
					if(isHospCashTopUpAvail(premiaIntimationTable.getGiIntimationNo())) {
						isHospTopUpIntmAvail =true;
					}
					if(null != premiaIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimationMode()))
						intimationModeValue = getMastersValue(premiaIntimationTable.getGiIntimationMode(),ReferenceTable.MODE_OF_INTIMATION);
					else
						intimationModeValue = getMastersValue(SHAConstants.INTIMATION_MODE_PHONE,ReferenceTable.MODE_OF_INTIMATION);
					
					if(null != premiaIntimationTable.getGiIntimationMode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimationMode())){
						if(premiaIntimationTable.getGiIntimationMode().equalsIgnoreCase(SHAConstants.INTIMATION_MODE_WEBSERVICE)){
							intimationModeValue = getMaster(ReferenceTable.INTIMATION_ONLINE_MODE);
						}
					}

					if(null != premiaIntimationTable.getGiIntimatedBy() && !("").equalsIgnoreCase(premiaIntimationTable.getGiIntimatedBy()))
						intimationIntimatedBy = getMastersValue(premiaIntimationTable.getGiIntimatedBy(),ReferenceTable.INTIMATED_BY);
					else
						intimationIntimatedBy = getMastersValue(SHAConstants.INTIMATION_INTIMATED_BY,ReferenceTable.INTIMATED_BY);
					
					if(null != premiaIntimationTable.getGiPolNo() && !("").equalsIgnoreCase(premiaIntimationTable.getGiPolNo()))
						policy = getPolicyByPolicyNubember(premiaIntimationTable.getGiPolNo());
					
					/* For the product star criticare platinum and gold,cashless process is not applicable and it should be routed to reimbursement
					  even the user create intimation for network hospital.the below code is added for this scenario */
					if(null != premiaIntimationTable.getProductCode() &&
						(ReferenceTable.getDirectReimbursementProducts().containsKey(premiaIntimationTable.getProductCode()))) {
						premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
					}
					
					// GLX2020054 add for star novel grp corona
					if(policy != null && (policy.getProduct().getCode().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE) /*||
							policy.getProduct().getCode().equals(ReferenceTable.STAR_GRP_COVID_PROD_CODE)*/)&& null !=  policy.getPolicyPlan()&& (policy.getPolicyPlan().equals(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))) {
							premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
					}
					
					if(policy != null && (SHAConstants.N_FLAG).equals(policy.getProduct().getCashlessElgFlag())){
						premiaIntimationTable.setGiHospitalTypeYn(SHAConstants.PREMIA_NON_NETWORK_HOSPITAL);
					}
					
					if(null != premiaIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiHospCode())) {
						
						String hospCode =  premiaIntimationTable.getGiHospCode();
						hospitals = getHospitalByHospNo(hospCode.toUpperCase());
						
						if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) ||
								isHospTopUpIntmAvail) {
							
							if(policy.getHomeOfficeCode() != null) {
								OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
								if(branchOffice != null){
									String officeCpuCode = branchOffice.getCpuCode();
									if(officeCpuCode != null) {
										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
									}
								}
							}
							
						}else if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
							if(null != hospCode) {
								log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
								if(null != hospitals)
									cpuCode =  getCpuDetails(hospitals.getCpuId());
							}
						}
					}
					else
					{
						String hospCode =  SHAConstants.DEFAULT_HOSP_CODE;
						isHospTypeEmpty = true;
						if(null != hospCode) {
							log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
							hospitals = getHospitalByHospNo(hospCode.toUpperCase());
							if(null != hospitals)
								cpuCode =  getCpuDetails(hospitals.getCpuId());
						}
					}
					//added foe CR GMC CPU Routing GLX2020075
					if(policy != null && policy.getProduct().getKey() != null){
						MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(policy.getProduct().getKey());
						if(gmcRoutingProduct != null){
							if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {	
								if(null != premiaIntimationTable.getGiHospCode() && !("").equalsIgnoreCase(premiaIntimationTable.getGiHospCode())) {
									String hospCode =  premiaIntimationTable.getGiHospCode();
									hospitals = getHospitalByHospNo(hospCode.toUpperCase());
									log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :"+hospCode.toUpperCase());
									if(null != hospitals)
										cpuCode =  getCpuDetails(hospitals.getCpuId());
									if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
										cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
									}

								}
							}else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
								if(policy.getHomeOfficeCode() != null) {
									OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
									if(branchOffice != null){
										String officeCpuCode = branchOffice.getCpuCode();
										if(officeCpuCode != null) {
											MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
											if(masCpuLimit != null){
												cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
											}else {
												cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
												if(cpuCode != null && cpuCode.getGmcRoutingCpuCode() !=null){
													cpuCode =  getMasCpuCode( cpuCode.getGmcRoutingCpuCode());
												}
											}
										}
									}
								}

							}
							intimation.setCpuCode(cpuCode);
						}
					}
					//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
					/*if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
						cpuCode =  getCpuDetails(ReferenceTable.GMC_CPU_CODE);
						if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
							if(policy.getHomeOfficeCode() != null) {
								OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
								if(branchOffice != null){
									String officeCpuCode = branchOffice.getCpuCode();
									if(officeCpuCode != null) {
										MasCpuLimit masCpuLimit = getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
										if(masCpuLimit != null){
											cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
										}
									}
								}
							}
					   }
						intimation.setCpuCode(cpuCode);
					}*/
					
					if(policy != null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(policy.getProduct().getKey())){
						String baayasCpuCodeByPolicy = getPaayasCpuCodeByPolicy(policy.getPolicyNumber());
						if(baayasCpuCodeByPolicy != null){
							cpuCode = getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
							if(cpuCode != null){
								intimation.setCpuCode(cpuCode);
							}
						}
						
						String jioCpuCodeByPolicy = getJioPolicyCpuCode(policy.getPolicyNumber());
						if(jioCpuCodeByPolicy != null){
							cpuCode = getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
							if(cpuCode != null){
								intimation.setCpuCode(cpuCode);
							}
						}
						
						Long tataPolicy = getTataPolicy(policy.getPolicyNumber());
						if(tataPolicy != null){
							cpuCode = getMasCpuCode(tataPolicy);
							if(cpuCode != null){
								intimation.setCpuCode(cpuCode);
							}
						}
						
						String kotakCpuCodeByPolicy = getKotakPolicyCpuCode(policy.getPolicyNumber());
						if(kotakCpuCodeByPolicy != null){
							cpuCode = getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
							if(cpuCode != null){
								intimation.setCpuCode(cpuCode);
							}
						}
						
						
					}
					
					if(policy != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
						cpuCode =  getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
						intimation.setCpuCode(cpuCode);
					}
					//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//					//added for CPU routing
//					if(policy != null && policy.getProduct().getKey() != null){
//						String CpuCode= getMasProductCpu(policy.getProduct().getKey());
//						if(CpuCode != null){
//							cpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//							intimation.setCpuCode(cpuCode);
//						}
//					}
//					//added for CPU routing
					
					String gpaPolicyDetails = getGpaPolicyDetails(policy.getPolicyNumber());
					if(gpaPolicyDetails != null){
						TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
						if(masCpuCode != null){
							intimation.setCpuCode(masCpuCode);
							cpuCode = masCpuCode;
						}
					}
					
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) 
							{
								hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
							}
					else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
					}
//					if(null != premiaIntimationTable.getGiClmProcDivn()){
//						cpuCode = getMasCpuCode(Long.parseLong(premiaIntimationTable.getGiClmProcDivn()));
//					}
					
					/***
					 * Added for pa claims .
					 * */
					setGalaxyClaimTypeAndHospitalType(premiaIntimationTable,hospitalType,claimType,policy,cpuCode,premiaIntimationTable.getGiPACategory(),isHospTypeEmpty,isHospTopUpIntmAvail);
				
					if(null != premiaIntimationTable.getGiAdmissionType() && !("").equalsIgnoreCase(premiaIntimationTable.getGiAdmissionType()))
						admissionType = getMastersValue(premiaIntimationTable.getGiAdmissionType(), ReferenceTable.ADMISSION_TYPE);
					
					if(null != premiaIntimationTable.getGiManagementType() && !("").equalsIgnoreCase(premiaIntimationTable.getGiManagementType()))
						managementType = getMastersValue(premiaIntimationTable.getGiManagementType(),ReferenceTable.TREATMENT_MANAGEMENT);
					else
						managementType = getMastersValue(SHAConstants.MGMT_SURGICAL,ReferenceTable.TREATMENT_MANAGEMENT);
					if(null != premiaIntimationTable.getGiRoomCategory() && !("").equalsIgnoreCase(premiaIntimationTable.getGiRoomCategory()))
						roomCategory = getMastersValue(premiaIntimationTable.getGiRoomCategory(), ReferenceTable.ROOM_CATEGORY);
					
					/*else
						roomCategory = getMastersValue(SHAConstants.DELUXE, ReferenceTable.ROOM_CATEGORY);*/

					if(null != premiaIntimationTable.getGiInsuredName() && !("").equalsIgnoreCase(premiaIntimationTable.getGiInsuredName())){
						insured = getInsuredByPolicyAndInsuredName(premiaIntimationTable.getGiPolNo(), premiaIntimationTable.getGiInsuredName(),premiaIntimationTable.getGiPACategory());
					}
					
					if(insured == null){
						insured = getInsuredByPolicyAndInsuredNameForDefault(premiaIntimationTable.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
					}
					/*
					 * Intimation source - Currently in premia , there is no column available 
					 * in premia table. As of now,  hardcoding 131. Later this needs to be removed.
					 * */
					intimationSource = new MastersValue();
					
					if(null != premiaIntimationTable.getGiSavedType() && (SHAConstants.PREMIA_INTIMATION_HOSP_SAVE).equalsIgnoreCase(premiaIntimationTable.getGiSavedType())) {
						intimationSource.setKey(ReferenceTable.HOSPITAL_PORTAL);
					} else {
						intimationSource.setKey(ReferenceTable.CALL_CENTRE_SOURCE);
					}
					
					intimation.setInsured(insured);
					intimation.setIntimationMode(intimationModeValue);
					intimation.setIntimatedBy(intimationIntimatedBy);
					intimation.setIntimaterName(premiaIntimationTable.getGiIntimatorName());
					if(null != premiaIntimationTable.getGiIntimatorContactNo()) {
						String userNameForDB = SHAUtils.getUserNameForDB(premiaIntimationTable.getGiIntimatorContactNo());
						intimation.setCallerLandlineNumber(userNameForDB);
						intimation.setCallerMobileNumber(userNameForDB);
					}

					intimation.setPolicy(policy);
					intimation.setIntimationId(premiaIntimationTable.getGiIntimationNo());
					if(null != hospitals) {
						intimation.setHospital(hospitals.getKey());
					}
					
					/**
					 * new column added for policy year
					 */
					if(premiaIntimationTable.getGiPolicyYear() != null){
						intimation.setPolicyYear(premiaIntimationTable.getGiPolicyYear());
					}
					
					if(premiaIntimationTable.getGiAttenderMobileNumber() != null){
						//As per Satish and Saran instruction used substring for attender mobile
//						intimation.setAttendersMobileNumber(premiaIntimationTable.getGiAttenderMobileNumber());
						String attenderMobNo = SHAUtils.getUserNameForDB(premiaIntimationTable.getGiAttenderMobileNumber());
						intimation.setAttendersMobileNumber(attenderMobNo);
					}
					
					intimation.setHospitalType(hospitalType);
					intimation.setHospitalComments(premiaIntimationTable.getGiSHospComments());
					intimation.setCallerAddress(premiaIntimationTable.getCallerAddress());
					intimation.setAdmissionType(admissionType);
					intimation.setManagementType(managementType);
					intimation.setAdmissionReason(premiaIntimationTable.getGiReasonForAdmission());

					/**
					 * Commented on 25/10/2019 and the batch scheduled process comment as per Raja A. instruction
					 **/
					
					intimation.setAdmissionDate(premiaIntimationTable.getGiAdmitted());
					/*//Added as per Sathish Sir comment - 14-SEP-2019
					if(premiaIntimationTable.getGiAdmitted() != null && premiaIntimationTable.getGiAdmittedTime() != null){
						String dateTimeAdmitted = SHAUtils.formatDateForAdmissionDischarge(premiaIntimationTable.getGiAdmitted())+ " "+ premiaIntimationTable.getGiAdmittedTime();
						Date admissionDateTime = SHAUtils.combineDateTime(dateTimeAdmitted);
						intimation.setAdmissionDate(admissionDateTime);
					}else{
						intimation.setAdmissionDate(premiaIntimationTable.getGiAdmitted());
					}

					if(premiaIntimationTable.getGiDischargeDate() != null && premiaIntimationTable.getGiDischargeTime() != null){
						String dateTimeDischarged = SHAUtils.formatDateForAdmissionDischarge(premiaIntimationTable.getGiDischargeDate())+ " "+ premiaIntimationTable.getGiDischargeTime();
						Date dischargeDateTime = SHAUtils.combineDateTime(dateTimeDischarged);
						intimation.setDateOfDischarge(dischargeDateTime);
					}else{
						intimation.setDateOfDischarge(premiaIntimationTable.getGiDischargeDate());
					}*/
					
					intimation.setRoomCategory(roomCategory);
					intimation.setInsuredPatientName(premiaIntimationTable.getGiPatientNameYn());
					intimation.setInpatientNumber(premiaIntimationTable.getGiInpatientNo());
					intimation.setCpuCode(cpuCode);
					intimation.setIntimationSource(intimationSource);
					intimation.setCreatedBy(premiaIntimationTable.getGiCreatedBy());
					intimation.setCreatedDate(premiaIntimationTable.getGiCreatedOn());
					intimation.setClaimType(claimType);
					intimation.setStatus(getStatus(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY));
					intimation.setStage(getStage(ReferenceTable.INTIMATION_STAGE_KEY));
					if(premiaIntimationTable.getGiEmail() !=null && !premiaIntimationTable.getGiEmail().isEmpty())
					{
					intimation.setPortalEmailId(premiaIntimationTable.getGiEmail());
					//As per Saran/Satish Sir comments added below line - Customer APP portal
					intimation.setCallerEmail(premiaIntimationTable.getGiEmail());
					}
					if(cpuCode != null){
						intimation.setOriginalCpuCode(cpuCode.getCpuCode());
					}
					
			
					/**
					 * Added for PA.
					 * 
					 * **/
					if(SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
							intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
					else 
						intimation.setIncidenceFlag(SHAConstants.DEATH_FLAG);
					
					//intimation.setIncidenceFlag(premiaIntimationTable.getGiAccidentDeathFlag());
					intimation.setProcessClaimType(premiaIntimationTable.getGiPACategory());
					if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiPACategory()))
					{
						intimation.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
					}
					intimation.setHospitalReqFlag(premiaIntimationTable.getGiHospitalRequiredFlag());
					if(null != policy.getLobId())
					{
						MastersValue masLob = getMaster(policy.getLobId());
						intimation.setLobId(masLob);
					}			
					
					if(null != intimation.getPolicy().getProduct().getKey() && 
							(ReferenceTable.getCombinationOfHealthAndPA().containsKey(intimation.getPolicy().getProduct().getKey()))){

						if(premiaIntimationTable.getGiAccidentDeathFlag() != null){
							MastersValue masLob = getMaster(ReferenceTable.PA_LOB_KEY);
							intimation.setLobId(masLob);
						}
					}

					if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())){
						intimation.setPaParentName(premiaIntimationTable.getGiPAParentName());
						if(premiaIntimationTable.getGiPAParentAge() != null){
							intimation.setPaParentAge(Double.valueOf(premiaIntimationTable.getGiPAParentAge()));
						}
						if(premiaIntimationTable.getGiPAParentDOB() != null){
//							intimation.setPaParentDOB(new Date(premiaIntimationTable.getGiPAParentDOB()));
							Date parentDOB = SHAUtils.formatTimeFromString(premiaIntimationTable.getGiPAParentDOB());
							intimation.setPaParentDOB(parentDOB);
						}
						intimation.setPaPatientName(premiaIntimationTable.getGiPAPatientName());
						intimation.setPaCategory(premiaIntimationTable.getGiPACategory());
						
						//CR2018085
						if(premiaIntimationTable.getGiPAPatientDOB() != null){
							Date patientDOB = SHAUtils.formatTimeFromString(premiaIntimationTable.getGiPAPatientDOB());
							intimation.setPaPatientDOB(patientDOB);
						}
						if(premiaIntimationTable.getGiPAPatientAge() != null){
							intimation.setPaPatientAge(Integer.valueOf(premiaIntimationTable.getGiPAPatientAge()));
						}
					}
					
					if(intimation.getInsured() == null || intimation.getHospital() == null){
						log.info(" &&&&&&&&&GLX INTIMATION IS NOT INSERTED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! >"+ premiaIntimationTable.getGiIntimationNo() +"<------------");
						utx.rollback();
						return null;
					}
					
					// PA added fix ends up here.
					if(null != intimation.getHospital()){
						entityManager.persist(intimation);
						entityManager.flush();
						log.info(" &&&&&&&&&GLX INTIMATION INSERTED SUCESSFULLY------>"+ intimation.getIntimationId() +"<------------");
					}
					
					
					
					if(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()) && intimation.getPolicy().getGpaPolicyType() != null 
							&& intimation.getPolicy().getGpaPolicyType().equalsIgnoreCase("Un Named")){
						DBCalculationService dbCalculationService = new DBCalculationService();
						dbCalculationService.invokeProcedureForUnnamedPolicy(intimation.getIntimationId());
					}
					utx.commit();
					
					return intimation;
				
				} catch(Exception e) {
					utx.rollback();
				}
				return null;
			}
			
			private void setGalaxyClaimTypeAndHospitalType(GalaxyIntimationTable premiaIntimationTable,MastersValue hospitalType ,MastersValue claimType,Policy policy ,TmpCPUCode cpuCode,String lobType,Boolean isHospTypeEmpty,Boolean isHospTopUpIntmAvail)
			{
				if(SHAConstants.PA_LOB_TYPE.equals(lobType))
				{
					if(isHospTypeEmpty)
					{
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
					else{
						if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn()) && 
								SHAConstants.YES_FLAG.equalsIgnoreCase(premiaIntimationTable.getGiAccidentDeathFlag()))
						{
							//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
							claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
						}
						else {
							//hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
							claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
							if(policy.getHomeOfficeCode() != null) {
								OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
								if(branchOffice != null){
									String officeCpuCode = branchOffice.getCpuCode();
									if(officeCpuCode != null) {
										cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
									}
								}
							}
						}
					}
				}
				else
				{
					if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
					}
					else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(premiaIntimationTable.getGiHospitalTypeYn())) {
						hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						if(policy.getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									cpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					} 
					if(isHospTopUpIntmAvail) {
						claimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					}
				}
			}
			
			public List<GalaxyIntimationTable> processGalaxyIntimationDataForNonNetwork(String batchSize) {
				try {
					log.info("********* BATCH SIZE FOR GALAXY PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
					List<GalaxyIntimationTable> premiaIntimationList = fetchRecFromGalaxyIntimationTableForNonNetwork(batchSize);
					return premiaIntimationList;
				} catch(Exception e) {
					log.error("********* EXCEPTION OCCURED DURING FETCH FROM GALAXY TABLE **********************" + e.getMessage());
					return null;
				}
			}
			
			private List<GalaxyIntimationTable> fetchRecFromGalaxyIntimationTableForNonNetwork(String batchSize) {
				Query query = entityManager.createNamedQuery("GalaxyIntimationTable.findByNonNetwork");
				query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
				/**
				 * Will remove once the final integration of batch is over.
				 * */
			//	query = query.setParameter("claimType", "P");

				if(batchSize != null) {
					query.setMaxResults(SHAUtils.getIntegerFromString("20"));
				}
				List<GalaxyIntimationTable> premiaIntimationTableList = query.getResultList();
				log.info("********* COUNT FOR GALAXY PULL FOR NON NETWORK******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
				return premiaIntimationTableList;
			}
			
			public List<GalaxyIntimationTable> processGalaxyIntimationDataForGmc(String batchSize) {
				try {
					log.info("********* BATCH SIZE FOR GMC GALAXY PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
					List<GalaxyIntimationTable> premiaIntimationList = fetchRecFromGalaxyIntimationTableForGMC(batchSize);
					return premiaIntimationList;
				} catch(Exception e) {
					log.error("********* EXCEPTION OCCURED DURING FETCH FROM GALAXY TABLE **********************" + e.getMessage());
					return null;
				}
			}
			
			private List<GalaxyIntimationTable> fetchRecFromGalaxyIntimationTableForGMC(String batchSize) {
				Query query = entityManager.createNamedQuery("GalaxyIntimationTable.findAllGmc");
				query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);

				if(batchSize != null) {
					query.setMaxResults(SHAUtils.getIntegerFromString("20"));
				}
				List<GalaxyIntimationTable> premiaIntimationTableList = query.getResultList();
				log.info("********* COUNT FOR GALAXY PULL FOR GMC NETWORK ******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
				return premiaIntimationTableList;
			}
			
			public List<GalaxyIntimationTable> processGalaxyIntimationData(String batchSize) {
				try {
					log.info("********* BATCH SIZE FOR GALAXY PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
					List<GalaxyIntimationTable> premiaIntimationList = fetchRecFromGalaxyIntimationTable(batchSize);
					return premiaIntimationList;
				} catch(Exception e) {
					log.error("********* EXCEPTION OCCURED DURING FETCH FROM GALAXY TABLE **********************" + e.getMessage());
					return null;
				}
			}
			
			private List<GalaxyIntimationTable> fetchRecFromGalaxyIntimationTable(String batchSize) {
				Query query = entityManager.createNamedQuery("GalaxyIntimationTable.findAll");
				query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
				/**
				 * Will remove once the final integration of batch is over.
				 * */
			//	query = query.setParameter("claimType", "P");

				if(batchSize != null) {
					query.setMaxResults(SHAUtils.getIntegerFromString("20"));
				}
				List<GalaxyIntimationTable> premiaIntimationTableList = query.getResultList();
				log.info("********* COUNT FOR GALAXY PULL FOR NON GMC NETWORK ******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
				return premiaIntimationTableList;
			}
	
	 public Product getCoronaGrpForProduct(Long productId)
		{
		//	Product a_mastersValue = new MastersValue();
			//if (a_key != null) {
		 Product product = null;
		 try{
			 Query query = entityManager
					 .createNamedQuery("Product.findByKey");
			 query = query.setParameter("key", productId);
			 List<Product> productValList = query.getResultList();
			 if(null != productValList && !productValList.isEmpty())
			 {
				 product = productValList.get(0);
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return product;	
		}
	 
	 public List<OPPremiaIntimationTable> processOPPremiaIntimationDataForCashless(String batchSize) {
			try {
				log.info("********* BATCH SIZE FOR OP PREMIA PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
				List<OPPremiaIntimationTable> oppremiaIntimationList = fetchRecFromOPPremiaIntimationTableForCashless(batchSize);
				return oppremiaIntimationList;
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING FETCH FROM OP CASHLESS PREMIA TABLE **********************" + e.getMessage());
				return null;
			}
		}
	 
	 private List<OPPremiaIntimationTable> fetchRecFromOPPremiaIntimationTableForCashless(String batchSize) {
			Query query = entityManager.createNamedQuery("OPPremiaIntimationTable.findByCashlessClaimTypeKey");
			query = query.setParameter("claimTypeKey",ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			if(batchSize != null) {
				query.setMaxResults(SHAUtils.getIntegerFromString("20"));
			}
			List<OPPremiaIntimationTable> oppremiaIntimationTableList = query.getResultList();
			log.info("********* COUNT FOR PREMIA PULL FOR OP CASHLESS INTIMATIONS ******------> " + (oppremiaIntimationTableList != null ? String.valueOf(oppremiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
			return oppremiaIntimationTableList;
		}
	 
	 public Boolean processSavedOPIntimationRevisedForCashless(OPIntimation intimationObj, OPPremiaIntimationTable oppremiaIntimation) throws IllegalStateException, SecurityException, SystemException {
			try {
				utx.begin();
				OPIntimation intimation = getOPIntimationByKey(intimationObj.getKey());
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				log.info("**************WHILE PROCESSING SAVED OP CASHLESS INTIMATION ***********-----> "
						+ intimation != null ? intimation.getIntimationId()
						: (intimation != null ? intimation.getIntimationId()
								+ "--->THIS OP CASHLESS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
								: "NO OP CASHLESS INTIMATIONS"));
				log.info("**************WHILE PROCESSING SAVED OP CASHLESS INTIMATION ***********-----> " + intimation != null ? intimation.getIntimationId() : (intimation != null ? intimation.getIntimationId() + "--->THIS OP INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US" : "NO OP INTIMATIONS") );

				IntimationRule intimationRule = new IntimationRule();

				if (null != intimation.getAdmissionDate()) {
					String date = String.valueOf(intimation.getAdmissionDate());
					String date1 = date.replaceAll("-", "/");
				}

				OPClaim claimObject = null;
				
				Policy policy = intimation.getPolicy();
				
				log.info("&&&&&&&&&&&&&THIS OP CASHLESS INTIMATION IS GOING FOR AUTO REGISTRATION (NO ISSUES BE HAPPY) -------->"
							+ intimation.getIntimationId());
				claimObject = doAutoRegistrationForOPForCashless(intimation,oppremiaIntimation);
				

				utx.commit();
				OPClaim opClaim = getOPClaimByKey(claimObject.getKey());
				DBCalculationService dbCalculationService = new DBCalculationService();
				Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCallForOPCashless(opClaim);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
//				dbCalculationService.initiateTaskProcedure(parameter);
				dbCalculationService.revisedInitiateTaskProcedureForOP(parameter);
				dbService.invokeAccumulatorForOP(policy.getPolicyNumber(), intimation.getInsured().getHealthCardNumber(), "0");
				updateOPPremiaIntimationTable(oppremiaIntimation,SHAConstants.SUCCESS_FLAG, SHAConstants.YES_FLAG);
				return true;
			}
			catch(Exception e) {
				e.printStackTrace();
				utx.rollback();
//				updatePremiaIntimationTable(premiaIntimation, SHAConstants.PREMIA_INTIMATION_STG_FAILURE_STATUS);
				log.error("***************************ERROR OCCURED WHILE OP CASHLESS CLAIM GENERATION **************************" + (intimationObj != null ? intimationObj.getIntimationId() : "NO INTIMATION NUMBER"));
				return false;
			}
					
		}
			
			public OPClaim doAutoRegistrationForOPForCashless(OPIntimation objIntimation,OPPremiaIntimationTable oppremiaIntimation) {

				String strClaimTypeRequest = "";
				if(null != objIntimation.getClaimType())
				{
					strClaimTypeRequest = objIntimation.getClaimType().getValue();
				}
				OPClaim objClaim = populateOPClaimObjectForCashless(objIntimation,oppremiaIntimation, strClaimTypeRequest);
//				OPHealthCheckup objOPHealthClaim = populateOPHealthCheckUp(objClaim,objIntimation,oppremiaIntimation, strClaimTypeRequest);
				    log.info("before save claim --->" + objIntimation.getIntimationId());
				try {
					entityManager.persist(objClaim);
					entityManager.flush();
//					entityManager.refresh(objClaim);
					
//					entityManager.persist(objOPHealthClaim);
//					entityManager.flush();
//					entityManager.refresh(objOPHealthClaim);
				} catch (Exception e) {

					e.printStackTrace();
					log.error("******************Not Pulled from Premia***************** -----> " + objIntimation.getIntimationId());
				}

				log.info("after save claim --->" + objIntimation.getIntimationId() + "Claim No --- >" + objClaim.getClaimId());
				
				/*objIntimation = getOPIntimationByKey(objIntimation.getKey());
				entityManager.merge(objIntimation);
				entityManager.flush();*/
				return objClaim;
			}
			
			public OPClaim populateOPClaimObjectForCashless(OPIntimation objIntimation,OPPremiaIntimationTable oppremiaIntimation,String strClaimTypeRequest) {

				OPClaim claimObj = new OPClaim();
				Status objStatus = new Status();
				Stage objStage = new Stage();
				MastersValue masValue = new MastersValue();
				MastersValue masForClaim = new MastersValue();
				//objStatus.setKey(11l);
				objStatus.setKey(ReferenceTable.OP_VERIFICATION_INITIATED_STATUS);
				objStage.setKey(55l);
				claimObj.setIntimation(objIntimation);
				claimObj.setStatus(objStatus);
				claimObj.setStage(objStage);
				claimObj.setOfficeCode(objIntimation.getOfficeCode());
				claimObj.setCreatedBy(objIntimation.getCreatedBy());
				claimObj.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				claimObj.setAccidentFlag(objIntimation.getAccidentFlag());
				claimObj.setEmergencyFlag(objIntimation.getEmergencyFlag());
				claimObj.setDoctorNo(Long.valueOf(oppremiaIntimation.getIntimatorContactNumber()));
				claimObj.setDocumentReceivedDate(oppremiaIntimation.getBillReceivedDate());
				if(objIntimation.getRemarksForEmergencyAccident() != null){
					claimObj.setRemarksForEmergencyAccident(objIntimation.getRemarksForEmergencyAccident());
				}
				if(objIntimation.getAdmissionDate() != null){
					claimObj.setDataOfAdmission(objIntimation.getAdmissionDate());
				}
				if(objIntimation.getLobId() != null && objIntimation.getLobId().getKey() != null){
					claimObj.setLobId(objIntimation.getLobId().getKey());
				}
				if(objIntimation.getModeOfReceiptId() != null){
					claimObj.setModeOfReceiptId(objIntimation.getModeOfReceiptId().getKey());
				}
				if(objIntimation.getTreatmentTypeId() != null){
					claimObj.setTreatmentTypeId(objIntimation.getTreatmentTypeId().getKey());
				}
				
				if(objIntimation.getCpuCode() != null){
					claimObj.setOriginalCpuCode(objIntimation.getCpuCode().getCpuCode());
				}
				if(objIntimation.getAggregatorCode() != null){
					claimObj.setAggregatorCode(objIntimation.getAggregatorCode());
				}
				if(objIntimation.getServiceType() != null){
					claimObj.setServiceType(objIntimation.getServiceType());
				}
				if(objIntimation.getClaimTypeKey() != null){
					claimObj.setClaimTypeKey(objIntimation.getClaimTypeKey());
				} else {
					claimObj.setClaimTypeKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				}
				MastersValue value = new MastersValue();
				value.setKey(ReferenceTable.OUT_PATIENT);
				value.setValue("Out Patient");
				claimObj.setClaimType(value);

				Policy policyByKey = getPolicyByKey(objIntimation.getPolicy().getKey());
				
				MASClaimAdvancedProvision claimAdvProvision = getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));

//				Double amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
//						 , objIntimation.getCpuCode() != null ? objIntimation.getCpuCode().getProvisionAmount() : 0d,objIntimation.getKey());
				
				Double amt = 0d;
				/*amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
						 , claimAdvProvision != null ? claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d : 0d,objIntimation.getKey(),claimObj);*/
				
				claimObj.setClaimedAmount(oppremiaIntimation.getAmountClaimed());
//				claimObj.setProvisionAmount(oppremiaIntimation.getAmountClaimed());
				Long claimKey= 0l;
				Map<String, Integer> amountList = dbService.getOPAvailableAmount(objIntimation.getInsured().getKey(),claimKey, claimObj.getClaimType().getKey() != null ? claimObj.getClaimType().getKey() : 0l,
						claimObj.getOpcoverSection() != null ? claimObj.getOpcoverSection() : "0");
				Integer opAvailableAmount = 0;
				Integer perClaimLimit = 0;
				Integer perPolicyLimit = 0;
				
				if(amountList != null && !amountList.isEmpty()){
					opAvailableAmount = amountList.get(SHAConstants.CURRENT_BALANCE_SI);
					perClaimLimit = amountList.get(SHAConstants.OP_CLAIM_LIMIT);
					perPolicyLimit = amountList.get(SHAConstants.OP_POLICY_LIMIT);
				}
				Double min = 0d;
				Double minclaimLimit = 0d;
				Double minSi = 0d;
				Double amountClaimed = 0d;
				if(oppremiaIntimation.getAmountClaimed() != null){
					amountClaimed = oppremiaIntimation.getAmountClaimed();
				}
				if(perClaimLimit != null &&  perClaimLimit > 0){
					minclaimLimit = Math.min(Double.valueOf(perClaimLimit.toString()), opAvailableAmount);
					min = Math.min(oppremiaIntimation.getAmountClaimed(),minclaimLimit);
				} else if(perPolicyLimit != null && perPolicyLimit > 0){
					minclaimLimit = Math.min(Double.valueOf(perPolicyLimit.toString()), opAvailableAmount);
					min = Math.min(amountClaimed,minclaimLimit);
				} else {
					min= Math.min(amountClaimed, opAvailableAmount);
				}
				claimObj.setProvisionAmount(min);
				claimObj.setCurrentProvisionAmount(min);

				claimObj.setRegistrationRemarks("Auto Registered");
				claimObj.setCurrentProvisionAmount(amt);
				
				return claimObj;
			}
			
			public OPClaim getOPClaimByKey(Long key) {
				Query query = entityManager.createNamedQuery("OPClaim.findByClaimKey");
				query.setParameter("claimKey", key);
				List<OPClaim> claim = (List<OPClaim>)query.getResultList();
				if(claim != null && ! claim.isEmpty()){
					return claim.get(0);
				}
				else{
					return null;
				}
			}
	 public GmcCoorporateBufferLimit getcorpBufferLimit(Long policyKey,Double sumInsuredFrom, Double sumInsuredTo) {
			Query query = entityManager
					.createNamedQuery("GmcCoorporateBufferLimit.findBasedOnSIFromTo");
			query = query.setParameter("policyKey", policyKey);
			query = query.setParameter("sumInsuredFrom", sumInsuredFrom);
			query = query.setParameter("sumInsuredTo", sumInsuredTo);
//			query = query.setParameter("roomType", roomType);
			List<GmcCoorporateBufferLimit> ailmentLimit = query.getResultList();
			if(ailmentLimit != null && ! ailmentLimit.isEmpty()){
				return ailmentLimit.get(0);
			}
			return null;
		}
	 
	 public List<PremiaIntimationTable> processPremiaIntimationForNonNetworkError(String batchSize) {
			try {
				log.info("********* BATCH SIZE FOR PREMIA PULL ERROR BATCH******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
				List<PremiaIntimationTable> premiaIntimationList = fetchRecFromPremiaIntimationForNonNetworkError(batchSize);
				return premiaIntimationList;
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE ERROR BATCH**********************" + e.getMessage());
				return null;
			}
		}
	 
	 private List<PremiaIntimationTable> fetchRecFromPremiaIntimationForNonNetworkError(String batchSize) {
			Query query = entityManager.createNamedQuery("PremiaIntimationTable.findByNonNetworkErr");
			/**
			 * Will remove once the final integration of batch is over.
			 * */
		//	query = query.setParameter("claimType", "P");

			if(batchSize != null) {
				query.setMaxResults(SHAUtils.getIntegerFromString("20"));
			}
			List<PremiaIntimationTable> premiaIntimationTableList = query.getResultList();
			log.info("********* COUNT FOR PREMIA PULL FOR NON NETWORK ERROR BATCH******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
			return premiaIntimationTableList;
		}
	 
	 public Boolean isHospCashTopUpAvail(String strIntimationNo) {
			Query query = entityManager.createNamedQuery("HospCashIntimation.findByTopUpIntimationNumber");
			query = query.setParameter("tpIntimationNumber",strIntimationNo);
			List<HospCashIntimation>	resultList = query.getResultList();
			if (null != resultList && !resultList.isEmpty()){
				return true;
			}
			return false;
		}
	 
	 public List<GalaxyIntimationTable> processGlxIntimationForNonNetworkError(String batchSize) {
			try {
				log.info("********* BATCH SIZE FOR GLX PULL ERROR BATCH******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
				List<GalaxyIntimationTable> premiaIntimationList = fetchRecFromGlxIntimationForNonNetworkError(batchSize);
				return premiaIntimationList;
			} catch(Exception e) {
				log.error("********* EXCEPTION OCCURED DURING FETCH FROM GLX TABLE ERROR BATCH**********************" + e.getMessage());
				return null;
			}
		}
	 
	 private List<GalaxyIntimationTable> fetchRecFromGlxIntimationForNonNetworkError(String batchSize) {
			Query query = entityManager.createNamedQuery("GalaxyIntimationTable.findByNonNetworkErr");
			/**
			 * Will remove once the final integration of batch is over.
			 * */
		//	query = query.setParameter("claimType", "P");

			if(batchSize != null) {
				query.setMaxResults(SHAUtils.getIntegerFromString("20"));
			}
			List<GalaxyIntimationTable> premiaIntimationTableList = query.getResultList();
			log.info("********* COUNT FOR GLX PULL FOR NON NETWORK ERROR BATCH******------> " + (premiaIntimationTableList != null ? String.valueOf(premiaIntimationTableList.size()) : "NO RECORDS TO PULL"));
			return premiaIntimationTableList;
		}
}