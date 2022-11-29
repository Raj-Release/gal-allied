package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.EJB;
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
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.ReportDto;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewRecoverableTableDto;
import com.shaic.claim.claimhistory.view.ompView.OMPAckDocReceivedMapper;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Currency;
import com.shaic.domain.Insured;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPClaimRateChange;
import com.shaic.domain.OMPCloseReopenClaim;
import com.shaic.domain.OMPCurrencyHistory;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPRODDocumentCheckList;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OMPRodRejection;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPBenefitCover;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPNegotiation;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class OMPProcessRODBillEntryService extends
AbstractDAO<OMPReimbursement> {

@PersistenceContext
protected EntityManager entityManager;

@EJB
private OMPClaimService ompClaimService;

@EJB
private MasterService masterService;

private final Logger log = LoggerFactory
	.getLogger(OMPProcessRODBillEntryService.class);

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public OMPReimbursement saveReimbursement(OMPClaimProcessorDTO ompClaimProcessorDTO, OMPClaim claim, OMPReimbursement reimbursement ) {

	OMPAckDocReceivedMapper ackDocRecMapper = OMPAckDocReceivedMapper
			.getInstance();
	if(reimbursement==null){
		 reimbursement = new OMPReimbursement();
		 reimbursement.setCreatedBy(ompClaimProcessorDTO.getUserName());
		 reimbursement.setCreatedDate(new Date());
		 //reimbursement.setBenApprAmt(ompClaimProcessorDTO.getRodProvisionAmt());
	}
	
	OMPClaim ompclaim =  OMPClaimMapper.getInstance().getClaim(ompClaimProcessorDTO.getClaimDto());

	SelectValue eventCode = ompClaimProcessorDTO.getEventCode();
	MastersEvents events = null;
		if(eventCode != null && eventCode.getValue() != null){
			MastersEvents masterEventValueBykey = getMasterEventValueBykey(eventCode.getId());						
			reimbursement.setEventCodeId(masterEventValueBykey);
			claim.setEvent(masterEventValueBykey);
	}
	if(ompClaimProcessorDTO.getRodNumber()==null){
		String generateRODNumber = generateRODNumber(claim.getKey(), claim.getIntimation().getIntimationId());
		if(generateRODNumber!=null&& reimbursement.getRodNumber()== null){
			ompClaimProcessorDTO.setRodNumber(generateRODNumber);
			reimbursement.setRodNumber(generateRODNumber);
		}
	}
	



	List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
	 if(claimCalculationViewTable!= null){}
	
		
	
	
	SelectValue negoName = ompClaimProcessorDTO.getNegotiatorName();
	MastersValue negoNameMast = new MastersValue();
	if(negoName != null){
		negoNameMast.setKey(negoName.getId());
		negoNameMast.setValue(negoName.getValue());						
//		reimbursement.setNegotiatorName(negoNameMast.getValue().toString());
	}

	if(ompClaimProcessorDTO.getOmpPaymentDetailsList()!=null){
		List<OMPPaymentDetailsTableDTO> paymentListDto = ompClaimProcessorDTO.getOmpPaymentDetailsList();
		OMPPaymentDetailsTableDTO paymentDto = new OMPPaymentDetailsTableDTO();
		SelectValue paymentTo = paymentDto.getPaymentTo();
		MastersValue paymentToMast = null;
		if(paymentTo != null && paymentTo.getId() != null){
			paymentToMast = new MastersValue();
			paymentToMast.setKey(paymentTo.getId());
			paymentToMast.setValue(paymentTo.getValue());
			reimbursement.setPaymentTo(paymentToMast);
		}
		reimbursement.setPanNumber(paymentDto.getPanNo());
		reimbursement.setPayeeName(paymentDto.getPayeeNameStr());
		if(paymentDto.getPayMode() != null){
		reimbursement.setPaymentModeId(paymentDto.getPayMode().getId());
		}
		reimbursement.setPayableAt(paymentDto.getPayableAt());
		reimbursement.setEmailId(paymentDto.getEmailId());
	}
	
	OMPDocAcknowledgement acknowledgement =null;
	
	if(ompClaimProcessorDTO.getAckKey()!=null){
		acknowledgement = getAcknowledgementByKey(ompClaimProcessorDTO.getAckKey());
		if(acknowledgement!=null){
			acknowledgement.setModifiedBy(ompClaimProcessorDTO.getUserName());
			acknowledgement.setModifiedDate(new Date());
			ompClaimProcessorDTO.setRodKey(reimbursement.getKey());
			acknowledgement.setRodKey(reimbursement.getKey());
			entityManager.merge(acknowledgement);
		}else{
			acknowledgement = new OMPDocAcknowledgement();
			Status status = new Status();
			status.setKey(ReferenceTable.OMP_ACKNOWLEDGE_STATUS_KEY);

			Stage stage = new Stage();
			stage.setKey(ReferenceTable.OMP_ACKNOWLEDGE_STAGE_KEY);
			acknowledgement.setStage(stage);
			acknowledgement.setStatus(status);
			acknowledgement.setClaim(claim);
			
			acknowledgement.setCreatedBy(ompClaimProcessorDTO.getUserName());
			acknowledgement.setCreatedDate(new Date());
			ompClaimProcessorDTO.setRodKey(reimbursement.getKey());
			acknowledgement.setRodKey(reimbursement.getKey());
			entityManager.persist(acknowledgement);
		}
	}
	//need to call  save acknowledge method
// comented for ack created before rod.
/*	if(reimbursement.getDocAcknowLedgement()!=null){
		acknowledgement = reimbursement.getDocAcknowLedgement();
		acknowledgement.setModifiedBy(ompClaimProcessorDTO.getUserName());
		acknowledgement.setModifiedDate(new Date());
		ompClaimProcessorDTO.setRodKey(reimbursement.getKey());
		acknowledgement.setRodKey(reimbursement.getKey());
		entityManager.merge(acknowledgement);
	}else{
		acknowledgement = new OMPDocAcknowledgement();
		acknowledgement.setStage(claim.getStage());
		acknowledgement.setStatus(claim.getStatus());
		acknowledgement.setClaim(claim);
		
		acknowledgement.setCreatedBy(ompClaimProcessorDTO.getUserName());
		acknowledgement.setCreatedDate(new Date());
		ompClaimProcessorDTO.setRodKey(reimbursement.getKey());
		acknowledgement.setRodKey(reimbursement.getKey());
		entityManager.persist(acknowledgement);
	}*/
	//claim.setModifiedBy(ompClaimProcessorDTO.getUserName());
	//claim.setModifiedDate(new Date());
	//entityManager.merge(claim);
	
	reimbursement.setInsuredKey(claim.getInsuredKey());
	reimbursement.setClaim(claim);
//	entityManager.refresh(acknowledgement);
	reimbursement.setDocAcknowLedgement(acknowledgement);	
	reimbursement.setCreatedBy(ompClaimProcessorDTO.getUserName());
	
	reimbursement.setModifiedBy(ompClaimProcessorDTO.getUserName());
	reimbursement.setModifiedDate(new Date());
	/*reimbursement.setStage(claim.getStage());
	reimbursement.setStatus(claim.getStatus());*/
	//insertOrUpdate(reimbursement);
	
	//BILL ENTRY
			//saveBenefit(ompClaimProcessorDTO, reimbursement);	
			
			List<DocumentCheckListDTO> documentCheckListDto = ompClaimProcessorDTO.getDocumentDetails().getDocumentCheckList();
			if (documentCheckListDto!=null && !documentCheckListDto.isEmpty()){
			for (DocumentCheckListDTO ompdocumentCheckListDto : documentCheckListDto) {
				if (null != ompdocumentCheckListDto.getReceivedStatus()
						&& !("").equalsIgnoreCase(ompdocumentCheckListDto
								.getReceivedStatus().getValue())) {
						
			OMPRODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
					.getRODDocumentCheckList(ompdocumentCheckListDto);
			rodDocumentCheckList.setDocAcknowledgement(acknowledgement);
			// findRODDocumentCheckListByKey(masterService);
			entityManager.persist(rodDocumentCheckList);
			entityManager.flush();
			log.info("------RODDocumentCheckList------>"
					+ rodDocumentCheckList + "<------------");
				}
			}
			}
			ReceiptOfDocumentsDTO receiptOfDocumentsDTO = ompClaimProcessorDTO.getReceiptOfDocumentsDTO();
			
	ClaimDto responseClaim = new OMPClaimMapper().getClaimDto(ompclaim);
	responseClaim.setNewIntimationDto(ompClaimProcessorDTO.getNewIntimationDto());
	if(null != responseClaim.getKey()){
		
	
	}
	BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
	BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
	ompClaimProcessorDTO.setPaymentToContainer(paymentTo);
	ompClaimProcessorDTO.setPaymentModeContainer(paymentMode);
	if(reimbursement!=null && claim!=null){
		DBCalculationService calculationService = new DBCalculationService();
		if(reimbursement.getKey()!=null && claim.getKey()!=null && reimbursement.getCurrentProvisionAmt()!=null){
			calculationService.getOmpProvisionCalc(reimbursement.getKey(), claim.getKey());
		}
		
	}
	return reimbursement;
}


public void saveBenefit(List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable,
		OMPReimbursement reimbursement) {
	if(claimCalculationViewTable != null){
	for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : claimCalculationViewTable) {
		if(ompClaimCalculationViewTableDTO.getKey()!= null){
			OMPBenefitCover benefitCover = new OMPBenefitCover();
			OMPBenefitCover saveBenefitCover = saveBenefitCover(ompClaimCalculationViewTableDTO, benefitCover);
			saveBenefitCover.setRodKey(reimbursement);
		}
		
//				insertOrUpdateBenefit(saveBenefitCover);
	}
	}
}


@TransactionAttribute(TransactionAttributeType.REQUIRED)
public OMPBenefitCover saveBenefitCover(OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO ,OMPBenefitCover benefitCover) {
	

	benefitCover.setBillAmount(ompClaimCalculationViewTableDTO.getBillAmt());
	benefitCover.setNetAmt(ompClaimCalculationViewTableDTO.getAmtIn());
	benefitCover.setDeductibleNonPayBles(ompClaimCalculationViewTableDTO.getDeduction());
	benefitCover.setTotalAmtPayBleDollar(ompClaimCalculationViewTableDTO.getTotalAmt());
	benefitCover.setTotalAmtInr(ompClaimCalculationViewTableDTO.getTotalAmtInr());
	/*benefitCover.setApprovedAmountDollor(ompClaimCalculationViewTableDTO.getApprovedAmt());
	benefitCover.setAgreedAmtDollar(ompClaimCalculationViewTableDTO.getAgreedAmt());
	benefitCover.setDiffAmtDollar(ompClaimCalculationViewTableDTO.getDifferenceAmt());
	benefitCover.setExpenesesDollar(ompClaimCalculationViewTableDTO.getExpenses());
	benefitCover.setNegoFeesClaimedDollar(ompClaimCalculationViewTableDTO.getNegotiationClaimed());
	benefitCover.setNegoFeeCapping(ompClaimCalculationViewTableDTO.getNegotiationCapping());
	benefitCover.setNegoFeesClaimedInr(ompClaimCalculationViewTableDTO.getNegotiationPayable());
	benefitCover.setHandlingChargesDollar(ompClaimCalculationViewTableDTO.getHandlingCharges());
	benefitCover.setTotalExpenceDollar(ompClaimCalculationViewTableDTO.getTotalExp());*/
	benefitCover.setDeletedFlag(ompClaimCalculationViewTableDTO.getDeleted());
	if(ompClaimCalculationViewTableDTO.getCopay()!= null && ompClaimCalculationViewTableDTO.getCopay().getId()!= null){
		SelectValue copayValue = new SelectValue();
		copayValue.setId(ompClaimCalculationViewTableDTO.getCopay().getId());
		copayValue.setValue(ompClaimCalculationViewTableDTO.getCopay().getValue());
		benefitCover.setCopayPercentage(copayValue.getId());
	}
	benefitCover.setApprAmtAftrCopya(ompClaimCalculationViewTableDTO.getApprovedamountaftecopay());
	benefitCover.setCopayAmount(ompClaimCalculationViewTableDTO.getCopayamount());
	/*SelectValue category = ompClaimCalculationViewTableDTO.getCategory();
	MastersValue categoryMast = null;
	if(category != null && category.getId() != null){
		categoryMast = new MastersValue();
		categoryMast.setKey(category.getId());
		categoryMast.setValue(category.getValue());						
	}
	benefitCover.setCategory(categoryMast);*/

	return benefitCover;
	
}

private OMPBenefitCover insertOrUpdateBillEntey(OMPBenefitCover benefitCover, OMPClaimCalculationViewTableDTO claimCalculationViewTable) {
	benefitCover.setBillAmount(claimCalculationViewTable.getBillAmt());
	benefitCover.setAmountDoller(claimCalculationViewTable.getAmtIn());
	benefitCover.setDeductibleNonPayBles(claimCalculationViewTable.getDeduction());
	benefitCover.setTotalAmtPayBleDollar(claimCalculationViewTable.getTotalAmt());
	
	return null;
}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public OMPClaimPayment saveClaimpayment(OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO ,OMPClaimPayment claimpayment,OMPClaim claim,OMPReimbursement reimbursement, OMPClaimProcessorDTO ompClaimProcessorDTO) {
	
	if(claimpayment == null){
		claimpayment = new OMPClaimPayment();
	}
		claimpayment.setClaimNumber(claim.getClaimId());
		claimpayment.setIntimationNumber(claim.getIntimation().getIntimationId());
		claimpayment.setCreatedBy(ompClaimProcessorDTO.getUserName());
		claimpayment.setCreatedDate(new Date());
		if(claim.getIntimation().getInsured()!=null){
			claimpayment.setRiskId(claim.getIntimation().getInsured().getInsuredId());
			claimpayment.setInsuredName(claim
					.getIntimation().getInsured().getInsuredName());
		}
		if (ompClaimCalculationViewTableDTO != null) {
			claimpayment.setConversionRate(ompClaimCalculationViewTableDTO.getConversionValue());
			List<OMPPaymentDetailsTableDTO> paymentListDto = ompClaimCalculationViewTableDTO
					.getOmpPaymentDetailsList();
			if (paymentListDto != null && !paymentListDto.isEmpty() && paymentListDto.size()>0
					&& paymentListDto.get(0) != null) {
				OMPPaymentDetailsTableDTO paymentDto = paymentListDto.get(0);
				SelectValue paymentTo = paymentDto.getPaymentTo();
				MastersValue paymentToMast = null;
				if (paymentTo != null && paymentTo.getId() != null) {
					paymentToMast = new MastersValue();
					paymentToMast.setKey(paymentTo.getId());
					paymentToMast.setValue(paymentTo.getValue());
					claimpayment.setPaymentStatusId(paymentToMast);
				}
				claimpayment.setPanNo(paymentDto.getPanNo());
				claimpayment.setPayeeName(paymentDto.getPayeeNameStr());
				claimpayment.setPayabelAt(paymentDto.getPayableAt());
				claimpayment.setEmailId(paymentDto.getEmailId());

				SelectValue paymentType = paymentDto.getPayMode();
				if (paymentType != null) {
					MastersValue paymentTypeMast = new MastersValue();
					paymentTypeMast.setKey(paymentType.getId());
					paymentTypeMast.setValue(paymentType.getValue());
					claimpayment.setPaymentType(paymentTypeMast);
				}
				claimpayment.setTotApprovedAmt(ompClaimCalculationViewTableDTO
						.getFinalApprovedAmtInr());
				claimpayment.setApprovedAmt(ompClaimCalculationViewTableDTO
						.getApprovedAmt());
				claimpayment
						.setApprovedAmtDollar(ompClaimCalculationViewTableDTO
								.getFinalApprovedAmtDollor());
				claimpayment.setAgreedAmtDollar(ompClaimCalculationViewTableDTO
						.getAgreedAmt());
				claimpayment.setExpensesDollar(ompClaimCalculationViewTableDTO
						.getExpenses());
				claimpayment
						.setDeductibleNonPayBles(ompClaimCalculationViewTableDTO
								.getDeduction());
				claimpayment.setDiffAmtDollar(ompClaimCalculationViewTableDTO
						.getDifferenceAmt());
				claimpayment.setRodNumber(reimbursement.getRodNumber());
				claimpayment.setHandlingChargesDollar(ompClaimCalculationViewTableDTO.getHandlingCharges());
				/*
				 * claimpayment.setStageid(reimbursement.getStage());
				 * claimpayment.setStatusId(reimbursement.getStatus());
				 */
				claimpayment
						.setTotalExpenceDollar(ompClaimCalculationViewTableDTO
								.getTotalExp());
				claimpayment.setTotAmtINr(ompClaimCalculationViewTableDTO
						.getTotalAmtInr());
				if (reimbursement != null) {
					if (reimbursement.getClassificationId() != null) {
						claimpayment.setClmSecCode(reimbursement
								.getClassificationId().getValue());
					}
					if (reimbursement.getSubClassificationId() != null) {
						claimpayment.setClmSubCvrCode(reimbursement
								.getSubClassificationId().getValue());
					}
					if (reimbursement.getEventCodeId() != null) {
						claimpayment.setClmCvrCode(reimbursement
								.getEventCodeId().getEventCode());
					}
					claimpayment.setPioCode(reimbursement.getClaim()
							.getIntimation().getPolicy().getHomeOfficeCode());
					claimpayment.setProductCode(reimbursement.getClaim()
							.getProductCode());
					MastersValue claimType = reimbursement.getClaim()
							.getClaimType();
					if (claimType != null) {
						claimpayment.setClaimType(claimType.getValue());
					}
					claimpayment.setPolicyNo(reimbursement.getClaim()
							.getIntimation().getPolicy().getPolicyNumber());
					claimpayment.setPolicySysId(reimbursement.getClaim()
							.getIntimation().getPolicy().getPolicySystemId());
					OMPHospitals hospitalDetailsBykey = getHospitalDetailsBykey(reimbursement
							.getClaim().getHospital());
					if (hospitalDetailsBykey != null) {
						claimpayment.setHospitalCode(hospitalDetailsBykey
								.getHospitalCode());
					}
					claimpayment.setProposerCode(reimbursement.getClaim()
							.getIntimation().getPolicy().getProposerCode());
					claimpayment
							.setProposerName(reimbursement.getClaim()
									.getIntimation().getPolicy()
									.getProposerFirstName());
					claimpayment.setFaApprovalDate(new Date());
					if (reimbursement.getClassiDocumentRecivedFmId() != null) {
						claimpayment.setDocReceicedFrom(reimbursement
								.getClassiDocumentRecivedFmId().getValue());
					}
					if (reimbursement.getCategoryId() != null) {
						claimpayment.setExpensesType(reimbursement
								.getCategoryId().getValue());
					}
				}
			}
			if (ompClaimCalculationViewTableDTO.getOmpRecoverableTableList() != null
					&& ompClaimCalculationViewTableDTO
							.getOmpRecoverableTableList().get(0) != null) {
				OMPNewRecoverableTableDto newRecoverableTableDtos = ompClaimCalculationViewTableDTO
						.getOmpRecoverableTableList().get(0);
				if (newRecoverableTableDtos.getKey() == null) {
					if (newRecoverableTableDtos.getAmountRecoveredInr() != null) {
						claimpayment.setAmtRecoveredInr(newRecoverableTableDtos
								.getAmountRecoveredInr());
					}
					if (newRecoverableTableDtos.getAmountRecoveredUsd() != null) {
						claimpayment.setAmountDoller(newRecoverableTableDtos
								.getAmountRecoveredUsd());
					}
					claimpayment.setDateRecovery(newRecoverableTableDtos
							.getDateofRecovery());
					claimpayment.setRemarks(newRecoverableTableDtos
							.getRemarks());
					claimpayment
							.setRemarksProcessor(ompClaimCalculationViewTableDTO.getProcessorRemarks());
					claimpayment
							.setRemarksApprover(ompClaimCalculationViewTableDTO.getReasonForApproval());
					reimbursement
							.setFaSubmitFlg(ompClaimCalculationViewTableDTO
									.getSubmit());
					insertOrUpdate(reimbursement);
				}
			}
		}
		
		claimpayment.setRemarksProcessor(ompClaimCalculationViewTableDTO.getProcessorRemarks());
		claimpayment.setRemarksApprover(ompClaimCalculationViewTableDTO.getReasonForApproval());
		if(ompClaimCalculationViewTableDTO.getReasonForRejectionRemarks()!=null){
			claimpayment.setReasonSugesstApproval(ompClaimCalculationViewTableDTO.getReasonForRejectionRemarks().getValue());
		}
		insertOrUpdatePayment(claimpayment);
	
	
	return claimpayment;
	
}

