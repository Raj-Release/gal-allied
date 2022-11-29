/**
 * 
 */
package com.shaic.claim.rod.wizard.service;

import java.sql.Timestamp;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
/*
 import com.oracle.xmlns.bpel.workflow.task.InitiateAckProcessPayloadType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.docreceiptack.DocReceiptACKType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType;*/
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.mapper.AcknowledgeDocumentReceivedMapper;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewRejectionDTO;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.AcknowledgeDocumentBatchTable;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimVerification;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasAcknowledgementDoctor;
import com.shaic.domain.MasAilmentLimit;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasPaClaimCovers;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MasUserAutoAllocation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PAAdditionalCovers;
import com.shaic.domain.PAOptionalCover;
import com.shaic.domain.Policy;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
//import com.shaic.ims.bpm.claim.servicev2.ProcessInstanceService;
/*import com.shaic.ims.bpm.claim.bo.ClaimRequestType;
 import com.shaic.ims.bpm.claim.bo.DocReceiptACKType;
 import com.shaic.ims.bpm.claim.bo.InitiateAckProcessPayloadType;
 import com.shaic.ims.bpm.claim.bo.IntimationType;
 import com.shaic.ims.bpm.claim.bo.PayloadBOType;
 import com.shaic.ims.bpm.claim.bo.PolicyType;
 import com.shaic.ims.bpm.claim.servicev2.ProcessInstanceService;*/
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class AcknowledgementDocumentsReceivedService extends
		AbstractDAO<Preauth> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;

	private final Logger log = LoggerFactory
			.getLogger(AcknowledgementDocumentsReceivedService.class);

	/*
	 * public Page<SearchAcknowledgementDocumentReceiverTableDTO> search(
	 * SearchAcknowledgementDocumentReceiverFormDTO searchFormDTO, String
	 * userName, String passWord) {
	 * 
	 * 
	 * List<SearchAcknowledgementDocumentReceiverTableDTO> result = new
	 * ArrayList<SearchAcknowledgementDocumentReceiverTableDTO>();
	 * SearchAcknowledgementDocumentReceiverTableDTO
	 * searchAcknowledgementDocumentReceiverTableDTO = new
	 * SearchAcknowledgementDocumentReceiverTableDTO();
	 * searchAcknowledgementDocumentReceiverTableDTO.setIntimationNo("12213");
	 * searchAcknowledgementDocumentReceiverTableDTO.setClaimNo("45645");
	 * searchAcknowledgementDocumentReceiverTableDTO.setCpuCode("4564");
	 * searchAcknowledgementDocumentReceiverTableDTO.setDateOfAdmission(new
	 * Date());
	 * searchAcknowledgementDocumentReceiverTableDTO.setHealthCardNo("54654");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setHospitalAddress("tryrty");
	 * searchAcknowledgementDocumentReceiverTableDTO.setHospitalCity("tytry");
	 * searchAcknowledgementDocumentReceiverTableDTO.setHospitalName("tytry");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setInsuredPatiendName("yttr");
	 * searchAcknowledgementDocumentReceiverTableDTO.setPolicyNo("yty");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setReasonForAdmission("tty");
	 * result.add(searchAcknowledgementDocumentReceiverTableDTO);
	 * Page<SearchAcknowledgementDocumentReceiverTableDTO> page = new
	 * Page<SearchAcknowledgementDocumentReceiverTableDTO>();
	 * 
	 * 
	 * page.setPageItems(result);
	 * 
	 * 
	 * return page; }
	 */

	public List<DocumentCheckListDTO> getDocumentCheckListValues(
			MasterService masterService) {
		int i = 1;
		List<DocumentCheckListMaster> masterDocumentCheckList = masterService
				.getDocumentCheckListValuesByType(SHAConstants.MASTER_TYPE_REIMBURSEMENT);

		AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper = AcknowledgeDocumentReceivedMapper
				.getInstance();

		List<DocumentCheckListDTO> documentCheckListDTO = acknowledgeDocumentReceivedMapper
				.getMasDocumentCheckList(masterDocumentCheckList);
		List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
		for (DocumentCheckListDTO documentCheckListDTO1 : documentCheckListDTO) {

			documentCheckListDTO1.setSlNo(documentCheckListDTO
					.indexOf(documentCheckListDTO1) + 1);
			documentCheckListDTOList.add(documentCheckListDTO1);
			
		}
		return documentCheckListDTOList;
	}

	
	public List<DocumentCheckListDTO> getPADocumentCheckListValues(
			MasterService masterService) {
		int i = 1;
		List<DocumentCheckListMaster> masterDocumentCheckList = masterService
				.getPADocumentCheckListValuesByType(SHAConstants.MASTER_TYPE_PA);
		
		AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		
		List<DocumentCheckListDTO> documentCheckListDTO = acknowledgeDocumentReceivedMapper
				.getMasDocumentCheckList(masterDocumentCheckList);
		List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
		for (DocumentCheckListDTO documentCheckListDTO1 : documentCheckListDTO) {

			documentCheckListDTO1.setSlNo(documentCheckListDTO.indexOf(documentCheckListDTO1)+1);
			documentCheckListDTOList.add(documentCheckListDTO1);			
		}
		return documentCheckListDTOList;
	}

	public List<DocumentCheckListDTO> getPADocCheckListValues(
			MasterService masterService,String benefitName) {
		int i = 1;
		List<DocumentCheckListMaster> masterDocumentCheckList = masterService
				.getPADocCheckListForBenefit(SHAConstants.MASTER_TYPE_PA,benefitName);
		
		AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		
		List<DocumentCheckListDTO> documentCheckListDTO = acknowledgeDocumentReceivedMapper
				.getMasDocumentCheckList(masterDocumentCheckList);
		List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
		for (DocumentCheckListDTO documentCheckListDTO1 : documentCheckListDTO) {

			documentCheckListDTO1.setSlNo(documentCheckListDTO.indexOf(documentCheckListDTO1)+1);
			documentCheckListDTOList.add(documentCheckListDTO1);			
		}
		return documentCheckListDTOList;
	}
	
	
	public List<DocumentCheckListDTO> getRODDocumentList(
			MasterService masterService, DocAcknowledgement objDocAck) {

		List<DocumentCheckListDTO> documentCheckListDTO = null;
		List<RODDocumentCheckList> rodDocCheckList = masterService
				.getRODDocumentListValues(objDocAck);

		if (null != rodDocCheckList && !rodDocCheckList.isEmpty()) {
			// documentCheckListDTO =
			// AcknowledgeDocumentReceivedMapper.getRODDocumentCheckList(rodDocCheckList);
		}

		return documentCheckListDTO;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitAckDocReceived(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit ACKNOWLEDGEMENT ________________"
				+ (rodDTO.getNewIntimationDTO() != null ? rodDTO
						.getNewIntimationDTO().getIntimationId()
						: "NULL INTIMATION"));
		try {
			saveAcknowledgeDocRecValues(rodDTO);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}

	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ReceiptOfDocumentsDTO submitPAAckDocReceived(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit PA ACKNOWLEDGEMENT ________________" + ((rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getNewIntimationDto() != null) ? rodDTO.getClaimDTO().getNewIntimationDto().getIntimationId() : "NULL INTIMATION"));
		try {
			return savePAAcknowledgeDocRecValues(rodDTO);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
			return null;
		}
	

	}
	private ReceiptOfDocumentsDTO savePAAcknowledgeDocRecValues(ReceiptOfDocumentsDTO rodDTO){


//		AcknowledgeDocumentReceivedMapper ackDocRecMapper = new AcknowledgeDocumentReceivedMapper();
		AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		Boolean isQueryReplyReceived = false;
		Boolean isReconsideration = false;
		Boolean isQueryReplyNo = false;
		Long rodKey = null;
		// rodDTO.getDocumentDetails().se
		/**
		 * Since we dont know the possible status of the ROD, sample values are
		 * set. Later this will be changed.
		 * */
		rodDTO.setStatusKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		rodDTO.setStageKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		DocAcknowledgement docAck = ackDocRecMapper.getDocAckRecDetails(rodDTO);
		//setClaimedAmountFields(docAck,false ,rodDTO);
		if ((null != rodDTO.getDocumentDetails()
				.getBenifitClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getBenifitClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getBenifitClaimedAmount())))
		docAck.setBenifitClaimedAmount(Double.parseDouble(rodDTO
					.getDocumentDetails().getBenifitClaimedAmount()));
		
		
		   if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getWorkOrNonWorkPlace()){
			   docAck.setWorkPlace(rodDTO.getDocumentDetails().getWorkOrNonWorkPlaceFlag());
		   }
					
				
			if(null != rodDTO.getDocumentDetails().getDeath() && (rodDTO.getDocumentDetails().getDeath().equals(true))){
				docAck.setBenifitFlag(SHAConstants.DEATH_FLAGS);
			}	
			else if(null != rodDTO.getDocumentDetails().getPermanentPartialDisability() && (rodDTO.getDocumentDetails().getPermanentPartialDisability().equals(true))){
				docAck.setBenifitFlag(SHAConstants.PPD);
			}
			else if(null != rodDTO.getDocumentDetails().getPermanentTotalDisability() && (rodDTO.getDocumentDetails().getPermanentTotalDisability().equals(true))){
				docAck.setBenifitFlag(SHAConstants.PTD);
			}
			else if(null != rodDTO.getDocumentDetails().getTemporaryTotalDisability() && (rodDTO.getDocumentDetails().getTemporaryTotalDisability().equals(true))){
				docAck.setBenifitFlag(SHAConstants.TTD);
			}
			else if(null != rodDTO.getDocumentDetails().getHospitalization() && (rodDTO.getDocumentDetails().getHospitalization().equals(true))){
				docAck.setBenifitFlag(SHAConstants.HOSP);
			}
			else if(null != rodDTO.getDocumentDetails().getPartialHospitalization() && (rodDTO.getDocumentDetails().getPartialHospitalization().equals(true))){
				
				docAck.setBenifitFlag(SHAConstants.PART);
			}
			
			
			docAck.setPreHospitalisationFlag(SHAConstants.N_FLAG);
			docAck.setPostHospitalisationFlag(SHAConstants.N_FLAG);
			docAck.setPartialHospitalisationFlag(SHAConstants.N_FLAG);
			docAck.setLumpsumAmountFlag(SHAConstants.N_FLAG);
			docAck.setHospitalCashFlag(SHAConstants.N_FLAG);
			docAck.setPatientCareFlag(SHAConstants.N_FLAG);
			docAck.setHospitalizationRepeatFlag(SHAConstants.N_FLAG);
			
			
			if(null != rodDTO.getDocumentDetails().getDocumentType()){
				docAck.setDocumentTypeId(rodDTO.getDocumentDetails().getDocumentType().getId());
			}
		
		if (null != rodDTO.getReconsiderRODdto()
				&& null != rodDTO.getReconsiderRODdto().getRodKey()) {
			
			ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO.getReconsiderRODdto(); 
			
			docAck.setRodKey(reconsiderDTO.getRodKey());
			rodKey = reconsiderDTO.getRodKey();
			
			docAck.setHospitalisationFlag(reconsiderDTO.getHospitalizationFlag());			
			docAck.setPartialHospitalisationFlag(reconsiderDTO.getPartialHospitalizationFlag());		
			docAck.setBenifitFlag(reconsiderDTO.getBenefitFlag());
			docAck.setBenifitClaimedAmount(reconsiderDTO.getBenefitClaimedAmnt());
			
			docAck.setReconsideredDate((new Timestamp(System
					.currentTimeMillis())));
			
			if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
			{
				MastersValue masValue = new MastersValue();
				masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
				masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
				docAck.setReconsiderationReasonId(masValue);
			}
			if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
			{
				docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
			}
			SelectValue reasonForReconsideration = rodDTO.getDocumentDetails().getReasonForReconsideration();
			if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
			{
				MastersValue masReasonForReconsideration = new MastersValue();
				masReasonForReconsideration.setKey(reasonForReconsideration.getId());
				masReasonForReconsideration.setValue(reasonForReconsideration.getValue());
				docAck.setReconsiderationReasonId(masReasonForReconsideration);
			}
		
			if(null != rodDTO.getReconsiderRodRequestList() && !rodDTO.getReconsiderRodRequestList().isEmpty() && rodDTO.getDocumentDetails().getDocumentType().getId().equals(ReferenceTable.PA_RECONSIDERATION_DOCUMENT))
			{
				docAck.setReconsiderationRequest(SHAConstants.YES_FLAG);
			}
			else
			{
				docAck.setReconsiderationRequest(SHAConstants.N_FLAG);
			}
			isReconsideration = true;
			
			//setClaimedAmountFields(docAck,true,rodDTO);
		} 
//		else if (null != rodDTO.getRodqueryDTO()
//				&& null != rodDTO.getRodqueryDTO().getReimbursementKey()) 
//		{
//			docAck.setRodKey(rodDTO.getRodqueryDTO().getReimbursementKey());
//			isQueryReplyReceived = true;
//			
//		}
		else if(null != rodDTO.getRodqueryDTO() && rodDTO.getRodqueryDTO().getReimbursementKey() != null){
			
			isQueryReplyReceived = true;
			RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
			rodKey = rodQueryDetailsDTO.getReimbursementKey();
			docAck.setRodKey(rodKey);
			docAck.setHospitalisationFlag(rodQueryDetailsDTO.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(rodQueryDetailsDTO.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(rodQueryDetailsDTO.getPostHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(rodQueryDetailsDTO.getPartialHospitalizationFlag());
			docAck.setLumpsumAmountFlag(rodQueryDetailsDTO.getAddOnBenefitsLumpsumFlag());
			docAck.setHospitalCashFlag(rodQueryDetailsDTO.getAddOnBeneftisHospitalCashFlag());
			docAck.setPatientCareFlag(rodQueryDetailsDTO.getAddOnBenefitsPatientCareFlag());
			docAck.setHospitalizationRepeatFlag(rodQueryDetailsDTO.getHospitalizationRepeatFlag());
			docAck.setBenifitFlag(rodQueryDetailsDTO.getBenefitFlag());
			docAck.setBenifitClaimedAmount(rodQueryDetailsDTO.getBenefitClaimedAmount());
			docAck.setPaymentCancellationFlag("N");
			
			if(null != rodQueryDetailsDTO.getReimbursementQueryKey())
			{
//				ReimbursementQuery reimbursmentQuery =	getReimbursementQueryData(rodQueryDetailsDTO.getReimbursementQueryKey());
//				reimbursmentQuery.setQueryReply(SHAConstants.YES_FLAG);
//			
//				if(null != reimbursmentQuery)
//				{
////					reimbursmentQuery.setQueryReplyDate((new Timestamp(System
////							.currentTimeMillis())));
//					if(null != rodDTO)
//					{
////						if(reimbursmentQuery.getStage() != null && reimbursmentQuery.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
////							Status status = new Status();
////							status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
////							reimbursmentQuery.setStatus(status);
////						}else{
////							Status status = new Status();
////							status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
////							reimbursmentQuery.setStatus(status);
////						}
//						reimbursmentQuery.setModifiedBy(rodDTO.getStrUserName());
//						reimbursmentQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//					}
//					entityManager.merge(reimbursmentQuery);
//					entityManager.flush();
//					log.info("------ReimbursementQuery------>"+reimbursmentQuery+"<------------");
//
//				}
			}
		}
		
		docAck.setActiveStatus(1l);
		docAck.setClaim(searchByClaimKey(rodDTO.getClaimDTO().getKey()));
		docAck.setAcknowledgeNumber(rodDTO.getDocumentDetails()
				.getAcknowledgementNumber());
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		docAck.setCreatedBy(userNameForDB);
		
		/**
		 * As per DB Team(Prakash) suggestion, Updating claim object before acknowledgement creation,
		 * since the status updated by the trigger is getting
		 * overridden by the application in claim table.
		 * 
		 * **/
		
		Claim claimObj = docAck.getClaim();
		if(null != claimObj)
		{
		claimObj = getClaimByClaimKey(claimObj.getKey());
		}
		if(null != claimObj)
		{
			Status status = new Status();
			status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			
			
			Boolean incidentFlag = null !=  rodDTO.getDocumentDetails().getAccidentOrDeath() ?  rodDTO.getDocumentDetails().getAccidentOrDeath() : null;
			Date incidenceDate = null !=  rodDTO.getDocumentDetails().getAccidentOrDeathDate() ?  rodDTO.getDocumentDetails().getAccidentOrDeathDate() : null;
			
			if(null != incidentFlag && incidentFlag.equals(true)){
			claimObj.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
			}
			else
			{
				claimObj.setIncidenceFlag(SHAConstants.DEATH_FLAG);
			}
			
			if(null != incidenceDate){
				
				claimObj.setIncidenceDate(incidenceDate);
			}
		/*	
			if(claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && claimObj.getIncidenceFlag().equalsIgnoreCase(SHAConstants.ACCIDENT_FLAG))
			{
				docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			}
			else
			{
				docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
			}*/
			
			if(claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
			{
				if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
				{
					docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				}
				
				else
				{
					docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
				}
				if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) &&
						((docAck.getHospitalisationFlag() != null && docAck.getHospitalisationFlag().equals("Y")) || (docAck.getPartialHospitalisationFlag() != null && docAck.getPartialHospitalisationFlag().equals("Y")))) //---need to implement partialhospitallization
				{
					docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				}	
				
			}
			
			if(claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
			{
				if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && docAck.getHospitalisationFlag() != null && 
						("Y").equalsIgnoreCase(docAck.getHospitalisationFlag())) //---need to implement partialhospitallization
				{
					docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				}	
				else
				{
				docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
				//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
				}
			}
			
			if(!((null != docAck && null != docAck.getReconsiderationRequest() && ("Y").equalsIgnoreCase(docAck.getReconsiderationRequest())) || isQueryReplyReceived))
			{
				if(null != docAck && null != docAck.getClaim() && null != docAck.getClaim().getClaimType() && docAck.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
					/*&& null != docAck.getHospitalisationFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getHospitalisationFlag()))*/
				{
					//claimObj.set
					updatePAProvisionAndClaimStatus(docAck, claimObj,rodDTO);
				}
				else if(rodDTO.getIsConversionAllowed() != null && rodDTO.getIsConversionAllowed())
				{
					if(claimObj.getStatus().getKey().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS))
					{
						updatePAProvisionAndClaimStatus(docAck, claimObj,rodDTO);
					}

					
					/*Status claimStatus = new Status();
					claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
					claimObj.setStatus(claimStatus);*/
					
					/**
					 * For those cases where ack is created for claim
					 * which got converted via, convert to reimbursement
					 * process, claim registered date should not be
					 * updated. Hence the below condition of null check
					 * is added.
					 * */
					/**
					 * 
					 * Only if the claim is not registered, then the registration
					 * date would be null and hence we update the status and date. If this 
					 * is not null, then claim is already registered and this is a 
					 * conversion case.
					 * */
					
					if(null == claimObj.getClaimRegisteredDate())
					{
						claimObj.setClaimRegisteredDate((new Timestamp(System
								.currentTimeMillis())));
						
						Status claimStatus = new Status();
						claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
						claimObj.setStatus(claimStatus);
						/*
						 * Modified by user updation code is commented, since
						 * modified user id and created user id would be
						 * diff in this scenario. This is not required. Hence
						 * commenting based on prakash inputs.
						 * **/
						/*if(rodDTO != null){
							claimObj.setModifiedBy(rodDTO.getStrUserName());
						}*/
						claimObj.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
					
					}
					
					/*
					if(null != rodDTO)
					{
						if(null != rodDTO)
						claimObj.setModifiedBy(rodDTO.getStrUserName());
						claimObj.setModifiedDate(new Timestamp(System
							.currentTimeMillis()));
					}*/
					entityManager.merge(claimObj);
					entityManager.flush();
					log.info("------Claim------>"+claimObj+"<------------");

				}
			}
			
			/**
			 * As per sathish sir suggestion,
			 * in all cases where claim registered 
			 * date is null, then only claim registered date 
			 * will be updated. Else it won't get updated.
			 * 
			 * */
			
			if(null == claimObj.getClaimRegisteredDate())
			{
				claimObj.setClaimRegisteredDate((new Timestamp(System
						.currentTimeMillis())));
				Status claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				claimObj.setStatus(claimStatus);
				/*if(rodDTO != null){
					claimObj.setModifiedBy(rodDTO.getStrUserName());
				}*/
				claimObj.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));
				entityManager.merge(claimObj);
				entityManager.flush();
			}
			
//			claimObj.setStatus(status);
//			claimObj.setStage(stage);
//			claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(claimObj);
//			entityManager.flush();
			
		}
		
		
		entityManager.persist(docAck);
		entityManager.flush();
		log.info("------DocAcknowledgement------>"+docAck.getAcknowledgeNumber()+"<------------");
		entityManager.refresh(docAck);
		
		/**
		 * If reconsideration request is selected, then
		 * the current acknowledgement needs to be updated in
		 * reimbursement table.
		 * */
		if(null != rodKey)
		{
			
			Reimbursement reimbursement = getReimbursement(rodKey);
			
//			if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
//				Status status = new Status();
//				status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
//				reimbursement.setStatus(status);
//			}else if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
//				Status status = new Status();
//				status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
//				reimbursement.setStatus(status);
//			}

			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);
			reimbursement.setModifiedBy(userName);
			reimbursement.setDocAcknowLedgement(docAck);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
			log.info("------Reimbursement------>"+reimbursement+"<------------");
		}
		
