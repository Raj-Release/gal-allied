/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.mapper.ProcessClaimRequestBenefitsMapper;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.reimbursement.MedicalApprover;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Notification;

/**
 * @author ntv.vijayar
 *
 */
@Stateless
public class ProcessClaimRequestBenefitsService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(ProcessClaimRequestBenefitsService.class);
	
	public UploadDocumentDTO getReimbursementBenefitsValue(Long rodKey)
	{
		UploadDocumentDTO dto = null;
		
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKey");
		query = query.setParameter("rodKey", rodKey);
		
		List<ReimbursementBenefits> reimbBenefits = query.getResultList();
		
		if(null != reimbBenefits && !reimbBenefits.isEmpty())
		{
			dto = new UploadDocumentDTO();
			for (ReimbursementBenefits reimbursementBenefits : reimbBenefits) {
				entityManager.refresh(reimbursementBenefits);
				
				if(("HC").equalsIgnoreCase(reimbursementBenefits.getBenefitsFlag()))
				{
					dto.setHospitalCashNoofDays(String.valueOf(reimbursementBenefits.getNumberOfDaysBills()));
					dto.setHospitalCashPerDayAmt((String.valueOf(reimbursementBenefits.getPerDayAmountBills())));
					dto.setHospitalCashTotalClaimedAmt(String.valueOf(reimbursementBenefits.getTotalClaimAmountBills()));
					dto.setTreatmentPhysiotherapyFlag(reimbursementBenefits.getTreatmentForPhysiotherapy());
					dto.setTreatmentGovtHospFlag(reimbursementBenefits.getTreatmentForGovtHosp());
					dto.setHospitalBenefitFlag(reimbursementBenefits.getBenefitsFlag());
					dto.setHospitalBenefitKey(reimbursementBenefits.getKey());
				}
				else
				{  
					dto.setPatientCareBenefitFlag(reimbursementBenefits.getBenefitsFlag());
					dto.setPatientCareNoofDays(String.valueOf(reimbursementBenefits.getNumberOfDaysBills()));
					dto.setPatientCarePerDayAmt(String.valueOf(reimbursementBenefits.getPerDayAmountBills()));
					dto.setPatientCareTotalClaimedAmt(String.valueOf(reimbursementBenefits.getTotalClaimAmountBills()));
					dto.setPatientBenefitKey(reimbursementBenefits.getKey());
				}
			}
		}
		return dto;
	}
	
	public ReimbursementBenefits getReimbursementBefitsByKey(Long key)
	{
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByKey");
		query = query.setParameter("key", key);
		if (key !=  null){
			List<ReimbursementBenefits> reimbBenefits = query.getResultList();
			if(reimbBenefits != null && !reimbBenefits.isEmpty()) {
				return reimbBenefits.get(0);
			}
		}
		return null;
	}
	
	/*public DocAcknowledgement getDocAckObject(Long key)
	{
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("key", key);
		ReimbursementBenefits reimbBenefits = (ReimbursementBenefits)query.getSingleResult();
		return reimbBenefits;
	}*/
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitProcessClaimRequestBenefits(ReceiptOfDocumentsDTO rodDTO) {
		try
		{
			log.info("Submit Claim Hospital Cash Medical Request ---------------" + (rodDTO.getNewIntimationDTO() != null ? rodDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
			Reimbursement reimbursement = saveProcessClaimRequestBenefitsValues(rodDTO);
			String outCome = "";
			if(null != rodDTO && rodDTO.getIsMedicalScreen()){
				Boolean isFvrOrInvesInitiated = false;
				Boolean isZonalReview = false;
				if(null != rodDTO.getPreauthDTO().getIsParallelInvFvrQuery() && !(rodDTO.getPreauthDTO().getIsParallelInvFvrQuery()) || 
						(null != rodDTO.getPreauthDTO().getScreenName() && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(rodDTO.getPreauthDTO().getScreenName())))){
					outCome = getOutComeForMedicalApproval(rodDTO);
				} else{
					isFvrOrInvesInitiated = true;
					outCome = getOutcomeForClaimRequestWaitForInput(rodDTO);
					updateStageInformation(reimbursement,rodDTO.getPreauthDTO());
				}
				
				rodDTO.setBpmnOutCome(outCome);
				submitClaimRequestTaskToDBProcedure(rodDTO.getPreauthDTO(),isZonalReview,outCome,reimbursement);
			}
			else if(null != rodDTO && rodDTO.getIsBillingScreen())
			{
				outCome = getOutComeForClaimBilling(rodDTO);
				rodDTO.setBpmnOutCome(outCome);
				submitBillingBenefitsTaskToDB(rodDTO,reimbursement);
			}
			else if ((null != rodDTO && !rodDTO.getIsBillingScreen())&& (!rodDTO.getIsMedicalScreen()))
			{
				outCome = getOutComeForFinancial(rodDTO);
				rodDTO.setBpmnOutCome(outCome);
				submitFinancialBenefitsTaskToDB(rodDTO,reimbursement);
			}
			return reimbursement;
			
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		return null;
		
	}
	
	
	private Reimbursement saveProcessClaimRequestBenefitsValues(ReceiptOfDocumentsDTO rodDTO) 
	{	
		
		Reimbursement reimbursement = getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
		reimbursement.setFaDocumentVerifiedFlag((rodDTO.getDocumentDetails().getOriginalBillsReceived()) ? "Y" : "N");
		if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getHospitalCashDueTo() && null != rodDTO.getDocumentDetails().getHospitalCashDueTo().getId()){
			
			reimbursement.setProdBenefitDueToID(rodDTO.getDocumentDetails().getHospitalCashDueTo().getId());
		}
       if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash() && null != rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId()){
			
			reimbursement.setProdDiagnosisID(rodDTO.getDocumentDetails().getDiagnosisHospitalCash().getId());
		}
		Double totalAmountPayable = 0d;
		
		 if(null != rodDTO && null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getPatientDayCare() 
				 && rodDTO.getDocumentDetails().getPatientDayCare().equals(true)){
			 reimbursement.setPhcDayCareFlag(SHAConstants.YES_FLAG);
			 if(rodDTO.getDocumentDetails().getPatientDayCareDueTo().getId() != null){
				 reimbursement.setPhcDayCareID(rodDTO.getDocumentDetails().getPatientDayCareDueTo().getId());;
			 }
		 }
		 else{
			 reimbursement.setPhcDayCareFlag(SHAConstants.N_FLAG);
		 }
		 
		if(null != reimbursement)
		{	
			DocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
			docAck.setHospitalisationFlag(rodDTO.getDocumentDetails().getHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(rodDTO.getDocumentDetails().getPartialHospitalizationFlag());
			docAck.setPreHospitalisationFlag(rodDTO.getDocumentDetails().getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(rodDTO.getDocumentDetails().getPostHospitalizationFlag());
			docAck.setLumpsumAmountFlag(rodDTO.getDocumentDetails().getLumpSumAmountFlag());
			docAck.setHospitalCashFlag(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag());
			docAck.setPatientCareFlag(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag());
			docAck.setProdHospBenefitFlag(rodDTO.getDocumentDetails().getHospitalCashFlag());
			
			entityManager.merge(docAck);
			entityManager.flush();
			entityManager.refresh(docAck);
		}
		
		if(null != rodDTO.getAddOnBenefitsDTO() && !rodDTO.getAddOnBenefitsDTO().isEmpty())
		{
			
			Status status = new Status();
		    Double hospitalCashPayableAmt = 0d;
		    Double patientCarePayableAmt = 0d;
			
			UploadDocumentDTO uploadDTO = rodDTO.getUploadDocumentsDTO();
			if(null != rodDTO.getAddOnBenefitsDTO() && !rodDTO.getAddOnBenefitsDTO().isEmpty())
			{
				Stage stage = null;
				for (AddOnBenefitsDTO benefitsDTO : rodDTO.getAddOnBenefitsDTO()) {
	
					ReimbursementBenefits reimbursementHospBenefits = ProcessClaimRequestBenefitsMapper.getInstance().getAddOnBenefits(benefitsDTO);
					if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
							.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
                      || (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){

						//ReimbursementBenefits reimbursementHospBenefits = ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
						ReimbursementBenefits reimbursementBenefits = getReimBenefitsByRODPHCKey(rodDTO.getKey(),benefitsDTO.getPhcBenefitId().longValue());
						
						reimbursementHospBenefits.setKey(reimbursementBenefits.getKey());
						reimbursementHospBenefits.setReimbursementKey(reimbursementBenefits.getReimbursementKey());
//						reimbursementHospBenefits.setTreatmentForPhysiotherapy(benefitsDTO.get());
						
						reimbursementHospBenefits.setNumberOfDaysBills(Long.valueOf(benefitsDTO.getAllowedNoOfDays()));
						reimbursementHospBenefits.setPerDayAmountBills(Double.valueOf(benefitsDTO.getEligiblePerDayAmt()));
						reimbursementHospBenefits.setTotalClaimAmountBills(Double.valueOf((benefitsDTO.getTotalClaimedAmount())));
						reimbursementHospBenefits.setDisallowanceRemarks(benefitsDTO.getDisallowanceRemarks());
						reimbursementHospBenefits.setTotalNoOfDays(Long.valueOf(benefitsDTO.getTotalNoOfDaysClaimed()));
						reimbursementHospBenefits.setBenefitsFlag(reimbursementBenefits.getBenefitsFlag());
						reimbursementHospBenefits.setProductBenefitID(reimbursementBenefits.getProductBenefitID());
						reimbursementHospBenefits.setCreatedBy(reimbursementHospBenefits.getCreatedBy());
						reimbursementHospBenefits.setCreatedDate(reimbursementHospBenefits.getCreatedDate());
						
						if (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))
								{
									reimbursementHospBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
								}
						else
						{
							reimbursementHospBenefits.setDeletedFlag(SHAConstants.N_FLAG);
							reimbursementHospBenefits.setModifiedBy(rodDTO.getStrUserName());
							reimbursementHospBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						}
						
						populateBenefitsData(uploadDTO ,  reimbursementHospBenefits  ,  benefitsDTO);
						
						if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) || rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
							reimbursementHospBenefits.setPayableAmount(0d);
							reimbursementHospBenefits.setTotalClaimAmountBills(0d);
						}
						
						if(null != reimbursementHospBenefits.getPayableAmount()) {
							totalAmountPayable += reimbursementHospBenefits.getPayableAmount();
							hospitalCashPayableAmt += reimbursementHospBenefits.getPayableAmount();
						}
						
						entityManager.merge(reimbursementHospBenefits);
						entityManager.flush();
						entityManager.clear();
						//entityManager.refresh(reimbursementHospBenefits);
						
						if(reimbursement != null){
							
							if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
									|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) || rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
								reimbursement.setCurrentProvisionAmt(0d);
								if(rodDTO.getIsBillingScreen()){
									status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
									stage = new Stage();
									stage.setKey(ReferenceTable.BILLING_STAGE);
									reimbursement.setStatus(status);
									reimbursement.setStage(stage);
									
									saveCancelRODValues(reimbursement, ReferenceTable.BILLING_CANCEL_ROD,ReferenceTable.BILLING_STAGE);
									
								}/*else{
									status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
									Stage stage = new Stage();
									stage.setKey(ReferenceTable.FINANCIAL_STAGE);
									reimbursement.setStatus(status);
									reimbursement.setStage(stage);
								}*/
								//ad on test
								if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setBillingApprovedAmount(totalAmountPayable);
									
								}
								else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setFinancialApprovedAmount(totalAmountPayable);
									
								}
								
								entityManager.merge(reimbursement);
								entityManager.flush();
								entityManager.clear();
							}else{
								if(reimbursementHospBenefits.getPayableAmount() != null){
									reimbursement.setCurrentProvisionAmt(reimbursementHospBenefits.getPayableAmount());
									//if(rodDTO.getIsBillingScreen()){
									if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
										status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
										reimbursement.setStatus(status);
									} 
									else if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR))
									{
										status.setKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
										reimbursement.setStatus(status);
									}
									/**
									 * This below status will change once financial approve status is implemented
									 * for financial benefits screen.
									 * */
									
									else if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
										status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
										reimbursement.setStatus(status);
										reimbursement.setProdBenefitAmount(totalAmountPayable);
									}
									stage = new Stage();
									if(null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen()){
										stage.setKey(ReferenceTable.BILLING_STAGE);
									}
									else if(null != rodDTO.getIsMedicalScreen() && rodDTO.getIsMedicalScreen())
									{
										stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
									}
									else if(!rodDTO.getIsBillingScreen() && !rodDTO.getIsMedicalScreen()){
										stage.setKey(ReferenceTable.FINANCIAL_STAGE);
									}
									//add on test
									if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
									{
										
										if(rodDTO.getStrUserName() != null){
											reimbursement.setModifiedBy(rodDTO.getStrUserName());
										}
										reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
										//reimbursement.setBillingApprovedAmount(totalAmountPayable);
									
									}
									else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
									{
										if(rodDTO.getStrUserName() != null){
											reimbursement.setModifiedBy(rodDTO.getStrUserName());
										}
										reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
										//reimbursement.setFinancialApprovedAmount(totalAmountPayable);
									
									}
									
									