public OMPDocumentDetails submitSearchOrUploadDocumentsForAckNotReceived(UploadDocumentDTO uploadDocumentDTO){
	//List<UploadDocumentDTO> uploadDocsDTO = uploadDto.getUploadDocsList();
	String userName = SHAUtils.getUserNameForDB(uploadDocumentDTO.getUsername());
	OMPReimbursement reimbursementByKey = getReimbursementByKey(uploadDocumentDTO.getRodKey());
	//if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
	//	for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			if (null != uploadDocumentDTO.getFileTypeValue()	/*&& reimbursementByKey!=null */
					&& !("").equalsIgnoreCase(uploadDocumentDTO.getFileTypeValue())) {
				OMPDocumentDetails documentDetails =null;
				if(uploadDocumentDTO.getDmsDocToken()!=null && !uploadDocumentDTO.getDmsDocToken().equalsIgnoreCase("")){
					documentDetails = getDocumentDetailsBytoken(Long.parseLong(uploadDocumentDTO.getDmsDocToken()));
				}
				if(documentDetails==null){
					 documentDetails =  new OMPDocumentDetails();
				}
				OMPClaim claim = null;
				if(documentDetails != null && documentDetails.getClaimNumber() != null) {
					claim = ompClaimService.searchByClaimNum(documentDetails.getClaimNumber());	
				}
				
				if(claim!=null){
					documentDetails.setIntimationNumber(claim.getIntimation().getIntimationId());
					documentDetails.setClaimNumber(claim.getClaimId());
				}else{
					documentDetails.setIntimationNumber(uploadDocumentDTO.getIntimationNo());
					documentDetails.setClaimNumber(uploadDocumentDTO.getClaimNo());
				}
				if(reimbursementByKey!=null && uploadDocumentDTO.getAcknowledgementNo()==null){
					documentDetails.setReimbursementNumber(reimbursementByKey.getRodNumber());	
				}
				if(uploadDocumentDTO.getFileName()!=null){
					documentDetails.setFileName(uploadDocumentDTO.getFileName());
				}
				documentDetails.setDocumentType(uploadDocumentDTO.getDocumentTypeValue());
				documentDetails.setFileType(uploadDocumentDTO.getFileTypeValue());
				documentDetails.setNoOfDoc(uploadDocumentDTO.getNoOfItems());
				documentDetails.setRecievedStatus(uploadDocumentDTO.getReceivStatusValue());
				if(null != uploadDocumentDTO.getDmsDocToken()){
				documentDetails.setDocumentToken(Long.parseLong(uploadDocumentDTO.getDmsDocToken()));
				}
				if(uploadDocumentDTO.getAckDocumentSource()!=null){
					documentDetails.setDocumentSource(uploadDocumentDTO.getAckDocumentSource());
				}else{
					documentDetails.setDocumentSource(SHAConstants.POST_PROCESS); 
				}
				
				documentDetails.setDocRecievedDate(uploadDocumentDTO.getDocReceivedDate());
				documentDetails.setDocAcknowledgementDate(uploadDocumentDTO.getDocReceivedDate());
				documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setCreatedBy(userName);
				documentDetails.setKey(uploadDocumentDTO.getDocDetailsKey());
				documentDetails.setDeletedFlag(uploadDocumentDTO.getDeleted());
				documentDetails.setRemarks(uploadDocumentDTO.getRemarks());
				if(uploadDocumentDTO.getAcknowledgementNo()!=null){
					documentDetails.setAcknowledgementNumber(uploadDocumentDTO.getAcknowledgementNo());
				}
				if(null!= documentDetails.getDocumentKey()){
					
					entityManager.merge(documentDetails); 
					entityManager.flush();
					entityManager.refresh(documentDetails);
				}
				else 
				{	
					entityManager.persist(documentDetails);
					entityManager.flush();
					entityManager.refresh(documentDetails);
				}
				
				List<UploadDocumentDTO> deletedDocsList = uploadDocumentDTO.getDeletedDocumentList();
				if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {
						if(null != uploadDocumentDTO2.getDocDetailsKey())
						{
							OMPDocumentDetails documentDetailsObj = getDocumentDetailsByKey(uploadDocumentDTO2.getDocDetailsKey());
							documentDetailsObj.setDeletedFlag("Y");
							if (null != uploadDocumentDTO2.getDocDetailsKey()) {
								//documentDetailsObj.setModifiedBy("");
								//documentDetailsObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(documentDetailsObj);
								entityManager.flush();
								log.info("------RODDocumentSummary------>"+documentDetailsObj+"<------------");
							}
						}
					}
				}
			}
		//}
	//}
	return null;
}


@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void insertOrUpdate(OMPReimbursement reimbursement){
	
	if(reimbursement != null){
		if(reimbursement.getKey() == null){
			entityManager.persist(reimbursement);
			
		}
		else if(reimbursement != null && reimbursement.getKey() != null ){
			entityManager.merge(reimbursement);
			
		}
		entityManager.flush();
		//entityManager.refresh(reimbursement);
	}

}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void insertOrUpdatePayment(OMPClaimPayment claimpayment){
	
	if(claimpayment != null){
		if(claimpayment.getPaymentKey() == null){
			
			entityManager.persist(claimpayment);
			
		}
		else if(claimpayment != null && claimpayment.getPaymentKey() != null ){
			entityManager.merge(claimpayment);
		}
		entityManager.flush();
		//entityManager.refresh(reimbursement);
	}

}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void insertOrUpdateBenefit(OMPBenefitCover benefitCover){
	
	if(benefitCover != null){
		if(benefitCover.getKey() == null){
			entityManager.persist(benefitCover);
			
		}
		else if(benefitCover != null && benefitCover.getKey() != null ){
			entityManager.merge(benefitCover);
		}
		entityManager.flush();
		//entityManager.refresh(reimbursement);
	}

}

public Object[] getParamter(OMPClaimProcessorDTO ompClaimProcessorDTO , OMPClaim claim ,String outCome, OMPReimbursement reimbursement)
	{
	OMPDocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
	OMPClaim claimObj = entityManager.find(OMPClaim.class, ompClaimProcessorDTO.getClaimDto().getKey());
	Map<String,Object> workTaskMap = (Map<String,Object>)ompClaimProcessorDTO.getDbOutArray();
	Long workFlowKey = 0l;
	ompClaimProcessorDTO.setOutCome(outCome);
	if(workTaskMap != null && workTaskMap.containsKey(SHAConstants.WK_KEY) && workTaskMap.get(SHAConstants.WK_KEY) != null){
		workFlowKey = (Long)workTaskMap.get(SHAConstants.WK_KEY);
	}
	Object[] arrayListForDBCall = SHAUtils.getOMPRevisedArrayListForDBCall(claimObj);
	
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
	
	Insured insured = claimObj.getIntimation().getInsured();

	if (claimObj != null && claimObj.getIsVipCustomer() != null
			&& claimObj.getIsVipCustomer().equals(1l)) {

		inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
	//	classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
	} else if (insured != null && insured.getInsuredAge() != null
			&& insured.getInsuredAge() > 60) {
		//classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
	} else {
		inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
		//classificationType.setPriority(SHAConstants.NORMAL);
	}
	Boolean isReconsideration = Boolean.FALSE;
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
	}  else {
		
		inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.N_FLAG;
		inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
	}
	inputArray[SHAConstants.INDEX_ROD_KEY] = reimbursement.getKey();
	
	
	List<UploadDocumentDTO> uploadDocsList = ompClaimProcessorDTO.getUploadDocsList();

	if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
			if (null != uploadDocumentDTO.getFileType()
					&& null != uploadDocumentDTO.getFileType().getValue()
					&& uploadDocumentDTO.getFileType().getValue()
							.contains("Bill")) {
				inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "Y";
				break;
			} else {
				inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "N";
			}
		}
	}
	
	if (null != ompClaimProcessorDTO.getDocumentDetails().getHospitalization()
			&& ompClaimProcessorDTO.getDocumentDetails().getHospitalization()) {
		inputArray[SHAConstants.INDEX_HOSPITALIZATION] = "Y";
		
	} else {
		inputArray[SHAConstants.INDEX_HOSPITALIZATION] = "N";
	}
	if (null != ompClaimProcessorDTO.getDocumentDetails().getPartialHospitalization()
			&& ompClaimProcessorDTO.getDocumentDetails().getPartialHospitalization()) {
		inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "N";
	}
	if (null != ompClaimProcessorDTO.getDocumentDetails().getPreHospitalization()
			&& ompClaimProcessorDTO.getDocumentDetails().getPreHospitalization()) {
		inputArray[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "N";
	}
	
	
	if (null != ompClaimProcessorDTO.getDocumentDetails().getPostHospitalization()
			&& ompClaimProcessorDTO.getDocumentDetails().getPostHospitalization()) {
		inputArray[SHAConstants.INDEX_POST_HOSPITALIZATION] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_POST_HOSPITALIZATION] = "N";
	}

	if (null != ompClaimProcessorDTO.getDocumentDetails().getLumpSumAmount()
			&& ompClaimProcessorDTO.getDocumentDetails().getLumpSumAmount()) {
		inputArray[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "Y";
		inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "N";
	}

	if (null != ompClaimProcessorDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
			&& ompClaimProcessorDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
//		payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
//		payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "Y";
		inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "N";
	}
	
	if (null != ompClaimProcessorDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
			&& ompClaimProcessorDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "Y";
		inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "Y";
	} else {
		inputArray[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "N";
	}
	
	if (null != ompClaimProcessorDTO.getDocumentDetails().getHospitalizationRepeat()
			&& ompClaimProcessorDTO.getDocumentDetails().getHospitalizationRepeat()) {
		
		inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "Y";
	} 
	

	//inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = "Y";
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_CREATE_ROD;
		
		if(isReconsideration) {
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.RECONSIDERATION;
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "Y";
		}
	
		if(claim != null && claim.getClaimType() != null && claim.getClaimType().getValue() != null){
			inputArray[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType().getValue();
		}
		
		if((SHAConstants.YES_FLAG).equalsIgnoreCase(ompClaimProcessorDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			inputArray[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "Y";
			inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "Y";
		}
		else if((SHAConstants.N_FLAG).equalsIgnoreCase(ompClaimProcessorDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			inputArray[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "N";
		}
		
		if(null != reimbursement.getSkipZmrFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(reimbursement.getSkipZmrFlag()))
		{
			inputArray[SHAConstants.INDEX_ZONAL_BYPASS] = "Y";
		}
		
		if(null != reimbursement && null!= reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() &&  null != reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue())
		{
			inputArray[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
		}
		
		inputArray[SHAConstants.INDEX_OUT_COME] = ompClaimProcessorDTO.getOutCome();
		
		inputArray[SHAConstants.INDEX_CLAIMED_AMT] = claim.getClaimedAmount();
		
		if(ompClaimProcessorDTO.getClassification()!=null){
			inputArray[SHAConstants.INDEX_CLASSIFICATION] = ompClaimProcessorDTO.getClassification().getValue();
		}
		if(ompClaimProcessorDTO.getSubClassification()!=null){
			inputArray[SHAConstants.INDEX_SUB_CLASSIFICATION] = ompClaimProcessorDTO.getSubClassification().getValue();
		}
		if(ompClaimProcessorDTO.getEventCode()!=null){
			inputArray[SHAConstants.INDEX_EVENT_CODE] = ompClaimProcessorDTO.getEventCode().getValue();
		}
		inputArray[SHAConstants.INDEX_AILMENT_LOSS] = ompClaimProcessorDTO.getAilmentLoss();
		//dbCalculationService.initiateOMPTaskProcedure(parameter);
			
		return parameter;
			
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
	
	public OMPReimbursement	getReimbursementByRodNo(String rodNo) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findOMPRodByNumber")
				.setParameter("rodNumber", rodNo);
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
	public void updateReimbursement(Long rodKey, Stage stgObj, Status statusObj,Double benAprAmt) {
		OMPReimbursement reimbursement = null;
		if(rodKey != null){
			Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey")
					.setParameter("primaryKey", rodKey);
			List<OMPReimbursement> rodList = query.getResultList();
		
			if (rodList != null && !rodList.isEmpty()) {
				 reimbursement = rodList.get(0);
					if(reimbursement != null){
						entityManager.refresh(reimbursement);
						if(benAprAmt!=null){
							reimbursement.setBenApprAmt(0d);
						}
						reimbursement.setStage(stgObj);
						reimbursement.setStatus(statusObj);
						entityManager.merge(reimbursement);
						
						reimbursement.getDocAcknowLedgement().setStage(stgObj);
						reimbursement.getDocAcknowLedgement().setStatus(statusObj);
						entityManager.merge(reimbursement.getDocAcknowLedgement());
						
						reimbursement.getClaim().setStage(stgObj);
						reimbursement.getClaim().setStatus(statusObj);
						entityManager.merge(reimbursement.getClaim());						
						entityManager.flush();
						
					}
			}
		}		
	}
	

@Override
public Class<OMPReimbursement> getDTOClass() {
	// TODO Auto-generated method stub
	return null;
	}

@SuppressWarnings({ "unchecked", "unused" })
public List<ViewDocumentDetailsDTO> listOfEarlierAckByClaimKey(Long a_key,
		Long reimbursementKey) {
	Query query = entityManager
			.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
	query = query.setParameter("claimkey", a_key);

	// Integer.parseInt(Strin)
	List<Long> keysList = new ArrayList<Long>();
	List<OMPDocAcknowledgement> docAcknowledgmentList = (List<OMPDocAcknowledgement>) query
			.getResultList();

	List<OMPDocAcknowledgement> earlierAck = new ArrayList<OMPDocAcknowledgement>();
	for (OMPDocAcknowledgement OMPDocAcknowledgement : docAcknowledgmentList) {
		// if(OMPDocAcknowledgement.getRodKey() != null){
		// if(! OMPDocAcknowledgement.getRodKey().equals(reimbursementKey)){
		// earlierAck.add(OMPDocAcknowledgement);
		// }
		earlierAck.add(OMPDocAcknowledgement);
		keysList.add(OMPDocAcknowledgement.getKey());
		// }
	}

	Long maximumKey = 0l;
	if (!keysList.isEmpty()) {
		maximumKey = Collections.max(keysList);
	}

	for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {
		entityManager.refresh(OMPDocAcknowledgement);
	}

	List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
			.getInstance().getDocumentDetailsTableDTO(earlierAck);

	for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

		String date = SHAUtils.getDateFormat(documentDetailsDTO
				.getReceivedDate());

		if (documentDetailsDTO.getRodKey() != null) {
			Long rodKey = new Long(documentDetailsDTO.getRodKey());
			Query rodQuery = entityManager
					.createNamedQuery("OMPReimbursement.findByKey");
			rodQuery = rodQuery.setParameter("primaryKey", rodKey);

			OMPReimbursement OMPReimbursement = (OMPReimbursement) rodQuery
					.getSingleResult();
			if (OMPReimbursement != null) {
				entityManager.refresh(OMPReimbursement);
			}
			documentDetailsDTO.setRodNumber(OMPReimbursement.getRodNumber());
			documentDetailsDTO.setReimbursementKey(OMPReimbursement.getKey());

			if (documentDetailsDTO.getKey() != null
					&& !documentDetailsDTO.getKey().equals(
							OMPReimbursement.getDocAcknowLedgement().getKey())) {
				documentDetailsDTO.setIsReadOnly(true);
			}

			// documentDetailsDTO.setMedicalResponseTime(OMPReimbursement
			// .getMedicalCompletedDate());
			documentDetailsDTO.setApprovedAmount(OMPReimbursement
					.getApprovedAmount());

			// if(OMPReimbursement.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)
			// ||
			// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
			// ||
			// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
			//
			// documentDetailsDTO.setApprovedAmount(OMPReimbursement.getCurrentProvisionAmt());
			//
			// }

			documentDetailsDTO.setStatus(OMPReimbursement.getStatus()
					.getProcessValue());
			documentDetailsDTO.setStatusKey(OMPReimbursement.getStatus()
					.getKey());
			if (OMPReimbursement.getMedicalCompletedDate() != null) {
				documentDetailsDTO
						.setMedicalResponseTime(SHAUtils
								.formatDate(OMPReimbursement
										.getMedicalCompletedDate()));
			}

			documentDetailsDTO.setApprovedAmount(OMPReimbursement
					.getCurrentProvisionAmt());
			// documentDetailsDTO.setApprovedAmount(OMPReimbursement.getCurrentProvisionAmt());

			if (OMPReimbursement.getStage().getKey()
					.equals(ReferenceTable.BILLING_STAGE)) {

				// Long approvedAmount =
				// getReimbursementApprovedAmount(OMPReimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				
				if(OMPReimbursement
						.getBillingApprovedAmount() == null){
					documentDetailsDTO.setApprovedAmount(OMPReimbursement
							.getCurrentProvisionAmt());
					
				}else {
					if(OMPReimbursement
							.getBillingApprovedAmount().equals(0)){
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getCurrentProvisionAmt());
					}else{
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getBillingApprovedAmount());
					}
				}
				

			} else if (OMPReimbursement.getStage().getKey()
					.equals(ReferenceTable.FINANCIAL_STAGE)) {
				// Long approvedAmount =
				// getReimbursementApprovedAmount(OMPReimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				
				if(OMPReimbursement
						.getFinancialApprovedAmount() == null){
					documentDetailsDTO.setApprovedAmount(OMPReimbursement
							.getCurrentProvisionAmt());
					
				}else{
				
					if(OMPReimbursement
							.getFinancialApprovedAmount().equals(0)){
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getCurrentProvisionAmt());
					}
					else{
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getFinancialApprovedAmount());	
					}
				
				}
				
			}

			// if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(OMPReimbursement.getStatus().getKey())){
			// Double provisionAmount = getProvisionAmount(OMPReimbursement);
			// documentDetailsDTO.setApprovedAmount(provisionAmount);
			// }

			if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
					.containsKey(OMPReimbursement.getStatus().getKey())) {
				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getCurrentProvisionAmt());
			}

			if (OMPReimbursement
					.getStatus()
					.getKey()
					.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)) {

				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getCurrentProvisionAmt());

			}

			if (ReferenceTable.CANCEL_ROD_KEYS.containsKey(OMPReimbursement
					.getStatus().getKey())) {
				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getCurrentProvisionAmt());
			}

			if (OMPReimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {

				String statusValue = "MA - "
						+ OMPReimbursement.getStatus().getProcessValue();
				documentDetailsDTO.setStatus(statusValue);

			} else if (OMPReimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {

				String statusValue = "FA - "
						+ OMPReimbursement.getStatus().getProcessValue();
				documentDetailsDTO.setStatus(statusValue);
			}

		}
	}

	List<String> hospitalization = new ArrayList<String>();

	for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {

		getDocumentBillClassification(hospitalization, OMPDocAcknowledgement);				//chk

	}

	for (int i = 0; i < listDocumentDetails.size(); i++) {

		listDocumentDetails.get(i).setBillClassification(
				hospitalization.get(i));
		// listDocumentDetails.get(0).setLatestKey(maximumKey);
		OMPReimbursement OMPReimbursement = null;
		if (reimbursementKey != null) {
			OMPReimbursement = getReimbursement(reimbursementKey);
		}
		if (OMPReimbursement != null
				&& OMPReimbursement.getDocAcknowLedgement() != null) {
			listDocumentDetails.get(0).setLatestKey(
					OMPReimbursement.getDocAcknowLedgement().getKey());
		} else {
			listDocumentDetails.get(0).setLatestKey(maximumKey);
		}
	}

	return listDocumentDetails;
}