//		if(isQueryReplyReceived)
//		{
//			RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
//			ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO.getReimbursementQueryKey());
//			if(("No").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
//			{
//				reimbQuery.setQueryReply("N");
//			}
//			else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
//			{
//				reimbQuery.setQueryReply("Y");
//			}
//			reimbQuery.setDocAcknowledgement(docAck);
//			if(null != reimbQuery.getKey())
//			{
//				entityManager.merge(reimbQuery);
//				entityManager.flush();
//			}
//		}else 
		
		Boolean isQueryStatusYes = false;
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO.getRodQueryDetailsList();
		if(isQueryReplyReceived && rodQueryDetailsDTO != null){

			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
				ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2.getReimbursementQueryKey());
				if(("No").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("N");
				}
				else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("Y");
					reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
					rodDTO.setCreatedByForQuery(reimbQuery.getCreatedBy());
					isQueryStatusYes = true;
				}
				reimbQuery.setDocAcknowledgement(docAck);
				if(null != reimbQuery.getKey())
				{
					if(null != rodDTO)
					{
						reimbQuery.setModifiedBy(rodDTO.getStrUserName());
						reimbQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
					entityManager.merge(reimbQuery);
					entityManager.flush();
					log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
				}
			}
		}

		List<RODQueryDetailsDTO> rodQueryDetailsDTO1 = rodDTO.getPaymentQueryDetailsList();
		if(isQueryReplyReceived && rodQueryDetailsDTO1 != null){
			
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO1) {
				ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2.getReimbursementQueryKey());
				if(("No").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("N");
				}
				else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("Y");
					reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
					rodDTO.setCreatedByForQuery(reimbQuery.getCreatedBy());
					isQueryStatusYes = true;
				}
				reimbQuery.setDocAcknowledgement(docAck);
				if(null != reimbQuery.getKey())
				{
					if(null != rodDTO)
					{
						reimbQuery.setModifiedBy(rodDTO.getStrUserName());
						reimbQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
					entityManager.merge(reimbQuery);
					entityManager.flush();
					log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
				}
			}
		}
		
		
		
		List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
				.getDocumentCheckList();
		if (!docCheckList.isEmpty()) {
			for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
				// if(null != docCheckListDTO.getNoOfDocuments())
				if (null != docCheckListDTO.getReceivedStatus()
						&& !("").equalsIgnoreCase(docCheckListDTO
								.getReceivedStatus().getValue())) {
					/*
					 * if(null != docCheckListDTO.getDocChkLstKey()) {
					 * RODDocumentCheckList documentDetails =
					 * findRODDocumentCheckListByKey
					 * (docCheckListDTO.getDocChkLstKey());
					 * entityManager.merge(documentDetails);
					 * entityManager.flush(); } else
					 */
					// {
					RODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
							.getRODDocumentCheckList(docCheckListDTO);
					rodDocumentCheckList.setDocAcknowledgement(docAck);
					// findRODDocumentCheckListByKey(masterService);
					entityManager.persist(rodDocumentCheckList);
					entityManager.flush();
					log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
					// }
				}
			}
		}
		
		
		
		
		
		//DBCalculationService dbCalculationService = new DBCalculationService();
		/*List<AddOnCoversTableDTO> addOnCoversProcedureList = dbCalculationService.getClaimCoverValues(
				SHAConstants.ADDITIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
				docAck.getClaim().getIntimation().getInsured().getKey());*/
		
		//PAAdditionalCovers paAdditionalCovers = new PAAdditionalCovers();
		
		if((null != rodDTO.getRodqueryDTO() && null != rodDTO.getRodqueryDTO().getReimbursementKey()))
		{
			
			saveAdditionalAndOptionalCoversList(rodDTO.getRodqueryDTO().getReimbursementKey(), docAck.getKey());
		}
		else if (null != rodDTO.getReconsiderRODdto()
				&& null != rodDTO.getReconsiderRODdto().getRodKey())
		{
			saveAdditionalAndOptionalCoversList(rodDTO.getReconsiderRODdto().getRodKey(), docAck.getKey());
		}
		else		
		{
			List<AddOnCoversTableDTO> paAddOnCovers = rodDTO.getDocumentDetails().getAddOnCoversList();
			if(null != paAddOnCovers && !paAddOnCovers.isEmpty() )
			{
			
				for (AddOnCoversTableDTO addOnCoversTableDTO : paAddOnCovers) {
				
					PAAdditionalCovers paAdditionalCovers = new PAAdditionalCovers();
					paAdditionalCovers.setAcknowledgementKey(docAck.getKey());				
					paAdditionalCovers.setRodKey(rodDTO.getDocumentDetails().getRodKey());
					paAdditionalCovers.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
					paAdditionalCovers.setClaimKey(rodDTO.getClaimDTO().getKey());
					
					if((ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))){
						if(docAck.getClaim().getIntimation().getUnNamedKey() != null){
							paAdditionalCovers.setUnNamedKey(docAck.getClaim().getIntimation().getUnNamedKey());
						}
					}
					
					if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCovers())
					{
					paAdditionalCovers.setCoverId(addOnCoversTableDTO.getCovers().getId());
					paAdditionalCovers.setClaimedAmount(addOnCoversTableDTO.getClaimedAmount());
					}
					
					//Since provision doesn't happen at ack level, setting this amount to 0.
					paAdditionalCovers.setProvisionAmount(0d);
					
					/*if(null != addOnCoversProcedureList && !addOnCoversProcedureList.isEmpty())
					{
						for (AddOnCoversTableDTO addOnCoversProcObj : addOnCoversProcedureList) {
							
							if(null != addOnCoversTableDTO.getCovers() && addOnCoversProcObj.getCoverId().equals(addOnCoversTableDTO.getCovers().getId()))
							{
								if(null != addOnCoversTableDTO.getClaimedAmount())
								{
									Double ProvisionAmt = Math.min(addOnCoversTableDTO.getClaimedAmount() , addOnCoversProcObj.getClaimedAmount());
									paAdditionalCovers.setProvisionAmount(ProvisionAmt);
								}
								break;
							
							}
							
						}
					}*/
					String userName = rodDTO.getStrUserName();
					userName = SHAUtils.getUserNameForDB(userName);
					paAdditionalCovers.setModifiedBy(userName);				
					paAdditionalCovers.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					paAdditionalCovers.setCreatedBy(userNameForDB);
					paAdditionalCovers.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					if(null != paAdditionalCovers.getKey())
					{
					entityManager.merge(paAdditionalCovers);
					entityManager.flush();
					}
					else
					{
					entityManager.persist(paAdditionalCovers);
					entityManager.flush();
					}
				}
				
				
			}
		
		
			
			List<AddOnCoversTableDTO> paOptionalCover = rodDTO.getDocumentDetails().getOptionalCoversList();
			
		//	PAOptionalCover paOptionalCovers = new PAOptionalCover();
			
			if(null != paOptionalCover && !paOptionalCover.isEmpty() )
			{
			
				for (AddOnCoversTableDTO addOnCoversTableDTO : paOptionalCover) {
					PAOptionalCover paOptionalCovers = new PAOptionalCover();
					paOptionalCovers.setAcknowledgementKey(docAck.getKey());				
					paOptionalCovers.setRodKey(rodDTO.getDocumentDetails().getRodKey());
					paOptionalCovers.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
					paOptionalCovers.setClaimKey(rodDTO.getClaimDTO().getKey());
					if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getOptionalCover())
					{
						paOptionalCovers.setCoverId(addOnCoversTableDTO.getOptionalCover().getId());
						paOptionalCovers.setClaimedAmount(addOnCoversTableDTO.getClaimedAmount());
					
						//CR2019100 Deduction for PA Medical Extention
						if(ReferenceTable.getMedicalExtentionKeys().containsKey((addOnCoversTableDTO.getOptionalCover().getId()))) {
							paOptionalCovers.setTotalClaimAmt(addOnCoversTableDTO.getClaimedAmount());
						}
					
					}
					paOptionalCovers.setProvisionAmount(0d);
					String userName = rodDTO.getStrUserName();
					userName = SHAUtils.getUserNameForDB(userName);
					paOptionalCovers.setModifiedBy(userName);	
					paOptionalCovers.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					paOptionalCovers.setCreatedBy(userNameForDB);
					paOptionalCovers.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					if(null != paOptionalCovers.getKey())
					{
					entityManager.merge(paOptionalCovers);
					entityManager.flush();
					}
					else
					{
					entityManager.persist(paOptionalCovers);
					entityManager.flush();
					}
				}
			
			}
		}
		
	
		if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
		{
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber",docAck.getClaim().getIntimation().getIntimationId());
			Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
			if(null != objClaim)
			{
				dataMap.put("claimNumber",objClaim.getClaimId());
				if(null != objClaim.getClaimType())
				{
					if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
						{
							Preauth preauth = getPreauthClaimKey( objClaim.getKey());
							if(null != preauth)
								dataMap.put("cashlessNumber", preauth.getPreauthId());
						}
				}
			}
			dataMap.put("filePath", rodDTO.getDocFilePath());
			dataMap.put("docType", rodDTO.getDocType());
			dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			dataMap.put("createdBy", rodDTO.getStrUserName());
			SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			SHAUtils.setClearReferenceData(dataMap);
		}
		//submitPATaskToBPM(rodDTO, docAck,isQueryStatusYes,isReconsideration);
		
		submitTaskToDB(rodDTO,docAck,isQueryStatusYes, isReconsideration);
	
		return rodDTO;
	}
	private void saveAcknowledgeDocRecValues(ReceiptOfDocumentsDTO rodDTO) {

//		AcknowledgeDocumentReceivedMapper ackDocRecMapper = new AcknowledgeDocumentReceivedMapper();
		AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		Boolean isQueryReplyReceived = false;
		Boolean isReconsideration = false;
		Boolean isQueryReplyNo = false;
		Long rodKey = null;
		// rodDTO.getDocumentDetails().se
		/**
		 * Since we dont know the possible status of the ROD, sample values are
		 * set. Later this will be changed.
		 * */
		rodDTO.setStatusKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		rodDTO.setStageKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		DocAcknowledgement docAck = ackDocRecMapper.getDocAckRecDetails(rodDTO);
		//setClaimedAmountFields(docAck,false ,rodDTO);
		if ((null != rodDTO.getDocumentDetails()
				.getHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())))
		{
			docAck.setHospitalizationClaimedAmount(Double.parseDouble(rodDTO
					.getDocumentDetails().getHospitalizationClaimedAmount()));
		docAck.setHospClaimedAmountDocRec(Double.parseDouble(rodDTO
				.getDocumentDetails().getHospitalizationClaimedAmount()));
		}
		
		if ((null != rodDTO.getDocumentDetails()
				.getPreHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())))
		{
			docAck.setPreHospitalizationClaimedAmount(Double.parseDouble(rodDTO
					.getDocumentDetails().getPreHospitalizationClaimedAmount()));
		docAck.setPreHospClaimedAmountDocRec(Double.parseDouble(rodDTO
				.getDocumentDetails().getPreHospitalizationClaimedAmount()));
		}
		
		if ((null != rodDTO.getDocumentDetails()
				.getPostHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())))
		{
			docAck.setPostHospitalizationClaimedAmount(Double
					.parseDouble(rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount()));
		docAck.setPostHospClaimedAmountDocRec(Double
				.parseDouble(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount()));
		}
		
		if ((null != rodDTO.getDocumentDetails()
				.getOtherBenefitclaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getOtherBenefitclaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getOtherBenefitclaimedAmount())))
		{
			docAck.setOtherBenefitsClaimedAmount(Double
					.parseDouble(rodDTO.getDocumentDetails()
							.getOtherBenefitclaimedAmount()));
			docAck.setOtherBenefitsAmountDocRec(Double
					.parseDouble(rodDTO.getDocumentDetails()
							.getOtherBenefitclaimedAmount()));
		}
		
		if(null != rodDTO.getDocumentDetails().getDocumentVerificationFlag()){
			
			docAck.setDocumentVerifiedFlag(rodDTO.getDocumentDetails().getDocumentVerificationFlag());
		}
		
	
		
		if (null != rodDTO.getReconsiderRODdto()
				&& null != rodDTO.getReconsiderRODdto().getRodKey()) {
			
			ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO.getReconsiderRODdto(); 
			
			docAck.setRodKey(reconsiderDTO.getRodKey());
			rodKey = reconsiderDTO.getRodKey();
			/*if ((null != reconsiderDTO.
					getHospitalizationClaimedAmt()))
				docAck.setHospitalizationClaimedAmount( reconsiderDTO.
						getHospitalizationClaimedAmt());
			if ((null != reconsiderDTO
					.getPreHospClaimedAmt()))
				docAck.setPreHospitalizationClaimedAmount(reconsiderDTO
						.getPreHospClaimedAmt());
			if ((null != reconsiderDTO
					.getPostHospClaimedAmt()))
				docAck.setPostHospitalizationClaimedAmount(reconsiderDTO
						.getPostHospClaimedAmt());*/
			
			docAck.setHospitalisationFlag(reconsiderDTO.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(reconsiderDTO.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(reconsiderDTO.getPostHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(reconsiderDTO.getPartialHospitalizationFlag());
			docAck.setLumpsumAmountFlag(reconsiderDTO.getLumpSumAmountFlag());
			docAck.setHospitalCashFlag(reconsiderDTO.getAddOnBenefitsHospitalCashFlag());
			docAck.setPatientCareFlag(reconsiderDTO.getAddOnBenefitsPatientCareFlag());
			docAck.setHospitalizationRepeatFlag(reconsiderDTO.getHospitalizationRepeatFlag());
			docAck.setOtherBenefitsFlag(reconsiderDTO.getOtherBenefitFlag());
			if(null != reconsiderDTO.getOtherBenefitFlag() && (SHAConstants.YES_FLAG.equalsIgnoreCase(reconsiderDTO.getOtherBenefitFlag()))){
				docAck.setCompassionateTravel(reconsiderDTO.getCompassionateTravelFlag());
				docAck.setEmergencyMedicalEvaluation(reconsiderDTO.getEmergencyMedicalEvaluationFlag());
				docAck.setRepatriationOfMortalRemain(reconsiderDTO.getRepatriationOfMortalRemainsFlag());
				docAck.setSharedAccomodation(reconsiderDTO.getSharedAccomodationFlag());
				docAck.setPreferredNetworkHospita(reconsiderDTO.getPreferredNetworkHospitalFlag());
			}
			docAck.setReconsideredDate((new Timestamp(System
					.currentTimeMillis())));
			
			if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
			{
				MastersValue masValue = new MastersValue();
				masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
				masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
				docAck.setReconsiderationReasonId(masValue);
			}
			if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
			{
				docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
			}
			SelectValue reasonForReconsideration = rodDTO.getDocumentDetails().getReasonForReconsideration();
			if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
			{
				MastersValue masReasonForReconsideration = new MastersValue();
				masReasonForReconsideration.setKey(reasonForReconsideration.getId());
				masReasonForReconsideration.setValue(reasonForReconsideration.getValue());
				docAck.setReconsiderationReasonId(masReasonForReconsideration);
			}
			
			isReconsideration = true;
			
			//setClaimedAmountFields(docAck,true,rodDTO);
		} 
//		else if (null != rodDTO.getRodqueryDTO()
//				&& null != rodDTO.getRodqueryDTO().getReimbursementKey()) 
//		{
//			docAck.setRodKey(rodDTO.getRodqueryDTO().getReimbursementKey());
//			isQueryReplyReceived = true;
//			
//		}
		else if(null != rodDTO.getRodqueryDTO() && rodDTO.getRodqueryDTO().getReimbursementKey() != null){
			
			isQueryReplyReceived = true;
			RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
			rodKey = rodQueryDetailsDTO.getReimbursementKey();
			docAck.setRodKey(rodKey);
			docAck.setHospitalisationFlag(rodQueryDetailsDTO.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(rodQueryDetailsDTO.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(rodQueryDetailsDTO.getPostHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(rodQueryDetailsDTO.getPartialHospitalizationFlag());
			docAck.setLumpsumAmountFlag(rodQueryDetailsDTO.getAddOnBenefitsLumpsumFlag());
			docAck.setHospitalCashFlag(rodQueryDetailsDTO.getAddOnBeneftisHospitalCashFlag());
			docAck.setPatientCareFlag(rodQueryDetailsDTO.getAddOnBenefitsPatientCareFlag());
			docAck.setHospitalizationRepeatFlag(rodQueryDetailsDTO.getHospitalizationRepeatFlag());
			docAck.setOtherBenefitsFlag(rodQueryDetailsDTO.getOtherBenefitsFlag());
			docAck.setPaymentCancellationFlag("N");
			if(null != rodQueryDetailsDTO.getBenefitFlag() && (SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetailsDTO.getBenefitFlag()))){
				docAck.setCompassionateTravel(rodQueryDetailsDTO.getCompassionateTravelFlag());
				docAck.setEmergencyMedicalEvaluation(rodQueryDetailsDTO.getEmergencyMedicalEvaluationFlag());
				docAck.setRepatriationOfMortalRemain(rodQueryDetailsDTO.getRepatriationOfMortalRemainsFlag());
				docAck.setSharedAccomodation(rodQueryDetailsDTO.getSharedAccomodationFlag());
				docAck.setPreferredNetworkHospita(rodQueryDetailsDTO.getPreferredNetworkHospitalFlag());
			}
			
			if(null != rodQueryDetailsDTO.getReimbursementQueryKey())
			{
//				ReimbursementQuery reimbursmentQuery =	getReimbursementQueryData(rodQueryDetailsDTO.getReimbursementQueryKey());
//				reimbursmentQuery.setQueryReply(SHAConstants.YES_FLAG);
//			
//				if(null != reimbursmentQuery)
//				{
////					reimbursmentQuery.setQueryReplyDate((new Timestamp(System
////							.currentTimeMillis())));
//					if(null != rodDTO)
//					{
////						if(reimbursmentQuery.getStage() != null && reimbursmentQuery.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
////							Status status = new Status();
////							status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
////							reimbursmentQuery.setStatus(status);
////						}else{
////							Status status = new Status();
////							status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
////							reimbursmentQuery.setStatus(status);
////						}
//						reimbursmentQuery.setModifiedBy(rodDTO.getStrUserName());
//						reimbursmentQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//					}
//					entityManager.merge(reimbursmentQuery);
//					entityManager.flush();
//					log.info("------ReimbursementQuery------>"+reimbursmentQuery+"<------------");
//
//				}
			}
		}
		
		docAck.setActiveStatus(1l);
		docAck.setClaim(searchByClaimKey(rodDTO.getClaimDTO().getKey()));
		docAck.setAcknowledgeNumber(rodDTO.getDocumentDetails()
				.getAcknowledgementNumber());
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		docAck.setCreatedBy(userNameForDB);
		
		/**
		 * As per DB Team(Prakash) suggestion, Updating claim object before acknowledgement creation,
		 * since the status updated by the trigger is getting
		 * overridden by the application in claim table.
		 * 
		 * **/
		
		Claim claimObj = docAck.getClaim();
		if(null != claimObj)
		{
			Status status = new Status();
			status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			
			if(!((null != docAck && null != docAck.getReconsiderationRequest() && ("Y").equalsIgnoreCase(docAck.getReconsiderationRequest())) || isQueryReplyReceived))
			{
				if(null != docAck && null != docAck.getClaim() && null != docAck.getClaim().getClaimType() && docAck.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
					&& null != docAck.getHospitalisationFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getHospitalisationFlag()))
				{
					//claimObj.set
					updateProvisionAndClaimStatus(docAck, claimObj,false, rodDTO);
				}
				else if(rodDTO.getIsConversionAllowed() != null && rodDTO.getIsConversionAllowed())
				{
					if(claimObj.getStatus() != null && claimObj.getStatus().getKey().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS))
					{
						updateProvisionAndClaimStatus(docAck, claimObj,false, rodDTO);
					}

					
					/*Status claimStatus = new Status();
					claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
					claimObj.setStatus(claimStatus);*/
					
					/**
					 * For those cases where ack is created for claim
					 * which got converted via, convert to reimbursement
					 * process, claim registered date should not be
					 * updated. Hence the below condition of null check
					 * is added.
					 * */
					/**
					 * 
					 * Only if the claim is not registered, then the registration
					 * date would be null and hence we update the status and date. If this 
					 * is not null, then claim is already registered and this is a 
					 * conversion case.
					 * */
					
					if(null == claimObj.getClaimRegisteredDate())
					{
						claimObj.setClaimRegisteredDate((new Timestamp(System
								.currentTimeMillis())));
						
						Status claimStatus = new Status();
						claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
						// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
						SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
						if(sectionDetailsDTO != null) {
							claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
							claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
							claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
						}
						claimObj.setStatus(claimStatus);
						/*
						 * Modified by user updation code is commented, since
						 * modified user id and created user id would be
						 * diff in this scenario. This is not required. Hence
						 * commenting based on prakash inputs.
						 * **/
						/*if(rodDTO != null){
							claimObj.setModifiedBy(rodDTO.getStrUserName());
						}*/
						claimObj.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
					}
					
					/*
					if(null != rodDTO)
					{
						if(null != rodDTO)
						claimObj.setModifiedBy(rodDTO.getStrUserName());
						claimObj.setModifiedDate(new Timestamp(System
							.currentTimeMillis()));
					}*/
					
					SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
					if(sectionDetailsDTO != null) {
						claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
						claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
						claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
					}
					entityManager.merge(claimObj);
					entityManager.flush();
					log.info("------Claim------>"+claimObj+"<------------");

				}
				/**
				 * Added for lumpsum change.
				 * */
				else if (("N").equalsIgnoreCase(docAck.getHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPreHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck.getPostHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalCashFlag()) && ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
						&& ("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
				{
						updateProvisionAndClaimStatus(docAck, claimObj,true, rodDTO);	
				}
				
				//Added for Star hospital cash Product
				else if(null != docAck.getProdHospBenefitFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getProdHospBenefitFlag()))
				{
					updateProvisionAndClaimStatus(docAck, claimObj,true, rodDTO);	
				}
				
			}
			
			/**
			 * As per sathish sir suggestion,
			 * in all cases where claim registered 
			 * date is null, then only claim registered date 
			 * will be updated. Else it won't get updated.
			 * 
			 * */
			
			if(null == claimObj.getClaimRegisteredDate())
			{
				claimObj.setClaimRegisteredDate((new Timestamp(System
						.currentTimeMillis())));
				Status claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				claimObj.setStatus(claimStatus);
				
				
				/*if(rodDTO != null){
					claimObj.setModifiedBy(rodDTO.getStrUserName());
				}*/
				claimObj.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));
				
				SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
				if(sectionDetailsDTO != null) {
					claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
					claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
					claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
				}
				
				/*Mail - Document Received date null cases setting system date */
                if(null == claimObj.getDocumentReceivedDate()){
                        if(claimObj.getKey() != null){
                                DocAcknowledgement docAckeResult = null;
                                Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckByClaim");
                                query = query.setParameter("claimkey", claimObj.getKey());
                                List<DocAcknowledgement> docAckList = query.getResultList();
                                if(null != docAckList && !docAckList.isEmpty())
                                        {
                                        docAckeResult = (DocAcknowledgement) docAckList
                                                        .get(0);
                                        }
                                if(docAckeResult != null && docAckeResult.getCreatedDate() != null){
                                        claimObj.setDocumentReceivedDate(docAckeResult.getCreatedDate());
                                } else {
                                        claimObj.setDocumentReceivedDate(new Timestamp(System
                                                        .currentTimeMillis()));
                                }
                        }

                }
				
				entityManager.merge(claimObj);
				entityManager.flush();
			}
			
//			claimObj.setStatus(status);
//			claimObj.setStage(stage);
//			claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(claimObj);
//			entityManager.flush();
			
		}
		
		
		entityManager.persist(docAck);
		entityManager.flush();
		log.info("------DocAcknowledgement------>"+docAck.getAcknowledgeNumber()+"<------------");
		entityManager.refresh(docAck);
		
		/**
		 * If reconsideration request is selected, then
		 * the current acknowledgement needs to be updated in
		 * reimbursement table.
		 * */
		if(null != rodKey)
		{
			/**
			 * Not reconsideration case
			 */
		/*	if (! (null != rodDTO.getReconsiderRODdto()
					&& null != rodDTO.getReconsiderRODdto().getRodKey())) {*/
				
			Reimbursement reimbursement = getReimbursement(rodKey);
			
//			if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
//				Status status = new Status();
//				status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
//				reimbursement.setStatus(status);
//			}else if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
//				Status status = new Status();
//				status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
//				reimbursement.setStatus(status);
//			}

				String userName = rodDTO.getStrUserName();
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setModifiedBy(userName);
				reimbursement.setDocAcknowLedgement(docAck);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
				entityManager.flush();
				log.info("------Reimbursement------>"+reimbursement+"<------------");
//			}
			
			
		}
		
//		if(isQueryReplyReceived)
//		{
//			RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
//			ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO.getReimbursementQueryKey());
//			if(("No").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
//			{
//				reimbQuery.setQueryReply("N");
//			}
//			else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
//			{
//				reimbQuery.setQueryReply("Y");
//			}
//			reimbQuery.setDocAcknowledgement(docAck);
//			if(null != reimbQuery.getKey())
//			{
//				entityManager.merge(reimbQuery);
//				entityManager.flush();
//			}
//		}else 
		
		Boolean isQueryStatusYes = false;
		
		if(isQueryReplyReceived){
			
			List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO.getRodQueryDetailsList();
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
				ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2.getReimbursementQueryKey());
				if(("No").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("N");
				}
				else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
				{
					reimbQuery.setQueryReply("Y");
					reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
					rodDTO.setCreatedByForQuery(reimbQuery.getCreatedBy());
					isQueryStatusYes = true;
				}
				reimbQuery.setDocAcknowledgement(docAck);
				if(null != reimbQuery.getKey())
				{
					if(null != rodDTO)
					{
						reimbQuery.setModifiedBy(rodDTO.getStrUserName());
						reimbQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
					entityManager.merge(reimbQuery);
					entityManager.flush();
					log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
				}
			}
		}

		List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
				.getDocumentCheckList();
		if (!docCheckList.isEmpty()) {
			for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
				// if(null != docCheckListDTO.getNoOfDocuments())
				if (null != docCheckListDTO.getReceivedStatus()
						&& !("").equalsIgnoreCase(docCheckListDTO
								.getReceivedStatus().getValue())) {
					/*
					 * if(null != docCheckListDTO.getDocChkLstKey()) {
					 * RODDocumentCheckList documentDetails =
					 * findRODDocumentCheckListByKey
					 * (docCheckListDTO.getDocChkLstKey());
					 * entityManager.merge(documentDetails);
					 * entityManager.flush(); } else
					 */
					// {
					RODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
							.getRODDocumentCheckList(docCheckListDTO);
					rodDocumentCheckList.setDocAcknowledgement(docAck);
					// findRODDocumentCheckListByKey(masterService);
					entityManager.persist(rodDocumentCheckList);
					entityManager.flush();
					log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
					// }
				}
			}
		}
		
	/*	Claim claimObj = docAck.getClaim();
		if(null != claimObj)
		{
			Status status = new Status();
			status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			
			if(!((null != docAck && null != docAck.getReconsiderationRequest() && ("Y").equalsIgnoreCase(docAck.getReconsiderationRequest())) || isQueryReplyReceived))
			{
				if(null != docAck && null != docAck.getClaim() && null != docAck.getClaim().getClaimType() && docAck.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
					&& null != docAck.getHospitalisationFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getHospitalisationFlag()))
				{
					updateProvisionAndClaimStatus(docAck, claimObj);
				}
				else if(rodDTO.getIsConversionAllowed() != null && rodDTO.getIsConversionAllowed())
				{
					if(claimObj.getStatus().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS))
					{
						updateProvisionAndClaimStatus(docAck, claimObj);
					}

					Status claimStatus = new Status();
					claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
					claimObj.setStatus(claimStatus);
					
					claimObj.setClaimRegisteredDate((new Timestamp(System
							.currentTimeMillis())));
					
					if(null != rodDTO)
					{
						if(null != rodDTO)
						claimObj.setModifiedBy(rodDTO.getStrUserName());
						claimObj.setModifiedDate(new Timestamp(System
							.currentTimeMillis()));
					}
					entityManager.merge(claimObj);
					entityManager.flush();
					log.info("------Claim------>"+claimObj+"<------------");

				}
			}
			
//			claimObj.setStatus(status);
//			claimObj.setStage(stage);
//			claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(claimObj);
//			entityManager.flush();		
		}*/
		if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
		{
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber",docAck.getClaim().getIntimation().getIntimationId());
			Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
			if(null != objClaim)
			{
				dataMap.put("claimNumber",objClaim.getClaimId());
				if(null != objClaim.getClaimType())
				{
					if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
						{
							Preauth preauth = getPreauthClaimKey( objClaim.getKey());
							if(null != preauth)
								dataMap.put("cashlessNumber", preauth.getPreauthId());
						}
				}
			}
			dataMap.put("filePath", rodDTO.getDocFilePath());
			dataMap.put("docType", rodDTO.getDocType());
			dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			dataMap.put("createdBy", rodDTO.getStrUserName());
			SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
			SHAUtils.setClearReferenceData(dataMap);
		}
		//submitTaskToBPM(rodDTO, docAck, isQueryStatusYes, isReconsideration);
		
		submitTaskToDB(rodDTO,docAck,isQueryStatusYes, isReconsideration);
	}

	@SuppressWarnings("unchecked")
	public Preauth getPreauthClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();

		if (preauthList != null && !preauthList.isEmpty()) {
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}

	private void updateProvisionAndClaimStatus(DocAcknowledgement docAck,
			Claim claimObj, Boolean isLumpSumOnly, ReceiptOfDocumentsDTO rodDTO) {
	/*	if (!isLumpSumOnly) {
			Double totalClaimedAmt = 0d;
			if (null != docAck.getHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getHospitalizationClaimedAmount();
			}
			if (null != docAck.getPreHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getPreHospitalizationClaimedAmount();
			}

			if (null != docAck.getPostHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getPostHospitalizationClaimedAmount();
			}

			DBCalculationService dbCalculationService = new DBCalculationService();
			if (null != docAck.getClaim()
					&& null != docAck.getClaim().getIntimation()
					&& null != docAck.getClaim().getIntimation().getInsured()
					&& null != docAck.getClaim().getIntimation().getInsured()
							.getInsuredId()
					&& null != docAck.getClaim().getIntimation().getPolicy()
							.getKey()) {
				Double insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(docAck.getClaim().getIntimation()
								.getInsured().getInsuredId().toString(), docAck
								.getClaim().getIntimation().getPolicy()
								.getKey(),docAck.getClaim().getIntimation()
								.getInsured().getLopFlag());
				Double balSI = 0d;
				if(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
					balSI = dbCalculationService
							.getBalanceSIForGMC(
									docAck.getClaim().getIntimation().getPolicy()
											.getKey(),
									docAck.getClaim().getIntimation().getInsured()
											.getKey(), docAck.getClaim().getKey());
				}else{
					balSI = dbCalculationService
							.getBalanceSI(
									docAck.getClaim().getIntimation().getPolicy()
											.getKey(),
									docAck.getClaim().getIntimation().getInsured()
											.getKey(), docAck.getClaim().getKey(),
									insuredSumInsured,
									docAck.getClaim().getIntimation().getKey())
							.get(SHAConstants.TOTAL_BALANCE_SI);
				}
				//Double amt = Math.min(balSI, totalClaimedAmt);

				
				//claimObj.setCurrentProvisionAmount(amt);
				//claimObj.setClaimedAmount(amt);
				//claimObj.setProvisionAmount(amt);
			}
		} else if (isLumpSumOnly) {
			DBCalculationService dbCalculationService = new DBCalculationService();
			Long productKey = 0l;
			if (null != claimObj.getIntimation().getPolicy().getProduct())
				productKey = claimObj.getIntimation().getPolicy().getProduct()
						.getKey();
			Double lumpSumAmt = dbCalculationService.getLumpSumAmount(
					productKey, docAck.getClaim().getKey(),
					ReferenceTable.LUMPSUM_SUB_COVER_CODE);
			if (null != claimObj) {
				//claimObj.setCurrentProvisionAmount(lumpSumAmt);
				//claimObj.setClaimedAmount(lumpSumAmt);
				//claimObj.setProvisionAmount(lumpSumAmt);
			}
		}*/

		/*
		 * Status claimStatus = new Status();
		 * claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
		 * claimObj.setStatus(claimStatus);
		 */
		/**
		 * As per the rule, Lumpsum is going to be first and last rod for an
		 * intimation. Hence for first rod, this below validation will happen
		 * irrespective of any classification.
		 * 
		 * */
		if (null == claimObj.getClaimRegisteredDate()) {
			Status claimStatus = new Status();
			claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimObj.setStatus(claimStatus);

			claimObj.setClaimRegisteredDate((new Timestamp(System
					.currentTimeMillis())));

			claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		}
		claimObj.setNormalClaimFlag("N");
		claimObj.setDocumentReceivedDate(new Timestamp(System
				.currentTimeMillis()));

		SectionDetailsTableDTO sectionDetailsDTO = rodDTO
				.getSectionDetailsDTO();
		if (sectionDetailsDTO != null) {
			claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO
					.getSection().getCommonValue() : null);
			claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
					.getCover().getCommonValue() : null);
			claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO
					.getSubCover().getCommonValue() : null);
		}

		entityManager.merge(claimObj);
		entityManager.flush();
		log.info("------Claim------>" + claimObj + "<------------");
	}



	/*
	 * public void uploadGeneratedLetterToDMS(EntityManager
	 * entityManager,HashMap dataMap) { HashMap fileUploadMap =
	 * SHAUtils.uploadFileToDMS((String)dataMap.get("filePath")); if(null !=
	 * fileUploadMap && !fileUploadMap.isEmpty()) { String docToken =
	 * (String)fileUploadMap.get("fileKey"); String fileName =
	 * (String)fileUploadMap.get("fileName"); dataMap.put("fileKey", docToken);
	 * dataMap.put("fileName", fileName); dataMap.put("fileSize",
	 * (Long)fileUploadMap.get("fileSize"));
	 * populateDocumentDetailsObject(entityManager , dataMap); } }
	 * 
	 * public void populateDocumentDetailsObject(EntityManager
	 * entityManager,HashMap fileUploadMap) { DocumentDetails docDetails = new
	 * DocumentDetails(); String docToken =
	 * (String)fileUploadMap.get("fileKey"); docDetails.setDocumentToken((null
	 * != docToken && !docToken.isEmpty())? Long.parseLong(docToken):null);
	 * docDetails
	 * .setIntimationNumber((String)fileUploadMap.get("intimationNumber"));
	 * docDetails.setClaimNumber((String)fileUploadMap.get("claimNumber"));
	 * docDetails
	 * .setCashlessNumber((String)fileUploadMap.get("cashlessNumber"));
	 * docDetails.setDocumentType((String)fileUploadMap.get("docType"));
	 * docDetails.setDocumentSource((String)fileUploadMap.get("docSource"));
	 * docDetails.setSfFileSize((Long)fileUploadMap.get("fileSize"));
	 * docDetails.setSfFileName((String)fileUploadMap.get("fileName"));
	 * docDetails.setDocSubmittedDate((new Timestamp(System
	 * .currentTimeMillis()))); docDetails.setDocAcknowledgementDate((new
	 * Timestamp(System .currentTimeMillis())));
	 * entityManager.persist(docDetails); entityManager.flush(); }
	 */

	/**
	 * Commenting temporarily.
	 * */

	/*
	 * private void setClaimedAmountFields(DocAcknowledgement docAck , Boolean
	 * isReconsiderationRequest, ReceiptOfDocumentsDTO rodDTO) {
	 * 
	 * if(isReconsiderationRequest) {
	 * 
	 * } else {
	 * 
	 * } }
	 */
	
	public void submitTaskFromConvertToROD(ReceiptOfDocumentsDTO rodDTO,
			DocAcknowledgement docAck, Boolean isQueryReply,
			Boolean isReconsideration,EntityManager entityManager){
		this.entityManager = entityManager;
		
//		submitTaskToBPM(rodDTO, docAck, isQueryReply, isReconsideration);
		
		submitTaskToDB(rodDTO, docAck, isQueryReply, isReconsideration);
		
	}
	
/*	
=======
			if(null == claimObj.getClaimRegisteredDate())
			{
				Status claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				claimObj.setStatus(claimStatus);
				
				claimObj.setClaimRegisteredDate((new Timestamp(System
					.currentTimeMillis())));
				
				claimObj.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));
			}
			claimObj.setNormalClaimFlag("N");
			claimObj.setDocumentReceivedDate(new Timestamp(System.currentTimeMillis()));
		
		
		SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
		if(sectionDetailsDTO != null) {
			claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
			claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
			claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
		}	
		entityManager.merge(claimObj);
		entityManager.flush();
		log.info("------Claim------>"+claimObj+"<------------");
	}*/
	
		private void updatePAProvisionAndClaimStatus(DocAcknowledgement docAck, Claim claimObj, ReceiptOfDocumentsDTO rodDTO)
	{
		/*
			Double totalClaimedAmt = 0d;
			if(null != docAck.getBenifitClaimedAmount())
			{
				totalClaimedAmt += docAck.getBenifitClaimedAmount();
			}

			
			DBCalculationService dbCalculationService = new DBCalculationService();
			if(null != docAck.getClaim() && null != docAck.getClaim().getIntimation() && null !=  docAck.getClaim().getIntimation().getInsured() && null != docAck.getClaim().getIntimation().getInsured().getInsuredId() && null != docAck.getClaim().getIntimation().getPolicy().getKey())
			{
				MastersValue masterValue = getMasterKeyBasedOnMappingCode(docAck.getBenifitFlag());
				Double calculatedBenefitsAmt = 0d;
				Double balSI = 0d;
				if((null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization()) || (null != rodDTO.getDocumentDetails().getPartialHospitalization() && null != rodDTO.getDocumentDetails().getPartialHospitalization()))
				{
					 Map<String, Double> valuesMap = dbCalculationService.getBalanceSIForPAHealth(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey());
					 if(null != valuesMap)
					 {	 
						 balSI = (Double) valuesMap.get(SHAConstants.TOTAL_BALANCE_SI);
					 }
				}
				else
				{
					balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 0l ,null != masterValue ? masterValue.getKey() : 0l);
				}
				calculatedBenefitsAmt = Math.min(balSI,totalClaimedAmt);
				Double addOnCoversAmt = 0d;
				Double optionalCoversAmt = 0d;
				
				if(!(null != ReferenceTable.getTraumaCareValues() && null != ReferenceTable.getTraumaCareValues().get(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())))
			{
				List<AddOnCoversTableDTO> addOnCoversTableList = rodDTO.getDocumentDetails().getAddOnCoversList();
				if(null != addOnCoversTableList && !addOnCoversTableList.isEmpty())
				{
					List<AddOnCoversTableDTO> addOnCoversProcedureList = dbCalculationService.getClaimCoverValues(
							SHAConstants.ADDITIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
							docAck.getClaim().getIntimation().getInsured().getKey());
					
					for (AddOnCoversTableDTO addOnCoversTableDTOObj : addOnCoversTableList) {
						for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversProcedureList) {
							if(null != addOnCoversTableDTOObj.getCovers() && addOnCoversTableDTO.getCoverId().equals(addOnCoversTableDTOObj.getCovers().getId()))
							{
								if(null != addOnCoversTableDTOObj.getClaimedAmount())
								{
								addOnCoversAmt += Math.min(addOnCoversTableDTO.getClaimedAmount() , addOnCoversTableDTOObj.getClaimedAmount());
								}
								break;
							
							}
						}
					}
				}
					
					List<AddOnCoversTableDTO> optionalCoversList = rodDTO.getDocumentDetails().getOptionalCoversList();
					if(null != optionalCoversList && !optionalCoversList.isEmpty())
					{
						List<AddOnCoversTableDTO> optionalCoversProcedureList = dbCalculationService.getClaimCoverValues(
								SHAConstants.OPTIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
								docAck.getClaim().getIntimation().getInsured().getKey());
						for (AddOnCoversTableDTO addOnCoversTableDTOObj : optionalCoversList) {
							
							if(null !=  addOnCoversTableDTOObj.getClaimedAmount())
							{
							optionalCoversAmt +=  addOnCoversTableDTOObj.getClaimedAmount();
							}
							for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversProcedureList) {
								if(null != addOnCoversTableDTOObj.getOptionalCover() && addOnCoversTableDTO.getCoverId().equals(addOnCoversTableDTOObj.getOptionalCover().getId()))
								{
									if(null != addOnCoversTableDTOObj.getClaimedAmount())
									{
									optionalCoversAmt += Math.min(addOnCoversTableDTO.getClaimedAmount() , addOnCoversTableDTOObj.getClaimedAmount());
									}
									break;
									
								}
							}
						}
					}
				}
				*//**
				 * When sending provision to premia, sum of benefits, add covers and optional covers
				 * needs to sent.
				 * 
				 * But while saving at claim, only benefits amount to be saved. 
				 * 
				 * After discussion with srikanth sir, this implementation has be done.
				 * *//*
				Double provisionToPremia = calculatedBenefitsAmt + addOnCoversAmt + optionalCoversAmt;
				rodDTO.setPaProvisionAmtToPremia(provisionToPremia);
				Double amt = calculatedBenefitsAmt;
				claimObj.setCurrentProvisionAmount(amt);
				claimObj.setClaimedAmount(amt);
				claimObj.setProvisionAmount(amt);
			}*/

			/*Status claimStatus = new Status();
			claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimObj.setStatus(claimStatus);*/
			
		/**
		 * As per the rule, Lumpsum is going to be first and last rod for an intimation.
		 * Hence for first rod, this below validation will happen irrespective of
		 * any classification.
		 * 
		 * */
			if(null == claimObj.getClaimRegisteredDate())
			{
				Status claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				claimObj.setStatus(claimStatus);
				
				claimObj.setClaimRegisteredDate((new Timestamp(System
					.currentTimeMillis())));
				
				claimObj.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));
			}
			claimObj.setNormalClaimFlag("N");
			claimObj.setDocumentReceivedDate(new Timestamp(System.currentTimeMillis()));
		
		
		SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
		if(sectionDetailsDTO != null) {
			claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
			claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
			claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
		}	
		entityManager.merge(claimObj);
		entityManager.flush();
		log.info("------Claim------>"+claimObj+"<------------");
	}
	
	
	private ReimbursementQuery getReimbursementQueryData(Long queryKey)
	{
		Query query = entityManager.createNamedQuery("ReimbursementQuery.findByKey");
		query = query.setParameter("primaryKey", queryKey);
		List<ReimbursementQuery> reimbursementQueryList = query.getResultList();
		if(null != reimbursementQueryList && !reimbursementQueryList.isEmpty())
		{
			entityManager.refresh(reimbursementQueryList.get(0));
			return reimbursementQueryList.get(0);
		}
		return null;
	}
	
	/*public void uploadGeneratedLetterToDMS(EntityManager entityManager,HashMap dataMap)
	{
		HashMap fileUploadMap = SHAUtils.uploadFileToDMS((String)dataMap.get("filePath"));
		if(null != fileUploadMap && !fileUploadMap.isEmpty())
		{
			String docToken = (String)fileUploadMap.get("fileKey");
			String fileName = (String)fileUploadMap.get("fileName");
			dataMap.put("fileKey", docToken);
			dataMap.put("fileName", fileName);
			dataMap.put("fileSize", (Long)fileUploadMap.get("fileSize"));
			populateDocumentDetailsObject(entityManager , dataMap);
		}
	}
	
	public void populateDocumentDetailsObject(EntityManager entityManager,HashMap fileUploadMap)
	{
		DocumentDetails docDetails = new DocumentDetails();
		String docToken = (String)fileUploadMap.get("fileKey");
		docDetails.setDocumentToken((null != docToken && !docToken.isEmpty())? Long.parseLong(docToken):null);
		docDetails.setIntimationNumber((String)fileUploadMap.get("intimationNumber"));
		docDetails.setClaimNumber((String)fileUploadMap.get("claimNumber"));
		docDetails.setCashlessNumber((String)fileUploadMap.get("cashlessNumber"));
		docDetails.setDocumentType((String)fileUploadMap.get("docType"));
		docDetails.setDocumentSource((String)fileUploadMap.get("docSource"));
		docDetails.setSfFileSize((Long)fileUploadMap.get("fileSize"));
		docDetails.setSfFileName((String)fileUploadMap.get("fileName"));
		docDetails.setDocSubmittedDate((new Timestamp(System
				.currentTimeMillis())));
		docDetails.setDocAcknowledgementDate((new Timestamp(System
				.currentTimeMillis())));
		entityManager.persist(docDetails);
		entityManager.flush();
	}*/
	
	/**
	 * Commenting temporarily.
	 * */
	
/*	private void setClaimedAmountFields(DocAcknowledgement docAck , Boolean isReconsiderationRequest, ReceiptOfDocumentsDTO rodDTO)
	{
		
		if(isReconsiderationRequest)
		{
			
		}
		else
		{
			
		}
	}
*/
	public void submitPATaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			DocAcknowledgement docAck,Boolean isQueryReply,Boolean isReconsideration) {/*
		Acknowledgement acknowledgemnetTask = BPMClientContext
				.getAcknowledgementTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		InitiateAckProcessPayloadType payload = new InitiateAckProcessPayloadType();

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(rodDTO.getClaimDTO()
				.getNewIntimationDto().getIntimationId());
		
		intimationType.setIntimationSource(rodDTO.getClaimDTO().getNewIntimationDto().getIntimationSource().getValue());
		if(null != rodDTO.getDocumentDetails().getAccidentOrDeath() && rodDTO.getDocumentDetails().getAccidentOrDeath())
		{
			intimationType.setReason(SHAConstants.ACCIDENT_FLAG);
		}
		else
		{
			intimationType.setReason(SHAConstants.DEATH_FLAG);
		}
		
		
		if(rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate() != null){
			String intimDate = SHAUtils.formatIntimationDateValue(rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate());
			intimationType.setStatus(intimDate);
		}
		
		

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getPolicyNumber());
		
		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");
		
		productInfo.setLobType(docAck.getProcessClaimType());
		productInfo.setLob(SHAConstants.PA_LOB);
		
		
		if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null){
			productInfo.setProductId(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().toString());
		}
		
		
		productInfo.setProductName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getValue());

		*//**
		 * @author yosuva.a
		 *//*
		ClaimType claim = new ClaimType();
		claim.setClaimId(docAck.getClaim().getClaimId());
		claim.setKey(docAck.getClaim().getKey());
		if (docAck.getClaim().getClaimType() != null) {
			claim.setClaimType(docAck.getClaim().getClaimType().getValue() != null ? docAck.getClaim().getClaimType().getValue().toString().toUpperCase() : "REIMBURSEMENT");
		}

		ClaimRequestType claimRequest = new ClaimRequestType();
		
		claimRequest.setKey(rodDTO.getClaimDTO().getKey());
		claimRequest.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto()
				.getCpuCode());
		claimRequest.setClaimRequestType("All");
		claimRequest.setClientType("ACK");
		
		
		*//**
		 * Added for pa claims
		 * *//*
		
		if(null != docAck && null != docAck.getClaim() && null != docAck.getClaim().getIntimation() )
		{
			Intimation intimation = docAck.getClaim().getIntimation();
			if(null != intimation.getLobId())
			{
				if(ReferenceTable.PA_LOB_KEY.equals(intimation.getLobId().getKey()))
				{
					//intimationType.setReason(intimation.getIncidenceFlag()); // will be A or D. A-accident and D-Death.
				//	intimationType.setReason(rodDTO.getClaimDTO().getIncidenceFlagValue());
					//a_message.setIsBalanceSIAvailable(false);
					productInfo.setLob(SHAConstants.PA_LOB);
					productInfo.setLobType(SHAConstants.PA_LOB_TYPE);
				}
				*//**
				 * The below if condition is not required, as this service is exclusively
				 * used for PA alone. So directly we harcod the lob as PA.
				 * *//*
				if((ReferenceTable.HEALTH_LOB_KEY).equals(intimation.getLobId().getKey()))
				{
					productInfo.setLob(SHAConstants.HEALTH_LOB_TYPE);
					productInfo.setLobType("H");
				}
				if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey()))
						{
							productInfo.setLob(SHAConstants.PACKAGE_LOB_TYPE);
							productInfo.setLobType(SHAConstants.PACKAGE_LOB_TYPE);
						}
			}
		}
		
		if((null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization()) || (null != rodDTO.getDocumentDetails().getPartialHospitalization() && rodDTO.getDocumentDetails().getPartialHospitalization())){
			productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
		}
			
		DocReceiptACKType docReceiptAck = new DocReceiptACKType();
		docReceiptAck.setAckNumber(docAck.getAcknowledgeNumber());
		docReceiptAck.setKey(docAck.getKey());
		
		if(null != rodDTO.getIsConversionAllowed() && rodDTO.getIsConversionAllowed())
		{
			docReceiptAck.setStatus(SHAConstants.PRE_AUTH_STATUS_FOR_CONVERSION);
		}
		else
		{
			docReceiptAck.setStatus(docAck.getStatus().getProcessValue());
		}
		if (("Y").equalsIgnoreCase(docAck.getHospitalisationFlag()))
			docReceiptAck.setHospitalization(true);
		//if (("Y").equalsIgnoreCase(docAck.getPostHospitalisationFlag()))
			docReceiptAck.setPosthospitalization(false);
		//if (("Y").equalsIgnoreCase(docAck.getPreHospitalisationFlag()))
			docReceiptAck.setPrehospitalization(false);
		if (("Y").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()))
			docReceiptAck.setPartialhospitalization(true);
		//if (("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
			docReceiptAck.setLumpsumamount(false);
		//if (("Y").equalsIgnoreCase(docAck.getHospitalCashFlag()))
			docReceiptAck.setAddonbenefitshospcash(false);
		//if (("Y").equalsIgnoreCase(docAck.getPatientCareFlag()))
			docReceiptAck.setAddonbenefitspatientcare(false);
		//if (("Y").equalsIgnoreCase(docAck.getHospitalizationRepeatFlag()))
		{
			*//**
			 * Instead of below line, add 
			 * docReceiptAck.setPartialhospitalization(true);
			 * *//*
			*//***
			 * Below line added for hospitalization repeat flow.
			 * Repeat should follow normal flow from create rod, bill entry
			 * zmr. 
			 *//*
			docReceiptAck.setPartialhospitalization(false);
			
			//docReceiptAck.setHospitalization(true);
		}

		HospitalInfoType hospInfoType = new HospitalInfoType();
		
		String hospitalType = rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue();
		if(null != hospitalType && (SHAConstants.NETWORK_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType))
		{
			hospInfoType.setHospitalType("NET");
		}
		else if(null != hospitalType && ("Non-Network").equalsIgnoreCase(hospitalType))
		{
			hospInfoType.setHospitalType("NONNT");
		}
		else if(null != hospitalType && (SHAConstants.NOT_REGISTERED_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType))
		{
			hospInfoType.setHospitalType("NOTREGISTERED");
		}
		hospInfoType.setHospitalType(hospitalType);
		
		hospInfoType.setHospitalType(rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue());
		hospInfoType.setNetworkHospitalType(rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
		
		//ClaimRequestType claimRequestType = new ClaimRequestType(); 
//		/claimRequestType.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto().getCpuCode());
		
		Claim claimObj = entityManager.find(Claim.class, rodDTO.getClaimDTO().getKey());
			
		entityManager.refresh(claimObj);
			
		Insured insured = claimObj.getIntimation().getInsured();
			
		ClassificationType classificationType = new ClassificationType();
		if(claimObj != null && claimObj.getIsVipCustomer() != null && claimObj.getIsVipCustomer().equals(1l))
		{
				
			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		}
		else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		}else{
			classificationType.setPriority(SHAConstants.NORMAL);
		}
		
		if(isReconsideration){
			classificationType.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			claimRequest.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
			RODType rodType = new RODType();
			rodType.setKey(rodDTO.getReconsiderRODdto().getRodKey());
			payloadBO.setRod(rodType);
			classificationType.setSource(SHAConstants.RECONSIDERATION);
		}else if(isQueryReply){
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.QUERY_REPLY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser(rodDTO.getCreatedByForQuery());
			payloadBO.setProcessActorInfo(processActor);
			RODType rodType = new RODType();
			rodType.setKey(rodDTO.getRodqueryDTO().getReimbursementKey());
			payloadBO.setRod(rodType);
			classificationType.setSource(SHAConstants.QUERY_REPLY);
		}else{
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.TYPE_FRESH);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}
		
//		classificationType.setSource(SHAConstants.NORMAL);
		if(! isReconsideration && ! isQueryReply) {
			classificationType.setSource(docAck.getStatus().getProcessValue());
		}

		payloadBO.setIntimation(intimationType);
		payloadBO.setPolicy(policyType);
		payloadBO.setClaimRequest(claimRequest);  
		payloadBO.setDocReceiptACK(docReceiptAck);
		payloadBO.setClaim(claim);
		payloadBO.setHospitalInfo(hospInfoType);
		payloadBO.setProductInfo(productInfo);
		payloadBO.setClassification(classificationType);

	
		// payload.setPayloadBO(payloadBO);
		// acknowledgemnetTask.initiate(rodDTO.getStrUserName(), payloadBO,
		// "submit");
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO,
		// "submit",ReferenceTable.ACK_TASK_BPM_FORM_NAME);
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO);
		try{
		acknowledgemnetTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
		
//		CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,rodDTO.getStrPassword());
//		cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, payloadBO);

		}catch(Exception e){
			e.printStackTrace();
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				acknowledgemnetTask.initiate("claimshead", payloadBO);

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}

	*/}
	