//									reimbursement.setStage(stage);
								    entityManager.merge(reimbursement);
								    entityManager.flush();
								    entityManager.clear();
								}
						   }
						}
					}
					else if((ReferenceTable.HOSPITAL_CASH).equalsIgnoreCase(benefitsDTO.getParticulars()))
					{
						//ReimbursementBenefits reimbursementHospBenefits = ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(uploadDTO.getHospitalBenefitKey());
						
						reimbursementHospBenefits.setKey(reimbursementBenefits.getKey());
						reimbursementHospBenefits.setReimbursementKey(reimbursementBenefits.getReimbursementKey());
						reimbursementHospBenefits.setTreatmentForPhysiotherapy(uploadDTO.getTreatmentPhysiotherapyFlag());
						
						reimbursementHospBenefits.setNumberOfDaysBills(Long.valueOf(uploadDTO.getHospitalCashNoofDays()));
						reimbursementHospBenefits.setPerDayAmountBills(Double.valueOf(uploadDTO.getHospitalCashPerDayAmt()));
						reimbursementHospBenefits.setTotalClaimAmountBills(Double.valueOf((uploadDTO.getHospitalCashTotalClaimedAmt())));
						reimbursementHospBenefits.setBenefitsFlag(reimbursementBenefits.getBenefitsFlag());
						
						if (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))
								{
									reimbursementHospBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
								}
						else
						{
							reimbursementHospBenefits.setDeletedFlag(SHAConstants.N_FLAG);
						}
						
						populateBenefitsData(uploadDTO ,  reimbursementHospBenefits  ,  benefitsDTO);
						
						if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))){
							reimbursementHospBenefits.setPayableAmount(0d);
							reimbursementHospBenefits.setTotalClaimAmountBills(0d);
						}
						
						if(null != reimbursementHospBenefits.getPayableAmount()) {
							totalAmountPayable += reimbursementHospBenefits.getPayableAmount();
							hospitalCashPayableAmt += reimbursementHospBenefits.getPayableAmount();
						}
						
						entityManager.merge(reimbursementHospBenefits);
						entityManager.flush();
						entityManager.clear();
						//entityManager.refresh(reimbursementHospBenefits);
						
						if(reimbursement != null){
							
							if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
									|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))){
								reimbursement.setCurrentProvisionAmt(0d);
								if(rodDTO.getIsBillingScreen()){
									status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
									stage = new Stage();
									stage.setKey(ReferenceTable.BILLING_STAGE);
									reimbursement.setStatus(status);
									reimbursement.setStage(stage);
									
									saveCancelRODValues(reimbursement, ReferenceTable.BILLING_CANCEL_ROD,ReferenceTable.BILLING_STAGE);
									
								}/*else{
									status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
									Stage stage = new Stage();
									stage.setKey(ReferenceTable.FINANCIAL_STAGE);
									reimbursement.setStatus(status);
									reimbursement.setStage(stage);
								}*/
								//ad on test
								if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setBillingApprovedAmount(totalAmountPayable);
									
								}
								else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setFinancialApprovedAmount(totalAmountPayable);
									
								}
								
								entityManager.merge(reimbursement);
								entityManager.flush();
								entityManager.clear();
							}else{
								if(reimbursementHospBenefits.getPayableAmount() != null){
									reimbursement.setCurrentProvisionAmt(reimbursementHospBenefits.getPayableAmount());
									//if(rodDTO.getIsBillingScreen()){
									if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
										status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
										reimbursement.setStatus(status);
									} 
									else if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR))
									{
										status.setKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
										reimbursement.setStatus(status);
									}
									/**
									 * This below status will change once financial approve status is implemented
									 * for financial benefits screen.
									 * */
									
									else if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
										status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
										reimbursement.setStatus(status);
									}
									stage = new Stage();
									if(null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen()){
										stage.setKey(ReferenceTable.BILLING_STAGE);
									}
									else
									{
										stage.setKey(ReferenceTable.FINANCIAL_STAGE);
									}
									//add on test
									if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
									{
										
										if(rodDTO.getStrUserName() != null){
											reimbursement.setModifiedBy(rodDTO.getStrUserName());
										}
										reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
										//reimbursement.setBillingApprovedAmount(totalAmountPayable);
									
									}
									else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
									{
										if(rodDTO.getStrUserName() != null){
											reimbursement.setModifiedBy(rodDTO.getStrUserName());
										}
										reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
										//reimbursement.setFinancialApprovedAmount(totalAmountPayable);
									
									}
									
									
									reimbursement.setStage(stage);
								    entityManager.merge(reimbursement);
								    entityManager.flush();
								    entityManager.clear();
								}
						   }
						}
					
						
					}
					
					else if((ReferenceTable.PATIENT_CARE).equalsIgnoreCase(benefitsDTO.getParticulars()))
					{
						
						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(rodDTO.getUploadDocumentsDTO().getPatientBenefitKey());
						
						reimbursementHospBenefits.setKey(reimbursementBenefits.getKey());
						reimbursementHospBenefits.setReimbursementKey(reimbursementBenefits.getReimbursementKey());
						reimbursementHospBenefits.setTreatmentForPhysiotherapy(uploadDTO.getTreatmentPhysiotherapyFlag());
						
						reimbursementHospBenefits.setNumberOfDaysBills(Long.valueOf(uploadDTO.getPatientCareNoofDays()));
						reimbursementHospBenefits.setPerDayAmountBills(Double.valueOf(uploadDTO.getPatientCarePerDayAmt()));
						reimbursementHospBenefits.setTotalClaimAmountBills(Double.valueOf((uploadDTO.getPatientCareTotalClaimedAmt())));
						reimbursementHospBenefits.setBenefitsFlag(reimbursementBenefits.getBenefitsFlag());
						
						populateBenefitsData(uploadDTO ,  reimbursementHospBenefits  ,  benefitsDTO);
						
						if (null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))
								{
									reimbursementHospBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
								}
						else
						{
							reimbursementHospBenefits.setDeletedFlag(SHAConstants.N_FLAG);
						}
						
						if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
								|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))){
							reimbursementHospBenefits.setPayableAmount(0d);
							reimbursementHospBenefits.setTotalClaimAmountBills(0d);
						}
						
						entityManager.merge(reimbursementHospBenefits);
						entityManager.flush();
						entityManager.clear();
						
						if(null != reimbursementHospBenefits.getPayableAmount()) {
							totalAmountPayable += reimbursementHospBenefits.getPayableAmount();
							patientCarePayableAmt += reimbursementHospBenefits.getPayableAmount();
						}
						
						
						if(reimbursement != null){
							if(reimbursementHospBenefits.getPayableAmount() != null){
								reimbursement.setCurrentProvisionAmt(reimbursementHospBenefits.getPayableAmount());
								
								if(rodDTO.getIsBillingScreen()){
									status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
									reimbursement.setStatus(status);
								}else{
									status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
									reimbursement.setStatus(status);
								}
								//add on test
								if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setBillingApprovedAmount(totalAmountPayable);
								
								}
								else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
								{
									if(rodDTO.getStrUserName() != null){
										reimbursement.setModifiedBy(rodDTO.getStrUserName());
									}
									reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
									//reimbursement.setFinancialApprovedAmount(totalAmountPayable);
									
								}
								if(rodDTO.getDocumentDetails().getHospitalCashDueTo() != null ){
									reimbursement.setProdBenefitDueToID(rodDTO.getDocumentDetails().getHospitalCashDueTo().getId());
								}
								
							    entityManager.merge(reimbursement);
							    entityManager.flush();
							    entityManager.clear();
							}
						}
						//entityManager.refresh(reimbursementHospBenefits);
						
						List<ReimbursementBenefitsDetails> reimbBenefitsList = getPatientCareTableValues(rodDTO.getUploadDocumentsDTO().getPatientBenefitKey());
						
						List<PatientCareDTO> patientList = rodDTO.getUploadDocumentsDTO().getPatientCareDTO();
						
						if(null != reimbBenefitsList && !reimbBenefitsList.isEmpty() && null != patientList && !patientList.isEmpty()) 
						{
							for (ReimbursementBenefitsDetails benefitsDetails : reimbBenefitsList) {
								
								for (PatientCareDTO patientCareDTO : patientList) {
									
									if(benefitsDetails.getKey().equals(patientCareDTO.getKey()))
									{
										benefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
										benefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
										
										/*if (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
												|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))
												{
													benefitsDetails.setDeletedFlag(SHAConstants.YES_FLAG);
												}
										else
										{
											benefitsDetails.setDeletedFlag(SHAConstants.N_FLAG);
										}
										*/
										entityManager.merge(benefitsDetails);
										entityManager.flush();
										entityManager.clear();
										
										break;
									}
								}
								
							}
						}
						
						/*if(null !=  patientList && !patientList.isEmpty())
						{
							 for (PatientCareDTO patientCareDTO : patientList) 
							 {
								ReimbursementBenefitsDetails reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
								reimbursementBenefitDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
								reimbursementBenefitDetails.setEngagedTo(patientCareDTO.getEngagedTo());
								reimbursementBenefitDetails.setReimbursementBenefits(reimbursementHospBenefits);
								
								entityManager.merge(reimbursementBenefitDetails);
								entityManager.flush();
								//entityManager.refresh(reimbursementBenefitDetails);
							 }
						}*/
						
					}
				}
			}
			
		if(null != reimbursement)
		{
			if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null && (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
					|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) || rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
				reimbursement.setCurrentProvisionAmt(0d);
				hospitalCashPayableAmt = 0d;
				patientCarePayableAmt = 0d;
			}
			else
			{
				reimbursement.setCurrentProvisionAmt(totalAmountPayable);
				reimbursement.setApprovedAmount(totalAmountPayable);
			}
			if(null != rodDTO && null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen() && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(rodDTO.getDocumentDetails().getStatusId()))
			{
				reimbursement.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setBillingApprovedAmount(totalAmountPayable);
			}
			else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
			{
				reimbursement.setFinancialCompletedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setFinancialApprovedAmount(totalAmountPayable);
			}
			else if(null != rodDTO && null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS).equals(rodDTO.getDocumentDetails().getStatusId()))
			{
				reimbursement.setMedicalCompletedDate(new Timestamp(System.currentTimeMillis()));
				reimbursement.setApprovedAmount(totalAmountPayable);
			}
			
			if(rodDTO.getStrUserName() != null){
				reimbursement.setModifiedBy(rodDTO.getStrUserName());
			}
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setAddOnCoversApprovedAmount(hospitalCashPayableAmt);
			reimbursement.setOptionalApprovedAmount(patientCarePayableAmt);
			
			if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getDateOfAdmission() != null){
				reimbursement.setDateOfAdmission(rodDTO.getDocumentDetails().getDateOfAdmission());
			}
			
			if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getDateOfDischarge() != null){
				reimbursement.setDateOfDischarge(rodDTO.getDocumentDetails().getDateOfDischarge());
			}
			
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
		}
		}
		if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR))
		{
			saveCoordinatorValues (reimbursement, rodDTO);
		}
		
		if(null != rodDTO.getIsBillingScreen() && rodDTO.getIsBillingScreen())
		{
			if (null != reimbursement.getClaim()) {
				Claim claim  = reimbursement.getClaim();
				claim.setDataOfAdmission(reimbursement.getDateOfAdmission());
				claim.setDataOfDischarge(reimbursement.getDateOfDischarge());
				claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(claim);
				entityManager.flush();
				entityManager.clear();
			}
		}
		
		if(null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (null != rodDTO.getIsMedicalScreen() && !rodDTO.getIsMedicalScreen()) )
		{
			rodDTO.setBenefitsApprovedAmt(totalAmountPayable);
			saveFABenefitsValues(rodDTO, reimbursement);
		}
		
		if(null != rodDTO.getIsMedicalScreen() && rodDTO.getIsMedicalScreen())
		{
			rodDTO.setBenefitsApprovedAmt(totalAmountPayable);
			saveMABenefitsValues(rodDTO, reimbursement);
		}
		
		return reimbursement;
		
	}
	
	
	private void saveFABenefitsValues(ReceiptOfDocumentsDTO rodDTO, Reimbursement reimbursement)
	{
		

		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_REJECT_STATUS)) {
			createReimbursementRejection(rodDTO,reimbursement);	
		}

		if (null != reimbursement.getClaim()) {
			Claim claim  = reimbursement.getClaim();
			claim.setDataOfAdmission(reimbursement.getDateOfAdmission());
			claim.setDataOfDischarge(reimbursement.getDateOfDischarge());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
			claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(claim);
		}
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
			reimbursement.setBillingRemarks(rodDTO.getDocumentDetails().getBillingRemarks());
		}
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
			reimbursement.setMedicalRemarks(rodDTO.getDocumentDetails().getMedicalApproverRemarks());
			
			Status status = new Status();
			status.setKey(rodDTO.getDocumentDetails().getStatusId());
			reimbursement.setStatus(status);
				String remarks = rodDTO.getPreauthDTO().getPreauthMedicalDecisionDetails()
						.getReasonForRefering();
				referToMedicalForFinancial(rodDTO, reimbursement, remarks);
		}
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
			reimbursement.setFinancialApprovalRemarks(rodDTO.getDocumentDetails().getFinancialRemarks());
			/**
			 * As per sathish  sir if approved amt is 0 , then
			 * payment values will not be saved.
			 * */
			if(null != reimbursement && 0l != reimbursement.getFinancialApprovedAmount())
				saveClaimPaymentValues(rodDTO,reimbursement);
		}
		
		//added for new product076
		if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
				.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
              || (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			WeakHashMap dataMap = rodDTO.getPreauthDTO().getFilePathAndTypeMap();
			if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
					ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
				dataMap.put("intimationNumber",reimbursement.getClaim().getIntimation().getIntimationId());
				dataMap.put("claimNumber",reimbursement.getClaim().getClaimId());
				dataMap.put("createdBy", rodDTO.getStrUserName());
				if(null != reimbursement.getClaim().getClaimType())
				{
				 if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(reimbursement.getClaim().getClaimType().getKey()) && dataMap != null)
					{
						dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
					}
				}
				
				if(dataMap != null) {
					String billSummaryFilePath = (String) dataMap.get("BillSummaryDocFilePath");
					String billSummaryDocType = (String) dataMap.get("BillSummaryDocType");
					dataMap.put("filePath", billSummaryFilePath);
					dataMap.put("docType", billSummaryDocType);
					uploadGeneratedLetterToDMS(dataMap);
					SHAUtils.setClearReferenceData(dataMap);
			}
		  }
		}
		
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_QUERY_STATUS))
		{
			saveReimbursementQueryValues(rodDTO,reimbursement);
			Status status = new Status();
			status.setKey(ReferenceTable.FINANCIAL_QUERY_STATUS);
			reimbursement.setStatus(status);
		}
		
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS))
		{
			saveCoordinatorValues(reimbursement, rodDTO);
		}
		
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.FINANCIAL_CANCEL_ROD)) {
			
			saveCancelRODValues(reimbursement, ReferenceTable.FINANCIAL_CANCEL_ROD,ReferenceTable.FINANCIAL_STAGE);
			
			/*reimbursement.setNatureOfTreatment(null);
			reimbursement.setDateOfDischarge(null);
			reimbursement.setTreatmentRemarks(null);
			reimbursement.setTreatmentType(null);
			reimbursement.setApprovedAmount(0d);
			reimbursement.setBillingApprovedAmount(0d);
			reimbursement.setFinancialApprovalRemarks(null);
			reimbursement.setFinancialApprovedAmount(0d);
			reimbursement.setBillingRemarks(null);
			reimbursement.setBillingCompletedDate(null);
			reimbursement.setApprovalRemarks(null);
			reimbursement.setCriticalIllness(null);
			reimbursement.setConsultationDate(null);
			reimbursement.setDateOfDeath(null);
			reimbursement.setDateOfDelivery(null);
			reimbursement.setDoctorNote(null);
			reimbursement.setDomicillary(null);
			reimbursement.setFirNumber(null);
			reimbursement.setFirstDiseaseDetectedDate(null);
			reimbursement.setAttachedPoliceReport(null);
			reimbursement.setHopsitaliztionDueto(null);
			reimbursement.setPatientStatus(null);
			reimbursement.setNumberOfDays(0);
			reimbursement.setMedicalRemarks(null);*/
		}
		
		if (reimbursement.getKey() != null) {
			Status status = getStatusByKey(rodDTO.getDocumentDetails().getStatusId());
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStatus(status);
			//IMSSUPPOR-30741
			if(rodDTO.getDocumentDetails().getHospitalCashDueTo() !=null && rodDTO.getDocumentDetails().getHospitalCashDueTo().getId()!=null){
                reimbursement.setProdBenefitDueToID(rodDTO.getDocumentDetails().getHospitalCashDueTo().getId());
			}
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.FINANCIAL_STAGE);
			reimbursement.setModifiedBy(rodDTO.getStrUserName());
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStage(stage);
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
		} 
		
	}
	
	
	
	private void saveCancelRODValues(Reimbursement reimbursement,Long statusKey,Long stageKey)
	{
		reimbursement.setNatureOfTreatment(null);
		reimbursement.setDateOfDischarge(null);
		reimbursement.setTreatmentRemarks(null);
		reimbursement.setTreatmentType(null);
		reimbursement.setApprovedAmount(0d);
		reimbursement.setBillingApprovedAmount(0d);
		reimbursement.setFinancialApprovalRemarks(null);
		reimbursement.setFinancialApprovedAmount(0d);
		reimbursement.setBillingRemarks(null);
		reimbursement.setBillingCompletedDate(null);
		reimbursement.setApprovalRemarks(null);
		reimbursement.setCriticalIllness(null);
		reimbursement.setConsultationDate(null);
		reimbursement.setDateOfDeath(null);
		reimbursement.setDateOfDelivery(null);
		reimbursement.setDoctorNote(null);
		reimbursement.setDomicillary(null);
		reimbursement.setFirNumber(null);
		reimbursement.setFirstDiseaseDetectedDate(null);
		reimbursement.setAttachedPoliceReport(null);
		reimbursement.setHopsitaliztionDueto(null);
		reimbursement.setPatientStatus(null);
		reimbursement.setNumberOfDays(0);
		reimbursement.setMedicalRemarks(null);
	
		Stage stage = new Stage();
		stage.setKey(stageKey);
		
		Status status = new Status();
		status.setKey(statusKey);
		
		reimbursement.setStatus(status);
		reimbursement.setStage(stage);
		
		entityManager.merge(reimbursement);
		entityManager.flush();
		entityManager.clear();
		
		
		List<RODDocumentSummary> docSummaryList = getRodDocSummaryByReimbursement(reimbursement.getKey());
				if(null != docSummaryList && !docSummaryList.isEmpty())
				{
					for (RODDocumentSummary rodDocumentSummary : docSummaryList) {
						rodDocumentSummary.setDeletedFlag(SHAConstants.YES_FLAG);
						if(null != rodDocumentSummary.getKey())
						{
							entityManager.merge(rodDocumentSummary);
							entityManager.flush();
							entityManager.clear();
							List<RODBillDetails> rodBillDetailsList = getRODBillDetailsForDocSummary(rodDocumentSummary.getKey());
									if(null != rodBillDetailsList && !rodBillDetailsList.isEmpty())
									{
										for (RODBillDetails rodBillDetails : rodBillDetailsList) {
											rodBillDetails.setDeletedFlag(SHAConstants.YES_FLAG);
											entityManager.merge(rodBillDetails);
											entityManager.flush();
											entityManager.clear();
										}
									}
						}
					}
				}
	}
	
	
	private List<RODDocumentSummary> getRodDocSummaryByReimbursement(Long reimbursementKey)
	{
		Query query = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", reimbursementKey);
		List<RODDocumentSummary> docSummaryList = query.getResultList();
		if(null != docSummaryList && !docSummaryList.isEmpty())
		{
			for (RODDocumentSummary rodDocumentSummary : docSummaryList) {
					entityManager.refresh(rodDocumentSummary);
			}
			return docSummaryList;
		}
		return null;
	}
	 
	private List<RODBillDetails> getRODBillDetailsForDocSummary(Long summaryKey)
	{
		Query query = entityManager.createNamedQuery("RODBillDetails.findByRodDocumentSummaryKey");
		query = query.setParameter("summaryKey", summaryKey);
		List<RODBillDetails> rodBillDetails = query.getResultList();
		if(null != rodBillDetails && !rodBillDetails.isEmpty())
		{
			for (RODBillDetails rodBillDetails2 : rodBillDetails) {
				entityManager.refresh(rodBillDetails2);
			}
			return rodBillDetails;
		}
		return null;
	}
	
	private void saveReimbursementQueryValues(ReceiptOfDocumentsDTO rodDTO, Reimbursement reimbursement)
	{
		Status queryStatus = new Status();
		com.shaic.domain.preauth.Stage queryStage = new com.shaic.domain.preauth.Stage();
		if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId())
		{
			queryStatus.setKey(ReferenceTable.FINANCIAL_QUERY_STATUS);
		}
		queryStage.setKey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
		/*stage.setKey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
		status.setKey(ReferenceTable.FINANCIAL_QUERY_STATUS);*/
		//reimbursement.setStatus(status);
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		
		ReimbursementQuery reimbursementQuery = new ReimbursementQuery();
		reimbursementQuery.setQueryRemarks(rodDTO.getDocumentDetails().getQueryRemarks());
		reimbursementQuery.setStatus(queryStatus);
		reimbursementQuery.setStage(queryStage);
		reimbursementQuery.setReimbursement(reimbursement);
		reimbursementQuery.setModifiedBy(userNameForDB);

		String createdBy = rodDTO.getStrUserName();
		if (null != rodDTO.getStrUserName()
				&& rodDTO.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(rodDTO.getStrUserName(),
					15);
		}
		reimbursementQuery.setCreatedBy(createdBy);

		entityManager.persist(reimbursementQuery);
		entityManager.flush();
		entityManager.clear();
		rodDTO.getDocumentDetails().setReimbursementQueryKey(reimbursementQuery.getKey());
	}
	
	private void saveClaimPaymentValues(ReceiptOfDocumentsDTO rodDTO, Reimbursement reimbursement)
	{
		ClaimPayment claimPayment = new ClaimPayment();
		//acknowledgementDocumentReceivedService.getReimbursementApprovedAmount(reimbursement.getKey());
		//claimPayment.setRodkey(reimbursement);
		claimPayment.setRodNumber(reimbursement.getRodNumber());
		claimPayment.setRodkey(reimbursement.getKey());
		Claim claim = reimbursement.getClaim() ;
		
		if(null != reimbursement.getClaim() && null != claim.getIntimation() && null != claim.getIntimation().getPolicy())
		{				
			
			Intimation intimation =  claim.getIntimation();
			Policy policy = intimation.getPolicy();
			claimPayment.setPioCode(policy.getHomeOfficeCode());
//			if(null != claim.getClaimType())
//				claimPayment.setClaimType(claim.getClaimType().getValue());
			DocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
			claimPayment.setPaymentCpuCode(intimation.getCpuCode().getCpuCode());
			if(null != reimbursement.getDocAcknowLedgement() && null != docAck.getDocumentReceivedFromId() 
					&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) && 
					claim != null && claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))

			{
				claimPayment.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
			}
			else
			{
				claimPayment.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					OrganaizationUnit masBranchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
					if(masBranchOffice != null && masBranchOffice.getCpuCode() != null){
						claimPayment.setPaymentCpuCode(Long.valueOf(masBranchOffice.getCpuCode()));
					}
				}
			}
			
			if(null != policy.getProduct())
				claimPayment.setProductCode(policy.getProduct().getCode());
			claimPayment.setIntimationNumber(intimation.getIntimationId());
			claimPayment.setClaimNumber(claim.getClaimId());
			claimPayment.setPolicyNumber(policy.getPolicyNumber());
			claimPayment.setPolicySysId(policy.getPolicySystemId());
			claimPayment.setRiskId(intimation.getInsured().getInsuredId());
			claimPayment.setApprovedAmount(reimbursement.getFinancialApprovedAmount());
			claimPayment.setTotalApprovedAmount(reimbursement.getFinancialApprovedAmount());
			if(null != intimation.getCpuCode())
				claimPayment.setCpuCode(intimation.getCpuCode().getCpuCode());
			claimPayment.setPanNumber(reimbursement.getPanNumber());
			claimPayment.setPayeeName(reimbursement.getPayeeName());
			if(null != rodDTO.getClaimDTO() && null != rodDTO.getClaimDTO().getNewIntimationDto() && null != rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto() )
				claimPayment.setHospitalCode(rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalCode());
			claimPayment.setEmailId(reimbursement.getPayeeEmailId());
			claimPayment.setProposerCode(policy.getProposerCode());
			claimPayment.setProposerName(policy.getProposerFirstName());	
		}
		
		claimPayment.setReasonForChange(reimbursement.getReasonForChange());
		claimPayment.setLegalHeirName(reimbursement.getLegalHeirFirstName());        
		
		claimPayment.setFaApprovalDate(new Timestamp(System.currentTimeMillis()));
		if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(reimbursement.getPaymentModeId()))
		{
			claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
			claimPayment.setAccountNumber(null);
			claimPayment.setBankCode(null);
			claimPayment.setIfscCode(null);
			claimPayment.setBankName(null);
			claimPayment.setBranchName(null);
		}
		else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(reimbursement.getPaymentModeId()))
		{
			
			claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
			claimPayment.setPayableAt(null);
			claimPayment.setAccountNumber(reimbursement.getAccountNumber());
			BankMaster masBank = getBankDetails(reimbursement.getBankId());
					if(null != masBank)
					{
			//claimPayment.setBankCode(reimbursement.getBankId());
						claimPayment.setIfscCode(masBank.getIfscCode());
						claimPayment.setBankName(masBank.getBankName());
						claimPayment.setBranchName(masBank.getBranchName());
					}
		}
		claimPayment.setCreatedBy(rodDTO.getStrUserName());
		claimPayment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		claimPayment.setPayableAt(reimbursement.getPayableAt());
		
		if(null != reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId())//.equals(ReferenceTable.RECEIVED_FROM_INSURED))
		{
			claimPayment.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
			
		}
		if(reimbursement.getDocAcknowLedgement() != null){
			claimPayment.setLastAckDate(reimbursement.getDocAcknowLedgement().getDocumentReceivedDate());
		}
		Status status = new Status();
		status.setKey(ReferenceTable.PAYMENT_NEW_STATUS);
		claimPayment.setStatusId(status);
		
		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PAYMENT_PROCESS_STAGE);
		claimPayment.setStageId(stage);
		
		/*claimPayment.setLotStatus(status);*/
		
		MastersValue paymentStatus = new MastersValue();
		paymentStatus.setKey(ReferenceTable.PAYMENT_STATUS_FRESH);
		
		claimPayment.setPaymentStatus(paymentStatus);
		claimPayment.setBatchHoldFlag(SHAConstants.N_FLAG);
		
		if(null != rodDTO.getBenefitsApprovedAmt())
			claimPayment.setBenefitsApprovedAmt(rodDTO.getBenefitsApprovedAmt());
		                                                                                                                                       
		entityManager.persist(claimPayment);
		entityManager.flush();
		entityManager.clear();
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
	
	private void createReimbursementRejection(ReceiptOfDocumentsDTO bean,
			Reimbursement reimbursement) {

		ReimbursementRejection reimbursementRejection = new ReimbursementRejection();

		if (reimbursement != null) {
			try {

//				reimbursement.setStatus(status);
				String strUserName = bean.getStrUserName();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
				Status status = new Status();
				status.setKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
				
//				reimbursement.setModifiedBy(userNameForDB);
//				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//				entityManager.merge(reimbursement);
//				entityManager.flush();

				reimbursementRejection.setRejectionRemarks(bean
						.getDocumentDetails()
						.getRejectionRemarks());
				reimbursementRejection.setReimbursement(reimbursement);
				reimbursementRejection.setStatus(status);
				reimbursementRejection.setStage(reimbursement.getStage());
                reimbursementRejection.setCreatedBy(userNameForDB);
				reimbursementRejection.setCreatedDate(new Timestamp(System
						.currentTimeMillis()));

//				String createdBy = bean.getStrUserName();
//				if (bean.getStrUserName() != null
//						&& bean.getStrUserName().length() > 15) {
//					createdBy = SHAUtils.getTruncateString(
//							bean.getStrUserName(), 15);
//				}
//				reimbursementRejection.setCreatedBy(createdBy);

				entityManager.persist(reimbursementRejection);
				entityManager.flush();
				entityManager.clear();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getOutComeForMedicalApproval(ReceiptOfDocumentsDTO rodDTO) {

		String outCome = "";
	

		if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_MEDICAL_TO_FA_PRD076;
		}  else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS;
		}else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS;
		}
		 //IMSSUPPOR-30901
		else if (rodDTO.getDocumentDetails().getStatusId()
                    .equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
            outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REJECT_STATUS;
		}
		return outCome;
	}
	
	private String getOutcomeForClaimRequestWaitForInput(ReceiptOfDocumentsDTO rodDTO){
		
		String outCome = "";
		outCome = SHAConstants.CLAIM_REQUEST_TO_WAIT_FOR_INPUT_OUTCOME;
		
		return outCome;
	}
	
	private String getOutComeForClaimBilling(ReceiptOfDocumentsDTO rodDTO) {

		String outCome = "";
	

		if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
			outCome = SHAConstants.OUTCOME_BILLING_APPROVE_STATUS;
		}  else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
			outCome = SHAConstants.OUTCOME_BILLING_REFER_TO_COORDINATOR;
		}else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
			outCome = SHAConstants.OUTCOME_BILLING_CANCEL_ROD;
		}
		return outCome;
	}
	
	private String getOutComeForFinancial(ReceiptOfDocumentsDTO rodDTO) {

		String outCome = "";
		 if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)) {
			outCome = SHAConstants.OUTCOME_FA_COORDINATOR_REPLY_STATUS;
		} else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_BILLING_STATUS;
		} else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
			outCome = SHAConstants.OUTCOME_FA_QUERY_STATUS;
		} else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_REJECT_STATUS)) {
			outCome = SHAConstants.OUTCOME_FA_REJECTION_STATUS;
		} else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_FA_APPROVE_STATUS;
		}else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
			outCome = SHAConstants.OUTCOME_FA_CANCEL_ROD_STATUS;
		}else if (rodDTO.getDocumentDetails().getStatusId()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_MEDICAL_APPROVER_STATUS;
		}
		return outCome;

	}
	
	
	private void saveCoordinatorValues(Reimbursement reimbursement,ReceiptOfDocumentsDTO rodDTO) 
	{
		//CoordinatorDTO coordinator = new CoordinatorDTO();
		Coordinator coordinator = new Coordinator();
		Status coorStatus = new Status();
		coordinator.setPolicy(reimbursement.getClaim().getIntimation().getPolicy());
		coordinator.setIntimation(reimbursement.getClaim().getIntimation());
		if(null != rodDTO.getDocumentDetails().getTypeOfCoordinatorRequest())
		{
			MastersValue typeOfCoordinatorRequest = new MastersValue();
			typeOfCoordinatorRequest.setKey(rodDTO.getDocumentDetails().getTypeOfCoordinatorRequest().getId());
			typeOfCoordinatorRequest.setValue(rodDTO.getDocumentDetails().getTypeOfCoordinatorRequest().getValue());
			coordinator.setCoordinatorRequestType(typeOfCoordinatorRequest);
		}
	//	coordinator.setCoordinatorRemarks(rodDTO.getDocumentDetails().getReasonForRefering());
		coordinator.setRequestorRemarks(rodDTO.getDocumentDetails().getReasonForRefering());
		coordinator.setActiveStatus(1l);
		coordinator.setStage(reimbursement.getStage());
		//IMSSUPPOR-30335
		if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId()
						&& rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)){
					coorStatus.setKey(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS);
				}
				if(null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)){
					coorStatus.setKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
				}
		coordinator.setStatus(coorStatus);
		coordinator.setClaim(reimbursement.getClaim());
		coordinator.setPolicy(reimbursement.getClaim().getIntimation()
				.getPolicy());
		coordinator.setIntimation(reimbursement.getClaim()
				.getIntimation());
		coordinator.setTransactionKey(reimbursement.getKey());
		coordinator.setTransactionFlag("R");
		coordinator.setCreatedBy(rodDTO.getStrUserName());
		
		entityManager.persist(coordinator);
		entityManager.flush();
		entityManager.clear();
		
	}
	
	private void populateBenefitsData(UploadDocumentDTO uploadDTO , ReimbursementBenefits reimbursementHospBenefits  , AddOnBenefitsDTO benefitsDTO)
	{
		reimbursementHospBenefits.setNumberOfDaysEligible(benefitsDTO.getEligibleNoofDays().longValue());
		reimbursementHospBenefits.setNumberOfDaysPayable(benefitsDTO.getEligiblePayableNoOfDays().longValue());
		reimbursementHospBenefits.setPerDayAmount(benefitsDTO.getEligiblePerDayAmt().doubleValue());
		reimbursementHospBenefits.setTotalAmount(benefitsDTO.getEligibleNetAmount().doubleValue());
		if(null != benefitsDTO.getCoPayPercentage())
		{
			reimbursementHospBenefits.setCopayPercentage(Double.valueOf(benefitsDTO.getCoPayPercentage().getValue()));
		}
		reimbursementHospBenefits.setCopayAmount(benefitsDTO.getCopayAmount().doubleValue());
		reimbursementHospBenefits.setNetAmount(benefitsDTO.getNetAmountAfterCopay().doubleValue());
		reimbursementHospBenefits.setLimitForPolicy(benefitsDTO.getLimitAsPerPolicy().doubleValue());
		reimbursementHospBenefits.setPayableAmount(benefitsDTO.getPayableAmount().doubleValue());
	}
	
	private List<ReimbursementBenefitsDetails> getPatientCareTableValues (Long key)
	{
		if(key != null) {
			Query query = entityManager
					.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
			query = query.setParameter("benefitsKey", key);
			List<ReimbursementBenefitsDetails> reimbBenefitsDetails = query.getResultList();
			return reimbBenefitsDetails;
		} 
		return null;
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
	
	public Reimbursement getReimbursementObjectByKey(Long key)
	{
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", key);
		
		List<Reimbursement> rodList = query.getResultList();
		
		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		
		//entityManager.refresh(reimbursement);
		return null;
	}
	
	public Reimbursement getFirstRODObjectByClaimKey(Long claimKey)
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
	
	public List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues(NewIntimationDto intimationDTO , Long claimKey ,Long rodKey,List<Double> copayList,PreauthDTO preauthDto
			)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
	//	Reimbursement reimbursement = getReimbursementObjectByKey(rodKey);
		Reimbursement reimbursement = getFirstRODObjectByClaimKey(claimKey);
		List<ReimbursementBenefits> reimbursementBenefits = getReimbursementBenefits(rodKey);
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		
		if(null != reimbursementBenefits && !reimbursementBenefits.isEmpty())
		{
			addOnBenefitsList = new ArrayList<AddOnBenefitsDTO>();
			AddOnBenefitsDTO addOnBenefitsdto = null;
			for (ReimbursementBenefits reimbursementBenefits2 : reimbursementBenefits) {
				Double sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(intimationDTO.getInsuredPatient().getInsuredId()),intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());
				List<Object> addOnBenefitsObj  = null;
				if(("HC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
				{
					addOnBenefitsdto = new AddOnBenefitsDTO();
					if(null != reimbursement && preauthDto == null)
					{
						addOnBenefitsdto.setDateOfAdmission((null != reimbursement.getDateOfAdmission() ? SHAUtils.formatDate(reimbursement.getDateOfAdmission()) : ""));
						addOnBenefitsdto.setDateOfDischarge((null != reimbursement.getDateOfDischarge() ? SHAUtils.formatDate(reimbursement.getDateOfDischarge()) : ""));
						addOnBenefitsdto.setAdmittedNoOfDays(calculateNoOfDaysAdmitted(reimbursement.getDateOfDischarge(), reimbursement.getDateOfAdmission()));
					}else if(preauthDto != null){
						if(preauthDto.getPreauthDataExtractionDetails().getAdmissionDate() != null){
							String admissionDate = SHAUtils.formatDate(preauthDto.getPreauthDataExtractionDetails().getAdmissionDate());
							addOnBenefitsdto.setDateOfAdmission(admissionDate);
							}
							if(preauthDto.getPreauthDataExtractionDetails().getDischargeDate() != null){
								String dischargeDate = SHAUtils.formatDate(preauthDto.getPreauthDataExtractionDetails().getDischargeDate());
								addOnBenefitsdto.setDateOfDischarge(dischargeDate);
							}
							addOnBenefitsdto.setAdmittedNoOfDays(calculateNoOfDaysAdmitted(preauthDto.getPreauthDataExtractionDetails().getDischargeDate(), 
									preauthDto.getPreauthDataExtractionDetails().getAdmissionDate()));
					}
					
					
					addOnBenefitsdto.setAllowedNoOfDays(Integer.valueOf(String.valueOf(reimbursementBenefits2.getNumberOfDaysBills())));
					addOnBenefitsdto.setTotalClaimedAmount(Integer.valueOf(String.valueOf(Math.round(reimbursementBenefits2.getTotalClaimAmountBills()))));
					addOnBenefitsObj = dbCalculationService.getAddOnBenefitsValues(rodKey, 
							intimationDTO.getInsuredPatient().getKey(),sumInsured , intimationDTO.getPolicy().getProduct().getKey(),"HC");
					populateDataCommonForHospitalCashAndPatientCare(addOnBenefitsObj,addOnBenefitsdto,copayList,true,reimbursementBenefits2);
					addOnBenefitsdto.setParticulars(ReferenceTable.HOSPITAL_CASH);
					addOnBenefitsList.add(addOnBenefitsdto);
				}
				else if(("PC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
				{
					List<ReimbursementBenefitsDetails> reimbursementBenefitDetails = getReimbursementBenefitsDetailsByBenefitsKey(reimbursementBenefits2.getKey());
					for (ReimbursementBenefitsDetails objReimbursementBenefitsDetails : reimbursementBenefitDetails) {
						addOnBenefitsdto = new AddOnBenefitsDTO();
						addOnBenefitsdto.setEngagedFrom(SHAUtils.formatDate(objReimbursementBenefitsDetails.getEngagedFrom()));
						addOnBenefitsdto.setEngagedTo(SHAUtils.formatDate(objReimbursementBenefitsDetails.getEngagedTo()));
						String noOfDaysAdmitted = calculateNoOfDaysAdmitted(objReimbursementBenefitsDetails.getEngagedTo(), objReimbursementBenefitsDetails.getEngagedFrom());
						addOnBenefitsdto.setAdmittedNoOfDays(noOfDaysAdmitted);
						
						Double totalClaimedAmt = reimbursementBenefits2.getPerDayAmountBills()*Long.valueOf(noOfDaysAdmitted);
						addOnBenefitsdto.setTotalClaimedAmount(Integer.valueOf(String.valueOf(Math.round(totalClaimedAmt))));
						addOnBenefitsObj = dbCalculationService.getAddOnBenefitsValues(rodKey, 
								intimationDTO.getInsuredPatient().getKey(),sumInsured , intimationDTO.getPolicy().getProduct().getKey(),"PC");
						populateDataCommonForHospitalCashAndPatientCare(addOnBenefitsObj,addOnBenefitsdto,copayList,false,reimbursementBenefits2);
						addOnBenefitsdto.setParticulars(ReferenceTable.PATIENT_CARE);
						addOnBenefitsList.add(addOnBenefitsdto);
					}	
				}	
			}
			
		}
		return addOnBenefitsList;

	}
	

	
	
	
	private void populateDataCommonForHospitalCashAndPatientCare(List<Object> addOnBenefitsObj , AddOnBenefitsDTO addOnBenefitsdto,List<Double> copayList,Boolean isHospitalCash,ReimbursementBenefits reimbursementBenefits2)
	{
		addOnBenefitsdto.setEntitledNoOfDays(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(1))));
		addOnBenefitsdto.setNoOfDaysPerHospitalization(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(2))));
		addOnBenefitsdto.setEntitlementPerDayAmt(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(3))));
		addOnBenefitsdto.setUtilizedNoOfDays(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(4))));
		addOnBenefitsdto.setBalanceAvailable(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(5))));
		SelectValue copaySelValue = new SelectValue();
		if(null != reimbursementBenefits2.getCopayPercentage())
		{
			String[] copayWithPercentage = reimbursementBenefits2.getCopayPercentage().toString().split("\\.");
			String copay = copayWithPercentage[0].trim();			
			copaySelValue.setId(Long.valueOf(copay));
			copaySelValue.setValue(String.valueOf(reimbursementBenefits2.getCopayPercentage()));
			addOnBenefitsdto.setCoPayPercentage(copaySelValue);
		}
		//addOnBenefitsdto.setC
		
		/**
		 * Added for add on beneifts bug. As per sathish sir suggestion, balance available = noOfDaysEntitled-noOfDaysUtilized.
		 * */
		/**
		 * This is been commented since there was a problem with no of days payable. Hence reverting back to old code.
		 * Implemented for ticket 4347.
		 * */
		//addOnBenefitsdto.setBalanceAvailable(getDiffOfTwoNumber(addOnBenefitsdto.getEntitledNoOfDays(), addOnBenefitsdto.getUtilizedNoOfDays()));
		
		//addOnBenefitsdto.setEligibleNoofDays(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(1))));
		Integer iEligiblePerDayAmt = Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(3)));
		
		//addOnBenefitsdto.setEligiblePerDayAmt(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(3))));
		addOnBenefitsdto.setEligiblePerDayAmt(Math.min(iEligiblePerDayAmt, reimbursementBenefits2.getPerDayAmountBills().intValue()));

		//Integer minDay1 = 0;
		
		if(isHospitalCash)
		{
			//minDay1 = Math.min(addOnBenefitsdto.getEntitledNoOfDays() , addOnBenefitsdto.getNoOfDaysPerHospitalization());
			Integer iReducedNoDays = (Integer)addOnBenefitsObj.get(6);
			if(null != addOnBenefitsdto.getAllowedNoOfDays()  && null != iReducedNoDays)
			{
				/**
				 * Added for ticket 4464, where allowed no of days is less than reduced no of
				 * days, then 0 will be populated.
				 * 
				 * **/
				if(addOnBenefitsdto.getAllowedNoOfDays() >= iReducedNoDays)
				{
					addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(addOnBenefitsdto.getAllowedNoOfDays() , iReducedNoDays));
				}
				else
				{
					addOnBenefitsdto.setEligibleNoofDays(0);
				}
			}
			else
			{
				addOnBenefitsdto.setEligibleNoofDays(0);
			}
			/*else
			{
				addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(0 , iReducedNoDays));
			}*/
		}
		else 
		{
			//minDay1 = Math.min(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) , addOnBenefitsdto.getNoOfDaysPerHospitalization());
			Integer iReducedNoDays = (Integer)addOnBenefitsObj.get(6);
			if(null != addOnBenefitsdto.getAdmittedNoOfDays() && null != iReducedNoDays)
			{
				if(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) >= iReducedNoDays)
				{
					addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) , iReducedNoDays));
				}
				else
				{
					addOnBenefitsdto.setEligibleNoofDays(0);
				}
			}
			else
			{ 
				addOnBenefitsdto.setEligibleNoofDays(0);
			}
				
		}
		Integer minDay1 =  Math.min(addOnBenefitsdto.getEligibleNoofDays() ,addOnBenefitsdto.getBalanceAvailable());
		Integer minDay2 = Math.min(minDay1  , addOnBenefitsdto.getNoOfDaysPerHospitalization());
		Integer eligiblePayableNoOfDays = Math.min(minDay1 , minDay2);
		addOnBenefitsdto.setEligiblePayableNoOfDays(eligiblePayableNoOfDays);	
		addOnBenefitsdto.setEligibleNetAmount(calculateNetAmt(addOnBenefitsdto.getEligiblePerDayAmt() , addOnBenefitsdto.getEligiblePayableNoOfDays()));
		addOnBenefitsdto.setProductCoPay((List<Double>)copayList);
		if(null != addOnBenefitsdto.getCoPayPercentage())
		{
			SelectValue copayPercentageValue =  addOnBenefitsdto.getCoPayPercentage();
			if(null != copayPercentageValue && null != copayPercentageValue.getValue())
			{
				calculateTotalFields(addOnBenefitsdto,Double.valueOf(copayPercentageValue.getValue()));
			}
		}
		else if(null != copayList && !copayList.isEmpty())
		{
			calculateTotalFields(addOnBenefitsdto,copayList.get(0));
		}
	}
	
	
	private Integer getDiffOfTwoNumber(Integer number1 , Integer number2)
	{
		Integer diff = 0;
		if(number1 > number2)
		{
			diff = number1 - number2;
		}
		else
		{
			diff = number2 - number1;
		}
		return diff;
	}
	
	private List<ReimbursementBenefitsDetails> getReimbursementBenefitsDetailsByBenefitsKey(Long claimBenefitkey)
	{
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query = query.setParameter("benefitsKey", claimBenefitkey);
		
		List<ReimbursementBenefitsDetails> reimbBenefitsDtls = query.getResultList();
		
		if(reimbBenefitsDtls != null && !reimbBenefitsDtls.isEmpty()) {
			for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDtls) {
				entityManager.refresh(reimbursementBenefitsDetails);
			}
		}
		return reimbBenefitsDtls;
	}
	
	private String calculateNoOfDaysAdmitted( Date dischargeDate,Date admissionDate )
	{
		Long diff = 0l;
		if(null != admissionDate && null != dischargeDate)
		{
		  diff =  SHAUtils.getDaysBetweenDate (admissionDate,dischargeDate);
		}
		return String.valueOf(diff);
	}
	
	public void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement)
	{/*
		
		SubmitProcessClaimBillingTask submitTask = BPMClientContext.getClaimBillingTask(rodDTO.getStrUserName(), rodDTO.getStrPassword());
		HumanTask humanTask = rodDTO.getHumanTask();
		
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null 
				&& (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
				|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))) {
			PayloadBOType payloadForCancelROD = getPayloadForCancelROD(rodDTO, reimbursement);
			humanTask.setOutcome(SHAConstants.CANCEL_ROD_OUTCOME);
			humanTask.setPayload(payloadForCancelROD);
		}
		else{
		
			PayloadBOType payloadBO = humanTask.getPayload();
			ClaimRequestType claimRequestType = payloadBO.getClaimRequest();
			*//**
			 * Added for coordinator and FA workflow.
			 * *//*
			if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR))
			{
				claimRequestType.setClientType(null);
				claimRequestType.setResult(rodDTO.getBpmnOutCome());
				claimRequestType.setReferTo(rodDTO.getBpmnOutCome());
				claimRequestType.setReimbReqBy("BILLING");
			}
			else if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER))
			{
				claimRequestType.setClientType("ACK");
				claimRequestType.setResult(null);
				claimRequestType.setReferTo(null);
			}
			humanTask.setOutcome(rodDTO.getBpmnOutCome());
			payloadBO.setClaimRequest(claimRequestType);
			humanTask.setPayload(payloadBO);
		}
		
		try{
		submitTask.execute(rodDTO.getStrUserName(),humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
	*/}
	
	public void submitBillingBenefitsTaskToDB(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement)
	{
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null 
				&& (rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
				|| rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))) {
			//getDBWorkFlowMapForCancelROD(wrkFlowMap, reimbursement);
		}
		else{
		
			
			/**
			 * Added for coordinator and FA workflow.
			 * */
			if(null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR))
			{
				//wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_BILLING_REFER_TO_COORDINATOR);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.BILLING_CURRENT_QUEUE);
				/*claimRequestType.setClientType(null);
				claimRequestType.setResult(rodDTO.getBpmnOutCome());
				claimRequestType.setReferTo(rodDTO.getBpmnOutCome());
				claimRequestType.setReimbReqBy("BILLING");*/
			}
			
		}
		wrkFlowMap.put(SHAConstants.OUTCOME, rodDTO.getBpmnOutCome());
		try{
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void submitFinancialBenefitsToBPMN(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement)
	{
		
	}
	/**
	 * commenting to remove bpmn references. This needs to be migrated. 
	 * uncomment at that time and migrate the same.
	 * 
	 * */
	/*private PayloadBOType getPayloadForCancelROD(ReceiptOfDocumentsDTO bean,
>>>>>>> removalofbpmn
			Reimbursement reimbursement) {

		InitiateAckProcessPayloadType payload = new InitiateAckProcessPayloadType();

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(bean.getClaimDTO().getNewIntimationDto().getIntimationId());
		
		intimationType.setIntimationSource(bean.getClaimDTO().getNewIntimationDto().getIntimationSource().getValue());
		

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(bean.getNewIntimationDTO()
				.getPolicy().getPolicyNumber());
		
		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");
		
		productInfo.setProductName(bean.getNewIntimationDTO().getPolicy().getProductName());

		*//**
		 * @author yosuva.a
		 *//*
		
		DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
		
		ClaimType claim = new ClaimType();
		claim.setClaimId(docAcknowLedgement.getClaim().getClaimId());
		claim.setKey(docAcknowLedgement.getClaim().getKey());
		if (docAcknowLedgement.getClaim().getClaimType() != null) {
			claim.setClaimType(docAcknowLedgement.getClaim().getClaimType().getValue());
		}

		ClaimRequestType claimRequest = new ClaimRequestType();
		claimRequest.setResult(SHAConstants.CANCEL_ROD_OUTCOME);
		claimRequest.setKey(bean.getClaimDTO().getKey());
		claimRequest.setCpuCode(bean.getNewIntimationDTO()
				.getCpuCode());
		claimRequest.setClaimRequestType("All");
		claimRequest.setClientType("ACK");

		DocReceiptACKType docReceiptAck = new DocReceiptACKType();
		docReceiptAck.setAckNumber(docAcknowLedgement.getAcknowledgeNumber());
		docReceiptAck.setKey(docAcknowLedgement.getKey());
		docReceiptAck.setStatus(docAcknowLedgement.getStatus().getProcessValue());

		if (("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalisationFlag()))
			docReceiptAck.setHospitalization(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPostHospitalisationFlag()))
			docReceiptAck.setPosthospitalization(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPreHospitalisationFlag()))
			docReceiptAck.setPrehospitalization(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPartialHospitalisationFlag()))
			docReceiptAck.setPartialhospitalization(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getLumpsumAmountFlag()))
			docReceiptAck.setLumpsumamount(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalCashFlag()))
			docReceiptAck.setAddonbenefitshospcash(true);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPatientCareFlag()))
			docReceiptAck.setAddonbenefitspatientcare(true);
		
		HospitalInfoType hospInfoType = new HospitalInfoType();
		hospInfoType.setHospitalType(bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue());
		hospInfoType.setNetworkHospitalType(bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
		
		Claim claimObj = docAcknowLedgement.getClaim();
		
		Insured insured = claimObj.getIntimation().getInsured();
		
		ClassificationType classificationType = new ClassificationType();
		if(claimObj != null && claimObj.getIsVipCustomer() != null && claimObj.getIsVipCustomer().equals(1l)){
			
			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		}
		else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		}else{
			classificationType.setPriority(SHAConstants.NORMAL);
		}
	
		classificationType.setType(SHAConstants.TYPE_FRESH);
		classificationType.setSource(SHAConstants.NORMAL);
		
		payloadBO.setIntimation(intimationType);
		payloadBO.setPolicy(policyType);
		payloadBO.setClaimRequest(claimRequest);
		payloadBO.setDocReceiptACK(docReceiptAck);
		payloadBO.setClaim(claim);
		payloadBO.setHospitalInfo(hospInfoType);
		payloadBO.setProductInfo(productInfo);
		payloadBO.setClassification(classificationType);
		
		RODType rodType = new RODType();
		rodType.setKey(reimbursement.getKey());
		
		payloadBO.setRod(rodType);
		
		return payloadBO;
	}*/
	
	
	private void getDBWorkFlowMapForCancelROD(Map<String, Object> wrkFlowMap,
			Reimbursement reimbursement) {

		if(null != wrkFlowMap)
		{
			wrkFlowMap.put(SHAConstants.INTIMATION_NUMBER, reimbursement.getClaim().getIntimation().getIntimationId());
			wrkFlowMap.put(SHAConstants.INT_SOURCE, reimbursement.getClaim().getIntimation().getIntimationSource().getValue());
			wrkFlowMap.put(SHAConstants.POLICY_NUMBER, reimbursement.getClaim().getIntimation().getPolicy().getPolicyNumber());
			wrkFlowMap.put(SHAConstants.LOB, "HEALTH");
			wrkFlowMap.put(SHAConstants.PRODUCT_NAME, reimbursement.getClaim().getIntimation().getPolicy().getProductName());
		/**
		 * @author yosuva.a
		 */
		
		DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
		wrkFlowMap.put(SHAConstants.CLAIM_NUMBER, docAcknowLedgement.getClaim().getClaimId());
		wrkFlowMap.put(SHAConstants.CLAIM_KEY, docAcknowLedgement.getClaim().getKey());
		if (docAcknowLedgement.getClaim().getClaimType() != null) {
			wrkFlowMap.put(SHAConstants.CLAIM_TYPE, docAcknowLedgement.getClaim().getClaimType().getValue());
		}
		//MSSUPPOR-28495 add for cancell rod in FA
		wrkFlowMap.put(SHAConstants.CPU_CODE, String.valueOf(reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode()));
//		wrkFlowMap.put(SHAConstants.CPU_CODE, reimbursement.getClaim().getIntimation().getIntimationId());
		wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, docAcknowLedgement.getAcknowledgeNumber());
		wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_KEY, docAcknowLedgement.getKey());
		//wrkFlowMap.put(SHAConstants., value)
		
		//docReceiptAck.setStatus(docAcknowLedgement.getStatus().getProcessValue());

		if (("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalisationFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPostHospitalisationFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPreHospitalisationFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPartialHospitalisationFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getLumpsumAmountFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getHospitalCashFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, SHAConstants.YES_FLAG);
		if (("Y").equalsIgnoreCase(docAcknowLedgement.getPatientCareFlag()))
			wrkFlowMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, SHAConstants.YES_FLAG);
		Hospitals hospitalObj = getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
		if(null != hospitalObj &&  null != hospitalObj.getHospitalType())
		{
			wrkFlowMap.put(SHAConstants.HOSPITAL_TYPE, hospitalObj.getHospitalType().getValue());
			wrkFlowMap.put(SHAConstants.NETWORK_HOSPITAL_TYPE,hospitalObj.getNetworkHospitalType());
		
		
		}
		
		Claim claimObj = docAcknowLedgement.getClaim();
		
		Insured insured = claimObj.getIntimation().getInsured();
		
		
		if(claimObj != null && claimObj.getIsVipCustomer() != null && claimObj.getIsVipCustomer().equals(1l)){
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.VIP_CUSTOMER);
			
		}
		else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.SENIOR_CITIZEN);
			
		}else{
			wrkFlowMap.put(SHAConstants.PRIORITY, SHAConstants.NORMAL);
			
		}
		wrkFlowMap.put(SHAConstants.RECORD_TYPE, SHAConstants.TYPE_FRESH);
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.NORMAL);
		}
	}
	
	private void calculateTotalFields(AddOnBenefitsDTO addOnBenefitsDTO, Double coPay)
	{
		Integer netAmt = addOnBenefitsDTO.getEligibleNetAmount();
		Double calculatedAmt = (netAmt) * (coPay/100f);
		Long roundedValue = Math.round(calculatedAmt);
		Long value = netAmt - roundedValue;
		addOnBenefitsDTO.setCopayAmount(Integer.valueOf(String.valueOf(roundedValue)));
		addOnBenefitsDTO.setNetAmountAfterCopay(Integer.valueOf(String.valueOf(value)));
		addOnBenefitsDTO.setPayableAmount(Integer.valueOf(String.valueOf(value)));
	}
	
	private Integer calculateNetAmt(Integer eligiblePerDayAmt , Integer noOfDaysPayable)
	{
		Integer amount = 0;
		if(null != eligiblePerDayAmt & null != noOfDaysPayable)
		{
			amount = eligiblePerDayAmt * noOfDaysPayable;
		}
		return amount;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> savePatientCareTableValues(ReceiptOfDocumentsDTO dto)
	{
		List<PatientCareDTO> patientList = dto.getUploadDocumentsDTO().getPatientCareDTO();
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		//Long rodKey = 0l;
		if (null != patientList && !patientList.isEmpty()) {
			List<ReimbursementBenefitsDetails> reimbursementList = null;
			//= null;
			for (PatientCareDTO patientCareDTO : patientList) {
				if(null != patientCareDTO.getKey())
				{
					Query query = entityManager.createNamedQuery("ReimbursementBenefitsDetails.findByKey");
					query = query.setParameter("benefitsDetailsKey",patientCareDTO.getKey());	
					reimbursementList =  query.getResultList();
					if(null != reimbursementList && !reimbursementList.isEmpty())
					{
						for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbursementList) {
						//	rodKey = reimbursementBenefitsDetails.getReimbursementBenefits().getReimbursementKey().getKey();
							reimbursementBenefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
							reimbursementBenefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
							//reimbursementBenefitDetails.setReimbursementBenefits(reimbursementBenefits);
							entityManager.merge(reimbursementBenefitsDetails);
							entityManager.flush();
							entityManager.refresh(reimbursementBenefitsDetails);
						}
					}				
				}
			}
			
			addOnBenefitsList =  populateAddOnBenefitsTableValues(dto.getClaimDTO().getNewIntimationDto(), dto.getClaimDTO().getKey(),dto.getDocumentDetails().getRodKey(), dto.getProductCoPay(),null);
			
		}
		return addOnBenefitsList;

	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> saveHospitalCashValues(ReceiptOfDocumentsDTO rodDTO)
	{
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		 {
			UploadDocumentDTO dto = rodDTO.getUploadDocumentsDTO();
			if(null != dto.getHospitalBenefitKey())
			{
				Query query = entityManager.createNamedQuery("ReimbursementBenefits.findByKey");
				query = query.setParameter("key",dto.getHospitalBenefitKey());	
				List<ReimbursementBenefits> reimbursementList =  query.getResultList();
				if(null != reimbursementList && !reimbursementList.isEmpty())
				{
					for (ReimbursementBenefits reimbursementBenefits : reimbursementList) {
						reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
								.getHospitalCashNoofDays()));
						reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
								.getHospitalCashPerDayAmt()));
						entityManager.merge(reimbursementBenefits);
						entityManager.flush();
						entityManager.clear();
					}
				}
			}
			addOnBenefitsList =  populateAddOnBenefitsTableValues(rodDTO.getClaimDTO().getNewIntimationDto(), rodDTO.getClaimDTO().getKey(),rodDTO.getDocumentDetails().getRodKey(), rodDTO.getProductCoPay(),null);
		}
		 return addOnBenefitsList;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> updatePatientCareValues(PreauthDTO dto)
	{
//		List<PatientCareDTO> patientList = dto.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareDTO();
		
//		//Long rodKey = 0l;
//		if (null != patientList && !patientList.isEmpty()) {
//			List<ReimbursementBenefitsDetails> reimbursementList = null;
//			//= null;
//			for (PatientCareDTO patientCareDTO : patientList) {
//				if(null != patientCareDTO.getKey())
//				{
//					Query query = entityManager.createNamedQuery("ReimbursementBenefitsDetails.findByKey");
//					query = query.setParameter("benefitsDetailsKey",patientCareDTO.getKey());	
//					reimbursementList =  query.getResultList();
//					if(null != reimbursementList && !reimbursementList.isEmpty())
//					{
//						for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbursementList) {
//						//	rodKey = reimbursementBenefitsDetails.getReimbursementBenefits().getReimbursementKey().getKey();
//							reimbursementBenefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
//							reimbursementBenefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
//							//reimbursementBenefitDetails.setReimbursementBenefits(reimbursementBenefits);
//							entityManager.merge(reimbursementBenefitsDetails);
//							entityManager.flush();
//							entityManager.refresh(reimbursementBenefitsDetails);
//						}
//					}				
//				}
//			}
		
		    List<AddOnBenefitsDTO> saveReimbursementBenfitsTable = saveReimbursementBenfitsTable(dto);
			
			List<Double> coPayValue = new ArrayList<Double>();
			if(dto.getCoPayValue() != null){
				coPayValue.add(Double.valueOf(dto.getCoPayValue()));
			}
			
			
		return saveReimbursementBenfitsTable;

	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> updateHospitalCashValues(PreauthDTO preauthDTO)
	{
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		 {
			UploadDocumentDTO dto = preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO();
			List<ReimbursementBenefits> reimbursementList = null;
			if(dto.getHospitalBenefitKey() != null) {
				Query query = entityManager.createNamedQuery("ReimbursementBenefits.findByKey");
				query = query.setParameter("key",dto.getHospitalBenefitKey());	
				reimbursementList =  query.getResultList();
			}
			
			if(null != reimbursementList && !reimbursementList.isEmpty())
			{
				for (ReimbursementBenefits reimbursementBenefits : reimbursementList) {
					reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
							.getHospitalCashNoofDays()));
					reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
							.getHospitalCashPerDayAmt()));
					reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
					reimbursementBenefits.setTotalClaimAmountBills(Double.valueOf(dto.getHospitalCashTotalClaimedAmt()));
					reimbursementBenefits.setBenefitsFlag(SHAConstants.HOSPITAL_CASH_FLAG);
					reimbursementBenefits.setReimbursementKey(getReimbursementByKey(preauthDTO.getKey()));
					entityManager.merge(reimbursementBenefits);
					entityManager.flush();
					entityManager.clear();
				}
			} else {
				ReimbursementBenefits reimbursementBenefits = new ReimbursementBenefits();
				reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
						.getHospitalCashNoofDays()));
				reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
						.getHospitalCashPerDayAmt()));
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				reimbursementBenefits.setBenefitsFlag(SHAConstants.HOSPITAL_CASH_FLAG);
				reimbursementBenefits.setTotalClaimAmountBills(Double.valueOf(dto.getHospitalCashTotalClaimedAmt()));
				reimbursementBenefits.setReimbursementKey(getReimbursementByKey(preauthDTO.getKey()));
				entityManager.persist(reimbursementBenefits);
				entityManager.flush();
				entityManager.clear();
				
				dto.setHospitalBenefitKey(reimbursementBenefits.getKey());
			}
			
			List<Double> coPayValue = new ArrayList<Double>();
			if(preauthDTO.getProductCopay() != null){
				coPayValue.addAll(preauthDTO.getProductCopay());
			} 
			addOnBenefitsList =  populateAddOnBenefitsTableValues(preauthDTO.getClaimDTO().getNewIntimationDto(), preauthDTO.getClaimDTO().getKey(),preauthDTO.getKey(), coPayValue,preauthDTO);
			if(addOnBenefitsList != null) {
				for (AddOnBenefitsDTO reimbursementBenefitsDetails : addOnBenefitsList) {
					if(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate() != null){
					String admissionDate = SHAUtils.formatDate(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate());
					reimbursementBenefitsDetails.setDateOfAdmission(admissionDate);
					}
					if(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate() != null){
						String dischargeDate = SHAUtils.formatDate(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
						reimbursementBenefitsDetails.setDateOfDischarge(dischargeDate);
					}
				}
			}
			
		 }
		 return addOnBenefitsList;
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
	
	public List<AddOnBenefitsDTO> saveReimbursementBenfitsTable(PreauthDTO preauthDTO){
		
		ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientBenefitKey());
		UploadDocumentDTO uploadDTO = preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO();
		if(reimbursementBenefits == null) {
			reimbursementBenefits = new ReimbursementBenefits();
		}
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		/*	reimbursementHospBenefits.setKey(reimbursementBenefits.getKey());
			reimbursementHospBenefits.setReimbursementKey(reimbursementBenefits.getReimbursementKey());*/
			reimbursementBenefits.setTreatmentForPhysiotherapy(uploadDTO.getTreatmentPhysiotherapyFlag());
			reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(uploadDTO.getPatientCareNoofDays()));
			reimbursementBenefits.setPerDayAmountBills(Double.valueOf(uploadDTO.getPatientCarePerDayAmt()));
			reimbursementBenefits.setTotalClaimAmountBills(Double.valueOf((uploadDTO.getPatientCareTotalClaimedAmt())));
			reimbursementBenefits.setBenefitsFlag(reimbursementBenefits.getBenefitsFlag());
			reimbursementBenefits.setReimbursementKey(getReimbursementByKey(preauthDTO.getKey()));
			reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
			reimbursementBenefits.setBenefitsFlag(SHAConstants.PATIENT_CARE_FLAG);