private String getDocumentBillClassification(List<String> hospitalization,
		OMPDocAcknowledgement docAcknowledgement) {
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

	hospitalization.add(classification);

	return classification;
}

@SuppressWarnings("unchecked")
public OMPReimbursement getReimbursement(Long rodkey) {

	Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey");
	query.setParameter("primaryKey", rodkey);

	List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
			.getResultList();

	if (reimbursementList != null && !reimbursementList.isEmpty()) {

		entityManager.refresh(reimbursementList.get(0));
		return reimbursementList.get(0);
	}

	return null;

}


@SuppressWarnings("unchecked")
public OMPReimbursement getLatestReimbursementDetailsByclaimKey(
		Long claimKey) {

	OMPReimbursement reimbursement = null;
	Query query = entityManager
			.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
	query.setParameter("claimKey", claimKey);

	List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
			.getResultList();

	if (null != reimbursementList && !reimbursementList.isEmpty()) {

		for (OMPReimbursement viewTmpReimbursement : reimbursementList) {
			
			if (viewTmpReimbursement.getStatus() != null && viewTmpReimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_SETTLED)) {
				reimbursement = viewTmpReimbursement;
				break;
			}
		}

	}

	return reimbursement;

}

public OMPClaimPayment getClaimPaymentByRodNumber(String rodNumber) {

	Query query = entityManager
			.createNamedQuery("OMPClaimPayment.findByRodNo");
	query.setParameter("rodNumber", rodNumber);

	List<OMPClaimPayment> result = (List<OMPClaimPayment>) query
			.getResultList();

	if (result != null && !result.isEmpty()) {
		return result.get(0);
	}

	return null;

}

@SuppressWarnings("unchecked")
public OMPClaimPayment getRimbursementForPayment(String claimNumber) {

	Query query = entityManager
			.createNamedQuery("OMPClaimPayment.findByClaimNumber");
	query.setParameter("claimNumber", claimNumber);

	List<OMPClaimPayment> reimbursementList = (List<OMPClaimPayment>) query
			.getResultList();

	if (reimbursementList != null && !reimbursementList.isEmpty()) {
		return reimbursementList.get(0);
	}

	return null;

}


public OMPDocAcknowledgement findAcknowledgment(Long rodKey) {
	Query query = entityManager
			.createNamedQuery("OMPDocAcknowledgement.findByRODKey");
	query = query.setParameter("rodKey", rodKey);

	// Integer.parseInt(Strin)
	List<OMPDocAcknowledgement> earlierAck = (List<OMPDocAcknowledgement>) query
			.getResultList();
	List<Long> keys = new ArrayList<Long>();

	for (OMPDocAcknowledgement docAcknowledgement : earlierAck) {

		keys.add(docAcknowledgement.getKey());

	}
	if (!keys.isEmpty()) {
		Long maxKey = Collections.max(keys);
		OMPDocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(maxKey);
		return docAcknowledgement;
	}
	return null;
}

public OMPDocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
	OMPDocAcknowledgement docAcknowledgement = null;
	Query query = entityManager
			.createNamedQuery("OMPDocAcknowledgement.findByKey");
	query = query.setParameter("ackDocKey", docAckKey);
	try {
		if (null != query.getSingleResult()) {
			docAcknowledgement = (OMPDocAcknowledgement) query
					.getSingleResult();

			entityManager.refresh(docAcknowledgement);
		}
		return docAcknowledgement;
	} catch (Exception e) {
		System.out.println("No elements");
	}

	return null;
}

public OMPDocumentDetails getDocumentDetailsByKey(Long docKey) {
	
	Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByKey");
	query = query.setParameter("key", docKey);
	List<OMPDocumentDetails> docDetailsList = query.getResultList();
	if(null != docDetailsList && !docDetailsList.isEmpty()) {
		entityManager.refresh(docDetailsList.get(0));
		return docDetailsList.get(0);
	}
	return null;
}

public OMPDocumentDetails getDocumentDetailsBytoken(Long documentToken) {
	
	Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByDocToken");
	query = query.setParameter("documentToken", documentToken);
	List<OMPDocumentDetails> docDetailsList = query.getResultList();
	if(null != docDetailsList && !docDetailsList.isEmpty()) {
		entityManager.refresh(docDetailsList.get(0));
		return docDetailsList.get(0);
	}
	return null;
}

@SuppressWarnings("unchecked")
public List<OMPBenefitCover> getOMPBenefitCoverByKey(Long rodKey) {
	Query query = entityManager.createNamedQuery("OMPBenefitCover.findByRodKey")
			.setParameter("rodKey", rodKey);
	List<OMPBenefitCover> rodList = query.getResultList();

	if (rodList != null && !rodList.isEmpty()) {
		for (OMPBenefitCover reimbursement : rodList) {
			entityManager.refresh(reimbursement);
		}
		return rodList;
	}
	return null;
}

public MastersEvents getMasterEventValueBykey(Long key) {
	
	MastersEvents a_mastersValue = new MastersEvents();
	
		Query query = entityManager
				.createNamedQuery("MastersEvents.findByKey");
		query = query.setParameter("primaryKey", key);
		List<MastersEvents> mastersValueList = query.getResultList();
			
		MastersEvents mastersEvents = mastersValueList.get(0);
		return mastersEvents;
}


public void setSaveNegotiation(OMPClaimCalculationViewTableDTO calculationViewTableDTO, OMPClaim claim ,
		OMPReimbursement ompReimbursement) {
	List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = calculationViewTableDTO.getNegotiationDetailsDTOs();
	if(negotiationDetailsDTOs!= null && !negotiationDetailsDTOs.isEmpty()){
		for (OMPNegotiationDetailsDTO ompNegotiationDetailsDTO : negotiationDetailsDTOs) {
			OMPNegotiation negotiation =null;
			if(ompNegotiationDetailsDTO.getKey()==null ){
				 negotiation = new OMPNegotiation();
			}else{
				 negotiation = getOMPNegotiationbyKey(ompNegotiationDetailsDTO.getKey());
			}	
				negotiation.setRodKey(ompReimbursement);
				negotiation.setNegotiatorName(ompNegotiationDetailsDTO.getNameOfNegotiatior());
				negotiation.setClaim(claim);
				negotiation.setApprovedAmount(ompNegotiationDetailsDTO.getApprovedAmt());
				negotiation.setAggredAmount(ompNegotiationDetailsDTO.getAgreedAmount());
				negotiation.setNegotiationCompletedDate(ompNegotiationDetailsDTO.getNegotiationCompletDate());
				negotiation.setNegotiationRemarks(ompNegotiationDetailsDTO.getNegotiationRemarks());
				negotiation.setNegotiationRequestedDate(ompNegotiationDetailsDTO.getNegotiationReqstDate());
				negotiation.setIntimation(claim.getIntimation());
				negotiation.setExpenseAmountusd(ompNegotiationDetailsDTO.getExpenseAmt());
				negotiation.setDiffAmountusd(ompNegotiationDetailsDTO.getDiffAmt());
				negotiation.setHandlingChargsUsd(ompNegotiationDetailsDTO.getHandlingCharges());
				if(ompNegotiationDetailsDTO.getAgreedAmount()!=null){
					if(calculationViewTableDTO!= null){
						calculationViewTableDTO.setAfternegotiation(ompNegotiationDetailsDTO.getAgreedAmount());
					}
				}
				ompNegotiationDetailsDTO.setKey(negotiation.getKey());
				if(negotiation.getKey()!=null){
						entityManager.merge(negotiation);
				}else{
					entityManager.persist(negotiation);
				}
		}
	}/*else{
		//CR2019041
		OMPNegotiation negotiation =null;
		if(calculationViewTableDTO.getSelect().getId() !=null){
			 negotiation = getOMPNegotiationbyKey(calculationViewTableDTO.getSelect().getId());
			 
				if(calculationViewTableDTO.getHandlingCharges()!=null){
					negotiation.setHandlingChargsUsd(calculationViewTableDTO.getHandlingCharges());
				}
				if(calculationViewTableDTO.getExpenses()!=null){
					negotiation.setExpenseAmountusd(calculationViewTableDTO.getExpenses());
				}
				if(negotiation.getKey()!=null){
					entityManager.merge(negotiation);
				}else{
					entityManager.persist(negotiation);
				}
		}
	}*/
	//CR2019041
}


public List<OMPNegotiation> getOMPNegotiation(Long rodKey) {
	Query query = entityManager.createNamedQuery("OMPNegotiation.findByRodKey")
			.setParameter("rodKey", rodKey);
	List<OMPNegotiation> rodList = query.getResultList();

	if (rodList != null && !rodList.isEmpty()) {
		for (OMPNegotiation reimbursement : rodList) {
			entityManager.refresh(reimbursement);
		}
		return rodList;
	}
	return null;
}

public List<OMPNegotiation> getOMPNegotiationbyClaimKey(Long claimKey) {
	Query query = entityManager.createNamedQuery("OMPNegotiation.findByClaimKey")
			.setParameter("claimKey", claimKey);
	List<OMPNegotiation> claimList = query.getResultList();

	if (claimList != null && !claimList.isEmpty()) {
		for (OMPNegotiation reimbursement : claimList) {
			entityManager.refresh(reimbursement);
		}
		return claimList;
	}
	return null;
}

public OMPNegotiation getOMPNegotiationbyKey(Long key) {
	Query query = entityManager.createNamedQuery("OMPNegotiation.findByOMPNegotiationkey")
			.setParameter("key", key);
	List<OMPNegotiation> rodList = query.getResultList();

	if (rodList != null && !rodList.isEmpty()) {
		
		return rodList.get(0);
	}
	return null;
}

