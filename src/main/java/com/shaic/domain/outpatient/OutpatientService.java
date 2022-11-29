package com.shaic.domain.outpatient;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.outpatient.processOPpages.BenefitsAvailedDTO;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityDTO;
import com.shaic.claim.outpatient.registerclaim.dto.InsuredDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.mapper.OutpatientMapper;
import com.shaic.claim.outpatient.registerclaim.wizard.DiagnosisDetailsOPTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.balancesuminsured.view.OPClaimStatusDoumentListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasOpClaimSection;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PremiaOPAccumulatorDtls;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.OPPedValidation;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless
public class OutpatientService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService dbService;
	
	
	public OutpatientService() {
		super();
	}
	
	public Double getOPHealthCheckup(Long opHealthCheckupKey) {
		Query query = entityManager.createNamedQuery("OPHealthCheckup.findByKey");
//		query.setParameter("doumentSummaryIds", documentSummaryList);
		Double totalAmount = (Double) query.getSingleResult();
		return totalAmount;
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
	
	/*@SuppressWarnings("unchecked")
	public List<DiagnosisDetailsOPTableDTO> getDiagnosisDetails(Long opKey) {
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", opKey);
		List<PedValidation> resultList =  query.getResultList();
		List<DiagnosisDetailsOPTableDTO> listOfDiagnosisDTO = new ArrayList<DiagnosisDetailsOPTableDTO>();
		if(resultList != null ) {
			for (PedValidation rec : resultList) {
				DiagnosisDetailsOPTableDTO newRec = new DiagnosisDetailsOPTableDTO();
				newRec.setIC rec.get
				
				listOfDiagnosisDTO.add(e);
			}
			return resultList;
		}
		return null;
	}*/
	
	
	@SuppressWarnings("unchecked")
	public List<DiagnosisDetailsOPTableDTO> getDiagnosisList(Long opKey){

		Query query = entityManager.createNamedQuery("OPPedValidation.findByTransactionKey");
		query.setParameter("transactionKey", opKey);

		List<OPPedValidation> results = (List<OPPedValidation>) query.getResultList();
		List<DiagnosisDetailsOPTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsOPTableDTO>();
		for (OPPedValidation pedValidation : results) {
			DiagnosisDetailsOPTableDTO dto = new DiagnosisDetailsOPTableDTO();
			dto.setKey(pedValidation.getKey());
			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();

//			Long sublimitId = pedValidation.getSublimitId();
			pedValidation.getSumInsuredRestrictionId();

			/*if(sublimitId != null){

				Query claimLimitQuery = entityManager.createNamedQuery("ClaimLimit.findByKey");
				claimLimitQuery.setParameter("limitKey",sublimitId);

				ClaimLimit claimLimit = (ClaimLimit)claimLimitQuery.getSingleResult();

				dto.setSublimitNameValue(claimLimit.getLimitName());

				if(claimLimit.getMaxPerPolicyPeriod() != null){
					dto.setSublimitAmt(claimLimit.getMaxPerPolicyPeriod().toString());
				}

				if(pedValidation.getSumInsuredRestrictionId() != null){
					MastersValue master = getMaster(pedValidation.getSumInsuredRestrictionId());
					if(master != null && master.getValue() != null){
						dto.setSumInsuredRestrictionValue(Integer.valueOf(master.getValue()));
					}
				}
			}*/

			dto.setDiagnosis(masters.getValue());
			dto.setDiagnosisId(masters.getKey());
			
			SelectValue diagnosisSelectvalue = new SelectValue();
			diagnosisSelectvalue.setId(masters.getKey());
			diagnosisSelectvalue.setValue(masters.getValue());
			dto.setDiagnosisName(diagnosisSelectvalue);
			
			dto.setIcdChapterKey(pedValidation.getIcdChpterId());			
			dto.setIcdChapter(masterService.getIcdChapterbyId(pedValidation.getIcdChpterId()));
			
			dto.setIcdBlockKey(pedValidation.getIcdBlockId());
			dto.setIcdBlock(masterService.getIcdBlock(pedValidation.getIcdBlockId()));
			
			dto.setIcdCodeKey(pedValidation.getIcdCodeId());
			dto.setIcdCode(masterService.getIcdCodeByKey(pedValidation.getIcdCodeId()));
			
			dto.setRemarks(pedValidation.getDiagnosisRemarks());
			
			/*if(pedValidation.getSubLimitApplicable() != null){
				if(pedValidation.getSubLimitApplicable().equals("Y")){
					dto.setSublimitApplicableValue("Yes");
				}
				else{
					dto.setSublimitApplicableValue("No");
				}
			}
			if(pedValidation.getConsiderForPayment() != null){
				if(pedValidation.getConsiderForPayment().equals("Y")){
					dto.setConsiderForPaymentValue("Yes");
				}
				else
				{
					dto.setConsiderForPaymentValue("No");
				}
			}*/
			diagnosisList.add(dto);

		}
		return diagnosisList;
	}
	
/*	@SuppressWarnings({ "unchecked" })
	public MastersValue getMaster(Long a_key) {
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
	}*/
	
	public List<OPHCDetails> getOpHCDetails(Long healthCheckupKey) {
		Query query = entityManager.createNamedQuery("OPHCDetails.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPHCDetails> resultList =  query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPHCDetails ophcDetails : resultList) {
				entityManager.refresh(ophcDetails);
			}
			return resultList;
		}
		return resultList;
	}
	public List<OPClaimStatusDoumentListDTO> getOPDocumentList(Long healthCheckupKey){
		Query query = entityManager.createNamedQuery("OPDocumentList.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPDocumentList> resultList =  query.getResultList();
		
		List<OPClaimStatusDoumentListDTO> dtoList = new ArrayList<OPClaimStatusDoumentListDTO>();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPDocumentList oPDocumentList : resultList) {
				
				entityManager.refresh(oPDocumentList);
				
				OPClaimStatusDoumentListDTO dto = new OPClaimStatusDoumentListDTO();
			
				dto.setStrReceivedStatus(oPDocumentList.getReceivedStatusId().getValue());
				dto.setNoOfDocuments(oPDocumentList.getNumberOfDocuments());
				dto.setRemarks(oPDocumentList.getRemarks());
	
				Long documentTypeId = oPDocumentList.getDocumentTypeId();
				
				DocumentCheckListMaster documentCheckListMaster = getDocumentCheckListMaster(documentTypeId);
				if(documentCheckListMaster != null){
					dto.setValue(documentCheckListMaster.getValue());
					if(documentCheckListMaster.getMandatoryDocFlag() != null && documentCheckListMaster.getMandatoryDocFlag().equalsIgnoreCase("Y")){
						dto.setMandatoryDocFlag("Yes");
					}else{
						dto.setMandatoryDocFlag("No");
					}
					
					dto.setRequiredDocType(documentCheckListMaster.getRequiredDocType());
				}
				dtoList.add(dto);
				
			}
			return dtoList;
		}
		return dtoList;
		
	}
	
	
	public DocumentCheckListMaster getDocumentCheckListMaster(Long key){
		
		Query query = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
		query.setParameter("primaryKey", key);
		
		List<DocumentCheckListMaster> resultList =  (List<DocumentCheckListMaster>)query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		
		return null;
		
	}
	
	
	
	
	
	
	
	
	
	public List<OPDocumentBillEntry> getOpBillEntryDetails(Long healthCheckupKey) {
		Query query = entityManager.createNamedQuery("OPDocumentBillEntry.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPDocumentBillEntry> resultList =  query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPDocumentBillEntry opBillEntryDetails : resultList) {
				entityManager.refresh(opBillEntryDetails);
			}
			return resultList;
		}
		return null;
	}
	
	public Policy getPolicyByKey(Long policyKey) {
		if (policyKey != null) {
			return entityManager.find(Policy.class, policyKey);
		}
		return null;
	}
	
	public TmpCPUCode getCpuObjectByPincode(String pincode) {
		TmpCPUCode cpuObject = null;
		if (pincode != null) {
			cpuObject = entityManager.find(TmpCPUCode.class,
					Long.valueOf(StringUtils.trim(pincode)));
		}
		return cpuObject;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitOPRegisterClaim(OutPatientDTO bean) {
		try {

			String strUserName = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			Status status = new Status();
			status.setKey(ReferenceTable.OP_REGISTER_CLAIM_STATUS);

			Stage stage = new Stage();
			stage.setKey(ReferenceTable.OP_STAGE);

			Stage intiamtionStage = entityManager.find(Stage.class, ReferenceTable.INTIMATION_STAGE_KEY);
			Status intimationStatus = entityManager.find(Status.class, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);

			OPIntimation intimation = new OPIntimation();
			intimation.setStage(intiamtionStage);
			intimation.setStatus(intimationStatus);
			intimation.setActiveStatus("1");
			/*MastersValue hosTypeValue = new MastersValue();
			hosTypeValue.setKey(bean.getSelectedHospital().getHospitalType().getId());
			hosTypeValue.setValue(bean.getSelectedHospital().getHospitalType().getValue());
			intimation.setHospitalType(hosTypeValue);
			intimation.setHospital(bean.getSelectedHospital().getKey());
			MastersValue claimTypeValue = new MastersValue();
			claimTypeValue.setKey(bean.getDocumentDetails().getClaimType().getId());
			claimTypeValue.setValue(bean.getDocumentDetails().getClaimType().getValue());
			intimation.setClaimType(claimTypeValue);*/
			MastersValue claimType = new MastersValue();
			claimType.setKey(ReferenceTable.OUT_PATIENT);
			claimType.setValue("Out Patient");
			intimation.setClaimType(claimType);
			Policy policyByKey = getPolicyByKey(bean.getPolicyDto().getKey());
			intimation.setPolicy(policyByKey);
			intimation.setInsured(bean.getDocumentDetails().getInsuredPatientName());
			intimation.setCreatedBy(strUserName);
			intimation.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			/*if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null
					&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase("true")) {
				intimation.setEmergencyFlag(SHAConstants.YES_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null
					&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase("true")) {
				intimation.setEmergencyFlag(SHAConstants.N_FLAG);
			}*/
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null
					&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase("true")) {
				intimation.setEmergencyFlag(SHAConstants.YES_FLAG);
			}else {
				intimation.setEmergencyFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null
					&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase("true")) {
				intimation.setAccidentFlag(SHAConstants.YES_FLAG);
			}else {
				intimation.setAccidentFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getRemarksForEmergencyAccident() != null) {
				intimation.setRemarksForEmergencyAccident(bean.getDocumentDetails().getRemarksForEmergencyAccident());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedContactNo() != null) {
				intimation.setDoctorNo(bean.getDocumentDetails().getDocSubmittedContactNo());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedName() != null) {
				intimation.setDoctorName(bean.getDocumentDetails().getDocSubmittedName());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getReasonforConsultation() != null) {
				intimation.setAdmissionReason(bean.getDocumentDetails().getReasonforConsultation());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocEmailId() != null) {
				intimation.setCallerEmail(bean.getDocumentDetails().getDocEmailId());
			}
			
			MastersValue treatmentTypeValue = new MastersValue();
			treatmentTypeValue.setKey(bean.getDocumentDetails().getTreatmentType().getId());
			treatmentTypeValue.setValue(bean.getDocumentDetails().getTreatmentType().getValue());
			intimation.setTreatmentTypeId(treatmentTypeValue);
			MastersValue modeOfReceiptValue = new MastersValue();
			modeOfReceiptValue.setKey(bean.getDocumentDetails().getModeOfReceipt().getId());
			modeOfReceiptValue.setValue(bean.getDocumentDetails().getModeOfReceipt().getValue());
			intimation.setModeOfReceiptId(modeOfReceiptValue);

			MastersValue opLOB = getMaster(policyByKey.getLobId());
			intimation.setLobId(opLOB);
			
			OrganaizationUnit PIOCpuCode = getOrgUnitByCode(policyByKey.getHomeOfficeCode());
			
			if(PIOCpuCode != null){
				TmpCPUCode cpuObjectByPincode = getMasCpuCode(Long.parseLong(PIOCpuCode.getCpuCode())); // discussed and entered the policy issuing office CPU Code.
				intimation.setCpuCode(cpuObjectByPincode);
			}else{
				intimation.setCpuCode(null);
			}

			MastersValue intimationSource = entityManager.find(MastersValue.class, 131l);
			intimation.setIntimationSource(intimationSource);

			if (intimation.getKey() != null) {
				intimation.setModifiedDate(new Date());
				entityManager.merge(intimation);
			} else {
				entityManager.persist(intimation);
			}
			entityManager.flush();
			entityManager.refresh(intimation);

			bean.setIntimationId(intimation.getIntimationId());
			bean.setIntimationKey(intimation.getKey());

			OPClaim claim = new OPClaim();
			claim.setIntimation(intimation);

			Stage claimStage = entityManager.find(Stage.class, ReferenceTable.CLAIM_REGISTRATION_STAGE);
			Status claimStatus = entityManager.find(Status.class, ReferenceTable.CLAIM_REGISTERED_STATUS);

			claim.setStage(claimStage);
			claim.setStatus(claimStatus);
			
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null
					&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase("true")) {
				claim.setEmergencyFlag(SHAConstants.YES_FLAG);
			}else {
				claim.setEmergencyFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null
					&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase("true")) {
				claim.setAccidentFlag(SHAConstants.YES_FLAG);
			}else {
				claim.setAccidentFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getRemarksForEmergencyAccident() != null) {
				claim.setRemarksForEmergencyAccident(bean.getDocumentDetails().getRemarksForEmergencyAccident());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedContactNo() != null) {
				claim.setDoctorNo(bean.getDocumentDetails().getDocSubmittedContactNo());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAmountClaimed() != null) {
				claim.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getOPCheckupDate() != null) {
				claim.setDataOfAdmission(bean.getDocumentDetails().getOPCheckupDate());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getBillReceivedDate() != null) {
				claim.setDocumentReceivedDate(bean.getDocumentDetails().getBillReceivedDate());
			}

			if (treatmentTypeValue != null && treatmentTypeValue.getKey() != null) {
				claim.setTreatmentTypeId(treatmentTypeValue.getKey());
			}
			if (modeOfReceiptValue != null && modeOfReceiptValue.getKey() != null) {
				claim.setModeOfReceiptId(modeOfReceiptValue.getKey());
			}
			if(bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getId() != null){
				claim.setConsulationTypeId(bean.getDocumentDetails().getConsultationType().getId());
			}
			if(bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getCommonValue() != null){
				claim.setOpcoverSection(bean.getDocumentDetails().getConsultationType().getCommonValue());
			}

//			claim.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed() != null ? SHAUtils.getDoubleValueFromString(bean.getDocumentDetails().getAmountClaimed())  : 0d);
//			claim.setProvisionAmount(bean.getDocumentDetails().getProvisionAmt() != null ? SHAUtils.getDoubleValueFromString(bean.getDocumentDetails().getProvisionAmt())  : 0d);
			
			claim.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed() != null ? bean.getDocumentDetails().getAmountClaimed()  : 0d);