//			reimbursementBenefits = populateBenefitsData(uploadDTO ,  reimbursementBenefits  ,  benefitsDTO);
			if(reimbursementBenefits.getKey() != null) {
				entityManager.merge(reimbursementBenefits);
			} else {
				entityManager.persist(reimbursementBenefits);
			}
			
			entityManager.flush();
			entityManager.clear();
			preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().setPatientBenefitKey(reimbursementBenefits.getKey());
//			List<ReimbursementBenefitsDetails> reimbBenefitsList = getPatientCareTableValues(preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientBenefitKey());
			
			List<PatientCareDTO> patientList = preauthDTO.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareDTO();
			ReimbursementBenefitsDetails reimbursementBenefitDetails = null;
			for (PatientCareDTO patientCareDTO : patientList) {
				reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
				reimbursementBenefitDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
				reimbursementBenefitDetails.setEngagedTo(patientCareDTO.getEngagedTo());
				reimbursementBenefitDetails.setKey(patientCareDTO.getKey());
				reimbursementBenefitDetails.setReimbursementBenefits(reimbursementBenefits);
				reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
				if(reimbursementBenefitDetails.getKey() != null) {
					entityManager.merge(reimbursementBenefitDetails);
				} else {
					entityManager.persist(reimbursementBenefitDetails);
				}
				
				entityManager.flush();
				entityManager.clear();
				patientCareDTO.setKey(reimbursementBenefitDetails.getKey());
			}
			
			