private void saveAcknowledgeDocRecValues(OMPClaimCalculationViewTableDTO CalculateDTO){

	OMPAckDocReceivedMapper ackDocRecMapper = OMPAckDocReceivedMapper
			.getInstance();
	Boolean isQueryReplyReceived = false;
	Boolean isReconsideration = false;
	Boolean isQueryReplyNo = false;
	Long rodKey = null;
	
	ReceiptOfDocumentsDTO rodDTO = CalculateDTO.getReceiptOfDocumentsDTO();
	/**
	 * Since we dont know the possible status of the ROD, sample values are
	 * set. Later this will be changed.
	 * */
	rodDTO.setStatusKey(ReferenceTable.OMP_ACKNOWLEDGE_STATUS_KEY);
	rodDTO.setStageKey(ReferenceTable.OMP_ACKNOWLEDGE_STAGE_KEY);
	OMPDocAcknowledgement docAck = ackDocRecMapper.getDocAckRecDetails(rodDTO);
	if ((null != rodDTO.getDocumentDetails()
			.getHospitalizationClaimedAmount())
			&& !("").equals(rodDTO.getDocumentDetails()
					.getHospitalizationClaimedAmount())
			&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
					.getHospitalizationClaimedAmount())))
		docAck.setHospitalizationClaimedAmount(Double.parseDouble(rodDTO
				.getDocumentDetails().getHospitalizationClaimedAmount()));
	if(CalculateDTO!=null){
		SelectValue categoryId = CalculateDTO.getCategory();
		MastersValue category = new MastersValue();
		if(categoryId != null){
			category.setKey(categoryId.getId());
			category.setValue(categoryId.getValue());						
			docAck.setCategoryId(category);
		}
		SelectValue clasificationId = CalculateDTO.getClassification();
		MastersValue clasification = new MastersValue();
		if(clasificationId != null){
			clasification.setKey(clasificationId.getId());
			clasification.setValue(clasificationId.getValue());						
			docAck.setClassificationId(clasification);
		}
		
		SelectValue subclasificationId = CalculateDTO.getSubClassification();
		MastersValue subclasification = new MastersValue();
		if(subclasificationId != null){
			subclasification.setKey(subclasificationId.getId());
			subclasification.setValue(subclasificationId.getValue());						
			docAck.setSubClassificationId(subclasification);
		}
		
		SelectValue docReceivedId = CalculateDTO.getDocRecivedFrm();
		MastersValue docReceived = new MastersValue();
		if(categoryId != null){
			docReceived.setKey(docReceivedId.getId());
			docReceived.setValue(docReceivedId.getValue());						
			docAck.setDocumentReceivedFromId(docReceived);
		}
		SelectValue modeofReceiptId = CalculateDTO.getModeOfReceipt();
		MastersValue modeofReceipt = new MastersValue();
		if(categoryId != null){
			modeofReceipt.setKey(modeofReceiptId.getId());
			modeofReceipt.setValue(modeofReceiptId.getValue());						
			docAck.setModeOfReceiptId(modeofReceipt);
		}
		docAck.setDocumentReceivedDate(CalculateDTO.getAckDocReceivedDate());
		docAck.setInsuredContactNumber(CalculateDTO.getAckContactNumber());
		docAck.setInsuredEmailId(CalculateDTO.getEmailId());
	}
//	if ((null != rodDTO.getDocumentDetails()
//			.getPreHospitalizationClaimedAmount())
//			&& !("").equals(rodDTO.getDocumentDetails()
//					.getPreHospitalizationClaimedAmount())
//			&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
//					.getPreHospitalizationClaimedAmount())))
//		docAck.setPreHospitalizationClaimedAmount(Double.parseDouble(rodDTO
//				.getDocumentDetails().getPreHospitalizationClaimedAmount()));
//	if ((null != rodDTO.getDocumentDetails()
//			.getPostHospitalizationClaimedAmount())
//			&& !("").equals(rodDTO.getDocumentDetails()
//					.getPostHospitalizationClaimedAmount())
//			&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
//					.getPostHospitalizationClaimedAmount())))
//		docAck.setPostHospitalizationClaimedAmount(Double
//				.parseDouble(rodDTO.getDocumentDetails()
//						.getPostHospitalizationClaimedAmount()));

	if (null != rodDTO.getReconsiderRODdto()
			&& null != rodDTO.getReconsiderRODdto().getRodKey()) {/*

		ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO
				.getReconsiderRODdto();

		docAck.setRodKey(reconsiderDTO.getRodKey());
		rodKey = reconsiderDTO.getRodKey();
		
		docAck.setHospitalisationFlag(reconsiderDTO
				.getHospitalizationFlag());
//		docAck.setPreHospitalisationFlag(reconsiderDTO
//				.getPreHospitalizationFlag());
//		docAck.setPostHospitalisationFlag(reconsiderDTO
//				.getPostHospitalizationFlag());
//		docAck.setPartialHospitalisationFlag(reconsiderDTO
//				.getPartialHospitalizationFlag());
//		docAck.setLumpsumAmountFlag(reconsiderDTO.getLumpSumAmountFlag());
//		docAck.setHospitalCashFlag(reconsiderDTO
//				.getAddOnBenefitsHospitalCashFlag());
//		docAck.setPatientCareFlag(reconsiderDTO
//				.getAddOnBenefitsPatientCareFlag());
//		docAck.setHospitalizationRepeatFlag(reconsiderDTO
//				.getHospitalizationRepeatFlag());
		
		docAck.setReconsideredDate((new Timestamp(System
				.currentTimeMillis())));

		if (null != rodDTO.getDocumentDetails()
				.getReasonForReconsideration()) {
			MastersValue masValue = new MastersValue();
			masValue.setKey(rodDTO.getDocumentDetails()
					.getReasonForReconsideration().getId());
			masValue.setValue(rodDTO.getDocumentDetails()
					.getReasonForReconsideration().getValue());
			docAck.setReconsiderationReasonId(masValue);
		}
		if (null != rodDTO.getDocumentDetails()
				.getPaymentCancellationNeededFlag()) {
			docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails()
					.getPaymentCancellationNeededFlag());
		}
		SelectValue reasonForReconsideration = rodDTO.getDocumentDetails()
				.getReasonForReconsideration();
		if (null != rodDTO.getDocumentDetails()
				.getReasonForReconsideration()) {
			MastersValue masReasonForReconsideration = new MastersValue();
			masReasonForReconsideration.setKey(reasonForReconsideration
					.getId());
			masReasonForReconsideration.setValue(reasonForReconsideration
					.getValue());
			docAck.setReconsiderationReasonId(masReasonForReconsideration);
		}

		isReconsideration = true;

	*/}
	
	else if (null != rodDTO.getRodqueryDTO()
			&& rodDTO.getRodqueryDTO().getReimbursementKey() != null) {/*

		isQueryReplyReceived = true;
		RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
		rodKey = rodQueryDetailsDTO.getReimbursementKey();
		docAck.setRodKey(rodKey);
		docAck.setHospitalisationFlag(rodQueryDetailsDTO
				.getHospitalizationFlag());
		
//		docAck.setPreHospitalisationFlag(rodQueryDetailsDTO
//				.getPreHospitalizationFlag());
//		docAck.setPostHospitalisationFlag(rodQueryDetailsDTO
//				.getPostHospitalizationFlag());
//		docAck.setPartialHospitalisationFlag(rodQueryDetailsDTO
//				.getPartialHospitalizationFlag());
//		docAck.setLumpsumAmountFlag(rodQueryDetailsDTO
//				.getAddOnBenefitsLumpsumFlag());
//		docAck.setHospitalCashFlag(rodQueryDetailsDTO
//				.getAddOnBeneftisHospitalCashFlag());
//		docAck.setPatientCareFlag(rodQueryDetailsDTO
//				.getAddOnBenefitsPatientCareFlag());
//		docAck.setHospitalizationRepeatFlag(rodQueryDetailsDTO
//				.getHospitalizationRepeatFlag());

	*/}

	docAck.setActiveStatus(1l);
	docAck.setClaim(searchByClaimKey(rodDTO.getClaimDTO().getKey()));
	docAck.setAcknowledgeNumber(rodDTO.getDocumentDetails()
			.getAcknowledgementNumber());
	String strUserName = rodDTO.getStrUserName();
	String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
	String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
	docAck.setCreatedBy(userId);

	/**
	 * As per DB Team(Prakash) suggestion, Updating claim object before
	 * acknowledgement creation, since the status updated by the trigger is
	 * getting overridden by the application in claim table.
	 * 
	 * **/

	OMPClaim claimObj = docAck.getClaim();
	if (null != claimObj) {
		Status status = new Status();
		status.setKey(ReferenceTable.OMP_ACKNOWLEDGE_STATUS_KEY);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.OMP_ACKNOWLEDGE_STAGE_KEY);
		claimObj.setStage(stage);
		claimObj.setStatus(status);
		claimObj.setModifiedBy(userId);
		claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(claimObj);
		entityManager.flush();
		log.info("------Claim------>" + claimObj + "<------------");
//
//

		/***
		 * Need  To clarify regardin updation  of   Provision  for OMP Claims
		 * 
		 * 
		 */
		
//		if (!((null != docAck && null != docAck.getReconsiderationRequest() && ("Y")
//				.equalsIgnoreCase(docAck.getReconsiderationRequest())) || isQueryReplyReceived)) {
//			if (null != docAck
//					&& null != docAck.getClaim()
//					&& null != docAck.getClaim().getClaimType()
//					&& docAck
//							.getClaim()
//							.getClaimType()
//							.getKey()
//							.equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
//					&& null != docAck.getHospitalisationFlag()
//					&& (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck
//							.getHospitalisationFlag())) {
//				// claimObj.set
//				updateProvisionAndClaimStatus(docAck, claimObj, false,
//						rodDTO);
//			} else if (rodDTO.getIsConversionAllowed() != null
//					&& rodDTO.getIsConversionAllowed()) {
//				if (claimObj.getStatus().equals(
//						ReferenceTable.INTIMATION_REGISTERED_STATUS)) {
//					updateProvisionAndClaimStatus(docAck, claimObj, false,
//							rodDTO);
//				}
//
//				/*
//				 * Status claimStatus = new Status();
//				 * claimStatus.setKey(ReferenceTable
//				 * .CLAIM_REGISTERED_STATUS);
//				 * claimObj.setStatus(claimStatus);
//				 */
//
//				/**
//				 * For those cases where ack is created for claim which got
//				 * converted via, convert to reimbursement process, claim
//				 * registered date should not be updated. Hence the below
//				 * condition of null check is added.
//				 * */
//				/**
//				 * 
//				 * Only if the claim is not registered, then the
//				 * registration date would be null and hence we update the
//				 * status and date. If this is not null, then claim is
//				 * already registered and this is a conversion case.
//				 * */
//
//				if (null == claimObj.getClaimRegisteredDate()) {
//					claimObj.setClaimRegisteredDate((new Timestamp(System
//							.currentTimeMillis())));
//
//					Status claimStatus = new Status();
//					claimStatus
//							.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
//					// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE
//					// SECTION VALUES IN CLAIM LEVEL...
//					SectionDetailsTableDTO sectionDetailsDTO = rodDTO
//							.getSectionDetailsDTO();
//					if (sectionDetailsDTO != null) {
//						claimObj.setClaimSectionCode(sectionDetailsDTO
//								.getSection() != null ? sectionDetailsDTO
//								.getSection().getCommonValue() : null);
//						claimObj.setClaimCoverCode(sectionDetailsDTO
//								.getCover() != null ? sectionDetailsDTO
//								.getCover().getCommonValue() : null);
//						claimObj.setClaimSubCoverCode(sectionDetailsDTO
//								.getSubCover() != null ? sectionDetailsDTO
//								.getSubCover().getCommonValue() : null);
//					}
//					claimObj.setStatus(claimStatus);
//					/*
//					 * Modified by user updation code is commented, since
//					 * modified user id and created user id would be diff in
//					 * this scenario. This is not required. Hence commenting
//					 * based on prakash inputs. *
//					 */
//					/*
//					 * if(rodDTO != null){
//					 * claimObj.setModifiedBy(rodDTO.getStrUserName()); }
//					 */
//					claimObj.setModifiedDate(new Timestamp(System
//							.currentTimeMillis()));
//				}
//
//				/*
//				 * if(null != rodDTO) { if(null != rodDTO)
//				 * claimObj.setModifiedBy(rodDTO.getStrUserName());
//				 * claimObj.setModifiedDate(new Timestamp(System
//				 * .currentTimeMillis())); }
//				 */
//
//				SectionDetailsTableDTO sectionDetailsDTO = rodDTO
//						.getSectionDetailsDTO();
//				if (sectionDetailsDTO != null) {
//					claimObj.setClaimSectionCode(sectionDetailsDTO
//							.getSection() != null ? sectionDetailsDTO
//							.getSection().getCommonValue() : null);
//					claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
//							.getCover().getCommonValue() : null);
//					claimObj.setClaimSubCoverCode(sectionDetailsDTO
//							.getSubCover() != null ? sectionDetailsDTO
//							.getSubCover().getCommonValue() : null);
//				}
//				entityManager.merge(claimObj);
//				entityManager.flush();
//				log.info("------Claim------>" + claimObj + "<------------");
//
//			}
//			/**
//			 * Added for lumpsum change.
//			 * */
//			else if (("N")
//					.equalsIgnoreCase(docAck.getHospitalisationFlag())
//					&& ("N").equalsIgnoreCase(docAck
//							.getPartialHospitalisationFlag())
//					&& ("N").equalsIgnoreCase(docAck
//							.getPreHospitalisationFlag())
//					&& ("N").equalsIgnoreCase(docAck
//							.getPostHospitalisationFlag())
//					&& ("N").equalsIgnoreCase(docAck.getHospitalCashFlag())
//					&& ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
//					&& ("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag())) {
////				updateProvisionAndClaimStatus(docAck, claimObj, true,
////						rodDTO);
//			}
//
//		}

		/**
		 * As per sathish sir suggestion, in all cases where claim
		 * registered date is null, then only claim registered date will be
		 * updated. Else it won't get updated.
		 * 
		 * */

		if (null == claimObj.getClaimRegisteredDate()) {/*
			claimObj.setClaimRegisteredDate((new Timestamp(System
					.currentTimeMillis())));
			Status claimStatus = new Status();
			claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimObj.setStatus(claimStatus);
			claimObj.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));

			SectionDetailsTableDTO sectionDetailsDTO = rodDTO
					.getSectionDetailsDTO();
			if (sectionDetailsDTO != null) {
				claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO
						.getSection().getCommonValue() : null);
				claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
						.getCover().getCommonValue() : null);
				claimObj.setClaimSubCoverCode(sectionDetailsDTO
						.getSubCover() != null ? sectionDetailsDTO
						.getSubCover().getCommonValue() : null);
			}

			entityManager.merge(claimObj);
			entityManager.flush();
		*/}

	}

	entityManager.persist(docAck);
	entityManager.flush();
	log.info("------DocAcknowledgement------>"
			+ docAck.getAcknowledgeNumber() + "<------------");
	entityManager.refresh(docAck);
 
	saveUploadAckDocuments(CalculateDTO,docAck,CalculateDTO.getClaimProcessorDTO());
	
	/**
	 * If reconsideration request is selected, then the current
	 * acknowledgement needs to be updated in reimbursement table.
	 * */
	if (null != rodKey) {/*

		OMPReimbursement reimbursement = getReimbursement(rodKey);

		String userName = rodDTO.getStrUserName();
		userName = SHAUtils.getUserNameForDB(userName);
		reimbursement.setModifiedBy(userName);
		reimbursement.setDocAcknowLedgement(docAck);
		reimbursement.setModifiedDate(new Timestamp(System
				.currentTimeMillis()));
		entityManager.merge(reimbursement);
		entityManager.flush();
		log.info("------Reimbursement------>" + reimbursement
				+ "<------------");
	*/}
	
	Boolean isQueryStatusYes = false;
	
	/***
	 * Need  to clarify  regarding query initiate table for reimb. query table.
	 */

//	if (isQueryReplyReceived) {
//
//		List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO
//				.getRodQueryDetailsList();
//		for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
//			ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2
//					.getReimbursementQueryKey());
//			if (("No").equalsIgnoreCase(rodQueryDetailsDTO2
//					.getReplyStatus())) {
//				reimbQuery.setQueryReply("N");
//			} else if (("Yes").equalsIgnoreCase(rodQueryDetailsDTO2
//					.getReplyStatus())) {
//				reimbQuery.setQueryReply("Y");
//				reimbQuery.setQueryReplyDate(new Timestamp(System
//						.currentTimeMillis()));
//				rodDTO.setCreatedByForQuery(reimbQuery.getCreatedBy());
//				isQueryStatusYes = true;
//			}
//			reimbQuery.setDocAcknowledgement(docAck);
//			if (null != reimbQuery.getKey()) {
//				if (null != rodDTO) {
//					reimbQuery.setModifiedBy(rodDTO.getStrUserName());
//					reimbQuery.setModifiedDate(new Timestamp(System
//							.currentTimeMillis()));
//				}
//				entityManager.merge(reimbQuery);
//				entityManager.flush();
//				log.info("------ReimbursementQuery------>" + reimbQuery
//						+ "<------------");
//			}
//		}
//	}

	List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
			.getDocumentCheckList();
	if (docCheckList!=null &&!docCheckList.isEmpty()) {
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
				OMPRODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
						.getRODDocumentCheckList(docCheckListDTO);
				rodDocumentCheckList.setDocAcknowledgement(docAck);
				// findRODDocumentCheckListByKey(masterService);
				entityManager.persist(rodDocumentCheckList);
				entityManager.flush();
				log.info("------RODDocumentCheckList------>"
						+ rodDocumentCheckList + "<------------");
				// }
			}
		}
	}
	