//			claim.setProvisionAmount(bean.getDocumentDetails().getProvisionAmt() != null ? bean.getDocumentDetails().getProvisionAmt()  : 0d);
//		    claim.setProvisionAmount(bean.getDocumentDetails().getAmountClaimed() != null ? bean.getDocumentDetails().getAmountClaimed()  : 0d);
		    Long claimKey = 0l;
		    Map<String, Integer> amountList = dbService.getOPAvailableAmount(bean.getDocumentDetails().getInsuredPatientName().getKey(),claimKey, ReferenceTable.OUT_PATIENT,bean.getDocumentDetails().getConsultationType().getCommonValue());
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
			if(bean.getDocumentDetails().getAmountClaimed() != null){
				amountClaimed = bean.getDocumentDetails().getAmountClaimed();
			}
			if(perClaimLimit != null &&  perClaimLimit > 0){
				minclaimLimit = Math.min(Double.valueOf(perClaimLimit.toString()), opAvailableAmount);
				min = Math.min(bean.getDocumentDetails().getAmountClaimed(),minclaimLimit);
			} else if(perPolicyLimit != null && perPolicyLimit > 0){
				minclaimLimit = Math.min(Double.valueOf(perPolicyLimit.toString()), opAvailableAmount);
				min = Math.min(amountClaimed,minclaimLimit);
			} else {
				min= Math.min(amountClaimed, opAvailableAmount);
			}
			claim.setProvisionAmount(min);
			claim.setCurrentProvisionAmount(min);
			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.OUT_PATIENT);
			value.setValue("Out Patient");
			claim.setClaimType(value);
			intimation.setClaimType(claimType);
			claim.setCreatedBy(strUserName);
			claim.setCreatedDate((new Timestamp(System.currentTimeMillis())));

			entityManager.persist(claim);
			entityManager.flush();

			entityManager.refresh(claim);
			bean.setClaimKey(claim.getKey());
			bean.setClaimId(claim.getClaimId());

			//OutpatientMapper mapper = new OutpatientMapper();
			OPHealthCheckup opHealthCheckup = new OPHealthCheckup(); //mapper.getOPHealthCheckup(bean);
			opHealthCheckup.setActiveStatus(1l);
			opHealthCheckup.setStage(stage);
			opHealthCheckup.setStatus(status);
			opHealthCheckup.setClaim(claim);
			opHealthCheckup.setIntimation(intimation);
			
			//OP_HEALTH_CHECKUP_DATE, BILL_REC_DATE, OP_HEALTH_REASON, OP_HEALTH_REMARKS

			opHealthCheckup.setOpHealthCheckupDate(bean.getDocumentDetails().getOPCheckupDate());
			opHealthCheckup.setBillReceivedDate(bean.getDocumentDetails().getBillReceivedDate());
			
			/*MastersValue opReasonValue = new MastersValue();
			opReasonValue.setKey(bean.getDocumentDetails().getReasonForOPVisit().getId());
			opReasonValue.setValue(bean.getDocumentDetails().getReasonForOPVisit().getValue());
			opHealthCheckup.setOpReason(opReasonValue);*/
			
			opHealthCheckup.setOpHealthRemarks(bean.getDocumentDetails().getRemarksForOpVisit());
			opHealthCheckup.setCreatedBy(strUserName);
			opHealthCheckup.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			
			System.out.println(String.format("Home office Code.... [%s]", policyByKey.getHomeOfficeCode()));
			opHealthCheckup.setOfficeCode(policyByKey.getHomeOfficeCode());
			entityManager.persist(opHealthCheckup);
			