//			if(null != reimbBenefitsList && !reimbBenefitsList.isEmpty() && null != patientList && !patientList.isEmpty()) 
//			{
//				for (ReimbursementBenefitsDetails benefitsDetails : reimbBenefitsList) {
//					
//					for (PatientCareDTO patientCareDTO : patientList) {
//						
//						if(patientCareDTO.getKey() != null && benefitsDetails.getKey().equals(patientCareDTO.getKey()))
//						{
//							benefitsDetails.setEngagedFrom(patientCareDTO.getEngagedFrom());
//							benefitsDetails.setEngagedTo(patientCareDTO.getEngagedTo());
//							
//							if(benefitsDetails.getKey() != null) {
//								entityManager.merge(benefitsDetails);
//							} else {
//								entityManager.persist(benefitsDetails);
//							}
////							entityManager.merge(benefitsDetails);
//							entityManager.flush();
//							patientCareDTO.setKey(benefitsDetails.getKey());
//							break;
//						}
//					}
//					
//				}
//			}
				
				List<Double> coPayValue = new ArrayList<Double>();
				if(preauthDTO.getProductCopay() != null){
					coPayValue.addAll(preauthDTO.getProductCopay());
				}
				
				addOnBenefitsList =  populateAddOnBenefitsTableValues(preauthDTO.getClaimDTO().getNewIntimationDto(), preauthDTO.getClaimDTO().getKey(),preauthDTO.getKey(), coPayValue,preauthDTO);
				if(addOnBenefitsList != null) {
					for (AddOnBenefitsDTO reimbursementBenefitsDetails : addOnBenefitsList) {
						if(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate() != null){
						String admissionDate = SHAUtils.formatDate(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate());
						reimbursementBenefitsDetails.setDateOfAdmission(admissionDate);
						}
						if(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate() != null){
							String dischargeDate = SHAUtils.formatDate(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
							reimbursementBenefitsDetails.setDateOfDischarge(dischargeDate);
						}
					}
				}
				
			
			
			return addOnBenefitsList;
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
	

	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	public void submitFinancialBenefitsTaskToDB(ReceiptOfDocumentsDTO rodDTO,Reimbursement reimbursement)
	{
		Map<String, Object> wrkFlowMap = (Map<String, Object>) rodDTO.getDbOutArray();
		if(null != wrkFlowMap)
		{
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getStatusId() != null 
				&&  rodDTO.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
			//MSSUPPOR-28495 add for cancell rod in FA
			wrkFlowMap.put(SHAConstants.OUTCOME, rodDTO.getBpmnOutCome());
			getDBWorkFlowMapForCancelROD(wrkFlowMap, reimbursement);
			
		} else {
			String outCome = rodDTO.getBpmnOutCome();
			
			
			 if (rodDTO.getDocumentDetails().getStatusId()
					.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
				if (null != rodDTO.getDocumentDetails().getReimbursementQueryKey()) {
					wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY, rodDTO.getDocumentDetails().getReimbursementQueryKey());
				}
			} 
				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.FA_CURRENT_QUEUE);
			
			
			
			if(null != reimbursement.getClaim().getIntimation().getCpuCode() && null != reimbursement.getClaim().getIntimation().getCpuCode())
				wrkFlowMap.put(SHAConstants.CPU_CODE, String.valueOf(reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode()));
          if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FA_STAGE_SOURCE);
			}
          if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
        		  || reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
        	  wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FA_STAGE_SOURCE);
        	  wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS,SHAConstants.SEND_REPLY_FA);
        	  wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
			}
          
         
          wrkFlowMap.put(SHAConstants.OUTCOME, rodDTO.getBpmnOutCome());
		}
		try{
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		}catch(Exception e){
			e.printStackTrace();
		}
		}
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
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AddOnBenefitsDTO> saveHospitalCashPhcValues(ReceiptOfDocumentsDTO rodDTO)
	{
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		Reimbursement reim = getReimbursementObjectByKey(rodDTO.getKey());
		List<HopsitalCashBenefitDTO> hospitalCashBenefitdto = rodDTO.getUploadDocumentsDTO().getHopsitalCashBenefitDTO();
		
		ReimbursementBenefits reimbursementBenefits   = null;
		List<ReimbursementBenefits> reimbHospitalCash = getReimbursementBenefits(reim.getKey());
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
		
		reimbursementBenefits.setReimbursementKey(reim);
		reimbursementBenefits.setTotalNoOfDays(Long.valueOf(hospitalCashBenefitDTO.getHospitalCashDays() != null && !hospitalCashBenefitDTO.getHospitalCashDays().isEmpty()
				? hospitalCashBenefitDTO.getHospitalCashDays() : "0"));
		reimbursementBenefits.setPerDayAmountBills(Double.valueOf(hospitalCashBenefitDTO.getHospitalCashPerDayAmt() != null && !hospitalCashBenefitDTO.getHospitalCashPerDayAmt().isEmpty()
				? hospitalCashBenefitDTO.getHospitalCashPerDayAmt() : "0"));
		reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(hospitalCashBenefitDTO.getNoOfDaysAllowed() != null && !hospitalCashBenefitDTO.getNoOfDaysAllowed().isEmpty()
				? hospitalCashBenefitDTO.getNoOfDaysAllowed() : "0"));
		reimbursementBenefits.setDisallowanceRemarks(hospitalCashBenefitDTO.getDisallowanceRemarks());
		reimbursementBenefits.setDeletedFlag(SHAConstants.N_FLAG);