/*	public void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			DocAcknowledgement docAck, Boolean isQueryReply,
			Boolean isReconsideration) {
		Acknowledgement acknowledgemnetTask = BPMClientContext
				.getAcknowledgementTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		InitiateAckProcessPayloadType payload = new InitiateAckProcessPayloadType();

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(rodDTO.getClaimDTO()
				.getNewIntimationDto().getIntimationId());
<<<<<<< HEAD

		intimationType.setIntimationSource(rodDTO.getClaimDTO()
				.getNewIntimationDto().getIntimationSource().getValue());

		if (rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate() != null) {
			String intimDate = SHAUtils.formatIntimationDateValue(rodDTO
					.getClaimDTO().getNewIntimationDto().getAdmissionDate());
=======
		
		intimationType.setIntimationSource(rodDTO.getClaimDTO().getNewIntimationDto().getIntimationSource().getValue());		
		
		
		
		if(rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate() != null){
			String intimDate = SHAUtils.formatIntimationDateValue(rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate());
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
			intimationType.setStatus(intimDate);
		}

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getPolicyNumber());

		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");
<<<<<<< HEAD

		if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
				.getKey() != null) {
			productInfo.setProductId(rodDTO.getClaimDTO().getNewIntimationDto()
					.getPolicy().getProduct().getKey().toString());
=======
		productInfo.setLobType("H");
		
		if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null){
			productInfo.setProductId(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().toString());
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		}

		productInfo.setProductName(rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getProduct().getValue());

		*//**
		 * @author yosuva.a
		 *//*
		ClaimType claim = new ClaimType();
		claim.setClaimId(docAck.getClaim().getClaimId());
		claim.setKey(docAck.getClaim().getKey());
		if (docAck.getClaim().getClaimType() != null) {
			claim.setClaimType(docAck.getClaim().getClaimType().getValue() != null ? docAck
					.getClaim().getClaimType().getValue().toString()
					.toUpperCase()
					: "REIMBURSEMENT");
		}

		ClaimRequestType claimRequest = new ClaimRequestType();

		claimRequest.setKey(rodDTO.getClaimDTO().getKey());
		claimRequest.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto()
				.getCpuCode());
		claimRequest.setClaimRequestType("All");
		claimRequest.setClientType("ACK");
<<<<<<< HEAD

=======
		
		
		*//**
		 * Added for pa claims
		 * *//*
		
		if(null != docAck && null != docAck.getClaim() && null != docAck.getClaim().getIntimation() )
		{
			Intimation intimation = docAck.getClaim().getIntimation();
			if(null != intimation.getLobId())
			{
				if(ReferenceTable.PA_MASTER_VALUE.equals(intimation.getLobId().getKey()))
				{
					intimationType.setReason(intimation.getIncidenceFlag()); // will be A or D. A-accident and D-Death.
					//a_message.setIsBalanceSIAvailable(false);
					productInfo.setLob(SHAConstants.PA_LOB);
					productInfo.setLobType(SHAConstants.PA_LOB_TYPE);
				}
				*//**
				 * The below if condition is not required, as this service is exclusively
				 * used for PA alone. So directly we harcod the lob as PA.
				 * *//*
				if((ReferenceTable.HEALTH_LOB_KEY).equals(intimation.getLobId().getKey()))
				{
					productInfo.setLob(SHAConstants.HEALTH_LOB_TYPE);
					productInfo.setLobType("H");
				}
				if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey()))
						{
							productInfo.setLob(SHAConstants.PACKAGE_LOB_TYPE);
							productInfo.setLobType(SHAConstants.PACKAGE_LOB_TYPE);
						}
			}
		}
		
			
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		DocReceiptACKType docReceiptAck = new DocReceiptACKType();
		docReceiptAck.setAckNumber(docAck.getAcknowledgeNumber());
		docReceiptAck.setKey(docAck.getKey());

		if (null != rodDTO.getIsConversionAllowed()
				&& rodDTO.getIsConversionAllowed()) {
			docReceiptAck
					.setStatus(SHAConstants.PRE_AUTH_STATUS_FOR_CONVERSION);
		} else {
			docReceiptAck.setStatus(docAck.getStatus().getProcessValue());
		}
		if (("Y").equalsIgnoreCase(docAck.getHospitalisationFlag()))
			docReceiptAck.setHospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPostHospitalisationFlag()))
			docReceiptAck.setPosthospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPreHospitalisationFlag()))
			docReceiptAck.setPrehospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()))
			docReceiptAck.setPartialhospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
			docReceiptAck.setLumpsumamount(true);
		if (("Y").equalsIgnoreCase(docAck.getHospitalCashFlag()))
			docReceiptAck.setAddonbenefitshospcash(true);
		if (("Y").equalsIgnoreCase(docAck.getPatientCareFlag()))
			docReceiptAck.setAddonbenefitspatientcare(true);
		if (("Y").equalsIgnoreCase(docAck.getHospitalizationRepeatFlag())) {
			*//**
			 * Instead of below line, add
			 * docReceiptAck.setPartialhospitalization(true);
			 * *//*
			*//***
			 * Below line added for hospitalization repeat flow. Repeat should
			 * follow normal flow from create rod, bill entry zmr.
			 *//*
			docReceiptAck.setPartialhospitalization(true);

			// docReceiptAck.setHospitalization(true);
		}

		HospitalInfoType hospInfoType = new HospitalInfoType();

		
		 * String hospitalType =
		 * rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto
		 * ().getRegistedHospitals().getHospitalType().getValue(); if(null !=
		 * hospitalType &&
		 * (SHAConstants.NETWORK_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType))
		 * { hospInfoType.setHospitalType("NET"); } else if(null != hospitalType
		 * && ("Non-Network").equalsIgnoreCase(hospitalType)) {
		 * hospInfoType.setHospitalType("NONNT"); } else if(null != hospitalType
		 * &&
		 * (SHAConstants.NOT_REGISTERED_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType
		 * )) { hospInfoType.setHospitalType("NOTREGISTERED"); }
		 * hospInfoType.setHospitalType(hospitalType);
		 

		hospInfoType.setHospitalType(rodDTO.getClaimDTO().getNewIntimationDto()
				.getHospitalDto().getRegistedHospitals().getHospitalType()
				.getValue());
		hospInfoType.setNetworkHospitalType(rodDTO.getClaimDTO()
				.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
				.getNetworkHospitalType());

		// ClaimRequestType claimRequestType = new ClaimRequestType();
		// /claimRequestType.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto().getCpuCode());

		Claim claimObj = entityManager.find(Claim.class, rodDTO.getClaimDTO()
				.getKey());

		entityManager.refresh(claimObj);

		Insured insured = claimObj.getIntimation().getInsured();

		ClassificationType classificationType = new ClassificationType();
		if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		} else {
			classificationType.setPriority(SHAConstants.NORMAL);
		}

		if (isReconsideration) {
			classificationType.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			claimRequest
					.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);

			classificationType.setSource(SHAConstants.RECONSIDERATION);
		} else if (isQueryReply) {
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.QUERY_REPLY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser(rodDTO.getCreatedByForQuery());
			payloadBO.setProcessActorInfo(processActor);
			classificationType.setSource(SHAConstants.QUERY_REPLY);
<<<<<<< HEAD
		} else {
=======
			stopQueryReminderLetter(payloadBO, rodDTO.getStrUserName(),rodDTO.getStrPassword());
			stopQueryReplyReminderProcess(payloadBO,rodDTO.getStrUserName(),rodDTO.getStrPassword());
		}else{
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.TYPE_FRESH);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}

		// classificationType.setSource(SHAConstants.NORMAL);
		if (!isReconsideration && !isQueryReply) {
			classificationType.setSource(docAck.getStatus().getProcessValue());
		}

		
		intimationType.setReason("H");
		
		payloadBO.setIntimation(intimationType);
		payloadBO.setPolicy(policyType);
		payloadBO.setClaimRequest(claimRequest);
		payloadBO.setDocReceiptACK(docReceiptAck);
		payloadBO.setClaim(claim);
		payloadBO.setHospitalInfo(hospInfoType);
		payloadBO.setProductInfo(productInfo);
		payloadBO.setClassification(classificationType);
		// payload.setPayloadBO(payloadBO);
		// acknowledgemnetTask.initiate(rodDTO.getStrUserName(), payloadBO,
		// "submit");
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO,
		// "submit",ReferenceTable.ACK_TASK_BPM_FORM_NAME);
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO);
		try {
			
			
			if (null != rodDTO.getIsConversionAllowed()
					&& rodDTO.getIsConversionAllowed()) {
				
				Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());
				
				
				Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claimObj, hospitals);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];

				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_CONVERSION;
				inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] = docAck.getKey().toString();
				
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				dbCalculationService.initiateTaskProcedure(parameter);
				
				
			}else{
				acknowledgemnetTask.initiate(BPMClientContext.BPMN_TASK_USER,
						payloadBO);
			}
			
			

			// CancelAcknowledgement cancelAcknowledgement =
			// BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,rodDTO.getStrPassword());
			// cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC,
			// payloadBO);

		} catch (Exception e) {
			e.printStackTrace();

			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#"
					+ e.toString());

			try {
				acknowledgemnetTask.initiate("claimshead", payloadBO);

			} catch (Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}

<<<<<<< HEAD
	}*/


	
	private Hospitals getHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);

		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}

	public Boolean isConversionAllowed(Preauth preauth) {
		Boolean allowConversion = false;
		Long valueForConversion = ReferenceTable.getConversionStatusMap().get(
				preauth.getStatus().getKey());
		if (null != valueForConversion) {
			allowConversion = true;
		} else if (null != preauth.getClaim()) {
			Long conversionVal = ReferenceTable.getConversionStatusMap().get(
					preauth.getStatus().getKey());
			if (null != conversionVal) {
				allowConversion = true;
			}
		}
		return allowConversion;
	}

	/*public Boolean getWaitingForPreauthTask(Claim objClaim) {
		Boolean isWaitingForPreauth = false;
		ReceivePreAuthTask receivePreauthTask = BPMClientContext
				.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
<<<<<<< HEAD
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask
				.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(),
						payloadBO);
=======
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if(null != humanTaskList && !humanTaskList.isEmpty())
		{
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj.getClaim();
				//String claimId = claimType.getClaimId();
				Long claimKey = claimType.getKey();
				if(objClaim.getKey().equals(claimKey))
				{
					isWaitingForPreauth = true;
					break;
				}
			}
		}
		return isWaitingForPreauth;
	}
	
	public Boolean getConvertClaimTask(Claim objClaim)
	{
		Boolean isWaitingForConversion = false;
		AckProcessConvertClaimToReimbTask ackProcessConvertClaim = BPMClientContext.getConvertClaimTaskFromAck(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType objIntimationType = new IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation().getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim.getTasks("claimshead", new Pageable(), payloadBO);
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
<<<<<<< HEAD
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
						.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj
						.getClaim();
				// String claimId = claimType.getClaimId();
				Long claimKey = claimType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForPreauth = true;
=======
				PayloadBOType payloadBOObj = humanTask.getPayload();
				ClaimType claimType = payloadBOObj.getClaim();
				Long claimKey = claimType.getKey();
				if(objClaim.getKey().equals(claimKey))
				{
					isWaitingForConversion = true;
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
					break;
				}
			}
		}
<<<<<<< HEAD
		return isWaitingForPreauth;
	}*/

/*	public Boolean getConvertClaimTask(Claim objClaim) {
=======
		return isWaitingForConversion;
		
		
	}
	
	
	public Boolean getPAConvertClaimTask(Claim objClaim)
	{
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		Boolean isWaitingForConversion = false;
		AckProcessConvertClaimToReimbTask ackProcessConvertClaim = BPMClientContext
				.getConvertClaimTaskFromAck(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType objIntimationType = new IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
<<<<<<< HEAD
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim
				.getTasks("claimshead", new Pageable(), payloadBO);
=======
		
		ProductInfoType productInfo = payloadBO.getProductInfo();
    	if(productInfo == null) {
    		productInfo = new ProductInfoType();
    	}
		productInfo.setLob(SHAConstants.PA_LOB);
		payloadBO.setProductInfo(productInfo);
		
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim.getTasks("claimshead", new Pageable(), payloadBO);
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
				PayloadBOType payloadBOObj = humanTask.getPayload();
				ClaimType claimType = payloadBOObj.getClaim();
				Long claimKey = claimType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForConversion = true;
					break;
				}
			}
		}
		return isWaitingForConversion;

	}*/
	
public Boolean getDBTaskForPreauth(Intimation intimation,String currentQ){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		// Issue fix Commented on 23/10/2020 jira IMSSUPPOR-34002 changed Gettask Procodure
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForBatch(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		return false;
	}
/*
	public Boolean getProcessRejectionTaks(Claim objClaim, String userName,
			String password) {
		Boolean isWaitingInRejectionQ = false;
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);

		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = null;
		com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask processRejectionTask = BPMClientContext
				.getProcessRejectionTask(userName, password);
		// com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =
		// processRejectionTask.getTasks(userName, pageable, payloadBO);
		tasks = processRejectionTask.getTasks("claimshead", new Pageable(),
				payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		{
			if (null != humanTaskList && !humanTaskList.isEmpty()) {
				for (HumanTask humanTask2 : humanTaskList) {
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask2
							.getPayloadCashless();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationType = payloadBOObj
							.getIntimation();
					if (null != intimationType) {
						String intimationId = intimationType
								.getIntimationNumber();
						if (null != intimationId
								&& (intimationId).equalsIgnoreCase(objClaim
										.getIntimation().getIntimationId())) {
							isWaitingInRejectionQ = true;
							break;
						}
					}

				}

<<<<<<< HEAD
=======
				/**
				 * As per Satish sir, current provision amount and provision amount is not updated in Ack level
				 
				//claimObj.setCurrentProvisionAmount(amt);
				claimObj.setClaimedAmount(amt);
				//claimObj.setProvisionAmount(amt);
			}
		} else if (isLumpSumOnly) {
			DBCalculationService dbCalculationService = new DBCalculationService();
			Long productKey = 0l;
			if (null != claimObj.getIntimation().getPolicy().getProduct())
				productKey = claimObj.getIntimation().getPolicy().getProduct()
						.getKey();
			Double lumpSumAmt = dbCalculationService.getLumpSumAmount(
					productKey, docAck.getClaim().getKey(),
					ReferenceTable.LUMPSUM_SUB_COVER_CODE);
			if (null != claimObj) {
				*//**
				 * As per Satish sir, current provision amount and provision amount is not updated in Ack level
				 *//*
				//claimObj.setCurrentProvisionAmount(lumpSumAmt);
				claimObj.setClaimedAmount(lumpSumAmt);
				//claimObj.setProvisionAmount(lumpSumAmt);
>>>>>>> FHOwithproduction
			}

		}

		if (!isWaitingInRejectionQ) {
			ProcessRejectionNonMedicalTask processRejectionNonMedicalTask = BPMClientContext
					.getProcessRejectionNonMedicalTask(userName, password);
			PagedTaskList nonMedicalTask = processRejectionNonMedicalTask
					.getTasks(userName, new Pageable(), payloadBO);
			List<HumanTask> nonMedicalHumanTaksList = nonMedicalTask
					.getHumanTasks();
			if (null != nonMedicalHumanTaksList
					&& !nonMedicalHumanTaksList.isEmpty()) {
				for (HumanTask humanTask : nonMedicalHumanTaksList) {
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
							.getPayloadCashless();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationType = payloadBOObj
							.getIntimation();
					if (null != intimationType) {
						String intimationId = intimationType
								.getIntimationNumber();
						if (null != intimationId
								&& (intimationId).equalsIgnoreCase(objClaim
										.getIntimation().getIntimationId())) {
							isWaitingInRejectionQ = true;
							break;
						}
					}

<<<<<<< HEAD
				}
			}
=======
		entityManager.merge(claimObj);
		entityManager.flush();
		entityManager.clear();
		log.info("------Claim------>" + claimObj + "<------------");
	}
>>>>>>> FHOwithproduction

		}

		return isWaitingInRejectionQ;

	}*/
/*
	public Boolean getConvertClaimTaskForROD(Claim objClaim) {
		Boolean isWaitingForConversion = false;
		com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask ackProcessConvertClaim = BPMClientContext
				.getConvertClaimSearchTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim
				.getTasks("claimshead", new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
						.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj
						.getClaim();
				PreAuthReqType preauthReqType = payloadBOObj.getPreAuthReq();
				Long claimKey = null;
				*//**
				 * For those cases which as moved to convert claim to reimb as
				 * per the date of admission rule, the claim key would be in
				 * claim type. Hence first claim key from claim type is obtained
				 * and if thats null then we get from preauth req type.
				 * *//*
				if (null != claimType && null != claimType.getKey()) {
					claimKey = claimType.getKey();
				} else if (null != preauthReqType.getKey()) {
					claimKey = preauthReqType.getKey();
				}

				// Long claimKey = preauthReqType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForConversion = true;
					break;
				}
			}
		}
		return isWaitingForConversion;

	}*/

	private RODDocumentCheckList findRODDocumentCheckListByKey(Long key) {
		RODDocumentCheckList documentChkLst = null;
		Query query = entityManager
				.createNamedQuery("RODDocumentCheckList.findByKey");
		query.setParameter("docListKey", key);
		if (null != query.getSingleResult()) {
			documentChkLst = (RODDocumentCheckList) query.getSingleResult();
		}
		return documentChkLst;
	}

	public ViewRejectionDTO getViewRejectionDTO(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByKey");
		query.setParameter("primaryKey", key);

		ReimbursementRejection rejectionDetails = (ReimbursementRejection) query
				.getSingleResult();

		if (rejectionDetails != null) {
			entityManager.refresh(rejectionDetails);
		}
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewRejectionDTO viewRejectionDto = instance
				.getRejectionDTO(rejectionDetails);
		
		if(viewRejectionDto.getRejectionRemarks2()!=null && !viewRejectionDto.getRejectionRemarks2().isEmpty()){
			viewRejectionDto.setRejectionRemarks(viewRejectionDto.getRejectionRemarks()+(viewRejectionDto.getRejectionRemarks2()));
		}else{
			
			if(viewRejectionDto.getRejectionRemarks()!=null && !viewRejectionDto.getRejectionRemarks().isEmpty()){
				viewRejectionDto.setRejectionRemarks2(viewRejectionDto.getRejectionRemarks());
			}
			
		}
		
		if(viewRejectionDto.getLetterRemarks2()!=null && !viewRejectionDto.getLetterRemarks2().isEmpty()){
			viewRejectionDto.setLetterRemarks(viewRejectionDto.getLetterRemarks()+(viewRejectionDto.getLetterRemarks2()));
		}else{
			
			if(viewRejectionDto.getLetterRemarks()!=null && !viewRejectionDto.getLetterRemarks().isEmpty()){
				viewRejectionDto.setLetterRemarks(viewRejectionDto.getLetterRemarks());
			}
			
		}
		if (viewRejectionDto.getRejectedDate() != null) {

			viewRejectionDto.setRejectedDate(SHAUtils
					.formatDate(rejectionDetails.getCreatedDate()));
		}
		if (viewRejectionDto.getAdmissionDate() != null) {
			viewRejectionDto.setAdmissionDate(SHAUtils
					.formatDate(rejectionDetails.getReimbursement().getClaim()
							.getIntimation().getAdmissionDate()));
		}
		if (rejectionDetails.getRejectionDraftDate() != null) {
			viewRejectionDto.setDraftedDate(SHAUtils
					.formatDate(rejectionDetails.getRejectionDraftDate()));
		}
		if (rejectionDetails.getRedraftDate() != null) {
			viewRejectionDto.setReDraftedDate(SHAUtils
					.formatDate(rejectionDetails.getRedraftDate()));
		}
		if (rejectionDetails.getApprovedRejectionDate() != null) {
			viewRejectionDto.setReDraftedDate(SHAUtils
					.formatDate(rejectionDetails.getApprovedRejectionDate()));
		}

		if (rejectionDetails.getDisapprovedDate() != null) {
			viewRejectionDto.setReDraftedDate(SHAUtils
					.formatDate(rejectionDetails.getDisapprovedDate()));
		}

		return viewRejectionDto;

	}

	public ReimbursementRejection getReimbursementRejection(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByKey");
		query.setParameter("primaryKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		for (ReimbursementRejection reimbursementRejection : rejectionDetails) {
			entityManager.refresh(reimbursementRejection);
		}

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return rejectionDetails.get(0);
		}
		return null;

	}

	public Boolean isRejectionExistOrNot(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return true;
		}

		return false;

	}

	public ReimbursementRejection getRejection(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return rejectionDetails.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Reimbursement> getRimbursementDetails(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
		}

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<ViewTmpReimbursement> getViewTmpRimbursementDetails(
			Long claimKey) {

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
	public ViewTmpClaimPayment getRimbursementForPayment(String claimNumber) {

		Query query = entityManager
				.createNamedQuery("ViewTmpClaimPayment.findByClaimNumber");
		query.setParameter("claimNumber", claimNumber);

		List<ViewTmpClaimPayment> reimbursementList = (List<ViewTmpClaimPayment>) query
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}

		return null;

	}

	public BankMaster getBankDetails(Long key) {
		Query query = entityManager.createNamedQuery("BankMaster.findByKey");
		query.setParameter("key", key);

		BankMaster bankMaster = (BankMaster) query.getSingleResult();

		return bankMaster;
	}

	/**
	 * Below method fetches list of records from reimbursement based on claim
	 * key. From that list , only latest reimbursement record will be taken into
	 * consideration and that latest record will be returned by this method.
	 * */

	@SuppressWarnings("unchecked")
	public Reimbursement getLatestReimbursementDetails(Long claimKey) {

		Reimbursement reimbursement = null;
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			reimbursement = reimbursementList.get(0);
		}

		return reimbursement;

	}

	@SuppressWarnings("unchecked")
	public ViewTmpReimbursement getLatestViewTmpReimbursementDetails(
			Long claimKey) {

		ViewTmpReimbursement reimbursement = null;
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			reimbursement = reimbursementList.get(0);
		}

		return reimbursement;

	}

	@SuppressWarnings("unchecked")
	public ViewTmpReimbursement getLatestViewTmpReimbursementSettlementDetails(
			Long claimKey) {

		ViewTmpReimbursement reimbursement = null;
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {

			for (ViewTmpReimbursement viewTmpReimbursement : reimbursementList) {
				if (viewTmpReimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_SETTLED)) {
					reimbursement = viewTmpReimbursement;
					break;
				}
			}

		}

		return reimbursement;

	}

	public ViewTmpClaimPayment getClaimPaymentByRodNumber(String rodNumber) {

		Query query = entityManager
				.createNamedQuery("ViewTmpClaimPayment.findByRodNo");
		query.setParameter("rodNumber", rodNumber);

		List<ViewTmpClaimPayment> result = (List<ViewTmpClaimPayment>) query
				.getResultList();

		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementDetailsForBillClassificationValidation(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
		}

		/*
		 * if(null != reimbursementList && !reimbursementList.isEmpty()) {
		 * reimbursementFinalList = new ArrayList<Reimbursement>(); for
		 * (Reimbursement reimbursement : reimbursementList) {
		 * 
		 * } //reimbursement = reimbursementList.get(0); }
		 */

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementDetailsForBillClassificationValidationWithoutCancelAck(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestNonCanceledRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
		}

		/*
		 * if(null != reimbursementList && !reimbursementList.isEmpty()) {
		 * reimbursementFinalList = new ArrayList<Reimbursement>(); for
		 * (Reimbursement reimbursement : reimbursementList) {
		 * 
		 * } //reimbursement = reimbursementList.get(0); }
		 */

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<DocAcknowledgement> getAckDetailsForBillClassificationValidation(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findNonCancelledAck");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			for (DocAcknowledgement docAcknowledgement : reimbursementList) {
				entityManager.refresh(docAcknowledgement);
			}
		}

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursement(Long rodkey) {

		Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		query.setParameter("primaryKey", rodkey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {

			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query.setParameter("ackDocKey", acknowledgementKey);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query
				.getResultList();

		for (DocAcknowledgement docAcknowledgement : reimbursementList) {
			entityManager.refresh(docAcknowledgement);
		}

		if (reimbursementList.size() > 0) {
			return reimbursementList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public Long getReimbursementByAckKey(Long ackKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findByAcknowledgement");
		query.setParameter("docAcknowledgmentKey", ackKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();
		for (Reimbursement reimbursement : reimbursementList) {
			keys.add(reimbursement.getKey());
		}
		if (!keys.isEmpty()) {
			Long key = Collections.max(keys);
			return key;
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByRodNo(String rodNo) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findRodByNumber");
		query.setParameter("rodNumber", rodNo);
		Reimbursement reimbursement = null;

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			reimbursement = reimbursementList.get(0);
			entityManager.refresh(reimbursement);
		}
		return reimbursement;

	}

	public List<DiagnosisDetailsTableDTO> getDiagnosisList(Long rodKey) {

		// rodKey = 5026l;
		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", rodKey);

		List<PedValidation> procedure = (List<PedValidation>) query
				.getResultList();

		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();

		for (PedValidation pedValidation : procedure) {

			DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
			Long diagnosisId = pedValidation.getDiagnosisId();
			if (diagnosisId != null) {
				try {
					Query diagnosis = entityManager
							.createNamedQuery("Diagnosis.findDiagnosisByKey");
					diagnosis.setParameter("diagnosisKey", diagnosisId);

					Diagnosis diagnosisValue = (Diagnosis) diagnosis
							.getSingleResult();
					dto.setDiagnosis(diagnosisValue.getValue());
					Query diagnosisPED = entityManager.createNamedQuery(
							"DiagnosisPED.findByPEDValidationKey")
							.setParameter("pedValidationKey",
									pedValidation.getKey());
					List<DiagnosisPED> pedDiagnosis = (List<DiagnosisPED>) diagnosisPED
							.getResultList();

					StringBuffer exclusionDetails = new StringBuffer();
					for (DiagnosisPED diagnosisPED2 : pedDiagnosis) {
						if (null != diagnosisPED2.getExclusionDetails()) {
							if (diagnosisPED2.getExclusionDetails() != null) {
								MastersValue master = getMaster(diagnosisPED2
										.getExclusionDetails().getImpactId());
								if (master != null)
									dto.setExclusionDiagnosis(master.getValue());
							}
							exclusionDetails.append(diagnosisPED2
									.getExclusionDetails().getExclusion()).append("; ");
						}
					}

					dto.setExclusionDetailsValue(exclusionDetails.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			
			//CR R20181300
			if (null != pedValidation.getPedImpactId()) {
				dto.setPedImpactOnDiagnosis(new SelectValue(pedValidation.getPedImpactId().getKey(), pedValidation.getPedImpactId().getValue()));
			}
			
			if (null != pedValidation.getNotPayingReason()) {
				dto.setReasonForNotPaying(new SelectValue(pedValidation.getNotPayingReason().getKey(),pedValidation.getNotPayingReason().getValue()));
			}	
			
			//CR R20181300
			
			dto.setRemarks(pedValidation.getApprovedRemarks());
			if (null != pedValidation.getCopayPercentage()) {
				dto.setCoPayValue(pedValidation.getCopayPercentage().toString());
			}
			if (null != pedValidation.getConsiderForPayment()) {
				if (pedValidation.getConsiderForPayment().equals("Y")) {
					dto.setConsiderForPaymentValue("Yes");
				} else {
					dto.setConsiderForPaymentValue("No");
				}
			}

			if (pedValidation.getSubLimitApplicable() != null
					&& ("Y").equalsIgnoreCase(pedValidation
							.getSubLimitApplicable())) {
				dto.setSublimitApplicableValue("Yes");
			} else {
				dto.setSublimitApplicableValue("No");
			}

			if (pedValidation.getSumInsuredRestrictionId() != null) {
				MastersValue masterValue = getMaster(pedValidation
						.getSumInsuredRestrictionId());
				if (masterValue != null) {
					dto.setSumInsuredRestrictionValue(Integer
							.valueOf(masterValue.getValue()));
				}
			}

			if (null != pedValidation.getIcdChpterId()) {
				dto.setIcdChapterKey(pedValidation.getIcdChpterId());
			}
			if (null != pedValidation.getIcdBlockId()) {
				dto.setIcdBlockKey(pedValidation.getIcdBlockId());
			}
			if (null != pedValidation.getIcdCodeId()) {
				dto.setIcdCodeKey(pedValidation.getIcdCodeId());
			}
			if (null != pedValidation.getSublimitId()) {

				/*Comment below code this not working for GMC
				Query limit = entityManager
						.createNamedQuery("ClaimLimit.findByKey");
				limit.setParameter("limitKey", pedValidation.getSublimitId());
				ClaimLimit claimLimit = (ClaimLimit) limit.getSingleResult();
				dto.setSublimitNameValue(claimLimit.getLimitName());
				if (null != claimLimit.getLimitName()) {
					dto.setSublimitNameValue(claimLimit.getLimitName());
					if (null != claimLimit.getMaxPerClaimAmount()) {
						dto.setSublimitAmt(claimLimit.getMaxPerClaimAmount()
								.toString());
					}
				}*/
				
				/*Below Code implemented for both GMC & Health affect from R0999*/
				if(pedValidation.getPolicy() != null && pedValidation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(pedValidation.getPolicy().getProduct().getKey())){
		    		Query claimLimitQuery = entityManager.createNamedQuery("MasAilmentLimit.findByKey");
			    	claimLimitQuery.setParameter("limitKey",pedValidation.getSublimitId());
			    	
			    	List<MasAilmentLimit> claimLimit = (List<MasAilmentLimit>)claimLimitQuery.getResultList();
					
			    	if(claimLimit != null && !claimLimit.isEmpty()){
			    		dto.setSublimitNameValue(claimLimit.get(0).getAilment());
						
						if(claimLimit.get(0).getLimitPerEye() != null){
						dto.setSublimitAmt(claimLimit.get(0).getLimitPerEye().toString());
						}
			    	}
		    		
		    	}
		    	else{
		    		Query claimLimitQuery = entityManager.createNamedQuery("ClaimLimit.findByKey");
			    	claimLimitQuery.setParameter("limitKey",pedValidation.getSublimitId());
					
					ClaimLimit claimLimit = (ClaimLimit)claimLimitQuery.getSingleResult();
					
					dto.setSublimitNameValue(claimLimit.getLimitName());
					
					if(claimLimit.getMaxPerPolicyPeriod() != null){
					dto.setSublimitAmt(claimLimit.getMaxPerPolicyPeriod().toString());
					}
					
				   
		    	}

			}
			 //added for prmiary diagnosis CR changes
			if(pedValidation.getPrimaryDiagnosis()!=null && pedValidation.getPrimaryDiagnosis().equalsIgnoreCase("Y"))
			{
				dto.setPrimaryDiagnosis(true);
			}
			else
			{
				dto.setPrimaryDiagnosis(null);
			}
			diagnosisList.add(dto);
		}

		return diagnosisList;
	}

	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			if (mastersValueList != null && !mastersValueList.isEmpty()) {
				return mastersValueList.get(0);
			}
		}

		return null;
	}

	public List<MedicalVerificationDTO> getMedicalVerification(
			BeanItemContainer<SelectValue> selectValueContainer, Long rodKey) {

		Query query = entityManager
				.createNamedQuery("ClaimVerification.findByReimbursementKey");
		query.setParameter("reimbursementKey", rodKey);

		List<ClaimVerification> claimVerification = (List<ClaimVerification>) query
				.getResultList();

		List<SelectValue> mastersValue = selectValueContainer.getItemIds();

		List<MedicalVerificationDTO> medicalVerificationList = new ArrayList<MedicalVerificationDTO>();

		for (SelectValue selectValue : mastersValue) {
			MedicalVerificationDTO dto = new MedicalVerificationDTO();
			if (claimVerification != null) {
				for (ClaimVerification claimVerification2 : claimVerification) {
					if (null != claimVerification2.getVerificationTypeId()) {
						if (selectValue.getId().equals(
								claimVerification2.getVerificationTypeId())) {
							dto.setDescription(selectValue.getValue());
							if (null != claimVerification2.getVerifiedFlag()) {
								if (claimVerification2.getVerifiedFlag()
										.equals("Y")) {
									dto.setVerifiedFlag("Yes");
								} else {
									dto.setVerifiedFlag("No");
								}
							}
							dto.setRemarks(claimVerification2
									.getMedicalRemarks());
						}
					}
				}
			}
			medicalVerificationList.add(dto);

		}

		return medicalVerificationList;

	}

	@SuppressWarnings("unchecked")
	public List<Speciality> getSpecialityListByClaim(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("Speciality.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Speciality> speciality = (List<Speciality>) query.getResultList();

		return speciality;

	}

	@SuppressWarnings({ "unchecked" })
	public List<ProcedureDTO> getProcedureDto(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("Procedure.findByTransactionKey");
		query.setParameter("transactionKey", rodKey);

		List<Procedure> procedure = (List<Procedure>) query.getResultList();

		List<ProcedureDTO> procedureDtoList = new ArrayList<ProcedureDTO>();

		for (Procedure procedure2 : procedure) {
			ProcedureDTO dto = new ProcedureDTO();
			dto.setProcedureNameValue(procedure2.getProcedureName());
			dto.setProcedureCodeValue(procedure2.getProcedureCode());
			dto.setPackageRate(procedure2.getPackageRate());
			if (procedure2.getDayCareProcedure() != null) {
				if (procedure2.getDayCareProcedure().equals("Y")) {
					dto.setDayCareProcedure("Yes");
				} else {
					dto.setDayCareProcedure("No");
				}
			}
			if (procedure2.getConsiderForDayCare() != null) {
				if (procedure2.getConsiderForDayCare().equals("Y")) {
					dto.setConsiderForDayCareValue("Yes");
				} else {
					dto.setConsiderForDayCareValue("No");
				}
			}

			if (procedure2.getConsiderForPayment() != null) {
				if (procedure2.getConsiderForPayment().equals("Y")) {
					dto.setConsiderForPaymentValue("Yes");
				} else {
					dto.setConsiderForPaymentValue("No");
				}
			}
			dto.setRemarks(procedure2.getProcedureRemarks());
			dto.setSubLimitApplicable(procedure2.getSubLimitApplicable());
			if (procedure2.getSubLimitApplicable() != null
					&& ("Y").equalsIgnoreCase(procedure2
							.getSubLimitApplicable())) {
				dto.setSublimitApplicableFlag("Yes");
			} else {
				dto.setSublimitApplicableFlag("No");
			}
			if (null != procedure2.getSublimitNameId()) {
				Query limit = entityManager
						.createNamedQuery("ClaimLimit.findByKey");
				limit.setParameter("limitKey", procedure2.getSublimitNameId());
				ClaimLimit claimLimit = (ClaimLimit) limit.getSingleResult();
				dto.setSublimitNameValue(claimLimit.getLimitName());
				if (null != claimLimit.getSumInsured()) {
					dto.setSublimitAmount(claimLimit.getSumInsured().toString());
					dto.setSubLimitAmount(Double.valueOf(claimLimit
							.getSumInsured()));
				}
			}
			if(procedure2.getNewProceudreName() !=null){
				dto.setNewProcedureName(procedure2.getNewProceudreName());
			}
			if(procedure2.getSpeciality() !=null && procedure2.getSpeciality().getValue() !=null){
				SelectValue selectValue = new SelectValue(procedure2.getSpeciality().getKey(),procedure2.getSpeciality().getValue());
				dto.setSpeciality(selectValue);
			}
			procedureDtoList.add(dto);
		}
		return procedureDtoList;

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

	public DocumentDetailsDTO getAcknowledgementDetails(Long ackKey) {

		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
		DocumentDetailsDTO documentDetailsDto = null;
		if (docAcknowledgement != null) {
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			documentDetailsDto = instance.getAcknowledgementDetail(docAcknowledgement);
			if (docAcknowledgement.getHospitalisationFlag() != null) {
				documentDetailsDto.setHospitalization(docAcknowledgement
						.getHospitalisationFlag().equals("Y") ? true : false);
			}
			if (docAcknowledgement.getPreHospitalisationFlag() != null) {
				documentDetailsDto
						.setPreHospitalization(docAcknowledgement
								.getPreHospitalisationFlag().equals("Y") ? true
								: false);
			}
			if (docAcknowledgement.getPostHospitalisationFlag() != null) {
				documentDetailsDto.setPostHospitalization(docAcknowledgement
						.getPostHospitalisationFlag().equals("Y") ? true
						: false);
			}
			if (docAcknowledgement.getPartialHospitalisationFlag() != null) {
				documentDetailsDto.setPartialHospitalization(docAcknowledgement
						.getPartialHospitalisationFlag().equals("Y") ? true
						: false);
			}
			if (docAcknowledgement.getLumpsumAmountFlag() != null) {
				documentDetailsDto.setLumpSumAmount(docAcknowledgement
						.getLumpsumAmountFlag().equals("Y") ? true : false);
			}
			if (docAcknowledgement.getHospitalCashFlag() != null) {
				documentDetailsDto
						.setAddOnBenefitsHospitalCash(docAcknowledgement
								.getHospitalCashFlag().equals("Y") ? true
								: false);
			}
			if (docAcknowledgement.getPatientCareFlag() != null) {
				documentDetailsDto
						.setAddOnBenefitsPatientCare(docAcknowledgement
								.getPatientCareFlag().equals("Y") ? true
								: false);
			}
			
			if (docAcknowledgement.getHospitalizationRepeatFlag() != null) {
				documentDetailsDto.setHospitalizationRepeat(docAcknowledgement
						.getHospitalizationRepeatFlag().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getOtherBenefitsFlag() != null) {
				documentDetailsDto.setOtherBenefits(docAcknowledgement
						.getOtherBenefitsFlag().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getEmergencyMedicalEvaluation() != null) {
				documentDetailsDto.setEmergencyMedicalEvaluation(docAcknowledgement
						.getEmergencyMedicalEvaluation().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getCompassionateTravel() != null) {
				documentDetailsDto.setCompassionateTravel(docAcknowledgement
						.getCompassionateTravel().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getRepatriationOfMortalRemain() != null) {
				documentDetailsDto.setRepatriationOfMortalRemains(docAcknowledgement
						.getRepatriationOfMortalRemain().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getPreferredNetworkHospita() != null) {
				documentDetailsDto.setPreferredNetworkHospital(docAcknowledgement
						.getPreferredNetworkHospita().equals("Y") ? true
						: false);
			}
			
			if (docAcknowledgement.getSharedAccomodation() != null) {
				documentDetailsDto.setSharedAccomodation(docAcknowledgement
						.getSharedAccomodation().equals("Y") ? true
						: false);
			}
			if (docAcknowledgement.getReconsiderationRequest() != null) {
				documentDetailsDto
						.setReconsiderationRequestValue(docAcknowledgement
								.getReconsiderationRequest().equals("Y") ? "Yes"
								: "No");
			}
			
			
			if (docAcknowledgement.getBenifitFlag() != null) {
				
					documentDetailsDto.setDeath(docAcknowledgement.getBenifitFlag().equals(SHAConstants.DEATH_FLAGS) ? true: false);
					
					documentDetailsDto.setPermanentPartialDisability(docAcknowledgement.getBenifitFlag().equals(SHAConstants.PPD) ? true: false);
					
					documentDetailsDto.setPermanentTotalDisability(docAcknowledgement.getBenifitFlag().equals(SHAConstants.PTD) ? true: false);
					
					documentDetailsDto.setTemporaryTotalDisability(docAcknowledgement.getBenifitFlag().equals(SHAConstants.TTD) ? true: false);
					
					documentDetailsDto.setPaHospitalisation(docAcknowledgement.getBenifitFlag().equals(SHAConstants.HOSP) ? true: false);
					
					documentDetailsDto.setPaPartialHospitalisation(docAcknowledgement.getBenifitFlag().equals(SHAConstants.PART) ? true: false);
					
			}
			
			if(docAcknowledgement.getAdditionalRemarks() != null)
			{
				documentDetailsDto.setAdditionalRemarks(docAcknowledgement.getAdditionalRemarks());
			}
			EarlierRodMapper.invalidate(instance);
		}

		return documentDetailsDto;

	}

	/**
	 * Method to retrieve data for select earlier ROD request to re consider.
	 * **/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValues(Claim claim) {

		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;

			//List<Reimbursement> reimbursementDetails = getReimbursementDetails(claim.getKey());
		  List<Reimbursement> reimbursementDetails = getReimbursementDetailsForReconsideration(claim.getKey());
		  
		  
			
			if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
				
				AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
				List<ReconsiderRODRequestTableDTO> reimbursementList = acknowledgeDocumentReceivedMapper
						.getReimbursementDetails(reimbursementDetails);

				if (null != reimbursementList && !reimbursementList.isEmpty()) {
					reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();

						for (ReconsiderRODRequestTableDTO reimbursementData : reimbursementList) {
							/**
							 * Since settled claim functionality is not yet implemented, 
							 * no status is available for the same. Hence as of now, only
							 * rejected claims will be shown for reconsideration.
							 * */
							if((ReferenceTable.PAYMENT_SETTLED.equals(reimbursementData.getStatusId()) ||
									ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) || 
									ReferenceTable.PROCESS_CLAIM_APPROVAL_REJECTION_STATUS.equals(reimbursementData.getStatusId()) ||
									ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
									ReferenceTable.PAYMENT_REJECTED.equals(reimbursementData.getStatusId()) ||
									(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId())  && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
									(ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()) && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
									(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS.equals(reimbursementData.getStatusId())) || (ReferenceTable.REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS.equals(reimbursementData.getStatusId())))||
									(ReferenceTable.PAYMENT_PROCESS_STAGE.equals(reimbursementData.getStageId())&& ReferenceTable.PAYMENT_PROCESS_REOPENED.equals(reimbursementData.getStatusId())))	
							 {
								DocAcknowledgement docAck =  getDocAcknowledgment(reimbursementData.getDocAcknowledgementKey());
								ReconsiderRODRequestTableDTO docAckData = AcknowledgeDocumentReceivedMapper.getDocAckRecDetails(docAck);
											
							if (reimbursementData.getKey().equals(
									docAckData.getRodKey())) {
								docAckData.setRodStatus(reimbursementData
										.getRodStatus());
								if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementData.getStageId()))
								{
									docAckData.setApprovedAmt(reimbursementData.getFinancialApprovedAmount());
								}
								else if(ReferenceTable.BILLING_STAGE.equals(reimbursementData.getStageId()))
								{
									docAckData.setApprovedAmt(reimbursementData.getBillingApprovedAmount());
								}
								else 
								{
									docAckData.setApprovedAmt(reimbursementData.getApprovedAmt());
								}
								
								if(ReferenceTable.FINANCIAL_SETTLED.equals(reimbursementData.getStatusId()) ||
										(ReferenceTable.PAYMENT_PROCESS_STAGE.equals(reimbursementData.getStageId())&& 
												ReferenceTable.PAYMENT_PROCESS_REOPENED.equals(reimbursementData.getStatusId())))
								{
									ClaimPayment claimPaymentDetails = getClaimPaymentDetails(reimbursementData.getRodNo());
									if(null != claimPaymentDetails)
									{
									docAckData.setApprovedAmt(claimPaymentDetails.getApprovedAmount());
									docAckData.setIsSettledReconsideration(true);
									}
								}
								
								docAckData.setRodNo(reimbursementData.getRodNo());
								docAckData.setBillClassification(getBillClassificationValue(docAckData));
								
								Reimbursement reimbObj = getReimbursement(reimbursementData.getKey());
								if(null != reimbObj){
									Double claimedAmount = getClaimedAmount(reimbObj);
									docAckData.setClaimedAmt(claimedAmount);
									
									MastersValue patientStatus = reimbObj.getPatientStatus();
									if(patientStatus != null) {
										docAckData.setPatientStatus(new SelectValue(patientStatus.getKey(), patientStatus.getValue()));
									}
									
									docAckData.setLegalHeirName(reimbObj.getNomineeName());
									docAckData.setLegalHeirAddr(reimbObj.getNomineeAddr());
									
								}
								
								docAckData.setBenefitFlag(docAck.getBenifitFlag());
								if(null != docAck.getBenifitClaimedAmount())
									docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
								String benefit = "";
								if(null != docAck.getBenifitFlag())
								{
									benefit = docAck.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
									 additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getKey());
								/*}
								else
								{
									additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
								}*/
								String additionalCover = "";
								Double addOnCoversAmt = 0d;
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										coversList.add(coverName);
										
										
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
														
									optionalCovers =  getOptionalCoversForRodAndBillEntry(reimbursementData.getKey());
									
							/*	}
								else
								{
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
								}*/
								String optionalCover = "";
								Double optionCoversClaimedAmt = 0d;
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();								
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										optionalCoversList.add(coverName);
										

										if(null  != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
								
//								String billClassificationValue = getBillClassificationValue(docAck);
//								benefitOrCover = billClassificationValue != null ? benefitOrCover+billClassificationValue:benefitOrCover;
								if(null != benefitOrCover)
								{								
									docAckData.setBenifitOrCover(benefitOrCover);
								}
								if (null != docAckData.getHospitalizationClaimedAmt())
									docAckData.setClaimedAmt(calculatedClaimedAmt(
													docAckData.getHospitalizationClaimedAmt(),
													docAckData.getPreHospClaimedAmt(),
													docAckData.getPostHospClaimedAmt(),0d));
																
								
								if(null != docAck.getBenifitClaimedAmount())
								{
									Double claimedAmt = docAck.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									docAckData.setClaimedAmt(claimedAmt);
									docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
								}
								if(null != docAckData.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsHospitalCashFlag()))
								{
									ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.HOSPITAL_CASH_FLAG);
									if(null != reimbursementBenefits)
									{
										docAckData.setHospitalCashNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
										docAckData.setHospitalCashPerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
										docAckData.setHospitalCashTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
										docAckData.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits.getKey());
									}
								}
								if(null != docAckData.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsPatientCareFlag()))
								{
									ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.PATIENT_CARE_FLAG);
									if(null != reimbursementBenefits)
									{
										docAckData.setPatientCareNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
										docAckData.setPatientCarePerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
										docAckData.setPatientCareTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
										docAckData.setPatientCareReimbursementBenefitsKey(reimbursementBenefits.getKey());
										List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits.getKey());
										if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
										{
											List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
											for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
												PatientCareDTO patientCareDTO = new PatientCareDTO();
												patientCareDTO.setEngagedFrom(reimbursementBenefitsDetails.getEngagedFrom());
												patientCareDTO.setEngagedTo(reimbursementBenefitsDetails.getEngagedTo());
												patientCareDTO.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails.getKey());
												patientCareList.add(patientCareDTO);
											}
											docAckData.setPatientCareDTOList(patientCareList);
										}
									}
								}
								
								if(("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()))
									docAckData.setSelect(true);
								docAckData.setRodKey(reimbursementData.getKey());
								reconsiderRODRequestDTO.add(docAckData);
								//break;
							}
						}
					}
					//}
				}
			}
		return reconsiderRODRequestDTO;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValuesForCreateRodAndBillEntry(Claim claim) {

		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;
		
		
	/*	List<DocAcknowledgement> docAcknowledgements = masterService
				.getDocumentAcknowledgeByClaim(claim);*/
		/*List<DocAcknowledgement> docAcknowledgements = masterService
				.getLatestDocumentAcknowledgeByClaim(claim);*/

	//	if (null != docAcknowledgements && !docAcknowledgements.isEmpty()) {
		/*	List<ReconsiderRODRequestTableDTO> docAckList = AcknowledgeDocumentReceivedMapper
					.getDocAcknowledgeList(docAcknowledgements);
			
			if(null != docAckList && !docAckList.isEmpty())
			{
				docAckData = docAckList.get(0);
			}*/

			//List<Reimbursement> reimbursementDetails = getReimbursementDetails(claim.getKey());
		  List<Reimbursement> reimbursementDetails = getReimbursementDetailsForReconsideration(claim.getKey());

			if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
				
				AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
				List<ReconsiderRODRequestTableDTO> reimbursementList = acknowledgeDocumentReceivedMapper
						.getReimbursementDetails(reimbursementDetails);

				if (null != reimbursementList && !reimbursementList.isEmpty()) {
					reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();
					//for (ReconsiderRODRequestTableDTO docAckData : docAckList) {

						for (ReconsiderRODRequestTableDTO reimbursementData : reimbursementList) {
							// if(docAckData.getRodKey().equals(reimbursementData.getKey()))
							/**
							 * Since settled claim functionality is not yet implemented, 
							 * no status is available for the same. Hence as of now, only
							 * rejected claims will be shown for reconsideration.
							 * */
							// if((reimbursementData.getKey().equals(docAckData.getRodKey()))
							// &&
							// ("Settled").equals(reimbursementData.getRodStatus()))
							if(
									/*(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||  
											ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
											(ReferenceTable.CREATE_ROD_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.PROCESS_CLAIM_REQUEST_CLSOED_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.BILLING_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()))
											
									)*/
									
									(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||  
										//	ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS.equals(reimbursementData.getStatusId()) ||
											ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId())||
											ReferenceTable.PAYMENT_REJECTED.equals(reimbursementData.getStatusId())||
											ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
											(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId())  && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
											(ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()) && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
											(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS.equals(reimbursementData.getStatusId())) || (ReferenceTable.REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS.equals(reimbursementData.getStatusId()))
											)
								)
											
							 {
								DocAcknowledgement docAck =  getDocAcknowledgment(reimbursementData.getDocAcknowledgementKey());
								ReconsiderRODRequestTableDTO docAckData = AcknowledgeDocumentReceivedMapper.getDocAckRecDetails(docAck);
								/*if (null != docAcknowledgements && !docAcknowledgements.isEmpty()) {
										List<ReconsiderRODRequestTableDTO> docAckList = AcknowledgeDocumentReceivedMapper
												.getDocAcknowledgeList(docAcknowledgements);	
										if(null != docAckList && !docAckList.isEmpty())
										{
											docAckData = docAckList.get(0);
										}
								}*/
											
							if (reimbursementData.getKey().equals(
									docAckData.getRodKey())) {
								/*docAckData.setApprovedAmt(reimbursementData
										.getApprovedAmt());*/
								docAckData.setRodStatus(reimbursementData
										.getRodStatus());
								if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementData.getStageId()))
								{
									docAckData.setApprovedAmt(reimbursementData.getFinancialApprovedAmount());
								}
								else if(ReferenceTable.BILLING_STAGE.equals(reimbursementData.getStageId()))
								{
									docAckData.setApprovedAmt(reimbursementData.getBillingApprovedAmount());
								}
								else 
								{
									docAckData.setApprovedAmt(reimbursementData.getApprovedAmt());
								}
								
								if(ReferenceTable.FINANCIAL_SETTLED.equals(reimbursementData.getStatusId()))
								{
									ClaimPayment claimPaymentDetails = getClaimPaymentDetails(reimbursementData.getRodNo());
									if(null != claimPaymentDetails)
									{
									docAckData.setApprovedAmt(claimPaymentDetails.getApprovedAmount());
									}
								}
								docAckData.setRodNo(reimbursementData.getRodNo());
								docAckData.setBillClassification(getBillClassificationValue(docAckData));
								
								docAckData.setBenefitFlag(docAck.getBenifitFlag());
								if(null != docAck.getBenifitClaimedAmount())
									docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
								String benefit = "";
								if(null != docAck.getBenifitFlag())
								{
									benefit = docAck.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();								
								
									 //additionalCovers =  getAdditionalCovers(docAck.getKey());
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getKey());
								
								String additionalCover = "";
								Double addOnCoversAmt = 0d;
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
														
									optionalCovers =  getOptionalCoversForRodAndBillEntry(reimbursementData.getKey());
									Double optionCoversClaimedAmt = 0d;
							/*	}
								else
								{
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
								}*/
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();								
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
								
//								String billClassificationValue = getBillClassificationValue(docAck);
//								benefitOrCover = billClassificationValue != null ? benefitOrCover+billClassificationValue:benefitOrCover;
								if(null != benefitOrCover)
								{								
									docAckData.setBenifitOrCover(benefitOrCover);
								}

								//if (null != docAckData.getHospitalizationClaimedAmt())
									/*docAckData.setClaimedAmt(calculatedClaimedAmt(
													docAckData.getHospitalizationClaimedAmt(),
													docAckData.getPreHospClaimedAmt(),
													docAckData.getPostHospClaimedAmt()));
													*/			
								
								if(null != docAck.getBenifitClaimedAmount())
								{
									
									//if(null != docAck.getBenifitClaimedAmount())
									//{
										Double claimedAmt = docAck.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
										docAckData.setClaimedAmt(claimedAmt);
									//}
									
									//docAckData.setClaimedAmt(docAck.getBenifitClaimedAmount());
									docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
								}
								if(null != docAckData.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsHospitalCashFlag()))
								{
									ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.HOSPITAL_CASH_FLAG);
									if(null != reimbursementBenefits)
									{
										docAckData.setHospitalCashNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
										docAckData.setHospitalCashPerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
										docAckData.setHospitalCashTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
										docAckData.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits.getKey());
									}
								}
								if(null != docAckData.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsPatientCareFlag()))
								{
									ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.PATIENT_CARE_FLAG);
									if(null != reimbursementBenefits)
									{
										docAckData.setPatientCareNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
										docAckData.setPatientCarePerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
										docAckData.setPatientCareTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
										docAckData.setPatientCareReimbursementBenefitsKey(reimbursementBenefits.getKey());
										List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits.getKey());
										if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
										{
											List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
											for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
												PatientCareDTO patientCareDTO = new PatientCareDTO();
												patientCareDTO.setEngagedFrom(reimbursementBenefitsDetails.getEngagedFrom());
												patientCareDTO.setEngagedTo(reimbursementBenefitsDetails.getEngagedTo());
												patientCareDTO.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails.getKey());
												patientCareList.add(patientCareDTO);
											}
											docAckData.setPatientCareDTOList(patientCareList);
										}
									}
								}
								
								if(("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()))
									docAckData.setSelect(true);
								docAckData.setRodKey(reimbursementData.getKey());
								reconsiderRODRequestDTO.add(docAckData);
								//break;
							}
						}
					}
					//}
				}
			}
		return reconsiderRODRequestDTO;
	}

	