//			if(opHealthCheckup.getKey() != null) {
//				entityManager.merge(opHealthCheckup);
//				opHealthCheckup.setModifiedBy(strUserName);
//			} else {
//				opHealthCheckup.setCreatedBy(strUserName);
//				entityManager.persist(opHealthCheckup);
//			}
			entityManager.flush();
			entityManager.clear();
			bean.setKey(opHealthCheckup.getKey());
			
//			PreMedicalMapper preMedicalMapper=PreMedicalMapper.getInstance();
			
			/*List<DiagnosisDetailsOPTableDTO> insuredDetailsList = bean.getDiagnosisListenerTableList();
			for (DiagnosisDetailsOPTableDTO pedValidationDTO : insuredDetailsList) {
				OPPedValidation pedValidation = new OPPedValidation();	
				pedValidation.setTransactionKey(opHealthCheckup.getKey());  // need to discuss with satish.... OPKey is getting inserted.....
				pedValidation.setIntimation(intimation);
				pedValidation.setPolicy(policyByKey);
				pedValidation.setStage(intiamtionStage);
				pedValidation.setStatus(intimationStatus);
				
				pedValidation.setDiagnosisId(pedValidationDTO.getDiagnosisName().getId());
				pedValidation.setIcdChpterId(pedValidationDTO.getIcdChapter().getId());
				pedValidation.setIcdBlockId(pedValidationDTO.getIcdBlock().getId());
				pedValidation.setIcdCodeId(pedValidationDTO.getIcdCode().getId());
				pedValidation.setDiagnosisRemarks(pedValidationDTO.getRemarks());
				
				pedValidation.setCreatedBy(strUserName);
				pedValidation.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				entityManager.persist(pedValidation);
			}
			
			entityManager.flush();
			entityManager.clear();*/