//	if (null != rodDTO.getDocFilePath()
//			&& !("").equalsIgnoreCase(rodDTO.getDocFilePath())) {
//		HashMap dataMap = new HashMap();
//		dataMap.put("intimationNumber", docAck.getClaim().getIntimation()
//				.getIntimationId());
//		Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
//		if (null != objClaim) {
//			dataMap.put("claimNumber", objClaim.getClaimId());
//			if (null != objClaim.getClaimType()) {
//				if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
//						.equals(objClaim.getClaimType().getKey())) {
//					Preauth preauth = getPreauthClaimKey(objClaim.getKey());
//					if (null != preauth)
//						dataMap.put("cashlessNumber",
//								preauth.getPreauthId());
//				}
//			}
//		}
//		dataMap.put("filePath", rodDTO.getDocFilePath());
//		dataMap.put("docType", rodDTO.getDocType());
//		dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
//		dataMap.put("createdBy", rodDTO.getStrUserName());
//		SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
//	}
	
}

public OMPBenefitCover getOMPBenefitCoverByBenefitKey(Long key) {
	Query query = entityManager.createNamedQuery("OMPBenefitCover.findByRodKey");
	query.setParameter("rodKey", key);
	List<OMPBenefitCover> benefitList = (List<OMPBenefitCover>)query.getResultList();
	
	if(benefitList != null && ! benefitList.isEmpty()){
		entityManager.refresh(benefitList.get(0));
		return benefitList.get(0);
	}
	else{
		return null;
	}
}

public Currency getOMPCurrency(Long key) {
	Query query = entityManager.createNamedQuery("Currency.findByKey");
	query.setParameter("key", key);
	List<Currency> benefitList = (List<Currency>)query.getResultList();
	
	if(benefitList != null && ! benefitList.isEmpty()){
		entityManager.refresh(benefitList.get(0));
		return benefitList.get(0);
	}
	else{
		return null;
	}
}


public List<OMPClaimRateChange> getClaimRateHistory(Long rodkey) {
	Query query = entityManager.createNamedQuery("OMPClaimRateChange.findByrodKey");
	query.setParameter("rodKey", rodkey);
	List<OMPClaimRateChange> resultList = (List<OMPClaimRateChange>) query.getResultList();
	
	for (OMPClaimRateChange claimRate : resultList) {
		entityManager.refresh(claimRate);
	}
	return   resultList;
}

public List<DMSDocumentDetailsDTO> getRODDocumentList(String intimationNo ,String claimNo)
{


	if(null != intimationNo && claimNo!=null)
	{
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByIntimationNoOrderByCreatedDate");
		query = query.setParameter("intimationNumber", intimationNo);
		List<OMPDocumentDetails> rodDocList = query.getResultList();
		List<DMSDocumentDetailsDTO> dtoList = null;
		if(null != rodDocList && !rodDocList.isEmpty())
		{
			dtoList = new ArrayList<DMSDocumentDetailsDTO>();
			entityManager.refresh(rodDocList.get(0));
			for (OMPDocumentDetails rodDocument : rodDocList) {
			
				DMSDocumentDetailsDTO detailsDTO = new DMSDocumentDetailsDTO();
				detailsDTO.setDmsRestApiURL(dmsAPIUrl);	
				detailsDTO.setIntimationNo(intimationNo);
				detailsDTO.setClaimNo(claimNo);
				detailsDTO.setDocumentType(SHAConstants.SEARCH_UPLOAD_DOC_TYPE);
				detailsDTO.setFileName(rodDocument.getFileName());		
				detailsDTO.setDocumentSource(SHAConstants.SEARCH_UPLOAD_DOC_SOURCE);
				if(rodDocument.getDocumentToken()!=null){
					detailsDTO.setDmsDocToken(rodDocument.getDocumentToken().toString());
				}
				detailsDTO.setGalaxyFileName(rodDocument.getFileName());
				detailsDTO.setDocumentCreatedDate(rodDocument.getCreatedDate());
				dtoList.add(detailsDTO);
				
			}
			return dtoList;
		}
	}
	return null;
}


public List<DMSDocumentDetailsDTO> getDocumentDetailsData(String intimationNumber)
{
	//Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNo");
	Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByIntimationNoOrderByCreatedDate");
	query = query.setParameter("intimationNumber", intimationNumber);
	BPMClientContext context = new BPMClientContext();
	String dmsAPIUrl = context.getDMSRestApiUrl();
	List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
	List<OMPDocumentDetails> documentDetailsList  = query.getResultList();
	
	if(documentDetailsList != null && !documentDetailsList.isEmpty()){
		for (OMPDocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		
		documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTOOMP(documentDetailsList);
	}
	
	

	
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
			
			//IMSSUPPOR-29747
			if((!(documentDetails.getDocumentType() != null && documentDetails.getDocumentType().equalsIgnoreCase(SHAConstants.OMP_PAYMENT_LETTER)) && 
					!(documentDetails.getDocumentSource() != null && documentDetails.getDocumentSource().equalsIgnoreCase(SHAConstants.OMP_POST_PROCESS_ACK)))){
			documentDetails.setDocumentType(SHAConstants.ROD_DOC_TYPE);
			documentDetails.setDocumentSource(SHAConstants.ROD_DOC_SOURCE);
			documentDetails.setIntimationNo(intimationNumber);
			}
			
			//IMSSUPPOR-35666
            if(documentDetails.getDocumentType() == null && documentDetails.getDocumentSource() != null 
                            && documentDetails.getDocumentSource().equalsIgnoreCase(SHAConstants.OMP_POST_PROCESS_ACK)){
                    documentDetails.setDocumentType(SHAConstants.OMP_POST_PROCESS_ACK);
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

private String generateRODNumber(Long claimKey, String intimationNumber) {
	Long count = ompClaimService.getACknowledgeNumberCountByClaimKey(claimKey);
	StringBuffer ackNoBuf = new StringBuffer();
	Long lackCount = count + 001;
	ackNoBuf.append("ROD/")
			.append(intimationNumber).append("/").append(lackCount);
	return ackNoBuf.toString();
	
}


public void saveOrUpdateClaim(OMPClaimProcessorDTO ompClaimProcessorDTO,
		OMPClaim claim) {
	claim.setDataOfAdmission(ompClaimProcessorDTO.getAdmissionDate());
	claim.setDataOfDischarge(ompClaimProcessorDTO.getDischargeDate());
	claim.setAilmentLoss(ompClaimProcessorDTO.getAilmentLoss());
	claim.setPlaceOfAccident(ompClaimProcessorDTO.getPlaceEvent());
	claim.setPlaveOfVisit(ompClaimProcessorDTO.getPlaceOfVisit());
	claim.setHospitalName(ompClaimProcessorDTO.getHospName());
	if(ompClaimProcessorDTO.getHospital()!=null){
		claim.setHospital(ompClaimProcessorDTO.getHospital().getId());
	}
	claim.setCityName(ompClaimProcessorDTO.getHospCity());
	if(ompClaimProcessorDTO.getHospCountry()!= null){
		SelectValue countryValueByKey = masterService.getCountryValueByKey(ompClaimProcessorDTO.getHospCountry().getId());
		if(countryValueByKey!= null&& countryValueByKey.getId()!= null){
			claim.setCountryId(countryValueByKey.getId());
		}
	}
	claim.setLossDateTime(ompClaimProcessorDTO.getLossOfDate());
	claim.setLossTime(ompClaimProcessorDTO.getLossTime());
	claim.setPlaveOfLossOrDelay(ompClaimProcessorDTO.getLossOrDelay());
	claim.setLossDetails(ompClaimProcessorDTO.getLossDetails());
	if(ompClaimProcessorDTO.getIsLegalFlag()!= null && ompClaimProcessorDTO.getIsLegalFlag().equals(Boolean.TRUE)){
		claim.setLegalOpinionFlag("Y");
	}else{
		claim.setLegalOpinionFlag("N");
	}
	claim.setInrConversionRate(ompClaimProcessorDTO.getInrConversionRate());
	claim.setInrTotalAmount(ompClaimProcessorDTO.getInrtotal());
	claim.setDollarInitProvisionAmount(ompClaimProcessorDTO.getProvisionAmt());
	SelectValue eventValue = ompClaimProcessorDTO.getEventCode();
	MastersEvents events = null;
	
	if(eventValue != null && eventValue.getValue() != null){
		MastersEvents masterEventValueBykey = getMasterEventValueBykey(eventValue.getId());						
		claim.setEvent(masterEventValueBykey);
		}
	
	if(ompClaimProcessorDTO.getClaimType()!= null && ompClaimProcessorDTO.getClaimType().equals(Boolean.TRUE)){
		MastersValue master = getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		SelectValue claimType = new SelectValue();
		claimType.setId(master.getKey());
		claimType.setValue(master.getValue());
		claim.setClaimType(master);
	}
	if(ompClaimProcessorDTO.getClaimType()!= null && ompClaimProcessorDTO.getClaimType().equals(Boolean.FALSE)){
		MastersValue master = getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		SelectValue claimType = new SelectValue();
		claimType.setId(master.getKey());
		claimType.setValue(master.getValue());
		claim.setClaimType(master);
	}
	if(ompClaimProcessorDTO.getNonHospitalisationFlag()!= null){
		claim.setNonHospitalisationFlag(ompClaimProcessorDTO.getNonHospitalisationFlag());
	}
	
	if(ompClaimProcessorDTO.getIsLegalFlag()!=null && ompClaimProcessorDTO.getIsLegalFlag()){
		claim.setLegalOpinionFlag("Y");
	}
}

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void insertOrUpdate(OMPClaim claim) {
		if(claim != null){
	if(claim.getKey() == null){
		entityManager.persist(claim);
		
	}
	else if(claim != null && claim.getKey() != null ){
		claim.setModifiedDate(new Date());
		entityManager.merge(claim);
	}
	entityManager.flush();
	//entityManager.refresh(reimbursement);
}	
	
}


public void saveClosedClaim(OMPCloseReopenClaim closeReopenClaim, OMPClaim claim, OMPClaimProcessorDTO ompClaimProcessorDTO) {
	if(closeReopenClaim == null){
		closeReopenClaim = new OMPCloseReopenClaim();
		closeReopenClaim.setCreatedBy(ompClaimProcessorDTO.getUserName());
		closeReopenClaim.setClaim(claim);
		closeReopenClaim.setClaimNumber(claim.getClaimId());
		closeReopenClaim.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		
		Stage stageObj = masterService.getStageBykey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		closeReopenClaim.setStage(stageObj);
		Status statusObj = null;
		/*if(ompClaimProcessorDTO.getRemarksForClose()!=null){
			closeReopenClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
		}
		if(ompClaimProcessorDTO.getRemarksForReopn()!= null && ompClaimProcessorDTO.getRemarksForReopn().getValue()!= null){
			closeReopenClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
		}
		if(ompClaimProcessorDTO.getRemarksForReopn()!= null && ompClaimProcessorDTO.getRemarksForReopn().getValue()!= null){
			
		}*/
		if(ompClaimProcessorDTO.getStatusKey()!=null && ompClaimProcessorDTO.getStatusKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
			statusObj = masterService.getStatusByKey(ompClaimProcessorDTO.getStatusKey());
			closeReopenClaim.setStatus(statusObj);
			closeReopenClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
			closeReopenClaim.setClosingRemarks(ompClaimProcessorDTO.getRemarksForClose());
		}
		if(ompClaimProcessorDTO.getStatusKey()!= null && ompClaimProcessorDTO.getStatusKey().equals(ReferenceTable.CLAIM_REOPENED_STATUS)){
			statusObj = masterService.getStatusByKey(ompClaimProcessorDTO.getStatusKey());
			closeReopenClaim.setStatus(statusObj);
			closeReopenClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
		
			SelectValue reopnValue = ompClaimProcessorDTO.getRemarksForReopn();
			MastersValue reopenMasetr = new MastersValue();
		if(reopnValue!= null && reopnValue.getValue()!= null){
			reopenMasetr.setKey(reopnValue.getId());
			reopenMasetr.setValue(reopnValue.getValue());
			closeReopenClaim.setReOpenReasonId(reopenMasetr);
			closeReopenClaim.setReOpenRemarks(reopenMasetr.getValue());
			}
		}
		insertOrUpdate(closeReopenClaim,ompClaimProcessorDTO);
	}
}
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void insertOrUpdate(OMPCloseReopenClaim closeReopenClaim,OMPClaimProcessorDTO ompClaimProcessorDTO) {
		if(closeReopenClaim != null){
	if(closeReopenClaim.getKey() == null){
		entityManager.persist(closeReopenClaim);
		
	}
	else if(closeReopenClaim != null && closeReopenClaim.getKey() != null ){
		closeReopenClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		closeReopenClaim.setModifiedBy(ompClaimProcessorDTO.getUserName());
		entityManager.merge(closeReopenClaim);
	}
	entityManager.flush();
	//entityManager.refresh(reimbursement);
}	
	
}

public OMPCloseReopenClaim getCloseClaim(Long claimKey){
	
	Query query = entityManager.createNamedQuery("OMPCloseReopenClaim.getByCloseClaimKey");
	query.setParameter("claimKey", claimKey);
	
	
	List<OMPCloseReopenClaim> closeClaimList = (List<OMPCloseReopenClaim>)query.getResultList();
	if(closeClaimList != null && ! closeClaimList.isEmpty()){
		return closeClaimList.get(0);
	}
	
	return null;
	
}

public List<OMPCurrencyHistory> getCurrencyRateHistory(Long rodkey) {
	Query query = entityManager.createNamedQuery("OMPCurrencyHistory.findByrodKey");
	query.setParameter("rodKey", rodkey);
	List<OMPCurrencyHistory> resultList = (List<OMPCurrencyHistory>) query.getResultList();
	
	for (OMPCurrencyHistory claimRate : resultList) {
		entityManager.refresh(claimRate);
	}
	return   resultList;
}
	

public void saveReimbursementBilldetails(OMPClaimProcessorDTO ompClaimProcessorDTO, OMPClaim claim,List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable, String screenName) {
	OMPReimbursement reimbursement =null;
	for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : claimCalculationViewTable) {
		if(ompClaimCalculationViewTableDTO.getDeleted().equalsIgnoreCase("N")){
		 String rodNo = ompClaimCalculationViewTableDTO.getRodnumber();
		 Long rodKey = ompClaimProcessorDTO.getRodKey();
		 if(rodNo!=null){
			 reimbursement = getReimbursementByRodNo(rodNo);
		 }else{
			 reimbursement= new OMPReimbursement();
			 if(ompClaimProcessorDTO.getRodNumber()==null){
					String generateRODNumber = generateRODNumber(claim.getKey(), claim.getIntimation().getIntimationId());
					if(generateRODNumber!=null&& reimbursement.getRodNumber()== null){
						ompClaimProcessorDTO.setRodNumber(generateRODNumber);
						reimbursement.setRodNumber(generateRODNumber);
					}
				}
		 }
		 if(ompClaimCalculationViewTableDTO.getDeleted()!=null && ompClaimCalculationViewTableDTO.getDeleted().equals("Y")){
			 reimbursement.setActiveStatus(0l);
			 reimbursement.setCurrentProvisionAmt(0d);
			 reimbursement.setBenApprAmt(0d);
		 }
//					 reimbursement = rodBillentryService.getReimbursementByKey(rodKey);
			 SelectValue classifType = ompClaimCalculationViewTableDTO.getClassification();
				MastersValue classifTypeMast = null;
					if(classifType != null && classifType.getId() != null){
						classifTypeMast = new MastersValue();
					classifTypeMast.setKey(classifType.getId());
					classifTypeMast.setValue(classifType.getValue());						
					reimbursement.setClassificationId(classifTypeMast);
				}
//							reimbursement.setSubClassificationId(subClassificationId);
					SelectValue subclassifType = ompClaimCalculationViewTableDTO.getSubClassification();
					MastersValue subclassifTypeMast = null;
					if(subclassifType != null && subclassifType.getId() != null){
						subclassifTypeMast = new MastersValue();
						subclassifTypeMast.setKey(subclassifType.getId());
						subclassifTypeMast.setValue(subclassifType.getValue());						
						reimbursement.setSubClassificationId(subclassifTypeMast);
					}
					
					 SelectValue rodClaimType = ompClaimCalculationViewTableDTO.getRodClaimType();
						MastersValue rodClaimTypeMast = null;
							if(rodClaimType != null && rodClaimType.getId() != null){
								rodClaimTypeMast = new MastersValue();
								rodClaimTypeMast.setKey(rodClaimType.getId());
								rodClaimTypeMast.setValue(rodClaimType.getValue());						
							reimbursement.setRodClaimType(rodClaimTypeMast);
						}
					
					SelectValue docType = ompClaimCalculationViewTableDTO.getDocRecivedFrm();
					MastersValue docTypeMast =null;
					if(docType != null && docType.getId() != null){
						docTypeMast = new MastersValue();
						docTypeMast.setKey(docType.getId());
						docTypeMast.setValue(docType.getValue());						
						reimbursement.setClassiDocumentRecivedFmId(docTypeMast);
					}
					SelectValue category = ompClaimCalculationViewTableDTO.getCategory();
					MastersValue categValue = null;
					if(category!= null && category.getId()!= null){
						categValue = new MastersValue();
						categValue.setKey(category.getId());
						categValue.setValue(category.getValue());
						reimbursement.setCategoryId(categValue);
					}
					
					SelectValue currencyType =ompClaimCalculationViewTableDTO.getCurrencyType();
					if(currencyType!=null){
					Long currencyTypeId = currencyType.getId();
					Currency currency = getOMPCurrency(currencyTypeId);
					if(currency != null){
						reimbursement.setCurrencyTypeId(currency);
					}
				}
					if(ompClaimCalculationViewTableDTO.getConversionValue()!=null){
						reimbursement.setInrConversionRate(ompClaimCalculationViewTableDTO.getConversionValue());
					}
					if(ompClaimCalculationViewTableDTO.getCurrencyrate()!=null){
						reimbursement.setInrTotalAmount(ompClaimCalculationViewTableDTO.getCurrencyrate());
					}
					if(ompClaimCalculationViewTableDTO.getDeduction()!=null){
						reimbursement.setDeductbleUsd(ompClaimCalculationViewTableDTO.getDeduction().longValue());
					}
					
					if(ompClaimCalculationViewTableDTO.getLateDocReceivedDate() != null){
						reimbursement.setLastDocRecDate(ompClaimCalculationViewTableDTO.getLateDocReceivedDate());
					}
					
					if(ompClaimCalculationViewTableDTO.getNegotiationDone()!= null && ompClaimCalculationViewTableDTO.getNegotiationDone().getValue()!= null){
						SelectValue negoDone = ompClaimCalculationViewTableDTO.getNegotiationDone();
						negoDone.setId(ompClaimCalculationViewTableDTO.getNegotiationDone().getId());
						negoDone.setValue(ompClaimCalculationViewTableDTO.getNegotiationDone().getValue());
						if(ompClaimCalculationViewTableDTO.getNegotiationDone().getValue().equalsIgnoreCase("Yes")){
							reimbursement.setNegotiationDone("Y");
						}else{
							reimbursement.setNegotiationDone("N");
						}
					}else{
						reimbursement.setNegotiationDone(null);
					}
					if(ompClaimCalculationViewTableDTO.getViewforApprover()!= null && ompClaimCalculationViewTableDTO.getViewforApprover().getValue()!= null){
						SelectValue forApprov =ompClaimCalculationViewTableDTO.getViewforApprover();
						forApprov.setId(ompClaimCalculationViewTableDTO.getViewforApprover().getId());
						forApprov.setValue(ompClaimCalculationViewTableDTO.getViewforApprover().getValue());
						if(forApprov.getValue().equalsIgnoreCase("Yes")){
							reimbursement.setViewForApprover("Y");
						}else{
							reimbursement.setViewForApprover("N");
						}
					}
					if(!SHAConstants.OMP_APPROVER.equals(screenName)){
						if(ompClaimCalculationViewTableDTO.getSendforApprover()!= null){
							if(ompClaimCalculationViewTableDTO.getSendforApprover().equals("Y")){
								
								reimbursement.setSendForApproverFlg("Y");
							}else{
								reimbursement.setSendForApproverFlg("N");
							}
						}
						
					}
					
					if(ompClaimCalculationViewTableDTO.getReconsiderFlag() != null){
						reimbursement.setReconsiderFlag(ompClaimCalculationViewTableDTO.getReconsiderFlag());
					}
					
					if(ompClaimCalculationViewTableDTO.getReject()!= null){
						if(ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
							reimbursement.setRejectFlg("Y");
						}else{
							reimbursement.setRejectFlg("N");
						}
						reimbursement.setRejectionRemarks(ompClaimCalculationViewTableDTO.getRejectionRemarks());
					}
					
					if(ompClaimCalculationViewTableDTO.getNotinClaimCount()!=null){
						if(ompClaimCalculationViewTableDTO.getNotinClaimCount().equalsIgnoreCase("Y")){
							reimbursement.setNotInClaimCountFlg("Y");
						}else{
							reimbursement.setNotInClaimCountFlg("N");
						}
					}
					if(ompClaimCalculationViewTableDTO.getSelect()!= null && ompClaimCalculationViewTableDTO.getSelect().getId()!= null){
						reimbursement.setNegotiationList(ompClaimCalculationViewTableDTO.getSelect().getId());
					}else{
						reimbursement.setNegotiationList(null);
					}
					if(ompClaimCalculationViewTableDTO.getAfternegotiation()!=null){
						reimbursement.setAmountAfterNego(ompClaimCalculationViewTableDTO.getAfternegotiation());
					}else{
						reimbursement.setAmountAfterNego(null);
					}
					if(ompClaimCalculationViewTableDTO.getConversionValue()!=null){
						reimbursement.setInrConversionRate(ompClaimCalculationViewTableDTO.getConversionValue());
					}
					if(ompClaimCalculationViewTableDTO.getCurrencyrate()!=null){
						reimbursement.setInrTotalAmount(ompClaimCalculationViewTableDTO.getCurrencyrate());
						
					}
					reimbursement.setFinalApprovedAmtInr(ompClaimCalculationViewTableDTO.getFinalApprovedAmtInr());
					reimbursement.setFinalApprovedAmtUsd(ompClaimCalculationViewTableDTO.getFinalApprovedAmtDollor());
					if(reimbursement.getRejectFlg()!=null){
						if(!reimbursement.getRejectFlg().equalsIgnoreCase("Y")){
							//if(ompClaimCalculationViewTableDTO.getSendforApprover()!=null && ompClaimCalculationViewTableDTO.getSendforApprover().equals("Y")){
								reimbursement.setCurrentProvisionAmt(ompClaimCalculationViewTableDTO.getFinalApprovedAmtDollor());
								reimbursement.setBenApprAmt(ompClaimCalculationViewTableDTO.getFinalApprovedAmtDollor());
							//}
						}
					}else{
						reimbursement.setCurrentProvisionAmt(ompClaimCalculationViewTableDTO.getFinalApprovedAmtDollor());
						reimbursement.setBenApprAmt(ompClaimCalculationViewTableDTO.getFinalApprovedAmtDollor());
					}
					if(reimbursement.getRejectFlg()!=null){
						if(reimbursement.getRejectFlg().equalsIgnoreCase("Y")){
							reimbursement.setCurrentProvisionAmt(0d);
							reimbursement.setBenApprAmt(0d);
						}
					}
					reimbursement.setRemarksProcessor(ompClaimCalculationViewTableDTO.getProcessorRemarks());
					if(ompClaimCalculationViewTableDTO.getReasonForRejectionRemarks()!=null){
						if(ompClaimCalculationViewTableDTO.getSendforApprover()!=null && !ompClaimCalculationViewTableDTO.getSendforApprover().equals("Y")){
							MastersValue master = masterService.getMaster(ompClaimCalculationViewTableDTO.getReasonForRejectionRemarks().getId());
							reimbursement.setRejectionCategoryId(master);
						}else if(ompClaimCalculationViewTableDTO.getSendforApprover()==null){
							MastersValue master = masterService.getMaster(ompClaimCalculationViewTableDTO.getReasonForRejectionRemarks().getId());
							reimbursement.setRejectionCategoryId(master);
						}
					}
					reimbursement.setReasonSuggestApprover(ompClaimCalculationViewTableDTO.getReasonForApproval());
					if(SHAConstants.OMP_APPROVER.equals(screenName)){
						reimbursement.setApprovedFlg(ompClaimCalculationViewTableDTO.getSendforApprover());
						reimbursement.setFaSubmitFlg(ompClaimCalculationViewTableDTO.getSubmit());
						if(ompClaimCalculationViewTableDTO.getSubmit()!= null && ompClaimCalculationViewTableDTO.getSubmit().equalsIgnoreCase("Y")){
							if(ompClaimCalculationViewTableDTO.getSendforApprover()!=null && ompClaimCalculationViewTableDTO.getSendforApprover().equalsIgnoreCase("Y")){
								//OMPClaimPayment claimpayment = null;
								OMPClaimPayment claimpayment = getClaimPaymentByRodNumber(reimbursement.getRodNumber());
								saveClaimpayment( ompClaimCalculationViewTableDTO , claimpayment, claim, reimbursement,ompClaimProcessorDTO);
							}
						}
					}
					//rodBillentryService.insertOrUpdate(reimbursement);
					ompClaimProcessorDTO.setRodNumber(ompClaimCalculationViewTableDTO.getRodnumber());
					//reimbursement.setRemarksProcessor(ompClaimProcessorDTO.getProcessorRemarks());
			reimbursement = saveReimbursement(ompClaimProcessorDTO, claim, reimbursement);
			
			List<Long> reimStgSts = new ArrayList<Long>();
			reimStgSts.add(ReferenceTable.OMP_APPROVER_APPROVE_STATUS);
			reimStgSts.add(ReferenceTable.OMP_APPROVER_REJECT_STATUS);
			reimStgSts.add(ReferenceTable.FINANCIAL_SETTLED);
			
			if(SHAConstants.OMP_APPROVER.equals(screenName)){
				if(ompClaimCalculationViewTableDTO!= null && ompClaimCalculationViewTableDTO.getSendforApprover()!= null && ompClaimCalculationViewTableDTO.getSendforApprover().equalsIgnoreCase("Y")){
					if(ompClaimCalculationViewTableDTO.getDisVoucFlag() != null){
						if(ompClaimCalculationViewTableDTO.getDisVoucFlag().equals("Y")){
							if(ompClaimCalculationViewTableDTO.getGenDisVoucherFlag() != null && ompClaimCalculationViewTableDTO.getGenDisVoucherFlag()){
								
								if(ompClaimCalculationViewTableDTO.getProcessorStatus().intValue() == 3221){
									if(ompClaimCalculationViewTableDTO.getDvReceivedDate() != null){
										Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
										Status statusObj = null;
										statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_DISCHARGE_VOUCHER_RECEIVED_STATUS);
										reimbursement.setStage(stgObj);
										reimbursement.setStatus(statusObj);
										reimbursement.setDvReceivedDate(ompClaimCalculationViewTableDTO.getDvReceivedDate());
									}else{
										Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
										Status statusObj = null;
										statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_DISCHARGE_VOUCHER_DISPATCHED_STATUS);
										reimbursement.setStage(stgObj);
										reimbursement.setStatus(statusObj);
									}
								}else{
									if( reimbursement.getStatus()!=null && !reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED)){
										Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
										Status statusObj = null;
										statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_DISCHARGE_VOUCHER_DISPATCHED_STATUS);
										reimbursement.setStage(stgObj);
										reimbursement.setStatus(statusObj);
									}
								}
								
								/*if(rodDTO.getProcessorStatus().intValue() == 3221){
									rodDTO.setDvReceivedDate(dvReceivedDate.getValue());
								}*/
								
							}else{
								Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
								Status statusObj = null;
								statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_APPROVE_STATUS);
								reimbursement.setStage(stgObj);
								reimbursement.setStatus(statusObj);
							}
						}else{
							Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
							Status statusObj = null;
							statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_APPROVE_STATUS);
							reimbursement.setStage(stgObj);
							reimbursement.setStatus(statusObj);
						}
					}else{
						Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
						Status statusObj = null;
						statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_APPROVE_STATUS);
						reimbursement.setStage(stgObj);
						reimbursement.setStatus(statusObj);
					}
					 
					 insertOrUpdate(reimbursement);