public List<ReconsiderRODRequestTableDTO> getPAReconsiderationDetailsList(DocAcknowledgement docAcknowledgement){
		
		List<ReconsiderRODRequestTableDTO> reconsiderationTable = new ArrayList<ReconsiderRODRequestTableDTO>();
		
		ReconsiderRODRequestTableDTO reconsideration = new ReconsiderRODRequestTableDTO();
		if(docAcknowledgement != null){
			if(docAcknowledgement.getRodKey() != null){
				Reimbursement reimbursementByKey = getReimbursementByKey(docAcknowledgement.getRodKey());
				if(reimbursementByKey != null){
					reconsideration.setRodNo(reimbursementByKey.getRodNumber());
					Double claimedAmount = getClaimedAmountValueForView(docAcknowledgement);
					Double approvedAmt = getApprovedAmtForView(reimbursementByKey);
					reconsideration.setClaimedAmt(claimedAmount);
					reconsideration.setApprovedAmt(approvedAmt);
					reconsideration.setRodStatus(reimbursementByKey.getStatus().getProcessValue());
					
					if(reimbursementByKey.getBenefitsId() != null)
					{
						String benefit = reimbursementByKey.getBenefitsId().getValue();
						String benefitOrCover = benefit.isEmpty() ? getCoverValueForViewBasedOnrodKey(reimbursementByKey.getKey()) : benefit + ", " + getCoverValueForViewBasedOnrodKey(reimbursementByKey.getKey());
						reconsideration.setBenifitOrCover(benefitOrCover);
					}
				}
			}
		}
		reconsiderationTable.add(reconsideration);
		
		return reconsiderationTable;
	}


	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}

	public List<Reimbursement> getReimbursementDetails(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementDetails = query.getResultList();
		if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
			for (Reimbursement reimbursement : reimbursementDetails) {
				entityManager.refresh(reimbursement);
			}

		}
		return reimbursementDetails;
	}
	
public List<Reimbursement> getReimbursementDetailsForReconsideration(Long claimKey) { 
		
		List<Reimbursement> listOfRod = new ArrayList<Reimbursement>();
		
		List<String> rodNumberList = getRodNumberList(claimKey);
		for (String rodNumber : rodNumberList) {
			Query query = entityManager
					.createNamedQuery("Reimbursement.findRodByNumberWise");
			query.setParameter("rodNumber", rodNumber);
			List<Reimbursement> reimbursementDetails = query.getResultList();
			Boolean isSettledExist = Boolean.FALSE;
			for (Reimbursement reimbursement : reimbursementDetails) {
				
				//IMSSUPPOR-27766 - Added for reconsider of settled reject cases - 12-Mar-19
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS) || 
						reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
						|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_REJECT_STATUS)){
					isSettledExist = Boolean.TRUE;
					listOfRod.add(reimbursement);
					break;
				}
				
				
			}
			if(!isSettledExist && ! reimbursementDetails.isEmpty()){
				for (Reimbursement reimbursement : reimbursementDetails) {
					if(! ReferenceTable.getCancelRODKeys().containsKey(reimbursement.getStatus().getKey()))
					{
						listOfRod.add(reimbursement);
						break;
						
					}
						
				}
				
			}
		}
		
		
		return listOfRod;
	}
	
	
	public List<String> getRodNumberList(Long claimKey) {
		
		List<Long> statusList = new ArrayList<Long>();
		/*statusList.add(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
		statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
		statusList.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS);
		statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);*/
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.getRodNumber");
		query.setParameter("claimKey", claimKey);
		//query.setParameter("statusList", statusList);
		
		List<String> rodNumber = query.getResultList();
		
		return rodNumber;
	}

	public Boolean getReimbursementQueryStatus(Long reimbursementKey) {
		Boolean queryStatus = false;
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findLatestQueryByKey");
		query = query.setParameter("primaryKey", reimbursementKey);
		List<ReimbursementQuery> reimbQueryList = query.getResultList();
		if (null != reimbQueryList && !reimbQueryList.isEmpty()) {
			ReimbursementQuery reimbQuery = reimbQueryList.get(0);
			if (reimbQuery
					.getStatus()
					.getKey()
					.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
					|| (reimbQuery.getStatus().getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))) {
				queryStatus = true;
			} else {
				queryStatus = false;
			}
		} else {
			queryStatus = false;
		}
		return queryStatus;
	}

	@SuppressWarnings("unchecked")
	public ReimbursementBenefits getReimbursementBenefits(
			Long reimbBenefitsKey, String benefitsFlag) {

		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKeyAndBenefitsFlag");
		query.setParameter("rodKey", reimbBenefitsKey);
		query.setParameter("benefitsFlag", benefitsFlag);

		List<ReimbursementBenefits> reimbursementList = (List<ReimbursementBenefits>) query
				.getResultList();

		for (ReimbursementBenefits reimbursementBenefits : reimbursementList) {
			entityManager.refresh(reimbursementBenefits);
		}

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}

		return null;

	}

	public List<ReimbursementBenefitsDetails> getReimbursementBenefitsDetailsList(
			Long reimbBenefitsKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query.setParameter("benefitsKey", reimbBenefitsKey);
		List<ReimbursementBenefitsDetails> reimburesmentBenefitsDetails = query
				.getResultList();
		return reimburesmentBenefitsDetails;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValuesForBillEntry(Long rodKey) {

		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;
			Reimbursement reimbursementDetails = getReimbursement(rodKey);
			
			if (null != reimbursementDetails ) {
				AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
				ReconsiderRODRequestTableDTO reconsiderList = acknowledgeDocumentReceivedMapper
						.getReimbursementDetails(reimbursementDetails);
				if (null != reconsiderList) 
				{
					reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();
					DocAcknowledgement docAck =  getDocAcknowledgment(reconsiderList.getDocAcknowledgementKey());
					ReconsiderRODRequestTableDTO docAckData = AcknowledgeDocumentReceivedMapper.getDocAckRecDetails(docAck);	
					if (reconsiderList.getKey().equals(
							docAckData.getRodKey())) {
						/*docAckData.setApprovedAmt(reimbursementData
								.getApprovedAmt());*/
						docAckData.setRodStatus(reconsiderList
								.getRodStatus());
						
						ReimbursementRejection reimbRejection = getReimbRejectRecord(docAckData.getRodKey());
						if(null != reimbRejection && null != reimbRejection.getStatus())
						{
							if(reimbRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || reimbRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
									|| reimbRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS)
									|| reimbRejection.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED))
							{
								docAckData.setIsRejectReconsidered(true);
							}
							else
							{
								docAckData.setIsRejectReconsidered(false);
							}
						}
						if(null != reimbursementDetails && null != reimbursementDetails.getFinancialCompletedDate() && null != reimbRejection && null != reimbRejection.getModifiedDate() 
								&& reimbursementDetails.getFinancialCompletedDate().after(reimbRejection.getModifiedDate()))
						{
							docAckData.setIsRejectReconsidered(false);
							docAckData.setIsSettledReconsideration(true);
						}

						Double claimedAmount = getClaimedAmount(reimbursementDetails);
						docAckData.setClaimedAmt(claimedAmount);
						/**
						 * The below condition is commented, since in bill entry stage, the 
						 * status and stage of rod will be updated as per create ROD process.
						 * Hence financial approval status or billing send to financial approver
						 * status will be not be applicable and  hence below condition will now work.
						 * This needs to be discussed with sathish sir once.
						 * */
						/*if(ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reconsiderList.getStatusId()))
						{
							docAckData.setApprovedAmt(reconsiderList.getFinancialApprovedAmount());
						}
						else if(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER.equals(reconsiderList.getStatusId()))
						{
							docAckData.setApprovedAmt(reconsiderList.getBillingApprovedAmount());
						}
						else 
						{
							docAckData.setApprovedAmt(reconsiderList.getApprovedAmt());
						}*/
						Double approvedAmt = 0d;
						if(null != reconsiderList.getProcessClmType() && (SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(reconsiderList.getProcessClmType())))
						{
							
							if(docAckData.getIsRejectReconsidered())
								{
									approvedAmt = 0d;
								}
							else
							{
								approvedAmt = (null != reconsiderList.getBenefitsApprovedAmt() ? reconsiderList.getBenefitsApprovedAmt() : 0d) 
										 + (null != reconsiderList.getAddOnApprovedAmt() ? reconsiderList.getAddOnApprovedAmt() : 0d) 
										 +(null != reconsiderList.getOptionalApprovedAmt() ? reconsiderList.getOptionalApprovedAmt() :0d);
							}
							
						}
						else
						{
							approvedAmt = reconsiderList.getFinancialApprovedAmount(); 
						}
						
						if(reconsiderList.getRodNo() != null){
							ClaimPayment claimPayment = getClaimPaymentDetails(reconsiderList.getRodNo());
							if(claimPayment != null && claimPayment.getTotalApprovedAmount() != null){
								approvedAmt = claimPayment.getTotalApprovedAmount();	
							}
						}
						
						docAckData.setApprovedAmt(approvedAmt);
						docAckData.setRodNo(reconsiderList.getRodNo());
						docAckData.setBillClassification(getBillClassificationValue(docAckData));
						docAckData.setBenefitFlag(docAck.getBenifitFlag());
						String benefit = docAckData.getBenefitFlag();
						
						
						//List<PAAdditionalCovers> additionalCovers =  getAdditionalCovers(docAckData.getKey());
						
						List<PAAdditionalCovers> additionalCovers = getAdditionalCoversForRodAndBillEntry(reimbursementDetails.getKey());
						Double addOnCoversAmt = 0d;
						String additionalCover = "";
						List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
						if(null != additionalCovers)
						{
							for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
								
								Long coverId = paAdditionalCovers.getCoverId();
								MasPaClaimCovers coverName = getCoversName(coverId);
								Double addOnappAmt = paAdditionalCovers.getApprovedAmt();
								
								if(null  != paAdditionalCovers.getClaimedAmount())
								{
									addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
								}
								
								
								coversList.add(coverName);
							}
							
							if(null != coversList && !coversList.isEmpty())
							{
								for (MasPaClaimCovers masPaClaimCovers : coversList) {
									additionalCover += masPaClaimCovers.getCoverDescription();
								}
							}
							
						}
						
						List<PAOptionalCover> optionalCovers =  getOptionalCoversForRodAndBillEntry(reimbursementDetails.getKey());
						String optionalCover = "";
						Double optionCoversClaimedAmt = 0d;
						List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
						if(null != optionalCovers)
						{
							for (PAOptionalCover paOptionalCover : optionalCovers) {						
								Long coverId = paOptionalCover.getCoverId();
								Double optApprovedAmt = paOptionalCover.getApprovedAmt();
								MasPaClaimCovers coverName = getCoversName(coverId);
								
								if(null != paOptionalCover.getClaimedAmount())
								{
									optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
								}
								
								optionalCoversList.add(coverName);
							} 
							
							if(null != optionalCoversList && !optionalCoversList.isEmpty())
							{
								for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
									optionalCover += masPaClaimCovers.getCoverDescription();
								}
							}
						}
						
						String benefitOrCover = benefit + "," + additionalCover + "," + optionalCover;
						docAckData.setBenifitOrCover(benefitOrCover);
						if (null != docAckData.getHospitalizationClaimedAmt())
							docAckData.setClaimedAmt(calculatedClaimedAmt(
											docAckData.getHospitalizationClaimedAmt(),
											docAckData.getPreHospClaimedAmt(),
											docAckData.getPostHospClaimedAmt(),0d));
						
						if(null != docAck.getBenifitClaimedAmount())
						{
							Double claimedAmt = docAck.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
							docAckData.setClaimedAmt(claimedAmt);
						}
						
						docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
						//docAckData.setClaimedAmt(docAck.getBenifitClaimedAmount());
						if(null != docAckData.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsHospitalCashFlag()))
						{
							ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reconsiderList.getKey(), SHAConstants.HOSPITAL_CASH_FLAG);
							if(null != reimbursementBenefits)
							{
								docAckData.setHospitalCashNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
								docAckData.setHospitalCashPerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
								docAckData.setHospitalCashTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
								docAckData.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits.getKey());
							}
						}
						if(null != docAckData.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsPatientCareFlag()))
						{
							ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reconsiderList.getKey(), SHAConstants.PATIENT_CARE_FLAG);
							if(null != reimbursementBenefits)
							{
								docAckData.setPatientCareNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
								docAckData.setPatientCarePerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
								docAckData.setPatientCareTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
								docAckData.setPatientCareReimbursementBenefitsKey(reimbursementBenefits.getKey());
								List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits.getKey());
								if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
								{
									List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
									for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
										PatientCareDTO patientCareDTO = new PatientCareDTO();
										patientCareDTO.setEngagedFrom(reimbursementBenefitsDetails.getEngagedFrom());
										patientCareDTO.setEngagedTo(reimbursementBenefitsDetails.getEngagedTo());
										patientCareDTO.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails.getKey());
										patientCareList.add(patientCareDTO);
									}
									docAckData.setPatientCareDTOList(patientCareList);
								}
							}
						}
						
						if(("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()))
							docAckData.setSelect(true);
						docAckData.setRodKey(reconsiderList.getKey());
						
						MastersValue patientStatus = reimbursementDetails.getPatientStatus();
						if(patientStatus != null) {
							docAckData.setPatientStatus(new SelectValue(patientStatus.getKey(), patientStatus.getValue()));
						}
						
						docAckData.setLegalHeirName(reimbursementDetails.getNomineeName());
						docAckData.setLegalHeirAddr(reimbursementDetails.getNomineeAddr());
						
						reconsiderRODRequestDTO.add(docAckData);
						//break;
					}
			}
		}
		return reconsiderRODRequestDTO;
	}

	private Double calculatedClaimedAmt(Double hospAmt, Double preHospAmt,
			Double postHospAmt,Double otherBenefitAmnt) {
		Double total = 0d;
		if (null != hospAmt) {
			total = total + hospAmt;
		}
		if (null != preHospAmt) {
			total = total + preHospAmt;
		}
		if (null != postHospAmt) {
			total = total + postHospAmt;
		}
		if (null != otherBenefitAmnt) {
			total = total + otherBenefitAmnt;
		}
		

		return total;
	}

	private String getBillClassificationValue(
			ReconsiderRODRequestTableDTO reconsiderReqDTO) {
		StringBuilder strBuilder = new StringBuilder();
		if (("Y").equals(reconsiderReqDTO.getHospitalizationFlag())) {
			strBuilder.append("Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getHospitalizationRepeatFlag())) {
			strBuilder.append("Hospitalization Repeat");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getPreHospitalizationFlag())) {
			strBuilder.append("Pre-Hospitalization");
			strBuilder.append(",");
		}
		if (("Y").equals(reconsiderReqDTO.getPostHospitalizationFlag())) {
			strBuilder.append("Post-Hospitalization");
			strBuilder.append(",");
		}
		

		if (("Y").equals(reconsiderReqDTO.getPartialHospitalizationFlag())) {
			strBuilder.append("Partial-Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getLumpSumAmountFlag())) {
			strBuilder.append("Lumpsum Amount");
			strBuilder.append(",");

		}
		if (("Y").equals(reconsiderReqDTO.getAddOnBenefitsHospitalCashFlag())) {
			strBuilder.append("Add on Benefits (Hospital cash)");
			strBuilder.append(",");

		}
		if (("Y").equals(reconsiderReqDTO.getAddOnBenefitsPatientCareFlag())) {
			strBuilder.append("Add on Benefits (Patient Care)");
			strBuilder.append(",");
		}
		
		if (("Y").equals(reconsiderReqDTO.getEmergencyMedicalEvaluationFlag())) {
			strBuilder.append("Emergency Medical Evaluation");
			strBuilder.append(",");
		}
		if (("Y").equals(reconsiderReqDTO.getCompassionateTravelFlag())) {
			strBuilder.append("Compassionate Travel");
			strBuilder.append(",");
		}
		if (("Y").equals(reconsiderReqDTO.getRepatriationOfMortalRemainsFlag())) {
			strBuilder.append("Repartion Of Mortal Remain");
			strBuilder.append(",");
		}
        Long ProductKey = null;
		if(reconsiderReqDTO.getRodKey() != null){
			Reimbursement reimbursement = getReimbursement(reconsiderReqDTO.getRodKey());
			if(reimbursement != null && null != reimbursement.getClaim()&& reimbursement.getClaim().getIntimation() != null 
					&& reimbursement.getClaim().getIntimation().getPolicy() != null 
					&& reimbursement.getClaim().getIntimation().getPolicy().getProduct() != null
					&& ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey())){
				ProductKey = reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey();
			}
		}
		if(ProductKey != null){
			if (("Y").equals(reconsiderReqDTO.getPreferredNetworkHospitalFlag())) {
				strBuilder.append("Valuable Service Provider (Hospital)");
				strBuilder.append(",");
			}
		}else{
			if (("Y").equals(reconsiderReqDTO.getPreferredNetworkHospitalFlag())) {
				strBuilder.append("Preferred Network Hospital");
				strBuilder.append(",");
			}
		}
		if (("Y").equals(reconsiderReqDTO.getSharedAccomodationFlag())) {
			strBuilder.append("Shared Accomodation");
			strBuilder.append(",");
		}

		return strBuilder.toString();
	}

	
	public String getCoverValueForViewBasedOnrodKey(Long rodKey)
	{
		String optCover = "";
		String addOnCover = "";
		String coverValues = "";
		
		if(rodKey != null)
		{
			List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
			List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
			List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
			List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
			
			additionalCovers =  getAdditionalCoversForRodAndBillEntry(rodKey);
			optionalCovers = getOptionalCoversForRodAndBillEntry(rodKey);
			
			if(additionalCovers != null)
			{
				for(PAAdditionalCovers paAdditionalCovers : additionalCovers)
				{
					Long coverId = paAdditionalCovers.getCoverId();
					MasPaClaimCovers coverName = getCoversName(coverId);
					coversList.add(coverName);
				}
				
				if(null != coversList && !coversList.isEmpty())
				{
					for (MasPaClaimCovers masPaClaimCovers : coversList) {
						if(masPaClaimCovers.getCoverDescription() != null){
							addOnCover += masPaClaimCovers.getCoverDescription()+", ";
						}
						}
				}
			}
			if(optionalCovers != null)
			{
				for (PAOptionalCover paOptionalCover : optionalCovers) {						
						Long coverId = paOptionalCover.getCoverId();								
						MasPaClaimCovers coverName = getCoversName(coverId);
						
						optionalCoversList.add(coverName);
				} 
				if(null != optionalCoversList && !optionalCoversList.isEmpty())
				{
						for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
							if(masPaClaimCovers.getCoverDescription() != null){
								optCover += masPaClaimCovers.getCoverDescription()+", ";
							}
						}
				}
			}
		coverValues = optCover + addOnCover;
		}
		return coverValues;
	}
	
	public String getCoverValueForViewBasedOnAckKey(Long docAckKey)
	{
		String optCover = "";
		String addOnCover = "";
		String coverValues = "";
		
		if(docAckKey != null)
		{
			List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
			List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
			List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
			List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
			
			additionalCovers =  getAdditionalCovers(docAckKey);
			optionalCovers = getOptionalCovers(docAckKey);
			
			if(additionalCovers != null)
			{
				for(PAAdditionalCovers paAdditionalCovers : additionalCovers)
				{
					Long coverId = paAdditionalCovers.getCoverId();
					MasPaClaimCovers coverName = getCoversName(coverId);
					coversList.add(coverName);
				}
				
				if(null != coversList && !coversList.isEmpty())
				{
					for (MasPaClaimCovers masPaClaimCovers : coversList) {
						if(masPaClaimCovers.getCoverDescription() != null){
							addOnCover += masPaClaimCovers.getCoverDescription()+", ";	
						}
					}
				}
			}
			if(optionalCovers != null)
			{
				for (PAOptionalCover paOptionalCover : optionalCovers) {						
						Long coverId = paOptionalCover.getCoverId();								
						MasPaClaimCovers coverName = getCoversName(coverId);
						
						optionalCoversList.add(coverName);
				} 
				if(null != optionalCoversList && !optionalCoversList.isEmpty())
				{
						for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
							if(masPaClaimCovers.getCoverDescription() != null){
								optCover += masPaClaimCovers.getCoverDescription()+", ";
							}
						}
				}
			}
		coverValues = optCover + addOnCover;
		}
		
		return coverValues;
	}
	
	public Double getBenefitAddOnOptionalApprovedAmt(Reimbursement reimbursementByKey)
	{
		Double totalAmt = 0d;
		Double addApprovedAmt = 0d;
		Double optApprovedAmt = 0d;
		Double benApprovedAmt = 0d;
		
		if(reimbursementByKey != null)
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
		return totalAmt;
	}
	
	private Double getApprovedAmtForView(Reimbursement reimbursementByKey)
	{
		Double totalAmt = 0d;
		Double approvedAmt = 0d;
		Double addApprovedAmt = 0d;
		Double optApprovedAmt = 0d;
		Double benApprovedAmt = 0d;
		
		if(reimbursementByKey != null)
		{
			if(reimbursementByKey.getStage() != null)
			{
				if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementByKey.getStage().getKey()))
				{
					if(reimbursementByKey.getFinancialApprovedAmount() != null)
					{
						approvedAmt = reimbursementByKey.getFinancialApprovedAmount();
					}
				}
				else if(ReferenceTable.BILLING_STAGE.equals(reimbursementByKey.getStage().getKey()))
				{
					if(reimbursementByKey.getBillingApprovedAmount() != null)
					{
						approvedAmt = reimbursementByKey.getBillingApprovedAmount();
					}
				}
				else 
				{
					if(reimbursementByKey.getApprovedAmount() != null)
					{
						approvedAmt = reimbursementByKey.getApprovedAmount();	
					}
				}
			}
			
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
			
			totalAmt = approvedAmt + benApprovedAmt + optApprovedAmt + addApprovedAmt;
		}
		return totalAmt;
	}
	
	
	public Double getClaimedAmountValueForView(DocAcknowledgement docAck)
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
	
	
	private Double getDocAckClaimedAmount(DocAcknowledgement docAck)
	{
		Double claimedAmt = 0d;
		if(null != docAck)
		{
			if(null != docAck.getHospitalizationClaimedAmount())
			{
				claimedAmt += docAck.getHospitalizationClaimedAmount();
			}
			if(null != docAck.getPreHospitalizationClaimedAmount())
			{
				claimedAmt += docAck.getPreHospitalizationClaimedAmount();
			}
			if(null != docAck.getPostHospitalizationClaimedAmount())
			{
				claimedAmt += docAck.getPostHospitalizationClaimedAmount();
			}
		}
		return claimedAmt;
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
					ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())){
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
			
			// added for hospital cash IMSSUPPOR-36652
			
			if (("Y").equals(docAck.getProdHospBenefitFlag())) {
				strBuilder.append("Hospital Cash");
				strBuilder.append(",");
			}
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}

	public Long getCountOfAckByClaimKey(Long a_key) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.CountAckByClaimKey");
		query = query.setParameter("claimkey", a_key);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
	}

	public List<ViewQueryDTO> getQueryDetails(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();

		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			entityManager.refresh(reimbursementQuery2);
			entityManager.refresh(reimbursementQuery2.getReimbursement());
		}
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewQueryDTO> queryDetails = instance
				.getViewQueryDTO(reimbursementQuery);

		
		String coverValue = getCoverValueForViewBasedOnrodKey(rodKey);
		
		if(reimbursementQuery != null && !reimbursementQuery.isEmpty())
		{
			if(queryDetails != null && !queryDetails.isEmpty()){
				for(ReimbursementQuery rQuery :reimbursementQuery){
					String billClassification = getBillClassificationValue(rQuery.getReimbursement().getDocAcknowLedgement());
					
					Double claimedAmt = getClaimedAmountValueForView(rQuery.getReimbursement().getDocAcknowLedgement());
					
					for (ViewQueryDTO viewQueryDTO : queryDetails) {
						if(viewQueryDTO.getKey() == rQuery.getKey()){
							viewQueryDTO.setBillClassification(billClassification);
							
							if(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag() != null)
							{
								if(SHAUtils.getBenefitsValue(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag()).isEmpty())
								{
									viewQueryDTO.setBenefitCover(coverValue);
								}
								else
								{
									viewQueryDTO.setBenefitCover(SHAUtils.getBenefitsValue(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag())+", "+coverValue);
								}
							}
							
							if(rQuery.getQueryType() != null && rQuery.getQueryType().equalsIgnoreCase("Y")){
								viewQueryDTO.setQueryType("PAYMENT QUERY");
							}else{
								viewQueryDTO.setQueryType("QUERY");
							}
							
							viewQueryDTO.setClaimedAmt(claimedAmt);
							if(rQuery.getQryTyp() != null){
								viewQueryDTO.setOpnQryTyp(rQuery.getQryTyp().getValue());
							}
						}
						
					}
				}
			}
		}

		return queryDetails;
	}

	public List<ViewQueryDTO> getPAymentQueryDetails(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementKeyForPayment");
		query = query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();

		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			entityManager.refresh(reimbursementQuery2);
			entityManager.refresh(reimbursementQuery2.getReimbursement());
		}
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewQueryDTO> queryDetails = instance
				.getViewQueryDTO(reimbursementQuery);

		
		String coverValue = getCoverValueForViewBasedOnrodKey(rodKey);
		
		if(reimbursementQuery != null && !reimbursementQuery.isEmpty())
		{
			if(queryDetails != null && !queryDetails.isEmpty()){
				for(ReimbursementQuery rQuery :reimbursementQuery){
					String billClassification = getBillClassificationValue(rQuery.getReimbursement().getDocAcknowLedgement());
					
					Double claimedAmt = getClaimedAmountValueForView(rQuery.getReimbursement().getDocAcknowLedgement());
					
					for (ViewQueryDTO viewQueryDTO : queryDetails) {
						if(viewQueryDTO.getKey() == rQuery.getKey()){
							viewQueryDTO.setBillClassification(billClassification);
							
							if(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag() != null)
							{
								if(SHAUtils.getBenefitsValue(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag()).isEmpty())
								{
									viewQueryDTO.setBenefitCover(coverValue);
								}
								else
								{
									viewQueryDTO.setBenefitCover(SHAUtils.getBenefitsValue(rQuery.getReimbursement().getDocAcknowLedgement().getBenifitFlag())+", "+coverValue);
								}
							}
							
							if(rQuery.getQueryType() != null && rQuery.getQueryType().equalsIgnoreCase("Y")){
								viewQueryDTO.setQueryType("PAYMENT QUERY");
							}else{
								viewQueryDTO.setQueryType("QUERY");
							}
							
							viewQueryDTO.setClaimedAmt(claimedAmt);
						}
						
					}
				}
			}
		}

		return queryDetails;
	}

	
	
	
	
	
	public List<ViewQueryDTO> getQueryDetails(Long rodKey, EntityManager em) {
		entityManager = em;
		List<ViewQueryDTO> queryDetails = getQueryDetails(rodKey);

		return queryDetails;
	}

	public List<PedValidation> search(Long preAuthKey) {

		List<PedValidation> resultList = new ArrayList<PedValidation>();

		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);

		resultList = (List<PedValidation>) query.getResultList();

		return resultList;

	}

	public String getDiagnosisName(Long key) {

		String diagnosisName = "";

		List<PedValidation> pedValidationList = search(key);

		for (PedValidation pedValidation : pedValidationList) {

			Query diagnosis = entityManager
					.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey",
					pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if (masters != null) {
				diagnosisName += masters.getValue() + ",";
			}
		}

		return diagnosisName;
	}

	
	public List<RODQueryDetailsDTO> getPAQueryDetailsList(Long ackKey) {

		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);

		if (docAcknowledgement != null) {

			Query query = entityManager
					.createNamedQuery("ReimbursementQuery.findByReimbursement");
			query = query.setParameter("reimbursementKey",
					docAcknowledgement.getRodKey());

			List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
					.getResultList();
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			List<RODQueryDetailsDTO> queryDetails = 
					instance.getAckQueryDTO(reimbursementQuery);
			
			for (RODQueryDetailsDTO rodQueryDetailsDTO : queryDetails) {
				
				if(rodQueryDetailsDTO.getReimbursementKey() != null){
					String diagnosisForPreauthByKey = getDiagnosisForPreauthByKey(rodQueryDetailsDTO.getReimbursementKey());
					rodQueryDetailsDTO.setDiagnosis(diagnosisForPreauthByKey);
				}
				
				if(rodQueryDetailsDTO.getReplyStatus() != null && rodQueryDetailsDTO.getReplyStatus().equalsIgnoreCase("Y")){
					rodQueryDetailsDTO.setQueryReplyStatus("YES");
				}else{
					rodQueryDetailsDTO.setQueryReplyStatus("NO");
				}
				
				if(rodQueryDetailsDTO.getReimbursement() != null){
					Double claimedAmount = getClaimedAmountValueForView(docAcknowledgement);
					rodQueryDetailsDTO.setClaimedAmount(claimedAmount);
					
					if(rodQueryDetailsDTO.getReimbursement().getBenefitsId() != null){
						rodQueryDetailsDTO.setBenifitOrCover(rodQueryDetailsDTO.getReimbursement().getBenefitsId().getValue()+", "+getCoverValueForViewBasedOnrodKey(rodQueryDetailsDTO.getReimbursementKey()));
					}
					else
					{
						rodQueryDetailsDTO.setBenifitOrCover(getCoverValueForViewBasedOnrodKey(rodQueryDetailsDTO.getReimbursementKey()));
					}
				}
				
				
			}

			return queryDetails;
		}

		return null;
	}
	
	
	public List<RODQueryDetailsDTO> getQueryDetailsList(Long ackKey) {

		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);

		if (docAcknowledgement != null) {

			Query query = entityManager
					.createNamedQuery("ReimbursementQuery.findByReimbursement");
			query = query.setParameter("reimbursementKey",
					docAcknowledgement.getRodKey());

			List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
					.getResultList();
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			List<RODQueryDetailsDTO> queryDetails = instance.getAckQueryDTO(reimbursementQuery);

			for (RODQueryDetailsDTO rodQueryDetailsDTO : queryDetails) {
				if (rodQueryDetailsDTO.getDocAcknowledgment() != null) {
					String billClassificationValue = getBillClassificationValue(rodQueryDetailsDTO
							.getDocAcknowledgment());
					rodQueryDetailsDTO
							.setBillClassification(billClassificationValue);
				}

				if (rodQueryDetailsDTO.getReimbursementKey() != null) {
					String diagnosisForPreauthByKey = getDiagnosisForPreauthByKey(rodQueryDetailsDTO
							.getReimbursementKey());
					rodQueryDetailsDTO.setDiagnosis(diagnosisForPreauthByKey);
				}

				if (rodQueryDetailsDTO.getReplyStatus() != null
						&& rodQueryDetailsDTO.getReplyStatus()
								.equalsIgnoreCase("Y")) {
					rodQueryDetailsDTO.setQueryReplyStatus("YES");
				} else {
					rodQueryDetailsDTO.setQueryReplyStatus("NO");
				}

				if (rodQueryDetailsDTO.getReimbursement() != null) {
					Double claimedAmount = getClaimedAmount(rodQueryDetailsDTO
							.getReimbursement());
					rodQueryDetailsDTO.setClaimedAmount(claimedAmount);
				}
				
				
			}

			return queryDetails;
		}

		return null;
	}

	public String getDiagnosisForPreauthByKey(Long preauthKey) {
		String diagnosis = "";
		try {
			List<PedValidation> findPedValidationByPreauthKey = findPedValidationByPreauthKey(preauthKey);
			List<DiagnosisDetailsTableDTO> diagDtoList = (PreMedicalMapper
					.getInstance())
					.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

			if (diagDtoList != null && !diagDtoList.isEmpty()) {

				for (DiagnosisDetailsTableDTO diagDto : diagDtoList) {
					Long diagKey = diagDto.getDiagnosisId();
					if (diagKey != null) {
						Diagnosis diagObj = entityManager.find(Diagnosis.class,
								diagKey);
						if (diagObj != null) {
							diagnosis = ("").equals(diagnosis) ? diagObj
									.getValue() : diagnosis + " , "
									+ diagObj.getValue();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		return diagnosis;
	}

	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedValidationByPreauthKey(Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<PedValidation> resultList = (List<PedValidation>) query
				.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (PedValidation pedValidation : resultList) {
				entityManager.refresh(pedValidation);
			}
		}
		return resultList;
	}

	public ReimbursementQuery getReimbursementQuery(Long primaryKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByKey");
		query = query.setParameter("primaryKey", primaryKey);

		ReimbursementQuery reimbursementQuery = (ReimbursementQuery) query
				.getSingleResult();

		entityManager.refresh(reimbursementQuery);

		return reimbursementQuery;

	}

	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		try {
			if (null != query.getSingleResult()) {
				docAcknowledgement = (DocAcknowledgement) query
						.getSingleResult();

				entityManager.refresh(docAcknowledgement);
			}
			return docAcknowledgement;
		} catch (Exception e) {
			System.out.println("No elements");
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> listOfEarlierAckByClaimKey(Long a_key,
			Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", a_key);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<DocAcknowledgement> docAcknowledgmentList = (List<DocAcknowledgement>) query
				.getResultList();

		List<DocAcknowledgement> earlierAck = new ArrayList<DocAcknowledgement>();
		for (DocAcknowledgement docAcknowledgement : docAcknowledgmentList) {
			// if(docAcknowledgement.getRodKey() != null){
			// if(! docAcknowledgement.getRodKey().equals(reimbursementKey)){
			// earlierAck.add(docAcknowledgement);
			// }
			earlierAck.add(docAcknowledgement);
			keysList.add(docAcknowledgement.getKey());
			// }
		}

		Long maximumKey = 0l;
		if (!keysList.isEmpty()) {
			maximumKey = Collections.max(keysList);
		}

		for (DocAcknowledgement docAcknowledgement : earlierAck) {
			entityManager.refresh(docAcknowledgement);
		}
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewDocumentDetailsDTO> listDocumentDetails = instance.getDocumentDetailsTableDTO(earlierAck);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

			String date = SHAUtils.getDateFormat(documentDetailsDTO.getReceivedDate());

			String benefitCover = "";
			String coverValue = "";
			Boolean isPA = false;
			
			if (documentDetailsDTO.getRodKey() != null) {
				Long rodKey = new Long(documentDetailsDTO.getRodKey());
				Query rodQuery = entityManager
						.createNamedQuery("Reimbursement.findByKey");
				rodQuery = rodQuery.setParameter("primaryKey", rodKey);

				Reimbursement reimbursement = (Reimbursement) rodQuery
						.getSingleResult();
				if (reimbursement != null) {
					entityManager.refresh(reimbursement);
				}
				
				if(reimbursement != null && reimbursement.getClaim() != null && reimbursement.getClaim().getIntimation().getLobId() != null && reimbursement.getClaim().getIntimation().getLobId().getKey() != null){
					if(((ReferenceTable.PA_LOB_KEY).equals(reimbursement.getClaim().getIntimation().getLobId().getKey())) || 
							((ReferenceTable.PACKAGE_MASTER_VALUE).equals(reimbursement.getClaim().getIntimation().getLobId().getKey()))){
						isPA = true;
					}
				}
				
				documentDetailsDTO.setRodNumber(reimbursement.getRodNumber());
				documentDetailsDTO.setReimbursementKey(reimbursement.getKey());

				if (documentDetailsDTO.getKey() != null
						&& !documentDetailsDTO.getKey().equals(
								reimbursement.getDocAcknowLedgement().getKey())) {
					documentDetailsDTO.setIsReadOnly(true);
				}

				// documentDetailsDTO.setMedicalResponseTime(reimbursement
				// .getMedicalCompletedDate());
				documentDetailsDTO.setApprovedAmount(reimbursement
						.getApprovedAmount());
				
				if(null != reimbursement.getCorporateBufferFlag() && ("1").equals(reimbursement.getCorporateBufferFlag())){
					documentDetailsDTO.setCorpBufferUtilised(SHAConstants.YES);
				}
				else
				{
					documentDetailsDTO.setCorpBufferUtilised(SHAConstants.No);
				}

				// if(reimbursement.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)
				// ||
				// reimbursement.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
				// ||
				// reimbursement.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
				//
				// documentDetailsDTO.setApprovedAmount(reimbursement.getCurrentProvisionAmt());
				//
				// }

				documentDetailsDTO.setStatus(reimbursement.getStatus()
						.getProcessValue());
				documentDetailsDTO.setStatusKey(reimbursement.getStatus()
						.getKey());
				if (reimbursement.getMedicalCompletedDate() != null) {
					documentDetailsDTO
							.setMedicalResponseTime(SHAUtils
									.formatDate(reimbursement
											.getMedicalCompletedDate()));
				}

				documentDetailsDTO.setApprovedAmount(reimbursement
						.getCurrentProvisionAmt());
				// documentDetailsDTO.setApprovedAmount(reimbursement.getCurrentProvisionAmt());

				if (reimbursement.getStage().getKey()
						.equals(ReferenceTable.BILLING_STAGE)) {

					// Long approvedAmount =
					// getReimbursementApprovedAmount(reimbursement.getKey());
					// if(approvedAmount >0){
					// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
					// }
					
					if(reimbursement
							.getBillingApprovedAmount() == null){
						documentDetailsDTO.setApprovedAmount(reimbursement
								.getCurrentProvisionAmt());
						
					}else {
						if(reimbursement
								.getBillingApprovedAmount().equals(0)){
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getCurrentProvisionAmt());
						}else{
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getBillingApprovedAmount());
						}
					}
					

				} else if (reimbursement.getStage().getKey()
						.equals(ReferenceTable.FINANCIAL_STAGE)) {
					// Long approvedAmount =
					// getReimbursementApprovedAmount(reimbursement.getKey());
					// if(approvedAmount >0){
					// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
					// }
					
					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());
					
					if((ReferenceTable.FINANCIAL_SETTLED).equals(reimbursement.getStatus().getKey())){
						
						documentDetailsDTO.setApprovedAmount(reimbursement.getFinancialApprovedAmount());
					}
					
					/*if(reimbursement
							.getFinancialApprovedAmount() == null){
						documentDetailsDTO.setApprovedAmount(reimbursement
								.getCurrentProvisionAmt());
						
					}else{
					

					documentDetailsDTO.setApprovedAmount(reimbursement.getBillingApprovedAmount());
					}*/

				}
				else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
//					Long approvedAmount = getReimbursementApprovedAmount(reimbursement.getKey());
//					if(approvedAmount >0){
//						documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
//					}

						documentDetailsDTO.setApprovedAmount(reimbursement.getFinancialApprovedAmount());
					
				}
				
				

				// if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				// Double provisionAmount = getProvisionAmount(reimbursement);
				// documentDetailsDTO.setApprovedAmount(provisionAmount);
				// }

				if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
						.containsKey(reimbursement.getStatus().getKey())) {
					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());
				}

				if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
						|| reimbursement
								.getStatus()
								.getKey()
								.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
						|| reimbursement
								.getStatus()
								.getKey()
								.equals(ReferenceTable.PAYMENT_REJECTED)) {

					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());

				}

				
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
					documentDetailsDTO.setApprovedAmount(reimbursement.getBillingApprovedAmount());
				}
				
				if(ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
					documentDetailsDTO.setApprovedAmount(reimbursement.getCurrentProvisionAmt());
				}

				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {

					String statusValue = "MA - "
							+ reimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);

				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {

					String statusValue = "FA - "
							+ reimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);
				}

			
				if(reimbursement.getBenefitsId() != null)
				{
					benefitCover = reimbursement.getBenefitsId().getValue();
					
				}
				
				coverValue = getCoverValueForViewBasedOnrodKey(reimbursement.getKey());
				
				if(benefitCover.isEmpty())
				{
					documentDetailsDTO.setBenefits(coverValue);
				}
				else
				{
					documentDetailsDTO.setBenefits(benefitCover+", "+coverValue);
				}
				
				if(isPA && (SHAConstants.PA_LOB_TYPE).equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getProcessClaimType()))
				{
					if(ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
						documentDetailsDTO.setApprovedAmount(reimbursement.getCurrentProvisionAmt());
					}
					else
					{
					documentDetailsDTO.setApprovedAmount(getBenefitAddOnOptionalApprovedAmt(reimbursement));
					}
				}
				
				//IMSSUPPOR-26424
				//ClaimPayment claimPayment = getClaimPaymentDetails(reimbursement.getRodNumber());
				ClaimPayment claimPayment = getClaimPaymentByRodKey(rodKey);

				
				Double approveAmnt = 0d;
				if(isPA)
				{
					
					
					
					if((ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(documentDetailsDTO.getStatusKey()))
					{
						if(claimPayment != null){
							documentDetailsDTO.setApprovedAmount(claimPayment.getApprovedAmount());
						}
					}
					
					
					
				}
				
				if((ReferenceTable.REIMBURSEMENT_SETTLED_STATUS).equals(documentDetailsDTO.getStatusKey()))
				{	
			
						if(null != claimPayment)
							{
								documentDetailsDTO.setApprovedAmount(claimPayment.getApprovedAmount());
							}
				}
				
			}
		}

		List<String> hospitalization = new ArrayList<String>();

		for (DocAcknowledgement docAcknowledgement : earlierAck) {

			getDocumentBillClassification(hospitalization, docAcknowledgement);

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			// listDocumentDetails.get(0).setLatestKey(maximumKey);
			Reimbursement reimbursement = null;
			if (reimbursementKey != null) {
				reimbursement = getReimbursement(reimbursementKey);
			}
			if (reimbursement != null
					&& reimbursement.getDocAcknowLedgement() != null) {
				listDocumentDetails.get(0).setLatestKey(
						reimbursement.getDocAcknowLedgement().getKey());
			} else {
				listDocumentDetails.get(0).setLatestKey(maximumKey);
			}
		}

		return listDocumentDetails;
	}

	public List<ViewDocumentDetailsDTO> getDocumentDetailsForCloseClaim(
			String intimationNo, String userName, String password) {


		
		 DBCalculationService dbCalculationService = new DBCalculationService();
		
		 List<Map<String, Object>> taskProcedureForCloseClaim = dbCalculationService.getTaskProcedureForCloseClaim(intimationNo);

		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

		Claim claim = getClaimsByIntimationNumber(intimationNo);

		List<Reimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
				.getKey());

		for (Reimbursement reimbursement : reimbursementList) {
			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PAYMENT_REJECTED)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)
					|| ReferenceTable.getPaymentStatus().containsKey(
							reimbursement.getStatus().getKey())) {
				DocAcknowledgement docAcknowLedgement = reimbursement
						.getDocAcknowLedgement();
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);

				documentDetailsDTO.setStatusKey(reimbursement.getStatus().getKey());
				setReimbursementToDTO(documentList, reimbursement,
						docAcknowLedgement, documentDetailsDTO);
				EarlierRodMapper.invalidate(instance);
			}
		}
		
		/**
		 * The below code needs to be migrated.
		 * */

		if (taskProcedureForCloseClaim != null) {
			getDocumentListByHumanTask(taskProcedureForCloseClaim, documentList);
		}

		return documentList;

	}
	public List<ViewDocumentDetailsDTO> getDocumentDetailsForReOpenClaim(
			String intimationNo, String userName, String password) {

//		PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(intimationNo);
//		payloadType.setIntimation(intimationType);
//
//		ReopenAllClaimTask reOpenClaimTask = BPMClientContext
//				.getReOpAllClaimTask(userName, password);
//
//		PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
//				payloadType);
		
		List<Map<String, Object>> dbTaskForCloseClaim = getDBTaskForCloseClaim(intimationNo, SHAConstants.CLOSE_CLAIM_CURRENT_Q);
		
		/**
		 * The commented code to be refractored.
		 * 
		 * **/
		
		/*PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadType.setIntimation(intimationType);

		ReopenAllClaimTask reOpenClaimTask = BPMClientContext
				.getReOpAllClaimTask(userName, password);

		PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
				payloadType);*/

		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

		Claim claim = getClaimsByIntimationNumber(intimationNo);

		List<Reimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
				.getKey());

		List<Long> rodKeyList = new ArrayList<Long>();