//		if(null != hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() && ! hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty())
//		{
		reimbursementBenefits.setTotalClaimAmountBills(Double
				.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
				? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
		reimbursementBenefits.setPayableAmount(Double
				.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
				? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
		/*totalClaimedAmtForBenefits += Double.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && !hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
				? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0");*/
		
		/** Below code is added for MED-PRD-058 - Classic Health Group. The below change done for BSI View. 
		  Based on this change in BSI View current provision is not included both hospital cash and patient care.
		  This is  applicable only for this product*/
		//if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(rodDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			reim.setAddOnCoversApprovedAmount(Double.valueOf(hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() != null && ! hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt().isEmpty()
					? hospitalCashBenefitDTO.getHospitalCashTotalClaimedAmt() : "0"));
			
		//}
		reimbursementBenefits.setProductBenefitID(hospitalCashBenefitDTO.getParticulars().getId());
		reimbursementBenefits.setBenefitsFlag("PHC");
		
		
		//if(null == reimbursementBenefits.getKey())
		//if(null ==  dto.getHospitalCashReimbursementBenefitsKey())
		if(null ==  reimbursementBenefits.getKey())
		{
			reimbursementBenefits.setCreatedBy(rodDTO.getStrUserName());
			reimbursementBenefits.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(reimbursementBenefits);
			entityManager.flush();
			entityManager.clear();
		}
		else
		{
			//reimbursementBenefits.setKey(dto.getHospitalCashReimbursementBenefitsKey());
			reimbursementBenefits.setModifiedBy(rodDTO.getStrUserName());
			reimbursementBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursementBenefits);
			entityManager.flush();
			entityManager.clear();
		}
	}
		
		addOnBenefitsList =  populateAddOnBenefitsTableValuesPhc(rodDTO.getClaimDTO().getNewIntimationDto(), rodDTO.getClaimDTO().getKey(),rodDTO.getDocumentDetails().getRodKey(), rodDTO.getProductCoPay(),null, rodDTO.getDocumentDetails());
		return addOnBenefitsList;
		
	}
	
	public List<AddOnBenefitsDTO> populateAddOnBenefitsTableValuesPhc(NewIntimationDto intimationDTO , Long claimKey ,Long rodKey,List<Double> copayList,PreauthDTO preauthDto
			,DocumentDetailsDTO documentDetailsDTO)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
	//	Reimbursement reimbursement = getReimbursementObjectByKey(rodKey);
		Reimbursement reimbursement = getFirstRODObjectByClaimKey(claimKey);
		List<ReimbursementBenefits> reimbursementBenefits = getReimbursementBenefits(rodKey);
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		List<AddOnBenefitsDTO> addOnBenefitsdto1 = null;
		addOnBenefitsdto1 = dbCalculationService.getAddOnBenefitsValuesPhc(rodKey,intimationDTO.getPolicy().getKey(), 
				intimationDTO.getInsuredPatient().getKey());
		
		if(null != reimbursementBenefits && !reimbursementBenefits.isEmpty())
		{
			addOnBenefitsList = new ArrayList<AddOnBenefitsDTO>();
			AddOnBenefitsDTO addOnBenefitsdto = null;
			for (ReimbursementBenefits reimbursementBenefits2 : reimbursementBenefits) {
				if(("PHC").equalsIgnoreCase(reimbursementBenefits2.getBenefitsFlag()))
				{
					addOnBenefitsdto = new AddOnBenefitsDTO();
					if(null != reimbursement && preauthDto == null)
					{
						addOnBenefitsdto.setDateOfAdmission((null != reimbursement.getDateOfAdmission() ? SHAUtils.formatDate(reimbursement.getDateOfAdmission()) : ""));
						addOnBenefitsdto.setDateOfDischarge((null != reimbursement.getDateOfDischarge() ? SHAUtils.formatDate(reimbursement.getDateOfDischarge()) : ""));
						
						if(documentDetailsDTO != null && documentDetailsDTO.getDateOfAdmission() != null){
							addOnBenefitsdto.setDateOfAdmission(SHAUtils.formatDate(documentDetailsDTO.getDateOfAdmission()));
						}
						if(documentDetailsDTO != null && documentDetailsDTO.getDateOfDischarge() != null){
							addOnBenefitsdto.setDateOfDischarge(SHAUtils.formatDate(documentDetailsDTO.getDateOfDischarge()));
						}
						
						addOnBenefitsdto.setAdmittedNoOfDays(calculateNoOfDaysAdmitted(reimbursement.getDateOfDischarge(), reimbursement.getDateOfAdmission()));
						
						if(documentDetailsDTO != null && documentDetailsDTO.getDateOfAdmission() != null && documentDetailsDTO.getDateOfDischarge() != null){
							addOnBenefitsdto.setAdmittedNoOfDays(calculateNoOfDaysAdmitted(documentDetailsDTO.getDateOfDischarge(), documentDetailsDTO.getDateOfAdmission()));
						}
						
					}else if(preauthDto != null){
						if(preauthDto.getPreauthDataExtractionDetails().getAdmissionDate() != null){
							String admissionDate = SHAUtils.formatDate(preauthDto.getPreauthDataExtractionDetails().getAdmissionDate());
							addOnBenefitsdto.setDateOfAdmission(admissionDate);
							}
							if(preauthDto.getPreauthDataExtractionDetails().getDischargeDate() != null){
								String dischargeDate = SHAUtils.formatDate(preauthDto.getPreauthDataExtractionDetails().getDischargeDate());
								addOnBenefitsdto.setDateOfDischarge(dischargeDate);
							}
							addOnBenefitsdto.setAdmittedNoOfDays(calculateNoOfDaysAdmitted(preauthDto.getPreauthDataExtractionDetails().getDischargeDate(), 
									preauthDto.getPreauthDataExtractionDetails().getAdmissionDate()));
					}
					
					addOnBenefitsdto.setPhcBenefitId(Integer.valueOf(String.valueOf(reimbursementBenefits2.getProductBenefitID())));
					addOnBenefitsdto.setAllowedNoOfDays(Integer.valueOf(String.valueOf(reimbursementBenefits2.getNumberOfDaysBills())));
					addOnBenefitsdto.setTotalClaimedAmount(Integer.valueOf(String.valueOf(Math.round(reimbursementBenefits2.getTotalClaimAmountBills()))));
					addOnBenefitsdto.setDisallowanceRemarks(reimbursementBenefits2.getDisallowanceRemarks());
					addOnBenefitsdto.setTotalNoOfDaysClaimed(Integer.valueOf(String.valueOf(reimbursementBenefits2.getTotalNoOfDays())));
					populateDataCommonForPhc(addOnBenefitsdto1,addOnBenefitsdto,copayList,true,reimbursementBenefits2);
//					addOnBenefitsdto.setParticulars(ReferenceTable.HOSPITAL_CASH);
					addOnBenefitsList.add(addOnBenefitsdto);
				}
				
			}
			
		}
		return addOnBenefitsList;

	}
		
	
	private void populateDataCommonForPhc(List<AddOnBenefitsDTO> addOnBenefitsObj , AddOnBenefitsDTO addOnBenefitsdto,List<Double> copayList,Boolean isHospitalCash,ReimbursementBenefits reimbursementBenefits2)
	{
		for (AddOnBenefitsDTO addOnBenefitsObj2 : addOnBenefitsObj) {
			if(addOnBenefitsObj2.getPhcBenefitId().equals(addOnBenefitsdto.getPhcBenefitId())){
				addOnBenefitsdto.setEntitledNoOfDays(addOnBenefitsObj2.getEntitledNoOfDays());
				addOnBenefitsdto.setNoOfDaysPerHospitalization(addOnBenefitsObj2.getNoOfDaysPerHospitalization());
				addOnBenefitsdto.setEntitlementPerDayAmt(addOnBenefitsObj2.getEntitlementPerDayAmt());
				addOnBenefitsdto.setUtilizedNoOfDays(addOnBenefitsObj2.getUtilizedNoOfDays());
				addOnBenefitsdto.setBalanceAvailable(addOnBenefitsObj2.getBalanceAvailable());
				addOnBenefitsdto.setParticulars(addOnBenefitsObj2.getParticulars());
				break;
			}
			
		}
		
		SelectValue copaySelValue = new SelectValue();
		if(null != reimbursementBenefits2.getCopayPercentage())
		{
			String[] copayWithPercentage = reimbursementBenefits2.getCopayPercentage().toString().split("\\.");
			String copay = copayWithPercentage[0].trim();			
			copaySelValue.setId(Long.valueOf(copay));
			copaySelValue.setValue(String.valueOf(reimbursementBenefits2.getCopayPercentage()));
			addOnBenefitsdto.setCoPayPercentage(copaySelValue);
		}
		//addOnBenefitsdto.setC
		
		/**
		 * Added for add on beneifts bug. As per sathish sir suggestion, balance available = noOfDaysEntitled-noOfDaysUtilized.
		 * */
		/**
		 * This is been commented since there was a problem with no of days payable. Hence reverting back to old code.
		 * Implemented for ticket 4347.
		 * */
		//addOnBenefitsdto.setBalanceAvailable(getDiffOfTwoNumber(addOnBenefitsdto.getEntitledNoOfDays(), addOnBenefitsdto.getUtilizedNoOfDays()));
		
		//addOnBenefitsdto.setEligibleNoofDays(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(1))));
		Integer iEligiblePerDayAmt = addOnBenefitsdto.getEntitlementPerDayAmt();
		
		//addOnBenefitsdto.setEligiblePerDayAmt(Integer.valueOf(String.valueOf((BigDecimal)addOnBenefitsObj.get(3))));
		addOnBenefitsdto.setEligiblePerDayAmt(Math.min(iEligiblePerDayAmt, reimbursementBenefits2.getPerDayAmountBills().intValue()));

		//Integer minDay1 = 0;
		
		if(isHospitalCash)
		{
			//minDay1 = Math.min(addOnBenefitsdto.getEntitledNoOfDays() , addOnBenefitsdto.getNoOfDaysPerHospitalization());
			Integer iReducedNoDays = 0;
			if(null != addOnBenefitsdto.getAllowedNoOfDays()  && null != iReducedNoDays)
			{
				/**
				 * Added for ticket 4464, where allowed no of days is less than reduced no of
				 * days, then 0 will be populated.
				 * 
				 * **/
				if(addOnBenefitsdto.getAllowedNoOfDays() >= iReducedNoDays)
				{
					addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(addOnBenefitsdto.getAllowedNoOfDays() , iReducedNoDays));
				}
				else
				{
					addOnBenefitsdto.setEligibleNoofDays(0);
				}
			}
			else
			{
				addOnBenefitsdto.setEligibleNoofDays(0);
			}
			/*else
			{
				addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(0 , iReducedNoDays));
			}*/
		}
		else 
		{
			//minDay1 = Math.min(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) , addOnBenefitsdto.getNoOfDaysPerHospitalization());
			Integer iReducedNoDays = 0;
			if(null != addOnBenefitsdto.getAdmittedNoOfDays() && null != iReducedNoDays)
			{
				if(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) >= iReducedNoDays)
				{
					addOnBenefitsdto.setEligibleNoofDays(getDiffOfTwoNumber(Integer.valueOf(addOnBenefitsdto.getAdmittedNoOfDays()) , iReducedNoDays));
				}
				else
				{
					addOnBenefitsdto.setEligibleNoofDays(0);
				}
			}
			else
			{ 
				addOnBenefitsdto.setEligibleNoofDays(0);
			}
				
		}
		Integer minDay1 =  Math.min(addOnBenefitsdto.getEligibleNoofDays() ,addOnBenefitsdto.getBalanceAvailable());
		Integer minDay2 = Math.min(minDay1  , addOnBenefitsdto.getNoOfDaysPerHospitalization());
		Integer eligiblePayableNoOfDays = Math.min(minDay1 , minDay2);
		addOnBenefitsdto.setEligiblePayableNoOfDays(eligiblePayableNoOfDays);	
		addOnBenefitsdto.setEligibleNetAmount(calculateNetAmt(addOnBenefitsdto.getEligiblePerDayAmt() , addOnBenefitsdto.getEligiblePayableNoOfDays()));
		addOnBenefitsdto.setProductCoPay((List<Double>)copayList);
		if(null != addOnBenefitsdto.getCoPayPercentage())
		{
			SelectValue copayPercentageValue =  addOnBenefitsdto.getCoPayPercentage();
			if(null != copayPercentageValue && null != copayPercentageValue.getValue())
			{
				calculateTotalFields(addOnBenefitsdto,Double.valueOf(copayPercentageValue.getValue()));
			}
		}
		else if(null != copayList && !copayList.isEmpty())
		{
			calculateTotalFields(addOnBenefitsdto,copayList.get(0));
		}
	}

	public ReimbursementBenefits getReimBenefitsByRODPHCKey(
			Long rodKey, Long phcId) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKeyAndPHC");
		query.setParameter("rodKey", rodKey);
		query.setParameter("phcId", phcId);
		List<ReimbursementBenefits> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0);
			
		}
		
		return null;
	}
	
	private void saveMABenefitsValues(ReceiptOfDocumentsDTO rodDTO, Reimbursement reimbursement)
	{
		

		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
			createReimbursementRejection(rodDTO,reimbursement);	
		}

		if (null != reimbursement.getClaim()) {
			Claim claim  = reimbursement.getClaim();
			claim.setDataOfAdmission(reimbursement.getDateOfAdmission());
			claim.setDataOfDischarge(reimbursement.getDateOfDischarge());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
			claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(claim);
		}