//					 reimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, reimbursement);
				 }
				//IMSSUPPOR-32727
				 if((ompClaimCalculationViewTableDTO.getReject()!= null && ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y"))
				 &&(reimbursement!=null && reimbursement.getStatus()!=null && !reimbursement.getStatus().getKey().equals(ReferenceTable.OMP_PROCESSOR_REJECT_STATUS))){
				 //if(ompClaimCalculationViewTableDTO.getReject()!= null && ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
					 Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_APPROVER_STAGE);
					 Status statusObj = null;
					 statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_APPROVER_REJECT_STATUS);
					 reimbursement.setStage(stgObj);
					 reimbursement.setStatus(statusObj);
					 insertOrUpdate(reimbursement);
				 }
				 
			}else{
				if(ompClaimCalculationViewTableDTO!= null && ompClaimCalculationViewTableDTO.getSendforApprover()!= null && ompClaimCalculationViewTableDTO.getSendforApprover().equalsIgnoreCase("Y")){
					 Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_PROCESSOR_STAGE);
					 Status statusObj = null;
					 statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_PROCESSOR_APPROVE_STATUS);
					 //IMSSUPPOR-28921
					 if( reimbursement.getStatus()!=null && !reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED)){
						 
						 if(reimbursement.getStage() != null ){
							 if((reimbursement.getStage().getKey().intValue() != ReferenceTable.OMP_APPROVER_STAGE.intValue() 
									 && (!reimStgSts.contains(statusObj.getKey())))){
								 reimbursement.setStage(stgObj);
								 reimbursement.setStatus(statusObj);
							 }
							 insertOrUpdate(reimbursement);
						 }else{
							 reimbursement.setStage(stgObj);
							 reimbursement.setStatus(statusObj);
							 insertOrUpdate(reimbursement);
						 }
					 }else if(reimbursement.getStatus()==null){
						 reimbursement.setStage(stgObj);
						 reimbursement.setStatus(statusObj);
						 insertOrUpdate(reimbursement);
						 
					 }
//					 reimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, reimbursement);
				 }
				 if(ompClaimCalculationViewTableDTO.getReject()!= null && ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
					 Stage stgObj = masterService.getStageBykey(ReferenceTable.OMP_PROCESSOR_STAGE);
					 Status statusObj = null;
					 statusObj =	masterService.getStatusByKey(ReferenceTable.OMP_PROCESSOR_REJECT_STATUS);
					 if(reimbursement.getStage() != null){
						 if((reimbursement.getStage().getKey().intValue() != ReferenceTable.OMP_APPROVER_STAGE.intValue() 
								 && (!reimStgSts.contains(statusObj.getKey())))){
							 reimbursement.setStage(stgObj);
							 reimbursement.setStatus(statusObj);
						 }
						 insertOrUpdate(reimbursement);
					 }else{
						 reimbursement.setStage(stgObj);
						 reimbursement.setStatus(statusObj);
						 insertOrUpdate(reimbursement);
					 }
				 }
			}
			if(reimbursement.getKey()==null){
				 insertOrUpdate(reimbursement);
			}else{
				ompClaimCalculationViewTableDTO.setRodKey(reimbursement.getKey());
			}
//			Add for rodkey in ack
			OMPDocAcknowledgement acknowledgement =null;
			
			if(ompClaimProcessorDTO.getAckKey()!=null){
				acknowledgement = getAcknowledgementByKey(ompClaimProcessorDTO.getAckKey());
				if(acknowledgement!=null){
					acknowledgement.setModifiedBy(ompClaimProcessorDTO.getUserName());
					acknowledgement.setModifiedDate(new Date());
					ompClaimProcessorDTO.setRodKey(reimbursement.getKey());
					acknowledgement.setRodKey(reimbursement.getKey());
					 SelectValue ackclassifType = ompClaimCalculationViewTableDTO.getClassification();
						MastersValue ackclassifTypeMast = null;
							if(ackclassifType != null && ackclassifType.getId() != null){
								ackclassifTypeMast = new MastersValue();
								ackclassifTypeMast.setKey(ackclassifType.getId());
								ackclassifTypeMast.setValue(ackclassifType.getValue());						
								acknowledgement.setClassificationId(ackclassifTypeMast);
						}
							SelectValue acksubclassifType = ompClaimCalculationViewTableDTO.getSubClassification();
							MastersValue acksubclassifTypeMast = null;
							if(acksubclassifType != null && acksubclassifType.getId() != null){
								acksubclassifTypeMast = new MastersValue();
								acksubclassifTypeMast.setKey(acksubclassifType.getId());
								acksubclassifTypeMast.setValue(acksubclassifType.getValue());						
								acknowledgement.setSubClassificationId(acksubclassifTypeMast);
							}
							
							SelectValue ackdocType = ompClaimCalculationViewTableDTO.getDocRecivedFrm();
							MastersValue ackdocTypeMast =null;
							if(ackdocType != null && ackdocType.getId() != null){
								ackdocTypeMast = new MastersValue();
								ackdocTypeMast.setKey(ackdocType.getId());
								ackdocTypeMast.setValue(ackdocType.getValue());						
								acknowledgement.setDocumentReceivedFromId(ackdocTypeMast);
							}
							SelectValue ackcategory = ompClaimCalculationViewTableDTO.getCategory();
							MastersValue ackcategValue = null;
							if(ackcategory!= null && ackcategory.getId()!= null){
								ackcategValue = new MastersValue();
								ackcategValue.setKey(ackcategory.getId());
								ackcategValue.setValue(ackcategory.getValue());
								acknowledgement.setCategoryId(ackcategValue);
							}
					
					entityManager.merge(acknowledgement);
				}
			}