//		List<HumanTask> humanTasks = tasks.getHumanTasks();
		/*List<HumanTask> humanTasks = tasks.getHumanTasks();
>>>>>>> removalofbpmn*/

		for (Map<String, Object> map : dbTaskForCloseClaim) {
			Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
			
			if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
				rodKeyList.add(reimbursementKey);
			}

		}

		for (Reimbursement reimbursement : reimbursementList) {
			entityManager.refresh(reimbursement);
			if (!rodKeyList.contains(reimbursement.getKey())) {
				DocAcknowledgement docAcknowLedgement = reimbursement
						.getDocAcknowLedgement();
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
				setReimbursementToDTO(documentList, reimbursement,
						docAcknowLedgement, documentDetailsDTO);
				EarlierRodMapper.invalidate(instance);
			}
		}


		if (dbTaskForCloseClaim != null && ! dbTaskForCloseClaim.isEmpty()) {
			getDocumentListByHumanTask(dbTaskForCloseClaim, documentList);
		}


		return documentList;

	}

public List<Map<String, Object>> getDBTaskForCloseClaim(String intimation,String currentQ){
	
	Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	mapValues.put(SHAConstants.INTIMATION_NO, intimation);
	mapValues.put(SHAConstants.CURRENT_Q, currentQ);
	
//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
	
	Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

	DBCalculationService db = new DBCalculationService();
//	 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
	
	 //List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
	List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForReOpen(setMapValues);
	if (taskProcedure != null && !taskProcedure.isEmpty()){
		return taskProcedure;
	} 
	return taskProcedure;
}

public List<ViewDocumentDetailsDTO> getDocumentDetailsForPAHealthReOpenClaim(String intimationNo,String userName, String password){
		
	//PayloadBOType payloadType = new PayloadBOType();
//	IntimationType intimationType = new IntimationType();
//	intimationType.setIntimationNumber(intimationNo);
//	payloadType.setIntimation(intimationType);
//
//	ReopenAllClaimTask reOpenClaimTask = BPMClientContext
//			.getReOpAllClaimTask(userName, password);
//
//	PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
//			payloadType);
	
	List<Map<String, Object>> dbTaskForCloseClaim = getDBTaskForCloseClaim(intimationNo, SHAConstants.CLOSE_CLAIM_CURRENT_Q);
	
	/**
	 * The commented code to be refractored.
	 * 
	 * **/
	
	/*PayloadBOType payloadType = new PayloadBOType();
	IntimationType intimationType = new IntimationType();
	intimationType.setIntimationNumber(intimationNo);
	payloadType.setIntimation(intimationType);

	ReopenAllClaimTask reOpenClaimTask = BPMClientContext
			.getReOpAllClaimTask(userName, password);

	PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
			payloadType);*/

	List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

	Claim claim = getClaimsByIntimationNumber(intimationNo);

	List<Reimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
			.getKey());

	List<Long> rodKeyList = new ArrayList<Long>();

//	List<HumanTask> humanTasks = tasks.getHumanTasks();
	/*List<HumanTask> humanTasks = tasks.getHumanTasks();
>>>>>>> removalofbpmn

	for (Map<String, Object> map : dbTaskForCloseClaim) {
		Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
		
		if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
			rodKeyList.add(reimbursementKey);
		}

	}
*/
	for (Reimbursement reimbursement : reimbursementList) {
		entityManager.refresh(reimbursement);
		if (!rodKeyList.contains(reimbursement.getKey())) {
			DocAcknowledgement docAcknowLedgement = reimbursement
					.getDocAcknowLedgement();
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
			setReimbursementToDTO(documentList, reimbursement,
					docAcknowLedgement, documentDetailsDTO);
			EarlierRodMapper.invalidate(instance);
		}
	}


	if (dbTaskForCloseClaim != null && ! dbTaskForCloseClaim.isEmpty()) {
		getDocumentListByHumanTask(dbTaskForCloseClaim, documentList);
	}


	return documentList;
		
		
	}	


public List<ViewDocumentDetailsDTO> getDocumentDetailsForCloseClaimInProcess(
		String intimationNo, String userName, String passWord) {


	/**
	 * Method to be refractored.
	 * */
	/*PayloadBOType payloadType = new PayloadBOType();
	IntimationType intimationType = new IntimationType();
	intimationType.setIntimationNumber(intimationNo);
	payloadType.setIntimation(intimationType);

	CloseClaimTask closeClaimInProcessTask = BPMClientContext
			.getCloseClaimInProcessTask(userName, passWord);

	PagedTaskList tasks = closeClaimInProcessTask.getTasks(userName, null,
			payloadType);*/

	List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

	/**
	 * Method to be refractored.
	 * */
/*		if (tasks != null) {

//		getDocumentListByHumanTask(tasks, documentList);

	}*/
	return documentList;
}
	
public List<ViewDocumentDetailsDTO> getDocumentDetailsForPACloseClaimInProcess(String intimationNo, String userName,String passWord){


	List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

	/**
	 * Method to be refractored.
	 * */
/*		if (tasks != null) {

//		getDocumentListByHumanTask(tasks, documentList);

	}*/
	return documentList;
}
	
	

private void getDocumentListByHumanTask(List<Map<String, Object>> tasks,
		List<ViewDocumentDetailsDTO> documentList) {

	
 for (Map<String, Object> map : tasks) {
//		PayloadBOType reimbursementPayload = humanTask.getPayload();
//		RODType rod = reimbursementPayload.getRod();
	    String  currentQ = (String)map.get(SHAConstants.CURRENT_Q);
	    String reimbReqBy = (String)map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
	    if(reimbReqBy != null && (reimbReqBy.equalsIgnoreCase(SHAConstants.MA_CURRENT_QUEUE) || reimbReqBy.equalsIgnoreCase(SHAConstants.ZMR_CURRENT_QUEUE))){
	    	if(! SHAConstants.getNonClosureCurrentQ().contains(currentQ) && ! SHAConstants.queryCurrentQ().contains(currentQ)){
	    		Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
	    		if (reimbursementKey != null && ! reimbursementKey.equals(0l)) {

	    			Reimbursement reimbursement = getReimbursement(reimbursementKey);
	    			DocAcknowledgement docAcknowLedgement = reimbursement
	    					.getDocAcknowLedgement();
	    			EarlierRodMapper instance = EarlierRodMapper.getInstance();
	    			ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
//	    			documentDetailsDTO.setTaskNumber(humanTask.getNumber());
	    			documentDetailsDTO.setDbOutArray(map);

	    			setReimbursementToDTO(documentList, reimbursement,
	    					docAcknowLedgement, documentDetailsDTO);
	    			EarlierRodMapper.invalidate(instance);
	    		}
	    	}
	    }else{
	    	Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);

			if (reimbursementKey != null && ! reimbursementKey.equals(0l)) {

				Reimbursement reimbursement = getReimbursement(reimbursementKey);
				DocAcknowledgement docAcknowLedgement = reimbursement
						.getDocAcknowLedgement();
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
//				documentDetailsDTO.setTaskNumber(humanTask.getNumber());
				documentDetailsDTO.setDbOutArray(map);

				setReimbursementToDTO(documentList, reimbursement,
						docAcknowLedgement, documentDetailsDTO);
				EarlierRodMapper.invalidate(instance);
			}
	    }
	}
}

	private void setReimbursementToDTO(
			List<ViewDocumentDetailsDTO> documentList,
			Reimbursement reimbursement, DocAcknowledgement docAcknowLedgement,
			ViewDocumentDetailsDTO documentDetailsDTO) {
		String benefitCover = "";
		String coverValue = "";
		Boolean isPA = false;
		String documentClassification = documentClassification(docAcknowLedgement);
		documentDetailsDTO.setBillClassification(documentClassification);

		documentDetailsDTO.setRodNumber(reimbursement.getRodNumber());
		documentDetailsDTO.setReimbursementKey(reimbursement.getKey());
		// documentDetailsDTO.setMedicalResponseTime(reimbursement
		// .getMedicalCompletedDate());

		documentDetailsDTO.setApprovedAmount(reimbursement
				.getCurrentProvisionAmt());

		documentDetailsDTO.setStatus(reimbursement.getStatus()
				.getProcessValue());

		if (reimbursement.getMedicalCompletedDate() != null) {
			documentDetailsDTO.setMedicalResponseTime(SHAUtils
					.formatDate(reimbursement.getMedicalCompletedDate()));
		}

		if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.BILLING_STAGE)) {

			Long approvedAmount = getReimbursementApprovedAmount(reimbursement
					.getKey());
			if (approvedAmount > 0) {
				documentDetailsDTO.setApprovedAmount(approvedAmount
						.doubleValue());
			}

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.FINANCIAL_STAGE)) {
			Long approvedAmount = getReimbursementApprovedAmount(reimbursement
					.getKey());
			if (approvedAmount > 0) {
				documentDetailsDTO.setApprovedAmount(approvedAmount
						.doubleValue());
			}
		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_STAGE)) {
			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
					|| reimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_CLOSED)
					&& reimbursement.getApprovalRemarks() != null
					|| reimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_REOPENED)
					&& reimbursement.getApprovalRemarks() != null) {

				Double approvedAmount = reimbursement.getApprovedAmount() != null ? reimbursement.getApprovedAmount() : 0d;
				if (reimbursement.getDocAcknowLedgement() != null) {
					if (reimbursement.getDocAcknowLedgement()
							.getPreHospitalizationClaimedAmount() != null) {
						approvedAmount += reimbursement.getDocAcknowLedgement()
								.getPreHospitalizationClaimedAmount();
					}
					if (reimbursement.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null) {
						approvedAmount += reimbursement.getDocAcknowLedgement()
								.getPostHospitalizationClaimedAmount();
					}
				}
				documentDetailsDTO.setApprovedAmount(approvedAmount);
			} else {
				documentDetailsDTO
						.setApprovedAmount(getClaimedAmount(reimbursement));
			}

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)) {
//			documentDetailsDTO
//					.setApprovedAmount(getClaimedAmount(reimbursement));
			
			
			documentDetailsDTO
			.setApprovedAmount(reimbursement.getCurrentProvisionAmt());
			
			
		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.CREATE_ROD_STAGE_KEY)) {
			documentDetailsDTO
					.setApprovedAmount(getClaimedAmount(reimbursement));
		}

		if (ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement
				.getStatus().getKey())) {
			
			
			CloseClaim closeClaimByReimbursementKey = getCloseClaimByReimbursementKey(reimbursement.getKey());
			
			if(closeClaimByReimbursementKey != null){
				
				if(closeClaimByReimbursementKey.getClosedProvisionAmt() != null && ! closeClaimByReimbursementKey.getClosedProvisionAmt().equals(0d)){
					documentDetailsDTO.setApprovedAmount(closeClaimByReimbursementKey.getClosedProvisionAmt());
				}else{
					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());
				}
				
			}else{
				documentDetailsDTO.setApprovedAmount(reimbursement
						.getCurrentProvisionAmt());
			}
			
			Map<String, Object> map = new WeakHashMap<String, Object>();
			if(documentDetailsDTO.getDbOutArray() == null){
				documentDetailsDTO.setDbOutArray(map);
			}
			
			
			

//			 Double provisionAmount = getProvisionAmount(reimbursement);
//			 documentDetailsDTO.setApprovedAmount(provisionAmount);

			// if(reimbursement.getCurrentProvisionAmt() > 0){
//			documentDetailsDTO.setApprovedAmount(reimbursement
//					.getCurrentProvisionAmt());
			// }

		} else if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
				.containsKey(reimbursement.getStatus().getKey())) {
			// Double provisionAmount = getProvisionAmount(reimbursement);
			// documentDetailsDTO.setApprovedAmount(provisionAmount);
			// if(reimbursement.getCurrentProvisionAmt() > 0){
			documentDetailsDTO.setApprovedAmount(reimbursement
					.getCurrentProvisionAmt());
			// }
		}		
		if(reimbursement != null && reimbursement.getClaim() != null && reimbursement.getClaim().getLobId() != null){
			if((ReferenceTable.PA_LOB_KEY).equals(reimbursement.getClaim().getLobId())){
				isPA = true;
			}
		}
		
		if(reimbursement.getBenefitsId() != null)
		{
			benefitCover = reimbursement.getBenefitsId().getValue();
			
		}
		
		coverValue = getCoverValueForViewBasedOnrodKey(reimbursement.getKey());
		
		if(benefitCover.isEmpty())
		{
			documentDetailsDTO.setBenefits(coverValue);
		}
		else
		{
			documentDetailsDTO.setBenefits(benefitCover+", "+coverValue);
		}
		
		if(isPA)
		{
			documentDetailsDTO.setApprovedAmount(getBenefitAddOnOptionalApprovedAmt(reimbursement));
		}	
		if(reimbursement.getStage().getKey().equals(ReferenceTable.PAYMENT_PROCESS_STAGE) && 
				(reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_PROCESS_CLOSED))){
			documentDetailsDTO.setRodStatusKey(ReferenceTable.PAYMENT_PROCESS_CLOSED);
		}

		documentList.add(documentDetailsDTO);
	}
	