//		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
//				ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
//			reimbursement.setBillingRemarks(rodDTO.getDocumentDetails().getBillingRemarks());
//		}
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			reimbursement.setMedicalRemarks(rodDTO.getDocumentDetails().getFinancialRemarks());
		}
		
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS))
		{
			saveCoordinatorValues(reimbursement, rodDTO);
		}
		
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
			
			saveCancelRODValues(reimbursement, ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS,ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		if (null != rodDTO.getDocumentDetails() && null != rodDTO.getDocumentDetails().getStatusId() && (rodDTO.getDocumentDetails().getStatusId().equals(
				ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || rodDTO.getDocumentDetails().getStatusId().equals(
						ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS))) {
			String remarks = rodDTO.getDocumentDetails()
					.getApproverReply();
			updateMedicalApproverForClaimRequest(rodDTO, reimbursement, remarks);
		}
		
		if(rodDTO.getDocumentDetails().getHospitalCashDueTo() != null ){
			reimbursement.setProdBenefitDueToID(rodDTO.getDocumentDetails().getHospitalCashDueTo().getId());
		}
		
		if (reimbursement.getKey() != null) {
			Status status = getStatusByKey(rodDTO.getDocumentDetails().getStatusId());
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStatus(status);
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			reimbursement.setModifiedBy(rodDTO.getStrUserName());
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStage(stage);
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
		} 
		
	}
	
	public void updateStageInformation(Reimbursement reimbursement,PreauthDTO preauthDTO){
		
		StageInformation stgInformation = new StageInformation();
		stgInformation.setIntimation(reimbursement.getClaim().getIntimation());				
		stgInformation.setClaimType(reimbursement.getClaim().getClaimType());
		stgInformation.setStage(reimbursement.getStage());
		Status status = new Status();
		status.setKey(ReferenceTable.WAIT_FOR_INPUT_KEY);
		status.setProcessValue(ReferenceTable.WAIT_FOR_INPUT_STATUS);
		stgInformation.setStatus(status);
		stgInformation.setClaim(reimbursement.getClaim());
		stgInformation.setReimbursement(null !=reimbursement ? reimbursement :null);
		stgInformation.setCreatedBy(preauthDTO.getStrUserName());
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		//stgInformation.setStatusRemarks(assignedInvestigation.getCompletionRemarks());
		
		entityManager.persist(stgInformation);
		entityManager.flush();
}
	
	private String referToMedicalForFinancial(ReceiptOfDocumentsDTO bean,
			Reimbursement reimbursement, String remarks) {
		String createdBy;
		if (bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getApproverReply() == null) {
			remarks = bean.getPreauthDTO().getPreauthMedicalDecisionDetails()
					.getMedicalApproverRemarks();
		}
		MedicalApprover medicalApprover = new MedicalApprover();
		medicalApprover.setReimbursement(reimbursement);
		medicalApprover.setApproverReply(remarks);
		medicalApprover.setRecordType(SHAConstants.FINANCIAL_REMEDICAL);
		medicalApprover.setReferringRemarks(remarks);
		medicalApprover.setReasonForReferring(bean
			.getDocumentDetails().getReasonForRefering());

		createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
					15);
		}
		medicalApprover.setCreatedBy(createdBy);

		entityManager.persist(medicalApprover);
		entityManager.flush();
		return remarks;
	}
	
	public void submitClaimRequestTaskToDBProcedure(PreauthDTO dto, Boolean isZonalReview,
			String outCome,Reimbursement reimbursement) throws Exception {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) dto.getDbOutArray();
		String sendReplyFrom = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if (isZonalReview) {
			
			if(outCome != null && (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_ZMR_CANCEL_ROD_STATUS) 
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_ZMR_REFER_TO_BILLENTRY) 
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FA_REFER_TO_BILL_ENTRY_STATUS) )) {
				//PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				//humanTask.setOutcome(outCome);
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, SHAConstants.YES_FLAG);
				//humanTask.setPayload(payloadForCancelROD);
			} else {
				
				
//				Double claimedAmount = dto.getAmountConsidered() != null ? SHAUtils
//						.getDoubleValueFromString(dto.getAmountConsidered())
//						: 0d;
//						wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);

				if (dto.getStatusKey() != null
						&& dto.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS)) {
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
				}
				
				
				if (null != dto.getStatusKey() && dto.getStatusKey().equals(
						ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS) && outCome.equalsIgnoreCase(SHAConstants.OUTCOME_ZONAL_REVIEW_INTIATE_FVR_STATUS)) {
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
//							wrkFlowMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//							wrkFlowMap.put(SHAConstants.CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							}
						}
					}
					
					if (dto.getFvrKey() != null) {
						wrkFlowMap.put(SHAConstants.FVR_KEY, dto.getFvrKey());
					}
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.ZMR_CURRENT_QUEUE);

				}
				
				
				if(reimbursement.getStage() != null){
					//claimType.setOption(reimbursement.getStage().getStageName());
				}

				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, dto.getRodNumber());
				wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY, dto.getKey());

				if (dto.getPreauthDataExtractionDetails().getTreatmentType() != null) {
					wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, dto.getPreauthDataExtractionDetails()
							.getTreatmentType().getValue());
				} else {
					wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, "");
				}

				wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getSpecialityName());
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.ZMR_STAGE_NAME);
				}
					wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
				
			}
			try{
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}catch(Exception e){
//				log.error(e.toString());
				e.printStackTrace();
				
				
//				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN ZONAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
//					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR ZONAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					/*BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					claimRequestZMRTask.execute("claimshead", humanTask);*/

				} catch(Exception u) {
//					log.error("*#*#*#*# SECOND SUBMIT ERROR IN ZONAL (#*#&*#*#*#*#*#*#");
				}
				
				
			}
		} else {
			
			if(outCome != null && (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS)
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY)
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FA_REFER_TO_BILL_ENTRY_STATUS))) {
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, SHAConstants.YES_FLAG);
				//IMSSUPPOR-28693
				wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
			} else {
//				Double claimedAmount = dto.getAmountConsidered() != null ? SHAUtils
//						.getDoubleValueFromString(dto.getAmountConsidered())
//						: 0d; 
//				
//				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);


				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
							}
						}
					}
					
					if (dto.getFvrKey() != null) {
						wrkFlowMap.put(SHAConstants.FVR_KEY, dto.getFvrKey());
						/*fieldVisit.setKey(dto.getFvrKey());
						payloadBO.setFieldVisit(fieldVisit);*/
					}
					
					//GALAXYMAIN-9448
					if(reimbursement.getClaim().getLobId() != null && !(reimbursement.getClaim().getLobId().equals(ReferenceTable.PA_LOB_KEY))){
						if(null != sendReplyFrom && !(SHAConstants.SEND_REPLY.equalsIgnoreCase(sendReplyFrom))){
						
							wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.MA_CURRENT_QUEUE);
						}
					}else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.MA_CURRENT_QUEUE);
					}

				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);

				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {
					//GALAXYMAIN-9448
					if(reimbursement.getClaim().getLobId() != null && !(reimbursement.getClaim().getLobId().equals(ReferenceTable.PA_LOB_KEY))){
					if(null != sendReplyFrom && !(SHAConstants.SEND_REPLY.equalsIgnoreCase(sendReplyFrom))){
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					}
					}else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					}
					if (dto.getQueryKey() != null) {
						wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY, dto.getQueryKey());
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
					//GALAXYMAIN-9448
					if(reimbursement.getClaim().getLobId() != null && !(reimbursement.getClaim().getLobId().equals(ReferenceTable.PA_LOB_KEY))){
						if(null != sendReplyFrom && !(SHAConstants.SEND_REPLY.equalsIgnoreCase(sendReplyFrom))){
							wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
						}	
					}else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					}
					if (dto.getInvestigationKey() != null) {
						wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, dto.getInvestigationKey());
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
					if (dto.getPreauthMedicalDecisionDetails().getSpecialistType() != null) {
						if(null != dto.getStrUserName())
						{
						wrkFlowMap.put(SHAConstants.USER_ID,dto.getStrUserName());
						}
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
						wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getPreauthMedicalDecisionDetails()
									.getSpecialistType().getValue());
					}
					//IMSSUPPOR-28265
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
					//claimType.setReimbReqBy("ESCALATE");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					if(null != dto.getStrUserName())
					{
					wrkFlowMap.put(SHAConstants.USER_ID,dto.getStrUserName());
					}
					if(outCome.equalsIgnoreCase("SPECIALIST")){
						if(dto.getStrUserName() != null){
//							TmpEmployee employeeByName = getEmployeeByName(dto.getStrUserName());
							
						}
					}
				}
				
				if(dto.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)){
					//claimType.setReimbReqBy("MEDICAL");
					//GALAXYMAIN-9448
					if(reimbursement.getClaim().getLobId() != null && !(reimbursement.getClaim().getLobId().equals(ReferenceTable.PA_LOB_KEY))){
					if(null != sendReplyFrom && !(SHAConstants.SEND_REPLY.equalsIgnoreCase(sendReplyFrom))){
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					}}else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
					}
				}
				
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				
				if(reimbursement.getStage() != null){
				}
				
			
				
				if (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM)){
					
					if (dto != null
							&& dto.getPreauthMedicalDecisionDetails() != null
							&& dto.getPreauthMedicalDecisionDetails().getEscalateTo() != null
							&& dto.getPreauthMedicalDecisionDetails().getEscalateTo()
									.getId() != null) {
						if (dto.getPreauthMedicalDecisionDetails().getEscalateTo()
								.getId().equals(ReferenceTable.RMA1)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,1l);
						} else if (dto.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId().equals(ReferenceTable.RMA2)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,2l);
						} else if (dto.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId().equals(ReferenceTable.RMA3)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,3l);
						} 
						else if (dto.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId().equals(ReferenceTable.RMA4)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,4l);
						} 
						else if (dto.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId().equals(ReferenceTable.RMA5)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,5l);
						} else if (dto.getPreauthMedicalDecisionDetails()
								.getEscalateTo().getId().equals(ReferenceTable.RMA6)) {
							wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID,6l);
						}
						
					}
					
				}
				
								
				if(dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || dto.getStatusKey().equals(
								ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS))
				{
					
				String replyOutcome = (String)wrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
				if(SHAConstants.BILLING_CURRENT_QUEUE.equalsIgnoreCase(replyOutcome)||
						(null != sendReplyFrom && SHAConstants.PARALLEL_SEND_REPLY_BILLING.equalsIgnoreCase(sendReplyFrom)))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLING);
					
					/** The below column updated to  identify the send reply cases in parallel flow. jira -GALAXYMAIN-8985**/
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS,null);
				}
				else if(SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(replyOutcome)||
						(null != sendReplyFrom && SHAConstants.PARALLEL_SEND_REPLY_FA.equalsIgnoreCase(sendReplyFrom)))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_FA);
					/** The below column updated to  identify the send reply cases in parallel flow. jira -GALAXYMAIN-8985**/
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS,null);
				}
				
			}
				wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.MA_STAGE_SOURCE);
				
				if(!(ReferenceTable.getMedicalDecisionButtonStatus().containsKey(dto.getStatusKey()))){
					wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_WAITING_FOR_INPUT);
				}
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.ZMR_STAGE_NAME);
					
				}
				
				wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.MA_STAGE_SOURCE);
				
				
				//humanTask.setPayload(payloadBO);
			}
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			//dbCalService.initiateTaskProcedure(objArrayForSubmit);
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		}
	}
	
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
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
	
	private void updateMedicalApproverForClaimRequest(ReceiptOfDocumentsDTO bean,
			Reimbursement reimbursement, String remarks) {
		Long latestMedicalApproverKey = getLatestMedicalApproverKey(reimbursement
				.getKey());
		if (latestMedicalApproverKey != null) {
			MedicalApprover medicalApproverByKey = getMedicalApproverByKey(latestMedicalApproverKey);
			medicalApproverByKey.setApproverReply(remarks);
			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						bean.getStrUserName(), 15);
			}