//			if(claim != null){
//				//				claim.setStage(stage);
//				//				claim.setStatus(status);
//				//				claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//				//				entityManager.merge(claim);
//				//				entityManager.flush();
//			}

			/*List<InsuredDetailsDTO> insuredDetailsList = bean.getDocumentDetails().getInsuredDetailsList();
			for (InsuredDetailsDTO insuredDetailsDTO : insuredDetailsList) {
				OPHCDetails ophcDetails = mapper.getOPHCDetails(insuredDetailsDTO);
				ophcDetails.setOpHealthCheckup(opHealthCheckup);
				ophcDetails.setInsured(insuredDetailsDTO.getInsuredPatientName());
				if(ophcDetails.getKey() != null) {
					entityManager.merge(ophcDetails);
				} else {
					entityManager.persist(ophcDetails);
				}
				entityManager.flush();
				insuredDetailsDTO.setKey(ophcDetails.getKey());
			}

			List<DocumentCheckListDTO> docCheckListVal = bean.getDocumentDetails().getDocumentCheckListDTO();
			if (!docCheckListVal.isEmpty()) {
				for (DocumentCheckListDTO docCheckListDTO : docCheckListVal) {
					OPDocumentList opDocumentList = mapper.getOPDocumentList(docCheckListDTO);

					opDocumentList.setOpHealthCheckup(opHealthCheckup);		

					opDocumentList.setKey(null);
					entityManager.persist(opDocumentList);
					entityManager.flush();
					entityManager.clear();

					//					}
				}
			}



			List<OPBillDetailsDTO> billDetailsDTOList = bean.getOpBillEntryDetails().getBillDetailsDTOList();
			for (OPBillDetailsDTO opBillDetailsDTO : billDetailsDTOList) {
				OPDocumentBillEntry opBillEntry = mapper.getOPBillEntry(opBillDetailsDTO);
				opBillEntry.setOpHealthCheckup(opHealthCheckup);
				if(opBillEntry.getKey() != null) {
					entityManager.merge(opBillEntry);
				} else {
					entityManager.persist(opBillEntry);
				}
				entityManager.flush();
				entityManager.clear();
			}

			List<UploadDocumentDTO> uploadDocsDTO = bean.getOpBillEntryDetails().getUploadDocumentDTOList();

			if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
					if (null != uploadDocumentDTO.getFileType()
							&& !("").equalsIgnoreCase(uploadDocumentDTO
									.getFileType().getValue())) {
						@SuppressWarnings("static-access")
						OPDocumentSummary opDocSummary = mapper.getDocumentSummary(uploadDocumentDTO);
						opDocSummary.setOpHealthCheckup(opHealthCheckup);
						entityManager.persist(opDocSummary);
						entityManager.flush();
						entityManager.clear();
					}
				}
			}*/

			//submitTaskToBPM(bean, claim);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void submitTaskToBPM(OutPatientDTO dto, Claim claim) {/*
		RegisterClaim registerClaimTask = BPMClientContext
				.getRegisterClaimTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		
		PayloadBOType payloadBO = new PayloadBOType();
		ClaimType claimReq = new ClaimType();
		claimReq.setClaimId(claim.getClaimId());
		claimReq.setClaimType(claim.getClaimType().getValue());
		claimReq.setKey(claim.getKey());
		
		
		
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(claim.getIntimation().getIntimationId());
		intimationType.setKey(claim.getIntimation().getKey());

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(claim.getIntimation().getPolicy().getPolicyNumber());
		
		CustomerType cusType = new CustomerType();
		cusType.setHealthCardId(claim.getIntimation().getInsured().getHealthCardNumber());
		
		Insured insured = claim.getIntimation().getInsured();
		
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
		classificationType.setSource(SHAConstants.NORMAL);
		
		ClaimRequestType claimRequestType = new ClaimRequestType();
		if(null != claim.getIntimation().getCpuCode() && null != claim.getIntimation().getCpuCode().getCpuCode())
			claimRequestType.setCpuCode(String.valueOf(claim.getIntimation().getCpuCode().getCpuCode()));
		
		payloadBO.setIntimation(intimationType);
		payloadBO.setPolicy(policyType);
		payloadBO.setCustomer(cusType);
		payloadBO.setClaim(claimReq);
		payloadBO.setClassification(classificationType);
		payloadBO.setClaimRequest(claimRequestType);
		
		try{
		registerClaimTask.initiate(BPMClientContext.BPMN_PASSWORD, payloadBO);
		}catch(Exception e){
			e.printStackTrace();
		}
	*/}
	
	public Claim getClaimByKey(Long key) {
		return entityManager.find(Claim.class, key);
	}
	
	public void submitProcessOPClaim(OutPatientDTO bean) {
		try{
			String strUserName = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			
			Stage stage =  null;
			Status status = null;
			if(bean.isApprove()){
				status = entityManager.find(Status.class, ReferenceTable.OP_APPROVE);
			}
			if(bean.isReject()){
				status = entityManager.find(Status.class, ReferenceTable.OP_REJECT);
			}
			
			stage = entityManager.find(Stage.class, ReferenceTable.OP_STAGE);
			
			/*Status status = new Status();
			status.setKey(ReferenceTable.OP_REGISTER_CLAIM_STATUS);

			Stage stage = new Stage();
			if(bean.isApprove()){
				stage.setKey(ReferenceTable.OP_APPROVE);
			}
			if(bean.isReject()){
				stage.setKey(ReferenceTable.OP_REJECT);
			}*/
			
			// Intimation
			OPIntimation intimation = intimationService.getOPIntimationByNo(bean.getIntimationId());
//			Stage intiamtionStage = entityManager.find(Stage.class, ReferenceTable.INTIMATION_STAGE_KEY);
//			Status intimationStatus = entityManager.find(Status.class, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
//			intimation.setStage(stage);
//			intimation.setStatus(status);
			intimation.setActiveStatus("1");
			/*MastersValue hosTypeValue = new MastersValue();
			hosTypeValue.setKey(bean.getSelectedHospital().getHospitalType().getId());
			hosTypeValue.setValue(bean.getSelectedHospital().getHospitalType().getValue());
			intimation.setHospitalType(hosTypeValue);
			intimation.setHospital(bean.getSelectedHospital().getKey());*/
			/*MastersValue claimTypeValue = new MastersValue();
			claimTypeValue.setKey(bean.getDocumentDetails().getClaimType() != null ? bean.getDocumentDetails().getClaimType().getId() : 403l);
			claimTypeValue.setValue(bean.getDocumentDetails().getClaimType() != null ? bean.getDocumentDetails().getClaimType().getValue() : "OUT_PATIENT");
			intimation.setClaimType(claimTypeValue);*/
			Policy policyByKey = getPolicyByKey(bean.getPolicyDto().getKey());
			intimation.setPolicy(policyByKey);
			
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null
					&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				intimation.setEmergencyFlag(SHAConstants.YES_FLAG);
			}else {
				intimation.setEmergencyFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null
					&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				intimation.setAccidentFlag(SHAConstants.YES_FLAG);
			}else {
				intimation.setAccidentFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getRemarksForEmergencyAccident() != null) {
				intimation.setRemarksForEmergencyAccident(bean.getDocumentDetails().getRemarksForEmergencyAccident());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedContactNo() != null) {
				intimation.setDoctorNo(bean.getDocumentDetails().getDocSubmittedContactNo());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedName() != null) {
				intimation.setDoctorName(bean.getDocumentDetails().getDocSubmittedName());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getReasonforConsultation() != null) {
				intimation.setAdmissionReason(bean.getDocumentDetails().getReasonforConsultation());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocEmailId() != null) {
				intimation.setCallerEmail(bean.getDocumentDetails().getDocEmailId());
			}
			
			MastersValue treatmentTypeValue = new MastersValue();
			treatmentTypeValue.setKey(bean.getDocumentDetails().getTreatmentType().getId());
			treatmentTypeValue.setValue(bean.getDocumentDetails().getTreatmentType().getValue());
			intimation.setTreatmentTypeId(treatmentTypeValue);
			MastersValue modeOfReceiptValue = new MastersValue();
			modeOfReceiptValue.setKey(bean.getDocumentDetails().getModeOfReceipt().getId());
			modeOfReceiptValue.setValue(bean.getDocumentDetails().getModeOfReceipt().getValue());
			intimation.setModeOfReceiptId(modeOfReceiptValue);
			intimation.setInsured(bean.getDocumentDetails().getInsuredPatientName());
			intimation.setDoctorName(bean.getDocumentDetails().getDocSubmittedName());

			/*TmpCPUCode cpuObjectByPincode = null;
			intimation.setCpuCode(cpuObjectByPincode);*/

			MastersValue intimationSource = entityManager.find(MastersValue.class, 131l);
			intimation.setIntimationSource(intimationSource);

			intimation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			intimation.setModifiedBy(strUserName);
			
			entityManager.merge(intimation);

			entityManager.flush();
			entityManager.refresh(intimation);

			// Claim
			OPClaim claim = claimService.searchByOPClaimNum(bean.getClaimDTO().getClaimId());
			claim.setIntimation(intimation);

//			Stage claimStage = entityManager.find(Stage.class, ReferenceTable.CLAIM_REGISTRATION_STAGE);
//			Status claimStatus = entityManager.find(Status.class, ReferenceTable.CLAIM_REGISTERED_STATUS);

			claim.setStage(stage); 
			claim.setStatus(status);

			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null
					&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				claim.setEmergencyFlag(SHAConstants.YES_FLAG);
			}else {
				claim.setEmergencyFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null
					&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				claim.setAccidentFlag(SHAConstants.YES_FLAG);
			}else {
				claim.setAccidentFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getRemarksForEmergencyAccident() != null) {
				claim.setRemarksForEmergencyAccident(bean.getDocumentDetails().getRemarksForEmergencyAccident());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocSubmittedContactNo() != null) {
				claim.setDoctorNo(bean.getDocumentDetails().getDocSubmittedContactNo());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getAmountClaimed() != null) {
				claim.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getOPCheckupDate() != null) {
				claim.setDataOfAdmission(bean.getDocumentDetails().getOPCheckupDate());
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getBillReceivedDate() != null) {
				claim.setDocumentReceivedDate(bean.getDocumentDetails().getBillReceivedDate());
			}

			
			if (treatmentTypeValue != null && treatmentTypeValue.getKey() != null) {
				claim.setTreatmentTypeId(treatmentTypeValue.getKey());
			}
			if (modeOfReceiptValue != null && modeOfReceiptValue.getKey() != null) {
				claim.setModeOfReceiptId(modeOfReceiptValue.getKey());
			}
			if(bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getId() != null){
				claim.setConsulationTypeId(bean.getDocumentDetails().getConsultationType().getId());
			}
			if(bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getCommonValue() != null){
				claim.setOpcoverSection(bean.getDocumentDetails().getConsultationType().getCommonValue());
			}
			claim.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed() != null ? bean.getDocumentDetails().getAmountClaimed()  : 0d);