public CloseClaim getCloseClaimByReimbursementKey(Long reimbursementKey){
		
		Query closeClaimQuery = entityManager
				.createNamedQuery("CloseClaim.getByReimbursmentKey");
		closeClaimQuery.setParameter("reimbursmentKey", reimbursementKey);

		List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
		
		if(closedClaimList != null && ! closedClaimList.isEmpty()){
			return closedClaimList.get(0);
		}
		
		return null;
	}

	public Double getProvisionAmount(Reimbursement reimbursement) {

		Double claimedAmount = 0d;

		if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.CREATE_ROD_STAGE_KEY)) {

			claimedAmount = getClaimedAmount(reimbursement);

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)) {

			claimedAmount = getClaimedAmount(reimbursement);

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_STAGE)) {

			claimedAmount = getClaimedAmount(reimbursement);

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_STAGE)) {

			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
					|| reimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_CLOSED)
					&& reimbursement.getApprovalRemarks() != null) {

				claimedAmount = reimbursement.getApprovedAmount() != null ? reimbursement.getApprovedAmount() : 0d;

				if (reimbursement.getDocAcknowLedgement() != null) {
					if (reimbursement.getDocAcknowLedgement()
							.getPreHospitalizationClaimedAmount() != null) {
						claimedAmount += reimbursement.getDocAcknowLedgement()
								.getPreHospitalizationClaimedAmount();
					}
					if (reimbursement.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null) {
						claimedAmount += reimbursement.getDocAcknowLedgement()
								.getPostHospitalizationClaimedAmount();
					}
				}

			} else {
				claimedAmount = getClaimedAmount(reimbursement); // will be
																	// executed
																	// if staus
																	// is refer
																	// to
																	// specialist,
																	// escalate,
																	// investigation
																	// from MA
																	// stage
			}

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.BILLING_STAGE)) {

			Long reimbursementApprovedAmount = getReimbursementApprovedAmount(reimbursement
					.getKey());

			claimedAmount = Double.valueOf(reimbursementApprovedAmount);

		} else if (reimbursement.getStage().getKey()
				.equals(ReferenceTable.FINANCIAL_STAGE)) {

			Long reimbursementApprovedAmount = getReimbursementApprovedAmount(reimbursement
					.getKey());

			claimedAmount = Double.valueOf(reimbursementApprovedAmount);
		}

		return claimedAmount;

	}

	public Double getClaimedAmount(Reimbursement reimbursement) {

		DocAcknowledgement docAcknowLedgement = reimbursement
				.getDocAcknowLedgement();

		Double totalClaimedAmount = 0d;

		totalClaimedAmount = getClaimedAmountForReimbursement(reimbursement);

		if (reimbursement.getClaim().getClaimType() != null
				&& reimbursement.getClaim().getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

			if (docAcknowLedgement.getDocumentReceivedFromId() != null
					&& docAcknowLedgement.getDocumentReceivedFromId().getKey()
							.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)
					&& docAcknowLedgement.getHospitalisationFlag() != null
					&& docAcknowLedgement.getHospitalisationFlag()
							.equalsIgnoreCase("Y")
					&& docAcknowLedgement.getPreHospitalisationFlag() != null
					&& docAcknowLedgement.getPreHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& docAcknowLedgement.getPostHospitalisationFlag() != null
					&& docAcknowLedgement.getPostHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& docAcknowLedgement.getPartialHospitalisationFlag() != null
					&& docAcknowLedgement.getPartialHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& docAcknowLedgement.getLumpsumAmountFlag() != null
					&& docAcknowLedgement.getLumpsumAmountFlag()
							.equalsIgnoreCase("N")
					&& docAcknowLedgement.getPatientCareFlag() != null
					&& docAcknowLedgement.getPatientCareFlag()
							.equalsIgnoreCase("N")
					&& docAcknowLedgement.getHospitalCashFlag() != null
					&& docAcknowLedgement.getHospitalCashFlag()
							.equalsIgnoreCase("N")) {

				totalClaimedAmount = getPreauthApprovedAmt(reimbursement
						.getClaim().getKey());

			}
		}

		Double balanceSI = getBalanceSI(reimbursement);
		if (balanceSI != null) {

			totalClaimedAmount = Math.min(balanceSI, totalClaimedAmount);

		}

		return totalClaimedAmount;
	}

	public Double getBalanceSI(Reimbursement reimbursement) {
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Long policyKey = reimbursement.getClaim().getIntimation().getPolicy()
				.getKey();
		if (null != reimbursement.getClaim().getIntimation().getInsured()
				.getInsuredId()) {
			sumInsured = dbCalculationService.getInsuredSumInsured(
					String.valueOf(reimbursement.getClaim().getIntimation()
							.getInsured().getInsuredId()), policyKey,reimbursement.getClaim().getIntimation()
							.getInsured().getLopFlag());
		}
		Double balanceSI = 0d;
		if(ReferenceTable.getGMCProductList().containsKey(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(policyKey,
					reimbursement.getClaim().getIntimation().getInsured().getKey(),
					reimbursement.getClaim().getKey());
		}else{
			Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey,
					reimbursement.getClaim().getIntimation().getInsured().getKey(),
					reimbursement.getClaim().getKey(), sumInsured, reimbursement
							.getClaim().getIntimation().getKey());
			balanceSI = (Double) balanceSIMap
					.get(SHAConstants.TOTAL_BALANCE_SI);
		}
		
		return balanceSI;

	}

	private Double getPreauthApprovedAmt(Long claimKey) {

		Double approvedAmt = 0d;

		Query query = entityManager
				.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);

		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		if (null != preauthList && !preauthList.isEmpty()) {
			Preauth preauth = preauthList.get(0);
			approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth
					.getTotalApprovalAmount() : 0d;
			/*
			 * for (Preauth preauth : preauthList) { approvedAmt +=
			 * preauth.getTotalApprovalAmount() != null ?
			 * preauth.getTotalApprovalAmount() : 0d; }
			 */
		}
		return approvedAmt;

	}

	public Double getClaimedAmountForReimbursement(Reimbursement reimbursement) {
		try {
			Double claimedAmount = 0.0;

			DocAcknowledgement docAcknowledgment = reimbursement
					.getDocAcknowLedgement();

			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter(
					"rodKey", reimbursement.getKey());
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1
					.getResultList();
			Double currentProvisionalAmount = 0.0;
			for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList) {
				currentProvisionalAmount += reimbursementBenefits
						.getTotalClaimAmountBills();

			}

			Double hospitalizationClaimedAmount = null != docAcknowledgment
					.getHospitalizationClaimedAmount() ? docAcknowledgment
					.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgment
					.getPostHospitalizationClaimedAmount() ? docAcknowledgment
					.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgment
					.getPreHospitalizationClaimedAmount() ? docAcknowledgment
					.getPreHospitalizationClaimedAmount() : 0.0;
			Double otherbenefitClaimedAmount = null != docAcknowledgment
					.getOtherBenefitsClaimedAmount() ? docAcknowledgment
					.getOtherBenefitsClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount
					+ postHospitalizationClaimedAmount
					+ preHospitalizationClaimedAmount
					+ otherbenefitClaimedAmount
					+ currentProvisionalAmount;

			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}

	public List<ViewDocumentDetailsDTO> getAcknowledgmentForCancel(
			Long acknowledgementKey) {

		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(acknowledgementKey);
		List<ViewDocumentDetailsDTO> documentDetailsDTO = new ArrayList<ViewDocumentDetailsDTO>();
		if (docAcknowledgement != null) {
			ViewDocumentDetailsDTO docAcknowledgementDTO = new ViewDocumentDetailsDTO();
			docAcknowledgementDTO.setAcknowledgeNumber(docAcknowledgement
					.getAcknowledgeNumber());
			if (docAcknowledgement.getDocumentReceivedFromId() != null) {
				docAcknowledgementDTO.setReceivedFromValue(docAcknowledgement
						.getDocumentReceivedFromId().getValue());
			}
			docAcknowledgementDTO.setDocumentReceivedDate(docAcknowledgement
					.getDocumentReceivedDate());
			if (docAcknowledgement.getModeOfReceiptId() != null) {
				docAcknowledgementDTO.setModeOfReceiptValue(docAcknowledgement
						.getModeOfReceiptId().getValue());
			}
			String documentClassification = documentClassification(docAcknowledgement);
			docAcknowledgementDTO.setBillClassification(documentClassification);

			
			//added for PA
			if (docAcknowledgement.getBenifitFlag() != null) {
				
				if(getCoverValueForViewBasedOnAckKey(docAcknowledgement
						.getKey()).isEmpty()){
					docAcknowledgementDTO.setBenefits(SHAUtils
							.getBenefitsValue(docAcknowledgement.getBenifitFlag()));
				}else{
					docAcknowledgementDTO.setBenefits(SHAUtils
							.getBenefitsValue(docAcknowledgement.getBenifitFlag())
							+ ", "
							+ getCoverValueForViewBasedOnAckKey(docAcknowledgement
									.getKey()));
				}
				
			} else {
				docAcknowledgementDTO
						.setBenefits(getCoverValueForViewBasedOnAckKey(docAcknowledgement
								.getKey()));
			}
			

			Double claimedAmount = 0d;
			if (docAcknowledgement.getHospitalizationClaimedAmount() != null) {
				claimedAmount += docAcknowledgement
						.getHospitalizationClaimedAmount();
			}
			if (docAcknowledgement.getPreHospitalizationClaimedAmount() != null) {
				claimedAmount += docAcknowledgement
						.getPreHospitalizationClaimedAmount();
			}
			if (docAcknowledgement.getPostHospitalizationClaimedAmount() != null) {
				claimedAmount += docAcknowledgement
						.getPostHospitalizationClaimedAmount();
			}


			docAcknowledgementDTO.setApprovedAmount(claimedAmount);
			docAcknowledgementDTO.setStatus(docAcknowledgement.getStatus()
					.getProcessValue());


			
			if(docAcknowledgement.getClaim() != null && docAcknowledgement.getClaim().getLobId() != null && docAcknowledgement.getClaim().getLobId().equals(ReferenceTable.PA_LOB_KEY)){
				Double paclaimedAmt = getClaimedAmountValueForView(docAcknowledgement);
				docAcknowledgementDTO.setApprovedAmount(paclaimedAmt);
			}else{
				docAcknowledgementDTO.setApprovedAmount(claimedAmount);
			}
			
			docAcknowledgementDTO.setStatus(docAcknowledgement.getStatus().getProcessValue());

			documentDetailsDTO.add(docAcknowledgementDTO);

		}

		return documentDetailsDTO;
	}

	public String documentClassification(DocAcknowledgement docAcknowledgement) {

		String classification = "";
		if (docAcknowledgement.getPreHospitalisationFlag() != null) {
			if (docAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
				if (classification.equals("")) {
					classification = "Pre-Hospitalisation";
				} else {
					classification = classification + ","
							+ "Pre-Hospitalisation";
				}
			}
		}
		if (docAcknowledgement.getHospitalisationFlag() != null) {
			if (docAcknowledgement.getHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalisation";
				} else {
					classification = classification + "," + " Hospitalisation";
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

		if (docAcknowledgement.getHospitalCashFlag() != null) {

			if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Hospital cash)";
				}
			}
		}

		if (docAcknowledgement.getPatientCareFlag() != null) {

			if (docAcknowledgement.getPatientCareFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Patient Care)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Patient Care)";
				}
			}
		}

		if (docAcknowledgement.getLumpsumAmountFlag() != null) {

			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Lumpsum Amount";
				} else {
					classification = classification + "," + "Lumpsum Amount";
				}
			}
		}

		if (docAcknowledgement.getHospitalizationRepeatFlag() != null) {

			if (docAcknowledgement.getHospitalizationRepeatFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalization Repeat";
				} else {
					classification = classification + ","
							+ "Hospitalization Repeat";
				}
			}
		}

		if (docAcknowledgement.getPartialHospitalisationFlag() != null) {

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

	public void submitCancelAcknowlegement(ReceiptOfDocumentsDTO rodDTO) {

		Long acknowledgementKey = rodDTO.getDocumentDetails()
				.getDocAcknowledgementKey();

		DocAcknowledgement docacknowledgement = getDocAcknowledgementBasedOnKey(acknowledgementKey);

		Status status = entityManager.find(Status.class,
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		Stage stage = entityManager.find(Stage.class,
				ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

		docacknowledgement.setStage(stage);
		docacknowledgement.setStatus(status);

		String cancelAcknowledgementRemarks = rodDTO.getDocumentDetails()
				.getCancelAcknowledgementRemarks();
		docacknowledgement.setCancelRemarks(cancelAcknowledgementRemarks);

		SelectValue cancelAcknowledgementReason = rodDTO.getDocumentDetails()
				.getCancelAcknowledgementReason();

		MastersValue reason = entityManager.find(MastersValue.class,
				cancelAcknowledgementReason.getId());
		docacknowledgement.setCancelReason(reason);

		entityManager.merge(docacknowledgement);
		entityManager.flush();
		log.info("------DocAcknowledgement------>" + docacknowledgement
				+ "<------------");

		// need to implement bpmn task

	}

	@SuppressWarnings("unchecked")
	public List<ReimbursementCalCulationDetails> getReimbursementCalculationDetails(
			Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
		query = query.setParameter("reimbursementKey", reimbursementKey);

		List<ReimbursementCalCulationDetails> reimbursmentCalculationDetails = (List<ReimbursementCalCulationDetails>) query
				.getResultList();

		return reimbursmentCalculationDetails;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ReimbursementBenefits> getReimbursmentBenefits(
			Long reimbursementKey) {

		Query query = entityManager.createNamedQuery(
				"ReimbursementBenefits.findByRodKey").setParameter("rodKey",
				reimbursementKey);

		List<ReimbursementBenefits> reimbursmentBenefits = (List<ReimbursementBenefits>) query
				.getResultList();
		return reimbursmentBenefits;

	}

	public Long getReimbursementApprovedAmount(Long reimbursementKey) {

		Long approvedAmount = 0l;

		List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = getReimbursementCalculationDetails(reimbursementKey);

		Reimbursement reimbursement = getReimbursement(reimbursementKey);

		if (reimbursementCalculationDetails != null) {
			for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {

				if (reimbursement.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
						&& reimbursement.getDocAcknowLedgement()
								.getDocumentReceivedFromId().getKey()
								.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
					if (reimbursementCalCulationDetails2
							.getPayableToHospAftTDS() != null) {
						if (reimbursementCalCulationDetails2
								.getPayableToHospAftTDS() > 0) {
							approvedAmount += reimbursementCalCulationDetails2
									.getPayableToHospAftTDS();
						}
					}
					// if(reimbursementCalCulationDetails2.getPayableToInsured()
					// != null){
					// if(reimbursementCalCulationDetails2.getPayableToInsured()>0){
					// approvedAmount +=
					// reimbursementCalCulationDetails2.getPayableToInsured();
					// }
					// }
				} else {
					if (reimbursementCalCulationDetails2.getPayableToInsured() != null) {
						approvedAmount += reimbursementCalCulationDetails2
								.getPayableInsuredAfterPremium();
					}
				}
			}
		}

		List<ReimbursementBenefits> reimbursmentBenefits = getReimbursmentBenefits(reimbursementKey);

		if (reimbursmentBenefits != null) {
			for (ReimbursementBenefits reimbursementBenefits : reimbursmentBenefits) {
				if (reimbursementBenefits.getPayableAmount() != null) {
					approvedAmount += reimbursementBenefits.getPayableAmount()
							.longValue();
				}
			}
		}
		return approvedAmount;
	}

	public DocAcknowledgement findAcknowledgmentByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);

		// Integer.parseInt(Strin)
		List<DocAcknowledgement> earlierAck = (List<DocAcknowledgement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();

		for (DocAcknowledgement docAcknowledgement : earlierAck) {

			keys.add(docAcknowledgement.getKey());

		}
		if (!keys.isEmpty()) {
			Long maxKey = Collections.max(keys);
			DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(maxKey);
			return docAcknowledgement;
		}
		return null;

	}

	public DocAcknowledgement findAcknowledgment(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);

		// Integer.parseInt(Strin)
		List<DocAcknowledgement> earlierAck = (List<DocAcknowledgement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();

		for (DocAcknowledgement docAcknowledgement : earlierAck) {

			keys.add(docAcknowledgement.getKey());

		}
		if (!keys.isEmpty()) {
			Long maxKey = Collections.max(keys);
			DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(maxKey);
			return docAcknowledgement;
		}
		return null;
	}

	public Claim searchByClaimKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
	}

	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<List<DocumentCheckListDTO>> getDocumentListForClaim(
			MasterService masterService, Claim claim) {
		List<List<DocumentCheckListDTO>> previousDocCheckList = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claim.getKey());

		List<DocAcknowledgement> docAckObjList = (List<DocAcknowledgement>) query
				.getResultList();
		if (null != docAckObjList && !docAckObjList.isEmpty()) {
			previousDocCheckList = new ArrayList<List<DocumentCheckListDTO>>();
			// List<DocumentCheckListDTO> docCheckList = new
			// ArrayList<DocumentCheckListDTO>();
			for (DocAcknowledgement docAck : docAckObjList) {
				Query query1 = entityManager
						.createNamedQuery("RODDocumentCheckList.findByDocAck");
				query1.setParameter("docAck", docAck);
				List<RODDocumentCheckList> docList = (List<RODDocumentCheckList>) query1
						.getResultList();
				List<DocumentCheckListDTO> docChkList = new ArrayList<DocumentCheckListDTO>();

				AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper = AcknowledgeDocumentReceivedMapper
						.getInstance();
				docChkList = acknowledgeDocumentReceivedMapper
						.getRODDocumentCheckList(docList);
				previousDocCheckList.add(docChkList);
			}
			return previousDocCheckList;
		} else {
			return previousDocCheckList;
		}

	}

	@SuppressWarnings("unchecked")
	public List<RODQueryDetailsDTO> getRODQueryDetails(Claim claim) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (Reimbursement rod : rodList) {
				
				if(! ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(rod.getStatus().getKey())){
				
				Reimbursement reimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getRodQueryBasedOnRODKey(rod
						.getKey());

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),0d));
							if(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag() != null && 
									reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag().equalsIgnoreCase("Y")){
								List<RODBillDetails> billEntryDetails = getBillEntryDetails(reimbursement.getKey());
								if(billEntryDetails != null){
									Double billAmount = 0d;
									for (RODBillDetails rodBillDetails : billEntryDetails) {
										billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
									}
									rodQueryDetails.setClaimedAmount(billAmount);
								}
							}
							
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rodQuery.getStatus().getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							rodQueryDetails.setRelaInstalFlag(rodQuery.getRelInstaFlg());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								
								if(null != reimbursement.getClaim().getIntimation().getLobId() && 
										(ReferenceTable.HEALTH_LOB_KEY.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) ||
												(ReferenceTable.PACKAGE_MASTER_VALUE.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) && 
														SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(reimbursement.getClaim().getProcessClaimType()))))
								{
									
									rodQueryDetails.setClaimedAmount(getDocAckClaimedAmount(doc));
									rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								/*rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								else
								{
									rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								}
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rodQuery.getStatus().getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								if(rodQuery.getQueryReply() != null){
									rodQueryDetails.setOnPageLoad(true);
								}
								
								rodQueryDetails.setBenefitFlag(doc.getBenifitFlag());
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double addOnCoversAmt = 0d;
								
								String additionalCover = "";
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								
								
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								
								rodQueryDetailsList.add(rodQueryDetails);
								

							}
							if (rodQuery.getQueryReply() != null) {
								rodQueryDetails.setOnPageLoad(true);
							}
							
							rodQueryDetails.setReimbursement(rod);
							
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}
			}
			}
		}
		return rodQueryDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<RODQueryDetailsDTO> getPARODQueryDetails(Claim claim) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (Reimbursement rod : rodList) {

				Reimbursement reimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getPARodQueryBasedOnRODKey(rod
						.getKey());

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),0d));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								
								if(null != reimbursement.getClaim().getIntimation().getLobId() && 
										(ReferenceTable.HEALTH_LOB_KEY.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) ||
												(ReferenceTable.PACKAGE_MASTER_VALUE.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) && 
														SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(reimbursement.getClaim().getProcessClaimType()))))
								{
									
									rodQueryDetails.setClaimedAmount(getDocAckClaimedAmount(doc));
									rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								/*rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								else
								{
									rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								}
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rod.getStatus()
										.getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								if(rodQuery.getQueryReply() != null){
									rodQueryDetails.setOnPageLoad(true);
								}
								
								rodQueryDetails.setBenefitFlag(doc.getBenifitFlag());
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double addOnCoversAmt = 0d;
								
								String additionalCover = "";
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								
								
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								
								rodQueryDetailsList.add(rodQueryDetails);
								

							}
							if (rodQuery.getQueryReply() != null) {
								rodQueryDetails.setOnPageLoad(true);
							}
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}

	@SuppressWarnings("unchecked")
	public List<RODQueryDetailsDTO> getPAPaymentRODQueryDetails(Claim claim) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (Reimbursement rod : rodList) {

				Reimbursement reimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getPARodPaymentQueryBasedOnRODKey(rod
						.getKey());

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),0d));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								
								if(null != reimbursement.getClaim().getIntimation().getLobId() && 
										(ReferenceTable.HEALTH_LOB_KEY.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) ||
												(ReferenceTable.PACKAGE_MASTER_VALUE.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) && 
														SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(reimbursement.getClaim().getProcessClaimType()))))
								{
									
									rodQueryDetails.setClaimedAmount(getDocAckClaimedAmount(doc));
									rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								/*rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								else
								{
									rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								}
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rod.getStatus()
										.getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								if(rodQuery.getQueryReply() != null){
									rodQueryDetails.setOnPageLoad(true);
								}
								
								rodQueryDetails.setBenefitFlag(doc.getBenifitFlag());
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double addOnCoversAmt = 0d;
								
								String additionalCover = "";
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										MasPaClaimCovers coverName = getCoversName(coverId);
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription() + ",";
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								
								
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								
								rodQueryDetailsList.add(rodQueryDetails);
							}
							if (rodQuery.getQueryReply() != null) {
								rodQueryDetails.setOnPageLoad(true);
							}
							rodQueryDetailsList.add(rodQueryDetails);


						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}
	
	public String getDiagnosisBasedOnRODKey(Long key) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", key);
		List<PedValidation> pedValList = query.getResultList();
		StringBuilder strDiag = null;
		if (null != pedValList && !pedValList.isEmpty()) {
			strDiag = new StringBuilder();
			for (PedValidation pedValidation : pedValList) {
				Query query1 = entityManager
						.createNamedQuery("Diagnosis.findDiagnosisByKey");
				query1.setParameter("diagnosisKey",
						pedValidation.getDiagnosisId());
				List<Diagnosis> diag = (List<Diagnosis>) query1.getResultList();
				for (Diagnosis diagnosis : diag) {
					strDiag.append(diagnosis.getValue());
					strDiag.append(",");
				}
			
			}
		}
		return String.valueOf(strDiag);
	}

	private List<ReimbursementQuery> getRodQueryBasedOnRODKey(Long rodKey) {

		List<ReimbursementQuery> filterQuery = new ArrayList<ReimbursementQuery>();

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForQuery");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> mainResultQuery = query.getResultList();

		for (ReimbursementQuery reimbursementQuery : mainResultQuery) {
			entityManager.refresh(reimbursementQuery);
			if (reimbursementQuery.getStatus() != null
					&& (reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)) || 
							reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)) {
				filterQuery.add(reimbursementQuery);
			}
		}
		return filterQuery;

	}

	private List<ReimbursementQuery> getPARodQueryBasedOnRODKey(Long rodKey) {

		List<ReimbursementQuery> filterQuery = new ArrayList<ReimbursementQuery>();

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForQueryPA");
		query.setParameter("reimbursementKey", rodKey);
		

		List<ReimbursementQuery> mainResultQuery = query.getResultList();

		for (ReimbursementQuery reimbursementQuery : mainResultQuery) {
			entityManager.refresh(reimbursementQuery);
			if (reimbursementQuery.getStatus() != null
					&& (reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)) || 
							reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)) {
				filterQuery.add(reimbursementQuery);
			}
		}
		return filterQuery;

	}
	
	private List<ReimbursementQuery> getPARodPaymentQueryBasedOnRODKey(Long rodKey) {

		List<ReimbursementQuery> filterQuery = new ArrayList<ReimbursementQuery>();

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForPaymentQueryPA");
		query.setParameter("reimbursementKey", rodKey);
		

		List<ReimbursementQuery> mainResultQuery = query.getResultList();

		for (ReimbursementQuery reimbursementQuery : mainResultQuery) {
			entityManager.refresh(reimbursementQuery);
			if (reimbursementQuery.getStatus() != null
					&& (reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)) || 
							reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)) {
				filterQuery.add(reimbursementQuery);
			}
		}
		return filterQuery;

	}
	public List<RODQueryDetailsDTO> getRODQueryDetailsForCreateRodAndBillEntry(
			Claim claim, Long ackKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (Reimbursement rod : rodList) {

				if(! ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(rod.getStatus().getKey())){
				Reimbursement reimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getRodQueryBasedOnAckKey(
						rod.getKey(), ackKey);

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),0d));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rodQuery.getStatus().getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							rodQueryDetails.setRelaInstalFlag(rodQuery.getRelInstaFlg());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails

										.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
							/*	rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								//GALAXYMAIN-13431
								if(null != reimbursement.getClaim().getIntimation().getLobId() && 
										(ReferenceTable.HEALTH_LOB_KEY.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) ||
												(ReferenceTable.PACKAGE_MASTER_VALUE.equals(reimbursement.getClaim().getIntimation().getLobId().getKey()) && 
														SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(reimbursement.getClaim().getProcessClaimType()))))
								{
									
									rodQueryDetails.setClaimedAmount(getDocAckClaimedAmount(doc));
									rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
								else
								{
									rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								}
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rodQuery.getStatus().getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								
								if(rodQuery.getQueryReply() != null && rodQuery.getQueryReply().equalsIgnoreCase("N")){
									rodQueryDetails.setOnPageLoad(true);
								}
//								rodQueryDetails.setOnPageLoad(true);
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								
								String additionalCover = "";
								Double addOnCoversAmt = 0d;
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();								
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								
								rodQueryDetails.setReimbursement(rod);
								rodQueryDetailsList.add(rodQueryDetails);								

							}

							if (rodQuery.getQueryReply() != null
									&& rodQuery.getQueryReply()
											.equalsIgnoreCase("N")) {
								rodQueryDetails.setOnPageLoad(true);
							}
							// rodQueryDetails.setOnPageLoad(true);
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}
			}
			}
		}
		return rodQueryDetailsList;
	}
	
	
	public List<RODQueryDetailsDTO> getRODPaymentQueryDetailsForCreateRodAndBillEntry(
			Claim claim, Long ackKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			Reimbursement reimbursement = null;
			List<ReimbursementQuery> rodQueryList = null;
			for (Reimbursement rod : rodList) {

				reimbursement = getReimbursement(rod.getKey());

				rodQueryList = getPARodPAymentQueryBasedOnAckKey(
						rod.getKey(), ackKey);

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());
				RODQueryDetailsDTO rodQueryDetails = null;
				for (ReimbursementQuery rodQuery : rodQueryList) {

					rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),
											doc.getOtherBenefitsClaimedAmount()));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							rodQueryDetails.setRelaInstalFlag(rodQuery.getRelInstaFlg());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails

										.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
							/*	rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rod.getStatus()
										.getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								
								if(rodQuery.getQueryReply() != null && rodQuery.getQueryReply().equalsIgnoreCase("N")){
									rodQueryDetails.setOnPageLoad(true);
								}
//								rodQueryDetails.setOnPageLoad(true);
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								
								String additionalCover = "";
								Double addOnCoversAmt = 0d;
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();								
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								rodQueryDetailsList.add(rodQueryDetails);								

							}

							if (rodQuery.getQueryReply() != null
									&& rodQuery.getQueryReply()
											.equalsIgnoreCase("N")) {
								rodQueryDetails.setOnPageLoad(true);
							}
							// rodQueryDetails.setOnPageLoad(true);
							rodQueryDetailsList.add(rodQueryDetails);
							rodQueryDetails = null;
						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}

	private List<ReimbursementQuery> getRodQueryBasedOnAckKey(Long rodKey,
			Long ackKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();
		List<ReimbursementQuery> approvedQuery = new ArrayList<ReimbursementQuery>();

		
        for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			if(reimbursementQuery2.getStatus() != null && (reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS)||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.FA_QUERY_REPLY_STATUS) || 
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) ||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) ||
				    reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){

				approvedQuery.add(reimbursementQuery2);
			}
		}

		List<ReimbursementQuery> filterRodQuery = new ArrayList<ReimbursementQuery>();

		for (ReimbursementQuery reimbursementQuery2 : approvedQuery) {
			entityManager.refresh(reimbursementQuery2);

			if (reimbursementQuery2.getQueryReply() == null
					|| (reimbursementQuery2.getQueryReply() != null && reimbursementQuery2
							.getQueryReply().equalsIgnoreCase("N"))) {
				//IMSSUPPOR-27722 - fix for query received rod and ack cancelled case
				if (reimbursementQuery2.getDocAcknowledgement() != null
						&& reimbursementQuery2.getDocAcknowledgement().getKey()
								.equals(ackKey)){
				filterRodQuery.add(reimbursementQuery2);
				}
			} else if (reimbursementQuery2.getQueryReply() != null
					&& reimbursementQuery2.getQueryReply()
							.equalsIgnoreCase("Y")) {
				if (reimbursementQuery2.getDocAcknowledgement() != null
						&& reimbursementQuery2.getDocAcknowledgement().getKey()
								.equals(ackKey)) {
					filterRodQuery.add(reimbursementQuery2);
				}
				
			}

		}
		return filterRodQuery;
	}

	private List<ReimbursementQuery> getPARodPAymentQueryBasedOnAckKey(Long rodKey,
			Long ackKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForPaymentQueryPA");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();
		List<ReimbursementQuery> approvedQuery = new ArrayList<ReimbursementQuery>();

		
        for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			if(reimbursementQuery2.getStatus() != null && (reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS)||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.FA_QUERY_REPLY_STATUS) || 
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) ||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) ||
				    reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){

				approvedQuery.add(reimbursementQuery2);
			}
		}

		List<ReimbursementQuery> filterRodQuery = new ArrayList<ReimbursementQuery>();

		for (ReimbursementQuery reimbursementQuery2 : approvedQuery) {
			entityManager.refresh(reimbursementQuery2);

			if (reimbursementQuery2.getQueryReply() == null
					|| (reimbursementQuery2.getQueryReply() != null && reimbursementQuery2
							.getQueryReply().equalsIgnoreCase("N"))) {
				filterRodQuery.add(reimbursementQuery2);
			} else if (reimbursementQuery2.getQueryReply() != null
					&& reimbursementQuery2.getQueryReply()
							.equalsIgnoreCase("Y")) {
				if (reimbursementQuery2.getDocAcknowledgement() != null
						&& reimbursementQuery2.getDocAcknowledgement().getKey()
								.equals(ackKey)) {
					filterRodQuery.add(reimbursementQuery2);
				}
				
			}

		}
		return filterRodQuery;
	}

	
	
	private List<ReimbursementQuery> getPARodQueryBasedOnAckKey(Long rodKey,
			Long ackKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForQueryPA");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();
		List<ReimbursementQuery> approvedQuery = new ArrayList<ReimbursementQuery>();

		
        for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			if(reimbursementQuery2.getStatus() != null && (reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS)||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.FA_QUERY_REPLY_STATUS) || 
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) ||
					reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) ||
				    reimbursementQuery2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){

				approvedQuery.add(reimbursementQuery2);
			}
		}

		List<ReimbursementQuery> filterRodQuery = new ArrayList<ReimbursementQuery>();

		for (ReimbursementQuery reimbursementQuery2 : approvedQuery) {
			entityManager.refresh(reimbursementQuery2);

			if (reimbursementQuery2.getQueryReply() == null
					|| (reimbursementQuery2.getQueryReply() != null && reimbursementQuery2
							.getQueryReply().equalsIgnoreCase("N"))) {
				filterRodQuery.add(reimbursementQuery2);
			} else if (reimbursementQuery2.getQueryReply() != null
					&& reimbursementQuery2.getQueryReply()
							.equalsIgnoreCase("Y")) {
				if (reimbursementQuery2.getDocAcknowledgement() != null
						&& reimbursementQuery2.getDocAcknowledgement().getKey()
								.equals(ackKey)) {
					filterRodQuery.add(reimbursementQuery2);
				}
				
			}

		}
		return filterRodQuery;
	}

	
	
	/**
	 * Not in use
	 * */
	public Boolean getStatusOfFirstRODForAckValidationForRepeat(Long claimKey) {
		Boolean isFirstRODApproved = true;

		Query query = entityManager
				.createNamedQuery("Reimbursement.findFirstRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			int iListSize = reimbursementList.size();
			// if(iListSize >1)
			{
				Reimbursement reimbursement = reimbursementList.get(0);

				// DocAcknowledgement docAcknowLedgement =
				// reimbursement.getDocAcknowLedgement();
				/*
				 * if(docAcknowLedgement != null
				 * &&(("Y").equalsIgnoreCase(docAcknowLedgement
				 * .getHospitalisationFlag())||
				 * ("Y").equalsIgnoreCase(docAcknowLedgement
				 * .getPartialHospitalisationFlag()))){ return true; }
				 */
				if (!(ReferenceTable.FINANCIAL_APPROVE_STATUS
						.equals(reimbursement.getStatus().getKey()))
						&& !ReferenceTable.getPaymentStatus().containsKey(
								reimbursement.getStatus().getKey())) {
					isFirstRODApproved = false;
				} else {
					isFirstRODApproved = true;
				}
			}/*
			 * else{ isFirstRODApproved = true; }
			 */
		} else {
			isFirstRODApproved = false;
		}
		return isFirstRODApproved;
	}

	public Boolean getStatusOfFirstRODForAckValidation(Long claimKey) {
		Boolean isFirstRODApproved = true;

		Query query = entityManager
				.createNamedQuery("Reimbursement.findFirstRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			for (Reimbursement reimbursement : reimbursementList) {
				DocAcknowledgement docAcknowledgement = reimbursement
						.getDocAcknowLedgement();
				if (("Y").equalsIgnoreCase(docAcknowledgement
						.getHospitalisationFlag())
						|| ("Y").equalsIgnoreCase(docAcknowledgement
								.getPartialHospitalisationFlag())) {

					if ((ReferenceTable.FINANCIAL_APPROVE_STATUS
							.equals(reimbursement.getStatus().getKey()))
							|| ReferenceTable.getPaymentStatus().containsKey(
									reimbursement.getStatus().getKey())) {
						isFirstRODApproved = true;
						break;

					} else {
						isFirstRODApproved = false;

					}

				}
			}
		} else {
			isFirstRODApproved = false;
		}
		return isFirstRODApproved;
	}

	
	public Boolean getAlreadySelectedBenefit(Long claimKey ,String value)
	{
		Boolean isSelectedBenefitSelected = false;
		
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		List<DocAcknowledgement> ackList = query.getResultList();
		if(null != ackList && !ackList.isEmpty())
		{
			for (DocAcknowledgement acknowledgement : ackList) {
				
				if(value.equalsIgnoreCase(acknowledgement.getBenifitFlag()) && !(acknowledgement.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)))
				{
					isSelectedBenefitSelected = true;
					break;
				}				
				else
				{
					isSelectedBenefitSelected = false;
				}
			}
		}
		else
		{
			isSelectedBenefitSelected = false;
		}
		return isSelectedBenefitSelected;
	}
	
	

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> getReceiptOfMedicalApproverClaim(
			Long a_key, Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", a_key);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<DocAcknowledgement> docAcknowledgmentList = (List<DocAcknowledgement>) query
				.getResultList();

		List<DocAcknowledgement> earlierAck = new ArrayList<DocAcknowledgement>();
		for (DocAcknowledgement docAcknowledgement : docAcknowledgmentList) {
			// if(docAcknowledgement.getRodKey() != null){
			// if(! docAcknowledgement.getRodKey().equals(reimbursementKey)){
			// earlierAck.add(docAcknowledgement);
			// }
			earlierAck.add(docAcknowledgement);
			keysList.add(docAcknowledgement.getKey());
			// }
		}

		Long maximumKey = 0l;
		if (!keysList.isEmpty()) {
			maximumKey = Collections.max(keysList);
		}

		for (DocAcknowledgement docAcknowledgement : earlierAck) {
			entityManager.refresh(docAcknowledgement);
		}
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewDocumentDetailsDTO> listDocumentDetails = instance.getDocumentDetailsTableDTO(earlierAck);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

			String date = SHAUtils.getDateFormat(documentDetailsDTO
					.getReceivedDate());

			if (documentDetailsDTO.getRodKey() != null) {
				Long rodKey = new Long(documentDetailsDTO.getRodKey());
				Query rodQuery = entityManager
						.createNamedQuery("Reimbursement.findByKey");
				rodQuery = rodQuery.setParameter("primaryKey", rodKey);

				Reimbursement reimbursement = (Reimbursement) rodQuery
						.getSingleResult();
				if (reimbursement != null) {
					entityManager.refresh(reimbursement);
				}
				documentDetailsDTO.setRodNumber(reimbursement.getRodNumber());
				documentDetailsDTO.setReimbursementKey(reimbursement.getKey());
				// documentDetailsDTO.setMedicalResponseTime(reimbursement
				// .getMedicalCompletedDate());
				documentDetailsDTO.setApprovedAmount(reimbursement
						.getApprovedAmount());

				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)
						|| reimbursement.getStatus().getKey()
								.equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
						|| reimbursement.getStatus().getKey()
								.equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)) {

					documentDetailsDTO.setApprovedAmount(null);

				}

				documentDetailsDTO.setStatus(reimbursement.getStatus()
						.getProcessValue());
				if (reimbursement.getMedicalCompletedDate() != null) {
					documentDetailsDTO
							.setMedicalResponseTime(SHAUtils
									.formatDate(reimbursement
											.getMedicalCompletedDate()));
				}

				documentDetailsDTO.setApprovedAmount(reimbursement
						.getCurrentProvisionAmt());

				// if (reimbursement.getStatus().getKey()
				// .equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
				// documentDetailsDTO.setApprovedAmount(reimbursement
				// .getFinancialApprovedAmount());
				// } else if (reimbursement
				// .getStatus()
				// .getKey()
				// .equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				// documentDetailsDTO.setApprovedAmount(reimbursement
				// .getBillingApprovedAmount());
				// }

				// documentDetailsDTO.setApprovedAmount(reimbursement.getApprovedAmount());

				// if
				// (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
				//
				// Long approvedAmount =
				// getReimbursementApprovedAmount(reimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				//
				// }else
				// if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
				// Long approvedAmount =
				// getReimbursementApprovedAmount(reimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				// }

			}
		}

		List<String> hospitalization = new ArrayList<String>();

		for (DocAcknowledgement docAcknowledgement : earlierAck) {

			getDocumentBillClassification(hospitalization, docAcknowledgement);

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			listDocumentDetails.get(0).setLatestKey(maximumKey);
		}

		return listDocumentDetails;
	}

	private String getDocumentBillClassification(List<String> hospitalization,
			DocAcknowledgement docAcknowledgement) {
		String classification = "";
		if (docAcknowledgement.getPreHospitalisationFlag() != null) {
			if (docAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
				if (classification.equals("")) {
					classification = "Pre-Hospitalisation";
				} else {
					classification = classification + ","
							+ "Pre-Hospitalisation";
				}
			}
		}
		if (docAcknowledgement.getHospitalisationFlag() != null) {
			if (docAcknowledgement.getHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalisation";
				} else {
					classification = classification + "," + " Hospitalisation";
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

		if (docAcknowledgement.getHospitalCashFlag() != null) {

			if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Hospital cash)";
				}
			}
		}

		if (docAcknowledgement.getPatientCareFlag() != null) {

			if (docAcknowledgement.getPatientCareFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Patient Care)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Patient Care)";
				}
			}
		}

		if (docAcknowledgement.getLumpsumAmountFlag() != null) {

			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Lumpsum Amount";
				} else {
					classification = classification + "," + "Lumpsum Amount";
				}
			}
		}

		if (docAcknowledgement.getHospitalizationRepeatFlag() != null) {

			if (docAcknowledgement.getHospitalizationRepeatFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalization Repeat";
				} else {
					classification = classification + ","
							+ "Hospitalization Repeat";
				}
			}
		}

		if (docAcknowledgement.getPartialHospitalisationFlag() != null) {

			if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Partial Hospitalisation";
				} else {
					classification = classification + ","
							+ "Partial Hospitalisation";
				}
			}
		}
		
		if (docAcknowledgement.getEmergencyMedicalEvaluation() != null) {

			if (docAcknowledgement.getEmergencyMedicalEvaluation().equals("Y")) {

				if (classification.equals("")) {
					classification = "Emergency Medical Evacuation";
				} else {
					classification = classification + ","
							+ "Emergency Medical Evacuation";
				}
			}
		}
		
		if (docAcknowledgement.getCompassionateTravel() != null) {

			if (docAcknowledgement.getCompassionateTravel().equals("Y")) {

				if (classification.equals("")) {
					classification = "Compassionate Travel";
				} else {
					classification = classification + ","
							+ "Compassionate Travel";
				}
			}
		}


		if (docAcknowledgement.getRepatriationOfMortalRemain() != null) {

			if (docAcknowledgement.getRepatriationOfMortalRemain().equals("Y")) {

				if (classification.equals("")) {
					classification = "Repatriation Of Mortal Remains";
				} else {
					classification = classification + ","
							+ "Repatriation Of Mortal Remains";
				}
			}
		}
		

		if (docAcknowledgement.getPreferredNetworkHospita() != null) {

			if (docAcknowledgement.getPreferredNetworkHospita().equals("Y")) {
				
				if(null != docAcknowledgement.getClaim()&& docAcknowledgement.getClaim().getIntimation() != null && docAcknowledgement.getClaim().getIntimation().getPolicy() != null &&
						ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAcknowledgement.getClaim().getIntimation().getPolicy().getProduct().getKey())){
					if (classification.equals("")) {
						classification = "Valuable Service Provider (Hospital)";
					} else {
						classification = classification + ","
								+ "Valuable Service Provider (Hospital)";
					}
				}
				else{
					if (classification.equals("")) {
						classification = "Preferred Network Hospital";
					} else {
						classification = classification + ","
								+ "Preferred Network Hospital";
					}
				}
			}
		}
		

		if (docAcknowledgement.getSharedAccomodation() != null) {

			if (docAcknowledgement.getSharedAccomodation().equals("Y")) {

				if (classification.equals("")) {
					classification = "Shared Accomodation";
				} else {
					classification = classification + ","
							+ "Shared Accomodation";
				}
			}
		}		

		
		hospitalization.add(classification);

		return classification;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> getReceiptOfMedicalApproverByReimbursement(Long a_key,
			Long reimbursementKey) {
		
		List<Reimbursement> reimbursementDetails = getReimbursementDetails(a_key);
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewDocumentDetailsDTO> listDocumentDetails = instance
				.getDocumentDetailsTableReimbursemntWise(reimbursementDetails);
		
		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {
			if(documentDetailsDTO.getMedicalCompleteDate() != null){
				documentDetailsDTO.setMedicalResponseTime(SHAUtils.formatDate(documentDetailsDTO.getMedicalCompleteDate()));
			}
			
			String coverValue = getCoverValueForViewBasedOnrodKey(documentDetailsDTO.getReimbursementKey());
			
			List<DocAcknowledgement> docAcknowledgmentList = getDocumentAckListByROD(documentDetailsDTO.getReimbursementKey());
			
			if(docAcknowledgmentList != null && ! docAcknowledgmentList.isEmpty()){
				documentDetailsDTO.setStrLastDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgmentList.get(0).getDocumentReceivedDate()));
			}
			
			if(documentDetailsDTO.getDocAcknowledgement() != null){
				String billClassificationValue = getBillClassificationValue(documentDetailsDTO.getDocAcknowledgement());
				documentDetailsDTO.setBillClassification(billClassificationValue);
				documentDetailsDTO.setLatestKey(reimbursementKey);
			}
			
			List<RODBillDetails> billEntryDetails = getBillEntryDetails(documentDetailsDTO.getReimbursementKey());
			if(billEntryDetails != null){
				Double billAmount = 0d;
				for (RODBillDetails rodBillDetails : billEntryDetails) {
					billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
				}
				documentDetailsDTO.setTotalBillAmount(billAmount);
			}
			
			
			if(null != documentDetailsDTO && null != documentDetailsDTO.getClaim().getIntimation() && null != documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct() &&
					  null != documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct().getCode() && documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					  || (null != documentDetailsDTO && null != documentDetailsDTO.getClaim().getIntimation() && null != documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct() &&
							  null != documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct().getCode() && documentDetailsDTO.getClaim().getIntimation().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			List<RODDocumentSummary> billClaimAmountforHC = getRODSummaryDetailsByReimbursementKey(documentDetailsDTO.getReimbursementKey());
			if(billClaimAmountforHC != null){
				Double billAmount = 0d;
				for (RODDocumentSummary rodSummaryDetails : billClaimAmountforHC) {
					billAmount += rodSummaryDetails.getBillAmount() != null ?  rodSummaryDetails.getBillAmount() : 0d;
				}
				documentDetailsDTO.setTotalBillAmount(billAmount);
			}
		}
			
						
			if(documentDetailsDTO.getDocAcknowledgement() != null){
				
				DocAcknowledgement docAcknowledgement = documentDetailsDTO.getDocAcknowledgement();
				
				if(documentDetailsDTO.getReconsiderationFlag() != null && documentDetailsDTO.getReconsiderationFlag().equalsIgnoreCase("Y")){
					Long version = documentDetailsDTO.getRodVersion() != null ? documentDetailsDTO.getRodVersion() - 1l : 1l;
					documentDetailsDTO.setRodType("Reconsideration - " +version);
				}else{
					documentDetailsDTO.setRodType("Original");
				}
			}

			if(documentDetailsDTO.getClaim() != null)
			{
			documentDetailsDTO.setTypeOfClaim(documentDetailsDTO.getClaim().getClaimType() != null ? documentDetailsDTO.getClaim().getClaimType().getValue() : "Reimbursement");
			}
			
			if(documentDetailsDTO.getReimbursementKey() != null){
				DocAcknowledgement firstAcknowledgmentDetails = getFirstAcknowledgmentDetails(documentDetailsDTO.getReimbursementKey());
				if(firstAcknowledgmentDetails != null){
					documentDetailsDTO.setDocumentReceivedDate(firstAcknowledgmentDetails.getDocumentReceivedDate());
				}
			}
			
			if(documentDetailsDTO.getBenefits() != null)
			{
				documentDetailsDTO.setBenefits(documentDetailsDTO.getBenefits()+", "+coverValue);
			}
			else
			{
				documentDetailsDTO.setBenefits(coverValue);
			}
			if(documentDetailsDTO.getCrmFlagged() != null && documentDetailsDTO.getCrmFlagged().equalsIgnoreCase("Y")){
				documentDetailsDTO.setColorCodeCell("GREENFLAG");
				documentDetailsDTO.setRowDescRow(documentDetailsDTO.getReconsiderationRejectionFlagRemarks());
				documentDetailsDTO.setReconsiderationRequestFlag(documentDetailsDTO.getCrmFlagged());
				Reimbursement reimbursementParentkey = getReimbursementParent(documentDetailsDTO.getReimbursementKey());

				if(reimbursementParentkey != null && reimbursementParentkey.getParentKey() != null ){
					documentDetailsDTO.setColorCodeCell("GREYFLAG");
					documentDetailsDTO.setReconsiderationRequestFlag("N");
				}
				
				documentDetailsDTO.setCrmFlagged(null);
			}
			if(documentDetailsDTO.getCrmFlagged() != null && documentDetailsDTO.getCrmFlagged().equalsIgnoreCase("N")){
				documentDetailsDTO.setColorCodeCell("GREYFLAG");
				documentDetailsDTO.setRowDescRow(documentDetailsDTO.getReconsiderationRejectionFlagRemarks());
				documentDetailsDTO.setReconsiderationRequestFlag(documentDetailsDTO.getCrmFlagged());
				documentDetailsDTO.setCrmFlagged(null);
			}
		
		}
		
		return listDocumentDetails;

	}
	


	private  DocAcknowledgement getFirstAcknowledgmentDetails(Long rodKey) {

		DocAcknowledgement ack = null;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"DocAcknowledgement.findByReimbursement").setParameter(
				"rodKey", rodKey);
		try {
			List<DocAcknowledgement> ackList = (List<DocAcknowledgement>) findByReimbursementKey
					.getResultList();
			if (null != ackList && !ackList.isEmpty()) {
				ack = ackList.get(0);
			}
			return ack;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

public List<ViewDocumentDetailsDTO> getViewAcknowledgementList(Long rodKey){
		
		List<DocAcknowledgement> docAcknowledgmentList = getDocumentAckListByROD(rodKey);
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		List<ViewDocumentDetailsDTO> listDocumentDetails = 
				instance.getDocumentDetailsTableDTO(docAcknowledgmentList);
		
		List<String> hospitalization = new ArrayList<String>();

		for (DocAcknowledgement docAcknowledgement : docAcknowledgmentList) {

			getDocumentBillClassification(hospitalization, docAcknowledgement);
			

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			listDocumentDetails.get(0).setLatestKey(listDocumentDetails.get(i).getKey());
		}
		
		for(ViewDocumentDetailsDTO viewDocumentDetails : listDocumentDetails)
		{
			DocAcknowledgement docAck = getDocAcknowledgment(viewDocumentDetails.getKey());
			
			if(docAck != null)
			{
				if(docAck.getBenifitFlag() != null)
				{
					if(getCoverValueForViewBasedOnAckKey(docAck.getKey()).isEmpty()){
						viewDocumentDetails.setBenefits(SHAUtils.getBenefitsValue(docAck.getBenifitFlag()));
					}else{
						viewDocumentDetails.setBenefits(SHAUtils.getBenefitsValue(docAck.getBenifitFlag())+", "+getCoverValueForViewBasedOnAckKey(docAck.getKey()));	
					}
				}else{
					viewDocumentDetails.setBenefits(getCoverValueForViewBasedOnAckKey(docAck.getKey()));
				}
			}
		}
		
		return listDocumentDetails;
		
	}

	private List<DocAcknowledgement> getDocumentAckListByROD(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<DocAcknowledgement> docAcknowledgmentList = (List<DocAcknowledgement>) query
				.getResultList();
		return docAcknowledgmentList;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		Claim resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (Claim) findByIntimationNum.getSingleResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultClaim;
	}

	/**
	 * Added for claim legal requirement.
	 * */

	public String getLegalFlagFromClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByKey");
		query = query.setParameter("primaryKey", claimKey);
		List<Claim> claimList = query.getResultList();
		if (null != claimList && !claimList.isEmpty()) {
			entityManager.refresh(claimList.get(0));
			Claim objClaim = claimList.get(0);
			if (null != objClaim) {
				return objClaim.getLegalFlag();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claim = (List<Claim>) query.getResultList();

		if (claim != null && !claim.isEmpty()) {
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		} else {
			return null;
		}
	}

	public ReimbursementRejection getReimbRejectRecord(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);
		List<ReimbursementRejection> reimbRejection = query.getResultList();
		if (null != reimbRejection && !reimbRejection.isEmpty()) {
			entityManager.refresh(reimbRejection.get(0));
			return reimbRejection.get(0);
		}
		return null;
	}	

	public Boolean isLumpSumExistsForClaim(Long claimKey)
	{

		Boolean validationFlag = false;
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestNonCanceledRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		query = query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<Reimbursement> rodList = query.getResultList();
		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement rodObj : rodList) {
				entityManager.refresh(rodObj);
			}
			for (Reimbursement reimbursement : rodList) {
				if (SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement
						.getDocAcknowLedgement().getLumpsumAmountFlag())) {
					validationFlag = true;
					break;
				}
			}
		}
		return validationFlag;
	}


	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		return reimbursementList;
	}
	
	public String getAlreadySelectedAdditionalCovers(Long claimKey,List<AddOnCoversTableDTO> coversList)
	{
		String isSelectedCoversSelected = null;
		
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByClaimKeyAndDeletedFlag");

		query = query.setParameter("claimKey", claimKey);
		List<PAAdditionalCovers> AdditionalCoverList = query.getResultList();
		if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty() && null != coversList && !coversList.isEmpty())
		{
			for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
			{
				   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
				   {
					   if(null != coversList.get(cover1).getCovers())
					   {
						   DocAcknowledgement ackDetails = getDocAcknowledgementBasedOnKey(AdditionalCoverList.get(cover).getAcknowledgementKey());
						   if(!ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(ackDetails.getStatus().getKey()))
						   {
				      if(AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getCovers().getId()))
				      {
				    	  isSelectedCoversSelected = coversList.get(cover1).getCovers().getValue();
				      }
				      }
					   }
				   }
			}
				    
			
		}
		
		return isSelectedCoversSelected;
	}
	
	public String getAlreadySelectedOptionalCovers(Long claimKey,List<AddOnCoversTableDTO> coversList)
	{
		String isSelectedCoversSelected = null;
		 String isSelectedCoversSelected1 = null;
		
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByClaimKeyAndDeletedFlag");
		query = query.setParameter("claimKey", claimKey);
		List<PAOptionalCover> AdditionalCoverList = query.getResultList();
		if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty() && null != coversList && !coversList.isEmpty())
		{
			for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
				   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
					   if(null != coversList.get(cover1).getOptionalCover())
					   {
						   DocAcknowledgement ackDetails = getDocAcknowledgementBasedOnKey(AdditionalCoverList.get(cover).getAcknowledgementKey());
						   if(!ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(ackDetails.getStatus().getKey()))
						   {
							   if(AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getOptionalCover().getId())
									   && coversList.get(cover1).getOptionalCover() != null && coversList.get(cover1).getOptionalCover().getValue() != null
									   && !coversList.get(cover1).getOptionalCover().getValue().equalsIgnoreCase(SHAConstants.MEDICAL_EXTENSION_COVER_DESC))
							   {
								   isSelectedCoversSelected1 = coversList.get(cover1).getOptionalCover().getValue();			    	  
							   }
					   }
					   }
				    
			isSelectedCoversSelected = isSelectedCoversSelected1;
		}
		
		return isSelectedCoversSelected;
	}
	
	public List<PAAdditionalCovers> getAdditionalCovers(Long ackKey)
	{
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByAckKey");
		query = query.setParameter("ackDocKey",ackKey );
		List<PAAdditionalCovers> additionalCovers = query.getResultList();
		if(null != additionalCovers && !additionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return additionalCovers;
		}
		return null;
	}

	
	
	public List<PAOptionalCover> getOptionalCovers(Long ackKey)
	{
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByAckKey");
		query = query.setParameter("ackDocKey",ackKey );
		List<PAOptionalCover> optionalCovers = query.getResultList();
		if(null != optionalCovers && !optionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return optionalCovers;
		}
		return null;
	}
	
	public MasPaClaimCovers getCoversName(Long coverId)
	{
		Query query = entityManager.createNamedQuery("MasPaClaimCovers.findByCoverKey");
		query = query.setParameter("coverKey",coverId );
		List<MasPaClaimCovers> coverName = query.getResultList();
		if(null != coverName && !coverName.isEmpty())
		{
			entityManager.refresh(coverName.get(0));
			return coverName.get(0);
		}
		return null;
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
	/*public void stopQueryReminderLetter(PayloadBOType payloadBO,String userName,String password)
	{

		try {
			log.info("===== inside stop reminder letter for query" );
			
			GenerateReminderLetterTask generateReminderLetterTask = BPMClientContext
					.getGenerateRemainderLetterTask(userName, password);
			PagedTaskList queryLetterTaskList = generateReminderLetterTask
					.getTasks(userName, new Pageable(), payloadBO);

			if (null != queryLetterTaskList) {
				List<HumanTask> humanTaskList = queryLetterTaskList
						.getHumanTasks();
				if (null != humanTaskList && !humanTaskList.isEmpty()) {

					for (HumanTask humanTask : humanTaskList) {
						
						SubmitGenerateReminderLetterTask submitTask = BPMClientContext.getSubmitQueryReimnderLetterTask(userName, password);
						humanTask.setOutcome("SUBMIT");
						submitTask.execute(userName, humanTask);
						
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.toString());
		}
	
	}*/
	/*public void stopQueryReplyReminderProcess(PayloadBOType payloadBO,String userName,String password)
	{
		log.info("===== inside stop reminder process for query process" );
		QueryReplyDocReimbTask reimbReminder = BPMClientContext.getReimbQueryReplyDocTask(userName,password);
		PagedTaskList pagedHumanTaskList = reimbReminder.getTasks(userName,new Pageable(),payloadBO);
		if(null != pagedHumanTaskList)
		{
			 List<HumanTask> humanTaskList  = pagedHumanTaskList.getHumanTasks();
			 if(null != humanTaskList && !humanTaskList.isEmpty())
			 {
				 humanTaskList.get(0).setOutcome("APPROVE");
				 SubmitQueryReplyDocReimbTask submitTask =  BPMClientContext.getReimbSubmitQueryReplyDocTask(userName,password);
				 try {
					submitTask.execute(userName , humanTaskList.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.toString());
				}
			 }
		}
		
	}
	*/
    public  MastersValue getMasterKeyBasedOnMappingCode(String mappingCode) {
    	MastersValue masterKey = null;
					
			Query findByMappingCode = entityManager.createNamedQuery(
					"MastersValue.findByMappingCode").setParameter("mappingCode", mappingCode);
			try{
				List<MastersValue> masterList = (List<MastersValue>) findByMappingCode.getResultList();
				if(null != masterList && !masterList.isEmpty())
				{
					masterKey = masterList.get(0);
				}
				return masterKey;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

}
    
    @SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgmentByClaimKey(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.getByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<DocAcknowledgement> ackList = (List<DocAcknowledgement>) query
				.getResultList();
		
		for (DocAcknowledgement docAcknowledgement : ackList) {
			entityManager.refresh(docAcknowledgement);
		}

		if (ackList.size() > 0) {
			return ackList.get(0);
		}

		return null;

	}
	
    @SuppressWarnings("unchecked")
	public List<DocAcknowledgement> getDocAckByClaimKey(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.getByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<DocAcknowledgement> ackList = (List<DocAcknowledgement>) query
				.getResultList();
		if (ackList.size() > 0) {
			for (DocAcknowledgement docAcknowledgement : ackList) {
				entityManager.refresh(docAcknowledgement);
			}
		}

		return ackList;

	}
	public void submitTaskToDB(ReceiptOfDocumentsDTO rodDTO,
			DocAcknowledgement docAck, Boolean isQueryReply,
			Boolean isReconsideration)
	{				  
		Claim claimObj = entityManager.find(Claim.class, rodDTO.getClaimDTO()
				.getKey());

		entityManager.refresh(claimObj);
		
		/**
		 * Cpu code changed for kerala
		 */
		changeCpuCode(claimObj.getIntimation());
		
		Map<String,Object> workTaskMap = (Map<String,Object>)rodDTO.getDbOutArray();
		Long workFlowKey = 0l;
		
		if(workTaskMap != null && workTaskMap.containsKey(SHAConstants.WK_KEY) && workTaskMap.get(SHAConstants.WK_KEY) != null){
			workFlowKey = (Long)workTaskMap.get(SHAConstants.WK_KEY);
		}	

		Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());		
		
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObj, hospitals);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();

		if(workFlowKey != null && workFlowKey.intValue() != 0){
			inputArray[SHAConstants.INDEX_WORK_FLOW_KEY] = workFlowKey; 
		}
		
		inputArray[SHAConstants.INDEX_ACK_KEY] = docAck.getKey();
		inputArray[SHAConstants.INDEX_ACK_NUMBER] = docAck.getAcknowledgeNumber();
		inputArray[SHAConstants.INDEX_HOSPITALIZATION] = docAck.getHospitalisationFlag();
		inputArray[SHAConstants.INDEX_POST_HOSPITALIZATION] = docAck.getPostHospitalisationFlag();
		inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = docAck.getPartialHospitalisationFlag();
		inputArray[SHAConstants.INDEX_PRE_HOSPITALIZATION] = docAck.getPreHospitalisationFlag();
		inputArray[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = docAck.getLumpsumAmountFlag();
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = docAck.getPatientCareFlag();
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = docAck.getHospitalCashFlag();		
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.DOCUMENT_ACKNOWLEDGED; 
		inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		//inputArray[SHAConstants.INDEX_REIMB_REQ_BY] = SHAConstants.ACK_Q; 
		
		Insured insured = claimObj.getIntimation().getInsured();
		//code change done by noufel for updating cmd club memeber update
		if(claimObj != null && claimObj.getPriorityEvent() != null && !claimObj.getPriorityEvent().trim().isEmpty()){
			inputArray[SHAConstants.INDEX_PRIORITY] = claimObj.getPriorityEvent() ;
		}
		else if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		//	classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		}else if (claimObj.getClaimPriorityLabel() != null && claimObj.getClaimPriorityLabel().equals("Y")) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
		}else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			//classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		} else {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			//classificationType.setPriority(SHAConstants.NORMAL);
		}
		
		if (isReconsideration) {	
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.RECONSIDERATION;
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.YES_FLAG;
			//classificationType.setType(SHAConstants.RECONSIDERATION);
			//claimRequest.setIsReconsider(false);
			inputArray[SHAConstants.INDEX_REIMB_REQ_BY] = SHAConstants.MA_Q; 
			//claimRequest.setClientType(SHAConstants.MEDICAL);
			/*claimRequest
					.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);*/
		
			//ProcessActorInfoType processActor = new ProcessActorInfoType();
			//processActor.setEscalatedByUser("");
			//payloadBO.setProcessActorInfo(processActor);
			
			//classificationType.setSource(SHAConstants.RECONSIDERATION);
		} else if (isQueryReply) {
			
			//claimRequest.setIsReconsider(false);
			//inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.YES_FLAG;
			//classificationType.setType(SHAConstants.QUERY_REPLY);
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.QUERY_REPLY;
		//	ProcessActorInfoType processActor = new ProcessActorInfoType();
			//processActor.setEscalatedByUser(rodDTO.getCreatedByForQuery());
			inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = rodDTO.getCreatedByForQuery();
			//payloadBO.setProcessActorInfo(processActor);
			//classificationType.setSource(SHAConstants.QUERY_REPLY);
			
		} else {
			
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.N_FLAG;
		//	claimRequest.setIsReconsider(false);			
			//classificationType.setType(SHAConstants.TYPE_FRESH);
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
		//	ProcessActorInfoType processActor = new ProcessActorInfoType();
		//	processActor.setEscalatedByUser("");
		//	payloadBO.setProcessActorInfo(processActor);
		}

		// classificationType.setSource(SHAConstants.NORMAL);
		if (!isReconsideration && !isQueryReply) {
		//	classificationType.setSource(docAck.getStatus().getProcessValue());
			//inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.DOCUMENT_ACKNOWLEDGED;
		}
		
		