//				medicalApproverByKey.setCreatedBy(createdBy);
			medicalApproverByKey.setModifiedBy(createdBy);
			medicalApproverByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(medicalApproverByKey);
			entityManager.flush();
		} else {
			MedicalApprover medicalApprover = new MedicalApprover();
			medicalApprover.setReimbursement(reimbursement);
			medicalApprover.setApproverReply(remarks);
		    
			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						bean.getStrUserName(), 15);
			}
			medicalApprover.setCreatedBy(createdBy);

			entityManager.persist(medicalApprover);
			entityManager.flush();
		}
	}
	
	public MedicalApprover getMedicalApproverByKey(Long primaryKey) {

		Query query = entityManager.createNamedQuery("MedicalApprover.findKey");
		query.setParameter("primaryKey", primaryKey);
		List<MedicalApprover> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}

	public Long getLatestMedicalApproverKey(Long reimbursmentKey) {
		Query query = entityManager
				.createNamedQuery("MedicalApprover.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursmentKey);
		List<MedicalApprover> resultList = query.getResultList();
		List<Long> keysList = new ArrayList<Long>();
		for (MedicalApprover medicalApprover : resultList) {
			entityManager.refresh(medicalApprover);
			if (medicalApprover.getRecordType() != null
					&& SHAConstants.FINANCIAL_REMEDICAL
							.equalsIgnoreCase(medicalApprover.getRecordType())) {
				keysList.add(medicalApprover.getKey());
			}
		}

		if (!keysList.isEmpty()) {
			Long key = Collections.max(keysList);
			return key;
		}

		return null;
	}
	
	public void uploadGeneratedLetterToDMS(WeakHashMap dataMap)
	{
		String filePath = (String)dataMap.get("filePath");
		if(null != filePath && !("").equalsIgnoreCase(filePath))
		{
			SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
		}
	}
}