//			Add for rodkey in ack
			//CR20181332 Start
			if(ompClaimCalculationViewTableDTO.getReject()!= null){
				if(SHAConstants.OMP_PROCESSOR.equals(screenName)){
					if(ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
						insertRejectionCategories(ompClaimProcessorDTO, claim,reimbursement, ompClaimCalculationViewTableDTO);
					}
				}
				if(SHAConstants.OMP_APPROVER.equals(screenName)){
					/*if(ompClaimCalculationViewTableDTO.getSendforApprover() != null){
						if(ompClaimCalculationViewTableDTO.getSendforApprover().equals("Y")){
							reimbursement.setDischargeFlag(ompClaimCalculationViewTableDTO.getDisVoucFlag());
							if(ompClaimCalculationViewTableDTO.getGenDisVoucherFlag() != null){
								if(ompClaimCalculationViewTableDTO.getGenDisVoucherFlag()){
									reimbursement.setGenerateDischargeVoucherFlag("Y");
								}else{
									reimbursement.setGenerateDischargeVoucherFlag("N");
								}

								if(reimbursement.getGenerateDischargeVoucherFlag().equals("Y")){
									// Omp Discharge Voucher Letter Upload...
									DocumentGenerator letterObj = new DocumentGenerator();
									ReportDto reportDto = new ReportDto();
									if(reimbursement.getClaim() != null){
										reportDto.setClaimId(reimbursement.getClaim().getClaimId());
									}					
									List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
									letterDTOList.add(ompClaimCalculationViewTableDTO);		
									reportDto.setBeanList(letterDTOList);
									String generatedFilePath = letterObj.generatePdfDocument("OMPDischargeVoucher", reportDto);
									HashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);

									OMPDocumentDetails ompDocument = new OMPDocumentDetails();
									ompDocument.setIntimationNumber(reimbursement.getClaim().getIntimation().getIntimationId());
									ompDocument.setClaimNumber(reimbursement.getClaim().getClaimId());
									ompDocument.setReimbursementNumber(reimbursement.getRodNumber());
									ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
									ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
									ompDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
									ompDocument.setDocumentType("OMPDischargeVoucher");
									ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
									ompDocument.setCreatedBy(ompClaimProcessorDTO.getUserName());
									entityManager.persist(ompDocument);
								}
							}
							reimbursement.setDischargeVoucherRemarks(ompClaimCalculationViewTableDTO.getDisVoucherRemarks());
							reimbursement.setNomineeName(ompClaimCalculationViewTableDTO.getNomineeName());
							reimbursement.setNomineeAddress(ompClaimCalculationViewTableDTO.getNomineeAddress());
							
							if(ompClaimCalculationViewTableDTO.getGenCoveringLetterFlag() != null){
								if(ompClaimCalculationViewTableDTO.getGenCoveringLetterFlag()){
									reimbursement.setGenerateCoveringLetterFlag("Y");
								}else{
									reimbursement.setGenerateCoveringLetterFlag("N");
								}

								if(reimbursement.getGenerateCoveringLetterFlag().equals("Y")){
									// Omp Covering Letter Upload...
									DocumentGenerator letterObj = new DocumentGenerator();
									ReportDto reportDto = new ReportDto();
									if(reimbursement.getClaim() != null){
										reportDto.setClaimId(reimbursement.getClaim().getClaimId());
									}					
									List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
									letterDTOList.add(ompClaimCalculationViewTableDTO);		
									reportDto.setBeanList(letterDTOList);
									String generatedFilePath = "";
									if(ompClaimCalculationViewTableDTO.getFinalApprovedAmtInr().longValue() > 100000){
										generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterAboveOneLakh", reportDto);
									}else{
										generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterLessThanOneLakh", reportDto);
									}
									HashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);

									OMPDocumentDetails ompDocument = new OMPDocumentDetails();
									ompDocument.setIntimationNumber(reimbursement.getClaim().getIntimation().getIntimationId());
									ompDocument.setClaimNumber(reimbursement.getClaim().getClaimId());
									ompDocument.setReimbursementNumber(reimbursement.getRodNumber());
									ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
									ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
									ompDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
									ompDocument.setDocumentType("OMPCoveringLetter");
									ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
									ompDocument.setCreatedBy(ompClaimProcessorDTO.getUserName());
									entityManager.persist(ompDocument);
								}
							}


						}
					}*/
					
					if(!ompClaimCalculationViewTableDTO.isProcessorRejectedClaim()){
						if(ompClaimCalculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
							insertRejectionCategories(ompClaimProcessorDTO, claim,reimbursement, ompClaimCalculationViewTableDTO);
						}
					}
				}
				
			}
			
			
			if(SHAConstants.OMP_APPROVER.equals(screenName)){
				if(ompClaimCalculationViewTableDTO.getSendforApprover() != null){
					if(ompClaimCalculationViewTableDTO.getSendforApprover().equals("Y") && ompClaimCalculationViewTableDTO.getProcessorStatus().intValue() != 3221){
						reimbursement.setDischargeFlag(ompClaimCalculationViewTableDTO.getDisVoucFlag());
						if(ompClaimCalculationViewTableDTO.getGenDisVoucherFlag() != null){
							if(ompClaimCalculationViewTableDTO.getGenDisVoucherFlag()){
								reimbursement.setGenerateDischargeVoucherFlag("Y");
							}else{
								reimbursement.setGenerateDischargeVoucherFlag("N");
							}

							if(reimbursement.getGenerateDischargeVoucherFlag().equals("Y")){
								// Omp Discharge Voucher Letter Upload...
								DocumentGenerator letterObj = new DocumentGenerator();
								ReportDto reportDto = new ReportDto();
								if(reimbursement.getClaim() != null){
									reportDto.setClaimId(reimbursement.getClaim().getClaimId());
								}					
								List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
								letterDTOList.add(ompClaimCalculationViewTableDTO);		
								reportDto.setBeanList(letterDTOList);
								String generatedFilePath = letterObj.generatePdfDocument("OMPDischargeVoucher", reportDto);
								WeakHashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);

								OMPDocumentDetails ompDocument = new OMPDocumentDetails();
								ompDocument.setIntimationNumber(reimbursement.getClaim().getIntimation().getIntimationId());
								ompDocument.setClaimNumber(reimbursement.getClaim().getClaimId());
								ompDocument.setReimbursementNumber(reimbursement.getRodNumber());
								ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
								ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
								ompDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
								ompDocument.setDocumentType("OMPDischargeVoucher");
								ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								ompDocument.setCreatedBy(ompClaimProcessorDTO.getUserName());
								entityManager.persist(ompDocument);
							}
						}
						reimbursement.setDischargeVoucherRemarks(ompClaimCalculationViewTableDTO.getDisVoucherRemarks());
						reimbursement.setNomineeName(ompClaimCalculationViewTableDTO.getNomineeName());
						reimbursement.setNomineeAddress(ompClaimCalculationViewTableDTO.getNomineeAddress());
						//IMSSUPPOR-29558
						ompClaimCalculationViewTableDTO.setClaimProcessorDTO(ompClaimProcessorDTO);
						if(null != reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode()){
							
							List<MasOmbudsman> ombudsmanDetailsList = getOmbudsmanOffiAddrByPIOCode(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode());
							
							if(ombudsmanDetailsList != null &&  !ombudsmanDetailsList.isEmpty()) {
								ompClaimCalculationViewTableDTO.getClaimProcessorDTO().getClaimDto().setOmbudsManAddressList(ombudsmanDetailsList);
							}	
						}						
						
						if(ompClaimCalculationViewTableDTO.getGenCoveringLetterFlag() != null){
							if(ompClaimCalculationViewTableDTO.getGenCoveringLetterFlag()){
								reimbursement.setGenerateCoveringLetterFlag("Y");
							}else{
								reimbursement.setGenerateCoveringLetterFlag("N");
							}

							if(reimbursement.getGenerateCoveringLetterFlag().equals("Y")){
								// Omp Covering Letter Upload...
								DocumentGenerator letterObj = new DocumentGenerator();
								ReportDto reportDto = new ReportDto();
								if(reimbursement.getClaim() != null){
									reportDto.setClaimId(reimbursement.getClaim().getClaimId());
								}					
								List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
								letterDTOList.add(ompClaimCalculationViewTableDTO);		
								reportDto.setBeanList(letterDTOList);
								String generatedFilePath = "";
								if(ompClaimCalculationViewTableDTO.getFinalApprovedAmtInr().longValue() > 100000){
									generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterAboveOneLakh", reportDto);
								}else{
									generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterLessThanOneLakh", reportDto);
								}
								WeakHashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);

								OMPDocumentDetails ompDocument = new OMPDocumentDetails();
								ompDocument.setIntimationNumber(reimbursement.getClaim().getIntimation().getIntimationId());
								ompDocument.setClaimNumber(reimbursement.getClaim().getClaimId());
								ompDocument.setReimbursementNumber(reimbursement.getRodNumber());
								ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
								ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
								ompDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
								ompDocument.setDocumentType("OMPCoveringLetter");
								ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								ompDocument.setCreatedBy(ompClaimProcessorDTO.getUserName());
								entityManager.persist(ompDocument);
							}
						}
					}
				}
			}
			
			//CR20181332 End
			savePayment(ompClaimCalculationViewTableDTO, reimbursement);
			setSaveNegotiation(ompClaimCalculationViewTableDTO, claim, reimbursement);
			saveRecovarables(ompClaimCalculationViewTableDTO, reimbursement);
			saveUploadDocuments(ompClaimCalculationViewTableDTO, reimbursement,ompClaimProcessorDTO);
		 OMPBenefitCover ompBenefitCoverByKey =getOMPBenefitCoverByBenefitKey(ompClaimCalculationViewTableDTO.getRodKey());
		 if(ompClaimCalculationViewTableDTO.getKey()!=null){
			 if(ompBenefitCoverByKey!=null){
				 saveBenefitCover(ompClaimCalculationViewTableDTO ,ompBenefitCoverByKey);
				 ompBenefitCoverByKey.setRodKey(reimbursement);
				 ompBenefitCoverByKey.setInsuredKey(reimbursement.getInsuredKey());
				 MastersEvents eventCodeId = reimbursement.getEventCodeId();
				 ompBenefitCoverByKey.setCovedId(eventCodeId.getKey());
				 ompBenefitCoverByKey.setClaimKey(claim);
				 insertOrUpdateBenefit(ompBenefitCoverByKey);
			 }
			 
		 }else{
			 ompBenefitCoverByKey = new OMPBenefitCover();
			 saveBenefitCover(ompClaimCalculationViewTableDTO , ompBenefitCoverByKey);
			 ompBenefitCoverByKey.setRodKey(reimbursement);
			 ompBenefitCoverByKey.setInsuredKey(reimbursement.getInsuredKey());
			 MastersEvents eventCodeId = reimbursement.getEventCodeId();
			 ompBenefitCoverByKey.setCovedId(eventCodeId.getKey());
			 ompBenefitCoverByKey.setClaimKey(claim);
			 insertOrUpdateBenefit(ompBenefitCoverByKey);
			 }
			}
			}
	//updating stage and status into claim table
	if(reimbursement!=null && reimbursement.getStage()!=null && reimbursement.getStatus()!=null){
		claim.setStage(reimbursement.getStage());
		claim.setStatus(reimbursement.getStatus());
		insertOrUpdate(claim);
	}
}


	@SuppressWarnings("unchecked")
	private void insertRejectionCategories(OMPClaimProcessorDTO ompClaimProcessorDTO, OMPClaim claim, OMPReimbursement reimbursement, OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO) {
		List<OMPRodRejection> listOfRejectionObj = new ArrayList<OMPRodRejection>();
		if(ompClaimCalculationViewTableDTO.getRejectionIds() != null){
			HashSet<SelectValue> listOfObj = new HashSet<SelectValue>((Collection) ompClaimCalculationViewTableDTO.getRejectionIds());
//			List<OMPRodRejection> dbList = checkAlreadyRejIdsAvaiable(reimbursement);
//			if(dbList != null && dbList.isEmpty()){
				OMPRodRejection rejectionObj = null;
				for(SelectValue select : listOfObj){
					String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
					rejectionObj = new OMPRodRejection();
					rejectionObj.setReimbursementKey(reimbursement);
					rejectionObj.setClaimKey(claim.getKey());
					rejectionObj.setRodNumber(reimbursement.getRodNumber());
					rejectionObj.setRejectionCategoryId(select.getId());
					rejectionObj.setCreatedBy(loginUserId);
					rejectionObj.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					rejectionObj.setActiveStatus(1L);
					listOfRejectionObj.add(rejectionObj);
				}
			/*}else if(dbList != null && !dbList.isEmpty()){
				System.out.println("Came in else for rejection categories OMP....");
				List<Object> innerData = null; 
				Map<Long, Object> dbListIds = new HashMap<Long, Object>();
				OMPRodRejection rejectionNewObj = null;
				// Collecting data from DB.....
				for(OMPRodRejection rec : dbList){
					innerData = new ArrayList<Object>();
					innerData.add(rec);
					if(rec.getActiveStatus().intValue() == 1){
						innerData.add("ACTIVE");
					}else{
						innerData.add("DELETED");
					}
					dbListIds.put(rec.getRejectionCategoryId(), innerData);
				}
				
				List<Long> currentUpdatedList = new ArrayList<Long>();
				for(SelectValue select : listOfObj){
					if(dbListIds.get(select.getId()) != null){
						// Omitting already inserted and Active rej Ids ....
						currentUpdatedList.add(select.getId());
					}else if(dbListIds.get(select.getId()) == null){
						// Inserting the newly added rejection Ids to rejection table....
						String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
						rejectionNewObj = new OMPRodRejection();
						rejectionNewObj.setReimbursementKey(reimbursement);
						rejectionNewObj.setClaimKey(claim.getKey());
						rejectionNewObj.setRodNumber(reimbursement.getRodNumber());
						//rejectionNewObj.setDocAckKey(docAckKey);
						rejectionNewObj.setRejectionCategoryId(select.getId());
						rejectionNewObj.setCreatedBy(loginUserId);
						rejectionNewObj.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						rejectionNewObj.setActiveStatus(1L);
						entityManager.persist(rejectionNewObj);
						currentUpdatedList.add(select.getId());
					}
				}				
				// Updating remaining ids as Deleted in rejection table....
				List<Long> dbIds = new ArrayList<Long>();
				if(dbList != null && dbList.isEmpty()){
					for(SelectValue select : listOfObj){
						dbIds.add(select.getId());
					}
				}
				dbIds.removeAll(currentUpdatedList);
				//Here is updating......
				if(dbIds != null && !dbIds.isEmpty()){
					for(Long val : dbIds){
						List<Object> toBeDelObj = (List<Object>) dbListIds.get(val);
						OMPRodRejection obj = (OMPRodRejection) toBeDelObj.get(0);
						String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
						obj.setModifiedBy(loginUserId);
						obj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						obj.setActiveStatus(0L);
						entityManager.merge(obj);
					}
				}
			}*/
				
			
		}
		// Omp Rejection Letter Upload...
		if(!checkAlreadyRejectionLetterGenerated(reimbursement)){
			DocumentGenerator letterObj = new DocumentGenerator();
			ReportDto reportDto = new ReportDto();
			if(reimbursement.getClaim() != null){
				reportDto.setClaimId(reimbursement.getClaim().getClaimId());
			}					
			List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
			letterDTOList.add(ompClaimCalculationViewTableDTO);		
			reportDto.setBeanList(letterDTOList);
			String generatedFilePath = letterObj.generatePdfDocument("OMPRejectionLetter", reportDto);
			WeakHashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);

			OMPDocumentDetails ompDocument = new OMPDocumentDetails();
			ompDocument.setIntimationNumber(reimbursement.getClaim().getIntimation().getIntimationId());
			ompDocument.setClaimNumber(reimbursement.getClaim().getClaimId());
			ompDocument.setReimbursementNumber(reimbursement.getRodNumber());
			ompDocument.setFileName(String.valueOf(inParameters.get("fileName")));// to be taken from inParameters hash map.
			ompDocument.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
			ompDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
			ompDocument.setDocumentType("OMPRejectionLetter");
			ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			ompDocument.setCreatedBy(ompClaimProcessorDTO.getUserName());
			entityManager.persist(ompDocument);
		}
		if(listOfRejectionObj.size() > 0){
			for(OMPRodRejection rejRec : listOfRejectionObj){
				entityManager.persist(rejRec);
			}
		}
	}
	
	/*private List<OMPRodRejection> checkAlreadyRejIdsAvaiable(OMPReimbursement argReimbursement){
		Query query = entityManager.createNamedQuery("OMPRodRejection.findByRODKeyWithDeleted");
		query = query.setParameter("rodKey", argReimbursement.getKey());
		List<OMPRodRejection> docDetailsList = query.getResultList();
		if(null != docDetailsList && !docDetailsList.isEmpty()){
			for (OMPRodRejection documentDetails : docDetailsList) {
				entityManager.refresh(documentDetails);
			}
			return docDetailsList;
		}
		return null;
	}*/
	
	private boolean checkAlreadyRejectionLetterGenerated(OMPReimbursement argReimbursement){
		Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByRejectionLetter");
		query = query.setParameter("intNo", argReimbursement.getClaim().getIntimation().getIntimationId());
		query = query.setParameter("claimNo", argReimbursement.getClaim().getClaimId());
		query = query.setParameter("rodNo", argReimbursement.getRodNumber());
		query = query.setParameter("docType", "OMPRejectionLetter");
		
		List<OMPDocumentDetails> docDetailsList = query.getResultList();
		if(null != docDetailsList && !docDetailsList.isEmpty()){
			return true;
		}
		return false;
	}