//			claim.setProvisionAmount(bean.getDocumentDetails().getProvisionAmt() != null ? bean.getDocumentDetails().getProvisionAmt()  : 0d);
			if(bean.isApprove()){
				claim.setProvisionAmount(bean.getPayble() != null ? Double.valueOf(bean.getPayble()) : 0d);
				claim.setCurrentProvisionAmount(bean.getPayble() != null ? Double.valueOf(bean.getPayble()) : 0d);
			}
			
			if(bean.isReject()){
				claim.setProvisionAmount(0d);
				claim.setCurrentProvisionAmount(0d);
			}
			/*MastersValue value = new MastersValue();
			value.setKey(bean.getDocumentDetails().getClaimType().getId());
			value.setValue(bean.getDocumentDetails().getClaimType().getValue());
			claim.setClaimType(value);*/
			claim.setModifiedBy(strUserName);
			claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getPhysicalDocsReceivedFlag() != null) {
				claim.setPhysicalDocReceivedFlag(bean.getDocumentDetails().getPhysicalDocsReceivedFlag());
			}else {
				claim.setPhysicalDocReceivedFlag(SHAConstants.N_FLAG);
			}
			if (bean != null &&bean.getDocumentDetails() != null && bean.getDocumentDetails().getPhysicalDocsReceivedDate() != null) {
				claim.setPhysicalDocReceivedDate(bean.getDocumentDetails().getPhysicalDocsReceivedDate());
			}

			entityManager.merge(claim);
			
			entityManager.flush();
			entityManager.refresh(claim);

			// OPHealthCheckup
			OPHealthCheckup opHealthCheckup = getOpHealthByClaimKey(claim.getKey());
			bean.setKey(opHealthCheckup.getKey());
			opHealthCheckup.setStage(stage); 
			opHealthCheckup.setStatus(status);
			opHealthCheckup.setClaim(claim);
			
			opHealthCheckup.setOpHealthCheckupDate(bean.getDocumentDetails().getOPCheckupDate());
			opHealthCheckup.setBillReceivedDate(bean.getDocumentDetails().getBillReceivedDate());

			if(bean.isApprove()){
				opHealthCheckup.setApprovalRemarks(bean.getApprovalRemarks());
			}
			
			if(bean.isReject()){
				opHealthCheckup.setRejectionRemarks(bean.getRejectRemarks());
			}
			
			//Document Details Start 
			MastersValue insuredMas = new MastersValue();
			insuredMas.setKey(ReferenceTable.RECEIVED_FROM_INSURED);
			insuredMas.setValue("Insured");
			
			opHealthCheckup.setDocumentReceivedFromId(insuredMas);
			opHealthCheckup.setDocSubmittedName(bean.getDocumentDetails().getDocSubmittedName());
			opHealthCheckup.setPersonContactNumber(new Long(bean.getDocumentDetails().getDocSubmittedContactNo() != null ? bean.getDocumentDetails().getDocSubmittedContactNo() : 0l));
			
			/*MastersValue mode = new MastersValue();
			mode.setKey(bean.getDocumentDetails().getClaimType().getId());
			mode.setValue(bean.getDocumentDetails().getClaimType().getValue());
			
			opHealthCheckup.setModeOfReceipt(mode);*/
			opHealthCheckup.setPersonEmailId(bean.getDocumentDetails().getDocEmailId());
			if(bean.isApprove()){
				opHealthCheckup.setAmountEligible(bean.getEligible() != null ? Double.valueOf(bean.getEligible()) :0d);
				opHealthCheckup.setAmountPayable(bean.getPayble() != null ? Double.valueOf(bean.getPayble()) : 0d);
			}
			if(bean.isReject()){
				opHealthCheckup.setAmountEligible(0d);
				opHealthCheckup.setAmountPayable(0d);
			}
			opHealthCheckup.setPerVisitAmount(bean.getPerClaimLimit() != null ? bean.getPerClaimLimit():0d);
			opHealthCheckup.setPerPolicyPeriod(bean.getPerPolicyLimit() != null ? bean.getPerPolicyLimit():0d);
			
			
			//Document Details End 
			
			// Payment Details Start
//				461	CHEQUE / DD 462	BANK TRANSFER
			if(bean.isChqPayment() != null){
				if(bean.isChqPayment()){
					opHealthCheckup.setPaymentNoteId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					opHealthCheckup.setReasonForChange(bean.getChqModeChngReason());
					opHealthCheckup.setPayeeEmailId(bean.getChqEmailId());
					opHealthCheckup.setPanNumber(bean.getChqPanno());
					opHealthCheckup.setPayableAt(bean.getChqPayableAt());
					opHealthCheckup.setPayeeName(bean.getChqPayeeName().getInsuredName());
					opHealthCheckup.setChangePayeeNameReason(bean.getChqNameChngReason());
//					opHealthCheckup.setLegalHeirName(bean.getChqHeirName());
				}else{
					opHealthCheckup.setPaymentNoteId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					opHealthCheckup.setReasonForChange(bean.getBnkmodeChngReason());
					opHealthCheckup.setPayeeEmailId(bean.getBnkEmailId());
					opHealthCheckup.setPanNumber(bean.getBnkPanno());
					opHealthCheckup.setPayeeName(bean.getBnkPayeeName().getInsuredName());
					opHealthCheckup.setChangePayeeNameReason(bean.getBnkNameChngReason());
//					opHealthCheckup.setLegalHeirName(bean.getBnkHeirName());
					opHealthCheckup.setAccountNumber(bean.getBnkAccNo());
					opHealthCheckup.setIfscCode(bean.getBnkIfsc());
				}
			}
			
			// Payment Details End
			
			/*MastersValue opReasonValue = new MastersValue();
			opReasonValue.setKey(bean.getDocumentDetails().getReasonForOPVisit().getId());
			opReasonValue.setValue(bean.getDocumentDetails().getReasonForOPVisit().getValue());
			opHealthCheckup.setOpReason(opReasonValue);*/
			
//			opHealthCheckup.setOpHealthRemarks(bean.getDocumentDetails().getRemarksForOpVisit());
			opHealthCheckup.setModifiedBy(strUserName);
			opHealthCheckup.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(opHealthCheckup);
			entityManager.flush();
			entityManager.refresh(opHealthCheckup);
			
			/*OPBenefitAccumulator benefitAccmulator = new OPBenefitAccumulator();
			benefitAccmulator.setPolicyNumber(policyByKey.getPolicyNumber());
			benefitAccmulator.setInsuredNumber(bean.getNewIntimationDTO().getInsuredKey());
			benefitAccmulator.setInsuredName(bean.getNewIntimationDTO().getInsuredPatientName());
			benefitAccmulator.setSumInsured(policyByKey.getTotalSumInsured());
			benefitAccmulator.setClaimYear(Long.valueOf(policyByKey.getPolicyYear()));
			benefitAccmulator.setReferenceflag("OP");
			benefitAccmulator.setApprovedAmt(bean.getPayble() != null ? Double.valueOf(bean.getPayble()) : 0d);
			benefitAccmulator.setAvailableAmt(bean.getAvailableSI());
			benefitAccmulator.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(benefitAccmulator);
			entityManager.flush();*/
			
			/*//Diagnosis Details			
			List<DiagnosisDetailsOPTableDTO> diagnosisDetailsList = bean.getDiagnosisListenerTableList();
			for (DiagnosisDetailsOPTableDTO pedValidationDTO : diagnosisDetailsList) {
				if(pedValidationDTO.getKey() != null){
					OPPedValidation pedValidation = getDiagnosisByKey(pedValidationDTO.getKey());
					pedValidation.setTransactionKey(opHealthCheckup.getKey());
					pedValidation.setIntimation(intimation);
					pedValidation.setPolicy(policyByKey);
					pedValidation.setStage(stage);
					pedValidation.setStatus(status);
					
					pedValidation.setDiagnosisId(pedValidationDTO.getDiagnosisName().getId());
					pedValidation.setIcdChpterId(pedValidationDTO.getIcdChapter().getId());
					pedValidation.setIcdBlockId(pedValidationDTO.getIcdBlock().getId());
					pedValidation.setIcdCodeId(pedValidationDTO.getIcdCode().getId());
					pedValidation.setDiagnosisRemarks(pedValidationDTO.getRemarks());
					pedValidation.setModifiedBy(strUserName);
					pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(pedValidation);
					entityManager.flush();
				}else{
					OPPedValidation pedValidation = new OPPedValidation();
					pedValidation.setTransactionKey(opHealthCheckup.getKey());
					pedValidation.setIntimation(intimation);
					pedValidation.setPolicy(policyByKey);
					pedValidation.setStage(stage);
					pedValidation.setStatus(status);
					
					pedValidation.setDiagnosisId(pedValidationDTO.getDiagnosisName().getId());
					pedValidation.setIcdChpterId(pedValidationDTO.getIcdChapter().getId());
					pedValidation.setIcdBlockId(pedValidationDTO.getIcdBlock().getId());
					pedValidation.setIcdCodeId(pedValidationDTO.getIcdCode().getId());
					pedValidation.setDiagnosisRemarks(pedValidationDTO.getRemarks());
					pedValidation.setCreatedBy(strUserName);
					pedValidation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(pedValidation);
					entityManager.flush();
				}
			}
			
			// Deleted Diagnosis Details
			List<DiagnosisDetailsOPTableDTO> diagnosisDeletedDetailsList = bean.getDeletedDiagnosisListenerTableList();
			for (DiagnosisDetailsOPTableDTO pedValidationDTO : diagnosisDeletedDetailsList) {
				if(pedValidationDTO.getKey() != null){
					OPPedValidation pedValidation = getDiagnosisByKey(pedValidationDTO.getKey());
					pedValidation.setTransactionKey(opHealthCheckup.getKey());
					pedValidation.setIntimation(intimation);
					pedValidation.setPolicy(policyByKey);
					pedValidation.setStage(stage);
					pedValidation.setStatus(status);
					
					pedValidation.setDiagnosisId(pedValidationDTO.getDiagnosisName().getId());
					pedValidation.setIcdChpterId(pedValidationDTO.getIcdChapter().getId());
					pedValidation.setIcdBlockId(pedValidationDTO.getIcdBlock().getId());
					pedValidation.setIcdCodeId(pedValidationDTO.getIcdCode().getId());
					pedValidation.setDiagnosisRemarks(pedValidationDTO.getRemarks());
					pedValidation.setModifiedBy(strUserName);
					pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(pedValidation);
					entityManager.flush();
				}else{
					OPPedValidation pedValidation = new OPPedValidation();
					pedValidation.setTransactionKey(opHealthCheckup.getKey());
					pedValidation.setIntimation(intimation);
					pedValidation.setPolicy(policyByKey);
					pedValidation.setStage(stage);
					pedValidation.setStatus(status);
					
					pedValidation.setDiagnosisId(pedValidationDTO.getDiagnosisName().getId());
					pedValidation.setIcdChpterId(pedValidationDTO.getIcdChapter().getId());
					pedValidation.setIcdBlockId(pedValidationDTO.getIcdBlock().getId());
					pedValidation.setIcdCodeId(pedValidationDTO.getIcdCode().getId());
					pedValidation.setDiagnosisRemarks(pedValidationDTO.getRemarks());
					pedValidation.setCreatedBy(strUserName);
					pedValidation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(pedValidation);
					entityManager.flush();
				}
			}*/

			//Bill Details