//		inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] = docAck.getKey().toString();
		
		if (null != rodDTO.getIsConversionAllowed()
				&& rodDTO.getIsConversionAllowed()) {
			
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_CONVERSION;
			
			
			
			
		}else{
			//acknowledgemnetTask.initiate(BPMClientContext.BPMN_TASK_USER,
					//payloadBO);
			
			/**
			 * If conversion is not allowed then this value needs to be updated . As
			 * of now same values is added for both conversion and non conversion.
			 * The outcome value needs to be updated once sathish sir gets back on this.
			 * */
			
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_SUBMIT; 
			
			
		}
		
		if(hospitals.getFspFlag() != null && hospitals.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 MastersValue master = getMaster(ReferenceTable.FSP);
			 if(master != null){
				 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
			 }
		}
		
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
		//Added as per Sathish Sir - 07-JAN-2019
		dbCalculationService.stopReminderProcessProcedure(claimObj.getIntimation().getIntimationId(),SHAConstants.OTHERS);
		
	}
	
	
	public void submitTaskForReimbursement(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement, Boolean isQueryReply,
			Boolean isReconsideration)
	{				  
		Claim claimObj = entityManager.find(Claim.class, rodDTO.getClaimDTO()
				.getKey());

		entityManager.refresh(claimObj);
		
		Map<String,Object> workTaskMap = (Map<String,Object>)rodDTO.getDbOutArray();
		Long workFlowKey = 0l;
		
		if(workTaskMap != null && workTaskMap.containsKey(SHAConstants.WK_KEY) && workTaskMap.get(SHAConstants.WK_KEY) != null){
			workFlowKey = (Long)workTaskMap.get(SHAConstants.WK_KEY);
		}	

		Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());		
		
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObj, hospitals);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();

		if(workFlowKey != null && workFlowKey.intValue() != 0){
			inputArray[SHAConstants.INDEX_WORK_FLOW_KEY] = workFlowKey; 
		}
		
		inputArray[SHAConstants.INDEX_ACK_KEY] = reimbursement.getDocAcknowLedgement().getKey();
		inputArray[SHAConstants.INDEX_ACK_NUMBER] = reimbursement.getDocAcknowLedgement().getAcknowledgeNumber();
		inputArray[SHAConstants.INDEX_HOSPITALIZATION] = reimbursement.getDocAcknowLedgement().getHospitalisationFlag();
		inputArray[SHAConstants.INDEX_POST_HOSPITALIZATION] = reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag();
		inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag();
		inputArray[SHAConstants.INDEX_PRE_HOSPITALIZATION] = reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag();
		inputArray[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag();
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = reimbursement.getDocAcknowLedgement().getPatientCareFlag();
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = reimbursement.getDocAcknowLedgement().getHospitalCashFlag();		
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.DOCUMENT_ACKNOWLEDGED; 
		inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		inputArray[SHAConstants.INDEX_ROD_KEY] = reimbursement.getKey();
		inputArray[SHAConstants.INDEX_ROD_NUMBER] = reimbursement.getRodNumber();
		inputArray[SHAConstants.INDEX_ROD_CREATED_DATE] = SHAUtils.parseDate(reimbursement.getCreatedDate());
		inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = 'Y';
		//inputArray[SHAConstants.INDEX_REIMB_REQ_BY] = SHAConstants.ACK_Q; 
		
		Insured insured = claimObj.getIntimation().getInsured();
		//code change done by noufel for updating cmd club memeber update
		if(claimObj != null && claimObj.getPriorityEvent() != null && !claimObj.getPriorityEvent().trim().isEmpty()){
			inputArray[SHAConstants.INDEX_PRIORITY] = claimObj.getPriorityEvent() ;
		}
		else if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		//	classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			//classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		} else if (claimObj.getClaimPriorityLabel() != null && claimObj.getClaimPriorityLabel().equals("Y")) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
		} else {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			//classificationType.setPriority(SHAConstants.NORMAL);
		}
		
		if (isReconsideration) {	
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.RECONSIDERATION;
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.YES_FLAG;
			//classificationType.setType(SHAConstants.RECONSIDERATION);
			//claimRequest.setIsReconsider(false);
			inputArray[SHAConstants.INDEX_REIMB_REQ_BY] = SHAConstants.MA_Q; 
			//claimRequest.setClientType(SHAConstants.MEDICAL);
			/*claimRequest
					.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);*/
		
			//ProcessActorInfoType processActor = new ProcessActorInfoType();
			//processActor.setEscalatedByUser("");
			//payloadBO.setProcessActorInfo(processActor);
			
			//classificationType.setSource(SHAConstants.RECONSIDERATION);
		} else if (isQueryReply) {
			
			//claimRequest.setIsReconsider(false);
			//inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.YES_FLAG;
			//classificationType.setType(SHAConstants.QUERY_REPLY);
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.QUERY_REPLY;
		//	ProcessActorInfoType processActor = new ProcessActorInfoType();
			//processActor.setEscalatedByUser(rodDTO.getCreatedByForQuery());
			inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = rodDTO.getCreatedByForQuery();
			//payloadBO.setProcessActorInfo(processActor);
			//classificationType.setSource(SHAConstants.QUERY_REPLY);
			
		} else {
			
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.N_FLAG;
		//	claimRequest.setIsReconsider(false);			
			//classificationType.setType(SHAConstants.TYPE_FRESH);
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
		//	ProcessActorInfoType processActor = new ProcessActorInfoType();
		//	processActor.setEscalatedByUser("");
		//	payloadBO.setProcessActorInfo(processActor);
		}

		// classificationType.setSource(SHAConstants.NORMAL);
		/*if (!isReconsideration && !isQueryReply) {
		//	classificationType.setSource(docAck.getStatus().getProcessValue());
			//inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.DOCUMENT_ACKNOWLEDGED;
		}*/
		
		
//		inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] = docAck.getKey().toString();
		
		if (null != rodDTO.getIsConversionAllowed()
				&& rodDTO.getIsConversionAllowed()) {
			
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_CONVERSION;
			
			
			
			
		}else{
			//acknowledgemnetTask.initiate(BPMClientContext.BPMN_TASK_USER,
					//payloadBO);
			
			/**
			 * If conversion is not allowed then this value needs to be updated . As
			 * of now same values is added for both conversion and non conversion.
			 * The outcome value needs to be updated once sathish sir gets back on this.
			 * */
			
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_SUBMIT; 
			
			
		}
		
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
	}
	
	public List<CloseClaim> getCloseClaimByReimbursementKeyAndType(Long reimbursementKey) {

		Query closeClaimQuery = entityManager
				.createNamedQuery("CloseClaim.getByReimbursmentKeyType");
		closeClaimQuery.setParameter("reimbursmentKey", reimbursementKey);

		List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();

		if (closedClaimList != null && !closedClaimList.isEmpty()) {
			return closedClaimList;
		}

		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> updateOtherBenefitsValues(PreauthDTO preauthDTO,DocAcknowledgement docAcknowledgement)
	{
		if(null != docAcknowledgement)
		{
			docAcknowledgement.setOtherBenefitsFlag(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getOtherBenefitFlag());
			if(null != docAcknowledgement.getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(docAcknowledgement.getOtherBenefitsFlag())){
			docAcknowledgement.setEmergencyMedicalEvaluation(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getEmergencyMedicalEvaluationFlag());
			docAcknowledgement.setCompassionateTravel(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getCompassionateTravelFlag());
			docAcknowledgement.setRepatriationOfMortalRemain(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getRepatriationOfMortalRemainsFlag());
			docAcknowledgement.setPreferredNetworkHospita(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPreferredNetworkHospitalFlag());
			docAcknowledgement.setSharedAccomodation(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getSharedAccomodationFlag());
			}
			else
			{
				docAcknowledgement.setEmergencyMedicalEvaluation(SHAConstants.N_FLAG);
				docAcknowledgement.setCompassionateTravel(SHAConstants.N_FLAG);
				docAcknowledgement.setRepatriationOfMortalRemain(SHAConstants.N_FLAG);
				docAcknowledgement.setPreferredNetworkHospita(SHAConstants.N_FLAG);
				docAcknowledgement.setSharedAccomodation(SHAConstants.N_FLAG);
				
			}
			if(null != docAcknowledgement.getKey())
			{
			entityManager.merge(docAcknowledgement);
			entityManager.flush();
			entityManager.clear();
			}
		}
		
		
		return null;		
	}
		
				
		
public Boolean getDBTaskForCurrentQ(Intimation intimation,String currentQ,Long reimbursementKey){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);	
		
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			 for (Map<String, Object> map : taskProcedure) {
				 Long keyValue = (Long) map.get(SHAConstants.PAYLOAD_ROD_KEY);
				 if(keyValue != null && keyValue.equals(reimbursementKey)){
					 return true;
				 }
			}
		} 
		return false;
	}

    
    @SuppressWarnings("unchecked")
   	public ClaimPayment getClaimPaymentDetails(String rodNo) {

   		Query query = entityManager
   				.createNamedQuery("ClaimPayment.findLatestByRodNo");
   		query.setParameter("rodNumber", rodNo);

   		List<ClaimPayment> paymentList = (List<ClaimPayment>) query
   				.getResultList();
   		
   		for (ClaimPayment claimDetails : paymentList) {
   			entityManager.refresh(claimDetails);
   		}

   		if (paymentList.size() > 0) {
   			return paymentList.get(0);
   		}

   		return null;

   	}

    
    public List<PAAdditionalCovers> getAdditionalCoversByRodKey(Long rodKey)
    {
    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAAdditionalCovers> additionalCoversList = query.getResultList();
    	if(null != additionalCoversList && !additionalCoversList.isEmpty())
    	{
    		/*for (PAAdditionalCovers paAdditionalCovers : additionalCoversList) {
    			entityManager.refresh(paAdditionalCovers);
			}*/
    		return additionalCoversList;
    	}
    	return null;
    }
    
    public List<PAOptionalCover> getOptionalCoversByRodKey(Long rodKey)
    {
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAOptionalCover> optionalCoverList = query.getResultList();
    	if(null != optionalCoverList && !optionalCoverList.isEmpty())
    	{
    		/*for (PAOptionalCover paOptionalCover : optionalCoverList) {
				entityManager.refresh(paOptionalCover);
			}*/
    		return optionalCoverList;
    	}
    	return optionalCoverList;
    }
    
    private void saveAdditionalAndOptionalCoversList(Long rodKey,Long ackKey)
    {
    	List<PAAdditionalCovers> additionalCoverList = getAdditionalCoversByRodKey(rodKey);
		if(null != additionalCoverList && !additionalCoverList.isEmpty())
		{
			for (PAAdditionalCovers paAdditionalCovers : additionalCoverList) {
				paAdditionalCovers.setAcknowledgementKey(ackKey);
				entityManager.merge(paAdditionalCovers);
			}
		}
		
		List<PAOptionalCover> optionalCoverList = getOptionalCoversByRodKey(rodKey);
		if(null != optionalCoverList && !optionalCoverList.isEmpty())
		{
			for (PAOptionalCover paOptionalCover : optionalCoverList) {
				paOptionalCover.setAcknowledgementKey(ackKey);
				entityManager.merge(paOptionalCover);
			}
		}
    }
	
    public Boolean getPADBTaskForPreauth(Intimation intimation,String currentQ){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		return false;
	}
    
public List<ViewDocumentDetailsDTO> getDocumentDetailsForPACloseClaim(String intimationNo,String userName, String password){
		
	 DBCalculationService dbCalculationService = new DBCalculationService();
		
	 List<Map<String, Object>> taskProcedureForCloseClaim = dbCalculationService.getTaskProcedureForCloseClaim(intimationNo);
	 
		
		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();
		
		
		Claim claim = getClaimsByIntimationNumber(intimationNo);
		
		List<Reimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim.getKey());
		
		for (Reimbursement reimbursement : reimbursementList) {
			if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)
					|| ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey()) ){
                DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
                EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
				
			
				setReimbursementToDTO(documentList, reimbursement, docAcknowLedgement, documentDetailsDTO);
				EarlierRodMapper.invalidate(instance);
			}
		}
		
		if(taskProcedureForCloseClaim != null){
			getDocumentListByHumanTask(taskProcedureForCloseClaim, documentList);
		}
		
		return documentList;
		
	}  
    

public List<ViewDocumentDetailsDTO> getDocumentDetailsForPAReOpenClaim(
		String intimationNo, String userName, String password) {

//	PayloadBOType payloadType = new PayloadBOType();
//	IntimationType intimationType = new IntimationType();
//	intimationType.setIntimationNumber(intimationNo);
//	payloadType.setIntimation(intimationType);
//
//	ReopenAllClaimTask reOpenClaimTask = BPMClientContext
//			.getReOpAllClaimTask(userName, password);
//
//	PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
//			payloadType);
	
	List<Map<String, Object>> dbTaskForCloseClaim = getDBTaskForCloseClaim(intimationNo, SHAConstants.CLOSE_CLAIM_CURRENT_Q);
	
	/**
	 * The commented code to be refractored.
	 * 
	 * **/
	
	/*PayloadBOType payloadType = new PayloadBOType();
	IntimationType intimationType = new IntimationType();
	intimationType.setIntimationNumber(intimationNo);
	payloadType.setIntimation(intimationType);

	ReopenAllClaimTask reOpenClaimTask = BPMClientContext
			.getReOpAllClaimTask(userName, password);

	PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
			payloadType);*/

	List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

	Claim claim = getClaimsByIntimationNumber(intimationNo);

	List<Reimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
			.getKey());

	List<Long> rodKeyList = new ArrayList<Long>();
	
	for (Map<String, Object> map : dbTaskForCloseClaim) {
		Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
		
		if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
			rodKeyList.add(reimbursementKey);
		}

	}

//	List<HumanTask> humanTasks = tasks.getHumanTasks();
	/*List<HumanTask> humanTasks = tasks.getHumanTasks();
>>>>>>> removalofbpmn

	
*/
	for (Reimbursement reimbursement : reimbursementList) {
		entityManager.refresh(reimbursement);
		if (!rodKeyList.contains(reimbursement.getKey())) {
			DocAcknowledgement docAcknowLedgement = reimbursement
					.getDocAcknowLedgement();
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewDocumentDetailsDTO documentDetailsDTO = instance.getDocumentDetails(docAcknowLedgement);
			setReimbursementToDTO(documentList, reimbursement,
					docAcknowLedgement, documentDetailsDTO);
			EarlierRodMapper.invalidate(instance);
		}
	}


	if (dbTaskForCloseClaim != null && ! dbTaskForCloseClaim.isEmpty()) {
		getDocumentListByHumanTask(dbTaskForCloseClaim, documentList);
	}


	return documentList;

}


public List<ReconsiderRODRequestTableDTO> getReconsiderationDetailsList(
		DocAcknowledgement docAcknowledgement) {

	List<ReconsiderRODRequestTableDTO> reconsiderationTable = new ArrayList<ReconsiderRODRequestTableDTO>();

	ReconsiderRODRequestTableDTO reconsideration = new ReconsiderRODRequestTableDTO();
	if (docAcknowledgement != null) {
		if (docAcknowledgement.getRodKey() != null) {
			Reimbursement reimbursementByKey = getReimbursementByKey(docAcknowledgement
					.getRodKey());
			if (reimbursementByKey != null) {
				reconsideration.setRodNo(reimbursementByKey.getRodNumber());
				String billClassificationValue = getBillClassificationValue(docAcknowledgement);
				reconsideration
						.setBillClassification(billClassificationValue);
				Double claimedAmount = getClaimedAmount(reimbursementByKey);
				reconsideration.setClaimedAmt(claimedAmount);
				reconsideration.setRodStatus(reimbursementByKey.getStatus()
						.getProcessValue());

			}
		}
	}
	reconsiderationTable.add(reconsideration);

	return reconsiderationTable;
}
    public String getBenefitSelectedForTransaction(Long transacKey){
    	String benefitSelected = "";
    	
    	List<Reimbursement> reimbList = getReimbursementByClaimKey(transacKey);
    	
    	if(reimbList == null || reimbList.isEmpty()){
    		
    		Reimbursement reimbObj = getReimbursement(transacKey);
    		if(reimbObj != null){
    			if(reimbObj.getBenefitsId() != null)
    			{    			
    				benefitSelected = ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(reimbObj.getBenefitsId().getKey()) ? SHAConstants.DEATH : SHAConstants.OTHERS;
    			}
    		}
    		else{
	    		List<DocAcknowledgement> ackList = getDocAckByClaimKey(transacKey);
	    		
	    		if(ackList != null && !ackList.isEmpty()){
	    			for (DocAcknowledgement docAcknowledgement : ackList) {
	    				if(docAcknowledgement.getBenifitFlag() != null){
	    					benefitSelected = docAcknowledgement.getBenifitFlag().equalsIgnoreCase(SHAConstants.DEATH) ? SHAConstants.DEATH : SHAConstants.OTHERS; 
	    				}
					} 
	    		}
    		}
    	}
    	else{
    		for (Reimbursement reimbursement : reimbList) {
    			if(reimbursement.getBenefitsId() != null){
    				benefitSelected = ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(reimbursement.getBenefitsId().getKey()) ? SHAConstants.DEATH : SHAConstants.OTHERS;
        			break;
    			}
			}
    		
    	}    	
    	
    	return benefitSelected;
    	
    }
    
    public List<RODQueryDetailsDTO> getPARODQueryDetailsForCreateRodAndBillEntry(
			Claim claim, Long ackKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<Reimbursement> rodList = (List<Reimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (Reimbursement rod : rodList) {

				Reimbursement reimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getPARodQueryBasedOnAckKey(
						rod.getKey(), ackKey);

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				DocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (reimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(reimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount(),0d));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails

										.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());

								rodQueryDetails.setBillClassification(getBillClassificationValue(doc));
								if(null != getDiagnosisName && !("null").equalsIgnoreCase(getDiagnosisName) && (SHAConstants.HOSP).equalsIgnoreCase(doc.getBenifitFlag())){
								rodQueryDetails.setDiagnosis(getDiagnosisName);
								}
							/*	rodQueryDetails.setClaimedAmount(calculatedClaimedAmt(
										doc.getHospitalizationClaimedAmount(),
										doc.getPreHospitalizationClaimedAmount(),
										doc.getPostHospitalizationClaimedAmount()));*/
								rodQueryDetails.setClaimedAmount(doc.getBenifitClaimedAmount());
								rodQueryDetails.setQueryRaisedRole(rodQuery.getCreatedBy());
								rodQueryDetails.setQueryRaisedDate(rodQuery
										.getCreatedDate());
								rodQueryDetails.setQueryStatus(rod.getStatus()
										.getProcessValue());
								rodQueryDetails.setReimbursementKey(rod.getKey());
								rodQueryDetails.setReimbursementQueryKey(rodQuery.getKey());
								rodQueryDetails.setAcknowledgementKey(rod.getDocAcknowLedgement().getKey());
								rodQueryDetails.setReplyStatus(rodQuery.getQueryReply());
								rodQueryDetails.setDocumentTypeId(rod.getDocAcknowLedgement().getDocumentTypeId());
								if(null != rod.getDocAcknowLedgement() && null != rod.getDocAcknowLedgement().getDocumentReceivedFromId())
								{
									rodQueryDetails.setDocReceivedFrom(rod.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
								}
								
								if(rodQuery.getQueryReply() != null && rodQuery.getQueryReply().equalsIgnoreCase("N")){
									rodQueryDetails.setOnPageLoad(true);
								}
//								rodQueryDetails.setOnPageLoad(true);
								String benefit = "";
								if(null != doc.getBenifitFlag())
								{
									benefit = doc.getBenifitFlag();
								}
								List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
								
								
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursement.getKey());
								
								String additionalCover = "";
								Double addOnCoversAmt = 0d;
								List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
								if(null != additionalCovers)
								{
									for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
										
										Long coverId = paAdditionalCovers.getCoverId();
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null  != paAdditionalCovers.getClaimedAmount())
										{
											addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
										}
										
										coversList.add(coverName);
									}
									
									if(null != coversList && !coversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : coversList) {
											additionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
									
								}
								
								List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
								
									optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursement.getKey());
								Double optionCoversClaimedAmt = 0d;
								String optionalCover = "";
								List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
								if(null != optionalCovers)
								{
									for (PAOptionalCover paOptionalCover : optionalCovers) {						
										Long coverId = paOptionalCover.getCoverId();								
										MasPaClaimCovers coverName = getCoversName(coverId);
										if(null != paOptionalCover.getClaimedAmount())
										{
											optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
										}
										
										optionalCoversList.add(coverName);
									} 
									
									if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
											optionalCover += masPaClaimCovers.getCoverDescription();
										}
									}
								}
								
								String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
								
								if(null != benefitOrCover)
								{								
									rodQueryDetails.setBenifitOrCover(benefitOrCover);
								}
								
								if(null != doc.getBenifitClaimedAmount())
								{
									Double claimedAmt = doc.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									rodQueryDetails.setClaimedAmount(claimedAmt);
								}
								rodQueryDetails.setBenefitClaimedAmount(doc.getBenifitClaimedAmount());
								rodQueryDetailsList.add(rodQueryDetails);								

							}

							if (rodQuery.getQueryReply() != null
									&& rodQuery.getQueryReply()
											.equalsIgnoreCase("N")) {
								rodQueryDetails.setOnPageLoad(true);
							}
							// rodQueryDetails.setOnPageLoad(true);
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}

