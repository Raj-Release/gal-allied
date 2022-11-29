package com.shaic.claim.registration.convertclaimcashlesspage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import oracle.sql.DATE;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.clearcashless.ClearCashlessService;
import com.shaic.claim.intimation.rule.IntimationRule;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.starfax.simulation.PremiaPullService;

@ViewInterface(ConvertClaimCashlessPageView.class)
public class ConvertClaimCashlessPagePresenter extends AbstractMVPPresenter<ConvertClaimCashlessPageView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7640561472049224002L;

	
	public static final String CONVERSION_CASHLESS="Convert to Cashless";
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	@EJB
	private  ClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClearCashlessService clearCashlessService; 
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PremiaPullService premiaPullService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreauthService preauthService;
	//RM-STUB
	/*@EJB
	private StarFaxSimulatorService starfaxSimulateService;*/
	
	

	public void setUpconversion(@Observes @CDIEvent(CONVERSION_CASHLESS) final ParameterDTO parameters)	{
		
		ConvertClaimDTO bean=(ConvertClaimDTO)parameters.getPrimaryParameter();
		
		SearchConverClaimCashlessTableDTO searchFormDto=(SearchConverClaimCashlessTableDTO) parameters.getSecondaryParameter(0,SearchConverClaimCashlessTableDTO.class);
				
		Boolean result=claimService.saveCashlessConversion(bean, "Conversion",searchFormDto);
		
		
		
		Intimation intimationByNo = intimationService.getIntimationByNo(searchFormDto.getIntimationNumber());
		
		if (intimationByNo != null){
			Claim claimforIntimation = intimationService.getClaimforIntimation(intimationByNo.getKey());
			List<Preauth> preauthByIntimationKey = preauthService.getPreauthByIntimationKey(intimationByNo.getKey());
			
			if (preauthByIntimationKey != null && preauthByIntimationKey.isEmpty()){
				processSavedIntimationTmp(intimationByNo,claimforIntimation);
			}
			
			if (preauthByIntimationKey != null){
				Preauth latestPreauthByIntimationKey = preauthService.getLatestPreauthByIntimationKey(intimationByNo.getKey());
				if(latestPreauthByIntimationKey != null){
				
					if (latestPreauthByIntimationKey.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || 
							latestPreauthByIntimationKey.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS)) {
						
						if(getDBTaskForPreauth(latestPreauthByIntimationKey.getClaim().getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE)){
							submitDBprocedure(latestPreauthByIntimationKey.getClaim(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE, SHAConstants.CONVERSION_REIMBURSEMENT_END);
						}
				
						if(latestPreauthByIntimationKey.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)){
		
							submitDBProcedureForQueryReply(latestPreauthByIntimationKey.getClaim(), latestPreauthByIntimationKey, true);
						}else{
							submitDBProcedureForQueryReply(latestPreauthByIntimationKey.getClaim(), latestPreauthByIntimationKey, false);
						}
						
//						invokeBPMProcess(latestPreauthByIntimationKey.getClaim(),"QUERYREPLYQ",latestPreauthByIntimationKey);
					}
				}
			}
			
			if(claimforIntimation != null && claimforIntimation.getLobId() != null && claimforIntimation.getLobId().equals(ReferenceTable.PA_LOB_KEY)){
				dbCalculationService.stopReminderProcessProcedure(intimationByNo.getIntimationId(), SHAConstants.OTHERS);
			}
		}
		
		
		if(result){
			view.result();
		}
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	/*public void invokeBPMProcess(Claim claimObject,String strType,Preauth preauth) {
		
		if (null != claimObject)	{
			Intimation intimation = claimObject.getIntimation();
			IntimationRule intimationRule = new IntimationRule();
			//IntimationMessage intimationMessage = new IntimationMessage();
			IntimationType intimationType = new IntimationType();
			PolicyType policyType = new PolicyType();
			PaymentInfoType paymentInfoType = new PaymentInfoType();
			HospitalInfoType hospitalInfoType = new HospitalInfoType();
			PreAuthReqType preauthReqType = new PreAuthReqType();
			ClaimType claimType = new ClaimType();
			ClaimRequestType claimReqType = new ClaimRequestType();
			
			CustomerType customer = new CustomerType();
			
//			Output output = new Output();
//			PreAuthReq preAuthReq = new PreAuthReq();
//			PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
			
			intimationType.setIntimationNumber(intimation.getIntimationId());
			paymentInfoType.setClaimedAmount(claimObject.getClaimedAmount());
			//hospitalInfoType.setHospitalType(objHosp.getHospitalTypeName().toUpperCase());
			hospitalInfoType.setHospitalType(intimation.getHospitalType().getValue());
			intimationType.setIntimationSource(intimation.getIntimationSource() != null ? intimation.getIntimationSource().getValue() : "");
			if(intimation.getHospital() != null){
				Hospitals hospitalById = starfaxSimulateService.getHospitalById(intimation.getHospital());
				if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
				}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
				}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
				}else{
					hospitalInfoType.setNetworkHospitalType("");
				}
//				hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType() != null ? hospitalById.getNetworkHospitalType() : "");
			}
			
			claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
			
			if(("PREAUTHRECEIVED").equals(strType)){
				
				claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
				
			}
			
//			hospitalInfoType.setNetworkHospitalType(intimation.get);
			claimType.setClaimType(claimObject.getClaimType().getValue().toUpperCase());
			claimType.setKey(claimObject.getKey());
			if(null != intimation.getCpuCode())
			{
				if(null != intimation.getCpuCode().getCpuCode())
				{
					claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
				}
			}
			//intimationMessage.setIntimationNumber(objIntimation.getIntimationId());
			//intimationMessage.setAmountClaimed(claimObject.getClaimedAmount());
			//intimationMessage.setHospitalType(objHosp.getHospitalTypeName().toUpperCase());
			//intimationMessage.setClaimType(claimObject.getClaimType().getValue().toUpperCase());
			//intimationMessage.setCpuCode(""+intimation.getCpuCode().getCpuCode());
			//intimationMessage.setHospitalType(intimation.getHospitalType().getValue());

			Boolean isBalSIAvailable = starfaxSimulateService.isBalanceSIAvailable(claimObject.getIntimation().getInsured().getInsuredId(),claimObject.getIntimation().getPolicy().getKey());
			intimationType.setIsBalanceSIAvailable(isBalSIAvailable);
			//intimationMessage.setIsBalanceSIAvailable(isBalSIAvailable);
			intimationType.setIsPolicyValid(intimationRule.isPolicyValid(intimation));
			intimationType.setKey(intimation.getKey());
			paymentInfoType.setProvisionAmount(claimObject.getProvisionAmount());
			intimationType.setStatus(intimation.getStatus().getProcessValue());
		//	intimationMessage.setIsPolicyValid(intimationRule.isPolicyValid(intimation));
			//intimationMessage.setKey(intimation.getKey());
		//	intimationMessage.setProvisionAmount(claimObject.getProvisionAmount());
			//intimationMessage.setStatus(intimation.getStatus().getProcessValue());
			preauthReqType.setIsFVRReceived(false);
			preauthReqType.setOutcome(strType);
			preauthReqType.setResult(strType);
			preauthReqType.setKey(preauth.getKey());
			preauthReqType.setPreAuthAmt(0d);
			
			
			if(preauth.getTreatmentType() != null){
				customer.setTreatmentType(preauth.getTreatmentType().getValue());

			}else {
				customer.setSpeciality("");
				customer.setTreatmentType("");
			}
			
			com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax simulateStarfax = BPMClientContext.getSimulateStarfax(BPMClientContext.BPMN_TASK_USER, "Star@123");
			
			//output.setResult(strType);
			//output.setResult("PREAUTHNOTRECEIVED");
			//preAuthReq.setIsFVRReceived(false);
			if(("QUERYREPLY").equals(strType) || ("QUERYREPLYQ").equals(strType)) {
			
				
//				preauthReqType.setKey(objPreAuth.getKey());
//				preAuthReq.setKey(objPreAuth.getKey());
			}
			else
			{
				
				preauthReqType.setKey(claimObject.getKey());
				//claimType.setKey(claimObject.getKey());
				//preAuthReq.setKey(claimObject.getKey());
			}
			policyType.setPolicyId(intimation.getPolicy().getPolicyNumber());
			
            ClassificationType classificationType = new ClassificationType();
            
            Insured insured = intimation.getInsured();
 
 			if(claimObject != null && claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().equals(1l)){
 				
 				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
 			}
 			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
 				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
 			}else{
 				classificationType.setPriority(SHAConstants.NORMAL);
 			}
 		
 			classificationType.setType(SHAConstants.TYPE_FRESH);
 			classificationType.setSource(SHAConstants.NORMAL);
 			
 			
 			
 			QueryType queryType = new QueryType();
 			queryType.setStatus("No Query");
			
			
			PayloadBOType payload = new PayloadBOType();
			payload.setIntimation(intimationType);
			payload.setPolicy(policyType);
			payload.setPreAuthReq(preauthReqType);
			payload.setQuery(queryType);
			payload.setClaim(claimType);
			payload.setClaimRequest(claimReqType);
			payload.setPaymentInfo(paymentInfoType);
			payload.setHospitalInfo(hospitalInfoType);
			payload.setClassification(classificationType);
			payload.setCustomer(customer);
			//payload.set
			//preAuthReqDetails.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
			//preAuthReq.setPreAuthReqDetails(preAuthReqDetails);
			try{
				System.out.println("---before intiating bpm process");
			simulateStarfax.initiate(BPMClientContext.BPMN_TASK_USER, payload);
			System.out.println("---after intiating bpm process");
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//simulateStarfax.initiate("weblogic", "Star@123", intimationMessage, preAuthReq,output);	
		}
		
	}*/
	
	public Boolean processSavedIntimationTmp(Intimation intimationObj,Claim claimByKey)
	{
		try
		{
			Intimation intimation = policyService.getIntimationByKey(intimationObj.getKey());
			
			IntimationRule intimationRule = new IntimationRule();
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		/*	IntimationType a_message = new IntimationType();
			ClaimRequestType claimReqType = new ClaimRequestType();
			ClaimType claimType = new ClaimType();*/
			String strClaimType = "";
			if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL))
			{
				strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
			}
			else if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL))
			{
				strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
			}
			//claimType.setClaimType(strClaimType);
			if(null != intimation.getCpuCode() && null != intimation.getCpuCode().getCpuCode())
				//claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
			//a_message.setKey(intimation.getKey());
			if(null != intimation.getCreatedDate())
			{
//				String date = String.valueOf(intimation.getAdmissionDate());
				String date = String.valueOf(intimation.getCreatedDate());
				String date1 = date.replaceAll("-", "/");
				//a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
			}
			//a_message.setIntimationNumber(intimation.getIntimationId());
			
			//a_message.setIntimationSource(intimation.getIntimationSource() != null ? (intimation.getIntimationSource().getValue() != null ? intimation.getIntimationSource().getValue() : "" ) : "" );
			
			/*a_message.setIsClaimPending(!intimationRule
					.isClaimExist(intimation));
			
			a_message.setIsPolicyValid(intimationRule.isPolicyValid(intimation));				*/
			
			Double balsceSI = dbCalculationService.getBalanceSI(
					intimation.getPolicy().getKey(),intimation.getInsured().getKey(), 0l, intimation.getInsured().getInsuredSumInsured(),intimation.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType))
			{
				if(null != balsceSI && balsceSI > 0 && null != intimation.getPolicy() && (SHAConstants.NEW_POLICY).equalsIgnoreCase(intimation.getPolicy().getPolicyStatus())
						&& null != strPremiaFlag && ("true").equalsIgnoreCase(strPremiaFlag))
				{
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
					String get64vbStatus = PremiaService.getInstance().get64VBStatus(intimation.getPolicy().getPolicyNumber(), intimation.getIntimationId());
					if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
					}
				}
				else
				{
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
					if( null != intimation.getPolicy() && !(SHAConstants.NEW_POLICY).equalsIgnoreCase(intimation.getPolicy().getPolicyStatus())) {
						Boolean endorsedPolicyStatus = PremiaService.getInstance().getEndorsedPolicyStatus(intimation.getPolicy().getPolicyNumber());
						if(endorsedPolicyStatus) {
							mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
							String get64vbStatus = PremiaService.getInstance().get64VBStatus(intimation.getPolicy().getPolicyNumber(), intimation.getIntimationId());
							if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
								mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
							}
						}
					}
					
				}
			}
			else
			{
				if(null != intimation.getPolicy() && !(SHAConstants.NEW_POLICY).equalsIgnoreCase(intimation.getPolicy().getPolicyStatus())) 
				{
					Boolean endorsedPolicyStatus = PremiaService.getInstance().getEndorsedPolicyStatus(intimation.getPolicy().getPolicyNumber());
					
					if(endorsedPolicyStatus) 
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
					else
						mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "N");
				}  else {
					mapValues.put(SHAConstants.TOTAL_BALANCE_SI, "Y");
				}
			}
			
			List<Claim> claimList = new ArrayList<Claim>();
			claimList = claimService.getClaimByIntimation(intimation.getKey());
			Claim claimObject = claimList.get(0);
			/*PaymentInfoType paymentInfoType = new PaymentInfoType();
			Claim claimObject = null;
			
			if(null != a_message.isIsBalanceSIAvailable() && a_message.isIsBalanceSIAvailable())
			{
				List<Claim> claimList = new ArrayList<Claim>();
				claimList = claimService.getClaimByIntimation(intimation.getKey());
				claimObject = claimList.get(0);
				claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
				paymentInfoType.setClaimedAmount(claimObject.getClaimedAmount());
				claimType.setClaimId(claimObject.getClaimId());
				claimType.setKey(claimObject.getKey());
			} else {
				
			}
			
			
			ClassificationType classificationType = new ClassificationType();
			
			Insured insured = intimation.getInsured();
			
			if(null != claimObject && null != claimObject.getIsVipCustomer() && claimObject.getIsVipCustomer().equals(1l)){
				
				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
			}
			else if(null != insured && null != insured.getInsuredAge() && insured.getInsuredAge()>60){
				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			}else{
				classificationType.setPriority(SHAConstants.NORMAL);
			}
		
			classificationType.setType(SHAConstants.TYPE_FRESH);
			classificationType.setSource(SHAConstants.NORMAL);
			
			
			PreAuthReqType preauthReqType = new PreAuthReqType();
			preauthReqType.setOutcome("PREAUTHNOTRECEIVED");
			preauthReqType.setResult("PREAUTHNOTRECEIVED");
			preauthReqType.setPreAuthAmt(0d);
			
			//setting senior citizen hardcoded as of now. Later this needs to be changed.
	
			if( null != intimation.getPolicy() && null != intimation.getPolicy().getProduct() 
					&& null != intimation.getPolicy().getProduct().getKey() && intimation.getPolicy().getProduct().getKey().equals(30l))
				preauthReqType.setIsSrCitizen(true);
			else
				preauthReqType.setIsSrCitizen(false);
			
			if (balsceSI != null && balsceSI < 0) {
				
			}
			HospitalInfoType hospitalInfoType = new HospitalInfoType();
			hospitalInfoType.setHospitalType(intimationRule
					.getHospitalTypeForPremia(intimation));
			*/
			/*if(intimation.getHospitalType() != null)
			{*/
			//Hospitals hospital = hospitalService.getHospitalById(intimation.getHospital());
				/*if(null != intimation.getHospitalType().getKey())
				{
					if(intimation.getHospitalType().getKey().equals( ReferenceTable.NETWORK_HOSPITAL_TYPE_ID))
					{
						Hospitals hospital = hospitalService.getHospitalById(intimation.getHospital());
						if(hospital != null)
						{
							if(hospital.getNetworkHospitalTypeId() != null)
							{
								MastersValue networkHospitalType = 	masterService.getMaster(hospital.getNetworkHospitalTypeId());
								if(networkHospitalType != null)
								{
									hospitalInfoType.setNetworkHospitalType(networkHospitalType.getValue() != null ? networkHospitalType.getValue():"");
								}							
							}
						}
					}
				}
			
			//}
			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(intimation.getPolicy().getPolicyNumber());
			String userId = BPMClientContext.BPMN_TASK_USER;
			String password = BPMClientContext.BPMN_PASSWORD;
			PayloadBOType payloadBO = new PayloadBOType();
			payloadBO.setIntimation(a_message);
			payloadBO.setClaim(claimType);
			payloadBO.setClaimRequest(claimReqType);
			payloadBO.setHospitalInfo(hospitalInfoType);
			payloadBO.setPolicy(policyType);
			payloadBO.setPreAuthReq(preauthReqType);;
			payloadBO.setPaymentInfo(paymentInfoType);
			payloadBO.setClassification(classificationType);*/
			
			Hospitals hospital = null;
			
			if(hospital == null){
	
				hospital = hospitalService.getHospitalById(intimation.getHospital());
			}
			
			
			
			try {
				
				
				if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("Y").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI)) && claimObject != null){
					
					/**
					 *    auto registration for cashless claim and reimbursement claim
					 */
					
					if(claimObject.getClaimId() == null && claimObject.getKey() != null){
						claimObject = clearCashlessService.getClaimByKey(claimObject.getKey());
					}
					
//					Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObject, hospital);
					
					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
					
					Object[] inputArray = (Object[])arrayListForDBCall[0];
					
					if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType)){
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
					}else{
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME;
					}
					
					Object[] parameter = new Object[1];
					parameter[0] = inputArray;
