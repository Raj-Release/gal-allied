
package com.shaic.claim.rod.wizard.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import oracle.jdbc.proxy.annotation.GetDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SearchScreenValidationTableDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.ReportDto;
import com.shaic.claim.cashlessprocess.processicac.search.IcacRequest;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsMapper;
import com.shaic.claim.reimbursement.billing.benefits.wizard.mapper.ProcessClaimRequestBenefitsMapper;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
/*import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.rod.RODType;*/
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.mapper.AcknowledgeDocumentReceivedMapper;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.PAcoverTableViewDTO;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.BankMaster;
import com.shaic.domain.BillingHospitalisation;
import com.shaic.domain.BillingPostHospitalisation;
import com.shaic.domain.BillingPreHospitalisation;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.CoorporateBuffer;
import com.shaic.domain.DiagnosisHospitalDetails;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.GpaBenefitDetails;
import com.shaic.domain.GpaPolicy;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MASPincode;
import com.shaic.domain.MASPincodeZoneClass;
import com.shaic.domain.MasBillDetailsType;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasHospitalCashBenefit;
import com.shaic.domain.MasIrdaLevel1;
import com.shaic.domain.MasIrdaLevel2;
import com.shaic.domain.MasIrdaLevel3;
import com.shaic.domain.MasPAClaimBenefitsCover;
import com.shaic.domain.MasPAClaimCover;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NEFTQueryDetails;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PAAdditionalCovers;
import com.shaic.domain.PABenefitsCovers;
import com.shaic.domain.PAOptionalCover;
import com.shaic.domain.PaayasPolicy;
import com.shaic.domain.PhysicalDocumentVerificationDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyBankDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreHospitalisation;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.Product;
import com.shaic.domain.ProposerNominee;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.StarJioPolicy;
import com.shaic.domain.StarKotakPolicy;
import com.shaic.domain.Status;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TataPolicy;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.ViewBillRemarks;
import com.shaic.domain.preauth.BenefitAmountDetails;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.PccRemarks;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.RodBillSummary;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.skipZMR.CPUStageMapping;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimPageDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PAUploadDocumentCloseClaimDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimPageDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.UploadDocumentCloseClaimDTO;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimPageDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
/*import com.shaic.ims.bpm.claim.modelv2.HumanTask;
 import com.shaic.ims.bpm.claim.servicev2.InvokeHumanTaskServiceV2;*/
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.util.StringUtil;

/**
 * @author ntv.vijayar
 *
 */
@Stateless
public class CreateRODService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;
	
	private final Logger log = LoggerFactory.getLogger(CreateRODService.class);

	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		if (null != query) {
			docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
			entityManager.refresh(docAcknowledgement);
		}
		return docAcknowledgement;
	}
	
	

	public List<DocumentCheckListDTO> getRODDocumentList(
			MasterService masterService, DocAcknowledgement objDocAck) {

		List<DocumentCheckListDTO> documentCheckListDTO = null;
		List<RODDocumentCheckList> rodDocCheckList = masterService
				.getRODDocumentListValues(objDocAck);

		if (null != rodDocCheckList && !rodDocCheckList.isEmpty()) {
			/*
			 * for (RODDocumentCheckList rodDocumentCheckList : rodDocCheckList)
			 * {
			 * 
			 * }
			 */
			documentCheckListDTO = CreateRODMapper.getInstance()
					.getRODDocumentCheckList(rodDocCheckList);
		}

		return documentCheckListDTO;
	}
	
	public Double getClaimedAmountForRod(Long rodKey,EntityManager em) {
	
		this.entityManager = em;
		return 	getClaimedAmount(rodKey);
	
	}
	
	public Double getClaimedAmount(Long key) {
		
		Double claimedAmount = 0.0;
		try {
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByRODKey").setParameter("rodKey", key);
			List<DocAcknowledgement> reimbursementdocList = (List<DocAcknowledgement>) findType
					.getResultList();
			
			if(reimbursementdocList != null && !reimbursementdocList.isEmpty())
			{
				DocAcknowledgement reimbursementdoc = reimbursementdocList.get(0);
				entityManager.refresh(reimbursementdoc);
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", key);
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}

			Double hospitalizationClaimedAmount = null != reimbursementdoc.getHospitalizationClaimedAmount() ? reimbursementdoc.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != reimbursementdoc.getPostHospitalizationClaimedAmount() ? reimbursementdoc.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != reimbursementdoc.getPreHospitalizationClaimedAmount() ? reimbursementdoc.getPreHospitalizationClaimedAmount() : 0.0;
			
			Double otherbenefitClaimedAmount = null != reimbursementdoc.getOtherBenefitsClaimedAmount() ? reimbursementdoc.getOtherBenefitsClaimedAmount() : 0.0;
			
			//added for new product076
			Double prodHospitalCashClaimedAmount = null != reimbursementdoc.getProdHospBenefitClaimedAmount() ? reimbursementdoc.getProdHospBenefitClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount + otherbenefitClaimedAmount + currentProvisionalAmount + prodHospitalCashClaimedAmount;
			
			}	
		} catch (Exception e) {

		}
		return claimedAmount;
	}
	
	public Double getPAClaimedAmnt(Long key)
	{

		
		Double claimedAmount = 0.0;
		try {
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByRODKey").setParameter("rodKey", key);
			List<DocAcknowledgement> reimbursementdocList = (List<DocAcknowledgement>) findType
					.getResultList();
			
			if(reimbursementdocList != null && !reimbursementdocList.isEmpty())
			{
				DocAcknowledgement reimbursementdoc = reimbursementdocList.get(0);
				entityManager.refresh(reimbursementdoc);			
			
			Double benefitClaimedAmnt = null != reimbursementdoc.getBenifitClaimedAmount()? reimbursementdoc.getBenifitClaimedAmount() : 0.0;
			
			List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();			
					
			additionalCovers =  getAdditionalCoversForRodAndBillEntry(reimbursementdoc.getRodKey());
					
			Double addOnCoversAmt = 0d;			
			if(null != additionalCovers)
			{
				for (PAAdditionalCovers paAdditionalCovers : additionalCovers) {
					
					if(null  != paAdditionalCovers.getClaimedAmount())
					{
						addOnCoversAmt += paAdditionalCovers.getClaimedAmount();
					}
					
				}
			}
			

			List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
			optionalCovers = getOptionalCoversForRodAndBillEntry(reimbursementdoc.getRodKey());
			Double optionCoversClaimedAmt = 0d;			
			if(null != optionalCovers)
			{
				for (PAOptionalCover paOptionalCover : optionalCovers) {						
					
					if(null != paOptionalCover.getClaimedAmount())
					{
						optionCoversClaimedAmt += paOptionalCover.getClaimedAmount();
					}					
					
				} 
			}
			claimedAmount = benefitClaimedAmnt + addOnCoversAmt + optionCoversClaimedAmt;
			}
		}catch (Exception e) {

		}
		return claimedAmount;
	
	}

	public Preauth getLatestPreauthForClaim(Long claimKey) {
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

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitROD(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit CREATE ROD ________________" + (rodDTO.getNewIntimationDTO() != null ? rodDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
		Reimbursement reimbursement = null;
		try {
			reimbursement = saveRODValues(rodDTO,SHAConstants.HEALTH_LOB);
			NEFTDocumentUpload(rodDTO);

			if (rodDTO.getChangeHospitalDto() != null) {
				try {
					changeHospitalDetails(rodDTO.getChangeHospitalDto());   
				} catch (Exception e) {
					log.error(e.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		return reimbursement;
	}
	
	private Reimbursement saveRODValues(ReceiptOfDocumentsDTO rodDTO,String productType) {

		CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		String ackNumber = docAck.getAcknowledgeNumber();
				
		
		DocAcknowledgement docAck1 = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		String globalAck = docAck1.getAcknowledgeNumber();
		
		Reimbursement reimbursement = null;
		Boolean isQueryReplyReceived = false;
		
		Boolean shouldSkipZMR = false;
		//DocAcknowledgement docAcknowledgement = null;
		
		//Added for current provision amt.
		Double totalClaimedAmt = 0d;
		
		//Double previousCurrentProvAmt = 0d;
		//Long reimbKey = null;
		Boolean isReconsideration = false;
		
		String username1 = rodDTO.getStrUserName();
		String userNameForDB1 = SHAUtils.getUserNameForDB(username1);
		
		//HumanTask humanTaskForROD = rodDTO.getHumanTask();
		Long rodKeyFromPayload = null;		
		
		
		/*BankMaster bankMaster = validateIFSCCode( rodDTO.getDocumentDetails().getIfscCode());
		if(null != bankMaster)
		{
			rodDTO.getDocumentDetails().setBankId(bankMaster.getKey());
		}*/
		
		/*if(null != rodDTO.getDocumentDetails().getBankId())
		{*/
			/*if(null != humanTaskForROD)
			{
				PayloadBOType payloadBO = humanTaskForROD.getPayload();
				if(null != payloadBO && null != payloadBO.getRod()) 
				{
					rodKeyFromPayload = payloadBO.getRod().getKey();
				}
		
			}*/
		
		
//		legalHeirAndDocumentDetails(rodDTO);
		
		if(null != rodDTO.getRodKeyFromPayload())
		{
			rodKeyFromPayload = rodDTO.getRodKeyFromPayload();
		}
			
			ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO.getReconsiderRODdto();
			
			RODQueryDetailsDTO rodQueryDetailsDTOObj = rodDTO.getRodqueryDTO();
			
			Boolean isQueryReplyYes = false;
			Claim claimObj = null;
			if(rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getKey() != null) {
				claimObj = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
			}
			
			
			if (null != reconsiderDTO
					&& null != reconsiderDTO.getRodKey()) {
				Long rodKey = reconsiderDTO.getRodKey();
				isReconsideration = true;
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				//version of Rod
				reimbursement = getReimbursementObjectByKey(rodKey);
				DocAcknowledgement latestDocAcknowledgementKeyByROD = getLatestDocAcknowledgementKeyByROD(reimbursement.getKey(), reimbursement.getDocAcknowLedgement().getKey());
				
				
				// reimbursementKey = previousLatestROD.getKey();
				ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//				ZonalMedicalReviewMapper.getAllMapValues();
				PreauthDTO reimbursementDTO = mapper
						.getReimbursementDTO(reimbursement);
				reimbursementDTO.setIsPostHospitalization(false);
				
			    reimbursementDTO.setNewIntimationDTO(rodDTO.getClaimDTO().getNewIntimationDto());
			    reimbursementDTO.setClaimDTO(rodDTO.getClaimDTO());
			    PolicyDto policyDto = new PolicyMapper().getPolicyDto(reimbursement.getClaim()
						.getIntimation().getPolicy());
			    reimbursementDTO.setPolicyDto(policyDto);
				
				setReimbursmentTOPreauthDTO(mapper, reimbursement.getClaim(), reimbursement,
						reimbursementDTO, true, SHAConstants.CREATE_ROD);
				reimbursementDTO.setIsPreviousROD(true);
				
				docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
				if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
					docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
				
				if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
						&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
						|| !(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
								&& reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId().getKey()))) {
					
					populatePaymentDetails(reimbursement, rodDTO);
				}

				populateDocumentDetails(rodDTO , docAck);
				
				rodDTO.setPreauthDTO(reimbursementDTO);
				
				
				/*docAck.setReconsideredDate((new Timestamp(System
						.currentTimeMillis())));*/
				
				rodDTO.getDocumentDetails().setRodNumber(reimbursement.getRodNumber());
				//docAck.setRodKey(rodKey);
				//reimbKey = rodKey; 
				
				 rodDTO.setIsRodVersion(true);
				 
				 List<ReimbursementCalCulationDetails> reimbursementCalDetails = getReimbursementCalDetails(rodKey);
				 rodDTO.setExistingReimbursementCalDtsList(reimbursementCalDetails);
				 reimbursement = saveNewVersionROD(rodDTO, docAck1, userNameForDB1, rodKeyFromPayload, claimObj);
				 reimbursement.setReconsiderationRequest("Y");
				 /*for bancs*/
					if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
						
						reimbursement.setCatastrophicLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
					
					}
					
					if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
						reimbursement.setNatureOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss().getId());
						
					}
					
					if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
							
						reimbursement.setCauseOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss().getId());
						
					}
					//IMSSUPPOR-33874 - Reconsider rod cancelled and reprocessed due to this original rod ack wrongly updated with reconsider rod.
					if(rodDTO.getDocumentDetails().getDocAcknowledgementKey() != null && rodDTO.getDocumentDetails().getDocAcknowledgementKey().equals(docAck.getKey())){
						docAck.setRodKey(reimbursement.getKey());
					}
				 entityManager.merge(docAck);
				 entityManager.flush();
				 
				 if(latestDocAcknowledgementKeyByROD != null){
					 Reimbursement existingReimbursement = getReimbursementObjectByKey(rodKey);
					 existingReimbursement.setDocAcknowLedgement(latestDocAcknowledgementKeyByROD);
					 
					 if(docAck.getDocumentReceivedFromId() != null 
								&& docAck.getDocumentReceivedFromId().getKey() != null	
								&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docAck.getDocumentReceivedFromId().getKey())
								&& rodDTO.getDocumentDetails().getPatientStatus() != null) {
								
								reimbursement.setPatientStatus(new MastersValue());
								reimbursement.getPatientStatus().setKey(rodDTO.getDocumentDetails().getPatientStatus().getId());
							
								if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))) {
									if(rodDTO.getDocumentDetails().getDeathDate() != null) {
										reimbursement.setDateOfDeath(rodDTO.getDocumentDetails().getDeathDate());
										if(rodDTO.getDocumentDetails().getReasonForDeath() != null)	
											reimbursement.setDeathReason(rodDTO.getDocumentDetails().getReasonForDeath());
									}								
								}
							}
					//added for new product076 Comment for new change flow in host cash cr
						if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
								.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
								|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
									rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
							existingReimbursement.setProdBenefitAmount(Double.valueOf(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
							if(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
									rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
								DiagnosisHospitalDetails diagnosisId = new DiagnosisHospitalDetails();
								diagnosisId.setDiagnosisHospitalKey(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
								existingReimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
							}
						}
					 entityManager.merge(existingReimbursement);
					 entityManager.flush();
				 }
				 
				 
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */
	
			} 
			
			else if (null != rodQueryDetailsDTOObj
					&& null != rodQueryDetailsDTOObj.getReimbursementKey()) {
				
				Long rodKey = rodQueryDetailsDTOObj.getReimbursementKey();
				
				isQueryReplyReceived = true;
				docAck = getDocAcknowledgementBasedOnKey(rodQueryDetailsDTOObj.getAcknowledgementKey());
				ackNumber = docAck.getAcknowledgeNumber();
				reimbursement = getReimbursementObjectByKey(rodKey);
				if(reimbursement != null){
					reimbursement.setDocAcknowLedgement(docAck);
				}
				
				if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
						&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
						|| !(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
								&& reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId().getKey()))) {

					populatePaymentDetails(reimbursement, rodDTO);
				}

				if(docAck.getDocumentReceivedFromId() != null 
						&& docAck.getDocumentReceivedFromId().getKey() != null	
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docAck.getDocumentReceivedFromId().getKey())
						&& rodDTO.getDocumentDetails().getPatientStatus() != null) {
						
						reimbursement.setPatientStatus(new MastersValue());
						reimbursement.getPatientStatus().setKey(rodDTO.getDocumentDetails().getPatientStatus().getId());
					
						if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))) {
							if(rodDTO.getDocumentDetails().getDeathDate() != null) {
								reimbursement.setDateOfDeath(rodDTO.getDocumentDetails().getDeathDate());
								if(rodDTO.getDocumentDetails().getReasonForDeath() != null)	
									reimbursement.setDeathReason(rodDTO.getDocumentDetails().getReasonForDeath());
							}
							
						}
				}
				//added for new product076
				if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
						.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
					reimbursement.setProdBenefitAmount(Double.valueOf(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
					if(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
					DiagnosisHospitalDetails diagnosisId = new DiagnosisHospitalDetails();
					diagnosisId.setDiagnosisHospitalKey(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
					reimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
					}
				}
				
				Status status = new Status();
				status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
		
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
				
				reimbursement.setStatus(status);
				reimbursement.setStage(stage);
				
				/*for bancs*/
				if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
					
					reimbursement.setCatastrophicLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
				
				}
				
				if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
					reimbursement.setNatureOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss().getId());
					
				}
				
				if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
						
					reimbursement.setCauseOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss().getId());
					
				}
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				
				
				//previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */
	
			}
			/**
			 * Below block added for cancel rod scenario.
			 * */
	//		else if(null != rodKeyFromPayload)
	//		{
	//			reimbursement = getReimbursementObjectByKey(rodKeyFromPayload);
	//			docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
	//			populatePaymentDetails(reimbursement, rodDTO);
	//			populateDocumentDetails(rodDTO , docAck);
	//			//docAck.setRodKey(rodKey);
	//			
	//			Status status = new Status();
	//			status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
	//	
	//			Stage stage = new Stage();
	//			stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
	//			
	//			reimbursement.setStage(stage);
	//			reimbursement.setStatus(status);
	//			
	//			entityManager.merge(reimbursement);
	//			entityManager.flush();
	//
	//		}
			
			else if (rodDTO.getClaimDTO().getClaimType() != null
					&& rodDTO.getClaimDTO().getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				String[] split = rodDTO.getDocumentDetails().getRodNumber()
						.split("/");
				String string = split[split.length - 1];
				rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
				rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
				if (SHAUtils.getIntegerFromString(string) == 1) {
					
					reimbursement = getCopyFromPreauth(rodDTO, createRODMapper, docAck,
							userNameForDB1, rodKeyFromPayload, claimObj);
				}
				else{
	//				reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
	//				savePayementDetails(reimbursement, rodDTO.getDocumentDetails());
	//				reimbursement.setActiveStatus(1l);
	//				reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
	//						.getKey()));
	//				reimbursement.setRodNumber(rodDTO.getDocumentDetails()
	//						.getRodNumber());
	//				reimbursement.setDocAcknowLedgement(docAck);
					rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
					rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
	
					if (rodDTO.getPreauthDTO().getIsPreviousROD()) {
						ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//						ZonalMedicalReviewMapper.getAllMapValues();
						DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
						rodDTO.getPreauthDTO().setDateOfAdmission(
								docsDTO.getDateOfAdmission());
						
						rodDTO.getPreauthDTO()
								.getPreauthDataExtractionDetails()
								.setRoomCategory(
										rodDTO.getClaimDTO().getNewIntimationDto()
												.getRoomCategory());
						rodDTO.getPreauthDTO().setStatusValue(
								rodDTO.getStatusValue());
						rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
						rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
						rodDTO.getPreauthDTO().setRodNumber(
								rodDTO.getDocumentDetails().getRodNumber());
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
								.setDocAckknowledgement(docAck);
						reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().setVentilatorSupport(null);
						if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
								&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
								|| !(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
										&& rodDTO.getPreauthDTO().getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
										&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getPreauthDTO().getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
						
							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
								.setReasonForChange(docsDTO.getReasonForChange());
							rodDTO.getPreauthDTO().setPaymentModeId(
									docsDTO.getPaymentModeFlag());
							if (null != docsDTO.getPayeeName()) {
								rodDTO.getPreauthDTO().setPayeeName(
										docsDTO.getPayeeName().getValue());
							}
							rodDTO.getPreauthDTO()
									.setPayeeEmailId(docsDTO.getEmailId());
							rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());
							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
									.setLegalFirstName(docsDTO.getLegalFirstName());
							rodDTO.getPreauthDTO().setAccountNumber(
									docsDTO.getAccountNo());
							
							if(docsDTO.getBankId() != null){
								rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
							}
						}
						/*for bancs*/
						if(rodDTO.getPreauthDTO().getCatastrophicLoss() != null) {
							
							reimbursement.setCatastrophicLoss(rodDTO.getPreauthDTO().getCatastrophicLoss().getId());
						
						}
						
						if(rodDTO.getPreauthDTO().getNatureOfLoss() != null) {
							reimbursement.setNatureOfLoss(rodDTO.getPreauthDTO().getNatureOfLoss().getId());
							
						}
						
						if(rodDTO.getPreauthDTO().getCauseOfLoss() != null) {
								
							reimbursement.setCauseOfLoss(rodDTO.getPreauthDTO().getCauseOfLoss().getId());
							
						}
						

						if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
								&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
								|| !(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
										&& rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
										&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
							savePayementDetails(reimbursement, docsDTO, claimObj);
						}	
					//	reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
						
						if(null != rodKeyFromPayload)
						{
							reimbursement.setKey(rodKeyFromPayload);
						}
						
						if (reimbursement.getKey() != null) {
						reimbursement.setDocAcknowLedgement(docAck);
	//					reimbursement.setBillingApprovedAmount(0d);
	//					reimbursement.setFinancialApprovedAmount(0d);
						reimbursement.setSkipZmrFlag("N");
						reimbursement.setModifiedBy(userNameForDB1);
						reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						reimbursement.setApprovedAmount(0d);
						reimbursement.setBillingApprovedAmount(null);
						reimbursement.setFinancialApprovedAmount(null);
						reimbursement.setZonalDate(null);
						reimbursement.setAddOnCoversApprovedAmount(0d);
						reimbursement.setOptionalApprovedAmount(0d);
						reimbursement.setVersion(1l);
						reimbursement.setReconsiderationRequest("N");
//						entityManager.merge(reimbursement);
							
						} else {
	//						reimbursement.setBillingApprovedAmount(0d);
	//						reimbursement.setFinancialApprovedAmount(0d);
							reimbursement.setSkipZmrFlag("N");
							reimbursement.setApprovedAmount(0d);
							reimbursement.setCreatedBy(userNameForDB1);
							reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							reimbursement.setBillingApprovedAmount(null);
							reimbursement.setFinancialApprovedAmount(null);
							reimbursement.setZonalDate(null);
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursement.setVersion(1l);
							reimbursement.setReconsiderationRequest("N");
//							entityManager.persist(reimbursement);
						}
//						entityManager.flush();
						reimbursement.setOtherInsurerApplicableFlag(null);
						
						reimbursement = savePreauthValues(rodDTO.getPreauthDTO(),reimbursement,reimbursementMapper,
								true, false,rodDTO.getStrUserName(), rodKeyFromPayload, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
						
						Product product = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
						if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
							if(rodDTO.getPreauthDTO() != null){
							List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO();
							if(!updateOtherClaimDetailDTO.isEmpty()){
								PreMedicalMapper premedicalMapper = new PreMedicalMapper();
								List<UpdateOtherClaimDetails> updateOtherClaimDetails = premedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
								for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
									updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
									updateOtherClaimDetails2.setClaimKey(claimObj.getKey());
									updateOtherClaimDetails2.setStage(reimbursement.getStage());
//									updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
									updateOtherClaimDetails2.setIntimationKey(claimObj.getIntimation().getKey());
									updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
									updateOtherClaimDetails2.setClaimType(claimObj.getClaimType().getValue());
									updateOtherClaimDetails2.setKey(null);
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
						}
						
						log.info("------Reimbursement------>"+reimbursement+"<------------");
					} else {
						reimbursement = getCopyFromPreauth(rodDTO, createRODMapper, docAck,
								userNameForDB1, rodKeyFromPayload, claimObj);
					}
				}
	
			} else {
				
				rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
				rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
	
				if (rodDTO.getPreauthDTO().getIsPreviousROD()) {
					ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
					DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
					rodDTO.getPreauthDTO().setDateOfAdmission(
							docsDTO.getDateOfAdmission());

					if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
							&& !((ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
									&& rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {						
					
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
								.setReasonForChange(docsDTO.getReasonForChange());
						rodDTO.getPreauthDTO().setPaymentModeId(
								docsDTO.getPaymentModeFlag());
						if (null != docsDTO.getPayeeName()) {
							rodDTO.getPreauthDTO().setPayeeName(
									docsDTO.getPayeeName().getValue());
						}
						rodDTO.getPreauthDTO().setPayeeEmailId(docsDTO.getEmailId());
						rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
								.setLegalFirstName(docsDTO.getLegalFirstName());
						rodDTO.getPreauthDTO().setAccountNumber(docsDTO.getAccountNo());
						rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
					}	
						
					rodDTO.getPreauthDTO()
							.getPreauthDataExtractionDetails()
							.setRoomCategory(
									rodDTO.getClaimDTO().getNewIntimationDto()
											.getRoomCategory());
					rodDTO.getPreauthDTO().setStatusValue(rodDTO.getStatusValue());
					rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
					rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
					rodDTO.getPreauthDTO().setRodNumber(
							rodDTO.getDocumentDetails().getRodNumber());
					rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
							.setDocAckknowledgement(docAck);
					reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());

					if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
							&& !((ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
									&& rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
						savePayementDetails(reimbursement, docsDTO, claimObj);
					}	
					
					reimbursement.setDocAcknowLedgement(docAck);
					reimbursement.setVentilatorSupport(null);
					Hospitals hospital = getHospitalByName(rodDTO.getDocumentDetails().getHospitalName());
					if(null != hospital)
						reimbursement.setHospitalId(hospital.getKey());
					
					if(null != rodKeyFromPayload)
					{
						reimbursement.setKey(rodKeyFromPayload);
					}
					if(reimbursement.getKey() != null) {
						reimbursement.setSkipZmrFlag("N");
						reimbursement.setBillingApprovedAmount(0d);
						reimbursement.setApprovedAmount(0d);
						reimbursement.setFinancialApprovedAmount(0d);
						reimbursement.setBillingCompletedDate(null);
						reimbursement.setFinancialCompletedDate(null);
						reimbursement.setMedicalCompletedDate(null);
						reimbursement.setZonalDate(null);
						reimbursement.setModifiedBy(userNameForDB1);
						reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						reimbursement.setAddOnCoversApprovedAmount(0d);
						reimbursement.setOptionalApprovedAmount(0d);
						reimbursement.setVersion(1l);
						reimbursement.setReconsiderationRequest("N");
//						entityManager.merge(reimbursement);
						
					} else {
						String strUserName = rodDTO.getStrUserName();
						String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
						reimbursement.setSkipZmrFlag("N");
						reimbursement.setCreatedBy(userNameForDB);
						reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						reimbursement.setBillingApprovedAmount(0d);
						reimbursement.setBillingCompletedDate(null);
						reimbursement.setFinancialApprovedAmount(0d);
						reimbursement.setFinancialCompletedDate(null);
						reimbursement.setMedicalCompletedDate(null);
						reimbursement.setZonalDate(null);
						reimbursement.setAddOnCoversApprovedAmount(0d);
						reimbursement.setOptionalApprovedAmount(0d);
						reimbursement.setReconsiderationRequest("N");
						reimbursement.setVersion(1l);
//						entityManager.persist(reimbursement);
					}
//					entityManager.flush();
					//added for new product076
					if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
							.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
							|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
						reimbursement.setProdBenefitAmount(Double.valueOf(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
						if(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
						DiagnosisHospitalDetails diagnosisId = new DiagnosisHospitalDetails();
						diagnosisId.setDiagnosisHospitalKey(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
						reimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
						}
					}
					reimbursement.setOtherInsurerApplicableFlag(null);
					reimbursement = savePreauthValues(rodDTO.getPreauthDTO(), reimbursement, reimbursementMapper, true,
							false,rodDTO.getStrUserName(), rodKeyFromPayload, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
					
					log.info("------Reimbursement------>"+reimbursement+"<------------");
				} else {
					
					if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
							&& !((ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
									&& rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
						if(null != rodDTO.getDocumentDetails().getPaymentMode() && rodDTO.getDocumentDetails().getPaymentMode())
						{
							rodDTO.getDocumentDetails().setAccountNo(null);
							rodDTO.getDocumentDetails().setBankId(null);
							rodDTO.getDocumentDetails().setIfscCode(null);
							rodDTO.getDocumentDetails().setCity(null);
							rodDTO.getDocumentDetails().setBankName(null);
							rodDTO.getDocumentDetails().setBranch(null);
						}
						else
						{
							rodDTO.getDocumentDetails().setPayableAt(null);
						}
					}
					
					reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
					if(null != rodDTO.getDocumentDetails().getInsuredPatientName())
					{
						if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
							
							if(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient() != null && 
									rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() != null){
								reimbursement.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient());
							}
							
						}else{
							Insured insured = null;
							/*Below Code for IMSSUPPOR-28573*/
							if(rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId() != null && rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId().equals(ReferenceTable.PACKAGE_MASTER_VALUE)) {
								insured = getInsuredByPolicyAndInsuredNameLob(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
							} else {
								//changed the code due to issue faced for comprehensive product we are getting two records with same name by noufel 15-10-2020
								insured = getInsuredByPolicyAndInsuredNameLob(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
							}

							reimbursement.setInsuredKey(insured);
							
								if(insured != null){
									Intimation intimation = getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
									if(intimation != null){
										intimation.setInsured(insured);
										if(null != rodDTO)
										{
											intimation.setModifiedBy(rodDTO.getStrUserName());
											intimation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
										}
										entityManager.merge(intimation);
										entityManager.flush();
										entityManager.clear();
										log.info("------Intimation------>"+intimation+"<------------");
									}
									
									Claim claim = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
									if(claim != null){
										claim.setInsuredKey(insured);
										if(null != rodDTO)
										{
											claim.setModifiedBy(rodDTO.getStrUserName());
											claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
										}
										entityManager.merge(claim);
										entityManager.flush();
										entityManager.clear();
										log.info("------Intimation------>"+claim+"<------------");
									}
								}
						}
						
						
					}
					Hospitals hospital = getHospitalByName(rodDTO.getDocumentDetails().getHospitalName());
					if(null != hospital)
						reimbursement.setHospitalId(hospital.getKey());
					
					if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
							&& !((ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
									&& rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
						savePayementDetails(reimbursement, rodDTO.getDocumentDetails(), claimObj);
					}	
					
					reimbursement.setActiveStatus(1l);
					reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
							.getKey()));
					reimbursement.setRodNumber(rodDTO.getDocumentDetails()
							.getRodNumber());
					reimbursement.setDocAcknowLedgement(docAck);
					reimbursement.setCreatedBy(userNameForDB1);
					reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					reimbursement.setOtherInsurerApplicableFlag(null);					
					
					if(docAck.getDocumentReceivedFromId() != null 
							&& docAck.getDocumentReceivedFromId().getKey() != null	
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docAck.getDocumentReceivedFromId().getKey())
							&& rodDTO.getDocumentDetails().getPatientStatus() != null) {
							
							reimbursement.setPatientStatus(new MastersValue());
							reimbursement.getPatientStatus().setKey(rodDTO.getDocumentDetails().getPatientStatus().getId());
						
							if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))) {
								if(rodDTO.getDocumentDetails().getDeathDate() != null) {
									reimbursement.setDateOfDeath(rodDTO.getDocumentDetails().getDeathDate());
									if(rodDTO.getDocumentDetails().getReasonForDeath() != null)	
										reimbursement.setDeathReason(rodDTO.getDocumentDetails().getReasonForDeath());
								}
							
							}
					}
					//added for new product076
					if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
							.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
							|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
						reimbursement.setProdBenefitAmount(Double.valueOf(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
						if(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
						DiagnosisHospitalDetails diagnosisId = new DiagnosisHospitalDetails();
						diagnosisId.setDiagnosisHospitalKey(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
						reimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
						}
					}
					
					if(rodDTO.getDocumentDetails() !=null && rodDTO.getDocumentDetails().isGrievanceRepresentation() !=null){
						String grievanceFlag = rodDTO.getDocumentDetails().isGrievanceRepresentation() ? "Y" : "N";
						reimbursement.setGrievanceRepresentation(grievanceFlag);
					}
					
					if(null != rodKeyFromPayload)
					{
						reimbursement.setKey(rodKeyFromPayload);
						if(null != rodDTO)
						{
							reimbursement.setModifiedBy(userNameForDB1);
							reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						}
						entityManager.merge(reimbursement);
					}
					else
					{
						entityManager.persist(reimbursement);
					}
					entityManager.flush();
					entityManager.clear();
					log.info("------Reimbursement------>"+reimbursement+"<------------");
				}
				
				Product product = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
				if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
					if(rodDTO.getPreauthDTO() != null){
					List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO();
					if(!updateOtherClaimDetailDTO.isEmpty()){
						PreMedicalMapper premedicalMapper = new PreMedicalMapper();
						List<UpdateOtherClaimDetails> updateOtherClaimDetails = premedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
						for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
							updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
							updateOtherClaimDetails2.setClaimKey(claimObj.getKey());
							updateOtherClaimDetails2.setStage(reimbursement.getStage());
//							updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
							updateOtherClaimDetails2.setIntimationKey(claimObj.getIntimation().getKey());
							updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
							updateOtherClaimDetails2.setClaimType(claimObj.getClaimType().getValue());
							updateOtherClaimDetails2.setKey(null);
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
				}
	
				// entityManager.refresh(reimbursement);
			}
			
			if(null != rodDTO.getRodQueryDetailsList() && ! rodDTO.getRodQueryDetailsList().isEmpty()){
				
				List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO.getRodQueryDetailsList();
				Status status = null;
				ReimbursementQuery reimbQuery  = null;
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
					reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2.getReimbursementQueryKey());
					if(("No").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
					{
						reimbQuery.setQueryReply("N");
						reimbQuery.setDocAcknowledgement(null);
						reimbQuery.setQueryReplyDate(null);
						status = new Status();
						if(reimbQuery.getStage() != null && reimbQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
							status.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
							reimbQuery.setStatus(status);
						}else{
							status.setKey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
							reimbQuery.setStatus(status);
						}
					}
					else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
					{
						reimbQuery.setQueryReply("Y");
						isQueryReplyYes = true;
						reimbQuery.setDocAcknowledgement(docAck);
						
						if(reimbQuery.getStage() != null){
							rodDTO.setQueryStage(reimbQuery.getStage().getKey());
						}
						
						if(reimbQuery.getQueryReplyDate() == null){
							reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
						}
						status = new Status();
						if(reimbQuery.getStage() != null && reimbQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
							status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
							reimbQuery.setStatus(status);
							reimbursement.setStatus(status);
						}else{
							status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
							reimbQuery.setStatus(status);
							reimbursement.setStatus(status);
						}
					}
					
					if(null != reimbQuery.getKey())
					{
						if(null != rodDTO)
						{
							reimbQuery.setModifiedBy(userNameForDB1);
							reimbQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						}
						entityManager.merge(reimbQuery);
						entityManager.flush();
						entityManager.clear();
						log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
					}
				}
			}

			//if(!isQueryReplyReceived){
			//{
				List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
				CreateRODMapper rodMapper = CreateRODMapper.getInstance();
		
				if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
						if (null != uploadDocumentDTO.getFileType()
								&& !("").equalsIgnoreCase(uploadDocumentDTO
										.getFileType().getValue())) {
							
							if(rodDTO.getIsRodVersion()){
								List<BillEntryDetailsDTO> dtoList = getBillEntryDetailsList(uploadDocumentDTO);
								for (BillEntryDetailsDTO billEntryDetailsDTO : dtoList) {
									billEntryDetailsDTO.setKey(null);
								}
								uploadDocumentDTO.setBillEntryDetailList(dtoList);
								uploadDocumentDTO.setDocSummaryKey(null);
								if(rodDTO.getPreviousRodForReconsider() != null){
									deletedPreviousDocument(rodDTO.getPreviousRodForReconsider());
								}
							}
							RODDocumentSummary rodSummary = createRODMapper
									.getDocumentSummary(uploadDocumentDTO);
							rodSummary.setReimbursement(reimbursement);
							rodSummary.setRodVersion(reimbursement.getVersion());
							rodSummary.setDocumentToken(uploadDocumentDTO
									.getDmsDocToken());
							rodSummary.setDeletedFlag("N");
							
							if(null == rodSummary.getKey())
							{
								if(null != rodSummary)
								{
									rodSummary.setCreatedBy(userNameForDB1);
									/**
									 * As per Satish sir instruction, the below code was commented
									 */
									//rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								}
								if(rodSummary.getCreatedDate() == null){
									rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								}
								entityManager.persist(rodSummary);
								entityManager.flush();
								entityManager.clear();
								log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
								if(rodDTO.getIsRodVersion()){
									List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
									for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
										billEntryDetailsDTO.setDocumentSummaryKey(rodSummary.getKey());
										 RODBillDetails rodBillDetails = rodMapper
													.getRODBillDetails(billEntryDetailsDTO);
										 rodBillDetails.setRodDocumentSummaryKey(rodSummary);
										 rodBillDetails.setReimbursementKey(reimbursement.getKey());
										 rodBillDetails.setDeletedFlag("N");
										 entityManager.persist(rodBillDetails);
										 entityManager.flush();
									}
								}
								
							}
							else
							{
								if(null != rodDTO)
								{
									rodSummary.setModifiedBy(userNameForDB1);
									rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								}
								
								entityManager.merge(rodSummary);
								entityManager.flush();
								entityManager.clear();
								log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
							//	entityManager.refresh(rodSummary);
							}
							
							if(null != uploadDocumentDTO.getAckDocKey())
							{
								String userNameForDB = SHAUtils.getUserNameForDB(rodDTO.getStrUserName());
								AcknowledgeDocument ackDocument = getAcknowledgeDocObjByKey(uploadDocumentDTO.getAckDocKey());
								if(null != ackDocument)
								{
									log.info("------Ack document summary key------>"+ackDocument.getKey()+"<------------");

									ackDocument.setDeleteFlag(SHAConstants.YES_FLAG);
									if(null != ackDocument.getKey())
									{
										ackDocument.setModifiedBy(userNameForDB);
										ackDocument.setModifiedDate(new Timestamp(System.currentTimeMillis()));
										entityManager.merge(ackDocument);
										entityManager.flush();
										entityManager.clear();
									}
									
									log.info("------Ack document summary  key successfully updated------>"+ackDocument.getKey()+"<------------");
								}
							}
							// entityManager.refresh(rodSummary);
						}
					}
					}
				
				/**
				 * In case of cancel rod, those documents which are uploaded before
				 * cancelling will be available in uploaded documents table.
				 * This document can be deleted when user is trying to recreate
				 * the cancelled the rod. That time deleted record needs 
				 * to be updated in table. Below code was added for the same.
				 * 
				 * */
				List<UploadDocumentDTO> deletedDocsList = rodDTO
						.getUploadDocumentsDTO().getDeletedDocumentList();
				String userNameForDBObj = SHAUtils.getUserNameForDB(rodDTO.getStrUserName());
				if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

						RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
								.getDocumentSummary(uploadDocumentDTO2);
						rodSummary.setReimbursement(reimbursement);
						rodSummary.setDeletedFlag("Y");

						if (null != uploadDocumentDTO2.getDocSummaryKey()) {
							rodSummary.setModifiedBy(userNameForDBObj);
							rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							entityManager.merge(rodSummary);
							entityManager.flush();
							entityManager.clear();
							log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
						} /*
						 * else { entityManager.persist(rodSummary);
						 * entityManager.flush(); entityManager.refresh(rodSummary);
						 * }
						 */
						
						DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
						if(null != details)
						{
							details.setDeletedFlag("Y");
							entityManager.merge(details);
							entityManager.flush();
							entityManager.clear();
							log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
						}

					}
				}
				
			//}/*else{
				
			//}
	
			// Update the ROD KEY in DocAcknowledgement Table.
				
	
					if (null != docAck) {
						DocAcknowledgement docAcknowledgement = createRODMapper
								.getAcknowledgeDocumentList(rodDTO);
						if ((null != rodDTO.getDocumentDetails().getHospitalizationClaimedAmount()) && !("").equals(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())
								&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())))
						{
						totalClaimedAmt += (Double
								.parseDouble(rodDTO.getDocumentDetails()
										.getHospitalizationClaimedAmount()));
						
						docAcknowledgement.setHospitalizationClaimedAmount(Double
								.parseDouble(rodDTO.getDocumentDetails()
										.getHospitalizationClaimedAmount()));
							
							/*docAcknowledgement.setHospClaimedAmountDocRec(Double.parseDouble(rodDTO
									.getDocumentDetails().getHospitalizationClaimedAmount()));*/
														
						}
						if ((null != rodDTO.getDocumentDetails()
								.getPreHospitalizationClaimedAmount())
								&& !("").equals(rodDTO.getDocumentDetails()
										.getPreHospitalizationClaimedAmount())
								&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
										.getPreHospitalizationClaimedAmount())))
						{
								totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()));
								docAcknowledgement.setPreHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()));
							
							/*docAcknowledgement.setPreHospClaimedAmountDocRec(Double.parseDouble(rodDTO
									.getDocumentDetails().getPreHospitalizationClaimedAmount()));*/
						
							
						}
						if ((null != rodDTO.getDocumentDetails()
								.getPostHospitalizationClaimedAmount())
								&& !("").equals(rodDTO.getDocumentDetails()
										.getPostHospitalizationClaimedAmount())
								&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
										.getPostHospitalizationClaimedAmount())))
						{
							
							totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount()));
							docAcknowledgement.setPostHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO.getDocumentDetails()
											.getPostHospitalizationClaimedAmount()));
							
						/*	docAcknowledgement.setPostHospClaimedAmountDocRec(Double
									.parseDouble(rodDTO.getDocumentDetails()
											.getPostHospitalizationClaimedAmount()));*/
						}
						
						if ((null != rodDTO.getDocumentDetails()
								.getOtherBenefitclaimedAmount())
								&& !("").equals(rodDTO.getDocumentDetails()
										.getOtherBenefitclaimedAmount())
								&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
										.getOtherBenefitclaimedAmount())))
						{
							
							totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()));
							docAcknowledgement.setOtherBenefitsClaimedAmount(Double
									.parseDouble(rodDTO.getDocumentDetails()
											.getOtherBenefitclaimedAmount()));
							
							/*docAcknowledgement.setOtherBenefitsAmountDocRec(Double
									.parseDouble(rodDTO.getDocumentDetails()
											.getPostHospitalizationClaimedAmount()));*/
							}
						
						//added for new product 076
						
						if ((null != rodDTO.getDocumentDetails()
								.getHospitalCashClaimedAmnt())
								&& !("").equals(rodDTO.getDocumentDetails()
										.getHospitalCashClaimedAmnt())
								&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
										.getHospitalCashClaimedAmnt())))
						{
							
							totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
							docAcknowledgement.setProdHospBenefitClaimedAmount(Double
									.parseDouble(rodDTO.getDocumentDetails()
											.getHospitalCashClaimedAmnt()));
							}
						
						if(null != rodDTO.getDocumentDetails().getDocumentVerificationFlag()){
							
							docAcknowledgement.setRodDocumentVerifiedFlag(rodDTO.getDocumentDetails().getDocumentVerificationFlag());
						}
						
						if(null != docAck.getDocumentVerifiedFlag()){
							
							docAcknowledgement.setDocumentVerifiedFlag(docAck.getDocumentVerifiedFlag());
						}
						
						docAcknowledgement.setClaim(docAck.getClaim());
						docAcknowledgement.setStage(docAck.getStage());
						docAcknowledgement.setStatus(docAck.getStatus());
						docAcknowledgement.setInsuredContactNumber(docAck
								.getInsuredContactNumber());
						docAcknowledgement.setInsuredEmailId(docAck.getInsuredEmailId());
						/**
						 * In case of reconsideration, in rod level if user changes the reconsideration
						 * preferencen, then for the ack reconsidered, rod number alone needs to be
						 * changed. hence the below condition.
						 * */
						if(null != reconsiderDTO)
						{
							docAcknowledgement.setAcknowledgeNumber(ackNumber);
							if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
							{
								MastersValue masValue = new MastersValue();
								masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
								masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
								docAcknowledgement.setReconsiderationReasonId(masValue);
							}
							
							if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
								docAcknowledgement.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
							
							docAcknowledgement.setReconsideredDate(new Timestamp(System.currentTimeMillis()));
							/*Map reconsiderationMap = rodDTO.getReconsiderationMap();
							Long ackKey = (Long) reconsiderationMap.get("ackKey");
							Long rodKey = (Long) reconsiderationMap.get("rodKey");
							if(!(rodKey.equals(reconsiderDTO.getRodKey())))
							//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
							{
								revertRODData(rodKey);
							}*/
						}
						/**
						 * The below condition was added , since unique constraint violation 
						 * issue was faced. Removing the else condition, might have an reverse 
						 * impact. Since only for query this prob has occured, we're adding
						 * below else if condition.
						 * */
						else if(null != isQueryReplyReceived && isQueryReplyReceived)
						{
							docAcknowledgement.setAcknowledgeNumber(globalAck);
						}
						else
						{
							docAcknowledgement.setAcknowledgeNumber(docAck
									.getAcknowledgeNumber());
						}
						docAcknowledgement.setCreatedDate(docAck.getCreatedDate());
						docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						
						docAcknowledgement.setRodKey(reimbursement.getKey());
						
						docAcknowledgement.setKey(docAcknowledgement.getKey());
						docAcknowledgement.setModifiedBy(userNameForDB1);
						if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
								|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
									rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
							docAcknowledgement.setProdHospBenefitFlag(rodDTO.getDocumentDetails().getHospitalCashFlag());
						}
						
						if((rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
								|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
								|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))*/
									(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
										&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
												|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode())
												|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
										&& ("G").equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getPolicyPlan()))
								|| (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy() != null 
										&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))))
										
						{
						docAcknowledgement.setOtherBenefitsFlag(rodDTO.getDocumentDetails().getOtherBenefitsFlag());
						if(null != rodDTO.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getOtherBenefitsFlag()) )
						{
							docAcknowledgement.setEmergencyMedicalEvaluation(rodDTO.getDocumentDetails().getEmergencyMedicalEvaluationFlag());
							docAcknowledgement.setCompassionateTravel(rodDTO.getDocumentDetails().getCompassionateTravelFlag());
							docAcknowledgement.setRepatriationOfMortalRemain(rodDTO.getDocumentDetails().getRepatriationOfMortalRemainsFlag());
							docAcknowledgement.setPreferredNetworkHospita(rodDTO.getDocumentDetails().getPreferredNetworkHospitalFlag());
							docAcknowledgement.setSharedAccomodation(rodDTO.getDocumentDetails().getSharedAccomodationFlag());
						}
						else
						{
							docAcknowledgement.setEmergencyMedicalEvaluation(SHAConstants.N_FLAG);
							docAcknowledgement.setCompassionateTravel(SHAConstants.N_FLAG);
							docAcknowledgement.setRepatriationOfMortalRemain(SHAConstants.N_FLAG);
							docAcknowledgement.setPreferredNetworkHospita(SHAConstants.N_FLAG);
							docAcknowledgement.setSharedAccomodation(SHAConstants.N_FLAG);
							
						}
						
						 List<OtherBenefitsTableDto> otherBenefitsList = rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getOtherBenefitsList();
						  
						    if(otherBenefitsList != null && ! otherBenefitsList.isEmpty()){
						    	ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
						    	List<BenefitAmountDetails> otherBenefitsAmountDetails = reimbursementMapper.getOtherBenefitsAmountDetails(otherBenefitsList);
						    	for (BenefitAmountDetails benefitAmountDetails : otherBenefitsAmountDetails) {
									benefitAmountDetails.setClaim(reimbursement.getClaim());
									benefitAmountDetails.setInsured(reimbursement.getClaim().getIntimation().getInsured());
									benefitAmountDetails.setStage(reimbursement.getStage());
									benefitAmountDetails.setStatus(reimbursement.getStatus());
									benefitAmountDetails.setTransactionKey(reimbursement.getKey());
									if(benefitAmountDetails.getKey() != null){
										entityManager.merge(benefitAmountDetails);
										entityManager.flush();
									}else{
										entityManager.persist(benefitAmountDetails);
										entityManager.flush();
									}
								}
						    }
						
						
						}
						
						
						/*if(null != rodDTO)
							docAcknowledgement.setModifiedBy(rodDTO.getStrUserName());*/
			
						entityManager.merge(docAcknowledgement);
 						entityManager.flush();
 						entityManager.clear();
						log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
						
						Map reconsiderationMap = rodDTO.getReconsiderationMap();
						if(null != reconsiderDTO)
						{
							//Map reconsiderationMap = rodDTO.getReconsiderationMap();
							if(null != reconsiderationMap)
							{
								//Long ackKey = (Long) reconsiderationMap.get("ackKey");
								Long rodKey = (Long) reconsiderationMap.get("rodKey");
								if(null != reconsiderDTO && rodKey != null)
								{
									if(!(rodKey.equals(reconsiderDTO.getRodKey())))
									//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
									{
										revertRODData(rodKey,userNameForDB1);
									}
								}
								
							}
						}
						else if(null != reconsiderationMap)
						{
							//Long ackKey = (Long) reconsiderationMap.get("ackKey");
							Long rodKey = (Long) reconsiderationMap.get("rodKey");
							if(!(rodKey.equals(reimbursement.getKey())) && rodKey != null)
							//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
							{
								revertRODData(rodKey,userNameForDB1);
							}
						}
						// entityManager.refresh(docAcknowledgement);
					}
				
	
			List<DocumentCheckListDTO> docCheckListVal = rodDTO
					.getDocumentDetails().getDocumentCheckList();
			if (!docCheckListVal.isEmpty()) {
				for (DocumentCheckListDTO docCheckListDTO : docCheckListVal) {
					//if (null != docCheckListDTO.getRodReceivedStatusFlag()) {
					RODDocumentCheckList rodDocumentCheckList = createRODMapper
							.getRODCheckListForUpdation(docCheckListDTO);
					rodDocumentCheckList.setDocAcknowledgement(docAck);
				//	if (null != docCheckListDTO.getDocTypeId()) {
					if(null != docCheckListDTO.getParticulars().getId())
					{
						if(null != rodDocumentCheckList)
						{
							rodDocumentCheckList.setDocumentTypeId(docCheckListDTO.getParticulars().getId());
							rodDocumentCheckList.setModifiedBy(userNameForDB1);
							rodDocumentCheckList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						}
						entityManager.merge(rodDocumentCheckList);
						entityManager.flush();
						log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
						// entityManager.refresh(rodDocumentCheckList);
					} else {

						rodDocumentCheckList.setDocumentTypeId(docCheckListDTO
								.getKey());
						rodDocumentCheckList.setKey(null);
						rodDocumentCheckList.setCreatedBy(userNameForDB1);
						entityManager.persist(rodDocumentCheckList);
						entityManager.flush();
						log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
						// entityManager.refresh(rodDocumentCheckList);
					}

				//}
			}
			}
	
			List<Preauth> preAuthList = getPreauthByIntimationKey(rodDTO
					.getClaimDTO().getNewIntimationDto().getKey());
			if (null != preAuthList && !preAuthList.isEmpty()) {
				int iSize = preAuthList.size();
				Preauth preAuth = preAuthList.get(iSize - 1);
				if (null != preAuth && null != preAuth.getTreatmentType())
					rodDTO.setTreatmentType(preAuth.getTreatmentType().getValue());
			} else {
				/**
				 * If cashless is not available for this intimation, then by default
				 * we set treatment type as Medical. Any how during zonal , this
				 * will changed.
				 */
				rodDTO.setTreatmentType("Medical");
			}
			// Intimation objIntimation =
			// getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
	
			// Need to check whether the insured object
	
			/*
			 * if(null != objIntimation) { Insured insured =
			 * getCLSInsured(objIntimation.getInsured().getKey());
			 * objIntimation.setAdmissionDate
			 * (rodDTO.getDocumentDetails().getDateOfAdmission());
			 * insured.setInsuredName
			 * (rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
			 * objIntimation.setInsured(insured);
			 * 
			 * entityManager.merge(objIntimation); entityManager.flush(); }
			 */
			/***
			 * Coded added for updating the current provision amt
			 ***/
	
	//		if(reimbursement.getCurrentProvisionAmt())
			Double balanceSI = getBalanceSI(rodDTO);
			//Double currentProvisionAmt = 0d;
	//		previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
			/*if(null != previousCurrentProvAmt)
			{*/
				//currentProvisionAmt =   calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
			calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
			/*}
			else
			{
				currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
			}
			*/
			/*if(null != balanceSI && null != currentProvisionAmt)
			{
	//			reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, currentProvisionAmt));
			}*/
			
			/*if(balanceSI > currentProvisionAmt)
			{
				reimbursement.setCurrentProvisionAmt(balanceSI);
			}
			else
			{
				reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			}*/
			//Claim claimObj = reimbursement.getClaim();
			if(claimObj != null) {
				claimObj = getClaimByClaimKey(reimbursement.getClaim().getKey());
			}
			
			/*<------------ As per requirement Provision Amount changes has been made by Saravana........ ------------------------------> *//////
			if(reimbursement.getClaim().getClaimType() != null) {
				if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
					if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){ /*&&  null != ReferenceTable.getSeniorCitizenKeys() && null == ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getProduct().getKey()) ) {*/
						reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, totalClaimedAmt));
						//reimbursement.setCurrentProvisionAmt(totalClaimedAmt);
					} else {
						Preauth latestPreauthByClaim = getLatestPreauthByClaim(claimObj.getKey());
						if(latestPreauthByClaim != null) {
							Double totalApprovalAmt = latestPreauthByClaim.getTotalApprovalAmount() != null ? latestPreauthByClaim.getTotalApprovalAmount(): 0d;
							totalApprovalAmt += latestPreauthByClaim.getOtherBenefitApprovedAmt() != null ? latestPreauthByClaim.getOtherBenefitApprovedAmt(): 0d;
							reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, totalApprovalAmt));
						}
						
						// No update for Cashless with hospitalization........
					}
				}
				/**
				 * Added for star criticare and star medipremier product , where
				 * lumpsum is applicable.
				 * 
				 * */
				else if (null != docAck && ("N").equalsIgnoreCase(docAck.getHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPreHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck.getPostHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalCashFlag()) && ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
						&& ("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
				{
					DBCalculationService dbCalculationService = new DBCalculationService();
					Long productKey = 0l;
					if(null != claimObj.getIntimation().getPolicy().getProduct())
						productKey  = claimObj.getIntimation().getPolicy().getProduct().getKey();
					Double lumpSumAmt = dbCalculationService.getLumpSumAmount(productKey, docAck.getClaim().getKey(), ReferenceTable.LUMPSUM_SUB_COVER_CODE);
					
					if(null != reimbursement)
					{
						reimbursement.setCurrentProvisionAmt(lumpSumAmt);
					}
					
						//updateProvisionAndClaimStatus(docAck, claimObj,true);	
				}

			/*	else if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
=======
				/*else if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
>>>>>>> dbef35826fc77e256ad795f3291bfa8766dd6a72
						null != rodDTO.getNewIntimationDTO().getProduct() && null != rodDTO.getNewIntimationDTO().getProduct().getKey() && null != ReferenceTable.getSeniorCitizenKeys() && null != ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getProduct().getKey()))
				{
					Double claimAmt = (70 /100) *totalClaimedAmt;
					if(null != claimAmt)
					{
						reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, claimAmt));
					}
				}*/
				
				else {
					// Other than Hospitalization we have to set Total Claimed Amount as current provision amount for both the cases....
					reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, totalClaimedAmt));
					//reimbursement.setCurrentProvisionAmt(totalClaimedAmt);
				}
				
			}
			
			
			if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
					null != rodDTO.getNewIntimationDTO().getPolicy().getProduct() && null != rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() && null != ReferenceTable.getSeniorCitizenKeys() && null != ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))
			{
				Double claimAmt = (70d /100d) *totalClaimedAmt;
				if(null != claimAmt)
				{
					reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, claimAmt));
				}
			}

			/*<---------------------------- Current  Provision amount updates completed ---------------------------->*/
			
			if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
					&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
					|| !(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
							&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()))
							&& reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursement.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId().getKey()))) {

				if(null != rodDTO.getDocumentDetails().getPaymentMode() && rodDTO.getDocumentDetails().getPaymentMode())
				{
					reimbursement.setAccountNumber(null);	
					reimbursement.setBankId(null);
					reimbursement.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
	
				}
				else
				{
					reimbursement.setPaymentModeId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					reimbursement.setPayableAt(null);
				}
			}			
			
			
			if(null != reimbursement.getKey())
			{
				String strUserName = rodDTO.getStrUserName();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
				reimbursement.setModifiedBy(userNameForDB);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				if(reimbursement.getDateOfDischarge() == null ) {
					if(rodDTO.getDocumentDetails().getDateOfDischarge() != null) {
						reimbursement.setDateOfDischarge(rodDTO.getDocumentDetails().getDateOfDischarge());
					} else {
						if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getDischargeDate() != null) {
							reimbursement.setDateOfDischarge(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getDischargeDate());
						}
					}
				}
				
				rodDTO.getDocumentDetails().setRodKey(reimbursement.getKey());
				if(docAck.getDocumentReceivedFromId() != null 
						&& docAck.getDocumentReceivedFromId().getKey() != null	
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docAck.getDocumentReceivedFromId().getKey())
						&& rodDTO.getDocumentDetails().getPatientStatus() != null) {
						
						reimbursement.setPatientStatus(new MastersValue());
						reimbursement.getPatientStatus().setKey(rodDTO.getDocumentDetails().getPatientStatus().getId());
					
						if(rodDTO.getDocumentDetails().getPatientStatus().getId() != null
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId())) && rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
												&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
							if(rodDTO.getDocumentDetails().getDeathDate() != null) {
								reimbursement.setDateOfDeath(rodDTO.getDocumentDetails().getDeathDate());
								if(rodDTO.getDocumentDetails().getReasonForDeath() != null)	
									reimbursement.setDeathReason(rodDTO.getDocumentDetails().getReasonForDeath());
							}
							
							if(rodDTO.getNewIntimationDTO().getNomineeList() != null && !rodDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
								saveNomineeDetails(rodDTO.getNewIntimationDTO().getNomineeList(), reimbursement);
							}else{
								rodDTO.setKey(reimbursement.getKey());
								legalHeirAndDocumentDetails(rodDTO);
								
							}
						
						}
				}
				
				shouldSkipZMR = shouldSkipZMR(reimbursement.getClaim());
				if(null != rodDTO.getDocumentDetails().getDocumentsReceivedFrom())
				{
				if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null && rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					shouldSkipZMR = false;
				}
				}
				

				if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()
						&& ! rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount().isEmpty()){
					
					reimbursement.setOtherBenefitApprovedAmt(Double.valueOf(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()));
				}
				
				if(rodDTO.getDocumentDetails() !=null && rodDTO.getDocumentDetails().isGrievanceRepresentation() !=null){
					String grievanceFlag = rodDTO.getDocumentDetails().isGrievanceRepresentation() ? "Y" : "N";
					reimbursement.setGrievanceRepresentation(grievanceFlag);
				}
				reimbursement.setSkipZmrFlag(shouldSkipZMR ? "Y":"N");			
				
				
				reimbursement.setBillEntryRemarks(null);
				reimbursement.setOtherInsurerHospAmt(0d);
				reimbursement.setOtherInsurerPreHospAmt(0d);
				reimbursement.setOtherInsurerPostHospAmt(0d);
				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
				log.info("------Reimbursement------>"+reimbursement.getKey()+"<------------");
	
			}
			
			
		
			
			if(null != claimObj)
			{
				
				Status status1 = new Status();
				status1.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
				
				Stage stage1 = new Stage();
				stage1.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
				
				if(rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getLatestPreauthKey() != null){
					claimObj.setLatestPreauthKey(rodDTO.getClaimDTO().getLatestPreauthKey());
				}
				
	//			claimObj.setStatus(status1);
	//			claimObj.setStage(stage1);
				claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				claimObj.setModifiedBy(userNameForDB1);
				claimObj.setDataOfAdmission(reimbursement.getDateOfAdmission());
				claimObj.setDataOfDischarge(reimbursement.getDateOfDischarge());
				
				SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
				if(sectionDetailsDTO != null) {
					claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
					claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
					claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
				}
				
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
				log.info("------Claim------>"+claimObj+"<------------");
			}
			
			String sendTowhere = ReferenceTable.NORMAL;
			if(!isReconsideration && ! isQueryReplyReceived) {
				sendTowhere = loadByPass(reimbursement, claimObj, false);
			}
			if(isQueryReplyReceived) {
				sendTowhere =  loadByPass(reimbursement, claimObj, true);
			}
			/*if((claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) || (claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && !isHavingEarlierRod(claimObj.getKey()) )) {
	//			callRemainderProcess(rodDTO, reimbursement, isQueryReplyYes);
			}*/
			
			// Bypass will always go to BILLING .....
	//		if(sendTowhere.equalsIgnoreCase(ReferenceTable.DIRECT_TO_FINANCIAL)) {
	//			sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
	//		}
			
			
			/*BankMaster bankMaster = validateIFSCCode( rodDTO.getDocumentDetails().getIfscCode());
			if(null != bankMaster)
			{
				reimbursement.setBankId(bankMaster.getKey());
				if(null != reimbursement.getKey())
				{
					entityManager.merge(reimbursement);
					entityManager.flush();
				}
				submitTaskToBPM(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
			}*/
		

		
		// Bypass will always go to BILLING .....
		if(sendTowhere.equalsIgnoreCase(ReferenceTable.DIRECT_TO_FINANCIAL)) {
			sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
		}
		
		if(isReconsideration && claimObj.getClaimType() !=  null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
			sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
		}
		
		/***
		 * Code to upload letter generated in ROD stage. 
		 * This will happen only for cashless claim and docs received from
		 * hospital and first ROD.
		 * 
		 * **/
		
		/**
		 * As per Raja, the below code is commented.
		 */
		/*if((!isQueryReplyReceived || !isReconsideration) && null != reimbursement && null != reimbursement.getClaim() && null != reimbursement.getClaim().getClaimType() && null != reimbursement.getClaim().getClaimType().getKey() 
				&& reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL)
				&& ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()))
		{
			DocumentGenerator docGen = new DocumentGenerator();
			ReportDto reportDto = new ReportDto();
			
			List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
			rodDTOList.add(rodDTO);		
			reportDto.setClaimId(rodDTO.getClaimDTO().getClaimId());
			reportDto.setBeanList(rodDTOList);
			final String filePath = docGen.generatePdfDocument("PaymentLetterToInsured", reportDto);	
			
			rodDTO.setDocFilePath(filePath);
			rodDTO.setDocType(SHAConstants.ROD_DOC_TYPE);
			

			HashMap dataMap = new HashMap();
			dataMap.put("intimationNumber",reimbursement.getClaim().getIntimation().getIntimationId());
			Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
			if(null != objClaim)
			{
				dataMap.put("claimNumber",reimbursement.getClaim().getClaimId());
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
			dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
			dataMap.put("filePath", rodDTO.getDocFilePath());
			dataMap.put("docType", rodDTO.getDocType());
			dataMap.put("docSources", SHAConstants.ROD_DOC_SOURCE);
			dataMap.put("createdBy", rodDTO.getStrUserName());
			String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);

			
			if(null != docToken)
			{
				rodDTO.setDocToken(docToken);
				//rodDTO.setIsletterGeneratedInROD(true);
			}
		
		}*/
		
		rodDTO.setTotalClaimedAmount(totalClaimedAmt);
		/**The below code added for R1169.To show the pending cashless investigations in a waiving pop up in Medical stage Rod key is updated against the 
		  corresponding investigation while creation of ROD **/
		/*if(null != rodDTO.getClaimDTO().getClaimType() && 
				rodDTO.getClaimDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE)){
			Investigation investigation = getInvestigationDetails(rodDTO.getClaimDTO().getKey(),SHAConstants.TRANSACTION_FLAG_CASHLESS);	
			DBCalculationService dbCalculationService = new DBCalculationService();
			if(null != investigation){
				
				dbCalculationService.getCashlessInvestigations(investigation.getKey(),reimbursement.getKey());
			}
		}	*/
		
		//added for PCC remarks CR
		PccRemarks  escalateRemarks = getEscalateRemarks(rodDTO.getNewIntimationDTO().getKey());
		if(escalateRemarks != null){
			rodDTO.setIsPCCSelected(true);
		}
		
		/*//Saving Document details for NEFT
		
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(docAck.getDocumentReceivedFromId().getKey())){
					 
					List<DocumentDetails> documentDetails = getDocumentDetailsByIntimationNumber(rodDTO.getPreauthDTO().getNewIntimationDTO().getIntimationId(),SHAConstants.NEFT_DETAILS);
					if(documentDetails !=null){
						System.out.println("Document type NEFT Details Uploaded to DMS");
						rodDTO.setIsNEFTDetailsAvailableinDMS(true);
						
					}
				
				}*/
		
		//Added for Upload NEFT Details
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getPaymentModeFlag() != null && rodDTO.getDocumentDetails().getPaymentModeFlag().equals(ReferenceTable.BANK_TRANSFER)){
//		reimbursement = getReimbursementByKey(reimbursement.getKey());
		saveNEFTDetails(rodDTO,reimbursement);
		}
		if(rodDTO.getDocumentDetails().getPaymentModeFlag().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)
				&& rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProductType() != null
				&& !rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProductType().getKey().equals(ReferenceTable.GROUP_POLICY)){
//			reimbursement = getReimbursementByKey(reimbursement.getKey());
			saveNEFTDetailsForCheque(rodDTO,reimbursement);

		}
	    
//		submitTaskToBPM(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
		submitTaskToDBProcedureForCreateROD(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
		
		return reimbursement;

	}



	private void legalHeirAndDocumentDetails(ReceiptOfDocumentsDTO rodDTO) {
		String username1 = rodDTO.getStrUserName();
		String userNameForDB1 = SHAUtils.getUserNameForDB(username1);
		if(rodDTO.getPreauthDTO().getLegalHeirDTOList() != null && !rodDTO.getPreauthDTO().getLegalHeirDTOList().isEmpty()) {
			LegalHeir legalHeir;
			for (LegalHeirDTO legalHeirDto : rodDTO.getPreauthDTO().getLegalHeirDTOList()) {
				
				legalHeir = new LegalHeir();
				if(legalHeirDto.getLegalHeirKey() != null) {
					legalHeir.setKey(legalHeirDto.getLegalHeirKey());
				}
			
			legalHeir.setLegalHeirName(legalHeirDto.getHeirName());
//			legalHeir.setRelationCode(legalHeirDto.getRelationship().getId());

			if(legalHeirDto.getRelationship() != null && legalHeirDto.getRelationship().getValue() != null) {
			legalHeir.setRelationDesc(legalHeirDto.getRelationship().getValue());
			}
			//IMSSUPPOR-31064 - If user not entered share percentage, default to zero
			//legalHeir.setSharePercentage(legalHeirDto.getSharePercentage());
			legalHeir.setSharePercentage(legalHeirDto.getSharePercentage() != null ? legalHeirDto.getSharePercentage() : 0d);
			if(legalHeirDto.getAddress() != null && !legalHeirDto.getAddress().isEmpty()) {
			legalHeir.setAddress(legalHeirDto.getAddress());
			}
			if(legalHeirDto.getAccountType() != null && legalHeirDto.getAccountType().getValue() != null) {
				legalHeir.setAccountType(legalHeirDto.getAccountType().getValue());
			}
			legalHeir.setBeneficiaryName(legalHeirDto.getBeneficiaryName());
			if(legalHeirDto.getAccountNo() != null && !legalHeirDto.getAccountNo().isEmpty()){
				legalHeir.setAccountNo(Long.valueOf(legalHeirDto.getAccountNo()));
			}
			legalHeir.setAccountPreference(legalHeirDto.getAccountPreference() != null ? legalHeirDto.getAccountPreference().getValue() : null);
			
			legalHeir.setPaymentModeId(legalHeirDto.getPaymentModeId() != null ? legalHeirDto.getPaymentModeId() : null);
			legalHeir.setPayableAt(legalHeirDto.getPayableAt() != null ? legalHeirDto.getPayableAt() : null);
			BankMaster bankMaster = validateIFSCCode( legalHeirDto.getIfscCode());
			if(null != bankMaster)
			{
				legalHeir.setBankName(bankMaster.getBankName() != null ? bankMaster.getBankName() : legalHeirDto.getBankName());
				legalHeir.setBankBranchName(bankMaster.getBranchName() != null ? bankMaster.getBranchName(): legalHeirDto.getBankBranchName());
			}
			legalHeir.setIfscCode(legalHeirDto.getIfscCode());
			if (legalHeirDto.getDocumentToken() != null) {
				legalHeir.setUploadFlag(SHAConstants.YES_FLAG);
			} else {
				legalHeir.setUploadFlag(SHAConstants.N_FLAG);
			}
			if (legalHeirDto.getDocumentToken() != null) {
			legalHeir.setDocKey(legalHeirDto.getDocumentToken());
			}
			legalHeir.setRodKey(rodDTO.getKey());
//			legalHeir.setIntimationKey(rodDTO.getNewIntimationDTO().getKey());
			legalHeir.setPolicyKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
			
			if(rodDTO.getNewIntimationDTO().getInsuredPatient() != null){
				legalHeir.setInsuredKey(rodDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			}
			else if(rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient() != null){
				legalHeir.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
			}
			legalHeir.setPincode(legalHeirDto.getPincode() != null ? legalHeirDto.getPincode() : null);
			
			if(legalHeirDto.getLegalHeirKey() == null && (legalHeirDto.getDeleteLegalHeir() == null || (legalHeirDto.getDeleteLegalHeir() != null && legalHeirDto.getDeleteLegalHeir().equalsIgnoreCase(SHAConstants.N_FLAG)))) {
				legalHeir.setActiveStatus(SHAConstants.YES_FLAG);
				legalHeir.setCreatedBy(userNameForDB1);
				legalHeir.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(legalHeir);
			}
			else if(legalHeirDto.getLegalHeirKey() != null){
				legalHeir.setModifiedBy(userNameForDB1);
				legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				legalHeir.setActiveStatus(SHAConstants.YES_FLAG);
				if(legalHeirDto.getDeleteLegalHeir() != null){
					//IMSSUPPOR-38069
					if(legalHeirDto.getDeleteLegalHeir() != null && legalHeirDto.getDeleteLegalHeir().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						legalHeir.setActiveStatus(SHAConstants.N_FLAG);
					}else{
					legalHeir.setActiveStatus(SHAConstants.YES_FLAG);
					}
//					legalHeir.setActiveStatus(legalHeirDto.getDeleteLegalHeir());
				}
				entityManager.merge(legalHeir);
			}
			entityManager.flush();
			//persisting in document details table
			if(legalHeirDto.getDocumentToken() != null){
				DocumentDetails docDetails = new DocumentDetails();
				docDetails.setDocumentToken(legalHeirDto.getDocumentToken());
				docDetails.setIntimationNumber(rodDTO.getNewIntimationDTO().getIntimationId());
				docDetails.setClaimNumber(rodDTO.getNewIntimationDTO().getClaimNumber());
				docDetails.setReimbursementNumber(rodDTO.getRodNumberForUploadTbl());
				docDetails.setFileName(legalHeirDto.getFileName());
				docDetails.setDocumentType(SHAConstants.LEGAL_HEIR_CERT);
				docDetails.setRodKey(rodDTO.getKey());
				entityManager.persist(docDetails);
				entityManager.flush();
			}
		}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitPAROD(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit CREATE ROD ________________" + (rodDTO.getNewIntimationDTO() != null ? rodDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
		Reimbursement reimbursement = null;
		try {
			reimbursement = savePARODValues(rodDTO,SHAConstants.PA_LOB);

			if (rodDTO.getChangeHospitalDto() != null) {
				try {
					changeHospitalDetails(rodDTO.getChangeHospitalDto());   
				} catch (Exception e) {
					log.error(e.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		return reimbursement;
	}
	
	public Boolean validateRODForCashlessClaim(ReceiptOfDocumentsDTO rodDTO)
	{
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		//Long claimKey = rodDTO.getClaimDTO().getKey();
		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
			Double totalClaimedAmt = 0d;
			if(null != rodDTO.getDocumentDetails().getHospitalizationClaimedAmount() && !("").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount()))
			{
				totalClaimedAmt += Double.parseDouble(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount());
			}
			if(null != rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount() && !("").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()))
			{
				totalClaimedAmt += Double.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount());
			}
			if(null != rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount() && !("").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount()))
			{
				totalClaimedAmt += Double.parseDouble(rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount());
			}
			
			if(null != rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount() && !("").equalsIgnoreCase(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()))
			{
				totalClaimedAmt += Double.parseDouble(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount());
			}
			
			Double preauthApprovedAmt = getPreauthApprovedAmtForValidation(rodDTO.getClaimDTO().getKey());
			if(null != preauthApprovedAmt && (totalClaimedAmt < preauthApprovedAmt))
			{
				return true;
			}
			return false;
		}
			return false;
	}
	
	public Boolean validatePARODForCashlessClaim(ReceiptOfDocumentsDTO rodDTO)
	{
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		Long claimKey = rodDTO.getClaimDTO().getKey();
		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
			Double totalClaimedAmt = 0d;
			if(null != rodDTO.getDocumentDetails().getBenifitClaimedAmount() && !("").equalsIgnoreCase(rodDTO.getDocumentDetails().getBenifitClaimedAmount()))
			{
				totalClaimedAmt += Double.parseDouble(rodDTO.getDocumentDetails().getBenifitClaimedAmount());
			} 
						
			Double preauthApprovedAmt = getPreauthApprovedAmtForValidation(rodDTO.getClaimDTO().getKey());
			if(null != preauthApprovedAmt && (totalClaimedAmt < preauthApprovedAmt))
			{
				return true;
			}
			return false;
		}
			return false;
	}

	public Boolean changeHospitalDetails(UpdateHospitalDetailsDTO bean) {
		
		
		
		if(bean.getKey() != null){
			
			Hospitals hospitals = getHospitalsByKey(bean.getKey());
			
			Intimation intimation = bean.getIntimation();
			if(intimation != null && hospitals != null){
				intimation.setHospital(hospitals.getKey());
				
				/**
				 * cpu code updated in intimation
				 */
				
				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitals.getCpuId());
				if(tmpCPUCode != null){
					intimation.setCpuCode(tmpCPUCode);
				}
				
				if(hospitals.getHospitalType() != null){
					intimation.setHospitalType(hospitals.getHospitalType());
				}				
				/*else{
					MastersValue type = new MastersValue();
					type.setKey(bean.getHospitalType().getId());
					type.setValue(bean.getHospitalType().getValue());
				}*/

				entityManager.merge(intimation);
				entityManager.flush();
				entityManager.clear();
				log.info("------Intimation------>"+intimation+"<------------");

			}
			
			return true;
			
		}else{
			
			UpdateHospital updateDetails =  UpdateHospitalDetailsMapper.getInstance().getUpdateHospital(bean);
	
			if (updateDetails != null) {
		
				updateDetails.setHospitalTypeId(bean.getHospitalType().getId());
				entityManager.persist(updateDetails);
				entityManager.flush();
				entityManager.clear();
				log.info("------UpdateHospital------>"+updateDetails+"<------------");
		
				Intimation intimation = bean.getIntimation();
		
				if (intimation != null) {
					if (intimation.getKey() != null) {
						if (updateDetails.getKey() != null) {
							intimation.setHospital(updateDetails.getKey());
							MastersValue type = new MastersValue();
							type.setKey(bean.getHospitalType().getId());
							type.setValue(bean.getHospitalType().getValue());
							intimation.setHospitalType(type);
							entityManager.merge(intimation);
							entityManager.flush();
							entityManager.clear();
							log.info("------Intimation------>"+intimation+"<------------");
						}
					}
				}
				return true;
			}
			
		}
		return false;

	}
	
	public TmpCPUCode getTmpCPUCode(Long cpuID) {
		TmpCPUCode tmpCpuCode = null;

		Query findByTmpCpuCode = entityManager.createNamedQuery(
				"TmpCPUCode.findByKey").setParameter("cpuId",
						cpuID);
		try {
			List<TmpCPUCode> resultList = (List<TmpCPUCode>) findByTmpCpuCode.getResultList();
			
			if(resultList != null && !resultList.isEmpty()) {
				return resultList.get(0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}

		return tmpCpuCode;
	}

	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean submitBillEntryValues(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit BILL ENTRY ________________" + (rodDTO.getNewIntimationDTO() != null ? rodDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
		Boolean isBillEntrySubmitted = true; 
	    //Boolean isQueryReceived = false;
	    Boolean isReconsider = false;
		try {
			

			CreateRODMapper rodMapper = CreateRODMapper.getInstance();
			
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());

			Reimbursement reimbursement = null;
			Double totalClaimedAmtForBenefits = 0d;
			Double totalClaimedAmt = 0d;
		//	Double previousCurrentProvAmt = 0d;
			if (null != rodDTO.getReconsiderRODdto()
					&& null != rodDTO.getReconsiderRODdto().getRodKey()) {
				Long rodKey = rodDTO.getReconsiderRODdto().getRodKey();
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				reimbursement = getReimbursementObjectByKey(rodKey);
				isReconsider = true;
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */

			} else if (null != rodDTO.getRodqueryDTO()
					&& null != rodDTO.getRodqueryDTO().getReimbursementKey()) {
				Long rodKey = rodDTO.getRodqueryDTO().getReimbursementKey();
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				reimbursement = getReimbursementObjectByKey(rodKey);
				//isQueryReceived = true;
				//previousCurrentProvAmt= reimbursement.getCurrentProvisionAmt();
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */

			} else {
				reimbursement = getReimbursementObjectByKey(rodDTO
						.getDocumentDetails().getRodKey());
			}

			/**
			 * The total amount to be claimed for submitted documents will be
			 * calculated during bill entry level. This amount corresponds to
			 * the documents submitted per ROD. Hence this amount to be saved in
			 * reimbursement table.
			 * */
			/*reimbursement.setCurrentProvisionAmt(rodDTO
					.getCurrentProvisionAmount());*/

			Stage stage = new Stage();
			stage.setKey(ReferenceTable.BILL_ENTRY_STAGE_KEY);

			Status status = new Status();
			status.setKey(ReferenceTable.BILL_ENTRY_STATUS_KEY);

			
			String strUserName = rodDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			reimbursement.setModifiedBy(strUserName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStage(stage);
			reimbursement.setStatus(status);
			
			if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()
					&& ! rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount().isEmpty()){
				
				reimbursement.setOtherBenefitApprovedAmt(Double.valueOf(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()));
			}// Below condition for IMSSUPPOR-29627 - Scenario - Other benefits selected in Rod and Uncheck in Bill entry other benefit amont not reset to zero
			else {
				reimbursement.setOtherBenefitApprovedAmt(0d);
			}

			if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()
					&& ! rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt().isEmpty()){
				
				reimbursement.setProdBenefitAmount(Double.valueOf(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
			}
		/*	if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getHospitalCashDueTo() && null != rodDTO.getDocumentDetails().getHospitalCashDueTo().getId()){
				
				reimbursement.setProdBenefitDueToID(rodDTO.getDocumentDetails().getHospitalCashDueTo().getId());
			}
           if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash() && null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId()){
				
				reimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
			}*/
           
           if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getDateOfDischarge()){
        	   reimbursement.setDateOfDischarge(rodDTO.getDocumentDetails().getDateOfDischarge());
           }
           
           if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getDateOfAdmission()){
        	   reimbursement.setDateOfAdmission(rodDTO.getDocumentDetails().getDateOfAdmission());
           }
           
           if(null != rodDTO && null != rodDTO.getUploadDocumentsDTO() && null != rodDTO.getUploadDocumentsDTO().getRemarksBillEntry()){
        	   reimbursement.setRemarksBillEntry(rodDTO.getUploadDocumentsDTO().getRemarksBillEntry());
           }
           
            
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
			log.info("------Reimbursement------>"+reimbursement+"<------------");

			List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				saveBillEntryValues(uploadDocumentDTO);
				/*

				RODDocumentSummary rodSummary = CreateRODMapper
						.getDocumentSummary(uploadDocumentDTO);
				rodSummary.setReimbursement(reimbursement);
				rodSummary.setDeletedFlag("N");
				if (null != uploadDocumentDTO.getDocSummaryKey()) {
					
					rodSummary.setModifiedBy(strUserName);
					rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.merge(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
				} else {
					
					rodSummary.setCreatedBy(strUserName);
					rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					entityManager.refresh(rodSummary);
				}

				if (null != docAck) {
					DocAcknowledgement docAcknowledgement = rodMapper
							.getAcknowledgeDocumentList(rodDTO);
					if ((null != rodDTO.getDocumentDetails()
							.getHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
 									.getHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getHospitalizationClaimedAmount());
					}
					if ((null != rodDTO.getDocumentDetails()
							.getPreHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getPreHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
									.getPreHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setPreHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getPreHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getPreHospitalizationClaimedAmount());
					}
					if ((null != rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getPostHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
									.getPostHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setPostHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getPostHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getPostHospitalizationClaimedAmount());	
					}
					docAcknowledgement.setClaim(docAck.getClaim());
					docAcknowledgement.setStage(docAck.getStage());
					docAcknowledgement.setStatus(docAck.getStatus());
					docAcknowledgement.setInsuredContactNumber(docAck
							.getInsuredContactNumber());
					docAcknowledgement.setInsuredEmailId(docAck
							.getInsuredEmailId());
					docAcknowledgement.setAcknowledgeNumber(docAck
							.getAcknowledgeNumber());
					docAcknowledgement.setCreatedDate(docAck.getCreatedDate());

					docAcknowledgement.setRodKey(reimbursement.getKey());

					entityManager.merge(docAcknowledgement);
					entityManager.flush();
					// entityManager.refresh(docAcknowledgement);
				}
				
				

				List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
						.getBillEntryDetailList();
				if (null != billEntryDetailsList
						&& !billEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

						if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
								billEntryDetailsDTO.getBillNo())) {
							if(isQueryReceived){

							}
							RODBillDetails rodBillDetails = rodMapper
									.getRODBillDetails(billEntryDetailsDTO);
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setDeletedFlag("N");
							if(null != rodBillDetails.getKey())
							{
								//rodBillDetails.setM(strUserName);
								// modified date and time field is not available in bill details table.
								//rodBillDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(rodBillDetails);
								entityManager.flush();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							else
							{
								// created date and time field is not available in bill details table.
								entityManager.persist(rodBillDetails);
								entityManager.flush();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}

						}
					}
				}
				
				*//**
				 * Below method is added for enabling save bill entry
				 * option in bill entry pop up. Same is now resued in save also.
				 * *//*
				//saveBillEntryValues(uploadDocumentDTO,rodSummary);

			*/}

			List<UploadDocumentDTO> deletedDocsList = rodDTO
					.getUploadDocumentsDTO().getDeletedDocumentList();
			if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

					RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
							.getDocumentSummary(uploadDocumentDTO2);
					rodSummary.setReimbursement(reimbursement);
					rodSummary.setDeletedFlag("Y");

					if (null != uploadDocumentDTO2.getDocSummaryKey()) {
						rodSummary.setModifiedBy(strUserName);
						rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(rodSummary);
						entityManager.flush();
						entityManager.clear();
						log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					} 
					
					DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
					if(null != details)
					{
						details.setDeletedFlag("Y");
						entityManager.merge(details);
						entityManager.flush();
						entityManager.clear();
						log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
					}
							
					
					/*
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 */

				}
			}
			
			if (null != docAck) {
				DocAcknowledgement docAcknowledgement = rodMapper
						.getAcknowledgeDocumentList(rodDTO);
				if ((null != rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
									.getHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getHospitalizationClaimedAmount());
				}
				if ((null != rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getPreHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPreHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setPreHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getPreHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getPreHospitalizationClaimedAmount());
				}
				if ((null != rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getPostHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPostHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setPostHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getPostHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getPostHospitalizationClaimedAmount());	
				}
				
				//added for new product
				if ((null != rodDTO.getDocumentDetails()
						.getHospitalCashClaimedAmnt())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getHospitalCashClaimedAmnt())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
									.getHospitalCashClaimedAmnt())))
				{
					docAcknowledgement
							.setProdHospBenefitClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getHospitalCashClaimedAmnt()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getHospitalCashClaimedAmnt());
				}
				docAcknowledgement.setClaim(docAck.getClaim());
				docAcknowledgement.setStage(docAck.getStage());
				docAcknowledgement.setStatus(docAck.getStatus());
				docAcknowledgement.setInsuredContactNumber(docAck
						.getInsuredContactNumber());
				docAcknowledgement.setInsuredEmailId(docAck
						.getInsuredEmailId());
				docAcknowledgement.setAcknowledgeNumber(docAck
						.getAcknowledgeNumber());
				docAcknowledgement.setCreatedDate(docAck.getCreatedDate());

				docAcknowledgement.setRodKey(reimbursement.getKey());
				if(isReconsider)
				{
					if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
					{
						MastersValue masValue = new MastersValue();
						masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
						masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
						docAcknowledgement.setReconsiderationReasonId(masValue);
					}
					if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
						docAcknowledgement.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
					
					docAcknowledgement.setReconsideredDate(new Timestamp(System.currentTimeMillis()));
				}
				
				
				
				docAcknowledgement.setModifiedBy(strUserName);
				docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				if((rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && (ReferenceTable.getFHORevisedKeys().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
						|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
						|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))*/
							(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
								&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode())
									||  SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getPolicyPlan()))
						|| (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
								&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))))
								
				{
				docAcknowledgement.setOtherBenefitsFlag(rodDTO.getDocumentDetails().getOtherBenefitsFlag());
				if(null != rodDTO.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getOtherBenefitsFlag()) )
				{
					docAcknowledgement.setEmergencyMedicalEvaluation(rodDTO.getDocumentDetails().getEmergencyMedicalEvaluationFlag());
					docAcknowledgement.setCompassionateTravel(rodDTO.getDocumentDetails().getCompassionateTravelFlag());
					docAcknowledgement.setRepatriationOfMortalRemain(rodDTO.getDocumentDetails().getRepatriationOfMortalRemainsFlag());
					docAcknowledgement.setPreferredNetworkHospita(rodDTO.getDocumentDetails().getPreferredNetworkHospitalFlag());
					docAcknowledgement.setSharedAccomodation(rodDTO.getDocumentDetails().getSharedAccomodationFlag());
					
					if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()
							&& ! rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount().equalsIgnoreCase("")){
						docAcknowledgement.setOtherBenefitsClaimedAmount(Double.valueOf(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()));
					}
				}
				else
				{
					docAcknowledgement.setEmergencyMedicalEvaluation(SHAConstants.N_FLAG);
					docAcknowledgement.setCompassionateTravel(SHAConstants.N_FLAG);
					docAcknowledgement.setRepatriationOfMortalRemain(SHAConstants.N_FLAG);
					docAcknowledgement.setPreferredNetworkHospita(SHAConstants.N_FLAG);
					docAcknowledgement.setSharedAccomodation(SHAConstants.N_FLAG);
					
				}
				}
				//added for new product
				docAcknowledgement.setProdHospBenefitFlag(rodDTO.getDocumentDetails().getHospitalCashFlag());
				

				entityManager.merge(docAcknowledgement);
				entityManager.flush();
				entityManager.clear();
				log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
				// entityManager.refresh(docAcknowledgement);
			}

			// if(null !=
			// rodDTO.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag()
			// &&
			// ("Y").equalsIgnoreCase(rodDTO.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag()))
			
			
			
			if ((null != rodDTO.getDocumentDetails()
					.getAddOnBenefitsHospitalCash() && (rodDTO
					.getDocumentDetails().getAddOnBenefitsHospitalCash()))) {
				UploadDocumentDTO dto = rodDTO.getUploadDocumentsDTO();
				ReimbursementBenefits reimbursementBenefits   = null;
				List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reimbursement.getKey());
				if(null != reimbHospitalCash && !reimbHospitalCash.isEmpty())
						{
							for (ReimbursementBenefits reimbursementBenefits2 : reimbHospitalCash) {
								if(("HC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
										{
											reimbursementBenefits = reimbursementBenefits2;
											break;
										}
								else
								{
									reimbursementBenefits	= new ReimbursementBenefits();
								}
							}
						}
				else
				{
					reimbursementBenefits	= new ReimbursementBenefits();
				}
				reimbursementBenefits.setReimbursementKey(reimbursement);
				reimbursementBenefits.setTreatmentForPhysiotherapy(dto
						.getTreatmentPhysiotherapyFlag());
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
						.getHospitalCashNoofDays()));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
						.getHospitalCashPerDayAmt()));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				if(null != dto.getHospitalCashTotalClaimedAmt())
				{
				reimbursementBenefits.setTotalClaimAmountBills(Double
						.valueOf(dto.getHospitalCashTotalClaimedAmt()));
				reimbursementBenefits.setPayableAmount(Double
						.valueOf(dto.getHospitalCashTotalClaimedAmt()));
				totalClaimedAmtForBenefits += Double.valueOf(dto.getHospitalCashTotalClaimedAmt());
				
				/** Below code is added for MED-PRD-058 - Classic Health Group. The below change done for BSI View. 
				  Based on this change in BSI View current provision is not included both hospital cash and patient care.
				  This is  applicable only for this product*/
				//if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					
					reimbursement.setAddOnCoversApprovedAmount(Double.valueOf(dto.getHospitalCashTotalClaimedAmt()));
					}
				//}
				reimbursementBenefits.setBenefitsFlag("HC");
				
				//if(null == reimbursementBenefits.getKey())
				//if(null ==  dto.getHospitalCashReimbursementBenefitsKey())
				if(null ==  reimbursementBenefits.getKey())
				{
					reimbursementBenefits.setCreatedBy(strUserName);
					reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				else
				{
					//reimbursementBenefits.setKey(dto.getHospitalCashReimbursementBenefitsKey());
					reimbursementBenefits.setModifiedBy(strUserName);
					reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}

			}
			else
			{
				/**
				 * Below code is added due to impact of refer to bill entry option.
				 * Assume a scenario , where intially add on benefits was selected and saved
				 * during bill entry submit. Again user refers the record to bill entry.
				 * This time he/she removes benefits. Then on submit the previously saved
				 * record should be marked as deleted one. For this purpose only
				 * the below code of setting deleted flag as "Y" is added.
				 * */
				
				updateBenefitsRecord("HC", reimbursement);
			}

			// if(null !=
			// rodDTO.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag()
			// &&
			// ("Y").equalsIgnoreCase(rodDTO.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag()))
			if ((null != rodDTO.getDocumentDetails()
					.getAddOnBenefitsPatientCare() && (rodDTO
					.getDocumentDetails().getAddOnBenefitsPatientCare()))) {
				UploadDocumentDTO dto = rodDTO.getUploadDocumentsDTO();
				ReimbursementBenefits reimbursementBenefits   = null;
				List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reimbursement.getKey());
				if(null != reimbHospitalCash && !reimbHospitalCash.isEmpty())
						{
							for (ReimbursementBenefits reimbursementBenefits2 : reimbHospitalCash) {
								if(("PC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
										{
											reimbursementBenefits = reimbursementBenefits2;
											break;
										}
								else
								{
									reimbursementBenefits	= new ReimbursementBenefits();
								}
							}
						}
				else
				{
					reimbursementBenefits	= new ReimbursementBenefits();
				}
				reimbursementBenefits.setReimbursementKey(reimbursement);
				reimbursementBenefits.setTreatmentForPhysiotherapy(dto
						.getTreatmentPhysiotherapyFlag());
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
						.getPatientCareNoofDays()));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
						.getPatientCarePerDayAmt()));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				if(null != dto.getPatientCareTotalClaimedAmt())
				{
				reimbursementBenefits.setTotalClaimAmountBills(Double
						.valueOf(dto.getPatientCareTotalClaimedAmt()));
				reimbursementBenefits.setPayableAmount(Double
						.valueOf(dto.getPatientCareTotalClaimedAmt()));
				totalClaimedAmtForBenefits += Double
						.valueOf(dto.getPatientCareTotalClaimedAmt());
				
				/** Below code is added for MED-PRD-058 - Classic Health Group. 
				  Based on this change in BSI View current provision is not included both hospital cash and patient care.
				  This is  applicable only for this product*/
				//if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					
					reimbursement.setOptionalApprovedAmount(Double.valueOf(dto.getPatientCareTotalClaimedAmt()));
					}
					
				//}
				reimbursementBenefits.setBenefitsFlag("PC");
				
			//	if(null == reimbursementBenefits.getKey())
				//if(null == dto.getPatientCareReimbursementBenefitsKey())
				if(null == reimbursementBenefits.getKey())
				{
					reimbursementBenefits.setCreatedBy(strUserName);
					reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				else
				{
					//reimbursementBenefits.setKey(dto.getPatientCareReimbursementBenefitsKey());
					reimbursementBenefits.setModifiedBy(strUserName);
					reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				

				List<PatientCareDTO> patientList = rodDTO
						.getUploadDocumentsDTO().getPatientCareDTO();

				if (null != patientList && !patientList.isEmpty()) {
					for (PatientCareDTO patientCareDTO : patientList) {
						//ReimbursementBenefitsDetails reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
						ReimbursementBenefitsDetails reimbursementBenefitDetails = null;
						List<ReimbursementBenefitsDetails> reimbBenefitsDetails = null;
						if(null != reimbursementBenefits)
							reimbBenefitsDetails = geReimbursmentBenefitsDetails(reimbursementBenefits.getKey());
						if(null != reimbBenefitsDetails && !reimbBenefitsDetails.isEmpty())
						{
							for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetails) {
								if(null != reimbursementBenefitsDetails.getKey() && null != patientCareDTO.getKey() && reimbursementBenefitsDetails.getKey().equals(patientCareDTO.getKey()))
										{
											reimbursementBenefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
											reimbursementBenefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
											reimbursementBenefitsDetails.setReimbursementBenefits(reimbursementBenefits);
											reimbursementBenefits.setModifiedBy(strUserName);
											reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
											entityManager.merge(reimbursementBenefitsDetails);
											entityManager.flush();
											entityManager.clear();
											log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
										//	break;
										}
								else
								{
									reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
								}
							}
						}
						else
						{
							reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
							reimbursementBenefitDetails
									.setEngagedFrom(patientCareDTO.getEngagedFrom());
							reimbursementBenefitDetails.setEngagedTo(patientCareDTO
									.getEngagedTo());
							reimbursementBenefitDetails
									.setReimbursementBenefits(reimbursementBenefits);
							
							if(null == patientCareDTO.getReconsiderReimbursementBenefitsKey())
							{	
								reimbursementBenefits.setCreatedBy(strUserName);
								reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.persist(reimbursementBenefitDetails);
								entityManager.flush();
								log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
							}
							else
							{
								reimbursementBenefitDetails.setKey(patientCareDTO.getReconsiderReimbursementBenefitsKey());
								reimbursementBenefits.setModifiedBy(strUserName);
								reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(reimbursementBenefitDetails);
								entityManager.flush();
								log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
							}
							entityManager.clear();
						}
					}
				}

			}
			else
			{
					/**
					 * Below code is added due to impact of refer to bill entry option.
					 * Assume a scenario , where intially add on benefits was selected and saved
					 * during bill entry submit. Again user refers the record to bill entry.
					 * This time he/she removes benefits. Then on submit the previously saved
					 * record should be marked as deleted one. For this purpose only
					 * the below code of setting deleted flag as "Y" is added.
					 * */
					updateBenefitsRecord("PC", reimbursement);
				
			}
			
			//added for new product076
			if ((null != rodDTO.getDocumentDetails()
					.getHospitalCash() && (rodDTO
					.getDocumentDetails().getHospitalCash()))) {
				List<HopsitalCashBenefitDTO> hospitalCashBenefitdto = rodDTO.getUploadDocumentsDTO().getHopsitalCashBenefitDTO();
				ReimbursementBenefits reimbursementBenefits   = null;
				List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reimbursement.getKey());
				for(HopsitalCashBenefitDTO hospitalCashBenefitDTO : hospitalCashBenefitdto){
				if(null != reimbHospitalCash && !reimbHospitalCash.isEmpty())
						{
							for (ReimbursementBenefits reimbursementBenefits2 : reimbHospitalCash) {
								if(("PHC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()) && reimbursementBenefits2.getProductBenefitID() != null && reimbursementBenefits2.getProductBenefitID().equals(hospitalCashBenefitDTO.getParticulars().getId()))
										{
											reimbursementBenefits = reimbursementBenefits2;
											break;
										}
								else
								{
									reimbursementBenefits	= new ReimbursementBenefits();
								}
							}
						}
				else
				{
					reimbursementBenefits	= new ReimbursementBenefits();
				}
				
				reimbursementBenefits.setReimbursementKey(reimbursement);
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(hospitalCashBenefitDTO.getNoOfDaysAllowed() != null && !hospitalCashBenefitDTO.getNoOfDaysAllowed().isEmpty()
						? hospitalCashBenefitDTO.getNoOfDaysAllowed() : "0"));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(hospitalCashBenefitDTO.getHospitalCashPerDayAmt() != null && !hospitalCashBenefitDTO.getHospitalCashPerDayAmt().isEmpty()
						? hospitalCashBenefitDTO.getHospitalCashPerDayAmt() : "0"));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
//				if(null != hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() && ! hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty())
//				{
				reimbursementBenefits.setTotalClaimAmountBills(Double
						.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
						? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
				reimbursementBenefits.setPayableAmount(Double
						.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
						? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
				totalClaimedAmtForBenefits += Double.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
						? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0");
				
				/** Below code is added for MED-PRD-058 - Classic Health Group. The below change done for BSI View. 
				  Based on this change in BSI View current provision is not included both hospital cash and patient care.
				  This is  applicable only for this product*/
				//if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					
					reimbursement.setAddOnCoversApprovedAmount(Double.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && ! hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
							? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
					
				//}
				reimbursementBenefits.setProductBenefitID(hospitalCashBenefitDTO.getParticulars().getId());
				reimbursementBenefits.setBenefitsFlag("PHC");
				reimbursementBenefits.setTotalNoOfDays(Long.valueOf(hospitalCashBenefitDTO.getHospitalCashDays() != null && !hospitalCashBenefitDTO.getHospitalCashDays().isEmpty()
						? hospitalCashBenefitDTO.getHospitalCashDays() : "0"));
				reimbursementBenefits.setDisallowanceRemarks(hospitalCashBenefitDTO.getDisallowanceRemarks());
				
				//if(null == reimbursementBenefits.getKey())
				//if(null ==  dto.getHospitalCashReimbursementBenefitsKey())
				if(null ==  reimbursementBenefits.getKey())
				{
					reimbursementBenefits.setCreatedBy(strUserName);
					reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				else
				{
					//reimbursementBenefits.setKey(dto.getHospitalCashReimbursementBenefitsKey());
					reimbursementBenefits.setModifiedBy(strUserName);
					reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
			}
			}
			
			/***
			 * Coded added for updating the current provision amt
			 ***/
			Double balanceSI = getBalanceSI(rodDTO);
			Double totalClaimedAmtInclBenefits = totalClaimedAmtForBenefits +totalClaimedAmt ;
		//	Double currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
			
			Double currentProvisionAmt = 0d;
		//	previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
//			previousCurrentProvAmt = getPreviousProvisionAmtForBillEntry(rodDTO.getClaimDTO().getKey(),reimbursement);
			currentProvisionAmt =   calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
			/*else
			{
				currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
			}
			*/
			
			/**
			 * provison amount is update only add on benefits in Bill Entry stage
			 */
			if(null != balanceSI && null != currentProvisionAmt)
			{
				
				if((null != rodDTO.getDocumentDetails()
					.getAddOnBenefitsHospitalCash() && (rodDTO
					.getDocumentDetails().getAddOnBenefitsHospitalCash()))
					|| (null != rodDTO.getDocumentDetails()
							.getAddOnBenefitsPatientCare() && (rodDTO
									.getDocumentDetails().getAddOnBenefitsPatientCare())) ){
				reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, currentProvisionAmt));
				}
			}
			
			//currentProvisionAmt += 
			/*if(balanceSI > currentProvisionAmt)
			{
				reimbursement.setCurrentProvisionAmt(balanceSI);
			}
			else
			{
				reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			}*/
			
			Boolean shouldSkipZMR = false;
			
			if(null != reimbursement.getKey())
			{
				Claim claim = reimbursement.getClaim();
				
				/**
				 * Added for star criticare and star medipremier product , where
				 * lumpsum is applicable.
				 * 
				 * */
				if (null != docAck && ("N").equalsIgnoreCase(docAck.getHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPreHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck.getPostHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalCashFlag()) && ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
						&& ("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
				{
					DBCalculationService dbCalculationService = new DBCalculationService();
					Long productKey = 0l;
					if(null != claim.getIntimation().getPolicy().getProduct())
						productKey  = claim.getIntimation().getPolicy().getProduct().getKey();
					Double lumpSumAmt = dbCalculationService.getLumpSumAmount(productKey, docAck.getClaim().getKey(), ReferenceTable.LUMPSUM_SUB_COVER_CODE);
					
					if(null != reimbursement)
					{
						reimbursement.setCurrentProvisionAmt(lumpSumAmt);
					}
					
						//updateProvisionAndClaimStatus(docAck, claimObj,true);	
				}
				rodDTO.setCurrentProvisionAmount(reimbursement.getCurrentProvisionAmt());
				String userName = rodDTO.getStrUserName();
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setBillEntryDate(new Timestamp(System.currentTimeMillis()));
				if(null != rodDTO && null != rodDTO.getTotalClaimedAmount())
				{
					reimbursement.setBillEntryAmt(rodDTO.getTotalClaimedAmount());
				}
				
				
				shouldSkipZMR = shouldSkipZMR(claim);
				if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					shouldSkipZMR = false;
				}
				
				reimbursement.setSkipZmrFlag(shouldSkipZMR ? "Y":"N");

				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
				log.info("------Reimbursement------>"+reimbursement+"<------------");
//				entityManager.refresh(reimbursement);
			}
			Claim claimObj = getClaimByClaimKey(reimbursement.getClaim().getKey())   ;
			if(null != claimObj)
			{
				Status status1 = new Status();
				status1.setKey(ReferenceTable.BILL_ENTRY_STATUS_KEY);
				
				Stage stage1 = new Stage();
				stage1.setKey(ReferenceTable.BILL_ENTRY_STAGE_KEY);
				
//				claimObj.setStatus(status1);
//				claimObj.setStage(stage1);
				claimObj.setDataOfAdmission(reimbursement.getDateOfAdmission());
				claimObj.setDataOfDischarge(reimbursement.getDateOfDischarge());
				claimObj.setModifiedBy(strUserName);
				claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
				if(sectionDetailsDTO != null) {
					claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
					claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
					claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
				}
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
				log.info("------Claim------>"+claimObj+"<------------");
			}
			
			Product product = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
			if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
					rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
				if(rodDTO.getPreauthDTO() != null){
				List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO();
				if(!updateOtherClaimDetailDTO.isEmpty()){
					PreMedicalMapper premedicalMapper = new PreMedicalMapper();
					List<UpdateOtherClaimDetails> updateOtherClaimDetails = premedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
					for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
						updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
						updateOtherClaimDetails2.setClaimKey(claimObj.getKey());
						updateOtherClaimDetails2.setStage(reimbursement.getStage());
//						updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
						updateOtherClaimDetails2.setIntimationKey(claimObj.getIntimation().getKey());
						updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
						updateOtherClaimDetails2.setClaimType(claimObj.getClaimType().getValue());
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
			}
			
			
//			Boolean isFirstRODApproved = getStatusOfFirstROD(rodDTO.getClaimDTO().getKey(),reimbursement);
			
			
				//submitBillEntryTaskToBPM(rodDTO,claimObj,shouldSkipZMR);
			rodDTO.getDocumentDetails().setRodNumber(reimbursement.getRodNumber());
			submitBillEntryTaskToDB(rodDTO,claimObj,shouldSkipZMR);
				isBillEntrySubmitted = true;
			
			
			
			
			/*Double currentProvisionAmt = totalClaimedAmt + totalClaimedAmtForBenefits;
			
			Double currentProvisionAmt = rodDTO.getCurrentProvisionAmount() + totalClaimedAmtForBenefits;
			reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			if(null != reimbursement.getKey())
			{
				entityManager.merge(reimbursement);
				entityManager.flush();
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		
		return isBillEntrySubmitted;
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean submitPABillEntryValues(ReceiptOfDocumentsDTO rodDTO,String productType) {
		

		log.info("Submit BILL ENTRY ________________" + (rodDTO.getNewIntimationDTO() != null ? rodDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
		Boolean isBillEntrySubmitted = true; 
	    Boolean isQueryReceived = false;
	    Boolean isReconsider = false;
		try {
			
			CreateRODMapper rodMapper =  CreateRODMapper.getInstance();

			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());

			Reimbursement reimbursement = null;
			Double totalClaimedAmtForBenefits = 0d;
			Double totalClaimedAmt = 0d;
		//	Double previousCurrentProvAmt = 0d;
			if (null != rodDTO.getReconsiderRODdto()
					&& null != rodDTO.getReconsiderRODdto().getRodKey()) {
				Long rodKey = rodDTO.getReconsiderRODdto().getRodKey();
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				reimbursement = getReimbursementObjectByKey(rodKey);
				isReconsider = true;
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */

			} else if (null != rodDTO.getRodqueryDTO()
					&& null != rodDTO.getRodqueryDTO().getReimbursementKey()) {
				Long rodKey = rodDTO.getRodqueryDTO().getReimbursementKey();
				/*
				 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
				 * entityManager.flush();
				 */
				reimbursement = getReimbursementObjectByKey(rodKey);
				isQueryReceived = true;
				//previousCurrentProvAmt= reimbursement.getCurrentProvisionAmt();
				/*
				 * reimbursement.setDocAcknowLedgement(docAck);
				 * entityManager.persist(reimbursement); entityManager.flush();
				 */

			} else {
				reimbursement = getReimbursementObjectByKey(rodDTO
						.getDocumentDetails().getRodKey());
				if(reimbursement!=null){
					reimbursement.setFinancialApprovalRemarks(null);
				}
			}

			/**
			 * The total amount to be claimed for submitted documents will be
			 * calculated during bill entry level. This amount corresponds to
			 * the documents submitted per ROD. Hence this amount to be saved in
			 * reimbursement table.
			 * */
			/*reimbursement.setCurrentProvisionAmt(rodDTO
					.getCurrentProvisionAmount());*/

			Stage stage = new Stage();
			stage.setKey(ReferenceTable.BILL_ENTRY_STAGE_KEY);

			Status status = new Status();
			status.setKey(ReferenceTable.BILL_ENTRY_STATUS_KEY);

			
			String strUserName = rodDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			reimbursement.setModifiedBy(strUserName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStage(stage);
			reimbursement.setStatus(status);
			
			if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getWorkOrNonWorkPlaceFlag()){
				
				reimbursement.setWorkPlace(rodDTO.getDocumentDetails().getWorkOrNonWorkPlaceFlag());
			}
            
			entityManager.merge(reimbursement);
			entityManager.flush();
			log.info("------Reimbursement------>"+reimbursement+"<------------");

			List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				saveBillEntryValues(uploadDocumentDTO);
				/*

				RODDocumentSummary rodSummary = CreateRODMapper
						.getDocumentSummary(uploadDocumentDTO);
				rodSummary.setReimbursement(reimbursement);
				rodSummary.setDeletedFlag("N");
				if (null != uploadDocumentDTO.getDocSummaryKey()) {
					
					rodSummary.setModifiedBy(strUserName);
					rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.merge(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
				} else {
					
					rodSummary.setCreatedBy(strUserName);
					rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					entityManager.refresh(rodSummary);
				}

				if (null != docAck) {
					DocAcknowledgement docAcknowledgement = rodMapper
							.getAcknowledgeDocumentList(rodDTO);
					if ((null != rodDTO.getDocumentDetails()
							.getHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
 									.getHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getHospitalizationClaimedAmount());
					}
					if ((null != rodDTO.getDocumentDetails()
							.getPreHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getPreHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
									.getPreHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setPreHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getPreHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getPreHospitalizationClaimedAmount());
					}
					if ((null != rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount())
							&& !("").equals(rodDTO.getDocumentDetails()
									.getPostHospitalizationClaimedAmount())
							&& (!("null").equalsIgnoreCase(rodDTO
									.getDocumentDetails()
									.getPostHospitalizationClaimedAmount())))
					{
						docAcknowledgement
								.setPostHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO
												.getDocumentDetails()
												.getPostHospitalizationClaimedAmount()));
						totalClaimedAmt += 	Double
								.parseDouble(rodDTO
										.getDocumentDetails()
										.getPostHospitalizationClaimedAmount());	
					}
					docAcknowledgement.setClaim(docAck.getClaim());
					docAcknowledgement.setStage(docAck.getStage());
					docAcknowledgement.setStatus(docAck.getStatus());
					docAcknowledgement.setInsuredContactNumber(docAck
							.getInsuredContactNumber());
					docAcknowledgement.setInsuredEmailId(docAck
							.getInsuredEmailId());
					docAcknowledgement.setAcknowledgeNumber(docAck
							.getAcknowledgeNumber());
					docAcknowledgement.setCreatedDate(docAck.getCreatedDate());

					docAcknowledgement.setRodKey(reimbursement.getKey());

					entityManager.merge(docAcknowledgement);
					entityManager.flush();
					// entityManager.refresh(docAcknowledgement);
				}
				
				

				List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
						.getBillEntryDetailList();
				if (null != billEntryDetailsList
						&& !billEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

<<<<<<< HEAD
			reimb.setModifiedBy(userName);
			reimb.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimb);
			entityManager.flush();
			entityManager.clear();
			log.info("------Reimbursement------>"+reimb+"<------------");
		}
		
	}
	
	public Double getBalanceSI(ReceiptOfDocumentsDTO rodDTO)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Long policyKey = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey();
		if(null != rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId())
		{
			sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()),
					policyKey);
		}
		Double balanceSI = 0d;
		if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
			balanceSI = dbCalculationService.getBalanceSIForGMC(policyKey , rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() , rodDTO.getClaimDTO().getKey());
		}else{
			Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey , rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() , rodDTO.getClaimDTO().getKey(),sumInsured,rodDTO.getClaimDTO().getNewIntimationDto().getKey());
			balanceSI = (Double)balanceSIMap.get(SHAConstants.CURRENT_BALANCE_SI);
		}
		
		return balanceSI;
		
		
	}
=======
						if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
								billEntryDetailsDTO.getBillNo())) {
							if(isQueryReceived){
>>>>>>> mayrelease_17_05

							}
							RODBillDetails rodBillDetails = rodMapper
									.getRODBillDetails(billEntryDetailsDTO);
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setDeletedFlag("N");
							if(null != rodBillDetails.getKey())
							{
								//rodBillDetails.setM(strUserName);
								// modified date and time field is not available in bill details table.
								//rodBillDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(rodBillDetails);
								entityManager.flush();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							else
							{
								// created date and time field is not available in bill details table.
								entityManager.persist(rodBillDetails);
								entityManager.flush();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}

						}
					}
				}
				
				*//**
				 * Below method is added for enabling save bill entry
				 * option in bill entry pop up. Same is now resued in save also.
				 * *//*
				//saveBillEntryValues(uploadDocumentDTO,rodSummary);

			*/}

			List<UploadDocumentDTO> deletedDocsList = rodDTO
					.getUploadDocumentsDTO().getDeletedDocumentList();
			if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

					RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
							.getDocumentSummary(uploadDocumentDTO2);
					rodSummary.setReimbursement(reimbursement);
					rodSummary.setDeletedFlag("Y");

					if (null != uploadDocumentDTO2.getDocSummaryKey()) {
						rodSummary.setModifiedBy(strUserName);
						rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(rodSummary);
						entityManager.flush();
						log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					} 
					
					DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
					if(null != details)
					{
						details.setDeletedFlag("Y");
						entityManager.merge(details);
						entityManager.flush();
						log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
					}
							
					
					/*
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 */

				}
			}
			
			DocAcknowledgement docAcknowledgement  = null;
			if (null != docAck) {
				docAcknowledgement = rodMapper
						.getAcknowledgeDocumentList(rodDTO);
				if ((null != rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
									.getHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getHospitalizationClaimedAmount());
				}
				if ((null != rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getPreHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPreHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setPreHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getPreHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getPreHospitalizationClaimedAmount());
				}
				if ((null != rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getPostHospitalizationClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPostHospitalizationClaimedAmount())))
				{
					docAcknowledgement
							.setPostHospitalizationClaimedAmount(Double
									.parseDouble(rodDTO
											.getDocumentDetails()
											.getPostHospitalizationClaimedAmount()));
					totalClaimedAmt += 	Double
							.parseDouble(rodDTO
									.getDocumentDetails()
									.getPostHospitalizationClaimedAmount());	
				}
				docAcknowledgement.setClaim(docAck.getClaim());
				docAcknowledgement.setStage(docAck.getStage());
				docAcknowledgement.setStatus(docAck.getStatus());
				docAcknowledgement.setInsuredContactNumber(docAck
						.getInsuredContactNumber());
				docAcknowledgement.setInsuredEmailId(docAck
						.getInsuredEmailId());
				docAcknowledgement.setAcknowledgeNumber(docAck
						.getAcknowledgeNumber());
				docAcknowledgement.setCreatedDate(docAck.getCreatedDate());

				docAcknowledgement.setRodKey(reimbursement.getKey());
				if(isReconsider)
				{
					if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
					{
						MastersValue masValue = new MastersValue();
						masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
						masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
						docAcknowledgement.setReconsiderationReasonId(masValue);
					}
					if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
						docAcknowledgement.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
					
					docAcknowledgement.setReconsideredDate(new Timestamp(System.currentTimeMillis()));
				}
				
				if ((null != rodDTO.getDocumentDetails()
						.getBenifitClaimedAmount())
						&& !("").equals(rodDTO.getDocumentDetails()
								.getBenifitClaimedAmount())
						&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
								.getBenifitClaimedAmount())))
				{
					docAcknowledgement.setBenifitClaimedAmount(Double
							.parseDouble(rodDTO.getDocumentDetails().getBenifitClaimedAmount()));
					totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getBenifitClaimedAmount()));
				}
				
				
				
				if(null != rodDTO.getDocumentDetails().getDeath() && (rodDTO.getDocumentDetails().getDeath().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.DEATH_FLAGS);
				}	
				else if(null != rodDTO.getDocumentDetails().getPermanentPartialDisability() && (rodDTO.getDocumentDetails().getPermanentPartialDisability().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.PPD);
				}
				else if(null != rodDTO.getDocumentDetails().getPermanentTotalDisability() && (rodDTO.getDocumentDetails().getPermanentTotalDisability().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.PTD);
				}
				else if(null != rodDTO.getDocumentDetails().getTemporaryTotalDisability() && (rodDTO.getDocumentDetails().getTemporaryTotalDisability().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.TTD);
				}
				else if(null != rodDTO.getDocumentDetails().getHospitalization() && (rodDTO.getDocumentDetails().getHospitalization().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.HOSP);
				}
				else if(null != rodDTO.getDocumentDetails().getPartialHospitalization() && (rodDTO.getDocumentDetails().getPartialHospitalization().equals(true))){
					docAcknowledgement.setBenifitFlag(SHAConstants.PART);
				}
			
				
				if(null != docAcknowledgement.getBenifitFlag())
				{
					MastersValue benefitFlag = getMasterKeyBasedOnMappingCode(docAcknowledgement.getBenifitFlag());
					if(null != benefitFlag)
					{
						MastersValue masValue = new MastersValue();
						masValue.setKey(benefitFlag.getKey());
						masValue.setValue(benefitFlag.getValue());
						reimbursement.setBenefitsId(masValue);
					}
				}
				
				if(null != reimbursement.getDocAcknowLedgement().getDocumentTypeId()){
					docAcknowledgement.setDocumentTypeId(reimbursement.getDocAcknowLedgement().getDocumentTypeId());
				}	
				
				if(null != reimbursement.getProcessClaimType())
				{
				docAcknowledgement.setProcessClaimType(reimbursement.getProcessClaimType());
				}
				
				
				docAcknowledgement.setModifiedBy(strUserName);
				docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				

				entityManager.merge(docAcknowledgement);
				entityManager.flush();
				log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
				// entityManager.refresh(docAcknowledgement);
			}

			if(null != docAcknowledgement && null != docAcknowledgement.getReconsiderationRequest() && !docAcknowledgement.getReconsiderationRequest().equals("Y"))
			{
				saveBenefitsAndCoverValues(rodDTO, docAcknowledgement, reimbursement);
			}
			
			// if(null !=
			// rodDTO.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag()
			// &&
			// ("Y").equalsIgnoreCase(rodDTO.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag()))
			
			
			
			if ((null != rodDTO.getDocumentDetails()
					.getAddOnBenefitsHospitalCash() && (rodDTO
					.getDocumentDetails().getAddOnBenefitsHospitalCash()))) {
				UploadDocumentDTO dto = rodDTO.getUploadDocumentsDTO();
				ReimbursementBenefits reimbursementBenefits   = null;
				List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reimbursement.getKey());
				if(null != reimbHospitalCash && !reimbHospitalCash.isEmpty())
						{
							for (ReimbursementBenefits reimbursementBenefits2 : reimbHospitalCash) {
								if(("HC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
										{
											reimbursementBenefits = reimbursementBenefits2;
											break;
										}
								else
								{
									reimbursementBenefits	= new ReimbursementBenefits();
								}
							}
						}
				else
				{
					reimbursementBenefits	= new ReimbursementBenefits();
				}
				reimbursementBenefits.setReimbursementKey(reimbursement);
				reimbursementBenefits.setTreatmentForPhysiotherapy(dto
						.getTreatmentPhysiotherapyFlag());
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
						.getHospitalCashNoofDays()));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
						.getHospitalCashPerDayAmt()));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				if(null != dto.getHospitalCashTotalClaimedAmt())
				{
				reimbursementBenefits.setTotalClaimAmountBills(Double
						.valueOf(dto.getHospitalCashTotalClaimedAmt()));
				totalClaimedAmtForBenefits += Double.valueOf(dto.getHospitalCashTotalClaimedAmt());
				}
				reimbursementBenefits.setBenefitsFlag("HC");
				
				//if(null == reimbursementBenefits.getKey())
				//if(null ==  dto.getHospitalCashReimbursementBenefitsKey())
				if(null ==  reimbursementBenefits.getKey())
				{
					reimbursementBenefits.setCreatedBy(strUserName);
					reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(reimbursementBenefits);
					entityManager.flush();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				else
				{
					//reimbursementBenefits.setKey(dto.getHospitalCashReimbursementBenefitsKey());
					reimbursementBenefits.setModifiedBy(strUserName);
					reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}

			}
			else
			{
				/**
				 * Below code is added due to impact of refer to bill entry option.
				 * Assume a scenario , where intially add on benefits was selected and saved
				 * during bill entry submit. Again user refers the record to bill entry.
				 * This time he/she removes benefits. Then on submit the previously saved
				 * record should be marked as deleted one. For this purpose only
				 * the below code of setting deleted flag as "Y" is added.
				 * */
				
				updateBenefitsRecord("HC", reimbursement);
			}

			// if(null !=
			// rodDTO.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag()
			// &&
			// ("Y").equalsIgnoreCase(rodDTO.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag()))
			if ((null != rodDTO.getDocumentDetails()
					.getAddOnBenefitsPatientCare() && (rodDTO
					.getDocumentDetails().getAddOnBenefitsPatientCare()))) {
				UploadDocumentDTO dto = rodDTO.getUploadDocumentsDTO();
				ReimbursementBenefits reimbursementBenefits   = null;
				List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reimbursement.getKey());
				if(null != reimbHospitalCash && !reimbHospitalCash.isEmpty())
						{
							for (ReimbursementBenefits reimbursementBenefits2 : reimbHospitalCash) {
								if(("PC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
										{
											reimbursementBenefits = reimbursementBenefits2;
											break;
										}
								else
								{
									reimbursementBenefits	= new ReimbursementBenefits();
								}
							}
						}
				else
				{
					reimbursementBenefits	= new ReimbursementBenefits();
				}
				reimbursementBenefits.setReimbursementKey(reimbursement);
				reimbursementBenefits.setTreatmentForPhysiotherapy(dto
						.getTreatmentPhysiotherapyFlag());
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
						.getPatientCareNoofDays()));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
						.getPatientCarePerDayAmt()));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				if(null != dto.getPatientCareTotalClaimedAmt())
				{
				reimbursementBenefits.setTotalClaimAmountBills(Double
						.valueOf(dto.getPatientCareTotalClaimedAmt()));
				
				totalClaimedAmtForBenefits += Double
						.valueOf(dto.getPatientCareTotalClaimedAmt());
					
				}
				reimbursementBenefits.setBenefitsFlag("PC");
				
			//	if(null == reimbursementBenefits.getKey())
				//if(null == dto.getPatientCareReimbursementBenefitsKey())
				if(null == reimbursementBenefits.getKey())
				{
					reimbursementBenefits.setCreatedBy(strUserName);
					reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(reimbursementBenefits);
					entityManager.flush();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				else
				{
					//reimbursementBenefits.setKey(dto.getPatientCareReimbursementBenefitsKey());
					reimbursementBenefits.setModifiedBy(strUserName);
					reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
				}
				

				List<PatientCareDTO> patientList = rodDTO
						.getUploadDocumentsDTO().getPatientCareDTO();

				if (null != patientList && !patientList.isEmpty()) {
					for (PatientCareDTO patientCareDTO : patientList) {
						//ReimbursementBenefitsDetails reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
						ReimbursementBenefitsDetails reimbursementBenefitDetails = null;
						List<ReimbursementBenefitsDetails> reimbBenefitsDetails = null;
						if(null != reimbursementBenefits)
							reimbBenefitsDetails = geReimbursmentBenefitsDetails(reimbursementBenefits.getKey());
						if(null != reimbBenefitsDetails && !reimbBenefitsDetails.isEmpty())
						{
							for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetails) {
								if(null != reimbursementBenefitsDetails.getKey() && null != patientCareDTO.getKey() && reimbursementBenefitsDetails.getKey().equals(patientCareDTO.getKey()))
										{
											reimbursementBenefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
											reimbursementBenefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
											reimbursementBenefitsDetails.setReimbursementBenefits(reimbursementBenefits);
											reimbursementBenefits.setModifiedBy(strUserName);
											reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
											entityManager.merge(reimbursementBenefitsDetails);
											entityManager.flush();
											log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
										//	break;
										}
								else
								{
									reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
								}
							}
						}
						else
						{
							reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
							reimbursementBenefitDetails
									.setEngagedFrom(patientCareDTO.getEngagedFrom());
							reimbursementBenefitDetails.setEngagedTo(patientCareDTO
									.getEngagedTo());
							reimbursementBenefitDetails
									.setReimbursementBenefits(reimbursementBenefits);
							
							if(null == patientCareDTO.getReconsiderReimbursementBenefitsKey())
							{	
								reimbursementBenefits.setCreatedBy(strUserName);
								reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.persist(reimbursementBenefitDetails);
								entityManager.flush();
								log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
							}
							else
							{
								reimbursementBenefitDetails.setKey(patientCareDTO.getReconsiderReimbursementBenefitsKey());
								reimbursementBenefits.setModifiedBy(strUserName);
								reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(reimbursementBenefitDetails);
								entityManager.flush();
								log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
							}
						}
					}
				}

			}
			else
			{
					/**
					 * Below code is added due to impact of refer to bill entry option.
					 * Assume a scenario , where intially add on benefits was selected and saved
					 * during bill entry submit. Again user refers the record to bill entry.
					 * This time he/she removes benefits. Then on submit the previously saved
					 * record should be marked as deleted one. For this purpose only
					 * the below code of setting deleted flag as "Y" is added.
					 * */
					updateBenefitsRecord("PC", reimbursement);
				
			}
			
			
			
				if(null != docAcknowledgement)
				{
					//Double amt = updatePAProvisionAndClaimStatus(docAcknowledgement,docAcknowledgement.getClaim(),rodDTO);
					
					reimbursement = updatePAProvisionAndClaimStatus(docAcknowledgement,docAcknowledgement.getClaim(),rodDTO,reimbursement,SHAConstants.BILL_ENTRY_PA);
					
					/*DBCalculationService dbCalculationService = new DBCalculationService();
					Double balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 0l ,null != masterValue ? masterValue.getKey() : 0l);*/
					//reimbursement.setCurrentProvisionAmt(amt);
				}
			
			/*else
			{
				
				*//***
				 * Coded added for updating the current provision amt
				 ***//*
				Double balanceSI = getBalanceSI(rodDTO);
				Double totalClaimedAmtInclBenefits = totalClaimedAmtForBenefits +totalClaimedAmt ;
			//	Double currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
				
				Double currentProvisionAmt = 0d;
			//	previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
	//			previousCurrentProvAmt = getPreviousProvisionAmtForBillEntry(rodDTO.getClaimDTO().getKey(),reimbursement);
				currentProvisionAmt =   calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
				else
				{
					currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmtInclBenefits , rodDTO);
				}
				
				
				*//**
				 * provison amount is update only add on benefits in Bill Entry stage
				 *//*
				if(null != balanceSI && null != currentProvisionAmt)
				{
					
					if((null != rodDTO.getDocumentDetails()
						.getAddOnBenefitsHospitalCash() && (rodDTO
						.getDocumentDetails().getAddOnBenefitsHospitalCash()))
						|| (null != rodDTO.getDocumentDetails()
								.getAddOnBenefitsPatientCare() && (rodDTO
										.getDocumentDetails().getAddOnBenefitsPatientCare())) ){
					reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, currentProvisionAmt));
					}
				}
			}
			*/
			//currentProvisionAmt += 
			/*if(balanceSI > currentProvisionAmt)
			{
				reimbursement.setCurrentProvisionAmt(balanceSI);
			}
			else
			{
				reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			}*/
			
			Boolean shouldSkipZMR = false;
			
			if(null != reimbursement.getKey())
			{
				rodDTO.setCurrentProvisionAmount(reimbursement.getCurrentProvisionAmt());
				String userName = rodDTO.getStrUserName();
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setBillEntryDate(new Timestamp(System.currentTimeMillis()));
				if(null != rodDTO && null != rodDTO.getTotalClaimedAmount())
				{
					reimbursement.setBillEntryAmt(rodDTO.getTotalClaimedAmount());
				}
				
				Claim claim = reimbursement.getClaim();
				shouldSkipZMR = shouldSkipZMR(claim);
				
				if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					shouldSkipZMR = false;
				}
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
				{
					
					if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
					{
					reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}					
					else
					{
					reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					}

				if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
						("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()) || ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag())) //---need to implement partialhospitallization
					{
					reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}
									
				
				}
				if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
				{
					if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
							("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) //---need to implement partialhospitallization
					{
						docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}	
					else
					{
					docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					}
				}
				
				reimbursement.setSkipZmrFlag(shouldSkipZMR ? "Y":"N");

				entityManager.merge(reimbursement);
				entityManager.flush();
				log.info("------Reimbursement------>"+reimbursement+"<------------");
				//entityManager.refresh(reimbursement);
			}
			Claim claimObj = getClaimByClaimKey(reimbursement.getClaim().getKey())   ;
			if(null != claimObj)
			{
				Status status1 = new Status();
				status1.setKey(ReferenceTable.BILL_ENTRY_STATUS_KEY);
				
				Stage stage1 = new Stage();
				stage1.setKey(ReferenceTable.BILL_ENTRY_STAGE_KEY);
				
//				claimObj.setStatus(status1);
//				claimObj.setStage(stage1);
				claimObj.setDataOfAdmission(reimbursement.getDateOfAdmission());
				claimObj.setDataOfDischarge(reimbursement.getDateOfDischarge());
				claimObj.setModifiedBy(strUserName);
				claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				if(SHAConstants.PA_LOB.equalsIgnoreCase(productType))
				{
					shouldSkipZMR = true;
					Boolean incidentFlag = null !=  rodDTO.getDocumentDetails().getAccidentOrDeath() ?  rodDTO.getDocumentDetails().getAccidentOrDeath() : null;
					Date incidenceDate = null !=  rodDTO.getDocumentDetails().getAccidentOrDeathDate() ?  rodDTO.getDocumentDetails().getAccidentOrDeathDate() : null;
					
					if(null !=  rodDTO.getDocumentDetails().getAccidentOrDeath() &&  rodDTO.getDocumentDetails().getAccidentOrDeath().equals(true)){
					claimObj.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
					}
					else
					{
						claimObj.setIncidenceFlag(SHAConstants.DEATH_FLAG);
					}
					
					if(null != incidenceDate){
						
						claimObj.setIncidenceDate(incidenceDate);
					}
					
					if(claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
					{
						
						if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
						{
						reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						}					
						else
						{
						reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
						}

					if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
							("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()) || ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag())) //---need to implement partialhospitallization
						{
						reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						}
										
					
					}
					if(claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
					{
						if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
								("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) //---need to implement partialhospitallization
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
				
				}
				
				if((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && rodDTO.getDocumentDetails().getHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) || (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
					//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				}
				
				entityManager.merge(claimObj);
				entityManager.flush();
				log.info("------Claim------>"+claimObj+"<------------");
			}
			
			
//			Boolean isFirstRODApproved = getStatusOfFirstROD(rodDTO.getClaimDTO().getKey(),reimbursement);
			
			
			//	submitPABillEntryTaskToBPM(rodDTO,claimObj,shouldSkipZMR);
			submitPABillEntryTaskToDB(rodDTO, claimObj, shouldSkipZMR);
				isBillEntrySubmitted = true;
			
			
			
			
			/*Double currentProvisionAmt = totalClaimedAmt + totalClaimedAmtForBenefits;
			
			Double currentProvisionAmt = rodDTO.getCurrentProvisionAmount() + totalClaimedAmtForBenefits;
			reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			if(null != reimbursement.getKey())
			{
				entityManager.merge(reimbursement);
				entityManager.flush();
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		
		return isBillEntrySubmitted;	
	
	}

	public DocumentDetails getDocumentDetailsBasedOnDocToken(String documentToken)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByDocumentToken");
		if(null != documentToken)
		{
			query = query.setParameter("documentToken", Long.valueOf(documentToken));
			List<DocumentDetails> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				entityManager.refresh(documentDetailsList.get(0));
				return documentDetailsList.get(0);
			}
		}
		return null;
	}
	
	public DocumentDetails getDocumentDetailsBasedOnDocType(String documentType)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByDocType");
		if(null != documentType)
		{
			query = query.setParameter("documentType",documentType);
			List<DocumentDetails> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				entityManager.refresh(documentDetailsList.get(0));
				return documentDetailsList.get(0);
			}
		}
		return null;
	}
	
	public List<DocumentDetails> getDocumentDetailsByIntimationNumber(String intimationNo,String docType){

		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoDocType");
		query = query.setParameter("intimationNumber",intimationNo);
		query = query.setParameter("documentType",docType);
		List<DocumentDetails> docList =(List<DocumentDetails>) query.getResultList();
		if(docList != null && !docList.isEmpty()) {
			return docList;
		}

		return null;
	}
	
	
	public Boolean saveBillEntryValues(UploadDocumentDTO uploadDocumentDTO)
	{
		CreateRODMapper rodMapper = CreateRODMapper.getInstance();
			try{
				if(null != uploadDocumentDTO)
				{
					Reimbursement reimbursement = getReimbursementObjectByKey(uploadDocumentDTO.getRodKey());
					RODDocumentSummary rodSummary = null;
					if(null != uploadDocumentDTO.getDocSummaryKey())
					{
						 
								RODDocumentSummary rodSummaryObj = getRODDocumentSummaryDetailsByKey(uploadDocumentDTO.getDocSummaryKey());
								 rodSummary = CreateRODMapper.getInstance()
											.getDocumentSummary(uploadDocumentDTO);
								 rodSummary.setCreatedBy(rodSummaryObj.getCreatedBy());
								 rodSummary.setCreatedDate(rodSummaryObj.getCreatedDate());
					}
					else
					{
						 rodSummary = CreateRODMapper.getInstance()
									.getDocumentSummary(uploadDocumentDTO);
						 rodSummary.setCreatedBy(uploadDocumentDTO.getStrUserName());
						 rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					}
				
				
				rodSummary.setReimbursement(reimbursement);
				rodSummary.setDeletedFlag("N");
				
				if (uploadDocumentDTO.getBillEntryDetailsDTO()
						.getZonalRemarks() != null) {
					rodSummary.setZonalRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getZonalRemarks());
				}
				if (uploadDocumentDTO.getBillEntryDetailsDTO()
						.getCorporateRemarks() != null) {
					rodSummary.setCorporateRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getCorporateRemarks());
				}
				if (uploadDocumentDTO.getBillEntryDetailsDTO()
						.getBillingRemarks() != null) {
					rodSummary.setBillingRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getBillingRemarks());
					uploadDocumentDTO.setBillingRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getBillingRemarks());
				}
				
				if (null != uploadDocumentDTO.getDocSummaryKey()) {
					
					rodSummary.setModifiedBy(uploadDocumentDTO.getStrUserName());
					rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.merge(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
				} else {
					
					rodSummary.setCreatedBy(uploadDocumentDTO.getStrUserName());
					rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					entityManager.refresh(rodSummary);
				}
				
				uploadDocumentDTO.setDocSummaryKey(rodSummary.getKey());

				List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
						.getBillEntryDetailList();
				if (null != billEntryDetailsList
						&& !billEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

						if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
								billEntryDetailsDTO.getBillNo())) {
							/*if(isQueryReceived){

							}*/
							//IMSSUPPOR-31032
							if(billEntryDetailsDTO!=null && billEntryDetailsDTO.getClassification()!=null){
														
							RODBillDetails  rodBillDetails = null;
							if(null != billEntryDetailsDTO.getBillDetailsKey())
							{
								 rodBillDetails = getBillEntryDetailsByKey( billEntryDetailsDTO.getBillDetailsKey());
								 
								//IMSSUPPOR-28331
                                 rodBillDetails = rodMapper
                                                        .getRODBillDetails(billEntryDetailsDTO);
                                 rodBillDetails.setKey( billEntryDetailsDTO.getBillDetailsKey());
							}
							else
							{
							 rodBillDetails = rodMapper
										.getRODBillDetails(billEntryDetailsDTO);
							}
							
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setReimbursementKey(reimbursement.getKey());
							rodBillDetails.setDeletedFlag("N");
							
							if(billEntryDetailsDTO.getRoomType() != null){
								rodBillDetails.setRoomType(billEntryDetailsDTO.getRoomType().getValue());
							}
							
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getZonalRemarks() != null) {
								rodSummary.setZonalRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getZonalRemarks());
							} else {
								if (billEntryDetailsDTO.getZonalRemarks() != null) {
									rodSummary.setZonalRemarks(billEntryDetailsDTO
											.getZonalRemarks());
								}
							}
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getCorporateRemarks() != null) {
								rodSummary.setCorporateRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getCorporateRemarks());
							} else {
								if (billEntryDetailsDTO.getCorporateRemarks() != null) {
									rodSummary.setCorporateRemarks(billEntryDetailsDTO
											.getCorporateRemarks());
								}
							}
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getBillingRemarks() != null) {
								rodSummary.setBillingRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getBillingRemarks());
							} else {
								if (billEntryDetailsDTO.getBillingRemarks() != null) {
									rodSummary.setBillingRemarks(billEntryDetailsDTO
											.getBillingRemarks());
								}
							}
							
							
							if(null != rodBillDetails.getKey())
							{
								//rodBillDetails.setM(strUserName);
								// modified date and time field is not available in bill details table.
								//rodBillDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(rodBillDetails);
								entityManager.flush();
								entityManager.clear();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							else
							{
								// created date and time field is not available in bill details table.
								entityManager.persist(rodBillDetails);
								entityManager.flush();
								entityManager.clear();
								log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							
							billEntryDetailsDTO.setBillDetailsKey(rodBillDetails.getKey());

						}
						}
					}
				}
				List<BillEntryDetailsDTO> deletedBillEntryDetailsList = uploadDocumentDTO
						.getDeletedBillList();
				if (null != deletedBillEntryDetailsList
						&& !deletedBillEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : deletedBillEntryDetailsList) {

						// if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(billEntryDetailsDTO.getBillNo()))
						{
							@SuppressWarnings("static-access")
							RODBillDetails rodBillDetails = rodMapper
									.getRODBillDetails(billEntryDetailsDTO);
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setDeletedFlag("Y");
							/*
							 * rodSummary.setZonalRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getZonalRemarks());
							 * rodSummary.setCorporateRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getCorporateRemarks());
							 * rodSummary.setBillingRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getBillingRemarks());
							 */
							/*
							 * entityManager.merge(rodSummary);
							 * entityManager.flush();
							 */

							if (rodBillDetails.getKey() != null) {
								entityManager.merge(rodBillDetails);
							} /*
							 * else { entityManager.persist(rodBillDetails); }
							 */
							log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							entityManager.flush();
							entityManager.clear();
						}
					}
				}
				
				/**
				 * Below method is added for enabling save bill entry
				 * option in bill entry pop up. Same is now resued in save also.
				 * */
				//saveBillEntryValues(uploadDocumentDTO,rodSummary);

			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Error occured while saving bill entry details"+e);
			e.printStackTrace();
			return false;
		}
	}

	/*private Reimbursement saveRODValues(ReceiptOfDocumentsDTO rodDTO) {

		CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		String ackNumber = docAck.getAcknowledgeNumber();

		
		DocAcknowledgement docAck1 = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		String globalAck = docAck1.getAcknowledgeNumber();
		
		Reimbursement reimbursement = null;
		Boolean isQueryReplyReceived = false;
		
		Boolean shouldSkipZMR = false;
		//DocAcknowledgement docAcknowledgement = null;
		
		//Added for current provision amt.
		Double totalClaimedAmt = 0d;
		
		Double previousCurrentProvAmt = 0d;
		Long reimbKey = null;
		Boolean isReconsideration = false;
		
		String username1 = rodDTO.getStrUserName();
		String userNameForDB1 = SHAUtils.getUserNameForDB(username1);
		
		//HumanTask humanTaskForROD = rodDTO.getHumanTask();
		Long rodKeyFromPayload = null;
*/
	
//	private Double getPreviousProvisionAmtForBillEntry(Long claimKey,Reimbursement currentReimbursement)
//	{
//		
//		Double provisionAmount = 0d;
//		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
//		query = query.setParameter("claimKey", claimKey);
//		List<Reimbursement> reimbursementList = query.getResultList();
//			
//		for (Reimbursement reimbursement : reimbursementList) {
//			if(currentReimbursement != reimbursement){
//				provisionAmount = provisionAmount + reimbursement.getCurrentProvisionAmt();
//			}
//		}
//		
//		return provisionAmount;
//		
//		
//	}
	
	
/*	public Boolean getStatusOfFirstROD(Long claimKey,Reimbursement currentReimbursement)
	{
		Boolean isFirstRODApproved = false;

		
		Boolean isSkipValidation = false;
		
		DocAcknowledgement docAcknowLedgement = currentReimbursement.getDocAcknowLedgement();
		
		Claim claim = currentReimbursement.getClaim();
		if(null != claim)
		{

			rodDTO.getDocumentDetails().setBankId(bankMaster.getKey());
		}
		
		if(null != rodDTO.getDocumentDetails().getBankId())
		{
			if(null != humanTaskForROD)

			Preauth preauth = getLatestPreauthForClaim(claim.getKey());
			if(null != preauth)

			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey()) && ("F").equalsIgnoreCase(preauth.getEnhancementType()))
				{
					isSkipValidation = true;
				}
<<<<<<< HEAD

		
=======
			}
			dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
			dataMap.put("filePath", rodDTO.getDocFilePath());
			dataMap.put("docType", rodDTO.getDocType());
			dataMap.put("docSources", SHAConstants.ROD_DOC_SOURCE);
			dataMap.put("createdBy", rodDTO.getStrUserName());
			String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);

			
			if(null != docToken)
			{
				rodDTO.setDocToken(docToken);
				//rodDTO.setIsletterGeneratedInROD(true);
>>>>>>> FHOwithproduction
			}
		
		if(null != rodDTO.getRodKeyFromPayload())
		{
			rodKeyFromPayload = rodDTO.getRodKeyFromPayload();
		}

			}
		}
		
		if(!isSkipValidation)
		{
		if(docAcknowLedgement != null &&(("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalisationFlag())||
				("Y").equalsIgnoreCase(docAcknowLedgement.getPartialHospitalisationFlag()) || ("Y").equalsIgnoreCase(docAcknowLedgement.getLumpsumAmountFlag()))){
			
			return !isFirstRODApproved;
			
		}
		
		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())  {
			for (Reimbursement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
				if(currentReimbursement.getKey() != reimbursement.getKey()) {
					if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()!=null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y") 
							|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null &&
							reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) {
						if((ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey()) || ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey()))
								|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey()))
										|| (ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey()))
										|| (ReferenceTable.FINANCIAL_SETTLED.equals(reimbursement.getStatus().getKey())) )
								{
							isFirstRODApproved  = true;
						}
						
					}
					
			     } else {
			    	 if(currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y")
			    			 || reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null &&
								reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) {
			    		 isFirstRODApproved = true;
			    	 }
			    	
			     }
			}
			
		} else {
			isFirstRODApproved = true;
		}
		}
		else
		{
			isFirstRODApproved = true;
		}
		return isFirstRODApproved;
		}*/
	
	
	public Boolean getStatusOfFirstRODForAckValidation(Long claimKey)
	{
		Boolean isFirstRODApproved = true;
		
		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			int iListSize = reimbursementList.size();
			if(iListSize >1)
			{
				Reimbursement reimbursement = reimbursementList.get(0);
				DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
				if(docAcknowLedgement != null &&(("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalisationFlag())||
						("Y").equalsIgnoreCase(docAcknowLedgement.getPartialHospitalisationFlag()))){
					return true;
				}
				if(!(ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())) && !ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey()))
				{
					isFirstRODApproved  = false;
				}
				else
				{
					isFirstRODApproved  = true;
				}
			}else{
			    	isFirstRODApproved = true;
			     }
		}
		else
		{
			isFirstRODApproved = true;
		}
		return isFirstRODApproved;
	}
	

	@SuppressWarnings({ "unchecked" })
	public List<MasBillDetailsType> getBillDetails(Long billCategory) {

		Query query = entityManager
				.createNamedQuery("MasBillDetailsType.findByBillClassification");
		query = query.setParameter("billClassificationKey", billCategory);

		List<MasBillDetailsType> billDetails = (List<MasBillDetailsType>) query
				.getResultList();

		return billDetails;

	}
	
	
	public List<RODBillDetails> getBillEntryDetails(Long billSummaryKey,EntityManager em){
		this.entityManager = em;
		return getBillEntryDetails(billSummaryKey);
		
	}
	

	@SuppressWarnings({ "unchecked" })
	public List<RODBillDetails> getBillEntryDetails(Long billDocumentSummaryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByRodDocumentSummaryKey");
		query = query.setParameter("summaryKey", billDocumentSummaryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (RODBillDetails rodBillDetails : billDetails) {
				entityManager.refresh(rodBillDetails);
			}
		}

		return billDetails;
	}
	
	public RODBillDetails getBillEntryDetailsByKey(Long billEntryDetailsKey)
	{
		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByKey");
		query = query.setParameter("primaryKey", billEntryDetailsKey);
		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		if(null != billDetails && !billDetails.isEmpty())
		{
				entityManager.refresh(billDetails.get(0));
				return billDetails.get(0);
		}
		return null;

	}

	public List<BillItemMapping> getBillItemMappingByDocSummaryKey(
			Long documentSummaryKey) {
		List<BillItemMapping> billItemMappingList = null;
		Query query = entityManager
				.createNamedQuery("BillItemMapping.findByRoomIcuRentId");
		query = query.setParameter("roomIcuRentId", documentSummaryKey);

		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			billItemMappingList = query.getResultList();
		}

		return billItemMappingList;
	}
	private String loadByPass(Reimbursement reimbursement, Claim claimObj, Boolean isQueryReplyReceived) {
		 String sendToWhere = ReferenceTable.NORMAL;
		 Double billEntryTotalAmt = 0d;
		/*------------------------------- Bypass to FA or Billing Start --------------------------------------------------*/
		if(claimObj.getClaimType() !=  null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
			Preauth latestPreauthByClaim = getLatestPreauthByClaim(claimObj.getKey());
			List<ClaimAmountDetails> claimAmountDetailsByPreauth = getClaimAmountDetailsByPreauth(latestPreauthByClaim.getKey());
			Double billValueAmount = 0d;
			for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsByPreauth) {
				billValueAmount += claimAmountDetails.getPaybleAmount();
			}
			// Upload the Cashless settlement bill manually.......
			if(!isQueryReplyReceived) {
				Map<String, String> uploadDocumentManually = SHAUtils.uploadDocumentManually(BPMClientContext.CASHLESS_SETTLEMENT_BILL_PATH);
				RODDocumentSummary dummyDocSummary = SHAUtils.getDummyDocSummary(uploadDocumentManually);
				SHAUtils.setClearMapStringValue(uploadDocumentManually);
				dummyDocSummary.setReimbursement(reimbursement);
				dummyDocSummary.setBillAmount(billValueAmount);
				if(dummyDocSummary.getKey() == null) {
					entityManager.persist(dummyDocSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+dummyDocSummary+"<------------");
				} else {
					entityManager.merge(dummyDocSummary);
					entityManager.flush();	
					log.info("------RODDocumentSummary------>"+dummyDocSummary+"<------------");
				}
				List<RODBillDetails> dummyBillDetailsForByPass = SHAUtils.getDummyBillDetailsForByPass(claimAmountDetailsByPreauth);
				for (RODBillDetails rodBillDetails : dummyBillDetailsForByPass) {
					/**
					 * The below coded was added to save the total bill entry amount
					 * in reimbursement table. The summation of item value entered
					 * by user will be saved in this column.
					 * */
					
					//R1006
					if(null != rodBillDetails.getClaimedAmountBills())
					{
						if(rodBillDetails.getBillCategory() != null && rodBillDetails.getBillCategory().getKey().equals(23L)){
							billEntryTotalAmt -= rodBillDetails.getClaimedAmountBills();
						}else{
							billEntryTotalAmt += rodBillDetails.getClaimedAmountBills();	
						}
					}
						//billEntryTotalAmt += rodBillDetails.getClaimedAmountBills();
					rodBillDetails.setRodDocumentSummaryKey(dummyDocSummary);
					rodBillDetails.setReimbursementKey(reimbursement.getKey());
					
					if(rodBillDetails.getKey() == null) {
						entityManager.persist(rodBillDetails);
						entityManager.flush();
						log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
					} else {
						entityManager.merge(rodBillDetails);
						entityManager.flush();	
						log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
					}
					entityManager.clear();
				}
			}
			
			if(SHAUtils.isDirectToBilling(latestPreauthByClaim, claimAmountDetailsByPreauth)) {
				sendToWhere = ReferenceTable.DIRECT_TO_BILLING;
			} else if(SHAUtils.isDirectToFinancial(latestPreauthByClaim, claimAmountDetailsByPreauth)) {
				sendToWhere = ReferenceTable.DIRECT_TO_FINANCIAL;
			}
			
			reimbursement = getProrataFlagFromProduct(claimObj, reimbursement);
			//added for prorata calcualtion value from preauth table
			if(claimObj.getIntimation().getPolicy().getProduct().getKey() != null && 
					(claimObj.getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
							|| claimObj.getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
             if(latestPreauthByClaim != null && latestPreauthByClaim.getProportionateFlag() != null){
            	 reimbursement.setProrataDeductionFlag(latestPreauthByClaim.getProportionateFlag());
             }
			}
			/**
			 * Setting the bill entry total amount calculated in
			 * above for loop will be set to reimbursement bill entry
			 * amount column.
			 * */
			reimbursement.setBillEntryAmt(billEntryTotalAmt);
			if(reimbursement.getKey() != null) {
				entityManager.merge(reimbursement);
				entityManager.flush();	
				log.info("------Reimbursement------>"+reimbursement+"<------------");
			}
		}
		return sendToWhere;
		/*------------------------------- Bypass to FA or Billing End --------------------------------------------------*/
	}

	@SuppressWarnings({ "unchecked" })
	public List<RODBillDetails> getBillEntryDetails(
			Long billDocumentSummaryKey, Long billCategoryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByRODDocSummaryAndCategoryId");
		query = query.setParameter("summaryKey", billDocumentSummaryKey)
				.setParameter("billCategoryKey", billCategoryKey);
		// query = query.setParameter("billCategoryKey", billCategoryKey)

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();

		return billDetails;

	}

	public List<MasIrdaLevel1> getIrdaLevelOneList() {

		Query query = entityManager.createNamedQuery("MasIrdaLevel1.findAll");

		List<MasIrdaLevel1> billDetails = (List<MasIrdaLevel1>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<PreHospitalisation> getPreHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("PreHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<PreHospitalisation> billDetails = (List<PreHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (PreHospitalisation preHospitalisation : billDetails) {
				entityManager.refresh(preHospitalisation);
			}
		}

		return billDetails;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<BillingPreHospitalisation> getBillingPreHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("BillingPreHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<BillingPreHospitalisation> billDetails = (List<BillingPreHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (BillingPreHospitalisation preHospitalisation : billDetails) {
				entityManager.refresh(preHospitalisation);
			}
		}

		return billDetails;

	}

	public List<RODDocumentSummary> getBillDetailsByRodKey(Long rodKey,EntityManager em) {
		this.entityManager = em;
		return getBillDetailsByRodKey(rodKey);
	}


	@SuppressWarnings("unchecked")
	public List<RODDocumentSummary> getBillDetailsByRodKey(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);

		List<RODDocumentSummary> billDetails = (List<RODDocumentSummary>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<RODBillDetails> getBilldetailsByDocumentSummayKey(
			Long summaryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByRodDocumentSummaryKey");
		query = query.setParameter("summaryKey", summaryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		
		for (RODBillDetails rodBillDetails : billDetails) {
			entityManager.refresh(rodBillDetails);
		}

		return billDetails;

	}
	
	

	@SuppressWarnings("unchecked")
	public List<PostHospitalisation> getPostHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("PostHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<PostHospitalisation> billDetails = (List<PostHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (PostHospitalisation postHospitalisation : billDetails) {
				entityManager.refresh(postHospitalisation);
			}
		}

		return billDetails;


	}
	
	@SuppressWarnings("unchecked")
	public List<BillingPostHospitalisation> getBillingPostHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("BillingPostHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<BillingPostHospitalisation> billDetails = (List<BillingPostHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (BillingPostHospitalisation postHospitalisation : billDetails) {
				entityManager.refresh(postHospitalisation);
			}
		}

		return billDetails;

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
	
	@SuppressWarnings("unchecked")
	public List<BillingHospitalisation> getBillingHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("BillingHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<BillingHospitalisation> billDetails = (List<BillingHospitalisation>) query
				.getResultList();

		for (BillingHospitalisation hospitalisation : billDetails) {
			entityManager.refresh(hospitalisation);
		}

		return billDetails;


	}
	
	

	@SuppressWarnings("unchecked")
	public List<Hospitalisation> getHospitalisationListOrderByItemNumber(
			Long rodKey) {

		Query query = entityManager
				.createNamedQuery("Hospitalisation.findByReimbursementOrderByItemNo");
		query = query.setParameter("reimbursementKey", rodKey);

		List<Hospitalisation> billDetails = (List<Hospitalisation>) query
				.getResultList();

		for (Hospitalisation hospitalisation : billDetails) {
			entityManager.refresh(hospitalisation);
		}

		return billDetails;

	}
	
	@SuppressWarnings("unchecked")
	public List<BillingHospitalisation> getBillingHospitalisationListOrderByItemNumber(
			Long rodKey) {

		Query query = entityManager
				.createNamedQuery("BillingHospitalisation.findByReimbursementOrderByItemNo");
		query = query.setParameter("reimbursementKey", rodKey);

		List<BillingHospitalisation> billDetails = (List<BillingHospitalisation>) query
				.getResultList();

		for (BillingHospitalisation hospitalisation : billDetails) {
			entityManager.refresh(hospitalisation);
		}

		return billDetails;

	}
	private void saveBenefitsAndCoverValues(ReceiptOfDocumentsDTO rodDTO,
			DocAcknowledgement docAck, Reimbursement reimbursement) {
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<AddOnCoversTableDTO> addOnCoversProcedureList = null;
		List<AddOnCoversTableDTO> optionalCoversProcedureList = null;
		
		if(null != rodDTO && null != rodDTO.getClaimDTO().getNewIntimationDto() && null != rodDTO.getClaimDTO().getNewIntimationDto().getPolicy() &&
				null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()&&
				!(ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))){		
		
			if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				addOnCoversProcedureList = dbCalculationService.getClaimCoverValues(
						SHAConstants.JET_ADDITIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
						docAck.getClaim().getIntimation().getInsured().getKey());
					
					 optionalCoversProcedureList = dbCalculationService.getClaimCoverValues(
							SHAConstants.JET_OPTIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
							docAck.getClaim().getIntimation().getInsured().getKey());
			}else{
				addOnCoversProcedureList = dbCalculationService.getClaimCoverValues(
						SHAConstants.ADDITIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
						docAck.getClaim().getIntimation().getInsured().getKey());
					
					 optionalCoversProcedureList = dbCalculationService.getClaimCoverValues(
							SHAConstants.OPTIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
							docAck.getClaim().getIntimation().getInsured().getKey());
			}
			
		}
		else
		{			
			 addOnCoversProcedureList = dbCalculationService.getClaimCoverValues(
					SHAConstants.GPA_ADDITIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
					docAck.getClaim().getIntimation().getInsured().getKey());
			 
			 optionalCoversProcedureList = dbCalculationService.getClaimCoverValues(
						SHAConstants.GPA_OPTIONAL_COVER, docAck.getClaim().getIntimation().getPolicy().getProduct().getKey(),
						docAck.getClaim().getIntimation().getInsured().getKey());
		}
		
		
		
		ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO.getReconsiderRODdto();
		
		RODQueryDetailsDTO rodQueryDetailsDTOObj = rodDTO.getRodqueryDTO();		
		
		
		List<AddOnCoversTableDTO> paAddOnCovers = rodDTO.getDocumentDetails().getAddOnCoversList();
				
		if(null == paAddOnCovers || (null != paAddOnCovers && paAddOnCovers.isEmpty()))
		{
			if (null != reconsiderDTO
					&& null != reconsiderDTO.getRodKey())
			{
				paAddOnCovers = getAddOnCoversValueBasedOnROD(reconsiderDTO.getRodKey());
			}
			else if (null != rodQueryDetailsDTOObj
					&& null != rodQueryDetailsDTOObj.getReimbursementKey())
			{
				paAddOnCovers = getAddOnCoversValueBasedOnROD(rodQueryDetailsDTOObj.getReimbursementKey());
			}
		}
				
				if(null != paAddOnCovers && !paAddOnCovers.isEmpty() )
				{
				
					for (AddOnCoversTableDTO addOnCoversTableDTO : paAddOnCovers) {
						PAAdditionalCovers paAdditionalCovers = null;
						if(null != addOnCoversTableDTO.getCoverKey())
						{
							paAdditionalCovers = getAdditionalCoversByKey(addOnCoversTableDTO.getCoverKey());
							if(null == paAdditionalCovers)
							{
								paAdditionalCovers = new PAAdditionalCovers();
							}
						}
						else
						{
							 paAdditionalCovers = new PAAdditionalCovers();
						}
						
					
						paAdditionalCovers.setAcknowledgementKey(docAck.getKey());				
						paAdditionalCovers.setRodKey(reimbursement.getKey());
						paAdditionalCovers.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
						paAdditionalCovers.setCoverId(addOnCoversTableDTO.getCovers().getId());
						paAdditionalCovers.setClaimedAmount(addOnCoversTableDTO.getClaimedAmount());
						String userName = rodDTO.getStrUserName();
						userName = SHAUtils.getUserNameForDB(userName);
						paAdditionalCovers.setModifiedBy(userName);				
						paAdditionalCovers.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						paAdditionalCovers.setCreatedBy(userName);
						paAdditionalCovers.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						paAdditionalCovers.setKey(addOnCoversTableDTO.getCoverKey());
						paAdditionalCovers.setClaimKey(rodDTO.getClaimDTO().getKey());
						
						if(null != addOnCoversProcedureList && !addOnCoversProcedureList.isEmpty())
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
						}
						
						if(null != paAdditionalCovers.getKey())
						{
						entityManager.merge(paAdditionalCovers);
						entityManager.flush();
						}
						else
						{
							paAdditionalCovers.setDeletedFlag("N");
							entityManager.persist(paAdditionalCovers);
							entityManager.flush();
						}
				}
					
				}				
				
			
				
				List<AddOnCoversTableDTO> paOptionalCover = rodDTO.getDocumentDetails().getOptionalCoversList();
				
				if(null == paOptionalCover || (null != paOptionalCover && paOptionalCover.isEmpty()))
				{
					if (null != reconsiderDTO
							&& null != reconsiderDTO.getRodKey())
					{
						paOptionalCover = getOpitionalCoversValueBasedOnROD(reconsiderDTO.getRodKey());
					}
					else if (null != rodQueryDetailsDTOObj
							&& null != rodQueryDetailsDTOObj.getReimbursementKey())
					{
						paOptionalCover = getOpitionalCoversValueBasedOnROD(rodQueryDetailsDTOObj.getReimbursementKey());
					}
				}
				
				if(null != paOptionalCover && !paOptionalCover.isEmpty() )
				{
				
					for (AddOnCoversTableDTO addOnCoversTableDTO : paOptionalCover) {
						PAOptionalCover paOptionalCovers = null;
						if(null != addOnCoversTableDTO.getCoverKey())
						{
							paOptionalCovers = getOptionalCoversByKey(addOnCoversTableDTO.getCoverKey());
							if(null == paOptionalCovers)
							{
								paOptionalCovers = new PAOptionalCover();
							}
						}
						else
						{
							paOptionalCovers = new PAOptionalCover();
						}
						paOptionalCovers.setAcknowledgementKey(docAck.getKey());				
						paOptionalCovers.setRodKey(reimbursement.getKey());
						paOptionalCovers.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
						paOptionalCovers.setCoverId(addOnCoversTableDTO.getOptionalCover().getId());
						paOptionalCovers.setClaimedAmount(addOnCoversTableDTO.getClaimedAmount());
						
						//CR2019100 Deduction for  PA Medical Extention
						if(ReferenceTable.getMedicalExtentionKeys().containsKey((addOnCoversTableDTO.getOptionalCover().getId()))) {
							paOptionalCovers.setTotalClaimAmt(addOnCoversTableDTO.getClaimedAmount());
						}
						
						String userName = rodDTO.getStrUserName();
						userName = SHAUtils.getUserNameForDB(userName);
						paOptionalCovers.setModifiedBy(userName);				
						paOptionalCovers.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						paOptionalCovers.setCreatedBy(userName);
						paOptionalCovers.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						paOptionalCovers.setKey(addOnCoversTableDTO.getCoverKey());
						paOptionalCovers.setClaimKey(rodDTO.getClaimDTO().getKey());
						
						
						if(null != optionalCoversProcedureList && !optionalCoversProcedureList.isEmpty())
						{
							for (AddOnCoversTableDTO optionalOnCoversProcObj : optionalCoversProcedureList) {
								
								if(null != addOnCoversTableDTO.getOptionalCover() && optionalOnCoversProcObj.getCoverId().equals(addOnCoversTableDTO.getOptionalCover().getId()))
								{
									if(null != addOnCoversTableDTO.getClaimedAmount())
									{
										Double ProvisionAmt = Math.min(addOnCoversTableDTO.getClaimedAmount() , optionalOnCoversProcObj.getClaimedAmount());
										paOptionalCovers.setProvisionAmount(ProvisionAmt);
									}
									break;
								
								}
								
							}
						}
						
						if(null != paOptionalCovers.getKey())
						{
							entityManager.merge(paOptionalCovers);
							entityManager.flush();
						}
						else
						{
							paOptionalCovers.setDeletedFlag("N");
							entityManager.persist(paOptionalCovers);
							entityManager.flush();
							
						}
				}
					
				}
							
				
				
				if(null != reimbursement)
				{	
					if(null != reimbursement.getBenefitsId() && null != reimbursement.getBenefitsId().getKey())
					{
					List<PABenefitsDTO> paBenefitsProcedureDTOList = dbCalculationService.getBenefitCoverValues(docAck.getClaim().getIntimation().getInsured().getKey()  , reimbursement.getBenefitsId().getKey());
					
					if( null != paBenefitsProcedureDTOList && !paBenefitsProcedureDTOList.isEmpty())
						{	
						PABenefitsCovers benefitCovers = null;
							for (PABenefitsDTO paBenefitsDTOObj : paBenefitsProcedureDTOList){						
					
							benefitCovers = getBenefitCoversByRodKey(reimbursement.getKey());					
						
							MasPAClaimBenefitsCover coverId = getPABenefitsMasterValueBySubCoverKey(paBenefitsDTOObj.getBenefitsId());
								if(null != coverId && null != reimbursement.getBenefitsId().getKey() && (coverId.getBenefitsId().equals(reimbursement.getBenefitsId().getKey())))
									{
										if(null != benefitCovers)
										{
										benefitCovers.setBenefitsId(reimbursement.getBenefitsId());
										benefitCovers.setCoverId(coverId);
										}
									}
						}
						if(null != benefitCovers && null != benefitCovers.getKey())
						{
							entityManager.merge(benefitCovers);
							entityManager.flush();
						}
					}
					}
					
				}
				
				
				
				List<AddOnCoversTableDTO> addOnCoversDeletedList = rodDTO.getDocumentDetails().getAddOnCoversDeletedList();
				if(null != addOnCoversDeletedList && !addOnCoversDeletedList.isEmpty())
				{
					for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversDeletedList) {
						if(null != addOnCoversTableDTO.getCoverKey())
						{
							PAAdditionalCovers additionalCovers = getPAdditionalCoversByKey(addOnCoversTableDTO.getCoverKey());
							if(null != additionalCovers)
							{
								additionalCovers.setDeletedFlag("Y");
								/**
								 * In case of deletion, the provision should
								 * get reset. Hence while saving provision
								 * amount is set 0.
								 * 
								 * **/
								additionalCovers.setProvisionAmount(0d);
								if(null != additionalCovers.getKey())
								{
									entityManager.merge(additionalCovers);
									entityManager.flush();
								}
							}
						}
					}
				}
				
				
				
				List<AddOnCoversTableDTO> optionalCoversDeletedList = rodDTO.getDocumentDetails().getOptionalCoversDeletedList();
				if(null != optionalCoversDeletedList && !optionalCoversDeletedList.isEmpty())
				{
					for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversDeletedList) {
						if(null != addOnCoversTableDTO.getCoverKey())
						{
							PAOptionalCover paOptionalCoverObj = getPAOptionalCoversByKey(addOnCoversTableDTO.getCoverKey());
							if(null != paOptionalCover)
							{
								paOptionalCoverObj.setDeletedFlag("Y");
								/**
								 * In case of deletion, the provision should
								 * get reset. Hence while saving provision
								 * amount is set 0.
								 * 
								 * **/
								paOptionalCoverObj.setProvisionAmount(0d);
								if(null != paOptionalCoverObj.getKey())
								{
									entityManager.merge(paOptionalCoverObj);
									entityManager.flush();
								}
							}
						}
					}
				}
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
	
	public BankMaster validateIFSCCode(String ifscCode)
	{
		BankMaster bankMaster = null;
		if(null != ifscCode && !("").equalsIgnoreCase(ifscCode))
		{
			 bankMaster = getBankDetails(ifscCode);
			if(null != bankMaster && null != bankMaster.getKey())
			{
				return bankMaster;
			}
		}
		return  bankMaster;
	}
	
	public BankMaster getBankDetails (String ifscCode)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> masBank = (List<BankMaster>)query.getResultList();
		if(masBank != null && ! masBank.isEmpty()){
			return masBank.get(0);
		}

		return null;
	}
	
	public Product getProrataForProduct(Long productId)
	{
	//	Product a_mastersValue = new MastersValue();
		//if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("Product.findByKey");
			query = query.setParameter("key", productId);
			List<Product> productValList = query.getResultList();
			Product product = null;
			if(null != productValList && !productValList.isEmpty())
			{
				for (Product productValue : productValList)
				{
					product = productValList.get(0);
				}
			}
				
		//}

		return product;
	}
	
	private Reimbursement getProrataFlagFromProduct(Claim claim, Reimbursement reimbursement)
	{
		Product product = getProrataForProduct(claim.getIntimation().getPolicy().getProduct().getKey());
		if(null != product)
		{
			reimbursement.setProrataDeductionFlag(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			reimbursement.setPackageAvailableFlag(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
		if(product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_PRODUCT_CODE) || product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE)){
			DBCalculationService dbCalculationService = new DBCalculationService();
			Double sumInsured =0d;
			 sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(claim.getIntimation().getPolicy().getKey(),claim.getIntimation().getInsured().getKey(),claim.getIntimation().getPolicy().getSectionCode());
			MasRoomRentLimit gmcProrataFlag = getMasRoomRentLimitbySuminsured(claim.getIntimation().getPolicy().getKey(),sumInsured);
			if(gmcProrataFlag != null && gmcProrataFlag.getProportionateFlag() != null){
				reimbursement.setProrataDeductionFlag( gmcProrataFlag.getProportionateFlag() != null ? gmcProrataFlag.getProportionateFlag() : "N");	
			}else {
				reimbursement.setProrataDeductionFlag("N");	
			}
		
		}
		}
		return reimbursement;
	}

	private String loadByPass(Reimbursement reimbursement, Claim claimObj, Boolean isQueryReplyReceived,String productType) {
		 String sendToWhere = ReferenceTable.NORMAL;
		 Double billEntryTotalAmt = 0d;
		/*------------------------------- Bypass to FA or Billing Start --------------------------------------------------*/
		if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && claimObj.getClaimType() !=  null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
			Preauth latestPreauthByClaim = getLatestPreauthByClaim(claimObj.getKey());
			List<ClaimAmountDetails> claimAmountDetailsByPreauth = getClaimAmountDetailsByPreauth(latestPreauthByClaim.getKey());
			Double billValueAmount = 0d;
			for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsByPreauth) {
				billValueAmount += claimAmountDetails.getPaybleAmount();
			}
			// Upload the Cashless settlement bill manually.......
			if(!isQueryReplyReceived) {
				Map<String, String> uploadDocumentManually = SHAUtils.uploadDocumentManually(BPMClientContext.CASHLESS_SETTLEMENT_BILL_PATH);
				RODDocumentSummary dummyDocSummary = SHAUtils.getDummyDocSummary(uploadDocumentManually);
				SHAUtils.setClearMapStringValue(uploadDocumentManually);
				if(SHAConstants.PA_LOB.equalsIgnoreCase(productType))
				{
					MastersValue value = new MastersValue();
					value.setKey(ReferenceTable.PA_HEALTH_CASHLESS_SETTLEMENT_BILL_KEY);
					dummyDocSummary.setFileType(value);
				}
				dummyDocSummary.setReimbursement(reimbursement);
				dummyDocSummary.setBillAmount(billValueAmount);
				if(dummyDocSummary.getKey() == null) {
					entityManager.persist(dummyDocSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+dummyDocSummary+"<------------");
				} else {
					entityManager.merge(dummyDocSummary);
					entityManager.flush();	
					log.info("------RODDocumentSummary------>"+dummyDocSummary+"<------------");
				}
				List<RODBillDetails> dummyBillDetailsForByPass = SHAUtils.getDummyBillDetailsForByPass(claimAmountDetailsByPreauth);
				for (RODBillDetails rodBillDetails : dummyBillDetailsForByPass) {
					/**
					 * The below coded was added to save the total bill entry amount
					 * in reimbursement table. The summation of item value entered
					 * by user will be saved in this column.
					 * */
					if(null != rodBillDetails.getClaimedAmountBills()){
						if(rodBillDetails.getBillCategory() != null && rodBillDetails.getBillCategory().getKey().equals(23L)){
							billEntryTotalAmt -= rodBillDetails.getClaimedAmountBills();
						}else{
							billEntryTotalAmt += rodBillDetails.getClaimedAmountBills();	
						}
						
					}
						
					rodBillDetails.setRodDocumentSummaryKey(dummyDocSummary);
					rodBillDetails.setReimbursementKey(reimbursement.getKey());
					
					if(rodBillDetails.getKey() == null) {
						entityManager.persist(rodBillDetails);
						entityManager.flush();
						log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
					} else {
						entityManager.merge(rodBillDetails);
						entityManager.flush();	
						log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
					}
				}
			}
			
			if(SHAUtils.isDirectToBilling(latestPreauthByClaim, claimAmountDetailsByPreauth)) {
				sendToWhere = ReferenceTable.DIRECT_TO_BILLING;
			} else if(SHAUtils.isDirectToFinancial(latestPreauthByClaim, claimAmountDetailsByPreauth)) {
				sendToWhere = ReferenceTable.DIRECT_TO_FINANCIAL;
			}
			
			reimbursement = getProrataFlagFromProduct(claimObj, reimbursement);
			//added for prorata calcualtion value from preauth table
			if(claimObj.getIntimation().getPolicy().getProduct().getKey() != null && 
					(claimObj.getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
					|| claimObj.getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
             if(latestPreauthByClaim != null && latestPreauthByClaim.getProportionateFlag() != null){
            	 reimbursement.setProrataDeductionFlag(latestPreauthByClaim.getProportionateFlag());
             }
			}
			/**
			 * Setting the bill entry total amount calculated in
			 * above for loop will be set to reimbursement bill entry
			 * amount column.
			 * */
			reimbursement.setBillEntryAmt(billEntryTotalAmt);
			if(reimbursement.getKey() != null) {
				entityManager.merge(reimbursement);
				entityManager.flush();	
				log.info("------Reimbursement------>"+reimbursement+"<------------");
			}
		}
		return sendToWhere;
		/*------------------------------- Bypass to FA or Billing End --------------------------------------------------*/
	}

	private Reimbursement getCopyFromPreauth(ReceiptOfDocumentsDTO rodDTO,
			CreateRODMapper createRODMapper, DocAcknowledgement docAck,
			String userNameForDB1, Long rodKeyForCancelROD, Claim claimObj) {
		Reimbursement reimbursement;
		reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
		
		if(rodDTO.getDocumentDetails().getPatientStatus() != null
				&& rodDTO.getDocumentDetails().getPatientStatus().getId() != null
				&& (ReferenceTable.PATIENT_STATUS_ADMITTED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
								|| ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId())
								|| ReferenceTable.getNewPatientStatusKeys().containsKey(rodDTO.getDocumentDetails().getPatientStatus().getId()))) {
			savePayementDetails(reimbursement, rodDTO.getDocumentDetails(), claimObj);
		}	
		
		setReimbursementValues(reimbursement, rodDTO.getPreauthDTO(),rodDTO);
		ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();
		reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());
		
		/*for bancs*/
		if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			
			reimbursement.setCatastrophicLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		
		}
		
		if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			reimbursement.setNatureOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNatureOfLoss().getId());
			
		}
		
		if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
				
			reimbursement.setCauseOfLoss(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getCauseOfLoss().getId());
			
		}
		
		if(null != rodDTO.getDocumentDetails().getInsuredPatientName())
		{
			
			if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				
				if(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient() != null && 
						rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() != null){
					reimbursement.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient());
				}else{
					Insured insured = getInsuredByPolicyAndInsuredName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
					reimbursement.setInsuredKey(insured);
				}
				
			}else{
				Insured insured = getInsuredByPolicyAndInsuredName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
				reimbursement.setInsuredKey(insured);
			}
			
			
		}
		Hospitals hospital = getHospitalByName(rodDTO.getDocumentDetails().getHospitalName());
		if(null != hospital)
			reimbursement.setHospitalId(hospital.getKey());
		
		
		reimbursement.setActiveStatus(1l);
		reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
				.getKey()));

		reimbursement.setRodNumber(rodDTO.getDocumentDetails()
				.getRodNumber());
		reimbursement.setDocAcknowLedgement(docAck);
		//reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
//		String strUserName = rodDTO.getStrUserName();
//		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//		reimbursement.setCreatedBy(userNameForDB);
//		reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		if(rodKeyForCancelROD != null) {
			reimbursement.setKey(rodKeyForCancelROD);
		}
//		if(reimbursement.getKey() != null) {
//			entityManager.merge(reimbursement);
//		} else {
//			entityManager.persist(reimbursement);
//		}
//		entityManager.flush();
		log.info("------Reimbursement------>"+reimbursement+"<------------");
		
		
		reimbursement.setDocAcknowLedgement(docAck);
		//if(null != rodDTO.getDocumentDetails().getInsuredPatientName())
		{
			
			if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				
				if(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient() != null && 
						rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() != null){
					reimbursement.setInsuredKey(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient());
				}else if(null != rodDTO.getDocumentDetails().getInsuredPatientName()){
					Insured insured = getInsuredByPolicyAndInsuredName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
					reimbursement.setInsuredKey(insured);
				}
				
			}else{
				if(null != rodDTO.getDocumentDetails().getInsuredPatientName()){
					Insured insured = getInsuredByPolicyAndInsuredName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
					reimbursement.setInsuredKey(insured);
					}
			}
			
			
		}
		
		if(reimbursement!=null){
			reimbursement.setFinancialApprovalRemarks(null);
		}
		
		if(rodKeyForCancelROD != null) {
			reimbursement.setKey(rodKeyForCancelROD);
		}
		if(null != hospital)
			reimbursement.setHospitalId(hospital.getKey());
		if(reimbursement.getKey() != null) {
			
			reimbursement.setModifiedBy(userNameForDB1);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setApprovedAmount(0d);
			reimbursement.setBillingApprovedAmount(0d);
			reimbursement.setFinancialApprovedAmount(0d);
//			entityManager.merge(reimbursement);
			
		} else {
			
			reimbursement.setCreatedBy(userNameForDB1);
			reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setApprovedAmount(0d);
			reimbursement.setBillingApprovedAmount(0d);
			reimbursement.setFinancialApprovedAmount(0d);
//			entityManager.persist(reimbursement);
		}
//		entityManager.flush();
		
		ClaimDto claimDTO = rodDTO.getClaimDTO();
		rodDTO.getPreauthDTO().setClaimDTO(claimDTO);
		
		
		reimbursement = savePreauthValues(rodDTO.getPreauthDTO(),reimbursement, reimbursementMapper, true,
				true,rodDTO.getStrUserName(), rodKeyForCancelROD, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
		
		
		
			Product product = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
					.getProduct();
			if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey())
					 && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
				Preauth preauth = getLatestPreauthForClaim(reimbursement.getClaim().getKey());
				if(preauth != null){
					
//					List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsList(preauth.getKey());
						List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsDTO(preauth.getKey());
						
						for (UpdateOtherClaimDetailDTO updateOtherClaimDetailDTO : updateOtherClaimDetailsList) {
							updateOtherClaimDetailDTO.setCashlessKey(null);
							updateOtherClaimDetailDTO.setKey(null);
						}
					
					if(updateOtherClaimDetailsList != null){
					
						if(!updateOtherClaimDetailsList.isEmpty()){
							List<UpdateOtherClaimDetails> updateOtherClaimDetails = PreauthMapper.getInstance().getUpdateOtherClaimDetails(updateOtherClaimDetailsList);
							for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
								updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
								updateOtherClaimDetails2.setClaimKey(reimbursement.getClaim().getKey());
								updateOtherClaimDetails2.setStage(reimbursement.getStage());
//								updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
								updateOtherClaimDetails2.setIntimationKey(reimbursement.getClaim().getIntimation().getKey());
								updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
								updateOtherClaimDetails2.setClaimType(reimbursement.getClaim().getClaimType().getValue());
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
					}
				
			}
		
		log.info("------Reimbursement KEY FROM CREATE ROD ------>"+((reimbursement != null && reimbursement.getKey() != null) ? reimbursement.getKey().toString() : "")+"<------------");
		return reimbursement;
	}
	
	public void revertRODData(Long rodKey, String userName)
	{
		DocAcknowledgement docAck = getLatestDocAcknowledgement(rodKey);
		if(null != docAck)
		{
			Reimbursement reimb =  getReimbursementObjectByKey(rodKey);
			reimb.setDocAcknowLedgement(docAck);

			reimb.setModifiedBy(userName);
			reimb.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimb);
			entityManager.flush();
			log.info("------Reimbursement------>"+reimb+"<------------");
		}
		
	}
	
	public Double getBalanceSI(ReceiptOfDocumentsDTO rodDTO)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Double balanceSI = 0d;
		Long policyKey = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey();
		if(null != rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId())
		{
			if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				//sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(),rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getSectionCode());
				balanceSI = dbCalculationService.getBalanceSIForGMC(policyKey, rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(), rodDTO.getClaimDTO().getKey());
			}else{
				sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()),
						policyKey,rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getLopFlag());	
				Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey , rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey() , rodDTO.getClaimDTO().getKey(),sumInsured,rodDTO.getClaimDTO().getNewIntimationDto().getKey());
				balanceSI = (Double)balanceSIMap.get(SHAConstants.CURRENT_BALANCE_SI);
			}
			
		}
		
		return balanceSI;
		
		
	}

	public void savePayementDetails(Reimbursement reimbursement,
			DocumentDetailsDTO docsDTO, Claim claimObj) {

		if (docsDTO != null) {
			reimbursement.setPaymentModeId(docsDTO.getPaymentModeFlag());
			reimbursement.setPayeeEmailId(docsDTO.getEmailId());
			reimbursement.setPanNumber(docsDTO.getPanNo());
			reimbursement.setAccountNumber(docsDTO.getAccountNo());
			reimbursement.setNameAsPerBankAccount(docsDTO.getNameAsPerBank());
			reimbursement.setAccountType(docsDTO.getAccountType());
			reimbursement.setAccountPreference(docsDTO.getAccountPreference());
			reimbursement.setBankId(docsDTO.getBankId());
			reimbursement.setPayableAt(docsDTO.getPayableAt());
			if(docsDTO.getPayeeName() != null) {
				reimbursement.setPayeeName(docsDTO.getPayeeName().getValue());
			}
			
		}
//		// If it is bypass then create rod stage we have to set the prorata flag from the product.
//	
//		if(claimObj != null && claimObj.getClaimType() !=  null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && docsDTO.getHospitalization() != null && docsDTO.getHospitalization()) {
//			reimbursement = getProrataFlagFromProduct(claimObj, reimbursement);
//		}
	}

	/*public Reimbursement savePreauthValues(PreauthDTO preauthDTO, Reimbursement reimbursement, ZonalMedicalReviewMapper reimbursementMapper,
			Boolean isZonalReview, Boolean isCashless,String userName, Long rodKeyForCancelROD, Boolean isHospitalizationRepeat) {
		
		
		if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus() != null && 
				preauthDTO.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_ADMITTED)){
			MastersValue master = getMaster(ReferenceTable.PATIENT_STATUS_DISCHARGED);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(master.getKey());
			selectValue.setValue(master.getValue());
			preauthDTO.getPreauthDataExtractionDetails().setPatientStatus(selectValue);
		}
		
//		reimbursement = reimbursementMapper
//				.getReimbursement(preauthDTO);
		
		if(reimbursement.getPatientStatus() != null) {
			MastersValue masterValue = new MastersValue();
			masterValue.setKey(reimbursement.getPatientStatus().getKey());
			masterValue.setValue(reimbursement.getPatientStatus().getValue());
			if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_ADMITTED)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_ADMITTED_REIMB);
			} else if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_DECEASED)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB);
			}
			reimbursement.setPatientStatus(masterValue);
		}
		
		reimbursement.setActiveStatus(1l);
		Claim searchByClaimKey2 = searchByClaimKey(preauthDTO.getClaimKey());
		reimbursement.setClaim(searchByClaimKey2);
	//	reimbursement.setCurrentProvisionAmt(preauthDTO.getC);

		Claim currentClaim = null;

		if (reimbursement.getClaim() != null) {
			currentClaim = searchByClaimKey(reimbursement.getClaim().getKey());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
			entityManager.merge(currentClaim);
			entityManager.flush();
			log.info("------Claim------>"+currentClaim+"<------------");
		}

		if (preauthDTO.getPreauthPreviousClaimsDetails()
				.getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO
					.getPreauthPreviousClaimsDetails()
					.getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			log.info("------Claim------>"+currentClaim+"<------------");
		}

		
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) { String processflag =
		 * "A";
		 * if(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() !=
		 * null &&
		 * preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt
		 * () != null &&
		 * preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() >
		 * preauthDTO
		 * .getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()) {
		 * processflag = "R"; } reimbursement.setApprovedAmount(preauthDTO.
		 * getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()); }
		 
		if(preauthDTO
				.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			reimbursement.setAutomaticRestoration(preauthDTO
					.getPreauthDataExtractionDetails().getAutoRestoration()
					.toLowerCase().contains("not") ? "N" : "Y");
		} else {
			reimbursement.setAutomaticRestoration("Y");
		}
	
		*//**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE during
		 * policy refractoring activity. Below code is added for inserting value
		 * in the insured key column.
		 * *//*
		// reimbursement.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		
		 * if(preauthDTO.getKey() != null) { entityManager.merge(reimbursement);
		 * } else
		 
		reimbursement.setOtherInsurerApplicableFlag(null);
		if(null !=  rodKeyForCancelROD) {
			reimbursement.setKey(rodKeyForCancelROD);
		}
		if (!isCashless) {
			reimbursement.setKey(null);
			
			userName = SHAUtils.getUserNameForDB(userName);
			
			if(null !=  rodKeyForCancelROD) {
				reimbursement.setKey(rodKeyForCancelROD);
			}
			if(reimbursement.getKey() != null) {
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
			} else {
				reimbursement.setCreatedBy(userName);
				reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(reimbursement);
			}
			
			
		} else {
			if (reimbursement.getKey() != null) {
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
			} else {
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setCreatedBy(userName);
				reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(reimbursement);
			}
		}

		entityManager.flush();
		log.info("------Reimbursement------>"+reimbursement+"<------------");
		preauthDTO.setKey(reimbursement.getKey());

		
		 * preauthDTO.getCoordinatorDetails().setPreauthKey(reimbursement.getKey(
		 * ));
		 * preauthDTO.getCoordinatorDetails().setIntimationKey(reimbursement.
		 * getClaim().getIntimation().getKey());
		 * preauthDTO.getCoordinatorDetails
		 * ().setPolicyKey(reimbursement.getClaim
		 * ().getIntimation().getPolicy().getKey());
		 
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = preauthDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		UpdateHospital updateHospital = reimbursementMapper
				.getUpdateHospital(updateHospitalDetails);
		if (updateHospital != null && updateHospital.getHospitalId() != null) {
			updateHospital.setReimbursement(reimbursement);

			if (updateHospital.getKey() != null) {
				entityManager.merge(updateHospital);
			} else {
				entityManager.persist(updateHospital);
			}
			entityManager.flush();
			log.info("------UpdateHospital------>"+updateHospital+"<------------");
			updateHospitalDetails.setKey(updateHospital.getKey());
		}

		OtherClaimDetailsDTO otherClaimDetails = preauthDTO
				.getPreauthDataExtractionDetails().getOtherClaimDetails();
		if (null != preauthDTO.getPreauthDataExtractionDetails()
				.getCoveredPreviousClaimFlag()
				&& preauthDTO.getPreauthDataExtractionDetails()
						.getCoveredPreviousClaimFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			PreviousClaimedHistory claimedHistory = reimbursementMapper
					.getClaimedHistory(otherClaimDetails);
			claimedHistory.setReimbursement(reimbursement);
			if (claimedHistory.getKey() != null) {
				entityManager.merge(claimedHistory);
			} else {
				entityManager.persist(claimedHistory);
			}
			entityManager.flush();
			log.info("------PreviousClaimedHistory------>"+claimedHistory+"<------------");
			preauthDTO.getPreauthDataExtractionDetails().getOtherClaimDetails()
					.setKey(claimedHistory.getKey());

			List<OtherClaimDiagnosisDTO> otherClaimDetailsList = preauthDTO
					.getPreauthDataExtractionDetails()
					.getOtherClaimDetailsList();
			if (!otherClaimDetailsList.isEmpty()) {
				for (OtherClaimDiagnosisDTO otherClaimsDiagDTO : otherClaimDetailsList) {
					PreviousClaimedHospitalization claimedHospitalization = reimbursementMapper
							.getClaimedHospitalization(otherClaimsDiagDTO);
					claimedHospitalization
							.setPreviousClaimedHistory(claimedHistory);
					if (isZonalReview) {
						claimedHospitalization.setKey(null);
					}

					if (claimedHospitalization.getKey() != null) {
						entityManager.merge(claimedHospitalization);
					} else {
						entityManager.persist(claimedHospitalization);
						otherClaimsDiagDTO.setKey(claimedHospitalization
								.getKey());
					}
					log.info("------PreviousClaimedHospitalization------>"+claimedHospitalization+"<------------");
				}
				entityManager.flush();
			}
		}

		entityManager.clear();
		if (preauthDTO.getCoordinatorDetails() != null
				&& preauthDTO.getCoordinatorDetails().getRefertoCoordinator() != null) {
			if (preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag()
					.toLowerCase().equalsIgnoreCase("y")) {
				Coordinator coordinator = reimbursementMapper
						.getCoordinator(preauthDTO.getCoordinatorDetails());
				coordinator.setActiveStatus(1l);
				coordinator.setStage(reimbursement.getStage());
				coordinator.setStatus(reimbursement.getStatus());
				coordinator.setClaim(reimbursement.getClaim());
				if (isZonalReview && rodKeyForCancelROD == null) {
					coordinator.setKey(null);
				}

				if (coordinator.getKey() != null) {
					entityManager.merge(coordinator);
				} else {
					entityManager.persist(coordinator);
				}
				entityManager.flush();
				log.info("------Coordinator------>"+coordinator+"<------------");
				preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
			}
		}

		List<SpecialityDTO> specialityDTOList = preauthDTO
				.getPreauthDataExtractionDetails().getSpecialityList();
		if (!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = reimbursementMapper
						.getSpeciality(specialityDTO);
				// speciality.setPreauth(preauth);
				speciality.setClaim(reimbursement.getClaim());
				speciality.setStage(reimbursement.getStage());
				speciality.setStatus(reimbursement.getStatus());

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
			}
			entityManager.flush();
		}

		entityManager.clear();

		
		 * Map<Long, String> keyMap = new HashMap<Long, String>();
		 * keyMap.put(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED,
		 * "coordinatorreply");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_COORDINATOR,
		 * "coordinator");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER,
		 * "medical");
		 * keyMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER,
		 * "financial"); keyMap.put(ReferenceTable.BILLING_BENEFITS_APPROVED,
		 * "benefits");
		 * 
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) ||
		 * keyMap.containsKey(reimbursement.getStatus().getKey())) {
		 * List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO =
		 * preauthDTO.
		 * getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO(); for
		 * (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO :
		 * medicalDecisionTableDTO) {
		 * if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
		 * 
		 * DiagnosisDetailsTableDTO diagnosisDetailsDTO =
		 * diagnosisProcedureTableDTO.getDiagnosisDetailsDTO();
		 * diagnosisDetailsDTO
		 * .setAmountConsideredAmount(diagnosisProcedureTableDTO
		 * .getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setNetAmount(diagnosisProcedureTableDTO.getNetAmount() != null ?
		 * diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount() !=
		 * null ? diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString
		 * (diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * diagnosisDetailsDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setApproveRemarks(diagnosisProcedureTableDTO.
		 * getRemarks());
		 * diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.
		 * getApprovedAmount() != null ? diagnosisDetailsDTO.getApprovedAmount()
		 * : 0d) - (diagnosisDetailsDTO.getOldApprovedAmount() != null ?
		 * diagnosisDetailsDTO.getOldApprovedAmount() : 0d )); } else
		 * if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
		 * ProcedureDTO procedureDTO =
		 * diagnosisProcedureTableDTO.getProcedureDTO();
		 * procedureDTO.setAmountConsideredAmount
		 * (diagnosisProcedureTableDTO.getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * procedureDTO.setNetAmount(diagnosisProcedureTableDTO.getNetAmount()
		 * != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() :
		 * 0d);
		 * procedureDTO.setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount
		 * () != null ?
		 * diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() : 0d);
		 * procedureDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString(
		 * diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * procedureDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * procedureDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO.getRemarks
		 * ()); procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount() !=
		 * null ? procedureDTO.getApprovedAmount() : 0d) -
		 * (procedureDTO.getOldApprovedAmount() != null ?
		 * procedureDTO.getOldApprovedAmount() : 0d )); } } }
		 

		List<ProcedureDTO> procedureList = preauthDTO
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		if (!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = reimbursementMapper
						.getProcedure(procedureDTO);
				procedure.setTransactionKey(reimbursement.getKey());
				procedure.setStage(reimbursement.getStage());
				procedure.setStatus(reimbursement.getStatus());

				if(isCashless) {
					procedure.setProcessFlag("0");
				}
				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					if (procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double
								.valueOf(procedureDTO.getCopay().getValue()));
					}

				}

				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					procedure.setProcessFlag("A");
					if (procedureDTO.getOldApprovedAmount() != null
							&& procedureDTO.getApprovedAmount() != null
							&& procedureDTO.getOldApprovedAmount() > procedureDTO
									.getApprovedAmount()) {
						procedure.setProcessFlag("R");
					}
				}

				if (isZonalReview || isCashless && rodKeyForCancelROD == null) {
					procedure.setKey(null);
				}
				   
				if(isHospitalizationRepeat) {
					procedure.setNetApprovedAmount(0d);
					procedure.setDiffAmount(0d);
					procedure.setApprovedAmount(0d);
				}
				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
				log.info("------Procedure------>"+procedure+"<------------");
			}

	
			List<Preauth> preAuthList = getPreauthByIntimationKey(rodDTO
					.getClaimDTO().getNewIntimationDto().getKey());
			if (null != preAuthList && !preAuthList.isEmpty()) {
				int iSize = preAuthList.size();
				Preauth preAuth = preAuthList.get(iSize - 1);
				if (null != preAuth && null != preAuth.getTreatmentType())
					rodDTO.setTreatmentType(preAuth.getTreatmentType().getValue());
			} else {
				*//**
				 * If cashless is not available for this intimation, then by default
				 * we set treatment type as Medical. Any how during zonal , this
				 * will changed.
				 *//*
				rodDTO.setTreatmentType("Medical");
			}
			// Intimation objIntimation =
			// getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
	
			// Need to check whether the insured object
	
			
			 * if(null != objIntimation) { Insured insured =
			 * getCLSInsured(objIntimation.getInsured().getKey());
			 * objIntimation.setAdmissionDate
			 * (rodDTO.getDocumentDetails().getDateOfAdmission());
			 * insured.setInsuredName
			 * (rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
			 * objIntimation.setInsured(insured);
			 * 
			 * entityManager.merge(objIntimation); entityManager.flush(); }
			 
			*//***
			 * Coded added for updating the current provision amt
			 ***//*
	
	//		if(reimbursement.getCurrentProvisionAmt())
			Double balanceSI = getBalanceSI(rodDTO);
			Double currentProvisionAmt = 0d;
	//		previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
			if(null != previousCurrentProvAmt)
			{
				currentProvisionAmt =   calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
			}
			else
			{
				currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
			}
			
			if(null != balanceSI && null != currentProvisionAmt)
			{
	//			reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, currentProvisionAmt));
			}
			
			if(balanceSI > currentProvisionAmt)
			{
				reimbursement.setCurrentProvisionAmt(balanceSI);
			}
			else
			{
				reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
			}
			//Claim claimObj = reimbursement.getClaim();
			if(claimObj != null) {
				claimObj = getClaimByClaimKey(reimbursement.getClaim().getKey());
			}
			
			<------------ As per requirement Provision Amount changes has been made by Saravana........ ------------------------------> /////
			if(reimbursement.getClaim().getClaimType() != null) {
				if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
					if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){ &&  null != ReferenceTable.getSeniorCitizenKeys() && null == ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getProduct().getKey()) ) {
						reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, totalClaimedAmt));
						//reimbursement.setCurrentProvisionAmt(totalClaimedAmt);
					} else {
						Preauth latestPreauthByClaim = getLatestPreauthByClaim(claimObj.getKey());
						if(latestPreauthByClaim != null) {
							reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, latestPreauthByClaim.getTotalApprovalAmount() != null ? latestPreauthByClaim.getTotalApprovalAmount(): 0d));
						}
						
						// No update for Cashless with hospitalization........

			entityManager.flush();
		}

		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
				.getPreauthDataExtractionDetails().getDiagnosisTableList();
		// Iterate pedValidationTable List.
		if (!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {

				// Method to insert into MAS Diagnosis.
				// saveToMasterDiagnosis(pedValidationDTO);

				PedValidation pedValidation = reimbursementMapper
						.getPedValidation(pedValidationDTO);
				pedValidation.setTransactionKey(reimbursement.getKey());
				pedValidation.setIntimation(reimbursement.getClaim()
						.getIntimation());
				pedValidation.setPolicy(reimbursement.getClaim()
						.getIntimation().getPolicy());
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				if(isCashless) {
					pedValidation.setProcessFlag("0");
				}
				if (isZonalReview || isCashless && rodKeyForCancelROD == null) {

					pedValidation.setKey(null);
				}
				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					pedValidation.setProcessFlag("A");
					if (pedValidationDTO.getOldApprovedAmount() != null
							&& pedValidationDTO.getApprovedAmount() != null
							&& pedValidationDTO.getOldApprovedAmount() > pedValidationDTO
									.getApprovedAmount()) {
						pedValidation.setProcessFlag("R");

					}
				}

				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO
							.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if (pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double
									.valueOf(pedDetailsTableDTO.getCopay()
											.getValue()));
						}
					}
				}

				else if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
=======
				/*else if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
>>>>>>> dbef35826fc77e256ad795f3291bfa8766dd6a72
						null != rodDTO.getNewIntimationDTO().getProduct() && null != rodDTO.getNewIntimationDTO().getProduct().getKey() && null != ReferenceTable.getSeniorCitizenKeys() && null != ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getProduct().getKey()))
				{
					Double claimAmt = (70 /100) *totalClaimedAmt;
					if(null != claimAmt)
					{
						reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, claimAmt));
					}
				}
				
				else {
					// Other than Hospitalization we have to set Total Claimed Amount as current provision amount for both the cases....
					reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, totalClaimedAmt));
					//reimbursement.setCurrentProvisionAmt(totalClaimedAmt);
				}
				
			}
			
			
			if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && null != rodDTO.getNewIntimationDTO() &&
					null != rodDTO.getNewIntimationDTO().getProduct() && null != rodDTO.getNewIntimationDTO().getProduct().getKey() && null != ReferenceTable.getSeniorCitizenKeys() && null != ReferenceTable.getSeniorCitizenKeys().get(rodDTO.getNewIntimationDTO().getProduct().getKey()))
			{
				Double claimAmt = (70 /100) *totalClaimedAmt;
				if(null != claimAmt)
				{
					reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, claimAmt));
				}
			}

			<---------------------------- Current  Provision amount updates completed ---------------------------->
			
			
			if(null != rodDTO.getDocumentDetails().getPaymentMode() && rodDTO.getDocumentDetails().getPaymentMode())
			{
				reimbursement.setAccountNumber(null);	
				reimbursement.setBankId(null);
				reimbursement.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);

				if(isHospitalizationRepeat) {
					pedValidation.setNetApprovedAmount(0d);
					pedValidation.setDiffAmount(0d);
					pedValidation.setApproveAmount(0d);
				}
				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				log.info("------PedValidation------>"+pedValidation+"<------------");
				List<PedDetailsTableDTO> pedList = pedValidationDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						DiagnosisPED diagnosisPED = reimbursementMapper
								.getDiagnosisPED(pedDetailsTableDTO);
						diagnosisPED.setPedValidation(pedValidation);
						if (pedDetailsTableDTO
								.getPedExclusionImpactOnDiagnosis() != null) {
							MastersValue value = new MastersValue();
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setValue(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis()
									.getValue() : null);
							diagnosisPED.setDiagonsisImpact(value);
						}
						if (pedDetailsTableDTO.getExclusionDetails() != null) {
							ExclusionDetails exclusionValue = new ExclusionDetails();
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setExclusion(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getValue()
											: null);
							diagnosisPED.setExclusionDetails(exclusionValue);
						}
						diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO
								.getRemarks());
						if (isZonalReview || isCashless && rodKeyForCancelROD == null) {
							diagnosisPED.setKey(null);
						}


						if (diagnosisPED.getKey() != null) {
							entityManager.merge(diagnosisPED);
						} else {
							entityManager.persist(diagnosisPED);
							pedDetailsTableDTO.setKey(diagnosisPED.getKey());
						}
						entityManager.flush();
						log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
					}
				}
			}

		}

		return reimbursement;
	}*/
	
	public Reimbursement savePreauthValues(PreauthDTO preauthDTO, Reimbursement reimbursement, ZonalMedicalReviewMapper reimbursementMapper,
			Boolean isZonalReview, Boolean isCashless,String userName, Long rodKeyForCancelROD, Boolean isHospitalizationRepeat) {
		
		
		if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus() != null){
			if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_ADMITTED)){
			MastersValue master = getMaster(ReferenceTable.PATIENT_STATUS_DISCHARGED);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(master.getKey());
			selectValue.setValue(master.getValue());
			preauthDTO.getPreauthDataExtractionDetails().setPatientStatus(selectValue);
			}
			else if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_RECOVERED_CASHLESS)){
				MastersValue master = getMaster(ReferenceTable.PATIENT_STATUS_DISCHARGED);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(master.getKey());
				selectValue.setValue(master.getValue());
				preauthDTO.getPreauthDataExtractionDetails().setPatientStatus(selectValue);
				}
			else if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_SHIFTED_CASHLESS)){
				MastersValue master = getMaster(ReferenceTable.PATIENT_STATUS_SHIFTED_REIMB);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(master.getKey());
				selectValue.setValue(master.getValue());
				preauthDTO.getPreauthDataExtractionDetails().setPatientStatus(selectValue);
				}
			else if(preauthDTO.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_MED_ADVICE_CASHLESS)){
				MastersValue master = getMaster(ReferenceTable.PATIENT_STATUS_MED_ADVICE_REIMB);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(master.getKey());
				selectValue.setValue(master.getValue());
				preauthDTO.getPreauthDataExtractionDetails().setPatientStatus(selectValue);
				}
	}
		
//		reimbursement = reimbursementMapper
//				.getReimbursement(preauthDTO);
		
		if(reimbursement.getPatientStatus() != null) {
			MastersValue masterValue = new MastersValue();
			masterValue.setKey(reimbursement.getPatientStatus().getKey());
			masterValue.setValue(reimbursement.getPatientStatus().getValue());
			if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_ADMITTED)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_ADMITTED_REIMB);
			} else if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_DECEASED)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB);
			}else if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_RECOVERED_CASHLESS)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_DISCHARGED);
			}else if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_SHIFTED_CASHLESS)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_SHIFTED_REIMB);
			}else if(reimbursement.getPatientStatus().getKey().equals(ReferenceTable.PATIENT_STATUS_MED_ADVICE_CASHLESS)) {
				masterValue.setKey(ReferenceTable.PATIENT_STATUS_MED_ADVICE_REIMB);
			}
			reimbursement.setPatientStatus(masterValue);
		}
		
		reimbursement.setActiveStatus(1l);
		Claim searchByClaimKey2 = searchByClaimKey(preauthDTO.getClaimKey());
		reimbursement.setClaim(searchByClaimKey2);
	//	reimbursement.setCurrentProvisionAmt(preauthDTO.getC);

		Claim currentClaim = null;

		if (reimbursement.getClaim() != null) {
			currentClaim = searchByClaimKey(reimbursement.getClaim().getKey());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
			entityManager.merge(currentClaim);
			entityManager.flush();
			log.info("------Claim------>"+currentClaim+"<------------");
		}

		if (preauthDTO.getPreauthPreviousClaimsDetails()
				.getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO
					.getPreauthPreviousClaimsDetails()
					.getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			log.info("------Claim------>"+currentClaim+"<------------");
		}

		/*
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) { String processflag =
		 * "A";
		 * if(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() !=
		 * null &&
		 * preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt
		 * () != null &&
		 * preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() >
		 * preauthDTO
		 * .getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()) {
		 * processflag = "R"; } reimbursement.setApprovedAmount(preauthDTO.
		 * getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()); }
		 */
		if(preauthDTO
				.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			reimbursement.setAutomaticRestoration(preauthDTO
					.getPreauthDataExtractionDetails().getAutoRestoration()
					.toLowerCase().contains("not") ? "N" : "Y");
		} else {
			reimbursement.setAutomaticRestoration("Y");
		}
	
		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE during
		 * policy refractoring activity. Below code is added for inserting value
		 * in the insured key column.
		 * */
		// reimbursement.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		/*
		 * if(preauthDTO.getKey() != null) { entityManager.merge(reimbursement);
		 * } else
		 */
		reimbursement.setOtherInsurerApplicableFlag(null);
		if(null !=  rodKeyForCancelROD) {
			reimbursement.setKey(rodKeyForCancelROD);
		}
		if (!isCashless) {
			reimbursement.setKey(null);
			
			userName = SHAUtils.getUserNameForDB(userName);
			
			if(null !=  rodKeyForCancelROD) {
				reimbursement.setKey(rodKeyForCancelROD);
			}
			if(reimbursement.getKey() != null) {
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
			} else {
				reimbursement.setCreatedBy(userName);
				reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//				add on test
				reimbursement.setBillingCompletedDate(null);
				reimbursement.setFinancialCompletedDate(null);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setModifiedBy(userName);
				entityManager.persist(reimbursement);
			}
			
			
		} else {
			if (reimbursement.getKey() != null) {
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setModifiedBy(userName);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
			} else {
				userName = SHAUtils.getUserNameForDB(userName);
				reimbursement.setCreatedBy(userName);
				reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				//add on test
				reimbursement.setBillingCompletedDate(null);
				reimbursement.setFinancialCompletedDate(null);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setModifiedBy(userName);
				entityManager.persist(reimbursement);
			}
		}

		entityManager.flush();
		log.info("------Reimbursement------>"+reimbursement+"<------------");
		preauthDTO.setKey(reimbursement.getKey());

		/*
		 * preauthDTO.getCoordinatorDetails().setPreauthKey(reimbursement.getKey(
		 * ));
		 * preauthDTO.getCoordinatorDetails().setIntimationKey(reimbursement.
		 * getClaim().getIntimation().getKey());
		 * preauthDTO.getCoordinatorDetails
		 * ().setPolicyKey(reimbursement.getClaim
		 * ().getIntimation().getPolicy().getKey());
		 */
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = preauthDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		UpdateHospital updateHospital = reimbursementMapper
				.getUpdateHospital(updateHospitalDetails);
		if (updateHospital != null && updateHospital.getHospitalId() != null) {
			updateHospital.setReimbursement(reimbursement);

			if (updateHospital.getKey() != null) {
				entityManager.merge(updateHospital);
			} else {
				entityManager.persist(updateHospital);
			}
			entityManager.flush();
			log.info("------UpdateHospital------>"+updateHospital+"<------------");
			updateHospitalDetails.setKey(updateHospital.getKey());
		}

		OtherClaimDetailsDTO otherClaimDetails = preauthDTO
				.getPreauthDataExtractionDetails().getOtherClaimDetails();
		if (null != preauthDTO.getPreauthDataExtractionDetails()
				.getCoveredPreviousClaimFlag()
				&& preauthDTO.getPreauthDataExtractionDetails()
						.getCoveredPreviousClaimFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			PreviousClaimedHistory claimedHistory = reimbursementMapper
					.getClaimedHistory(otherClaimDetails);
			claimedHistory.setReimbursement(reimbursement);
			if (claimedHistory.getKey() != null) {
				entityManager.merge(claimedHistory);
			} else {
				entityManager.persist(claimedHistory);
			}
			entityManager.flush();
			log.info("------PreviousClaimedHistory------>"+claimedHistory+"<------------");
			preauthDTO.getPreauthDataExtractionDetails().getOtherClaimDetails()
					.setKey(claimedHistory.getKey());

			List<OtherClaimDiagnosisDTO> otherClaimDetailsList = preauthDTO
					.getPreauthDataExtractionDetails()
					.getOtherClaimDetailsList();
			if (!otherClaimDetailsList.isEmpty()) {
				for (OtherClaimDiagnosisDTO otherClaimsDiagDTO : otherClaimDetailsList) {
					PreviousClaimedHospitalization claimedHospitalization = reimbursementMapper
							.getClaimedHospitalization(otherClaimsDiagDTO);
					claimedHospitalization
							.setPreviousClaimedHistory(claimedHistory);
					if (isZonalReview) {
						claimedHospitalization.setKey(null);
					}

					if (claimedHospitalization.getKey() != null) {
						entityManager.merge(claimedHospitalization);
					} else {
						entityManager.persist(claimedHospitalization);
						otherClaimsDiagDTO.setKey(claimedHospitalization
								.getKey());
					}
					log.info("------PreviousClaimedHospitalization------>"+claimedHospitalization+"<------------");
				}
				entityManager.flush();
			}
		}

		entityManager.clear();
		if (preauthDTO.getCoordinatorDetails() != null
				&& preauthDTO.getCoordinatorDetails().getRefertoCoordinator() != null) {
			if (preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag()
					.toLowerCase().equalsIgnoreCase("y")) {
				Coordinator coordinator = reimbursementMapper
						.getCoordinator(preauthDTO.getCoordinatorDetails());
				coordinator.setActiveStatus(1l);
				coordinator.setStage(reimbursement.getStage());
				coordinator.setStatus(reimbursement.getStatus());
				coordinator.setClaim(reimbursement.getClaim());
				if (isZonalReview && rodKeyForCancelROD == null) {
					coordinator.setKey(null);
				}

				if (coordinator.getKey() != null) {
					entityManager.merge(coordinator);
				} else {
					entityManager.persist(coordinator);
				}
				entityManager.flush();
				log.info("------Coordinator------>"+coordinator+"<------------");
				preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
			}
		}

		List<SpecialityDTO> specialityDTOList = preauthDTO
				.getPreauthDataExtractionDetails().getSpecialityList();
		if (!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = reimbursementMapper
						.getSpeciality(specialityDTO);
				// speciality.setPreauth(preauth);
				speciality.setClaim(reimbursement.getClaim());
				speciality.setStage(reimbursement.getStage());
				speciality.setStatus(reimbursement.getStatus());

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
			}
			entityManager.flush();
		}

		entityManager.clear();

		/*
		 * Map<Long, String> keyMap = new HashMap<Long, String>();
		 * keyMap.put(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED,
		 * "coordinatorreply");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_COORDINATOR,
		 * "coordinator");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER,
		 * "medical");
		 * keyMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER,
		 * "financial"); keyMap.put(ReferenceTable.BILLING_BENEFITS_APPROVED,
		 * "benefits");
		 * 
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) ||
		 * keyMap.containsKey(reimbursement.getStatus().getKey())) {
		 * List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO =
		 * preauthDTO.
		 * getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO(); for
		 * (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO :
		 * medicalDecisionTableDTO) {
		 * if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
		 * 
		 * DiagnosisDetailsTableDTO diagnosisDetailsDTO =
		 * diagnosisProcedureTableDTO.getDiagnosisDetailsDTO();
		 * diagnosisDetailsDTO
		 * .setAmountConsideredAmount(diagnosisProcedureTableDTO
		 * .getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setNetAmount(diagnosisProcedureTableDTO.getNetAmount() != null ?
		 * diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount() !=
		 * null ? diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString
		 * (diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * diagnosisDetailsDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setApproveRemarks(diagnosisProcedureTableDTO.
		 * getRemarks());
		 * diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.
		 * getApprovedAmount() != null ? diagnosisDetailsDTO.getApprovedAmount()
		 * : 0d) - (diagnosisDetailsDTO.getOldApprovedAmount() != null ?
		 * diagnosisDetailsDTO.getOldApprovedAmount() : 0d )); } else
		 * if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
		 * ProcedureDTO procedureDTO =
		 * diagnosisProcedureTableDTO.getProcedureDTO();
		 * procedureDTO.setAmountConsideredAmount
		 * (diagnosisProcedureTableDTO.getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * procedureDTO.setNetAmount(diagnosisProcedureTableDTO.getNetAmount()
		 * != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() :
		 * 0d);
		 * procedureDTO.setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount
		 * () != null ?
		 * diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() : 0d);
		 * procedureDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString(
		 * diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * procedureDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * procedureDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO.getRemarks
		 * ()); procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount() !=
		 * null ? procedureDTO.getApprovedAmount() : 0d) -
		 * (procedureDTO.getOldApprovedAmount() != null ?
		 * procedureDTO.getOldApprovedAmount() : 0d )); } } }
		 */

		List<ProcedureDTO> procedureList = preauthDTO
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		if (!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = reimbursementMapper
						.getProcedure(procedureDTO);
				procedure.setTransactionKey(reimbursement.getKey());
				procedure.setStage(reimbursement.getStage());
				procedure.setStatus(reimbursement.getStatus());

				if(isCashless) {
					procedure.setProcessFlag("0");
				}
				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					if (procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double
								.valueOf(procedureDTO.getCopay().getValue()));
					}

				}

				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					procedure.setProcessFlag("A");
					if (procedureDTO.getOldApprovedAmount() != null
							&& procedureDTO.getApprovedAmount() != null
							&& procedureDTO.getOldApprovedAmount() > procedureDTO
									.getApprovedAmount()) {
						procedure.setProcessFlag("R");
					}
				}

				if (isZonalReview || isCashless && rodKeyForCancelROD == null) {
					procedure.setKey(null);
				}
				   
				if(isHospitalizationRepeat) {
					procedure.setNetApprovedAmount(0d);
					procedure.setDiffAmount(0d);
					procedure.setApprovedAmount(0d);
				}
				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
				log.info("------Procedure------>"+procedure+"<------------");
			}
			entityManager.flush();
		}

		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
				.getPreauthDataExtractionDetails().getDiagnosisTableList();
		// Iterate pedValidationTable List.
		if (!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {

				// Method to insert into MAS Diagnosis.
				// saveToMasterDiagnosis(pedValidationDTO);

				PedValidation pedValidation = reimbursementMapper
						.getPedValidation(pedValidationDTO);
				pedValidation.setTransactionKey(reimbursement.getKey());
				pedValidation.setIntimation(reimbursement.getClaim()
						.getIntimation());
				pedValidation.setPolicy(reimbursement.getClaim()
						.getIntimation().getPolicy());
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				if(isCashless) {
					pedValidation.setProcessFlag("0");
				}
				if (isZonalReview || isCashless && rodKeyForCancelROD == null) {

					pedValidation.setKey(null);
				}
				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					pedValidation.setProcessFlag("A");
					if (pedValidationDTO.getOldApprovedAmount() != null
							&& pedValidationDTO.getApprovedAmount() != null
							&& pedValidationDTO.getOldApprovedAmount() > pedValidationDTO
									.getApprovedAmount()) {
						pedValidation.setProcessFlag("R");
					}
				}

				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO
							.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if (pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double
									.valueOf(pedDetailsTableDTO.getCopay()
											.getValue()));
						}
					}
				}

				if(isHospitalizationRepeat) {
					pedValidation.setNetApprovedAmount(0d);
					pedValidation.setDiffAmount(0d);
					pedValidation.setApproveAmount(0d);
				}
				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				log.info("------PedValidation------>"+pedValidation+"<------------");
				List<PedDetailsTableDTO> pedList = pedValidationDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						DiagnosisPED diagnosisPED = reimbursementMapper
								.getDiagnosisPED(pedDetailsTableDTO);
						diagnosisPED.setPedValidation(pedValidation);
						if (pedDetailsTableDTO
								.getPedExclusionImpactOnDiagnosis() != null) {
							MastersValue value = new MastersValue();
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setValue(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis()
									.getValue() : null);
							diagnosisPED.setDiagonsisImpact(value);
						}
						if (pedDetailsTableDTO.getExclusionDetails() != null) {
							ExclusionDetails exclusionValue = new ExclusionDetails();
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setExclusion(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getValue()
											: null);
							diagnosisPED.setExclusionDetails(exclusionValue);
						}
						diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO
								.getRemarks());
						if (isZonalReview || isCashless && rodKeyForCancelROD == null) {
							diagnosisPED.setKey(null);
						}

						if (diagnosisPED.getKey() != null) {
							entityManager.merge(diagnosisPED);
						} else {
							entityManager.persist(diagnosisPED);
							pedDetailsTableDTO.setKey(diagnosisPED.getKey());
						}
						entityManager.flush();
						log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
					}
				}
			}

		}

		return reimbursement;
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
	
	public NEFTQueryDetails getNEFTQueryDetailsObjectByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("NEFTQueryDetails.findByKey")
				.setParameter("rodKey", rodKey);
		
		List<NEFTQueryDetails> neftQueryDetailsList = query.getResultList();
		if(null != neftQueryDetailsList && !neftQueryDetailsList.isEmpty())
		{
			entityManager.refresh(neftQueryDetailsList.get(0));
			return neftQueryDetailsList.get(0);
		}
		return null;
	}
	
	

	
	
	public List<DMSDocumentDetailsDTO> getDocumentDetailsData(String intimationNumber, long lumenRequestKey)
	{
		//Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNo");
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoOrderByCreatedDate");
		query = query.setParameter("intimationNumber", intimationNumber);
		query = query.setParameter("lumenRequestKey", lumenRequestKey);
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<Object[]> documentDetailsList  = query.getResultList();
		List<DocumentDetails> listOfDocument = new ArrayList<DocumentDetails>();
		for (Object[] documentDetails : documentDetailsList) {
			DocumentDetails singleList = new DocumentDetails();
			if(documentDetails[0] != null){
				singleList.setIntimationNumber((String)documentDetails[0]);
			}
			if(documentDetails[1] != null){
				singleList.setClaimNumber((String)documentDetails[1]);
			}
			if(documentDetails[2] != null){
				singleList.setDocumentType((String)documentDetails[2]);
			}
			if(documentDetails[3] != null){
				singleList.setSfFileName((String)documentDetails[3]);
			}
			if(documentDetails[4] != null){
				singleList.setDocumentSource((String)documentDetails[4]);
			}
			if(documentDetails[5] != null){
				singleList.setDocumentToken((Long)documentDetails[5]);
			}
			if(documentDetails[6] != null){
				singleList.setFileName((String)documentDetails[6]);
			}
			if(documentDetails[7] != null){
				singleList.setCreatedDate((Date)documentDetails[7]);
			}
			if(documentDetails[8] != null){
				singleList.setReimbursementNumber((String)documentDetails[8]);
			}
			if(documentDetails[9] != null){
				singleList.setCashlessNumber((String)documentDetails[9]);
			}
			listOfDocument.add(singleList);
		}
		

		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(listOfDocument);
		/**
		 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
		 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
		 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
		 * both will be populated in file name variable of dto. 		 */
		
		
		List<DMSDocumentDetailsDTO> finalDMSDataList = new ArrayList<DMSDocumentDetailsDTO>();
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				
				documentDetails.setDmsRestApiURL(dmsAPIUrl);
											
				if(null != documentDetails.getReimbursementNumber())
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());
				
				}
				else
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
				}
					
				
				
				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
				{
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}
				
				/**
				 * If ROD is cancelled, then documents corresponding to that ROD number
				 * will not be visible in claims DMS. Added for ticket 813. 
				 * **/
				
				/**
				 * Post the above ticket , then came the enhancement R0254.
				 			 * R0254 - To retain uploaded documents and bill entry values
							 *  even if the rod is cancelled. Hence if, rod is cancelled 
							 *  then those documents should also reflect in claims dms.
							 *  Hence below code is commented. 
				 * This enhancement is complete in contrary with above ticket.
				 * Hence commenting the below code which was added as a part
				 * of ticket 813.
				 * 
				 * */
				// 813 ticket fix starts.
				/*Reimbursement reimbursementObj = getReimbursementObject(documentDetails.getReimbursementNumber());
				
				if(null != reimbursementObj)
				{
				
					Map<Long,Long> cancelMap = ReferenceTable.getCancelRODKeys();
					if(null != cancelMap)
					{
						if(null != reimbursementObj.getStatus())
						{
							Long key = cancelMap.get(reimbursementObj.getStatus().getKey());
							*//**
							 *  
							 * 
							 * *//*
							if(null == key)
							{
								finalDMSDataList.add(documentDetails);
							}
						}
					}
				}
				else*/
				{
					finalDMSDataList.add(documentDetails);
				}
				// 813 ticket fix ends.
			}
		}	
		
		return finalDMSDataList;
	}
	
	//private Reimbursement getReimbursementObject(String rodNo)
	public Reimbursement getReimbursementObject(String rodNo)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
		{
			entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}
	
	public List<DMSDocumentDetailsDTO> getDocumentDetailsForFVR(String intimationNumber)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findQueryByIntimationNo");
		query = query.setParameter("intimationNumber", intimationNumber);
	    String docType = SHAConstants.PREMIA_DOC_TYPE_FVR;
		query = query.setParameter("query", "%"+docType+"%");
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		

		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
		
		/**
		 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
		 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
		 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
		 * both will be populated in file name variable of dto. 
		 */
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
				{
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}
			}
		}
		
		return documentDetailsDTOList;
	}
	
	
	
	
	
	public List<DMSDocumentDetailsDTO> getQueryDocumentDetailsData(String intimationNumber,String docType)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findQueryByIntimationNo");
		query = query.setParameter("intimationNumber", intimationNumber);
		docType = docType.toLowerCase();
		query = query.setParameter("query", "%"+docType+"%");
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		
		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
		
		/**
		 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
		 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
		 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
		 * both will be populated in file name variable of dto. 
		 */
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
				{
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}
			}
		}
		
		return documentDetailsDTOList;
	}
	
	
	public List<DMSDocumentDetailsDTO> getQueryDocumentDetailsDataByRod(String rodNumber,String docType,String docTypeScrc,String docSource)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findQueryByRodNo"); 
		query = query.setParameter("reimbursementNumber", rodNumber);
	//	docType = docType.toLowerCase();
	//	docTypeScrc = docTypeScrc.toLowerCase();
		docSource = docSource.toLowerCase();
		
		if(docTypeScrc != null){
			docTypeScrc = docTypeScrc.toLowerCase();
		}
		
		if(docType != null){
			docType = docType.toLowerCase();
		}
		
		query = query.setParameter("billsummary", docType);
		query = query.setParameter("billassessment", docTypeScrc);
		query = query.setParameter("financialapproval", docSource);
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		

		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
		
		/**
		 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
		 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
		 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
		 * both will be populated in file name variable of dto. 
		 */
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				
				if(null != documentDetails.getReimbursementNumber())
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());;
				
				}
				else
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
				}
				
				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
				{
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}
			}
		}
		
		return documentDetailsDTOList;
	}
	
	 private DocumentDetails getDocumentDetails()
		{
		 DocumentDetails documentDetail;
			Query findByAll = entityManager.createNamedQuery("DocumentDetails.findAll");
			try{
				documentDetail =(DocumentDetails) findByAll.getResultList();
				return documentDetail;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;				
		}


	public List<UploadDocumentDTO> getRODSummaryDetails(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		List<UploadDocumentDTO> uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			for (RODDocumentSummary docSummary : rodDocSummary) {
				entityManager.refresh(docSummary);
			}
			uploadDocsDTO = CreateRODMapper.getInstance().getUploadDocumentDTO(rodDocSummary);
		}
		return uploadDocsDTO;
	}
	
	public RODDocumentSummary getRODDocumentSummaryDetailsByKey(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		//RODDocumentSummary rodDocSummaryObj = null;
		List<UploadDocumentDTO> uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByKey");
		query.setParameter("primaryKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			if(null != rodDocSummary && !rodDocSummary.isEmpty())
			{
				entityManager.refresh(rodDocSummary.get(0));
				return rodDocSummary.get(0);
			}
		
		
		}
		return null;
	}
	

	
	public List<UploadDocumentDTO> getRODSummaryDetailsForReconsideration(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		List<UploadDocumentDTO> uploadDocsDTO = null;
		List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			for (RODDocumentSummary docSummary : rodDocSummary) {
				entityManager.refresh(docSummary);
			}
	
			uploadDocsDTO = CreateRODMapper.getInstance().getUploadDocumentDTO(rodDocSummary);
		}
		
		if(null != uploadDocsDTO)
		{
			for (UploadDocumentDTO uploadDocDTO : uploadDocsDTO) {
				//sss
				/**
				 * In case of reconsideratio is true, then  status and bill entry icon needs to be disablded.
				 * For this, the below variable is set to true. This is added as a part of jiira bug.
				 * */
				//uploadDocDTO.setStatus(true);
				//uploadDocDTO.setEnableOrDisableBtn(false);
				List<RODBillDetails> billEntryDetails = getBillEntryDetails(uploadDocDTO.getDocSummaryKey());
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billDetails : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */
						
						BillEntryDetailsDTO dto = new BillEntryDetailsDTO();
	
						dto.setItemName(billDetails.getItemName());
						dto.setKey(billDetails.getKey());
						SelectValue classificationValue = new SelectValue();
						classificationValue.setId(billDetails.getBillClassification().getKey());
						classificationValue.setValue(billDetails.getBillClassification()
								.getValue());
						dto.setClassification(classificationValue);
						dto.setItemNo(billDetails.getItemNumber());
						/*dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
								.getNoOfDaysBills().doubleValue() : 0d);*/
						dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
								.getNoOfDaysBills().doubleValue() : null);
						dto.setPerDayAmt(billDetails.getPerDayAmountBills());
						dto.setBillValue(billDetails.getClaimedAmountBills());
						dto.setItemValue(billDetails.getClaimedAmountBills());
						SelectValue billCategoryvalue = new SelectValue();
						if((billDetails.getBillCategory() != null && billDetails.getBillCategory().getKey() != null && billDetails.getBillCategory().getKey().equals(46l)) && (billDetails.getBillClassification() != null && billDetails.getBillClassification().getKey() != null && billDetails.getBillClassification().getKey().equals(ReferenceTable.HOSPITALIZATION)) && (billDetails.getRodDocumentSummaryKey() != null && 
								billDetails.getRodDocumentSummaryKey().getReimbursement() != null && billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim() != null	&& (billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
										|| billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)))){

							billCategoryvalue.setId(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION);
							billCategoryvalue.setValue("Others with Proportionate Deduction");
						}
						else if(billDetails.getBillCategory() != null)
						{
							billCategoryvalue.setId(billDetails.getBillCategory().getKey());
							billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
						}
						dto.setCategory(billCategoryvalue);
						dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey().getKey());
						dtoList.add(dto);
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
					uploadDocDTO.setBillEntryDetailList(dtoList);
				}
			}
		}

		return uploadDocsDTO;
	}
	
	
	
	
	public List<UploadDocumentDTO> getRODBillSummaryDetails(Long key, ZonalMedicalReviewMapper mapper) {
//		ZonalMedicalReviewMapper mapper = new ZonalMedicalReviewMapper();
		List<RodBillSummary> rodBillSummary = null;
		List<UploadDocumentDTO> uploadDocsDTO = null;
		Query query = entityManager
				.createNamedQuery("RodBillSummary.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodBillSummary = query.getResultList();
			for (RodBillSummary billSummary : rodBillSummary) {
				entityManager.refresh(billSummary);
			}
			uploadDocsDTO = mapper.getUploadDocListForBillSummary(rodBillSummary);

		}

		return uploadDocsDTO;
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

	public Hospitals getHospitalsDetails(Long key, MasterService masterService) {
		return masterService.getHospitalDetails(key);
	}

	public BankMaster getBankMaster(String ifscCode, MasterService masterService) {
		return masterService.getBankDetails(ifscCode);
	}
	
	public BankMaster getBankMasterByKey(Long bankId, MasterService masterService) {
		return masterService.getBankDetailsByKey(bankId);
	}

	/*
	 * public List<DocumentDetailsDTO> getDocumentDetailsDTO (Long claimKey) {
	 * Query query =
	 * entityManager.createNamedQuery("DocAcknowledgement.findByClaimKey");
	 * query.setParameter("claimkey", claimKey); List<DocumentDetailsDTO>
	 * documentDetailsDTO = null; if(null != query.getResultList() &&
	 * !query.getResultList().isEmpty()) {
	 * 
	 * documentDetailsDTO =
	 * CreateRODMapper.getAcknowledgeDocumentList(query.getResultList()); }
	 * 
	 * 
	 * 
	 * return documentDetailsDTO; }
	 */

	public List<DocumentDetailsDTO> getDocumentDetailsDTO(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<DocumentDetailsDTO> documentDetailsDTO = null;
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			List<DocAcknowledgement> docAcknowlegementList = (List<DocAcknowledgement>) query
					.getResultList();
			documentDetailsDTO = new ArrayList<DocumentDetailsDTO>();
			for (DocAcknowledgement docAcknowledgement : docAcknowlegementList) {
				if (null != docAcknowledgement.getRodKey()) {
					DocumentDetailsDTO documentDetailDTO = new DocumentDetailsDTO();
					documentDetailDTO.setRodKey(docAcknowledgement.getRodKey());
					documentDetailDTO.setHospitalizationFlag(docAcknowledgement
							.getHospitalisationFlag());
					documentDetailDTO
							.setPreHospitalizationFlag(docAcknowledgement
									.getPreHospitalisationFlag());
					documentDetailDTO
							.setPostHospitalizationFlag(docAcknowledgement
									.getPostHospitalisationFlag());
					documentDetailDTO
							.setPartialHospitalizationFlag(docAcknowledgement
									.getPartialHospitalisationFlag());
					documentDetailDTO.setLumpSumAmountFlag(docAcknowledgement
							.getLumpsumAmountFlag());
					documentDetailDTO
							.setAddOnBenefitsHospitalCashFlag(docAcknowledgement
									.getHospitalCashFlag());
					documentDetailDTO
							.setAddOnBenefitsPatientCareFlag(docAcknowledgement
									.getPatientCareFlag());
					documentDetailDTO
					.setHospitalCashFlag(docAcknowledgement
							.getProdHospBenefitFlag());
					documentDetailsDTO.add(documentDetailDTO);
				}
			}
		}
		return documentDetailsDTO;
	}

	/**
	 * During bug fixing activity , below method implementation logic was
	 * changed. Planned to change method signature also, but since without
	 * checking impact analysis this can't be changed. Hence retaining the same.
	 * 
	 * */
	public DocumentDetailsDTO getPreviousRODDetailsForClaim(Long claimKey,DocumentDetailsDTO docDetailsDTO) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findMaxRODKey");
		query.setParameter("claimkey", claimKey);
		DocumentDetailsDTO docDTO = null;
		if (null != query.getSingleResult()) {
			docDTO = new DocumentDetailsDTO();
			Long rodKey = (Long) query.getSingleResult();
			Query query1 = entityManager
					.createNamedQuery("Reimbursement.findByKey");
			query1.setParameter("primaryKey", rodKey);
			Reimbursement objReimbursement = (Reimbursement) query1
					.getSingleResult();
			
			/*if(docDetailsDTO.getDocumentsReceivedFrom() != null 
					&& docDetailsDTO.getDocumentsReceivedFrom().getId().
					equals(objReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()))
			{*/
				docDTO.setAccountNo(objReimbursement.getAccountNumber());
			//}
			docDTO.setPayableAt(objReimbursement.getPayableAt());

			docDTO.setEmailId(objReimbursement.getPayeeEmailId());
			docDTO.setPanNo(objReimbursement.getPanNumber());
			if(objReimbursement.getPayeeName() != null){
				SelectValue selValue = new SelectValue();
				// selValue.setId(id);
				selValue.setValue(objReimbursement.getPayeeName());
				docDTO.setPayeeName(selValue);
			}
			docDTO.setReasonForChange(objReimbursement.getReasonForChange());
			docDTO.setLegalFirstName(objReimbursement.getLegalHeirFirstName());
			docDTO.setPaymentModeFlag(objReimbursement.getPaymentModeId());
			docDTO.setPayModeChangeReason(objReimbursement.getPayModeChangeReason());
			if (null != objReimbursement.getBankId()) {
				BankMaster masBank = getBankDetails(objReimbursement
						.getBankId());
				docDTO.setBankId(masBank.getKey());
				docDTO.setBankName(masBank.getBankName());
				docDTO.setCity(masBank.getCity());
				docDTO.setIfscCode(masBank.getIfscCode());
			}
		}
		return docDTO;
	}
	
	public DocumentDetailsDTO getPreviousRODDetailsForPayment(Long claimKey,DocumentDetailsDTO docDetailsDTO) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findMaxRODKey");
		query.setParameter("claimkey", claimKey);
		DocumentDetailsDTO docDTO = null;
		if (null != query.getSingleResult()) {
			docDTO = new DocumentDetailsDTO();
			Long rodKey = (Long) query.getSingleResult();
			Query query1 = entityManager
					.createNamedQuery("Reimbursement.findByKey");
			query1.setParameter("primaryKey", rodKey);
			Reimbursement objReimbursement = (Reimbursement) query1
					.getSingleResult();
			
			/*if(docDetailsDTO.getDocumentsReceivedFrom() != null 
					&& docDetailsDTO.getDocumentsReceivedFrom().getId().
					equals(objReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()))
			{*/
				docDTO.setAccountNo(objReimbursement.getAccountNumber());
			//}
			docDTO.setPayableAt(objReimbursement.getPayableAt());

			docDTO.setEmailId(objReimbursement.getPayeeEmailId());
			docDTO.setPanNo(objReimbursement.getPanNumber());
			SelectValue selValue = new SelectValue();
			// selValue.setId(id);
			selValue.setValue(objReimbursement.getPayeeName());
			docDTO.setPayeeName(selValue);
			docDTO.setReasonForChange(objReimbursement.getReasonForChange());
			docDTO.setLegalFirstName(objReimbursement.getLegalHeirFirstName());
			docDTO.setPaymentModeFlag(objReimbursement.getPaymentModeId());
			docDTO.setPayModeChangeReason(objReimbursement.getPayModeChangeReason());
			if (null != objReimbursement.getBankId()) {
				BankMaster masBank = getBankDetails(objReimbursement
						.getBankId());
				docDTO.setBankId(masBank.getKey());
				docDTO.setBankName(masBank.getBankName());
				docDTO.setCity(masBank.getCity());
				docDTO.setIfscCode(masBank.getIfscCode());
			}
		}
		return docDTO;
	}
	

	public Reimbursement getPreviousRODDetails(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = query.getResultList();
		
		Reimbursement reimbursement = null;
		
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			entityManager.refresh(reimbursementList.get(0));
//			reimbursement = reimbursementList.get(0);
			for(int i=0; i <reimbursementList.size(); i++) {
				entityManager.refresh(reimbursementList.get(i));
				if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursementList.get(i).getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursementList.get(i).getStatus().getKey())) {
					entityManager.refresh(reimbursementList.get(i));
					reimbursement = reimbursementList.get(i);
					break;
				} 
			}
		}
		return reimbursement;

	}
	
	public List<Reimbursement> getListOfPreviousRODDetails(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = query.getResultList();
		
		List<Reimbursement> reimbursement = new ArrayList<Reimbursement>();
		
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			entityManager.refresh(reimbursementList.get(0));
//			reimbursement = reimbursementList.get(0);
			for(int i=0; i <reimbursementList.size(); i++) {
				entityManager.refresh(reimbursementList.get(i));
				if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursementList.get(i).getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursementList.get(i).getStatus().getKey())) {
					entityManager.refresh(reimbursementList.get(i));
					reimbursement.add(reimbursementList.get(i));
				} 
			}
		}
		return reimbursement;

	}
	
	public Boolean isHavingEarlierRod(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = query.getResultList();
		
		
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			Integer size = reimbursementList.size();
			if(!size.equals(1)) {
				return true;
			}
			return false;
		}
		return false;

	}
	
	
	

	public BankMaster getBankDetails(Long key) {
		BankMaster masBank = null;
		Query query = entityManager.createNamedQuery("BankMaster.findByKey");
		query = query.setParameter("key", key);
		List<BankMaster> bankMasList = query.getResultList();
		if (null != bankMasList && !bankMasList.isEmpty()) {
			for (BankMaster bankMaster : bankMasList) {
				masBank = bankMaster;
			}
		}
		// BankMaster masBank = (BankMaster)query.getSingleResult();
		return masBank;
	}

	public Long getLatestDocAcknowledgementKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findMaxDocAckKey");
		query.setParameter("rodKey", rodKey);
		Long docAckKey = (Long) query.getSingleResult();
		return docAckKey;
	}
	
	public DocAcknowledgement getLatestDocAcknowledgement(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByLatestAcknowledge");
		query.setParameter("rodKey", rodKey);
		List<DocAcknowledgement> docAckList = (List<DocAcknowledgement>) query.getResultList();
		
		if(null != docAckList && !docAckList.isEmpty())
		{
			entityManager.refresh(docAckList.get(0));
			return docAckList.get(0);
		}
		return null;
	}

	public Long getACknowledgeNumberCountByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.CountAckByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
	}
	
	public Reimbursement getLatestReimbursementByRodNumberwise(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByRodNumberWise");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursement = (List<Reimbursement>) query.getResultList();
		if(reimbursement != null && ! reimbursement.isEmpty()){
			return reimbursement.get(0);
		}
		return null;
	}

	public ReceiptOfDocumentsDTO getBillClassificationFlagDetails(
			Long claimKey, ReceiptOfDocumentsDTO rodDTO) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			int iSize = reimbursementList.size();
			Reimbursement rod = reimbursementList.get(iSize - 1);
			rodDTO.setHospitalizationFlag(rod.getDocAcknowLedgement()
					.getHospitalisationFlag());
			rodDTO.setPartialHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPartialHospitalisationFlag());
			rodDTO.setPostHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPostHospitalisationFlag());
			rodDTO.setPreHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPreHospitalisationFlag());
//			//added for new product
//			rodDTO.getDocumentDetails().setHospitalCashFlag(rod.getDocAcknowLedgement()
//					.getProdHospBenefitFlag());
		}
		return rodDTO;
	}

	
	public ReceiptOfDocumentsDTO getBenefitFlagDetails(
			Long claimKey, ReceiptOfDocumentsDTO rodDTO) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			int iSize = reimbursementList.size();
			Reimbursement rod = reimbursementList.get(iSize - 1);			
			
			if(rod.getDocAcknowLedgement().getBenifitFlag().equalsIgnoreCase(SHAConstants.DEATH_FLAGS)){
				rodDTO.setDeath(true);
			}	
			else if(rod.getDocAcknowLedgement().getBenifitFlag().equalsIgnoreCase(SHAConstants.PPD)){
				rodDTO.setPermanentPartialDisability(true);
			}
			else if(rod.getDocAcknowLedgement().getBenifitFlag().equalsIgnoreCase(SHAConstants.PTD)){
				rodDTO.setPermanentTotalDisability(true);
			}
			else if(rod.getDocAcknowLedgement().getBenifitFlag().equalsIgnoreCase(SHAConstants.TTD)){
				rodDTO.setTemporaryTotalDisability(true);
			}
			else {
				rodDTO.setHospitalExpensesCover(true);
			}
			rodDTO.setHospitalizationFlag(rod.getDocAcknowLedgement()
					.getHospitalisationFlag());
			rodDTO.setPartialHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPartialHospitalisationFlag());
			rodDTO.setPostHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPostHospitalisationFlag());
			rodDTO.setPreHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPreHospitalisationFlag());
		}
		return rodDTO;
	}
	public ReceiptOfDocumentsDTO getBillClassificationFlagDetails(
			Long claimKey, ReceiptOfDocumentsDTO rodDTO,EntityManager em) {
		this.entityManager = em;
	return 	getBillClassificationFlagDetails(claimKey,rodDTO);
	
	}	
	
	
	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		List<Reimbursement> rodList = new ArrayList<Reimbursement>();
		for (Reimbursement reimbursement2 : reimbursementList) {
			/**The below condition added  for JIRA - GALAXYMAIN-11084 **/
			if(!(ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())) && 
					! reimbursement2.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) /*&&
					(ReferenceTable.getRevisedCriticareProducts().containsKey(reimbursement2.getClaim().getIntimation().getPolicy().getProduct().getKey()) &&
							!(ReferenceTable.getRejectedRODKeys().containsKey(reimbursement2.getStatus().getKey())))*/){
			
				rodList.add(reimbursement2);
				/*if(isCloseClaimValid){
					isCloseClaimValid = false;
					view.showErrorPopUp("Closing of claim is not possible. Please Process/Cancel and then trying Closing");
				}*/
			}
		}
		return rodList;
		//Boolean validationFlag = false;
		/*Query query = entityManager.createNamedQuery("Reimbursement.findLatestNonCanceledRODACKByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		query = query.setParameter("rodcancelstatusId", ReferenceTable.);
		query = query.setParameter("ackcancelstatusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<Reimbursement> rodList = query.getResultList();	
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}
		return  rodList;*/
	}
	
	public List<Reimbursement> getNonCancelledReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		List<Reimbursement> rodList = new ArrayList<Reimbursement>();
		for (Reimbursement reimbursement2 : reimbursementList) {
			if(!(ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())) || ! reimbursement2.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)){
				rodList.add(reimbursement2);
				/*if(isCloseClaimValid){
					isCloseClaimValid = false;
					view.showErrorPopUp("Closing of claim is not possible. Please Process/Cancel and then trying Closing");
				}*/
			}
		}
		return rodList;
		//Boolean validationFlag = false;
		/*Query query = entityManager.createNamedQuery("Reimbursement.findLatestNonCanceledRODACKByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		query = query.setParameter("rodcancelstatusId", ReferenceTable.);
		query = query.setParameter("ackcancelstatusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<Reimbursement> rodList = query.getResultList();	
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}
		return  rodList;*/
	}


	/*
	 * public void saveUploadDocuments(List<UploadDocumentDTO> uploadDocsList) {
	 * 
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		this.entityManager = entityManager;
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}	
	

	/*public void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery, String sendToWhere, Boolean isReconsideration,Claim claim) {
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("APPROVE");
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());
		RODType rodType = new RODType();
		rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		
		rodType.setKey(reimbursement.getKey());
		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
		
		PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
		if(paymentInfo == null){
			paymentInfo = new PaymentInfoType();
		}
		paymentInfo.setClaimedAmount(rodDTO.getTotalClaimedAmount() != null ? rodDTO.getTotalClaimedAmount():0d);
		
		
//		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		
		

		
		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

*/
	
	
/*	public List<DMSDocumentDetailsDTO> getDocumentDetailsData(String intimationNumber)
	{
		//Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNo");
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoOrderByCreatedDate");
		query = query.setParameter("intimationNumber", intimationNumber);
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);

		}
		
		if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
				&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
			

			*//**
			 * Instead of below line, add 
			 * docReceiptAck.setPartialhospitalization(true);
			 * *//*
			*//***
			 * Below line added for hospitalization repeat flow.
			 * Repeat should follow normal flow from create rod, bill entry
			 * zmr. 
			 *//*
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);

			//payloadBO.getDocReceiptACK().setHospitalization(true);
			//don't comment below line.
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		
		

		List<DMSDocumentDetailsDTO> finalDMSDataList = new ArrayList<DMSDocumentDetailsDTO>();
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				documentDetails.setDmsRestApiURL(dmsAPIUrl);	
				if(null != documentDetails.getReimbursementNumber())
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());;
				
				}
				else
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
				}
					
				
				
				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
				{
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}
				
				*//**
				 * If ROD is cancelled, then documents corresponding to that ROD number
				 * will not be visible in claims DMS. Added for ticket 813. 
				 * **//*
				
				*//**
				 * Post the above ticket , then came the enhancement R0254.
				 			 * R0254 - To retain uploaded documents and bill entry values
							 *  even if the rod is cancelled. Hence if, rod is cancelled 
							 *  then those documents should also reflect in claims dms.
							 *  Hence below code is commented. 
				 * This enhancement is complete in contrary with above ticket.
				 * Hence commenting the below code which was added as a part
				 * of ticket 813.
				 * 
				 * *//*
				// 813 ticket fix starts.
				Reimbursement reimbursementObj = getReimbursementObject(documentDetails.getReimbursementNumber());
				
				if(null != reimbursementObj)
				{
				
					Map<Long,Long> cancelMap = ReferenceTable.getCancelRODKeys();
					if(null != cancelMap)
					{
						if(null != reimbursementObj.getStatus())
						{
							Long key = cancelMap.get(reimbursementObj.getStatus().getKey());
							*//**
							 *  
							 * 
							 * *//*
							if(null == key)
							{
								finalDMSDataList.add(documentDetails);
							}
						}
					}
				}
				else
				{
					finalDMSDataList.add(documentDetails);
				}
				// 813 ticket fix ends.

		
		
		CustomerType customerType = new CustomerType();
		customerType.setTreatmentType(rodDTO.getTreatmentType());
		ClassificationType classification = null;
		if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
			Stage stage = entityManager.find(Stage.class, ReferenceTable.CREATE_ROD_STAGE_KEY);
			 classification = payloadBO.getClassification();
			classification.setSource(stage.getStageName());
			
			if(isQuery){
				classification.setType(SHAConstants.QUERY_REPLY);
				classification.setSource(SHAConstants.QUERY_REPLY);

			}
			if(isReconsideration) {
				classification.setSource(SHAConstants.RECONSIDERATION);
			}
			
			payloadBO.setClassification(classification);
		}
		
		ClaimRequestType claimRequest = payloadBO.getClaimRequest();
		claimRequest.setResult("APPROVE");
		
		if(isReconsideration){
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			classification.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
		}else if(isQuery){
			classification.setType(SHAConstants.QUERY_REPLY);
			

		}
		else
		{
			claimRequest.setIsReconsider(false);
			classification.setType(SHAConstants.TYPE_FRESH);
			claimRequest.setReimbReqBy(null);
		}
		
		
		
		ClaimType claimType = payloadBO.getClaim();
		if(claimType != null && claim != null && claim.getClaimType() != null && claim.getClaimType().getValue() != null){
			claimType.setClaimType(claim.getClaimType().getValue().toUpperCase());
		}
		
		if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			claimRequest.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
			claimRequest.setIsReconsider(true);
			//rodType.setAckNumber(reimbursement.getRodNumber());
		}
		else
		{
			claimRequest.setOption("");
			//claimRequest.setIsReconsider(false);
		}
		
		if(sendToWhere.equals(ReferenceTable.DIRECT_TO_BILLING)) {
			claimRequest.setReimbReqBy("BILLING");
			rodType.setStatus(SHAConstants.NEW);
		} else if(sendToWhere.equals(ReferenceTable.DIRECT_TO_FINANCIAL)) {
			claimRequest.setReimbReqBy("FA");
			rodType.setStatus(SHAConstants.NEW);
		}
		payloadBO.setClaimRequest(claimRequest);

		payloadBO.setRod(rodType);
		payloadBO.setCustomer(customerType);
		payloadBO.setPaymentInfo(paymentInfo);
		
		RRCType rrc = payloadBO.getRrc();
		if(rrc == null){
		   rrc = new RRCType();
		}
			Date currentDate = reimbursement.getCreatedDate();
			rrc.setToDate(SHAUtils.changeDatetoString(currentDate));

		payloadBO.setRrc(rrc);
		
		ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
		if(processActor == null){
			processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}else if(processActor != null && processActor.getEscalatedByUser() == null){
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}
		
		
		if(! payloadBO.getDocReceiptACK().isIsBillAvailable()){
			Boolean shouldSkipZMR = false;
			shouldSkipZMR = shouldSkipZMR(claim);
			if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				shouldSkipZMR = false;
//				payloadBO.getClaimRequest().setReimbReqBy("BILLING");
//				payloadBO.getDocReceiptACK().setHospitalization(false);
//				payloadBO.getDocReceiptACK().setPartialhospitalization(false);
			}
			if(shouldSkipZMR) {
				payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
			}
		}
		
		// payloadBO.setDocReceiptACK(receiptAckType);
		if(payloadBO.getClaim() != null && payloadBO.getClaim().getClaimType() != null && !(null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
				&& rodDTO.getDocumentDetails().getHospitalizationRepeat())) {
			payloadBO.getClaim().setClaimType(payloadBO.getClaim().getClaimType().toUpperCase());
		}
		
		if(! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
				&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
				&& ! payloadBO.getDocReceiptACK().isLumpsumamount()){

			   claimRequest.setClientType(SHAConstants.MEDICAL);
			   payloadBO.getDocReceiptACK().setPartialhospitalization(true);

		}
		
		humanTaskForROD.setPayload(payloadBO);
		try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
//		submitBpmForCancelAcknowledgement(rodDTO);        // retair the task from cancel acknowledgement
		
//			stopCashlessReminderLetter(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
//			stopCashlessReminderProcess(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
			
			stopCashlessReminderLetter(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
			stopCashlessReminderProcess(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());

			PayloadBOType dupPayload = new PayloadBOType();
			IntimationType intimationBO = new IntimationType();
			intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
			dupPayload.setIntimation(intimationBO);
			stopReimbReminderLetter(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
			stopReimbReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());

		if(isQuery)
		{
			dupPayload = new PayloadBOType();
			intimationBO = new IntimationType();
			intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
			dupPayload.setIntimation(intimationBO);
			stopQueryReminderLetter(dupPayload, rodDTO.getStrUserName(),rodDTO.getStrPassword());
			stopQueryReplyReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR CREATE ROD ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
				BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForROD.getNumber(), SHAConstants.SYS_RELEASE);
				rodSubmitTask.execute("claimshead", humanTaskForROD);

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}
	}
	*/
	
	public void paSubmitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery, String sendToWhere, Boolean isReconsideration,Claim claim) {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
	/*RODType rodType = new RODType();
	rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());	
	rodType.setKey(reimbursement.getKey());*/
		
	wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY, reimbursement.getKey() );
	List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

	if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
			if (null != uploadDocumentDTO.getFileType()
					&& null != uploadDocumentDTO.getFileType().getValue()
					&& uploadDocumentDTO.getFileType().getValue()
							.contains("Bill")) {
				//payloadBO.getDocReceiptACK().setIsBillAvailable(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
				break;
			} else {
				//payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				//payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
				wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "N");
			}
		}
	}
	
	/*PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
	if(paymentInfo == null){
		paymentInfo = new PaymentInfoType();
	}
	paymentInfo.setClaimedAmount(rodDTO.getTotalClaimedAmount() != null ? rodDTO.getTotalClaimedAmount():0d);

	IntimationType intimationType = payloadBO.getIntimation();
	if(null == intimationType)
	{
		intimationType = new IntimationType();
	}*/

	//IntimationType intimationType = new IntimationType();


	/*if(null != rodDTO.getDocumentDetails().getAccidentOrDeath() && rodDTO.getDocumentDetails().getAccidentOrDeath())
	{
		intimationType.setReason(SHAConstants.ACCIDENT_FLAG);
	}
	else
	{
		intimationType.setReason(SHAConstants.DEATH_FLAG);
	}
	
	payloadBO.setIntimation(intimationType);
	ProductInfoType productInfo = payloadBO.getProductInfo();*/
	
	/*if(productInfo ==null){
		productInfo = new ProductInfoType();
		if(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null){
			productInfo.setProductId(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().toString());
			productInfo.setProductName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getValue());
		}
		
	}*/
	/*productInfo.setLob("HEALTH");*/
//	productInfo.setLobType(claim != null ? claim.getProcessClaimType() : reimbursement.getProcessClaimType());
	
	/*productInfo.setLobType(reimbursement != null ? reimbursement.getProcessClaimType() : reimbursement.getProcessClaimType());
	productInfo.setLob(SHAConstants.PA_LOB);*/
	
	wrkFlowMap.put(SHAConstants.LOB_TYPE, reimbursement != null ? reimbursement.getProcessClaimType() : reimbursement.getProcessClaimType());
	wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
	
	if((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && rodDTO.getDocumentDetails().getHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) || (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
		//productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
		wrkFlowMap.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
	}
	
//	payloadBO.getDocReceiptACK().setIsBillAvailable(true);
	
	

	
	/***
	 * The below condition check was added , since the record which was
	 * submitted with hospitalization during ROD level didn't move to zonal
	 * medical review screen.
	 */
	/*if (null != rodDTO.getDocumentDetails().getHospitalization()
			&& rodDTO.getDocumentDetails().getHospitalization()) {
		payloadBO.getDocReceiptACK().setHospitalization(true);
	} else*/ {
	//	payloadBO.getDocReceiptACK().setHospitalization(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
		
		
	}
	
/*	if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
			&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
		payloadBO.getDocReceiptACK().setPartialhospitalization(true);
	} else*/ {
		//payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
		
	}
	/*if (null != rodDTO.getDocumentDetails().getPreHospitalization()
			&& rodDTO.getDocumentDetails().getPreHospitalization()) {
		payloadBO.getDocReceiptACK().setPrehospitalization(true);
	} else */{
		//payloadBO.getDocReceiptACK().setPrehospitalization(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "N");
	}

	/*if (null != rodDTO.getDocumentDetails().getPostHospitalization()
			&& rodDTO.getDocumentDetails().getPostHospitalization()) {
		payloadBO.getDocReceiptACK().setPosthospitalization(true);
	} else */{
		//payloadBO.getDocReceiptACK().setPosthospitalization(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "N");
	}

	/*if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
			&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
		payloadBO.getDocReceiptACK().setLumpsumamount(true);
		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
	} else*/ {
		//payloadBO.getDocReceiptACK().setLumpsumamount(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "N");
	}

	/*if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
			&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
		payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
	} else*/ {
		//payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "N");
	}

	/*if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
			&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
		payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
	} else*/ {
		//payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "N");
	}
	
	//payloadBO.getDocReceiptACK().setPartialhospitalization(false);
	wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
	
	if(null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization()) {
	//	payloadBO.getDocReceiptACK().setHospitalization(true);
		wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
	}
	
	if(null != rodDTO.getDocumentDetails().getPartialHospitalization() && rodDTO.getDocumentDetails().getPartialHospitalization()) {
		//payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");
	}
	
	/*if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
			&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
		

		*//**
		 * Instead of below line, add 
		 * docReceiptAck.setPartialhospitalization(true);
		 * *//*
		*//***
		 * Below line added for hospitalization repeat flow.
		 * Repeat should follow normal flow from create rod, bill entry
		 * zmr. 
		 *//*
		payloadBO.getDocReceiptACK().setPartialhospitalization(true);

		//payloadBO.getDocReceiptACK().setHospitalization(true);
		//don't comment below line.
//		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
	}*/ /*else {
		payloadBO.getDocReceiptACK().setHospitalization(false);
	}*/
	
	
	
	
	/*CustomerType customerType = new CustomerType();
	customerType.setTreatmentType(rodDTO.getTreatmentType());
	ClassificationType classification = null;*/
	wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, rodDTO.getTreatmentType());
	
	if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
		Stage stage = entityManager.find(Stage.class, ReferenceTable.CREATE_ROD_STAGE_KEY);
		/* classification = payloadBO.getClassification();
		classification.setSource(stage.getStageName());*/
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.SOURCE_CREATE_ROD);
		
		if(isQuery){
			/*classification.setType(SHAConstants.QUERY_REPLY);
			classification.setSource(SHAConstants.QUERY_REPLY);*/
			wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.QUERY_REPLY);
		}
		if(isReconsideration) {
			//classification.setSource(SHAConstants.RECONSIDERATION);
			wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.RECONSIDERATION);
			wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
		}
		
		//payloadBO.setClassification(classification);
	}
	
	/*ClaimRequestType claimRequest = payloadBO.getClaimRequest();
	claimRequest.setResult("APPROVE");*/
	
	if(isReconsideration){
		//claimRequest.setIsReconsider(false);
		//claimRequest.setClientType(SHAConstants.MEDICAL);
		//classification.setType(SHAConstants.RECONSIDERATION);
		//claimRequest.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);		
		wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.RECONSIDERATION);
		wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
	}else if(isQuery){
		//classification.setType(SHAConstants.QUERY_REPLY);
		wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.QUERY_REPLY);
		

	}
	else
	{
		//claimRequest.setIsReconsider(false);
		//classification.setType(SHAConstants.TYPE_FRESH);
		wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"N");
		wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.TYPE_FRESH);
		//claimRequest.setReimbReqBy(null);
	}
	
	
	
	//ClaimType claimType = payloadBO.getClaim();
	if(claim != null && claim.getClaimType() != null && claim.getClaimType().getValue() != null){
		//claimType.setClaimType(claim.getClaimType().getValue().toUpperCase());
		wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
	}
	
	if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
	{
		/*claimRequest.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
		claimRequest.setIsReconsider(true);*/		
		wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"Y");
		wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
		//rodType.setAckNumber(reimbursement.getRodNumber());
	}
	else
	{
		//claimRequest.setOption("");
		//claimRequest.setIsReconsider(false);
		wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
	}
	
/*	if(sendToWhere.equals(ReferenceTable.DIRECT_TO_BILLING)) {
		claimRequest.setReimbReqBy("BILLING");
		rodType.setStatus(SHAConstants.NEW);
	} else if(sendToWhere.equals(ReferenceTable.DIRECT_TO_FINANCIAL)) {
		claimRequest.setReimbReqBy("FA");
		rodType.setStatus(SHAConstants.NEW);
	}
	payloadBO.setClaimRequest(claimRequest);

	payloadBO.setRod(rodType);
	payloadBO.setCustomer(customerType);
	payloadBO.setPaymentInfo(paymentInfo);
	payloadBO.setProductInfo(productInfo);
	
	RRCType rrc = payloadBO.getRrc();
	if(rrc == null){
		rrc = new RRCType();
	}
	Date currentDate = new Date();
	rrc.setToDate(SHAUtils.changeDatetoString(currentDate));
	payloadBO.setRrc(rrc);
	
	ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
	if(processActor == null){
		processActor = new ProcessActorInfoType();
		processActor.setEscalatedByUser("");
		payloadBO.setProcessActorInfo(processActor);
	}else if(processActor != null && processActor.getEscalatedByUser() == null){
		processActor.setEscalatedByUser("");
		payloadBO.setProcessActorInfo(processActor);
	}
	
	*/
	/*if(! payloadBO.getDocReceiptACK().isIsBillAvailable()){
		Boolean shouldSkipZMR = false;
		shouldSkipZMR = shouldSkipZMR(claim);
		if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			shouldSkipZMR = false;
//			payloadBO.getClaimRequest().setReimbReqBy("BILLING");
//			payloadBO.getDocReceiptACK().setHospitalization(false);
//			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if(shouldSkipZMR) {
			payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
		}
	}
	
	// payloadBO.setDocReceiptACK(receiptAckType);
	if(payloadBO.getClaim() != null && payloadBO.getClaim().getClaimType() != null && !(null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
			&& rodDTO.getDocumentDetails().getHospitalizationRepeat())) {
		payloadBO.getClaim().setClaimType(payloadBO.getClaim().getClaimType().toUpperCase());
	}
	
	if(! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
			&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
			&& ! payloadBO.getDocReceiptACK().isLumpsumamount()){

		   claimRequest.setClientType(SHAConstants.MEDICAL);
		   payloadBO.getDocReceiptACK().setPartialhospitalization(true);

	}
	
	humanTaskForROD.setPayload(payloadBO);*/
	try{
//	rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
//	submitBpmForCancelAcknowledgement(rodDTO);        // retair the task from cancel acknowledgement
	
//		stopCashlessReminderLetter(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
//		stopCashlessReminderProcess(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
		
		wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_CREATE_ROD);
		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, getClaimedAmt(reimbursement.getDocAcknowLedgement()));
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	
		/*
		stopCashlessReminderLetter(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
		stopCashlessReminderProcess(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());

		PayloadBOType dupPayload = new PayloadBOType();
		IntimationType intimationBO = new IntimationType();
		intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
		dupPayload.setIntimation(intimationBO);
		stopReimbReminderLetter(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
		stopReimbReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());*/
		DBCalculationService dbCalculationService = new DBCalculationService();
		dbCalculationService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId(),SHAConstants.OTHERS);
	if(isQuery)
	{
//		dupPayload = new PayloadBOType();
//		intimationBO = new IntimationType();
//		intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
//		dupPayload.setIntimation(intimationBO);
//		stopQueryReminderLetter(dupPayload, rodDTO.getStrUserName(),rodDTO.getStrPassword());
//		stopQueryReplyReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
		dbCalculationService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId(),SHAConstants.OTHERS);
	}
	}catch(Exception e){
		e.printStackTrace();
		log.error(e.toString());
		
		log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
		
		try {
			log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR CREATE ROD ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
			//BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForROD.getNumber(), SHAConstants.SYS_RELEASE);
			//rodSubmitTask.execute("claimshead", humanTaskForROD);

		} catch(Exception u) {
			log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
		}
	}
	}
	
	/*public void submitBpmForCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO){
		
		PayloadBOType payloadBOType = new PayloadBOType();
		
		String userName = rodDTO.getStrUserName();
		String passWord = rodDTO.getStrPassword();
		
		String intimationId = rodDTO.getClaimDTO().getNewIntimationDto().getIntimationId();
		IntimationType intimation = new IntimationType();
		intimation.setIntimationNumber(intimationId);
		payloadBOType.setIntimation(intimation);
		
		CancelAcknowledgementTask cancelTask = BPMClientContext.getCancelAcknowledgementTask(userName, passWord);
		
		Pageable pageable = new Pageable(1, 10, true);
		
		PagedTaskList taskList = cancelTask.getTasks(userName,pageable,payloadBOType);
		
		taskList = cancelTask.getTasks(userName,pageable,payloadBOType);
		
		if(taskList != null){
		List<HumanTask> humanTaskList = taskList.getHumanTasks();
		   if(humanTaskList != null){
			   for (HumanTask humanTask : humanTaskList) {
			    PayloadBOType payload = humanTask.getPayload();
				
				humanTask.setOutcome("APPROVE");

				payload.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
				
				QueryType queryType = new QueryType();
				payload.setQuery(queryType);
				humanTask.setPayload(payload);
				
				SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask = BPMClientContext.submitCancelAcknowledgementTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
				
				try {
					submitCancelAcknowledgementTask.execute(rodDTO.getStrUserName(), humanTask);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.toString());
				}
			}
		   }
		}
		 
		 
		
	}
	*/
	/*public DocAcknowledgement submitCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO){
		Long docAcknowledgementKey = rodDTO.getDocumentDetails().getDocAcknowledgementKey();
		
		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(docAcknowledgementKey);
		
		
		if(docAcknowledgement != null){
			Status status = entityManager.find(Status.class, ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
			docAcknowledgement.setStatus(status);
			
			if(rodDTO.getDocumentDetails().getCancelAcknowledgementReason() != null && rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId() != null){
				MastersValue cancelReason = entityManager.find(MastersValue.class, rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId());
				docAcknowledgement.setCancelReason(cancelReason);
			}
			docAcknowledgement.setCancelRemarks(rodDTO.getDocumentDetails().getCancelAcknowledgementRemarks());
			
			entityManager.merge(docAcknowledgement);
			entityManager.flush();
			log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
			
			if(docAcknowledgement.getRodKey() != null){
				ReimbursementQuery reimbursementQuery = getReimbursementQueryOnlyReceived(docAcknowledgement.getRodKey());
				if(reimbursementQuery != null){
					reimbursementQuery.setQueryReply("N");
					entityManager.merge(reimbursementQuery);
					entityManager.flush();
				}
			}
		}

		return docAcknowledgement;
	}*/
	
	public DocAcknowledgement submitCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO){
		Long docAcknowledgementKey = rodDTO.getDocumentDetails().getDocAcknowledgementKey();
		
		DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(docAcknowledgementKey);
		
		if(null != docAcknowledgement)
		{
			//Query reply and reconsider ack cancell IMSSUPPOR-33655
			if(docAcknowledgement.getRodKey()!=null){
				Reimbursement reimbursementObj = getReimbursementObjectByKey(docAcknowledgement.getRodKey());
				ReimbursementQuery reimbursementQuery1 = getReimbursementQueryOnlyReceived(docAcknowledgement.getRodKey());
				if(((reimbursementQuery1!=null && reimbursementQuery1.getDocAcknowledgement()!=null &&reimbursementQuery1.getDocAcknowledgement().getKey().equals(docAcknowledgement.getKey())
						&&reimbursementQuery1.getQueryReply().equalsIgnoreCase("Y")) 
						|| (docAcknowledgement.getReconsiderationRequest()!=null && docAcknowledgement.getReconsiderationRequest().equalsIgnoreCase("Y"))  )
						&& reimbursementObj.getStatus()!=null && !ReferenceTable.getCancelRODKeys().containsKey(reimbursementObj.getStatus().getKey())){
							
							Status status = entityManager.find(Status.class, ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
							docAcknowledgement.setStatus(status);
							
							if(rodDTO.getDocumentDetails().getCancelAcknowledgementReason() != null && rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId() != null){
								MastersValue cancelReason = entityManager.find(MastersValue.class, rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId());
								docAcknowledgement.setCancelReason(cancelReason);
							}
							docAcknowledgement.setCancelRemarks(rodDTO.getDocumentDetails().getCancelAcknowledgementRemarks());
							docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							docAcknowledgement.setModifiedBy(rodDTO.getStrUserName());
							entityManager.merge(docAcknowledgement);
							entityManager.flush();
							log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
							
							if(docAcknowledgement.getRodKey() != null){
								ReimbursementQuery reimbursementQuery = getReimbursementQueryOnlyReceived(docAcknowledgement.getRodKey());
								if(reimbursementQuery != null){
									reimbursementQuery.setQueryReply("N");
									reimbursementQuery.setQueryReplyDate(null);
									entityManager.merge(reimbursementQuery);
									entityManager.flush();
								}
							}
							
			}else{
				if(docAcknowledgement.getRodKey()!=null){
					List<DocAcknowledgement> docAckList = getDocAcknowledgeBasedOnROD(docAcknowledgement.getRodKey());
					if(null != docAckList && !docAckList.isEmpty())
					{
						for (DocAcknowledgement docAcknowledgement1 : docAckList) {
							
								ClaimPayment claimPayment = getClaimPaymentByRodKey(docAcknowledgement1.getRodKey());
								if(claimPayment!=null && claimPayment.getStatusId()!=null && claimPayment.getStatusId().getKey()!=null
										&& !claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
//									if(!claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
										
										Status status = entityManager.find(Status.class, ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
										docAcknowledgement.setStatus(status);
										
										if(rodDTO.getDocumentDetails().getCancelAcknowledgementReason() != null && rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId() != null){
											MastersValue cancelReason = entityManager.find(MastersValue.class, rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId());
											docAcknowledgement.setCancelReason(cancelReason);
										}
										docAcknowledgement.setCancelRemarks(rodDTO.getDocumentDetails().getCancelAcknowledgementRemarks());
										docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
										docAcknowledgement.setModifiedBy(rodDTO.getStrUserName());
										entityManager.merge(docAcknowledgement);
										entityManager.flush();
										log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
										
										if(docAcknowledgement.getRodKey() != null){
											ReimbursementQuery reimbursementQuery = getReimbursementQueryOnlyReceived(docAcknowledgement.getRodKey());
											if(reimbursementQuery != null){
												reimbursementQuery.setQueryReply("N");
												reimbursementQuery.setQueryReplyDate(null);
												entityManager.merge(reimbursementQuery);
												entityManager.flush();
											}
										}
//								}
							}else{

							Status status = entityManager.find(Status.class, ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
							docAcknowledgement1.setStatus(status);
							
							if(rodDTO.getDocumentDetails().getCancelAcknowledgementReason() != null && rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId() != null){
								MastersValue cancelReason = entityManager.find(MastersValue.class, rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId());
								docAcknowledgement1.setCancelReason(cancelReason);
								docAcknowledgement1.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								docAcknowledgement1.setModifiedBy(rodDTO.getStrUserName());
							}
							docAcknowledgement1.setCancelRemarks(rodDTO.getDocumentDetails().getCancelAcknowledgementRemarks());
							
							entityManager.merge(docAcknowledgement1);
							entityManager.flush();
							log.info("------DocAcknowledgement------>"+docAcknowledgement1+"<------------");
							
							if(docAcknowledgement1.getRodKey() != null){
								ReimbursementQuery reimbursementQuery = getReimbursementQueryOnlyReceived(docAcknowledgement1.getRodKey());
								if(reimbursementQuery != null){
									reimbursementQuery.setQueryReply("N");
									entityManager.merge(reimbursementQuery);
									entityManager.flush();
								}
							}
						
						}
					}
					}
				}
			}
			
		}else{
				if(docAcknowledgement != null){//for single ack cancel
					Status status = entityManager.find(Status.class, ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
					docAcknowledgement.setStatus(status);
					
					if(rodDTO.getDocumentDetails().getCancelAcknowledgementReason() != null && rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId() != null){
						MastersValue cancelReason = entityManager.find(MastersValue.class, rodDTO.getDocumentDetails().getCancelAcknowledgementReason().getId());
						docAcknowledgement.setCancelReason(cancelReason);
					}
					docAcknowledgement.setCancelRemarks(rodDTO.getDocumentDetails().getCancelAcknowledgementRemarks());
					docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					docAcknowledgement.setModifiedBy(rodDTO.getStrUserName());
					entityManager.merge(docAcknowledgement);
					entityManager.flush();
					log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
					
					if(docAcknowledgement.getRodKey() != null){
						ReimbursementQuery reimbursementQuery = getReimbursementQueryOnlyReceived(docAcknowledgement.getRodKey());
						if(reimbursementQuery != null){
							reimbursementQuery.setQueryReply("N");
							reimbursementQuery.setQueryReplyDate(null);
							entityManager.merge(reimbursementQuery);
							entityManager.flush();
						}
					}
				}
				
			}
		}
	

		return docAcknowledgement;
		
	}

	
	public List<DocAcknowledgement> getDocAcknowledgeBasedOnROD(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			for (DocAcknowledgement docAcknowledgement : docAckList) {
				entityManager.refresh(docAcknowledgement);
			}
		}
		
		return docAckList;
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
	
	public ReimbursementQuery getReimbursementQueryOnlyReceived(Long rodKey){
		
		Query reimbqueryRecieved = entityManager
				.createNamedQuery("ReimbursementQuery.findByReceivedQueryForCancelAck");
		reimbqueryRecieved = reimbqueryRecieved
				.setParameter("reimbursementKey",
						rodKey);
		
		List<ReimbursementQuery> result = (List<ReimbursementQuery>)reimbqueryRecieved.getResultList();
		
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}
	
	public void updateClaimProvisionAmount(Long countOfAckByClaimKey, DocAcknowledgement docAcknowledgement){
		
		Claim claim = getClaimByKey(docAcknowledgement.getClaim().getKey());
		
		Boolean isPA = false;
		
		if(claim != null && claim.getLobId() != null && claim.getLobId().equals(ReferenceTable.PA_LOB_KEY)){
			isPA = true;
		}
		
		Reimbursement reimbursement = getReimbursementByAcknowledgementKey(docAcknowledgement.getKey());
		if(reimbursement != null){
			if(isPA){
				reimbursement.setCurrentProvisionAmt(0d);
				reimbursement.setBenApprovedAmt(0d);
				reimbursement.setOptionalApprovedAmount(0d);
				reimbursement.setAddOnCoversApprovedAmount(0d);
			}else{
				reimbursement.setCurrentProvisionAmt(0d);
				}
			entityManager.merge(reimbursement);
			entityManager.flush();
			log.info("------Reimbursement------>"+reimbursement+"<------------");
			
		}

		
		if(docAcknowledgement.getClaim().getClaimType() != null 
				&& docAcknowledgement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			if(countOfAckByClaimKey.equals(0l)){
				if(docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().equalsIgnoreCase("Y")){
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					claim.setCurrentProvisionAmount(preauth.getTotalApprovalAmount());
					claim.setClaimedAmount(0d);
					claim.setProvisionAmount(0d);
					entityManager.merge(claim);
					entityManager.flush();
					log.info("------Claim------>"+claim+"<------------");
				}else{
					claim.setCurrentProvisionAmount(0d);
					claim.setClaimedAmount(0d);
					claim.setProvisionAmount(0d);
					entityManager.merge(claim);
					entityManager.flush();
					log.info("------Claim------>"+claim+"<------------");
				}
			}
		}else if(docAcknowledgement.getClaim().getClaimType() != null 
				&& docAcknowledgement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			
			if(countOfAckByClaimKey.equals(0l)){
				
				claim.setCurrentProvisionAmount(0d);
				claim.setClaimedAmount(0d);
				claim.setProvisionAmount(0d);
				entityManager.merge(claim);
				entityManager.flush();
				log.info("------Claim------>"+claim+"<------------");
			}
		}
		
	}
	
public Reimbursement getReimbursementByAcknowledgementKey(Long ackkey){
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByAcknowledgement");
		query.setParameter("docAcknowledgmentKey", ackkey);
		
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();
		
		if(reimbursementList != null && ! reimbursementList.isEmpty()){
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		
		return null;
		
	}



   public Long getCountOfAckExceptCancel(Long a_key) {
	   
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.CountAckExceptCancel");
		query = query.setParameter("claimkey", a_key);
		query = query.setParameter("statusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
		
    }
   
   
   public List<DocAcknowledgement> getListOfAcknowledgement(Long claimKey){
	   
	    Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findAckByClaim");
		query = query.setParameter("claimkey", claimKey);
 
		List<DocAcknowledgement> resultList = (List<DocAcknowledgement>) query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			for (DocAcknowledgement docAcknowledgement : resultList) {
				entityManager.refresh(docAcknowledgement);
			}
			return resultList;
		}
		
		return null;
				
		
   }
	
	public Long getCountOfAckByClaimKey(Long a_key) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.CountAckByClaimKey");
		query = query.setParameter("claimkey", a_key);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
	}
	
	public Preauth getLatestPreauthByClaim(Long claimKey) {
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
	
	public void submitTaskToBPMForCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO,DocAcknowledgement docAcknowledgment) {/*
		
//		HumanTask humanTask = rodDTO.getHumanTask();
//		humanTask.setOutcome("APPROVE");
//		
//		PayloadBOType payload = humanTask.getPayload();
//		
//		payload.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//		Long cpuCode = 0l;
//		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
//		{
//			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
//		}
//		
//		if(null != payload.getClaimRequest())
//		{
//			payload.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
//				
//		}
//		else
//		{
//			ClaimRequestType claimRequestType = new ClaimRequestType();
//			claimRequestType.setCpuCode(String.valueOf(cpuCode));
//			payload.setClaimRequest(claimRequestType);
//		}
//
//		QueryType queryType = new QueryType();
//		payload.setQuery(queryType);
//		humanTask.setPayload(payload);
//
//		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
//		if(getpreviouReimbursement == null){
//			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//			try {
//				reimburseReimnderLetterInitiateTask.initiate(rodDTO.getStrUserName(), payload);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask = BPMClientContext.submitCancelAcknowledgementTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//		
//		try {
//			submitCancelAcknowledgementTask.execute(rodDTO.getStrUserName(), humanTask);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("CANCELACK");
		DocReceiptACKType docReceiptACK = payloadBO.getDocReceiptACK();
		if(docReceiptACK == null){
			docReceiptACK = new DocReceiptACKType();
		}
		
		docReceiptACK.setStatus("CANCELACK");
		
		payloadBO.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
		Long cpuCode = 0l;
		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
		{
			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
		}
		
		if(null != payloadBO.getClaimRequest())
		{
			payloadBO.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
				
		}
		else
		{
			ClaimRequestType claimRequestType = new ClaimRequestType();
			claimRequestType.setCpuCode(String.valueOf(cpuCode));
			payloadBO.setClaimRequest(claimRequestType);
		}

	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		this.entityManager = entityManager;
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	public void stopReimbReminderLetter(PayloadBOType payloadBO,String userName,String password)
	{
		try {
=======
		QueryType queryType = new QueryType();
		payloadBO.setQuery(queryType);
		humanTaskForROD.setPayload(payloadBO);
		
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6

		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
<<<<<<< HEAD
		
	}
	
	public void stopCashlessReminderLetter(Long claimKey,String userName,String password)
	{
		
		try{
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationBo = new 	com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
		
//		intimationBo.setIntimationNumber(intimationNo);
//		caPayloadBO.setIntimation(intimationBo);
=======
		/***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}


		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		
	*/}	
	/*public void stopQueryReminderLetter(PayloadBOType payloadBO,String userName,String password)
	{
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		}
//		CustomerType customerType = new CustomerType();
//		customerType.setTreatmentType(rodDTO.getTreatmentType());

//		payloadBO.setCustomer(customerType);
		// payloadBO.setDocReceiptACK(receiptAckType);
		humanTaskForROD.setPayload(payloadBO);
		
		
		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
		if(getpreviouReimbursement == null){
			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
			try {
				reimburseReimnderLetterInitiateTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		
		try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
<<<<<<< HEAD
	
	}*/
	
	/*public void stopReimbReminderProcess(PayloadBOType payloadBO,String userName,String password)
	{
		ReminderTask reimbReminder = BPMClientContext.getReimbRemainderTask(userName,password);
		PagedTaskList pagedHumanTaskList = reimbReminder.getTasks(userName,new Pageable(),payloadBO);
		if(null != pagedHumanTaskList)
		{
			 List<HumanTask> humanTaskList  = pagedHumanTaskList.getHumanTasks();
			 if(null != humanTaskList && !humanTaskList.isEmpty())
			 {
				 humanTaskList.get(0).setOutcome("APPROVE");
				 SubmitReminderTask submitTask =  BPMClientContext.getSubmitReimbReimnderTask(userName,password);
				 try {
					submitTask.execute(userName , humanTaskList.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.toString());
				}
			 }
		}
		
	}*/
	
	/*public void stopCashlessReminderProcess(Long claimKey,String userName,String password)
	{
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationBo = new 	com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
=======

	}
	
	
	
	public void submitTaskToBPMForPACancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO,DocAcknowledgement docAcknowledgment) {
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		
//		HumanTask humanTask = rodDTO.getHumanTask();
//		humanTask.setOutcome("APPROVE");
//		
//		PayloadBOType payload = humanTask.getPayload();
//		
//		payload.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//		Long cpuCode = 0l;
//		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
//		{
//			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
//		}
//		
//		if(null != payload.getClaimRequest())
//		{
//			payload.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
//				
//		}
//		else
//		{
//			ClaimRequestType claimRequestType = new ClaimRequestType();
//			claimRequestType.setCpuCode(String.valueOf(cpuCode));
//			payload.setClaimRequest(claimRequestType);
//		}
//
//		QueryType queryType = new QueryType();
//		payload.setQuery(queryType);
//		humanTask.setPayload(payload);
//
//		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
//		if(getpreviouReimbursement == null){
//			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//			try {
//				reimburseReimnderLetterInitiateTask.initiate(rodDTO.getStrUserName(), payload);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask = BPMClientContext.submitCancelAcknowledgementTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//		
//		try {
//			submitCancelAcknowledgementTask.execute(rodDTO.getStrUserName(), humanTask);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("CANCELACK");
		DocReceiptACKType docReceiptACK = payloadBO.getDocReceiptACK();
		if(docReceiptACK == null){
			docReceiptACK = new DocReceiptACKType();
		}
		
		docReceiptACK.setStatus("CANCELACK");
		
		payloadBO.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
		Long cpuCode = 0l;
		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
		{
			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
		}
<<<<<<< HEAD
	}*/
	
	/*public void stopQueryReplyReminderProcess(PayloadBOType payloadBO,String userName,String password)
	{
		log.info("===== inside stop reminder process for query process" );
		QueryReplyDocReimbTask reimbReminder = BPMClientContext.getReimbQueryReplyDocTask(userName,password);
		PagedTaskList pagedHumanTaskList = reimbReminder.getTasks(userName,new Pageable(),payloadBO);
		if(null != pagedHumanTaskList)
=======
		
		if(null != payloadBO.getClaimRequest())
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		{
			payloadBO.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
				
		}
		else
		{
			ClaimRequestType claimRequestType = new ClaimRequestType();
			claimRequestType.setCpuCode(String.valueOf(cpuCode));
			payloadBO.setClaimRequest(claimRequestType);
		}
<<<<<<< HEAD
		
	}*/
	

	/*public void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery, String sendToWhere, Boolean isReconsideration,Claim claim) {
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("APPROVE");
=======

		QueryType queryType = new QueryType();
		payloadBO.setQuery(queryType);
		humanTaskForROD.setPayload(payloadBO);
		
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());

		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
<<<<<<< HEAD
		
		PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
		if(paymentInfo == null){
			paymentInfo = new PaymentInfoType();
		}
		paymentInfo.setClaimedAmount(rodDTO.getTotalClaimedAmount() != null ? rodDTO.getTotalClaimedAmount():0d);
		
		
//		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		
		

		
		*//***
=======
		/***
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		}
<<<<<<< HEAD
		
		if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
				&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
			

			*//**
			 * Instead of below line, add 
			 * docReceiptAck.setPartialhospitalization(true);
			 * *//*
			*//***
			 * Below line added for hospitalization repeat flow.
			 * Repeat should follow normal flow from create rod, bill entry
			 * zmr. 
			 *//*
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);

			//payloadBO.getDocReceiptACK().setHospitalization(true);
			//don't comment below line.
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		
		
		
		
		CustomerType customerType = new CustomerType();
		customerType.setTreatmentType(rodDTO.getTreatmentType());
		ClassificationType classification = null;
		if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
			Stage stage = entityManager.find(Stage.class, ReferenceTable.CREATE_ROD_STAGE_KEY);
			 classification = payloadBO.getClassification();
			classification.setSource(stage.getStageName());
			
			if(isQuery){
				classification.setType(SHAConstants.QUERY_REPLY);
				classification.setSource(SHAConstants.QUERY_REPLY);
			}
			if(isReconsideration) {
				classification.setSource(SHAConstants.RECONSIDERATION);
			}
			
			payloadBO.setClassification(classification);
		}
		
		ClaimRequestType claimRequest = payloadBO.getClaimRequest();
		claimRequest.setResult("APPROVE");
		
		if(isReconsideration){
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			classification.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
		}else if(isQuery){
			classification.setType(SHAConstants.QUERY_REPLY);
			

		}
		else
		{
			claimRequest.setIsReconsider(false);
			classification.setType(SHAConstants.TYPE_FRESH);
			claimRequest.setReimbReqBy(null);
		}
		
		
		
		ClaimType claimType = payloadBO.getClaim();
		if(claimType != null && claim != null && claim.getClaimType() != null && claim.getClaimType().getValue() != null){
			claimType.setClaimType(claim.getClaimType().getValue().toUpperCase());
		}
		
		if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			claimRequest.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
			claimRequest.setIsReconsider(true);
			//rodType.setAckNumber(reimbursement.getRodNumber());
		}
		else
		{
			claimRequest.setOption("");
			//claimRequest.setIsReconsider(false);
		}
		
		if(sendToWhere.equals(ReferenceTable.DIRECT_TO_BILLING)) {
			claimRequest.setReimbReqBy("MEDICAL");
			rodType.setStatus(SHAConstants.BY_PASS_FLOW_CHANGE);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			payloadBO.getDocReceiptACK().setIsBillAvailable(false);
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
			payloadBO.getDocReceiptACK().setHospitalization(false);
			
		} else if(sendToWhere.equals(ReferenceTable.DIRECT_TO_FINANCIAL)) {
//			claimRequest.setReimbReqBy("FA");
//			rodType.setStatus(SHAConstants.BY_PASS_FLOW_CHANGE);
			
			claimRequest.setReimbReqBy("MEDICAL");
			rodType.setStatus(SHAConstants.BY_PASS_FLOW_CHANGE);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			payloadBO.getDocReceiptACK().setIsBillAvailable(false);
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		payloadBO.setClaimRequest(claimRequest);

		payloadBO.setRod(rodType);
		payloadBO.setCustomer(customerType);
		payloadBO.setPaymentInfo(paymentInfo);
		
		RRCType rrc = payloadBO.getRrc();
		if(rrc == null){
		   rrc = new RRCType();
		}
			Date currentDate = reimbursement.getCreatedDate();
			rrc.setToDate(SHAUtils.changeDatetoString(currentDate));

		payloadBO.setRrc(rrc);
		
		ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
		if(processActor == null){
			processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}else if(processActor != null && processActor.getEscalatedByUser() == null){
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}
		
		
		if( payloadBO.getDocReceiptACK().isIsBillAvailable() != null && ! payloadBO.getDocReceiptACK().isIsBillAvailable()){
			Boolean shouldSkipZMR = false;
			shouldSkipZMR = shouldSkipZMR(claim);
			if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				shouldSkipZMR = false;
//				payloadBO.getClaimRequest().setReimbReqBy("BILLING");
//				payloadBO.getDocReceiptACK().setHospitalization(false);
//				payloadBO.getDocReceiptACK().setPartialhospitalization(false);
			}
			if(shouldSkipZMR) {
				payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
			}
		}
		
		// payloadBO.setDocReceiptACK(receiptAckType);
		if(payloadBO.getClaim() != null && payloadBO.getClaim().getClaimType() != null && !(null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
				&& rodDTO.getDocumentDetails().getHospitalizationRepeat())) {
			payloadBO.getClaim().setClaimType(payloadBO.getClaim().getClaimType().toUpperCase());
		}
		
		if(! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
				&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
				&& ! payloadBO.getDocReceiptACK().isLumpsumamount()){

			   claimRequest.setClientType(SHAConstants.MEDICAL);
			   payloadBO.getDocReceiptACK().setPartialhospitalization(true);

		}
		
		humanTaskForROD.setPayload(payloadBO);
		try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
//		submitBpmForCancelAcknowledgement(rodDTO);        // retair the task from cancel acknowledgement
		
//			stopCashlessReminderLetter(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
//			stopCashlessReminderProcess(reimbursement.getClaim().getIntimation().getIntimationId(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
			
//			stopCashlessReminderLetter(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());
//			stopCashlessReminderProcess(reimbursement.getClaim().getKey(),rodDTO.getStrUserName(),rodDTO.getStrPassword());

//			PayloadBOType dupPayload = new PayloadBOType();
//			IntimationType intimationBO = new IntimationType();
//			intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
//			dupPayload.setIntimation(intimationBO);
//			stopReimbReminderLetter(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
//			stopReimbReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
			
			DBCalculationService dbcalculationService = new DBCalculationService();
			dbcalculationService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId());

		if(isQuery)
		{
//			dupPayload = new PayloadBOType();
//			intimationBO = new IntimationType();
//			intimationBO.setIntimationNumber(claim.getIntimation().getIntimationId());
//			dupPayload.setIntimation(intimationBO);
//			stopQueryReminderLetter(dupPayload, rodDTO.getStrUserName(),rodDTO.getStrPassword());
//			stopQueryReplyReminderProcess(dupPayload,rodDTO.getStrUserName(),rodDTO.getStrPassword());
			dbcalculationService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId());
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR CREATE ROD ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
				BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForROD.getNumber(), SHAConstants.SYS_RELEASE);
				rodSubmitTask.execute("claimshead", humanTaskForROD);

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}
	}*/
	
	public void submitTaskToDBProcedureForCreateROD(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery, String sendToWhere, Boolean isReconsideration,Claim claim) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());
		//RODType rodType = new RODType();
		
		//rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());		
		//rodType.setKey(reimbursement.getKey());
		
		wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY, reimbursement.getKey() );
	
		//wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_KEY, ;
		
		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
					
					if(null != rodDTO.getDocumentDetails() && ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()))
							&& (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()))
					{
						wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "N");
					}
					
					break;
				} else {
					wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "N");
				}
			}
		}
		
//		PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
//		if(paymentInfo == null){
//			paymentInfo = new PaymentInfoType();
//		}
//		paymentInfo.setClaimedAmount(rodDTO.getTotalClaimedAmount() != null ? rodDTO.getTotalClaimedAmount():0d);
		
		
//		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		
		

		
		/***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 */
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
//			payloadBO.getDocReceiptACK().setHospitalization(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
			
		} else {
//			payloadBO.getDocReceiptACK().setHospitalization(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
//			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");
		} else {
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
//			payloadBO.getDocReceiptACK().setPrehospitalization(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "Y");
		} else {
//			payloadBO.getDocReceiptACK().setPrehospitalization(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "N");
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
//			payloadBO.getDocReceiptACK().setPosthospitalization(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "Y");
		} else {
//			payloadBO.getDocReceiptACK().setPosthospitalization(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "N");
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
//			payloadBO.getDocReceiptACK().setLumpsumamount(true);
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "Y");
			wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
		} else {
//			payloadBO.getDocReceiptACK().setLumpsumamount(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "N");
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
//			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "Y");
			wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
		} else {
//			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "N");
		}
		
		//added for new product076
		if (null != rodDTO.getDocumentDetails().getHospitalCash()
				&& rodDTO.getDocumentDetails().getHospitalCash()) {
			wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
//			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "Y");
			wrkFlowMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, "Y");
		} else {
//			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "N");
		}
		
		if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
				&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
			

			/**
			 * Instead of below line, add 
			 * docReceiptAck.setPartialhospitalization(true);
			 * */
			/***
			 * Below line added for hospitalization repeat flow.
			 * Repeat should follow normal flow from create rod, bill entry
			 * zmr. 
			 */
//			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");

			//payloadBO.getDocReceiptACK().setHospitalization(true);
			//don't comment below line.
//			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} /*else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}*/
		
		
		
		
//		CustomerType customerType = new CustomerType();
//		customerType.setTreatmentType(rodDTO.getTreatmentType());
		
		wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, rodDTO.getTreatmentType());
		
//		ClassificationType classification = null;
		//if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
			Stage stage = entityManager.find(Stage.class, ReferenceTable.CREATE_ROD_STAGE_KEY);
//			 classification = payloadBO.getClassification();
//			classification.setSource(stage.getStageName());
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.SOURCE_CREATE_ROD);
			
			if(isQuery){
				wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.QUERY_REPLY);
				if(rodDTO.getQueryStage() != null && rodDTO.getQueryStage().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
				}
				if(rodDTO.getQueryStage() != null && rodDTO.getQueryStage().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.FA_CURRENT_QUEUE);
				}
				
				wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY,rodDTO.getRodqueryDTO().getReimbursementQueryKey());
				/*wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.QUERY_REPLY);*/
				
			}
			if(isReconsideration) {
//				classification.setSource(SHAConstants.RECONSIDERATION);
				
				wrkFlowMap.put(SHAConstants.RECORD_TYPE,SHAConstants.RECONSIDERATION);
				wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
			}
			
//			payloadBO.setClassification(classification);
		//}
		
//		ClaimRequestType claimRequest = payloadBO.getClaimRequest();
//		claimRequest.setResult("APPROVE");
		
/*		if(isReconsideration){
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			classification.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
		}else if(isQuery){
			classification.setType(SHAConstants.QUERY_REPLY);
			

		}
		else
		{
			claimRequest.setIsReconsider(false);
			classification.setType(SHAConstants.TYPE_FRESH);
			claimRequest.setReimbReqBy(null);
		}*/
		
		
		
//		ClaimType claimType = payloadBO.getClaim();
		if(claim != null && claim.getClaimType() != null && claim.getClaimType().getValue() != null){
			wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
		}
		
		if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
//			claimRequest.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
//			claimRequest.setIsReconsider(true);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"Y");
			wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
			//rodType.setAckNumber(reimbursement.getRodNumber());
		}
		else if((SHAConstants.N_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
//			claimRequest.setOption("");
			//claimRequest.setIsReconsider(false);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
		}
		
		if(null != reimbursement.getSkipZmrFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(reimbursement.getSkipZmrFlag()))
		{
			wrkFlowMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS,"Y");
		}
		
		if(null != reimbursement && null!= reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() &&  null != reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue())
		{
			wrkFlowMap.put(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM, reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
		}
	
		if(null !=rodDTO.getClaimDTO() && null != rodDTO.getClaimDTO().getNewIntimationDto() && 
		(null != rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId() && (ReferenceTable.PA_LOB_KEY).equals(rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId()) ||
		null != rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId() && ((ReferenceTable.PACKAGE_MASTER_VALUE).equals(rodDTO.getClaimDTO().getNewIntimationDto().getLobId().getId()) &&  claim.getProcessClaimType() != null && claim.getProcessClaimType().equalsIgnoreCase("P"))))
		{
			
		if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
		{
			
			if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
			{
		//	reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			}					
			else
			{
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
			//reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
			//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
			}

		if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
				("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()) || ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag())) //---need to implement partialhospitallization
			{
			//reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			}
							
		
		}
		if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
		{
			if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
					("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) //---need to implement partialhospitallization
			{
				//docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			}	
			else
			{
			//docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);				
			//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
			}
		}
	
	}
		try{
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_CREATE_ROD);
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, getClaimedAmt(reimbursement.getDocAcknowLedgement()));			

			if(null != reimbursement && null != reimbursement.getDocAcknowLedgement() && null != reimbursement.getDocAcknowLedgement().getDocumentTypeId() 
					&& (ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(reimbursement.getDocAcknowLedgement().getDocumentTypeId()))
			{
				wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_ROD_PAYMENT_QUERY);
			}
			
			if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				Preauth latestPreauthByClaim = getLatestPreauthByClaim(reimbursement.getClaim().getKey());
				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, latestPreauthByClaim.getTotalApprovalAmount());
			}else{
				Double claimedAmount = getClaimedAmount(reimbursement.getKey());
				if(claimedAmount != null){
					wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);
				}
			}
			wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
			
			if(rodDTO.getIsPCCSelected()){
				MastersValue masterService= getPCCMasterValue(ReferenceTable.PCC_MASTER_CODE);
				wrkFlowMap.put(SHAConstants.PCC_FLAG,masterService.getMappingCode() != null ? masterService.getMappingCode() : null );	
			}
			else
			{
				MastersValue masterService= getPCCMasterValue(ReferenceTable.NON_PCC_MASTER_CODE);
				wrkFlowMap.put(SHAConstants.PCC_FLAG,masterService.getMappingCode() != null ? masterService.getMappingCode() : null );	
			}
			//added for new product076 Comment for hosp cash flow change 
			if(rodDTO.getNewIntimationDTO() != null && rodDTO.getNewIntimationDTO().getPolicy() != null && 
					rodDTO.getNewIntimationDTO().getPolicy().getProduct() != null && rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			if (null != rodDTO.getDocumentDetails().getHospitalCash()
					&& rodDTO.getDocumentDetails().getHospitalCash()) {
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATEROD_TO_FA_PRD076);
			}
			//added for new product076
			if ((null != rodDTO.getDocumentDetails().getHospitalCash()
				&& rodDTO.getDocumentDetails().getHospitalCash()) && (null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash() 
				&& rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId().equals(SHAConstants.DIAGNOSIS_ID_OTHERS))) {
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATEROD_TO_MA_PRD076);
				}
			}
			//added for new product076 query reply in ma
			if((null != rodDTO.getDocumentDetails().getHospitalCash() && rodDTO.getDocumentDetails().getHospitalCash()) && ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(rodDTO.getPreauthDTO().getStatusKey())){
				if(wrkFlowMap.get(SHAConstants.RECORD_TYPE) != null && ((String)wrkFlowMap.get(SHAConstants.RECORD_TYPE)).equalsIgnoreCase(SHAConstants.QUERY_REPLY)){
					DBCalculationService dbCalService = new DBCalculationService();
					Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
					dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
					wrkFlowMap.put(SHAConstants.OUTCOME, "CRODQREN");
				}
			}

			if(isQuery && (wrkFlowMap.get(SHAConstants.LOB) != null && ((String)wrkFlowMap.get(SHAConstants.LOB)).equals("PA") && ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(rodDTO.getPreauthDTO().getStatusKey()))){
				if(wrkFlowMap.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) != null && ((String)wrkFlowMap.get(SHAConstants.PAYLOAD_BILL_AVAILABLE)).equals("N")){
				wrkFlowMap.put(SHAConstants.OUTCOME, "CRODQREN");
				}
			}
			
			if(isQuery){
				if(rodDTO.getRodqueryDTO().getReimbursementQueryKey() != null){
					List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO.getRodQueryDetailsList();
					for(RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO){
						if(rodQueryDetailsDTO2 != null && rodQueryDetailsDTO2.getReimbursementQueryKey() != null && 
								rodQueryDetailsDTO2.getReimbursementQueryKey().equals(rodDTO.getRodqueryDTO().getReimbursementQueryKey())){
						 if(rodQueryDetailsDTO2.getRelaInstalFlag() != null && rodQueryDetailsDTO2.getRelaInstalFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
							wrkFlowMap.put(SHAConstants.OUTCOME, "CRODFAPR");
						 }
						}
					}
				
				}
			}
			if (claim.getClaimPriorityLabel() != null && claim.getClaimPriorityLabel().equals("Y")) {
				wrkFlowMap.put(SHAConstants.PRIORITY,SHAConstants.ATOS);
			}
			//code added by noufel for updating CMB club member
			if (claim.getClaimClubMember() != null && !claim.getClaimClubMember().isEmpty()) {
				wrkFlowMap.put(SHAConstants.PRIORITY,claim.getClaimClubMember());
			}
			if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && claim.getPriorityEvent() != null && !claim.getPriorityEvent().isEmpty()){
				wrkFlowMap.put(SHAConstants.PRIORITY,claim.getPriorityEvent());
			}
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			//dbCalService.initiateTaskProcedure(objArrayForSubmit);
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
			if(isQuery){
			
				if(wrkFlowMap.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) != null && ((String)wrkFlowMap.get(SHAConstants.PAYLOAD_BILL_AVAILABLE)).equals("N")){
					
					Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
					
					dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
				}
			}
			//added for a  IMSSUPPOR-31956 fix - only for hospital cash product alone
			if((null != rodDTO.getDocumentDetails().getHospitalCash() && rodDTO.getDocumentDetails().getHospitalCash()) && ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(rodDTO.getPreauthDTO().getStatusKey())){
				if(wrkFlowMap.get(SHAConstants.RECORD_TYPE) != null && ((String)wrkFlowMap.get(SHAConstants.RECORD_TYPE)).equalsIgnoreCase(SHAConstants.QUERY_REPLY)){
					Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
					dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
				}
			}

			dbCalService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId(),SHAConstants.OTHERS);

		if(isQuery)
		{
			dbCalService.stopReminderProcessProcedure(claim.getIntimation().getIntimationId(),SHAConstants.OTHERS);
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR CREATE ROD ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
//				BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForROD.getNumber(), SHAConstants.SYS_RELEASE);
//				rodSubmitTask.execute("claimshead", humanTaskForROD);

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}

	}
	
	 /*private DocumentDetails getDocumentDetails()
		{
		 DocumentDetails documentDetail;
			Query findByAll = entityManager.createNamedQuery("DocumentDetails.findAll");
			try{
				documentDetail =(DocumentDetails) findByAll.getResultList();
				return documentDetail;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;				
		}*/
			
			private void setReimbursementValues(Reimbursement reimbursement,
					PreauthDTO reimbursmentDTO,ReceiptOfDocumentsDTO rodDTO) {
				reimbursmentDTO.setKey(reimbursement.getKey());
				reimbursmentDTO.setStatusKey(reimbursement.getStatus().getKey());
				reimbursmentDTO.setStageKey(reimbursement.getStage().getKey());

				/**
				 * Bank id as been changed from Long to MAS_BANK type. hence below line
				 * as been changed accordingly
				 * */
				reimbursmentDTO.setBankId(reimbursement.getBankId());
				reimbursmentDTO.setAccountNumber(reimbursement.getAccountNumber());
				reimbursmentDTO.setAccountPreference(reimbursement.getAccountPreference());
				reimbursmentDTO.setAccountType(reimbursement.getAccountType());
				reimbursmentDTO.setPayableAt(reimbursement.getPayableAt());
				reimbursmentDTO.setPayeeEmailId(reimbursement.getPayeeEmailId());
				reimbursmentDTO.setPanNumber(reimbursement.getPanNumber());
				reimbursmentDTO.setPayeeName(reimbursement.getPayeeName());
				reimbursmentDTO.setPaymentModeId(reimbursement.getPaymentModeId());
				reimbursmentDTO.setRodNumber(reimbursement.getRodNumber());
				
				//Setting current provision amt.
				//reimbursmentDTO.setC
				
				PreauthDataExtaractionDTO dataExtraction = reimbursmentDTO
						.getPreauthDataExtractionDetails();
				dataExtraction.setDocAckknowledgement(reimbursement
						.getDocAcknowLedgement());
				dataExtraction.setDocAcknowledgementKey(reimbursement
						.getDocAcknowLedgement() != null ? reimbursement
						.getDocAcknowLedgement().getKey() : null);
				dataExtraction.setPaymentModeFlag(reimbursement.getPaymentModeId());
				dataExtraction.setPanNo(reimbursement.getPanNumber());
				dataExtraction.setChangeInReasonDOA(reimbursement.getReasonForChange());
				dataExtraction.setLegalFirstName(reimbursement.getLegalHeirFirstName());
				dataExtraction.setLegalLastName(reimbursement.getLegalHeirLastName());
				dataExtraction.setLegalMiddleName(reimbursement
						.getLegalHeirMiddleName());
				dataExtraction.setAccountNo(reimbursement.getAccountNumber());
				dataExtraction.setEmailId(reimbursement.getPayeeEmailId());
				dataExtraction.setPayeeName(rodDTO.getDocumentDetails().getPayeeName());				
//				dataExtraction.setPayeeName(reimbursement.getpay);
				// dataExtraction.setPayeeName(reimbursement.getPayeeName());
				dataExtraction.setPayableAt(reimbursement.getPayableAt());
				dataExtraction.setAccountPref(reimbursement.getAccountPreference());
				dataExtraction.setNameAsPerBank(rodDTO.getDocumentDetails().getNameAsPerBank());
				dataExtraction.setAccType(reimbursement.getAccountType());
				
				
			}

	public Claim searchByClaimKey(Long a_key) {
		Query query = entityManager.createNamedQuery("Claim.findByKey");
		query = query.setParameter("primaryKey", a_key);
		List<Claim> claimList = query.getResultList();
		if(null != claimList && !claimList.isEmpty())
		{
			entityManager.refresh(claimList.get(0));
			return claimList.get(0);
		}
		else
		{
			return null;
		}
		
		/*Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;*/
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Intimation getIntimationByKey(Long intimationKey) {

		if(intimationKey != null){
			Query findByKey = entityManager
					.createNamedQuery("Intimation.findByKey").setParameter(
							"intiationKey", intimationKey);
	
			List<Intimation> intimationList = (List<Intimation>) findByKey
					.getResultList();
	
			if (!intimationList.isEmpty()) {
				return intimationList.get(0);
	
			}
		}
		return null;
	}

	/**
	 * The below method is used to fetch Insured details from IMS_CLS_INSURED
	 * Table. This table is a new table which was introduced during policy table
	 * change requirement.
	 * 
	 * */
	public Insured getCLSInsured(Long key) {

		Query query = entityManager.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}

	public Reimbursement getReimbursementByRODKey(Long rodKey)
	{
		Reimbursement reimbursementDetails; 
		Query findByTransactionKey = entityManager.createNamedQuery(
				"Reimbursement.findByKey").setParameter("primaryKey", rodKey);
		try{
			reimbursementDetails = (Reimbursement) findByTransactionKey.getSingleResult();
			return reimbursementDetails;
		}
		catch(Exception e)
		{
			return null;
		}
	}

/*	public void submitBpmForCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO){
		
		PayloadBOType payloadBOType = new PayloadBOType();
		
		String userName = rodDTO.getStrUserName();
		String passWord = rodDTO.getStrPassword();
		
		String intimationId = rodDTO.getClaimDTO().getNewIntimationDto().getIntimationId();
		IntimationType intimation = new IntimationType();
		intimation.setIntimationNumber(intimationId);
		payloadBOType.setIntimation(intimation);
		
		CancelAcknowledgementTask cancelTask = BPMClientContext.getCancelAcknowledgementTask(userName, passWord);
		
		Pageable pageable = new Pageable(1, 10, true);
		
		PagedTaskList taskList = cancelTask.getTasks(userName,pageable,payloadBOType);
=======
//		CustomerType customerType = new CustomerType();
//		customerType.setTreatmentType(rodDTO.getTreatmentType());

//		payloadBO.setCustomer(customerType);
		// payloadBO.setDocReceiptACK(receiptAckType);
		humanTaskForROD.setPayload(payloadBO);
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		
		
		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
		if(getpreviouReimbursement == null){
			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
			try {
				reimburseReimnderLetterInitiateTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		
<<<<<<< HEAD
	}*/

	/*	try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}

	}
*/
	
	public Reimbursement getpreviouReimbursement(Long claimKey)
	{
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<Reimbursement> rodList = query.getResultList();
		Reimbursement reimbursementObj = null;
		if(null != rodList && !rodList.isEmpty())
		{
			reimbursementObj = rodList.get(0);
		}
		return reimbursementObj;
		//entityManager.refresh(reimbursement);
		
	}
	
	
	/*private void submitPABillEntryTaskToBPM(ReceiptOfDocumentsDTO rodDTO, Claim claimObj, Boolean shouldSkipZMR) {
		SubmitBillEntryTask submitBillEntryTask = BPMClientContext
				.getSubmitBillEntryTask(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForBillEntry = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForBillEntry.getPayload();
		humanTaskForBillEntry.setOutcome("APPROVE");

		PaymentInfoType paymentInfoType = new PaymentInfoType();
		if(rodDTO.getTotalClaimedAmount() != null){
			paymentInfoType.setClaimedAmount(rodDTO.getTotalClaimedAmount());
		}else{
			paymentInfoType.setClaimedAmount(0d);
		}
		

		
		 * CustomerType customerType = new CustomerType();
		 * customerType.setTreatmentType(rodDTO.getTreatmentType());
		 
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		
		*//**
		 * The below condition is added for cashless by pass case.
		 * If from via refer to bill entry a cashless bypass
		 * case comes to bill entry, then on submit of bill entry
		 * record should go to billing again. To enable this below 
		 * code is added . If bypass, then  bill classification values
		 * will not be set. Because based on bill classification record	
		 * will get moved to zmr queue. 
		 * 
		 * **//*

		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
			DocReceiptACKType docReceiptType1 = payloadBO.getDocReceiptACK();
			if(null != docReceiptType1)
			{
				docReceiptType1.setHospitalization(false);
				docReceiptType1.setPartialhospitalization(false);
				payloadBO.setDocReceiptACK(docReceiptType1);
			}
			
			ClaimRequestType requestType = payloadBO.getClaimRequest();
			if(null != requestType)
			{
				requestType.setReimbReqBy("BILLING");
				payloadBO.setClaimRequest(requestType);
			}
		}

		else
		{
		
			DocReceiptACKType docReceiptType = payloadBO.getDocReceiptACK();
			
			 * docReceiptType.setHospitalization(true);
			 * docReceiptType.setPartialhospitalization(false);
			 * docReceiptType.setPrehospitalization(false);
			 * docReceiptType.setPosthospitalization(false);
			 * docReceiptType.setLumpsumamount(false);
			 * docReceiptType.setAddonbenefitshospcash(false);
			 * docReceiptType.setAddonbenefitspatientcare(false);
			 
			if (null != rodDTO.getDocumentDetails().getHospitalization()
					&& rodDTO.getDocumentDetails().getHospitalization()) {
				docReceiptType.setHospitalization(true);
			} else {
				docReceiptType.setHospitalization(false);
			}
			if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
					&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
				docReceiptType.setPartialhospitalization(true);
			} else {
				docReceiptType.setPartialhospitalization(false);
			}
			if (null != rodDTO.getDocumentDetails().getPreHospitalization()
					&& rodDTO.getDocumentDetails().getPreHospitalization()) {
				docReceiptType.setPrehospitalization(true);
			} else {
				docReceiptType.setPrehospitalization(false);
			}
	
			if (null != rodDTO.getDocumentDetails().getPostHospitalization()
					&& rodDTO.getDocumentDetails().getPostHospitalization()) {
				docReceiptType.setPosthospitalization(true);
			} else {
				docReceiptType.setPosthospitalization(false);
			}
	
			if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
					&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
				docReceiptType.setLumpsumamount(true);
			} else {
				docReceiptType.setLumpsumamount(false);
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
				docReceiptType.setAddonbenefitshospcash(true);
			} else {
				docReceiptType.setAddonbenefitshospcash(false);
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
				docReceiptType.setAddonbenefitspatientcare(true);
			} else {
				docReceiptType.setAddonbenefitspatientcare(false);
			}
			
			if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
					&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
				
				*//**
				 * Instead of below line, add 
				 * docReceiptAck.setPartialhospitalization(true);
				 * *//*
				
				*//***
				 * Below line added for hospitalization repeat flow.
				 * Repeat should follow normal flow from create rod, bill entry
				 * zmr. 
				 *//*
				
				//payloadBO.getDocReceiptACK().setHospitalization(true);
				payloadBO.getDocReceiptACK().setPartialhospitalization(true);
				//Don't comment the below line
//				payloadBO.getDocReceiptACK().setIsBillAvailable(true);
			} else {
				payloadBO.getDocReceiptACK().setHospitalization(false);
			}
	
			// Adding for BPMN filtering search. -- Hospital info type is set at
			// acknowledgement level itself.
			
			 * HospitalInfoType hospInfoType = new HospitalInfoType();
			 * hospInfoType.setHospitalType
			 * (rodDTO.getClaimDTO().getNewIntimationDto(
			 * ).getHospitalDto().getRegistedHospitals
			 * ().getHospitalType().getValue());
			 * hospInfoType.setNetworkHospitalType(
			 * rodDTO.getClaimDTO().getNewIntimationDto
			 * ().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
			 
	
			// ProductInfoType pdtTypeInfo = new ProductInfoType();
	
			
			 * IntimationType intimationType = payloadBO.getIntimation();
			 * intimationType.setIntimationSource("Call Center");
			 
	
			// Claim request type , intimation source are now set at acknowledgement
			// level itself.
			
			 * payloadBO.getClaimRequest().setClaimRequestType("All");
			 * payloadBO.getIntimation
			 * ().setIntimationSource(rodDTO.getClaimDTO().getNewIntimationDto
			 * ().getIntimationSource().getValue());
			 * payloadBO.setHospitalInfo(hospInfoType);
			 
	
			// payloadBO.setProductInfo(productInfo);
	
			// docReceiptType.setAddonbenefitshospcash(false);
			// docReceiptType.setAddonbenefitspatientcare(false);
	
			// payloadBO.getClaimRequest().setResult("ACK");
	
			// payloadBO.getClaimRequest().setClientType("ACK");
			
			if((! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
					&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
					&& ! payloadBO.getDocReceiptACK().isLumpsumamount())){

				   payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
				   docReceiptType.setHospitalization(true);
			}
	
			payloadBO.setDocReceiptACK(docReceiptType);
		}
		// payloadBO.setProductInfo(productInfo);
		payloadBO.setPaymentInfo(paymentInfoType);
		
<<<<<<< HEAD
		if(claimObj != null && claimObj.getStage() != null){
			ClaimRequestType requestType = payloadBO.getClaimRequest();
			if(requestType != null){
				*//**
				 * The below line is added for refer to bill entry process.
				 * For cashless claim which undergoes bypass, once after coming to 
				 * bill entry it should move to BILLING screen. For that the below change
				 * is done.
				 * *//*
				*//**
				 * The below code is not required as we dont have a bypass flow
				 * from bill entry.
				 * *//*
				if(null != requestType.isIsReconsider() && !requestType.isIsReconsider())
				{
					requestType.setReimbReqBy("BILLING");
				}
				*//**
				 * If its a reconsideration scenario, by default the
				 * instance should move to FA.
				 * *//*
				else
				{
					requestType.setReimbReqBy("FA");

				}
				if(null != requestType.isIsReconsider() && requestType.isIsReconsider() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
				{
					requestType.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
				}
				else
				{
					requestType.setOption(claimObj.getStage().getStageName());
				}
				payloadBO.setClaimRequest(requestType);
			}
		}
		if(claimObj.getStage() != null && claimObj.getStage().getKey() != null){
			Status status = entityManager.find(Status.class, claimObj.getStatus().getKey());
			ClassificationType classification = payloadBO.getClassification();
			if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
				classification.setSource(status.getProcessValue());
=======
		for (UploadDocumentDTO uploadDocDTO : uploadDocsDTO) {
			//sss
			/**
			 * In case of reconsideratio is true, then  status and bill entry icon needs to be disablded.
			 * For this, the below variable is set to true. This is added as a part of jiira bug.
			 * */
			//uploadDocDTO.setStatus(true);
			//uploadDocDTO.setEnableOrDisableBtn(false);

	
/*	public void submitTaskToBPMForCancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO,DocAcknowledgement docAcknowledgment) {
		
//		HumanTask humanTask = rodDTO.getHumanTask();
//		humanTask.setOutcome("APPROVE");
//		
//		PayloadBOType payload = humanTask.getPayload();
//		
//		payload.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//		Long cpuCode = 0l;
//		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
//		{
//			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
//		}
//		
//		if(null != payload.getClaimRequest())
//		{
//			payload.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
//				
//		}
//		else
//		{
//			ClaimRequestType claimRequestType = new ClaimRequestType();
//			claimRequestType.setCpuCode(String.valueOf(cpuCode));
//			payload.setClaimRequest(claimRequestType);
//		}
//
//		QueryType queryType = new QueryType();
//		payload.setQuery(queryType);
//		humanTask.setPayload(payload);
//
//		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
//		if(getpreviouReimbursement == null){
//			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//			try {
//				reimburseReimnderLetterInitiateTask.initiate(rodDTO.getStrUserName(), payload);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask = BPMClientContext.submitCancelAcknowledgementTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//		
//		try {
//			submitCancelAcknowledgementTask.execute(rodDTO.getStrUserName(), humanTask);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("CANCELACK");
		DocReceiptACKType docReceiptACK = payloadBO.getDocReceiptACK();
		if(docReceiptACK == null){
			docReceiptACK = new DocReceiptACKType();
=======
		if(! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
				&& (! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() || (payloadBO.getDocReceiptACK().isPosthospitalization() && payloadBO.getDocReceiptACK().isPrehospitalization()
						&& payloadBO.getDocReceiptACK().isAddonbenefitshospcash())) && (! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()||(payloadBO.getDocReceiptACK().isPosthospitalization() && payloadBO.getDocReceiptACK().isPrehospitalization()
						&& payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()))
				&& ! payloadBO.getDocReceiptACK().isLumpsumamount()){

			   payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
			   payloadBO.getDocReceiptACK().setPartialhospitalization(true);

>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		}

		if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			shouldSkipZMR = false;
//			payloadBO.getClaimRequest().setReimbReqBy("BILLING");
//			payloadBO.getRod().setStatus(SHAConstants.NEW);
			payloadBO.getDocReceiptACK().setHospitalization(false);
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		} 
		
		
		ProductInfoType productInfo = payloadBO.getProductInfo();
		if((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && rodDTO.getDocumentDetails().getHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) || (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){			
			productInfo.setLob(SHAConstants.PA_LOB);
			productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
		}
		else
		{
			productInfo.setLob(SHAConstants.PA_LOB);
			productInfo.setLobType(SHAConstants.PA_LOB_TYPE);
		}
		
		if(shouldSkipZMR) {
			payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
		}
		
		
		/***
		 * The below code is added for lumpsum change implemented for medicare
		 * and criticare product, where for lumpsum product , zmr should be
		 * skipped and it should  move to medical.
		 * 
		if(((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
		&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
		 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
		 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))
		{
			*//**
			 * As per bpmn flow, to skip ZMR , either hospitalization 
			 * or partial hospitalization needs to be set as true.
			 * Hence if lumpsum alone is enabled, then zmr to be skipped.
			 * For that , internally we're setting hospitalization
			 * and partial hospitalization to true. This is only for flow purpose.
			 * *//*
			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			if(null != docReceipt)
			{
				docReceipt.setHospitalization(true);
				docReceipt.setPartialhospitalization(true);
				payloadBO.setDocReceiptACK(docReceipt);
			}
			payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
		}

		
		ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
		if(processActor == null){
			processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}else if(processActor != null && processActor.getEscalatedByUser() == null){
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}
		
		if (rodDTO.getDocumentDetails().getHospitalizationFlag() != null && rodDTO.getDocumentDetails().getHospitalizationFlag().equals("Y")){
			
			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			if(null != docReceipt)
			{
				docReceipt.setHospitalization(true);
				docReceipt.setPartialhospitalization(true);
				payloadBO.setDocReceiptACK(docReceipt);
			}
		}

		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);

		
		if ((rodDTO.getDocumentDetails().getHospitalizationFlag() != null && rodDTO.getDocumentDetails().getHospitalizationFlag().equals("Y")) || 
				(rodDTO.getDocumentDetails().getPartialHospitalizationFlag() != null && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equals("Y") )){
			
			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			docReceipt.setHospitalization(true);
			docReceipt.setPartialhospitalization(true);

<<<<<<< HEAD
=======
	public List<DocumentDetailsDTO> getDocumentDetailsDTO(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<DocumentDetailsDTO> documentDetailsDTO = null;
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			List<DocAcknowledgement> docAcknowlegementList = (List<DocAcknowledgement>) query
					.getResultList();
			documentDetailsDTO = new ArrayList<DocumentDetailsDTO>();
			DocumentDetailsDTO documentDetailDTO = null;
			for (DocAcknowledgement docAcknowledgement : docAcknowlegementList) {
				if (null != docAcknowledgement.getRodKey()) {
					documentDetailDTO = new DocumentDetailsDTO();
					documentDetailDTO.setRodKey(docAcknowledgement.getRodKey());
					documentDetailDTO.setHospitalizationFlag(docAcknowledgement
							.getHospitalisationFlag());
					documentDetailDTO
							.setPreHospitalizationFlag(docAcknowledgement
									.getPreHospitalisationFlag());
					documentDetailDTO
							.setPostHospitalizationFlag(docAcknowledgement
									.getPostHospitalisationFlag());
					documentDetailDTO
							.setPartialHospitalizationFlag(docAcknowledgement
									.getPartialHospitalisationFlag());
					documentDetailDTO.setLumpSumAmountFlag(docAcknowledgement
							.getLumpsumAmountFlag());
					documentDetailDTO
							.setAddOnBenefitsHospitalCashFlag(docAcknowledgement
									.getHospitalCashFlag());
					documentDetailDTO
							.setAddOnBenefitsPatientCareFlag(docAcknowledgement
									.getPatientCareFlag());
					documentDetailsDTO.add(documentDetailDTO);
					documentDetailDTO = null;
				}
			}
>>>>>>> FHOwithproduction
		}
		else
		{
			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			docReceipt.setHospitalization(false);
			docReceipt.setPartialhospitalization(false);
		}
		DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
		if ((rodDTO.getDocumentDetails().getHospitalizationFlag() != null && rodDTO.getDocumentDetails().getHospitalizationFlag().equals("Y"))){
			docReceipt.setHospitalization(true);
		} else {
			docReceipt.setHospitalization(false);
		}
		
		if (rodDTO.getDocumentDetails().getPartialHospitalizationFlag() != null && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equals("Y") ){
			docReceipt.setPartialhospitalization(true);
		} else {
			docReceipt.setPartialhospitalization(false);
		}
		

//		IntimationType intimationType = new IntimationType();

		IntimationType intimationType = payloadBO.getIntimation();
		if(null == intimationType)
		{
			intimationType = new IntimationType();
		}
		if(null != rodDTO.getDocumentDetails().getAccidentOrDeath() && rodDTO.getDocumentDetails().getAccidentOrDeath())
		{
			intimationType.setReason(SHAConstants.ACCIDENT_FLAG);
		}
		else
		{
			intimationType.setReason(SHAConstants.DEATH_FLAG);
		}
		
		payloadBO.setIntimation(intimationType);
				
		humanTaskForBillEntry.setPayload(payloadBO);
		
		try{
		submitBillEntryTask.execute(rodDTO.getStrUserName(),
				humanTaskForBillEntry);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR BILL_ENTRY ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
				BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForBillEntry.getNumber(), SHAConstants.SYS_RELEASE);
				submitBillEntryTask.execute("claimshead", humanTaskForBillEntry);

	}*/
	
public void submitTaskToBPMForPACancelAcknowledgement(ReceiptOfDocumentsDTO rodDTO,DocAcknowledgement docAcknowledgment) {
		
//		HumanTask humanTask = rodDTO.getHumanTask();
//		humanTask.setOutcome("APPROVE");
//		
//		PayloadBOType payload = humanTask.getPayload();
//		
//		payload.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//		Long cpuCode = 0l;
//		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
//		{
//			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
//		}
//		
//		if(null != payload.getClaimRequest())
//		{
//			payload.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
//				
//		}
//		else
//		{
//			ClaimRequestType claimRequestType = new ClaimRequestType();
//			claimRequestType.setCpuCode(String.valueOf(cpuCode));
//			payload.setClaimRequest(claimRequestType);
//		}
//
//		QueryType queryType = new QueryType();
//		payload.setQuery(queryType);
//		humanTask.setPayload(payload);
//
//		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
//		if(getpreviouReimbursement == null){
//			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//			try {
//				reimburseReimnderLetterInitiateTask.initiate(rodDTO.getStrUserName(), payload);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask = BPMClientContext.submitCancelAcknowledgementTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
//		
//		try {
//			submitCancelAcknowledgementTask.execute(rodDTO.getStrUserName(), humanTask);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		/*SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("CANCELACK");
		DocReceiptACKType docReceiptACK = payloadBO.getDocReceiptACK();
		if(docReceiptACK == null){
			docReceiptACK = new DocReceiptACKType();
		}
		
		docReceiptACK.setStatus("CANCELACK");
		
		payloadBO.getClaimRequest().setOption(SHAConstants.BILLS_NOT_RECEIVED); 
		Long cpuCode = 0l;
		if(null != docAcknowledgment.getClaim() && null != docAcknowledgment.getClaim().getIntimation() && null != docAcknowledgment.getClaim().getIntimation().getCpuCode())
		{
			cpuCode = docAcknowledgment.getClaim().getIntimation().getCpuCode().getCpuCode();
		}
		
		if(null != payloadBO.getClaimRequest())
		{
			payloadBO.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
				
		}
		else
		{
			ClaimRequestType claimRequestType = new ClaimRequestType();
			claimRequestType.setCpuCode(String.valueOf(cpuCode));
			payloadBO.setClaimRequest(claimRequestType);
		}

		QueryType queryType = new QueryType();
		payloadBO.setQuery(queryType);
		humanTaskForROD.setPayload(payloadBO);
		
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());

		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		}
//		CustomerType customerType = new CustomerType();
//		customerType.setTreatmentType(rodDTO.getTreatmentType());

//		payloadBO.setCustomer(customerType);
		// payloadBO.setDocReceiptACK(receiptAckType);
		humanTaskForROD.setPayload(payloadBO);
		
		
		Reimbursement getpreviouReimbursement = getpreviouReimbursement(docAcknowledgment.getClaim().getKey());
		if(getpreviouReimbursement == null){
			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
			try {
				reimburseReimnderLetterInitiateTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		
		try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}*/

	}

	
	public void submitDBprocedureForCancelAcknowlegdement(ReceiptOfDocumentsDTO rodDTO,DocAcknowledgement docAcknowledgment) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_CANCEL_ACKNOWLEDGEMENT);
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
		
		
		
	}	


private void submitBillEntryTaskToDB(ReceiptOfDocumentsDTO rodDTO, Claim claimObj, Boolean shouldSkipZMR) {
		
		//HumanTask humanTaskForBillEntry = rodDTO.getHumanTask();
		
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
	//	humanTaskForBillEntry.setOutcome("APPROVE");
//
		Boolean isFirstRod = false;
		if(claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			String[] split = rodDTO.getDocumentDetails().getRodNumber()
					.split("/");
			String string = split[split.length - 1];
			isFirstRod  = string.equalsIgnoreCase("1") ? true : false;
		}
		
		if(rodDTO.getTotalClaimedAmount() != null){
			//paymentInfoType.setClaimedAmount(rodDTO.getTotalClaimedAmount());
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, rodDTO.getTotalClaimedAmount());
			if(isFirstRod)
			setProcessingCpuCodeBasedOnLimit(claimObj.getIntimation(),rodDTO.getTotalClaimedAmount().longValue(),claimObj);
		}else{
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, 0d);
			if(isFirstRod)
			setProcessingCpuCodeBasedOnLimit(claimObj.getIntimation(),0l,claimObj);
		}
		
		// added for new product076
		if (rodDTO.getClaimDTO() != null &&  rodDTO.getClaimDTO().getNewIntimationDto() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy() != null 
				&& rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  
				rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt());
		}
		
		wrkFlowMap.put(SHAConstants.CPU_CODE, claimObj.getIntimation().getCpuCode().getCpuCode().toString());
		

		/*
		 * CustomerType customerType = new CustomerType();
		 * customerType.setTreatmentType(rodDTO.getTreatmentType());
		 */
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		
		/**
		 * The below condition is added for cashless by pass case.
		 * If from via refer to bill entry a cashless bypass
		 * case comes to bill entry, then on submit of bill entry
		 * record should go to billing again. To enable this below 
		 * code is added . If bypass, then  bill classification values
		 * will not be set. Because based on bill classification record	
		 * will get moved to zmr queue. 
		 * 
		 * **/

		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
			/*DocReceiptACKType docReceiptType1 = payloadBO.getDocReceiptACK();
			if(null != docReceiptType1)
			{
				docReceiptType1.setHospitalization(false);
				docReceiptType1.setPartialhospitalization(false);
				payloadBO.setDocReceiptACK(docReceiptType1);
			}*/
			
			
			
			/*ClaimRequestType requestType = payloadBO.getClaimRequest();
			if(null != requestType)
			{
				requestType.setReimbReqBy("BILLING");
				payloadBO.setClaimRequest(requestType);
			}*/
			
			//wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 0d);
			
		}

		else
		{
		
//			DocReceiptACKType docReceiptType = payloadBO.getDocReceiptACK();
			/*
			 * docReceiptType.setHospitalization(true);
			 * docReceiptType.setPartialhospitalization(false);
			 * docReceiptType.setPrehospitalization(false);
			 * docReceiptType.setPosthospitalization(false);
			 * docReceiptType.setLumpsumamount(false);
			 * docReceiptType.setAddonbenefitshospcash(false);
			 * docReceiptType.setAddonbenefitspatientcare(false);
			 */
			if (null != rodDTO.getDocumentDetails().getHospitalization()
					&& rodDTO.getDocumentDetails().getHospitalization()) {
				//docReceiptType.setHospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
				
			} else {
				//docReceiptType.setHospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
			}
			if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
					&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
				//docReceiptType.setPartialhospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");
			} else {
//				docReceiptType.setPartialhospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
			}
			if (null != rodDTO.getDocumentDetails().getPreHospitalization()
					&& rodDTO.getDocumentDetails().getPreHospitalization()) {
				//docReceiptType.setPrehospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "Y");
			} else {
				/*docReceiptType.setPrehospitalization(false);*/
				wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getPostHospitalization()
					&& rodDTO.getDocumentDetails().getPostHospitalization()) {
				//docReceiptType.setPosthospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "Y");
			} else {
				//docReceiptType.setPosthospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
					&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
				//docReceiptType.setLumpsumamount(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "Y");
			} else {
				//docReceiptType.setLumpsumamount(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
				//docReceiptType.setAddonbenefitshospcash(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "Y");
			} else {
				//docReceiptType.setAddonbenefitshospcash(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
				//docReceiptType.setAddonbenefitspatientcare(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "Y");
			} else {
				//docReceiptType.setAddonbenefitspatientcare(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "N");
			}
			
			//added for new product076
			if (null != rodDTO.getDocumentDetails().getHospitalCash()
					&& rodDTO.getDocumentDetails().getHospitalCash()) {
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "Y");
			}
			
			if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
					&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
				
				/**
				 * Instead of below line, add 
				 * docReceiptAck.setPartialhospitalization(true);
				 * */
				
				/***
				 * Below line added for hospitalization repeat flow.
				 * Repeat should follow normal flow from create rod, bill entry
				 * zmr. 
				 */
				
				
//				payloadBO.getDocReceiptACK().setPartialhospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
				
			}		
			
			
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.BILL_ENTRY_COMPLETED);
//			if((! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
//					&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
//					&& ! payloadBO.getDocReceiptACK().isLumpsumamount())){
//
////				   payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
//				//   docReceiptType.setHospitalization(true);
//				   wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
//
//			}
	
			//payloadBO.setDocReceiptACK(docReceiptType);
		}
		// payloadBO.setProductInfo(productInfo);
		//payloadBO.setPaymentInfo(paymentInfoType);
		
//		if(claimObj != null && claimObj.getStage() != null){
//			ClaimRequestType requestType = payloadBO.getClaimRequest();
//			if(requestType != null){
//				/**
//				 * The below line is added for refer to bill entry process.
//				 * For cashless claim which undergoes bypass, once after coming to 
//				 * bill entry it should move to BILLING screen. For that the below change
//				 * is done.
//				 * */
//				/**
//				 * The below code is not required as we dont have a bypass flow
//				 * from bill entry.
//				 * */
//				if(null != requestType.isIsReconsider() && !requestType.isIsReconsider())
//				{
//					//requestType.setReimbReqBy("BILLING");
//				}
//				/**
//				 * If its a reconsideration scenario, by default the
//				 * instance should move to FA.
//				 * */
//				else
//				{
//					//requestType.setReimbReqBy("FA");
//
//				}
//				if(null != requestType.isIsReconsider() && requestType.isIsReconsider() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
//				{
//					//requestType.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
//					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION, "Y");
//				}
//				else
//				{
//					//requestType.setOption(claimObj.getStage().getStageName());
//					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION, "N");
//				}
//				//payloadBO.setClaimRequest(requestType);
//			}
//		}
		//if(claimObj.getStage() != null && claimObj.getStage().getKey() != null){
			//Status status = entityManager.find(Status.class, claimObj.getStatus().getKey());
//			ClassificationType classification = payloadBO.getClassification();
//			if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
//				//classification.setSource(status.getProcessValue());
//				wrkFlowMap.put(SHAConstants.STAGE_SOURCE, status.getProcessValue());
//			}
			
			//payloadBO.setClassification(classification);
		//}
		
//		if(! rodDTO.getDocumentDetails().isHospitalization() && ! rodDTO.getDocumentDetails().isPartialhospitalization()
//				&& (! rodDTO.getDocumentDetails().isAddonbenefitshospcash() || (rodDTO.getDocumentDetails().isPosthospitalization() && rodDTO.getDocumentDetails().isPrehospitalization()
//						&& rodDTO.getDocumentDetails().isAddonbenefitshospcash())) && (! rodDTO.getDocumentDetails().isAddonbenefitspatientcare()||(rodDTO.getDocumentDetails().isPosthospitalization() && rodDTO.getDocumentDetails().isPrehospitalization()
//						&& rodDTO.getDocumentDetails().isAddonbenefitspatientcare()))
//				&& ! rodDTO.getDocumentDetails().isLumpsumamount()){
//
//			//   payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
//			  // payloadBO.getDocReceiptACK().setPartialhospitalization(true);
//			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");
//
//		}

		if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			shouldSkipZMR = false;
			/*payloadBO.getClaimRequest().setReimbReqBy("BILLING");
			payloadBO.getDocReceiptACK().setHospitalization(false);
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);*/
			wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
		}
		if(shouldSkipZMR) {
			//payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS, "Y");
		}
		
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
		
//		//added for new product076
//		if (null != rodDTO.getDocumentDetails().getHospitalCash()
//				&& rodDTO.getDocumentDetails().getHospitalCash()) {
//			wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILLENTRY_TO_FA_PRD076);
//		}
//		
//		//added for new product076
//				if ((null != rodDTO.getDocumentDetails().getHospitalCash()
//						&& rodDTO.getDocumentDetails().getHospitalCash()) && (null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash() 
//						&& rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId().equals(SHAConstants.DIAGNOSIS_ID_OTHERS))) {
//					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILLENTRY_TO_MA_PRD076);
//				}
		//added for new product076
		if(rodDTO.getClaimDTO() != null &&  rodDTO.getClaimDTO().getNewIntimationDto() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy() != null 
				&& rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  
				rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)){
			if ((null != rodDTO.getDocumentDetails().getHospitalCash()
					&& rodDTO.getDocumentDetails().getHospitalCash())) {
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILLENTRY_TO_MA_PRD076);
			}
		}
		
		if (claimObj.getClaimPriorityLabel() != null && claimObj.getClaimPriorityLabel().equals("Y")) {
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.ATOS);
		}
		
		//code added by noufel for updating CMB club member
		if (claimObj.getClaimClubMember() != null && !claimObj.getClaimClubMember().isEmpty()) {
			wrkFlowMap.put(SHAConstants.PRIORITY,claimObj.getClaimClubMember());
		}

		/***
		 * The below code is added for lumpsum change implemented for medicare
		 * and criticare product, where for lumpsum product , zmr should be
		 * skipped and it should  move to medical.
		 * */
		if(((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
		&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
		 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
		 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))
		{
			/**
			 * As per bpmn flow, to skip ZMR , either hospitalization 
			 * or partial hospitalization needs to be set as true.
			 * Hence if lumpsum alone is enabled, then zmr to be skipped.
			 * For that , internally we're setting hospitalization
			 * and partial hospitalization to true. This is only for flow purpose.
			 * */
//			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			/*if(null != docReceipt)
			{
				docReceipt.setHospitalization(true);
				docReceipt.setPartialhospitalization(true);
				payloadBO.setDocReceiptACK(docReceipt);
			}
			payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);*/
		}

		
//		ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
//		if(processActor == null){
//			/*processActor = new ProcessActorInfoType();
//			processActor.setEscalatedByUser("");
//			payloadBO.setProcessActorInfo(processActor);*/
//		}else if(processActor != null && processActor.getEscalatedByUser() == null){
//			/*processActor.setEscalatedByUser("");
//			payloadBO.setProcessActorInfo(processActor);*/
//		}

	//	humanTaskForBillEntry.setPayload(payloadBO);
		
		
		try{
		//submitBillEntryTask.execute(rodDTO.getStrUserName(),
				//humanTaskForBillEntry);
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			//dbCalService.initiateTaskProcedure(objArrayForSubmit);
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
			if(wrkFlowMap.get(SHAConstants.RECORD_TYPE) != null && ((String)wrkFlowMap.get(SHAConstants.RECORD_TYPE)).equalsIgnoreCase(SHAConstants.QUERY_REPLY)){
				
				Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
				dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR BILL_ENTRY ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
				/*BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForBillEntry.getNumber(), SHAConstants.SYS_RELEASE);
				submitBillEntryTask.execute("claimshead", humanTaskForBillEntry);*/

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
			}
		}

	}

	private Double calculateCurrentProvisionAmtForHealthPA (Double totalClaimedAmt , ReceiptOfDocumentsDTO rodDTO)
	{
		Double currentProvisionAmt = 0d;
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		Long claimKey = rodDTO.getClaimDTO().getKey();
		currentProvisionAmt = getPreauthApprovedAmt(claimKey,totalClaimedAmt);
		
		/*else if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Insured").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
		((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
		&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
		 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
		 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))*/ 
		//else if (("Insured").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()))
		/*{
			currentProvisionAmt = totalClaimedAmt;
		}
		else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claimTypeId))
		{
			currentProvisionAmt = totalClaimedAmt;
		}*/
		return currentProvisionAmt;

	}
	
	private Double calculateCurrentProvisionAmt (Double totalClaimedAmt , ReceiptOfDocumentsDTO rodDTO)
	{
		Double currentProvisionAmt = 0d;
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		Long claimKey = rodDTO.getClaimDTO().getKey();
		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
//			Double enhancementAmt = getApprovedAmtForFinalEnh(claimKey);
//			currentProvisionAmt = Math.max(totalClaimedAmt, enhancementAmt);
			
			/*Double approvedAmt = getPreauthApprovedAmt(claimKey);
			currentProvisionAmt = Math.max(totalClaimedAmt, approvedAmt);*/
			
			//Changes done for claim provision amt enhancement 
			currentProvisionAmt = getPreauthApprovedAmt(claimKey,totalClaimedAmt);
		}
		
		else if(("Insured").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			currentProvisionAmt = totalClaimedAmt;
		}
		
		/*else if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Insured").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
		((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
		&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
		 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
		 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))*/ 
		//else if (("Insured").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()))
		/*{
			currentProvisionAmt = totalClaimedAmt;
		}
		else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claimTypeId))
		{
			currentProvisionAmt = totalClaimedAmt;
		}*/
		return currentProvisionAmt;
	}
	/**
	 * 
	 * @param claimKey
	 * @return
	 */
	
//	private Double getApprovedAmtForFinalEnh(Long claimKey)
//	{
//		Query query = entityManager.createNamedQuery("Preauth.getFinalEnhAmtByClaim");
//		query.setParameter("claimKey", claimKey);
//		Double approvedAmt = 0d;
//		//Double approvedAmt = 0d;
//		List<Double> listOfApprovedAmt = query.getResultList();
//		if(null != listOfApprovedAmt && !listOfApprovedAmt.isEmpty())
//		{
//			
//			approvedAmt = listOfApprovedAmt.get(0);
//			if(null == approvedAmt)
//			{
//				approvedAmt = 0d;
//			}
//		}
//		return approvedAmt;
//	}
	
    private Double getPreauthApprovedAmt(Long claimKey,Double totalClaimedAmt){
    	
    	Double approvedAmt = 0d;
    	
    	Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		
		List<Preauth> preauthList = (List<Preauth>)query.getResultList();
		if(null != preauthList && ! preauthList.isEmpty()){
			Preauth preauth = preauthList.get(0);
			
			if(null != preauth && null != preauth.getStatus())
			{
				Long statusKey = preauth.getStatus().getKey();
				if(statusKey.equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || statusKey.equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS))
				{
					return totalClaimedAmt;
				}
				else
				{
					approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
				}
			}
			
			/*for (Preauth preauth : preauthList) {
				approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
			}*/
		}
    	return approvedAmt;
    	
    }
    
	
	   private Double getPreauthApprovedAmt(Long claimKey){
	    	
	    	Double approvedAmt = 0d;
	    	
	    	Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
			query.setParameter("claimkey", claimKey);
			
			List<Preauth> preauthList = (List<Preauth>)query.getResultList();
			if(null != preauthList && ! preauthList.isEmpty()){
				Preauth preauth = preauthList.get(0);
				approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
				/*for (Preauth preauth : preauthList) {
					approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
				}*/
			}
	    	return approvedAmt;
	    	
	    }
    
 private Double getPreauthApprovedAmtForValidation(Long claimKey){
    	
    	Double approvedAmt = 0d;
    	//Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
    	Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescorder");
		query.setParameter("claimkey", claimKey);
		
		List<Preauth> preauthList = (List<Preauth>)query.getResultList();
		if(null != preauthList && ! preauthList.isEmpty()){

			
			entityManager.refresh(preauthList.get(0));
			Preauth preauth = preauthList.get(0);
			
			if(null != preauth && null != preauth.getStatus())
			{
				Long statusKey = preauth.getStatus().getKey();
				/*if(statusKey.equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || statusKey.equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS))
				{
					return totalClaimedAmt;
				}*/
				
					approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
					
					if(preauth.getOtherBenefitApprovedAmt() != null){
						approvedAmt += preauth.getOtherBenefitApprovedAmt() != null ? preauth.getOtherBenefitApprovedAmt() : 0d;
					}
				
			}
			
			/*for (Preauth preauth : preauthList) {
				approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;

			}*/
		}
    	return approvedAmt;
    	
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
	
	private void populatePaymentDetails(Reimbursement reimbursement , ReceiptOfDocumentsDTO rodDTO)
	{
		if(null != reimbursement && null != rodDTO && null != rodDTO.getDocumentDetails())
		{
			reimbursement.setPayeeEmailId(rodDTO.getDocumentDetails().getEmailId());
			reimbursement.setAccountNumber(rodDTO.getDocumentDetails().getAccountNo());
			reimbursement.setAccountPreference(rodDTO.getDocumentDetails().getAccountPreference());
			reimbursement.setAccountType(rodDTO.getDocumentDetails().getAccountType());
			reimbursement.setPanNumber(rodDTO.getDocumentDetails().getPanNo());
			if(null != rodDTO.getDocumentDetails().getPayeeName())
				reimbursement.setPayeeName(rodDTO.getDocumentDetails().getPayeeName().getValue());
			reimbursement.setNameAsPerBankAccount(rodDTO.getDocumentDetails().getNameAsPerBank());
			reimbursement.setLegalHeirFirstName(rodDTO.getDocumentDetails().getLegalFirstName());
			reimbursement.setReasonForChange(rodDTO.getDocumentDetails().getReasonForChange());
			reimbursement.setPayableAt(rodDTO.getDocumentDetails().getPayableAt());
			
			if(rodDTO.getDto().getBankId() != null)
				reimbursement.setBankId(rodDTO.getDto().getBankId());
		}
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
	
	private Insured getInsuredByPolicyAndInsuredName(String policyNo , String insuredName)
	{
		Query query = entityManager.createNamedQuery("Insured.findByInsuredNameAndPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredName)
		query = query.setParameter("insuredName", insuredName);
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty())
			return insuredList.get(0);
		else
			return null;
	}
	
	private Insured getPAInsuredByPolicyAndInsuredName(String policyNo , String insuredName,String lobFlag)
	{
		Query query = entityManager.createNamedQuery("Insured.findByInsuredNameAndPolicyNoAndLobFlag");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredName)
		{
			query = query.setParameter("insuredName", insuredName);
		}
		query = query.setParameter("lobFlag", lobFlag);
		
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty())
			return insuredList.get(0);
		else
			return null;
	}
	
	
	
	private Insured getGPAInsuredByPolicyAndInsuredName(String policyNo , String insuredName,String lobFlag)
	{
		Query query = entityManager.createNamedQuery("Insured.findByInsuredNameAndPolicyNoAndLobFlag");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredName)
		{
			query = query.setParameter("insuredName", insuredName);
		}
		query = query.setParameter("lobFlag", lobFlag);
		
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty())
			return insuredList.get(0);
		else
			return null;
	}
	private Hospitals getHospitalByName(String hospitalName)
	{
		Query query = entityManager.createNamedQuery("Hospitals.findByHospitalName");
		query = query.setParameter("name", hospitalName);
		List<Hospitals> hospitalList = query.getResultList();
		if(null != hospitalList && !hospitalList.isEmpty())
		{
			return hospitalList.get(0);
		}
		else
		{
			return null;
		}
			
	}
	
	public Hospitals getHospitalsByKey(Long hospitalKey){
		
		Query findHospitalElement = entityManager
				.createNamedQuery("Hospitals.findByKey").setParameter("key", hospitalKey);
		
		List<Hospitals> hospitalList = (List<Hospitals>) findHospitalElement.getResultList();
		
		if(hospitalList != null && ! hospitalList.isEmpty()){
			return hospitalList.get(0);
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
	
	@SuppressWarnings("unchecked")
	public List<DocAcknowledgement> getNonCancelledAcknowledgement(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findNonCancelledAck");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query
				.getResultList();
		
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			for (DocAcknowledgement docAcknowledgement : reimbursementList) {
				entityManager.refresh(docAcknowledgement);
			}

		}

		return reimbursementList;

	}
	
	@SuppressWarnings("unchecked")
	public List<ClaimAmountDetails> getClaimAmountDetailsByPreauth(Long preauthKey) {
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<ClaimAmountDetails> resultList = query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (ClaimAmountDetails claimAmountDetails : resultList) {
				entityManager.refresh(claimAmountDetails);
			}
		}
		
		return resultList;
	}
	
	
/*	private void callRemainderProcess(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery) {
		ReimbReminder initiateRemainderTaskForReimb = BPMClientContext
				.getInitiateRemainderTaskForReimb(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		 DocReceiptACKType receiptAckType = new DocReceiptACKType();
		 receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		 receiptAckType.setKey(reimbursement.getKey());
		 payloadBO.setDocReceiptACK(receiptAckType);
		 
		RODType rodType = new RODType();
		rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		rodType.setKey(reimbursement.getKey());
		
		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
=======
		if(docAcknowledgement.getClaim().getClaimType() != null 
				&& docAcknowledgement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			if(countOfAckByClaimKey.equals(0l)){
				if(docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().equalsIgnoreCase("Y")){
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					claim.setCurrentProvisionAmount(preauth.getTotalApprovalAmount());
					claim.setClaimedAmount(0d);
					claim.setProvisionAmount(0d);
					entityManager.merge(claim);
					entityManager.flush();
					entityManager.clear();
					log.info("------Claim------>"+claim+"<------------");
				}else{
					claim.setCurrentProvisionAmount(0d);
					claim.setClaimedAmount(0d);
					claim.setProvisionAmount(0d);
					entityManager.merge(claim);
					entityManager.flush();
					entityManager.clear();
					log.info("------Claim------>"+claim+"<------------");
>>>>>>> FHOwithproduction
				}
			}
		}
		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		}
		CustomerType customerType = new CustomerType();
		customerType.setTreatmentType(rodDTO.getTreatmentType());
		
		if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
			Stage stage = entityManager.find(Stage.class, ReferenceTable.CREATE_ROD_STAGE_KEY);
			ClassificationType classification = new ClassificationType();
			classification.setSource(stage.getStageName());
			
			if(isQuery){
				classification.setType(SHAConstants.QUERY_REPLY);
			}
			
			payloadBO.setClassification(classification);
		}
		
		ClaimRequestType claimRequest = new ClaimRequestType();
		if(null != reimbursement.getClaim() && null != reimbursement.getClaim().getIntimation() && null != reimbursement.getClaim().getIntimation().getCpuCode() && null != reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode())
			claimRequest.setCpuCode(String.valueOf(reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode()));
		claimRequest.setResult("APPROVE");
		if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			claimRequest.setIsReconsider(true);
		}
		else
		{
			claimRequest.setIsReconsider(false);
		}
		
		payloadBO.setClaimRequest(claimRequest);

		payloadBO.setRod(rodType);
		payloadBO.setCustomer(customerType);
		try{
			initiateRemainderTaskForReimb.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
	}*/
	
	public List<BillEntryDetailsDTO> getBillEntryDetailsList(UploadDocumentDTO uploadDTO)
	{
		List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
		List<RODBillDetails> billEntryDetails = getBillEntryDetails(uploadDTO.getDocSummaryKey());
		if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
			for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
				/*
				 * <<<<<<< HEAD
				 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
				 * uploadDocumentDTO)); =======
				 */
				dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,uploadDTO));
				
				// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0

			}
			//uploadDTO.setBillEntryDetailList(dtoList);
		}
		return dtoList;
	}
	
	private BillEntryDetailsDTO getBillDetailsDTOForBilling(RODBillDetails billDetails,
			UploadDocumentDTO uploadDocumentDTO){/*, Map<Integer, Object> detailsMap) {*/
		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();
		
		if( null != billDetails.getRodDocumentSummaryKey())
			dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey().getKey());
		
		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		if(billDetails.getBillClassification()!=null){
			classificationValue.setId(billDetails.getBillClassification().getKey());
			classificationValue.setValue(billDetails.getBillClassification()
					.getValue());
			dto.setClassification(classificationValue);
			
		}
		dto.setItemNo(billDetails.getItemNumber());
		
		if(uploadDocumentDTO.getBillEntryDetailsDTO().getBillNo().isEmpty())
		{
				if(billDetails.getItemNumber() != null){
					dto.setBillNo(billDetails.getItemNumber().toString());
				}
		}else{
			dto.setBillNo(uploadDocumentDTO.getBillEntryDetailsDTO().getBillNo());
		}
		dto.setZonalRemarks(uploadDocumentDTO.getZonalRemarks());
		dto.setCorporateRemarks(uploadDocumentDTO.getCorporateRemarks());
		dto.setBillingRemarks(uploadDocumentDTO.getBillingRemarks());
		/*dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : 0d);*/
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : null);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getClaimedAmountBills());
		dto.setItemValue(billDetails.getClaimedAmountBills());
		if(billDetails.getRoomType() != null){
			MastersValue master = getMaster(billDetails.getRoomType().toLowerCase());
			if(master != null){
			SelectValue selected = new SelectValue(master.getKey(), master.getValue());
			dto.setRoomType(selected);
			}
		}
		
		SelectValue billCategoryvalue = new SelectValue();
		if((billDetails.getBillCategory() != null && billDetails.getBillCategory().getKey() != null && billDetails.getBillCategory().getKey().equals(46l)) && (billDetails.getBillClassification() != null && billDetails.getBillClassification().getKey() != null && billDetails.getBillClassification().getKey().equals(ReferenceTable.HOSPITALIZATION)) && (billDetails.getRodDocumentSummaryKey() != null && 
				billDetails.getRodDocumentSummaryKey().getReimbursement() != null && (billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim() != null	&& billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)))){

			billCategoryvalue.setId(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION);
			billCategoryvalue.setValue("Others with Proportionate Deduction");
		}
		else if(billDetails.getBillCategory() != null)
		{
			billCategoryvalue.setId(billDetails.getBillCategory().getKey());
			billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		}
		dto.setCategory(billCategoryvalue);
		

		/*
		 * Below values are added as a part of amount claimed table enhancement
		 */
		dto.setNoOfDaysAllowed(billDetails.getNoOfDaysPolicy());
		dto.setPerDayAmtProductBased(billDetails.getPerDayAmountPolicy());
		dto.setAmountAllowableAmount(billDetails.getTotalAmount());
		dto.setNonPayableProductBased(billDetails.getNonPayableAmountProduct());
		dto.setNonPayable(billDetails.getNonPayableAmount());
		
		if(null != billCategoryvalue && null != billCategoryvalue.getValue() && (("Deductibles").equalsIgnoreCase(billCategoryvalue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(billCategoryvalue.getValue().trim())))
		{
			if(null != billDetails.getClaimedAmountBills())
			{
				dto.setReasonableDeduction(billDetails.getClaimedAmountBills());
			}
			else
			{
				dto.setReasonableDeduction(billDetails.getDeductibleAmount());
			}
		}
		else
		{
			dto.setReasonableDeduction(billDetails.getDeductibleAmount());
		}
		dto.setTotalDisallowances(billDetails.getPayableAmount());
		dto.setNetPayableAmount(billDetails.getNetAmount());
		dto.setDeductibleOrNonPayableReason(billDetails.getNonPayableReason());
		dto.setMedicalRemarks(billDetails.getMedicalRemarks());
		dto.setKey(billDetails.getKey());
		
		dto.setKey(billDetails.getKey());
		
		
		/*uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(objReimbursement.getDateOfDischarge()));
		uploadDocumentDTO.setInsuredPatientName(objReimbursement.getClaim().getIntimation().getInsuredPatientName());*/
		/*Reimbursement objReimbursement = billDetails.getRodDocumentSummaryKey().getReimbursement();
		if(null != objReimbursement)
		{
			dto.setIntimationNo(objReimbursement.getClaim().getIntimation().getIntimationId());
			dto.setDateOfAdmission(SHAUtils.formatDate(objReimbursement.getDateOfAdmission()));
			dto.setDateOfDischarge(SHAUtils.formatDate(objReimbursement.getDateOfDischarge()));
			dto.setInsuredPatientName(objReimbursement.getClaim().getIntimation().getInsuredPatientName());
		}*/

		if (billDetails != null && billDetails.getIrdaLevel1Id() != null) {
			SelectValue irdaLevel1ValueByKey = 
					getIRDALevel1ValueByKey(billDetails.getIrdaLevel1Id());
			dto.setIrdaLevel1(irdaLevel1ValueByKey);
		}
		if (billDetails != null && billDetails.getIrdaLevel2Id() != null) {
			SelectValue irdaLevel2ValueByKey = getIRDALevel2ValueByKey(billDetails.getIrdaLevel2Id());
			dto.setIrdaLevel2(irdaLevel2ValueByKey);
		}
		if (billDetails != null && billDetails.getIrdaLevel3Id() != null) {
			SelectValue irdaLevel3ValueByKey = getIRDALevel3ValueByKey(billDetails.getIrdaLevel3Id());
			dto.setIrdaLevel3(irdaLevel3ValueByKey);
		}

		// dto.setProductBasedRoomRent((Double)detailsMap.get(8));

		// IRDA level is not yet implemented. Once done, will do the necessary
		// changes below.
		/*
		 * SelectValue irdaLevelValue = new SelectValue(); if(null !=
		 * billDetails.getIrdaLevel1Id()) {
		 * irdaLevelValue.setId(billDetails.getIrdaLevel1Id()); }
		 */

		return dto;
	}
 
	private BillEntryDetailsDTO getBillDetailsDTOForBillEntry(RODBillDetails billDetails) {
		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();
		
		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		classificationValue.setId(billDetails.getBillClassification().getKey());
		classificationValue.setValue(billDetails.getBillClassification()
				.getValue());
		dto.setClassification(classificationValue);
		dto.setItemNo(billDetails.getItemNumber());
		/*dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : 0d);*/
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : null);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getClaimedAmountBills());
		dto.setItemValue(billDetails.getClaimedAmountBills());
		SelectValue billCategoryvalue = new SelectValue();
		if((billDetails.getBillCategory() != null && billDetails.getBillCategory().getKey() != null && billDetails.getBillCategory().getKey().equals(46l)) && (billDetails.getBillClassification() != null && billDetails.getBillClassification().getKey() != null && billDetails.getBillClassification().getKey().equals(ReferenceTable.HOSPITALIZATION)) && (billDetails.getRodDocumentSummaryKey() != null && 
				billDetails.getRodDocumentSummaryKey().getReimbursement() != null && (billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim() != null	&& billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| billDetails.getRodDocumentSummaryKey().getReimbursement().getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)))){

			billCategoryvalue.setId(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION);
			billCategoryvalue.setValue("Others with Proportionate Deduction");
		}
		else if(billDetails.getBillCategory() != null)
		{
			billCategoryvalue.setId(billDetails.getBillCategory().getKey());
			billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		}
		dto.setCategory(billCategoryvalue);
		dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey().getKey());
		dto.setKey(billDetails.getKey());
		
		Reimbursement objReimbursement = billDetails.getRodDocumentSummaryKey().getReimbursement();
		if(null != objReimbursement)
		{
			dto.setIntimationNo(objReimbursement.getClaim().getIntimation().getIntimationId());
			dto.setDateOfAdmission(SHAUtils.formatDate(objReimbursement.getDateOfAdmission()));
			dto.setDateOfDischarge(SHAUtils.formatDate(objReimbursement.getDateOfDischarge()));
			dto.setInsuredPatientName(objReimbursement.getClaim().getIntimation().getInsuredPatientName());
		}
		
		return dto;
	}
	
public SelectValue getIRDALevel1ValueByKey(Long key){
		
		Query query = entityManager.createNamedQuery("MasIrdaLevel1.findByKey");
		query = query.setParameter("primaryKey", key);
		
		MasIrdaLevel1 masterValue = (MasIrdaLevel1)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}
		
		return selectValue;
		
	}
	
    public SelectValue getIRDALevel2ValueByKey(Long key){
		
		Query query = entityManager.createNamedQuery("MasIrdaLevel2.findByKey");
		query = query.setParameter("primaryKey", key);
		
		MasIrdaLevel2 masterValue = (MasIrdaLevel2)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}
		
		return selectValue;
		
	}
    
    public SelectValue getIRDALevel3ValueByKey(Long key){
		
		Query query = entityManager.createNamedQuery("MasIrdaLevel3.findByKey");
		query = query.setParameter("primaryKey", key);
		
		MasIrdaLevel3 masterValue = (MasIrdaLevel3)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}
		
		return selectValue;
		
	  }

    public String getHospitalCityClass(Long policyKey)
    {
    	Query query = entityManager.createNamedQuery("Policy.findByKey");
    	query = query.setParameter("policyKey", policyKey);
    	List<Policy> policyList = query.getResultList();
    	if(null != policyList &&  !policyList.isEmpty())
    	{
    		Policy policy = policyList.get(0);
    		if(null != policy)
    		{
    			OrganaizationUnit branch = getBranchCode(policy.getHomeOfficeCode());
    			if(null != branch)
    			{
    				MASPincode masPinCode = getPincode(branch.getPinCode());
    				if(null != masPinCode && null != masPinCode.getValue())
    				{
    						MASPincodeZoneClass pinCode = getClass(Long.parseLong(masPinCode.getValue()));
    						if(null != pinCode)
    							return pinCode.getCityClass();
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public OrganaizationUnit getBranchCode(String branchCode)
    {
    	Query query = entityManager.createNamedQuery("OrganaizationUnit.findByBranchode");
    	query = query.setParameter("parentKey", branchCode);
    	List<OrganaizationUnit> orgList = query.getResultList();
    	if(null != orgList && !orgList.isEmpty())
    	{
    		return orgList.get(0);
    	}
    	return null;
    }
    
    private MASPincodeZoneClass getClass(Long pincode)
    {
    	Query query = entityManager.createNamedQuery("MASPincodeZoneClass.findByPinCode");
    	query = query.setParameter("pincode", pincode);
    	List<MASPincodeZoneClass> pinCodeList = query.getResultList();
    	if(null != pinCodeList && !pinCodeList.isEmpty())
    	{
    		return pinCodeList.get(0);
    	}
    	return null;
    	
    }
    
    private MASPincode getPincode(Long pincode)
    {
    	Query query = entityManager.createNamedQuery("MASPincode.findByPinCode");
    	query = query.setParameter("primaryKey", pincode);
    	List<MASPincode> pinCodeList = query.getResultList();
    	if(null != pinCodeList && !pinCodeList.isEmpty())
    	{
    		entityManager.refresh(pinCodeList.get(0));
    		return pinCodeList.get(0);
    	}
    	return null;
    	
    }
    
    private AcknowledgeDocument getAcknowledgeDocObjByKey(Long key)
    {
    	Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByKey");
    	query = query.setParameter("key",key);
    	List<AcknowledgeDocument> ackDocList = query.getResultList();
    	if(null != ackDocList && !ackDocList.isEmpty())
    	{
    		entityManager.refresh(ackDocList.get(0));
    		return ackDocList.get(0);
    	}
    	return null;
    }
    
    
    
    public List<UploadDocumentDTO> getUploadDocumentForAcknowledgementDocKey(Long key)
    {
    	Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByDocAcknowledgementKey");
    	query = query.setParameter("docAckKey",key);
    	List<AcknowledgeDocument> ackDocList = query.getResultList();
    	List<UploadDocumentDTO> uploadDocList = null;
    	if(null != ackDocList && !ackDocList.isEmpty())
    	{
    		uploadDocList = new ArrayList<UploadDocumentDTO>();
    		for (AcknowledgeDocument acknowledgeDocument : ackDocList) {
    			UploadDocumentDTO uploadDoc = new UploadDocumentDTO();
    			uploadDoc.setFileName(acknowledgeDocument.getFileName());
    			if(null != acknowledgeDocument.getFileType())
    			{
    				SelectValue selValue = new SelectValue();
    				selValue.setId(acknowledgeDocument.getFileType().getKey());
    				selValue.setValue(acknowledgeDocument.getFileType().getValue());
    				uploadDoc.setFileType(selValue);
    				uploadDoc.setFileTypeValue(acknowledgeDocument.getFileType().getValue());
    			}
    			uploadDoc.setBillNo(acknowledgeDocument.getBillNumber());
    			uploadDoc.setBillDate(acknowledgeDocument.getBillDate());
    			uploadDoc.setNoOfItems(acknowledgeDocument.getNoOfItems());
    			uploadDoc.setBillValue(acknowledgeDocument.getBillAmount());
    			uploadDoc.setAckDocKey(acknowledgeDocument.getKey());
    			uploadDoc.setDmsDocToken(acknowledgeDocument.getDocToken());
    			uploadDocList.add(uploadDoc);
			}
    	}
    	return uploadDocList;
    }
	/*private void callRemainderProcess(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement,Boolean isQuery) {
		ReimbReminder initiateRemainderTaskForReimb = BPMClientContext
				.getInitiateRemainderTaskForReimb(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		 DocReceiptACKType receiptAckType = new DocReceiptACKType();
		 receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		 receiptAckType.setKey(reimbursement.getKey());
		 payloadBO.setDocReceiptACK(receiptAckType);
		 
		RODType rodType = new RODType();
		rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		rodType.setKey(reimbursement.getKey());
		
		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
=======
    	}
    	return uploadDocList;
    }
    
    public PAUploadDocumentCloseClaimDTO uploadDocumentForPACloseClaim(PACloseClaimPageDTO bean){
    	
    	DocumentDetails documentDetails = new DocumentDetails();
    	
    	documentDetails.setIntimationNumber(bean.getIntimationNumber());
    	documentDetails.setClaimNumber(bean.getClaimNumber());
    	documentDetails.setFileName(bean.getFileName());
    	documentDetails.setDocumentType(bean.getFileType());
    	documentDetails.setReimbursementNumber(bean.getReimbursmentNumber());
    	documentDetails.setCashlessNumber(bean.getCashlessNumber());
    	if(bean.getFileToken() != null){
    		documentDetails.setDocumentToken(Long.valueOf(bean.getFileToken()));
    	}
    	documentDetails.setDocumentSource("CLOSE_ClAIM");
    	documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
    	documentDetails.setCreatedBy(bean.getUserName());
    	documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	documentDetails.setDeletedFlag("N");
    	
//    	entityManager.persist(documentDetails);
//    	entityManager.flush();
    	
    	PAUploadDocumentCloseClaimDTO dto = new PAUploadDocumentCloseClaimDTO();
    	dto.setFileName(bean.getFileName());
    	dto.setStrDateAndTime(SHAUtils.formateDateForHistory(documentDetails.getCreatedDate()));
    	dto.setReferenceNo(bean.getReferenceNo());
    	dto.setDocumentDetails(documentDetails);
    	
    	return dto;
    	
    }
    
public UploadDocumentCloseClaimDTO uploadDocumentForCloseClaim(CloseClaimPageDTO bean){
    	
    	DocumentDetails documentDetails = new DocumentDetails();
    	
    	documentDetails.setIntimationNumber(bean.getIntimationNumber());
    	documentDetails.setClaimNumber(bean.getClaimNumber());
    	documentDetails.setFileName(bean.getFileName());
    	documentDetails.setDocumentType(bean.getFileType());
    	documentDetails.setReimbursementNumber(bean.getReimbursmentNumber());
    	documentDetails.setCashlessNumber(bean.getCashlessNumber());
    	if(bean.getFileToken() != null){
    		documentDetails.setDocumentToken(Long.valueOf(bean.getFileToken()));
    	}
    	documentDetails.setDocumentSource("CLOSE_ClAIM");
    	documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
    	documentDetails.setCreatedBy(bean.getUserName());
    	documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	documentDetails.setDeletedFlag("N");
    	
//    	entityManager.persist(documentDetails);
//    	entityManager.flush();
    	
    	UploadDocumentCloseClaimDTO dto = new UploadDocumentCloseClaimDTO();
    	dto.setFileName(bean.getFileName());
    	dto.setStrDateAndTime(SHAUtils.formateDateForHistory(documentDetails.getCreatedDate()));
    	dto.setReferenceNo(bean.getReferenceNo());
    	dto.setDocumentDetails(documentDetails);
    	
    	return dto;
    	
    }
    
    public void updateDeletedDocumentForCloseClaim(UploadDocumentCloseClaimDTO bean){
    	if(bean.getDocumentDetailsKey() != null){
    		
    		DocumentDetails documentDetails = getDocumentDetailsByKey(bean.getDocumentDetailsKey());
    		
    		documentDetails.setDeletedFlag("");
    	}
    }
    
    public void submitCloseClaim(CloseClaimPageDTO bean){
    	
    	CloseClaim closeClaim = new CloseClaim();
    	
        Claim claim = getClaimByClaimKey(bean.getClaimKey());
        
    	if(bean.getReasonId() != null){
    	MastersValue closeReason = new MastersValue();
    	closeReason.setKey(bean.getReasonId().getId());
    	closeReason.setValue(bean.getReasonId().getValue());
    	closeClaim.setClosingReasonId(closeReason);
    	
    	}
    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
    	closeClaim.setCreatedBy(bean.getUserName());
    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setCloseType("C");
    	closeClaim.setClaim(claim);
    	closeClaim.setStage(claim.getStage());
    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
    	
    	Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
    	
    	closeClaim.setStatus(status);
    	
    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
        entityManager.persist(closeClaim);
        entityManager.flush();

        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null &&  preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
							&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
        		     Stage preauthStage = new Stage();
        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//        		     lastPreauth.setStage(preauthStage);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        		     entityManager.merge(lastPreauth);
        		     entityManager.flush();
        		     
        		}
        	}
        }
        
        claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        claim.setCurrentProvisionAmount(0d);
        entityManager.merge(claim);
        entityManager.flush();
        
        List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
        for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
			DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
			entityManager.persist(documentDetails);
			entityManager.flush();
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		}
<<<<<<< HEAD
=======
		return reimbursementObj;
		//entityManager.refresh(reimbursement);
		
	}

	//private void submitBillEntryTaskToBPM(ReceiptOfDocumentsDTO rodDTO, Claim claimObj, Boolean shouldSkipZMR) {
	/*
		SubmitBillEntryTask submitBillEntryTask = BPMClientContext
				.getSubmitBillEntryTask(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForBillEntry = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForBillEntry.getPayload();
		//humanTaskForBillEntry.setOutcome("APPROVE");
>>>>>>> FHOwithproduction

    	//need to implement provision amount;
    	
//    	PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		
//		PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
//		
//		
//		
//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				
//				Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
//
//			}
//			
//			SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//		    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//		}

    	
    }
    
public void submitPACloseClaim(PACloseClaimPageDTO bean){
    	
    	CloseClaim closeClaim = new CloseClaim();
    	
        Claim claim = getClaimByClaimKey(bean.getClaimKey());
        
    	if(bean.getReasonId() != null){
    	MastersValue closeReason = new MastersValue();
    	closeReason.setKey(bean.getReasonId().getId());
    	closeReason.setValue(bean.getReasonId().getValue());
    	closeClaim.setClosingReasonId(closeReason);
    	
    	}
    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
    	closeClaim.setCreatedBy(bean.getUserName());
    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setCloseType("C");
    	closeClaim.setClaim(claim);
    	closeClaim.setStage(claim.getStage());
    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
    	
    	Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
    	
    	closeClaim.setStatus(status);
    	
    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
        entityManager.persist(closeClaim);
        entityManager.flush();

        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null &&  preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
							&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
        		     Stage preauthStage = new Stage();
        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//        		     lastPreauth.setStage(preauthStage);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        		     entityManager.merge(lastPreauth);
        		     entityManager.flush();
        		     
        		}
        	}
        }
        
        claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        claim.setCurrentProvisionAmount(0d);
        entityManager.merge(claim);
        entityManager.flush();
        
        List<PAUploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
        for (PAUploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
			DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
			entityManager.persist(documentDetails);
			entityManager.flush();
		}

    	//need to implement provision amount;
    	
//    	PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		
//		PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
//		
//		
//		
//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				
//				Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
//
//			}
//			
//			SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//		    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//		}

    	
    }
    
    
	public List<Preauth> getPreauthListByClaimKey(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			
			return preauthList;
		}
<<<<<<< HEAD
	}*/   

	
	 public void updateReimbursmentForCloseClaimPANonHealth(String userName,Reimbursement reimbursement){
	    	
	      	reimbursement.setCurrentProvisionAmt(0d);
			reimbursement.setBenApprovedAmt(0d);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			
			HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMapForCloseClaim();
			
			Long stageKey = reimbursement.getStage().getKey();
			
			if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
					stageKey = ReferenceTable.BILLING_STAGE;
				}
			}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
					stageKey = ReferenceTable.FINANCIAL_STAGE;
				}
			}
			
			Long statusKey = stageAndStatusMap.get(stageKey);
			
			Stage stage = entityManager.find(Stage.class, stageKey);
			Status status = entityManager.find(Status.class, statusKey);
			
			reimbursement.setStage(stage);
			reimbursement.setStatus(status);
			reimbursement.setModifiedBy(userName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
			entityManager.merge(reimbursement);
			entityManager.flush();
			
			

			List<PedValidation> diagnosis = getDiagnosis(reimbursement.getKey());
			for (PedValidation pedValidation : diagnosis) {
				
				Double approvedAmount = pedValidation.getApproveAmount();
				pedValidation.setDiffAmount(approvedAmount);
				pedValidation.setNetAmount(0d);
				pedValidation.setProcessFlag("C");
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				entityManager.merge(pedValidation);
				entityManager.flush();
			}
			
			List<Procedure> procedure = getProcedure(reimbursement.getKey());
			for (Procedure procedure2 : procedure) {
				
				Double approvedAmount = procedure2.getApprovedAmount();
				procedure2.setDiffAmount(approvedAmount);
				procedure2.setNetAmount(0d);
				procedure2.setProcessFlag("C");
				procedure2.setStage(reimbursement.getStage());
				procedure2.setStatus(reimbursement.getStatus());
				entityManager.merge(procedure2);
				entityManager.flush();
			}
			
			List<PAAdditionalCovers> addOnCovers = getAdditionalCoversByRodKey(reimbursement.getKey());
			if(addOnCovers != null){
				for(PAAdditionalCovers addOn : addOnCovers){
					addOn.setProvisionAmount(0d);
					entityManager.merge(addOn);
					entityManager.flush();
				}	
			}
			
			List<PAOptionalCover> optionalCovers = getOptionalCoversByRodKey(reimbursement.getKey());
			if(optionalCovers != null){
				for(PAOptionalCover optional : optionalCovers){
					optional.setProvisionAmount(0d);
					entityManager.merge(optional);
					entityManager.flush();
				}	
			}
		}
    
    public void updateReimbursmentForCloseClaim(String userName,Reimbursement reimbursement){
    	
       
		
		reimbursement.setCurrentProvisionAmt(0d);
		HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMapForCloseClaim();
		
		Long stageKey = reimbursement.getStage().getKey();
		
		if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
			if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
				stageKey = ReferenceTable.BILLING_STAGE;
			}
		}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
			if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
				stageKey = ReferenceTable.FINANCIAL_STAGE;
			}
		}
		
		Long statusKey = stageAndStatusMap.get(stageKey);
		
		Stage stage = entityManager.find(Stage.class, stageKey);
		Status status = entityManager.find(Status.class, statusKey);
		
		reimbursement.setStage(stage);
		reimbursement.setStatus(status);
		reimbursement.setModifiedBy(userName);
		reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		entityManager.merge(reimbursement);
		entityManager.flush();
		
		

		List<PedValidation> diagnosis = getDiagnosis(reimbursement.getKey());
		for (PedValidation pedValidation : diagnosis) {
			
			Double approvedAmount = pedValidation.getApproveAmount();
			pedValidation.setDiffAmount(approvedAmount);
			pedValidation.setNetAmount(0d);
			pedValidation.setProcessFlag("C");
			pedValidation.setStage(reimbursement.getStage());
			pedValidation.setStatus(reimbursement.getStatus());
			entityManager.merge(pedValidation);
			entityManager.flush();
		}
		
		List<Procedure> procedure = getProcedure(reimbursement.getKey());
		for (Procedure procedure2 : procedure) {
			
			Double approvedAmount = procedure2.getApprovedAmount();
			procedure2.setDiffAmount(approvedAmount);
			procedure2.setNetAmount(0d);
			procedure2.setProcessFlag("C");
			procedure2.setStage(reimbursement.getStage());
			procedure2.setStatus(reimbursement.getStatus());
			entityManager.merge(procedure2);
			entityManager.flush();
		}


    }
    
    public void updateReimbursementReopenClaim(String userName,Reimbursement reimbursement){

		HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMapForReOpenClaim();
		
		Long StatusKey = stageAndStatusMap.get(reimbursement.getStage().getKey());
		
		Status status = new Status();
		status.setKey(StatusKey);

	    if(reimbursement !=  null){
	    	
	   	 //IMSSUPPOR-37576 rejection rod need stop close
	    	if(!ReferenceTable.getRejectedRODKeys().containsKey(reimbursement.getStatus().getKey())){
	    		
		    	reimbursement.setModifiedBy(userName);
		    	reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		    	reimbursement.setStatus(status);
	//	    	reimbursement.setCurrentProvisionAmt(bean.getProvisionAmount());
//		    	Double provisionAmount = getProvisionAmount(reimbursement);
//		    	reimbursement.setCurrentProvisionAmt(provisionAmount);
		    	
	             if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
		    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
		    		
		    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
		    		
		    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
		    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
		
		    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
		    	}
		    	else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
		    		
		    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
		    	}
		        entityManager.merge(reimbursement);
		        entityManager.flush();
		        log.info("------Reimbursement------>"+reimbursement+"<------------");
	    }
    }

    }
    
	public Double getProvisionAmount(Reimbursement reimbursement, Claim claim){
		
		Double claimedAmount = 0d;
		
    	if(reimbursement.getStage().getKey().equals(ReferenceTable.CREATE_ROD_STAGE_KEY)){
    		
    		claimedAmount = getClaimedAmount(reimbursement,claim);
    		
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)){
    		claimedAmount = getClaimedAmount(reimbursement,claim);
    	
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
    		
    		claimedAmount = getClaimedAmount(reimbursement,claim);

    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
    		
    		if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS) ||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_REOPENED) && reimbursement.getApprovalRemarks() != null
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CLOSED) && reimbursement.getApprovalRemarks() != null){
    			
    			
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
    			
			}else{
				claimedAmount = getClaimedAmount(reimbursement,claim);                              //will be executed if staus is refer to specialist, escalate, investigation from MA stage
			}
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
 
    		claimedAmount = reimbursement.getBillingApprovedAmount();
    		
    		if(claimedAmount == null && reimbursement.getApprovedAmount() != null){
    			claimedAmount = reimbursement.getApprovedAmount();
    		}
    		
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
 
    		claimedAmount = reimbursement.getFinancialApprovedAmount();
    		if(claimedAmount == null && reimbursement.getBillingApprovedAmount() != null){
    			claimedAmount = reimbursement.getBillingApprovedAmount();
    		}
    	}
    	
    	return claimedAmount;
		
	}
	
	public void updateNetAmountForDiagnosisAndProcedure(Reimbursement reimbursment){
		List<PedValidation> diagnosis = getDiagnosis(reimbursment.getKey());
		for (PedValidation pedValidation : diagnosis) {
			
			Double diffAmount = pedValidation.getDiffAmount();
			pedValidation.setApproveAmount(diffAmount);
			pedValidation.setNetApprovedAmount(diffAmount);
			pedValidation.setDiffAmount(0d);
			pedValidation.setProcessFlag("F");
			pedValidation.setStage(reimbursment.getStage());
			pedValidation.setStatus(reimbursment.getStatus());
			entityManager.merge(pedValidation);
			entityManager.flush();
			log.info("------PedValidation------>"+pedValidation+"<------------");
		}
		
		List<Procedure> procedure = getProcedure(reimbursment.getKey());
		for (Procedure procedure2 : procedure) {
			
			Double diffAmount = procedure2.getDiffAmount();
			procedure2.setApprovedAmount(diffAmount);
			procedure2.setNetApprovedAmount(diffAmount);
			procedure2.setDiffAmount(0d);
			procedure2.setProcessFlag("F");
			procedure2.setStage(reimbursment.getStage());
			procedure2.setStatus(reimbursment.getStatus());
			entityManager.merge(procedure2);
			entityManager.flush();
			log.info("------Procedure------>"+procedure2+"<------------");
		}
	}

	
	public Double getClaimedAmount(Reimbursement reimbursement, Claim claim){
		
		DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
		
		Double totalClaimedAmount = 0d;
		
		totalClaimedAmount = getClaimedAmountForReimbursement(reimbursement);
		
		if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			
			if(docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)
					&& docAcknowLedgement.getHospitalisationFlag() != null && docAcknowLedgement.getHospitalisationFlag().equalsIgnoreCase("Y")
					&& docAcknowLedgement.getPreHospitalisationFlag() != null && docAcknowLedgement.getPreHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPostHospitalisationFlag() != null && docAcknowLedgement.getPostHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPartialHospitalisationFlag() != null && docAcknowLedgement.getPartialHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getLumpsumAmountFlag() != null && docAcknowLedgement.getLumpsumAmountFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPatientCareFlag() != null && docAcknowLedgement.getPatientCareFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getHospitalCashFlag() != null && docAcknowLedgement.getHospitalCashFlag().equalsIgnoreCase("N")){
				
				totalClaimedAmount = getPreauthApprovedAmt(reimbursement.getClaim().getKey());
				
			}
		}
		
		Double balanceSI = getBalanceSI(reimbursement,claim);
		if(balanceSI != null){
			
			totalClaimedAmount = Math.min(balanceSI, totalClaimedAmount);
			
		}
		
		return totalClaimedAmount;
	}
	
	public Double getBalanceSI(Reimbursement reimbursement,Claim claim)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Long policyKey = reimbursement.getClaim().getIntimation().getPolicy().getKey();
		if(null != reimbursement.getClaim().getIntimation().getInsured().getInsuredId())
		{
			sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(reimbursement.getClaim().getIntimation().getInsured().getInsuredId()),
					policyKey,reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
		}
		Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey , reimbursement.getClaim().getIntimation().getInsured().getKey() , reimbursement.getClaim().getKey(),sumInsured,reimbursement.getClaim().getIntimation().getKey());
		Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
		return balanceSI;
	
	}
	
	public Double getClaimedAmountForReimbursement(Reimbursement reimbursement) {
		try {
			Double claimedAmount = 0.0;

			DocAcknowledgement docAcknowledgment = reimbursement.getDocAcknowLedgement();
			
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", reimbursement.getKey());
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
		
			Double hospitalizationClaimedAmount = null != docAcknowledgment.getHospitalizationClaimedAmount() ? docAcknowledgment.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgment.getPostHospitalizationClaimedAmount() ? docAcknowledgment.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgment.getPreHospitalizationClaimedAmount() ? docAcknowledgment.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}
    
    public List<PedValidation> getDiagnosis(Long transactionKey) {	
 		

  		List<PedValidation> resultList = new ArrayList<PedValidation>();
  		
  		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
  		query.setParameter("transactionKey", transactionKey);
  		
  		resultList = (List<PedValidation>)query.getResultList();
  	    
  		return resultList;

  	}
    
	@SuppressWarnings("unchecked")
	public List<Procedure> getProcedure(Long transactionKey){
		
			Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
			query.setParameter("transactionKey", transactionKey);
			
			List<Procedure> resultList = (List<Procedure>)query.getResultList();
			
			return resultList;
	}

    
    public void submitReopenClaim(ReopenClaimPageDTO bean){
    	
    	Claim claim = getClaimByClaimKey(bean.getClaimKey());
    	
    	CloseClaim closeClaim = getCloseClaim(claim.getKey());
    	
    	if(bean.getReasonForReopen() != null){
    		MastersValue reason = new MastersValue();
    		reason.setKey(bean.getReasonForReopen().getId());
    		reason.setValue(bean.getReasonForReopen().getValue());
    		closeClaim.setReOpenReasonId(reason);
    	}
    	
    	closeClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setModifiedBy(bean.getUserName());
    	
    	Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_REOPENED_STATUS);
    	closeClaim.setStatus(status);
    	closeClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setReopenProvisonAmt(bean.getProvisionAmount());
    	entityManager.merge(closeClaim);
    	entityManager.flush();
    	
        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null && preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(lastPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_CLOSED_STATUS)){
        		   
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_REOPENED_STATUS);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        		     entityManager.merge(lastPreauth);
        		     entityManager.flush();
        		     
        		}
        	}
        }
    	
    	claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        if(bean.getProvisionAmount() != null){
         claim.setCurrentProvisionAmount(bean.getProvisionAmount());
        }
        entityManager.merge(claim);
        entityManager.flush();
        
        
        
//        PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		ReopenAllClaimTask reOpenClaimTask = BPMClientContext.getReOpAllClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//			
//		PagedTaskList tasks = reOpenClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
//		
//		
//		
//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				
//				Reimbursement reimbursementByKey = getReimbursementByKey(reimbursementKey);
//				
//				CloseClaim alreadyRodClosed = getAlreadyRodClosed(reimbursementByKey.getKey());
//				
//	            if(alreadyRodClosed == null || (alreadyRodClosed != null && ! ReferenceTable.getCloseClaimKeys().containsKey(alreadyRodClosed.getStatus().getKey()))){
//
//					HashMap<Long, Long> stageAndStatusMapForCloseClaim = getStageAndStatusMapForReOpenClaim();
//					
//					Long statusKey = stageAndStatusMapForCloseClaim.get(reimbursementByKey.getStage().getKey());
//					
//					if(statusKey != null){
//						Status reimbursmentStatus = new Status();
//						reimbursmentStatus.setKey(statusKey);
//						reimbursementByKey.setStatus(reimbursmentStatus);
//						reimbursementByKey.setModifiedBy(bean.getUserName());
//						reimbursementByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//						entityManager.merge(reimbursementByKey);
//						entityManager.flush();
//					}
//					
//					SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//				    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.RESUME_HUMANTASK);
//					
//	            }
//
//			}
//		}

    }
    
public void submitReopenPAClaim(PAReopenClaimPageDTO bean){
    	
    	Claim claim = getClaimByClaimKey(bean.getClaimKey());
    	
    	CloseClaim closeClaim = getCloseClaim(claim.getKey());
    	
    	if(bean.getReasonForReopen() != null){
    		MastersValue reason = new MastersValue();
    		reason.setKey(bean.getReasonForReopen().getId());
    		reason.setValue(bean.getReasonForReopen().getValue());
    		closeClaim.setReOpenReasonId(reason);
    	}
    	
    	closeClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setModifiedBy(bean.getUserName());
    	
    	Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_REOPENED_STATUS);
    	closeClaim.setStatus(status);
    	closeClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setReopenProvisonAmt(bean.getProvisionAmount());
    	entityManager.merge(closeClaim);
    	entityManager.flush();
    	
        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null && preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(lastPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_CLOSED_STATUS)){
        		   
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_REOPENED_STATUS);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        		     entityManager.merge(lastPreauth);
        		     entityManager.flush();
        		     
        		}
        	}
        }
    	
    	claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        claim.setCurrentProvisionAmount(0d);
        claim.setCloseDate(new Timestamp(System.currentTimeMillis()));
        if(bean.getProvisionAmount() != null){
         claim.setCurrentProvisionAmount(bean.getProvisionAmount());
        }
        entityManager.merge(claim);
        entityManager.flush();
        
        
        
//        PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		ReopenAllClaimTask reOpenClaimTask = BPMClientContext.getReOpAllClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//			
//		PagedTaskList tasks = reOpenClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
//		
//		
//		
//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				
//				Reimbursement reimbursementByKey = getReimbursementByKey(reimbursementKey);
//				
//				CloseClaim alreadyRodClosed = getAlreadyRodClosed(reimbursementByKey.getKey());
//				
//	            if(alreadyRodClosed == null || (alreadyRodClosed != null && ! ReferenceTable.getCloseClaimKeys().containsKey(alreadyRodClosed.getStatus().getKey()))){
//
//					HashMap<Long, Long> stageAndStatusMapForCloseClaim = getStageAndStatusMapForReOpenClaim();
//					
//					Long statusKey = stageAndStatusMapForCloseClaim.get(reimbursementByKey.getStage().getKey());
//					
//					if(statusKey != null){
//						Status reimbursmentStatus = new Status();
//						reimbursmentStatus.setKey(statusKey);
//						reimbursementByKey.setStatus(reimbursmentStatus);
//						reimbursementByKey.setModifiedBy(bean.getUserName());
//						reimbursementByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//						entityManager.merge(reimbursementByKey);
//						entityManager.flush();
//					}
//					
//					SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//				    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.RESUME_HUMANTASK);
//					
//	            }
//
//			}
//		}

    }
    

public void submitReopenPAHealthClaim(PAHealthReopenClaimPageDTO bean){
	
	Claim claim = getClaimByClaimKey(bean.getClaimKey());
	
	CloseClaim closeClaim = getCloseClaim(claim.getKey());
	
	if(bean.getReasonForReopen() != null){
		MastersValue reason = new MastersValue();
		reason.setKey(bean.getReasonForReopen().getId());
		reason.setValue(bean.getReasonForReopen().getValue());
		closeClaim.setReOpenReasonId(reason);
	}
	
	closeClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
	closeClaim.setModifiedBy(bean.getUserName());
	
	Status status = new Status();
	status.setKey(ReferenceTable.CLAIM_REOPENED_STATUS);
	closeClaim.setStatus(status);
	closeClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	closeClaim.setReopenProvisonAmt(bean.getProvisionAmount());
	entityManager.merge(closeClaim);
	entityManager.flush();
	
    if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
    	
    	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
    	
    	if(preauthList != null && preauthList.size() == 1){
    		Preauth lastPreauth = preauthList.get(0);
    		if(lastPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_CLOSED_STATUS)){
    		   
    		     Status preauthStatus = new Status();
    		     preauthStatus.setKey(ReferenceTable.PREAUTH_REOPENED_STATUS);
    		     lastPreauth.setStatus(preauthStatus);
    		     lastPreauth.setModifiedBy(bean.getUserName());
    		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
    		     entityManager.merge(lastPreauth);
    		     entityManager.flush();
    		     
    		}
    	}
    }
	
	claim.setStatus(status);
    claim.setModifiedBy(bean.getUserName());
    claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
    if(bean.getProvisionAmount() != null){
     claim.setCurrentProvisionAmount(bean.getProvisionAmount());
     //IMSSUPPOR-28254 - For reopening claim.
     if(claim.getPaHospExpenseAmt() != null){
    	 claim.setPaHospExpenseAmt(bean.getProvisionAmount());
     }
    }
    entityManager.merge(claim);
    entityManager.flush();
    
    
    
//    PayloadBOType payloadType = new PayloadBOType();
//	IntimationType intimationType = new IntimationType();
//	intimationType.setIntimationNumber(bean.getIntimationNumber());
//	payloadType.setIntimation(intimationType);
//	
//	ReopenAllClaimTask reOpenClaimTask = BPMClientContext.getReOpAllClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		
//	PagedTaskList tasks = reOpenClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//	
//	List<HumanTask> humanTasks = tasks.getHumanTasks();
//	
//	
//	
//	for (HumanTask humanTask : humanTasks) {
//		
//		if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//				&& humanTask.getPayload().getRod().getKey() != null){
//			
//			Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//			
//			Reimbursement reimbursementByKey = getReimbursementByKey(reimbursementKey);
//			
//			CloseClaim alreadyRodClosed = getAlreadyRodClosed(reimbursementByKey.getKey());
//			
//            if(alreadyRodClosed == null || (alreadyRodClosed != null && ! ReferenceTable.getCloseClaimKeys().containsKey(alreadyRodClosed.getStatus().getKey()))){
//
//				HashMap<Long, Long> stageAndStatusMapForCloseClaim = getStageAndStatusMapForReOpenClaim();
//				
//				Long statusKey = stageAndStatusMapForCloseClaim.get(reimbursementByKey.getStage().getKey());
//				
//				if(statusKey != null){
//					Status reimbursmentStatus = new Status();
//					reimbursmentStatus.setKey(statusKey);
//					reimbursementByKey.setStatus(reimbursmentStatus);
//					reimbursementByKey.setModifiedBy(bean.getUserName());
//					reimbursementByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//					entityManager.merge(reimbursementByKey);
//					entityManager.flush();
//				}
//				
//				SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//			    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.RESUME_HUMANTASK);
//				
//            }
//
//		}
//	}

}
   

 public void submitCloseClaimForRodLevel(CloseClaimPageDTO bean,Reimbursement reimbursement){
    	
    	CloseClaim closeClaim = new CloseClaim();
    	
        Claim claim = getClaimByClaimKey(bean.getClaimKey());
        
    	if(bean.getReasonId() != null){
    	MastersValue closeReason = new MastersValue();
    	closeReason.setKey(bean.getReasonId().getId());
    	closeReason.setValue(bean.getReasonId().getValue());
    	closeClaim.setClosingReasonId(closeReason);
    	
    	}
    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
    	closeClaim.setCreatedBy(bean.getUserName());
    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setCloseType("R");
    	closeClaim.setClaim(claim);
    	closeClaim.setReimbursement(reimbursement);
    	closeClaim.setStage(reimbursement.getStage());
    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
    	
    	/*Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);*/
    	
    	closeClaim.setStatus(reimbursement.getStatus());
    	
    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
        entityManager.persist(closeClaim);
        entityManager.flush();

        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null &&  preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
							&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
        		     Stage preauthStage = new Stage();
        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//        		     lastPreauth.setStage(preauthStage);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//        		     entityManager.merge(lastPreauth);
//        		     entityManager.flush();
        		     
        		}
        	}
        }
        
        /*claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        claim.setCurrentProvisionAmount(0d);
        claim.setCloseDate(new Timestamp(System.currentTimeMillis()));*/
//        entityManager.merge(claim);
//        entityManager.flush();
        
        List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
        for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
			DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
			entityManager.persist(documentDetails);
			entityManager.flush();
		}

    	//need to implement provision amount;
    	
//    	PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		
//		PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
//		
//		
//		
//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				
//				Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);

//
//			}
//			
//			SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//		    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//		}

    	
    }
    
	/*public List<Preauth> getPreauthListByClaimKey(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
=======*/
 
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
 
 public CloseClaim getCloseClaim(Long claimKey){
		
		Query query = entityManager.createNamedQuery("CloseClaim.getByCloseClaimKey");
		query.setParameter("claimKey", claimKey);
		
		
		List<CloseClaim> closeClaimList = (List<CloseClaim>)query.getResultList();
		if(closeClaimList != null && ! closeClaimList.isEmpty()){
			return closeClaimList.get(0);
		}
		
		return null;
		
	}
	
	public CloseClaim getAlreadyRodClosed(Long reimbursmentKey){
		
		Query query = entityManager.createNamedQuery("CloseClaim.findByReimbursmentKey");
		query.setParameter("reimbursmentKey", reimbursmentKey);
		query.setParameter("fromCreateRod", 132l);
		query.setParameter("toFinancial", 137l);

		List<CloseClaim> closeClaimList = (List<CloseClaim>)query.getResultList();
		if(closeClaimList != null && ! closeClaimList.isEmpty()){
			return closeClaimList.get(0);
		}
		
		return null;
		
	}
	
	public List<DocAcknowledgement> getDocAckListByClaim(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckByClaim");
		query = query.setParameter("claimkey", claimKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
//			entityManager.refresh(docAckList.get(0));
			return docAckList;
		}
		return null;
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
    public Map<String, String> getAcquiredUserId(String intimationId)
    {
    	Map<String, List<String>> rodUserListMap = new  HashMap<String, List<String>>();
    	Map<String, String> userMap = new HashMap<String, String>();
	
    	/*CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask("claimshead", BPMClientContext.BPMN_PASSWORD);    	
		PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationId);
		payloadType.setIntimation(intimationType);
		
		PagedTaskList tasks = closeClaimTask.getTasks("claimshead", null, payloadType);

		
		List<HumanTask> humanTasks = tasks.getHumanTasks();

		List<Integer> taskNumberList = new ArrayList<Integer>();
		List<String> acquireUserList = null;
		List<String> rodNumberList = null;
		Map<Integer,String> taskListMap = new HashMap<Integer,String>();
		Map<String, String> userMap = new HashMap<String, String>();
		if(null != humanTasks && !humanTasks.isEmpty())
		{
			rodNumberList = new ArrayList<String>();
			for (HumanTask humanTask : humanTasks) {
				if(null != humanTask.getPayload() && null != humanTask.getPayload().getRod() 
						&& null != humanTask.getPayload().getRod().getKey()){
					taskNumberList.add(humanTask.getNumber());
					rodNumberList.add(humanTask.getPayload().getRod().getAckNumber());
					taskListMap.put(humanTask.getNumber(),humanTask.getPayload().getRod().getAckNumber());
				}
			}
			if(null != taskNumberList && !taskNumberList.isEmpty())
			{
<<<<<<< HEAD
				acquireUserList = new ArrayList<String>();
				for(Integer iValue : taskNumberList)
=======
				//Long statusKey = preauth.getStatus().getKey();
				/*if(statusKey.equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || statusKey.equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS))
>>>>>>> FHOwithproduction
				{
					if(null != iValue)
					{
						String aciquireByUserId = SHAUtils.getAciquireByUserId(iValue);
						String rodNo = taskListMap.get(iValue);
						if(null != aciquireByUserId){
						userMap.put(rodNo, aciquireByUserId);
						}
					}
				}
				
			}
			
		}
		rodUserListMap.put("rodNoList", rodNumberList);
		rodUserListMap.put("userList", acquireUserList);*/
		return userMap;
    }
    
    
  
    @SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNo").setParameter(
				"intimationNo", "%"+intimationNo+"%");

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
		private void populateDocumentDetails(ReceiptOfDocumentsDTO rodDTO,
				DocAcknowledgement docAcknowledgment ) {
			if (null != docAcknowledgment) {
			
				docAcknowledgment.setAdditionalRemarks(rodDTO.getDocumentDetails().getAdditionalRemarks());
				/*rodDTO.getDocumentDetails().setAdditionalRemarks(
						docAcknowledgment.getAdditionalRemarks());*/
				docAcknowledgment.setDocumentReceivedDate(rodDTO.getDocumentDetails().getDocumentsReceivedDate());
				/*rodDTO.getDocumentDetails().setDocumentsReceivedDate(docAcknowledgment.getDocumentReceivedDate());*/
				
				rodDTO.getDocumentDetails().setDocumentReceivedFromValue(
						docAcknowledgment.getDocumentReceivedFromId().getValue());
				rodDTO.getDocumentDetails().setModeOfReceiptValue(
						docAcknowledgment.getModeOfReceiptId().getValue());
				rodDTO.getDocumentDetails().setReconsiderationRequestValue("Y");
				
				docAcknowledgment.setHospitalisationFlag(rodDTO.getDocumentDetails().getHospitalizationFlag());
				/*rodDTO.getDocumentDetails().setHospitalizationFlag(
						docAcknowledgment.getHospitalisationFlag());*/
				docAcknowledgment.setPreHospitalisationFlag(rodDTO.getDocumentDetails().getPreHospitalizationFlag());
				/*rodDTO.getDocumentDetails().setPreHospitalizationFlag(
						docAcknowledgment.getPreHospitalisationFlag());*/
				docAcknowledgment.setPostHospitalisationFlag(rodDTO.getDocumentDetails().getPostHospitalizationFlag());
				/*rodDTO.getDocumentDetails().setPostHospitalizationFlag(
						docAcknowledgment.getPostHospitalisationFlag());*/
				docAcknowledgment.setPartialHospitalisationFlag(rodDTO.getDocumentDetails().getPartialHospitalizationFlag());
				/*rodDTO.getDocumentDetails().setPartialHospitalizationFlag(
						docAcknowledgment.getPartialHospitalisationFlag());*/
				docAcknowledgment.setLumpsumAmountFlag(rodDTO.getDocumentDetails().getLumpSumAmountFlag());
				/*rodDTO.getDocumentDetails().setLumpSumAmountFlag(
						docAcknowledgment.getLumpsumAmountFlag());*/
				docAcknowledgment.setHospitalCashFlag(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag());
				/*rodDTO.getDocumentDetails().setAddOnBenefitsHospitalCashFlag(
						docAcknowledgment.getHospitalCashFlag());*/
				docAcknowledgment.setPatientCareFlag(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag());
				/*rodDTO.getDocumentDetails().setAddOnBenefitsPatientCareFlag(
						docAcknowledgment.getPatientCareFlag());*/
				docAcknowledgment.setHospitalizationRepeatFlag(rodDTO.getDocumentDetails().getHospitalizationRepeatFlag());
				/*rodDTO.getDocumentDetails().setHospitalizationRepeatFlag(
						docAcknowledgment.getHospitalizationRepeatFlag());*/
				if((rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
						|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
						|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))*/
							(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
								&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
										|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode())
										|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getPolicyPlan())))
						|| (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
								&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))))
								{
				docAcknowledgment.setOtherBenefitsFlag(rodDTO.getDocumentDetails().getOtherBenefitsFlag());
				if(null != rodDTO.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getOtherBenefitsFlag()) )
				{
				docAcknowledgment.setEmergencyMedicalEvaluation(rodDTO.getDocumentDetails().getEmergencyMedicalEvaluationFlag());
				docAcknowledgment.setCompassionateTravel(rodDTO.getDocumentDetails().getCompassionateTravelFlag());
				docAcknowledgment.setRepatriationOfMortalRemain(rodDTO.getDocumentDetails().getRepatriationOfMortalRemainsFlag());
				docAcknowledgment.setPreferredNetworkHospita(rodDTO.getDocumentDetails().getPreferredNetworkHospitalFlag());
				docAcknowledgment.setSharedAccomodation(rodDTO.getDocumentDetails().getSharedAccomodationFlag());
				}
				else
				{
					docAcknowledgment.setEmergencyMedicalEvaluation(SHAConstants.N_FLAG);
					docAcknowledgment.setCompassionateTravel(SHAConstants.N_FLAG);
					docAcknowledgment.setRepatriationOfMortalRemain(SHAConstants.N_FLAG);
					docAcknowledgment.setPreferredNetworkHospita(SHAConstants.N_FLAG);
					docAcknowledgment.setSharedAccomodation(SHAConstants.N_FLAG);
					
				}
				}
			}
		}

public List<DMSDocumentDetailsDTO> getAcknowledgeDocumentList(Claim claim)
	{
		if(null != claim)
		{
			Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByClaimKey");
			query = query.setParameter("claimKey", claim.getKey());
			List<AcknowledgeDocument> ackDocList = query.getResultList();
			List<DMSDocumentDetailsDTO> dtoList = null;
			BPMClientContext context = new BPMClientContext();
			String dmsAPIUrl = context.getDMSRestApiUrl();
			if(null != ackDocList && !ackDocList.isEmpty())
			{
				dtoList = new ArrayList<DMSDocumentDetailsDTO>();
				entityManager.refresh(ackDocList.get(0));
				for (AcknowledgeDocument acknowledgeDocument : ackDocList) {
				
					DMSDocumentDetailsDTO detailsDTO = new DMSDocumentDetailsDTO();
					if(null != claim.getIntimation())
						detailsDTO.setIntimationNo(claim.getIntimation().getIntimationId());
					detailsDTO.setClaimNo(claim.getClaimId());
					detailsDTO.setDocumentType(SHAConstants.SEARCH_UPLOAD_DOC_TYPE);
					detailsDTO.setFileName(acknowledgeDocument.getFileName());		
					detailsDTO.setDocumentSource(SHAConstants.SEARCH_UPLOAD_DOC_SOURCE);
					detailsDTO.setDmsDocToken(acknowledgeDocument.getDocToken());
					detailsDTO.setGalaxyFileName(acknowledgeDocument.getFileName());
					detailsDTO.setDocumentCreatedDate(acknowledgeDocument.getCreatedDate());
					detailsDTO.setDmsRestApiURL(dmsAPIUrl);
					if (null != acknowledgeDocument.getFileType().getValue()){
							detailsDTO.setFileType(acknowledgeDocument.getFileType().getValue());
							if (SHAConstants.getGrievanceMasterValueList().contains(acknowledgeDocument.getFileType().getValue())) {
								detailsDTO.setDocumentType(acknowledgeDocument.getFileType().getValue());
							}
					}
					dtoList.add(detailsDTO);
					
				}
				return dtoList;
			}
		}
		return null;
	}


    public static HashMap<Long, Long> getStageAndStatusMapForCloseClaim(){
		
  		HashMap<Long, Long> hashMap = new HashMap<Long, Long>();
  		hashMap.put(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_CLOSED);
  		hashMap.put(ReferenceTable.BILL_ENTRY_STAGE_KEY, ReferenceTable.BILL_ENTRY_CLOSED);
  		hashMap.put(ReferenceTable.ZONAL_REVIEW_STAGE, ReferenceTable.ZONAL_REVIEW_CLOSED);
  		hashMap.put(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY, ReferenceTable.CLAIM_REQUEST_CLOSED);
  		hashMap.put(ReferenceTable.INVESTIGATION_STAGE, ReferenceTable.CLAIM_REQUEST_REOPENED);
  		hashMap.put(ReferenceTable.FVR_STAGE_KEY,  ReferenceTable.CLAIM_REQUEST_REOPENED);
  		hashMap.put(ReferenceTable.BILLING_STAGE, ReferenceTable.BILLING_CLOSED);
  		hashMap.put(ReferenceTable.FINANCIAL_STAGE, ReferenceTable.FINANCIAL_CLOSED);
  		hashMap.put(ReferenceTable.CLAIM_APPROVAL_STAGE, ReferenceTable.CLAIM_APPROVAL_CLOSED );
  		
  		return hashMap;
  		
  	}
    
    public HashMap<Long, Long> getStageAndStatusMapForReOpenClaim(){
		
		HashMap<Long, Long> hashMap = new HashMap<Long, Long>();
		hashMap.put(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_REOPENED);
		hashMap.put(ReferenceTable.BILL_ENTRY_STAGE_KEY, ReferenceTable.BILL_ENTRY_REOPENED);
		hashMap.put(ReferenceTable.ZONAL_REVIEW_STAGE, ReferenceTable.ZONAL_REVIEW_REOPENED);
		hashMap.put(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY, ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.INVESTIGATION_STAGE, ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.FVR_STAGE_KEY,  ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.BILLING_STAGE, ReferenceTable.BILLING_REOPENED);
		hashMap.put(ReferenceTable.FINANCIAL_STAGE, ReferenceTable.FINANCIAL_REOPENED);
		hashMap.put(ReferenceTable.CLAIM_APPROVAL_STAGE, ReferenceTable.CLAIM_APPROVAL_STAGE_REOPENED );
		
		return hashMap;
		
	}

    public List<SearchScreenValidationTableDTO> getReimbursementByIntimationKey(String intimationId)
    {
    	Query query = entityManager.createNamedQuery("Reimbursement.findByIntimationNumber");
    	query = query.setParameter("intimationNumber", intimationId);
    	List<Reimbursement> reimbursementList = query.getResultList();
    	List<SearchScreenValidationTableDTO> tableDTOList = null;
    	if(null != reimbursementList && !reimbursementList.isEmpty())
    	{
    		tableDTOList =  new ArrayList<SearchScreenValidationTableDTO>();
    		for (Reimbursement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
				SearchScreenValidationTableDTO tableDTO = new SearchScreenValidationTableDTO();
				tableDTO.setRodNo(reimbursement.getRodNumber());
				if(null != reimbursement.getStage())
					tableDTO.setLastCompletedStage(reimbursement.getStage().getStageName());
				if(null != reimbursement.getStatus())
					tableDTO.setStatus(reimbursement.getStatus().getProcessValue());
					if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE))
						tableDTO.setLastRemarks(reimbursement.getApprovalRemarks());
					if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE))
						tableDTO.setLastRemarks(reimbursement.getMedicalRemarks());
					if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE))
						tableDTO.setLastRemarks(reimbursement.getBillingRemarks());
					if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE))
						tableDTO.setLastRemarks(reimbursement.getClaimApprovalRemarks());
					if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE))
						tableDTO.setLastRemarks(reimbursement.getFinancialApprovalRemarks());
					tableDTOList.add(tableDTO);
				
			}
    		return tableDTOList;
    	}
    	return null;
    }

    public List<SearchScreenValidationTableDTO> getCashlessByIntimationNo(String intimationId)
    {
    	Query query = entityManager.createNamedQuery("Preauth.findByIntimationNumber");
    	query = query.setParameter("intimationNo", intimationId);
    	List<Preauth> cashlessList = query.getResultList();
    	List<SearchScreenValidationTableDTO> tableDTOList = null;
    	if(null != cashlessList && !cashlessList.isEmpty())
    	{
    		tableDTOList =  new ArrayList<SearchScreenValidationTableDTO>();
    		for (Preauth preauth : cashlessList) {
				entityManager.refresh(preauth);
				SearchScreenValidationTableDTO tableDTO = new SearchScreenValidationTableDTO();
				tableDTO.setCashlessKey(preauth.getKey());
				tableDTO.setCashlessNo(preauth.getPreauthId());
				if(null != preauth.getStage())
					tableDTO.setLastCompletedStage(preauth.getStage().getStageName());
				if(null != preauth.getStatus())
					tableDTO.setStatus(preauth.getStatus().getProcessValue());
					
					tableDTOList.add(tableDTO);
				
			}
    		return tableDTOList;
    	}
    	return null;
    }
    

    
	public DocumentDetails getDocumentDetailsByKey(Long docKey) {
		
			Query query = entityManager.createNamedQuery("DocumentDetails.findByKey");
			query = query.setParameter("key", docKey);
			List<DocumentDetails> docDetailsList = query.getResultList();
			if(null != docDetailsList && !docDetailsList.isEmpty()) {
				entityManager.refresh(docDetailsList.get(0));
				return docDetailsList.get(0);
			}
		return null;
	}
	
/*	@SuppressWarnings("unchecked")
	public List<RODBillDetails> getBilldetailsByDocumentSummayKeyAndCategoryID(
			Long summaryKey,Long billCategoryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByDocSummaryAndBillClassification");
		query = query.setParameter("summaryKey", summaryKey);
		query.setParameter("billClassification", billCategoryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		
		for (RODBillDetails rodBillDetails : billDetails) {
			entityManager.refresh(rodBillDetails);
		}

<<<<<<< HEAD
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
    		
    		if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS) ||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_REOPENED) && reimbursement.getApprovalRemarks() != null
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CLOSED) && reimbursement.getApprovalRemarks() != null){
    			
    			
    			claimedAmount = reimbursement.getApprovedAmount();
    			
    			if(claimedAmount == null){
    				claimedAmount = 0d;
    			}
    			
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
    			
			}else{
				claimedAmount = getClaimedAmount(reimbursement,claim);                              //will be executed if staus is refer to specialist, escalate, investigation from MA stage
			}
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
 
    		claimedAmount = reimbursement.getBillingApprovedAmount();
    		
    		if(claimedAmount == null && reimbursement.getApprovedAmount() != null){
    			claimedAmount = reimbursement.getApprovedAmount();
    		}
    		
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
 
    		claimedAmount = reimbursement.getFinancialApprovedAmount();
    		if(claimedAmount == null && reimbursement.getBillingApprovedAmount() != null){
    			claimedAmount = reimbursement.getBillingApprovedAmount();
    		}
    	}
    	
    	return claimedAmount;
=======
		return billDetails;

	}*/
	
	
	@SuppressWarnings("unchecked")
	public List<RODBillDetails> getBilldetailsByDocumentSummayKeyAndCategoryID(
			Long summaryKey,Long billCategoryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByDocSummaryAndBillClassification");
		query = query.setParameter("summaryKey", summaryKey);
		query.setParameter("billClassification", billCategoryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		
		for (RODBillDetails rodBillDetails : billDetails) {
			entityManager.refresh(rodBillDetails);
		}

		return billDetails;

	}
	public List<Long> getListOfBillCategory(Long rodKey,Long categoryKey)
	{
		List<RODDocumentSummary> rodDocSummaryList = getRODSummaryDetailsByReimbursementKey(rodKey);
		List<Long> categoryList = null;
		if(null != rodDocSummaryList && !rodDocSummaryList.isEmpty())
		{
			categoryList = new ArrayList<Long>();
			for (RODDocumentSummary rodDocumentSummary : rodDocSummaryList) {
				List<RODBillDetails> rodBillDetailsList = getBilldetailsByDocumentSummayKeyAndCategoryID(rodDocumentSummary.getKey(),categoryKey);
				if(null != rodBillDetailsList && !rodBillDetailsList.isEmpty())
				{
					for (RODBillDetails rodBillDetails : rodBillDetailsList) {
						if(null != rodBillDetails.getBillClassification())
							categoryList.add(rodBillDetails.getBillClassification().getKey());
					}
					
				}
						
			}
		}
		return categoryList;
		
	}
	
	
	public List<ReimbursementBenefits> getReimbursementBenefits(Long rodKey, String benefitsFlag)
	{
		Query query = entityManager.createNamedQuery("ReimbursementBenefits.findByRodKeyAndBenefitsFlag");
		query = query.setParameter("rodKey", rodKey);
		query = query.setParameter("benefitsFlag", benefitsFlag);
		List<ReimbursementBenefits> reimbBenefitsList = query.getResultList();
		if(null != reimbBenefitsList && !reimbBenefitsList.isEmpty())
		{
			for (ReimbursementBenefits reimbursementBenefits : reimbBenefitsList) {
				entityManager.refresh(reimbursementBenefits);

			}
		}
		return reimbBenefitsList;
	}
	
	
	public List<ReimbursementBenefits> getReimbursementBenefits(Long rodKey)
	{
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKey");
		query = query.setParameter("rodKey", rodKey);
		
		List<ReimbursementBenefits> reimbBenefits = query.getResultList();
		
		if(reimbBenefits != null && !reimbBenefits.isEmpty()) {
			for (ReimbursementBenefits reimbursementBenefits : reimbBenefits) {
				entityManager.refresh(reimbursementBenefits);
			}
		}

		return reimbBenefits;
	}
	
	
	/**
	 * Multiple room rent scenario to be handled. 
	 * For given billclassificationkey , bill type key if we
	 * have 3 entries in bill remarks table, then we would get
	 * list of viewBillRemarks object. This needs to be handled.
	 * */
	public ViewBillRemarks getViewBillRemarksForROD(Long rodKey, Long billClassificationKey, Long billTypeKey)
	{
		Query query = entityManager.createNamedQuery("ViewBillRemarks.findByRodBillTypeBillClassification");
		query = query.setParameter("rodKey", rodKey);
		query.setParameter("billClassificationKey", billClassificationKey);
		query.setParameter("billTypeKey",billTypeKey);
		
		List<ViewBillRemarks> viewBillRemarksList = query.getResultList();
		if(null != viewBillRemarksList && !viewBillRemarksList.isEmpty())
		{
			entityManager.refresh(viewBillRemarksList.get(0));
			return viewBillRemarksList.get(0);
		}
		return null;
	}
	
	
    private void updateBenefitsRecord(String benefitsFlag,Reimbursement reimbursement)
    {
    
		if(null != reimbursement)
		{
			List<ReimbursementBenefits> reimbBenefitsList = getReimbursementBenefits(reimbursement.getKey());
			if(null != reimbBenefitsList && !reimbBenefitsList.isEmpty())
			{
				
				for (ReimbursementBenefits reimbursementBenefits : reimbBenefitsList) {
					if((benefitsFlag).equalsIgnoreCase(reimbursementBenefits.getBenefitsFlag()))
					{
						reimbursementBenefits.setDeletedFlag("Y");
						entityManager.merge(reimbursementBenefits);
						entityManager.flush();
					}
					
				}
				
			}
		}
    }
    
    public List<PatientCareDTO> getPatientCareDetails(Long benefitsKey)
	{
		List<PatientCareDTO> patientCareList = null;
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query = query.setParameter("benefitsKey", benefitsKey);
		List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = query.getResultList();
		if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
		{
			patientCareList  = ProcessClaimRequestBenefitsMapper.getInstance().getPatientCareDetails(reimbBenefitsDetailsList);
		}
		return patientCareList;
		
	}
    
    public List<ReimbursementBenefitsDetails> geReimbursmentBenefitsDetails(Long benefitsKey)
   	{
   		List<PatientCareDTO> patientCareList = null;
   		Query query = entityManager
   				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
   		query = query.setParameter("benefitsKey", benefitsKey);
   		List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = query.getResultList();
   		if(null != reimbBenefitsDetailsList && !reimbBenefitsDetailsList.isEmpty())
   		{
   			for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
				entityManager.refresh(reimbursementBenefitsDetails);
			}
   		}
   		return reimbBenefitsDetailsList;
   		
   	}
    
    public List<DocumentDetails> getDocumentDetailsByRodNumber(String rodNumber)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByRodNo");
		query = query.setParameter("reimbursementNumber", rodNumber);
		List<DocumentDetails> docDetailsList = query.getResultList();
		if(null != docDetailsList && !docDetailsList.isEmpty())
		{
			for (DocumentDetails documentDetails : docDetailsList) {
				entityManager.refresh(documentDetails);
			}
			return docDetailsList;
		}
		return null;

	}
    
    public List<DocumentDetails> getDocumentDetailsForPreviousROD(Long rodKey)
   	{
   		Query query = entityManager.createNamedQuery("DocumentDetails.findByRodKey");
   		query = query.setParameter("rodKey", rodKey);
   		List<DocumentDetails> docDetailsList = query.getResultList();
   		if(null != docDetailsList && !docDetailsList.isEmpty())
   		{
   			for (DocumentDetails documentDetails : docDetailsList) {
   				entityManager.refresh(documentDetails);
   			}
   			return docDetailsList;
   		}
   		return null;

   	}
    
    public void deletedPreviousDocument(Long rodKey){
    	List<DocumentDetails> documentDetailsForPreviousROD = getDocumentDetailsForPreviousROD(rodKey);
    	if(documentDetailsForPreviousROD != null){
	    	for (DocumentDetails documentDetails : documentDetailsForPreviousROD) {
				documentDetails.setDeletedFlag("Y");
				documentDetails.setDocumentDeleteRemarks("Preious Document Deleted for Reconsideration");
				entityManager.merge(documentDetails);
				entityManager.flush();
			}
    	}
    }

    private Boolean shouldSkipZMR(Claim claim) {
    	Boolean shouldSkipZMR = false;
		Query query = entityManager.createNamedQuery("CPUStageMapping.findByCPUCode");
		query = query.setParameter("cpuCode", claim.getIntimation().getCpuCode().getCpuCode());
		List<CPUStageMapping> mappingList = query.getResultList();
		if(null != mappingList && !mappingList.isEmpty())
		{
			for (CPUStageMapping stageMapping : mappingList) {
				entityManager.refresh(stageMapping);
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && (stageMapping.getCashlessFlag() != null && stageMapping.getCashlessFlag().equalsIgnoreCase("Y"))) {
					shouldSkipZMR = true;
					break;
				} else if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && (stageMapping.getReimbursementFlag() != null && stageMapping.getReimbursementFlag().equalsIgnoreCase("Y"))){
					shouldSkipZMR = true;
					break;
				}
			}
		}
		return shouldSkipZMR;

	}

    public List<PreviousAccountDetailsDTO> getPaymentDetailsForPreviousClaim(String claimNo,String docReceivedFrom)
    {
    	List<PreviousAccountDetailsDTO> previousPaymentDTOList = null;
		CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
    	List<ClaimPayment> claimPayment = getPaymentDetailsByClaimNumberForView(claimNo);
    	List<PreviousAccountDetailsDTO> previousAccntDetailsList = createRODMapper.getPreviousAccountDetails(claimPayment);
    	if(null != previousAccntDetailsList && !previousAccntDetailsList.isEmpty())
    	{
    		previousPaymentDTOList = new ArrayList<PreviousAccountDetailsDTO>();
    		//Account type to be populated. Since column is not finalized, this is kept on hold
    		for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousAccntDetailsList) {
    			if(null != previousAccountDetailsDTO.getDocReceivedFrom() && (previousAccountDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docReceivedFrom)))
    			{
	    			previousAccountDetailsDTO.setSource("Claim");
					BankMaster masBank = getBankDetails(previousAccountDetailsDTO.getIfsccode());
					if(null != masBank)
					{
						previousAccountDetailsDTO.setBankName(masBank.getBankName());
						previousAccountDetailsDTO.setBankBranch(masBank.getBranchName());
						previousAccountDetailsDTO.setBankCity(masBank.getCity());
					}
	    			previousPaymentDTOList.add(previousAccountDetailsDTO);

    			}
			}
    	}
    	return previousPaymentDTOList;
    	
    }
   
    private List<ClaimPayment> getPaymentDetailsByClaimNumberForView(String claimNumber)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(claimNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByClaimNumberAndStatus");
		findByPaymentKey = findByPaymentKey.setParameter("claimNumber", claimNumber);
		findByPaymentKey = findByPaymentKey.setParameter("statusId", ReferenceTable.PAYMENT_SETTLED);
	//	findByPaymentKey = findByPaymentKey.setParameter("statusId", 160l);
		findByPaymentKey = findByPaymentKey.setParameter("paymentType", ReferenceTable.BANK_TRANSFER);
	//	findByPaymentKey = findByPaymentKey.setParameter("paymentType", 160l);
		
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail : paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}

    
    public List<AddOnCoversTableDTO> getAddOnCoversValue(Long docAckKey)
    {
    	
    	List<AddOnCoversTableDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByAckKey");
    	query = query.setParameter("ackDocKey", docAckKey);
    	List<PAAdditionalCovers> paAddCoversList = query.getResultList();
    	if(null != paAddCoversList && !paAddCoversList.isEmpty())
    	{
    		addOnCoversList = new ArrayList<AddOnCoversTableDTO>();
    		for (PAAdditionalCovers paAdditionalCovers : paAddCoversList) {
				AddOnCoversTableDTO addOnCoversDTO = new AddOnCoversTableDTO();
				SelectValue addOnCoversValue = new SelectValue();
				addOnCoversValue.setId(paAdditionalCovers.getCoverId());
				addOnCoversDTO.setCovers(addOnCoversValue);
				addOnCoversDTO.setClaimedAmount(paAdditionalCovers.getClaimedAmount());
				addOnCoversDTO.setCoverKey(paAdditionalCovers.getKey());
				addOnCoversDTO.setUnNamedKey(paAdditionalCovers.getUnNamedKey());
				addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }
    

    public List<AddOnCoversTableDTO> getAddOnCoversValueBasedOnROD(Long rodKey)
    {
    	
    	List<AddOnCoversTableDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAAdditionalCovers> paAddCoversList = query.getResultList();
    	if(null != paAddCoversList && !paAddCoversList.isEmpty())
    	{
    		addOnCoversList = new ArrayList<AddOnCoversTableDTO>();
    		for (PAAdditionalCovers paAdditionalCovers : paAddCoversList) {
				AddOnCoversTableDTO addOnCoversDTO = new AddOnCoversTableDTO();
				SelectValue addOnCoversValue = new SelectValue();
				addOnCoversValue.setId(paAdditionalCovers.getCoverId());
				addOnCoversDTO.setCovers(addOnCoversValue);
				addOnCoversDTO.setClaimedAmount(paAdditionalCovers.getClaimedAmount());
				addOnCoversDTO.setCoverKey(paAdditionalCovers.getKey());
				addOnCoversDTO.setClaimKey(paAdditionalCovers.getClaimKey());
				addOnCoversDTO.setRemarks(paAdditionalCovers.getRemarks());
				addOnCoversDTO.setUnNamedKey(paAdditionalCovers.getUnNamedKey());
				addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }
    
    public List<AddOnCoversTableDTO> getOpitionalCoversValueBasedOnROD(Long rodKey)
    {
    	
    	List<AddOnCoversTableDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);

    	List<PAOptionalCover> paAddCoversList = query.getResultList();
    	if(null != paAddCoversList && !paAddCoversList.isEmpty())
    	{
    		addOnCoversList = new ArrayList<AddOnCoversTableDTO>();
    		for (PAOptionalCover paAdditionalCovers : paAddCoversList) {
				AddOnCoversTableDTO addOnCoversDTO = new AddOnCoversTableDTO();
				SelectValue addOnCoversValue = new SelectValue();
				addOnCoversValue.setId(paAdditionalCovers.getCoverId());
				addOnCoversDTO.setOptionalCover(addOnCoversValue);
				addOnCoversDTO.setClaimedAmount(paAdditionalCovers.getClaimedAmount());
				addOnCoversDTO.setCoverKey(paAdditionalCovers.getKey());
				addOnCoversDTO.setClaimKey(paAdditionalCovers.getClaimKey());
				addOnCoversDTO.setRemarks(paAdditionalCovers.getRemarks());
				addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }
    
    
    
    public List<PABenefitsDTO> getPABenefitsBasedOnRodKey(Long rodKey)
    {
    	
    	List<PABenefitsDTO> pabenefitsList = null;
    	
    	
    	Query query = entityManager.createNamedQuery("PABenefitsCovers.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);

    	List<PABenefitsCovers> paBenefitsList = query.getResultList();
    	if(null != paBenefitsList && !paBenefitsList.isEmpty())
    	{
    		pabenefitsList = new ArrayList<PABenefitsDTO>();
    		for (PABenefitsCovers paBenefitsCover : paBenefitsList) {
    			PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
    			SelectValue benefitsCoverValue = new SelectValue();
    			benefitsCoverValue.setId(paBenefitsCover.getCoverId().getSubCoverKey());
    			benefitsCoverValue.setValue(paBenefitsCover.getCoverId().getCoverDescription());
				paBenefitsDTO.setBenefitCoverValue(benefitsCoverValue);
				paBenefitsDTO.setKey(paBenefitsCover.getKey());
				if(null != paBenefitsCover.getWeeksDuration() && !("").equalsIgnoreCase(paBenefitsCover.getWeeksDuration()))
					paBenefitsDTO.setNoOfWeeks(Long.valueOf(paBenefitsCover.getWeeksDuration()));
				//Percentage column is missing in benefits table.
				//paBenefitsDTO.setNoOfWeeks(null != paBenefitsCover.getWeeksDuration() ? Long.valueOf(paBenefitsCover.getWeeksDuration()): 0l);
				pabenefitsList.add(paBenefitsDTO);
				//paBenefitsDTO.setPercentage(paBenefitsCover.get);
			}
    		
    	}
    	return pabenefitsList;
    }
    
    
    public List<PABenefitsDTO> getPABenefitsDataBasedOnRodKey(Long rodKey,Long insuredKey,PreauthDTO bean)
    {
    	
    	List<PABenefitsDTO> pabenefitsList = null;
    	Query query = entityManager.createNamedQuery("PABenefitsCovers.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);

    	List<PABenefitsCovers> paBenefitsList = query.getResultList();
    	if(null != paBenefitsList && !paBenefitsList.isEmpty())
    	{
    		pabenefitsList = new ArrayList<PABenefitsDTO>();
    		for (PABenefitsCovers paBenefitsCover : paBenefitsList) {
    			PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
    			SelectValue benefitsCoverValue = new SelectValue();
    			benefitsCoverValue.setId(paBenefitsCover.getCoverId().getSubCoverKey());
    			benefitsCoverValue.setValue(paBenefitsCover.getCoverId().getCoverDescription());
				paBenefitsDTO.setBenefitCoverValue(benefitsCoverValue);
				paBenefitsDTO.setKey(paBenefitsCover.getKey());
				if(null != paBenefitsCover.getWeeksDuration() && !("").equalsIgnoreCase(paBenefitsCover.getWeeksDuration()))
					paBenefitsDTO.setNoOfWeeks(Long.valueOf(paBenefitsCover.getWeeksDuration()));
				//Percentage column is missing in benefits table.
				//paBenefitsDTO.setNoOfWeeks(null != paBenefitsCover.getWeeksDuration() ? Long.valueOf(paBenefitsCover.getWeeksDuration()): 0l);
				pabenefitsList.add(paBenefitsDTO);
				//paBenefitsDTO.setPercentage(paBenefitsCover.get);
			}
    		
    		DBCalculationService dbCalculationService = new DBCalculationService();
    		if(null != bean.getPreauthDataExtractionDetails().getOnloadBenefitId())
    		{
    			List<PABenefitsDTO> paBenefitsProcedureDTOList = null;
    			if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
						null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
						!(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
    				paBenefitsProcedureDTOList = dbCalculationService.getBenefitCoverValues(insuredKey  , bean.getPreauthDataExtractionDetails().getOnloadBenefitId());
	        	}else{
	        		paBenefitsProcedureDTOList = dbCalculationService.getGPABenefitCoverValues(insuredKey, bean.getPreauthDataExtractionDetails().getOnloadBenefitId());
	        	}
	    		if(null != paBenefitsProcedureDTOList && !paBenefitsProcedureDTOList.isEmpty())
	    		{
	    			for (PABenefitsDTO paBenefitsDTO : pabenefitsList) {
	    				
	    				for (PABenefitsDTO paBenefitsDTOObj : paBenefitsProcedureDTOList) {
	    					if(null != paBenefitsDTO.getBenefitCoverValue()  && paBenefitsDTO.getBenefitCoverValue().getValue().equalsIgnoreCase(paBenefitsDTOObj.getBenefitCover()))
	    					{
	    						paBenefitsDTO.setSumInsured(paBenefitsDTOObj.getSumInsured());
	    						paBenefitsDTO.setPercentage(paBenefitsDTOObj.getPercentage());
	    						//if(null != paBenefitsDTOObj.getNoOfWeeks())
	    							//paBenefitsDTO.setNoOfWeeks(paBenefitsDTOObj.getNoOfWeeks());
	    							//paBenefitsDTO.setNoOfWeeks(paBenefitsDTOObj.get);
	    						if(null != paBenefitsDTOObj.getEligibleAmountPerWeek())
	    							paBenefitsDTO.setEligibleAmountPerWeek(paBenefitsDTOObj.getEligibleAmountPerWeek());
	    						paBenefitsDTO.setEligibleAmount(paBenefitsDTOObj.getEligibleAmount());
	    					    Double noOfWeeks = null != paBenefitsDTO.getNoOfWeeks() ? paBenefitsDTO.getNoOfWeeks() : 0d;
	    						Double totalEligibleAmnt = paBenefitsDTO.getEligibleAmount() * noOfWeeks;
	    						paBenefitsDTO.setTotalEligibleAmount(totalEligibleAmnt);
	    						
	    						break;
	    					}
	    				}
	    			}
	    			
	    		}
    		}
    	}

    	return pabenefitsList;
    }
    
 /*   public List<AddOnCoversTableDTO> getOpitionalCoversValueBasedOnROD(Long rodKey)
    {
    	
    	List<AddOnCoversTableDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
    	query = query.setParameter("rodKey", rodKey);
    	List<PAAdditionalCovers> paAddCoversList = query.getResultList();
    	if(null != paAddCoversList && !paAddCoversList.isEmpty())
=======
    	List<PAOptionalCover> paOptionalCoverList = query.getResultList();
    	if(null != paOptionalCoverList && !paOptionalCoverList.isEmpty())
>>>>>>> 21c97585da45997228539d2e365ed16074bcb99c
    	{
    		addOnCoversList = new ArrayList<AddOnCoversTableDTO>();
    		for (PAOptionalCover paAdditionalCovers : paOptionalCoverList) {
				AddOnCoversTableDTO addOnCoversDTO = new AddOnCoversTableDTO();
				SelectValue addOnCoversValue = new SelectValue();
				addOnCoversValue.setId(paAdditionalCovers.getCoverId());
				addOnCoversDTO.setCovers(addOnCoversValue);
				addOnCoversDTO.setClaimedAmount(paAdditionalCovers.getClaimedAmount());
				addOnCoversDTO.setCoverKey(paAdditionalCovers.getKey()); 
				addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }*/
    
    public List<AddOnCoversTableDTO> getOptionalCoversValue(Long docAckKey)
    {
    	
    	List<AddOnCoversTableDTO> optionalCoversList = null;
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByAckKey");
    	query = query.setParameter("ackDocKey", docAckKey);
    	List<PAOptionalCover> paOptionalCoversList = query.getResultList();
    	if(null != paOptionalCoversList && !paOptionalCoversList.isEmpty())
    	{
    		optionalCoversList = new ArrayList<AddOnCoversTableDTO>();
    		for (PAOptionalCover paOptionalCovers : paOptionalCoversList) {
				AddOnCoversTableDTO addOnCoversDTO = new AddOnCoversTableDTO();
				SelectValue addOnCoversValue = new SelectValue();
				addOnCoversValue.setId(paOptionalCovers.getCoverId());
				addOnCoversDTO.setOptionalCover(addOnCoversValue);
				addOnCoversDTO.setClaimedAmount(paOptionalCovers.getClaimedAmount());
				addOnCoversDTO.setCoverKey(paOptionalCovers.getKey());
				optionalCoversList.add(addOnCoversDTO);
			}
    	}
    	return optionalCoversList;
    }
    


    
    public List<PAcoverTableViewDTO> getOptionalCoversValueForView(Long docAckKey)
    {
    	
    	List<PAcoverTableViewDTO> optionalCoversList = null;
    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByAckKey");
    	query = query.setParameter("ackDocKey", docAckKey);
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
				optionalCoversList.add(optionalCoversDTO);
				
			}
    	}
    	return optionalCoversList;
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
    
    public List<PAcoverTableViewDTO> getAdditionalCoversValueForView(Long docAckKey)
    {
    	List<PAcoverTableViewDTO> addOnCoversList = null;
    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByAckKey");
    	query = query.setParameter("ackDocKey", docAckKey);
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
    			addOnCoversList.add(addOnCoversDTO);
			}
    	}
    	return addOnCoversList;
    }
    
  
	public Boolean isLumpsumExist(Long claimKey,Long reimbKey)
	{
		Boolean isLumpsumexist = false;
		if(null != reimbKey)
		{
			Reimbursement currentReimbursement = getReimbursementObjectByKey(reimbKey);
			if(null != currentReimbursement)
			{
				DocAcknowledgement docAcknowLedgement = currentReimbursement.getDocAcknowLedgement();
				Claim claim = currentReimbursement.getClaim();
				if(null != claim)
				{
					Query query = entityManager.createNamedQuery("Reimbursement.findLatestNonCanceledRODByClaimKey");
					query = query.setParameter("claimKey", claimKey);
					query = query.setParameter("statusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
					List<Reimbursement> reimbursementList = query.getResultList();
					if(null != reimbursementList && !reimbursementList.isEmpty())  {
						for (Reimbursement reimbursement : reimbursementList) {
							entityManager.refresh(reimbursement);
							if(currentReimbursement.getKey() != reimbursement.getKey()) {
								if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
								{
									isLumpsumexist = true;
									break;
								}
						     }
						}
					} 
				}
			}
		}
		return isLumpsumexist;
	}

	
	public Boolean isLumpsumExists(Long claimKey,Long reimbKey,String classificationType)
	{
		Boolean isLumpsumexist = false;
		if(null != reimbKey)
		{
			Reimbursement currentReimbursement = getReimbursementObjectByKey(reimbKey);
			if(null != currentReimbursement)
			{
				DocAcknowledgement docAcknowledgement = currentReimbursement.getDocAcknowLedgement();
				if(null != docAcknowledgement)
				{
					Intimation intimation = docAcknowledgement.getClaim().getIntimation();
					if(null != intimation)
					{
						Long insuredId = intimation.getInsured().getKey();
						List<Long> previousClaimsList = getPreviousClaims(insuredId);
						if(null != previousClaimsList && !previousClaimsList.isEmpty())
						{
							for (Long key : previousClaimsList) {
								List<Reimbursement> reimbList = getReimbursementByClaimKeyForCriticare(key);
								if(null != reimbList && !reimbList.isEmpty())
								{
									/**
									 * If lumpsum is selected , then rule says, check
									 * whether any earlier rod is existing with any classification. If so
									 * restrict from creating a lumpsum rod. 
									 * 
									 * If any other classification is selected, then check
									 * whether lumpsum is existing for any earlier rod.
									 * These two validations happen in below loop.
									 * */
										for (Reimbursement reimbursement : reimbList) {
											if(!currentReimbursement.getRodNumber().equalsIgnoreCase(reimbursement.getRodNumber())) {
												/**
												 * If classification other than lumpsum is selected,
												 * then rule says, check if any earlier rod is existing with
												 * lumpsum.
												 * */
												if(!(SHAConstants.LUMPSUMAMOUNT.equalsIgnoreCase(classificationType)))
												{
													if(reimbursement.getClaim().getKey().equals(claimKey))
													{
														if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
														{
															isLumpsumexist = true;
															break;
														}
													}
												}
												/**
												 * If lumpsum is selected, then system should check if
												 * any earlier rod is existing.
												 * 
												 * */
												else
												{
													if(reimbursement.getClaim().getKey().equals(claimKey))
													{
														if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalCashFlag()))
														{
															isLumpsumexist = true;
															break;
														}
														else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPatientCareFlag()))
														{
															isLumpsumexist = true;
															break;
														}
													}
													
													/**
													 * The below condition is added to check if any lumpsum rod is
													 * existing for previous claim also. Above if will work for current claim
													 * validation and below else if will work for previous claim lumpsum rod.
													 * **/
													
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
													{
														isLumpsumexist = true;
														break;
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
		}
		return isLumpsumexist;
	}
	
	
	public Boolean isLumpSumExistsForClaim(Long claimKey,Long intimationKey,String classificationType)
	{
		Boolean isLumpsumexist = false;

				if(null != intimationKey)
				{
					Intimation intimation = getIntimationByKey(intimationKey);
					if(null != intimation)
					{
						Long insuredId = intimation.getInsured().getKey();
						List<Long> previousClaimsList = getPreviousClaims(insuredId);
						if(null != previousClaimsList && !previousClaimsList.isEmpty())
						{
							for (Long key : previousClaimsList) {
								List<Reimbursement> reimbList = getReimbursementByClaimKeyForCriticare(key);
								if(null != reimbList && !reimbList.isEmpty())
								{
								
										for (Reimbursement reimbursement : reimbList) {
											/**
											 * If classification other than lumpsum is selected,
											 * then rule says, check if any earlier rod is existing with
											 * lumpsum.
											 * */
											if(!(SHAConstants.LUMPSUMAMOUNT.equalsIgnoreCase(classificationType)))
											{
												/**
												 * Within the claim , if once lumpssum is selected, other 
												 * classification should not be selected. But if claims are different
												 * then any claissification can be selected, even though there is a 
												 * lumpsum rod existing for previous claims.
												 * */
												if(reimbursement.getClaim().getKey().equals(claimKey))
												{
													if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
													{
														isLumpsumexist = true;
														break;
													}
												}
											}
											/**
											 * If lumpsum is selected, then system should check if
											 * any earlier rod is existing.
											 * 
											 * */
											else
											{
												if(reimbursement.getClaim().getKey().equals(claimKey))
												{
													if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalCashFlag()))
													{
														isLumpsumexist = true;
														break;
													}
													else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPatientCareFlag()))
													{
														isLumpsumexist = true;
														break;
													}
												}
												/**
												 * The below condition is added to check if any lumpsum rod is
												 * existing for previous claim also. Above if will work for current claim
												 * validation and below else if will work for previous claim lumpsum rod.
												 * **/
												else if(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))
												{
													isLumpsumexist = true;
													break;
												}
											}
									     }		
									
								}
							}
						}
						
					}
				}
		
		return isLumpsumexist;
	}

/*	public List<Long> getPreviousClaims(Long insuredKey){
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY WHERE "
//				+ "B.STATUS_ID NOT IN("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") AND"
//				+ "A.INSURED_KEY  = "+insuredKey;
		
//		String query = "select count(1) from ims_cls_claim where status_id not in ("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") and insured_key = "+insuredKey;
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
//				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
//				+ "WHERE B.STATUS_ID NOT IN(7) AND A.INSURED_KEY  = "+insuredKey;
		
		String query = "SELECT  CLAIM_KEY FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
				+ "WHERE B.STATUS_ID NOT IN(7) AND A.POLICY_KEY  = C.POLICY_KEY AND A.INSURED_KEY = "+ insuredKey;
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		
<<<<<<< HEAD
		List<Object> objList = nativeQuery.getResultList();
		List<Long> claimKeyList = new ArrayList<Long>();
		
		if(null != objList && !objList.isEmpty())
		{
<<<<<<< HEAD
			sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(reimbursement.getClaim().getIntimation().getInsured().getInsuredId()),
					policyKey);
		}
		Double balanceSI = 0d;
		if(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
			balanceSI = dbCalculationService.getBalanceSIForGMC(policyKey , reimbursement.getClaim().getIntimation().getInsured().getKey() , reimbursement.getClaim().getKey());
		}else{
			Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey , reimbursement.getClaim().getIntimation().getInsured().getKey() , reimbursement.getClaim().getKey(),sumInsured,reimbursement.getClaim().getIntimation().getKey());
			balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
		}
		
		return balanceSI;
	
	}
	
	public Double getClaimedAmountForReimbursement(Reimbursement reimbursement) {
		try {
			Double claimedAmount = 0.0;

			DocAcknowledgement docAcknowledgment = reimbursement.getDocAcknowLedgement();
			
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", reimbursement.getKey());
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
		
			Double hospitalizationClaimedAmount = null != docAcknowledgment.getHospitalizationClaimedAmount() ? docAcknowledgment.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgment.getPostHospitalizationClaimedAmount() ? docAcknowledgment.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgment.getPreHospitalizationClaimedAmount() ? docAcknowledgment.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}
    
    public List<PedValidation> getDiagnosis(Long transactionKey) {	
 		

  		List<PedValidation> resultList = new ArrayList<PedValidation>();
  		
  		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
  		query.setParameter("transactionKey", transactionKey);
  		
  		resultList = (List<PedValidation>)query.getResultList();
  	    
  		return resultList;

  	}
    
	@SuppressWarnings("unchecked")
	public List<Procedure> getProcedure(Long transactionKey){
		
			Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
			query.setParameter("transactionKey", transactionKey);
=======
//			for (Iterator it = objList.iterator(); it.hasNext();)
//			{
//				 Object[] myResult = (Object[]) it.next();
//				 Long count = (Long) myResult[0];
//				 return count;
//			}
			for(int i = 0 ; i < objList.size() ; i++)
			{
				BigDecimal count = (BigDecimal)objList.get(i);
				if(null != count)
					claimKeyList.add(count.longValue());
			}
			for (Object object : objList) {
>>>>>>> mayrelease_17_05
			
				
			}
			
			//return count.longValue();
		}

		return null;
    }*/

    
    public Panel getPaymentLetterFromDMS(String docToken)
    {
    	
    	 Panel panel  = null;
    	 final String imageUrl = SHAFileUtils.viewFileByToken(docToken);
			if(null != imageUrl)
			{
				 Embedded e = new Embedded();
			     e.setSizeFull();
			     e.setType(Embedded.TYPE_BROWSER);
		         StreamResource.StreamSource source = new StreamResource.StreamSource() {
                        public InputStream getStream() {
                           
                        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        	InputStream is = null;
                        	URL u = null;
                        	URLConnection urlConnection = null;
                        	try {
//                        		u =  new URL(imageUrl);
//                        	  is = u.openStream();
                        		u =  new URL(imageUrl);
            					urlConnection =  u.openConnection();
            					is = urlConnection.getInputStream();
                        	  
//                        	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
            					byte[]  byteChunk = null;
        						if(urlConnection.getContentLength() > 25000){
        							byteChunk = new byte[25000];
        						} else {
        							byteChunk = new byte[urlConnection.getContentLength()];
        						}
                        	  int n;

                        	  while ( (n = is.read(byteChunk)) > 0 ) {
                        	    baos.write(byteChunk, 0, n);
                        	  }
                        	  byteChunk = null;
                        	}
                        	catch (IOException e) {
                        	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                        	  e.printStackTrace ();
                        	  // Perform any other exception handling that's appropriate.
                        	}
                        	finally {
                        	  if (is != null) {
                        		  try
                        		  {
                        			  is.close();
                        		  }
                        		  catch(Exception e)
                        		  {
                        			  e.printStackTrace();
                        		  }
                        		  }
                        	  
                        	  if (null != urlConnection) {
    		               		  try
    		               		  {
    		               			urlConnection.getInputStream().close();
    		               		  }
    		               		  catch(Exception e)
    		               		  {
    		               			  e.printStackTrace();
    		               		  }
    		                   	}
                        	}
                        	return new ByteArrayInputStream(baos.toByteArray());
                        }
                };
                StreamResource r = new StreamResource(source, "PaymentLetterToInsured");
                r.setMIMEType("application/pdf");
                r.setFilename("PaymentLetterToInsured");
                r.setStreamSource(source);
                r.setFilename("PaymentLetterToInsured");
                e.setSizeFull();
                e.setSource(r);
                panel = new Panel();	
        		panel.setHeight("100%");
        		panel.setContent(e);
				//
                
                //getUI().getPage().open(r, "_blank", false);

		}
			return panel;
    }
			
    
    public Double getTotalClaimedAmt(Long reimbKey, Long classificationId)
    {
    	Double totalAmt = 0d;
    	if(null != reimbKey)
    	{
	    	List<RODDocumentSummary> rodDocSummaryList = getRODSummaryDetailsByReimbursementKey(reimbKey);
	    	if(null != rodDocSummaryList && !rodDocSummaryList.isEmpty())
	    	{
	    		for (RODDocumentSummary rodDocumentSummary : rodDocSummaryList) {
					 
	    			List<RODBillDetails> rodBillDetails = getBilldetailsByDocumentSummayKey(rodDocumentSummary.getKey());
	    			if(null != rodBillDetails && !rodBillDetails.isEmpty())
	    			{
	    				for (RODBillDetails rodBillDetails2 : rodBillDetails) {
	    					if(null != rodBillDetails2.getBillClassification() && null != classificationId)
	    					{
	    						if(classificationId.equals(rodBillDetails2.getBillClassification().getKey()))
	    						{
	    							if(rodBillDetails2.getClaimedAmountBills() != null){
	    								totalAmt += rodBillDetails2.getClaimedAmountBills();	
	    							}
	    							
	    						}
	    					}
						}
	    			}
				}
	    	}
    	}
    	return totalAmt;
    }
	
	public List<Long> getPreviousClaims(Long insuredKey){
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY WHERE "
//				+ "B.STATUS_ID NOT IN("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") AND"
//				+ "A.INSURED_KEY  = "+insuredKey;
		
//		String query = "select count(1) from ims_cls_claim where status_id not in ("+ReferenceTable.INTIMATION_REGISTERED_STATUS+") and insured_key = "+insuredKey;
		
//		String query = "SELECT   COUNT(1)  FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
//				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
//				+ "WHERE B.STATUS_ID NOT IN(7) AND A.INSURED_KEY  = "+insuredKey;
		
		String query = "SELECT  CLAIM_KEY FROM IMS_CLS_INTIMATION A INNER JOIN IMS_CLS_CLAIM B ON A.INTIMATION_KEY=B.INTIMATION_KEY "
				+ "INNER JOIN IMS_CLS_POLICY C ON C.POLICY_KEY =A.POLICY_KEY "
				+ "WHERE B.STATUS_ID NOT IN(7) AND A.POLICY_KEY  = C.POLICY_KEY AND A.INSURED_KEY = "+ insuredKey;
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		
		List<Object> objList = nativeQuery.getResultList();
		List<Long> claimKeyList = new ArrayList<Long>();
		
		if(null != objList && !objList.isEmpty())
		{
//			for (Iterator it = objList.iterator(); it.hasNext();)
//			{
//				 Object[] myResult = (Object[]) it.next();
//				 Long count = (Long) myResult[0];
//				 return count;
//			}
			for(int i = 0 ; i < objList.size() ; i++)
			{
				BigDecimal count = (BigDecimal)objList.get(i);
				if(null != count)
					claimKeyList.add(count.longValue());
			}
			/*for (Object object : objList) {
			
				
			}*/
			
			//return count.longValue();
		}

		return claimKeyList;
	}
    
    public List<Reimbursement> getReimbursementListForClose(String intimationNumber){
    	
    	List<Reimbursement> reimbursement = new ArrayList<Reimbursement>();
    	
    	Query query = entityManager.createNamedQuery("Reimbursement.findByIntimationNumber")
				.setParameter("intimationNumber", intimationNumber);
		List<Reimbursement> rodList = query.getResultList();
		
		for (Reimbursement reimbursement2 : rodList) {
			
			if(reimbursement2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
					|| reimbursement2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
				reimbursement.add(reimbursement2);
			}
		}
		
		return reimbursement;
    	
    	
    }
    
    
    public Boolean isAnyRodActive(String intimationNumber){
    	
    	Claim claim = getClaimsByIntimationNumber(intimationNumber);
    	
    	List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claim.getKey());
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())
						&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && ! (reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED))) {
						return true;					
				}
			}
		}

		return false;
    }
    
	public List<Reimbursement> getPreviousRODByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (Reimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
	}
    
	@SuppressWarnings("unchecked")
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		List<Claim> resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (List<Claim>) findByIntimationNum.getResultList();
				
				if(resultClaim != null && !resultClaim.isEmpty()){
					return resultClaim.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
    
    /***
     * The below method to be refractored
     * during close claim migration.
     * 
     * */
    
    /*public Map<String, String> getAcquiredUserId(String intimationId)
    {
    	Map<String, List<String>> rodUserListMap = new  HashMap<String, List<String>>();
    	Map<String, String> userMap = new HashMap<String, String>();
    	CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask("claimshead", BPMClientContext.BPMN_PASSWORD);
=======
		return claimKeyList;
	}
	
/*	private List<ViewTmpClaim> getClaimByInsuredId(final Intimation intimation) {
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		
		List<Intimation> intimationlist = getInsuredWiseClaimList(intimation);
		getPreviousInsuredNumber(intimation);
		
		List<Claim> currentClaim = getClaimByIntimation(intimation.getKey());
		
		List<Claim> previousClaimList = new ArrayList<Claim>();
		
		String policyNumber =intimation.getPolicy().getPolicyNumber();
		
		List<Claim> claimsByPolicyNumber = 
				getClaimsByPolicyNumber(policyNumber);
		
		for (Claim viewTmpClaim : claimsByPolicyNumber) {
			if(currentClaim.get(0).getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
				previousClaimList.add(viewTmpClaim);
			}
		}
		
//		for (ViewTmpIntimation viewTmpIntimation : intimationlist) {
//			 previousClaimList = claimService.getTmpClaimByIntimation(viewTmpIntimation.getKey());
//		}
		try{
			
			previousClaimList = getPreviousClaimInsuedWiseForPreviousPolicy(intimation.getPolicy().getRenewalPolicyNumber(), 
					previousClaimList,currentClaim.get(0).getIntimation().getInsured().getHealthCardNumber());
			
		}catch(Exception e){
			e.printStackTrace();
		}
<<<<<<< HEAD
		rodUserListMap.put("rodNoList", rodNumberList);
		rodUserListMap.put("userList", acquireUserList);
		return userMap;
    }*/
    
    
/*    public Map<String, String> getAcquiredUserIdCashless(String intimationId)
    {
<<<<<<< HEAD
    	Map<String, List<String>> cashlessUserListMap = new  HashMap<String, List<String>>();
    	CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask("claimshead", BPMClientContext.BPMN_PASSWORD);
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		
	}
	
	
	public List<ViewTmpClaim> getPreviousClaimInsuedWiseForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList,String healthCardNumber) {
		
		try {
			Policy renewalPolNo = getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(healthCardNumber)){
							if(!generatedList.contains(viewTmpClaim)) {
								generatedList.add(viewTmpClaim);
							}
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimInsuedWiseForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList,healthCardNumber);
				} else {
					return generatedList;
				}
=======
    	Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByDocAcknowledgementKey");
    	query = query.setParameter("docAckKey",key);
    	List<AcknowledgeDocument> ackDocList = query.getResultList();
    	List<UploadDocumentDTO> uploadDocList = null;
    	if(null != ackDocList && !ackDocList.isEmpty())
    	{
    		uploadDocList = new ArrayList<UploadDocumentDTO>();
    		UploadDocumentDTO uploadDoc = null;
    		SelectValue selValue = null;
    		for (AcknowledgeDocument acknowledgeDocument : ackDocList) {
    			uploadDoc = new UploadDocumentDTO();
    			uploadDoc.setFileName(acknowledgeDocument.getFileName());
    			if(null != acknowledgeDocument.getFileType())
    			{
    				selValue = new SelectValue();
    				selValue.setId(acknowledgeDocument.getFileType().getKey());
    				selValue.setValue(acknowledgeDocument.getFileType().getValue());
    				uploadDoc.setFileType(selValue);
    				uploadDoc.setFileTypeValue(acknowledgeDocument.getFileType().getValue());
    				selValue = null;
    			}
    			uploadDoc.setBillNo(acknowledgeDocument.getBillNumber());
    			uploadDoc.setBillDate(acknowledgeDocument.getBillDate());
    			uploadDoc.setNoOfItems(acknowledgeDocument.getNoOfItems());
    			uploadDoc.setBillValue(acknowledgeDocument.getBillAmount());
    			uploadDoc.setAckDocKey(acknowledgeDocument.getKey());
    			uploadDoc.setDmsDocToken(acknowledgeDocument.getDocToken());
    			uploadDocList.add(uploadDoc);
>>>>>>> FHOwithproduction
			}
		} catch(Exception e) {
		
	}
	return generatedList;
}*/
	
	/*public Policy getByPolicyNumber(String policyNumber) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query findAll = entityManager.createNamedQuery(
				"Policy.findByPolicyNumber").setParameter("policyNumber",
				policyNumber);

		if (findAll.getResultList().size() > 0) {
			return (Policy) findAll.getSingleResult();
		}
<<<<<<< HEAD
		cashlessUserListMap.put("cashlessNoList", cashlessNumberList);
		cashlessUserListMap.put("userList", acquireUserList);
		return userMap;
    }*/
    
    public Policy getByPolicyNumber(String policyNumber) {
		Query findAll = entityManager.createNamedQuery(
				"Policy.findByPolicyNumber").setParameter("policyNumber",
				policyNumber);
		
		List<Policy> policyList = (List<Policy>)findAll.getResultList();
		if (policyList != null && ! policyList.isEmpty()) {
			return policyList.get(0);
		}
		return null;
    }
   
	@SuppressWarnings("unchecked")
	public List<Claim> getClaimsByPolicyNumber(String policyNumber) {
		
		List<Claim> resultList = new ArrayList<Claim>();
		if (policyNumber != null) {
			Query findByPolicyNumber = entityManager.createNamedQuery(
					"Claim.findByPolicyNumber").setParameter("policyNumber",
					policyNumber);

			try {
				resultList = findByPolicyNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			for (Claim claim : resultList) {
//				entityManager.refresh(claim);
			}
		}
		return resultList;
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

/*<<<<<<< HEAD
public List<DMSDocumentDetailsDTO> getAcknowledgeDocumentList(Claim claim)
	{
	
	
		if(null != claim)
		{
			BPMClientContext context = new BPMClientContext();
			String dmsAPIUrl = context.getDMSRestApiUrl();
			Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByClaimKey");
			query = query.setParameter("claimKey", claim.getKey());
			List<AcknowledgeDocument> ackDocList = query.getResultList();
			List<DMSDocumentDetailsDTO> dtoList = null;
			if(null != ackDocList && !ackDocList.isEmpty())
			{
				dtoList = new ArrayList<DMSDocumentDetailsDTO>();
				entityManager.refresh(ackDocList.get(0));
				for (AcknowledgeDocument acknowledgeDocument : ackDocList) {
				
					DMSDocumentDetailsDTO detailsDTO = new DMSDocumentDetailsDTO();
					detailsDTO.setDmsRestApiURL(dmsAPIUrl);	
					if(null != claim.getIntimation())
						detailsDTO.setIntimationNo(claim.getIntimation().getIntimationId());
					detailsDTO.setClaimNo(claim.getClaimId());
					detailsDTO.setDocumentType(SHAConstants.SEARCH_UPLOAD_DOC_TYPE);
					detailsDTO.setFileName(acknowledgeDocument.getFileName());		
					detailsDTO.setDocumentSource(SHAConstants.SEARCH_UPLOAD_DOC_SOURCE);
					detailsDTO.setDmsDocToken(acknowledgeDocument.getDocToken());
					detailsDTO.setGalaxyFileName(acknowledgeDocument.getFileName());
					detailsDTO.setDocumentCreatedDate(acknowledgeDocument.getCreatedDate());
					dtoList.add(detailsDTO);
					
=======
			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
				}

				System.out.println("size++++++++++++++" + a_claimList.size());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
=======
    public void submitCloseClaim(CloseClaimPageDTO bean){
    	
    	CloseClaim closeClaim = new CloseClaim();
    	
        Claim claim = getClaimByClaimKey(bean.getClaimKey());
        
    	if(bean.getReasonId() != null){
    	MastersValue closeReason = new MastersValue();
    	closeReason.setKey(bean.getReasonId().getId());
    	closeReason.setValue(bean.getReasonId().getValue());
    	closeClaim.setClosingReasonId(closeReason);
    	
    	}
    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
    	closeClaim.setCreatedBy(bean.getUserName());
    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    	closeClaim.setCloseType("C");
    	closeClaim.setClaim(claim);
    	closeClaim.setStage(claim.getStage());
    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
    	
    	Status status = new Status();
    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
    	
    	closeClaim.setStatus(status);
    	
    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
        entityManager.persist(closeClaim);
        entityManager.flush();
        entityManager.clear();

        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
        	
        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
        	
        	if(preauthList != null &&  preauthList.size() == 1){
        		Preauth lastPreauth = preauthList.get(0);
        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
							&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
        		     Stage preauthStage = new Stage();
        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
        		     Status preauthStatus = new Status();
        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//        		     lastPreauth.setStage(preauthStage);
        		     lastPreauth.setStatus(preauthStatus);
        		     lastPreauth.setModifiedBy(bean.getUserName());
        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        		     entityManager.merge(lastPreauth);
        		     entityManager.flush();
        		     entityManager.clear();
        		     
        		}
        	}
        }
        
        claim.setStatus(status);
        claim.setModifiedBy(bean.getUserName());
        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        claim.setCurrentProvisionAmount(0d);
        claim.setCloseDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(claim);
        entityManager.flush();
        entityManager.clear();
        
        List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
        for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
			DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
			entityManager.persist(documentDetails);
			entityManager.flush();
			entityManager.clear();
>>>>>>> FHOwithproduction
		}
		return a_claimList;

	}*/

	
	private List<Intimation> getInsuredWiseClaimList(final Intimation intimation) {
		List<Intimation> intimationlist =  getIntimationListByInsured(String.valueOf(intimation.getInsured()
						.getInsuredId()));
		String PreviousInsuredId = getPreviousInsuredNumber(intimation);
		intimationlist.remove(intimation);
		if (PreviousInsuredId != null) {
			List<Intimation> previousIntimationlist = getIntimationListByInsured(PreviousInsuredId);
			if (previousIntimationlist.size() != 0) {
				intimationlist.addAll(previousIntimationlist);
			}
		}
		
		return intimationlist;
	}
	
	
	
	private String getPreviousInsuredNumber(Intimation a_intimation) {

		/*TmpInsured tmpInsured = insuredService.getInsured(a_intimation
				.getPolicy().getInsuredId());*/
		Insured insured = getCLSInsured(String.valueOf(a_intimation
				.getInsured().getInsuredId()));
		
		// TmpInsured tmpInsured = insuredService.getInsured(a_intimation
		// .getPolicy().getPolicyNumber(), a_intimation.getPolicy()
		// .getInsuredFirstName(), a_intimation.getPolicy()
		// .getInsuredDob());
		if (insured != null)
			return insured.getRelationshipwithInsuredId().getValue();
		return null;
	}
	
	public Insured getCLSInsured(String key) {

		Query query = entityManager
				.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", Long.parseLong(key));
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}
	

    public  MastersValue getDocTypeValue(Long masterKey) {
    	MastersValue documenttype = null;
					
			Query findByReimbursementKey = entityManager.createNamedQuery(
					"MastersValue.findByKey").setParameter("parentKey", masterKey);
			try{
				List<MastersValue> masterList = (List<MastersValue>) findByReimbursementKey.getResultList();
				if(null != masterList && !masterList.isEmpty())
				{
					documenttype = masterList.get(0);
				}
				return documenttype;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

}
    

    
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
    
    public Boolean getAlreadySelectedBenefit(Long claimKey ,Long value)
	{
		Boolean isSelectedBenefitSelected = false;
		
		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbList = query.getResultList();
		if(null != reimbList && !reimbList.isEmpty())
		{
			for (Reimbursement reimbursement : reimbList) {
				
				if(null != reimbursement.getBenefitsId())
				{
				if(value.equals(reimbursement.getBenefitsId().getKey()) && ((ReferenceTable.FINANCIAL_CANCEL_ROD.equals(reimbursement.getStatus().getKey()))
						|| (ReferenceTable.BILLING_CANCEL_ROD.equals(reimbursement.getStatus().getKey())) || (ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS.equals(reimbursement.getStatus().getKey()))
						|| (ReferenceTable.ZONAL_REVIEW_CANCEL_ROD.equals(reimbursement.getStatus().getKey()))))
				{
					isSelectedBenefitSelected = false;
					
				}				
				else
				{   
					if(value.equals(reimbursement.getBenefitsId().getKey()))
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
			}
		}
		else
		{
			isSelectedBenefitSelected = false;
		}
		return isSelectedBenefitSelected;
	}
    
    public Boolean getAlreadySelectedBenefitBillEntry(Long claimKey ,Long value,Long rodKey)
 	{
 		Boolean isSelectedBenefitSelected = false;
 		
 		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
 		query = query.setParameter("claimKey", claimKey);
 		List<Reimbursement> reimbList = query.getResultList();
 		if(null != reimbList && !reimbList.isEmpty())
 		{
 			for (Reimbursement reimbursement : reimbList) {
 				
 				if(null != reimbursement.getBenefitsId())
 				{
 				if(!rodKey.equals(reimbursement.getKey()) &&value.equals(reimbursement.getBenefitsId().getKey()) && (ReferenceTable.FINANCIAL_CANCEL_ROD.equals(reimbursement.getStatus().getKey())
 						|| ReferenceTable.BILLING_CANCEL_ROD.equals(reimbursement.getStatus().getKey()) || ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS.equals(reimbursement.getStatus().getKey())
 						|| ReferenceTable.ZONAL_REVIEW_CANCEL_ROD.equals(reimbursement.getStatus().getKey())))
 				{
 					isSelectedBenefitSelected = false;
 					
 				}				
 				else
 				{
 					if(null != rodKey && !rodKey.equals(reimbursement.getKey()) && value.equals(reimbursement.getBenefitsId().getKey()))
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
 			}
 		}
 		else
 		{
 			isSelectedBenefitSelected = false;
 		}
 		return isSelectedBenefitSelected;
 	}
    
    
    
    public String getAlreadySelectedAdditionalCovers(Long rodKey,List<AddOnCoversTableDTO> coversList)
	{
		String isSelectedCoversSelected = null;
		
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
		query = query.setParameter("rodKey", rodKey);
		List<PAAdditionalCovers> AdditionalCoverList = query.getResultList();
		if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
		{
			for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
				   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
				      if(AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getCovers().getId())
				      {
				    	  isSelectedCoversSelected = coversList.get(cover1).getCovers().getValue();			    	  
				      }
				    
			
		}
		
		return isSelectedCoversSelected;
	}
	
	public String getAlreadySelectedOptionalCovers(Long rodKey,List<AddOnCoversTableDTO> coversList)
	{
		String isSelectedCoversSelected = null;
		
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
		query = query.setParameter("rodKey", rodKey);
		List<PAOptionalCover> AdditionalCoverList = query.getResultList();
		if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
		{
			for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
				   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
				      if(AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getOptionalCover().getId())
				      {
				    	  isSelectedCoversSelected = coversList.get(cover1).getOptionalCover().getValue();			    	  
				      }
				    
			
		}
		
		return isSelectedCoversSelected;
	}
	
	  public String getAlreadySelectedAdditionalCoversByAckKey(Long ackKey,List<AddOnCoversTableDTO> coversList,Long currentAckKey)
		{
			String isSelectedCoversSelected = null;
			
			Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByAckKey");
			query = query.setParameter("ackDocKey", ackKey);
			List<PAAdditionalCovers> AdditionalCoverList = query.getResultList();
			if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
			{
				for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
					   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
					      if(!currentAckKey.equals(ackKey) && AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getCovers().getId())
					      {
					    	  isSelectedCoversSelected = coversList.get(cover1).getCovers().getValue();	
					    	 
					      }
					    
				
			}
			
			return isSelectedCoversSelected;
		}
		
		public String getAlreadySelectedOptionalCoversByAckKey(Long ackKey,List<AddOnCoversTableDTO> coversList,Long currentAckKey)
		{
			String isSelectedCoversSelected = null;
			
			Query query = entityManager.createNamedQuery("PAOptionalCover.findByAckKey");
			query = query.setParameter("ackDocKey", ackKey);
			List<PAOptionalCover> AdditionalCoverList = query.getResultList();
			if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
			{
				for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
					   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
					      if(!currentAckKey.equals(ackKey) && AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getOptionalCover().getId())
					      {
					    	  isSelectedCoversSelected = coversList.get(cover1).getOptionalCover().getValue();	
					    	 
					      }
					    
				
			}
			
			return isSelectedCoversSelected;
		}
		
	
	 public String getAlreadySelectedAdditionalCoversForBillEntry(Long rodKey,List<AddOnCoversTableDTO> coversList,Long currentReimbKey)
		{
			String isSelectedCoversSelected = null;
			
			Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
			query = query.setParameter("rodKey", rodKey);
			List<PAAdditionalCovers> AdditionalCoverList = query.getResultList();
			if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
			{
				for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
					   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
					      if(!currentReimbKey.equals(rodKey) && AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getCovers().getId())
					      {
					    	  isSelectedCoversSelected = coversList.get(cover1).getCovers().getValue();			    	  
					      }
					    
				
			}
			
			return isSelectedCoversSelected;
		}
		
		public String getAlreadySelectedOptionalCoversForBillEntry(Long rodKey,List<AddOnCoversTableDTO> coversList,Long currentReimbKey)
		{
			String isSelectedCoversSelected = null;
			
			Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
			query = query.setParameter("rodKey", rodKey);
			List<PAOptionalCover> AdditionalCoverList = query.getResultList();
			if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty())
			{
				for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
					   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
					      if(!currentReimbKey.equals(rodKey) && AdditionalCoverList.get(cover).getCoverId() == coversList.get(cover1).getOptionalCover().getId())
					      {
					    	  isSelectedCoversSelected = coversList.get(cover1).getOptionalCover().getValue();			    	  
					      }
					    
				
			}
			
			return isSelectedCoversSelected;
		}

		public String getAlreadySelectedAdditionalCoversForRod(Long claimKey,List<AddOnCoversTableDTO> coversList,Long currentAckKey)
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
					      if(!currentAckKey.equals(AdditionalCoverList.get(cover).getAcknowledgementKey()) && AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getCovers().getId()))
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
		
		public String getAlreadySelectedOptionalCoversForRod(Long claimKey,List<AddOnCoversTableDTO> coversList,Long currentAckKey)
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
						   if(!currentAckKey.equals(AdditionalCoverList.get(cover).getAcknowledgementKey()) && AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getOptionalCover().getId())
								   && coversList.get(cover1).getOptionalCover() != null && coversList.get(cover1).getOptionalCover().getValue() != null
									&& !coversList.get(cover1).getOptionalCover().getValue().equalsIgnoreCase(SHAConstants.MEDICAL_EXTENSION_COVER_DESC))
					      {
					    	  isSelectedCoversSelected1 = coversList.get(cover1).getOptionalCover().getValue();			    	  
					      }
						   }
					    
				isSelectedCoversSelected = isSelectedCoversSelected1;
			}
			}
			return isSelectedCoversSelected;
		}
		public String getAlreadySelectedAdditionalCoversForBillEntryByClaimKey(Long claimKey,List<AddOnCoversTableDTO> coversList,Long currentRodKey)
		{
			String isSelectedCoversSelected = null;
			
			Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByClaimKey");
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
							   if(null != AdditionalCoverList.get(cover).getRodKey())
							   {
					      if(!currentRodKey.equals(AdditionalCoverList.get(cover).getRodKey()) && AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getCovers().getId()))
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
		
		public String getAlreadySelectedOptionalCoversForBillEntryByClaimKey(Long claimKey,List<AddOnCoversTableDTO> coversList,Long currentRodKey)
		{
			String isSelectedCoversSelected = null;
			 String isSelectedCoversSelected1 = null;
			
			Query query = entityManager.createNamedQuery("PAOptionalCover.findByClaimKey");
			query = query.setParameter("claimKey", claimKey);
			List<PAOptionalCover> AdditionalCoverList = query.getResultList();
			if(null != AdditionalCoverList && !AdditionalCoverList.isEmpty() && null != coversList && !coversList.isEmpty())
			{
				for (int cover = 0; cover < AdditionalCoverList.size(); cover++) 
					   for (int cover1 = 0; cover1 < coversList.size(); cover1++) 
						   if(null != coversList.get(cover1).getOptionalCover())
						   {
						   if(!currentRodKey.equals(AdditionalCoverList.get(cover).getRodKey()) && AdditionalCoverList.get(cover).getCoverId().equals(coversList.get(cover1).getOptionalCover().getId()))
					      {
					    	  isSelectedCoversSelected1 = coversList.get(cover1).getOptionalCover().getValue();			    	  
					      }
						   }
					    
				isSelectedCoversSelected = isSelectedCoversSelected1;
			}
			
			return isSelectedCoversSelected;
		}
		
		public PAAdditionalCovers getPAdditionalCoversByKey(Long key)
		{
			Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByKey");
			query = query.setParameter("key", key);
			List<PAAdditionalCovers> paAdditionalCoversList = query.getResultList();
			if(null != paAdditionalCoversList && !paAdditionalCoversList.isEmpty())
			{
				return paAdditionalCoversList.get(0);
			}
			return null;
		}
		
		public PAOptionalCover getPAOptionalCoversByKey(Long key)
		{
			Query query = entityManager.createNamedQuery("PAOptionalCover.findByKey");
			query = query.setParameter("key", key);
			List<PAOptionalCover> paOptionalCoversList = query.getResultList();
			if(null != paOptionalCoversList && !paOptionalCoversList.isEmpty())
			{
				return paOptionalCoversList.get(0);
			}
			return null;
		}
		
		public Reimbursement updatePAProvisionAndClaimStatus(DocAcknowledgement docAck, Claim claimObj, ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement,String presenterString)
		{
			
				Double addOnCoversAmt = 0d;
				Double optionalCoversAmt = 0d;
				Double totalClaimedAmt = 0d;
				Double calculatedBenefitsAmt = 0d;
				Double amt  = 0d;
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				if(null != docAck.getBenifitClaimedAmount())
				{
					totalClaimedAmt += docAck.getBenifitClaimedAmount();
				}

				if(null != docAck.getClaim().getProcessClaimType() && ("H").equalsIgnoreCase(docAck.getClaim().getProcessClaimType()) &&
						null != docAck.getClaim().getClaimType() && ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.equals(docAck.getClaim().getClaimType().getKey())
						&& ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization())
				{
					calculatedBenefitsAmt = calculateCurrentProvisionAmtForHealthPA(totalClaimedAmt, rodDTO);
				}
				
				else
				{
					//Double calculatedBenefitsAmt = 0d;
					Double balSI = 0d;
					MastersValue masterValue = getMasterKeyBasedOnMappingCode(docAck.getBenifitFlag());
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
						if(SHAConstants.CREATE_ROD_PA.equalsIgnoreCase(presenterString))
						{
							/**
							 * Added for issue GALAXYMAIN-6204	.
							 * For query reply cases, rod key is required for
							 * provision calculation .Hence below if condition is
							 * added for query reply cases.
							 * */
							if(null != rodDTO && null != rodDTO.getRodqueryDTO())
							{
								balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), rodDTO.getRodqueryDTO().getReimbursementKey() ,null != masterValue ? masterValue.getKey() : 0l);
								/*if(!(ReferenceTable.getGPAProducts().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
									balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), rodDTO.getRodqueryDTO().getReimbursementKey() ,null != masterValue ? masterValue.getKey() : 0l);
								}
								else
								{
									Double sumInsured = dbCalculationService.getGPAInsuredSumInsured(String.valueOf(docAck.getClaim().getIntimation().getInsured().getInsuredId()), 
											docAck.getClaim().getIntimation().getPolicy().getKey());
									
									balSI = dbCalculationService.getGPABalanceSI(
											docAck.getClaim().getIntimation().getPolicy().getKey(),
											docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 
											sumInsured,docAck.getClaim().getIntimation().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
								}*/
							}
							else
							{
								balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), reimbursement.getKey() ,null != masterValue ? masterValue.getKey() : 0l);
								/*if(!(ReferenceTable.getGPAProducts().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
									balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), reimbursement.getKey() ,null != masterValue ? masterValue.getKey() : 0l);
								}
								else{
									Double sumInsured = dbCalculationService.getGPAInsuredSumInsured(String.valueOf(docAck.getClaim().getIntimation().getInsured().getInsuredId()), 
											docAck.getClaim().getIntimation().getPolicy().getKey());
									
									balSI = dbCalculationService.getGPABalanceSI(
											docAck.getClaim().getIntimation().getPolicy().getKey(),
											docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 
											sumInsured,docAck.getClaim().getIntimation().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
								}*/
							}
						}
						else if (SHAConstants.BILL_ENTRY_PA.equalsIgnoreCase(presenterString))
							
							balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), reimbursement.getKey() ,null != masterValue ? masterValue.getKey() : 0l);
							/*if(!(ReferenceTable.getGPAProducts().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
								balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), reimbursement.getKey() ,null != masterValue ? masterValue.getKey() : 0l);
							}
							else
							{
								Double sumInsured = dbCalculationService.getGPAInsuredSumInsured(String.valueOf(docAck.getClaim().getIntimation().getInsured().getInsuredId()), 
										docAck.getClaim().getIntimation().getPolicy().getKey());
								
								balSI = dbCalculationService.getGPABalanceSI(
										docAck.getClaim().getIntimation().getPolicy().getKey(),
										docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 
										sumInsured,docAck.getClaim().getIntimation().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
							}*/
					}
					
					
					//balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 0l ,null != masterValue ? masterValue.getKey() : 0l);
					calculatedBenefitsAmt = Math.min(balSI,totalClaimedAmt);
				}
				if(null != docAck.getClaim() && null != docAck.getClaim().getIntimation() && null !=  docAck.getClaim().getIntimation().getInsured() && null != docAck.getClaim().getIntimation().getInsured().getInsuredId() && null != docAck.getClaim().getIntimation().getPolicy().getKey())
				{
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
									if(null != addOnCoversTableDTO.getClaimedAmount() && null != addOnCoversTableDTOObj.getClaimedAmount())
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
								
								/*if(null != addOnCoversTableDTOObj.getClaimedAmount())
								{
									optionalCoversAmt +=  addOnCoversTableDTOObj.getClaimedAmount();
								}*/
								// Added for ticket GALAXYMAIN-5926. This was ond as per Sathish sir input.
								for (AddOnCoversTableDTO optionalCoverDTO : optionalCoversProcedureList) {
									if(null != addOnCoversTableDTOObj.getOptionalCover() && optionalCoverDTO.getCoverId().equals(addOnCoversTableDTOObj.getOptionalCover().getId()))
									{
										if(null != addOnCoversTableDTOObj.getClaimedAmount())
										{
												optionalCoversAmt += Math.min(optionalCoverDTO.getClaimedAmount() , addOnCoversTableDTOObj.getClaimedAmount());
										}
										break;
										
									}
								}
							}
						}
					}
				}
				 amt = calculatedBenefitsAmt + addOnCoversAmt + optionalCoversAmt;
				 if(null != reimbursement)
				 {
					 reimbursement.setCurrentProvisionAmt(amt);
					 reimbursement.setBenApprovedAmt(calculatedBenefitsAmt);
					 reimbursement.setAddOnCoversApprovedAmount(addOnCoversAmt);
					 reimbursement.setOptionalApprovedAmount(optionalCoversAmt);
					 entityManager.merge(reimbursement);
					 entityManager.flush();
				 }
				return reimbursement;
		
		}
		
		private Reimbursement savePARODValues(ReceiptOfDocumentsDTO rodDTO,String productType) {

			CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());
			String ackNumber = docAck.getAcknowledgeNumber();

			
			DocAcknowledgement docAck1 = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());
			String globalAck = docAck1.getAcknowledgeNumber();
			
			Reimbursement reimbursement = null;
			Boolean isQueryReplyReceived = false;
			
			Boolean shouldSkipZMR = false;
			//DocAcknowledgement docAcknowledgement = null;
			
			//Added for current provision amt.
			Double totalClaimedAmt = 0d;
			
			Double previousCurrentProvAmt = 0d;
			Long reimbKey = null;
			Boolean isReconsideration = false;
			
			String username1 = rodDTO.getStrUserName();
			String userNameForDB1 = SHAUtils.getUserNameForDB(username1);
			
		//	HumanTask humanTaskForROD = rodDTO.getHumanTask();
			Long rodKeyFromPayload = null;		
			
			
			
			
			/*BankMaster bankMaster = validateIFSCCode( rodDTO.getDocumentDetails().getIfscCode());
			if(null != bankMaster)
			{
				rodDTO.getDocumentDetails().setBankId(bankMaster.getKey());
			}*/
			
			/*if(null != rodDTO.getDocumentDetails().getBankId())
			{*/
				/*if(null != humanTaskForROD)
				{
					PayloadBOType payloadBO = humanTaskForROD.getPayload();
					if(null != payloadBO && null != payloadBO.getRod()) 
					{
						rodKeyFromPayload = payloadBO.getRod().getKey();
					}
			
				}*/
			
			if(null != rodDTO.getRodKeyFromPayload())
			{
				rodKeyFromPayload = rodDTO.getRodKeyFromPayload();
			}
				
				
				ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO.getReconsiderRODdto();
				
				RODQueryDetailsDTO rodQueryDetailsDTOObj = rodDTO.getRodqueryDTO();
				
				Boolean isQueryReplyYes = false;
				Claim claimObj = null;
				if(rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getKey() != null) {
					claimObj = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
				}
				
				
				if (null != reconsiderDTO
						&& null != reconsiderDTO.getRodKey()) {
					Long rodKey = reconsiderDTO.getRodKey();
					isReconsideration = true;
					/*
					 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
					 * entityManager.flush();
					 */
					
					reimbursement = getReimbursementObjectByKey(rodKey);
					
					DocAcknowledgement latestDocAcknowledgementKeyByROD = getLatestDocAcknowledgementKeyByROD(reimbursement.getKey(), reimbursement.getDocAcknowLedgement().getKey());
					
					// reimbursementKey = previousLatestROD.getKey();
					ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//					ZonalMedicalReviewMapper.getAllMapValues();
					PreauthDTO reimbursementDTO = mapper
							.getReimbursementDTO(reimbursement);
					reimbursementDTO.setIsPostHospitalization(false);
					
				    reimbursementDTO.setNewIntimationDTO(rodDTO.getClaimDTO().getNewIntimationDto());
				    reimbursementDTO.setClaimDTO(rodDTO.getClaimDTO());
				    PolicyDto policyDto = new PolicyMapper().getPolicyDto(reimbursement.getClaim()
							.getIntimation().getPolicy());
				    reimbursementDTO.setPolicyDto(policyDto);
					
					setReimbursmentTOPreauthDTO(mapper, reimbursement.getClaim(), reimbursement,
							reimbursementDTO, true, SHAConstants.CREATE_ROD);
					reimbursementDTO.setIsPreviousROD(true);
					
					docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
					if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
						docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
					
					if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
							&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
							||	!(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
									&& !rodDTO.getDocumentDetails().getAccidentOrDeath()))
						populatePaymentDetails(reimbursement, rodDTO);
					
					populateDocumentDetails(rodDTO , docAck);
					
					/*docAck.setReconsideredDate((new Timestamp(System
							.currentTimeMillis())));*/
					
					rodDTO.getDocumentDetails().setRodNumber(reimbursement.getRodNumber());
					//docAck.setRodKey(rodKey);
					reimbKey = rodKey;
					
					rodDTO.setIsRodVersion(true);
					 
					List<ReimbursementCalCulationDetails> reimbursementCalDetails = getReimbursementCalDetails(rodKey);
					rodDTO.setExistingReimbursementCalDtsList(reimbursementCalDetails);
					reimbursement = saveNewVersionROD(rodDTO, docAck1, userNameForDB1, rodKeyFromPayload, claimObj);
					
					reimbursement.setReconsiderationRequest("Y");
					 
					Status status = new Status();
					status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
			
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
					
					reimbursement.setStage(stage);
					reimbursement.setStatus(status);					
				
					if(null != docAck.getBenifitFlag())
					{
						MastersValue benefitFlag = getMasterKeyBasedOnMappingCode(docAck.getBenifitFlag());
						if(null != benefitFlag)
						{
							MastersValue masValue = new MastersValue();
							masValue.setKey(benefitFlag.getKey());
							masValue.setValue(benefitFlag.getValue());
							reimbursement.setBenefitsId(masValue);
						}
					}				
					
					if(null != docAck.getBenifitClaimedAmount())
					{
						reimbursement.setBenApprovedAmt(docAck.getBenifitClaimedAmount());
					}
					
					 docAck.setRodKey(reimbursement.getKey());
					 entityManager.merge(docAck);
					 entityManager.flush();
					
					 if(latestDocAcknowledgementKeyByROD != null){
						 Reimbursement existingReimbursement = getReimbursementObjectByKey(rodKey);
						 existingReimbursement.setDocAcknowLedgement(latestDocAcknowledgementKeyByROD);
						 entityManager.merge(existingReimbursement);
						 entityManager.flush();
					 }
					
					/*
					 * reimbursement.setDocAcknowLedgement(docAck);
					 * entityManager.persist(reimbursement); entityManager.flush();
					 */
		
				} 
				
				else if (null != rodQueryDetailsDTOObj
						&& null != rodQueryDetailsDTOObj.getReimbursementKey()) {
					
					Long rodKey = rodQueryDetailsDTOObj.getReimbursementKey();
					
					isQueryReplyReceived = true;
					docAck = getDocAcknowledgementBasedOnKey(rodQueryDetailsDTOObj.getAcknowledgementKey());
					ackNumber = docAck.getAcknowledgeNumber();
					reimbursement = getReimbursementObjectByKey(rodKey);
					if(reimbursement != null){
						reimbursement.setDocAcknowLedgement(docAck);
					}
					
					if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
							&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
							||	!(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
									&& !rodDTO.getDocumentDetails().getAccidentOrDeath())) {

						populatePaymentDetails(reimbursement, rodDTO);
					}	
					
					Status status = new Status();
					status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
			
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
					
					reimbursement.setStatus(status);
					reimbursement.setStage(stage);
					
					/*
					 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
					 * entityManager.flush();
					 */
					
					
					//previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
					/*
					 * reimbursement.setDocAcknowLedgement(docAck);
					 * entityManager.persist(reimbursement); entityManager.flush();
					 */
		
				}
				/**
				 * Below block added for cancel rod scenario.
				 * */
		//		else if(null != rodKeyFromPayload)
		//		{
		//			reimbursement = getReimbursementObjectByKey(rodKeyFromPayload);
		//			docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
		//			populatePaymentDetails(reimbursement, rodDTO);
		//			populateDocumentDetails(rodDTO , docAck);
		//			//docAck.setRodKey(rodKey);
		//			
		//			Status status = new Status();
		//			status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
		//	
		//			Stage stage = new Stage();
		//			stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
		//			
		//			reimbursement.setStage(stage);
		//			reimbursement.setStatus(status);
		//			
		//			entityManager.merge(reimbursement);
		//			entityManager.flush();
		//
		//		}
				
				else if (rodDTO.getClaimDTO().getClaimType() != null
						&& rodDTO.getClaimDTO().getClaimType().getId()
								.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					String[] split = rodDTO.getDocumentDetails().getRodNumber()
							.split("/");
					String string = split[split.length - 1];
					rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
					rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
					if (SHAUtils.getIntegerFromString(string) == 1) {
						
						reimbursement = getCopyFromPreauth(rodDTO, createRODMapper, docAck,
								userNameForDB1, rodKeyFromPayload, claimObj);
					}
					else{
		//				reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
		//				savePayementDetails(reimbursement, rodDTO.getDocumentDetails());
		//				reimbursement.setActiveStatus(1l);
		//				reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
		//						.getKey()));
		//				reimbursement.setRodNumber(rodDTO.getDocumentDetails()
		//						.getRodNumber());
		//				reimbursement.setDocAcknowLedgement(docAck);
						rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
						rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
		
						if (rodDTO.getPreauthDTO().getIsPreviousROD()) {
							ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//							ZonalMedicalReviewMapper.getAllMapValues();
							DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
							rodDTO.getPreauthDTO().setDateOfAdmission(
									docsDTO.getDateOfAdmission());
							


							if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
									&& ReferenceTable.RECEIVED_FROM_HOSPITAL.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()))
									||	!(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
											&& rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
											&& !rodDTO.getDocumentDetails().getAccidentOrDeath())) {

								rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
										.setReasonForChange(docsDTO.getReasonForChange());
								rodDTO.getPreauthDTO().setPaymentModeId(
										docsDTO.getPaymentModeFlag());
								if (null != docsDTO.getPayeeName()) {
									rodDTO.getPreauthDTO().setPayeeName(
											docsDTO.getPayeeName().getValue());
								}
								rodDTO.getPreauthDTO()
										.setPayeeEmailId(docsDTO.getEmailId());
								rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());

//								rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
//										.setLegalFirstName(docsDTO.getLegalFirstName());

								rodDTO.getPreauthDTO().setAccountNumber(
										docsDTO.getAccountNo());
								rodDTO.getPreauthDTO().setAccountPreference(
										docsDTO.getAccountPreference());
								rodDTO.getPreauthDTO().setAccountType(
										docsDTO.getAccountType());
								rodDTO.getPreauthDTO().setNameAsPerBankAccount(docsDTO.getNameAsPerBank());
								if(docsDTO.getBankId() != null){
									rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
								}
							}							
							
							rodDTO.getPreauthDTO()
									.getPreauthDataExtractionDetails()
									.setRoomCategory(
											rodDTO.getClaimDTO().getNewIntimationDto()
													.getRoomCategory());
							rodDTO.getPreauthDTO().setStatusValue(
									rodDTO.getStatusValue());
							rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
							rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
							rodDTO.getPreauthDTO().setRodNumber(
									rodDTO.getDocumentDetails().getRodNumber());
							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
									.setDocAckknowledgement(docAck);
							reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());
							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().setVentilatorSupport(null);
							if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null
									&& rodDTO.getDocumentDetails().getAccidentOrDeath()) {
										savePayementDetails(reimbursement, docsDTO, claimObj);
							}
									
						//	reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
							
							if(null != rodKeyFromPayload)
							{
								reimbursement.setKey(rodKeyFromPayload);
							}
							
							if (reimbursement.getKey() != null) {
							reimbursement.setDocAcknowLedgement(docAck);
		//					reimbursement.setBillingApprovedAmount(0d);
		//					reimbursement.setFinancialApprovedAmount(0d);
							reimbursement.setSkipZmrFlag("N");
							reimbursement.setModifiedBy(userNameForDB1);
							reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							reimbursement.setApprovedAmount(0d);
							reimbursement.setBillingApprovedAmount(null);
							reimbursement.setFinancialApprovedAmount(null);
							reimbursement.setZonalDate(null);
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursement.setVersion(1l);
							reimbursement.setReconsiderationRequest("N");
//							entityManager.merge(reimbursement);
								
							} else {
		//						reimbursement.setBillingApprovedAmount(0d);
		//						reimbursement.setFinancialApprovedAmount(0d);
								reimbursement.setSkipZmrFlag("N");
								reimbursement.setApprovedAmount(0d);
								reimbursement.setCreatedBy(userNameForDB1);
								reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								reimbursement.setBillingApprovedAmount(null);
								reimbursement.setFinancialApprovedAmount(null);
								reimbursement.setZonalDate(null);
								reimbursement.setAddOnCoversApprovedAmount(0d);
								reimbursement.setOptionalApprovedAmount(0d);
								reimbursement.setVersion(1l);
								reimbursement.setReconsiderationRequest("N");
//								entityManager.persist(reimbursement);
							}
//							entityManager.flush();
							reimbursement.setOtherInsurerApplicableFlag(null);
							reimbursement = savePreauthValues(rodDTO.getPreauthDTO(),reimbursement,reimbursementMapper,
									true, false,rodDTO.getStrUserName(), rodKeyFromPayload, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
							log.info("------Reimbursement------>"+reimbursement+"<------------");
						} else {
							reimbursement = getCopyFromPreauth(rodDTO, createRODMapper, docAck,
									userNameForDB1, rodKeyFromPayload, claimObj);
						}
					}
		
				} else {
					
					rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
					rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
		
					if (rodDTO.getPreauthDTO().getIsPreviousROD()) {
						ZonalMedicalReviewMapper reimbursementMapper =ZonalMedicalReviewMapper.getInstance();
						DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
						rodDTO.getPreauthDTO().setDateOfAdmission(
								docsDTO.getDateOfAdmission());
						
						if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
								&& rodDTO.getDocumentDetails().getAccidentOrDeath()) {

							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
									.setReasonForChange(docsDTO.getReasonForChange());
							rodDTO.getPreauthDTO().setPaymentModeId(
									docsDTO.getPaymentModeFlag());
							if (null != docsDTO.getPayeeName()) {
								rodDTO.getPreauthDTO().setPayeeName(
										docsDTO.getPayeeName().getValue());
							}
							rodDTO.getPreauthDTO().setPayeeEmailId(docsDTO.getEmailId());
							rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());
							rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
									.setLegalFirstName(docsDTO.getLegalFirstName());
							rodDTO.getPreauthDTO().setAccountNumber(docsDTO.getAccountNo());
							rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
						}
						
						rodDTO.getPreauthDTO()
								.getPreauthDataExtractionDetails()
								.setRoomCategory(
										rodDTO.getClaimDTO().getNewIntimationDto()
												.getRoomCategory());
						rodDTO.getPreauthDTO().setStatusValue(rodDTO.getStatusValue());
						rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
						rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
						rodDTO.getPreauthDTO().setRodNumber(
								rodDTO.getDocumentDetails().getRodNumber());
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
								.setDocAckknowledgement(docAck);
						reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());
						if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null
								&& rodDTO.getDocumentDetails().getAccidentOrDeath()) {
							savePayementDetails(reimbursement, docsDTO, claimObj);
						}	
						
						reimbursement.setDocAcknowLedgement(docAck);
						reimbursement.setVentilatorSupport(null);
						
						
						Hospitals hospital = getHospitalByName(rodDTO.getDocumentDetails().getHospitalName());
						if(null != hospital)
							reimbursement.setHospitalId(hospital.getKey());
						if(null != rodKeyFromPayload)
						{
							reimbursement.setKey(rodKeyFromPayload);
						}
						if(reimbursement.getKey() != null) {
							reimbursement.setSkipZmrFlag("N");
							reimbursement.setBillingApprovedAmount(0d);
							reimbursement.setApprovedAmount(0d);
							reimbursement.setFinancialApprovedAmount(0d);
							reimbursement.setBillingCompletedDate(null);
							reimbursement.setFinancialCompletedDate(null);
							reimbursement.setMedicalCompletedDate(null);
							reimbursement.setZonalDate(null);
							reimbursement.setModifiedBy(userNameForDB1);
							reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursement.setVersion(1l);
							reimbursement.setReconsiderationRequest("N");
//							entityManager.merge(reimbursement);
							
						} else {
							String strUserName = rodDTO.getStrUserName();
							String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
							reimbursement.setSkipZmrFlag("N");
							reimbursement.setCreatedBy(userNameForDB);
							reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							reimbursement.setBillingApprovedAmount(0d);
							reimbursement.setBillingCompletedDate(null);
							reimbursement.setFinancialApprovedAmount(0d);
							reimbursement.setFinancialCompletedDate(null);
							reimbursement.setMedicalCompletedDate(null);
							reimbursement.setZonalDate(null);
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursement.setVersion(1l);
							reimbursement.setReconsiderationRequest("N");
//							entityManager.persist(reimbursement);
						}
//						entityManager.flush();
						reimbursement.setOtherInsurerApplicableFlag(null);
						reimbursement = savePreauthValues(rodDTO.getPreauthDTO(), reimbursement, reimbursementMapper, true,
								false,rodDTO.getStrUserName(), rodKeyFromPayload, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
						
						log.info("------Reimbursement------>"+reimbursement+"<------------");
					} else {
						if(null != rodDTO.getDocumentDetails().getPaymentMode() && rodDTO.getDocumentDetails().getPaymentMode())
						{
							rodDTO.getDocumentDetails().setAccountNo(null);
							rodDTO.getDocumentDetails().setBankId(null);
							rodDTO.getDocumentDetails().setIfscCode(null);
							rodDTO.getDocumentDetails().setCity(null);
							rodDTO.getDocumentDetails().setBankName(null);
							rodDTO.getDocumentDetails().setBranch(null);
						}
						else
						{
							rodDTO.getDocumentDetails().setPayableAt(null);
						}
						reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
						if(null != rodDTO.getDocumentDetails().getInsuredPatientName())
						{
							Insured insured = null;
							
							//commented for no unique insured id for BaNCS comprehensive policy
							//insured = getPAInsuredByPolicyAndInsuredID(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId());
							insured = getInsuredByPolicyAndRiskIdWithLobFlag(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId(), SHAConstants.PA_LOB_TYPE);
						/*	else
							{
								insured = getInsuredByPolicyAndInsuredName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber(),rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
							}*/
							if(null != insured)
								reimbursement.setInsuredKey(insured);
							
								if(insured != null){
									Intimation intimation = getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
									if(intimation != null){
										intimation.setInsured(insured);
										if(null != rodDTO)
										{
											intimation.setModifiedBy(rodDTO.getStrUserName());
											intimation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
										}
										entityManager.merge(intimation);
										entityManager.flush();
										log.info("------Intimation------>"+intimation+"<------------");
									}
								}
						}
						Hospitals hospital = getHospitalByName(rodDTO.getDocumentDetails().getHospitalName());
						if(null != hospital)
							reimbursement.setHospitalId(hospital.getKey());
						
						if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
								&& rodDTO.getDocumentDetails().getAccidentOrDeath()) {
							savePayementDetails(reimbursement, rodDTO.getDocumentDetails(), claimObj);
						}
						
						reimbursement.setActiveStatus(1l);
						reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
								.getKey()));
						reimbursement.setRodNumber(rodDTO.getDocumentDetails()
								.getRodNumber());
						reimbursement.setDocAcknowLedgement(docAck);
						reimbursement.setCreatedBy(userNameForDB1);
						reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						reimbursement.setOtherInsurerApplicableFlag(null);
						reimbursement.setNomineeFlag(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getNomineeDeceasedFlag());
						if(null != rodKeyFromPayload)
						{
							reimbursement.setKey(rodKeyFromPayload);
							if(null != rodDTO)
							{
								reimbursement.setModifiedBy(userNameForDB1);
								reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							}
							
							/*if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
									&& !rodDTO.getDocumentDetails().getAccidentOrDeath()
									&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
									&& ReferenceTable.RECEIVED_FROM_INSURED.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId())
									&& rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null 
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									){
								if(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()){
									saveNomineeDetails(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList(), reimbursement);
								}else{
									reimbursement.setNomineeName(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeName());
									reimbursement.setNomineeAddr(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeAddr());
								}
							}*/
							
							entityManager.merge(reimbursement);
						}
						else
						{
							entityManager.persist(reimbursement);
						}
						entityManager.flush();
						log.info("------Reimbursement------>"+reimbursement+"<------------");
					}
		
					// entityManager.refresh(reimbursement);
				}
				
				List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO.getRodQueryDetailsList();
				if(rodQueryDetailsDTO != null){
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
					ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2.getReimbursementQueryKey());
					if(("No").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
					{
						reimbQuery.setQueryReply("N");
						reimbQuery.setDocAcknowledgement(null);
						reimbQuery.setQueryReplyDate(null);
						Status status = new Status();
						if(reimbQuery.getStage() != null && reimbQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
							status.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
							reimbQuery.setStatus(status);
						}else{
							status.setKey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
							reimbQuery.setStatus(status);
						}
					}
					else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
					{
						reimbQuery.setQueryReply("Y");
						isQueryReplyYes = true;
						Long docAcknowledgementKey = rodDTO.getDocumentDetails().getDocAcknowledgementKey();
						DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(docAcknowledgementKey);
						reimbQuery.setDocAcknowledgement(docAcknowledgement);
						if(reimbQuery.getQueryReplyDate() == null){
							reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
						}
						else if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO2.getReplyStatus()))
						{
							reimbQuery.setQueryReply("Y");
							isQueryReplyYes = true;
							reimbQuery.setDocAcknowledgement(docAck);
							if(reimbQuery.getQueryReplyDate() == null){
								reimbQuery.setQueryReplyDate(new Timestamp(System.currentTimeMillis()));
							}
							if(reimbQuery.getStage() != null){
								rodDTO.setQueryStage(reimbQuery.getStage().getKey());
							}
							Status status = new Status();
							if(reimbQuery.getStage() != null && reimbQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
								status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
								reimbQuery.setStatus(status);
								reimbursement.setStatus(status);
							}else{
								status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
								reimbQuery.setStatus(status);
								reimbursement.setStatus(status);
							}
						}
						
						if(null != reimbQuery.getKey())
						{
							if(null != rodDTO)
							{
								reimbQuery.setModifiedBy(userNameForDB1);
								reimbQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							}
							entityManager.merge(reimbQuery);
							entityManager.flush();
							log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
						}
					}
				 }
				}
				

		
				//if(!isQueryReplyReceived){
				//{
					List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
			
					if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
						for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
							if (null != uploadDocumentDTO.getFileType()
									&& !("").equalsIgnoreCase(uploadDocumentDTO
											.getFileType().getValue())) {
								
								if(rodDTO.getIsRodVersion()){
									List<BillEntryDetailsDTO> dtoList = getBillEntryDetailsList(uploadDocumentDTO);
									for (BillEntryDetailsDTO billEntryDetailsDTO : dtoList) {
										billEntryDetailsDTO.setKey(null);
									}
									uploadDocumentDTO.setBillEntryDetailList(dtoList);
									uploadDocumentDTO.setDocSummaryKey(null);
									if(rodDTO.getPreviousRodForReconsider() != null){
										deletedPreviousDocument(rodDTO.getPreviousRodForReconsider());
									}
								}
								RODDocumentSummary rodSummary = createRODMapper
										.getDocumentSummary(uploadDocumentDTO);
								rodSummary.setReimbursement(reimbursement);
								rodSummary.setDocumentToken(uploadDocumentDTO
										.getDmsDocToken());
								rodSummary.setDeletedFlag("N");
								if(null == rodSummary.getKey())
								{
									if(null != rodSummary)
									{
										rodSummary.setCreatedBy(userNameForDB1);
										//IMSSUPPOR-28725
										//rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
										
										if(rodSummary.getCreatedDate() == null){
											rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
										}
									}
									entityManager.persist(rodSummary);
									entityManager.flush();
									log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
									if(rodDTO.getIsRodVersion()){
										List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
										for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
											billEntryDetailsDTO.setDocumentSummaryKey(rodSummary.getKey());
											 RODBillDetails rodBillDetails = createRODMapper
														.getRODBillDetails(billEntryDetailsDTO);
											 rodBillDetails.setRodDocumentSummaryKey(rodSummary);
											 rodBillDetails.setReimbursementKey(reimbursement.getKey());
											 rodBillDetails.setDeletedFlag("N");
											 entityManager.persist(rodBillDetails);
											 entityManager.flush();
										}
									}
								}
								else
								{
									if(null != rodDTO)
									{
										rodSummary.setModifiedBy(userNameForDB1);
										rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
									}
									
									entityManager.merge(rodSummary);
									entityManager.flush();
									log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
								//	entityManager.refresh(rodSummary);
								}
								
								if(null != uploadDocumentDTO.getAckDocKey())
								{
									String userNameForDB = SHAUtils.getUserNameForDB(rodDTO.getStrUserName());
									AcknowledgeDocument ackDocument = getAcknowledgeDocObjByKey(uploadDocumentDTO.getAckDocKey());
									if(null != ackDocument)
									{
										log.info("------Ack document summary key------>"+ackDocument.getKey()+"<------------");

										ackDocument.setDeleteFlag(SHAConstants.YES_FLAG);
										if(null != ackDocument.getKey())
										{
											ackDocument.setModifiedBy(userNameForDB);
											ackDocument.setModifiedDate(new Timestamp(System.currentTimeMillis()));
											entityManager.merge(ackDocument);
											entityManager.flush();
										}
										
										log.info("------Ack document summary  key successfully updated------>"+ackDocument.getKey()+"<------------");
									}
								}
								// entityManager.refresh(rodSummary);
							}
						}
						}
					
					/**
					 * In case of cancel rod, those documents which are uploaded before
					 * cancelling will be available in uploaded documents table.
					 * This document can be deleted when user is trying to recreate
					 * the cancelled the rod. That time deleted record needs 
					 * to be updated in table. Below code was added for the same.
					 * 
					 * */
					List<UploadDocumentDTO> deletedDocsList = rodDTO
							.getUploadDocumentsDTO().getDeletedDocumentList();
					String userNameForDBObj = SHAUtils.getUserNameForDB(rodDTO.getStrUserName());
					if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
						for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

							RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
									.getDocumentSummary(uploadDocumentDTO2);
							rodSummary.setReimbursement(reimbursement);
							rodSummary.setDeletedFlag("Y");

							if (null != uploadDocumentDTO2.getDocSummaryKey()) {
								rodSummary.setModifiedBy(userNameForDBObj);
								rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(rodSummary);
								entityManager.flush();
								log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
							} /*
							 * else { entityManager.persist(rodSummary);
							 * entityManager.flush(); entityManager.refresh(rodSummary);
							 * }
							 */
							
							DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
							if(null != details)
							{
								details.setDeletedFlag("Y");
								entityManager.merge(details);
								entityManager.flush();
								log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
							}

						}
					}
					
				//}/*else{
					
				//}
		
				// Update the ROD KEY in DocAcknowledgement Table.
					
					DocAcknowledgement docAcknowledgement = null;
						if (null != docAck) {
							docAcknowledgement = createRODMapper
									.getAcknowledgeDocumentList(rodDTO);
							if ((null != rodDTO.getDocumentDetails()
										.getBenifitClaimedAmount())
										&& !("").equals(rodDTO.getDocumentDetails()
												.getBenifitClaimedAmount())
										&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
												.getBenifitClaimedAmount())))
								{
									docAcknowledgement.setBenifitClaimedAmount(Double
											.parseDouble(rodDTO.getDocumentDetails().getBenifitClaimedAmount()));
									totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getBenifitClaimedAmount()));
								}
								
								
								if(null != rodDTO.getDocumentDetails().getDeath() && (rodDTO.getDocumentDetails().getDeath().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.DEATH_FLAGS);
								}	
								else if(null != rodDTO.getDocumentDetails().getPermanentPartialDisability() && (rodDTO.getDocumentDetails().getPermanentPartialDisability().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.PPD);
								}
								else if(null != rodDTO.getDocumentDetails().getPermanentTotalDisability() && (rodDTO.getDocumentDetails().getPermanentTotalDisability().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.PTD);
								}
								else if(null != rodDTO.getDocumentDetails().getTemporaryTotalDisability() && (rodDTO.getDocumentDetails().getTemporaryTotalDisability().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.TTD);
								}
								else if(null != rodDTO.getDocumentDetails().getHospitalization() && (rodDTO.getDocumentDetails().getHospitalization().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.HOSP);
								}
								else if(null != rodDTO.getDocumentDetails().getPartialHospitalization() && (rodDTO.getDocumentDetails().getPartialHospitalization().equals(true))){
									docAcknowledgement.setBenifitFlag(SHAConstants.PART);
								}
								
								
								if(null != docAcknowledgement.getBenifitFlag())
								{
									MastersValue benefitFlag = getMasterKeyBasedOnMappingCode(docAcknowledgement.getBenifitFlag());
									if(null != benefitFlag)
									{
										MastersValue masValue = new MastersValue();
										masValue.setKey(benefitFlag.getKey());
										masValue.setValue(benefitFlag.getValue());
										reimbursement.setBenefitsId(masValue);
									}
								}
								else
								{
									reimbursement.setBenefitsId(null);
								}
								
								if(null != docAcknowledgement.getBenifitClaimedAmount())
								{
									reimbursement.setBenApprovedAmt(docAcknowledgement.getBenifitClaimedAmount());
								}
								
								if(null != rodDTO.getDocumentDetails().getDocumentType()){
									docAcknowledgement.setDocumentTypeId(rodDTO.getDocumentDetails().getDocumentType().getId());
								}	
								
								rodDTO.setTotalClaimedAmount(totalClaimedAmt);
								
								
								docAcknowledgement.setProcessClaimType(docAck.getProcessClaimType());
								
								//if(SHAConstants.PA_LOB.equalsIgnoreCase(productType))
								//{
								
								saveBenefitsAndCoverValues(rodDTO, docAck, reimbursement);
								
								//}
								
							
						/*else{
							
							if ((null != rodDTO.getDocumentDetails().getHospitalizationClaimedAmount()) && !("").equals(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())
									&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())))
							{
								docAcknowledgement.setHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO.getDocumentDetails()
												.getHospitalizationClaimedAmount()));
								totalClaimedAmt += (Double
										.parseDouble(rodDTO.getDocumentDetails()
												.getHospitalizationClaimedAmount()));
							}
							if ((null != rodDTO.getDocumentDetails()
									.getPreHospitalizationClaimedAmount())
									&& !("").equals(rodDTO.getDocumentDetails()
											.getPreHospitalizationClaimedAmount())
									&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
											.getPreHospitalizationClaimedAmount())))
							{
								docAcknowledgement.setPreHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()));
								totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()));
							}
							if ((null != rodDTO.getDocumentDetails()
									.getPostHospitalizationClaimedAmount())
									&& !("").equals(rodDTO.getDocumentDetails()
											.getPostHospitalizationClaimedAmount())
									&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
											.getPostHospitalizationClaimedAmount())))
							{
								docAcknowledgement.setPostHospitalizationClaimedAmount(Double
										.parseDouble(rodDTO.getDocumentDetails()
												.getPostHospitalizationClaimedAmount()));
								totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount()));
							}	
						}
							*/
							
							
							docAcknowledgement.setClaim(docAck.getClaim());
							docAcknowledgement.setStage(docAck.getStage());
							docAcknowledgement.setStatus(docAck.getStatus());
							docAcknowledgement.setInsuredContactNumber(docAck
									.getInsuredContactNumber());
							docAcknowledgement.setInsuredEmailId(docAck.getInsuredEmailId());
							/**
							 * In case of reconsideration, in rod level if user changes the reconsideration
							 * preferencen, then for the ack reconsidered, rod number alone needs to be
							 * changed. hence the below condition.
							 * */
							if(null != reconsiderDTO)
							{
								docAcknowledgement.setAcknowledgeNumber(ackNumber);
								if(null != rodDTO.getDocumentDetails().getReasonForReconsideration())
								{
									MastersValue masValue = new MastersValue();
									masValue.setKey(rodDTO.getDocumentDetails().getReasonForReconsideration().getId());
									masValue.setValue(rodDTO.getDocumentDetails().getReasonForReconsideration().getValue());
									docAcknowledgement.setReconsiderationReasonId(masValue);
								}
								
								if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
									docAcknowledgement.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
								
								docAcknowledgement.setReconsideredDate(new Timestamp(System.currentTimeMillis()));
								/*Map reconsiderationMap = rodDTO.getReconsiderationMap();
								Long ackKey = (Long) reconsiderationMap.get("ackKey");
								Long rodKey = (Long) reconsiderationMap.get("rodKey");
								if(!(rodKey.equals(reconsiderDTO.getRodKey())))
								//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
								{
									revertRODData(rodKey);
								}*/
							}
							/**
							 * The below condition was added , since unique constraint violation 
							 * issue was faced. Removing the else condition, might have an reverse 
							 * impact. Since only for query this prob has occured, we're adding
							 * below else if condition.
							 * */
							else if(null != isQueryReplyReceived && isQueryReplyReceived)
							{
								docAcknowledgement.setAcknowledgeNumber(globalAck);
							}
							else
							{
								docAcknowledgement.setAcknowledgeNumber(docAck
										.getAcknowledgeNumber());
							}
							docAcknowledgement.setCreatedDate(docAck.getCreatedDate());
							docAcknowledgement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							
							docAcknowledgement.setRodKey(reimbursement.getKey());
							
							docAcknowledgement.setKey(docAcknowledgement.getKey());
							docAcknowledgement.setModifiedBy(userNameForDB1);
							
							/*if(null != rodDTO)
								docAcknowledgement.setModifiedBy(rodDTO.getStrUserName());*/
				
							entityManager.merge(docAcknowledgement);
	 						entityManager.flush();
							log.info("------DocAcknowledgement------>"+docAcknowledgement+"<------------");
							
							Map reconsiderationMap = rodDTO.getReconsiderationMap();
							if(null != reconsiderDTO)
							{
								//Map reconsiderationMap = rodDTO.getReconsiderationMap();
								if(null != reconsiderationMap)
								{
									Long ackKey = (Long) reconsiderationMap.get("ackKey");
									Long rodKey = (Long) reconsiderationMap.get("rodKey");
									if(null != reconsiderDTO && rodKey != null)
									{
										if(!(rodKey.equals(reconsiderDTO.getRodKey())))
										//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
										{
											revertRODData(rodKey,userNameForDB1);
										}
									}
									
								}
							}
							else if(null != reconsiderationMap)
							{
								Long ackKey = (Long) reconsiderationMap.get("ackKey");
								Long rodKey = (Long) reconsiderationMap.get("rodKey");
								if(!(rodKey.equals(reimbursement.getKey())) && rodKey != null)
								//if(!(reconsiderDTO.getRodKey().equals(rodKey)))
								{
									revertRODData(rodKey,userNameForDB1);
								}
							}
							// entityManager.refresh(docAcknowledgement);
						}
					
		
				List<DocumentCheckListDTO> docCheckListVal = rodDTO
						.getDocumentDetails().getDocumentCheckList();
				if (!docCheckListVal.isEmpty()) {
					for (DocumentCheckListDTO docCheckListDTO : docCheckListVal) {
						//if (null != docCheckListDTO.getRodReceivedStatusFlag()) {
							RODDocumentCheckList rodDocumentCheckList = createRODMapper
									.getRODCheckListForUpdation(docCheckListDTO);
							rodDocumentCheckList.setDocAcknowledgement(docAck);
						//	if (null != docCheckListDTO.getDocTypeId()) {
							if(null != docCheckListDTO.getParticulars().getId())
							{
								if(null != rodDocumentCheckList)
								{
									rodDocumentCheckList.setDocumentTypeId(docCheckListDTO.getParticulars().getId());
									rodDocumentCheckList.setModifiedBy(userNameForDB1);
									rodDocumentCheckList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								}
								entityManager.merge(rodDocumentCheckList);
								entityManager.flush();
								log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
								// entityManager.refresh(rodDocumentCheckList);
							} else {
		
								rodDocumentCheckList.setDocumentTypeId(docCheckListDTO
										.getKey());
								rodDocumentCheckList.setKey(null);
								rodDocumentCheckList.setCreatedBy(userNameForDB1);
								entityManager.persist(rodDocumentCheckList);
								entityManager.flush();
								log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
								// entityManager.refresh(rodDocumentCheckList);
							}
		
						//}
					}
				}
		
				List<Preauth> preAuthList = getPreauthByIntimationKey(rodDTO
						.getClaimDTO().getNewIntimationDto().getKey());
				if (null != preAuthList && !preAuthList.isEmpty()) {
					int iSize = preAuthList.size();
					Preauth preAuth = preAuthList.get(iSize - 1);
					if (null != preAuth && null != preAuth.getTreatmentType())
						rodDTO.setTreatmentType(preAuth.getTreatmentType().getValue());
				} else {
					/**
					 * If cashless is not available for this intimation, then by default
					 * we set treatment type as Medical. Any how during zonal , this
					 * will changed.
					 */
					rodDTO.setTreatmentType("Medical");
				}
				// Intimation objIntimation =
				// getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
		
				// Need to check whether the insured object
		
				/*
				 * if(null != objIntimation) { Insured insured =
				 * getCLSInsured(objIntimation.getInsured().getKey());
				 * objIntimation.setAdmissionDate
				 * (rodDTO.getDocumentDetails().getDateOfAdmission());
				 * insured.setInsuredName
				 * (rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
				 * objIntimation.setInsured(insured);
				 * 
				 * entityManager.merge(objIntimation); entityManager.flush(); }
				 */
				/***
				 * Coded added for updating the current provision amt
				 ***/
		
		//		if(reimbursement.getCurrentProvisionAmt())
				Double balanceSI = getBalanceSI(rodDTO);
				Double currentProvisionAmt = 0d;
		//		previousCurrentProvAmt = reimbursement.getCurrentProvisionAmt();
				/*if(null != previousCurrentProvAmt)
				{*/
					currentProvisionAmt =   calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
				/*}
				else
				{
					currentProvisionAmt = calculateCurrentProvisionAmt (totalClaimedAmt , rodDTO);
				}
				*/
				if(null != balanceSI && null != currentProvisionAmt)
				{
		//			reimbursement.setCurrentProvisionAmt(Math.min(balanceSI, currentProvisionAmt));
				}
				
				/*if(balanceSI > currentProvisionAmt)
				{
					reimbursement.setCurrentProvisionAmt(balanceSI);
				}
				else
				{
					reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
				}*/
				//Claim claimObj = reimbursement.getClaim();
				
				
				
				if(claimObj != null) {
					claimObj = getClaimByClaimKey(reimbursement.getClaim().getKey());
				}
							
			/** The below code commented because of issue to call the balance SI procedure.So the same functionality handled in presenter for both ROD and Bill Entry**/
				/*	if(null != docAcknowledgement)
					{
						//Double amt = updatePAProvisionAndClaimStatus(docAcknowledgement,docAcknowledgement.getClaim(),rodDTO,reimbursement);
						
						
							reimbursement = updatePAProvisionAndClaimStatus(docAcknowledgement,docAcknowledgement.getClaim(),rodDTO,reimbursement,SHAConstants.CREATE_ROD_PA);
						
						DBCalculationService dbCalculationService = new DBCalculationService();
						Double balSI = dbCalculationService.getPABalanceSI(docAck.getClaim().getIntimation().getInsured().getKey(), docAck.getClaim().getKey(), 0l ,null != masterValue ? masterValue.getKey() : 0l);
						//reimbursement.setCurrentProvisionAmt(amt);
					}*/
					
					if(claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
					{
						
					if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
					{
						reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}				
					else
					{
						reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
					}
					if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
							(("Y").equals(rodDTO.getDocumentDetails().getHospitalizationFlag()) || ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))) //---need to implement partialhospitallization
					{
						reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}	
					
					}
					if(claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
					{
						if(docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
								("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) //---need to implement partialhospitallization
						{
							reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
							//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
						}	
						else
						{
							reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
						//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);

						}
					}
					
					
					if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getWorkOrNonWorkPlaceFlag()){
						reimbursement.setWorkPlace(rodDTO.getDocumentDetails().getWorkOrNonWorkPlaceFlag());
					}
					
					/*if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
							&& !rodDTO.getDocumentDetails().getAccidentOrDeath()
							&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()) && rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null 
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
						if(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()){
							saveNomineeDetails(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList(), reimbursement);
						}else{
							reimbursement.setNomineeName(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeName());
							reimbursement.setNomineeAddr(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeAddr());
						}
					}*/
					
					
					if(null != reimbursement.getKey())
					{
					entityManager.merge(reimbursement);
					entityManager.flush();
					}
				
			
				/*<---------------------------- Current  Provision amount updates completed ---------------------------->*/
				
					if(rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
							&& rodDTO.getDocumentDetails().getAccidentOrDeath()) {
					
						if(null != rodDTO.getDocumentDetails().getPaymentMode() && rodDTO.getDocumentDetails().getPaymentMode())
						{
							reimbursement.setAccountNumber(null);	
							reimbursement.setBankId(null);
							reimbursement.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
		
						}
						else
						{
							reimbursement.setPaymentModeId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
							reimbursement.setPayableAt(null);
						}
					}
					
					if(rodDTO.getDocumentDetails().getDateOfDeath() != null && !rodDTO.getDocumentDetails().getDateOfDeath().equals(""))
					{
						reimbursement.setDateOfDeath(rodDTO.getDocumentDetails().getDateOfDeath());
					}
								

				
				if(null != reimbursement.getKey())
				{
					String strUserName = rodDTO.getStrUserName();
					String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
					reimbursement.setModifiedBy(userNameForDB);
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					if(reimbursement.getDateOfDischarge() == null ) {
						if(rodDTO.getDocumentDetails().getDateOfDischarge() != null) {
							reimbursement.setDateOfDischarge(rodDTO.getDocumentDetails().getDateOfDischarge());
						} else {
							if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getDischargeDate() != null) {
								reimbursement.setDateOfDischarge(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getDischargeDate());
							}
						}
					}
					
					
					shouldSkipZMR = shouldSkipZMR(reimbursement.getClaim());
					if(null != rodDTO.getDocumentDetails().getDocumentsReceivedFrom())
					{
					if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null && rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
						shouldSkipZMR = false;
					}
					}
					
					try{
						if((ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))){
							Insured insured = reimbursement.getClaim().getIntimation().getInsured();
								reimbursement.setInsuredKey(insured);								
								reimbursement.setUnNamedKey(reimbursement.getClaim().getIntimation().getUnNamedKey());
						}
					}catch(Exception e){
						e.printStackTrace();
					}
	
					reimbursement.setSkipZmrFlag(shouldSkipZMR ? "Y":"N");
					
					reimbursement.setBillEntryRemarks(null);
					reimbursement.setOtherInsurerHospAmt(0d);
					reimbursement.setOtherInsurerPreHospAmt(0d);
					reimbursement.setOtherInsurerPostHospAmt(0d);
					entityManager.merge(reimbursement);
					entityManager.flush();
					log.info("------Reimbursement------>"+reimbursement.getKey()+"<------------");
				}
					
				
				if(null != claimObj)
				{
					
					Status status1 = new Status();
					status1.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
					
					Stage stage1 = new Stage();
					stage1.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
					
				
						Boolean incidentFlag = null !=  rodDTO.getDocumentDetails().getAccidentOrDeath() ?  rodDTO.getDocumentDetails().getAccidentOrDeath() : null;
						Date incidenceDate = null !=  rodDTO.getDocumentDetails().getAccidentOrDeathDate() ?  rodDTO.getDocumentDetails().getAccidentOrDeathDate() : null;
						
						if(null !=  rodDTO.getDocumentDetails().getAccidentOrDeath() &&  rodDTO.getDocumentDetails().getAccidentOrDeath().equals(true)){
						claimObj.setIncidenceFlag(SHAConstants.ACCIDENT_FLAG);
						}
						else
						{
							claimObj.setIncidenceFlag(SHAConstants.DEATH_FLAG);
							
							if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
									&& ReferenceTable.RECEIVED_FROM_INSURED.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId()) && rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
								
								if(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()){
									saveNomineeDetails(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList(), reimbursement);
									if(rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getIsNomineeDeceased() != null
											&& !rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().getIsNomineeDeceased()){
										updateLegalHeir(rodDTO);
									}
								}else{
									rodDTO.setKey(reimbursement.getKey());
									legalHeirAndDocumentDetails(rodDTO);
								}
							
							}
						}
						
						if(null != incidenceDate){
							
							claimObj.setIncidenceDate(incidenceDate);
						}
										
				
					if(rodDTO.getClaimDTO() != null && rodDTO.getClaimDTO().getLatestPreauthKey() != null){
						claimObj.setLatestPreauthKey(rodDTO.getClaimDTO().getLatestPreauthKey());
					
					
		//			claimObj.setStatus(status1);
		//			claimObj.setStage(stage1);
					claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					claimObj.setModifiedBy(userNameForDB1);
					claimObj.setDataOfAdmission(reimbursement.getDateOfAdmission());
					claimObj.setDataOfDischarge(reimbursement.getDateOfDischarge());
					
					SectionDetailsTableDTO sectionDetailsDTO = rodDTO.getSectionDetailsDTO();
					if(sectionDetailsDTO != null) {
						claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO.getSection().getCommonValue() : null);
						claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO.getCover().getCommonValue() : null);
						claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO.getSubCover().getCommonValue() : null);
					}
					
					if((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && rodDTO.getDocumentDetails().getHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) || (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && rodDTO.getDocumentDetails().getPartialHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
						//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
					}
					
					
					if(null != rodDTO.getClaimDTO() && null != rodDTO.getClaimDTO().getNewIntimationDto() && null != rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()&&
							null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
							(ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
							(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase( rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){					
											
						claimObj.setGpaParentName(rodDTO.getDocumentDetails().getParentName());
						claimObj.setGpaParentDOB(rodDTO.getDocumentDetails().getDateOfBirth());				
						claimObj.setGpaParentAge(rodDTO.getDocumentDetails().getAge());
						claimObj.setGpaRiskName(rodDTO.getDocumentDetails().getRiskName());
						claimObj.setGpaRiskDOB(rodDTO.getDocumentDetails().getGpaRiskDOB());
						claimObj.setGpaRiskAge(rodDTO.getDocumentDetails().getGpaRiskAge());
						
						SelectValue gpaCategorySelvalue = new SelectValue();
						if(null != rodDTO.getDocumentDetails().getGpaCategory()){
							gpaCategorySelvalue = rodDTO.getDocumentDetails().getGpaCategory();
							String category = gpaCategorySelvalue.getValue();
							
							String[] splitCategory = category.split("-") ;
							String gpaCategory = splitCategory[0];
							
							claimObj.setGpaCategory(gpaCategory);
						}
						
					}
					
					//IMSSUPPOR-31684
					if(rodDTO.getDocumentDetails().getDateOfDeath() != null && !rodDTO.getDocumentDetails().getDateOfDeath().equals(""))
					{
						claimObj.setDeathDate(rodDTO.getDocumentDetails().getDateOfDeath());
					}
						
					entityManager.merge(claimObj);
					entityManager.flush();
					log.info("------Claim------>"+claimObj+"<------------");
					}
				}
				
				String sendTowhere = ReferenceTable.NORMAL;
				if(!isReconsideration && ! isQueryReplyReceived) {
					sendTowhere = loadByPass(reimbursement, claimObj, false, productType);
				}
				if(isQueryReplyReceived) {
					sendTowhere =  loadByPass(reimbursement, claimObj, true, productType);
				}
				if((claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) || (claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && !isHavingEarlierRod(claimObj.getKey()) )) {
		//			callRemainderProcess(rodDTO, reimbursement, isQueryReplyYes);
				}
				
				// Bypass will always go to BILLING .....
		//		if(sendTowhere.equalsIgnoreCase(ReferenceTable.DIRECT_TO_FINANCIAL)) {
		//			sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
		//		}
				
				
				/*BankMaster bankMaster = validateIFSCCode( rodDTO.getDocumentDetails().getIfscCode());
				if(null != bankMaster)
				{
					reimbursement.setBankId(bankMaster.getKey());
					if(null != reimbursement.getKey())
					{
						entityManager.merge(reimbursement);
						entityManager.flush();
					}
					submitTaskToBPM(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
				}*/
			

			
			// Bypass will always go to BILLING .....
			if(sendTowhere.equalsIgnoreCase(ReferenceTable.DIRECT_TO_FINANCIAL)) {
				sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
			}
			
			if(isReconsideration && claimObj.getClaimType() !=  null && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag())) {
				sendTowhere = ReferenceTable.DIRECT_TO_BILLING;
			}
			
			/***
			 * Code to upload letter generated in ROD stage. 
			 * This will happen only for cashless claim and docs received from
			 * hospital and first ROD.
			 * 
			 * **/
			if((!isQueryReplyReceived || !isReconsideration) && null != reimbursement && null != reimbursement.getClaim() && null != reimbursement.getClaim().getClaimType() && null != reimbursement.getClaim().getClaimType().getKey() 
					&& reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL)
					&& ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()))
			{
				DocumentGenerator docGen = new DocumentGenerator();
				ReportDto reportDto = new ReportDto();
				
				List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
				rodDTOList.add(rodDTO);		
				reportDto.setClaimId(rodDTO.getClaimDTO().getClaimId());
				reportDto.setBeanList(rodDTOList);
				final String filePath = docGen.generatePdfDocument("PaymentLetterToInsured", reportDto);	
				
				rodDTO.setDocFilePath(filePath);
				rodDTO.setDocType(SHAConstants.ROD_DOC_TYPE);
				
				
				/*
				if(null != rodDTO.getClaimDTO() && null != rodDTO.getClaimDTO().getNewIntimationDto() && null != rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()&&
						null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
						(ReferenceTable.GPA_PRODUCT_KEY.equals( rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
						(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase( rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){					
					
					
					if(null != rodDTO.getClaimDTO().getNewIntimationDto().getKey()){
						
						Intimation intimationObj = getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());
						if(null != intimationObj){
							
							intimationObj.setPaParentName(rodDTO.getDocumentDetails().getParentName());
							intimationObj.setPaParentDOB(rodDTO.getDocumentDetails().getDateOfBirth());
							intimationObj.setPaPatientName(rodDTO.getDocumentDetails().getRiskName());
							intimationObj.setPaParentAge(rodDTO.getDocumentDetails().getAge());
							
							if(null != intimationObj.getKey()){
							
								entityManager.merge(intimationObj);
								entityManager.flush();
							}else{
								entityManager.persist(intimationObj);
								entityManager.flush();
							}					
							
						}
						
						
					}
					
				}
				
				*/
								

				WeakHashMap dataMap = new WeakHashMap();
				dataMap.put("intimationNumber",reimbursement.getClaim().getIntimation().getIntimationId());
				Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
				if(null != objClaim)
				{
					dataMap.put("claimNumber",reimbursement.getClaim().getClaimId());
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
				dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
				dataMap.put("filePath", rodDTO.getDocFilePath());
				dataMap.put("docType", rodDTO.getDocType());
				dataMap.put("docSources", SHAConstants.ROD_DOC_SOURCE);
				dataMap.put("createdBy", rodDTO.getStrUserName());
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				if(null != docToken)
				{
					rodDTO.setDocToken(docToken);
					//rodDTO.setIsletterGeneratedInROD(true);
				}
				SHAUtils.setClearReferenceData(dataMap);
			
			}
					
			//paSubmitTaskToBPM(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
			submitTaskToDBProcedureForCreateROD(rodDTO, reimbursement,isQueryReplyYes, sendTowhere,isReconsideration,claimObj);
			
			
			return reimbursement;

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
		
		
		public List<PAAdditionalCovers> getAdditionalCoversByRodKey(Long rodKey)
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
		
		public List<PAOptionalCover> getOptionalCoversByRodKey(Long rodKey)
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
		
		
		 public PAAdditionalCovers getAdditionalCoversByKey(Long key)
		    {
		    	Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByKey");
		    	query = query.setParameter("key", key);
		    	List<PAAdditionalCovers> additionalCoversList = query.getResultList();
		    	if(null != additionalCoversList && !additionalCoversList.isEmpty())
		    	{
		    		return additionalCoversList.get(0);
		    	}
		    	return null;
		    }
		    
		    public PAOptionalCover getOptionalCoversByKey(Long key)
		    {
		    	Query query = entityManager.createNamedQuery("PAOptionalCover.findByKey");
		    	query = query.setParameter("key", key);
		    	List<PAOptionalCover> optionalCoverList = query.getResultList();
		    	if(null != optionalCoverList && !optionalCoverList.isEmpty())
		    	{
		    		return optionalCoverList.get(0);
		    	}
		    	return null;
		    }	
	/**
	 * There is already a method which would accept reimbursement key
	 * as input for calculating the claimed amt. But that involves a DB 
	 * hit to fetch doc ack object. But if in case we have doc ack
	 * directly, then that db hit is not required. Hence for those
	 * cases where doc ack is available, then we shall use the below
	 * method.
	 * 
	 * */
	public void updateCloseClaim(Long claimKey, Date closeDate){
		CloseClaim closeClaim = getCloseClaim(claimKey);
		closeClaim.setClosedDate(new Timestamp(closeDate.getTime()));
		entityManager.merge(closeClaim);
		entityManager.flush();
		entityManager.clear();
	}
	
	public Boolean getStatusOfFirstROD(Long claimKey,Reimbursement currentReimbursement)
	{
		Boolean isFirstRODApproved = false;
		
		Boolean isSkipValidation = false;
		
		DocAcknowledgement docAcknowLedgement = currentReimbursement.getDocAcknowLedgement();
		
		Claim claim = currentReimbursement.getClaim();
		if(null != claim)
		{
			Preauth preauth = getLatestPreauthForClaim(claim.getKey());
			if(null != preauth)
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey()) && ("F").equalsIgnoreCase(preauth.getEnhancementType()))
				{
					isSkipValidation = true;
				}
			}
		}
		
		if(!isSkipValidation)
		{
		if(docAcknowLedgement != null &&(("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalisationFlag())||
				("Y").equalsIgnoreCase(docAcknowLedgement.getPartialHospitalisationFlag()) || ("Y").equalsIgnoreCase(docAcknowLedgement.getLumpsumAmountFlag()))){
			
			return !isFirstRODApproved;
			
		}
		
		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())  {
			for (Reimbursement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
				if(!(currentReimbursement.getKey().equals((reimbursement.getKey())))) {
					if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y") 
							|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null &&
							reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) {
						if((ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey()) || ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey()))
								|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey()))
										|| (ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey()))
										|| (ReferenceTable.PAYMENT_REJECTED.equals(reimbursement.getStatus().getKey()))
										|| (ReferenceTable.FINANCIAL_SETTLED.equals(reimbursement.getStatus().getKey())) )
								{
							isFirstRODApproved  = true;
						}
						
					}
					
			     } else {
			    	 if(currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y")
			    			 || reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null &&
								reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) {
			    		 isFirstRODApproved = true;
			    	 }
			    	
			     }
			}
			
		} else {
			isFirstRODApproved = true;
		}
		}
		else
		{
			isFirstRODApproved = true;
		}
		return isFirstRODApproved;
		}
	
	
	
	
	private void submitPABillEntryTaskToDB(ReceiptOfDocumentsDTO rodDTO, Claim claimObj, Boolean shouldSkipZMR) {
	/*	SubmitBillEntryTask submitBillEntryTask = BPMClientContext
				.getSubmitBillEntryTask(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForBillEntry = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForBillEntry.getPayload();
		humanTaskForBillEntry.setOutcome("APPROVE");

		PaymentInfoType paymentInfoType = new PaymentInfoType();*/
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
	/*	if(rodDTO.getTotalClaimedAmount() != null){
			paymentInfoType.setClaimedAmount(rodDTO.getTotalClaimedAmount());
		}else{
			paymentInfoType.setClaimedAmount(0d);
		}*/
		if(rodDTO.getTotalClaimedAmount() != null){
			//paymentInfoType.setClaimedAmount(rodDTO.getTotalClaimedAmount());
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, rodDTO.getTotalClaimedAmount());
		}else{
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, 0d);
		}
		

		/*
		 * CustomerType customerType = new CustomerType();
		 * customerType.setTreatmentType(rodDTO.getTreatmentType());
		 */
		Long claimTypeId = rodDTO.getClaimDTO().getClaimType().getId();
		
		/**
		 * The below condition is added for cashless by pass case.
		 * If from via refer to bill entry a cashless bypass
		 * case comes to bill entry, then on submit of bill entry
		 * record should go to billing again. To enable this below 
		 * code is added . If bypass, then  bill classification values
		 * will not be set. Because based on bill classification record	
		 * will get moved to zmr queue. 
		 * 
		 * **/

		if(
				(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimTypeId) && ("Hospital").equalsIgnoreCase(rodDTO.getDocumentDetails().getDocumentReceivedFromValue()) && 
				((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
				&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
				 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
				 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
			) 
		{
			/*DocReceiptACKType docReceiptType1 = payloadBO.getDocReceiptACK();
			if(null != docReceiptType1)
			{
				docReceiptType1.setHospitalization(false);
				docReceiptType1.setPartialhospitalization(false);
				payloadBO.setDocReceiptACK(docReceiptType1);
			}
			
			ClaimRequestType requestType = payloadBO.getClaimRequest();
			if(null != requestType)
			{
				requestType.setReimbReqBy("BILLING");
				payloadBO.setClaimRequest(requestType);
			}*/
		}

		else
		{
		
			//DocReceiptACKType docReceiptType = payloadBO.getDocReceiptACK();
			/*
			 * docReceiptType.setHospitalization(true);
			 * docReceiptType.setPartialhospitalization(false);
			 * docReceiptType.setPrehospitalization(false);
			 * docReceiptType.setPosthospitalization(false);
			 * docReceiptType.setLumpsumamount(false);
			 * docReceiptType.setAddonbenefitshospcash(false);
			 * docReceiptType.setAddonbenefitspatientcare(false);
			 */
			if (null != rodDTO.getDocumentDetails().getHospitalization()
					&& rodDTO.getDocumentDetails().getHospitalization()) {
				//docReceiptType.setHospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
			} else {
				//docReceiptType.setHospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
			}
			if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
					&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
				//docReceiptType.setPartialhospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "Y");
			} else {
				//docReceiptType.setPartialhospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
			}
			if (null != rodDTO.getDocumentDetails().getPreHospitalization()
					&& rodDTO.getDocumentDetails().getPreHospitalization()) {
				//docReceiptType.setPrehospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "Y");
			} else {
				//docReceiptType.setPrehospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getPostHospitalization()
					&& rodDTO.getDocumentDetails().getPostHospitalization()) {
				//docReceiptType.setPosthospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "Y");
			} else {
				//docReceiptType.setPosthospitalization(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
					&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
				//docReceiptType.setLumpsumamount(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "Y");
			} else {
				//docReceiptType.setLumpsumamount(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
				//docReceiptType.setAddonbenefitshospcash(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "Y");
			} else {
				//docReceiptType.setAddonbenefitshospcash(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, "N");
			}
	
			if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
					&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
				//docReceiptType.setAddonbenefitspatientcare(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "Y");
			} else {
				//docReceiptType.setAddonbenefitspatientcare(false);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, "Y");
			}
			
			if (null != rodDTO.getDocumentDetails().getHospitalizationRepeat()
					&& rodDTO.getDocumentDetails().getHospitalizationRepeat()) {
				
				/**
				 * Instead of below line, add 
				 * docReceiptAck.setPartialhospitalization(true);
				 * */
				
				/***
				 * Below line added for hospitalization repeat flow.
				 * Repeat should follow normal flow from create rod, bill entry
				 * zmr. 
				 */
				
				//payloadBO.getDocReceiptACK().setHospitalization(true);
		//		payloadBO.getDocReceiptACK().setPartialhospitalization(true);
				wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "Y");
				//Don't comment the below line
//				payloadBO.getDocReceiptACK().setIsBillAvailable(true);
			}/* else {
				payloadBO.getDocReceiptACK().setHospitalization(false);
			}*/
	
			// Adding for BPMN filtering search. -- Hospital info type is set at
			// acknowledgement level itself.
			/*
			 * HospitalInfoType hospInfoType = new HospitalInfoType();
			 * hospInfoType.setHospitalType
			 * (rodDTO.getClaimDTO().getNewIntimationDto(
			 * ).getHospitalDto().getRegistedHospitals
			 * ().getHospitalType().getValue());
			 * hospInfoType.setNetworkHospitalType(
			 * rodDTO.getClaimDTO().getNewIntimationDto
			 * ().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
			 */
	
			// ProductInfoType pdtTypeInfo = new ProductInfoType();
	
			/*
			 * IntimationType intimationType = payloadBO.getIntimation();
			 * intimationType.setIntimationSource("Call Center");
			 */
	
			// Claim request type , intimation source are now set at acknowledgement
			// level itself.
			/*
			 * payloadBO.getClaimRequest().setClaimRequestType("All");
			 * payloadBO.getIntimation
			 * ().setIntimationSource(rodDTO.getClaimDTO().getNewIntimationDto
			 * ().getIntimationSource().getValue());
			 * payloadBO.setHospitalInfo(hospInfoType);
			 */
	
			// payloadBO.setProductInfo(productInfo);
	
			// docReceiptType.setAddonbenefitshospcash(false);
			// docReceiptType.setAddonbenefitspatientcare(false);
	
			// payloadBO.getClaimRequest().setResult("ACK");
	
			// payloadBO.getClaimRequest().setClientType("ACK");
			
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.BILL_ENTRY_COMPLETED);
			
		/*	if((! payloadBO.getDocReceiptACK().isHospitalization() && ! payloadBO.getDocReceiptACK().isPartialhospitalization()
					&& ! payloadBO.getDocReceiptACK().isAddonbenefitshospcash() && ! payloadBO.getDocReceiptACK().isAddonbenefitspatientcare()
					&& ! payloadBO.getDocReceiptACK().isLumpsumamount())){

				   payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
				   docReceiptType.setHospitalization(true);
			}
	
			payloadBO.setDocReceiptACK(docReceiptType);
		}
		// payloadBO.setProductInfo(productInfo);
		payloadBO.setPaymentInfo(paymentInfoType);*/
		
		if(claimObj != null && claimObj.getStage() != null){
		/*	ClaimRequestType requestType = payloadBO.getClaimRequest();
			if(requestType != null){
				*//**
				 * The below line is added for refer to bill entry process.
				 * For cashless claim which undergoes bypass, once after coming to 
				 * bill entry it should move to BILLING screen. For that the below change
				 * is done.
				 * *//*
				*//**
				 * The below code is not required as we dont have a bypass flow
				 * from bill entry.
				 * *//*
				if(null != requestType.isIsReconsider() && !requestType.isIsReconsider())
				{
					requestType.setReimbReqBy("BILLING");
				}
				*//**
				 * If its a reconsideration scenario, by default the
				 * instance should move to FA.
				 * *//*
				else
				{
					requestType.setReimbReqBy("FA");

				}
				if(null != requestType.isIsReconsider() && requestType.isIsReconsider() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
				{
					requestType.setOption(SHAConstants.RECONSIDERATION_PAYMENT_OPTION);
				}
				else
				{
					requestType.setOption(claimObj.getStage().getStageName());
				}
				payloadBO.setClaimRequest(requestType);*/
			}
		}
		if(claimObj.getStage() != null && claimObj.getStage().getKey() != null){
			Status status = entityManager.find(Status.class, claimObj.getStatus().getKey());
		//	ClassificationType classification = payloadBO.getClassification();
			/*if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
				classification.setSource(status.getProcessValue());
			}
			
			payloadBO.setClassification(classification);*/
		}
		if((rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			shouldSkipZMR = false;
			/*payloadBO.getClaimRequest().setReimbReqBy("BILLING");
			payloadBO.getDocReceiptACK().setHospitalization(false);
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);*/
			wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, "N");
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, "N");
		}
		if(shouldSkipZMR) {
			//payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);
			wrkFlowMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS, "Y");
		}
		
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
		/***
		 * The below code is added for lumpsum change implemented for medicare
		 * and criticare product, where for lumpsum product , zmr should be
		 * skipped and it should  move to medical.
		 * */
		if(((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
		&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
		 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
		 && (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))
		{
			/**
			 * As per bpmn flow, to skip ZMR , either hospitalization 
			 * or partial hospitalization needs to be set as true.
			 * Hence if lumpsum alone is enabled, then zmr to be skipped.
			 * For that , internally we're setting hospitalization
			 * and partial hospitalization to true. This is only for flow purpose.
			 * */
//			DocReceiptACKType docReceipt = payloadBO.getDocReceiptACK();
			/*if(null != docReceipt)
			{
				docReceipt.setHospitalization(true);
				docReceipt.setPartialhospitalization(true);
				payloadBO.setDocReceiptACK(docReceipt);
			}
			payloadBO.getClaimRequest().setClientType(SHAConstants.MEDICAL);*/
		}

		
		if(claimObj.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
		{
			
			if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
			{
		//	reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			}					
			else
			{
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			//reimbursement.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
			//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
			}

		if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
				("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()) || ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag())) //---need to implement partialhospitallization
			{
			//reimbursement.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			}
							
		
		}
		if(claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY))
		{
			if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED) && 
					("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) //---need to implement partialhospitallization
			{
				//docAck.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				//claimObj.setProcessClaimType(SHAConstants.PA_CASHLESS_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			}	
			else
			{
			//docAck.setProcessClaimType(SHAConstants.PA_LOB_TYPE);				
			//claimObj.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
				wrkFlowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			}
		}

	
		if(wrkFlowMap.get(SHAConstants.RECORD_TYPE) != null && ((String)wrkFlowMap.get(SHAConstants.RECORD_TYPE)).equalsIgnoreCase(SHAConstants.QUERY_REPLY) 
				&& (wrkFlowMap.get(SHAConstants.LOB) != null && ((String)wrkFlowMap.get(SHAConstants.LOB)).equals(SHAConstants.PA_LOB))
				 && ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS.equals(rodDTO.getStatusKey())){

			wrkFlowMap.put(SHAConstants.OUTCOME, "CRODQREN");
		}
		
		if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getDocumentType().getId() 
				&& (ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(rodDTO.getDocumentDetails().getDocumentType().getId())
				&& !(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS.equals(rodDTO.getStatusKey())))
		{
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_BILL_ENTRY_PAYMENT_QUERY);
		}
		
//		ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
//		if(processActor == null){
//			/*processActor = new ProcessActorInfoType();
//			processActor.setEscalatedByUser("");
//			payloadBO.setProcessActorInfo(processActor);*/
//		}else if(processActor != null && processActor.getEscalatedByUser() == null){
//			/*processActor.setEscalatedByUser("");
//			payloadBO.setProcessActorInfo(processActor);*/
//		}

	//	humanTaskForBillEntry.setPayload(payloadBO);
		
		
		try{
		//submitBillEntryTask.execute(rodDTO.getStrUserName(),
				//humanTaskForBillEntry);
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			//dbCalService.initiateTaskProcedure(objArrayForSubmit);
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			

			if(wrkFlowMap.get(SHAConstants.RECORD_TYPE) != null && ((String)wrkFlowMap.get(SHAConstants.RECORD_TYPE)).equalsIgnoreCase(SHAConstants.QUERY_REPLY)){
				
				Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
				dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
			
			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
			
			try {
				log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR BILL_ENTRY ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
				/*BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForBillEntry.getNumber(), SHAConstants.SYS_RELEASE);
				submitBillEntryTask.execute("claimshead", humanTaskForBillEntry);*/

			} catch(Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
			}
		}
	}
	
	 public void submitCloseClaim(CloseClaimPageDTO bean){
	    	
	    	CloseClaim closeClaim = new CloseClaim();
	    	
	        Claim claim = getClaimByClaimKey(bean.getClaimKey());
	        
	    	if(bean.getReasonId() != null){
	    	MastersValue closeReason = new MastersValue();
	    	closeReason.setKey(bean.getReasonId().getId());
	    	closeReason.setValue(bean.getReasonId().getValue());
	    	closeClaim.setClosingReasonId(closeReason);
	    	
	    	}
	    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
	    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
	    	closeClaim.setCreatedBy(bean.getUserName());
	    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	    	closeClaim.setCloseType("C");
	    	closeClaim.setClaim(claim);
	    	closeClaim.setStage(claim.getStage());
	    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
	    	
	    	Status status = new Status();
	    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
	    	
	    	closeClaim.setStatus(status);
	    	
	    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
	        entityManager.persist(closeClaim);
	        entityManager.flush();

	        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
	        	
	        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
	        	
	        	if(preauthList != null &&  preauthList.size() == 1){
	        		Preauth lastPreauth = preauthList.get(0);
	        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
								&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
	        		     Stage preauthStage = new Stage();
	        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
	        		     Status preauthStatus = new Status();
	        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//	        		     lastPreauth.setStage(preauthStage);
	        		     lastPreauth.setStatus(preauthStatus);
	        		     lastPreauth.setModifiedBy(bean.getUserName());
	        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	        		     entityManager.merge(lastPreauth);
	        		     entityManager.flush();
	        		     
	        		}
	        	}
	        }
	        
	        claim.setStatus(status);
	        claim.setModifiedBy(bean.getUserName());
	        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	        claim.setCurrentProvisionAmount(0d);
	        //IMSSUPPOR-28254
	        if(claim.getPaHospExpenseAmt() != null){
	        	claim.setPaHospExpenseAmt(0d);
	        }
	        claim.setCloseDate(new Timestamp(System.currentTimeMillis()));
	        entityManager.merge(claim);
	        entityManager.flush();
	        
	        List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
	        for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
				DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
				entityManager.persist(documentDetails);
				entityManager.flush();
			}

	    	//need to implement provision amount;
	    	
//	    	PayloadBOType payloadType = new PayloadBOType();
//			IntimationType intimationType = new IntimationType();
//			intimationType.setIntimationNumber(bean.getIntimationNumber());
//			payloadType.setIntimation(intimationType);
//			
//			
//			CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//			
//			PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//			
//			List<HumanTask> humanTasks = tasks.getHumanTasks();
//			
//			
//			
//			for (HumanTask humanTask : humanTasks) {
//				
//				if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//						&& humanTask.getPayload().getRod().getKey() != null){
//					
//					Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//					
//					Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
	//
//				}
//				
//				SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
	//
//			    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//			}

	    	
	    }
	 
	 public UploadDocumentCloseClaimDTO uploadDocumentForCloseClaim(CloseClaimPageDTO bean){
	    	
	    	DocumentDetails documentDetails = new DocumentDetails();
	    	
	    	documentDetails.setIntimationNumber(bean.getIntimationNumber());
	    	documentDetails.setClaimNumber(bean.getClaimNumber());
	    	documentDetails.setFileName(bean.getFileName());
	    	documentDetails.setDocumentType(bean.getFileType());
	    	documentDetails.setReimbursementNumber(bean.getReimbursmentNumber());
	    	documentDetails.setCashlessNumber(bean.getCashlessNumber());
	    	if(bean.getFileToken() != null){
	    		documentDetails.setDocumentToken(Long.valueOf(bean.getFileToken()));
	    	}
	    	documentDetails.setDocumentSource("CLOSE_ClAIM");
	    	documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
	    	documentDetails.setCreatedBy(bean.getUserName());
	    	documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	    	documentDetails.setDeletedFlag("N");
	    	
//	    	entityManager.persist(documentDetails);
//	    	entityManager.flush();
	    	
	    	UploadDocumentCloseClaimDTO dto = new UploadDocumentCloseClaimDTO();
	    	dto.setFileName(bean.getFileName());
	    	dto.setStrDateAndTime(SHAUtils.formateDateForHistory(documentDetails.getCreatedDate()));
	    	dto.setReferenceNo(bean.getReferenceNo());
	    	dto.setDocumentDetails(documentDetails);
	    	
	    	return dto;
	    	
	    }
	 
	 
	 public PAUploadDocumentCloseClaimDTO uploadDocumentForPACloseClaim(PACloseClaimPageDTO bean){
	    	
	    	DocumentDetails documentDetails = new DocumentDetails();
	    	
	    	documentDetails.setIntimationNumber(bean.getIntimationNumber());
	    	documentDetails.setClaimNumber(bean.getClaimNumber());
	    	documentDetails.setFileName(bean.getFileName());
	    	documentDetails.setDocumentType(bean.getFileType());
	    	documentDetails.setReimbursementNumber(bean.getReimbursmentNumber());
	    	documentDetails.setCashlessNumber(bean.getCashlessNumber());
	    	if(bean.getFileToken() != null){
	    		documentDetails.setDocumentToken(Long.valueOf(bean.getFileToken()));
	    	}
	    	documentDetails.setDocumentSource("CLOSE_ClAIM");
	    	documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
	    	documentDetails.setCreatedBy(bean.getUserName());
	    	documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	    	documentDetails.setDeletedFlag("N");
	    	
//	    	entityManager.persist(documentDetails);
//	    	entityManager.flush();
	    	
	    	PAUploadDocumentCloseClaimDTO dto = new PAUploadDocumentCloseClaimDTO();
	    	dto.setFileName(bean.getFileName());
	    	dto.setStrDateAndTime(SHAUtils.formateDateForHistory(documentDetails.getCreatedDate()));
	    	dto.setReferenceNo(bean.getReferenceNo());
	    	dto.setDocumentDetails(documentDetails);
	    	
	    	return dto;
	    	
	    }
	    
	 
	 public void submitPACloseClaim(PACloseClaimPageDTO bean){
	    	
	    	CloseClaim closeClaim = new CloseClaim();
	    	
	        Claim claim = getClaimByClaimKey(bean.getClaimKey());
	        
	    	if(bean.getReasonId() != null){
	    	MastersValue closeReason = new MastersValue();
	    	closeReason.setKey(bean.getReasonId().getId());
	    	closeReason.setValue(bean.getReasonId().getValue());
	    	closeClaim.setClosingReasonId(closeReason);
	    	
	    	}
	    	closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
	    	closeClaim.setClosingRemarks(bean.getCloseRemarks());
	    	closeClaim.setCreatedBy(bean.getUserName());
	    	closeClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	    	closeClaim.setCloseType("C");
	    	closeClaim.setClaim(claim);
	    	closeClaim.setStage(claim.getStage());
	    	closeClaim.setClosedProvisionAmt(bean.getClosedProvisionAmt());
	    	
	    	Status status = new Status();
	    	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
	    	
	    	closeClaim.setStatus(status);
	    	
	    	closeClaim.setPolicy(claim.getIntimation().getPolicy());
	        entityManager.persist(closeClaim);
	        entityManager.flush();

	        if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
	        	
	        	List<Preauth> preauthList = getPreauthListByClaimKey(claim.getKey());
	        	
	        	if(preauthList != null &&  preauthList.size() == 1){
	        		Preauth lastPreauth = preauthList.get(0);
	        		if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
								&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
	        		     Stage preauthStage = new Stage();
	        		     preauthStage.setKey(ReferenceTable.PREAUTH_STAGE);
	        		     Status preauthStatus = new Status();
	        		     preauthStatus.setKey(ReferenceTable.PREAUTH_CLOSED_STATUS);
//	        		     lastPreauth.setStage(preauthStage);
	        		     lastPreauth.setStatus(preauthStatus);
	        		     lastPreauth.setModifiedBy(bean.getUserName());
	        		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	        		     entityManager.merge(lastPreauth);
	        		     entityManager.flush();
	        		     
	        		}
	        	}
	        }
	        
	        claim.setStatus(status);
	        claim.setModifiedBy(bean.getUserName());
	        claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	        claim.setCurrentProvisionAmount(0d);
	        //IMSSUPPOR-28254
	        claim.setPaHospExpenseAmt(0d);
	        entityManager.merge(claim);
	        entityManager.flush();
	        
	        List<PAUploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
	        for (PAUploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
				DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
				entityManager.persist(documentDetails);
				entityManager.flush();
			}

	    	//need to implement provision amount;
	    	
//	    	PayloadBOType payloadType = new PayloadBOType();
//			IntimationType intimationType = new IntimationType();
//			intimationType.setIntimationNumber(bean.getIntimationNumber());
//			payloadType.setIntimation(intimationType);
//			
//			
//			CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//			
//			PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
//			
//			List<HumanTask> humanTasks = tasks.getHumanTasks();
//			
//			
//			
//			for (HumanTask humanTask : humanTasks) {
//				
//				if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//						&& humanTask.getPayload().getRod().getKey() != null){
//					
//					Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//					
//					Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
	//
//				}
//				
//				SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
	//
//			    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//			}
	/*private List<Intimation> getInsuredWiseClaimList(final Intimation intimation) {
		List<Intimation> intimationlist =  getIntimationListByInsured(String.valueOf(intimation.getInsured()
						.getInsuredId()));
		String PreviousInsuredId = getPreviousInsuredNumber(intimation);
		intimationlist.remove(intimation);
		if (PreviousInsuredId != null) {
			List<Intimation> previousIntimationlist = getIntimationListByInsured(PreviousInsuredId);
			if (previousIntimationlist.size() != 0) {
				intimationlist.addAll(previousIntimationlist);
			}

		}
		
		return intimationlist;
	}*/
	
	
	
	/*private String getPreviousInsuredNumber(Intimation a_intimation) {

		TmpInsured tmpInsured = insuredService.getInsured(a_intimation
				.getPolicy().getInsuredId());
		Insured insured = getCLSInsured(String.valueOf(a_intimation
				.getInsured().getInsuredId()));
		
		// TmpInsured tmpInsured = insuredService.getInsured(a_intimation
		// .getPolicy().getPolicyNumber(), a_intimation.getPolicy()
		// .getInsuredFirstName(), a_intimation.getPolicy()
		// .getInsuredDob());
		if (insured != null)
			return insured.getRelationshipwithInsuredId().getValue();
		return null;
	}*/



	}
	
	
	@SuppressWarnings({ "unchecked", })
	public List<Intimation> getIntimationListByInsured(String insuredId) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager.createNamedQuery(
				"Intimation.findByInsuredId");
		query = query.setParameter("insuredId",
				Long.valueOf(insuredId));
		List<Intimation> intimationList = (List<Intimation>) query
				.getResultList();
		return intimationList;
	}
	/**
	 * There is already a method which would accept reimbursement key
	 * as input for calculating the claimed amt. But that involves a DB 
	 * hit to fetch doc ack object. But if in case we have doc ack
	 * directly, then that db hit is not required. Hence for those
	 * cases where doc ack is available, then we shall use the below
	 * method.
	 * 
	 * */
	private Double getClaimedAmt(DocAcknowledgement docAck)
	{
		Double claimedAmt = 0d;
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
		if(null != docAck.getProdHospBenefitClaimedAmount())
		{
			claimedAmt += docAck.getProdHospBenefitClaimedAmount();
		}
		return claimedAmt;
	}
	
	public List<RODDocumentSummary> getRODDocumentSummaryByDocToken(String documentToken){
		Query query = entityManager.createNamedQuery("RODDocumentSummary.findByDocToken");
		if(null != documentToken)
		{
			query = query.setParameter("documentToken", documentToken);
			List<RODDocumentSummary> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				return documentDetailsList;
			}
		}
		return null;
	}
	
	
	public List<GpaBenefitDetails> getGpaBenefitDetails(Long policyKey)
	{
		Query query = entityManager.createNamedQuery("GpaBenefitDetails.findByPolicy");
		query.setParameter("policyKey", policyKey);
		List<GpaBenefitDetails> gpaDetailsList = (List<GpaBenefitDetails>) query.getResultList();
		
		if(gpaDetailsList != null && ! gpaDetailsList.isEmpty()){
			
			return gpaDetailsList;
		}
		return null;
	}
	private MasPAClaimBenefitsCover getPABenefitsMasterValueBySubCoverKey(Long subCoverKey)
	{
		Query query = entityManager.createNamedQuery("MasPAClaimBenefitsCover.findBySubCoverKey");
		query = query.setParameter("subCoverKey", subCoverKey);
		List<MasPAClaimBenefitsCover> masterValueList = (List<MasPAClaimBenefitsCover>)query.getResultList();
		if(null != masterValueList && !masterValueList.isEmpty())
		{
			return masterValueList.get(0);
		}
		return null;
	}	
	
		
		public DocumentDetails saveReceivedPhysicalDocumens(UploadDocumentDTO uploadDto){
			List<UploadDocumentDTO> uploadDocsDTO = uploadDto.getUploadDocsList();
			String userName = SHAUtils.getUserNameForDB(uploadDto.getUsername());
		
		if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				if (null != uploadDocumentDTO.getFileType()	&& !("").equalsIgnoreCase(uploadDocumentDTO.getFileType().getValue())) {
					
					DocumentDetails documentDetails =  new DocumentDetails();
									
					documentDetails.setIntimationNumber(uploadDto.getIntimationNo());
					documentDetails.setClaimNumber(uploadDto.getClaimNo());
					if(null != uploadDocumentDTO.getReferenceNoValue() && uploadDocumentDTO.getReferenceNoValue().startsWith("ROD"))
					{
						documentDetails.setReimbursementNumber(uploadDocumentDTO.getReferenceNoValue());					
					}
					else
					{
						documentDetails.setCashlessNumber(uploadDocumentDTO.getReferenceNoValue());	
					}
					documentDetails.setFileName(uploadDocumentDTO.getFileName());
					documentDetails.setDocumentType(uploadDocumentDTO.getFileTypeValue());
					if(null != uploadDocumentDTO.getDmsDocToken()){
					documentDetails.setDocumentToken(Long.parseLong(uploadDocumentDTO.getDmsDocToken()));
					}
					documentDetails.setDocumentSource(SHAConstants.POST_PROCESS); 
					
					documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
					documentDetails.setDocAcknowledgementDate(new Timestamp(System.currentTimeMillis()));
					documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					documentDetails.setCreatedBy(userName);
					documentDetails.setKey(uploadDocumentDTO.getDocDetailsKey());
					if(uploadDocumentDTO.getIsReceived()){
					documentDetails.setPhysicalDocumentStatus(SHAConstants.PHY_DOC_RECEIVED);
					}
					if(uploadDocumentDTO.getIsIgnored()){
						documentDetails.setPhysicalDocumentStatus(SHAConstants.PHY_DOC_IGNORE);
					}
					
					if(uploadDocumentDTO.getDocumentType() != null){
						documentDetails.setDocumentTypeId(uploadDocumentDTO.getDocumentType().getId());
					}
					if(uploadDocumentDTO.getDocReceivedDate() != null){
						documentDetails.setDocReceivedDate(uploadDocumentDTO.getDocReceivedDate());
					}
					
					
					if(null!= documentDetails.getKey()){
						
						entityManager.merge(documentDetails); 
						entityManager.flush();
					}
					else 
					{	
						entityManager.persist(documentDetails);
						entityManager.flush();
					}
					entityManager.clear();
					
					List<UploadDocumentDTO> deletedDocsList = uploadDto.getDeletedDocumentList();
					if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
						for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {
							if(null != uploadDocumentDTO2.getDocDetailsKey())
							{
								DocumentDetails documentDetailsObj = getDocumentDetailsByKey(uploadDocumentDTO2.getDocDetailsKey());
								documentDetailsObj.setDeletedFlag("Y");
								if (null != uploadDocumentDTO2.getDocDetailsKey()) {
									//documentDetailsObj.setModifiedBy("");
									//documentDetailsObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
									entityManager.merge(documentDetailsObj);
									entityManager.flush();
									entityManager.clear();
									log.info("------RODDocumentSummary------>"+documentDetailsObj+"<------------");
								} /*
								 * else { entityManager.persist(rodSummary);
								 * entityManager.flush(); entityManager.refresh(rodSummary);
								 * }
								 */
		
							}
						}
					}
					
					
				}
			}
		}
			
			return null;
			
		}
		

	
	
	private PABenefitsCovers getBenefitCoversByRodKey(Long rodKey)
	{
		List<PABenefitsCovers> benefitCovers; 
		Query query = entityManager
				.createNamedQuery("PABenefitsCovers.findByRodKey").setParameter(
						"rodKey", rodKey);
		try{
			
			benefitCovers = (List<PABenefitsCovers>) query.getResultList();
			if(benefitCovers != null && ! benefitCovers.isEmpty()){
				return benefitCovers.get(0);
			}
			return null;
		}
		catch(Exception e)
		{
			return null;
		}		
			
	}
	
public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetailsList(Long cashlessKey){
		
		Query query = entityManager.createNamedQuery("UpdateOtherClaimDetails.findByCashlessKey");
		query.setParameter("cashlessKey", cashlessKey);
		
		List<UpdateOtherClaimDetails> resultList = (List<UpdateOtherClaimDetails>) query.getResultList();
		
		return resultList;
		
	}
	
	
	public void updateUnnamedRiskCategory(UnnamedRiskDetailsPageDTO unnamedRiskPageDTO){
		
	}
	
public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetailsDTO(Long cashlessKey){
		
		List<UpdateOtherClaimDetails> updateOtherClaimDetailsList = getUpdateOtherClaimDetailsList(cashlessKey);
		List<UpdateOtherClaimDetailDTO> resultList = PreauthMapper.getInstance().getUpdateOtherClaimDetailsDTO(updateOtherClaimDetailsList);
		
		return resultList;
	}

public DocumentDetails getReimbursementQueryDocumentDetailsData(String intimationNumber,String rodNumber,String docType)
{
	Query query = entityManager.createNamedQuery("DocumentDetails.findQueryLetterByRodNumber");
	
	query = query.setParameter("reimbursementNumber", rodNumber);
	docType = docType.toLowerCase();
	query = query.setParameter("ReimbursementQueryLetter", "%"+docType+"%");
	
	List<DocumentDetails> documentDetailsList  = query.getResultList();
	
	if(null != documentDetailsList && !documentDetailsList.isEmpty())
	{
		return documentDetailsList.get(0);
		
	}
	return null;
	
	
	//documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
	

	/*if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
	{
		for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
			if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
			{
				documentDetails.setFileName(documentDetails.getGalaxyFileName());
			}
		}
	}
	
	return documentDetailsDTOList;*/
}
public DocumentDetails getReimbursementRejectionDocumentDetailsData(String intimationNumber,String rodNumber,String docType)
{
	Query query = entityManager.createNamedQuery("DocumentDetails.findRejectionLetterByRodNumber");

	query = query.setParameter("reimbursementNumber", rodNumber);
	docType = docType.toLowerCase();
	query = query.setParameter("ReimbursementRejectionLetter", "%"+docType+"%");
	
	List<DocumentDetails> documentDetailsList  = query.getResultList();
	
	if(null != documentDetailsList && !documentDetailsList.isEmpty())
	{
		return documentDetailsList.get(0);
		
	}
	return null;
	
	
	//documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
	

	/*if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
	{
		for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
			if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
			{
				documentDetails.setFileName(documentDetails.getGalaxyFileName());
			}
		}
	}
	
	return documentDetailsDTOList;*/
}

private Intimation setProcessingCpuCodeBasedOnLimit(Intimation objIntimation,
		Long estarFaxAmt,Claim claimObj) {
	TmpCPUCode existingCpuCode = objIntimation.getCpuCode();
	MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(objIntimation.getPolicy().getProduct().getKey());
	if(gmcRoutingProduct != null){
		if(objIntimation.getPolicy().getHomeOfficeCode() != null) {
			OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(objIntimation.getPolicy().getHomeOfficeCode());
			if(branchOffice != null && branchOffice.getCpuCode() != null ){
				String officeCpuCode = branchOffice.getCpuCode();
				Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
				if(processingCpuCode != null){
					TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
					objIntimation.setCpuCode(masCpuCode);
					entityManager.merge(objIntimation);
					entityManager.flush();
				}else{
					Boolean paayasPolicyDetails = getPaayasPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
					TmpCPUCode masCpuCode = null;
					//added foe CR GMC CPU Routing GLX2020075
					masCpuCode = objIntimation.getCpuCode();
					if(paayasPolicyDetails){
						 masCpuCode = getCpuDetails(ReferenceTable.PAAYAS_CPU_CODE);
					}else{
						
						Boolean jioPolicy = getJioPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
						if(jioPolicy){
							 masCpuCode = getCpuDetails(ReferenceTable.JIO_CPU_CODE);
						}
						//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//						else{
//							masCpuCode = getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//						}
						
						
					}
					//added for CR GMC CPU Routing GLX2020075 only for kerala CPU routing
					if(branchOffice != null && branchOffice.getCpuCode() != null  &&
							claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) &&
							branchOffice.getCpuCode().equals(ReferenceTable.KERALA_CPU_CODE)){
						masCpuCode = getMasCpuCode(Long.valueOf(branchOffice.getCpuCode()));
						if(masCpuCode != null && masCpuCode.getGmcRoutingCpuCode() !=null){
							masCpuCode =  getMasCpuCode( masCpuCode.getGmcRoutingCpuCode());
						}
					}
					Long tataPolicyCpuCode = getTataPolicy(objIntimation.getPolicy().getPolicyNumber());
					if(tataPolicyCpuCode != null){
						 masCpuCode = getMasCpuCode(tataPolicyCpuCode);
					}
					
					Boolean kotakPolicy = getKotakPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
					if(kotakPolicy){
						Long kotakCpuCode = getKotakProcessingCpuCode(estarFaxAmt, ReferenceTable.KOTAK_PROCESSING_CPU_CODE, SHAConstants.PROCESSING_CPU_CODE_GMC);
						 masCpuCode = getMasCpuCode(kotakCpuCode);
					}
					
					objIntimation.setCpuCode(masCpuCode);
					entityManager.merge(objIntimation);
					entityManager.flush();
					//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
				
				}
				
			}
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
	String gpaPolicyDetails = getGpaPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
	if(gpaPolicyDetails != null){
		TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
		objIntimation.setCpuCode(masCpuCode);
		entityManager.merge(objIntimation);
		entityManager.flush();
	}
	
	return objIntimation;
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


public Long getProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
	
	MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
	if(masCpuLimit != null){
			if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_GMC)){
				if(estarFaxAmt < masCpuLimit.getCpuLimit()){
					return masCpuLimit.getProcessCpuCode();
				}else{
					return null;
				}
			} else if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_RETAIL)){
					if(estarFaxAmt > masCpuLimit.getCpuLimit()){
						return masCpuLimit.getProcessCpuCode();
					}else{
						return null;
					}
			}
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


public Reimbursement isAnyRodActive(Long claimKey) {
	List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
	if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
		for (Reimbursement reimbursement : previousRODByClaimKey) {
			if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
//					&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					return reimbursement;					
			}
		}
	}

	return null;
}

private PreauthDTO setReimbursmentTOPreauthDTO(
		ZonalMedicalReviewMapper reimbursementMapper, Claim claimByKey,
		Reimbursement reimbursement, PreauthDTO preauthDTO,
		Boolean isEnabled, String screenName) {
	
	Date mapperStartDate = new Date();
	log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR CLAIM MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperStartDate);
	
	if (claimByKey != null && claimByKey.getIntimation() !=null 
			&&  claimByKey.getIntimation().getInsured() !=null && claimByKey.getIntimation().getInsured().getInsuredId() !=null) {
		CoorporateBuffer buffer = masterService.getBufferbyinsuerdNo(claimByKey.getIntimation().getInsured().getInsuredId());
		if(buffer !=null){
			preauthDTO.getPreauthDataExtractionDetails().setCorpBuffer(true);
			preauthDTO.getPreauthDataExtractionDetails().setBufferType(buffer.getBufferType());			
			if(buffer.getAllocatedAmount() != null){
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAllocatedClaim(buffer.getAllocatedAmount().intValue());
			}
		}
	}

	preauthDTO.setKey(reimbursement.getKey());

	if (null != reimbursement.getCoordinatorFlag()
			&& reimbursement.getCoordinatorFlag().equalsIgnoreCase("y")) {

		CoordinatorDTO coordinatorDTO = reimbursementMapper
				.getCoordinatorDTO(
						findCoordinatorByClaimKey(reimbursement.getClaim()
								.getKey()));
		coordinatorDTO.setRefertoCoordinator(true);
		preauthDTO.setCoordinatorDetails(coordinatorDTO);
	}

	UpdateHospital updateHospitalByReimbursementKey = updateHospitalByReimbursementKey(reimbursement.getKey());
	if (updateHospitalByReimbursementKey != null) {
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDTO = reimbursementMapper
				.getUpdateHospitalDTO(updateHospitalByReimbursementKey);
		preauthDTO.getPreauthDataExtractionDetails()
				.setUpdateHospitalDetails(updateHospitalDTO);
	}

	PreviousClaimedHistory claimedHistoryByTransactionKey = getClaimedHistoryByTransactionKey(reimbursement.getKey());
	if (claimedHistoryByTransactionKey != null) {
		preauthDTO.getPreauthDataExtractionDetails()
				.setCoveredPreviousClaim(true);
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setOtherClaimDetails(
						reimbursementMapper
								.getClaimedHistoryDTO(claimedHistoryByTransactionKey));

		List<PreviousClaimedHospitalization> claimedHospitalizationByClaimedHistoryKey = getClaimedHospitalizationByClaimedHistoryKey(claimedHistoryByTransactionKey
						.getKey());
		List<OtherClaimDiagnosisDTO> otherClaimDiagnosisDTOList = reimbursementMapper
				.getOtherClaimDiagnosisDTOList(claimedHospitalizationByClaimedHistoryKey);

		preauthDTO.getPreauthDataExtractionDetails()
				.setOtherClaimDetailsList(otherClaimDiagnosisDTOList);
	}

	List<SpecialityDTO> specialityDTOList = reimbursementMapper
			.getSpecialityDTOList(findSpecialityByClaimKey(reimbursement.getClaim()
							.getKey()));
	for (SpecialityDTO specialityDTO : specialityDTOList) {
		specialityDTO.setEnableOrDisable(isEnabled);
	}
	preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
			specialityDTOList);


	List<PedValidation> findPedValidationByPreauthKey = findPedValidationByPreauthKey(reimbursement.getKey());

	List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = reimbursementMapper
			.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

	mapperStartDate = new Date();
	log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR DB PROCEDURE CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperStartDate);
	
	// Fix for issue 732 starts.
	DBCalculationService dbCalculationService = new DBCalculationService();

	Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
			preauthDTO.getNewIntimationDTO().getInsuredPatient()
					.getInsuredId().toString(), preauthDTO.getPolicyDto()
					.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
	preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
	
	if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
			.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
			|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
					.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
		// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
		// ,claimByKey.getClaimId(), preauthDTO));
	}
	
	String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
	
	/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
	if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
			&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
					SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
					|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))) {
		policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
	}
	
	Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
			preauthDTO.getPolicyDto().getProduct().getKey(),
			insuredSumInsured, preauthDTO.getNewIntimationDTO()
					.getInsuredPatient().getInsuredAge(), preauthDTO.getPreauthDataExtractionDetails().getSection(), policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
	
	preauthDTO.setSublimitFunMap(sublimitFunObjMap);

	List<ProcedureDTO> procedureMainDTOList = reimbursementMapper
			.getProcedureMainDTOList(findProcedureByPreauthKey(reimbursement.getKey()));
	for (ProcedureDTO procedureDTO : procedureMainDTOList) {
		procedureDTO.setEnableOrDisable(isEnabled);
		/*
		 * This is for Reverse Allocation . 
		 * if any modifictation between Approved amount or 
		 * Net Approved Amount then we have to set reverse allocation into PreauthDTO.
		 * */
		
		if(procedureDTO.getApprovedAmount() != null && procedureDTO.getNetApprovedAmount() != null && !procedureDTO.getApprovedAmount().equals(procedureDTO.getNetApprovedAmount())) {
			preauthDTO.setIsReverseAllocation(true);
		}
		if (procedureDTO.getSublimitName() != null) {
			SublimitFunObject objSublimitFun = sublimitFunObjMap
					.get(procedureDTO
							.getSublimitName().getLimitId());
			if(objSublimitFun != null) {
				procedureDTO.setSublimitName(objSublimitFun);
				procedureDTO.setSublimitDesc(objSublimitFun.getDescription());
				procedureDTO.setSublimitAmount(String.valueOf(objSublimitFun.getAmount().intValue()));
			}
		}
		if (preauthDTO.getIsPostHospitalization()
				&& !preauthDTO.getHospitalizaionFlag()) {
			procedureDTO.setAmountConsideredAmount(0d);
			procedureDTO.setCopayAmount(0d);
			procedureDTO.setNetAmount(0d);
			procedureDTO.setApprovedAmount(0d);
			procedureDTO.setIsAmbChargeApplicable(false);
			procedureDTO.setAmbulanceCharge(0);
			procedureDTO.setAmtWithAmbulanceCharge(0);
		}

	}

	preauthDTO.getPreauthMedicalProcessingDetails()
			.setProcedureExclusionCheckTableList(procedureMainDTOList);
	
	

	for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
		pedValidationTableDTO.setEnableOrDisable(isEnabled);
		/*
		 * This is for Reverse Allocation . 
		 * if any modifictation between Approved amount or 
		 * Net Approved Amount then we have to set reverse allocation into PreauthDTO.
		 * */
		
		if(pedValidationTableDTO.getApprovedAmount() != null && pedValidationTableDTO.getNetApprovedAmount() != null && !pedValidationTableDTO.getApprovedAmount().equals(pedValidationTableDTO.getNetApprovedAmount())) {
			preauthDTO.setIsReverseAllocation(true);
		}
		if (pedValidationTableDTO.getDiagnosisName() != null) {
			String diagnosis = getDiagnosisByKey(pedValidationTableDTO.getDiagnosisName()
							.getId());
			pedValidationTableDTO.setDiagnosis(diagnosis);
			pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
			if (preauthDTO.getIsPostHospitalization()
					&& !preauthDTO.getHospitalizaionFlag()) {
				pedValidationTableDTO.setAmountConsideredAmount(0d);
				pedValidationTableDTO.setCopayAmount(0d);
				pedValidationTableDTO.setNetAmount(0d);
				pedValidationTableDTO.setApprovedAmount(0d);
				pedValidationTableDTO.setIsAmbChargeApplicable(false);
				pedValidationTableDTO.setAmbulanceCharge(0);
				pedValidationTableDTO.setAmtWithAmbulanceCharge(0);
			}
		}

		if (pedValidationTableDTO.getSublimitName() != null) {
			// Fix for issue 732 starts.
			SublimitFunObject objSublimitFun = sublimitFunObjMap
					.get(pedValidationTableDTO.getSublimitName()
							.getLimitId());
			// ClaimLimit limit =
			// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
			if (objSublimitFun != null) {
				pedValidationTableDTO.setSublimitName(objSublimitFun);
				pedValidationTableDTO.setSublimitAmt(String
						.valueOf(objSublimitFun.getAmount()));
			}
			// Fix for issue 732 ends
		}

		if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
			MastersValue master = getMaster(pedValidationTableDTO
							.getSumInsuredRestriction().getId());
			pedValidationTableDTO.getSumInsuredRestriction().setValue(
					master.getValue());
		}
		List<DiagnosisPED> pedDiagnosisByPEDValidationKey = getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
						.getKey());
		List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
		PedDetailsTableDTO dto = null;
		SelectValue value = null;
		SelectValue exclusionValue = null;
		for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
			dto = new PedDetailsTableDTO();
			// Added for disabling the procedure that is coming from
			// preauth.
			dto.setEnableOrDisable(isEnabled);
			dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
			dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
			dto.setKey(diagnosisPED.getKey());
			dto.setPedCode(diagnosisPED.getPedCode());
			dto.setPedName(diagnosisPED.getPedName());

			if (diagnosisPED.getDiagonsisImpact() != null) {
				value = new SelectValue();
				value.setId(diagnosisPED.getDiagonsisImpact().getKey());
				value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
				dto.setPedExclusionImpactOnDiagnosis(value);
				BeanItemContainer<ExclusionDetails> contaniner = getExclusionDetailsByImpactKey(diagnosisPED
								.getDiagonsisImpact().getKey());
				dto.setExclusionAllDetails(contaniner.getItemIds());
			}

			if (diagnosisPED.getExclusionDetails() != null) {
				exclusionValue = new SelectValue();
				exclusionValue.setId(diagnosisPED.getExclusionDetails()
						.getKey());
				exclusionValue.setValue(diagnosisPED.getExclusionDetails()
						.getExclusion());
				dto.setExclusionDetails(exclusionValue);
			}

			dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
			dtoList.add(dto);
		}
		pedValidationTableDTO.setPedList(dtoList);
		
		if ((preauthDTO.getIsPostHospitalization() || preauthDTO.getIsPreHospApplicable())
				&& !preauthDTO.getHospitalizaionFlag() && ! preauthDTO.getPartialHospitalizaionFlag()) {
			pedValidationTableDTO.setAmountConsideredAmount(0d);
		}
	}

	// TODO: Need to change this behaviour..
	preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
			newPedValidationTableListDto);

	// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);
	
	Map<String, Object> autoRestroation = dbCalculationService.getAutoRestroation(preauthDTO.getNewIntimationDTO().getIntimationId());
	preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(String.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK)));
	preauthDTO.getPreauthDataExtractionDetails().setRestorationCount(Integer.parseInt(autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT).toString()));
	if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
		preauthDTO.setIsAutoRestorationDone(true);
	}
	
	/*for bancs*/
	if(reimbursement.getCatastrophicLoss() != null) {
			preauthDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(new SelectValue(reimbursement.getCatastrophicLoss(),""));
	}
	
	if(reimbursement.getNatureOfLoss() != null) {
			preauthDTO.getPreauthDataExtractionDetails().setNatureOfLoss(new SelectValue(reimbursement.getNatureOfLoss(),""));
	}
	
	if(reimbursement.getCauseOfLoss() != null) {
		
		preauthDTO.getPreauthDataExtractionDetails().setCauseOfLoss(new SelectValue(reimbursement.getCauseOfLoss(),""));
	}
	
	//added for corpbuff CR
	if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
		CoorporateBuffer buffer = null;
		Long mainNo = 0L;
		DBCalculationService calcService = new DBCalculationService();
		if(claimByKey.getIntimation().getInsured().getDependentRiskId() !=null){
			mainNo = claimByKey.getIntimation().getInsured().getDependentRiskId();
		}else{
			mainNo = claimByKey.getIntimation().getInsured().getInsuredId();
		}
	if (claimByKey != null && claimByKey.getIntimation() !=null 
			&&  claimByKey.getIntimation().getInsured() !=null && claimByKey.getIntimation().getInsured().getDependentRiskId() !=null) {
		 buffer = masterService.getBufferbyMainMember(claimByKey.getIntimation().getInsured().getDependentRiskId());
	}else if(claimByKey != null && claimByKey.getIntimation() !=null 
			&&  claimByKey.getIntimation().getInsured() !=null && claimByKey.getIntimation().getInsured().getInsuredId() !=null){
		 buffer = masterService.getBufferbyMainMember(claimByKey.getIntimation().getInsured().getInsuredId());
	}
		if(buffer !=null){
			if(buffer.getBufferType() != null && buffer.getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_CB)){
				Map<String, Double> disBufferValues = calcService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_CB,
						claimByKey.getIntimation().getPolicy().getPolicyNumber(),claimByKey.getIntimation().getInsured().getKey(),mainNo,claimByKey.getKey());
				preauthDTO.getPreauthDataExtractionDetails().setBufferType(buffer.getBufferType());	
				preauthDTO.getPreauthDataExtractionDetails().setBufferTypeValue("Discretionary");
//				claimDTO.setIsgmcCorpBuffer(1L);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBuffer(true);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAllocatedClaim(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
//				claimDTO.setGmcCorpBufferLmt(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferUtilizedAmt(disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT).intValue());
			}
			if(buffer.getBufferType() != null && buffer.getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_WINTAGE)){
				Map<String, Double> disBufferValues = calcService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_WINTAGE,
						claimByKey.getIntimation().getPolicy().getPolicyNumber(),claimByKey.getIntimation().getInsured().getKey(),mainNo,claimByKey.getKey());
				preauthDTO.getPreauthDataExtractionDetails().setBufferType(buffer.getBufferType());	
				preauthDTO.getPreauthDataExtractionDetails().setBufferTypeValue("Vintage");
//				claimDTO.setIsgmcCorpBuffer(1L);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBuffer(true);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAllocatedClaim(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
//				claimDTO.setGmcCorpBufferLmt(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferUtilizedAmt(disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT).intValue());
			}
			if(buffer.getBufferType() != null && buffer.getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_NACB)){
				Map<String, Double> disBufferValues = calcService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_NACB,
						claimByKey.getIntimation().getPolicy().getPolicyNumber(),claimByKey.getIntimation().getInsured().getKey(),mainNo,claimByKey.getKey());
				preauthDTO.getPreauthDataExtractionDetails().setBufferType(buffer.getBufferType());	
				preauthDTO.getPreauthDataExtractionDetails().setBufferTypeValue("NACB");
//				claimDTO.setIsgmcCorpBuffer(1L);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBuffer(true);
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAllocatedClaim(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
//				claimDTO.setGmcCorpBufferLmt(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT).intValue());
				preauthDTO.getPreauthDataExtractionDetails().setCorpBufferUtilizedAmt(disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT).intValue());
			}
		}
	}
	
	return preauthDTO;
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

public UpdateHospital updateHospitalByReimbursementKey(Long reimbursementKey) {
	Query query = entityManager
			.createNamedQuery("UpdateHospital.findByReimbursementKey");
	query.setParameter("reimbursementKey", reimbursementKey);
	@SuppressWarnings("unchecked")
	List<UpdateHospital> resultList = query.getResultList();
	if (resultList != null && !resultList.isEmpty()) {
		UpdateHospital singleResult = (UpdateHospital) resultList.get(0);
		entityManager.refresh(singleResult);
		return singleResult; 
	}
	return null;
}

private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
	PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
			.getIntimation().getPolicy());
	preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
	Long hospital = claimByKey.getIntimation().getHospital();
	Hospitals hospitalById = getHospitalById(hospital);
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

public Hospitals getHospitalById(Long id) {
	if (id != null) {
		return entityManager.find(Hospitals.class, id);
	}
	return null;
}

public PreviousClaimedHistory getClaimedHistoryByTransactionKey(
		Long reimbursementKey) {
	Query query = entityManager
			.createNamedQuery("PreviousClaimedHistory.findByReimbursementKey");
	query.setParameter("reimbursementKey", reimbursementKey);
	List resultList = query.getResultList();
	if (resultList != null && !resultList.isEmpty()) {
		PreviousClaimedHistory singleResult = (PreviousClaimedHistory) resultList
				.get(0);
		entityManager.refresh(singleResult);
		return singleResult;
	}
	return null;

}

public List<PreviousClaimedHospitalization> getClaimedHospitalizationByClaimedHistoryKey(
		Long claimedHistoryKey) {
	Query query = entityManager
			.createNamedQuery("PreviousClaimedHospitalization.findByClaimedHistoryKey");
	query.setParameter("claimedHistoryKey", claimedHistoryKey);
	List<PreviousClaimedHospitalization> resultList = query.getResultList();
	if (resultList != null) {
		for (PreviousClaimedHospitalization previousClaimedHospitalization : resultList) {
			entityManager.refresh(previousClaimedHospitalization);
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

private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
		Double insuredSumInsured, Double insuredAge, SelectValue section, String plan, String sectionCode,Long insuredKey) {
	DBCalculationService dbCalculationService = new DBCalculationService();
	Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
	List<SublimitFunObject> sublimitList = dbCalculationService
			.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
					insuredAge,section != null ? section.getId() : 0l,plan, sectionCode,insuredKey);
	if (null != sublimitList && !sublimitList.isEmpty()) {
		for (SublimitFunObject sublimitFunObj : sublimitList) {
			sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
		}
	}
	return sublimitFunMap;

}

public String getDiagnosis(List<PedValidation> pedValidationList){

	StringBuffer diagnosisName = new StringBuffer();

	for (PedValidation pedValidation : pedValidationList) {

		Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
		diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
		Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
		if(masters != null){
			diagnosisName.append(masters.getValue()).append(",");
		}

	}

	return diagnosisName.toString();
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
public BeanItemContainer<ExclusionDetails> getExclusionDetailsByImpactKey(
		Long impactKey) {
	Query query = entityManager
			.createNamedQuery("ExclusionDetails.findByImpactId");
	List<ExclusionDetails> resultList;
	BeanItemContainer<ExclusionDetails> exclusionContainer = new BeanItemContainer<ExclusionDetails>(
			ExclusionDetails.class);
	if (impactKey != null) {
		query.setParameter("impactKey", impactKey);
		resultList = query.getResultList();

		exclusionContainer.addAll(resultList);
	}
	return exclusionContainer;
}

public String getDiagnosisByKey(Long key) {
	Diagnosis diagnosis = entityManager.find(Diagnosis.class, key);
	return diagnosis != null ? diagnosis.getValue() : "";
}

private Reimbursement saveNewVersionROD(ReceiptOfDocumentsDTO rodDTO,
		DocAcknowledgement docAck, String userNameForDB1,
		Long rodKeyFromPayload, Claim claimObj) {
	Reimbursement reimbursement;
	ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//					ZonalMedicalReviewMapper.getAllMapValues();
	DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
	rodDTO.getPreauthDTO().setDateOfAdmission(
			docsDTO.getDateOfAdmission());
	rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
			.setReasonForChange(docsDTO.getReasonForChange());
	rodDTO.getPreauthDTO().setPaymentModeId(
			docsDTO.getPaymentModeFlag());
	if (null != docsDTO.getPayeeName()) {
		rodDTO.getPreauthDTO().setPayeeName(
				docsDTO.getPayeeName().getValue());
	}
	rodDTO.getPreauthDTO()
			.setPayeeEmailId(docsDTO.getEmailId());
	rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());
	rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
			.setLegalFirstName(docsDTO.getLegalFirstName());
	rodDTO.getPreauthDTO().setAccountNumber(
			docsDTO.getAccountNo());
	
	if(docsDTO.getBankId() != null){
		rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
	}
	rodDTO.getPreauthDTO()
			.getPreauthDataExtractionDetails()
			.setRoomCategory(
					rodDTO.getClaimDTO().getNewIntimationDto()
							.getRoomCategory());
	rodDTO.getPreauthDTO().setStatusValue(
			rodDTO.getStatusValue());
	rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
	rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
	rodDTO.getPreauthDTO().setRodNumber(
			rodDTO.getDocumentDetails().getRodNumber());
	rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
			.setDocAckknowledgement(docAck);
	Long parentKey = rodDTO.getPreauthDTO().getKey();
	rodDTO.getPreauthDTO().setKey(null);
	reimbursement = reimbursementMapper.getReimbursement(rodDTO.getPreauthDTO());
	
	Status status = new Status();
	status.setKey(ReferenceTable.CREATE_ROD_STATUS_KEY);

	Stage stage = new Stage();
	stage.setKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
	
	reimbursement.setStage(stage);
	reimbursement.setStatus(status);
	
	reimbursement.setParentKey(parentKey);
	//reimbursement.setKey(null);
	
	/**
	 * Version of Rod
	 */
	Reimbursement latestReimbursementByRodNumber = getLatestReimbursementByRodNumber(reimbursement.getRodNumber());
	 rodDTO.setPreviousRodForReconsider(latestReimbursementByRodNumber.getKey());
	 if(latestReimbursementByRodNumber.getVersion() != null){
		 reimbursement.setVersion(latestReimbursementByRodNumber.getVersion() +1);
	 }else{
		 reimbursement.setVersion(2l); 
	 }

	 /**
	  * R1192 
	  */
	 reimbursement.setScoringFlag(latestReimbursementByRodNumber.getScoringFlag());
	 if(rodDTO.getDocumentDetails().getPatientStatus() != null
				&& rodDTO.getDocumentDetails().getPatientStatus().getId() != null
				&& (ReferenceTable.PATIENT_STATUS_ADMITTED_REIMB.equals(rodDTO.getDocumentDetails().getPatientStatus().getId()) 
								|| ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(rodDTO.getDocumentDetails().getPatientStatus().getId())
								|| ReferenceTable.getNewPatientStatusKeys().containsKey(rodDTO.getDocumentDetails().getPatientStatus().getId()))) {
		 savePayementDetails(reimbursement, docsDTO, claimObj);
	 }	 
//reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
	
	if(null != rodKeyFromPayload)
	{
		reimbursement.setKey(rodKeyFromPayload);
	}
	
	if (reimbursement.getKey() != null) {
	reimbursement.setDocAcknowLedgement(docAck);
//				reimbursement.setBillingApprovedAmount(0d);
//				reimbursement.setFinancialApprovedAmount(0d);
	reimbursement.setSkipZmrFlag("N");
	reimbursement.setModifiedBy(userNameForDB1);
	reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	reimbursement.setApprovedAmount(0d);
	reimbursement.setBillingApprovedAmount(null);
	reimbursement.setFinancialApprovedAmount(null);
	reimbursement.setZonalDate(null);
	reimbursement.setAddOnCoversApprovedAmount(0d);
	reimbursement.setOptionalApprovedAmount(0d);
//					entityManager.merge(reimbursement);
		
	} else {
//					reimbursement.setBillingApprovedAmount(0d);
//					reimbursement.setFinancialApprovedAmount(0d);
		reimbursement.setSkipZmrFlag("N");
		reimbursement.setApprovedAmount(0d);
		reimbursement.setCreatedBy(userNameForDB1);
		reimbursement.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		reimbursement.setBillingApprovedAmount(null);
		reimbursement.setFinancialApprovedAmount(null);
		reimbursement.setZonalDate(null);
		reimbursement.setAddOnCoversApprovedAmount(0d);
		reimbursement.setOptionalApprovedAmount(0d);
//						entityManager.persist(reimbursement);
	}
//					entityManager.flush();
	reimbursement.setOtherInsurerApplicableFlag(null);
	if(reimbursement.getRoomCategory() == null){
		reimbursement.setRoomCategory(latestReimbursementByRodNumber.getRoomCategory());
	}if(latestReimbursementByRodNumber.getRcFlag() !=null
			&& latestReimbursementByRodNumber.getRcFlag().equals("Y")){
		reimbursement.setRcFlag("Y");
		reimbursement.setOldRoomCategory(latestReimbursementByRodNumber.getOldRoomCategory());
	}
	reimbursement = savePreauthValues(rodDTO.getPreauthDTO(),reimbursement,reimbursementMapper,
			true, false,rodDTO.getStrUserName(), rodKeyFromPayload, docAck.getHospitalizationRepeatFlag() != null ? docAck.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? true : false : false);
	
	if(rodDTO.getExistingReimbursementCalDtsList() != null && ! rodDTO.getExistingReimbursementCalDtsList().isEmpty()){
		 saveReimbursementCalDtls(rodDTO.getExistingReimbursementCalDtsList(), reimbursement,rodDTO.getReconsiderRODdto().getIsSettledReconsideration());
	}
	
	Product product = rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
	if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
			rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
		if(rodDTO.getPreauthDTO() != null){
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO();
		if(!updateOtherClaimDetailDTO.isEmpty()){
			PreMedicalMapper premedicalMapper = new PreMedicalMapper();
			List<UpdateOtherClaimDetails> updateOtherClaimDetails = premedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
			for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
				updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
				updateOtherClaimDetails2.setClaimKey(claimObj.getKey());
				updateOtherClaimDetails2.setStage(reimbursement.getStage());
//								updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
				updateOtherClaimDetails2.setIntimationKey(claimObj.getIntimation().getKey());
				updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
				updateOtherClaimDetails2.setClaimType(claimObj.getClaimType().getValue());
				updateOtherClaimDetails2.setKey(null);
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
	}
	return reimbursement;
}

public void saveReimbursementCalDtls(List<ReimbursementCalCulationDetails> reimbursementCalCulationDetails,Reimbursement reimbursement,Boolean isSettledReconsideration){
	
	for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalCulationDetails) {
		ReimbursementCalCulationDetails reimbCal = new ReimbursementCalCulationDetails();
		reimbCal.setReimbursement(reimbursement);
		reimbCal.setBillClassificationId(reimbursementCalCulationDetails2.getBillClassificationId());
		reimbCal.setEligibleAmount(reimbursementCalCulationDetails2.getEligibleAmount());
		reimbCal.setCopayAmount(reimbursementCalCulationDetails2.getCopayAmount());
		reimbCal.setNetPayableAmount(reimbursementCalCulationDetails2.getNetPayableAmount());
		reimbCal.setMaximumPayableAmount(reimbursementCalCulationDetails2.getMaximumPayableAmount());
		reimbCal.setNetEligiblePayableAmount(reimbursementCalCulationDetails2.getNetEligiblePayableAmount());
		reimbCal.setClaimRestrictionAmount(reimbursementCalCulationDetails2.getClaimRestrictionAmount());
		reimbCal.setCashlessApprovedAmount(reimbursementCalCulationDetails2.getCashlessApprovedAmount());
		reimbCal.setPayableToHospital(reimbursementCalCulationDetails2.getPayableToHospital());
		reimbCal.setPayableToInsured(reimbursementCalCulationDetails2.getPayableToInsured());
		reimbCal.setTdsAmount(reimbursementCalCulationDetails2.getTdsAmount());
		reimbCal.setPayableToHospAftTDS(reimbursementCalCulationDetails2.getPayableToHospAftTDS());
		reimbCal.setDeductedBalancePremium(reimbursementCalCulationDetails2.getDeductedBalancePremium());
		reimbCal.setPayableInsuredAfterPremium(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium());
		reimbCal.setBalanceSIAftHosp(reimbursementCalCulationDetails2.getBalanceSIAftHosp());
		reimbCal.setRestrictedSIAftHosp(reimbursementCalCulationDetails2.getRestrictedSIAftHosp());
		reimbCal.setTpaClaimedAmt(reimbursementCalCulationDetails2.getTpaClaimedAmt());
		reimbCal.setHospitalDiscount(reimbursementCalCulationDetails2.getHospitalDiscount());
		reimbCal.setHospitalDiscountAmtAft(reimbursementCalCulationDetails2.getHospitalDiscountAmtAft());
		reimbCal.setTpaNonMedicalAmt(reimbursementCalCulationDetails2.getTpaNonMedicalAmt());
		reimbCal.setTpaPayableAmt(reimbursementCalCulationDetails2.getTpaPayableAmt());
		if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() != null){
	 		reimbCal.setAmountAlreadyPaidAmt(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt());
		}
		else{
			if(isSettledReconsideration != null && isSettledReconsideration){
			reimbCal.setAmountAlreadyPaidAmt(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium());
			}
		}
		reimbCal.setBalanceToBePaidAmt(reimbursementCalCulationDetails2.getBalanceToBePaidAmt());
		entityManager.persist(reimbCal);
		entityManager.flush();
		
	}

}

public List<ReimbursementCalCulationDetails> getReimbursementCalDetails(Long reimbursementKey){
	Query query = entityManager.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
	query = query.setParameter("reimbursementKey", reimbursementKey);
	List<ReimbursementCalCulationDetails> reimbCalculationDetails = query.getResultList();
	return reimbCalculationDetails;
}

public DocAcknowledgement getLatestDocAcknowledgementKeyByROD(Long rodKey,Long ackKey) {
	Query query = entityManager
			.createNamedQuery("DocAcknowledgement.findLatestAckByRod");
	query.setParameter("rodKey", rodKey);
	DocAcknowledgement docAcknowledgementValue = null;
	List<DocAcknowledgement> docAckKey = (List<DocAcknowledgement>) query.getResultList();
	if(docAckKey != null && ! docAckKey.isEmpty()){
		for (DocAcknowledgement docAcknowledgement : docAckKey) {
			if(! ackKey.equals(docAcknowledgement.getKey())){
				docAcknowledgementValue = docAcknowledgement;
				break;
			}
		}
	}
	return docAcknowledgementValue;
}

public Reimbursement getLatestReimbursementByRodNumber(String rodNumber) { 
	
	Query query = entityManager
			.createNamedQuery("Reimbursement.findRodByNumberWise");
	query.setParameter("rodNumber", rodNumber);
	List<Reimbursement> reimbursementDetails = query.getResultList();
	
	if(reimbursementDetails != null && ! reimbursementDetails.isEmpty()){
		return reimbursementDetails.get(0);
	}

    return null;
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

public String getGpaPolicyDetails(String policyNumber){
	
	 Query query = entityManager.createNamedQuery("GpaPolicy.findByPolicyNumber");
	 query = query.setParameter("policyNumber", policyNumber);
	 List<GpaPolicy> resultList = (List<GpaPolicy>)query.getResultList();		 
	 if(resultList != null && !resultList.isEmpty()) {
		 return resultList.get(0).getCpuCode();
	 } 
			
	 return null;
}

@SuppressWarnings("unchecked")
public Investigation getInvestigationDetails(Long claimKey,String transactionFlag) {
	
	Query findAll = entityManager.createNamedQuery(
			"Investigation.findByClaimKeyAndTransactionFlag");
			findAll.setParameter("claimKey", claimKey);
			findAll.setParameter("transactionFlag", transactionFlag);
	List<Investigation> investigationList = (List<Investigation>) findAll
			.getResultList(); 
	if(null != investigationList && !investigationList.isEmpty())
	{
		return investigationList.get(0);
	}
	
	return null;
}

	public void updateRod(ReceiptOfDocumentsDTO rodDTO) {
		AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		DocAcknowledgement docAckDtls = ackDocRecMapper.getDocAckRecDetails(rodDTO);
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		if(docAck != null) {
			if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
			{
				docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
			}
			Status status = new Status();
			status.setKey(ReferenceTable.PAYMENT_CANCEL_UPDATED);
			docAck.setStatus(status);
			String username = rodDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(username);
			docAck.setModifiedBy(userNameForDB);
			docAck.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(docAck);
			entityManager.flush();
			entityManager.clear();
			
			submitUpdateRodTaskToDB(rodDTO);
		}
	}

	private void submitUpdateRodTaskToDB(ReceiptOfDocumentsDTO rodDTO) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
			if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
				{
				if(rodDTO.getDocumentDetails().getDocumentReceivedFromValue() != null && rodDTO.getDocumentDetails().getDocumentReceivedFromValue().equals(SHAConstants.HOSPITAL)){
					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"Y");
					wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATE_ROD);
				} else {
					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"Y");
					wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG,"Y");
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
				}
	
				} else {
					if(rodDTO.getDocumentDetails().getDocumentReceivedFromValue() != null && rodDTO.getDocumentDetails().getDocumentReceivedFromValue().equals(SHAConstants.HOSPITAL)){
						wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
						wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATE_ROD);
					} else {
					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
					}
				}
	
			try{
	
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
	
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
				}
	
				catch(Exception e){
					e.printStackTrace();
					log.error(e.toString());
					
					log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
					
					try {
						log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR BILL_ENTRY ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());
//						BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTaskForBillEntry.getNumber(), SHAConstants.SYS_RELEASE);
//						submitBillEntryTask.execute("claimshead", humanTaskForBillEntry);
	
					} catch(Exception u) {
						log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
					}
				}
	}
	
	
	
	public void updateRodForPA(ReceiptOfDocumentsDTO rodDTO) {
		AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
		DocAcknowledgement docAckDtls = ackDocRecMapper.getDocAckRecDetails(rodDTO);
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());
		if(docAck != null) {
			if(null != rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())
			{
				docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag());
			}
			Status status = new Status();
			status.setKey(ReferenceTable.PAYMENT_CANCEL_UPDATED);
			docAck.setStatus(status);
			String username = rodDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(username);
			docAck.setModifiedBy(userNameForDB);
			docAck.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(docAck);
			entityManager.flush();
			entityManager.clear();
			
			submitPAUpdateRodTaskToDB(rodDTO);
			
		}
	}
	
	private void submitPAUpdateRodTaskToDB(ReceiptOfDocumentsDTO rodDTO) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
			if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag())){
				if(rodDTO.getDocumentDetails().getDocumentReceivedFromValue() != null && rodDTO.getDocumentDetails().getDocumentReceivedFromValue().equals(SHAConstants.HOSPITAL)){
				wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"Y");
				
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATE_ROD);
				} else {
					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
				}
			} else {
				if(rodDTO.getDocumentDetails().getDocumentReceivedFromValue() != null && rodDTO.getDocumentDetails().getDocumentReceivedFromValue().equals(SHAConstants.HOSPITAL)){
				wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
				
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CREATE_ROD);
				} else {
					wrkFlowMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION,"N");
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILL_ENTRY);
				}
			}
	
			try{
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			} catch(Exception e){
				e.printStackTrace();
				log.error(e.toString());
								
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR BILL_ENTRY ----------------- *&*&*&*&*&*&"+rodDTO.getNewIntimationDTO().getIntimationId());

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
				}
			}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean submitAddAdditionalPaymentValues(ReceiptOfDocumentsDTO rodDTO) {
		Boolean isBillEntrySubmitted = true; 
	    Boolean isReconsider = false;
		try {
			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);
			CreateRODMapper rodMapper = CreateRODMapper.getInstance();
			
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());

			Reimbursement reimbursement = null;
			Double totalClaimedAmtForBenefits = 0d;
			Double totalClaimedAmt = 0d;
		
				reimbursement = getReimbursementObjectByKey(rodDTO
						.getDocumentDetails().getRodKey());
		
			List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				uploadDocumentDTO.setStrUserName(userName);
				saveBillEntryValues(uploadDocumentDTO);
				}

			List<UploadDocumentDTO> deletedDocsList = rodDTO
					.getUploadDocumentsDTO().getDeletedDocumentList();
			if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

					RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
							.getDocumentSummary(uploadDocumentDTO2);
					rodSummary.setReimbursement(reimbursement);
					rodSummary.setDeletedFlag("Y");

					if (null != uploadDocumentDTO2.getDocSummaryKey()) {
						rodSummary.setModifiedBy(userName);
						rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(rodSummary);
						entityManager.flush();
						entityManager.clear();
						log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					} 
					
					DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
					if(null != details)
					{
						details.setDeletedFlag("Y");
						entityManager.merge(details);
						entityManager.flush();
						entityManager.clear();
						log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
					}
							
					
					/*
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 */

				}
			}
			
			reimbursement.setPaymentModeId(rodDTO.getDocumentDetails().getPaymentModeFlag());
			reimbursement.setPayeeEmailId(rodDTO.getDocumentDetails().getEmailId());
			reimbursement.setPanNumber(rodDTO.getDocumentDetails().getPanNo());
			reimbursement.setAccountNumber(rodDTO.getDocumentDetails().getAccountNo());
			reimbursement.setBankId(rodDTO.getDocumentDetails().getBankId());
			reimbursement.setPayableAt(rodDTO.getDocumentDetails().getPayableAt());
			reimbursement.setReasonForChange(rodDTO.getDocumentDetails().getReasonForChange());
			reimbursement.setLegalHeirFirstName(rodDTO.getDocumentDetails().getLegalFirstName());
			if(rodDTO.getDocumentDetails().getPayeeName() != null && rodDTO.getDocumentDetails().getPayeeName().getValue() != null) {
				reimbursement.setPayeeName(rodDTO.getDocumentDetails().getPayeeName().getValue());
			}
			reimbursement.setPayModeChangeReason(rodDTO.getDocumentDetails().getPayModeChangeReason());
			
			if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
			{			
				reimbursement.setAccountNumber(null);
				reimbursement.setPayableAt(rodDTO.getDocumentDetails().getPayableAt() != null ? rodDTO.getDocumentDetails().getPayableAt() : null);
			}
			else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
			{			
				
				reimbursement.setPayableAt(null);
			}
			reimbursement.setModifiedBy(userName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
			
			ClaimPayment claimPayment = getClaimPaymentByRodKey(reimbursement.getKey());
			
			if(claimPayment != null){
				claimPayment.setPayableAt(rodDTO.getDocumentDetails().getPayableAt());
				claimPayment.setIfscCode(rodDTO.getDocumentDetails().getIfscCode());
				claimPayment.setAccountNumber(rodDTO.getDocumentDetails().getAccountNo());
				claimPayment.setBranchName(rodDTO.getDocumentDetails().getBranch());
				if(rodDTO.getDocumentDetails().getPayeeName() != null && rodDTO.getDocumentDetails().getPayeeName().getValue() != null){
					claimPayment.setPayeeName(rodDTO.getDocumentDetails().getPayeeName().getValue());
				}
				claimPayment.setPanNumber(rodDTO.getDocumentDetails().getPanNo());
				claimPayment.setEmailId(rodDTO.getDocumentDetails().getEmailId());
				claimPayment.setBankName(rodDTO.getDocumentDetails().getBankName());
				claimPayment.setPayModeChangeReason(rodDTO.getDocumentDetails().getPayModeChangeReason());
				claimPayment.setReasonForChange(rodDTO.getDocumentDetails().getReasonForChange());
				claimPayment.setLegalHeirName(rodDTO.getDocumentDetails().getLegalFirstName());
				
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
				{
					claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					claimPayment.setAccountNumber(null);
					claimPayment.setBankCode(null);
					claimPayment.setIfscCode(null);
					claimPayment.setBankName(null);
					claimPayment.setBranchName(null);
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
				{
					
					claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
					claimPayment.setPayableAt(null);
				}
				claimPayment.setModifiedBy(userName);
				claimPayment.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(claimPayment);
				entityManager.flush();
			}
					
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		
		return isBillEntrySubmitted;
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean submitNEFTDetailsPaymentValues(ReceiptOfDocumentsDTO rodDTO) {
		Boolean isUploadNEFTDetailsSubmitted = true; 
	    Boolean isReconsider = false;
		try {
			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);
			CreateRODMapper rodMapper = CreateRODMapper.getInstance();
			
			/*DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());*/

			Reimbursement reimbursement = null;
			Double totalClaimedAmtForBenefits = 0d;
			Double totalClaimedAmt = 0d;
		
				reimbursement = getReimbursementObjectByKey(rodDTO
						.getDocumentDetails().getRodKey());
		
			List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				saveBillEntryValues(uploadDocumentDTO);
				}

			List<UploadDocumentDTO> deletedDocsList = rodDTO
					.getUploadDocumentsDTO().getDeletedDocumentList();
			if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

					RODDocumentSummary rodSummary = CreateRODMapper.getInstance()
							.getDocumentSummary(uploadDocumentDTO2);
					rodSummary.setReimbursement(reimbursement);
					rodSummary.setDeletedFlag("Y");

					if (null != uploadDocumentDTO2.getDocSummaryKey()) {
						rodSummary.setModifiedBy(userName);
						rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(rodSummary);
						entityManager.flush();
						entityManager.clear();
						log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					} 
					
					DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummary.getDocumentToken());
					if(null != details)
					{
						details.setDeletedFlag("Y");
						entityManager.merge(details);
						entityManager.flush();
						entityManager.clear();
						log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
					}
							
					
					/*
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 */

				}
			}
			
			NEFTQueryDetails neftQueryDetails = null;
			neftQueryDetails = getNEFTQueryDetailsObjectByKey(rodDTO.getDocumentDetails().getRodKey());
			if(neftQueryDetails !=null){
				
				Status status = new Status();
				status.setKey(ReferenceTable.NEFT_STATUS_RECEIVED_KEY);
				neftQueryDetails.setStatus(status);
				neftQueryDetails.setModifiedBy(userName);
				neftQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(neftQueryDetails);
				entityManager.flush();
				entityManager.clear();
				log.info("------NEFT Query Details------>"+neftQueryDetails+"<------------");
				
			}
		
			reimbursement.setPaymentModeId(rodDTO.getDocumentDetails().getPaymentModeFlag());
			reimbursement.setPayeeEmailId(rodDTO.getDocumentDetails().getEmailId());
			reimbursement.setPanNumber(rodDTO.getDocumentDetails().getPanNo());
			reimbursement.setAccountNumber(rodDTO.getDocumentDetails().getAccountNo());
			reimbursement.setBankId(rodDTO.getDocumentDetails().getBankId());
			reimbursement.setPayableAt(rodDTO.getDocumentDetails().getPayableAt());
			reimbursement.setReasonForChange(rodDTO.getDocumentDetails().getReasonForChange());
			reimbursement.setLegalHeirFirstName(rodDTO.getDocumentDetails().getLegalFirstName());
			if(rodDTO.getDocumentDetails().getPayeeName() != null && rodDTO.getDocumentDetails().getPayeeName().getValue() != null) {
				reimbursement.setPayeeName(rodDTO.getDocumentDetails().getPayeeName().getValue());
			}
			reimbursement.setPayModeChangeReason(rodDTO.getDocumentDetails().getPayModeChangeReason());
			
			if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
			{			
				reimbursement.setAccountNumber(null);
				reimbursement.setPayableAt(rodDTO.getDocumentDetails().getPayableAt() != null ? rodDTO.getDocumentDetails().getPayableAt() : null);
			}
			else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
			{			
				
				reimbursement.setPayableAt(null);
			}
			reimbursement.setModifiedBy(userName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
			
			ClaimPayment claimPayment = getClaimPaymentByRodKey(reimbursement.getKey());
			
			if(claimPayment != null){
				claimPayment.setPayableAt(rodDTO.getDocumentDetails().getPayableAt());
				claimPayment.setIfscCode(rodDTO.getDocumentDetails().getIfscCode());
				claimPayment.setAccountNumber(rodDTO.getDocumentDetails().getAccountNo());
				claimPayment.setBranchName(rodDTO.getDocumentDetails().getBranch());
				if(rodDTO.getDocumentDetails().getPayeeName() != null && rodDTO.getDocumentDetails().getPayeeName().getValue() != null){
					claimPayment.setPayeeName(rodDTO.getDocumentDetails().getPayeeName().getValue());
				}
				claimPayment.setPanNumber(rodDTO.getDocumentDetails().getPanNo());
				claimPayment.setEmailId(rodDTO.getDocumentDetails().getEmailId());
				claimPayment.setBankName(rodDTO.getDocumentDetails().getBankName());
				claimPayment.setPayModeChangeReason(rodDTO.getDocumentDetails().getPayModeChangeReason());
				claimPayment.setReasonForChange(rodDTO.getDocumentDetails().getReasonForChange());
				claimPayment.setLegalHeirName(rodDTO.getDocumentDetails().getLegalFirstName());
				
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
				{
					claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					claimPayment.setAccountNumber(null);
					claimPayment.setBankCode(null);
					claimPayment.setIfscCode(null);
					claimPayment.setBankName(null);
					claimPayment.setBranchName(null);
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(rodDTO.getDocumentDetails().getPaymentModeFlag()))
				{
					
					claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
					claimPayment.setPayableAt(null);
				}
				if(ReferenceTable.NEFT_STATUS_PENDING_KEY.equals(claimPayment.getStatusId().getKey())){
					Status status = new Status();
					status.setKey(ReferenceTable.PAYMENT_NEW_STATUS);
					claimPayment.setStatusId(status);
				}
				claimPayment.setModifiedBy(userName);
				claimPayment.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(claimPayment);
				entityManager.flush();
			}
					
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		
		return isUploadNEFTDetailsSubmitted;
		
		
	}
	
	
	
	public List<UploadDocumentDTO> getNeftRODSummaryDetails(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		List<UploadDocumentDTO> uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbKeyFileType");
		query.setParameter("reimbursementKey", key);
		query.setParameter("fileType", ReferenceTable.DOCTYPE_NEFT);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			for (RODDocumentSummary docSummary : rodDocSummary) {
				entityManager.refresh(docSummary);
			}
			uploadDocsDTO = CreateRODMapper.getInstance().getUploadDocumentDTO(rodDocSummary);
		}
		return uploadDocsDTO;
	}
	public PolicyBankDetails getCorpBankDetails(Long policyKey){
		Query query = entityManager.createNamedQuery("PolicyBankDetails.findByPolicyKey");
		query = query.setParameter("policyKey", policyKey);
		List<PolicyBankDetails> corpBankDtls = (List<PolicyBankDetails>)query.getResultList();
		if(corpBankDtls != null && !corpBankDtls.isEmpty()){
			return corpBankDtls.get(0);
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
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByValue");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList){
				a_mastersValue = mastersValue;
				break;
			}
		}
		return a_mastersValue;
	}
	
	public List<BillEntryDetailsDTO> getWithDrawClaimBillEntryDetailsList(Long documentSummKey)
	{
		List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
		UploadDocumentDTO uploadDTO  = new UploadDocumentDTO();
		List<RODBillDetails> billEntryDetails = getBillEntryDetails(documentSummKey);
		if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
			for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
				dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,uploadDTO));
			}

		}
		return dtoList;
	}
	
	public DocAcknowledgement saveWithdrawCashlessBillClassificationDtls(UploadDocumentDTO uploadDTO){
		
		
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(uploadDTO.getAckDocKey());
		if(docAck != null){
			docAck.setHospitalisationFlag(uploadDTO.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(uploadDTO.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(uploadDTO.getPostHospitalizationFlag());
			docAck.setBenifitFlag(uploadDTO.getHospitalBenefitFlag());
			docAck.setPartialHospitalisationFlag(uploadDTO.getPartialHospitalizationFlag());
			entityManager.merge(docAck);
			entityManager.flush();
		}
		return docAck;
	}

	public List<DMSDocumentDetailsDTO> getBillAssessmentDocumentsVersionsByRod(String rodNumber,String docType,String docTypeScrc,String docSource)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findBillAssessmentVersions"); 
		query = query.setParameter("reimbursementNumber", rodNumber);
		
		query = query.setParameter("billsummary", docType);
		query = query.setParameter("billassessment", docTypeScrc);
		query = query.setParameter("financialapproval", docSource);
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		

		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
		
		/**
		 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
		 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
		 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
		 * both will be populated in file name variable of dto. 
		 */
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				
				if(null != documentDetails.getReimbursementNumber())
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());
				
				}
				else
				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
				}
				
				if(documentDetails.getFileName() != null
						&& !documentDetails.getFileName().isEmpty()
						&& documentDetails.getDocVersion() != null
						&& !documentDetails.getDocVersion().isEmpty()) {
					String strFileName = documentDetails.getFileName().substring(0, documentDetails.getFileName().lastIndexOf("."));
					documentDetails.setFileName(strFileName + "_" + documentDetails.getDocVersion()+".pdf");
				}
			}
		}
		
		return documentDetailsDTOList;
	}
	
	public List<Reimbursement> getReimbursementByClaimKeyForCriticare(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		List<Reimbursement> rodList = new ArrayList<Reimbursement>();
		for (Reimbursement reimbursement2 : reimbursementList) {
			/**The below condition added  for JIRA - GALAXYMAIN-11084 **/
			if(!(ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())) && 
					! reimbursement2.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) &&
					(ReferenceTable.getRevisedCriticareProducts().containsKey(reimbursement2.getClaim().getIntimation().getPolicy().getProduct().getKey()) &&
							!(ReferenceTable.getRejectedRODKeys().containsKey(reimbursement2.getStatus().getKey()))) || 
					((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(reimbursement2.getClaim().getIntimation().getPolicy().getProduct().getKey())
							|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(reimbursement2.getClaim().getIntimation().getPolicy().getProduct().getKey()))
							&& reimbursement2.getClaim().getIntimation().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)&&
							!(ReferenceTable.getRejectedRODKeys().containsKey(reimbursement2.getStatus().getKey())
									//IMSSUPPOR-33000
								|| ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())))
							|| (ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY.equals(reimbursement2.getClaim().getIntimation().getPolicy().getProduct().getKey()) &&
							!(ReferenceTable.getRejectedRODKeys().containsKey(reimbursement2.getStatus().getKey())
									//IMSSUPPOR-33000
								|| ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())))){
			
				rodList.add(reimbursement2);
				/*if(isCloseClaimValid){
					isCloseClaimValid = false;
					view.showErrorPopUp("Closing of claim is not possible. Please Process/Cancel and then trying Closing");
				}*/
			}
		}
		return rodList;
		//Boolean validationFlag = false;
		/*Query query = entityManager.createNamedQuery("Reimbursement.findLatestNonCanceledRODACKByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		query = query.setParameter("rodcancelstatusId", ReferenceTable.);
		query = query.setParameter("ackcancelstatusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<Reimbursement> rodList = query.getResultList();	
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}
		return  rodList;*/
	}

	public void saveDiagnosisDetails(PedValidation peddetails){
		
	try{	
		entityManager.merge(peddetails);
		entityManager.flush();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}
	public Boolean getHospitalizationRODAvailableOrNot(Long claimKey){
	    Boolean isHospitalizationAvailable= true;
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckByClaim");
		query = query.setParameter("claimkey", claimKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
		 for(DocAcknowledgement docAcknowledgement : docAckList) {
			if(docAcknowledgement.getHospitalisationFlag() != null && 
			 docAcknowledgement.getHospitalisationFlag().equalsIgnoreCase(SHAConstants.HOSPITALIZATION_FLAG)){
			isHospitalizationAvailable= false;
			}		
		  }
	    }
		return isHospitalizationAvailable;
	}
	
	public Boolean getKotakPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("StarKotakPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarKotakPolicy> resultList = (List<StarKotakPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
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
	 
	 /*Below Code for IMSSUPPOR-28573*/
	 private Insured getInsuredByPolicyAndInsuredNameLob(String policyNo , String insuredName)
		{
			Query query = entityManager.createNamedQuery("Insured.findByInsuredNameAndPolicyNo");
			query = query.setParameter("policyNo", policyNo);
			if(null != insuredName)
			query = query.setParameter("insuredName", insuredName);
			List<Insured> insuredList = query.getResultList();
			insuredList = query.getResultList();
			List<Insured> healthInsured = new ArrayList<Insured>();
			for (Insured insured : insuredList) {
				if(insured.getLopFlag() != null && insured.getLopFlag().equalsIgnoreCase(SHAConstants.HEALTH_LOB_FLAG)){
					healthInsured.add(insured);
				}
				else {
					if(insured.getLopFlag() == null){
						healthInsured.add(insured);
				}
				}
			}
			if(null != healthInsured && !healthInsured.isEmpty()){
				return healthInsured.get(0);
			}
			else {
				return null;
			}
		}
	 
	 public DocAcknowledgement getDocAckBasedOnROD(Long rodKey)
		{
			Query query = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey");
			query = query.setParameter("rodKey", rodKey);
			List<DocAcknowledgement> docAckList = query.getResultList();
			if(null != docAckList && !docAckList.isEmpty())
			{
				
					entityManager.refresh(docAckList.get(0));
					return docAckList.get(0);
			}
			
			return null;
		}
	 
	 public void saveNomineeDetails(List<NomineeDetailsDto> nomineeDetailsList, Reimbursement reimbObj){
			
			for (NomineeDetailsDto nomineeDetailsDto : nomineeDetailsList) {
				
				ProposerNominee nomineeObj = getNomineeDetailsByKey(nomineeDetailsDto.getProposerNomineeKey());

				if(nomineeObj != null){
					nomineeObj.setModifiedBy(nomineeDetailsDto.getModifiedBy());
					nomineeObj.setModifiedDate(new Date());
				}
				else{
					nomineeObj = new ProposerNominee();
					nomineeObj.setNomineeName(nomineeDetailsDto.getNomineeName());
					nomineeObj.setNomineeAge(nomineeDetailsDto.getNomineeAge() != null && !nomineeDetailsDto.getNomineeAge().isEmpty() ? Integer.valueOf(nomineeDetailsDto.getNomineeAge()) : null);
					nomineeObj.setNomineeDob(nomineeDetailsDto.getNomineeDob() != null && !nomineeDetailsDto.getNomineeDob().isEmpty() ? SHAUtils.formatTimeFromString(nomineeDetailsDto.getNomineeDob()) : null);
					nomineeObj.setRelationshipWithProposer(nomineeDetailsDto.getNomineeRelationship());
					nomineeObj.setSharePercent(nomineeDetailsDto.getNomineePercent() != null && !nomineeDetailsDto.getNomineePercent().isEmpty() ?  Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0]) : null);
					nomineeObj.setPolicy(reimbObj.getClaim().getIntimation().getPolicy());
					nomineeObj.setPolicyNominee(getPolicyNomineebyKey(nomineeDetailsDto.getPolicyNomineeKey()));
					nomineeObj.setAppointeeName(nomineeDetailsDto.getAppointeeName());
					nomineeObj.setAppointeeAge(nomineeDetailsDto.getAppointeeAge() != null && !nomineeDetailsDto.getAppointeeAge().isEmpty() ? Integer.valueOf(nomineeDetailsDto.getAppointeeAge()) : null);
					nomineeObj.setAppointeeRelationship(nomineeDetailsDto.getAppointeeRelationship());
					nomineeObj.setCreatedBy(nomineeDetailsDto.getModifiedBy());
					nomineeObj.setCreatedDate(new Date());
					nomineeObj.setActiveStatus(1);
					nomineeObj.setNomineeCode(nomineeDetailsDto.getNomineeCode());
					nomineeObj.setInsured(reimbObj.getClaim().getIntimation().getInsured());
					nomineeObj.setIntimation(reimbObj.getClaim().getIntimation());
					nomineeObj.setClaim(reimbObj.getClaim());
					nomineeObj.setTransactionKey(reimbObj.getKey());
					nomineeObj.setTransactionType(reimbObj.getClaim().getClaimType() != null ? reimbObj.getClaim().getClaimType().getValue().toUpperCase() : ReferenceTable.REIMBURSEMENT_CLAIM);
					nomineeObj.setBankName(nomineeDetailsDto.getBankName());
					nomineeObj.setBankBranchName(nomineeDetailsDto.getBankBranchName());
				}

				if(nomineeDetailsDto.isPaymentMode()){
					nomineeObj.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
				}else{
					nomineeObj.setPaymentModeId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}

				if(nomineeDetailsDto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(nomineeDetailsDto.getPaymentModeId()))
					nomineeObj.setPayableAt(nomineeDetailsDto.getPayableAt());
				
				nomineeObj.setNameAsperBankAcc(nomineeDetailsDto.getNameAsPerBankAc());
				nomineeObj.setAccNumber(nomineeDetailsDto.getAccNumber());
				nomineeObj.setAccType(nomineeDetailsDto.getAccType());
				nomineeObj.setAccPreference(nomineeDetailsDto.getPreference());
				nomineeObj.setIfscCode(nomineeDetailsDto.getIfscCode());
				nomineeObj.setBankName(nomineeDetailsDto.getBankName());
				nomineeObj.setBankBranchName(nomineeDetailsDto.getBankBranchName());
				nomineeObj.setMicrCode(nomineeDetailsDto.getMicrCode());
//					nomineeObj.setVirtualPaymentAddress(nomineeDetailsDto.getMicrCode());
//					nomineeObj.setEffectiveFromDate();
//					nomineeObj.setEffectiveToDate();

				nomineeObj.setInsured(reimbObj.getClaim().getIntimation().getInsured());
				
				nomineeObj.setSelectedFlag(nomineeDetailsDto.isSelectedNominee() ? ReferenceTable.YES_FLAG : ReferenceTable.NO_FLAG);

				try{
					if(nomineeObj.getKey() == null) {
						entityManager.persist(nomineeObj);
					}
					else{
						entityManager.merge(nomineeObj);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		}
	 
	 public ProposerNominee getNomineeDetailsByKey(Long proposerNomineeKey) {
			ProposerNominee nomineeObj = null;
				try{
					Query query = entityManager.createNamedQuery("ProposerNominee.findByKey");
					query = query.setParameter("nomineeKey", proposerNomineeKey);
					
					List<ProposerNominee> resultList = (List<ProposerNominee>) query.getResultList();
					
					if(resultList != null && !resultList.isEmpty()) {
						nomineeObj = resultList.get(0);
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return nomineeObj;
			
		}
		
		public PolicyNominee getPolicyNomineebyKey(Long policyNomineeKey) {
			
			PolicyNominee nomineeObj = null;
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByKey");
				query = query.setParameter("nomineeKey", policyNomineeKey);
				
				List<PolicyNominee> resultList = (List<PolicyNominee>) query.getResultList();
				
				if(resultList != null && !resultList.isEmpty()) {
					nomineeObj = resultList.get(0);
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return nomineeObj;
		}

		
		public List<DocAcknowledgement> getListOfAcknowledgReconsider(Long claimByKey){
			   
		    Query query = entityManager
					.createNamedQuery("DocAcknowledgement.getByClaimKeyWithReconsiderflag");
			query = query.setParameter("claimkey", claimByKey);
	 
			List<DocAcknowledgement> resultList = (List<DocAcknowledgement>) query.getResultList();
			
			if(resultList != null && ! resultList.isEmpty()){
				for (DocAcknowledgement docAcknowledgement : resultList) {
					entityManager.refresh(docAcknowledgement);
				}
				return resultList;
			}
			
			return null;					
			
	   }
		
		public List<Map<String, Object>> getDBTaskForPreauth(Intimation intimation,String currentQ, Claim claim){

			log.info("@@@@@@@@@ GET TASK PROCEDURE CALL STARTING TIME @@@@@@@@@@-----> "+"-----> " +intimation.getIntimationId()+"-----> "+ System.currentTimeMillis());

			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, currentQ);
			List<Map<String, Object>> WeakHashMapWeakHashMap = /*(List<Map<String, Object>>)*/ new ArrayList<Map<String, Object>>();
			if(claim!=null){
				mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, claim.getCrcFlag());
			}

			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

			DBCalculationService db = new DBCalculationService();
			log.info("@@@@CALL GET TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
			List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForBatch(setMapValues);
			log.info("@@@@CALL GET TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());

			log.info("@@@@@@@@@ GET TASK PROCEDURE CALL ENDING TIME @@@@@@@@@@-----> "+"-----> " +intimation.getIntimationId()+"-----> "+ System.currentTimeMillis());
			if (taskProcedure != null && !taskProcedure.isEmpty()){
				//				return true;
			} 
			for (Map<String, Object> map : taskProcedure) {

				String reconsiderFlag = (String)map.get(SHAConstants.RECONSIDER_FLAG);

				if(reconsiderFlag != null && reconsiderFlag.equalsIgnoreCase("Y")){

					WeakHashMapWeakHashMap.add(map);

				}

			}

			return WeakHashMapWeakHashMap;
		}
		
        	public DiagnosisHospitalDetails getDiagnosisByID(Long key) {
			Query query = entityManager.createNamedQuery("DiagnosisHospitalDetails.findDiagnosisByKey");
			query=query.setParameter("findDiagnosisByKey", key);
			
			List<DiagnosisHospitalDetails> reimbursementList = query.getResultList();
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
        	
	public MasHospitalCashBenefit getHospCashBenefitByID(Long key) {
		Query query = entityManager
				.createNamedQuery("MasHospitalCashBenefit.findHospCashBenefitByKey");
		query = query.setParameter("benefitKey", key);

		List<MasHospitalCashBenefit> reimbursementList = query
				.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
		/*
		 * Reimbursement reimbursement = (Reimbursement)
		 * query.getSingleResult(); entityManager.refresh(reimbursement); return
		 * reimbursement;
		 */
	}
	public Insured getPAInsuredByPolicyAndInsuredID(String policyNo , Long insuredID)
	{
		Query query = entityManager.createNamedQuery("Insured.findByInsuredId");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredID)
		{
			query = query.setParameter("insuredId", insuredID);
		}	
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty())
			return insuredList.get(0);
		else
			return null;
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
	
	public MastersValue getPCCMasterValue(String pccCode) {


		Query query = entityManager
				.createNamedQuery("MastersValue.findByMappingCode");
		query = query.setParameter("mappingCode", pccCode);
		List<MastersValue> mastersValueList = query.getResultList();
		
		if(mastersValueList != null && ! mastersValueList.isEmpty()){
			return mastersValueList.get(0);
		}


		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<DMSDocumentDetailsDTO> getDocumentDetailsByIntimationNo(String intimationNo){
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoWithFlag");
		query = query.setParameter("intimationNumber", intimationNo);
		List<DocumentDetails> docdetailsList = query.getResultList();
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		for (DocumentDetails documentDetails : docdetailsList) {
			entityManager.refresh(documentDetails);
		}
		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(docdetailsList);
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				if(documentDetails.getFileName() != null
						&& !documentDetails.getFileName().isEmpty()) {
					String strFileName = documentDetails.getFileName().substring(0, documentDetails.getFileName().lastIndexOf("."));
					documentDetails.setFileName(strFileName + "_" + documentDetails.getDocVersion()+".pdf");
				}
			}
		}
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty()){
			return documentDetailsDTOList;
		}
		return null;
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
	
	public MasProductCpuRouting getMasProductForGMCRouting(Long key){

		Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		query = query.setParameter("key", key);
		List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		if(resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		} 

		return null;
	}
	
	public Long getKotakProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
		Long kotakCPUCode =cpuCode;
		MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
		if(masCpuLimit != null){
			if(estarFaxAmt >= masCpuLimit.getCpuLimit()){
				kotakCPUCode = masCpuLimit.getProcessCpuCode();
			}else {
				kotakCPUCode = masCpuLimit.getCpuCode();
			}
		}
		return kotakCPUCode;
	}

	public void submitICACProcess(PreauthDTO preauthDTO){
		
		IcacRequest icacRequest = null;
		String strUserName = preauthDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);

		Claim currentClaim = searchByClaimKeyValues(preauthDTO.getClaimKey());
		Reimbursement reimbursement = getReimbursementbyRodNumber(preauthDTO.getRodNumber());

		if(null != preauthDTO){
			if(preauthDTO.getRodNumber() != null){
			

				if(reimbursement != null && null != reimbursement.getKey()){
					reimbursement.setIcacFlag(SHAConstants.YES_FLAG);
					entityManager.merge(reimbursement);
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
			
			icacRequest = new IcacRequest();
			
			if(icacRequest != null){
				icacRequest.setIntimationNum(preauthDTO.getNewIntimationDTO().getIntimationId());
				icacRequest.setPolicyNumber(preauthDTO.getNewIntimationDTO().getPolicyNumber());
				icacRequest.setClaimStage(reimbursement.getStage());
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
	
	public Reimbursement getReimbursementbyRodNumber(String rodNumber) { 
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.findRodByNumberWise");
		query.setParameter("rodNumber", rodNumber);
		List<Reimbursement> reimbursementDetails = query.getResultList();
		
		if(reimbursementDetails != null && ! reimbursementDetails.isEmpty()){
			return reimbursementDetails.get(0);
		}

	    return null;
	}
	
	private void updateLegalHeir(ReceiptOfDocumentsDTO rodDTO){
		String username1 = rodDTO.getStrUserName();
		String userNameForDB1 = SHAUtils.getUserNameForDB(username1);
		List<LegalHeir> list = getlegalHeirListByTransactionKey(rodDTO.getKey());
		if(list != null && !list.isEmpty()){
			for (LegalHeir legalHeir : list) {
				legalHeir.setActiveStatus(SHAConstants.N_FLAG);
				legalHeir.setModifiedBy(userNameForDB1);
				legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(legalHeir);
				entityManager.flush();
			}
		}
	}
	
	public List<LegalHeir> getlegalHeirListByTransactionKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList;
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

	public boolean getDiagnosisICDValidtion(Long key) {
		
		Boolean isValidDiagnosis = false;

		List<PedValidation> resultList = new ArrayList<PedValidation>();
		Query query = entityManager
				.createNamedQuery("PedValidation.findByIntimationKey");
		query.setParameter("intimationKey", key);

		resultList = (List<PedValidation>) query.getResultList();
		
		if(resultList != null){
			for(PedValidation pedValue : resultList){
				if(pedValue.getIcdCodeId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY)  
						|| pedValue.getIcdCodeId().equals(SHAConstants.COVID_19_ICD_NOT_IDENT_KEY)){
					isValidDiagnosis= true;
				}		
			}

		}
		
	return isValidDiagnosis;
	}
	
	public PhysicalDocumentVerificationDetails getPhysicalVerificationDtlsBySummaryKey(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerificationDetails.findByDocSummaryKey")
				.setParameter("primaryKey", key);

		List<PhysicalDocumentVerificationDetails> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	public List<NEFTQueryDetails> getNEFTDetailsByRODKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("NEFTQueryDetails.findByRODDKey");
		query.setParameter("rodKey", rodKey);
		List<NEFTQueryDetails> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList;
		}
		return null;
		
	}
	
	
	public void saveNEFTDetails(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement) {

		/*DBCalculationService dbService = new DBCalculationService();

		String neftAvailableFlag  = dbService.getNEFTDetailsAvailableFlag(reimbursement.getClaim().getIntimation().getKey());
		if(neftAvailableFlag !=null && neftAvailableFlag.equalsIgnoreCase(SHAConstants.N_FLAG))*/

		NEFTQueryDetails neftQueryDetails =  new NEFTQueryDetails();
		System.out.println("Reimbursemnt Key " + reimbursement.getKey());
		List<NEFTQueryDetails> NEFTDetails = getNEFTDetailsByRODKey(reimbursement.getKey());

		if(rodDTO.getIsNEFTDetailsAvailable()!= null && rodDTO.getIsNEFTDetailsAvailable()){

			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);

			if(NEFTDetails !=null && !NEFTDetails.isEmpty()){

				neftQueryDetails.setModifiedBy(userName);				
				neftQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(neftQueryDetails);
				entityManager.flush();
				log.info("------NEFT DETAILS------>"+neftQueryDetails+"<------------");
				
			}
			else{

				neftQueryDetails.setCreatedBy(userName);
				neftQueryDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				Claim claimObj = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
				neftQueryDetails.setClaim(claimObj);
				neftQueryDetails.setIntimationKey(rodDTO.getNewIntimationDTO().getKey());
				//neftQueryDetails.setRodKey(rodDTO.getDocumentDetails().getRodKey());
				neftQueryDetails.setReimbursement(reimbursement);
				neftQueryDetails.setActiveStatus("1");

				Status status = new Status();
				status.setKey(ReferenceTable.NEFT_STATUS_PENDING_KEY);

				Stage stage = new Stage();
				stage.setKey(ReferenceTable.NEFT_STAGE_KEY);

				neftQueryDetails.setStage(stage);
				neftQueryDetails.setStatus(status);

				entityManager.persist(neftQueryDetails);
				entityManager.flush();
				log.info("------NEFT DETAILS------>"+neftQueryDetails+"<------------");

			}

		}


	}
	
	public void saveNEFTDetailsForCheque(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement) {

		/*DBCalculationService dbService = new DBCalculationService();

		String neftAvailableFlag  = dbService.getNEFTDetailsAvailableFlag(reimbursement.getClaim().getIntimation().getKey());
		if(neftAvailableFlag !=null && neftAvailableFlag.equalsIgnoreCase(SHAConstants.N_FLAG))*/

		NEFTQueryDetails neftQueryDetails =  new NEFTQueryDetails();
		System.out.println("Reimbursemnt Key " + reimbursement.getKey());
		List<NEFTQueryDetails> NEFTDetails = getNEFTDetailsByRODKey(reimbursement.getKey());

//		if(rodDTO.getIsNEFTDetailsAvailableinDMS()!= null && rodDTO.getIsNEFTDetailsAvailableinDMS()){

			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);

			if(NEFTDetails !=null && !NEFTDetails.isEmpty()){

				neftQueryDetails.setModifiedBy(userName);				
				neftQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(neftQueryDetails);
				entityManager.flush();
				log.info("------NEFT DETAILS------>"+neftQueryDetails+"<------------");
				
			}
			else{

				neftQueryDetails.setCreatedBy(userName);
				neftQueryDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				Claim claimObj = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
				neftQueryDetails.setClaim(claimObj);
				neftQueryDetails.setIntimationKey(rodDTO.getNewIntimationDTO().getKey());
				//neftQueryDetails.setRodKey(rodDTO.getDocumentDetails().getRodKey());
				neftQueryDetails.setReimbursement(reimbursement);
				neftQueryDetails.setActiveStatus("1");

				Status status = new Status();
				status.setKey(ReferenceTable.NEFT_STATUS_PENDING_KEY);

				Stage stage = new Stage();
				stage.setKey(ReferenceTable.NEFT_STAGE_KEY);

				neftQueryDetails.setStage(stage);
				neftQueryDetails.setStatus(status);

				entityManager.persist(neftQueryDetails);
				entityManager.flush();
				log.info("------NEFT DETAILS------>"+neftQueryDetails+"<------------");

			}

		}

//	}
	
	public void NEFTDocumentUpload(ReceiptOfDocumentsDTO rodDTO) {

		Boolean isValidClaimForAck = true;
		ReceiptOfDocumentsDTO rodDTO1 = new ReceiptOfDocumentsDTO();
		ClaimDto claimDTO = null;
		DocAcknowledgement docAck = new DocAcknowledgement();
		Boolean isQueryReplyReceived = false;
		Boolean isReconsideration = false;
		Boolean isQueryReplyNo = false;
		Boolean isQueryStatusYes = false;
		String templateName = "NEFTQueryLetter";
		ReportDto reportDto = null;
		DocumentGenerator docGen = null;
		DocumentDetailsDTO documentDetailsDTO = new DocumentDetailsDTO();
		AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();


		//Claim claim = docAck.getClaim();
		//log.info("claim.getClaimId() : "+claim.getClaimId());
		Hospitals registeredHospital = null;
		//TmpHospital tempHospital = null;
		/*if (claim.getIntimation().getHospital() != null
				&& claim.getIntimation().getHospitalType() != null
				&& StringUtils.contains(claim.getIntimation().getHospitalType()
						.getValue().toLowerCase(), "network")) {
		} 
		Long policyKey = claim.getIntimation().getPolicy().getKey();
		Insured insured = claim.getIntimation().getInsured();*/
		//claimDTO.getNewIntimationDto().setInsuredPatient(insured);
		
		claimDTO = rodDTO.getClaimDTO();

		rodDTO.setClaimDTO(claimDTO);
		rodDTO.setStrUserName("SYSTEM");
		reportDto = new ReportDto();
		List <ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
		rodDTOList.add(rodDTO);
		reportDto.setBeanList(rodDTOList);
		reportDto.setClaimId(claimDTO.getClaimId());
		docGen = new DocumentGenerator();
		log.info("------NEFT Query Document Template Generation Starts------");
		String filePath = generatePdfDocument(templateName, rodDTO);
		log.info("------NEFT Query Document Template Generation Ends------");
		if( !filePath.isEmpty() )
		{
			rodDTO.setDocFilePath(filePath);

			if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
			{
				HashMap dataMap = new HashMap();
				dataMap.put("intimationNumber",rodDTO.getNewIntimationDTO().getIntimationId());
				Claim objClaim = getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
				if(null != objClaim)
				{
					dataMap.put("claimNumber",objClaim.getClaimId());
					/*if(null != objClaim.getClaimType())
					{
						if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
						{
							Preauth preauth = getPreauthClaimKey( objClaim.getKey());
							if(null != preauth)
								dataMap.put("cashlessNumber", preauth.getPreauthId());
						}
					}*/
				}
				dataMap.put("filePath", rodDTO.getDocFilePath());
				dataMap.put("docType", rodDTO.getDocType());
				dataMap.put("docSources", SHAConstants.NEFT_QUERY_LETTER);
				dataMap.put("createdBy", rodDTO.getStrUserName());
				uploadGeneratedLetterToDMS(entityManager, dataMap);
				log.info("------DocAcknowledgement Batch Creation Template Upload Ends------");
			}
		}
		log.info("------DocAcknowledgement Batch Creation Submit task Starts------");

	}
	
	public String generatePdfDocument(String template_Name,ReceiptOfDocumentsDTO reportDto){
		try {
			if (ValidatorUtils.isNull(template_Name)
					|| ValidatorUtils.isNull(reportDto)) {
				return null;
			} else if (template_Name != "" && reportDto != null) {

				String filePath = System.getProperty("jboss.server.data.dir");
				System.out.println("file path : =====================" + filePath);
				File file = new File(filePath + File.separator + "templates" + File.separator + template_Name + ".jrxml");
				System.out.println("----File name and path ------" + file.getPath());

				if (file.exists() == false) {
					System.out.println("::::::::::::::::::::::file not available::::::::::::::::::::::");
				} else {
					JasperDesign a_jasperDesign = JRXmlLoader.load(file);
					JasperReport a_jasperReport = JasperCompileManager.compileReport(a_jasperDesign);
					if (a_jasperReport == null) {
					} else {
						String claimId = null;

						if (reportDto != null) {
							System.out.println("Jasper Design not null ");
							//claimId = reportDto.getClaimId();
							List<ReceiptOfDocumentsDTO> rodDTO = new ArrayList<ReceiptOfDocumentsDTO>();
							rodDTO.add(reportDto);
							JRBeanCollectionDataSource a_jrDataSource = new JRBeanCollectionDataSource(rodDTO);
							WeakHashMap<String, Object> subReportPathMap = new WeakHashMap<String, Object>();
							String resourcePath = filePath + File.separator+ "templates" + File.separator;
							System.out.println("--resourcepath---"+ resourcePath);
							subReportPathMap.put("resourcePath", resourcePath);
							JasperPrint a_jasperPrint = JasperFillManager.fillReport(a_jasperReport,subReportPathMap, a_jrDataSource);


							if (a_jasperPrint == null) {
							} else {
								String a_pdfFileName = null;
								if (claimId != null) {
									a_pdfFileName = "GAL_" + template_Name+ "_" + claimId.replace("/", "_");
								} else {
									a_pdfFileName = "GAL_" + template_Name+ "_";
								}
								//if(SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.equalsIgnoreCase(template_Name)){
								List<JRPrintPage> jrPages = a_jasperPrint.getPages();

								if(jrPages != null && !jrPages.isEmpty()) {
									for (Iterator<JRPrintPage> i=jrPages.iterator(); i.hasNext();) {
										JRPrintPage page = i.next();
										if (page.getElements().size() == 1)
											i.remove();
									}
								}								
								//}
							String PDF_URL = filePath+ File.separator + "documents"+ File.separator + a_pdfFileName + "_"+ new Date().getTime() + ".pdf";

							System.out.println(PDF_URL);
							JasperExportManager.exportReportToPdfFile(a_jasperPrint, PDF_URL);

							return PDF_URL;
							}
						} else {
							return StringUtils.EMPTY;
						}
					}
				}
			} else {
				System.out.println("Invalid template Name or List Empty");
				return StringUtils.EMPTY;
			}
		} catch (JRException jre) {
			jre.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return StringUtils.EMPTY;
	}
	
	public static void uploadGeneratedLetterToDMS(EntityManager entityManager,HashMap dataMap)
	{
		WeakHashMap fileUploadMap = SHAUtils.uploadFileToDMS((String)dataMap.get("filePath"));   
	}
	
	public NEFTQueryDetails getNEFTDetailsByReimbursementKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("NEFTQueryDetails.findByRODDKey")
				.setParameter("rodKey", rodKey);

		List<NEFTQueryDetails> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}

		
}