public List<Reimbursement> findReimbursementByClaimKey(Long claimKey) {
	Query query = entityManager
			.createNamedQuery("Reimbursement.findByClaimKey");
	query = query.setParameter("claimKey", claimKey);

	// Integer.parseInt(Strin)
	List<Reimbursement> reimbList = (List<Reimbursement>) query
			.getResultList();
	
	return reimbList;
	
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

public void changeCpuCode(Intimation objIntimation){
	
	if(objIntimation.getOriginalCpuCode() != null){
		if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct().getKey() != null){
			MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(objIntimation.getPolicy().getProduct().getKey());
			if(gmcRoutingProduct != null){
				TmpCPUCode masCpuCode = objIntimation.getCpuCode();
				MasCpuLimit masCpuLimit = getMasCpuLimit(objIntimation.getCpuCode().getCpuCode(), SHAConstants.PROCESSING_CPU_CODE_GMC);
				if(masCpuLimit != null){
					if(! objIntimation.getCpuCode().getCpuCode().equals(masCpuLimit.getSettlementCpuCode())){
						 masCpuCode = getMasCpuCode(masCpuLimit.getSettlementCpuCode());
						objIntimation.setCpuCode(masCpuCode);
						entityManager.merge(objIntimation);
						entityManager.flush();
					}
				}
			}
			else{
				MasCpuLimit masCpuLimit = getMasCpuLimit(objIntimation.getOriginalCpuCode(), SHAConstants.PROCESSING_CPU_CODE_RETAIL);
				if(masCpuLimit != null){
					if(! objIntimation.getCpuCode().getCpuCode().equals(masCpuLimit.getSettlementCpuCode())){
						TmpCPUCode masCpuCode = getMasCpuCode(masCpuLimit.getSettlementCpuCode());
						objIntimation.setCpuCode(masCpuCode);
						entityManager.merge(objIntimation);
						entityManager.flush();
					}
				}
			}
			//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//			else{
//				//GMC Product List - Only for Bangalore cpu code
//				//if(objIntimation.getCpuCode().getKey().equals(ReferenceTable.BANGALORE_CPU_ID)){
//				MasCpuLimit masCpuLimit = getMasCpuLimit(objIntimation.getCpuCode().getCpuCode(), SHAConstants.PROCESSING_CPU_CODE_GMC);
//				if(masCpuLimit != null){
//					if(! objIntimation.getCpuCode().getCpuCode().equals(masCpuLimit.getSettlementCpuCode())){
//						TmpCPUCode masCpuCode = getMasCpuCode(masCpuLimit.getSettlementCpuCode());
//						objIntimation.setCpuCode(masCpuCode);
//						entityManager.merge(objIntimation);
//						entityManager.flush();
//					}
//				}
//				//}
//			}
		}
	}
	//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//	//added for CPU routing
//	if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct().getKey() != null){
//		String CpuCode= getMasProductCpu(objIntimation.getPolicy().getProduct().getKey());
//		if(CpuCode != null){
//			TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//			objIntimation.setCpuCode(masCpuCode);
//			entityManager.merge(objIntimation);
//			entityManager.flush();
//		}
//	}
//	//added for CPU routing
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

@SuppressWarnings({ "unchecked" })
public List<RODBillDetails> getBillEntryDetails(Long reimbursementKey) {

	Query query = entityManager
			.createNamedQuery("RODBillDetails.findByReimbursementKey");
	query = query.setParameter("reimbursementKey", reimbursementKey);

	List<RODBillDetails> billDetails = (List<RODBillDetails>) query
			.getResultList();

	return billDetails;
}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValuesForUpdateRod(Claim claim) {

	List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;

		//List<Reimbursement> reimbursementDetails = getReimbursementDetails(claim.getKey());
	  List<Reimbursement> reimbursementDetails = getReimbursementDetailsForReconsideration(claim.getKey());
	  
	  
		
		if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
			
			AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
			List<ReconsiderRODRequestTableDTO> reimbursementList = acknowledgeDocumentReceivedMapper
					.getReimbursementDetails(reimbursementDetails);

			if (null != reimbursementList && !reimbursementList.isEmpty()) {
				reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();

					for (ReconsiderRODRequestTableDTO reimbursementData : reimbursementList) {
						/**
						 * Since settled claim functionality is not yet implemented, 
						 * no status is available for the same. Hence as of now, only
						 * rejected claims will be shown for reconsideration.
						 * */
						if(
								(ReferenceTable.PAYMENT_SETTLED.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) || 
										ReferenceTable.PROCESS_CLAIM_APPROVAL_REJECTION_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.PAYMENT_REJECTED.equals(reimbursementData.getStatusId()) ||
										(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId())  && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
										(ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()) && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
										(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS.equals(reimbursementData.getStatusId())) || (ReferenceTable.REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS.equals(reimbursementData.getStatusId()) ||
										(ReferenceTable.CREATE_ROD_STATUS_KEY.equals(reimbursementData.getStatusId())) || (ReferenceTable.BILL_ENTRY_STATUS_KEY.equals(reimbursementData.getStatusId()))) 
										)
							)
										
						 {
							DocAcknowledgement docAck =  getDocAcknowledgment(reimbursementData.getDocAcknowledgementKey());
							ReconsiderRODRequestTableDTO docAckData = AcknowledgeDocumentReceivedMapper.getDocAckRecDetails(docAck);
										
						if (reimbursementData.getKey().equals(
								docAckData.getRodKey())) {
							docAckData.setRodStatus(reimbursementData
									.getRodStatus());
							if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementData.getStageId()))
							{
								docAckData.setApprovedAmt(reimbursementData.getFinancialApprovedAmount());
							}
							else if(ReferenceTable.BILLING_STAGE.equals(reimbursementData.getStageId()))
							{
								docAckData.setApprovedAmt(reimbursementData.getBillingApprovedAmount());
							}
							else 
							{
								docAckData.setApprovedAmt(reimbursementData.getApprovedAmt());
							}
							
							if(ReferenceTable.FINANCIAL_SETTLED.equals(reimbursementData.getStatusId()))
							{
								ClaimPayment claimPaymentDetails = getClaimPaymentDetails(reimbursementData.getRodNo());
								if(null != claimPaymentDetails)
								{
								docAckData.setApprovedAmt(claimPaymentDetails.getApprovedAmount());
								docAckData.setIsSettledReconsideration(true);
								}
							}
							
							docAckData.setRodNo(reimbursementData.getRodNo());
							docAckData.setBillClassification(getBillClassificationValue(docAckData));
							
							Reimbursement reimbObj = getReimbursement(reimbursementData.getKey());
							if(null != reimbObj){
								Double claimedAmount = getClaimedAmount(reimbObj);
								docAckData.setClaimedAmt(claimedAmount);
							
								MastersValue patientStatus = reimbObj.getPatientStatus();
								if(patientStatus != null) {
									docAckData.setPatientStatus(new SelectValue(patientStatus.getKey(), patientStatus.getValue()));
								}
								
								docAckData.setLegalHeirName(reimbObj.getNomineeName());
								docAckData.setLegalHeirAddr(reimbObj.getNomineeAddr());
							}
							
							docAckData.setBenefitFlag(docAck.getBenifitFlag());
							if(null != docAck.getBenifitClaimedAmount())
								docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
							String benefit = "";
							if(null != docAck.getBenifitFlag())
							{
								benefit = docAck.getBenifitFlag();
							}
							List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
							
							
								 additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getKey());
							/*}
							else
							{
								additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
							}*/
							String additionalCover = "";
							Double addOnCoversAmt = 0d;
							List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
							if(null != additionalCovers)
							{
								for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
									
									Long coverId = paAdditionalCovers.getCoverId();
									MasPaClaimCovers coverName = getCoversName(coverId);
									
									coversList.add(coverName);
									
									
									if(null  != paAdditionalCovers.getClaimedAmount())
									{
										addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
									}
								}
								
								if(null != coversList && !coversList.isEmpty())
								{
									for (MasPaClaimCovers masPaClaimCovers : coversList) {
										additionalCover += masPaClaimCovers.getCoverDescription();
									}
								}
								
							}
							
							List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
													
								optionalCovers =  getOptionalCoversForRodAndBillEntry(reimbursementData.getKey());
								
						/*	}
							else
							{
								optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
							}*/
							String optionalCover = "";
							Double optionCoversClaimedAmt = 0d;
							List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
							if(null != optionalCovers)
							{
								for (PAOptionalCover paOptionalCover : optionalCovers) {						
									Long coverId = paOptionalCover.getCoverId();								
									MasPaClaimCovers coverName = getCoversName(coverId);
									
									optionalCoversList.add(coverName);
									

									if(null  != paOptionalCover.getClaimedAmount())
									{
										optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
									}
									
								} 
								
								if(null != optionalCoversList && !optionalCoversList.isEmpty())
								{
									for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
										optionalCover += masPaClaimCovers.getCoverDescription();
									}
								}
							}
							
							String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
							
							if(null != benefitOrCover)
							{								
								docAckData.setBenifitOrCover(benefitOrCover);
							}
						
						
							if (null != docAckData.getHospitalizationClaimedAmt())
								docAckData.setClaimedAmt(calculatedClaimedAmt(
												docAckData.getHospitalizationClaimedAmt(),
												docAckData.getPreHospClaimedAmt(),
												docAckData.getPostHospClaimedAmt(),0d));
															
							
							if(null != docAck.getBenifitClaimedAmount())
							{
								Double claimedAmt = docAck.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
								docAckData.setClaimedAmt(claimedAmt);
								docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
							}
							if(null != docAckData.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsHospitalCashFlag()))
							{
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.HOSPITAL_CASH_FLAG);
								if(null != reimbursementBenefits)
								{
									docAckData.setHospitalCashNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
									docAckData.setHospitalCashPerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
									docAckData.setHospitalCashTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
									docAckData.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits.getKey());
								}
							}
							if(null != docAckData.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsPatientCareFlag()))
							{
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.PATIENT_CARE_FLAG);
								if(null != reimbursementBenefits)
								{
									docAckData.setPatientCareNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
									docAckData.setPatientCarePerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
									docAckData.setPatientCareTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
									docAckData.setPatientCareReimbursementBenefitsKey(reimbursementBenefits.getKey());
									List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits.getKey());
									if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
									{
										List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
										for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
											PatientCareDTO patientCareDTO = new PatientCareDTO();
											patientCareDTO.setEngagedFrom(reimbursementBenefitsDetails.getEngagedFrom());
											patientCareDTO.setEngagedTo(reimbursementBenefitsDetails.getEngagedTo());
											patientCareDTO.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails.getKey());
											patientCareList.add(patientCareDTO);
										}
										docAckData.setPatientCareDTOList(patientCareList);
									}
								}
							}
							
							if(("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()))
								docAckData.setSelect(true);
							
							docAckData.setRodKey(reimbursementData.getKey());
							reconsiderRODRequestDTO.add(docAckData);
							//break;
						}
					}
				}
				//}
			}
		}
	return reconsiderRODRequestDTO;
}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValuesForCreateRodAndBillEntryforUpdateRod(Claim claim) {

	List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;

	  List<Reimbursement> reimbursementDetails = getReimbursementDetailsForReconsideration(claim.getKey());

		if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
			
			AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper =AcknowledgeDocumentReceivedMapper.getInstance();
			List<ReconsiderRODRequestTableDTO> reimbursementList = acknowledgeDocumentReceivedMapper
					.getReimbursementDetails(reimbursementDetails);

			if (null != reimbursementList && !reimbursementList.isEmpty()) {
				reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();
				//for (ReconsiderRODRequestTableDTO docAckData : docAckList) {

					for (ReconsiderRODRequestTableDTO reimbursementData : reimbursementList) {
						// if(docAckData.getRodKey().equals(reimbursementData.getKey()))
						/**
						 * Since settled claim functionality is not yet implemented, 
						 * no status is available for the same. Hence as of now, only
						 * rejected claims will be shown for reconsideration.
						 * */
						// if((reimbursementData.getKey().equals(docAckData.getRodKey()))
						// &&
						// ("Settled").equals(reimbursementData.getRodStatus()))
						if(
								/*(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||  
										ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
										(ReferenceTable.CREATE_ROD_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.PROCESS_CLAIM_REQUEST_CLSOED_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.BILLING_CLOSED_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()))
										
								)*/
								
								(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||  
									//	ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId())||
										ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursementData.getStatusId()) ||
										ReferenceTable.PAYMENT_REJECTED.equals(reimbursementData.getStatusId())||
										(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS.equals(reimbursementData.getStatusId())  && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
										(ReferenceTable.FINANCIAL_CLOSED_STATUS.equals(reimbursementData.getStatusId()) && (getReimbursementQueryStatus(reimbursementData.getKey()))) ||
										(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS.equals(reimbursementData.getStatusId())) || (ReferenceTable.REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS.equals(reimbursementData.getStatusId())) ||
										(ReferenceTable.CREATE_ROD_STATUS_KEY.equals(reimbursementData.getStatusId())) || (ReferenceTable.CREATE_ROD_STATUS_KEY.equals(reimbursementData.getStatusId())))
							)
										
						 {
							DocAcknowledgement docAck =  getDocAcknowledgment(reimbursementData.getDocAcknowledgementKey());
							ReconsiderRODRequestTableDTO docAckData = AcknowledgeDocumentReceivedMapper.getDocAckRecDetails(docAck);
							/*if (null != docAcknowledgements && !docAcknowledgements.isEmpty()) {
									List<ReconsiderRODRequestTableDTO> docAckList = AcknowledgeDocumentReceivedMapper
											.getDocAcknowledgeList(docAcknowledgements);	
									if(null != docAckList && !docAckList.isEmpty())
									{
										docAckData = docAckList.get(0);
									}
							}*/
										
						if (reimbursementData.getKey().equals(
								docAckData.getRodKey())) {
							/*docAckData.setApprovedAmt(reimbursementData
									.getApprovedAmt());*/
							docAckData.setRodStatus(reimbursementData
									.getRodStatus());
							if(ReferenceTable.FINANCIAL_STAGE.equals(reimbursementData.getStageId()))
							{
								docAckData.setApprovedAmt(reimbursementData.getFinancialApprovedAmount());
							}
							else if(ReferenceTable.BILLING_STAGE.equals(reimbursementData.getStageId()))
							{
								docAckData.setApprovedAmt(reimbursementData.getBillingApprovedAmount());
							}
							else 
							{
								docAckData.setApprovedAmt(reimbursementData.getApprovedAmt());
							}
							
							if(ReferenceTable.FINANCIAL_SETTLED.equals(reimbursementData.getStatusId()))
							{
								ClaimPayment claimPaymentDetails = getClaimPaymentDetails(reimbursementData.getRodNo());
								if(null != claimPaymentDetails)
								{
								docAckData.setApprovedAmt(claimPaymentDetails.getApprovedAmount());
								}
							}
							docAckData.setRodNo(reimbursementData.getRodNo());
							docAckData.setBillClassification(getBillClassificationValue(docAckData));
							
							docAckData.setBenefitFlag(docAck.getBenifitFlag());
							if(null != docAck.getBenifitClaimedAmount())
								docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
							String benefit = "";
							if(null != docAck.getBenifitFlag())
							{
								benefit = docAck.getBenifitFlag();
							}
							List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();								
							
								 //additionalCovers =  getAdditionalCovers(docAck.getKey());
							
							additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementData.getKey());
							
							String additionalCover = "";
							Double addOnCoversAmt = 0d;
							List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
							if(null != additionalCovers)
							{
								for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
									
									Long coverId = paAdditionalCovers.getCoverId();
									MasPaClaimCovers coverName = getCoversName(coverId);
									
									if(null  != paAdditionalCovers.getClaimedAmount())
									{
										addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
									}
									
									coversList.add(coverName);
								}
								
								if(null != coversList && !coversList.isEmpty())
								{
									for (MasPaClaimCovers masPaClaimCovers : coversList) {
										additionalCover += masPaClaimCovers.getCoverDescription();
									}
								}
								
							}
							
							List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
													
								optionalCovers =  getOptionalCoversForRodAndBillEntry(reimbursementData.getKey());
								Double optionCoversClaimedAmt = 0d;
						/*	}
							else
							{
								optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursementData.getRodKey());
							}*/
							String optionalCover = "";
							List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
							if(null != optionalCovers)
							{
								for (PAOptionalCover paOptionalCover : optionalCovers) {						
									Long coverId = paOptionalCover.getCoverId();								
									MasPaClaimCovers coverName = getCoversName(coverId);
									if(null != paOptionalCover.getClaimedAmount())
									{
										optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
									}
									optionalCoversList.add(coverName);
								} 
								
								if(null != optionalCoversList && !optionalCoversList.isEmpty())
								{
									for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
										optionalCover += masPaClaimCovers.getCoverDescription();
									}
								}
							}
							
							String benefitOrCover = benefit.isEmpty() ? additionalCover + "," + optionalCover : benefit + "," +  additionalCover + "," + optionalCover;
							
							if(null != benefitOrCover)
							{								
								docAckData.setBenifitOrCover(benefitOrCover);
							}
						
						
							//if (null != docAckData.getHospitalizationClaimedAmt())
								/*docAckData.setClaimedAmt(calculatedClaimedAmt(
												docAckData.getHospitalizationClaimedAmt(),
												docAckData.getPreHospClaimedAmt(),
												docAckData.getPostHospClaimedAmt()));
												*/			
							
							if(null != docAck.getBenifitClaimedAmount())
							{
								
								//if(null != docAck.getBenifitClaimedAmount())
								//{
									Double claimedAmt = docAck.getBenifitClaimedAmount() + addOnCoversAmt + optionCoversClaimedAmt;
									docAckData.setClaimedAmt(claimedAmt);
								//}
								
								//docAckData.setClaimedAmt(docAck.getBenifitClaimedAmount());
								docAckData.setBenefitClaimedAmnt(docAck.getBenifitClaimedAmount());
							}
							if(null != docAckData.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsHospitalCashFlag()))
							{
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.HOSPITAL_CASH_FLAG);
								if(null != reimbursementBenefits)
								{
									docAckData.setHospitalCashNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
									docAckData.setHospitalCashPerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
									docAckData.setHospitalCashTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
									docAckData.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits.getKey());
								}
							}
							if(null != docAckData.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(docAckData.getAddOnBenefitsPatientCareFlag()))
							{
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(reimbursementData.getKey(), SHAConstants.PATIENT_CARE_FLAG);
								if(null != reimbursementBenefits)
								{
									docAckData.setPatientCareNoOfDaysBills(reimbursementBenefits.getNumberOfDaysBills());
									docAckData.setPatientCarePerDayAmtBills(reimbursementBenefits.getPerDayAmountBills());
									docAckData.setPatientCareTotalClaimedAmount(reimbursementBenefits.getTotalClaimAmountBills());
									docAckData.setPatientCareReimbursementBenefitsKey(reimbursementBenefits.getKey());
									List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits.getKey());
									if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
									{
										List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
										for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
											PatientCareDTO patientCareDTO = new PatientCareDTO();
											patientCareDTO.setEngagedFrom(reimbursementBenefitsDetails.getEngagedFrom());
											patientCareDTO.setEngagedTo(reimbursementBenefitsDetails.getEngagedTo());
											patientCareDTO.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails.getKey());
											patientCareList.add(patientCareDTO);
										}
										docAckData.setPatientCareDTOList(patientCareList);
									}
								}
							}
							
							if(("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()))
								docAckData.setSelect(true);
							docAckData.setRodKey(reimbursementData.getKey());
							reconsiderRODRequestDTO.add(docAckData);
							//break;
						}
					}
				}
				//}
			}
		}
	return reconsiderRODRequestDTO;
}

	@SuppressWarnings("unchecked")
	public ClaimPayment getClaimPaymentByRodKey(Long rodkey) {
        Query query = entityManager.createNamedQuery("ClaimPayment.findByRodKey");
        query.setParameter("rodKey", rodkey);
        List<ClaimPayment> claimPayment = (List<ClaimPayment>)query.getResultList();
        
        if(claimPayment != null && ! claimPayment.isEmpty()){
                for (ClaimPayment claimPayment2 : claimPayment) {
                        entityManager.refresh(claimPayment2);
                }
                return claimPayment.get(0);
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
	 
	 public List<RODDocumentSummary> getRODSummaryDetailsByReimbursementKey(
				Long rodKey) {
			List<RODDocumentSummary> rodDocSummary = null;
			Query query = entityManager
					.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
			query.setParameter("reimbursementKey", rodKey);
			if (null != query.getResultList() && !query.getResultList().isEmpty()) {
				rodDocSummary = query.getResultList();
			}
			return rodDocSummary;
		}
	 
	 public MasAcknowledgementDoctor getSearchUser(String userName) {

		 MasAcknowledgementDoctor doctor = null;

			userName = userName.toLowerCase();

			Query query = entityManager
					.createNamedQuery("MasAcknowledgementDoctor.findByDoctorUserId");
			query.setParameter("userId", userName);

			List<MasAcknowledgementDoctor> doctorList = query.getResultList();
			if (doctorList != null && !doctorList.isEmpty()) {
				doctor = doctorList.get(0);
			}

			return doctor;

		}
    
	 public void acknowledgementDocumentCreationBatch() {
		 
		 List<AcknowledgeDocumentBatchTable> ackDocumentIntimationList = getAckDocumentCreationIntimation();

		 if(ackDocumentIntimationList != null){
			 for (AcknowledgeDocumentBatchTable ackDocumentIntimation : ackDocumentIntimationList) {
				 
				 Boolean isValidClaimForAck = true;
				 ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
				 ClaimDto claimDTO = null;
				 DocAcknowledgement docAck = new DocAcknowledgement();
				 Boolean isQueryReplyReceived = false;
				 Boolean isReconsideration = false;
				 Boolean isQueryReplyNo = false;
				 Boolean isQueryStatusYes = false;
				 String templateName = "AckReceipt";
				 ReportDto reportDto = null;
				 DocumentGenerator docGen = null;
				 DocumentDetailsDTO documentDetailsDTO = new DocumentDetailsDTO();
				 AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
				 
			 if (ackDocumentIntimation.getClaim() !=null && 
					 ackDocumentIntimation.getClaim().getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
				 isValidClaimForAck = false;
				 log.info("------DocAcknowledgement Creation Batch, Claim is Closed------>"+ackDocumentIntimation.getIntimation().getIntimationId()+"<------------");
			 }
			 if (ackDocumentIntimation.getClaim() !=null && ackDocumentIntimation.getClaim().getLegalFlag() != null &&
					 ackDocumentIntimation.getClaim().getLegalFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				 log.info("------DocAcknowledgement Creation Batch, intimation in Legally Locked------>"+ackDocumentIntimation.getIntimation().getIntimationId()+"<------------");

				 isValidClaimForAck = false;
			 }
			 if (ackDocumentIntimation.getClaim() !=null && (getDBTaskForPreauth(
					 ackDocumentIntimation.getClaim().getIntimation(),
					 SHAConstants.FLE_CURRENT_QUEUE))) {
				 log.info("------DocAcknowledgement Creation Batch, intimation in FLE Q------>"+ackDocumentIntimation.getIntimation().getIntimationId()+"<------------");
				 isValidClaimForAck = false;
			 }
			 if (ackDocumentIntimation.getClaim() !=null &&
					 (isPreviousHospAcknowledgment(ackDocumentIntimation.getClaim().getKey()))) {
				 log.info("------DocAcknowledgement Creation Batch, Hospitalisation Ack Already Created------>"+ackDocumentIntimation.getIntimation().getIntimationId()+"<------------");
				 isValidClaimForAck = false;
			 }
			 try{
				 if(isValidClaimForAck && ackDocumentIntimation.getClaim() != null){
					 log.info("------DocAcknowledgement Batch Creation Starts------>"+ackDocumentIntimation.getClaim().getClaimId()+"<------------");
					 claimDTO = ClaimMapper.getInstance()
							 .getClaimDto(ackDocumentIntimation.getClaim());
					 rodDTO.setClaimDTO(claimDTO);
					 NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
							 .getNewIntimationDto(ackDocumentIntimation.getClaim().getIntimation());
					 claimDTO.setNewIntimationDto(newIntimationDto);
					 rodDTO.setIsConversionAllowed(false);
					 rodDTO.getDocumentDetails().setDocumentCheckList(
							 setDocumentCheckListTableValues());
					 generateAcknowledgeNo(rodDTO);
					 Status status =  getStatusByKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
					 Stage stage  =   getStageByKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
					 MastersValue notApplicableStatus = getMaster(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					 SelectValue notApplicableStatusValue = new SelectValue();
					 notApplicableStatusValue.setId(notApplicableStatus.getKey());
					 notApplicableStatusValue.setValue(notApplicableStatus.getValue());
					 MastersValue receivedStatus = getMaster(ReferenceTable.ACK_DOC_RECEIVED_PHOTOCOPY);
					 SelectValue receivedStatusValue = new SelectValue();
					 receivedStatusValue.setId(receivedStatus.getKey());
					 receivedStatusValue.setValue(receivedStatus.getValue());
					 MastersValue docReceivedFrom = getMaster(ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL);
					 SelectValue docReceivedSelectValue = new SelectValue();
					 docReceivedSelectValue.setId(docReceivedFrom.getKey());
					 docReceivedSelectValue.setValue(docReceivedFrom.getValue());
					 MastersValue modeOfReceipt = getMaster(ReferenceTable.NO_HC_MODE_OF_RECEIPT);
					 docAck.setAcknowledgeNumber(rodDTO.getDocumentDetails().getAcknowledgementNumber());
					 docAck.setStatus(status);
					 docAck.setStage(stage); 
					 docAck.setDocumentReceivedFromId(docReceivedFrom);
					 docAck.setDocumentReceivedDate((new Timestamp(System
							 .currentTimeMillis())));
					 docAck.setModeOfReceiptId(modeOfReceipt);
					 docAck.setActiveStatus(1l);
					 docAck.setClaim(searchByClaimKey(rodDTO.getClaimDTO().getKey()));
					 String strUserName = "SYSTEM";
					 docAck.setCreatedBy(strUserName);
					 docAck.setCreatedDate((new Timestamp(System
							 .currentTimeMillis())));
					 docAck.setHospitalisationFlag(SHAConstants.YES_FLAG);
					 docAck.setReconsiderationRequest(SHAConstants.N_FLAG);
					 docAck.setPreHospitalisationFlag(SHAConstants.N_FLAG);
					 docAck.setPostHospitalisationFlag(SHAConstants.N_FLAG);
					 docAck.setPartialHospitalisationFlag(SHAConstants.N_FLAG);
					 docAck.setLumpsumAmountFlag(SHAConstants.N_FLAG);
					 docAck.setHospitalCashFlag(SHAConstants.N_FLAG);
					 docAck.setHospitalizationRepeatFlag(SHAConstants.N_FLAG);
					 docAck.setPatientCareFlag(SHAConstants.N_FLAG);
					 entityManager.persist(docAck);
					 entityManager.flush();
					 log.info("------DocAcknowledgement Created in Batch Successfully------>"+docAck.getAcknowledgeNumber()+"<------------");
					 entityManager.refresh(docAck);

					 List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
							 .getDocumentCheckList();
					 if (!docCheckList.isEmpty()) {
						 for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
							 // if(null != docCheckListDTO.getNoOfDocuments())
							 docCheckListDTO.setDocTypeId(docCheckListDTO.getKey());
							 docCheckListDTO.setReceivedStatus(notApplicableStatusValue);
							 if(docCheckListDTO.getKey() != null && docCheckListDTO.getKey().equals(10l)){
								 docCheckListDTO.setReceivedStatus(receivedStatusValue);
								 docCheckListDTO.setRemarks("Please submit the original which is mandatory for claim processing");
							 }
							 RODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
									 .getRODDocumentCheckList(docCheckListDTO);
							 rodDocumentCheckList.setDocAcknowledgement(docAck);
							 rodDocumentCheckList.setCreatedBy(strUserName);
							 docAck.setCreatedDate((new Timestamp(System
									 .currentTimeMillis())));
							 entityManager.persist(rodDocumentCheckList);
							 entityManager.flush();
							 log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");

						 }
					 }
						 Claim claim = docAck.getClaim();
						 log.info("------DocAcknowledgement Batch Creation Template Upload Starts------>"+docAck.getAcknowledgeNumber()+"<------------");
						 log.info("claim.getClaimId() : "+claim.getClaimId());
						 documentDetailsDTO.setDocumentsReceivedFrom(docReceivedSelectValue);
						 documentDetailsDTO.setDocumentCheckList(rodDTO.getDocumentDetails()
								 .getDocumentCheckList());
						 rodDTO.setDocumentDetails(documentDetailsDTO);
						 Hospitals registeredHospital = null;
						 TmpHospital tempHospital = null;
						 if (claim.getIntimation().getHospital() != null
								 && claim.getIntimation().getHospitalType() != null
								 && StringUtils.contains(claim.getIntimation().getHospitalType()
										 .getValue().toLowerCase(), "network")) {
							 registeredHospital = getHospitalDetailsByKey(claim.getIntimation().getHospital());
							 if (null != registeredHospital) {
								 HospitalDto hospitaldto = new HospitalDto(
										 registeredHospital);
								 hospitaldto.setRegistedHospitals(registeredHospital);
								 rodDTO.getClaimDTO().getNewIntimationDto().setHospitalType(hospitaldto.getHospitalType());
								 rodDTO.getClaimDTO().getNewIntimationDto().setHospitalTypeValue(hospitaldto.getHospitalType() != null ?hospitaldto.getHospitalType().getValue() : "");
								 rodDTO.getClaimDTO().getNewIntimationDto().setHospitalDto(hospitaldto);

							 }
						 } 
						 if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
								 && rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId() != null
								 && rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
							 String hospitAddress = rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getAddress();
							 String[] hospAddress = StringUtil.split(hospitAddress,',');
							 int length;
							 if(hospAddress.length != 0 ){
								 length=hospAddress.length;			
								 rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr1(hospAddress[0]);
								 if(length >2){
									 rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr2(hospAddress[1]);
								 }
								 if(length >3){
									 rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr3(hospAddress[2]);
								 }
								 if(length >4){
									 rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr4(hospAddress[3]);
								 }
							 }
						 }
						 //						 ClaimMapper claimMapper = ClaimMapper.getInstance();
						 //						 ClaimDto claimDto = claimMapper.getClaimDto(claim);
						 Long policyKey = claim.getIntimation().getPolicy().getKey();
						 Policy policy = getPolicyDetails(policyKey);
						 claimDTO.getNewIntimationDto().setPolicy(policy);
						 Insured insured = claim.getIntimation().getInsured();
						 claimDTO.getNewIntimationDto().setInsuredPatient(insured);

						 log.info("getIntimation().getPolicy().getKey() :  "+claim.getIntimation().getPolicy().getKey());
						 rodDTO.getDocumentDetails().setAcknowledgementNumber(docAck.getAcknowledgeNumber());
						 rodDTO.getDocumentDetails().setDocumentsReceivedDate(docAck.getDocumentReceivedDate());

						 rodDTO.setClaimDTO(claimDTO);
						 rodDTO.setStrUserName("SYSTEM");
						 reportDto = new ReportDto();
						 List <ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
						 rodDTOList.add(rodDTO);
						 reportDto.setBeanList(rodDTOList);
						 reportDto.setClaimId(claimDTO.getClaimId());
						 docGen = new DocumentGenerator();
						 log.info("------DocAcknowledgement Batch Creation Template Generation Starts------>"+docAck.getAcknowledgeNumber()+"<------------");
						 String filePath = docGen.generatePdfDocument(templateName, reportDto);
						 log.info("------DocAcknowledgement Batch Creation Template Generation Ends------>"+docAck.getAcknowledgeNumber()+"<------------");
						 if( !filePath.isEmpty() )
						 {
							 rodDTO.setDocFilePath(filePath);
							 rodDTO.setDocType(SHAConstants.ACK_RECEIPT);

							 if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
							 {
								 WeakHashMap dataMap = new WeakHashMap();
								 dataMap.put("intimationNumber",docAck.getClaim().getIntimation().getIntimationId());
								 Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
								 if(null != objClaim)
								 {
									 dataMap.put("claimNumber",objClaim.getClaimId());
									 if(null != objClaim.getClaimType())
									 {
										 if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
										 {
											 Preauth preauth = getPreauthClaimKey( objClaim.getKey());
											 if(null != preauth)
												 dataMap.put("cashlessNumber", preauth.getPreauthId());
										 }
									 }
								 }
								 dataMap.put("filePath", rodDTO.getDocFilePath());
								 dataMap.put("docType", rodDTO.getDocType());
								 dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
								 dataMap.put("createdBy", rodDTO.getStrUserName());
								 SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
								 log.info("------DocAcknowledgement Batch Creation Template Upload Ends------>"+docAck.getAcknowledgeNumber()+"<------------");
							 }
						 }
					 log.info("------DocAcknowledgement Batch Creation Submit task Starts------>"+docAck.getAcknowledgeNumber()+"<------------");
					 submitTaskToDB(rodDTO,docAck,isQueryStatusYes, isReconsideration);
					 ackDocumentIntimation.setModifiedBy("SYSTEM");
					 ackDocumentIntimation.setModifiedDate((new Timestamp(System
							 .currentTimeMillis())));
					 ackDocumentIntimation.setAckBatchRunFlag("Y");
					 ackDocumentIntimation.setAckBatchRunDate((new Timestamp(System
							 .currentTimeMillis())));
					 ackDocumentIntimation.setAckBatchRemarks("Acknowledgement Created Successfully Through Batch Process");
					 entityManager.merge(ackDocumentIntimation);
					 entityManager.flush();
					 log.info("------DocAcknowledgement Batch Creation Submit task Ends------>"+docAck.getAcknowledgeNumber()+"<------------");
				 }else{
					 ackDocumentIntimation.setModifiedBy("SYSTEM");
					 ackDocumentIntimation.setModifiedDate((new Timestamp(System
							 .currentTimeMillis())));
					 ackDocumentIntimation.setAckBatchRunFlag("F");
					 ackDocumentIntimation.setAckBatchRunDate((new Timestamp(System
							 .currentTimeMillis())));
					 ackDocumentIntimation.setAckBatchRemarks("Error Due to Invalid Acknowledgement Creation");
					 entityManager.merge(ackDocumentIntimation);
					 entityManager.flush();
					 log.info("------The mentioned claim number is not valid------>"+ackDocumentIntimation.getIntimation().getIntimationId()+"<------------"); 
					
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
				 log.error(e.toString());
				 ackDocumentIntimation.setAckBatchRunFlag("F");
				 ackDocumentIntimation.setAckBatchRunDate((new Timestamp(System
						 .currentTimeMillis())));
				 ackDocumentIntimation.setAckBatchRemarks("Error Due to Invalid Acknowledgement Creation");
				 entityManager.merge(ackDocumentIntimation);
				 entityManager.flush();
			 }
	 }
	 }
	 }
	 
	 public List<AcknowledgeDocumentBatchTable> getAckDocumentCreationIntimation() {
		 Query query = entityManager.createNamedQuery("AcknowledgeDocumentBatchTable.findIntimationByAckBatchFlag");
		 List<AcknowledgeDocumentBatchTable> ackDocumentList = (List<AcknowledgeDocumentBatchTable>)query.getResultList();

		 if(ackDocumentList != null && ! ackDocumentList.isEmpty()){
			 return ackDocumentList;
		 }
		 return null;

	 }
	 private void generateAcknowledgeNo(ReceiptOfDocumentsDTO rodDTO) {
		 Long claimKey = rodDTO.getClaimDTO().getKey();
		 Long count = getCountOfAckByClaimKey(claimKey);
		 StringBuffer ackNoBuf = new StringBuffer();
		 Long lackCount = count + 001;
		 ackNoBuf.append("ACK/")
		 .append(rodDTO.getClaimDTO().getNewIntimationDto()
				 .getIntimationId()).append("/").append(lackCount);
		 rodDTO.getDocumentDetails().setAcknowledgementNumber(
				 ackNoBuf.toString());
	 }
	 
	 public Boolean isPreviousHospAcknowledgment(Long claimKey) {
		 Boolean isExist = Boolean.FALSE;
		 List<DocAcknowledgement> previousAckList = getDocAckListByClaim(claimKey);
		 if (previousAckList != null && !previousAckList.isEmpty()) {
			 for (DocAcknowledgement docAcknowledgement : previousAckList) {
				 if(docAcknowledgement.getStatus() != null && ! docAcknowledgement.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) && (docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
						 )) {
					 isExist = Boolean.TRUE;
					 break;
				 }
			 }
		 }
		 return isExist;
	 }

	 public List<DocAcknowledgement> getDocAckListByClaim(Long claimKey)
	 {
		 Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckByClaim");
		 query = query.setParameter("claimkey", claimKey);
		 List<DocAcknowledgement> docAckList = query.getResultList();
		 if(null != docAckList && !docAckList.isEmpty())
		 {
			 //				entityManager.refresh(docAckList.get(0));
			 return docAckList;
		 }
		 return null;
	 }

	 public Status getStatusByKey(Long key) {

		 try {
			 Query findType = entityManager.createNamedQuery("Status.findByKey")
					 .setParameter("statusKey", key);
			 List<Status> status = findType.getResultList();
			 if(null != status && !status.isEmpty())
			 {
				 entityManager.refresh(status.get(0));
				 return status.get(0);
			 }
			 return null ;
		 } catch (Exception e) {
			 return null;
		 }

	 }

	 public Stage getStageByKey(Long key) {

		 try {
			 Query findType = entityManager.createNamedQuery("Stage.findByKey")
					 .setParameter("stageKey", key);
			 List<Stage> stageList = findType.getResultList();
			 if(null != stageList && !stageList.isEmpty())
			 {
				 entityManager.refresh(stageList.get(0));
				 return stageList.get(0);
			 }
			 return null ;
		 } catch (Exception e) {
			 return null;
		 }

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

	 private List<DocumentCheckListDTO> setDocumentCheckListTableValues() {
//		 MasterService masterService = new MasterService();
		 return getDocumentCheckListValues(masterService);
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
	 
	 public Reimbursement getReimbursementParent(Long key)
		{
			Query query = entityManager.createNamedQuery("Reimbursement.findReimbursementParentKey");
			query = query.setParameter("parentKey", key);
			List<Reimbursement> reimbursementObjectList = query.getResultList();
			if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
			{
				entityManager.refresh(reimbursementObjectList.get(0));
				return reimbursementObjectList.get(0);
			}
			return null;
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
}