private void saveUploadDocuments(OMPClaimCalculationViewTableDTO calculationViewTableDTO,OMPReimbursement reimbursement, OMPClaimProcessorDTO ompClaimProcessorDTO) {
			if(calculationViewTableDTO.getReceiptOfDocumentsDTO()!=null){
//				List<UploadDocumentDTO> uploadDocsList = receiptOfDocumentsDTO.getUploadDocsList();//DD
				ReceiptOfDocumentsDTO receiptOfDocumentsDTO = calculationViewTableDTO.getReceiptOfDocumentsDTO();
				List<UploadDocumentDTO> uploadDocsList = receiptOfDocumentsDTO.getUploadDocsList();
				if(uploadDocsList!=null && !uploadDocsList.isEmpty()){
					for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
						uploadDocumentDTO.setRodKey(reimbursement.getKey());
						uploadDocumentDTO.setRodNo(reimbursement.getRodNumber());
						uploadDocumentDTO.setClaimNo(reimbursement.getClaim().getClaimId());
						uploadDocumentDTO.setIntimationNo(reimbursement.getClaim().getIntimation().getIntimationId());
						uploadDocumentDTO.setUsername(ompClaimProcessorDTO.getUserName());
						uploadDocumentDTO.setCreatedDate(new Date());
						uploadDocumentDTO.setUploadDocsList(uploadDocsList);
						submitSearchOrUploadDocumentsForAckNotReceived(uploadDocumentDTO);
					}
				}
			}
	
}


		public List<OMPDocumentDetails> getDocumentDetailsByRodNumber(String rodNumber)
		{
			Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByRodNo");
			query = query.setParameter("reimbursementNumber", rodNumber);
			List<OMPDocumentDetails> docDetailsList = query.getResultList();
			if(null != docDetailsList && !docDetailsList.isEmpty())
			{
				for (OMPDocumentDetails documentDetails : docDetailsList) {
					entityManager.refresh(documentDetails);
				}
				return docDetailsList;
			}
			return null;
		
		}
		
		public List<OMPReimbursement> getOMPReimbursementList(Long rodKey) {
			Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey");
			query.setParameter("primaryKey", rodKey);
			List<OMPReimbursement> resultList = (List<OMPReimbursement>) query.getResultList();
			
			for (OMPReimbursement claimRate : resultList) {
				entityManager.refresh(claimRate);
			}
			return   resultList;
		}
		
		public List<OMPClaimPayment> getOMPClaimPaymentByIntimationNo(String intimationNo) {
			Query query = entityManager.createNamedQuery("OMPClaimPayment.findByOMPIntimationNumber");
			query.setParameter("intimationNumber", intimationNo);
			List<OMPClaimPayment> resultList = (List<OMPClaimPayment>) query.getResultList();
			
			for (OMPClaimPayment claimRate : resultList) {
				entityManager.refresh(claimRate);
			}
			return   resultList;
		}
		
		public void saveOrUpdateIntimation(OMPClaimProcessorDTO ompClaimProcessorDTO,
				OMPIntimation intimation) {
			
			intimation.setAdmissionDate(ompClaimProcessorDTO.getAdmissionDate());
			intimation.setDischargeDate(ompClaimProcessorDTO.getDischargeDate());
			intimation.setAilmentLoss(ompClaimProcessorDTO.getAilmentLoss());
			intimation.setEventPlace(ompClaimProcessorDTO.getPlaceEvent());
			intimation.setPlaceVisit(ompClaimProcessorDTO.getPlaceOfVisit());
			intimation.setHospitalName(ompClaimProcessorDTO.getHospName());
			if(ompClaimProcessorDTO.getHospital()!=null){
				intimation.setHospital(ompClaimProcessorDTO.getHospital().getId());
			}
			intimation.setCityName(ompClaimProcessorDTO.getHospCity());
			if(ompClaimProcessorDTO.getHospCountry()!= null){
				SelectValue countryValueByKey = masterService.getCountryValueByKey(ompClaimProcessorDTO.getHospCountry().getId());
				if(countryValueByKey!= null&& countryValueByKey.getId()!= null){
					intimation.setCountryId(countryValueByKey.getId());
				}
			}
			intimation.setLossDateTime(ompClaimProcessorDTO.getLossOfDate());
			intimation.setLossTime(ompClaimProcessorDTO.getLossTime());
			intimation.setPlaceLossDelay(ompClaimProcessorDTO.getLossOrDelay());
			intimation.setLossDetails(ompClaimProcessorDTO.getLossDetails());
				
			SelectValue eventValue = ompClaimProcessorDTO.getEventCode();
			MastersEvents events = null;
			
			if(eventValue != null && eventValue.getValue() != null){
				MastersEvents masterEventValueBykey = getMasterEventValueBykey(eventValue.getId());						
				intimation.setEvent(masterEventValueBykey);
				}
			
			if(ompClaimProcessorDTO.getClaimType()!= null && ompClaimProcessorDTO.getClaimType().equals(Boolean.TRUE)){
				MastersValue master = getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				SelectValue claimType = new SelectValue();
				claimType.setId(master.getKey());
				claimType.setValue(master.getValue());
				intimation.setClaimType(master);
			}
			if(ompClaimProcessorDTO.getClaimType()!= null && ompClaimProcessorDTO.getClaimType().equals(Boolean.FALSE)){
				MastersValue master = getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				SelectValue claimType = new SelectValue();
				claimType.setId(master.getKey());
				claimType.setValue(master.getValue());
				intimation.setClaimType(master);
			}
			if(ompClaimProcessorDTO.getNonHospitalisationFlag()!= null){
				intimation.setNonHospitalizationFlag(ompClaimProcessorDTO.getNonHospitalisationFlag());
			}
			
			intimation.setInrConversionRate(ompClaimProcessorDTO.getInrConversionRate());
			intimation.setInrTotalAmount(ompClaimProcessorDTO.getInrtotal());
			intimation.setDollarInitProvisionAmt(ompClaimProcessorDTO.getProvisionAmt());
		}
		
		
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public void insertOrUpdate(OMPIntimation intimation) {
				if(intimation != null){
			if(intimation.getKey() == null){
				entityManager.persist(intimation);
				
			}
			else if(intimation != null && intimation.getKey() != null ){
				
				intimation.setModifiedDate(new Date());
				entityManager.merge(intimation);
			}
			entityManager.flush();
			//entityManager.refresh(reimbursement);
		}	
			
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


		public void setSaveNegotiation(OMPNegotiation negotiation) {
			if(negotiation.getKey()!=null){
				entityManager.merge(negotiation);
			}else{
				entityManager.persist(negotiation);
			}
			
		}
		
		public void saveRecovarables(OMPClaimCalculationViewTableDTO calculationViewTableDTO,OMPReimbursement ompReimbursement) {
			List<OMPNewRecoverableTableDto> newRecoverableDtoList = calculationViewTableDTO.getOmpRecoverableTableList();
			if(newRecoverableDtoList!= null){
				for(OMPNewRecoverableTableDto newRecoverableTableDtos : newRecoverableDtoList){
					if(newRecoverableTableDtos.getKey() == null){
						if(ompReimbursement!= null && ompReimbursement.getKey()!= null){
							if(newRecoverableTableDtos.getAmountRecoveredInr()!= null){
								long amtInr = newRecoverableTableDtos.getAmountRecoveredInr().longValue();
								ompReimbursement.setRecoveredAmountInr(amtInr);
							}
							if(newRecoverableTableDtos.getAmountRecoveredUsd()!= null){
								long amtUsd = newRecoverableTableDtos.getAmountRecoveredUsd().longValue();
								ompReimbursement.setRecoveredAmountUsd(amtUsd);
							}
							ompReimbursement.setRecoveredDate(newRecoverableTableDtos.getDateofRecovery());
							ompReimbursement.setRemarks(newRecoverableTableDtos.getRemarks());
							ompReimbursement.setSendToAccounts(newRecoverableTableDtos.getSendToAccounts());
//							rodBillentryService.saveReimbursement(ompClaimProcessorDTO, null, ompReimbursement);
							insertOrUpdate(ompReimbursement);
						}
					}
				}
			}
		}
		
		public void savePayment(OMPClaimCalculationViewTableDTO ompClaimProcessorDTO,OMPReimbursement ompReimbursement) {
			List<OMPPaymentDetailsTableDTO> paymentTableDtoList = ompClaimProcessorDTO.getOmpPaymentDetailsList();
			if(paymentTableDtoList!= null){
				for(OMPPaymentDetailsTableDTO payTableDTO : paymentTableDtoList){
					if(payTableDTO.getKey() == null){
						if(ompReimbursement!= null && ompReimbursement.getKey()!= null){
							SelectValue paymentTo = payTableDTO.getPaymentTo();
							MastersValue paymentToMast = null;
							if(paymentTo != null && paymentTo.getId() != null){
								paymentToMast = new MastersValue();
								paymentToMast.setKey(paymentTo.getId());
								paymentToMast.setValue(paymentTo.getValue());
								ompReimbursement.setPaymentTo(paymentToMast);
							}
							ompReimbursement.setPayeeName(payTableDTO.getPayeeNameStr());
							if(payTableDTO.getPayMode()!= null && payTableDTO.getPayMode().getId()!= null){
								ompReimbursement.setPaymentModeId(payTableDTO.getPayMode().getId());
							}
							if(payTableDTO.getCurrency()!=null){
								SelectValue currency = payTableDTO.getCurrency();
								Currency ompCurrency = getOMPCurrency(currency.getId());
								ompReimbursement.setPayeeCurrencyTypeId(ompCurrency);
							}else{
								ompReimbursement.setPayeeCurrencyTypeId(null);
							}
							
							ompReimbursement.setPayableAt(payTableDTO.getPayableAt());
							ompReimbursement.setPanNumber(payTableDTO.getPanNo());
							ompReimbursement.setEmailId(payTableDTO.getEmailId());
							ompReimbursement.setTransactionNumber(payTableDTO.getTransectionChequeNo());
//							ompReimbursement.setda
							insertOrUpdate(ompReimbursement);
						}
					}
				}
				
			}
		}
		
		public void updateNegotaion(List<OMPClaimCalculationViewTableDTO> calculationViewTableDTOLIst){
			
			//CR2019041
			OMPNegotiation negotiation =null;
			if(calculationViewTableDTOLIst!=null && !calculationViewTableDTOLIst.isEmpty()){
				for(OMPClaimCalculationViewTableDTO calculationViewTableDTO :calculationViewTableDTOLIst){
					if(calculationViewTableDTO.getSelect()!=null && calculationViewTableDTO.getSelect().getId() !=null){
						negotiation = getOMPNegotiationbyKey(calculationViewTableDTO.getSelect().getId());
						
						if(calculationViewTableDTO.getHandlingCharges()!=null){
							negotiation.setHandlingChargsUsd(calculationViewTableDTO.getHandlingCharges());
						}
						if(calculationViewTableDTO.getExpenses()!=null){
							negotiation.setExpenseAmountusd(calculationViewTableDTO.getExpenses());
						}
						if(negotiation.getKey()!=null){
							entityManager.merge(negotiation);
						}else{
							entityManager.persist(negotiation);
						}
					}
				}
			}
		//CR2019041
			
		}
		
		private List<MasOmbudsman> getOmbudsmanOffiAddrByPIOCode(String pioCode) {
			
			List<MasOmbudsman> ombudsmanDetailsByCpuCode = new ArrayList<MasOmbudsman>();
			if(pioCode != null){
				OrganaizationUnit branchOffice = ompClaimService
						.getInsuredOfficeNameByDivisionCode(pioCode);
				if (branchOffice != null) {
					String ombudsManCode = branchOffice.getOmbudsmanCode();
					if (ombudsManCode != null) {
						ombudsmanDetailsByCpuCode = masterService
								.getOmbudsmanDetailsByCpuCode(ombudsManCode);
					}
				}
			}
			return ombudsmanDetailsByCpuCode;
		}
		
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public void submitAckDocReceived(OMPClaimCalculationViewTableDTO rodDTO) {
			log.info("Submit ACKNOWLEDGEMENT ________________"
					+ (rodDTO.getReceiptOfDocumentsDTO().getNewIntimationDTO() != null ? rodDTO.getReceiptOfDocumentsDTO()
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
		
		
		@SuppressWarnings("unchecked")
		public OMPClaim getClaimByClaimKey(Long claimKey) {
			Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<OMPClaim> claim = (List<OMPClaim>) query.getResultList();

			if (claim != null && !claim.isEmpty()) {
				for (OMPClaim claim2 : claim) {
					entityManager.refresh(claim2);
				}
				return claim.get(0);
			} else {
				return null;
			}
		}
		
		public OMPClaim searchByClaimKey(Long a_key) {
			OMPClaim find = entityManager.find(OMPClaim.class, a_key);
			entityManager.refresh(find);
			return find;
		}
		public void saveUploadAckDocuments(OMPClaimCalculationViewTableDTO calculationViewTableDTO,OMPDocAcknowledgement reimbursement, OMPClaimProcessorDTO ompClaimProcessorDTO) {
			if(calculationViewTableDTO.getReceiptOfDocumentsDTO()!=null){
//				List<UploadDocumentDTO> uploadDocsList = receiptOfDocumentsDTO.getUploadDocsList();//DD
				ReceiptOfDocumentsDTO receiptOfDocumentsDTO = calculationViewTableDTO.getReceiptOfDocumentsDTO();
				List<UploadDocumentDTO> uploadDocsList = receiptOfDocumentsDTO.getUploadDocsList();
				if(uploadDocsList!=null){
					for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
						if(reimbursement != null){
//							uploadDocumentDTO.setRodKey(reimbursement.getKey());
//							uploadDocumentDTO.setRodNo(reimbursement.getRodNumber());
							uploadDocumentDTO.setClaimNo(reimbursement.getClaim().getClaimId());
							uploadDocumentDTO.setIntimationNo(reimbursement.getClaim().getIntimation().getIntimationId());
							uploadDocumentDTO.setAcknowledgementNo(reimbursement.getAcknowledgeNumber());
							
						}
						uploadDocumentDTO.setAckDocumentSource(SHAConstants.OMP_POST_PROCESS_ACK);
						uploadDocumentDTO.setUsername(ompClaimProcessorDTO.getUserName());
						uploadDocumentDTO.setCreatedDate(new Date());
						uploadDocumentDTO.setUploadDocsList(uploadDocsList);
						submitSearchOrUploadDocumentsForAckNotReceived(uploadDocumentDTO);
					}
				}
			}
	
}
		@SuppressWarnings("unchecked")
		public OMPDocAcknowledgement getAcknowledgementByKey(Long ackkey) {
			Query query = entityManager.createNamedQuery("OMPDocAcknowledgement.findByKey")
					.setParameter("ackDocKey", ackkey);
			List<OMPDocAcknowledgement> rodList = query.getResultList();

			if (rodList != null && !rodList.isEmpty()) {
				for (OMPDocAcknowledgement reimbursement : rodList) {
					entityManager.refresh(reimbursement);
				}
				return rodList.get(0);
			}
			return null;
		}
		
		public List<OMPDocumentDetails> getDocumentsByAckNumber(String rodNumber)
		{
			Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByAckNo");
			query = query.setParameter("acknowledgeNumber", rodNumber);
			List<OMPDocumentDetails> docDetailsList = query.getResultList();
			if(null != docDetailsList && !docDetailsList.isEmpty())
			{
				for (OMPDocumentDetails documentDetails : docDetailsList) {
					if(documentDetails.getDocumentSource()!=null 
							&& documentDetails.getDocumentSource().equalsIgnoreCase(SHAConstants.OMP_POST_PROCESS_ACK))
					entityManager.refresh(documentDetails);
				}
				return docDetailsList;
			}
			return null;
		
		}
		
}