//					dbCalculationService.initiateTaskProcedure(parameter);
					dbCalculationService.revisedInitiateTaskProcedure(parameter);
					
				}else{
					if(null != mapValues.get(SHAConstants.TOTAL_BALANCE_SI) && ("N").equalsIgnoreCase((String)mapValues.get(SHAConstants.TOTAL_BALANCE_SI)) && claimObject == null){
					/**
					 *    Manual registration for cashless claim and reimbursement claim
					 */
					
//					Object[] arrayListForDBCall = SHAUtils.getArrayListForManualRegDBCall(intimation, hospital);
					
					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForManualRegistrationDBCall(intimation, hospital);
					
					Object[] inputArray = (Object[])arrayListForDBCall[0];
					
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
					Object[] parameter = new Object[1];
					parameter[0] = inputArray;
//					dbCalculationService.initiateTaskProcedure(parameter);
					dbCalculationService.revisedInitiateTaskProcedure(parameter);
					
				}
				else{
						if(claimObject.getClaimId() == null && claimObject.getKey() != null){
							claimObject = clearCashlessService.getClaimByKey(claimObject.getKey());
						}
						
//						Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObject, hospital);
						
						Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospital);
						
						Object[] inputArray = (Object[])arrayListForDBCall[0];
						
						if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(strClaimType)){
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
						}else{
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.AUTO_REGISTRATION_OUTCOME;
						}
						
						Object[] parameter = new Object[1];
						parameter[0] = inputArray;