//			List<com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO> OPBillDetailsList = bean.getOpBillDetailsList();
			List<UploadDocumentDTO> OPuploadedDocList = bean.getUploadedDocsTableList();
			for (UploadDocumentDTO uploadedDocDTO : OPuploadedDocList) {
				OPDocumentBillEntry billEntry = new OPDocumentBillEntry();
				billEntry.setOpHealthCheckup(opHealthCheckup);
				billEntry.setBillTypeId(uploadedDocDTO.getFileType().getId());
//				billEntry.setBillReceivedStatus(uploadedDocDTO.getReceivedStatus().getValue());
				if(bean.isApprove()){
					// below condition for bill type reports bill date & bill no disabled so checking null check.
					if(uploadedDocDTO.getBillDate() != null){
						billEntry.setBillDate(uploadedDocDTO.getBillDate());
					}	
					if(uploadedDocDTO.getBillNo() != null && uploadedDocDTO.getBillNo().toString() != null){
						billEntry.setBillNumber(uploadedDocDTO.getBillNo().toString());
					}
					if(uploadedDocDTO.getBillAmt() != null){
						billEntry.setClaimedAmount(uploadedDocDTO.getBillAmt().longValue());
					}
					if(uploadedDocDTO.getNonPaybleAmt() != null){
						billEntry.setNonPayableAmount(uploadedDocDTO.getNonPaybleAmt().longValue());
					}
					if(uploadedDocDTO.getDeductibleAmt() != null){
						billEntry.setDeductibleAmt(uploadedDocDTO.getDeductibleAmt().longValue());
					}
					if(uploadedDocDTO.getBillValue() != null){
						billEntry.setPayableAmt(uploadedDocDTO.getBillValue().longValue());
					}
				}
				billEntry.setBillEntryRemarks(uploadedDocDTO.getRemarks());
				if(uploadedDocDTO.getOpkey() != null){
					billEntry.setKey(uploadedDocDTO.getOpkey());
				}
				billEntry.setDocumentToken(uploadedDocDTO.getDmsDocToken());
				billEntry.setFileName(uploadedDocDTO.getFileName());
				if(billEntry.getKey() != null){
					billEntry.setModifiedBy(strUserName);
					billEntry.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(billEntry);

				} else {
					billEntry.setDocumentToken(uploadedDocDTO.getDmsDocToken());
					billEntry.setCreatedBy(strUserName);
					billEntry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(billEntry);
					entityManager.flush();
				}
			}
			
			List<BenefitsAvailedDTO> benefitsList = getBenefitAvailedDtls(bean);
			for (BenefitsAvailedDTO benefitsAvailedDTO : benefitsList) {
				OPHCHospitalDetails ophDetails = new OPHCHospitalDetails();
				ophDetails.setHospitalType(benefitsAvailedDTO.getHospitalTypeId());
				ophDetails.setHospitalName(benefitsAvailedDTO.getHospitalName());
				ophDetails.setHospitalAddress(benefitsAvailedDTO.getHospitalAddress());
				ophDetails.setState(benefitsAvailedDTO.getState());
				ophDetails.setCity(benefitsAvailedDTO.getCity());
				ophDetails.setEmailId(benefitsAvailedDTO.getEmailId());
				ophDetails.setHospitalContactNo(benefitsAvailedDTO.getHospitalContactNumber());
				ophDetails.setHospitalFaxNo(benefitsAvailedDTO.getHospitalFaxNo());
				ophDetails.setHospitalConsulationName(benefitsAvailedDTO.getHospitalName());
				ophDetails.setBenefitAvailedId(benefitsAvailedDTO.getHospitalConsulationName());
				ophDetails.setHospitalDoctorsName(benefitsAvailedDTO.getHospitalDoctorName());
				ophDetails.setClaim(claim);
				if(benefitsAvailedDTO.getHcKey() != null){
					ophDetails.setKey(benefitsAvailedDTO.getHcKey());
				}
				if(benefitsAvailedDTO.getClinic() != null && benefitsAvailedDTO.getClinic()){
					ophDetails.setClinicFlag("Y");
				}
				ophDetails.setOpHealthCheckup(opHealthCheckup);
				if(ophDetails.getKey() != null){
					ophDetails.setModifiedBy(strUserName);
					ophDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(ophDetails);
					entityManager.flush();
				} else {
					ophDetails.setCreatedBy(strUserName);
					ophDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(ophDetails);
					entityManager.flush();
				}
				
			}
			
			List<OPSpecialityDTO> specialityDtls = bean.getSpecialityDTOList();
			if(specialityDtls != null && !specialityDtls.isEmpty()){
				for (OPSpecialityDTO opSpecialityDTO : specialityDtls) {
					OPSpeciality opSpeciality = new OPSpeciality();
					opSpeciality.setSpecialityTypeId(opSpecialityDTO.getSpecialityType().getId());
					opSpeciality.setPedName(opSpecialityDTO.getPedfromPolicy());
					opSpeciality.setClaim(claim);
					opSpeciality.setOpHealthCheckup(opHealthCheckup);
					opSpeciality.setPedRelatedId(opSpecialityDTO.getPed().getId());
					opSpeciality.setRemarks(opSpecialityDTO.getRemarks());
					opSpeciality.setCreatedBy(strUserName);
					opSpeciality.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(opSpeciality);
					entityManager.flush();
				}
			}
			if(bean.isApprove()){
				WeakHashMap dataMap = bean.getFilePathAndTypeMap();
			dataMap.put("intimationNumber",intimation.getIntimationId());
			dataMap.put("claimNumber",claim.getClaimId());
			dataMap.put("createdBy",strUserName);
			if(dataMap != null){
				String billSummaryFilePath = (String) dataMap.get("BillSummaryDocFilePath");
				String billSummaryDocType = (String) dataMap.get("BillSummaryDocType");
				dataMap.put("filePath", billSummaryFilePath);
				dataMap.put("docType", billSummaryDocType);
				uploadGeneratedLetterToDMS(dataMap);
				}
			}
			//Upload Documents
			/*List<UploadDocumentDTO> OPuploadedDocList = bean.getUploadedDocsTableList();
			for (UploadDocumentDTO uploadedDocDTO : OPuploadedDocList) {
				OPDocumentSummary opDocSummary = new OPDocumentSummary();
				opDocSummary.setOpHealthCheckup(opHealthCheckup); // setting OpHealthCheckupKey instead of Reimbursement Key. because there is no ROD/Reimbursement Key ...
				MastersValue uploadFileTypeValue = new MastersValue();
				uploadFileTypeValue.setKey(uploadedDocDTO.getFileType().getId());
				uploadFileTypeValue.setValue(uploadedDocDTO.getFileType().getValue());
				opDocSummary.setFileType(uploadFileTypeValue);
				
				opDocSummary.setFileName(uploadedDocDTO.getFileName());
				opDocSummary.setBillNumber(uploadedDocDTO.getBillNo());
				opDocSummary.setBillDate(uploadedDocDTO.getBillDate());
				opDocSummary.setNoOfItems(uploadedDocDTO.getNoOfItems());
				opDocSummary.setBillAmount(uploadedDocDTO.getBillValue());
				opDocSummary.setDmsDocToken(uploadedDocDTO.getDmsDocToken());
				opDocSummary.setClaim(claim);
				opDocSummary.setCreatedBy(strUserName);
				opDocSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(opDocSummary);
				entityManager.flush();
			}*/
			entityManager.clear();
		}catch(Exception ex){
			System.out.println("Exception occurred in submitProcessOPClaim "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private List<BenefitsAvailedDTO> getBenefitAvailedDtls(OutPatientDTO bean){
		List<BenefitsAvailedDTO> benfitsAvailed = new ArrayList<BenefitsAvailedDTO>();
		if(bean.getBenefitsAvailedDto() != null && bean.getBenefitsAvailedDto().getHospitalName() != null){
			BenefitsAvailedDTO con = new BenefitsAvailedDTO();
			if(bean.getBenefitsAvailedDto().getConsulationHcKey() != null){
				con.setHcKey(bean.getBenefitsAvailedDto().getConsulationHcKey());
			}
			con.setState(bean.getBenefitsAvailedDto().getState());
			con.setCity(bean.getBenefitsAvailedDto().getCity());
			con.setHospitalName(bean.getBenefitsAvailedDto().getHospitalName());
			con.setHospitalAddress(bean.getBenefitsAvailedDto().getHospitalAddress());
			con.setHospitalContactNumber(bean.getBenefitsAvailedDto().getHospitalContactNumber());
			con.setHospitalFaxNo(bean.getBenefitsAvailedDto().getHospitalFaxNo());
			con.setHospitalDoctorName(bean.getBenefitsAvailedDto().getHospitalDoctorName());
			if(bean.getConsulation()){
				con.setHospitalConsulationName("Consultation");
			}
			con.setHospitalTypeId(bean.getBenefitsAvailedDto().getConsulationHospitalType());
			if(bean.getBenefitsAvailedDto().getClinic() != null
					&& bean.getBenefitsAvailedDto().getClinic()){
				con.setClinic(true);
			}
			benfitsAvailed.add(con);
		}
		
		if(bean.getBenefitsAvailedDto() != null && bean.getBenefitsAvailedDto().getProviderName() != null
				&& !bean.getBenefitsAvailedDto().getProviderName().isEmpty()){
			BenefitsAvailedDTO diag = new BenefitsAvailedDTO();
			if(bean.getBenefitsAvailedDto().getDiagnosisHcKey() != null){
				diag.setHcKey(bean.getBenefitsAvailedDto().getDiagnosisHcKey());
			}
			diag.setState(bean.getBenefitsAvailedDto().getProviderState());
			diag.setCity(bean.getBenefitsAvailedDto().getProviderCity());
			diag.setHospitalName(bean.getBenefitsAvailedDto().getProviderName());
			diag.setHospitalAddress(bean.getBenefitsAvailedDto().getProviderAddress());
			diag.setHospitalContactNumber(bean.getBenefitsAvailedDto().getProviderContactNo());
			if(bean.getDiagnosis()){
				diag.setHospitalConsulationName("Diagnostics");
			}
			diag.setHospitalTypeId(bean.getBenefitsAvailedDto().getDiagnosisHospitalType());
			benfitsAvailed.add(diag);
		}
		
		if(bean.getBenefitsAvailedDto() != null && bean.getBenefitsAvailedDto().getPhysiotherapistName() != null){
			BenefitsAvailedDTO phy = new BenefitsAvailedDTO();
			if(bean.getBenefitsAvailedDto().getPhysioThearapyHcKey() != null){
				phy.setHcKey(bean.getBenefitsAvailedDto().getPhysioThearapyHcKey());
			}
			phy.setHcKey(bean.getBenefitsAvailedDto().getPhysioThearapyHcKey());
			phy.setHospitalName(bean.getBenefitsAvailedDto().getPhysiotherapistName());
			phy.setHospitalAddress(bean.getBenefitsAvailedDto().getClinicAddress());
			phy.setHospitalContactNumber(bean.getBenefitsAvailedDto().getPhysiotherapistContactNo());
			phy.setEmailId(bean.getBenefitsAvailedDto().getEmailId());
			if(bean.getPhysiotherapy()){
				phy.setHospitalConsulationName("Physiotherapy");
			}
			benfitsAvailed.add(phy);
		}
		
		if(bean.getBenefitsAvailedDto() != null && bean.getBenefitsAvailedDto().getPharmacytName() != null){
			BenefitsAvailedDTO medicine = new BenefitsAvailedDTO();
			if(bean.getBenefitsAvailedDto().getMedicineHcKey() != null){
				medicine.setHcKey(bean.getBenefitsAvailedDto().getMedicineHcKey());
			}
			medicine.setHospitalName(bean.getBenefitsAvailedDto().getPharmacytName());
			medicine.setHospitalAddress(bean.getBenefitsAvailedDto().getPharmacyAddress());
			medicine.setHospitalContactNumber(bean.getBenefitsAvailedDto().getPharmacyContactNo());
			if(bean.getMedicine()){
				medicine.setHospitalConsulationName("Medicine");
			}
			benfitsAvailed.add(medicine);
		}
		
		return benfitsAvailed;
	}

	
	@SuppressWarnings("unchecked")
	public OPPedValidation getDiagnosisByKey(Long diagnosisKey) {
		List<OPPedValidation> resultList = new ArrayList<OPPedValidation>();
		Query query = entityManager.createNamedQuery("OPPedValidation.findByKey");
		query.setParameter("primaryKey", diagnosisKey);
		resultList = (List<OPPedValidation>)query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
//	public void submitProcessOPClaim(OutPatientDTO bean) {
//		try {
//			Claim claimByKey = getClaimByKey(bean.getClaimKey());
//
//			OutpatientMapper mapper = new OutpatientMapper();
//			OPHealthCheckup opHealthCheckup = mapper.getOPHealthCheckup(bean);
//			if(bean.getStatusKey().equals(ReferenceTable.OP_REJECT)) {
//				opHealthCheckup.setAmountPayable(0d);
//				opHealthCheckup.setAmountEligible(0d);
//				opHealthCheckup.setAvailableSI(0d);
//			}
//			opHealthCheckup.setActiveStatus(1l);
//			opHealthCheckup.setClaim(claimByKey);
//			if(opHealthCheckup.getKey() != null) {
//				entityManager.merge(opHealthCheckup);
//			} else {
//				entityManager.persist(opHealthCheckup);
//			}
//			entityManager.flush();
//			entityManager.clear();
//			bean.setKey(opHealthCheckup.getKey());
//
//			//			claimByKey.setStatus(opHealthCheckup.getStatus());
//			//			claimByKey.setStage(opHealthCheckup.getStage());
////			claimByKey.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed() != null ? SHAUtils.getDoubleValueFromString(bean.getDocumentDetails().getAmountClaimed())  : 0d);
////			claimByKey.setProvisionAmount(bean.getDocumentDetails().getProvisionAmt() != null ? SHAUtils.getDoubleValueFromString(bean.getDocumentDetails().getProvisionAmt())  : 0d);
//			
//			claimByKey.setClaimedAmount(bean.getDocumentDetails().getAmountClaimed() != null ? bean.getDocumentDetails().getAmountClaimed()  : 0d);
//			claimByKey.setProvisionAmount(bean.getDocumentDetails().getProvisionAmt() != null ? bean.getDocumentDetails().getProvisionAmt()  : 0d);
//			
//			claimByKey.setCurrentProvisionAmount(opHealthCheckup.getAmountPayable());
//			if(claimByKey.getKey() != null) {
//				entityManager.merge(claimByKey);
//				entityManager.flush();
//			} else {
//				entityManager.persist(claimByKey);
//				entityManager.flush();
//			}
//			entityManager.clear();
//
//
////			List<InsuredDetailsDTO> insuredDetailsList = bean.getDocumentDetails().getInsuredDetailsList();
////			for (InsuredDetailsDTO insuredDetailsDTO : insuredDetailsList) {
////				OPHCDetails ophcDetails = mapper.getOPHCDetails(insuredDetailsDTO);
////				ophcDetails.setOpHealthCheckup(opHealthCheckup);
////				ophcDetails.setInsured(insuredDetailsDTO.getInsuredPatientName());
////
////				if(ophcDetails.getKey() != null) {
////					entityManager.merge(ophcDetails);
////				} else {
////					entityManager.persist(ophcDetails);
////				}
////				entityManager.flush();
////				entityManager.clear();
////				insuredDetailsDTO.setKey(ophcDetails.getKey());
////			}
//
//			//			if(null != bean.getOpPaymentDTO())
//			//			{
//			//			OPPaymentDTO opPaymentDtoList = bean.getOpPaymentDTO();
//			//			if(null != opPaymentDtoList )
//			//			{
//			//			OPPayment opPayment = mapper.getOPPaymentList(opPaymentDtoList);
//			//			entityManager.persist(opPayment);
//			//			}
//			//			}
//
//
//			List<OPBillDetailsDTO> billDetailsDTOList = bean.getOpBillEntryDetails().getBillDetailsDTOList();
//			for (OPBillDetailsDTO opBillDetailsDTO : billDetailsDTOList) {
//				OPDocumentBillEntry opBillEntry = mapper.getOPBillEntry(opBillDetailsDTO);
//				opBillEntry.setOpHealthCheckup(opHealthCheckup);
//				if(opBillEntry.getKey() != null) {
//					entityManager.merge(opBillEntry);
//				} else {
//					entityManager.persist(opBillEntry);
//				}
//				entityManager.flush();
//				entityManager.clear();
//			}
//			setBPMOutCome(bean);
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	

	private void setBPMOutCome(OutPatientDTO dto) {/*
		SubmitProcessClaimTask submitTask = BPMClientContext.getProcessClaimOPTask(dto.getStrUserName(), dto.getStrPassword());
		HumanTask humanTask = dto.getHumanTask();
		
		String outcome = "REJECT";
		if(dto.getStatusKey().equals(ReferenceTable.OP_APPROVE)) {
			outcome = "APPROVE";
		}
		humanTask.setOutcome(outcome);

		try{
		submitTask.execute(dto.getStrUserName(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
	*/}
	
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
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	public List<OPHCHospitalDetails> getHospitalDetailsByHCKey(Long key) {
		Query query = entityManager.createNamedQuery("OPHCHospitalDetails.findByHCKey");
		query.setParameter("key", key);
		List<OPHCHospitalDetails> hospitalDtls =  query.getResultList();
		if(hospitalDtls != null && !hospitalDtls.isEmpty()) {
			for (OPHCHospitalDetails ophcHospitalDetails : hospitalDtls) {
				entityManager.refresh(ophcHospitalDetails);
			}
			return hospitalDtls;
		}
		return null;
	}
	
	public List<OPDocumentBillEntry> getOpBillEntryByOPHealthKey(Long healthCheckupKey){
		Query query=entityManager.createNamedQuery("OPDocumentBillEntry.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPDocumentBillEntry> resultList = query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPDocumentBillEntry opDocumentBillEntry : resultList) {
				entityManager.refresh(opDocumentBillEntry);
			}
			return resultList;
		}
		
		return resultList;
	}
	
	public List<OPSpeciality> getSpecialityDtlsByHcKey(Long healthCheckKey){
		
		Query qry = entityManager.createNamedQuery("OPSpeciality.findByHealthCheckUpKey");
		qry.setParameter("healthCheckKey", healthCheckKey);
		List<OPSpeciality> specialityList = qry.getResultList();
		if(specialityList != null && !specialityList.isEmpty()){
			for (OPSpeciality opSpeciality : specialityList) {
				entityManager.refresh(opSpeciality);
			}
			return specialityList;
		}
		return null;
	}
	
	public SpecialityType getSpecialityTypeByKey(Long key){
		Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey");
		findSpecilty.setParameter("key", key);
		SpecialityType result = (SpecialityType) findSpecilty.getSingleResult();
		if(result != null){
			return result;
		}
		return null;
	}
	
	public List<DMSDocumentDetailsDTO> getDocumentDetailsData(Long healthChekupKey){
		List<DMSDocumentDetailsDTO> finalDMSDataList = new ArrayList<DMSDocumentDetailsDTO>();
		List<OPDocumentBillEntry> opDocsList = getOpBillEntryDetails(healthChekupKey);
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		if(opDocsList != null && !opDocsList.isEmpty()){
			for (OPDocumentBillEntry opDocumentBillEntry : opDocsList) {
				DMSDocumentDetailsDTO documentList = new DMSDocumentDetailsDTO();
				documentList.setFileName(opDocumentBillEntry.getFileName());
				documentList.setClaimNo(opDocumentBillEntry.getOpHealthCheckup().getClaim().getClaimId());
				documentList.setDmsDocToken(opDocumentBillEntry.getDocumentToken());
				documentList.setIntimationNo(opDocumentBillEntry.getOpHealthCheckup().getClaim().getIntimation().getIntimationId());
				documentList.setDmsRestApiURL(dmsAPIUrl);
				documentList.setDocumentType(SHAConstants.OTHERS);
				documentList.setDocumentSource("OP");
				finalDMSDataList.add(documentList);
			}
		}
		return finalDMSDataList;
	}
	
	public void updateOPClaimDetails(OPClaim opClaim){
		if(opClaim.getKey() != null){
			/*Status status = new Status();
			status.setKey(ReferenceTable.OP_PAYMENT_SETTLED);*/
			Status status = null;
			status = entityManager.find(Status.class, ReferenceTable.OP_PAYMENT_SETTLED);
			if(status != null){
				opClaim.setStatus(status);
			}
			opClaim.setCurrentProvisionAmount(0d);
			entityManager.merge(opClaim);
			entityManager.flush();
//			entityManager.clear();
		}
		dbService.invokeAccumulatorForOP(opClaim.getIntimation().getPolicyNumber(), opClaim.getIntimation().getInsured().getHealthCardNumber(),opClaim.getOpcoverSection());
	}
	
	public void uploadGeneratedLetterToDMS(WeakHashMap dataMap)
	{
		String filePath = (String)dataMap.get("filePath");
		if(null != filePath && !("").equalsIgnoreCase(filePath))
		{
			SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
		}
	}
	
	public MasOpClaimSection getSectionDtlsByKey(Long key){
		
		 Query findBySecKey = entityManager.createNamedQuery(
					"MasOpClaimSection.findByKey").setParameter("key",
							key);
		 List<MasOpClaimSection> secList = findBySecKey.getResultList();
			if(secList !=null && !secList.isEmpty()){
				return secList.get(0);
			}
			return null;
		
	}
	
	public PremiaOPAccumulatorDtls getPremiaAccumulator(String policyNumber){
		
		 Query findByPolicyNo = entityManager.createNamedQuery(
					"PremiaOPAccumulatorDtls.findByPolicyNo").setParameter("policyNumber",
							policyNumber);
		 List<PremiaOPAccumulatorDtls> resultList = findByPolicyNo.getResultList();
		 if(resultList != null && !resultList.isEmpty()){
			 return resultList.get(0);
		 }
		 return null;	
	}
	
	public void updatePaymentDtlsBatch(OPUploadPaymentDetails opUploadPaymentBatchDtls){
		entityManager.persist(opUploadPaymentBatchDtls);
		entityManager.flush();
	}
	
	public String checkSettled(String intimationNum){
		OPIntimation intimationDtls = intimationService.getOPIntimationByNo(intimationNum);
		if(intimationDtls != null){
			OPHealthCheckup opHe = getOpHealthBIntimationKey(intimationDtls.getKey());
			if(opHe != null && opHe.getStatus().getKey().equals(ReferenceTable.OP_SETTLED)){
				return intimationDtls.getIntimationId();
			}
		}
		return null;
	}
	
	public OPHealthCheckup getOpHealthBIntimationKey(Long intimationKey) {
		Query query = entityManager.createNamedQuery("OPHealthCheckup.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<OPHealthCheckup> singleResult =  query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		return null;
	}
	
}