//						dbCalculationService.initiateTaskProcedure(parameter);
						dbCalculationService.revisedInitiateTaskProcedure(parameter);
					}
				}
				
//				IntMsg initiateInitmationTask = BPMClientContext.getIntimationIntiationTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//				initiateInitmationTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			} catch(Exception e) {
				
			}
			
			return true;
		}
		catch(Exception e)
		{
//			e.printStackTrace();
			
			return false;
		}
				
	}
	
public void submitDBProcedureForQueryReply(Claim claim, Preauth preauth,Boolean isPremedical){
		
		Hospitals hospitalById = hospitalService.getHospitalById(claim.getIntimation().getHospital());
		
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		if(isPremedical){
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FLP_QUERY;
		}else{
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_PROCESS_PREAUTH_QUERY;
		}

		inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
		inputArray[SHAConstants.INDEX_CASHLESS_NO] = preauth.getPreauthId();
		
		String preauthReqAmt = preauthService.getPreauthReqAmt(preauth.getKey(), preauth
				.getClaim().getKey());
		if (preauthReqAmt != null) {
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = Double.valueOf(preauthReqAmt);
		} else {
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = 0d;
		}
		
		if(preauth.getTreatmentType() != null){
			inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = preauth.getTreatmentType().getValue();
		}

		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		dbCalculationService.initiateTaskProcedure(parameter);
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
		
		
	}

public Boolean getDBTaskForPreauth(Intimation intimation,String currentQ){
	
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
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

public void submitDBprocedure(Claim claim,String currentQueue,String outCome){
	
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, claim.getIntimation().getIntimationId());
	mapValues.put(SHAConstants.CURRENT_Q, currentQueue);
	
//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
	Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
	
	DBCalculationService dbCalculationService = new DBCalculationService();
//	List<Map<String, Object>> taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);	
	
	List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
	 
	List<Object[]> arrayListFromGettask = SHAUtils.getArrayListFromGettask(taskProcedure);	
	
	 
	for (int i = 0; i < arrayListFromGettask.size(); i++) {
		Object[] resultArrayObj = arrayListFromGettask.get(i);
		Object[] object1 = (Object[]) resultArrayObj[i];
		object1[SHAConstants.INDEX_OUT_COME] = outCome;
		object1[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
		Object[] parameter = new Object[1];
		parameter[0] = object1;
		String initiateTaskProcedure = dbCalculationService.revisedInitiateTaskProcedure(parameter);
	}
	
}
	

}
