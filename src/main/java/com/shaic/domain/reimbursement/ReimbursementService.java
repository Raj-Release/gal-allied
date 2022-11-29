package com.shaic.domain.reimbursement;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multiset.Entry;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.cashlessprocess.processicac.search.IcacRequest;
import com.shaic.claim.common.NursingChargesMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.fileUpload.ReferenceDocument;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.fvrgrading.page.FvrReportGradingPageDto;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalTaxDeduction;
import com.shaic.claim.leagalbilling.LegalTaxDeductionMapper;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsuredDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.mapper.ProcessClaimRequestBenefitsMapper;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.BillingInternalRemarksTableDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.dto.PostHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PreHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.billing.wizard.BillingInternalRemarksMapper;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.FAInternalRemarksTableDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FAInternalRemarksMapper;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
import com.shaic.claim.reimbursement.rrc.services.ReviewRRCRequestMapper;
import com.shaic.claim.reports.PolicywiseClaimReportDto;
import com.shaic.claim.reports.ExecutiveStatusReport.EmpSearchDto;
import com.shaic.claim.reports.ExecutiveStatusReport.ExecutiveStatusDetailReportDto;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummaryReportDto;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummarySearchDto;
import com.shaic.claim.reports.medicalAuditClaimStatusReport.MedicalAuditClaimStatusReportDto;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.dto.ViewBillSummaryRemarksDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimSectionCover;
import com.shaic.domain.ClaimVerification;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.EmpSecUser;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.FVRGradingMaster;
import com.shaic.domain.FvrTriggerPoint;
import com.shaic.domain.GpaPolicy;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MasBillClassification;
import com.shaic.domain.MasBillDetailsType;
import com.shaic.domain.MasHospitalCashBenefit;
import com.shaic.domain.MasHospitals;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NEFTQueryDetails;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OpinionValidation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.Product;
import com.shaic.domain.ProposerNominee;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.RejectReason;
import com.shaic.domain.StarJioPolicy;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.ViewBillRemarks;
import com.shaic.domain.preauth.BenefitAmountDetails;
import com.shaic.domain.preauth.BillingAutoAllocationIcdCode;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.InternalInfo;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.RodBillSummary;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.UploadDocumentCloseClaimDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;

@Stateless
public class ReimbursementService {
	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private ViewFVRService viewFVRService;
	
	@EJB
	private InvestigationService investigationService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	private Boolean isCovidIcdSelected = false;
	
	private final Logger log = LoggerFactory.getLogger(ReimbursementService.class);

	public ReimbursementService() {
		super();
	}

	public void refereshObj(Reimbursement reimbursement) {
		entityManager.refresh(reimbursement);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitZonalReview(PreauthDTO preauthDTO,
			Boolean isZonalReview) throws Exception {
		String outCome = "";
	//	try {
			log.info("Submit Claim Zonal ---------------" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
			preauthDTO.setZonalDate(new Timestamp(System.currentTimeMillis()));
			Reimbursement reimbursement = savePreauthValues(preauthDTO, false,SHAConstants.ZONAL_REVIEW);

			if (reimbursement.getClaim() != null) {
				Claim currentClaim = searchByClaimKey(reimbursement.getClaim()
						.getKey());
				if (currentClaim != null) {
//					currentClaim.setStatus(reimbursement.getStatus());
//					currentClaim.setStage(reimbursement.getStage());  
					if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
						currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
					}
					currentClaim.setDataOfAdmission(reimbursement.getDateOfAdmission());
					if(reimbursement.getDateOfDischarge() != null){
						currentClaim.setDataOfDischarge(reimbursement.getDateOfDischarge());
					}
					
					// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
					if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
						currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
						currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
						currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
					}

					entityManager.merge(currentClaim);
					entityManager.flush();
					entityManager.clear();
					log.info("------CurrentClaim------>"+currentClaim+"<------------");

					
					reimbursement.setClaim(currentClaim);
				}
			}

//			String remarksForZonalReview = getRemarksForZonalReview(
//					reimbursement.getStatus().getKey(), preauthDTO,
//					reimbursement);

		/*	if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS)) {
//				reimbursement.setRejectionRemarks(remarksForZonalReview);
				outCome = SHAConstants.OUTCOME_ZMR_INITIATE_INVESTIGATION_STATUS;
			}*/ if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS)) {
//				reimbursement.setRejectionRemarks(remarksForZonalReview);
				outCome = SHAConstants.OUTCOME_ZMR_APPROVE_STATUS;
			} else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
//				reimbursement.setApprovalRemarks(remarksForZonalReview);
				outCome = SHAConstants.OUTCOME_ZMR_APPROVE_STATUS;
			} else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS)) {
//				reimbursement.setApprovalRemarks(remarksForZonalReview);
				outCome = SHAConstants.OUTCOME_ZMR_APPROVE_STATUS;
			} else if (reimbursement
					.getStatus()
					.getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS)) {
			//	outCome = "COORDINATOR";
				outCome = SHAConstants.OUTCOME_ZMR_REFER_TO_COORDINATOR_STATUS;
			} else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZMR_REFER_TO_BILL_ENTRY)) {
//				reimbursement.setApprovalRemarks(remarksForZonalReview);
				outCome = SHAConstants.OUTCOME_ZMR_REFER_TO_BILLENTRY;
			}else if (reimbursement
					.getStatus()
					.getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_COORDINATOR_REPLY_STATUS)) {
				// outCome = "coordinatorReply"; // need to implement
			} else if (reimbursement
					.getStatus()
					.getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD)) {
				//outCome = SHAConstants.CANCEL_ROD_OUTCOME; // need to implement
				outCome = SHAConstants.OUTCOME_ZMR_CANCEL_ROD_STATUS;
			}else if (reimbursement
					.getStatus()
					.getKey()
					.equals(ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS)) {
				//outCome = "FVR";
				outCome = SHAConstants.OUTCOME_ZONAL_REVIEW_INTIATE_FVR_STATUS;
				
				// preauthRequest.setKey(reimbursement.getClaim().getKey());
			}

//			entityManager.merge(reimbursement); 
//			entityManager.flush();

			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
				ResidualAmount residualAmt = new ResidualAmount();
				residualAmt.setKey(null);
				residualAmt.setTransactionKey(reimbursement.getKey());
				residualAmt.setStage(reimbursement.getStage());
				residualAmt.setStatus(reimbursement.getStatus());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setAmountConsideredAmount(preauthDTO
						.getResidualAmountDTO().getAmountConsideredAmount());
				residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO()
						.getMinimumAmount());
				residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO()
						.getCopayAmount());
				residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO()
						.getApprovedAmount());
				residualAmt.setCopayPercentage(preauthDTO
						.getResidualAmountDTO().getCopayPercentage());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO()
						.getNetAmount() != null ? preauthDTO
						.getResidualAmountDTO().getNetAmount() : 0d);
				residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d);
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
					entityManager.flush();
					entityManager.clear();
					
				}
			}

			//setBPMOutcome(preauthDTO, true, outCome,reimbursement);
			submitClaimRequestTaskToDBProcedure(preauthDTO, true, outCome, reimbursement,false);
			return reimbursement;
		/*} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show(e.getMessage());
		}
		return null;*/
	}
	
	
	private Reimbursement updateZonalReviewRemarks(PreauthDTO preauthDTO, Reimbursement reimbursement){
		
		String remarksForZonalReview = getRemarksForZonalReview(
				preauthDTO.getStatusKey(), preauthDTO,
				reimbursement);

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS)) {
			reimbursement.setRejectionRemarks(remarksForZonalReview);
			
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
			reimbursement.setApprovalRemarks(remarksForZonalReview);
			
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS)) {
			reimbursement.setApprovalRemarks(remarksForZonalReview);
			
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS)) {
			
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_COORDINATOR_REPLY_STATUS)) {
			// need to implement
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD)) {
			reimbursement.setCancellationRemarks(remarksForZonalReview);
			reimbursement.setCancellationReasonId(preauthDTO.getPreauthMedicalProcessingDetails().getCancellationReason() != null ? preauthDTO.getPreauthMedicalProcessingDetails().getCancellationReason().getId() : null);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			resetReimbursementBenefitAmount(reimbursement);
//			Long claimKey = reimbursement.getClaim().getKey();
//			Claim claim = getClaimByClaimKey(claimKey);
//			reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
		}
		reimbursement.setZonalDate(new Timestamp(System
					.currentTimeMillis()));
		return reimbursement;
	}
	
	
	private Reimbursement updateClaimRequestRemarks(PreauthDTO preauthDTO,Reimbursement reimbursement,Claim claim){
		
		String remarksForClaimRequest = getRemarksForClaimRequest(
				reimbursement.getStatus().getKey(), preauthDTO,
				reimbursement);

		if (null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
			reimbursement.setRejectionRemarks(remarksForClaimRequest);
		} else if (null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			reimbursement.setApprovalRemarks(remarksForClaimRequest);
		} else if (null != reimbursement.getStatus().getKey() && reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
			reimbursement.setCancellationRemarks(remarksForClaimRequest);
			reimbursement.setCancellationReasonId(preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason().getId() : null);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			resetReimbursementBenefitAmount(reimbursement);
		}
		
		// Provision Amount Related stuff......
		Double totalClaimedAmount = getTotalClaimedAmount(reimbursement);
		if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
			Double approvedAmount = reimbursement.getApprovedAmount() != null ? reimbursement
					.getApprovedAmount() : 0d;
			
			
			/*<------------ As per requirement Provision Amount changes has been made by Saravana........ ------------------------------> *//////
			if(reimbursement.getClaim().getClaimType() != null) {
				if((reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y") || (reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("Y")))) {
					if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
						Double prePostTotalClaimedAmount = getPrePostTotalClaimedAmount(reimbursement);
						approvedAmount += prePostTotalClaimedAmount;
						// Added the below line to prevent the provision amount  to exceed the Balance SI
						Double min = Math.min(preauthDTO.getBalanceSI(), approvedAmount);
						// Below condition for IMSSUPPOR-29565 - If Approved Amt is zero, current provision updated as zero
						if(min != null && min > 0){
							reimbursement.setCurrentProvisionAmt(min);
						}

					} else {
						// Lesser of (Amount claimed or Approved amount )
						Double prePostTotalClaimedAmount = getPrePostTotalClaimedAmount(reimbursement);
						approvedAmount += prePostTotalClaimedAmount;
						Double min = Math.min(totalClaimedAmount, approvedAmount);
						 Double revisedProvisionAmt = Math.min(min,preauthDTO.getBalanceSI());
						// Below condition for IMSSUPPOR-29565 - If Approved Amt is zero, current provision updated as zero
							if(revisedProvisionAmt != null && revisedProvisionAmt > 0){
								reimbursement.setCurrentProvisionAmt(revisedProvisionAmt);
							}

					}
				} else {
					if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
						// Not Applicable for updating current provision amount
					} else {
						// Lesser of (Amount claimed or Approved amount )
						Double prePostTotalClaimedAmount = getPrePostTotalClaimedAmount(reimbursement);
						approvedAmount += prePostTotalClaimedAmount;
						Double min = Math.min(totalClaimedAmount, approvedAmount);
						 Double revisedProvisionAmt = Math.min(min,preauthDTO.getBalanceSI());
						// Below condition for IMSSUPPOR-29565 - If Approved Amt is zero, current provision updated as zero
							if(revisedProvisionAmt != null && revisedProvisionAmt > 0){
								reimbursement.setCurrentProvisionAmt(revisedProvisionAmt);
							}

//						reimbursement.setCurrentProvisionAmt(min);
					}
				}
				
			}
			
			Double currentProvisionAmt = reimbursement.getCurrentProvisionAmt();
			if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
						(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
					|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))))
						{
				if(reimbursement.getOtherBenefitApprovedAmt() != null){
					currentProvisionAmt += reimbursement.getOtherBenefitApprovedAmt();
					reimbursement.setCurrentProvisionAmt(currentProvisionAmt);
				}
			}
			/*<---------------------------- Current  Provision amount updates completed ---------------------------->*/
		}
		
		// No updates for provision if it is cancel ROD....
//		reimbursement = getProivsioningForCancelROD(preauthDTO, reimbursement);
		
		if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) ) {
			
			if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
				//reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
				Preauth latestPreauthByClaim = getLatestPreauthByClaim(preauthDTO.getClaimKey());
				reimbursement.setCurrentProvisionAmt(latestPreauthByClaim.getTotalApprovalAmount());
			}else{
				reimbursement.setCurrentProvisionAmt(0d);
				reimbursement.setOtherBenefitApprovedAmt(0d);
			}
		} 

		if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) ) {
			if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
				reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
				reimbursement.setOtherBenefitApprovedAmt(0d);
			}else{
				reimbursement.setCurrentProvisionAmt(0d);
				reimbursement.setOtherBenefitApprovedAmt(0d);
			}
		} 
		return reimbursement;
		
	}

	public Double getTotalClaimedAmount(Reimbursement reimbursement) {
		
		Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());
		
		Double totalClaimedAmount = 0d;
		if (reimbursementByKey.getDocAcknowLedgement() != null) {
			if (reimbursementByKey.getDocAcknowLedgement()
					.getPreHospitalizationClaimedAmount() != null) {
				totalClaimedAmount += reimbursementByKey.getDocAcknowLedgement()
						.getPreHospitalizationClaimedAmount();
			}
			if (reimbursementByKey.getDocAcknowLedgement()
					.getPostHospitalizationClaimedAmount() != null) {
				totalClaimedAmount += reimbursementByKey.getDocAcknowLedgement()
						.getPostHospitalizationClaimedAmount();
			}
			if(reimbursementByKey.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null){
				totalClaimedAmount += reimbursementByKey.getDocAcknowLedgement().getHospitalizationClaimedAmount();
			}
		}

		List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement
				.getKey());
		if (reimbursementBenefitsByRodKey != null) {
			for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
				totalClaimedAmount += reimbursementBenefits
						.getTotalClaimAmountBills() != null ? reimbursementBenefits
						.getTotalClaimAmountBills() : 0d;

			}
		}
		return totalClaimedAmount;
	}
	
	
  private Double getPrePostTotalClaimedAmount(Reimbursement reimbursement) {
		
		Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());
		
		Double totalClaimedAmount = 0d;
		if (reimbursementByKey.getDocAcknowLedgement() != null) {
			if (reimbursementByKey.getDocAcknowLedgement()
					.getPreHospitalizationClaimedAmount() != null) {
				totalClaimedAmount += reimbursementByKey.getDocAcknowLedgement()
						.getPreHospitalizationClaimedAmount();
			}
			if (reimbursementByKey.getDocAcknowLedgement()
					.getPostHospitalizationClaimedAmount() != null) {
				totalClaimedAmount += reimbursementByKey.getDocAcknowLedgement()
						.getPostHospitalizationClaimedAmount();
			}
		}

		List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement
				.getKey());
		if (reimbursementBenefitsByRodKey != null) {
			for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
				totalClaimedAmount += reimbursementBenefits
						.getTotalClaimAmountBills() != null ? reimbursementBenefits
						.getTotalClaimAmountBills() : 0d;

			}
		}
		return totalClaimedAmount;
	}

	private Reimbursement getProivsioningForCancelROD(PreauthDTO preauthDTO, Reimbursement reimbursement) {
		if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey())) {
//			TmpCPUCode tmpCpuCode = getTmpCpuCode(preauthDTO.getNewIntimationDTO().getCpuId());
			Long claimKey = reimbursement.getClaim().getKey();
			Claim claim = getClaimByClaimKey(claimKey);
			if(claim != null) {
				reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
			}
		}
		
		String[] split = reimbursement.getRodNumber().split("/");
		String splitNo = split[split.length - 1];
		Integer rodNo = Integer.valueOf(splitNo);
		
		if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) && !rodNo.equals(1) && (preauthDTO.getPreHospitalizaionFlag() || preauthDTO.getPostHospitalizaionFlag() || preauthDTO.getIsHospitalizationRepeat())) {
			reimbursement.setCurrentProvisionAmt(0d);
			reimbursement.setOtherBenefitApprovedAmt(0d);
		}
		return reimbursement;
	}
	
	private Reimbursement updateClaimBillingRemarks(PreauthDTO preauthDTO, Reimbursement reimbursement,Claim claim){
		
		String remarksForClaimRequest = getRemarksForClaimBilling(
				reimbursement.getStatus().getKey(), preauthDTO,
				reimbursement);

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
			reimbursement.setBillingRemarks(remarksForClaimRequest);
		} else if(reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
			reimbursement.setCancellationRemarks(remarksForClaimRequest);
			reimbursement.setCancellationReasonId(preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason().getId() : null);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			resetReimbursementBenefitAmount(reimbursement);
		}
		reimbursement.setBillingApprovedAmount(preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getInitialTotalApprovedAmt() != null ? preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getInitialTotalApprovedAmt() : 0d);
		if(preauthDTO.getStatusKey() != null && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
			reimbursement.setBillingCompletedDate(new Timestamp(System
					.currentTimeMillis()));
		}
		
		Double provisionAmt = 0d;
		if (preauthDTO.getHospitalizaionFlag()
				|| preauthDTO.getIsHospitalizationRepeat()
				|| preauthDTO.getPartialHospitalizaionFlag()) {
			if(preauthDTO.getHospitalizaionFlag()) {
				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
						.getAfterHospitalDiscount() != null ? preauthDTO
						.getHospitalizationCalculationDTO()
						.getAfterHospitalDiscount() : 0d;
				if(preauthDTO.getClaimDTO() != null && preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
					if(preauthDTO.getIsReconsiderationRequest() != null && !preauthDTO.getIsReconsiderationRequest()) {
						provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
								.getPayableToInsuredAftPremiumAmt() != null ? preauthDTO
								.getHospitalizationCalculationDTO()
								.getPayableToInsuredAftPremiumAmt() : 0d;
					}
					
				}
			}
			
					if(preauthDTO.getIsReconsiderationRequest() != null && preauthDTO.getIsReconsiderationRequest()) {
						provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
								.getBalanceToBePaid() != null ? preauthDTO
								.getHospitalizationCalculationDTO()
								.getBalanceToBePaid() : 0d;
					} else {
						if(preauthDTO.getPartialHospitalizaionFlag() || preauthDTO.getIsHospitalizationRepeat()) {
							provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
									.getPayableToInsuredAftPremiumAmt() != null ? preauthDTO
									.getHospitalizationCalculationDTO()
									.getPayableToInsuredAftPremiumAmt() : 0d;
						}
						
					}
			
		}
			provisionAmt += preauthDTO.getPreHospitalizationCalculationDTO()
					.getPayableToInsAmt() != null ? preauthDTO
					.getPreHospitalizationCalculationDTO().getPayableToInsAmt()
					.doubleValue() : 0d;
			provisionAmt += preauthDTO.getPostHospitalizationCalculationDTO()
					.getPayableToInsAmt() != null ? preauthDTO
					.getPostHospitalizationCalculationDTO()
					.getPayableToInsAmt().doubleValue() : 0d;
		
		provisionAmt += preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() : 0d;
		provisionAmt += preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() : 0d;
		// List<ReimbursementBenefits> reimbursementBenefitsByRodKey =
		// getReimbursementBenefitsByRodKey(reimbursement.getKey());
		// if(reimbursementBenefitsByRodKey != null) {
		// for (ReimbursementBenefits reimbursementBenefits :
		// reimbursementBenefitsByRodKey) {
		// provisionAmt += reimbursementBenefits.getTotalClaimAmountBills()
		// != null ? reimbursementBenefits.getTotalClaimAmountBills() : 0d;
		// }
		// }
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER) || preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)){
			if(reimbursement.getCurrentProvisionAmt() != null){
				preauthDTO.setRevisedProvisionAmount(reimbursement.getCurrentProvisionAmt());
			}
		}
		
		Double excludingAddOnBenefits = preauthDTO.getRevisedProvisionAmount() - (preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() :0d)  - (preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() : 0d);
		Double revisedProvision = Math.min(excludingAddOnBenefits, preauthDTO.getBalanceSI());
		
		revisedProvision += preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() : 0d;
		revisedProvision += preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() :0d;
		
		//Below condition for IMSSUPPOR-29565 - Scenario - If Billing Approved Approved Amt is zero, current provision updated as zero 
				if(preauthDTO.getStatusKey() != null && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)){
					if(revisedProvision != null && revisedProvision > 0){
						reimbursement.setCurrentProvisionAmt(revisedProvision);
					}
				} else {
					reimbursement.setCurrentProvisionAmt(revisedProvision);
				}

		
		reimbursement.setBillingApprovedAmount(revisedProvision);
		
		if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getShouldDetectPremium()) {
			Double amt = preauthDTO.getRevisedProvisionAmount() - preauthDTO.getConsolidatedAmtDTO().getHospPayableAmt();
			if(preauthDTO.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) {
				amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getAfterHospitalDiscount());
			} else {
				amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getPreauthAppAmt());
			}
			reimbursement.setCurrentProvisionAmt(amt);
		}
		
//		Double totalClaimedAmount = getTotalClaimedAmount(reimbursement);
		
		if( ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey())) {
			
			if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) ) {
				
//				 reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
				getProivsioningForCancelROD(preauthDTO, reimbursement);
//				 reimbursement.setCurrentProvisionAmt(totalClaimedAmount);
			} else {
				if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
					Preauth latestPreauthByClaim = getLatestPreauthByClaim(preauthDTO.getClaimKey());
					reimbursement.setCurrentProvisionAmt(latestPreauthByClaim.getTotalApprovalAmount());
				} else {
					 reimbursement.setCurrentProvisionAmt(0d);
					 reimbursement.setOtherBenefitApprovedAmt(0d);
//					reimbursement.setCurrentProvisionAmt(totalClaimedAmount);
				}
				
			}
		}
		
		Reimbursement reimb = getReimbursementByKey(reimbursement.getKey());
		if(null != reimb)
		{
			reimbursement.setProcessClaimType(reimb.getProcessClaimType());
		
		}
		
		return reimbursement;
		
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
	
	private Reimbursement updateClaimFinancialRemarks(PreauthDTO preauthDTO, Reimbursement reimbursement,Claim claim){
		
		String remarksForClaimRequest = getRemarksForClaimFinancial(
				reimbursement.getStatus().getKey(), preauthDTO,
				reimbursement);
		
		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
			reimbursement
					.setFinancialApprovalRemarks(remarksForClaimRequest);
			reimbursement.setFinancialCompletedDate(new Timestamp(System
					.currentTimeMillis()));
		} else if(reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
			reimbursement.setCancellationRemarks(remarksForClaimRequest);
			reimbursement.setCancellationReasonId(preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason().getId() : null);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			resetReimbursementBenefitAmount(reimbursement);
		}
		
			reimbursement.setFinancialApprovedAmount(preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getInitialTotalApprovedAmt() != null ? preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInitialTotalApprovedAmt() : 0d);
			reimbursement.setBillingApprovedAmount(preauthDTO
					.getBillingApprovedAmount() != null ? preauthDTO
							.getBillingApprovedAmount() : 0d);
		
		
		Double provisionAmt = 0d;
		if (preauthDTO.getHospitalizaionFlag()
				|| preauthDTO.getIsHospitalizationRepeat()
				|| preauthDTO.getPartialHospitalizaionFlag()) {
			if(preauthDTO.getHospitalizaionFlag()) {
				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
						.getAfterHospitalDiscount() != null ? preauthDTO
						.getHospitalizationCalculationDTO()
						.getAfterHospitalDiscount() : 0d;
						if(preauthDTO.getClaimDTO() != null && preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
							if(preauthDTO.getIsReconsiderationRequest() != null && !preauthDTO.getIsReconsiderationRequest()) {
								provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
										.getPayableToInsuredAftPremiumAmt() != null ? preauthDTO
										.getHospitalizationCalculationDTO()
										.getPayableToInsuredAftPremiumAmt() : 0d;
							}
						}
			}
			
					if(preauthDTO.getIsReconsiderationRequest() != null && preauthDTO.getIsReconsiderationRequest()) {
						provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
								.getBalanceToBePaid() != null ? preauthDTO
								.getHospitalizationCalculationDTO()
								.getBalanceToBePaid() : 0d;
					} else {
						if(preauthDTO.getPartialHospitalizaionFlag() || preauthDTO.getIsHospitalizationRepeat()) {
							provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
									.getPayableToInsuredAftPremiumAmt() != null ? preauthDTO
									.getHospitalizationCalculationDTO()
									.getPayableToInsuredAftPremiumAmt() : 0d;
						}
						
					}
			
		}
			provisionAmt += preauthDTO.getPreHospitalizationCalculationDTO()
					.getPayableToInsAmt() != null ? preauthDTO
					.getPreHospitalizationCalculationDTO().getPayableToInsAmt()
					.doubleValue() : 0d;
			provisionAmt += preauthDTO.getPostHospitalizationCalculationDTO()
					.getPayableToInsAmt() != null ? preauthDTO
					.getPostHospitalizationCalculationDTO()
					.getPayableToInsAmt().doubleValue() : 0d;
		
		provisionAmt += preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() : 0d;
		provisionAmt += preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() : 0d;
//		Double revisedProvision = Math.min(provisionAmt, preauthDTO.getBalanceSI());
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) || preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER) || preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY) || preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS) || (ReferenceTable.FINANCIAL_QUERY_STATUS.equals(preauthDTO.getStatusKey()))){
			if(reimbursement.getCurrentProvisionAmt() != null){
				preauthDTO.setRevisedProvisionAmount(reimbursement.getCurrentProvisionAmt());
			}
		}
			
		Double excludingAddOnBenefits = preauthDTO.getRevisedProvisionAmount() - (preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() :0d)  - (preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() : 0d);
		
		Double revisedProvision = 0d;
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
				revisedProvision = Math.min(excludingAddOnBenefits, preauthDTO.getBalanceSI() + preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim());
			}else{
				revisedProvision = Math.min(excludingAddOnBenefits, preauthDTO.getBalanceSI());
			}
			
		}else{
			revisedProvision = Math.min(excludingAddOnBenefits, preauthDTO.getBalanceSI());
		}
		
		
		
		revisedProvision += preauthDTO.getPatientCareAmt() != null ? preauthDTO.getPatientCareAmt() : 0d;
		revisedProvision += preauthDTO.getHospitalCashAmt() != null ? preauthDTO.getHospitalCashAmt() : 0d;
		
		reimbursement.setPremiumAmt(0d);
		
		//Below condition for IMSSUPPOR-29565 - Scenario - If FA Approved Amt is zero, current provision updated as zero GALAXYMAIN-13433 - for refer to specialist
		if(preauthDTO.getStatusKey() != null && (preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST))){
			if(revisedProvision != null && revisedProvision > 0){
					reimbursement.setCurrentProvisionAmt(revisedProvision);
			}
		} else {
					reimbursement.setCurrentProvisionAmt(revisedProvision);
		}
		
		reimbursement.setFinancialApprovedAmount(revisedProvision);
		if((preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY))) {
			if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				reimbursement.setFinancialApprovedAmount(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium().doubleValue());
				reimbursement.setPremiumAmt(preauthDTO.getConsolidatedAmtDTO().getPremiumAmt() != null ? preauthDTO.getConsolidatedAmtDTO().getPremiumAmt().doubleValue() : 0d);
				reimbursement.setFinancialApprovedAmount(revisedProvision);
			}
		
			if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getShouldDetectPremium()) {
				Double amt = preauthDTO.getRevisedProvisionAmount() - preauthDTO.getConsolidatedAmtDTO().getHospPayableAmt();
				if(preauthDTO.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) {
					amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getAfterHospitalDiscount());
				} else {
					amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getPreauthAppAmt());
				}
				reimbursement.setCurrentProvisionAmt(amt);
				reimbursement.setPremiumAmt(preauthDTO.getUniquePremiumAmount());
			}
		}else if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) {
			if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				reimbursement.setFinancialApprovedAmount(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium().doubleValue());
				reimbursement.setPremiumAmt(preauthDTO.getConsolidatedAmtDTO().getPremiumAmt() != null ? preauthDTO.getConsolidatedAmtDTO().getPremiumAmt().doubleValue() : 0d);
				// UnCommented by Pavithran 01/10/2020 support fix 33105 
				reimbursement.setFinancialApprovedAmount(revisedProvision); // - revisedProvision is not deducting premiumAmt hence commented - 03-06-2020
//				//reimbursement.setFinancialApprovedAmount(revisedProvision); - revisedProvision is not deducting premiumAmt hence commented - 03-06-2020
//				reimbursement.setFinancialApprovedAmount(revisedProvision);//un commented by noufel since we are getting LOt payment amount as zero  //  - revisedProvision is not deducting premiumAmt hence commented - 03-06-2020
			}
		
			if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getShouldDetectPremium()) {
				Double amt = preauthDTO.getRevisedProvisionAmount() - preauthDTO.getConsolidatedAmtDTO().getHospPayableAmt();
				if(preauthDTO.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) {
					amt += (preauthDTO.getPolicyInstalmentPremiumAmt() + preauthDTO.getHospitalizationCalculationDTO().getAfterHospitalDiscount());
				} else {
					amt += (preauthDTO.getPolicyInstalmentPremiumAmt() + preauthDTO.getHospitalizationCalculationDTO().getPreauthAppAmt());
				}
				reimbursement.setCurrentProvisionAmt(amt);
				reimbursement.setPremiumAmt(preauthDTO.getPolicyInstalmentPremiumAmt());
			}
		}
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getShouldDetectPremium()) {
			Double amt = preauthDTO.getRevisedProvisionAmount() - preauthDTO.getConsolidatedAmtDTO().getHospPayableAmt();
			if(preauthDTO.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) {
				amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getAfterHospitalDiscount());
			} else {
				amt += (preauthDTO.getUniquePremiumAmount() + preauthDTO.getHospitalizationCalculationDTO().getPreauthAppAmt());
			}
			reimbursement.setCurrentProvisionAmt(amt);
		}else if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getShouldDetectPremium()) {
			Double amt = preauthDTO.getRevisedProvisionAmount() - preauthDTO.getConsolidatedAmtDTO().getHospPayableAmt();
			if(preauthDTO.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) {
				amt += (preauthDTO.getPolicyInstalmentPremiumAmt() + preauthDTO.getHospitalizationCalculationDTO().getAfterHospitalDiscount());
			} else {
				amt += (preauthDTO.getPolicyInstalmentPremiumAmt() + preauthDTO.getHospitalizationCalculationDTO().getPreauthAppAmt());
			}
			//Condition added for wrong provision amt setting - 02-06-2020
			if(preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && !preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				reimbursement.setCurrentProvisionAmt(amt);
			}
		}
		
		
//		reimbursement.setCurrentProvisionAmt(provisionAmt);
//		reimbursement.setFinancialApprovedAmount(provisionAmt);
		
//		Double totalClaimedAmount = getTotalClaimedAmount(reimbursement);
		
		if( ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey())) {
			if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) ) {
//				 reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
				getProivsioningForCancelROD(preauthDTO, reimbursement);
			} else {
				if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
					Preauth latestPreauthByClaim = getLatestPreauthByClaim(preauthDTO.getClaimKey());
					reimbursement.setCurrentProvisionAmt(latestPreauthByClaim.getTotalApprovalAmount());
				} else {
					reimbursement.setCurrentProvisionAmt(0d);
					reimbursement.setOtherBenefitApprovedAmt(0d);
				}
			}
		}        
		
		Reimbursement reimb = getReimbursementByKey(reimbursement.getKey());
		if(null != reimb)
		{
			reimbursement.setProcessClaimType(reimb.getProcessClaimType());
		
		}
//		reimbursement = getProivsioningForCancelROD(preauthDTO, reimbursement);
		
//		if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) ) {
//			if(preauthDTO.getHospitalizaionFlag()) {
//				Preauth latestPreauthByClaim = getLatestPreauthByClaim(preauthDTO.getClaimKey());
//				reimbursement.setCurrentProvisionAmt(latestPreauthByClaim.getTotalApprovalAmount());
//			} else if(!preauthDTO.getHospitalizaionFlag()) {
//				reimbursement.setCurrentProvisionAmt(0d);
//			}
//			
//		} 
		
		return reimbursement;
	}
/*
	public void setBPMOutcome(PreauthDTO dto, Boolean isZonalReview,
			String outCome,Reimbursement reimbursement) {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) dto.getDbOutArray();
		if (isZonalReview) {
			SubmitProcessClaimRequestZMRTask claimRequestZMRTask = BPMClientContext
					.getClaimRequestZonalReview(dto.getStrUserName(),
							dto.getStrPassword());
			
			

			HumanTask humanTask = dto.getRodHumanTask();
			
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)) {
				PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				//humanTask.setOutcome(outCome);
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				//humanTask.setPayload(payloadForCancelROD);
			} else {
				
				PayloadBOType payloadBO = humanTask.getPayload();

				PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();

				if (paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				}
				paymentInfo.setApprovedAmount(dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() != null ? dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() : 0d);
				paymentInfo
						.setClaimedAmount(dto.getAmountConsidered() != null ? SHAUtils
								.getDoubleValueFromString(dto.getAmountConsidered())
								: 0d);
				Double claimedAmount = dto.getAmountConsidered() != null ? SHAUtils
						.getDoubleValueFromString(dto.getAmountConsidered())
						: 0d;
						wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);
				
				//payloadBO.setPaymentInfo(paymentInfo);
				ClaimRequestType claimType = null;

				if (payloadBO.getClaimRequest() != null) {
					claimType = payloadBO.getClaimRequest();
				} else {
					claimType = new ClaimRequestType();
				}

				if (dto.getStatusKey() != null
						&& dto.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS)) {
					//claimType.setReimbReqBy("ZMA");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
				}
				
				if(payloadBO.getProcessActorInfo() != null){
					ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();
//					processActorInfo.setEscalatedByUser(dto.getStrUserName());
					if(processActorInfo.getEscalatedByUser() == null){
						//processActorInfo.setEscalatedByUser("");
						//payloadBO.setProcessActorInfo(processActorInfo);
					}
//					payloadBO.setProcessActorInfo(processActorInfo);
				}else{
					ProcessActorInfoType processActor = payloadBO.getProcessActorInfo();
					if(processActor == null){
						processActor = new ProcessActorInfoType();
					}
				//	processActor.setEscalatedByUser(dto.getStrUserName());
					//payloadBO.setProcessActorInfo(processActor);
				}
				
				if(reimbursement.getStage() != null){
					//claimType.setOption(reimbursement.getStage().getStageName());
				}

				humanTask.setOutcome(outCome);
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				//claimType.setResult(outCome);
				//payloadBO.setClaimRequest(claimType);

				//RODType rodType = new RODType();
				wrkFlowMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, dto.getRodNumber());
//				rodType.setAckNumber(dto.getRodNumber());
				wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY, dto.getKey());
				//rodType.setKey(dto.getKey());
			//	payloadBO.setRod(rodType);

				CustomerType custType = new CustomerType();
				if (dto.getPreauthDataExtractionDetails().getTreatmentType() != null) {
					custType.setTreatmentType(dto.getPreauthDataExtractionDetails()
							.getTreatmentType().getValue());
					wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, dto.getPreauthDataExtractionDetails()
							.getTreatmentType().getValue());
				} else {
					custType.setTreatmentType("");
					wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, "");
				}

				//custType.setSpeciality(dto.getSpecialityName());
				wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getSpecialityName());
				payloadBO.setCustomer(custType);
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					
					ClassificationType classification = payloadBO.getClassification();
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.ZMR_STAGE_NAME);
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						//classification.setSource(stage.getStageName());

					}
					
					//payloadBO.setClassification(classification);
				}
			
				
				//humanTask.setPayload(payloadBO);
			}
			try{
			//ClaimRequestZMRTask.execute(dto.getStrUserName(), humanTask);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
				
//				CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//				cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
				
			}
			
			}catch(Exception e){
				log.error(e.toString());
				e.printStackTrace();
				
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN ZONAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR ZONAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					
					BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					claimRequestZMRTask.execute("claimshead", humanTask);

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR IN ZONAL (#*#&*#*#*#*#*#*#");
				}
				
				
			}
		} else {
			SubmitProcessClaimRequestTask claimRequestTask = BPMClientContext
					.getClaimRequest(dto.getStrUserName(), dto.getStrPassword());

			HumanTask humanTask = dto.getRodHumanTask();

			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)) {
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				
				humanTask.setOutcome(outCome);
				humanTask.setPayload(payloadForCancelROD);
			} else {

				ClaimType claims = new ClaimType();

				ClaimRequestType claimType = null;

				PayloadBOType payloadBO = humanTask.getPayload();
				
				CustomerType customer = payloadBO.getCustomer();

				PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
				
				ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();
				if(processActorInfo == null){
					//processActorInfo = new ProcessActorInfoType();
					
				}
				//processActorInfo.setEscalatedByRole("");
				//processActorInfo.setEscalatedByUser(dto.getStrUserName());
				//payloadBO.setProcessActorInfo(processActorInfo);

				if (paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				}
				paymentInfo.setApprovedAmount(dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() != null ? dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() : 0d);
				paymentInfo
						.setClaimedAmount(dto.getAmountConsidered() != null ? SHAUtils
								.getDoubleValueFromString(dto.getAmountConsidered())
								: 0d);
				Double claimedAmount = dto.getAmountConsidered() != null ? SHAUtils
						.getDoubleValueFromString(dto.getAmountConsidered())
						: 0d; 
				
				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);

				if (payloadBO.getClaimRequest() != null) {
					claimType = payloadBO.getClaimRequest();
				} else {
					claimType = new ClaimRequestType();
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
					//claimType.setClientType("MEDICAL");
					//payloadBO.setInvestigation(null);
					//payloadBO.setQuery(null);
				//	FieldVisitType fieldVisit = new FieldVisitType();
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					
					if(dto.getNewIntimationDTO().getAdmissionDate() != null){
						String intimDate = SHAUtils.formatIntimationDateValue(dto.getNewIntimationDTO().getAdmissionDate());
						RRCType rrc = payloadBO.getRrc();
						if(rrc == null){
							rrc = new RRCType();
						}
						
						//rrc.setFromDate(intimDate);
						//payloadBO.setRrc(rrc);
					}
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
							wrkFlowMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							wrkFlowMap.put(SHAConstants.CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							//fieldVisit.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							//claimType.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							}
						}
					}
					
					if (dto.getFvrKey() != null) {
						wrkFlowMap.put(SHAConstants.FVR_KEY, dto.getFvrKey());
						fieldVisit.setKey(dto.getFvrKey());
						payloadBO.setFieldVisit(fieldVisit);
					}

				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
					//claimType.setClientType("MEDICAL");
					//payloadBO.setInvestigation(null);
					//payloadBO.setQuery(null);
					//payloadBO.setFieldVisit(null);
					if (claimType != null) {
						claimType.setReimbReqBy(null);
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {
					claimType.setClientType("MEDICAL");
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					QueryType queryType = new QueryType();
					if (dto.getQueryKey() != null) {
						wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY, dto.getQueryKey());
						//queryType.setKey(dto.getQueryKey());
						//queryType.setIsQueryPending(true);
						//queryType.setIsQueryReplyReceived(false);
						//payloadBO.setQuery(queryType);
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimType.setClientType("MEDICAL");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
					//claimType.setReimbReqBy("MEDICAL");
					InvestigationType investigation = new InvestigationType();
					if (dto.getInvestigationKey() != null) {
						//investigation.setKey(dto.getInvestigationKey());
						wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, dto.getInvestigationKey());
						//payloadBO.setInvestigation(investigation);
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimType.setClientType("MEDICAL");
					claimType.setReimbReqBy("MEDICAL");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimType.setClientType("MEDICAL");
				//	claimType.setReimbReqBy("MEDICAL");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
					if (dto.getPreauthMedicalDecisionDetails().getSpecialistType() != null) {
//						claimType.setReferTo(dto.getPreauthMedicalDecisionDetails()
//								.getSpecialistType().getValue());
						if(customer == null){
							customer = new CustomerType();
						}
						if(dto.getPreauthMedicalDecisionDetails().getSpecialistType() != null){
							customer.setSpeciality(dto.getPreauthMedicalDecisionDetails()
									.getSpecialistType().getValue());
							payloadBO.setCustomer(customer);
						}
						claimType.setReferTo(dto.getStrUserName());
						//claimType.setReimbReqBy("MEDICAL");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
					}

				}

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
					//claimType.setReimbReqBy("ESCALATE");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
					if(outCome.equalsIgnoreCase("SPECIALIST")){
						if(dto.getStrUserName() != null){
//							TmpEmployee employeeByName = getEmployeeByName(dto.getStrUserName());
							
						}
					}
				}
				
				if(dto.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)){
					//claimType.setReimbReqBy("MEDICAL");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
				}
				
				
				//claimType.setResult(outCome);
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
			//	humanTask.setOutcome(outCome);
				
				if(reimbursement.getStage() != null){
				//	claimType.setOption(reimbursement.getStage().getStageName());
				}
				
				//payloadBO.setClaimRequest(claimType);

//				payloadBO.getProductInfo().setProductName("MCA");

				if (("COORDINATOR".equalsIgnoreCase(claimType.getResult())
						|| "INVESTIGATION".equalsIgnoreCase(claimType.getResult())
						|| "FVR".equalsIgnoreCase(claimType.getResult())
						|| "SPECIALIST".equalsIgnoreCase(claimType.getResult())
						|| "QUERY".equalsIgnoreCase(claimType.getResult())
						|| "REJECT".equalsIgnoreCase(claimType.getResult())
						|| "RMA1".equalsIgnoreCase(claimType
								.getResult())
						|| "RMA2".equalsIgnoreCase(claimType
								.getResult()) 
						|| "RMA3".equalsIgnoreCase(claimType.getResult())
						|| "RMA4".equalsIgnoreCase(claimType.getResult())
						|| "RMA5".equalsIgnoreCase(claimType.getResult())
						|| "RMA6".equalsIgnoreCase(claimType.getResult()))
						
						&& !"BILLING".equalsIgnoreCase(claimType.getReimbReqBy())
						&& !"FA".equalsIgnoreCase(claimType.getReimbReqBy())) {
					if(dto.getPreauthMedicalDecisionDetails().getRMA1()){
						//claimType.setReimbReqBy("RMA1");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 1);
					}else if(dto.getPreauthMedicalDecisionDetails().getRMA2()){
						//claimType.setReimbReqBy("RMA2");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 2);
					}
					else if(dto.getPreauthMedicalDecisionDetails().getRMA3()){
						//claimType.setReimbReqBy("RMA3");	
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 3);
					}
					else if(dto.getPreauthMedicalDecisionDetails().getRMA4()){
					//	claimType.setReimbReqBy("RMA4");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 4);
					}
					else if(dto.getPreauthMedicalDecisionDetails().getRMA5()){
						//claimType.setReimbReqBy("RMA5");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 5);
					}
					else if(dto.getPreauthMedicalDecisionDetails().getRMA6()){
						//claimType.setReimbReqBy("RMA6");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 6);
					}else{
						//claimType.setReimbReqBy("MEDICAL_APPROVER");
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, 7);
					}
					
				}

				RODType rodType = new RODType();
				rodType.setAckNumber(dto.getRodNumber());
				rodType.setKey(dto.getKey());
				payloadBO.setRod(rodType);
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					
					ClassificationType classification = payloadBO.getClassification();
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.ZMR_STAGE_NAME);
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}
					
					payloadBO.setClassification(classification);
					
				}
				
				
				humanTask.setPayload(payloadBO);
			}
			try{
			//claimRequestTask.execute(dto.getStrUserName(), humanTask);
			
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
				
//				CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//				cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
				
			}
			
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.toString());
				
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR MA----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					//BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					//claimRequestTask.execute("claimshead", humanTask);

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
				}
			}
		}
		// HumanTask humanTask = dto.getHumanTask();

	}*/

	public TmpEmployee getEmployeeDetails(String loginId) {
		TmpEmployee tmpEmployee = null;
		/*
		 * Query query = entityManager
		 * .createNamedQuery("TmpEmployee.findByEmpName"); query =
		 * query.setParameter("empName", empName);
		 */
		Query query = entityManager
				.createNamedQuery("TmpEmployee.getEmpByLoginId");// .setParameter("primaryKey",
																	// key);
		if(null != loginId)
		{
			query.setParameter("loginId", "%" + loginId.toLowerCase() + "%");	
		}
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		/*for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
			tmpEmployee = tmpEmployee2;
		}*/
		
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		
		// TmpEmployee tmpEmployee = (TmpEmployee)query.getSingleResult();
		return tmpEmployee;
	}
	
	
	public TmpEmployee getEmployeeByName(String userName){
		
		TmpEmployee tmpEmployee = null;
		
		userName = userName.toLowerCase();
		
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpName");
		query.setParameter("empName", userName);
		
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		/*for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
			tmpEmployee = tmpEmployee2;
		}*/
		
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		
		return tmpEmployee;
		
		
	}

	public List<ReimbursementBenefits> getReimbursementBenefitsByRodKey(
			Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKey");
		query = query.setParameter("rodKey", rodKey);

		List<ReimbursementBenefits> reimbBenefits = query.getResultList();
		if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
			return reimbBenefits;
		}

		return reimbBenefits;
	}
	
	public List<BillItemMapping> getBillItemMappingByRODKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("BillItemMapping.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);

		List<BillItemMapping> billItemMapping = query.getResultList();
		if (billItemMapping != null && !billItemMapping.isEmpty()) {
			return billItemMapping;
		}
		return null;
	}

	public Long getReimbursementCalcByRodAndClassificationKey(Long rodKey,
			Long classificatonKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodAndBillClassificationKey");
		query.setParameter("reimbursementKey", rodKey);
		query.setParameter("billClassificationKey", classificatonKey);

		List<ReimbursementCalCulationDetails> reimbBenefits = query
				.getResultList();
		if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
			return reimbBenefits.get(0).getKey();
		}
		return null;
	}
	
	public ReimbursementCalCulationDetails getReimbursementCalcObjByRodAndClassificationKey(Long rodKey, Long classificatonKey) {
		if(rodKey != null) {
			Query query = entityManager
					.createNamedQuery("ReimbursementCalCulationDetails.findByRodAndBillClassificationKey");
			query.setParameter("reimbursementKey", rodKey);
			query.setParameter("billClassificationKey", classificatonKey);
			
			List<ReimbursementCalCulationDetails> reimbBenefits = query.getResultList();
			if(reimbBenefits != null && !reimbBenefits.isEmpty()) {
				entityManager.refresh(reimbBenefits.get(0));
				return reimbBenefits.get(0);
			}else{
				return null;
			}
		} else {
			return null;
		}
	}	
	
	public List<ReimbursementCalCulationDetails> getReimbursementCalcObjByRodKey(Long rodKey) {
		if(rodKey != null) {
			Query query = entityManager
					.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
			query.setParameter("reimbursementKey", rodKey);
			
			List<ReimbursementCalCulationDetails> reimbBenefits = query.getResultList();
			if(reimbBenefits != null && !reimbBenefits.isEmpty()) {
				for (ReimbursementCalCulationDetails reimbursementCalCulationDetails : reimbBenefits) {
					entityManager.refresh(reimbursementCalCulationDetails);
				}
				
				return reimbBenefits;
			}else{
				return null;
			}
		} else {
			return null;
		}
	}	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitClaimRequest(PreauthDTO preauthDTO) throws Exception {
		//try {
			log.info("Submit Claim Request ---------------" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
			
			
			
			if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){	
				preauthDTO.setMedicalCompletedDate(new Timestamp(System
						.currentTimeMillis()));
			}
			
			Reimbursement reimbursement = savePreauthValues(preauthDTO, false,SHAConstants.CLAIM_REQUEST);

			if (reimbursement.getClaim() != null) {
				Claim currentClaim = searchByClaimKey(reimbursement.getClaim()
						.getKey());
				if (currentClaim != null) {
//					currentClaim.setStatus(reimbursement.getStatus());
//					currentClaim.setStage(reimbursement.getStage());
					if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
						currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
					}
					currentClaim.setDataOfAdmission(reimbursement.getDateOfAdmission());
					if(reimbursement.getDateOfDischarge() != null){
						currentClaim.setDataOfDischarge(reimbursement.getDateOfDischarge());
					}
					// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
					if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
						currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
						currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
						currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
					}
					
					//added for saving priority in workflow for covid claims
					if(reimbursement.getBillingAutoAllocIcdFlag() != null && reimbursement.getBillingAutoAllocIcdFlag().equalsIgnoreCase("C")){
						Map<String, Object> getPriorityEvent =	dbCalculationService.getPriorityFlag(reimbursement.getClaim().getKey(),SHAConstants.COVID);
						if(getPriorityEvent != null){
							if(getPriorityEvent.containsKey("priorityWeightage")){
								currentClaim.setPriorityWeightage((Long) getPriorityEvent.get("priorityWeightage")); 
							}
							if(getPriorityEvent.containsKey("priorityEvent") && !((String) getPriorityEvent.get("priorityEvent")).isEmpty()){
								currentClaim.setPriorityEvent((String) getPriorityEvent.get("priorityEvent")); 
							}
						}
					}
					entityManager.merge(currentClaim);
					entityManager.flush();
					entityManager.clear();
					log.info("------CurrentClaim------>"+currentClaim+"<------------");
				}
			}

//			String remarksForClaimRequest = getRemarksForClaimRequest(
//					reimbursement.getStatus().getKey(), preauthDTO,
//					reimbursement);
//
//			if (reimbursement.getStatus().getKey()
//					.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
//				reimbursement.setRejectionRemarks(remarksForClaimRequest);
//			} else if (reimbursement.getStatus().getKey()
//					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
//				reimbursement.setApprovalRemarks(remarksForClaimRequest);
//
//			}
			
			

//			Double provisionAmt = 0d;
//			provisionAmt += reimbursement.getApprovedAmount() != null ? reimbursement
//					.getApprovedAmount() : 0d;
//			if (reimbursement.getDocAcknowLedgement() != null) {
//				if (reimbursement.getDocAcknowLedgement()
//						.getPreHospitalizationClaimedAmount() != null) {
//					provisionAmt += reimbursement.getDocAcknowLedgement()
//							.getPreHospitalizationClaimedAmount();
//				}
//				if (reimbursement.getDocAcknowLedgement()
//						.getPostHospitalizationClaimedAmount() != null) {
//					provisionAmt += reimbursement.getDocAcknowLedgement()
//							.getPostHospitalizationClaimedAmount();
//				}
//			}
//
//			List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement
//					.getKey());
//			if (reimbursementBenefitsByRodKey != null) {
//				for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
//					provisionAmt += reimbursementBenefits
//							.getTotalClaimAmountBills() != null ? reimbursementBenefits
//							.getTotalClaimAmountBills() : 0d;
//
//				}
//			}
//			reimbursement.setCurrentProvisionAmt(provisionAmt);
//			entityManager.merge(reimbursement);
//			entityManager.flush();

			List<MedicalVerificationDTO> medicalVerificationTableDTO = preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getMedicalVerificationTableDTO();
			ClaimVerification verification = null;
			for (MedicalVerificationDTO medicalVerificationDTO : medicalVerificationTableDTO) {
				verification = new ClaimVerification();
				verification.setReimbursement(reimbursement);
				verification.setKey(medicalVerificationDTO.getKey());
				verification.setStage(reimbursement.getStage());
				verification.setStatus(reimbursement.getStatus());
				verification.setVerificationTypeId(medicalVerificationDTO
						.getDescriptionId());
				verification.setVerificationType("Medical");
				verification.setVerifiedFlag(medicalVerificationDTO
						.getVerifiedFlag());
				verification.setMedicalRemarks(medicalVerificationDTO
						.getRemarks());

				if (verification.getKey() == null) {
					entityManager.persist(verification);
				} else {
					entityManager.merge(verification);
				}
				log.info("------ClaimVerification------>"+verification+"<------------");
			}
			entityManager.flush();

			List<TreatmentQualityVerificationDTO> treatmentVerificationTableDTO = preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getTreatmentVerificationDTO();
			for (TreatmentQualityVerificationDTO dto : treatmentVerificationTableDTO) {
				verification = new ClaimVerification();
				verification.setReimbursement(reimbursement);
				verification.setKey(dto.getKey());
				verification.setStage(reimbursement.getStage());
				verification.setStatus(reimbursement.getStatus());
				verification.setVerificationTypeId(dto.getDescriptionId());
				verification.setVerificationType("Treatment");
				verification.setVerifiedFlag(dto.getVerifiedFlag());
				verification.setMedicalRemarks(dto.getRemarks());

				if (verification.getKey() == null) {
					entityManager.persist(verification);
				} else {
					entityManager.merge(verification);
				}
				log.info("------ClaimVerification------>"+verification+"<------------");
			}
			entityManager.flush();
			entityManager.clear();

			/*List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO
					.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
				List<FVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2
						.getFvrGradingDTO();
				FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2
						.getKey());
				for (FVRGradingDTO fvrGradingDTO2 : fvrGradingDTO) {

					switch (Integer.valueOf(String.valueOf(fvrGradingDTO2
							.getKey()))) {
					case 8:
						fvrByKey.setPatientVerified(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 9:
						fvrByKey.setDiagnosisVerfied(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 10:
						fvrByKey.setRoomCategoryVerfied(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 11:
						fvrByKey.setTriggerPointsFocused(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 12:
						fvrByKey.setPedVerified(fvrGradingDTO2.getStatusFlag());
						break;
					case 13:
						fvrByKey.setPatientDischarged(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 14:
						fvrByKey.setPatientNotAdmitted(fvrGradingDTO2
								.getStatusFlag());
						break;
					case 15:
						fvrByKey.setOutstandingFvr(fvrGradingDTO2
								.getStatusFlag());
						break;
					default:
						break;
					}
				}
                fvrByKey.setFvrGradingDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(fvrByKey);
				entityManager.flush();
				entityManager.clear();
			}*/

			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
					|| reimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
				ResidualAmount residualAmt = new ResidualAmount();
				residualAmt.setKey(preauthDTO.getResidualAmountDTO().getKey());
				residualAmt.setTransactionKey(reimbursement.getKey());
				residualAmt.setStage(reimbursement.getStage());
				residualAmt.setStatus(reimbursement.getStatus());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setAmountConsideredAmount(preauthDTO
						.getResidualAmountDTO().getAmountConsideredAmount());
				residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO()
						.getMinimumAmount());
				residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO()
						.getCopayAmount());
				residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO()
						.getApprovedAmount());
				residualAmt.setCopayPercentage(preauthDTO
						.getResidualAmountDTO().getCopayPercentage());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO()
						.getNetAmount() != null ? preauthDTO
						.getResidualAmountDTO().getNetAmount() : 0d);
				residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d);
				
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
				log.info("------ResidualAmount------>"+residualAmt+"<------------");
			}
			
			// below line added for saving the values should be zero if rejection is happened.
			if(null != preauthDTO.getStatusKey() && (/*preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) ||*/ ReferenceTable.PAYMENT_SETTLED.equals(preauthDTO.getStatusKey()))){
				List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = getReimbursementCalculationDetails(preauthDTO.getKey());
				if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
					saveCalculationValues(preauthDTO, reimbursement, /*preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) ||*/ ReferenceTable.PAYMENT_SETTLED.equals(preauthDTO.getStatusKey()) ? true : false);
				}
			}
			
			String outcome = "";
			Boolean isFvrOrInvesInitiated = false;
			
			if(null != preauthDTO.getClaimDTO().getClaimType() && 
					preauthDTO.getClaimDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE)){
				
				Boolean investigationAvailable = getInvestigationPendingForClaim(preauthDTO.getClaimKey(),SHAConstants.TRANSACTION_FLAG_CASHLESS,preauthDTO);
				if(investigationAvailable){
					preauthDTO.setIsParallelInvFvrQuery(investigationAvailable);
				}
			}
			
			 if(null != preauthDTO.getIsParallelInvFvrQuery() && !(preauthDTO.getIsParallelInvFvrQuery())){
				
				outcome = getOutcomeForClaimRequest(reimbursement,
						preauthDTO);
			}
			else
			{
				
				isFvrOrInvesInitiated = true;
				
				outcome = getOutcomeForClaimRequestWaitForInput(reimbursement,preauthDTO);
			}
			if(null != preauthDTO.getStatusKey() && !(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
				submitFVRGradingDetail(preauthDTO);
			}
			
			if((preauthDTO.getStatusKey().intValue() == (ReferenceTable.CLAIM_REQUEST_REJECT_STATUS).intValue())){
				updateRejectReason(preauthDTO);
			}
			

			
			submitClaimRequestTaskToDBProcedure(preauthDTO,false,outcome, reimbursement,isFvrOrInvesInitiated);
			
			return reimbursement;
		/*} catch (Exception e) {
			e.printStackTrace();
			Notification.show(e.getMessage());
			log.error(e.toString());
		}
		return null;*/
	}

	public ReimbursementBenefits getReimbursementBefitsByKey(Long key) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByKey");
		query = query.setParameter("key", key);
		ReimbursementBenefits reimbBenefits = (ReimbursementBenefits) query
				.getSingleResult();
		return reimbBenefits;
	}
	
	public Reimbursement getLatestActiveROD(Long claimKey) {
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
//						&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) {
						return reimbursement;					
				}
			}
		}

		return null;
	}

	public ReimbursementQuery getReimbursementQueryByReimbursmentKey(Long key) {
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", key);
		List<ReimbursementQuery> reimbQueryList = query.getResultList();
		if (reimbQueryList != null && !reimbQueryList.isEmpty()) {
			return reimbQueryList.get(0);
		}
		return null;
	}
	
	public ReimbursementQuery getReimbursementQueryByReimbursmentKey(Long key,EntityManager em) {
		this.entityManager = em;
		ReimbursementQuery reimbQuery = getReimbursementQueryByReimbursmentKey(key);
		return reimbQuery;
	}

	private List<ReimbursementBenefitsDetails> getPatientCareTableValues(
			Long key) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query = query.setParameter("benefitsKey", key);
		List<ReimbursementBenefitsDetails> reimbBenefitsDetails = query
				.getResultList();
		return reimbBenefitsDetails;
	}

	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		/*Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);*/
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKeyForAddAdditionalDocs").setParameter("claimKey",
				claimKey);
		List<Reimbursement> rodList = query.getResultList();	
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}

		return rodList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Reimbursement> getRembursementDetails(Long claimKey) {

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
	public List<Reimbursement> getSettledRecords(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findSettledRod");
		query  = query.setParameter("claimKey", claimKey);
		query = query.setParameter("statusId", ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}

		return rodList;
	}
	
	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey, EntityManager em) {
		
		this.entityManager = em;
		List<Reimbursement> rodList = getPreviousRODByClaimKey(claimKey);

		return rodList;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getReimbursementStatusForDownsizeWithdraw(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);
		List<Reimbursement> rodList = query.getResultList();
		
		Boolean isStatus = true;
		
		for (Reimbursement reimbursement : rodList) {
			
			entityManager.refresh(reimbursement);
			
			Boolean isCancelROD = ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey());
			if(! isCancelROD && ! reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)){
				isStatus = false;
			}
		}

		return isStatus;
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

	
	public NEFTQueryDetails getReimbursementByKeyForNEFTDetails(Long rokKey) {
		Query query = entityManager.createNamedQuery("NEFTQueryDetails.findByRODDKey")
				.setParameter("rodKey", rokKey);

		List<NEFTQueryDetails> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementByClaimKeyAndStageId(
			Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKeyAndStageId").setParameter(
				"claimKey", claimKey);
		List<Long> stageList = new ArrayList<Long>();
		stageList.add(ReferenceTable.FVR_STAGE_KEY);
		stageList.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
		stageList.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
		stageList.add(ReferenceTable.BILLING_PROCESS_STAGE_KEY);
		stageList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
		stageList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
		query.setParameter("stageList", stageList);

		List<Reimbursement> rodList = query.getResultList();

		return rodList;
	}

	@SuppressWarnings("unchecked")
	public List<BillItemMapping> getMappingData(Long reimbursementKey) {
		Query query = entityManager.createNamedQuery(
				"BillItemMapping.findByReimbursementKey").setParameter(
				"reimbursementKey", reimbursementKey);
		List<BillItemMapping> billMappingList = query.getResultList();
		if (billMappingList != null && !billMappingList.isEmpty()) {
			for (BillItemMapping billItemMapping : billMappingList) {
				entityManager.refresh(billItemMapping);
			}
		}
		return billMappingList;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitClaimBilling(PreauthDTO preauthDTO) throws Exception{

		//try {
			Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
			log.info("Submit Claim Billing ---------------" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
			Boolean isCancelROD = ReferenceTable.CANCEL_ROD_KEYS.containsKey(preauthDTO.getStatusKey());
					
			Reimbursement reimbursement = savePreauthValues(preauthDTO,
					false,SHAConstants.CLAIM_BILLING);

			if (reimbursement.getClaim() != null) {
				Claim currentClaim = searchByClaimKey(reimbursement.getClaim()	
						.getKey());
				if (currentClaim != null) {
//					currentClaim.setStatus(reimbursement.getStatus());
//					currentClaim.setStage(reimbursement.getStage());
					if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
						currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
					}
					currentClaim.setDataOfAdmission(reimbursement.getDateOfAdmission());
					if(reimbursement.getDateOfDischarge() != null){
						currentClaim.setDataOfDischarge(reimbursement.getDateOfDischarge());
					}
					
					// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
					if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
						currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
						currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
						currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
					}
					
					entityManager.merge(currentClaim);
					entityManager.flush();
					log.info("------CurrentClaim------>"+currentClaim+"<------------");
				}
			}

			
					
//			String remarksForClaimRequest = getRemarksForClaimBilling(
//					reimbursement.getStatus().getKey(), preauthDTO,
//					reimbursement);
//
//			if (reimbursement.getStatus().getKey()
//					.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
//				reimbursement.setBillingRemarks(remarksForClaimRequest);
//			}
//			reimbursement.setBillingApprovedAmount(preauthDTO
//					.getPreauthMedicalDecisionDetails()
//					.getInitialTotalApprovedAmt() != null ? preauthDTO
//					.getPreauthMedicalDecisionDetails()
//					.getInitialTotalApprovedAmt() : 0d);
//			reimbursement.setBillingCompletedDate(new Timestamp(System
//					.currentTimeMillis()));
//
//			Double provisionAmt = 0d;
//			if (preauthDTO.getHospitalizaionFlag()
//					|| preauthDTO.getIsHospitalizationRepeat()
//					|| preauthDTO.getPartialHospitalizaionFlag()) {
//				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
//						.getPayableToHospitalAmt() != null ? preauthDTO
//						.getHospitalizationCalculationDTO()
//						.getPayableToHospitalAmt() : 0d;
//				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
//						.getPayableToInsAmt() != null ? preauthDTO
//						.getHospitalizationCalculationDTO()
//						.getPayableToInsAmt() : 0d;
//			}
//			provisionAmt += preauthDTO.getPreHospitalizationCalculationDTO()
//					.getPayableToInsAmt() != null ? preauthDTO
//					.getPreHospitalizationCalculationDTO().getPayableToInsAmt()
//					.doubleValue() : 0d;
//			provisionAmt += preauthDTO.getPostHospitalizationCalculationDTO()
//					.getPayableToInsAmt() != null ? preauthDTO
//					.getPostHospitalizationCalculationDTO()
//					.getPayableToInsAmt().doubleValue() : 0d;
//			provisionAmt += preauthDTO.getPatientCareAmt();
//			provisionAmt += preauthDTO.getHospitalCashAmt();
//			// List<ReimbursementBenefits> reimbursementBenefitsByRodKey =
//			// getReimbursementBenefitsByRodKey(reimbursement.getKey());
//			// if(reimbursementBenefitsByRodKey != null) {
//			// for (ReimbursementBenefits reimbursementBenefits :
//			// reimbursementBenefitsByRodKey) {
//			// provisionAmt += reimbursementBenefits.getTotalClaimAmountBills()
//			// != null ? reimbursementBenefits.getTotalClaimAmountBills() : 0d;
//			// }
//			// }
//			reimbursement.setCurrentProvisionAmt(provisionAmt);
//
//			entityManager.merge(reimbursement);
//			entityManager.flush();

			if (true) {
				ResidualAmount residualAmt = new ResidualAmount();
				residualAmt.setKey(preauthDTO.getResidualAmountDTO().getKey());
				residualAmt.setTransactionKey(reimbursement.getKey());
				residualAmt.setStage(reimbursement.getStage());
				residualAmt.setStatus(reimbursement.getStatus());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setAmountConsideredAmount(preauthDTO
						.getResidualAmountDTO().getAmountConsideredAmount());
				residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO()
						.getMinimumAmount());
				residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO()
						.getCopayAmount());
				residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO()
						.getApprovedAmount());
				residualAmt.setCopayPercentage(preauthDTO
						.getResidualAmountDTO().getCopayPercentage());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO()
						.getNetAmount() != null ? preauthDTO
						.getResidualAmountDTO().getNetAmount() : 0d);
				residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d);
				
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
				log.info("------ResidualAmt------>"+residualAmt+"<------------");
				entityManager.flush();
			}
			
			Double hospitalCashPayableAmt = 0d;
			Double patientCarePayableAmt = 0d;

			if (null != preauthDTO.getPreauthDataExtractionDetails()
					.getAddOnBenefitsDTOList()
					&& !preauthDTO.getPreauthDataExtractionDetails()
							.getAddOnBenefitsDTOList().isEmpty()) {
				UploadDocumentDTO uploadDTO = preauthDTO
						.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO();
				for (AddOnBenefitsDTO benefitsDTO : preauthDTO
						.getPreauthDataExtractionDetails()
						.getAddOnBenefitsDTOList()) {

					// ReimbursementBenefits reimbursementHospBenefits =
					// ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
					if ((ReferenceTable.HOSPITAL_CASH)
							.equalsIgnoreCase(benefitsDTO.getParticulars())) {
						// ReimbursementBenefits reimbursementHospBenefits =
						// ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(uploadDTO
								.getHospitalBenefitKey());

						/*
						 * reimbursementHospBenefits.setKey(reimbursementBenefits
						 * .getKey());
						 * reimbursementHospBenefits.setReimbursementKey
						 * (reimbursementBenefits.getReimbursementKey());
						 */
						reimbursementBenefits
								.setTreatmentForPhysiotherapy(uploadDTO
										.getTreatmentPhysiotherapyFlag());

						reimbursementBenefits.setNumberOfDaysBills(Long
								.valueOf(uploadDTO.getHospitalCashNoofDays()));
						reimbursementBenefits.setPerDayAmountBills(Double
								.valueOf(uploadDTO.getHospitalCashPerDayAmt()));
						reimbursementBenefits.setTotalClaimAmountBills(Double
								.valueOf((uploadDTO
										.getHospitalCashTotalClaimedAmt())));
						
						reimbursementBenefits
								.setBenefitsFlag(reimbursementBenefits
										.getBenefitsFlag());
						
						if(!preauthDTO.getAddOnBenefitsHospitalCash()) {
							reimbursementBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
						}

						reimbursementBenefits = populateBenefitsData(uploadDTO,
								reimbursementBenefits, benefitsDTO);
						
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursementBenefits.setTotalClaimAmountBills(0d);
							reimbursementBenefits.setTotalAmount(0d);
							reimbursementBenefits.setPayableAmount(0d);
						}
						
						if(reimbursementBenefits.getKey() != null) {
							entityManager.merge(reimbursementBenefits);
						} else {
							entityManager.persist(reimbursementBenefits);
						}
						
						entityManager.flush();
						
						if(reimbursementBenefits.getNetAmount() != null){
							hospitalCashPayableAmt += reimbursementBenefits.getNetAmount();
						}
						
						log.info("------ReimbursementBenefits------>"+reimbursementBenefits.getKey()+"<------------");

					}

					else if ((ReferenceTable.PATIENT_CARE)
							.equalsIgnoreCase(benefitsDTO.getParticulars())) {

						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientBenefitKey());

						/*
						 * reimbursementHospBenefits.setKey(reimbursementBenefits
						 * .getKey());
						 * reimbursementHospBenefits.setReimbursementKey
						 * (reimbursementBenefits.getReimbursementKey());
						 */
						reimbursementBenefits
								.setTreatmentForPhysiotherapy(uploadDTO
										.getTreatmentPhysiotherapyFlag());
						reimbursementBenefits.setNumberOfDaysBills(Long
								.valueOf(uploadDTO.getPatientCareNoofDays()));
						reimbursementBenefits.setPerDayAmountBills(Double
								.valueOf(uploadDTO.getPatientCarePerDayAmt()));
						reimbursementBenefits.setTotalClaimAmountBills(Double
								.valueOf((uploadDTO
										.getPatientCareTotalClaimedAmt())));
						reimbursementBenefits
								.setBenefitsFlag(reimbursementBenefits
										.getBenefitsFlag());
						
						reimbursementBenefits = populateBenefitsData(uploadDTO,
								reimbursementBenefits, benefitsDTO);
						
						if(!preauthDTO.getAddOnBenefitsPatientCare()) {
							reimbursementBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
						}
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursementBenefits.setTotalClaimAmountBills(0d);
							reimbursementBenefits.setTotalAmount(0d);
							reimbursementBenefits.setPayableAmount(0d);
						}
						
						if(reimbursementBenefits.getKey() != null) {
							entityManager.merge(reimbursementBenefits);
						} else {
							entityManager.persist(reimbursementBenefits);
						}
//						entityManager.merge(reimbursementBenefits);
						entityManager.flush();
						
						if(reimbursementBenefits.getNetAmount() != null){
							patientCarePayableAmt += reimbursementBenefits.getNetAmount();
						}
						
						log.info("------ReimbursementBenefits------>"+reimbursementBenefits.getKey()+"<------------");

						List<ReimbursementBenefitsDetails> reimbBenefitsList = getPatientCareTableValues(preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientBenefitKey());

						List<PatientCareDTO> patientList = preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientCareDTO();

						if (null != reimbBenefitsList
								&& !reimbBenefitsList.isEmpty()
								&& null != patientList
								&& !patientList.isEmpty()) {
							for (ReimbursementBenefitsDetails benefitsDetails : reimbBenefitsList) {

								for (PatientCareDTO patientCareDTO : patientList) {

									if (benefitsDetails.getKey().equals(
											patientCareDTO.getKey())) {
										benefitsDetails
												.setEngagedFrom(patientCareDTO
														.getEngagedFrom());
										benefitsDetails
												.setEngagedTo(patientCareDTO
														.getEngagedTo());

										entityManager.merge(benefitsDetails);
										entityManager.flush();
										log.info("------BenefitsDetails------>"+benefitsDetails.getKey()+"<------------");

										break;
									}
								}

							}
						}

						/*
						 * if(null != patientList && !patientList.isEmpty()) {
						 * for (PatientCareDTO patientCareDTO : patientList) {
						 * ReimbursementBenefitsDetails
						 * reimbursementBenefitDetails = new
						 * ReimbursementBenefitsDetails();
						 * reimbursementBenefitDetails
						 * .setEngagedFrom(patientCareDTO.getEngagedFrom());
						 * reimbursementBenefitDetails
						 * .setEngagedTo(patientCareDTO.getEngagedTo());
						 * reimbursementBenefitDetails
						 * .setReimbursementBenefits(reimbursementHospBenefits);
						 * 
						 * entityManager.merge(reimbursementBenefitDetails);
						 * entityManager.flush();
						 * //entityManager.refresh(reimbursementBenefitDetails);
						 * } }
						 */

					}
					
					/** Below code is added for MED-PRD-058 - Classic Health Group. 
					  Based on this change in BSI View current provision is not included both hospital cash and patient care.
					  This is  applicable only for this product*/
					
					if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						
						
						
						if(null != hospitalCashPayableAmt)
						{
							reimbursement.setAddOnCoversApprovedAmount(hospitalCashPayableAmt);
						}

						if(null != patientCarePayableAmt)
						{
							reimbursement.setOptionalApprovedAmount(patientCarePayableAmt);
						}
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							//resetReimbursementBenefitAmount(reimbursement);
						}
					}
					entityManager.merge(reimbursement);
					entityManager.flush();
				
				}
			}
			
			// saveAddOnBenefitsValues(preauthDTO, reimbursement);
			/*Below Condition for IMSSUPPOR-27502*/
			if(!preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
				saveCalculationValues(preauthDTO, reimbursement, isCancelROD);
			}
			List<BillEntryDetailsDTO> hospitalizationTableList = preauthDTO.getHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> preHospitalizationTableList = preauthDTO.getPreHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> postHospitalizationTableList = preauthDTO.getPostHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> prePostHospList = new ArrayList<PreHospitalizationDTO>();
			
			if (null != hospitalizationTableList && !hospitalizationTableList.isEmpty())
			{
				for (BillEntryDetailsDTO billEntryDetailsDTO : hospitalizationTableList) {
					billEntryDetailsDTO.setBillingCompletedDate(reimbursement.getBillingCompletedDate());
				}
			}
			
			if (null != preHospitalizationTableList && !preHospitalizationTableList.isEmpty())
			{
				for (PreHospitalizationDTO preHospitalizationDTO : preHospitalizationTableList) {
					preHospitalizationDTO.setBillingCompletedDate(reimbursement.getBillingCompletedDate());
					prePostHospList.add(preHospitalizationDTO);
				}
			}
			
			if (null != postHospitalizationTableList && !postHospitalizationTableList.isEmpty())
			{
				for (PreHospitalizationDTO postHospitalizationDTO : postHospitalizationTableList) {
					postHospitalizationDTO.setBillingCompletedDate(reimbursement.getBillingCompletedDate());
					prePostHospList.add(postHospitalizationDTO);
				}
			}
			
			if((ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
						(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))))
						{
				 List<OtherBenefitsTableDto> otherBenefitsList = preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList();
				  
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
			
			saveBillSummaryRemarksValues(hospitalizationTableList,prePostHospList,SHAConstants.BILLING);
			
			
			String outcome = getOutComeForClaimBilling(reimbursement);
			//setBPMForClaimBilling(preauthDTO, true, outcome, reimbursement);
			submitBillingAndFATaskToDBForClaim(preauthDTO, true, outcome, reimbursement);
			if(preauthDTO.getClaimDTO().getLegalClaim() !=null && preauthDTO.getClaimDTO().getLegalClaim().equals("Y")){
				if(preauthDTO.getLegalBillingDTO() !=null){
					String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
					LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
					LegalTaxDeduction taxDeduction = taxDeductionMapper.getLegalTaxDeductionFromDTO(preauthDTO.getLegalBillingDTO());
					taxDeduction.setIntimationKey(preauthDTO.getNewIntimationDTO().getKey());
					taxDeduction.setRodKey(reimbursement.getKey());
					taxDeduction.setClaimKey(reimbursement.getClaim().getKey());
					if(taxDeduction.getKey()!=null){
						taxDeduction.setModifyBy(userId);
						taxDeduction.setModifiedDate(new Date());
						entityManager.merge(taxDeduction);
						entityManager.flush(); 
					}else{
						taxDeduction.setCreatedBy(userId);
						taxDeduction.setCreatedDate(new Date());
						taxDeduction.setActiveStatus(1L);
						entityManager.persist(taxDeduction);			
						entityManager.flush();
					}
				}
			}
			
			if (preauthDTO.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName())) {
				if(null != wrkFlowMap){
					dbCalculationService.releaseHoldClaim(Long.parseLong(String.valueOf(wrkFlowMap.get(SHAConstants.WK_KEY))));
				}
			}
			return reimbursement;
		/*} catch (Exception e) {
			e.printStackTrace();
			Notification.show(e.getMessage());
			log.error(e.toString());
		}
		return null;*/

	}

	private ReimbursementBenefits populateBenefitsData(
			UploadDocumentDTO uploadDTO,
			ReimbursementBenefits reimbursementHospBenefits,
			AddOnBenefitsDTO benefitsDTO) {
		reimbursementHospBenefits.setNumberOfDaysEligible(benefitsDTO
				.getEligibleNoofDays().longValue());
		reimbursementHospBenefits.setNumberOfDaysPayable(benefitsDTO
				.getEligiblePayableNoOfDays().longValue());
		reimbursementHospBenefits.setPerDayAmount(benefitsDTO
				.getEligiblePerDayAmt().doubleValue());
		reimbursementHospBenefits.setTotalAmount(benefitsDTO
				.getEligibleNetAmount().doubleValue());
		if (null != benefitsDTO.getCoPayPercentage()) {
			reimbursementHospBenefits.setCopayPercentage(Double.valueOf(benefitsDTO.getCoPayPercentage().getValue()));
		}
		reimbursementHospBenefits.setCopayAmount(benefitsDTO.getCopayAmount()
				.doubleValue());
		reimbursementHospBenefits.setNetAmount(benefitsDTO
				.getNetAmountAfterCopay().doubleValue());
		reimbursementHospBenefits.setLimitForPolicy(benefitsDTO
				.getLimitAsPerPolicy().doubleValue());
		reimbursementHospBenefits.setPayableAmount(benefitsDTO
				.getPayableAmount().doubleValue());
		return reimbursementHospBenefits;
	}

	private void saveCalculationValues(PreauthDTO bean,
			Reimbursement reimbursement, Boolean isCancelROD) {
			saveHospCalculation(bean, reimbursement, isCancelROD);

			savePreHospCalculation(bean, reimbursement, isCancelROD);

			savePostHospCalculation(bean, reimbursement, isCancelROD);
	}

	private void savePostHospCalculation(PreauthDTO bean, Reimbursement reimbursement, Boolean isCancelROD) {
		ReimbursementCalCulationDetails reimbursementCalcForHosp = new ReimbursementCalCulationDetails();
		PostHopitalizationDetailsDTO hospDTO = bean
				.getPostHospitalizationCalculationDTO();
		reimbursementCalcForHosp.setReimbursement(reimbursement);
		reimbursementCalcForHosp.setNetPayableAmount(!isCancelROD ? hospDTO
				.getPayableAmt() != null ?hospDTO
						.getPayableAmt() : 0  : 0);
		reimbursementCalcForHosp.setClaimRestrictionAmount(!isCancelROD ? hospDTO
				.getClaimRestrictionAmt() != null ? hospDTO
						.getClaimRestrictionAmt() : 0 : 0);
		reimbursementCalcForHosp.setEligibleAmount(!isCancelROD  ? hospDTO.getNetAmount() != null ? hospDTO.getNetAmount() : 0 : 0);
		reimbursementCalcForHosp
				.setEligibleAmount(!isCancelROD  ? hospDTO.getEligibleAmt() != null ? hospDTO.getEligibleAmt() : 0 : 0);
		reimbursementCalcForHosp.setNetEligiblePayableAmount(!isCancelROD ? hospDTO
				.getNetPayable() != null ? hospDTO
						.getNetPayable() : 0 : 0 );
		reimbursementCalcForHosp.setCopayAmount(!isCancelROD  ? hospDTO.getCopayAmt() != null ? hospDTO.getCopayAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableToInsured(!isCancelROD  ? hospDTO
				.getPayableToInsAmt() != null ? hospDTO
						.getPayableToInsAmt() : 0 : 0);
		reimbursementCalcForHosp.setDeductedBalancePremium(!isCancelROD ? hospDTO
				.getBalancePremiumAmt() != null ? hospDTO
						.getBalancePremiumAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableInsuredAfterPremium(!isCancelROD ? hospDTO
				.getPayableToInsuredAftPremiumAmt() != null ? hospDTO
						.getPayableToInsuredAftPremiumAmt() : 0: 0);
		reimbursementCalcForHosp.setBalanceSIAftHosp(!isCancelROD ? hospDTO
				.getAvaliableSumInsuredAftHosp() != null ? hospDTO
						.getAvaliableSumInsuredAftHosp() : 0 : 0);
		reimbursementCalcForHosp.setRestrictedSIAftHosp(!isCancelROD ? hospDTO
				.getRestrictedSIAftHosp() != null ? hospDTO
						.getRestrictedSIAftHosp() : 0 : 0);
		reimbursementCalcForHosp
				.setBillClassificationId(ReferenceTable.POST_HOSPITALIZATION);
		reimbursementCalcForHosp.setAlreadyPaid(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
		
		
		if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
			reimbursementCalcForHosp.setTpaClaimedAmt(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getTotalClaimedAmt() : 0);
			reimbursementCalcForHosp.setTpaNonMedicalAmt(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getNonMedicalAmt() : 0);
			reimbursementCalcForHosp.setTpaBalanceAmt(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getBalanceAmt() : 0);
			reimbursementCalcForHosp.setTpaSettledAmt(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getTpaSettledAmt() : 0);
			reimbursementCalcForHosp.setTpaPayableToInsured(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableToIns() : 0);
			reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() : 0);
			
			if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
				reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
				//Integer amt = ((bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 ));
				reimbursementCalcForHosp.setBalanceToBePaidAmt(bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt());
			} 
			/*Start - IMSSUPPOR-27502*/
			else {
				if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					Integer amt1 = (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
                    reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1 + (reimbursementCalcForHosp.getPayableInsuredAfterPremium() != null ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0));
				}
			}
			/*End - IMSSUPPOR-27502*/
		} else {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0);
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					reimbursementCalcForHosp.setBalanceToBePaidAmt(hospDTO.getBalanceToBePaid());
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer alreadyPaidAmt = hospDTO.getAmountAlreadyPaid() + hospDTO.getBalanceToBePaid();
						reimbursementCalcForHosp.setAmountAlreadyPaidAmt(alreadyPaidAmt);
					}
					Integer amt = (hospDTO.getPayableToInsuredAftPremiumAmt() != null ? hospDTO.getPayableToInsuredAftPremiumAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
					if(amt.equals(hospDTO.getBalanceToBePaid() != null ? hospDTO.getBalanceToBePaid() : 0)) {
						//reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getBalanceToBePaid());
						reimbursementCalcForHosp.setPayableToInsured(hospDTO.getBalanceToBePaid());
					}
					
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
						reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					}
				}
				/*Start - IMSSUPPOR-27502*/
				else {
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer amt1 = (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
	                    reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1 + (reimbursementCalcForHosp.getPayableInsuredAfterPremium() != null ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0));
					}
				}
				/*End - IMSSUPPOR-27502*/
			} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
//				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableToHospAftTDS() : 0);
			}
		}
		
		Long reimbursementCalcByRodAndClassificationKey = getReimbursementCalcByRodAndClassificationKey(
				reimbursement.getKey(), ReferenceTable.POST_HOSPITALIZATION);
		if (reimbursementCalcByRodAndClassificationKey != null) {
			reimbursementCalcForHosp
					.setKey(reimbursementCalcByRodAndClassificationKey);
		}
		if (reimbursementCalcForHosp.getKey() == null) {
			entityManager.persist(reimbursementCalcForHosp);
		} else {
			entityManager.merge(reimbursementCalcForHosp);
		}
		entityManager.flush();
		log.info("------ReimbursementCalCulationDetails------>"+reimbursementCalcForHosp+"<------------");
	}

	private void savePreHospCalculation(PreauthDTO bean, Reimbursement reimbursement, Boolean isCancelROD) {
		ReimbursementCalCulationDetails reimbursementCalcForHosp = new ReimbursementCalCulationDetails();
		PreHopitalizationDetailsDTO hospDTO = bean
				.getPreHospitalizationCalculationDTO();
		reimbursementCalcForHosp.setReimbursement(reimbursement);
		reimbursementCalcForHosp.setNetPayableAmount(!isCancelROD ? hospDTO
				.getPayableAmt() != null ? hospDTO
						.getPayableAmt() : 0 : 0);
		reimbursementCalcForHosp.setClaimRestrictionAmount(!isCancelROD  ? hospDTO
				.getClaimRestrictionAmt() != null ? hospDTO
						.getClaimRestrictionAmt() : 0 : 0);
		reimbursementCalcForHosp.setEligibleAmount(!isCancelROD ? hospDTO
				.getAmountConsidered() != null ? hospDTO
						.getAmountConsidered() : 0 : 0);
		reimbursementCalcForHosp.setNetEligiblePayableAmount(!isCancelROD ? hospDTO
				.getNetPayable() != null ? hospDTO
						.getNetPayable() : 0  : 0);
		reimbursementCalcForHosp.setCopayAmount(!isCancelROD  ? hospDTO.getCopayAmt() != null ? hospDTO.getCopayAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableToInsured(!isCancelROD  ? hospDTO
				.getPayableToInsAmt() != null ? hospDTO
						.getPayableToInsAmt() : 0 : 0);
		reimbursementCalcForHosp.setDeductedBalancePremium(!isCancelROD  ? hospDTO
				.getBalancePremiumAmt() != null ? hospDTO
						.getBalancePremiumAmt() : 0 :0);
		reimbursementCalcForHosp.setPayableInsuredAfterPremium(!isCancelROD ? hospDTO
				.getPayableToInsuredAftPremiumAmt() != null ? hospDTO
						.getPayableToInsuredAftPremiumAmt() : 0 : 0);
		reimbursementCalcForHosp.setBalanceSIAftHosp(!isCancelROD  ? hospDTO
				.getAvailableSIAftHosp() != null ? hospDTO
						.getAvailableSIAftHosp() : 0 : 0);
		reimbursementCalcForHosp.setRestrictedSIAftHosp(!isCancelROD  ? hospDTO
				.getRestrictedSIAftHosp() != null ? hospDTO
						.getRestrictedSIAftHosp() : 0: 0);
		reimbursementCalcForHosp
				.setBillClassificationId(ReferenceTable.PRE_HOSPITALIZATION);
		reimbursementCalcForHosp.setAlreadyPaid(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
		
		
		if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
			reimbursementCalcForHosp.setTpaClaimedAmt(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getTotalClaimedAmt() : 0);
			reimbursementCalcForHosp.setTpaNonMedicalAmt(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getNonMedicalAmt() : 0);
			reimbursementCalcForHosp.setTpaBalanceAmt(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getBalanceAmt() : 0);
			reimbursementCalcForHosp.setTpaSettledAmt(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getTpaSettledAmt() : 0);
			reimbursementCalcForHosp.setTpaPayableToInsured(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getPayableToIns() : 0);
			reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt() : 0);
			if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
				reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
				//Integer amt = ((bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 ));
				reimbursementCalcForHosp.setBalanceToBePaidAmt(bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt());
			} 
			/*Start - IMSSUPPOR-27502*/
			else {
				if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					Integer amt1 = (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
                    reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1 + (reimbursementCalcForHosp.getPayableInsuredAfterPremium() != null ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0));
				}
			}
			/*End - IMSSUPPOR-27502*/
		} else {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0);
				
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					reimbursementCalcForHosp.setBalanceToBePaidAmt(hospDTO.getBalanceToBePaid());
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer alreadyPaidAmt = hospDTO.getAmountAlreadyPaid() + hospDTO.getBalanceToBePaid();
						reimbursementCalcForHosp.setAmountAlreadyPaidAmt(alreadyPaidAmt);
					}
					Integer amt = (hospDTO.getPayableToInsuredAftPremiumAmt() != null ? hospDTO.getPayableToInsuredAftPremiumAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
					if(amt.equals(hospDTO.getBalanceToBePaid() != null ? hospDTO.getBalanceToBePaid() : 0)) {
						//reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getBalanceToBePaid());
						reimbursementCalcForHosp.setPayableToInsured(hospDTO.getBalanceToBePaid());
					}
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
						reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					}
				}
				/*Start - IMSSUPPOR-27502*/
				else {
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer amt1 = (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
	                    reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1 + (reimbursementCalcForHosp.getPayableInsuredAfterPremium() != null ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0));
					}
				}
				/*End - IMSSUPPOR-27502*/
			} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
//				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableToHospAftTDS() : 0);
			}
			
		}
		
		Long reimbursementCalcByRodAndClassificationKey = getReimbursementCalcByRodAndClassificationKey(
				reimbursement.getKey(), ReferenceTable.PRE_HOSPITALIZATION);
		if (reimbursementCalcByRodAndClassificationKey != null) {
			reimbursementCalcForHosp
					.setKey(reimbursementCalcByRodAndClassificationKey);
		}
		if (reimbursementCalcForHosp.getKey() == null) {
			entityManager.persist(reimbursementCalcForHosp);
		} else {
			entityManager.merge(reimbursementCalcForHosp);
		}
		entityManager.flush();
		log.info("------ReimbursementCalCulationDetails------>"+reimbursementCalcForHosp+"<------------");
	}

	private void saveHospCalculation(PreauthDTO bean, Reimbursement reimbursement, Boolean isCancelROD) {
		HopitalizationCalulationDetailsDTO hospDTO = bean
				.getHospitalizationCalculationDTO();
		ReimbursementCalCulationDetails reimbursementCalcForHosp = new ReimbursementCalCulationDetails();
		reimbursementCalcForHosp.setReimbursement(reimbursement);
		reimbursementCalcForHosp.setNetPayableAmount(!isCancelROD ? hospDTO
				.getNetPayableAmt() != null ? hospDTO
						.getNetPayableAmt() : 0 : 0);
		reimbursementCalcForHosp.setClaimRestrictionAmount(!isCancelROD  ? hospDTO
				.getClaimRestrictionAmt() != null ? hospDTO
						.getClaimRestrictionAmt() : 0  : 0);
		reimbursementCalcForHosp.setCashlessApprovedAmount(!isCancelROD  ? hospDTO
				.getPreauthAppAmt() != null ? hospDTO
						.getPreauthAppAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableToHospital(!isCancelROD ? hospDTO
				.getPayableToHospitalAmt() != null ? hospDTO
						.getPayableToHospitalAmt() : 0 :0);
		reimbursementCalcForHosp.setPayableToInsured(!isCancelROD  ? ((hospDTO.getBalancePremiumAmt() !=null &&hospDTO.getBalancePremiumAmt().equals(0)) ? hospDTO
				.getPayableToInsAmt() : hospDTO
				.getPayableToInsuredAftPremiumAmt() != null ? hospDTO
						.getPayableToInsuredAftPremiumAmt() : 0) : 0);
		reimbursementCalcForHosp.setTdsAmount(!isCancelROD ? hospDTO.getTdsAmt() != null ? hospDTO.getTdsAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableToHospAftTDS(!isCancelROD ? hospDTO
				.getPayableToHospitalAftTDSAmt() != null ? hospDTO
						.getPayableToHospitalAftTDSAmt() : 0 : 0);
		reimbursementCalcForHosp.setDeductedBalancePremium(!isCancelROD  ? hospDTO
				.getBalancePremiumAmt() != null ? hospDTO
						.getBalancePremiumAmt() : 0 : 0);
		reimbursementCalcForHosp.setPayableInsuredAfterPremium(!isCancelROD  ? hospDTO
				.getPayableToInsuredAftPremiumAmt() != null ? hospDTO
						.getPayableToInsuredAftPremiumAmt() : 0: 0);
		reimbursementCalcForHosp
				.setBillClassificationId(ReferenceTable.HOSPITALIZATION);
		reimbursementCalcForHosp.setHospitalDiscount(!isCancelROD  ? hospDTO.getHospitalDiscount() != null ? hospDTO.getHospitalDiscount() :0 : 0);
		reimbursementCalcForHosp.setHospitalDiscountAmtAft(!isCancelROD  ? hospDTO.getAfterHospitalDiscount() != null ? hospDTO.getAfterHospitalDiscount() :0 : 0);
		reimbursementCalcForHosp.setAmountAlreadyPaidAmt(0);
		reimbursementCalcForHosp.setBalanceToBePaidAmt(0);
		reimbursementCalcForHosp.setAlreadyPaid(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
		
		
		if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
			reimbursementCalcForHosp.setTpaClaimedAmt(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getTotalClaimedAmt() : 0);
			reimbursementCalcForHosp.setTpaNonMedicalAmt(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getNonMedicalAmt() : 0);
			reimbursementCalcForHosp.setTpaBalanceAmt(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getBalanceAmt() : 0);
			reimbursementCalcForHosp.setTpaSettledAmt(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getTpaSettledAmt() : 0);
			reimbursementCalcForHosp.setTpaPayableToInsured(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getPayableToIns() : 0);
			reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() : 0);
			
			if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
				reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
				//Integer amt = ((bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 ));
				reimbursementCalcForHosp.setBalanceToBePaidAmt(bean.getOtherInsHospSettlementCalcDTO().getPayableAmt());
			}
			
		} else {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0);
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					reimbursementCalcForHosp.setBalanceToBePaidAmt(hospDTO.getBalanceToBePaid());
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer alreadyPaidAmt = hospDTO.getAmountAlreadyPaid() + hospDTO.getBalanceToBePaid();
						reimbursementCalcForHosp.setAmountAlreadyPaidAmt(alreadyPaidAmt);
					}
					Integer amt = (hospDTO.getPayableToInsuredAftPremiumAmt() != null ? hospDTO.getPayableToInsuredAftPremiumAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
					if(amt.equals(hospDTO.getBalanceToBePaid() != null ? hospDTO.getBalanceToBePaid() : 0)) {
						/*Integer alreadySettled = hospDTO.getPayableToInsuredAftPremiumAmt() + hospDTO.getBalanceToBePaid();
						reimbursementCalcForHosp.setPayableInsuredAfterPremium(alreadySettled);*/
						//reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getBalanceToBePaid());
						reimbursementCalcForHosp.setPayableToInsured(hospDTO.getBalanceToBePaid());
					}
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
						reimbursementCalcForHosp.setPayableInsuredAfterPremium(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					}
				}else{
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
						Integer amt1 = (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
						reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1 + (reimbursementCalcForHosp.getPayableInsuredAfterPremium() != null ? reimbursementCalcForHosp.getPayableInsuredAfterPremium() : 0));
					}
				}
				
			} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId()!= null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				reimbursementCalcForHosp.setTpaPayableAmt(!isCancelROD ? reimbursementCalcForHosp.getPayableToHospAftTDS() : 0);
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					reimbursementCalcForHosp.setAmountAlreadyPaidAmt(hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0 );
					reimbursementCalcForHosp.setBalanceToBePaidAmt(hospDTO.getBalanceToBePaid());
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						Integer alreadyPaidAmt = hospDTO.getAmountAlreadyPaid() + hospDTO.getBalanceToBePaid();
						reimbursementCalcForHosp.setAmountAlreadyPaidAmt(alreadyPaidAmt);
					}
					Integer amt = (hospDTO.getPayableToHospitalAftTDSAmt() != null ? hospDTO.getPayableToHospitalAftTDSAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
					if(amt.equals(hospDTO.getBalanceToBePaid() != null ? hospDTO.getBalanceToBePaid() : 0)) {
						reimbursementCalcForHosp.setPayableToHospAftTDS(amt);
						reimbursementCalcForHosp.setPayableToHospital(amt);
					}
				}else{
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
					Integer amt1 = (hospDTO.getPayableToHospitalAftTDSAmt() != null ? hospDTO.getPayableToHospitalAftTDSAmt() : 0) - (hospDTO.getAmountAlreadyPaid() != null ? hospDTO.getAmountAlreadyPaid() : 0);
					reimbursementCalcForHosp.setAmountAlreadyPaidAmt(amt1);
					}
				}
				
			}
			
			
			
		}
		
	
		
		Long reimbursementCalcByRodAndClassificationKey = getReimbursementCalcByRodAndClassificationKey(
				reimbursement.getKey(), ReferenceTable.HOSPITALIZATION);
		if (reimbursementCalcByRodAndClassificationKey != null) {
			reimbursementCalcForHosp
					.setKey(reimbursementCalcByRodAndClassificationKey);
		}
		if (reimbursementCalcForHosp.getKey() == null) {
			entityManager.persist(reimbursementCalcForHosp);
		} else {
			entityManager.merge(reimbursementCalcForHosp);
		}
		entityManager.flush();
		log.info("------ReimbursementCalCulationDetails------>"+reimbursementCalcForHosp.getKey()+"<------------");
	}

	/*private void saveAddOnBenefitsValues(PreauthDTO preauthDTO,
			Reimbursement reimbursement) {
		if (null != preauthDTO.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO().getHospitalCashAddonBenefitsFlag()
				&& ("Y").equalsIgnoreCase(preauthDTO
						.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.getHospitalCashAddonBenefitsFlag())) {
			UploadDocumentDTO dto = preauthDTO
					.getPreauthDataExtractionDetails().getUploadDocumentDTO();
			ReimbursementBenefits reimbursementBenefits = new ReimbursementBenefits();
			reimbursementBenefits.setReimbursementKey(reimbursement);
			reimbursementBenefits.setTreatmentForPhysiotherapy(dto
					.getTreatmentPhysiotherapyFlag());
			reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
					.getHospitalCashNoofDays()));
			reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
					.getHospitalCashPerDayAmt()));
			reimbursementBenefits.setTotalClaimAmountBills(Double.valueOf(dto
					.getHospitalCashTotalClaimedAmt()));
			reimbursementBenefits.setBenefitsFlag("HC");
			reimbursementBenefits.setKey(dto.getHospitalBenefitKey());

			if (reimbursementBenefits.getKey() == null) {
				entityManager.persist(reimbursementBenefits);
			} else {
				entityManager.merge(reimbursementBenefits);
			}

			entityManager.flush();
			log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");

		}

		if (null != preauthDTO.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO().getPatientCareAddOnBenefitsFlag()
				&& ("Y").equalsIgnoreCase(preauthDTO
						.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.getPatientCareAddOnBenefitsFlag())) {
			UploadDocumentDTO dto = preauthDTO
					.getPreauthDataExtractionDetails().getUploadDocumentDTO();
			ReimbursementBenefits reimbursementBenefits = new ReimbursementBenefits();
			reimbursementBenefits.setReimbursementKey(reimbursement);
			reimbursementBenefits.setTreatmentForPhysiotherapy(dto
					.getTreatmentPhysiotherapyFlag());
			reimbursementBenefits.setNumberOfDaysBills(Long.valueOf(dto
					.getPatientCareNoofDays()));
			reimbursementBenefits.setPerDayAmountBills(Double.valueOf(dto
					.getPatientCarePerDayAmt()));
			reimbursementBenefits.setTotalClaimAmountBills(Double.valueOf(dto
					.getPatientCareTotalClaimedAmt()));
			reimbursementBenefits.setBenefitsFlag("PC");

			reimbursementBenefits.setKey(dto.getPatientBenefitKey());

			if (reimbursementBenefits.getKey() == null) {
				entityManager.persist(reimbursementBenefits);
			} else {
				entityManager.merge(reimbursementBenefits);
			}

			entityManager.flush();
			log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");

			List<PatientCareDTO> patientList = preauthDTO
					.getPreauthDataExtractionDetails().getUploadDocumentDTO()
					.getPatientCareDTO();

			if (null != patientList && !patientList.isEmpty()) {
				for (PatientCareDTO patientCareDTO : patientList) {
					ReimbursementBenefitsDetails reimbursementBenefitDetails = new ReimbursementBenefitsDetails();
					reimbursementBenefitDetails.setEngagedFrom(patientCareDTO
							.getEngagedFrom());
					reimbursementBenefitDetails.setEngagedTo(patientCareDTO
							.getEngagedTo());
					reimbursementBenefitDetails
							.setReimbursementBenefits(reimbursementBenefits);
					reimbursementBenefitDetails.setKey(patientCareDTO.getKey());

					if (reimbursementBenefitDetails.getKey() == null) {
						entityManager.persist(reimbursementBenefitDetails);
					} else {
						entityManager.merge(reimbursementBenefitDetails);
					}

					entityManager.flush();
					log.info("------ReimbursementBenefitsDetails------>"+reimbursementBenefitDetails+"<------------");
				}
			}

		}
	}*/

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Reimbursement submitClaimFinancial(PreauthDTO preauthDTO) throws Exception {

			log.info("Submit Claim Financial --------------->" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
			Boolean isCancelROD = ReferenceTable.CANCEL_ROD_KEYS.containsKey(preauthDTO.getStatusKey());
			preauthDTO.setFinancialCompletedDate(new Timestamp(System
					.currentTimeMillis()));
			Boolean canCreatePayment = true;
			if((preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
					canCreatePayment = false;
				}
			}else if((preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
					canCreatePayment = false;
				}
			} 
			
			//MOVED AS PER SATISH SIR SUGGESTION FOR DMS SLOWNESS ISSUE - 04-03-2020
			WeakHashMap dataMap = preauthDTO.getFilePathAndTypeMap();
			
			if (null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(
					ReferenceTable.FINANCIAL_APPROVE_STATUS)) 
			{
				Claim searchByClaimKey = searchByClaimKey(preauthDTO.getClaimKey());
			dataMap.put("intimationNumber",searchByClaimKey.getIntimation().getIntimationId());
			dataMap.put("claimNumber",searchByClaimKey.getClaimId());
			dataMap.put("createdBy", preauthDTO.getStrUserName());
			//dataMap.put(SHAConstants.ROD_NUMBER,reimbursement.getRodNumber());
			
			
			if(null != searchByClaimKey.getClaimType())
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(searchByClaimKey.getClaimType().getKey()))
					{
						Preauth preauth = getPreauthClaimKey(searchByClaimKey.getKey());
						if(null != preauth && dataMap != null)
							dataMap.put("cashless", preauth.getPreauthId());
						dataMap.put("reimbursementNumber", preauthDTO.getRodNumber());
					}
				else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(searchByClaimKey.getClaimType().getKey()) && dataMap != null)
				{
					dataMap.put("reimbursementNumber", preauthDTO.getRodNumber());
				}
			}
			
			if(dataMap != null) {
				String billSummaryFilePath = (String) dataMap.get("BillSummaryDocFilePath");
				String billSummaryDocType = (String) dataMap.get("BillSummaryDocType");
				dataMap.put("filePath", billSummaryFilePath);
				dataMap.put("docType", billSummaryDocType);
				Date startDate = new Date();
				log.info("Submit Claim Financial before upload--------------->" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION")+" "+startDate);
				uploadGeneratedLetterToDMS(dataMap);
				SHAUtils.setClearReferenceData(dataMap);
				log.info("Submit Claim Financial after upload--------------->" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION")+" "+SHAUtils.getDurationFromTwoDate(startDate, new Date()));
				/**
				 * As per lakshmi's comment, discharge voucher is been removed
				 * from FA screen. Hence we will not upload the same to DMS.
				 * */
				
				
				/*String dischargeVoucherFilePath = (String)dataMap.get("DischargeVoucherFilePath");
				String dischargeVoucherDocType = (String) dataMap.get("DischargeVoucherDocType");
				dataMap.put("filePath", dischargeVoucherFilePath);
				dataMap.put("docType", dischargeVoucherDocType);
				uploadGeneratedLetterToDMS(dataMap);*/
			}
			}
			
			
			Reimbursement reimbursement = savePreauthValues(preauthDTO, false,SHAConstants.CLAIM_FINANCIAL);
			Claim currentClaim = null;
			if (reimbursement.getClaim() != null) {
				currentClaim = searchByClaimKey(reimbursement.getClaim()
						.getKey());
				if (currentClaim != null) {
					
//					currentClaim.setStatus(reimbursement.getStatus());
//					currentClaim.setStage(reimbursement.getStage());
					if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
						currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
					}
					currentClaim.setDataOfAdmission(reimbursement.getDateOfAdmission());
					if(reimbursement.getDateOfDischarge() != null){
						currentClaim.setDataOfDischarge(reimbursement.getDateOfDischarge());
					}
					
					// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
					if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
						currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
						currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
						currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
					}
					
					entityManager.merge(currentClaim);
					entityManager.flush();
					log.info("------CurrentClaim------>"+currentClaim+"<------------");
					reimbursement.setClaim(currentClaim);
				}
			}

//			String remarksForClaimRequest = getRemarksForClaimFinancial(
//					reimbursement.getStatus().getKey(), preauthDTO,
//					reimbursement);
//
//			if (reimbursement.getStatus().getKey()
//					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
//				reimbursement
//						.setFinancialApprovalRemarks(remarksForClaimRequest);
//			}
//			reimbursement.setFinancialApprovedAmount(preauthDTO
//					.getPreauthMedicalDecisionDetails()
//					.getInitialTotalApprovedAmt() != null ? preauthDTO
//					.getPreauthMedicalDecisionDetails()
//					.getInitialTotalApprovedAmt() : 0d);
//			reimbursement.setFinancialCompletedDate(new Timestamp(System
//					.currentTimeMillis()));
//			reimbursement.setBillingApprovedAmount(preauthDTO
//					.getBillingApprovedAmount() != null ? preauthDTO
//					.getBillingApprovedAmount() : 0d);
//
//			Double provisionAmt = 0d;
//			if (preauthDTO.getHospitalizaionFlag()
//					|| preauthDTO.getIsHospitalizationRepeat()
//					|| preauthDTO.getPartialHospitalizaionFlag()) {
//				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
//						.getPayableToHospitalAmt() != null ? preauthDTO
//						.getHospitalizationCalculationDTO()
//						.getPayableToHospitalAmt() : 0d;
//				provisionAmt += preauthDTO.getHospitalizationCalculationDTO()
//						.getPayableToInsAmt() != null ? preauthDTO
//						.getHospitalizationCalculationDTO()
//						.getPayableToInsAmt() : 0d;
//			}
//			provisionAmt += preauthDTO.getPreHospitalizationCalculationDTO()
//					.getPayableToInsAmt() != null ? preauthDTO
//					.getPreHospitalizationCalculationDTO().getPayableToInsAmt()
//					.doubleValue() : 0d;
//			provisionAmt += preauthDTO.getPostHospitalizationCalculationDTO()
//					.getPayableToInsAmt() != null ? preauthDTO
//					.getPostHospitalizationCalculationDTO()
//					.getPayableToInsAmt().doubleValue() : 0d;
//			provisionAmt += preauthDTO.getPatientCareAmt();
//			provisionAmt += preauthDTO.getHospitalCashAmt();
//
//			// List<ReimbursementBenefits> reimbursementBenefitsByRodKey =
//			// getReimbursementBenefitsByRodKey(reimbursement.getKey());
//			// if(reimbursementBenefitsByRodKey != null) {
//			// for (ReimbursementBenefits reimbursementBenefits :
//			// reimbursementBenefitsByRodKey) {
//			// provisionAmt += reimbursementBenefits.getTotalClaimAmountBills()
//			// != null ? reimbursementBenefits.getTotalClaimAmountBills() : 0d;
//			//
//			// }
//			// }
//			reimbursement.setCurrentProvisionAmt(provisionAmt);
//
//			entityManager.merge(reimbursement);
//			entityManager.flush();

			if (true) {
				ResidualAmount residualAmt = new ResidualAmount();
				residualAmt.setKey(preauthDTO.getResidualAmountDTO().getKey());
				residualAmt.setTransactionKey(reimbursement.getKey());
				residualAmt.setStage(reimbursement.getStage());
				residualAmt.setStatus(reimbursement.getStatus());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setAmountConsideredAmount(preauthDTO
						.getResidualAmountDTO().getAmountConsideredAmount());
				residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO()
						.getMinimumAmount());
				residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO()
						.getCopayAmount());
				residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO()
						.getApprovedAmount());
				residualAmt.setCopayPercentage(preauthDTO
						.getResidualAmountDTO().getCopayPercentage());
				residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
						.getRemarks());
				residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO()
						.getNetAmount() != null ? preauthDTO
						.getResidualAmountDTO().getNetAmount() : 0d);
				residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d);
				
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
				log.info("------ResidualAmount------>"+residualAmt+"<------------");
			}
			
			Double hospitalCashPayableAmt = 0d;
			Double patientCarePayableAmt = 0d;

			if (null != preauthDTO.getPreauthDataExtractionDetails()
					.getAddOnBenefitsDTOList()
					&& !preauthDTO.getPreauthDataExtractionDetails()
							.getAddOnBenefitsDTOList().isEmpty()) {
				UploadDocumentDTO uploadDTO = preauthDTO
						.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO();
				for (AddOnBenefitsDTO benefitsDTO : preauthDTO
						.getPreauthDataExtractionDetails()
						.getAddOnBenefitsDTOList()) {

					// ReimbursementBenefits reimbursementHospBenefits =
					// ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
					if ((ReferenceTable.HOSPITAL_CASH)
							.equalsIgnoreCase(benefitsDTO.getParticulars())) {
						// ReimbursementBenefits reimbursementHospBenefits =
						// ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(uploadDTO
								.getHospitalBenefitKey());

						/*
						 * reimbursementBenefits.setKey(reimbursementBenefits.getKey
						 * ()); reimbursementBenefits.setReimbursementKey(
						 * reimbursementBenefits.getReimbursementKey());
						 */
						reimbursementBenefits
								.setTreatmentForPhysiotherapy(uploadDTO
										.getTreatmentPhysiotherapyFlag());

						reimbursementBenefits.setNumberOfDaysBills(Long
								.valueOf(uploadDTO.getHospitalCashNoofDays()));
						reimbursementBenefits.setPerDayAmountBills(Double
								.valueOf(uploadDTO.getHospitalCashPerDayAmt()));
						reimbursementBenefits.setTotalClaimAmountBills(Double
								.valueOf((uploadDTO
										.getHospitalCashTotalClaimedAmt())));
						reimbursementBenefits
								.setBenefitsFlag(reimbursementBenefits
										.getBenefitsFlag());
						
						reimbursementBenefits = populateBenefitsData(uploadDTO,
								reimbursementBenefits, benefitsDTO);
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursementBenefits.setTotalClaimAmountBills(0d);
							reimbursementBenefits.setTotalAmount(0d);
							reimbursementBenefits.setPayableAmount(0d);
						}
						
						entityManager.merge(reimbursementBenefits);
						entityManager.flush();
						
						if(reimbursementBenefits.getNetAmount() != null){
							hospitalCashPayableAmt += reimbursementBenefits.getNetAmount();
						}
						
						log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
						// entityManager.refresh(reimbursementHospBenefits);

					}

					else if ((ReferenceTable.PATIENT_CARE)
							.equalsIgnoreCase(benefitsDTO.getParticulars())) {

						ReimbursementBenefits reimbursementBenefits = getReimbursementBefitsByKey(preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientBenefitKey());

						/*
						 * reimbursementBenefits.setKey(reimbursementBenefits.getKey
						 * ()); reimbursementBenefits.setReimbursementKey(
						 * reimbursementBenefits.getReimbursementKey());
						 */
						reimbursementBenefits
								.setTreatmentForPhysiotherapy(uploadDTO
										.getTreatmentPhysiotherapyFlag());

						reimbursementBenefits.setNumberOfDaysBills(Long
								.valueOf(uploadDTO.getPatientCareNoofDays()));
						reimbursementBenefits.setPerDayAmountBills(Double
								.valueOf(uploadDTO.getPatientCarePerDayAmt()));
						reimbursementBenefits.setTotalClaimAmountBills(Double
								.valueOf((uploadDTO
										.getPatientCareTotalClaimedAmt())));
						reimbursementBenefits
								.setBenefitsFlag(reimbursementBenefits
										.getBenefitsFlag());
						
						reimbursementBenefits = populateBenefitsData(uploadDTO,
								reimbursementBenefits, benefitsDTO);
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							reimbursementBenefits.setTotalClaimAmountBills(0d);
							reimbursementBenefits.setTotalAmount(0d);
							reimbursementBenefits.setPayableAmount(0d);
						}

						entityManager.merge(reimbursementBenefits);
						entityManager.flush();
						
						if(reimbursementBenefits.getNetAmount() != null){
							patientCarePayableAmt += reimbursementBenefits.getNetAmount();
						}
						
						log.info("------ReimbursementBenefits------>"+reimbursementBenefits+"<------------");
						// entityManager.refresh(reimbursementHospBenefits);

						List<ReimbursementBenefitsDetails> reimbBenefitsList = getPatientCareTableValues(preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientBenefitKey());

						List<PatientCareDTO> patientList = preauthDTO
								.getPreauthDataExtractionDetails()
								.getUploadDocumentDTO().getPatientCareDTO();

						if (null != reimbBenefitsList
								&& !reimbBenefitsList.isEmpty()
								&& null != patientList
								&& !patientList.isEmpty()) {
							for (ReimbursementBenefitsDetails benefitsDetails : reimbBenefitsList) {

								for (PatientCareDTO patientCareDTO : patientList) {

									if (benefitsDetails.getKey().equals(
											patientCareDTO.getKey())) {
										benefitsDetails
												.setEngagedFrom(patientCareDTO
														.getEngagedFrom());
										benefitsDetails
												.setEngagedTo(patientCareDTO
														.getEngagedTo());

										entityManager.merge(benefitsDetails);
										entityManager.flush();
										log.info("------ReimbursementBenefitsDetails------>"+benefitsDetails+"<------------");

										break;
									}
								}

							}
						}

						/*
						 * if(null != patientList && !patientList.isEmpty()) {
						 * for (PatientCareDTO patientCareDTO : patientList) {
						 * ReimbursementBenefitsDetails
						 * reimbursementBenefitDetails = new
						 * ReimbursementBenefitsDetails();
						 * reimbursementBenefitDetails
						 * .setEngagedFrom(patientCareDTO.getEngagedFrom());
						 * reimbursementBenefitDetails
						 * .setEngagedTo(patientCareDTO.getEngagedTo());
						 * reimbursementBenefitDetails
						 * .setReimbursementBenefits(reimbursementHospBenefits);
						 * 
						 * entityManager.merge(reimbursementBenefitDetails);
						 * entityManager.flush();
						 * //entityManager.refresh(reimbursementBenefitDetails);
						 * } }
						 */

					}
					
					/** Below code is added for MED-PRD-058 - Classic Health Group. 
					  Based on this change in BSI View current provision is not included both hospital cash and patient care.
					  This is  applicable only for this product*/
					
					if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						if(null != hospitalCashPayableAmt)
						{
							reimbursement.setAddOnCoversApprovedAmount(hospitalCashPayableAmt);
						}

						if(null != patientCarePayableAmt)
						{
							reimbursement.setOptionalApprovedAmount(patientCarePayableAmt);
						}
						
						if(reimbursement.getStatus().getKey()
								.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
							reimbursement.setAddOnCoversApprovedAmount(0d);
							reimbursement.setOptionalApprovedAmount(0d);
							//resetReimbursementBenefitAmount(reimbursement);
						}
					}
					entityManager.merge(reimbursement);
					entityManager.flush();
				}
			}

			// saveAddOnBenefitsValues(preauthDTO, reimbursement);
			//IMSSUPPOR-27169------IMSSUPPOR-28781
			if(!preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS) && !preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) && !preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS)){
				saveCalculationValues(preauthDTO, reimbursement, /*preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS) || */ReferenceTable.PAYMENT_SETTLED.equals(preauthDTO.getStatusKey()) ? true : isCancelROD);
			}
			
			
			List<BillEntryDetailsDTO> hospitalizationTableList = preauthDTO.getHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> preHospitalizationTableList = preauthDTO.getPreHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> postHospitalizationTableList = preauthDTO.getPostHospitalizationTabSummaryList();
			List<PreHospitalizationDTO> prePostHospList = new ArrayList<PreHospitalizationDTO>();
			
			if (null != hospitalizationTableList && !hospitalizationTableList.isEmpty())
			{
				for (BillEntryDetailsDTO billEntryDetailsDTO : hospitalizationTableList) {
					billEntryDetailsDTO.setBillingCompletedDate(reimbursement.getBillingCompletedDate());
				}
			}
			
			if (null != preHospitalizationTableList && !preHospitalizationTableList.isEmpty())
			{
				for (PreHospitalizationDTO preHospitalizationDTO : preHospitalizationTableList) {
					preHospitalizationDTO.setFaCompletedDate(reimbursement.getFinancialCompletedDate());
					prePostHospList.add(preHospitalizationDTO);
				}
			}
			
			if (null != postHospitalizationTableList && !postHospitalizationTableList.isEmpty())
			{
				for (PreHospitalizationDTO postHospitalizationDTO : postHospitalizationTableList) {
					postHospitalizationDTO.setFaCompletedDate(reimbursement.getFinancialCompletedDate());
					prePostHospList.add(postHospitalizationDTO);
				}
			}
			
			saveBillSummaryRemarksValues(hospitalizationTableList,prePostHospList,SHAConstants.FINANCIAL);

			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS))
			{
					
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) 
						&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
						&& ((reimbursement.getPatientStatus() != null 
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
							|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId())))) {

						savePaymentDetailsForNomineeLegalHeir(preauthDTO, reimbursement);
					}
					else {
						ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
		//				ZonalMedicalReviewMapper.getAllMapValues();
						ClaimPayment claimPayment = mapper.getClaimPaymentObject(preauthDTO);
						//claimPayment.setRodkey(reimbursement);
						//acknowledgementDocumentReceivedService.getReimbursementApprovedAmount(reimbursement.getKey());
						
						claimPayment.setSelectCount("1/1");
						
						if(reimbursement.getProcessClaimType() == null){
							claimPayment.setProcessClaimType("H");
						}else {
							claimPayment.setProcessClaimType(reimbursement.getProcessClaimType());
						}
						//added for jira IMSSUPPOR-27488 BSI issue
						if(reimbursement.getBenefitsId() != null){
							claimPayment.setBenefitsId(reimbursement.getBenefitsId().getKey());
						}
						if(reimbursement.getBenApprovedAmt() != null){
							claimPayment.setPaBenefitApproveAmount(reimbursement.getBenApprovedAmt());
						}
						if(reimbursement.getUnNamedKey() != null){
							claimPayment.setUnNamedKey(reimbursement.getUnNamedKey());
						}
						
						if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null 
								&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {
							//below line  commented due to TCSSQS-3491
							//claimPayment.setSourceRiskId(preauthDTO.getPreauthDataExtractionDetails().getPayeeName().getSourceRiskId() != null ? preauthDTO.getPreauthDataExtractionDetails().getPayeeName().getSourceRiskId() : "");
							claimPayment.setSourceRiskId(preauthDTO.getNewIntimationDTO().getInsuredPatient().getSourceRiskId() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getSourceRiskId() : "");
							
						}	
							
						if(preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
								&& preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
								&& (ReferenceTable.RECEIVED_FROM_INSURED).equals(preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
								&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {
							claimPayment.setPayeeName(preauthDTO.getPreauthDataExtractionDetails().getNameAsPerBank() != null ? preauthDTO.getPreauthDataExtractionDetails().getNameAsPerBank() : preauthDTO.getPreauthDataExtractionDetails().getPayeeName().getValue());
							claimPayment.setAccountPreference(preauthDTO.getPreauthDataExtractionDetails().getAccountPref() != null ? preauthDTO.getPreauthDataExtractionDetails().getAccountPref() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccPreference() : null));
							claimPayment.setAccountNumber(preauthDTO.getPreauthDataExtractionDetails().getAccountNo() != null ? preauthDTO.getPreauthDataExtractionDetails().getAccountNo() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccNumber() : null));
							claimPayment.setAccountType(preauthDTO.getPreauthDataExtractionDetails().getAccType() != null ?  preauthDTO.getPreauthDataExtractionDetails().getAccType() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccType() : null));
							claimPayment.setMicrCode(preauthDTO.getDto() != null ? preauthDTO.getDto().getMicrCode() : null);
							
						}
								
						claimPayment.setPayeeRelationship(preauthDTO.getPreauthDataExtractionDetails().getPayeeName() != null ? preauthDTO.getPreauthDataExtractionDetails().getPayeeName().getRelationshipWithProposer() : "");
						
						claimPayment.setApprovedAmount( reimbursement.getFinancialApprovedAmount());
						claimPayment.setBenefitsApprovedAmt(reimbursement.getOtherBenefitApprovedAmt());
						if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							if(reimbursement.getCorporateUtilizedAmt() != null && reimbursement.getCorporateUtilizedAmt() >0){
								claimPayment.setCorporateBufferLimit(reimbursement.getCorporateUtilizedAmt() != null ? reimbursement.getCorporateUtilizedAmt().doubleValue() : 0d);
							}
							else if(reimbursement.getWintageBufferUtilAmt() != null && reimbursement.getWintageBufferUtilAmt() >0){
								claimPayment.setCorporateBufferLimit(reimbursement.getWintageBufferUtilAmt().doubleValue());
							}
							else if(reimbursement.getNacBufferUtilAmt() != null && reimbursement.getNacBufferUtilAmt() >0){
								claimPayment.setCorporateBufferLimit(reimbursement.getNacBufferUtilAmt().doubleValue());
							}else{
								claimPayment.setCorporateBufferLimit(0d);
							}
						}
						
						claimPayment.setInstallmentDeductionFlag(SHAConstants.N_FLAG);
						claimPayment.setPremiaWSFlag(SHAConstants.YES_FLAG);
						
						claimPayment.setTotalApprovedAmount(claimPayment.getApprovedAmount());
						if((preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY))) {
							if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
								claimPayment.setPremiumAmt(reimbursement.getPremiumAmt());
								Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
								claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
								claimPayment.setInstallmentDeductionFlag(SHAConstants.YES_FLAG);
								claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
							} 
						}else if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) {
							if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
								claimPayment.setPremiumAmt(reimbursement.getPremiumAmt());
								Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
								claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
								claimPayment.setInstallmentDeductionFlag(SHAConstants.YES_FLAG);
								claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
							} 
						}
						//claimPayment.setRodkey(reimbursement);
						
						if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
		//					claimPayment.setApprovedAmount(preauthDTO.getRevisedProvisionAmount());
							claimPayment.setPremiumAmt(preauthDTO.getShouldDetectPremium() ? preauthDTO.getUniquePremiumAmount() : 0);
							Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
							claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
							claimPayment.setInstallmentDeductionFlag(preauthDTO.getShouldDetectPremium() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
							claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
						}else if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
		//					claimPayment.setApprovedAmount(preauthDTO.getRevisedProvisionAmount());
							claimPayment.setPremiumAmt(preauthDTO.getShouldDetectPremium() ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0);
							Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
							claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
							claimPayment.setInstallmentDeductionFlag(preauthDTO.getShouldDetectPremium() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
							claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
						}
												
						claimPayment.setReasonForChange(preauthDTO.getPreauthDataExtractionDetails().getReasonForChange());
//						claimPayment.setLegalHeirName(preauthDTO.getPreauthDataExtractionDetails().getLegalFirstName());
						
						if(null != preauthDTO.getPreauthDataExtractionDetails().getPayModeChangeReason()){
							claimPayment.setPayModeChangeReason(preauthDTO.getPreauthDataExtractionDetails().getPayModeChangeReason());
						}
						
						if(preauthDTO.getNewIntimationDTO().getHospitalDto() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getKey() != null){
							MasHospitals hospitalById = getMasHospitalById(preauthDTO.getNewIntimationDTO().getHospitalDto().getKey());
							claimPayment.setHospital(hospitalById);
						}
						
						if(null != preauthDTO && null != preauthDTO.getNewIntimationDTO().getCpuCode())
						{  
							//TmpCPUCode cpuCode = new TmpCPUCode();
							//cpuCode.setCpuCode(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode()));
							claimPayment.setCpuCode(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode()));
						}
						
						claimPayment.setFaApprovalDate(new Timestamp(System.currentTimeMillis()));
						if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
						{
							claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
							claimPayment.setAccountNumber(null);
							claimPayment.setBankCode(null);
							claimPayment.setIfscCode(null);
							claimPayment.setBankName(null);
							claimPayment.setBranchName(null);
							claimPayment.setPayableAt(preauthDTO.getPreauthDataExtractionDetails().getPayableAt());
						}
						else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
						{
							
							claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
							claimPayment.setPayableAt(null);
						}
						claimPayment.setCreatedBy(preauthDTO.getStrUserName());
						claimPayment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						
						DocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
						
						if(null != docAck.getDocumentReceivedFromId())//.equals(ReferenceTable.RECEIVED_FROM_INSURED))
						{
							claimPayment.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
						}
						
						if(("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalisationFlag())
								&& ("N").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPreHospitalisationFlag())
								&& ("N").equalsIgnoreCase(docAck.getPostHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalCashFlag())
								&& ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
								)
						{
							claimPayment.setLumpsumFlag("Y");
						}
							
							
						claimPayment.setPaymentCpuCode(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode()));
						
						if(null != docAck && null != docAck.getDocumentReceivedFromId() 
								&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) && 
								currentClaim != null && currentClaim.getClaimType() != null && currentClaim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
							/**
							 * As per Sathish Sir comments, following lines are commented.
							 */
		//						(SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getHospitalisationFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPartialHospitalisationFlag())
		//						&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPreHospitalisationFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPostHospitalisationFlag())
		//						&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getHospitalizationRepeatFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getHospitalCashFlag())
		//						&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPatientCareFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getLumpsumAmountFlag())
		//						)
						{
							claimPayment.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
						}
						else
						{
							claimPayment.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
							
							Boolean jioPolicyDetails = false;
							if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								jioPolicyDetails = getJioPolicyDetails(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
							}
							if(currentClaim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && !jioPolicyDetails){
								OrganaizationUnit masBranchOffice = getInsuredOfficeNameByDivisionCode(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());
								if(masBranchOffice != null && masBranchOffice.getCpuCode() != null){
									claimPayment.setPaymentCpuCode(Long.valueOf(masBranchOffice.getCpuCode()));
								}
							}
						}
						
						Status status = new Status();
						NEFTQueryDetails neftQueryDetails = getReimbursementByKeyForNEFTDetails(reimbursement.getKey());
						//Below condition added for physical doc verifciation by noufel
						
						PhysicalDocumentVerification physicalVerification = getReimbursementByKeyForPhysicalVerifcation(reimbursement.getKey());
						 if((preauthDTO.getStatusKey() != null  && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) && physicalVerification != null && (physicalVerification.getCommunicationEnableFlag() == null || physicalVerification.getCommunicationEnableFlag().equalsIgnoreCase(SHAConstants.N_FLAG))){
						
							status.setKey(ReferenceTable.PAYMENT_HOLD_STATUS);
							claimPayment.setStatusId(status);
							
							Stage stage = new Stage();
							stage.setKey(ReferenceTable.PHYSICAL_VERIFICATION_STAGE);
							claimPayment.setStageId(stage);
							
						}else if((preauthDTO.getStatusKey() != null  && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) && neftQueryDetails != null && neftQueryDetails.getStatus().getKey().equals(ReferenceTable.NEFT_STATUS_PENDING_KEY) && (neftQueryDetails.getReminder2Flag() == null || neftQueryDetails.getReminder2Flag().equalsIgnoreCase(SHAConstants.N_FLAG))){
							
								status.setKey(ReferenceTable.NEFT_STATUS_PENDING_KEY);
								claimPayment.setStatusId(status);
								
								Stage stage = new Stage();
								stage.setKey(ReferenceTable.PAYMENT_PROCESS_STAGE);
								claimPayment.setStageId(stage);
						}else {
							
							status.setKey(ReferenceTable.PAYMENT_NEW_STATUS);
							claimPayment.setStatusId(status);
							
							Stage stage = new Stage();
							stage.setKey(ReferenceTable.PAYMENT_PROCESS_STAGE);
							claimPayment.setStageId(stage);
							
						}
						
						/*claimPayment.setLotStatus(status);*/
						
						MastersValue paymentStatus = new MastersValue();
						paymentStatus.setKey(ReferenceTable.PAYMENT_STATUS_FRESH);
						
						claimPayment.setPaymentStatus(paymentStatus);
						claimPayment.setBatchHoldFlag(SHAConstants.N_FLAG);
						
						/**
						 * Below code added for saving only benefits approved 
						 * amt in claim payment table. This is done, since prakash
						 * had asked to save it for BSI calculation .
						 * */
						Double benefitsApprovedAmt = 0d;
						if(null != preauthDTO.getPatientCareAmt())
							benefitsApprovedAmt += preauthDTO.getPatientCareAmt();
						if(null !=  preauthDTO.getHospitalCashAmt())
							benefitsApprovedAmt += preauthDTO.getHospitalCashAmt();
						claimPayment.setBenefitsApprovedAmt(benefitsApprovedAmt);
						
						if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							
							if(null != preauthDTO.getPatientCareAmt()){
								claimPayment.setAddOnCoversApprovedAmount(preauthDTO.getHospitalCashAmt());
							}
							
							if(null !=  preauthDTO.getHospitalCashAmt()){
								claimPayment.setOptionalApprovedAmount(preauthDTO.getHospitalCashAmt());
							}
						}
						
						if(null != reimbursement && null != reimbursement.getDocAcknowLedgement())
						{
							claimPayment.setLastAckDate(reimbursement.getDocAcknowLedgement().getDocumentReceivedDate());
							claimPayment.setReconisderFlag(reimbursement.getReconsiderationRequest() != null ? reimbursement.getReconsiderationRequest() : "N");
							if(null != reimbursement.getReconsiderationRequest() && ("Y").equalsIgnoreCase(reimbursement.getReconsiderationRequest()))
								{
									String rodNo = reimbursement.getRodNumber();
									/**
									 * Only if the payment cancellation is Yes, the last ack date will be
									 * overrirdden with the least settled rod for the given rod number.
									 * If no, then the lastest ack date of the rod will be mapped. This is done
									 * already in above code. Hence else if is not required.
									 * */
									if(null != reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() && ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPaymentCancellationFlag()))
									{
										ClaimPayment objClaimPayment = getSettledClaimPaymentByRODNo(rodNo);
										if(null != objClaimPayment)
										{
											claimPayment.setLastAckDate(objClaimPayment.getLastAckDate());
										}
										else
										{									
											DocAcknowledgement documentAck = getDocumentDetailsByRodKey(reimbursement.getKey());
											if(null != documentAck)
											{
												claimPayment.setLastAckDate(documentAck.getDocumentReceivedDate());
											}
										}
									}
								}
									
						}
						
						if(null != claimPayment.getFaApprovalDate())
						{
							Calendar faDate = Calendar.getInstance();
						      faDate.setTime(claimPayment.getFaApprovalDate());
						      String irdaPlusDays = BPMClientContext.getFAPlusDays();
						      faDate.add(Calendar.DATE, Integer.valueOf(irdaPlusDays));
						      Date faApproveDate = faDate.getTime();
						      faDate.setTimeInMillis(faApproveDate.getTime());
						      long dateDiff = SHAUtils.getDaysBetweenDate(faApproveDate,claimPayment.getLastAckDate());
						      claimPayment.setDelayDays(dateDiff);
						      
						      BPMClientContext bpmClientContext = new BPMClientContext();
						      String irdaTatDays = bpmClientContext.getIRDATATDays();
						      if(null != irdaTatDays && !irdaTatDays.isEmpty() && null != claimPayment.getDelayDays())
						      {
						    	 long exceedsNoOfDays = claimPayment.getDelayDays() - Long.parseLong(irdaTatDays);
						    	 claimPayment.setAllowedDelayDays(exceedsNoOfDays);
						      }
						}
						
						if(null != reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey() &&
								(ReferenceTable.getGMCProductList().containsKey(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
							
							List<String> employeeName = dbCalculationService.getGMCEmployeeName(reimbursement.getClaim().getIntimation().getKey());
							if(null != employeeName && !employeeName.isEmpty()){
								claimPayment.setGmcEmployeeName(employeeName.get(1));
							}
						}
						
						claimPayment.setProductName(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getValue());
		//				Boolean canCreatePayment = true;
						if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
							canCreatePayment = false;
						}else if((preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d) && preauthDTO.getIsPremiumInstAmtEql() != null && preauthDTO.getIsPremiumInstAmtEql()) {
							canCreatePayment = false;
						} 
						
						
						if(!canCreatePayment) {
							log.info("???????<<<<<<< AS PER 'BA'TEAM UNIQUE POLICY APPROVED AMOUNT IS ZERO .. SO CLAIM PAYMENT IS SETTED AS PAYMENT STATUS......>>>>>>???????");
							status.setKey(ReferenceTable.PAYMENT_SETTLED);
							claimPayment.setLotNumber("PREMIUM_DEDUCTION");
							claimPayment.setBatchNumber("PREMIUM_DEDUCTION");
							claimPayment.setStatusId(status);
							claimPayment.setPaymentDate(new Date());
						}
						
						if(reimbursement.getClaim().getIntimation().getCpuCode() != null && reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode() != null){
							OrganaizationUnit branchCode = getBranchCode(reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode().toString());
							if(branchCode != null){
								if(null != docAck && null != docAck.getDocumentReceivedFromId() 
										&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
									Hospitals hospitalById = getHospitalById(reimbursement.getClaim().getIntimation().getHospital());
									String emailId2 = branchCode.getEmailId2();
									if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
										String val = emailId2.replaceAll("reimb.delhi@starhealth.in", "paayas@starhealth.in");
										emailId2 = val;
									}
									if(emailId2 != null && hospitalById != null && hospitalById.getEmailId() != null){
										emailId2 = hospitalById.getEmailId()+","+emailId2;
									}else if(hospitalById != null && hospitalById.getEmailId() != null){
										emailId2 = hospitalById.getEmailId();
									}
									claimPayment.setZonalMailId(emailId2);
								}/*else{
									claimPayment.setZonalMailId(branchCode.getEmailId() +", "+SHAConstants.REIMBURSEMENT_PAYMENT_MAIL_ID);
								}*/
								
							}else{
								/**
								 * As per sathis sir, For payaas policy, default email id is set to be payment email
								 */
								if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
									Hospitals hospitalById = getHospitalById(reimbursement.getClaim().getIntimation().getHospital());
									String emailId2 = "paayas@starhealth.in";
									
									if(emailId2 != null && hospitalById != null && hospitalById.getEmailId() != null){
										emailId2 = hospitalById.getEmailId()+","+emailId2;
									}else if(hospitalById != null && hospitalById.getEmailId() != null){
										emailId2 = hospitalById.getEmailId();
									}
									claimPayment.setZonalMailId(emailId2);
								}
							}
						}
						/**
						 * commented.
						 */
						try{
							
							if(null != docAck && null != docAck.getDocumentReceivedFromId() 
									&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
							
							if(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode() != null){
								String zonalEmailId = "";
								//OrganaizationUnit zonalMailBasedOnBranchType = getZonalMailBasedOnBranchType(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode(), ReferenceTable.BRANCH_TYPE_ID);
								OrganaizationUnit zonalMailBasedOnBranchType = getBranchCode(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode());
								if(zonalMailBasedOnBranchType != null){
									zonalEmailId = zonalMailBasedOnBranchType.getEmailId2();
									if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
										String val = zonalEmailId.replaceAll("reimb.delhi@starhealth.in", "paayas@starhealth.in");
										zonalEmailId = val;
									}
									
									
									/*if(zonalMailBasedOnBranchType.getParentOrgUnitKey() != null){
									OrganaizationUnit areaMailBasedOnType = getZonalMailBasedOnBranchType(zonalMailBasedOnBranchType.getParentOrgUnitKey().toString(), ReferenceTable.AREA_BRANCH_TYPE_ID);
										if(areaMailBasedOnType != null){
											zonalEmailId = areaMailBasedOnType.getEmailId();
											if(areaMailBasedOnType.getParentOrgUnitKey() != null){
												OrganaizationUnit zoneMailBasedOnType = getZonalMailBasedOnBranchType(areaMailBasedOnType.getParentOrgUnitKey().toString(), ReferenceTable.ZONE_BRANCH_TYPE_ID);
												if(zoneMailBasedOnType != null){
													zonalEmailId = zoneMailBasedOnType.getEmailId();
												}
											}
										}
									}*/
									
								}
								claimPayment.setZonalMailId(zonalEmailId /*+", "+SHAConstants.REIMBURSEMENT_PAYMENT_MAIL_ID*/);
							}
							}
						}catch(Exception e){
							log.info("Issuing office code update issue"+reimbursement.getClaim().getIntimation().getIntimationId());
							e.printStackTrace();
						}
						claimPayment.setRodkey(reimbursement.getKey());	
						
						String vnrFlag = null;
						if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) && 
								((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))){
							
							vnrFlag = callPaymentFunDeDupProcedure(preauthDTO.getNewIntimationDTO().getIntimationId(),preauthDTO.getPreauthDataExtractionDetails().getAccountNo(),
									  preauthDTO.getPreauthDataExtractionDetails().getIfscCode(),preauthDTO.getPayeeName() != null ? preauthDTO.getPayeeName().toUpperCase():null,
											preauthDTO.getLegalHeirDto().getHeirName() != null ?preauthDTO.getLegalHeirDto().getHeirName().toUpperCase():null);
							
							if(null != vnrFlag){
								claimPayment.setVnrFlag(vnrFlag);
							}
							
						}else{
							claimPayment.setVnrFlag(SHAConstants.VERIFICATION_NOT_REQUIRED);
						}
						
						if(reimbursement.getDateOfDeath() != null)
						{
							claimPayment.setDeathDate(reimbursement.getDateOfDeath());
						}
						if(preauthDTO.getClaimDTO().getLegalClaim() !=null
								&& preauthDTO.getClaimDTO().getLegalClaim().equals("Y")){
							if(preauthDTO.getLegalBillingDTO() !=null
									&& preauthDTO.getLegalBillingDTO().getTotalApprovedAmount() !=null){
								Double approvedAmt = preauthDTO.getLegalBillingDTO().getTotalApprovedAmount().doubleValue();
								claimPayment.setApprovedAmount(approvedAmt);
								claimPayment.setTotalApprovedAmount(approvedAmt);
								claimPayment.setClaimTotAmt(approvedAmt.longValue());
							}		
							claimPayment.setTdsAmt(preauthDTO.getLegalBillingDTO().getTdsAmount());
							claimPayment.setTotalIntAmt(preauthDTO.getLegalBillingDTO().getTotalInterestAmount());
							claimPayment.setClaimTotalnoofdays(preauthDTO.getLegalBillingDTO().getTotalnoofdays());
							claimPayment.setTdsPercentge(preauthDTO.getLegalBillingDTO().getTdsPercentge());
							claimPayment.setCompensationAmt(preauthDTO.getLegalBillingDTO().getCompensation());
							claimPayment.setLegalCost(preauthDTO.getLegalBillingDTO().getCost());					
						}
						entityManager.persist(claimPayment);
						entityManager.flush();
						
		//				if(canCreatePayment) {
		//					entityManager.persist(claimPayment);
		//					entityManager.flush();
		//					
		//					log.info("<------CLAIM PAYMENT KEY ------>"+ claimPayment.getKey()+"<------------");
		//				} else {
		//					log.info("-*#(#&#&$#&$#PAYMENT IS NOT CREATED &*#*#*#&#*------>"+ (preauthDTO.getUniqueDeductedAmount() != null ? preauthDTO.getUniqueDeductedAmount().toString() : "0" +"<------------"));
		//				}
						
					}
			}
			
			if((ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
						(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))))
						{
				 List<OtherBenefitsTableDto> otherBenefitsList = preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList();
				  
				    if(otherBenefitsList != null && ! otherBenefitsList.isEmpty()){
				    	ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
				    	List<BenefitAmountDetails> otherBenefitsAmountDetails = reimbursementMapper.getOtherBenefitsAmountDetails(otherBenefitsList);
				    	for (BenefitAmountDetails benefitAmountDetails : otherBenefitsAmountDetails) {
							benefitAmountDetails.setClaim(reimbursement.getClaim());
							benefitAmountDetails.setInsured(reimbursement.getClaim().getIntimation().getInsured());
							benefitAmountDetails.setStage(reimbursement.getStage());
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
			
			/*HashMap dataMap = preauthDTO.getFilePathAndTypeMap();

			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) 
			{
			dataMap.put("intimationNumber",reimbursement.getClaim().getIntimation().getIntimationId());
			dataMap.put("claimNumber",reimbursement.getClaim().getClaimId());
			dataMap.put("createdBy", preauthDTO.getStrUserName());
			//dataMap.put(SHAConstants.ROD_NUMBER,reimbursement.getRodNumber());
			
			
			if(null != reimbursement.getClaim().getClaimType())
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(reimbursement.getClaim().getClaimType().getKey()))
					{
						Preauth preauth = getPreauthClaimKey(reimbursement.getClaim().getKey());
						if(null != preauth && dataMap != null)
							dataMap.put("cashless", preauth.getPreauthId());
						dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
					}
				else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(reimbursement.getClaim().getClaimType().getKey()) && dataMap != null)
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
				
				*//**
				 * As per lakshmi's comment, discharge voucher is been removed
				 * from FA screen. Hence we will not upload the same to DMS.
				 * *//*
				
				
				String dischargeVoucherFilePath = (String)dataMap.get("DischargeVoucherFilePath");
				String dischargeVoucherDocType = (String) dataMap.get("DischargeVoucherDocType");
				dataMap.put("filePath", dischargeVoucherFilePath);
				dataMap.put("docType", dischargeVoucherDocType);
				uploadGeneratedLetterToDMS(dataMap);
			}
			}*/
			
			String outcome = getOutComeForFinancial(reimbursement);
			
			if(reimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS) && preauthDTO.isDirectToAssignInv()){
				outcome = SHAConstants.OUTCOME_DRAFT_INVESTIGATION;
			}
			//setBPMForClaimBilling(preauthDTO, false, outcome, reimbursement);
			
			
			submitBillingAndFATaskToDBForClaim(preauthDTO, false, outcome, reimbursement);
			if(preauthDTO.getClaimDTO().getLegalClaim() !=null && preauthDTO.getClaimDTO().getLegalClaim().equals("Y")){
				if(preauthDTO.getLegalBillingDTO() !=null){
					String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
					LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
					LegalTaxDeduction taxDeduction = taxDeductionMapper.getLegalTaxDeductionFromDTO(preauthDTO.getLegalBillingDTO());
					taxDeduction.setIntimationKey(preauthDTO.getNewIntimationDTO().getKey());
					taxDeduction.setRodKey(reimbursement.getKey());
					taxDeduction.setClaimKey(reimbursement.getClaim().getKey());
					if(taxDeduction.getKey()!=null){
						taxDeduction.setModifyBy(userId);
						taxDeduction.setModifiedDate(new Date());
						entityManager.merge(taxDeduction);
						entityManager.flush(); 
					}else{
						taxDeduction.setCreatedBy(userId);
						taxDeduction.setCreatedDate(new Date());
						taxDeduction.setActiveStatus(1L);
						entityManager.persist(taxDeduction);			
						entityManager.flush();
					}
				}
			}
			return reimbursement;
		/*} catch (Exception e) {
			e.printStackTrace();
			Notification.show(e.getMessage());
			log.error(e.toString());
<<<<<<< HEAD
			throw e;
		}*/
		
//=======
//		}
//		return null;*/
//>>>>>>> finalmigration

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
	
	  private OrganaizationUnit getBranchCode(String branchCode)
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
	  
	  private OrganaizationUnit getZonalMailBasedOnBranchType(String branchCode,Long branchTypeId)
	    {
	    	Query query = entityManager.createNamedQuery("OrganaizationUnit.findByBasedOnBranchType");
	    	query = query.setParameter("parentKey", branchCode);
	    	query = query.setParameter("branchTypeId", branchTypeId);
	    	List<OrganaizationUnit> orgList = query.getResultList();
	    	if(null != orgList && !orgList.isEmpty())
	    	{
	    		return orgList.get(0);
	    	}
	    	return null;
	    }
	
	public Long getReimbursementApprovedAmount(Long reimbursementKey){
		
		Long approvedAmount = 0l;
		
		List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = getReimbursementCalculationDetails(reimbursementKey);
		
		if(reimbursementCalculationDetails != null){
			for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
				
				if(reimbursementCalCulationDetails2.getBillClassificationId() != null 
						&& reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
					if(reimbursementCalCulationDetails2.getPayableToHospital() != null){
						if(reimbursementCalCulationDetails2.getPayableToHospital()>0){
						 approvedAmount += reimbursementCalCulationDetails2.getPayableToHospital();
						}
					}
					if(reimbursementCalCulationDetails2.getPayableToInsured() != null){
						if(reimbursementCalCulationDetails2.getPayableToInsured()>0){
							 approvedAmount += reimbursementCalCulationDetails2.getPayableToInsured();
							}
					}
				}else {
					if(reimbursementCalCulationDetails2.getPayableToInsured() != null){
						approvedAmount += reimbursementCalCulationDetails2.getPayableToInsured();
					}
				}
			}
		}
		
		List<ReimbursementBenefits> reimbursmentBenefits = getReimbursmentBenefits(reimbursementKey);
		
		if(reimbursmentBenefits != null){
			for (ReimbursementBenefits reimbursementBenefits : reimbursmentBenefits) {
			     if(reimbursementBenefits.getPayableAmount() != null){
			    	 approvedAmount += reimbursementBenefits.getPayableAmount().longValue();
			     }
			}
		}
		return approvedAmount;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReimbursementCalCulationDetails> getReimbursementCalculationDetails(Long reimbursementKey){
		
		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
		query = query.setParameter("reimbursementKey", reimbursementKey);
		
		List<ReimbursementCalCulationDetails> reimbursmentCalculationDetails = (List<ReimbursementCalCulationDetails>) query.getResultList();
		
		return reimbursmentCalculationDetails;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<ReimbursementBenefits> getReimbursmentBenefits(Long reimbursementKey){
		
		Query query = entityManager.createNamedQuery(
				"ReimbursementBenefits.findByRodKey").setParameter("rodKey", reimbursementKey);
		
		List<ReimbursementBenefits> reimbursmentBenefits = (List<ReimbursementBenefits>) query.getResultList();
		return reimbursmentBenefits;

	}
	
	/*private BankMaster getBankMaster(String ifscCode)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> bankMasterList = query.getResultList();
		if(null != bankMasterList && !bankMasterList.isEmpty())
		{
			return bankMasterList.get(0);
		}
		else
		{
			return null;
		}
	}
	*/
	public void uploadGeneratedLetterToDMS(WeakHashMap dataMap)
	{
		String filePath = (String)dataMap.get("filePath");
		if(null != filePath && !("").equalsIgnoreCase(filePath))
		{
			SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
		}
	}

	public void setBPMForClaimBilling(PreauthDTO dto,
			Boolean isClaimBilling, String outCome, Reimbursement reimbursement) {/*

		if (isClaimBilling) {
			SubmitProcessClaimBillingTask submitTask = BPMClientContext
					.getClaimBillingTask(dto.getStrUserName(),
							dto.getStrPassword());

			HumanTask humanTask = dto.getRodHumanTask();
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)) {
				PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				humanTask.setOutcome(outCome);
				
				humanTask.setPayload(payloadForCancelROD);
			} else {
			PayloadBOType payloadBO = humanTask.getPayload();
			humanTask.setOutcome(outCome);
			ClaimRequestType claimRequestType = payloadBO.getClaimRequest();
			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR) ) {
				claimRequestType.setClientType(null);
				claimRequestType.setReferTo(outCome);
				claimRequestType.setResult(outCome);
				claimRequestType.setReimbReqBy("BILLING");
			}else if (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
			{
				claimRequestType.setClientType("ACK");
				claimRequestType.setReferTo(outCome);
				claimRequestType.setResult(outCome);
				claimRequestType.setReimbReqBy("BILLING");
			}
			
			else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {

				claimRequestType.setClientType("MEDICAL");
				payloadBO.setInvestigation(null);
				payloadBO.setFieldVisit(null);
				payloadBO.setQuery(null);
				
				*//**
				 * This is added as a part of Training env issue. 
				 * *//*

				claimRequestType.setReferTo(null);
				claimRequestType.setReimbReqBy("BILLING");
				claimRequestType.setResult("MEDICAL");
				
				ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();
				if(processActorInfo == null){
					processActorInfo = new ProcessActorInfoType();
					processActorInfo.setEscalatedByRole("");
					processActorInfo.setEscalatedByUser("");
					payloadBO.setProcessActorInfo(processActorInfo);
				}

				CustomerType custType = payloadBO.getCustomer();
				if (custType == null) {
					custType = new CustomerType();
					if (dto.getPreauthDataExtractionDetails()
							.getTreatmentType() != null) {
						custType.setTreatmentType(dto
								.getPreauthDataExtractionDetails()
								.getTreatmentType().getValue());
					} else {
						custType.setTreatmentType("");
					}

					custType.setSpeciality(dto.getSpecialityName());
					payloadBO.setCustomer(custType);
				}
			}
			else
			{
				claimRequestType.setResult(null);
				claimRequestType.setReferTo(null);
			}

				payloadBO.setClaimRequest(claimRequestType);
				
				ClassificationType classification = payloadBO.getClassification();
				
				if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey() != null){
					Status status = entityManager.find(Status.class, reimbursement.getStatus().getKey());
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(status.getProcessValue());
					}
//					classification.setSource(status.getProcessValue());
				}
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)){
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}
//					classification.setSource(stage.getStageName());
				}
				if (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
				{
					Stage  stage = getStageByKey(ReferenceTable.BILLING_STAGE);
					if(null != stage)
						classification.setSource(stage.getStageName());
				}
				
				payloadBO.setClassification(classification);
				
				Double claimedAmount = 0d;
				if(reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount();
				} 
				if(reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount();
				}
				
				if(reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount();
				}

				PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
				if(paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				}
					
				paymentInfo.setClaimedAmount(dto.getAmountConsidered() != null ? SHAUtils
						.getDoubleValueFromString(dto.getAmountConsidered())
						: 0d);
				paymentInfo.setApprovedAmount(isClaimBilling ? reimbursement.getBillingApprovedAmount() : reimbursement.getFinancialApprovedAmount());
				payloadBO.setPaymentInfo(paymentInfo);
				
				RRCType rrc = payloadBO.getRrc();
				if(rrc == null){
				   rrc = new RRCType();
				   Date currentDate = reimbursement.getCreatedDate();
				   rrc.setToDate(SHAUtils.changeDatetoString(currentDate));
				}
				payloadBO.setRrc(rrc);


				humanTask.setPayload(payloadBO);
		}
			try{
			submitTask.execute(dto.getStrUserName(), humanTask);
			
				if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
					
//					CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//					cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
					
				}
			
			}catch(Exception e){
				e.printStackTrace();
//				log.error(e.toString());
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN FINANCIAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR FA APPROVAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					submitTask.execute("claimshead", humanTask);

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR (#*#&*#*#*#*#*#*#");
				}
			}
		} else {

			SubmitProcessClaimFinancialsTask submitTask = BPMClientContext
					.getClaimFinancialTask(dto.getStrUserName(),
							dto.getStrPassword());

			HumanTask humanTask = dto.getRodHumanTask();
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)) {
				PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				humanTask.setOutcome(outCome);
				humanTask.setPayload(payloadForCancelROD);
			} else {
				PayloadBOType payloadBO = humanTask.getPayload();
				payloadBO.getClaimRequest().setClientType(null);
				payloadBO.getClaimRequest().setReferTo(null);
				humanTask.setOutcome(outCome);
				ClaimRequestType claimRequestType = payloadBO.getClaimRequest();
				
				CustomerType customer = payloadBO.getCustomer();

				if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");
					InvestigationType investigation = new InvestigationType();
					if (dto.getInvestigationKey() != null) {
						investigation.setKey(dto.getInvestigationKey());
						payloadBO.setInvestigation(investigation);
					}
				} else if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");
					FieldVisitType fieldVisit = new FieldVisitType();
					
					
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
							fieldVisit.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							claimRequestType.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							}
						}
					}
					
					if(dto.getNewIntimationDTO().getAdmissionDate() != null){
						String intimDate = SHAUtils.formatIntimationDateValue(dto.getNewIntimationDTO().getAdmissionDate());
						RRCType rrcType = new RRCType();
						rrcType.setFromDate(intimDate);
						payloadBO.setRrc(rrcType);
					}
					
					if (dto.getFvrKey() != null) {
						fieldVisit.setKey(dto.getFvrKey());
						payloadBO.setFieldVisit(fieldVisit);
					}
				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
					claimRequestType.setClientType("MEDICAL");
					payloadBO.setInvestigation(null);
					payloadBO.setFieldVisit(null);
					payloadBO.setQuery(null);
					claimRequestType.setReimbReqBy("FA");
					claimRequestType.setResult("MEDICAL");
					CustomerType custType = payloadBO.getCustomer();
					if (custType == null) {
						custType = new CustomerType();
						if (dto.getPreauthDataExtractionDetails()
								.getTreatmentType() != null) {
							custType.setTreatmentType(dto
									.getPreauthDataExtractionDetails()
									.getTreatmentType().getValue());
						} else {
							custType.setTreatmentType("");
						}
                        if(dto.getSpecialityName() != null){
                        	custType.setSpeciality(dto.getSpecialityName());
                        }
                        
						payloadBO.setCustomer(custType);
						
						ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();
						if(processActorInfo == null){
							processActorInfo = new ProcessActorInfoType();
							processActorInfo.setEscalatedByUser("");
							processActorInfo.setEscalatedByRole("");
							payloadBO.setProcessActorInfo(processActorInfo);
						}
					}

				}
				*//**
				 * Added for refer to bill entry screen.
				 * *//*
				else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY)) {
					claimRequestType.setClientType("ACK");
					payloadBO.setInvestigation(null);
					payloadBO.setFieldVisit(null);
					payloadBO.setQuery(null);
					claimRequestType.setReimbReqBy("FA");
					claimRequestType.setResult(SHAConstants.BILL_ENTRY_OUTCOME);
					humanTask.setOutcome(outCome);
				}  
				else if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("MEDICAL");
					claimRequestType.setReferTo(outCome);
					claimRequestType.setResult("MEDICAL");
					claimRequestType.setReimbReqBy("FA");

				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
					payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");
					QueryType queryType = new QueryType();
					if (dto.getQueryKey() != null) {
						queryType.setKey(dto.getQueryKey());
						queryType.setIsQueryPending(true);
						queryType.setIsQueryReplyReceived(false);
						payloadBO.setQuery(queryType);
					}
					claimRequestType.setResult("QUERY");
				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REJECT_STATUS)
						|| reimbursement.getStatus().getKey()
								.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
					claimRequestType.setClientType("POSTMEDICAL");
				}
				*//**
				 * If approve is clicked from FA, the below
				 * attributes are set to null, so that task gets
				 * approved.
				 * *//*
				else
				{
					claimRequestType.setResult(null);
					claimRequestType.setReferTo(null);
				}

				claimRequestType.setReimbReqBy("FA");
				claimRequestType.setResult(outCome);
				// claimRequestType.setOption(outCome);

				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
					if (null != claimRequestType.getOption()
							&& SHAConstants.FINANCIAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())
							|| SHAConstants.REMEDICAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())) {
						claimRequestType
								.setOption(SHAConstants.REMEDICAL_REBILLING);
						claimRequestType.setClientType("");
					} else {
						claimRequestType
								.setOption(SHAConstants.FINANCIAL_REMEDICAL);
						claimRequestType.setClientType("");
					}
				}

				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
					if (claimRequestType.getOption() != null
							&& SHAConstants.FINANCIAL_REMEDICAL
									.equalsIgnoreCase(claimRequestType.getOption())
							|| SHAConstants.REMEDICAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())) {
						claimRequestType
								.setOption(SHAConstants.REMEDICAL_REBILLING);
					} else {
						claimRequestType
								.setOption(SHAConstants.FINANCIAL_REBILLING);
					}
				}

				if (dto.getStatusKey().equals(
						ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
					if (dto.getPreauthMedicalDecisionDetails()
							.getSpecialistType() != null) {
						claimRequestType.setReferTo(dto
								.getPreauthMedicalDecisionDetails()
								.getSpecialistType().getValue());
						if(customer == null){
							customer = new CustomerType();
						}
						
						if(dto.getPreauthMedicalDecisionDetails().getSpecialistType() != null){
							customer.setSpeciality(dto.getPreauthMedicalDecisionDetails().getSpecialistType().getValue());
							payloadBO.setCustomer(customer);
						}
						
						claimRequestType.setReferTo(dto.getStrUserName());
					}

				}

				if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)) {
					claimRequestType.setResult("MEDICAL");
				}

				payloadBO.setClaimRequest(claimRequestType);
				
				ClassificationType classification = payloadBO.getClassification();
				
              if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}
					
					
				}
              
              if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
            		  || reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){

					Status status = entityManager.find(Status.class, reimbursement.getStatus().getKey());
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(status.getProcessValue());
					}
					
				}
              
              if (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
				{
            	  Stage  stage = getStageByKey(ReferenceTable.FINANCIAL_STAGE);
					if(null != stage)
						classification.setSource(stage.getStageName());
				}
              
              Double claimedAmount = 0d;
				if(reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount();
				} 
				if(reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount();
				}
				
				if(reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount();
				}
              
              PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
              
				if(paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				} 
				paymentInfo.setClaimedAmount(claimedAmount);
				paymentInfo.setApprovedAmount(isClaimBilling ? reimbursement.getBillingApprovedAmount() : reimbursement.getFinancialApprovedAmount());
				payloadBO.setPaymentInfo(paymentInfo);
              
              
              payloadBO.setClassification(classification);

				humanTask.setPayload(payloadBO);
			}
			try{
			submitTask.execute(dto.getStrUserName(), humanTask);
			
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
				
//				CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//				cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
				
			}
			
			}catch(Exception e){
				e.printStackTrace();
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN FINANCIAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR FA APPROVAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					submitTask.execute("claimshead", humanTask);
					
					
				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR (#*#&*#*#*#*#*#*#");
				}
				
			}

		}
	*/}

	public Claim searchByClaimKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
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

	public Double getTotalBilledAmount(List<Long> documentSummaryList) {
		Double totalAmount = 0d;
		
	    if(! documentSummaryList.isEmpty()){
			Query query = entityManager
					.createNamedQuery("RODBillDetails.findSumOfAmount");
			query.setParameter("doumentSummaryIds", documentSummaryList);
			totalAmount = (Double) query.getSingleResult();
			
			
			
	    }
		return totalAmount;
		
	}
	
	public Double getTotalBilledAmountForHosp(List<Long> documentSummaryList) {
		Double totalAmount = 0d;
		
	    if(! documentSummaryList.isEmpty()){
			Query query = entityManager
					.createNamedQuery("RODBillDetails.findHospSumOfAmount");
			query.setParameter("doumentSummaryIds", documentSummaryList);
			query.setParameter("billClassificationId", ReferenceTable.HOSPITALIZATION);
			totalAmount = (Double) query.getSingleResult();

	    }
		return totalAmount;
		
	}

	public List<ClaimVerification> getClaimVerificationByReimbKey(
			Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("ClaimVerification.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementKey);
		List<ClaimVerification> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (ClaimVerification claimVerification : resultList) {
				entityManager.refresh(claimVerification);
			}
		}
		if (!resultList.isEmpty()) {
			return resultList;
		} else {

		}
		return null;
	}

	public DocAcknowledgement getAcknowledgementByKey(Long key) {
		DocAcknowledgement find = entityManager.find(DocAcknowledgement.class,
				key);
		return find;
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

	public List<ReimbursementBenefitsDetails> getPatientCareTableByBenefitKey(
			Long benefitKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query.setParameter("benefitsKey", benefitKey);
		List<ReimbursementBenefitsDetails> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (ReimbursementBenefitsDetails reimbursementBenefits : resultList) {
				entityManager.refresh(reimbursementBenefits);
			}
		}
		return resultList;
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

	public List<FVRGradingMaster> getFVRGrading() {
		Query query = entityManager
				.createNamedQuery("FVRGradingMaster.findAll");
		List<FVRGradingMaster> singleResult = query.getResultList();
		if (singleResult != null) {
			for (FVRGradingMaster fvrGradingMaster : singleResult) {
				entityManager.refresh(fvrGradingMaster);
			}

		}

		return singleResult;
	}

	public ResidualAmount getResidulaAmount(Long transactionKey) {
		Query query = entityManager
				.createNamedQuery("ResidualAmount.findByTransactionKey");
		query.setParameter("transactionKey", transactionKey);
		List<ResidualAmount> singleResult = query.getResultList();
		if (singleResult != null) {
			for (ResidualAmount residualAmt : singleResult) {
				entityManager.refresh(residualAmt);
			}

		}
		if (!singleResult.isEmpty()) {
			return singleResult.get(0);
		}
		return null;
	}

	public List<FieldVisitRequest> getFVRByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("FieldVisitRequest.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<FieldVisitRequest> resultList = query.getResultList();

		return resultList;
	}

	public MedicalApprover getMedicalApprover(Long reimbursementkey) {
		Query query = entityManager
				.createNamedQuery("MedicalApprover.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementkey);
		List<MedicalApprover> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (MedicalApprover medicalApprover : resultList) {
				entityManager.refresh(medicalApprover);
			}

			return resultList.get(0);
		}
		return null;
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

	public List<MedicalApprover> getMedicalApproverList(Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("MedicalApprover.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementKey);
		List<MedicalApprover> resultList = query.getResultList();

		for (MedicalApprover medicalApprover : resultList) {
			entityManager.refresh(medicalApprover);
		}

		return resultList;
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
	
	
//	public Long getLatestMedicalApproverForClaimRequest(Long reimbursementKey){
//		
//		Query query = entityManager
//				.createNamedQuery("MedicalApprover.findByReimbursementKey");
//		query.setParameter("reimbursementKey", reimbursementKey);
//		List<MedicalApprover> resultList = query.getResultList();
//		List<Long> keysList = new ArrayList<Long>();
//		for (MedicalApprover medicalApprover : resultList) {
//			entityManager.refresh(medicalApprover);
//			if (medicalApprover.getRecordType() != null
//					&& SHAConstants.BILLING
//							.equalsIgnoreCase(medicalApprover.getRecordType())) {
//				keysList.add(medicalApprover.getKey());
//			}
//		}
//
//		if (!keysList.isEmpty()) {
//			Long key = Collections.max(keysList);
//			return key;
//		}
//
//		return null;
//	}

	public Long getLatestMedicalApproverForRebilling(Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("MedicalApprover.findByReimbursementKey");
		query.setParameter("reimbursementKey", reimbursementKey);
		List<MedicalApprover> resultList = query.getResultList();
		List<Long> keysList = new ArrayList<Long>();
		for (MedicalApprover medicalApprover : resultList) {
			entityManager.refresh(medicalApprover);
			if (medicalApprover.getRecordType() != null
					&& SHAConstants.FINANCIAL_REBILLING
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

	public PreauthEscalate getEscalateByClaimKey(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("PreauthEscalate.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<PreauthEscalate> escalateList = (List<PreauthEscalate>) query
				.getResultList();
		for (PreauthEscalate preauthEscalate : escalateList) {
			entityManager.refresh(preauthEscalate);
		}

		if (!escalateList.isEmpty()) {
			return escalateList.get(0);
		} else {
			return null;
		}
	}
	
	public Specialist getSpecialistByClaimKey(Long claimKey){
		
		Query query = entityManager
				.createNamedQuery("Specialist.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Specialist> resultList = (List<Specialist>) query
				.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
		
	}

	public FieldVisitRequest getFVRByKey(Long fvrKey) {
		Query query = entityManager
				.createNamedQuery("FieldVisitRequest.findByKey");
		query.setParameter("primaryKey", fvrKey);
		FieldVisitRequest singleResult = (FieldVisitRequest) query
				.getSingleResult();

		return singleResult;
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

	public Reimbursement savePreauthValues(PreauthDTO preauthDTO,
			Boolean isZonalReview,String screenName) {

		ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();
		Reimbursement reimbursement = reimbursementMapper
				.getReimbursement(preauthDTO);
		reimbursement.setRejectionCategoryId(preauthDTO.getPreauthMedicalDecisionDetails().getRejectionCategory() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getRejectionCategory().getId() : null);
		reimbursement.setRejSubCategoryId(preauthDTO.getPreauthMedicalDecisionDetails().getRejSubCategory() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getRejSubCategory().getId() : null);
		reimbursement.setActiveStatus(1l);
		reimbursement.setFaDocumentVerifiedFlag((preauthDTO.getPreauthMedicalDecisionDetails().getOriginalBillsReceived()) ? "Y" : "N");
		
		if (preauthDTO.getHospitalDiscountFlag() != null){
		reimbursement.setHospitalDiscountFlag(preauthDTO.getHospitalDiscountFlag());
		}
		
		
		Boolean isCancelROD = ReferenceTable.CANCEL_ROD_KEYS.containsKey(preauthDTO.getStatusKey());
		if (null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(
				ReferenceTable.FINANCIAL_REJECT_STATUS)) {
			Status status = getStatusByKey(preauthDTO.getStatusKey());
			String strUserName = preauthDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			reimbursement.setModifiedBy(userNameForDB);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setStatus(status);
		}
		//GLX2020076
		if(preauthDTO.getPreauthMedicalDecisionDetails().getIcdExclusionReason()!=null){
			reimbursement.setReasonForIcdExclusion(preauthDTO.getPreauthMedicalDecisionDetails().getIcdExclusionReason());
		}
		//Cr2019169
		if(null != preauthDTO.getPreauthDataExtractionDetails() && preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			reimbursement.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
		}
		
		if(null != preauthDTO.getPreauthDataExtractionDetails() && preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			reimbursement.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
		}
		
		if(null != preauthDTO.getPreauthDataExtractionDetails() && preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			reimbursement.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		}
		
		//GLX2020132
		/*if(null != preauthDTO.getPreauthDataExtractionDetails() && preauthDTO.getPreauthDataExtractionDetails().getEmergenyFlag() != null) {
			reimbursement.setEmergencyFlag(preauthDTO.getPreauthDataExtractionDetails().getEmergenyFlag());
		}*/
		
		if(preauthDTO.getNewIntimationDTO() != null && preauthDTO.getNewIntimationDTO().getHospitalDto() != null &&
				 preauthDTO.getNewIntimationDTO().getHospitalDto().getExclusionProvideFlag() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getExclusionProvideFlag().equals(SHAConstants.YES_FLAG)){
			reimbursement.setEmergencyFlag(SHAConstants.YES_FLAG);
		}
		 
		if(preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport() != null){
			System.out.println(String.format("Ventilator Support value in Preauth Service method: [%s]", preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport()));
			//String VentilatorSupportFlag = preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport().toString().equalsIgnoreCase("true") ? "Y":"N";
			String VentilatorSupportFlag = preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport()? "Y":"N";
			reimbursement.setVentilatorSupport(VentilatorSupportFlag);
		}
		
		Claim searchByClaimKey2 = searchByClaimKey(preauthDTO.getClaimKey());
		reimbursement.setClaim(searchByClaimKey2);
		Claim currentClaim = null;

		if (reimbursement.getClaim() != null) {
			currentClaim = searchByClaimKey(reimbursement.getClaim().getKey());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null){
					currentClaim.setGmcCorpBufferFlag(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() ? "Y":"N");
					if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
						currentClaim.setGmcCorpBufferLmt(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim().doubleValue());
					}
				}
			}
			
			
			currentClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(currentClaim);
			
		}

		if (preauthDTO.getPreauthPreviousClaimsDetails()
				.getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO
					.getPreauthPreviousClaimsDetails()
					.getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			log.info("------Claim------>"+searchByClaimKey+"<------------");
		}
		
		// new product 076 Sumbit For hospcash change flow
		if(preauthDTO.getReceiptOfDocumentsDTO() != null &&(preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy()
				.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)))){
			if(preauthDTO != null && preauthDTO.getReceiptOfDocumentsDTO() != null){

				reimbursement.setFaDocumentVerifiedFlag((preauthDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getOriginalBillsReceived()) ? "Y" : "N");

				//add for new imple
				if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getPatientDayCare() 
						&& preauthDTO.getPreauthDataExtractionDetails().getPatientDayCare().equals(true)){
					reimbursement.setPhcDayCareFlag(SHAConstants.YES_FLAG);
					if(preauthDTO.getPreauthDataExtractionDetails().getPatientDayCareDueTo().getId() != null){
						reimbursement.setPhcDayCareID(preauthDTO.getPreauthDataExtractionDetails().getPatientDayCareDueTo().getId());
					}
				}
				else{
					reimbursement.setPhcDayCareFlag(SHAConstants.N_FLAG);
				}
				if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getHospitalCashDueTo() && null != preauthDTO.getPreauthDataExtractionDetails().getHospitalCashDueTo().getId()){

					reimbursement.setProdBenefitDueToID(preauthDTO.getPreauthDataExtractionDetails().getHospitalCashDueTo().getId());
				}
				
				savehospitalcash(preauthDTO);
			}
		}

		if (null != reimbursement.getStatus().getKey() && (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
				|| reimbursement.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
			//String processflag = "A";
			if (preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null
					&& preauthDTO.getPreauthMedicalDecisionDetails()
					.getInitialTotalApprovedAmt() != null
					&& preauthDTO.getPreauthDataExtractionDetails()
					.getTotalApprAmt() > preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getInitialTotalApprovedAmt()) {
				//processflag = "R";
			}

			reimbursement.setApprovedAmount(preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getInitialTotalApprovedAmt() != null ? preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInitialTotalApprovedAmt() : 0d);
			
			// add for host cash flow change 
			if(preauthDTO.getReceiptOfDocumentsDTO() != null &&(preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy()
					.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)))){
				reimbursement.setApprovedAmount(preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null ?
						preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() : 0d);
			}
		}
		if (preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			if (preauthDTO.getPreauthDataExtractionDetails()
					.getAutoRestoration().toLowerCase().contains("n/a")) {
				reimbursement.setAutomaticRestoration("O");
			} else {
				reimbursement.setAutomaticRestoration(preauthDTO
						.getPreauthDataExtractionDetails().getAutoRestoration()
						.toLowerCase().contains("not") ? "N" : "Y");
			}
		}

		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE during
		 * policy refractoring activity. Below code is added for inserting value
		 * in the insured key column.
		 * */
		// reimbursement.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								||  SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
				|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))))
				{
			reimbursement.setOtherBenefitApprovedAmt(preauthDTO.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt());
		}
		
		if(screenName.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)){
			reimbursement = updateZonalReviewRemarks(preauthDTO, reimbursement);
		}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
			//if(null != preauthDTO.getIsParallelInvFvrQuery() && !(preauthDTO.getIsParallelInvFvrQuery())){
				reimbursement = updateClaimRequestRemarks(preauthDTO, reimbursement,currentClaim); 
			//}//TODO  CREATE NEW SECOND FVR 
				
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) 
						&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
						&& ((reimbursement.getPatientStatus() != null 
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
							|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))) { 
					
					if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
							!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
						saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), reimbursement);
					}					
					else{
							saveLegalHeirAndDocumentDetailsFromFA(preauthDTO);
					}	
				}
				
				/*for bancs*/
				if(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
					
					reimbursement.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
				
				}
				
				if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
					reimbursement.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
					
				}
				
				if(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
						
					reimbursement.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
					
				}
				
				//added for setting catarct flag in reimbursemen table
				List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
						.getPreauthDataExtractionDetails().getDiagnosisTableList();
				List<Long> icdCodeList = new ArrayList<Long>();
				// Iterate pedValidationTable List.
				if (!pedValidationTableList.isEmpty()) {
					for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
						if(pedValidationDTO != null && pedValidationDTO.getIcdCode() != null){
						icdCodeList.add(pedValidationDTO.getIcdCode().getId());
						}
					}
				}
				 if(! icdCodeList.isEmpty()){
					   Boolean isCovidSelected = getCovidSelectedorNot(icdCodeList);
					   Boolean isCataractSelected = getCataractSelectedorNot(icdCodeList);
					   if(isCovidSelected){
						   reimbursement.setBillingAutoAllocIcdFlag("C");   
					   }
					    else if(isCataractSelected){
						   reimbursement.setBillingAutoAllocIcdFlag(SHAConstants.YES_FLAG);  
					   }else {
						   reimbursement.setBillingAutoAllocIcdFlag("N");
					   }
				   }
				
		}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_BILLING)){
			reimbursement = updateClaimBillingRemarks(preauthDTO, reimbursement,currentClaim);
			if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag()) {
				Double billingApprovedAmount = reimbursement.getBillingApprovedAmount();
				billingApprovedAmount += (preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);
				reimbursement.setBillingApprovedAmount(billingApprovedAmount);	
			}
			// add for host cash flow change 
			if(preauthDTO.getReceiptOfDocumentsDTO() != null &&(preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy()
					.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)))){
				reimbursement.setBillingApprovedAmount(preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null ?
						preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() : 0d);
			}
			reimbursement.setBillingInternalRemarks(preauthDTO.getPreauthDataExtractionDetails().getBillingInternalRemarks());
			reimbursement.setFaInternalRemarks(preauthDTO.getPreauthDataExtractionDetails().getFaInternalRemarks());
			
			if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) 
					&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((reimbursement.getPatientStatus() != null 
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
						|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))) { 
				
				if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
						!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
					saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), reimbursement);
					if(ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId())){
						if(preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null
								&& !preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()){
							updateLegalHeir(preauthDTO);
						}
					}
				}					
				else{
						saveLegalHeirAndDocumentDetailsFromFA(preauthDTO);
				}	
			}
			if(!(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((reimbursement.getPatientStatus() != null 
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
										|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))))
			{
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
				{			
					reimbursement.setAccountNumber(null);
					reimbursement.setPayableAt(preauthDTO.getPreauthDataExtractionDetails().getPayableAt() != null ? preauthDTO.getPreauthDataExtractionDetails().getPayableAt() : preauthDTO.getPayableAt());
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
				{			
					
					reimbursement.setPayableAt(null);
				}

				populatePaymentDetails(reimbursement, preauthDTO);
							
			}
			
			
			
		}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL)){
			reimbursement = updateClaimFinancialRemarks(preauthDTO,reimbursement,currentClaim);   //TODO  CREATE NEW SECOND FVR 
			if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag()) {
				Double financialApprovedAmount = reimbursement.getFinancialApprovedAmount();
				if(preauthDTO.getShouldDetectPremium()) {
					financialApprovedAmount += (preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);
				}
				reimbursement.setFinancialApprovedAmount(financialApprovedAmount);
			}else if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && preauthDTO.getHospitalizaionFlag()) {
				Double financialApprovedAmount = reimbursement.getFinancialApprovedAmount();
				if(preauthDTO.getShouldDetectPremium()) {
					financialApprovedAmount += (preauthDTO.getPolicyInstalmentPremiumAmt() != null ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0d);
				}
				reimbursement.setFinancialApprovedAmount(financialApprovedAmount);
			}
			// add for host cash flow change 
			if(preauthDTO.getReceiptOfDocumentsDTO() != null &&(preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy()
					.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)))){
				
				reimbursement.setFinancialApprovedAmount(preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null ?
						preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() : 0d);

				reimbursement.setProdBenefitAmount(preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null ?
						preauthDTO.getReceiptOfDocumentsDTO().getTotalClaimedAmount() : 0d);
			}
			
			/*if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
				Double financialApprovedAmount = reimbursement.getFinancialApprovedAmount();
				if(reimbursement.getCorporateUtilizedAmt() != null){
					financialApprovedAmount += reimbursement.getCorporateUtilizedAmt();
				}
				reimbursement.setFinancialApprovedAmount(financialApprovedAmount);
			}*/
			reimbursement.setBillingInternalRemarks(preauthDTO.getPreauthDataExtractionDetails().getBillingInternalRemarks());
			reimbursement.setFaInternalRemarks(preauthDTO.getPreauthDataExtractionDetails().getFaInternalRemarks());
			
			if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) 
					&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((reimbursement.getPatientStatus() != null 
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
										|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))) { 
				
				if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
						!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
					saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), reimbursement);
					if(ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId())){
						if(preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null
								&& !preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()){
							updateLegalHeir(preauthDTO);
						}
					}
				}					
				else{
						saveLegalHeirAndDocumentDetailsFromFA(preauthDTO);
				}	
			}
			if(!(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((reimbursement.getPatientStatus() != null 
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
										|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))))
			{
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
				{			
					reimbursement.setAccountNumber(null);
					reimbursement.setPayableAt(preauthDTO.getPreauthDataExtractionDetails().getPayableAt() != null ? preauthDTO.getPreauthDataExtractionDetails().getPayableAt() : preauthDTO.getPayableAt());
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
				{			
					
					reimbursement.setPayableAt(null);
				}

				populatePaymentDetails(reimbursement, preauthDTO);
			
			}
			if(preauthDTO.getScreenName() != null 
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(preauthDTO.getScreenName()) ||
					(preauthDTO.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName()))
					|| (preauthDTO.getScreenName() != null && SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName()))) {
				String strUserName = preauthDTO.getStrUserName();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
				reimbursement.setBillingFaAutoAllocSuBUser(userNameForDB);
				if(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(preauthDTO.getScreenName())){
					reimbursement.setBillingFaAutoAllocSuBType("CBFA");
				}
				else if(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName())){
					reimbursement.setBillingFaAutoAllocSuBType("CBFAA");
				}
				else if(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName())){
					reimbursement.setBillingFaAutoAllocSuBType("FAAA");
				}
				reimbursement.setBillingFaAutoAllocSuBDate(new Timestamp(System.currentTimeMillis()));
			}
		}
		//MA auto allocation save process
		if(preauthDTO.getScreenName() != null 
				&& SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equalsIgnoreCase(preauthDTO.getScreenName())) {
			String strUserName = preauthDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			reimbursement.setClaimRequestAutoAllocSuBUser(userNameForDB);
				reimbursement.setClaimRequestAutoAllocSuBType("MAPAA");
			reimbursement.setClaimRequestAutoAllocSuBDate(new Timestamp(System.currentTimeMillis()));
			
			//added for setting catarct flag in reimbursemen table
			List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			List<Long> icdCodeList = new ArrayList<Long>();
			// Iterate pedValidationTable List.
			if (!pedValidationTableList.isEmpty()) {
				for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
					if(pedValidationDTO != null && pedValidationDTO.getIcdCode() != null){
					icdCodeList.add(pedValidationDTO.getIcdCode().getId());
					}
				}
			}
			 if(! icdCodeList.isEmpty()){
				   Boolean isCovidSelected = getCovidSelectedorNot(icdCodeList);
				   Boolean isCataractSelected = getCataractSelectedorNot(icdCodeList);
				   if(isCovidSelected){
					   reimbursement.setBillingAutoAllocIcdFlag("C");   
				   }
				    else if(isCataractSelected){
					   reimbursement.setBillingAutoAllocIcdFlag(SHAConstants.YES_FLAG);  
				   }else {
					   reimbursement.setBillingAutoAllocIcdFlag("N");
				   }
			   }
		}else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
			String strUserName = preauthDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			reimbursement.setClaimRequestAutoAllocSuBUser(userNameForDB);
				reimbursement.setClaimRequestAutoAllocSuBType("MAPS");
			reimbursement.setClaimRequestAutoAllocSuBDate(new Timestamp(System.currentTimeMillis()));
		}
		/*for bancs*/
		if(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			
			reimbursement.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			reimbursement.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
			
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
				
			reimbursement.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
			
		}
		
		String strUserName = preauthDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
				
		
		if(isCancelROD) {
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
			reimbursement.setCauseOfLoss(null);
			reimbursement.setNatureOfLoss(null);
			reimbursement.setCatastrophicLoss(null);
		}
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
		

		if (screenName.equals(SHAConstants.CLAIM_REQUEST) ||screenName.equals(SHAConstants.CLAIM_BILLING) || screenName.equals(SHAConstants.CLAIM_FINANCIAL)) {
			if(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getKey() != null){
				
				docAck.setHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getHospitalizaionFlag()));
				docAck.setPreHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPreHospitalizaionFlag()));
				docAck.setPostHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPostHospitalizaionFlag()));
				docAck.setLumpsumAmountFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getLumpSumAmountFlag()));
				docAck.setPartialHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPartialHospitalizaionFlag()));
				docAck.setHospitalizationRepeatFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getIsHospitalizationRepeat()));
				docAck.setHospitalCashFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getAddOnBenefitsHospitalCash()));
				docAck.setPatientCareFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getAddOnBenefitsPatientCare()));
				reimbursement.setUpdatePaymentDtlsFlag(preauthDTO.getUpdatePaymentDtlsFlag());
			}
		}
		
		// Reason for reconsideration not editable  hence value can not be changed.
		
//		if (docAck.getReconsiderationRequest() != null && ("Y").equalsIgnoreCase(docAck.getReconsiderationRequest()) && screenName.equals(SHAConstants.ZONAL_REVIEW) || screenName.equals(SHAConstants.CLAIM_REQUEST) || screenName.equals(SHAConstants.CLAIM_FINANCIAL)) 
//		{
//			docAck.setReconsiderationReasonId(preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId());
//			entityManager.merge(docAck);
//			entityManager.flush();
//			reimbursement.setDocAcknowLedgement(docAck);
//		}
		
		
		if (screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST) || screenName.equals(SHAConstants.CLAIM_BILLING) || (reimbursement.getReconsiderationRequest() != null && ("N").equalsIgnoreCase(reimbursement.getReconsiderationRequest()) && screenName.equals(SHAConstants.CLAIM_FINANCIAL)))
		{
			if(preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					! preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					&& (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					! preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
				entityManager.merge(docAck);
				entityManager.flush();
				reimbursement.setDocAcknowLedgement(docAck);
			}

		}
		if(preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable() !=null){
			reimbursement.setImplantFlag(preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable() ? "Y" : "N");
		}
		if (reimbursement.getKey() != null) {
			
			if(null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
					log.info("<<<<<<<<<<<<<<<<< UNIQUE POLCIY APPROVED AMOUNT IS ZERO. SO STATUS WILL BE CAHNGED AS SETTLED STATUS >>>>>>>>>>>>>>>>>");
					Status status = new Status();
					status.setKey(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
					reimbursement.setStatus(status);
				}
			}else if(null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) 
					&& (preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) 
					&& (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d) && (preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
					&& preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null &&
					 preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))) {
//				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null) {
				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getIsPremiumInstAmtEql() != null && preauthDTO.getIsPremiumInstAmtEql()) {
					log.info("<<<<<<<<<<<<<<<<< INSTALLMENT POLCIY APPROVED AMOUNT IS ZERO. SO STATUS WILL BE CAHNGED AS SETTLED STATUS >>>>>>>>>>>>>>>>>");
					Status status = new Status();
					status.setKey(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
					reimbursement.setStatus(status);
					reimbursement.setCurrentProvisionAmt(0d);
				}
			}
			else if(null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) 
					&& (preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) 
					&& (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d) && (preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
					&& preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null &&
					 preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED))) {
				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() <= 0) {
//				if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getIsPremiumInstAmtEql() != null && preauthDTO.getIsPremiumInstAmtEql()) {
					log.info("<<<<<<<<<<<<<<<<< INSTALLMENT POLCIY APPROVED AMOUNT IS ZERO. SO STATUS WILL BE CAHNGED AS SETTLED STATUS >>>>>>>>>>>>>>>>>");
					Status status = new Status();
					status.setKey(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
					reimbursement.setStatus(status);
					reimbursement.setCurrentProvisionAmt(0d);
				}
			}
			
			reimbursement.setModifiedBy(userNameForDB);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setPremiumAmt(preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);

			//cr2019184
			if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			reimbursement.setPremiumAmt(preauthDTO.getPolicyInstalmentPremiumAmt() != null ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0d);
			}
			
			/*if (screenName.equals(SHAConstants.CLAIM_FINANCIAL)) {
				
				reimbursement.setFaHospClaimedAmount(preauthDTO.getPreauthDataExtractionDetails().getHospitalizationClaimedAmount());
				reimbursement.setFaPreHospClaimedAmount(preauthDTO.getPreauthDataExtractionDetails().getPreHospitalizationClaimedAmount());
				reimbursement.setFaPostHospClaimedAmount(preauthDTO.getPreauthDataExtractionDetails().getPostHospitalizationClaimedAmount());
				reimbursement.setFaOtherBenefitsAmount(preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitclaimedAmount());
				reimbursement.setFaDocumentVerifiedFlag(preauthDTO.getPreauthDataExtractionDetails().getDocumentVerificationFlag());
			}*/
			if(null != preauthDTO && null != preauthDTO.getIsFVRAlertOpened() && preauthDTO.getIsFVRAlertOpened()){
				reimbursement.setFvrAlertFlag(SHAConstants.YES_FLAG);
			}
			
			/*Below Code For - IMSSUPPOR-28291*/
			if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfTreatment() != null
					&& preauthDTO.getPreauthDataExtractionDetails().getNatureOfTreatment().getId().equals(ReferenceTable.NON_ALLOPATHIC_ID)){
				if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
					reimbursement.setNonAllopathicApprAmt(reimbursement.getApprovedAmount());
					Double diffAmt = 0d;
						if(preauthDTO.getOldNonAllopathicApprovedAmt() != null){
							diffAmt =  reimbursement.getApprovedAmount() - (preauthDTO.getOldNonAllopathicApprovedAmt() != null ? preauthDTO.getOldNonAllopathicApprovedAmt() : 0d);
							reimbursement.setNonAllopathicDiffAmt(diffAmt);
							//IMSSUPPOR-30066
							if(reimbursement.getApprovedAmount() != null && reimbursement.getApprovedAmount().equals(0d)){
								reimbursement.setNonAllopathicApprAmt(diffAmt);
							}
							} else {
								reimbursement.setNonAllopathicDiffAmt(diffAmt);
						}
					} else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_BILLING)){
						//IMSSUPPOR-34994
						if(reimbursement.getAddOnCoversApprovedAmount() != null){
							Double billingAmt = reimbursement.getBillingApprovedAmount() - reimbursement.getAddOnCoversApprovedAmount();
							reimbursement.setNonAllopathicApprAmt(billingAmt);
						} else {
							reimbursement.setNonAllopathicApprAmt(reimbursement.getBillingApprovedAmount());
						}
					Double diffAmt = 0d;
					if(reimbursement.getAddOnCoversApprovedAmount() != null){
						Double billingAmt = reimbursement.getBillingApprovedAmount() - reimbursement.getAddOnCoversApprovedAmount();
						diffAmt =  billingAmt - (preauthDTO.getOldNonAllopathicApprovedAmt() != null && preauthDTO.getOldNonAllopathicApprovedAmt() > 0d ? preauthDTO.getOldNonAllopathicApprovedAmt() : reimbursement.getApprovedAmount());
					} else {
					////IMSSUPPOR-30066
						diffAmt =  reimbursement.getBillingApprovedAmount() - (preauthDTO.getOldNonAllopathicApprovedAmt() != null && preauthDTO.getOldNonAllopathicApprovedAmt() > 0d ? preauthDTO.getOldNonAllopathicApprovedAmt() : reimbursement.getApprovedAmount());
					}
					if(preauthDTO.getIsReconsiderationRequest() != null && preauthDTO.getIsReconsiderationRequest()){
						reimbursement.setNonAllopathicDiffAmt(reimbursement.getBillingApprovedAmount());
					} else {
						reimbursement.setNonAllopathicDiffAmt(diffAmt);
					}
					} else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL)){
//						IMSSUPPOR-33243 - Below if condition is checking for refer to billing
						if(preauthDTO.getStatusKey() != null && !preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
							//IMSSUPPOR-34994
							if(reimbursement.getAddOnCoversApprovedAmount() != null){
								Double financialAmt = reimbursement.getFinancialApprovedAmount() - reimbursement.getAddOnCoversApprovedAmount();
								reimbursement.setNonAllopathicApprAmt(financialAmt);
							} else {
								reimbursement.setNonAllopathicApprAmt(reimbursement.getFinancialApprovedAmount());
							}
//							reimbursement.setNonAllopathicApprAmt(reimbursement.getFinancialApprovedAmount());
							Double diffAmt = 0d;
							if(reimbursement.getAddOnCoversApprovedAmount() != null){
								Double financialAmt = reimbursement.getFinancialApprovedAmount() - reimbursement.getAddOnCoversApprovedAmount();
								diffAmt =  financialAmt - (preauthDTO.getOldNonAllopathicApprovedAmt() != null && preauthDTO.getOldNonAllopathicApprovedAmt() > 0d ? preauthDTO.getOldNonAllopathicApprovedAmt() : reimbursement.getApprovedAmount());
							} else {
								diffAmt =  reimbursement.getFinancialApprovedAmount() - (preauthDTO.getOldNonAllopathicApprovedAmt() != null ? preauthDTO.getOldNonAllopathicApprovedAmt() : reimbursement.getBillingApprovedAmount());
							}
							reimbursement.setNonAllopathicDiffAmt(diffAmt);
						} else {
							reimbursement.setNonAllopathicApprAmt(0d);
							Double amount = 0d;
							Double diffAmt =  amount - (preauthDTO.getOldNonAllopathicApprovedAmt() != null ? preauthDTO.getOldNonAllopathicApprovedAmt() : reimbursement.getBillingApprovedAmount() !=null ? reimbursement.getBillingApprovedAmount() : 0d);
							reimbursement.setNonAllopathicDiffAmt(diffAmt);
						}
				}
			}
			
			//added for updating corp buffer amount in reimbursement table
			if (preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null 
					&& preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() !=null 
					&& preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer().equals(true)) {
				if(preauthDTO.getPreauthDataExtractionDetails().getBufferType() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_CB)){
					reimbursement.setCorporateUtilizedAmt(preauthDTO.getPreauthMedicalDecisionDetails().getCorporateBufferUtilizedAmt());
					reimbursement.setWintageBufferUtilAmt(0);
					reimbursement.setNacBufferUtilAmt(0);
				}else if(preauthDTO.getPreauthDataExtractionDetails().getBufferType() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_WINTAGE)){
					reimbursement.setWintageBufferUtilAmt(preauthDTO.getPreauthMedicalDecisionDetails().getCorporateBufferUtilizedAmt());
					reimbursement.setCorporateUtilizedAmt(0);
					reimbursement.setNacBufferUtilAmt(0);
				}else if(preauthDTO.getPreauthDataExtractionDetails().getBufferType() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_NACB)){
					reimbursement.setNacBufferUtilAmt(preauthDTO.getPreauthMedicalDecisionDetails().getCorporateBufferUtilizedAmt());
					reimbursement.setCorporateUtilizedAmt(0);
					reimbursement.setWintageBufferUtilAmt(0);
				}
				
				if(preauthDTO.getPreauthDataExtractionDetails().getBufferType() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getBufferType().equalsIgnoreCase(SHAConstants.PRC_BUFFERTYPE_NACB)){
					reimbursement.setNacBufferUtilAmt(preauthDTO.getPreauthDataExtractionDetails().getNacBufferUtilizedAmount());
				}
			}
			
			entityManager.merge(reimbursement);
			
		} else {

			String createdBy = preauthDTO.getStrUserName();
			if (preauthDTO.getStrUserName() != null
					&& preauthDTO.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						preauthDTO.getStrUserName(), 15);
			}
			reimbursement.setCreatedBy(createdBy);
			entityManager.persist(reimbursement);

		}
		    entityManager.flush();
		    log.info("------Reimbursement------>"+reimbursement+"<------------");
		    if(null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)) {
		    	Status status = new Status();
				status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
				reimbursement.setStatus(status);
		    }
		    
		if(reimbursement.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursement.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("y")) {
			isZonalReview = false;
		}
		
		if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_BILLING)){
			
//			entityManager.refresh(reimbursement);
			
			if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
				updateReferToMedicalApproverForBilling(preauthDTO, reimbursement,
						userNameForDB);
			}
			
		}
		
		if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
			if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)){
				
				String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
						.getApproverReply();
				updateMedicalApproverForClaimRequest(preauthDTO, reimbursement, remarks);
			}
		}
		
		if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL)){
			
			if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)){
				
				String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
						.getReasonForRefering();
				referToMedicalForFinancial(preauthDTO, reimbursement, remarks);
			}
			
			if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
				String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
						.getFinancialApproverRemarks();
				saveReferToBilling(preauthDTO, reimbursement, remarks);
			}
		}

		//IMSSUPPOR-27232
		if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL) && (null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
                ||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS)||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)
                ||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS)||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS)
                ||null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST))){
	        reimbursement.setFinancialApprovedAmount(0d);
	        reimbursement.setFinancialCompletedDate(null);
	        if(reimbursement.getKey() != null){
		        entityManager.merge(reimbursement);
		        entityManager.flush();        
	        
	        }
        }

		preauthDTO.setKey(reimbursement.getKey());

		preauthDTO.getCoordinatorDetails()
				.setPreauthKey(reimbursement.getKey());
		preauthDTO.getCoordinatorDetails().setIntimationKey(
				reimbursement.getClaim().getIntimation().getKey());
		preauthDTO.getCoordinatorDetails().setPolicyKey(
				reimbursement.getClaim().getIntimation().getPolicy().getKey());

		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = preauthDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		UpdateHospital updateHospital = reimbursementMapper
				.getUpdateHospital(updateHospitalDetails);
//		updateHospital.setPhoneNumber(updateHospitalDetails.gethospitalph);
		if (updateHospital != null) {
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
				coordinator.setPolicy(reimbursement.getClaim().getIntimation()
						.getPolicy());
				coordinator.setIntimation(reimbursement.getClaim()
						.getIntimation());
				coordinator.setTransactionKey(reimbursement.getKey());
				coordinator.setTransactionFlag("R");
				coordinator.setCreatedBy(userNameForDB);
				
				if (isZonalReview) {
					coordinator.setKey(null);
				}

				if (coordinator.getKey() != null) {
//					entityManager.merge(coordinator);
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
			String specialityName = "";
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				if (specialityDTO.getSpecialityType() != null) {
					specialityName += specialityDTO.getSpecialityType()
							.getValue() + ",";
				}
				Speciality speciality = reimbursementMapper
						.getSpeciality(specialityDTO);
				// speciality.setPreauth(preauth);
				speciality.setClaim(reimbursement.getClaim());
				speciality.setStage(reimbursement.getStage());
				speciality.setStatus(reimbursement.getStatus());
				// if(isZonalReview) {
				// speciality.setKey(null);
				// }

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
			}
			entityManager.flush();

			preauthDTO.setSpecialityName(specialityName);
		}

		entityManager.clear();

		Map<Long, String> keyMap = new HashMap<Long, String>();
		keyMap.put(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED,
				"coordinatorreply");
		keyMap.put(ReferenceTable.BILLING_REFER_TO_COORDINATOR, "coordinator");
		keyMap.put(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER, "medical");
		keyMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER,
				"billing");
		keyMap.put(ReferenceTable.FINANCIAL_APPROVE_STATUS,
				"financial");
		keyMap.put(ReferenceTable.BILLING_BENEFITS_APPROVED, "benefits");

//		if (reimbursement.getStatus().getKey()
//				.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
//				|| reimbursement.getStatus().getKey()
//						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
//				|| keyMap.containsKey(reimbursement.getStatus().getKey())) {
			List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = preauthDTO
					.getPreauthMedicalDecisionDetails()
					.getMedicalDecisionTableDTO();
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
				if (diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {

					DiagnosisDetailsTableDTO diagnosisDetailsDTO = diagnosisProcedureTableDTO
							.getDiagnosisDetailsDTO();
					diagnosisDetailsDTO
							.setAmountConsideredAmount(diagnosisProcedureTableDTO
									.getAmountConsidered() != null ? diagnosisProcedureTableDTO
									.getAmountConsidered().doubleValue() : 0d);
					diagnosisDetailsDTO
							.setNetAmount(diagnosisProcedureTableDTO
									.getNetAmount() != null ? diagnosisProcedureTableDTO
									.getNetAmount().doubleValue() : 0d);
					diagnosisDetailsDTO
							.setMinimumAmount(diagnosisProcedureTableDTO
									.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO
									.getMinimumAmountOfAmtconsideredAndPackAmt()
									.doubleValue()
									: 0d);
					diagnosisDetailsDTO
							.setCopayPercentage(SHAUtils
									.getDoubleValueFromString(diagnosisProcedureTableDTO
											.getCoPayPercentage() != null ? diagnosisProcedureTableDTO
											.getCoPayPercentage().getValue()
											: "0"));
					diagnosisDetailsDTO
							.setCopayAmount(diagnosisProcedureTableDTO
									.getCoPayAmount() != null ? diagnosisProcedureTableDTO
									.getCoPayAmount().doubleValue() : 0d);
					diagnosisDetailsDTO
							.setApprovedAmount(diagnosisProcedureTableDTO
									.getMinimumAmount() != null ? diagnosisProcedureTableDTO
									.getMinimumAmount().doubleValue() : 0d);
					diagnosisDetailsDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
					diagnosisDetailsDTO
							.setApproveRemarks(diagnosisProcedureTableDTO
									.getRemarks());
					// diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getApprovedAmount()
					// != null ? diagnosisDetailsDTO.getApprovedAmount() : 0d) -
					// (diagnosisDetailsDTO.getOldApprovedAmount() != null ?
					// diagnosisDetailsDTO.getOldApprovedAmount() : 0d ));
					diagnosisDetailsDTO
					.setDiffAmount((diagnosisDetailsDTO
							.getOldApprovedAmount() != null ? diagnosisDetailsDTO
							.getOldApprovedAmount() : 0d)
							- (diagnosisDetailsDTO.getNetApprovedAmount() != null ? diagnosisDetailsDTO
									.getNetApprovedAmount() : 0d));
					if(preauthDTO.getIsDirectToBilling() || preauthDTO.getIsDirectToFinancial()) {
						diagnosisDetailsDTO.setDiffAmount(diagnosisDetailsDTO.getNetApprovedAmount() != null ? 0 - diagnosisDetailsDTO
									.getNetApprovedAmount() : 0d);
					}
					
					diagnosisDetailsDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
					diagnosisDetailsDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
					diagnosisDetailsDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());
					diagnosisDetailsDTO.setCoPayTypeId(diagnosisProcedureTableDTO.getCoPayType());
					
					
					List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
							.getPreauthDataExtractionDetails().getDiagnosisTableList();
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : pedValidationTableList) {
						if(diagnosisDetailsTableDTO.getKey() != null && diagnosisDetailsDTO != null && diagnosisDetailsDTO.getKey() != null && 
								diagnosisDetailsTableDTO.getKey().equals(diagnosisDetailsDTO.getKey())){
							if(diagnosisProcedureTableDTO.getConsiderForPaymnt() != null && diagnosisProcedureTableDTO.getConsiderForPaymnt().getId()!= null){
								if(diagnosisProcedureTableDTO.getConsiderForPaymnt().getId().equals(ReferenceTable.COMMONMASTER_YES)){
									diagnosisDetailsTableDTO.setConsiderForPaymentFlag(SHAConstants.YES_FLAG);
								}else{
									diagnosisDetailsTableDTO.setConsiderForPaymentFlag(SHAConstants.N_FLAG);
								}
							}							
						}
					}
					
				} else if (diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					ProcedureDTO procedureDTO = diagnosisProcedureTableDTO
							.getProcedureDTO();
					procedureDTO
							.setAmountConsideredAmount(diagnosisProcedureTableDTO
									.getAmountConsidered() != null ? diagnosisProcedureTableDTO
									.getAmountConsidered().doubleValue() : 0d);
					procedureDTO
							.setNetAmount(diagnosisProcedureTableDTO
									.getNetAmount() != null ? diagnosisProcedureTableDTO
									.getNetAmount().doubleValue() : 0d);
					procedureDTO
							.setMinimumAmount(diagnosisProcedureTableDTO
									.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO
									.getMinimumAmountOfAmtconsideredAndPackAmt()
									.doubleValue()
									: 0d);
					procedureDTO
							.setCopayPercentage(SHAUtils
									.getDoubleValueFromString(diagnosisProcedureTableDTO
											.getCoPayPercentage() != null ? diagnosisProcedureTableDTO
											.getCoPayPercentage().getValue()
											: "0"));
					procedureDTO
							.setCopayAmount(diagnosisProcedureTableDTO
									.getCoPayAmount() != null ? diagnosisProcedureTableDTO
									.getCoPayAmount().doubleValue() : 0d);
					procedureDTO
							.setApprovedAmount(diagnosisProcedureTableDTO
									.getMinimumAmount() != null ? diagnosisProcedureTableDTO
									.getMinimumAmount().doubleValue() : 0d);
					procedureDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
					procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO
							.getRemarks());
					// procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount()
					// != null ? procedureDTO.getApprovedAmount() : 0d) -
					// (procedureDTO.getOldApprovedAmount() != null ?
					// procedureDTO.getOldApprovedAmount() : 0d ));
					procedureDTO
					.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO
							.getOldApprovedAmount() : 0d)
							- (procedureDTO.getNetApprovedAmount() != null ? procedureDTO
									.getNetApprovedAmount() : 0d));
					
					if(preauthDTO.getIsDirectToBilling() || preauthDTO.getIsDirectToFinancial()) {
						procedureDTO.setDiffAmount(procedureDTO.getNetApprovedAmount() != null ? 0 - procedureDTO
									.getNetApprovedAmount() : 0d);
					}
					
					procedureDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
					procedureDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
					procedureDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());		
					procedureDTO.setCoPayTypeId(diagnosisProcedureTableDTO.getCoPayType());
					
				}
			}
//		}
		List<ProcedureDTO> procedureList = preauthDTO
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		if (!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = reimbursementMapper
						.getProcedure(procedureDTO);
				procedure.setDeleteFlag(isCancelROD ? 0l : 1l);
				procedure.setTransactionKey(reimbursement.getKey());
				procedure.setStage(reimbursement.getStage());
				procedure.setStatus(reimbursement.getStatus());

				if (null != reimbursement.getStatus().getKey() && !reimbursement.getStatus().getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getStatus().getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					if (procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double
								.valueOf(procedureDTO.getCopay().getValue()));
					}

				}

				if (procedureDTO.getDiffAmount() == null
						|| procedureDTO.getDiffAmount() == 0) {
					procedure.setProcessFlag("0");
				} else if (procedureDTO.getOldApprovedAmount() != null
						&& procedureDTO.getNetApprovedAmount() != null
						&& procedureDTO.getOldApprovedAmount() > procedureDTO
								.getNetApprovedAmount()) {
					procedure.setProcessFlag("R");
					procedureDTO
							.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO
									.getOldApprovedAmount() : 0d)
									- (procedureDTO.getNetApprovedAmount() != null ? procedureDTO
											.getNetApprovedAmount() : 0d));
					procedure.setDiffAmount(procedureDTO.getDiffAmount());
				} else if (procedureDTO.getOldApprovedAmount() != null
						&& procedureDTO.getNetApprovedAmount() != null
						&& procedureDTO.getOldApprovedAmount() < procedureDTO
								.getNetApprovedAmount()) {

					procedure.setProcessFlag("A");
				} else {
					procedure.setProcessFlag("F");
				}
				
				if(preauthDTO.getIsDirectToBilling()) {
					procedure.setProcessFlag("F");
				}
				
				Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());
				
				if (reimbursementByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && (reimbursement.getStatus().getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						|| reimbursement.getStatus().getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
					procedure.setProcessFlag("F");
				}

				if (isZonalReview) {
					procedure.setKey(null);
				}
				if(reimbursement != null && reimbursement.getStatus() != null && ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey()) ) {
					procedure.setProcessFlag("0");
					procedure.setDiffAmount(0d);
				}
				
				
				if(null == procedure.getProcedureID()){
					procedure.setProcedureID(0l);
				}
				
				if (procedure.getKey() != null) {
					procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					procedure.setModifiedBy(userNameForDB);
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
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
				DiagnosisDetailsTableDTO pedValidationDTOWithCodes = setIcdChapterBlock(pedValidationDTO);
				PedValidation pedValidation = reimbursementMapper
						.getPedValidation(pedValidationDTOWithCodes);
				pedValidation.setDeleteFlag(isCancelROD ? 0l : 1l);
				pedValidation.setTransactionKey(reimbursement.getKey());
				pedValidation.setIntimation(reimbursement.getClaim()
						.getIntimation());
				pedValidation.setPolicy(reimbursement.getClaim()
						.getIntimation().getPolicy());
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				if (isZonalReview) {
					pedValidation.setKey(null);
				}
				pedValidation.setProcessFlag("0");

				if (pedValidation.getDiffAmount() == null
						|| pedValidation.getDiffAmount() == 0) {
					pedValidation.setProcessFlag("0");
				} else if (pedValidationDTO.getOldApprovedAmount() != null
						&& pedValidationDTO.getNetApprovedAmount() != null
						&& pedValidationDTO.getOldApprovedAmount() > pedValidationDTO
								.getNetApprovedAmount()) {
					pedValidation.setProcessFlag("R");
					pedValidationDTO
							.setDiffAmount((pedValidationDTO
									.getOldApprovedAmount() != null ? pedValidationDTO
									.getOldApprovedAmount() : 0d)
									- (pedValidationDTO.getNetApprovedAmount() != null ? pedValidationDTO
											.getNetApprovedAmount() : 0d));
					pedValidation.setDiffAmount(pedValidationDTO
							.getDiffAmount());
				} else if (pedValidationDTO.getOldApprovedAmount() != null
						&& pedValidationDTO.getNetApprovedAmount() != null
						&& pedValidationDTO.getOldApprovedAmount() < pedValidationDTO
								.getNetApprovedAmount()) {
					pedValidation.setProcessFlag("A");
				} else {
					pedValidation.setProcessFlag("F");
				}
				
				if(preauthDTO.getIsDirectToBilling()) {
					pedValidation.setProcessFlag("F");
				}
				
				if(pedValidationDTO.getPrimaryDiagnosis()!=null && pedValidationDTO.getPrimaryDiagnosis())
				{
					pedValidation.setPrimaryDiagnosis("Y");
				}
				else
				{
					pedValidation.setPrimaryDiagnosis("N");
				}
				
				Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());

				if (reimbursementByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && (null != reimbursement.getStatus().getKey() && (reimbursement.getStatus().getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						|| reimbursement.getStatus().getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)))) {
					pedValidation.setProcessFlag("F");
				}
				
				if (null != reimbursement.getStatus().getKey() && !reimbursement.getStatus().getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getStatus().getKey().equals(
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
				
				if(pedValidationDTO.getIsSublimitValueChange()){
					pedValidation.setProcessFlag("F");
				}
				
				if(reimbursement != null && reimbursement.getStatus() != null && ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey()) ) {
					pedValidation.setProcessFlag("0");
					pedValidation.setDiffAmount(0d);
				}
				
				if (pedValidation.getKey() != null) {
					pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					pedValidation.setModifiedBy(userNameForDB);
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
					MastersValue value = null;
					ExclusionDetails exclusionValue = null;
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						DiagnosisPED diagnosisPED = reimbursementMapper
								.getDiagnosisPED(pedDetailsTableDTO);
						diagnosisPED.setPedValidation(pedValidation);
						if (pedDetailsTableDTO
								.getPedExclusionImpactOnDiagnosis() != null) {
							value = new MastersValue();
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
							exclusionValue = new ExclusionDetails();
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
						if (isZonalReview) {
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

		/*if (preauthDTO.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
				|| preauthDTO.getStatusKey().equals(
						ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
			if (reimbursement.getKey() != null) {
				ReimbursementQuery preAuthQuery = new ReimbursementQuery();
				preAuthQuery.setReimbursement(reimbursement);
				preAuthQuery
						.setQueryRemarks(preauthDTO
								.getPreauthMedicalProcessingDetails()
								.getQueryRemarks());
				entityManager.persist(preAuthQuery);
			}
			entityManager.flush();
			entityManager.clear();
		}*/
		
		

		//saveBillEntryValues(preauthDTO, reimbursement, isCancelROD);
				/**
				 *
				 * 
				 * isCancelROD is set to false, for enhancement R0254.
				 * R0254 - To retain uploaded documents and bill entry values
				 * even if the rod is cancelled. 
				 * 
				 * **/
				saveBillEntryValues(preauthDTO, reimbursement, isCancelROD);
				
				/**
				 * Update other claim details for super surplus revised
				 */
				Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
					
					List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = preauthDTO.getUpdateOtherClaimDetailDTO();
					if(!updateOtherClaimDetailDTO.isEmpty()){
						List<UpdateOtherClaimDetails> updateOtherClaimDetails = PreauthMapper.getInstance().getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
						for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
							updateOtherClaimDetails2.setReimbursementKey(reimbursement.getKey());
							updateOtherClaimDetails2.setClaimKey(currentClaim.getKey());
							updateOtherClaimDetails2.setStage(reimbursement.getStage());
//							updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
							updateOtherClaimDetails2.setIntimationKey(currentClaim.getIntimation().getKey());
							updateOtherClaimDetails2.setStatus(reimbursement.getStatus());
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

		List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO
				.getDeletedDiagnosis();

		if (deletedDiagnosis != null && !deletedDiagnosis.isEmpty()) {
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
				PedValidation pedValidation = reimbursementMapper
						.getPedValidation(diagnosisDetailsTableDTO);
				pedValidation.setDeleteFlag(0l);
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				pedValidation.setTransactionKey(reimbursement.getKey());
				if (pedValidation.getKey() != null) {
					pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					pedValidation.setModifiedBy(userNameForDB);
					entityManager.merge(pedValidation);
				}
				entityManager.flush();
			}
		}

		List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
		if (deletedProcedure != null && !deletedProcedure.isEmpty()) {
			for (ProcedureDTO procedureDTO : deletedProcedure) {
				Procedure procedure = reimbursementMapper
						.getProcedure(procedureDTO);
				procedure.setTransactionKey(reimbursement.getKey());
				procedure.setStage(reimbursement.getStage());
				procedure.setStatus(reimbursement.getStatus());
				procedure.setDeleteFlag(0l);
				if (procedure.getKey() != null) {
					procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					procedure.setModifiedBy(userNameForDB);
					entityManager.merge(procedure);
				}
				entityManager.flush();
			}
		}
		/*Treating Doctor Details - CR2019211 */
		List<TreatingDoctorDTO> treatingDoctorLists = preauthDTO.getPreauthDataExtractionDetails().getTreatingDoctorDTOs();
		if(!treatingDoctorLists.isEmpty()) {
		for (TreatingDoctorDTO treatingDoctorDTO: treatingDoctorLists) {		
			TreatingDoctorDetails doctorDetails = reimbursementMapper.gettreatingDoctor(treatingDoctorDTO);	
			if (doctorDetails.getKey() != null) {
				doctorDetails.setModifiedBy(userNameForDB);
				doctorDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(doctorDetails);
			} else {
				doctorDetails.setClaimKey(reimbursement.getClaim().getKey());
				doctorDetails.setTransactionKey(reimbursement.getKey());
				doctorDetails.setCreatedBy(userNameForDB);
				doctorDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				doctorDetails.setActiveStatus(1L);	
				entityManager.persist(doctorDetails);
				treatingDoctorDTO.setKey(doctorDetails.getKey());
			}
			log.info("------TreatingDoctorDetails------>"+doctorDetails+"<------------");
			entityManager.flush();
			entityManager.clear();		
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

		if(null != preauthDTO.getStatusKey() && !preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
			Object doctorNameList = preauthDTO.getPreauthMedicalDecisionDetails().getDoctorName();
			if(null != doctorNameList ){
				List<String> docList = getListFromMultiSelectComponent(doctorNameList);
				if(null != docList && !docList.isEmpty()){
					for (String docName : docList) {
						OpinionValidation opinionValidation = new OpinionValidation();
						opinionValidation.setIntimationNumber(preauthDTO.getNewIntimationDTO().getIntimationId());				
						opinionValidation.setStage(reimbursement.getStage());
						opinionValidation.setStatus(reimbursement.getStatus());
						Stage stageByKey = getStageByKey(reimbursement.getStage().getKey());
						opinionValidation.setClaimStage(null != stageByKey ? stageByKey.getStageName() : null);
						String doctorName = docName.replaceAll("[\\[\\]]", "");
						opinionValidation.setAssignedDocName(doctorName);
						opinionValidation.setUpdatedBy(preauthDTO.getStrUserName());
						opinionValidation.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setAssignedDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setCpuCode(Long.valueOf(preauthDTO.getNewIntimationDTO().getCpuCode()));
						opinionValidation.setOpinionStatus(ReferenceTable.OPINION_VALIDATION_PENDING_KEY);
						opinionValidation.setOpinionStatusDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setCreatedBy(preauthDTO.getStrUserName());
						opinionValidation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setUpdatedRemarks(preauthDTO.getPreauthMedicalDecisionDetails().getRemarksFromDeptHead());
						if(preauthDTO.getPreauthMedicalDecisionDetails().getDoctorContainer() != null){
							BeanItemContainer<SpecialSelectValue> doctorContainer = preauthDTO.getPreauthMedicalDecisionDetails().getDoctorContainer();
							List<SpecialSelectValue> doctorsWithRole = doctorContainer.getItemIds();
								for (SpecialSelectValue specialSelectValue : doctorsWithRole) {
									if(doctorName.equals(specialSelectValue.getValue())){
										opinionValidation.setAssignedRoleBy(specialSelectValue.getSpecialValue());
									}
								}
							}
						entityManager.persist(opinionValidation);
					}
				}
			}
		}
		
		/*Implant Details - GLX2020057 */
		if(preauthDTO.getPreauthDataExtractionDetails().getImplantApplicable()){
			List<ImplantDetailsDTO> implantDetailsDTOs = preauthDTO.getPreauthDataExtractionDetails().getImplantDetailsDTOs();
			if(implantDetailsDTOs !=null && !implantDetailsDTOs.isEmpty()) {
				for (ImplantDetailsDTO implantDetailsDTO: implantDetailsDTOs) {		
					ImplantDetails implantDetails = reimbursementMapper.getimplantDetails(implantDetailsDTO);

					if (implantDetails.getKey() != null) {
						implantDetails.setModifiedBy(userNameForDB);
						implantDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(implantDetails);
					} else {
						implantDetails.setClaimKey(reimbursement.getClaim().getKey());
						implantDetails.setTransactionKey(reimbursement.getKey());
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
		
		
		return reimbursement;

	}

	public void billEntrySave(PreauthDTO dto) {
		Reimbursement reimbursementByKey = getReimbursementByKey(dto.getKey());
		saveBillEntryValues(dto, reimbursementByKey, false);
	}

	private void saveBillEntryValues(PreauthDTO preauthDTO,
			Reimbursement reimbursement, Boolean isCancelROD) {
		List<UploadDocumentDTO> uploadDocsDTO = preauthDTO
				.getUploadDocumentDTO();
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {

			CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
			RODDocumentSummary rodSummary = createRODMapper
					.getDocumentSummary(uploadDocumentDTO);
			if(isCancelROD)
			{
				if(null != rodSummary.getFileType() && null != rodSummary.getFileType().getKey())
				if(ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(rodSummary.getFileType().getKey()))
				{
					rodSummary.setDeletedFlag("Y");
				}
				else
				{
					rodSummary.setDeletedFlag("N");
				}
			}
			//rodSummary.setDeletedFlag(isCancelROD ?"Y" : "N");
			rodSummary.setReimbursement(reimbursement);

			if (null != uploadDocumentDTO.getDocSummaryKey()) {
				entityManager.merge(rodSummary);
				entityManager.flush();
				entityManager.clear();
			} else {
				entityManager.persist(rodSummary);
				entityManager.flush();
				entityManager.refresh(rodSummary);
			}
			/**
			 * Added for cancel rod scenario.
			 * */
			if(null != reimbursement && null != isCancelROD &&  null != rodSummary.getFileType() && null != rodSummary.getFileType().getKey() && isCancelROD && ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(rodSummary.getFileType().getKey()))
			{
				List<DocumentDetails> docDetailsList = getDocumentDetailsByRodNumber(reimbursement.getRodNumber());
				if(null != docDetailsList && !docDetailsList.isEmpty())
				{
					for (DocumentDetails documentDetails : docDetailsList) {
						if(SHAConstants.CASHLESS_SETTLEMENT_BILL.equals(documentDetails.getDocumentType()))
						{
							documentDetails.setDeletedFlag("Y");
							entityManager.merge(documentDetails);
							entityManager.flush();
						}
					}
				}
			}
			
			uploadDocumentDTO.setDocSummaryKey(rodSummary.getKey());
			//CreateRODMapper mapper = new CreateRODMapper();
			List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
					.getBillEntryDetailList();
			for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

				if(uploadDocumentDTO.getBillNo() != null && billEntryDetailsDTO.getBillNo() != null){
					if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
							billEntryDetailsDTO.getBillNo())) {
						@SuppressWarnings("static-access")
						/*RODBillDetails rodBillDetails = mapper
								.getRODBillDetails(billEntryDetailsDTO);*/
						
						RODBillDetails  rodBillDetails = null;
						if(null != billEntryDetailsDTO.getBillDetailsKey())
						{
							 rodBillDetails = getBillEntryDetailsByKey( billEntryDetailsDTO.getBillDetailsKey());
						}
						else
						{
						    //IMSSUPPOR-28935 added for this jira
                            if(billEntryDetailsDTO.getClassification()==null && billEntryDetailsDTO.getKey()!=null){
                                rodBillDetails = getBillEntryDetailsByKey(billEntryDetailsDTO.getKey());
                            }else{
                                 rodBillDetails = createRODMapper
                                                           .getRODBillDetails(billEntryDetailsDTO);
                          }
                          //IMSSUPPOR-28935 added for this jira
						}
						
						if(isCancelROD &&  null != rodSummary.getFileType() && null != rodSummary.getFileType().getKey() && isCancelROD && ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(rodSummary.getFileType().getKey()))
						{
							if( ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(rodSummary.getFileType().getKey()))
							{
								rodBillDetails.setDeletedFlag("Y");
							}
							else
							{
								rodBillDetails.setDeletedFlag("N");
							}
						}
						
						if(billEntryDetailsDTO.getRoomType() != null){
							rodBillDetails.setRoomType(billEntryDetailsDTO.getRoomType().getValue());
						}
						
						//rodBillDetails.setDeletedFlag(isCancelROD ?"Y" : "N");
						rodBillDetails.setRodDocumentSummaryKey(rodSummary);
						rodBillDetails.setReimbursementKey(reimbursement.getKey());
						
	//					rodSummary.setZonalRemarks(uploadDocumentDTO
	//							.getBillEntryDetailsDTO().getZonalRemarks());
	//					rodSummary.setCorporateRemarks(uploadDocumentDTO
	//							.getBillEntryDetailsDTO().getCorporateRemarks());
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
	
						entityManager.merge(rodSummary);
						entityManager.flush();
						log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
	
						if (rodBillDetails.getKey() != null) {
							entityManager.merge(rodBillDetails);
						} else {
							entityManager.persist(rodBillDetails);
							billEntryDetailsDTO.setKey(rodBillDetails.getKey());
						}
						entityManager.flush();
						entityManager.clear();
						log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
						billEntryDetailsDTO.setBillDetailsKey(rodBillDetails.getKey());
						billEntryDetailsDTO.setKey(rodBillDetails.getKey());
					}
				}
			}

			/**
			 * The below code will update the deleted record in rod bill details
			 * table. Those record which are deleted at zonal level,shouldn't be
			 * displayed in subsequent screens. Hence for deleted records, the
			 * deleted flag will be updated as "Y". This updation happens only
			 * in bill details table and not in document summary.
			 * */
			List<BillEntryDetailsDTO> deletedBillEntryDetailsList = uploadDocumentDTO
					.getDeletedBillList();
			if (null != deletedBillEntryDetailsList
					&& !deletedBillEntryDetailsList.isEmpty()) {
				for (BillEntryDetailsDTO billEntryDetailsDTO : deletedBillEntryDetailsList) {

					// if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(billEntryDetailsDTO.getBillNo()))
					{
						@SuppressWarnings("static-access")
						RODBillDetails rodBillDetails = createRODMapper
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

		}

		/**
		 * Added as a part of amount claimed table enhancement
		 * */
		DBCalculationService dbCalculationService = new DBCalculationService();
		//added by noufel fro GMC prop CR
		if(!(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		} 
		else if((preauthDTO.getProrataDeductionFlag() != null && preauthDTO.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
				(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		}

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

	protected NursingChargesMatchingDTO getCorrespondingNursingDTO(Long id,
			List<NursingChargesMatchingDTO> dtoList) {
		for (NursingChargesMatchingDTO nursingChargesMatchingDTO : dtoList) {
			if (nursingChargesMatchingDTO.getId().equals(id)) {
				return nursingChargesMatchingDTO;
			}
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveRoomRentNursingMapping(PreauthDTO dto,
			List<RoomRentMatchingDTO> dtoList) {
		
	List<BillItemMapping> billItemMappingByRODKey = getBillItemMappingByRODKey(dto.getKey());
	if(billItemMappingByRODKey != null) {
		for (BillItemMapping billItemMapping : billItemMappingByRODKey) {
			billItemMapping.setDeleteFlag("Y");
			entityManager.merge(billItemMapping);
			entityManager.flush();
			log.info("------BillItemMapping------>"+billItemMapping+"<------------");
		}
	}
		
		if (dto.getIsICUoneMapping()) {
			saveOnetoOneRoomRentMapping(dto, true,false);
		} else {
			saveRoomRentMapping(dto, dto.getIcuRoomRentMappingDTOList());
		}
		
		if (dto.getIsICCUoneMapping()) {
			saveOnetoOneRoomRentMapping(dto, false,true);
		} else {
			saveRoomRentMapping(dto, dto.getIccuRoomRentMappingDTOList());
		}
		
		if (dto.getIsOneMapping()) {
			saveOnetoOneRoomRentMapping(dto, false,false);
		} else {
			saveRoomRentMapping(dto, dtoList);
		}

	}

	private void saveRoomRentMapping(PreauthDTO dto, List<RoomRentMatchingDTO> dtoList) {
		for (RoomRentMatchingDTO roomRentMatchingDTO : dtoList) {
			List<NursingChargesMatchingDTO> nursingChargesDTOList2 = roomRentMatchingDTO
					.getNursingChargesDTOList();
			BillItemMapping mapping = null;
			for (NursingChargesMatchingDTO nursingChargesMatchingDTO : nursingChargesDTOList2) {
					mapping = new BillItemMapping();
					mapping.setRoomIcuRentId(roomRentMatchingDTO.getId());
					mapping.setNursingId(nursingChargesMatchingDTO.getId());
					mapping.setReimbursementKey(dto.getKey());
					mapping.setAllowedDays(nursingChargesMatchingDTO
							.getMapToRoomDays() != null ? nursingChargesMatchingDTO
							.getMapToRoomDays() : 0);
					mapping.setAllocatedDays(nursingChargesMatchingDTO
							.getAllocatedClaimedNoOfDays() != null ? nursingChargesMatchingDTO
							.getAllocatedClaimedNoOfDays() : 0);
					mapping.setUnAllocatedDays(nursingChargesMatchingDTO
							.getUnAllocatedDays() != null ? nursingChargesMatchingDTO
							.getUnAllocatedDays() : 0);
					mapping.setDeleteFlag("N");
					mapping.setKey(null);
					if (null != mapping.getKey()) {
						entityManager.merge(mapping);
						entityManager.flush();
					} else {
						entityManager.persist(mapping);
						entityManager.flush();
					}

			}
		}
		
	}

	private void saveOnetoOneRoomRentMapping(PreauthDTO dto,
			Boolean isICURoomRent,Boolean isICCURoomRent) {
		List<RoomRentMatchingDTO> roomRentMappingDTOList = dto
				.getRoomRentMappingDTOList();
		if (isICURoomRent) {
			roomRentMappingDTOList = dto.getIcuRoomRentMappingDTOList();
		}
		if(isICCURoomRent){
			roomRentMappingDTOList = dto.getIccuRoomRentMappingDTOList();
		}
		
		if(!roomRentMappingDTOList.isEmpty() && roomRentMappingDTOList.size() > 0 && !roomRentMappingDTOList.get(0).getNursingChargesDTOList().isEmpty() && roomRentMappingDTOList.get(0).getNursingChargesDTOList().size() > 1) {
			saveOnetoOneRoomRentMappingWithoutRoomRent(dto, isICURoomRent,isICCURoomRent);
		} else {
			BillItemMapping mapping = null;
			for (RoomRentMatchingDTO roomRentMatchingDTO : roomRentMappingDTOList) {
				if(!roomRentMatchingDTO.getNursingChargesDTOList().isEmpty()) {
					mapping = new BillItemMapping();
					mapping.setRoomIcuRentId(roomRentMatchingDTO.getId());
					mapping.setNursingId(roomRentMatchingDTO.getNursingChargesDTOList()
							.get(0).getId());
					
					Double allocatedNursingDays = roomRentMatchingDTO.getNursingChargesDTOList().get(0).getClaimedNoOfDays() != null ? roomRentMatchingDTO.getNursingChargesDTOList().get(0).getClaimedNoOfDays() : 0d;
					
					mapping.setReimbursementKey(dto.getKey());
					mapping.setAllowedDays(allocatedNursingDays <= roomRentMatchingDTO.getAllowedNoOfDays() ? allocatedNursingDays : roomRentMatchingDTO.getAllowedNoOfDays());
//					mapping.setAllowedDays(allocatedNursingDays);
					mapping.setAllocatedDays(roomRentMatchingDTO
							.getNursingChargesDTOList().get(0)
							.getAllocatedClaimedNoOfDays() != null ? roomRentMatchingDTO
							.getNursingChargesDTOList().get(0)
							.getAllocatedClaimedNoOfDays()
							: 0);
					mapping.setUnAllocatedDays(roomRentMatchingDTO
							.getNursingChargesDTOList().get(0).getUnAllocatedDays() != null ? roomRentMatchingDTO
							.getNursingChargesDTOList().get(0).getUnAllocatedDays()
							: 0);
					mapping.setDeleteFlag("N");
					// mapping.setKey(nursingChargesMatchingDTO.getMappingId());
					if (null != mapping.getKey()) {
						entityManager.merge(mapping);
						entityManager.flush();
					} else {
						entityManager.persist(mapping);
						entityManager.flush();
						entityManager.refresh(mapping);
						log.info("------BillItemMapping------>"+mapping.getKey()+"<------------");
					}
				}
				
			}
		}
	
	}
	
	
	private void saveOnetoOneRoomRentMappingWithoutRoomRent(PreauthDTO dto,
			Boolean isICURoomRent, Boolean isICCURoomRent) {
		List<RoomRentMatchingDTO> roomRentMappingDTOList = dto
				.getRoomRentMappingDTOList();
		if (isICURoomRent) {
			roomRentMappingDTOList = dto.getIcuRoomRentMappingDTOList();
		}
		
		if (isICCURoomRent) {
			roomRentMappingDTOList = dto.getIccuRoomRentMappingDTOList();
		}
		
		for (RoomRentMatchingDTO roomRentMatchingDTO : roomRentMappingDTOList) {
			if(!roomRentMatchingDTO.getNursingChargesDTOList().isEmpty()) {
				List<NursingChargesMatchingDTO> nursingChargesDTOList = roomRentMatchingDTO.getNursingChargesDTOList();
				BillItemMapping mapping = null;
				for (NursingChargesMatchingDTO nursingChargesMatchingDTO : nursingChargesDTOList) {
					mapping = new BillItemMapping();
					mapping.setRoomIcuRentId(roomRentMatchingDTO.getId());
					mapping.setNursingId(nursingChargesMatchingDTO.getId());
					
					Double allocatedNursingDays = nursingChargesMatchingDTO.getClaimedNoOfDays() != null ? nursingChargesMatchingDTO.getClaimedNoOfDays() : 0d;
					
					mapping.setReimbursementKey(dto.getKey());
//					mapping.setAllowedDays(allocatedNursingDays <= roomRentMatchingDTO.getAllowedNoOfDays() ? allocatedNursingDays : roomRentMatchingDTO.getAllowedNoOfDays());
					mapping.setAllowedDays(allocatedNursingDays);
					mapping.setAllocatedDays(nursingChargesMatchingDTO
							.getAllocatedClaimedNoOfDays() != null ? nursingChargesMatchingDTO
							.getAllocatedClaimedNoOfDays()
							: 0);
					mapping.setUnAllocatedDays(nursingChargesMatchingDTO.getUnAllocatedDays() != null ? nursingChargesMatchingDTO.getUnAllocatedDays()
							: 0);
					mapping.setDeleteFlag("N");
					// mapping.setKey(nursingChargesMatchingDTO.getMappingId());
					if (null != mapping.getKey()) {
						entityManager.merge(mapping);
						entityManager.flush();
					} else {
						entityManager.persist(mapping);
						entityManager.flush();
						entityManager.refresh(mapping);
						log.info("------BillItemMapping------>"+mapping.getKey()+"<------------");
					}
				}
			
			}
			
		}
	}

	public Reimbursement saveUpdatedBillDetails(PreauthDTO reimbursementDto) {

		ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();
		Reimbursement reimbursement = reimbursementMapper
				.getReimbursement(reimbursementDto);

		List<UploadDocumentDTO> uploadDocsDTO = reimbursementDto
				.getUploadDocumentDTO();
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {

			
			CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
			RODDocumentSummary rodSummary = createRODMapper
					.getDocumentSummary(uploadDocumentDTO);
			rodSummary.setReimbursement(reimbursement);

			if (null != uploadDocumentDTO.getDocSummaryKey()) {
				entityManager.merge(rodSummary);
				entityManager.flush();
				log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
			} else {
				entityManager.persist(rodSummary);
				entityManager.flush();
				entityManager.refresh(rodSummary);
				log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
			}
			//CreateRODMapper mapper = new CreateRODMapper();
			List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
					.getBillEntryDetailList();
			for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

				if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
						billEntryDetailsDTO.getBillNo())) {
					@SuppressWarnings("static-access")
					RODBillDetails rodBillDetails = createRODMapper
							.getRODBillDetails(billEntryDetailsDTO);
					rodBillDetails.setRodDocumentSummaryKey(rodSummary);
					rodBillDetails.setReimbursementKey(reimbursement.getKey());
					rodSummary.setZonalRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getZonalRemarks());
					rodSummary.setCorporateRemarks(uploadDocumentDTO
							.getBillEntryDetailsDTO().getCorporateRemarks());

					entityManager.merge(rodSummary);
					entityManager.flush();
					log.info("------RODDocumentSummary------>"+rodSummary+"<------------");

					if (rodBillDetails.getKey() != null) {
						entityManager.merge(rodBillDetails);
					} else {
						entityManager.persist(rodBillDetails);
					}
					entityManager.flush();
					entityManager.clear();
					log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
				}
			}
		}

		return reimbursement;
	}

	private void createEscalate(PreauthDTO bean, Reimbursement reimbursement,
			Status status, Stage stage, String remarks) {
		PreauthEscalate escalate = new PreauthEscalate();
		// preauthEscalte.setPreauth(preauth);
		escalate.setClaim(reimbursement.getClaim());
        escalate.setTransactionFlag("R");
        escalate.setTransactionKey(reimbursement.getKey());
		escalate.setEscalateRemarks(remarks);
		status.setKey(bean.getStatusKey());
		stage.setKey(bean.getStageKey());
		escalate.setStatus(status);
		escalate.setStage(stage);
		SelectValue escalateTo = bean.getPreauthMedicalDecisionDetails()
				.getEscalateTo();
		if (escalateTo != null) {
			MastersValue value = new MastersValue();
			value.setKey(escalateTo.getId());
			value.setValue(escalateTo.getValue());
			escalate.setEscalateTo(value);
		}

		String createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(), 15);
		}
		escalate.setCreatedBy(createdBy);

		entityManager.persist(escalate);
		
		entityManager.flush();
		log.info("------PreauthEscalate------>"+escalate+"<------------");
	}

	private void createEscalateReply(PreauthDTO bean,
			Reimbursement reimbursement, Status status, Stage stage,
			String remarks) {

		PreauthEscalate escalateByClaimKey = getEscalateByClaimKey(reimbursement
				.getClaim().getKey());

		if (escalateByClaimKey != null && escalateByClaimKey.getKey() != null) {
			escalateByClaimKey.setEsclateReplyRemarks(remarks);
			stage.setKey(bean.getStageKey());
			escalateByClaimKey.setStage(stage);
			status.setKey(bean.getStatusKey());
			escalateByClaimKey.setStatus(status);

			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
						15);
			}
			escalateByClaimKey.setCreatedBy(createdBy);

			entityManager.merge(escalateByClaimKey);
			entityManager.flush();
			log.info("------PreauthEscalate------>"+escalateByClaimKey+"<------------");

		} else {
			PreauthEscalate escalate = new PreauthEscalate();
			// preauthEscalte.setPreauth(preauth);
			escalate.setClaim(reimbursement.getClaim());
			escalate.setEsclateReplyRemarks(remarks);
			status.setKey(bean.getStatusKey());
			stage.setKey(bean.getStageKey());
			escalate.setStatus(status);
			escalate.setStage(stage);

			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
						15);
			}
			escalate.setCreatedBy(createdBy);

			entityManager.persist(escalate);

			entityManager.flush();
			log.info("------PreauthEscalate------>"+escalate+"<------------");
		}

	}

	private void createSpecialist(PreauthDTO bean, Reimbursement reimbursement,
			Status status, Stage stage) {
		Specialist specialist = new Specialist();
		// preauthEscalte.setPreauth(preauth);
		specialist.setClaim(reimbursement.getClaim());
		status.setKey(bean.getStatusKey());
		stage.setKey(bean.getStageKey());
		specialist.setStatus(status);
		specialist.setStage(stage);
		specialist.setFileName(bean.getFileName());
		specialist.setTransactionKey(reimbursement.getKey());
		specialist.setTransactionFlag("R");
		specialist.setFileToken(bean.getTokenName());
		specialist.setReasonForReferring(bean
				.getPreauthMedicalDecisionDetails().getReasonForRefering());
		SelectValue specialistType = bean.getPreauthMedicalDecisionDetails()
				.getSpecialistType();
		if (specialistType != null) {
			MastersValue value = new MastersValue();
			value.setKey(specialistType.getId());
			value.setValue(specialistType.getValue());
			specialist.setSpcialistType(value);
		}

		String createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(), 15);
		}
		specialist.setCreatedBy(createdBy);

		entityManager.persist(specialist);
		entityManager.flush();
		log.info("------Specialist------>"+specialist+"<------------");
		if (specialist.getKey() != null) {
			bean.setSpecialistKey(specialist.getKey());
		}

	}

	private void createCoordinator(PreauthDTO bean, Reimbursement reimbursement) {
		Coordinator coordinator = new Coordinator();
		MastersValue masterValue = new MastersValue();
		/*
		 * if(bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest
		 * () != null) {
		 * masterValue.setKey(bean.getPreauthMedicalDecisionDetails
		 * ().getTypeOfCoordinatorRequest().getId());
		 * masterValue.setValue(bean.getPreauthMedicalDecisionDetails
		 * ().getTypeOfCoordinatorRequest().getValue()); }
		 */
		SelectValue coordinatorType = new SelectValue();
		if (bean.getPreauthMedicalDecisionDetails()
				.getTypeOfCoordinatorRequest() != null) {
			coordinatorType = bean.getPreauthMedicalDecisionDetails()
					.getTypeOfCoordinatorRequest();
			coordinator.setRequestorRemarks(bean
					.getPreauthMedicalDecisionDetails().getReasonForRefering());
		} else {
			coordinatorType = bean.getCoordinatorDetails()
					.getTypeofCoordinatorRequest();
			coordinator.setRequestorRemarks(bean.getCoordinatorDetails()
					.getReasonForRefering());
		}
		if(null != coordinatorType){
			masterValue.setKey(coordinatorType.getId());
			masterValue.setValue(coordinatorType.getValue());
			coordinator.setCoordinatorRequestType(masterValue);
		}
		
		// coordinator.setCoordinatorRemarks(bean.getPreauthMedicalDecisionDetails().getReasonForRefering());
		/*
		 * coordinator.setRequestorRemarks(bean.getPreauthMedicalDecisionDetails(
		 * ) .getReasonForRefering());
		 */

		coordinator.setActiveStatus(1l);
		coordinator.setStage(reimbursement.getStage());
		coordinator.setStatus(reimbursement.getStatus());
		coordinator.setClaim(reimbursement.getClaim());
		coordinator.setPolicy(reimbursement.getClaim().getIntimation()
				.getPolicy());
		coordinator.setIntimation(reimbursement.getClaim().getIntimation());
		coordinator.setTransactionKey(reimbursement.getKey());
		coordinator.setTransactionFlag("R");

		String createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(), 15);
		}
		coordinator.setCreatedBy(createdBy);

		entityManager.persist(coordinator);
		entityManager.flush();
		log.info("------Coordinator------>"+coordinator+"<------------");
		bean.getCoordinatorDetails().setKey(coordinator.getKey());
	}

	private void createReimbursementRejection(PreauthDTO bean,
			Reimbursement reimbursement, Status status) {

		ReimbursementRejection reimbursementRejection = new ReimbursementRejection();

		if (reimbursement != null) {
			try {

//				reimbursement.setStatus(status);
				String strUserName = bean.getStrUserName();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//				reimbursement.setModifiedBy(userNameForDB);
//				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//				entityManager.merge(reimbursement);
//				entityManager.flush();
				
				String rejectRemarks1 = bean.getPreauthMedicalDecisionDetails().getRejectionRemarks();
				String subString1 = "";
				String subString2 = "";
				String rejectionRemarks="";
				String rejectionRemarks2 ="";
				System.out.println(String.format("Reject Remarks Length [%s]", rejectRemarks1.length()));
				if(rejectRemarks1.length() > 4000){
					 subString1 = rejectRemarks1.substring(0,4000);
					 subString2 = rejectRemarks1.substring(4000,8000);
					 
					 bean.getPreauthMedicalDecisionDetails().setRejectionRemarks(subString1);
					 bean.getPreauthMedicalDecisionDetails().setRejectionRemarks2(subString2);
					 rejectionRemarks = bean.getPreauthMedicalDecisionDetails().getRejectionRemarks();
					  rejectionRemarks2 = bean.getPreauthMedicalDecisionDetails().getRejectionRemarks2();
					 System.out.println(String.format("Reject Remarks 1 [%s]", rejectionRemarks.length()));
					 System.out.println(String.format("Reject Remarks 2 [%s]", rejectionRemarks2.length()));
					 //reimbursement.setRejectionRemarks2(rejectionRemarks2);
				}
				else{
					rejectionRemarks = bean.getPreauthMedicalDecisionDetails().getRejectionRemarks();
				}

				if(rejectionRemarks !=null && !rejectionRemarks.isEmpty()){
					reimbursementRejection.setRejectionRemarks(rejectionRemarks);
				}
				if(rejectionRemarks2 !=null && !rejectionRemarks2.isEmpty()){
					reimbursementRejection.setRejectionRemarks2(rejectionRemarks2);
				}
				
			
				
				//reimbursementRejection.setRejectionRemarks(bean.getPreauthMedicalDecisionDetails().getRejectionRemarks());
				if(ReferenceTable.HEALTH_LOB_KEY.equals(reimbursement.getClaim().getLobId())){
				
					MastersValue rejectCategory = new MastersValue();
					rejectCategory.setKey(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId());
					reimbursementRejection.setRejectionCategory(rejectCategory);
				}
				
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
				log.info("------ReimbursementRejection------>"+reimbursementRejection+"<------------");

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
	}

	public void createInvestigation(PreauthDTO bean,
			Reimbursement reimbursement, Status status, Stage stage) {
		Investigation investigation = new Investigation();
		investigation.setReasonForReferring(bean
				.getPreauthMedicalDecisionDetails().getReasonForRefering());
		
		if(!bean.isDirectToAssignInv()){
			investigation.setTriggerPoints(bean.getPreauthMedicalDecisionDetails()
					.getTriggerPointsToFocus());
		}

		investigation.setClaim(reimbursement.getClaim());
		investigation.setStage(stage);
		investigation.setTransactionKey(reimbursement.getKey());
		investigation.setTransactionFlag("R");
		investigation.setIntimation(reimbursement.getClaim().getIntimation());
		investigation.setPolicy(reimbursement.getClaim().getIntimation()
				.getPolicy());
		status.setKey(bean.getStatusKey());
		stage.setKey(bean.getStageKey());
		investigation.setStatus(status);
		investigation.setStage(stage);
		// investigation.setCreatedBy("Medical approve");
		SelectValue allocationTo = bean.getPreauthMedicalDecisionDetails()
				.getAllocationTo();
		if (allocationTo != null) {
			MastersValue value = new MastersValue();
			value.setKey(allocationTo.getId());
			value.setValue(allocationTo.getValue());
			investigation.setAllocationTo(value);
		}

		String createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(), 15);
		}
		investigation.setCreatedBy(createdBy);

		entityManager.persist(investigation);
		entityManager.flush();
		log.info("------Investigation------>"+investigation+"<------------");
		if (investigation.getKey() != null) {
			bean.setInvestigationKey(investigation.getKey());
			
			if(bean.isDirectToAssignInv()){
				if(bean.getPreauthMedicalDecisionDetails().getTriggerPointsList() != null && !bean.getPreauthMedicalDecisionDetails().getTriggerPointsList().isEmpty()){
					submitInitiateLevelInvestigationTriggerPointsDetails(bean.getPreauthMedicalDecisionDetails().getTriggerPointsList(), investigation);
				}
			
				uploadInvestigationLetter(bean,investigation);			
			}
		}	
	}

	public void submitInitiateLevelInvestigationTriggerPointsDetails(List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList, Investigation investigation){
			
			if(triggerPointsList != null && !triggerPointsList.isEmpty()){
				InvestigationDetails details = null;
			for(DraftTriggerPointsToFocusDetailsTableDto dto: triggerPointsList){
				details = new InvestigationDetails();
				details.setInvestigation(investigation);
				details.setReimbursementKey(investigation.getTransactionKey());
				details.setProcessType(SHAConstants.REIMBURSEMENT_CHAR);
				details.setSno(dto.getSno().longValue());
				details.setDraftOrReDraftRemarks(dto.getRemarks());
				details.setCreatedDate(new Date());
				details.setDeletedFlag(dto.getDeltedFlag());
				entityManager.persist(details);
				entityManager.flush();
				
			
			}
		}
	}	
	
	public void uploadInvestigationLetter(PreauthDTO bean, Investigation investigation){
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(bean.getClaimDTO().getClaimId());
		
		List<DraftInvestigatorDto> dtoList = new ArrayList<DraftInvestigatorDto>();
		DraftInvestigatorDto draftInvestigation = new DraftInvestigatorDto();					

		draftInvestigation.setClaimDto(bean.getClaimDTO());
		draftInvestigation.getClaimDto().setNewIntimationDto(bean.getNewIntimationDTO());
		draftInvestigation.setDischargeDate(bean.getPreauthDataExtractionDetails().getDischargeDate());
		
		if(investigation != null && investigation.getTransactionFlag() != null && investigation.getTransactionKey() != null) {
			
			String diagnosisForTransacByKey = getDiganosisByTransacKey(investigation.getTransactionKey());
			draftInvestigation.setDiagnosisName(diagnosisForTransacByKey); 
		}
		
		Double claimedAmt = 0d;
		if(investigation.getTransactionKey() != null){
			Reimbursement reimbObj = getReimbursementByKey(investigation.getTransactionKey());
			if(reimbObj != null){
				claimedAmt += reimbObj.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() :0d;
				claimedAmt += reimbObj.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getHospitalizationClaimedAmount() : 0d;
				claimedAmt += reimbObj.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() : 0d;
			}
		}						
		draftInvestigation.getClaimDto().setPreauthClaimedAmountAsPerBill(claimedAmt);
		
		draftInvestigation.setTriggerPointsList(bean.getPreauthMedicalDecisionDetails().getTriggerPointsList());
				
		if(draftInvestigation.getClaimDto().getNewIntimationDto().getOrginalSI() != null ){
			String amtWords = SHAUtils.getParsedAmount(draftInvestigation.getClaimDto().getNewIntimationDto().getOrginalSI());
			draftInvestigation.getClaimDto().getNewIntimationDto().setComments(amtWords);
		}
		else{
			draftInvestigation.getClaimDto().getNewIntimationDto().setComments(null);
		}
		Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(draftInvestigation.getClaimDto().getNewIntimationDto().getIntimationId());
		if (portablityStatus != null) {
			draftInvestigation.getClaimDto().getNewIntimationDto().setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}
		
		dtoList.add(draftInvestigation);
		reportDto.setBeanList(dtoList);
		
		String templateName = "InvestigationLetter(R)";
		
		final String filePath = docGen.generatePdfDocument(templateName, reportDto);
		
		draftInvestigation.setDocFilePath(filePath);
		draftInvestigation.setDocType(SHAConstants.DOC_TYPE_DRAFT_INVESTIGATION_LETTER);			
		draftInvestigation.setDocSource(SHAConstants.DOC_SOURCE_FA_DIRECT_TO_ASSIGN_INV_LETTER);
		investigationService.uploadInvLetterToDms(draftInvestigation);
	}
	
	public void createInitiateFVR(PreauthDTO bean, Reimbursement reimbursement,
			Status status, Stage stage) {
		FieldVisitRequest fieldVisitRequest = new FieldVisitRequest();
		fieldVisitRequest.setFvrTriggerPoints(bean
				.getPreauthMedicalDecisionDetails().getFvrTriggerPoints());

		fieldVisitRequest.setClaim(reimbursement.getClaim());
		fieldVisitRequest.setStage(stage);
//		fieldVisitRequest.setAssignedDate(new Date());
		fieldVisitRequest.setIntimation(reimbursement.getClaim()
				.getIntimation());
		
		if(reimbursement.getClaim().getIntimation() != null){
			
			Long hospital = reimbursement.getClaim().getIntimation().getHospital();
			Hospitals hospitalByKey = getHospitalByKey(hospital);
			Long cpuId = hospitalByKey.getCpuId();
			if(cpuId != null){
			TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
			fieldVisitRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
			}
			
		}
		
		fieldVisitRequest.setPolicy(reimbursement.getClaim().getIntimation()
				.getPolicy());
		status.setKey(ReferenceTable.INITITATE_FVR);
		stage.setKey(bean.getStageKey());
		
		fieldVisitRequest.setStatus(status);
		fieldVisitRequest.setStage(stage);
		fieldVisitRequest.setTransactionKey(reimbursement.getKey());
		fieldVisitRequest.setTransactionFlag("R");
		String userName = SHAUtils.getUserNameForDB(bean.getStrUserName());
		fieldVisitRequest.setCreatedBy(userName);
		
		SelectValue allocationTo = null;
		SelectValue assignTo = null;
		SelectValue priority = null;
		
		if(bean.getStageKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
			allocationTo = bean.getPreauthMedicalProcessingDetails()
					.getAllocationTo();
			
			priority = bean.getPreauthMedicalProcessingDetails().getPriority();
			
		}else{
			allocationTo = bean.getPreauthMedicalDecisionDetails()
					.getAllocationTo();
			assignTo = bean.getPreauthMedicalDecisionDetails().getAssignTo();
			
			priority = bean.getPreauthMedicalDecisionDetails().getPriority();
	
		}
		
				
		if (allocationTo != null) {
			MastersValue value = new MastersValue();
			value.setKey(allocationTo.getId());
			value.setValue(allocationTo.getValue());
			fieldVisitRequest.setAllocationTo(value);
		}
		
		if (assignTo != null) {
			MastersValue value = new MastersValue();
			value.setKey(assignTo.getId());
			value.setValue(assignTo.getValue());
			fieldVisitRequest.setAssignTo(value);
		}
		
		if (priority != null) {
			MastersValue value = new MastersValue();
			value.setKey(priority.getId());
			value.setValue(priority.getValue());
			fieldVisitRequest.setPriority(value);
		}		

		entityManager.persist(fieldVisitRequest);
		entityManager.flush();
		log.info("------FieldVisitRequest------>"+fieldVisitRequest+"<------------");

		if (fieldVisitRequest.getKey() != null) {
			bean.setFvrKey(fieldVisitRequest.getKey());
			
			viewFVRService.saveTriggerPoints(fieldVisitRequest,bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
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

	private String getRemarksForClaimRequest(Long statusKey, PreauthDTO bean,
			Reimbursement reimbursement) {
		Status status = new Status();
		Stage stage = new Stage();
		String remarks = "";
		switch (null != statusKey?statusKey.intValue():0) {
		case 87:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApproverReply();
//			updateMedicalApproverForClaimRequest(bean, reimbursement, remarks);
			break;
		/*case 88:
			remarks = bean.getPreauthMedicalDecisionDetails().getQueryRemarks();
			if (!bean.getIsZonalReviewQuery()) {

				status.setKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
				stage.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
				reimbursement.setStatus(status);
//				String strUserName = bean.getStrUserName();
//				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//				reimbursement.setModifiedBy(userNameForDB);
//				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//				entityManager.merge(reimbursement);
//				entityManager.flush();

				ReimbursementQuery reimbursementQuery = new ReimbursementQuery();
				reimbursementQuery.setQueryRemarks(remarks);
				reimbursementQuery.setReimbursement(reimbursement);
				reimbursementQuery.setStatus(status);
				reimbursementQuery.setStage(stage);
				String createdBy = bean.getStrUserName();
				if (bean.getStrUserName() != null
						&& bean.getStrUserName().length() > 15) {
					createdBy = SHAUtils.getTruncateString(
							bean.getStrUserName(), 15);
				}
				reimbursementQuery.setCreatedBy(createdBy);
				reimbursementQuery.setCreatedDate(new Timestamp(System
						.currentTimeMillis()));
				entityManager.persist(reimbursementQuery);
				entityManager.flush();
				log.info("------ReimbursementQuery------>"+reimbursementQuery+"<------------");
				bean.setQueryKey(reimbursementQuery.getKey());
			} else {
//				ReimbursementQuery reimbursementQuery = getReimbursementQuery(reimbursement
//						.getKey());
				status.setKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
				stage.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
				reimbursement.setStatus(status);

				ReimbursementQuery query = new ReimbursementQuery();
				query.setQueryRemarks(remarks);
				query.setReimbursement(reimbursement);
				query.setStatus(status);
				query.setStage(stage);
				String createdBy = bean.getStrUserName();
				if (bean.getStrUserName() != null
						&& bean.getStrUserName().length() > 15) {
					createdBy = SHAUtils.getTruncateString(
								bean.getStrUserName(), 15);
				}
				query.setCreatedBy(createdBy);
				query.setCreatedDate(new Timestamp(System
							.currentTimeMillis()));
				entityManager.persist(query);
				entityManager.flush();
				log.info("------ReimbursementQuery------>"+query+"<------------");
				bean.setQueryKey(query.getKey());
//				}
			}
			break;*/
		case 86:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getEscalationRemarks();
			if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
				bean.getPreauthMedicalDecisionDetails().setReasonForRefering(remarks);
				createSpecialist(bean, reimbursement, reimbursement.getStatus(), reimbursement.getStage());
			}else{
				createEscalate(bean, reimbursement, status, stage, remarks);
			}
			break;
		case 85:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getEscalateReply();
			createEscalateReply(bean, reimbursement, status, stage, remarks);
			break;
		case 81:
			createCoordinator(bean, reimbursement);
			break;
		case 83:
			createSpecialist(bean, reimbursement, status, stage);
			break;
		/*case 80:
			createInvestigation(bean, reimbursement, status, stage);
			break;
		case 79:
			createInitiateFVR(bean, reimbursement, status, stage);
			break;*/
		case 93:
			status.setKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			createReimbursementRejection(bean, reimbursement, status);
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			String remarks2 = bean.getPreauthMedicalDecisionDetails().getRejectionRemarks2();
			break;
		case 78:

			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApprovalRemarks();
			break;
			
		case 148:
			remarks = bean.getPreauthMedicalDecisionDetails()
			.getCancelRemarks();
		break;
		
		case 158:
			status.setKey(ReferenceTable.PAYMENT_SETTLED);
			createReimbursementRejection(bean, reimbursement, status);
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			break;		

		default:
			break;
		}
		return remarks;
	}

	private void updateMedicalApproverForClaimRequest(PreauthDTO bean,
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
			log.info("------MedicalApprover------>"+medicalApproverByKey+"<------------");
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
			log.info("------MedicalApprover------>"+medicalApprover+"<------------");
		}
	}
	


	/*private ReimbursementQuery getReimbursementQuery(Long rodKey) {

		ReimbursementQuery reimbursementQueryByReimbursmentKey = getReimbursementQueryByReimbursmentKey(rodKey);

		return reimbursementQueryByReimbursmentKey;

	}*/

	private String getRemarksForClaimFinancial(Long statusKey, PreauthDTO bean,
			Reimbursement reimbursement) {
		Status status = new Status();
		Stage stage = new Stage();
		String remarks = "";
		String createdBy;
		
		switch (statusKey.intValue()) {
		case 104:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApproverReply();
//			remarks = referToMedicalForFinancial(bean, reimbursement, remarks);
			break;
		case 105:

			remarks = bean.getPreauthMedicalDecisionDetails()
					.getFinancialApproverRemarks();
//			saveReferToBilling(bean, reimbursement, remarks);
			break;
		case 112:

			stage.setKey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
			status.setKey(ReferenceTable.FINANCIAL_QUERY_STATUS);
			reimbursement.setStatus(status);
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//			reimbursement.setModifiedBy(userNameForDB);
//			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(reimbursement);
//			entityManager.flush();

			remarks = bean.getPreauthMedicalDecisionDetails().getQueryRemarks();
			
			
			ReimbursementQuery reimbursementQuery = new ReimbursementQuery();
			reimbursementQuery.setQueryRemarks(remarks);
			reimbursementQuery.setStatus(status);
			reimbursementQuery.setStage(reimbursement.getStage());
			reimbursementQuery.setReimbursement(reimbursement);
			reimbursementQuery.setModifiedBy(userNameForDB);
			// add installment v3 CR
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) 
					&& bean.getPreauthMedicalDecisionDetails().getRelInstalmentOptFlag() != null && bean.getPreauthMedicalDecisionDetails().getRelInstalmentOptFlag()){
				reimbursementQuery.setRelInstaFlg(SHAConstants.YES_FLAG);
			}else{
				reimbursementQuery.setRelInstaFlg(SHAConstants.N_FLAG);
			}
			
	
			createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
						15);
			}
			reimbursementQuery.setCreatedBy(createdBy);

			entityManager.persist(reimbursementQuery);
			entityManager.flush();
			log.info("------ReimbursementQuery------>"+reimbursementQuery+"<------------");
			bean.setQueryKey(reimbursementQuery.getKey());
			break;
		case 108:
			createCoordinator(bean, reimbursement);
			break;
		case 110:
			createSpecialist(bean, reimbursement, status, stage);
			break;
		case 107:
			createInvestigation(bean, reimbursement, status, stage);
			break;
		case 106:
			createInitiateFVR(bean, reimbursement, status, stage);
			break;
		case 117:
			status.setKey(ReferenceTable.FINANCIAL_REJECT_STATUS);
			createReimbursementRejection(bean, reimbursement, status);
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			break;
		case 103:

			remarks = bean.getPreauthMedicalDecisionDetails()
					.getFinancialRemarks();
			break;
		case 150:

			remarks = bean.getPreauthMedicalDecisionDetails()
					.getCancelRemarks();
			break;
			
		case 158:
			status.setKey(ReferenceTable.PAYMENT_SETTLED);
			createReimbursementRejection(bean, reimbursement, status);
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getRejectionRemarks();
			break;
			
		default:
			break;
		}
		return remarks;
	}

	private void saveReferToBilling(PreauthDTO bean,
			Reimbursement reimbursement, String remarks) {
		String createdBy;
		MedicalApprover billingMedicalApprover = new MedicalApprover();
		billingMedicalApprover.setReimbursement(reimbursement);
		billingMedicalApprover.setApproverReply(remarks);
		billingMedicalApprover
				.setRecordType(SHAConstants.FINANCIAL_REBILLING);
		billingMedicalApprover.setReasonForReferring(bean
				.getPreauthMedicalDecisionDetails()
				.getReasonForReferringToBilling());

		createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
					15);
		}
		billingMedicalApprover.setCreatedBy(createdBy);

		entityManager.persist(billingMedicalApprover);
		entityManager.flush();
		log.info("------MedicalApprover------>"+billingMedicalApprover+"<------------");

		entityManager.persist(billingMedicalApprover);
		entityManager.flush();
	}

	private String referToMedicalForFinancial(PreauthDTO bean,
			Reimbursement reimbursement, String remarks) {
		String createdBy;
		if (bean.getPreauthMedicalDecisionDetails().getApproverReply() == null) {
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getMedicalApproverRemarks();
		}
		MedicalApprover medicalApprover = new MedicalApprover();
		medicalApprover.setReimbursement(reimbursement);
		medicalApprover.setApproverReply(remarks);
		medicalApprover.setRecordType(SHAConstants.FINANCIAL_REMEDICAL);
		medicalApprover.setReferringRemarks(remarks);
		medicalApprover.setReasonForReferring(bean
				.getPreauthMedicalDecisionDetails().getReasonForRefering());

		createdBy = bean.getStrUserName();
		if (bean.getStrUserName() != null
				&& bean.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(bean.getStrUserName(),
					15);
		}
		medicalApprover.setCreatedBy(createdBy);

		entityManager.persist(medicalApprover);
		entityManager.flush();
		log.info("------MedicalApprover------>"+medicalApprover+"<------------");

		return remarks;
	}

	private String getRemarksForClaimBilling(Long statusKey, PreauthDTO bean,
			Reimbursement reimbursement) {
		//Status status = new Status();
		//Stage stage = new Stage();
		String remarks = "";
		//String userName = bean.getStrUserName();
		//String userNameForDB = SHAUtils.getUserNameForDB(userName);
		switch (statusKey.intValue()) {
		case 100:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApproverReply();
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getApproverReply();
			if (bean.getPreauthMedicalDecisionDetails().getApproverReply() == null) {
				remarks = bean.getPreauthMedicalDecisionDetails()
						.getMedicalApproverRemarks();
			}
			break;
		case 98:
			createCoordinator(bean, reimbursement);
			break;
		case 101:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getBillingRemarks();
			break;
			
		case 149:
			remarks = bean.getPreauthMedicalDecisionDetails()
					.getCancelRemarks();
			break;

		default:
			break;
		}
		return remarks;
	}

	private void updateReferToMedicalApproverForBilling(PreauthDTO bean,
			Reimbursement reimbursement, String userNameForDB) {
		Long latestMedicalApproverKey = getLatestMedicalApproverForRebilling(reimbursement
				.getKey());
		if (latestMedicalApproverKey != null) {
			MedicalApprover medicalApproverByKey = getMedicalApproverByKey(latestMedicalApproverKey);
			// medicalApproverByKey.setApproverReply(remarks);
			medicalApproverByKey.setReasonForReferring(bean
					.getPreauthMedicalDecisionDetails()
					.getReasonForRefering());
			medicalApproverByKey.setReferringRemarks(bean
					.getPreauthMedicalDecisionDetails()
					.getMedicalApproverRemarks());
			medicalApproverByKey
					.setRecordType(SHAConstants.FINANCIAL_REMEDICAL);

			//String createdBy = bean.getStrUserName();
			/*if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						bean.getStrUserName(), 15);
			}*/
//				medicalApproverByKey.setCreatedBy(createdBy);
			medicalApproverByKey.setModifiedBy(userNameForDB);
		    medicalApproverByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(medicalApproverByKey);
			entityManager.flush();
			log.info("------MedicalApprover------>"+medicalApproverByKey+"<------------");
		} else {
			MedicalApprover medicalApprover = new MedicalApprover();
			medicalApprover.setReimbursement(reimbursement);
			// medicalApprover.setApproverReply(remarks);
			medicalApprover.setReasonForReferring(bean
					.getPreauthMedicalDecisionDetails()
					.getReasonForRefering());
			medicalApprover.setReferringRemarks(bean
					.getPreauthMedicalDecisionDetails()
					.getMedicalApproverRemarks());
			medicalApprover.setRecordType(SHAConstants.FINANCIAL_REMEDICAL);

			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						bean.getStrUserName(), 15);
			}
			medicalApprover.setCreatedBy(createdBy);
			medicalApprover.setCreatedDate(new Timestamp(System.currentTimeMillis()));

			entityManager.persist(medicalApprover);
			entityManager.flush();
			log.info("------MedicalApprover------>"+medicalApprover+"<------------");
		}
	}

	public String getOutcomeForClaimRequest(Reimbursement reimbursement,
			PreauthDTO bean) {

		String outCome = "";
		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			//outCome = "APPROVE";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_APPROVE_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) || (ReferenceTable.PAYMENT_SETTLED).equals(reimbursement.getStatus().getKey())) {
			//outCome = "REJECT";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REJECT_STATUS;
		} /*else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
			//outCome = "FVR";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_INTIATE_FVR_STATUS;
			
			// preauthRequest.setKey(reimbursement.getClaim().getKey());
		}*/ else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
			//outCome = "COORDINATOR";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS;
		}else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
		} /*else if (reimbursement.getStatus().getKey()
			}
			else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
				outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
			}
			/*else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_QUERY_STATUS;
		}*/ else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;
			/*if (bean != null
					&& bean.getPreauthMedicalDecisionDetails() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
					&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
							.getId() != null) {
				if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
						.getId().intValue() == ReferenceTable.RMA1) {
					outCome = "RMA1";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.RMA2)) {
					outCome = "2";
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.RMA3)) {
					outCome = "3";
				}
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.RMA4)) {
					outCome = "4";
				}
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.RMA5)) {
					outCome = "5";
				}
				else if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.RMA6)) {
					outCome = "6";
				}*/
				 if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
//					createSpecialist(bean, reimbursement, reimbursement.getStatus(), reimbursement.getStage());
					//outCome = "SPECIALIST";
					 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
				}
			}
		 else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
			 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;/* outcome for esclate reply to be set.**/
		//	outCome = "ESCALATEREPLY"; //***/
		} /*else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
			//outCome = "INVESTIGATION";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS;
		}*/ else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
		//	outCome = "SENDREPLY";
			//outCome = ;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
			//outCome = "SPECIALIST";
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS;
		}
		return outCome;
	}

	private String getOutComeForClaimBilling(Reimbursement reimbursement) {

		String outCome = "";

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
			//outCome = "APPROVE";
			outCome = SHAConstants.OUTCOME_BILLING_APPROVE_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {
			//outCome = "MEDICAL";
			outCome = SHAConstants.OUTCOME_BILLING_REFER_TO_MEDICAL; 
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
			//outCome = "COORDINATOR";
			outCome = SHAConstants.OUTCOME_BILLING_REFER_TO_COORDINATOR;
		}else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_CANCEL_ROD)) {
			//outCome = SHAConstants.CANCEL_ROD_OUTCOME;
			outCome = SHAConstants.OUTCOME_BILLING_CANCEL_ROD;
		}
		// Added for bill entry screen
		else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)) {
			//outCome = SHAConstants.BILL_ENTRY_OUTCOME;
			outCome = SHAConstants.OUTCOME_BILLING_REFER_TO_BILL_ENTRY;
		}
		return outCome;
	}
	

	private String getOutComeForFinancial(Reimbursement reimbursement) {

		String outCome = "";

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
			//outCome = "SPECIALIST";
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_SPECIALIST_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
			//outCome = "MEDICAL";
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_MEDICAL_APPROVER_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)) {
			//outCome = "COORDINATOR";
			outCome = SHAConstants.OUTCOME_FA_COORDINATOR_REPLY_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS)) {
			//outCome = "INVESTIGATION";
			outCome = SHAConstants.OUTCOME_FA_INTIATE_INVESTIGATION_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
			//outCome = "REBILLING";
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_BILLING_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS)) {
			//outCome = "FVR";
			outCome = SHAConstants.OUTCOME_FA_INTIATE_FVR_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
			//outCome = "QUERY";
			outCome = SHAConstants.OUTCOME_FA_QUERY_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REJECT_STATUS)
				|| reimbursement.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)  
				|| (ReferenceTable.PAYMENT_SETTLED).equals(reimbursement.getStatus().getKey())) {
			//outCome = "REJECT";
			outCome = SHAConstants.OUTCOME_FA_REJECTION_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_FA_APPROVE_STATUS;
			//outCome = SHAConstants.OUTCOME_FA_REJECTION_STATUS;
		}else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) {
			//outCome = SHAConstants.CANCEL_ROD_OUTCOME;
			outCome = SHAConstants.OUTCOME_FA_CANCEL_ROD_STATUS;
		}
		else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY)) {
			//outCome = SHAConstants.BILL_ENTRY_OUTCOME;
			outCome = SHAConstants.OUTCOME_FA_REFER_TO_BILL_ENTRY_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)) {
			outCome = "APPROVE";
		}
		return outCome;

	}

	private String getRemarksForZonalReview(Long statusKey, PreauthDTO bean,
			Reimbursement reimbursement) {
		/*Status status = new Status();
		Stage stage = new Stage();*/
		String remarks = "";
		switch (statusKey.intValue()) {
		case 73:
			remarks = bean.getPreauthMedicalProcessingDetails()
					.getQueryRemarks();
			// ReimbursementQuery reimbursementQuery = new ReimbursementQuery();
			// reimbursementQuery.setQueryRemarks(remarks);
			// reimbursementQuery.setReimbursement(reimbursement);
			// entityManager.persist(reimbursementQuery);
			// entityManager.flush();
			break;
		case 76:
			// createCoordinator(bean, reimbursement);
			break;
		case 74:
			remarks = bean.getPreauthMedicalProcessingDetails()
					.getRejectionRemarks();
			break;
		case 75:
			remarks = bean.getPreauthMedicalProcessingDetails()
					.getApprovalRemarks();
			break;
		case 147:
			remarks = bean.getPreauthMedicalProcessingDetails()
					.getCancelRemarks();
			break;
		case 207:
			Status status = new Status();
			Stage stage = new Stage();
			createInitiateFVR(bean, reimbursement, status, stage);
			break;
		default:
			break;
		}
		return remarks;
	}

	public Reimbursement getReimbursementbyRod(Long key,
			EntityManager entityManager) {
		Query findType = entityManager.createNamedQuery(
				"Reimbursement.findByKey").setParameter("primaryKey", key);
		List<Reimbursement> reimbursement = (List<Reimbursement>) findType
				.getResultList();
		if (reimbursement.size() > 0) {
			return reimbursement.get(0);
		} else {
			return null;
		}
	}

	public Reimbursement getReimbursementbyRod(Long key) {
		try {
			Query findType = entityManager.createNamedQuery(
					"Reimbursement.findByKey").setParameter("primaryKey", key);
			Reimbursement reimbursement = (Reimbursement) findType
					.getSingleResult();
			return reimbursement;
		} catch (Exception e) {
			return null;
		}
	}

	public Status getStatusByKey(Long key) {

		try {
			Query findType = entityManager.createNamedQuery("Status.findByKey")
					.setParameter("statusKey", key);
			//Status status = (Status) findType.getSingleResult();
			List<Status> statusList  = findType.getResultList();
			if(null != statusList && !statusList.isEmpty())
			{
				entityManager.refresh(statusList.get(0));
				return statusList.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * public List<EmployeeMasterDTO> getListOfEmployeeDetails(String query) {
	 * List<EmployeeMasterDTO> employeeDTOList = null; Query findType =
	 * entityManager.createNamedQuery(
	 * "TmpEmployee.getEmpByLoginId");//.setParameter("primaryKey", key);
	 * findType.setParameter("loginId", "%"+query+"%"); List<TmpEmployee>
	 * employeeList = findType.getResultList(); if(null != employeeList &&
	 * !employeeList.isEmpty()) { //employeeDTOList = new
	 * ArrayList<ExtraEmployeeEffortDTO>(); ZonalMedicalReviewMapper
	 * zonalMedicalMapper = new ZonalMedicalReviewMapper(); employeeDTOList =
	 * zonalMedicalMapper. getEmployeeMasterDTO(employeeList); } return
	 * employeeDTOList;
	 * 
	 * }
	 */
	public List<EmployeeMasterDTO> getListOfEmployeeDetails(String query) {
		List<EmployeeMasterDTO> employeeDTOList = null;
		Query findType = entityManager
				.createNamedQuery("TmpEmployee.getEmpByName");// .setParameter("primaryKey",
																// key);
		findType.setParameter("empName", "%" + query + "%");
		List<TmpEmployee> employeeList = findType.getResultList();
		if (null != employeeList && !employeeList.isEmpty()) {
			// employeeDTOList = new ArrayList<ExtraEmployeeEffortDTO>();
			ZonalMedicalReviewMapper zonalMedicalMapper = ZonalMedicalReviewMapper.getInstance();
//			ZonalMedicalReviewMapper.getAllMapValues();
			employeeDTOList = zonalMedicalMapper
					.getEmployeeMasterDTO(employeeList);
		}
		return employeeDTOList;

	}

	public Boolean validateUserForRRCRequest(PreauthDTO preauthDTO) {
		List<RRCRequest> rrcReqList = null;

		if ((SHAConstants.PROCESS_PED_QUERY).equalsIgnoreCase(preauthDTO
				.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PROCESS_REJECTION).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PROCESS_CONVERT_CLAIM)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.FIELD_VISIT).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PED_REQUEST_PROCESS)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PED_REQUEST_APPROVER)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ADVISE_ON_PED).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_COORDINATOR_REPLY)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_INVESTIGATION_INTIATED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ASSIGN_INVESTIGATION_INTIATED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ACKNOWLEDGE_INVESTIGATION)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ADD_ADDITIONAL_DOCUMENTS)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_UPLOAD_INVESTIGATION)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.CREATE_ROD).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.BILL_ENTRY).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage()) || (SHAConstants.DRAFT_INVESTIGATION_INTIATED)
								.equalsIgnoreCase(preauthDTO.getRrcDTO()
										.getProcessingStage())) {
			rrcReqList = getRRCRequestListFromClaimKey(preauthDTO.getRrcDTO()
					.getClaimDto().getKey());
			preauthDTO.setStrUserName(preauthDTO.getRrcDTO().getStrUserName());
		} else {
			rrcReqList = getRRCRequestListFromClaimKey(preauthDTO.getClaimDTO()
					.getKey());
		}

		// List<RRCRequest> rrcReqList =
		// getRRCRequestListFromRodKey(preauthDTO.getKey());
		// List<RRCRequest> rrcReqList =
		// getRRCRequestListFromRodKey(preauthDTO.get);
		Boolean isValid = true;
		// List<RRCRequest> rrcValList = new ArrayList<RRCRequest>();
		if (null != rrcReqList && !rrcReqList.isEmpty()) {
			for (RRCRequest rrcRequest : rrcReqList) {
				if (null != rrcRequest.getRequestedStageId()) {
					if (null != preauthDTO.getRrcDTO().getRequestedStageId()) {
						if ((preauthDTO.getRrcDTO().getRequestedStageId()
								.equals(rrcRequest.getRequestedStageId()
										.getKey()))) {
							if (preauthDTO.getStrUserName().equalsIgnoreCase(
									rrcRequest.getCreatedBy())) {
								isValid = false;
								break;
							}
						}
					}
				}
			}
		}
		return isValid;
	}

	/*private List<RRCRequest> getRRCRequestListFromRodKey(Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("RRCRequest.findByReimburesmentKey");
		query = query.setParameter("rodKey", reimbursementKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		return rrcRequestList;
	}*/

	private List<RRCRequest> getRRCRequestListFromClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("RRCRequest.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		return rrcRequestList;
	}
	
	public List<RRCRequest> getRRCRequestListForBpmnInitiate(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("RRCRequest.findByClaimKeyForInitiate");
		query = query.setParameter("claimKey", claimKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		return rrcRequestList;
	}

	// Added for RRC Request Submit;
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String submitRRCRequestValues(PreauthDTO preauthDTO, String claimType) {
		String rrcRequestNo = "";
		try {
			rrcRequestNo = saveRRCRequestValues(preauthDTO, claimType);
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		return rrcRequestNo;
	}

	private String saveRRCRequestValues(PreauthDTO preauthDTO, String claimType) {

		/*
		 * Currently , as per mock up, there is no feasiblity of adding new row
		 * in quantum reduction details table. Hence the below list will have
		 * only one record. But in future, if they would require to add new row,
		 * then we would have list of DTO returned from table. To cater both
		 * needs, in DTO we have a setter and getter for list as well as for
		 * single DTO.
		 * 
		 * Currenlty code implementation is given for one record. If in future,
		 * multiple records are retreived, then the commented code can be
		 * uncommented and used.
		 */

		// List<QuantumReductionDetailsDTO> quantumReductionDetailsList =
		// preauthDTO.getRrcDTO().getQuantumReductionDetailsDTOList();
		QuantumReductionDetailsDTO quantumReductionDetailsDTO = preauthDTO
				.getRrcDTO().getQuantumReductionDetailsDTO();
		
		ZonalMedicalReviewMapper zonalMedicalMapper = ZonalMedicalReviewMapper.getInstance();
		ReviewRRCRequestMapper rrcRequestMapper  = ReviewRRCRequestMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();

		//String intimationNumber = "";
		// Claim claim = null;
		/**
		 * Since RRC request stage and status is not finalized, setting zonal
		 * stage and status for testing purpose. Once stage and status for RRC
		 * request is finalized, the same will be incorporated.
		 **/
		preauthDTO.getRrcDTO().setStageKey(ReferenceTable.RRC_STAGE);
		preauthDTO.getRrcDTO().setStatusKey(
				ReferenceTable.INTIATE_RRC_REQUEST_STATUS);

		Reimbursement reimbursement = null;
		Preauth preauth = null;
		
		String rrcReqNo = "";
		Claim claim = null;

		if (SHAConstants.CLAIMREQUEST_REIMBURSEMENT.equalsIgnoreCase(claimType))
			reimbursement = getReimbursementByKey(preauthDTO.getKey());
		else if (SHAConstants.CLAIMREQUEST_CASHLESS.equalsIgnoreCase(claimType)) 
		{
			if (((SHAConstants.PROCESS_PREAUTH).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || 
					(SHAConstants.PROCESS_ENHANCEMENT).equalsIgnoreCase(preauthDTO.getRrcDTO()
							.getProcessingStage()) || (SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH)
							.equalsIgnoreCase(preauthDTO.getRrcDTO()
									.getProcessingStage()) || (SHAConstants.PROCESS_WITHDRAW_PREAUTH)
									.equalsIgnoreCase(preauthDTO.getRrcDTO()
											.getProcessingStage()) 	|| (SHAConstants.PROCESS_DOWNSIZE_PREAUTH)
											.equalsIgnoreCase(preauthDTO.getRrcDTO()
													.getProcessingStage())
							)) 
			{
				preauth = getPreauthById(preauthDTO.getKey());
				claim = preauth.getClaim();
			} 
			else if  ((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage())) 
			{
				if (null != preauthDTO.getIsReMedical()
						&& preauthDTO.getIsRepremedical()) 
				{
					preauth = getPreauthById(preauthDTO.getKey());
					claim = preauth.getClaim();
				}
				else
				{
					claim =  getClaimByClaimKey (preauthDTO
							.getClaimDTO().getKey());
				}
			} 
			else if ((SHAConstants.PROCESS_PRE_MEDICAL).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()))
					{
						if (null != preauthDTO.getIsReMedical()
								&& preauthDTO.getIsRepremedical()) 
						{
							preauth = getPreauthById(preauthDTO.getKey());
							claim = preauth.getClaim();
						}
						else
						{
							claim =  getClaimByClaimKey (preauthDTO
									.getClaimDTO().getKey());
						}
					}
			else if (null != preauthDTO.getRrcDTO().getPreauthKey()) 
			{
				preauth = getPreauthById(preauthDTO.getRrcDTO().getPreauthKey());
				claim = preauth.getClaim();
			}
		}
		// preauth = getPreviousRODByClaimKey(claimKey)

		

		if ((SHAConstants.PROCESS_PED_QUERY).equalsIgnoreCase(preauthDTO .getRrcDTO().getProcessingStage()) || 
				(SHAConstants.PROCESS_REJECTION).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || 
				(SHAConstants.PROCESS_CONVERT_CLAIM).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) ||
				(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || 
				(SHAConstants.FIELD_VISIT).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || 
				(SHAConstants.PED_REQUEST_PROCESS).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || 
				(SHAConstants.PED_REQUEST_APPROVER).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage())
				|| (SHAConstants.ADVISE_ON_PED).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_COORDINATOR_REPLY)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_INVESTIGATION_INTIATED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ASSIGN_INVESTIGATION_INTIATED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ACKNOWLEDGE_INVESTIGATION)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())  
				|| (SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT)
				.equalsIgnoreCase(preauthDTO.getRrcDTO()
						.getProcessingStage())
			
				|| (SHAConstants.ADD_ADDITIONAL_DOCUMENTS)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.PROCESS_UPLOAD_INVESTIGATION)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())
				|| (SHAConstants.CREATE_ROD).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage())
				|| (SHAConstants.BILL_ENTRY).equalsIgnoreCase(preauthDTO
						.getRrcDTO().getProcessingStage()) ||
						(SHAConstants.INITIATE_RRC_REQUEST).equalsIgnoreCase(preauthDTO
								.getRrcDTO().getProcessingStage()))
						
		{
			rrcReqNo = generateRRCRequestNo(preauthDTO.getRrcDTO()
					.getNewIntimationDTO().getIntimationId(), preauthDTO
					.getRrcDTO().getClaimDto().getKey());
			//intimationNumber = preauthDTO.getRrcDTO().getNewIntimationDTO().getIntimationId();
			claim = getClaimByClaimKey(preauthDTO.getRrcDTO().getClaimDto()
					.getKey());
			preauthDTO.setStrUserName(preauthDTO.getRrcDTO().getStrUserName());
		}

		else {
			if (!(SHAConstants.PROCESS_CLAIM_REGISTRATION
					.equalsIgnoreCase(preauthDTO.getRrcDTO()
							.getProcessingStage()))) {
				rrcReqNo = generateRRCRequestNo(preauthDTO
						.getNewIntimationDTO().getIntimationId(), preauthDTO
						.getClaimDTO().getKey());

				//intimationNumber = preauthDTO.getNewIntimationDTO().getIntimationId();
			}
		}
		preauthDTO.getRrcDTO().setRrcRequestNo(rrcReqNo);
		// if(null != quantumReductionDetailsList &&
		// !quantumReductionDetailsList.isEmpty())
		if (null != quantumReductionDetailsDTO) {

			// for (QuantumReductionDetailsDTO quantumReductionDetailsDTO :
			// quantumReductionDetailsList) {
			RRCRequest rrcRequest = zonalMedicalMapper
					.getQuantumReductionDetails(quantumReductionDetailsDTO);
			rrcRequest.setRrcRequestNumber(rrcReqNo);
			MastersValue significantInfo = null;
			if (null != preauthDTO.getRrcDTO()
					.getSignificantClinicalInformation()) {
				significantInfo = new MastersValue();
				significantInfo.setKey(preauthDTO.getRrcDTO()
						.getSignificantClinicalInformation().getId());
				// /significantInfo.setValue(preauthDTO.getRrcDTO().getSignificantClinicalInformation().getValue());
				rrcRequest.setSignificantClinicalId(significantInfo);
			}
			rrcRequest.setRequestRemarks(preauthDTO.getRrcDTO().getRemarks());
			Stage rrcStage = new Stage();
			rrcStage.setKey(preauthDTO.getRrcDTO().getStageKey());

			Status rrcStatus = new Status();
			rrcStatus.setKey(preauthDTO.getRrcDTO().getStatusKey());

			MastersValue masRequestedTypeId = new MastersValue();
			masRequestedTypeId.setKey(ReferenceTable.RRC_REQUEST_STATUS_FRESH);

			Stage requestedStageId = new Stage();
			requestedStageId.setKey(preauthDTO.getRrcDTO()
					.getRequestedStageId());

			rrcRequest.setRequestedStageId(requestedStageId);
			rrcRequest.setRequestedTypeId(masRequestedTypeId);
			rrcRequest.setStage(rrcStage);
			rrcRequest.setStatus(rrcStatus);
			rrcRequest.setRrcInitiatedDate((new Timestamp(System
					.currentTimeMillis())));
			// rrcRequest.setCreatedBy("zonalmedical");
			rrcRequest.setCreatedBy(preauthDTO.getStrUserName());
			rrcRequest.setActiveStatus(1l);
			rrcRequest.setRequestorID(preauthDTO.getStrUserName());
		
			// rrcRequest.setCreatedBy(createdBy);
			// rrcRequest.setCreatedBy(preauthDTO.g());
			if (SHAConstants.CLAIMREQUEST_REIMBURSEMENT.equalsIgnoreCase(claimType)) {
				if (null != reimbursement) {
					rrcRequest.setReimbursement(reimbursement);
					claim = reimbursement.getClaim();
				}
				rrcRequest.setClaim(claim);
			}

			else if (SHAConstants.CLAIMREQUEST_CASHLESS.equalsIgnoreCase(claimType)) 
			{
				if (null != preauth) {
					rrcRequest.setPreauth(preauth);
					// Claim claim =
					// getClaimByClaimKey(preauthDTO.getClaimKey());
					rrcRequest.setClaim(preauth.getClaim());
				} else {
					// Claim claim =
					// getClaimByClaimKey(preauthDTO.getClaimKey());
					rrcRequest.setClaim(claim);
				}
			}

			rrcRequest.setRrcType("RRC INTIATED");
			//IMSSUPPOR-31181
			if(rrcRequest.getAnh()!=null){
				if(rrcRequest.getAnh().equals(SHAConstants.YES)){
					rrcRequest.setAnh(ReferenceTable.YES_FLAG);
				}else{
					rrcRequest.setAnh(ReferenceTable.NO_FLAG);
				}
			}else{
				if(claim!=null && claim.getIntimation().getHospital()!=null){
					Hospitals hospitalObj =	getHospitalByKey(claim.getIntimation().getHospital());
					if(hospitalObj!=null && hospitalObj.getHospitalType()!=null && hospitalObj.getHospitalType().getValue().equalsIgnoreCase(SHAConstants.NETWORK_HOSPITAL)){
						rrcRequest.setAnh(ReferenceTable.YES_FLAG);
					}else{
						rrcRequest.setAnh(ReferenceTable.NO_FLAG);
					}
				}
			}

			/*
			 * Code for persisting RRC request stage and request stage is
			 * pending. Need DB team to assign stage and status value for the
			 * same. Post that the code changes will be done.
			 */
			// rrcRequest.setReq
			entityManager.persist(rrcRequest);
			entityManager.flush();
			entityManager.refresh(rrcRequest);
			log.info("------RRCRequest------>"+rrcRequest+"<------------");

			List<ExtraEmployeeEffortDTO> employeeDetailsList = preauthDTO
					.getRrcDTO().getEmployeeEffortList();
			if (null != employeeDetailsList && !employeeDetailsList.isEmpty()) {
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : employeeDetailsList) {
					RRCDetails rrcDetails = zonalMedicalMapper
							.getEmployeeDetails(extraEmployeeEffortDTO);
					if(rrcDetails.getEmployeeId() !=null){
						String employee[] = rrcDetails.getEmployeeId().split("-");
						rrcDetails.setEmployeeId(employee[0].trim());
						rrcDetails.setEmployeeName(employee[1].trim());
					}
					rrcDetails.setRrcRequest(rrcRequest.getRrcRequestKey());
					entityManager.persist(rrcDetails);
					entityManager.flush();
					entityManager.refresh(rrcDetails);
					log.info("------RRCRequest------>"+rrcRequest+"<------------");
				}
				entityManager.clear();
			}
			
			List<ExtraEmployeeEffortDTO> rrcCategoryDetailsList = preauthDTO.getRrcDTO().getRrcCategoryList();
			if (null != rrcCategoryDetailsList && !rrcCategoryDetailsList.isEmpty()) {
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcCategoryDetailsList) {
					RRCCategory rrcCategory = rrcRequestMapper.getRRCCategoryDetails(extraEmployeeEffortDTO);
					rrcCategory.setRrcRequest(rrcRequest.getRrcRequestKey());
					entityManager.persist(rrcCategory);
					entityManager.flush();
					entityManager.refresh(rrcCategory);
					log.info("------RRCCategory------>"+rrcCategory+"<------------");
				}
				entityManager.clear();
			}

			/*
			 * if(!submitRRCRequestTaskToBPM(preauthDTO,rrcRequest.getRrcRequestKey
			 * (),claimType)) { rrcReqNo = null; }
			 */

			if (SHAConstants.CLAIMREQUEST_REIMBURSEMENT
					.equalsIgnoreCase(claimType)) {
				//if (!submitRRCRequestTaskToBPM(preauthDTO,
				if(!submitRRCRequestTaskToDB(preauthDTO,
						rrcRequest.getRrcRequestKey(),claim)) {
					rrcReqNo = null;
				}
			} else if (SHAConstants.CLAIMREQUEST_CASHLESS
					.equalsIgnoreCase(claimType)) {
				//if (!submitRRCRequestTaskToBPMForCashless(preauthDTO,
				if (!submitRRCRequestTaskToDBForCashless(preauthDTO,
						rrcRequest.getRrcRequestKey(),claim)) {
					rrcReqNo = null;
				}
			}
		}
		return rrcReqNo;
	}

	/*
	 * @SuppressWarnings("unchecked") private Claim getClaimByClaimKey(Long
	 * claimKey) { Query query =
	 * entityManager.createNamedQuery("Claim.findByClaimKey");
	 * query.setParameter("claimKey", claimKey); List<Claim> claim =
	 * (List<Claim>)query.getResultList();
	 * 
	 * if(claim != null && ! claim.isEmpty()){ for (Claim claim2 : claim) {
	 * entityManager.refresh(claim2); } return claim.get(0); } else{ return
	 * null; } }
	 */

	// }

	/*private Boolean submitRRCRequestTaskToBPM(PreauthDTO preauthDTO,
			Long rrcRequestKey) {
		*//**
		 * Need to remove hardcoded user name and password, once active
		 * directory is finalized.
		 * *//*
		try {
			RRC rrcTask = BPMClientContext.getIntiateRRCRequestTask(
					BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);

			HumanTask humanTaskForRRCRequest = preauthDTO.getRodHumanTask();
			RRCType rrcType = new RRCType();
			PayloadBOType payloadBO = null;
			IntimationType intimationType = null;
			ClaimRequestType claimType = null;
			ClaimType claimTypeValue = null;
			ClassificationType classificationType = null;
			claimTypeValue = new ClaimType();			
			claimTypeValue.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
			
			
			if (null != humanTaskForRRCRequest) {
				payloadBO = humanTaskForRRCRequest.getPayload();
			} else {
				humanTaskForRRCRequest = new HumanTask();
				payloadBO = new PayloadBOType();
				intimationType = new IntimationType();
				claimType = new ClaimRequestType();
				claimTypeValue = new ClaimType();
				
				
				
				if((SHAConstants.ACKNOWLEDGE_DOC_RECEIVED).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || (SHAConstants.CREATE_ROD).equalsIgnoreCase(preauthDTO .getRrcDTO().getProcessingStage())
						|| (SHAConstants.BILL_ENTRY).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || (SHAConstants.ADD_ADDITIONAL_DOCUMENTS).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()))
				{
					intimationType.setIntimationNumber(preauthDTO
							.getNewIntimationDTO().getIntimationId());
					claimType.setCpuCode(preauthDTO.getNewIntimationDTO()
							.getCpuCode());
					intimationType.setIntimationNumber(preauthDTO.getRrcDTO().getNewIntimationDTO()
							.getIntimationId());
					claimType.setCpuCode(preauthDTO.getRrcDTO().getNewIntimationDTO()
							.getCpuCode());
					
					
					classificationType = new ClassificationType();
					
					Claim claimObject = getClaimByClaimKey(preauthDTO.getRrcDTO().getClaimDto().getKey());
					
					Insured insured = claimObject.getIntimation().getInsured();
					
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
					
					RRCDTO rrcDTO = preauthDTO.getRrcDTO();
					if(rrcDTO.getStageKey() != null){
						Stage stage = entityManager.find(Stage.class, rrcDTO.getStageKey());
						classificationType.setSource(stage.getStageName());
					}
					
//					if(claimObject != null){
//						if(claimObject.getStage() != null){
//							classificationType.setSource(claimObject.getStage().getStageName());
//						}
//					}
					
					payloadBO.setIntimation(intimationType);
					payloadBO.setClaimRequest(claimType);
					payloadBO.setClassification(classificationType);
					
				}
			}
			humanTaskForRRCRequest.setOutcome("SUBMIT");
			rrcType.setRequestNo(preauthDTO.getRrcDTO().getRrcRequestNo());
			rrcType.setSource("FRESH");
			rrcType.setRequestType("FRESH");
			rrcType.setKey(rrcRequestKey);
			payloadBO.setRrc(rrcType);
			payloadBO.setClaim(claimTypeValue);
			humanTaskForRRCRequest.setPayload(payloadBO);
			rrcTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return false;
		}
	
		return true;}*/

	/*
	 * private void
	 * setCashlessPayloadBOToReimPayloadBO(com.oracle.xmlns.bpm.bpmobject
	 * .claimregcabusinessobjects.payloadbo.PayloadBOType cashlessPayloadBO ,
	 * PayloadBOType reimbPayloadBO) { reimbPayloadBO = new PayloadBOType();
	 * reimbPayloadBO.setClaim(cashlessPayloadBO.getClaim());
	 * 
	 * }
	 */

	/*private Boolean submitRRCRequestTaskToBPMForCashless(PreauthDTO preauthDTO,
			Long rrcRequestKey) {
		*//**
		 * Need to remove hardcoded user name and password, once active
		 * directory is finalized.
		 * *//*
		try {
			RRC rrcTask = BPMClientContext.getIntiateRRCRequestTask(
					BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);

			// HumanTask humanTaskForRRCRequest = preauthDTO.getRodHumanTask();

			PayloadBOType payloadBO = new PayloadBOType();

			IntimationType intimationType = new IntimationType();
			ClaimRequestType claimType = new ClaimRequestType();
			ClassificationType classificationType = new ClassificationType();
			ClaimType claimTypeValue = new ClaimType();
			claimTypeValue.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);

			if (!((SHAConstants.PROCESS_PREAUTH).equalsIgnoreCase(preauthDTO
					.getRrcDTO().getProcessingStage())
					|| (SHAConstants.PROCESS_ENHANCEMENT)
							.equalsIgnoreCase(preauthDTO.getRrcDTO()
									.getProcessingStage())
					|| (SHAConstants.PROCESS_PRE_MEDICAL)
							.equalsIgnoreCase(preauthDTO.getRrcDTO()
									.getProcessingStage()) || (SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT)
						.equalsIgnoreCase(preauthDTO.getRrcDTO()
								.getProcessingStage())))
			// if(null != preauthDTO.getRrcDTO())
			{
				if (null != preauthDTO.getRrcDTO().getNewIntimationDTO() && preauthDTO.getRrcDTO().getNewIntimationDTO().getIntimationId() != null) {
					intimationType.setIntimationNumber(preauthDTO.getRrcDTO()
							.getNewIntimationDTO().getIntimationId());
					claimType.setCpuCode(preauthDTO.getRrcDTO()
							.getNewIntimationDTO().getCpuCode());
				}
				else if (null != preauthDTO.getNewIntimationDTO()) {
					intimationType.setIntimationNumber(preauthDTO
							.getNewIntimationDTO().getIntimationId());
					claimType.setCpuCode(preauthDTO.getNewIntimationDTO()
							.getCpuCode());
				}
			} else if (null != preauthDTO.getNewIntimationDTO()) {
				intimationType.setIntimationNumber(preauthDTO
						.getNewIntimationDTO().getIntimationId());
				claimType.setCpuCode(preauthDTO.getNewIntimationDTO()
						.getCpuCode());
			}
			// intimationType.setIntimationNumber(intimationNumber);
			RRCType rrcType = new RRCType();

			rrcType.setRequestNo(preauthDTO.getRrcDTO().getRrcRequestNo());
			rrcType.setSource("FRESH");
			rrcType.setRequestType("FRESH");

			rrcType.setKey(rrcRequestKey);
			
			Claim claimObject = getClaimByClaimKey(preauthDTO.getClaimKey());
			
			Insured insured = null;
			if (null != claimObject)
			insured = claimObject.getIntimation().getInsured();
			
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
			
			RRCDTO rrcDTO = preauthDTO.getRrcDTO();
			if(rrcDTO.getRequestedStageId()!= null){
				Stage stage = entityManager.find(Stage.class, rrcDTO.getRequestedStageId());
				classificationType.setSource(stage.getStageName());
			}

			// payloadBO.set
			payloadBO.setIntimation(intimationType);
			payloadBO.setRrc(rrcType);
			payloadBO.setClaimRequest(claimType);
			payloadBO.setClassification(classificationType);
			payloadBO.setClaim(claimTypeValue);
			// humanTaskForRRCRequest.setPayload(payloadBO);
			rrcTask.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return false;
		}
	
		return true;
		}*/

	// Added for RRC Request Submit;
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String saveBillingWorksheetUploadDocumentValues(PreauthDTO preauthDTO) {
		// Boolean isSuccess = false;
		String isSuccess = "false";
		try {
			Reimbursement reimbursement = getReimbursementByKey(preauthDTO
					.getKey());
			List<UploadDocumentDTO> uploadDocumentList = preauthDTO
					.getUploadDocDTO().getBillingWorkSheetUploadDocumentList();
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//			ZonalMedicalReviewMapper.getAllMapValues();
			if (null != uploadDocumentList && !uploadDocumentList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocumentList) {

					RodBillSummary rodBillSummary = mapper
							.getRodBillSummary(uploadDocumentDTO);
					rodBillSummary.setReimbursement(reimbursement);
					if (null == rodBillSummary.getKey()) {
						entityManager.persist(rodBillSummary);
						entityManager.flush();
						log.info("------RodBillSummary------>"+rodBillSummary+"<------------");
					} else {
						entityManager.merge(rodBillSummary);
						entityManager.flush();
						log.info("------RodBillSummary------>"+rodBillSummary+"<------------");
					}
					// entityManager.refresh(rodBillSummary);
					// rodBillSummary.setDeleteFlag(uploadDocumentDTO.getD);
				}
				isSuccess = "true";
			} else {
				isSuccess = "No documents to upload";
			}

			List<UploadDocumentDTO> deletedDocsList = preauthDTO
					.getUploadDocDTO().getBillingWorksheetDeletedList();
			if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
				for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

					RodBillSummary rodBillSummary = mapper
							.getRodBillSummary(uploadDocumentDTO2);
					rodBillSummary.setReimbursement(reimbursement);
					rodBillSummary.setDeletedFlag("Y");

					if (null != uploadDocumentDTO2.getRodBillSummaryKey()) {
						entityManager.merge(rodBillSummary);
						entityManager.flush();
						log.info("------RodBillSummary------>"+rodBillSummary+"<------------");
					} /*
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 */

				}
				isSuccess = "true";
			}
			/*
			 * else { isSuccess = "No documents to upload"; }
			 */

		} catch (Exception e) {
			isSuccess = "exception";
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		return isSuccess;

	}

	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if (singleResult != null && !singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}

		return null;

	}

	private String generateRRCRequestNo(String strIntimationNo, Long claimKey) {
		Long count = getRRCRequestCountByClaim(claimKey);
		StringBuffer strRRCReq = new StringBuffer();

		Long lackCount = count + 001;
		strRRCReq.append(strIntimationNo).append("/").append(lackCount);
		return strRRCReq.toString();
	}

	private Long getRRCRequestCountByClaim(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("RRCRequest.CountAckByClaimKey");
		query.setParameter("claimkey", claimKey);
		Long countOfRRCReq = (Long) query.getSingleResult();
		return countOfRRCReq;
	}

	public List<UploadDocumentDTO> getRodBillSummaryDetails(Long rodKey) {
		List<UploadDocumentDTO> uploadList = null;
		Query query = entityManager
				.createNamedQuery("RodBillSummary.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);
		List<RodBillSummary> rodBillSummaryList = query.getResultList();
		if (null != rodBillSummaryList && !rodBillSummaryList.isEmpty()) {
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//			ZonalMedicalReviewMapper.getAllMapValues();
			uploadList = mapper
					.getUploadDocListForBillSummary(rodBillSummaryList);
		}

		return uploadList;

	}

//	@SuppressWarnings("unchecked")
//	public List<PolicywiseClaimReportDto> getClaimsPolicyWise(
//			String policyNumber) {
//
//		List<PolicywiseClaimReportDto> resultList = new ArrayList<PolicywiseClaimReportDto>();
//
//		if (policyNumber != null) {
//
//			try {
//				SHAUtils.popinReportLog(entityManager, "", "SearchClaimPolicyWiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);	
//				//Long claimKey = null;
//				Query claimQuery = entityManager
//						.createNamedQuery("Claim.findByPolicyNumber");
//				claimQuery = claimQuery.setParameter("policyNumber", policyNumber);
//
//				List<Claim> claimByPolicyList = claimQuery.getResultList();
//				
////				List<Preauth> tempPreauthList = claimQuery.getResultList();
//
//				if (claimByPolicyList != null && !claimByPolicyList.isEmpty()) {
//
//					String totalNoOfClaims = String.valueOf(claimByPolicyList.size());
//					
//					System.out.println("Total Records : " + totalNoOfClaims);
//					PolicywiseClaimReportDto policyClaimDto = null;
//					for (Claim claimObj : claimByPolicyList) {
//						entityManager.refresh(claimObj);
//
//						ClaimDto claimDto = getClaimDto(claimObj);
//						
//						policyClaimDto = new PolicywiseClaimReportDto(claimDto);
//						policyClaimDto.setBasepremium(claimObj.getIntimation().getPolicy().getTotalPremium());
//						resultList.add(policyClaimDto);
//					
//					}
//					for(PolicywiseClaimReportDto polClaimObj : resultList){
//					
//						polClaimObj = getCashlessClaimsDetails(polClaimObj,claimByPolicyList);
//						polClaimObj = UpDatePolicyClaimObj(polClaimObj,claimByPolicyList);						
//						polClaimObj.setSno(resultList.indexOf(polClaimObj)+1);
//						polClaimObj.setTotalNoOfClaims(String.valueOf(resultList.size()));
//					}
//					
//					SHAUtils.popinReportLog(entityManager, "", "SearchClaimPolicyWiseReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
//					}
//					
//					
//			} catch (Exception e) {
//				
//				SHAUtils.popinReportLog(entityManager, "", "SearchClaimPolicyWiseReport", new Date(),new Date(),SHAConstants.RPT_ERROR);
//				log.error(e.toString());
//				e.printStackTrace();
//			}
//		}
//		return resultList;
//	}
	
	
	@SuppressWarnings("unchecked")
	private PolicywiseClaimReportDto UpDatePolicyClaimObj(PolicywiseClaimReportDto policyclmDto,List<Claim> clmList){
		try{
			if (clmList != null && !clmList.isEmpty()) {
				ReceiptOfDocumentsDTO rodDTO = null;
				for (Claim clm : clmList) {

					Long claimKey = clm.getKey();

					Query claimQuery = entityManager
							.createNamedQuery("Reimbursement.findByClaimKey");
					claimQuery = claimQuery.setParameter("claimKey", claimKey);

					List<Reimbursement> reimbursementList = claimQuery
							.getResultList();

					if (reimbursementList != null
							&& !reimbursementList.isEmpty()) {
						Reimbursement reimbursement = reimbursementList.get(reimbursementList.size()-1);
						if(reimbursement != null) {

							if (reimbursement
									.getClaim()
									.getIntimation()
									.getIntimationId()
									.equalsIgnoreCase(
											policyclmDto.getIntimationNo())) {
								
								policyclmDto.setClaimedAmount(reimbursement.getClaim().getClaimedAmount());
								ReimbursementMapper reimbMapper = new ReimbursementMapper();
								ReimbursementDto reimbDto = reimbMapper
										.getReimbursementDto(reimbursement);
								ClaimDto clmDto = getClaimDto(reimbursement.getClaim());
								reimbDto.setClaimDto(clmDto);
								policyclmDto
										.setDataOfDischarge(reimbDto
												.getDateOfDischarge() != null ? new SimpleDateFormat(
												"dd/MM/yyyy").format(reimbDto
												.getDateOfDischarge()) : "");

								
								ZonalMedicalReviewMapper preauthMapper = ZonalMedicalReviewMapper.getInstance();
//								ZonalMedicalReviewMapper.getAllMapValues();

								PreauthDTO preauthDto = preauthMapper
										.getReimbursementDTO(reimbursement);
								
								
								policyclmDto = updateDiagnosisDetails(policyclmDto,preauthDto);

								Query rodQuery = entityManager
										.createNamedQuery("DocAcknowledgement.findByClaimKey");
								rodQuery = rodQuery.setParameter("claimkey",
										reimbDto.getClaimDto().getKey());

								List<DocAcknowledgement> docAckList = rodQuery
										.getResultList();
								
								String clmClassification = "";
								
								if (docAckList != null && !docAckList.isEmpty()) {
									Date billreceivedDate = docAckList.get(0)
											.getDocumentReceivedDate();
									String billDateString = new SimpleDateFormat(
											"dd/MM/yyyy")
											.format(billreceivedDate);
									policyclmDto.setrODDate(billDateString);
									
								}
								if(reimbursement != null && reimbursement.getClaim() != null &&  reimbursement.getClaim().getKey() != null){ 
									
									rodDTO = new ReceiptOfDocumentsDTO();
									
									rodDTO = (new CreateRODService()).getBillClassificationFlagDetails(reimbursement.getClaim().getKey(), rodDTO, entityManager);
									
										if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null ){
										
											clmClassification = ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()) ? (("").equals(clmClassification) ? "Hospitalisation" : clmClassification + " , "+ "Hospitalisation"):clmClassification;
											
										}
										
										if(rodDTO.getPreHospitalizationFlag()
												!= null){
											clmClassification = ("Y").equalsIgnoreCase(rodDTO.getPreHospitalizationFlag()) ? (("")
												.equals(clmClassification) ? "Pre-Hospitalisation": clmClassification + " , "
														+ "Pre-Hospitalisation" ):clmClassification;
										}
										
										if(rodDTO.getPostHospitalizationFlag() != null){
										
											clmClassification = ("Y").equalsIgnoreCase(rodDTO.getPostHospitalizationFlag()) ? (("").equals(clmClassification)  ? "Post-Hospitalisation"
													: clmClassification	+ " , "	+ "Post-Hospitalisation") :clmClassification;
										}
								 
									policyclmDto
									.setClaimClassification(clmClassification);
									
								}	
								
								if(reimbDto.getClaimDto().getNewIntimationDto()
										   .getInsuredPatient().getInsuredId() != null){
								Double sumInsured = dbCalculationService
										.getInsuredSumInsured(reimbDto
												.getClaimDto()
												.getNewIntimationDto()
												.getInsuredPatient()
												.getInsuredId().toString(),
												reimbDto.getClaimDto()
														.getNewIntimationDto()
														.getPolicy().getKey(),reimbDto
														.getClaimDto()
														.getNewIntimationDto()
														.getInsuredPatient().getLopFlag());
								

								BalanceSumInsuredDTO paymentAmtDto = dbCalculationService
										.getClaimsOutstandingAmt(reimbDto
												.getClaimDto()
												.getNewIntimationDto()
												.getInsuredPatient().getKey(),
												reimbDto.getClaimDto()
														.getNewIntimationDto()
														.getIntimationId(),
												sumInsured);

								policyclmDto.setOutstandingAmount(String.valueOf(paymentAmtDto
										.getProvisionAmout()));
								policyclmDto.setPaidAmount(String.valueOf(paymentAmtDto
										.getPreviousClaimPaid()));
								
								}
								
								Long claimedAmt = (new PreauthService()).getClaimedAmountForRODByClaimKey(reimbursement.getClaim().getKey(),entityManager);
								policyclmDto.setClaimedAmount(claimedAmt != null ? Double.valueOf(claimedAmt.toString()) : 0d);
																
								if (reimbDto.getTreatmentTypeId() != null) {
									MastersValue managementType = entityManager
											.find(MastersValue.class, reimbDto
													.getTreatmentTypeId());

									policyclmDto
											.setManagementType(managementType != null ? managementType
													.getValue() : "");
								}

								Query reimbquery = entityManager
										.createNamedQuery("ReimbursementQuery.findByReimbursement");
								reimbquery = reimbquery.setParameter(
										"reimbursementKey", reimbDto.getKey());

								List<ReimbursementQuery> reimbQueryList = reimbquery
										.getResultList();

								if (reimbQueryList != null
										&& !reimbQueryList.isEmpty()) {

									Date qDate = reimbQueryList.get(0)
											.getCreatedDate();
									String queryRiseDate = qDate != null ? new SimpleDateFormat(
											"dd/MM/yyyy").format(reimbQueryList
											.get(0).getCreatedDate()) : "";
									policyclmDto
											.setQueryRaisedDate(queryRiseDate);
									policyclmDto.setQueryReason(reimbQueryList.get(0).getQueryRemarks() != null ?reimbQueryList.get(0).getQueryRemarks() : "");

								}

								/*Query reimbqueryRecieved = entityManager
										.createNamedQuery("ReimbursementQuery.findByReimbursementForQueryReceived");
								reimbqueryRecieved = reimbqueryRecieved
										.setParameter("reimbursementKey",
												reimbDto.getKey());

								List<ReimbursementQuery> reimbQueryReceivedList = (List<ReimbursementQuery>)reimbqueryRecieved
										.getResultList();*/

								/*if (reimbQueryReceivedList != null
										&& !reimbQueryReceivedList.isEmpty()) {
									Date qrDate = reimbQueryReceivedList.get(0)
											.getModifiedDate();
									String queryReceivedDate = qrDate != null ? new SimpleDateFormat(
											"dd/MM/yyyy").format(qrDate) : "";

								}*/

								Query reimbRejection = entityManager
										.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
								reimbRejection = reimbRejection.setParameter(
										"reimbursementKey", reimbDto.getKey());

								List<ReimbursementRejection> rejectionList = reimbRejection
										.getResultList();

								if (rejectionList != null
										&& !rejectionList.isEmpty()) {
									policyclmDto
											.setRejectionReason(rejectionList
													.get(0)
													.getRejectionRemarks());
									Date rDate = rejectionList.get(0).getCreatedDate();
									String rejectionDate = rDate != null ? new SimpleDateFormat(
											"dd/MM/yyyy").format(rDate) : "";
									policyclmDto
											.setClaimRejectedDate(rejectionDate);

								}
								
								policyclmDto.setClaimedAmount(Double.valueOf(claimedAmt.doubleValue()));
								policyclmDto.setClaimedAmount(reimbDto
										.getClaimDto().getClaimedAmount());
								
							}

						}

					}

				}
			}
			
		}catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}
		
		
		return policyclmDto;
	}
	
	private PolicywiseClaimReportDto updateDiagnosisDetails(PolicywiseClaimReportDto policyClmDto, PreauthDTO preauthDto){
	
		List<PedValidation> pedDiagList =	(new PreauthService()).findPedValidationByPreauthKey(preauthDto.getKey(),entityManager);
		List<DiagnosisDetailsTableDTO> diagList =( PreMedicalMapper.getInstance()).getNewPedValidationTableListDto(pedDiagList);

		if (diagList != null && !diagList.isEmpty()) {

			String diagnosis = "";
			String icdCode = "";
			String icdChapter = "";
			String icdBlock = "";
			for (DiagnosisDetailsTableDTO diagDto : diagList) {

				Diagnosis diagObj = entityManager.find(
						Diagnosis.class,
						diagDto.getDiagnosisId());

				if (diagObj != null) {
					diagnosis = !("")
							.equalsIgnoreCase(diagnosis) ? diagnosis
							+ " / "
							+ diagObj.getValue()
							: diagObj.getValue();
				}

				IcdChapter icdChapterObj = entityManager
						.find(IcdChapter.class, diagDto
								.getIcdChapter()
								.getId());
				IcdCode icdCodeObj = entityManager
						.find(IcdCode.class, diagDto
								.getIcdCode().getId());
				IcdBlock icdBlkObj = entityManager
						.find(IcdBlock.class, diagDto
								.getIcdBlock().getId());
				if (icdChapterObj != null) {
					icdCode = icdCode.equals("") ? icdChapterObj
							.getValue() : icdChapter
							+ ", "
							+ icdChapterObj.getValue();
				}
				if (icdCodeObj != null) {
					icdCode = icdCode.equals("") ? icdCodeObj
							.getValue() : icdCode
							+ ", "
							+ icdCodeObj.getValue();
				}
				if (icdBlkObj != null) {
					icdBlock = icdBlock.equals("") ? icdBlkObj
							.getValue() : icdBlock
							+ ", "
							+ icdBlkObj.getValue();
				}

			}

			policyClmDto.setiCDCode(icdCode);
			policyClmDto.setiCDDescription(icdChapter);
			if (diagnosis != null) {
				policyClmDto
						.setProvisionalDiagnosis(diagnosis);
			}
		}
		return policyClmDto;
	
	}
	
	@SuppressWarnings({ "unchecked" })
	private PolicywiseClaimReportDto getCashlessClaimsDetails(PolicywiseClaimReportDto policyClmDto, List<Claim> clmList){

		if (clmList != null && !clmList.isEmpty()) {

			for (Claim clmObj : clmList) {

				Query preauthQuery = entityManager
						.createNamedQuery("Preauth.findPreAuthIdInDescendingOrder");
				preauthQuery = preauthQuery.setParameter("intimationKey",
						clmObj.getIntimation().getKey());

				List<Preauth> preauthResultList = (List<Preauth>) preauthQuery
						.getResultList();

				if (preauthResultList != null && !preauthResultList.isEmpty()) {
					Preauth preauthObj = preauthResultList.get(0);
					if(preauthObj != null) {

						if (preauthObj
								.getIntimation()
								.getIntimationId()
								.equalsIgnoreCase(
										policyClmDto.getIntimationNo())) {

							PreauthMapper preauthMapper = PreauthMapper.getInstance();
//							PreauthMapper.getAllMapValues();
//							NewIntimationMapper intimationMapper = new NewIntimationMapper();
//							ClaimMapper clmMapper = new ClaimMapper();

							ClaimDto clmDto = getClaimDto(preauthObj.getClaim());
							PreauthDTO preauthObjDto = preauthMapper
									.getPreauthDTO(preauthObj);
							NewIntimationDto intimationDto = clmDto
									.getNewIntimationDto();
							preauthObjDto.setNewIntimationDTO(intimationDto);
							preauthObjDto.setClaimDTO(clmDto);
							
							Long claimedAmt = (new PreauthService()).getClaimAmountByPreauth(preauthObj.getKey(),entityManager);
							
							policyClmDto.setClaimedAmount(claimedAmt != null ? claimedAmt : 0d);
							
							policyClmDto = updateDiagnosisDetails(policyClmDto, preauthObjDto);
							
							Double sumInsured = dbCalculationService
									.getInsuredSumInsured(preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId()
											.toString(), preauthObjDto
											.getNewIntimationDTO().getPolicy()
											.getKey(),preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getLopFlag());

							BalanceSumInsuredDTO paymentAmtDto = dbCalculationService
									.getClaimsOutstandingAmt(preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getKey(),
											preauthObjDto.getNewIntimationDTO()
													.getIntimationId(),
											sumInsured);

							policyClmDto.setOutstandingAmount(String.valueOf(paymentAmtDto
									.getOutstandingAmout()));
							policyClmDto.setPaidAmount(String.valueOf(paymentAmtDto
									.getPreviousClaimPaid()));

							Query cashlessQuery = entityManager
									.createNamedQuery("PreauthQuery.findBypreauth");
							cashlessQuery = cashlessQuery.setParameter("preAuthPrimaryKey",
									preauthObj.getKey());

							List<PreauthQuery> cashlessQueryList = cashlessQuery
									.getResultList();

							if (cashlessQueryList != null && !cashlessQueryList.isEmpty()) {
								
								Date qDate = cashlessQueryList.get(0).getCreatedDate();
								String queryRiseDate = qDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(qDate) : "";
								
								policyClmDto.setQueryRaisedDate(queryRiseDate);
																
								Date queryReceivedDate = cashlessQueryList.get(0)
										.getModifiedDate();
								String billDateString = queryReceivedDate != null ? new SimpleDateFormat(
										"dd/MM/yyyy").format(queryReceivedDate)
										: "";
								policyClmDto.setrODDate(billDateString);
							}

							policyClmDto.setBasepremium(preauthObjDto
									.getClaimDTO().getNewIntimationDto()
									.getPolicy().getTotalPremium());
							
						}
					}

				}
			}
		}
		return policyClmDto;
		
	}

	public List<ExecutiveStatusDetailReportDto> getExecutiveStatusDetails(
			EmpSearchDto queryFilter, String user) {

		List<ExecutiveStatusDetailReportDto> resultList = new ArrayList<ExecutiveStatusDetailReportDto>();
		
		try{

		if (queryFilter != null) {

			String createdBy = null;
			Date fromDate = null;
			Date toDate = null;

			if (queryFilter.getFromDate() != null){
				fromDate = queryFilter.getFromDate();
			}

			if (queryFilter.getToDate() != null) {
				toDate = queryFilter.getToDate();
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}

			if (queryFilter.getEmpName() != null && queryFilter.getEmpName().getValue() != null) {
				
				String empLoginName = queryFilter.getEmpName().getValue();
				
				String[] empNameList = empLoginName.split("-");
				
				if(empNameList.length >0){
				
					createdBy = StringUtils.trim(empNameList[0].toLowerCase());
				}	
			}

			if (createdBy != null && fromDate != null && toDate != null) {

				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();

				final CriteriaQuery<StageInformation> criteriaClaimStageQuery = builder
						.createQuery(StageInformation.class);
				
				Root<StageInformation> stageRoot =  criteriaClaimStageQuery.from(StageInformation.class);
				
//				Root<StageInformation> stageRoot =  criteriaClaimStageQuery.distinct(true).from(StageInformation.class);				
				
				/*Join<StageInformation,Claim> claimJoin = stageRoot.join(
						"claim", JoinType.INNER);
				
				Join<StageInformation,Intimation> intiamtionJoin = stageRoot.join(
						"intimation", JoinType.INNER);*/
				
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (createdBy != null) {
					Expression nameExp = stageRoot.get("createdBy");
					Expression exp = builder.lower(nameExp);
					Predicate createdByPredicate = builder.like(exp, createdBy.toLowerCase());
					predicates.add(createdByPredicate);
				}

				if (fromDate != null && toDate != null) {

					Expression exp = stageRoot.get("createdDate");
					Predicate fromDatePredicate = builder.greaterThanOrEqualTo(exp,
							fromDate);
					predicates.add(fromDatePredicate);
				
					Predicate toDatePredicate = builder.lessThanOrEqualTo(exp, toDate);
					predicates.add(toDatePredicate);
				}
				
				List<Long> statusList = SHAUtils.getExecutiveSummaryStatusList();
						
				Predicate statusPredicate = stageRoot.get("status").get("key").in(statusList);
				predicates.add(statusPredicate);
				
				criteriaClaimStageQuery.select(stageRoot);
				criteriaClaimStageQuery.where(builder.and(predicates 
						.toArray(new Predicate[] {})));

				final TypedQuery<StageInformation> claimQuery1 = entityManager
						.createQuery(criteriaClaimStageQuery);
				
				SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusDetailReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				List<StageInformation> executiveStatusResultList = (List<StageInformation>) claimQuery1
						.getResultList();
				
				
				if(executiveStatusResultList != null && !executiveStatusResultList.isEmpty()){
					ExecutiveStatusDetailReportDto resultDto = null;
					Reimbursement reimbObj = null;
					Claim claimObject = null;
					Preauth preObj = null;
					Hospitals hospObj = null;
					
					for(StageInformation stage : executiveStatusResultList){
						
						reimbObj = stage.getReimbursement();
						claimObject = stage.getClaim();
						preObj = stage.getPreauth();
						
						if(reimbObj != null){
							entityManager.refresh(reimbObj);
							resultDto = new ExecutiveStatusDetailReportDto(reimbObj);
						}
						
						else{
							
							if(preObj != null){
								entityManager.refresh(preObj);	
								resultDto = new ExecutiveStatusDetailReportDto(preObj);
							}	
							
							if(claimObject != null){
								entityManager.refresh(claimObject);							
						
								if(preObj == null){
									resultDto = new ExecutiveStatusDetailReportDto(claimObject);
								}
								else{
									resultDto = ExecutiveStatusDetailReportDto.updateStatusDto(resultDto, claimObject);
								}
						
							}
						}
						if(stage.getIntimation() != null){
							hospObj = getHospitalById(stage.getIntimation().getHospital());
						}else if(stage.getClaim() != null && stage.getClaim().getIntimation() != null){
							hospObj = getHospitalById(stage.getClaim().getIntimation().getHospital());
						}
						if(hospObj != null){
							resultDto.setHospitalName(hospObj.getName());
							resultDto.setCity(hospObj.getCity());
							resultDto.setHospitalType(hospObj.getHospitalType() != null ? hospObj.getHospitalType().getValue() : "" );
						}								
							
//						ClaimDto clmDto = getClaimDto(claimObject);
//						ExecutiveStatusDetailReportDto resultDto = new ExecutiveStatusDetailReportDto(clmDto);
						
						if(stage.getCreatedBy() != null){
								
							resultDto.setCreatedModifiedBy(getEmployeeNameById(stage.getCreatedBy()));
						}
						
						String screenName = stage.getStage() != null ? stage.getStage().getStageName() : "";
						String statusName = stage.getStatus() != null ? stage.getStatus().getProcessValue() : "";
					    String processDate = stage.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").format(stage.getCreatedDate()) : "";	
						
						resultDto.setScreenStage(screenName);
						resultDto.setTransacOutcome(statusName);
						resultDto.setProcessDateNtime(processDate);
						resultDto.setTransacRemarks(stage.getStatusRemarks());
						resultDto.setSno(executiveStatusResultList.indexOf(stage)+1);						
						resultList.add(resultDto);
						
						reimbObj = null;
						claimObject = null;
						preObj = null;
						resultDto = null;
						hospObj = null;
						
//						if(clmDto.getNewIntimationDto().getInsuredPatient().getInsuredId() != null){		
//							DBCalculationService dbcalService = new DBCalculationService();
//							Double sumInsured = dbcalService.getInsuredSumInsured(String.valueOf(clmDto.getNewIntimationDto().getInsuredPatient().getInsuredId()), clmDto.getNewIntimationDto().getPolicy().getKey());
//									resultDto.setSumInsured(String.valueOf(sumInsured.intValue()));
//							Double balsceSI = dbcalService.getBalanceSI(
//								clmDto.getNewIntimationDto().getPolicy().getKey(),clmDto.getNewIntimationDto().getInsuredPatient().getKey(), clmDto.getKey(), sumInsured,clmDto.getNewIntimationDto().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
//							resultDto.setBalanceSumInsured(balsceSI != null ? String.valueOf(balsceSI.intValue()) : "");
//						}
//						Query fvrQuery = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey");
//						fvrQuery.setParameter("claimKey", clmDto.getKey());
//						List<FieldVisitRequest> fvrReqList = (List<FieldVisitRequest>)fvrQuery.getResultList();
//				
//						if(fvrReqList != null && !fvrReqList.isEmpty()){
//							FieldVisitRequest fvrReq = fvrReqList.get(fvrReqList.size()-1);
//							entityManager.refresh(fvrReq);
//							resultDto.setFieldVisitorName(fvrReq.getRepresentativeName());
//							resultDto.setFieldVisitorType(fvrReq.getAllocationTo() != null ? fvrReq.getAllocationTo().getValue() : "");
//						}
//						resultDto.setIsFVRReportUploaded("N");						
//						resultDto.setSno(executiveStatusResultList.indexOf(stage)+1);						
//						resultList.add(resultDto);
					}
					
//					for(ExecutiveStatusDetailReportDto executiveDetailDto : resultList){
//						
//						Query preauthQuery = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
//						preauthQuery.setParameter("claimkey", executiveDetailDto.getClaimKey());
//						List<Preauth> preauthList = preauthQuery.getResultList();						
//						if(preauthList != null && !preauthList.isEmpty()){
//							Preauth preauthObj = preauthList.get(0);
//							entityManager.refresh(preauthObj);
//							PreauthDTO preDto = (PreauthMapper.getInstance()).getPreauthDTO(preauthObj);
//							executiveDetailDto.setDischargeDate(preDto.getPreauthDataExtractionDetails().getDischargeDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preDto.getPreauthDataExtractionDetails().getDischargeDate()) :"");
//							if(preDto.getPreauthMedicalDecisionDetails().getInitialApprovedAmt() != null || preDto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
//									executiveDetailDto.setIsPreauthGiven("Yes");
//							}							
//						}
//						else{
//							executiveDetailDto.setIsPreauthGiven("Reimb");
//						}
//						Query reimbQuery = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
//						reimbQuery.setParameter("claimKey", executiveDetailDto.getClaimKey());
//						List<Reimbursement> reimbList = reimbQuery.getResultList();
//						if(reimbList != null && !reimbList.isEmpty()){
//							Reimbursement reimbObj = reimbList.get(0);
//							entityManager.refresh(reimbObj);
//							if(reimbObj != null){
//								if(reimbObj.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
//									executiveDetailDto.setIsPreauthGiven("Reimb");
//								}
//								executiveDetailDto.setDischargeDate(reimbObj.getDateOfDischarge() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbObj.getDateOfDischarge()) :"");
//							}
//						}
//					}
								
				}		
				SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusDetailReport", new Date(),new Date(),SHAConstants.RPT_SUCCESS);	
			}
		  }	
		}
		catch(Exception e){
			SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusDetailReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			log.error(e.toString());
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	
	private ClaimDto getClaimDto(Claim claimObj){
		
		//ClaimService clmService = new ClaimService();
		IntimationService intimationService = new IntimationService();
		
		ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
		NewIntimationDto intimationDto = intimationService.getIntimationDto(claimObj.getIntimation(), entityManager);
		
		claimDto.setNewIntimationDto(intimationDto);
		
		return claimDto;		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ExecutiveStatusSummaryReportDto> getExecutiveStatusSummary(
			ExecutiveStatusSummarySearchDto queryFilter,String user) {

		List<ExecutiveStatusSummaryReportDto> resultList = new ArrayList<ExecutiveStatusSummaryReportDto>();
		try{

		if (queryFilter != null) {

			String createdBy = null;
			Date fromDate = null;
			Date toDate = null;
			String empType = null;			

			if (queryFilter.getFromDate() != null){
				fromDate = queryFilter.getFromDate();
			}

			if (queryFilter.getToDate() != null) {
				
				toDate = queryFilter.getToDate();
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();				
				
			}

			if(queryFilter.getEmpType() != null && queryFilter.getEmpType().getValue() != null){
				
				empType = queryFilter.getEmpType().getValue();
			}
			
			if (queryFilter.getEmpName() != null && queryFilter.getEmpName().getValue() != null) {
				
				String empLoginName = queryFilter.getEmpName().getValue();
				
				String[] empNameList = empLoginName.split("-");
				
				if(empNameList.length >0){
				
					createdBy = StringUtils.trim(empNameList[0].toLowerCase());
				}
				
			}
			
			if((SHAConstants.CALLCENTRE_EMPLOYEE).equalsIgnoreCase(empType) && createdBy != null && fromDate != null && toDate != null){
				List<Intimation> callcentersummaryList = new ArrayList<Intimation>(); 
				
				Query intimationQuery = entityManager.createNamedQuery("Intimation.findEmpSummary");
				intimationQuery.setParameter("empSourceKey",ReferenceTable.CALL_CENTRE_SOURCE);
				intimationQuery.setParameter("createdBy", createdBy);
				intimationQuery.setParameter("fromDate", fromDate);
				intimationQuery.setParameter("toDate", toDate);
				
				SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusSummaryReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				callcentersummaryList = intimationQuery.getResultList();
				if(callcentersummaryList != null && !callcentersummaryList.isEmpty()){
				int index = 1;
				ExecutiveStatusSummaryReportDto exeDto;
				Object[] executiveResult;
					for(Iterator intimIter = callcentersummaryList.iterator(); intimIter.hasNext();){
						
						exeDto = new ExecutiveStatusSummaryReportDto();
						executiveResult = (Object[]) intimIter.next();
						
						exeDto.setExecutiveName((String)executiveResult[0]);
						exeDto.setStageCount((Long)executiveResult[1]);						
						
						Query empQuery = entityManager.createNamedQuery("EmpSecUser.getEmpByLoginId");
						empQuery.setParameter("loginId", exeDto.getExecutiveName());
						List<EmpSecUser> empList = empQuery.getResultList();
						if(empList != null && !empList.isEmpty()){
							exeDto.setExecutiveId(empList.get(0).getEmpId());			
							exeDto.setExecutiveName(empList.get(0).getUserName());
						}

								
						exeDto.setStageName(SHAConstants.INTIMATION);
						exeDto.setSno(index++);
						resultList.add(exeDto);						
						exeDto = null;
					}
				}
				SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusSummaryReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			}
			else if (createdBy != null && fromDate != null && toDate != null) {
				
				Query empstausSummaryQ = entityManager.createNamedQuery("StageInformation.findExecutiveSummary");
				
				empstausSummaryQ.setParameter("createdBy", createdBy.toLowerCase());
				empstausSummaryQ.setParameter("fromDate", fromDate);
				empstausSummaryQ.setParameter("toDate", toDate);
				List<Long> statusList = SHAUtils.getExecutiveSummaryStatusList();
				empstausSummaryQ.setParameter("statusList",statusList);
				
				SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusSummaryReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				
				List<StageInformation> executiveStatusSummaryList =  empstausSummaryQ.getResultList();
								
				if(executiveStatusSummaryList != null && !executiveStatusSummaryList.isEmpty()){
								
					int index = 1;
					ExecutiveStatusSummaryReportDto exeDto;
					Object[] executiveResult;
					List<EmpSecUser> empList = null;
					Status status = null;
					Stage stage = null;
					for(Iterator it = executiveStatusSummaryList.iterator(); it.hasNext();){
				
						exeDto = new ExecutiveStatusSummaryReportDto();
						executiveResult = (Object[]) it.next();
						
						exeDto.setStatusId((Long)executiveResult[0]);
						exeDto.setStageCount((Long)executiveResult[1]);						
						exeDto.setExecutiveName(createdBy);
						
						Query empQuery = entityManager.createNamedQuery("EmpSecUser.getEmpByLoginId");
						empQuery.setParameter("loginId", exeDto.getExecutiveName());
						empList = empQuery.getResultList();
						if(empList != null && !empList.isEmpty()){
							exeDto.setExecutiveId(empList.get(0).getEmpId());			
							exeDto.setExecutiveName(empList.get(0).getUserName());
						}
						
						if(exeDto.getStatusId() != null){
							status = entityManager.find(Status.class, exeDto.getStatusId());
							if(status != null){
								
								exeDto.setStatusName(status.getProcessValue() != null ? status.getProcessValue() : "");
								
								stage = status.getStage();
								
								if(stage != null){
								
									exeDto.setStageName(stage.getStageName() != null ? stage.getStageName() : "");
								}
						
//								exeDto.setStageId(status.getStage().getKey());
//								if(exeDto.getStageId() != null){
//									Stage stage = entityManager.find(Stage.class, exeDto.getStageId());
//									if(stage != null){
//										
//										exeDto.setStageName(stage.getStageName() != null ? stage.getStageName() : "");
//									}							
//								}
								
								
							}
							status = null;
							stage = null;
							empList = null;
						}
						
						exeDto.setSno(index++);
						resultList.add(exeDto);
						exeDto = null;
					}	
					SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusSummaryReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}
			}				
		}
		}
		catch(Exception e){
			SHAUtils.popinReportLog(entityManager, user, "ExecutiveStatusSummaryReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			log.error(e.toString());
			e.printStackTrace();
		}

		return resultList;
	}	
	

	
/*	private PayloadBOType getPayloadForCancelROD(PreauthDTO bean,
			DocAcknowledgement docAck) {

		InitiateAckProcessPayloadType payload = new InitiateAckProcessPayloadType();

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(bean.getNewIntimationDTO().getIntimationId());
		
		intimationType.setIntimationSource(bean.getNewIntimationDTO().getIntimationSource().getValue());
		

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(bean.getNewIntimationDTO()
				.getPolicy().getPolicyNumber());
		
		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");
		
		productInfo.setProductName(bean.getNewIntimationDTO().getPolicy().getProductName());

		*//**
		 * @author yosuva.a
		 *//*
		ClaimType claim = new ClaimType();
		claim.setClaimId(docAck.getClaim().getClaimId());
		claim.setKey(docAck.getClaim().getKey());
		if (docAck.getClaim().getClaimType() != null) {
			claim.setClaimType(docAck.getClaim().getClaimType().getValue());
		}

		ClaimRequestType claimRequest = new ClaimRequestType();
		claimRequest.setResult(SHAConstants.CANCEL_ROD_OUTCOME);
		claimRequest.setKey(bean.getClaimKey());
		claimRequest.setCpuCode(bean.getNewIntimationDTO()
				.getCpuCode());
		claimRequest.setClaimRequestType("All");
		claimRequest.setClientType("ACK");

		DocReceiptACKType docReceiptAck = new DocReceiptACKType();
		docReceiptAck.setAckNumber(docAck.getAcknowledgeNumber());
		docReceiptAck.setKey(docAck.getKey());
		docReceiptAck.setStatus(docAck.getStatus().getProcessValue());

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
		
		HospitalInfoType hospInfoType = new HospitalInfoType();
		hospInfoType.setHospitalType(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getValue());
		hospInfoType.setNetworkHospitalType(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
		
		Claim claimObj = docAck.getClaim();
		
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
		rodType.setKey(bean.getKey());
		
		payloadBO.setRod(rodType);
		
		return payloadBO;
	}*/
	
	public TmpCPUCode getTmpCpuCode(Long cpuKey) {
		if (!ValidatorUtils.isNull(cpuKey))
			return entityManager.find(TmpCPUCode.class, cpuKey);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<MedicalAuditClaimStatusReportDto> getMedicalAuditClaimStatusDetails(Map<String,Object> searchFilter){
		List<MedicalAuditClaimStatusReportDto> resultListDto = new ArrayList<MedicalAuditClaimStatusReportDto>();
		if(searchFilter != null && !searchFilter.isEmpty()){
			try{
				String status = null;
				Date fromDate = null;
				Date toDate = null;

				if (searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null){
					fromDate = (Date)searchFilter.get("fromDate");
				}

				if (searchFilter.containsKey("toDate") && searchFilter.get("toDate") != null) {
					toDate = (Date)searchFilter.get("toDate");
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
				}

				if (searchFilter.containsKey("status") && searchFilter.get("status") != null) {
					status = StringUtils.trim(searchFilter.get("status").toString());
				}

				if (status != null || (fromDate != null && toDate != null)) {

					final CriteriaBuilder builder = entityManager
							.getCriteriaBuilder();
					
					final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
							.createQuery(Reimbursement.class);
					
					Root<Reimbursement> reimbRoot =  criteriaReimbQuery.from(Reimbursement.class);
					
					/*Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
							"claim", JoinType.INNER);*/
					
					List<Predicate> predicates = new ArrayList<Predicate>();
					
					Predicate claimTypePred = builder.equal(reimbRoot.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
					predicates.add(claimTypePred);
					
					
					
					//					SHAConstants.SETTLED_STATUS
									
					Predicate claimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
					Predicate claimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_APPROVE_STATUS);
					Predicate approvedPred = builder.and(claimStagePred,claimStatusPred);
					
				if(status.equalsIgnoreCase(SHAConstants.SETTLED_STATUS)){
					predicates.add(approvedPred);
				}
				
				
				//				SHAConstants.REJECTED_STATUS
									
					Predicate medicalRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
					Predicate medicalRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
					
					Predicate medicalRejectPred = builder.and(medicalRejectStagePred,medicalRejectstatusPred);

					
					Predicate finanRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
					Predicate finanRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
					
					Predicate financialRejectPred = builder.and(finanRejectStagePred,finanRejectstatusPred);
					
					
					Predicate claimRejectPred = builder.or(medicalRejectPred,financialRejectPred);
					
				if(status.equalsIgnoreCase(SHAConstants.REJECTED_STATUS)){
					predicates.add(claimRejectPred);
				}
				
				
				//				SHAConstants.CLOSED_STATUS
									
					Predicate rodStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
					Predicate rodStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
					
					Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
				
					Predicate billEntryStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
					Predicate billEntryStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_CLOSED);
				
					Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
					
					Predicate zonalReviewStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
					Predicate zonalReviewStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
					
					Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
					
					Predicate processClaimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
					Predicate processClaimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
					
					Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
					
					Predicate claimBillingStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
					Predicate claimBillingStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED);

					Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
										
					Predicate finStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
					Predicate finStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);

					Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
					
					Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
				
				if(status.equalsIgnoreCase(SHAConstants.CLOSED_STATUS)){
					predicates.add(claimClosedPred);
					
				}
				
				//				SHAConstants.PENDING_STATUS
									
//					Predicate finStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
//					
//					List<Long> claimFinstatusKeyList = new ArrayList<Long>();
//					claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
//					
//					Predicate finStagusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(claimFinstatusKeyList);
//					
//					Predicate finClaimPedingPred = builder.and(finStagePred,finStagusPred);
//					
//					
//					Predicate medicalStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY); 
//					List<Long> medicalStatusKeyList = new ArrayList<Long>();
//					
//					medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
//					
//					Predicate medicalStatusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(medicalStatusKeyList);
//					
//					Predicate medicalPedndingPred = builder.and(medicalStagePred,medicalStatusPred);
//					
//					List<Long> pendingStageKeyList = new ArrayList<Long>();
//					
//					pendingStageKeyList.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//					Predicate otherPendingStagePred = reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key").in(pendingStageKeyList);
//					
//					Predicate claimPendingPred = builder.or(finClaimPedingPred,medicalPedndingPred,otherPendingStagePred);
				
				if(status.equalsIgnoreCase(SHAConstants.PENDING_STATUS)){
					//	predicates.add(claimPendingPred);
					
					Predicate notClosedPred = builder.not(claimClosedPred);
					Predicate notRejectedPred = builder.not(claimRejectPred);
					Predicate notSettledPred = builder.not(approvedPred);
					
					Predicate pedingPred = builder.and(notClosedPred,notRejectedPred,notSettledPred);
					predicates.add(pedingPred);					
					
				}
				
				if(fromDate != null && toDate != null){
				Expression<Date> fromDateExpression = reimbRoot.<Date> get("modifiedDate");
				Predicate fromDatePredicate = builder
						.greaterThanOrEqualTo(fromDateExpression,
								fromDate);
				predicates.add(fromDatePredicate);

				Expression<Date> toDateExpression = reimbRoot.<Date> get("modifiedDate");
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();					
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(toDateExpression, toDate);
				predicates.add(toDatePredicate);
				}
				
//				Order descedingReimbOrder = builder.desc(reimbRoot.<Long>get("key"));
				
				
				criteriaReimbQuery.select(reimbRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));             //.orderBy(descedingReimbOrder)

				final TypedQuery<Reimbursement> reimbusementQuery = entityManager
						.createQuery(criteriaReimbQuery);

				SHAUtils.popinReportLog(entityManager, "", "MedicalAuditClaimStatusReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				
				List<Reimbursement> resultreimbList = reimbusementQuery.getResultList();
				

				if (resultreimbList != null && !resultreimbList.isEmpty()) {					
					Claim claimObj;
					Hospitals hospObj;
					ReimbursementQuery reimbQuery;
					MedicalAuditClaimStatusReportDto auditClaimDto;
					for (Reimbursement reimbursementObj : resultreimbList) {
						entityManager.refresh(reimbursementObj);
						claimObj = reimbursementObj.getClaim();
						
//						ClaimDto claimDto = getClaimDto(claimObj);
//						MedicalAuditClaimStatusReportDto auditClaimDto = new MedicalAuditClaimStatusReportDto(claimDto);
						
						hospObj = getHospitalById(claimObj.getIntimation().getHospital());
						String hospCode = hospObj.getHospitalCode() != null ? hospObj.getHospitalCode() : "";
						
						auditClaimDto = new MedicalAuditClaimStatusReportDto(claimObj,hospCode);
						
//						List<Reimbursement> reimbList = getReimbursementByClaimKey(claimObj.getKey());
						entityManager.refresh(reimbursementObj);
						auditClaimDto.setGeneralRemarks(reimbursementObj.getApprovalRemarks() != null ? reimbursementObj.getApprovalRemarks() : "");
						auditClaimDto.setDoctorNote(reimbursementObj.getDoctorNote() != null ? reimbursementObj.getDoctorNote() : "");
						auditClaimDto.setFinalRemarks(reimbursementObj.getFinancialApprovalRemarks() != null ? reimbursementObj.getFinancialApprovalRemarks() : "");
							
						reimbQuery =  getReimbursementQueryByReimbursmentKey(reimbursementObj.getKey());
							
						if(reimbQuery != null){
							String queryRemarks = reimbQuery.getQueryRemarks() != null ? reimbQuery.getQueryRemarks() : "";
							auditClaimDto.setQueryRaisedOrMedRejReq(reimbursementObj.getRejectionRemarks() != null ? queryRemarks + " / " + reimbursementObj.getRejectionRemarks() : queryRemarks);
						}							
							
						String diagnosis = getDiganosisByTransacKey(reimbursementObj.getKey());							
						auditClaimDto.setDiagnosis(diagnosis);
						auditClaimDto.setReBillingOrReQuery("");
						resultListDto.add(auditClaimDto);	
						auditClaimDto = null;
					}
					SHAUtils.popinReportLog(entityManager, "", "MedicalAuditClaimStatusReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}
				
				}	
				
			}
			catch(Exception e){
				log.error(e.toString());
				e.printStackTrace();
			}
						
		}
		
		return resultListDto;
	}

	public String getDiganosisByTransacKey(Long reimbKey) {
		StringBuffer diagnosis = new StringBuffer("");
		Query diagQuery = entityManager
					.createNamedQuery("PedValidation.findByTransactionKey");
		diagQuery.setParameter("transactionKey",reimbKey);
		List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery.getResultList();
		
			if (diagnosisList != null
					&& !diagnosisList.isEmpty()) {
				for (PedValidation pedDiagnosis : diagnosisList) {

					if (pedDiagnosis != null) {
						if (pedDiagnosis.getDiagnosisId() != null) {
							Diagnosis diag = entityManager
									.find(Diagnosis.class,
											pedDiagnosis
													.getDiagnosisId());
							if (diag != null) {
								diagnosis = !("")
										.equalsIgnoreCase(diagnosis.toString()) ? diagnosis.append(" , "+diag.getValue())
										: diagnosis.append(diag.getValue());
							}
						}
					}
				}
			}
		return diagnosis.toString();
	}
	

	@SuppressWarnings("unchecked")
	public Reimbursement getHospitalizationRod(Long claimKey,Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.getRodAscendingOrder");
		query.setParameter("claimkey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement2 : reimbursementList) {
			
			if(! reimbursement2.getKey().equals(reimbursementKey)){
				
				if(reimbursement2.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement2.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")
						|| reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag() != null
						&& reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
					return reimbursement2;
				}
			}
			
		}
		
		return null;
	}
	
	public Reimbursement getHospitalizationRodForMA(Long claimKey,Long reimbursementKey,String rodNumber) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement2 : reimbursementList) {
			
			if((! reimbursement2.getKey().equals(reimbursementKey) && (! reimbursement2.getRodNumber().equals(rodNumber)))){
				
				if(reimbursement2.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement2.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")
						|| reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag() != null
						&& reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
					return reimbursement2;
				}
			}
			
		}
		
		return null;
	}
	
	public Boolean isPrePostReimbursement(Long reimbursmentKey){
		
		Boolean isPrePost = true;
		
		Reimbursement reimbursement = getReimbursementByKey(reimbursmentKey);
		
		if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")
				|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y") ){
			isPrePost = false;
		}
		
		return isPrePost;
		
	}
	
	public Reimbursement getHospitalizationROD(Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				/**
				 * The below if condition has been changed, to cater lumpsum rod need.
				 * In case of lumpsum product, lumpsum can be first rod. Hence if condition
				 * has been modified such that either hospitalization or lumpsum should exist for
				 * claim.
				 * */
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
						|| null != reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag() && ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag()))) {
					if(!(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED))) {
						previousROD = reimbursement;
						break;
					}
				}
			}
		}
		/*if(previousROD != null) {
			if(previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)) {
				return null;
			}
		}*/
		return previousROD;
	}
	
	public Reimbursement getPartialHospitalizationROD(Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
					previousROD = reimbursement;
					break;
				}
			}
		}
		if(previousROD != null) {
			if(previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| previousROD.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)) {
				return null;
			}
		}
		return previousROD;
	}
	
	public Reimbursement getHospitalizationOrPartialROD(Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
						|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y"))) {
					if(!reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) && !reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
							&& !reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)) {
						previousROD = reimbursement;
						break;
					}
					
				}
			}
		}
		return previousROD;
	}
	
	
	public Reimbursement getHospitalizationOrPartialRODForBalanceSI(Long claimKey, String rodNumber) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(!reimbursement.getRodNumber().equalsIgnoreCase(rodNumber) && reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS) && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
						|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y"))) {
					previousROD = reimbursement;
					break;
				}else if(!reimbursement.getRodNumber().equalsIgnoreCase(rodNumber) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y") && isSettledPaymentAvailable(reimbursement.getRodNumber())){
					previousROD = reimbursement;
					break;
				}
			}
		}
		if(previousROD != null) {
			if(previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || previousROD.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| previousROD.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)) {
				return null;
			}
		}
		return previousROD;
	}
		
//		Reimbursement reimbursment = getReimbursementByKey(reimbursementKey);

	public Reimbursement getFilteredPreviousLatestROD(Long claimKey,
			Long currentReimbursementKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && !ReferenceTable.REJECT_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					if((currentReimbursementKey == null || !reimbursement.getKey().equals(currentReimbursementKey)) && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y") ||
							(reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("y")) || (reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("y")))) {
						previousROD = reimbursement;
						break;
					}
					
				}
			}
		}

		return previousROD;
	}
	
	
	public Boolean isAnyRodActive(Reimbursement reimbursement){
		
		Long claimKey = reimbursement.getClaim().getKey();
		
		List<Reimbursement> reimbursementByClaimKey = getReimbursementByClaimKey(claimKey);
		
		for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
			if(!reimbursement2.getKey().equals(reimbursement.getKey())){
				if(! reimbursement2.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && !ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey())
						&& ! reimbursement2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
						&& ! reimbursement2.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
						&& ! reimbursement2.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)){
				    return false;
				}
					
			}
		}
		
		
		return true;
	}
	
	
	public Boolean isItHavingAnyROD(Long claimKey){
		Boolean isHavingROD = false;
		List<Reimbursement> reimbursementByClaimKey = getRembursementDetails(claimKey);
		if(reimbursementByClaimKey != null && !reimbursementByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : reimbursementByClaimKey) {
				if(!ReferenceTable.getCancelRODKeys().containsKey(reimbursement.getStatus().getKey()) && !reimbursement.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)) {
					isHavingROD = true;
					break;
				}
			}
		}
		return isHavingROD;
	}
	
	
	public Boolean isItHavingAnyROD(Long claimKey, Long rodKey){
		Boolean isItHavingAnyROD = false;
		List<Reimbursement> reimbursementByClaimKey = getRembursementDetails(claimKey);
		if(reimbursementByClaimKey != null && !reimbursementByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : reimbursementByClaimKey) {
				if(!reimbursement.getKey().equals(rodKey)) {
					if(!ReferenceTable.getCancelRODKeys().containsKey(reimbursement.getStatus().getKey()) && !reimbursement.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)) {
						isItHavingAnyROD = true;
						break;
					}
				}
			}
		}
		return isItHavingAnyROD;
	}
	
	public Double getClaimPaymentAmountByRODNumber(String rodNumber) {
		Query query = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNo").setParameter("rodNumber",
						rodNumber);
		Double amount = 0d;
		List<ClaimPayment> paymentList = query.getResultList();
		if(paymentList != null && !paymentList.isEmpty()) {
			for (ClaimPayment claimPayment : paymentList) {
				entityManager.refresh(claimPayment);
				if(claimPayment.getStatusId() != null && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)) {
					amount += claimPayment.getTotalApprovedAmount() != null ? claimPayment.getTotalApprovedAmount() : 0d;
				}
				
			}
		}

		return amount;
	}
	
	
	public Boolean isClaimPaymentAvailable(String rodNumber) {
		Query query = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNo").setParameter("rodNumber",
						rodNumber);
		//Double amount = 0d;
		List<ClaimPayment> paymentList = query.getResultList();
		if(paymentList != null && !paymentList.isEmpty()) {
			return true;
		}

		return false;
	}
	
	public Boolean isPaymentAvailable(String rodNumber) {
		Query query = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNoOrderByKey").setParameter("rodNumber",
						rodNumber);
		Boolean isAvailable = false;
		@SuppressWarnings("unchecked")
		List<ClaimPayment> paymentList = query.getResultList();
		if(paymentList != null && !paymentList.isEmpty()) {
			ClaimPayment claimPayment = paymentList.get(0);
			if(claimPayment != null && claimPayment.getStatusId() != null && ReferenceTable.getPaymentStatusExceptCancel().containsKey(claimPayment.getStatusId().getKey())) {
				isAvailable = true;
			}
		}
		return isAvailable;
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

	public DocAcknowledgement getPreviousAcknowledgmentDetails(Long claimKey) {
		DocAcknowledgement previousROD = null;
		List<DocAcknowledgement> previousAckList = getDocAckListByClaim(claimKey);
		if (previousAckList != null && !previousAckList.isEmpty()) {
			for (DocAcknowledgement docAcknowledgement : previousAckList) {
				if(docAcknowledgement.getStatus() != null && ! docAcknowledgement.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) && (docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
						|| docAcknowledgement.getPartialHospitalisationFlag() != null && docAcknowledgement.getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y"))) {
					previousROD = docAcknowledgement;
					break;
				}
			}
		}
		return previousROD;
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
	private List<DocumentDetails> getDocumentDetailsByRodNumber(String rodNumber)
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
	
	
	private DocAcknowledgement getDocumentDetailsByRodKey(long rodKey)
	{
		DocAcknowledgement docAck = null;
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findByReimbursement");
		query = query.setParameter("rodKey", rodKey);		
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			docAck = docAckList.get(0);
			entityManager.refresh(docAckList.get(0));
			return docAck;
			
		}
		return docAck;
		
	}
	
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
	private ClaimPayment getSettledClaimPaymentByRODNo(String rodNo)
	{
		
		ClaimPayment claimPayment = null;
		Query query = entityManager.createNamedQuery("ClaimPayment.findSettledClaimByRodNo");
		query = query.setParameter("rodNumber", rodNo);
		//query = query.setParameter("statusId", ReferenceTable.PAYMENT_SETTLED);
		List<ClaimPayment> claimPaymentList = query.getResultList();
		if(null != claimPaymentList && !claimPaymentList.isEmpty())
		{
			claimPayment = claimPaymentList.get(0);
			entityManager.refresh(claimPaymentList.get(0));
			return claimPayment;
			
		}
		return claimPayment;
		
	}
	
	public MultipleUploadDocumentDTO updateDocumentDetails(MultipleUploadDocumentDTO bean){
		
		ReferenceDocument referenceDocument = new ReferenceDocument();
		referenceDocument.setFileName(SHAUtils.getOnlyStrings(bean.getFileName()));
		
		if(SHAConstants.UPLOAD_INVESTGATION_SCREEN.equalsIgnoreCase(bean.getTransactionName())){
			MastersValue fileType = new MastersValue();
			fileType.setKey(ReferenceTable.CLAIM_VERIFICATION_REPORT_DOCUMENT_TYPE_KEY);
			referenceDocument.setFileType(fileType);
		}
		if(ReferenceTable.UPLOAD_BED_PHOTO.equalsIgnoreCase(bean.getTransactionName())){
			MastersValue fileType = new MastersValue();
			fileType.setKey(ReferenceTable.BED_PHOTO);
			referenceDocument.setFileType(fileType);
		}
		referenceDocument.setDocumentToken(bean.getFileToken());
		referenceDocument.setTransactionKey(bean.getTransactionKey());
		referenceDocument.setTransactionType(bean.getTransactionName());
		referenceDocument.setDeletedFlag("N");
		referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		referenceDocument.setCreatedBy(bean.getUsername());
		entityManager.persist(referenceDocument);
		entityManager.flush();
		
		return bean;
	}
	
	public MultipleUploadDocumentDTO updateDocumentDetailsInDocTable(MultipleUploadDocumentDTO bean, SearchProcessTranslationTableDTO argObj){

		Query findByClaimKey = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", argObj.getRodKey());
		Reimbursement reimbursement = (Reimbursement) findByClaimKey.getSingleResult();
		
		DocumentDetails  docRec = new DocumentDetails();
		docRec.setIntimationNumber(argObj.getRrcDTO().getIntimationNo());
		docRec.setClaimNumber(argObj.getRrcDTO().getClaimDto().getClaimId());
		docRec.setReimbursementNumber(reimbursement.getRodNumber());
		docRec.setDocumentType("Coordinator");
		docRec.setFileName(bean.getFileName());
		docRec.setDocumentToken(Long.parseLong(bean.getFileToken()));
		docRec.setDocumentSource("Coordinator");
		docRec.setCreatedBy(bean.getUsername());
		docRec.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(docRec);
		entityManager.flush();
		
		return bean;
	}
	
	public MultipleUploadDocumentDTO updateInvDocDetails(MultipleUploadDocumentDTO bean){
		
		try{
			if(bean.getKey() == null){
				ReferenceDocument referenceDocument = new ReferenceDocument();
				referenceDocument.setFileName(SHAUtils.getOnlyStrings(bean.getFileName()));
				
				if(SHAConstants.UPLOAD_INVESTGATION_SCREEN.equalsIgnoreCase(bean.getTransactionName())){
					MastersValue fileType = new MastersValue();
					fileType.setKey(ReferenceTable.CLAIM_VERIFICATION_REPORT_DOCUMENT_TYPE_KEY);
					referenceDocument.setFileType(fileType);
				}
				referenceDocument.setDocumentToken(bean.getFileToken());
				referenceDocument.setTransactionKey(bean.getTransactionKey());
				referenceDocument.setTransactionType(bean.getTransactionName());
				referenceDocument.setDeletedFlag("N");
				referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				referenceDocument.setCreatedBy(bean.getUsername());
				entityManager.persist(referenceDocument);
				entityManager.flush();
				
				bean.setKey(referenceDocument.getKey());
				AssignedInvestigatiorDetails assignObj = getAssignedInvestigByKey(bean.getTransactionKey());
				
				if(assignObj != null && assignObj.getInvestigation() != null){
					updateClaimDocumentDetails(bean,assignObj.getInvestigation());
				}	
			}
			else if(bean.getKey() != null && ("Y").equalsIgnoreCase(bean.getDeletedFlag())){
				ReferenceDocument referenceDocument = getReferenceDocByKey(bean.getKey());
				if(referenceDocument != null){
					referenceDocument.setDeletedFlag("Y");
					referenceDocument.setModifiedBy(bean.getUsername());
					referenceDocument.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(referenceDocument);
					entityManager.flush();	
					if(bean.getFileToken() != null){
						DocumentDetails dmsDocument = getDocDetailByToken(Long.valueOf(bean.getFileToken()));
						if(dmsDocument != null){
							dmsDocument.setDeletedFlag("Y");
							entityManager.merge(dmsDocument);
							entityManager.flush();
						}
					}	
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return bean;
	}
	

	public MultipleUploadDocumentDTO updatePCCDocDetails(MultipleUploadDocumentDTO bean){
	try{
		
		if(bean.getKey() == null){
			ReferenceDocument referenceDocument = new ReferenceDocument();
			referenceDocument.setFileName(SHAUtils.getOnlyStrings(bean.getFileName()));
			//referenceDocument.setFileType(bean.getPccFileType());
			referenceDocument.setDocumentToken(bean.getFileToken());
			referenceDocument.setTransactionKey(bean.getTransactionKey());
			referenceDocument.setTransactionType(bean.getTransactionName());
			referenceDocument.setDeletedFlag("N");
			referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			referenceDocument.setCreatedBy(bean.getUsername());
			referenceDocument.setFileUploadRemarks(bean.getFileUploadRemarks());
			entityManager.persist(referenceDocument);
			entityManager.flush();
			
			bean.setKey(referenceDocument.getKey());
			PCCRequest pccRequestObj = getPCCRequestByKey(bean.getTransactionKey());
			String intimationNumber = pccRequestObj.getIntimationNo();
			if(pccRequestObj != null && pccRequestObj.getIntimation()!= null){
				updatePCCDocumentDetails(bean,pccRequestObj.getIntimation());
			}	
		}
		else if(bean.getKey() != null && ("Y").equalsIgnoreCase(bean.getDeletedFlag())){
			
			ReferenceDocument referenceDocument = getReferenceDocByKey(bean.getKey());
			if(referenceDocument != null){
				referenceDocument.setDeletedFlag("Y");
				referenceDocument.setModifiedBy(bean.getUsername());
				referenceDocument.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(referenceDocument);
				entityManager.flush();	
				if(bean.getFileToken() != null){
					DocumentDetails dmsDocument = getDocDetailByToken(Long.valueOf(bean.getFileToken()));
					if(dmsDocument != null){
						dmsDocument.setDeletedFlag("Y");
						entityManager.merge(dmsDocument);
						entityManager.flush();
					}
				}	
			}
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	return bean;
}
	
	private DocumentDetails getDocDetailByToken(Long token){
		Query query = entityManager
				.createNamedQuery("DocumentDetails.findByDocToken");

		query = query.setParameter("documentToken", token);
		List<DocumentDetails> docList = query.getResultList();
		
		if(docList != null && !docList.isEmpty()){
			return docList.get(0); 
		}
		return null;
	}
	
	public ReferenceDocument getReferenceDocByKey(Long primaryKey){
		
		List<ReferenceDocument> resultList = new ArrayList<ReferenceDocument>();
		if(primaryKey != null){
			try{
				Query query = entityManager.createNamedQuery("ReferenceDocument.findByKey");
				query.setParameter("docKey", primaryKey);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(resultList != null && !resultList.isEmpty()){
				
				return resultList.get(0);			
			}
		}
		return null;
	}
	
	public AssignedInvestigatiorDetails getAssignedInvestigByKey(Long assignedKey)
	{
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		if(assignedKey != null){
			try{
				Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByAssignInvestigaitonKey");
				query.setParameter("key", assignedKey);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(resultList != null && !resultList.isEmpty()){
		
			return resultList.get(0);
		}
		return null;
		
	}
	
	
	public PCCRequest getPCCDetailsByInitmationNo(String intimationNumber)
	{
		List<PCCRequest> resultList = new ArrayList<PCCRequest>();
		if(intimationNumber != null){
			try{
				Query query = entityManager.createNamedQuery("PCCRequest.findByintimationNo");
				query.setParameter("key", intimationNumber);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(resultList != null && !resultList.isEmpty()){
		
			return resultList.get(0);
		}
		return null;
		
	}
	
	public PCCRequest getPCCRequestByKey(Long pccKey) {
		Query findByKey = entityManager.createNamedQuery("PCCRequest.findByKey");
		findByKey = findByKey.setParameter("key", pccKey);
		List<PCCRequest> pccList = findByKey.getResultList();
		if(null != pccList && !pccList.isEmpty()){
			entityManager.refresh(pccList.get(0));
			return pccList.get(0);
		}
		return null;
	}
	
	public void updateClaimDocumentDetails(MultipleUploadDocumentDTO bean,Investigation investigation){
		
		try{
		
		DocumentDetails referenceDocument = new DocumentDetails();
		referenceDocument.setFileName(bean.getFileName());
		referenceDocument.setDocumentToken(Long.valueOf(bean.getFileToken()));
		referenceDocument.setDocumentSource(SHAConstants.ROD_DATA_SOURCE);
		referenceDocument.setDocumentType(SHAConstants.CLAIM_VERIFICATION_REPORT);
		referenceDocument.setIntimationNumber(investigation.getClaim().getIntimation().getIntimationId());
		referenceDocument.setClaimNumber(investigation.getClaim().getClaimId());
		referenceDocument.setCreatedBy(bean.getUsername());
		Reimbursement reimbursementByKey = getReimbursementByKey(bean.getTransactionKey());
		
		if(reimbursementByKey != null){
			referenceDocument.setReimbursementNumber(reimbursementByKey.getRodNumber());
		}
		referenceDocument.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
		
		referenceDocument.setDeletedFlag("N");
		referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(referenceDocument);
		entityManager.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
public void updatePCCDocumentDetails(MultipleUploadDocumentDTO bean,Intimation intimation){
		
		try{
		DocumentDetails referenceDocument = new DocumentDetails();
		referenceDocument.setFileName(bean.getFileName());
		referenceDocument.setDocumentToken(Long.valueOf(bean.getFileToken()));
		referenceDocument.setDocumentSource(SHAConstants.PCC_DATA_SOURCE);
		referenceDocument.setDocumentType(SHAConstants.POST_CASHLESS_CELL);
		referenceDocument.setIntimationNumber(intimation.getIntimationId());
		referenceDocument.setRemarks(bean.getFileUploadRemarks());
		Claim claim = getClaimsByIntimationNumber(intimation.getIntimationId());
		if(claim !=null){
		referenceDocument.setClaimNumber(claim.getClaimId());
		}
		referenceDocument.setCreatedBy(bean.getUsername());
		Reimbursement reimbursementByClaimKey = getPCCByClaimKey(claim.getKey());
		
		if(reimbursementByClaimKey != null){
			referenceDocument.setReimbursementNumber(reimbursementByClaimKey.getRodNumber());
		}
		referenceDocument.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
		
		referenceDocument.setDeletedFlag("N");
		referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(referenceDocument);
		entityManager.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
public Reimbursement getPCCByClaimKey(Long claimKey)
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
	
}
		
	public List<MultipleUploadDocumentDTO> getUpdateDocumentDetails(Long key){
		
		Query query = entityManager.createNamedQuery("ReferenceDocument.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
		List<MultipleUploadDocumentDTO> documentList = new ArrayList<MultipleUploadDocumentDTO>();
		
		List<ReferenceDocument> resultList = (List<ReferenceDocument>) query.getResultList();
		int sNo = 1;
		MultipleUploadDocumentDTO dto = null;
		for (ReferenceDocument referenceDocument : resultList) {
			dto = new MultipleUploadDocumentDTO();
			dto.setTransactionKey(referenceDocument.getTransactionKey());
			dto.setFileName(referenceDocument.getFileName());
			dto.setFileToken(referenceDocument.getDocumentToken());
			dto.setsNo(sNo);
			dto.setUploadedDate(referenceDocument.getCreatedDate());
			dto.setUploadLetterDate(referenceDocument.getLetterDate());
			dto.setUsername(referenceDocument.getCreatedBy());
			dto.setNoOfPages(referenceDocument.getNoOfPages());
			dto.setDocKey(referenceDocument.getKey());
			documentList.add(dto);
			sNo++;
		}
		
		return documentList;
	}
	
	
	private void saveBillSummaryRemarksValues(List<BillEntryDetailsDTO> hospitalizationTabList , List<PreHospitalizationDTO> prePostHospList,String presenterString)
	{
		//hospitalizationTabList = null;
		 
		if(null != hospitalizationTabList && !hospitalizationTabList.isEmpty())
		{
			for (BillEntryDetailsDTO billEntryDetailsDTO : hospitalizationTabList) {

				if(   
						(null != billEntryDetailsDTO.getDeductibleNonPayableReasonBilling() && !("").equalsIgnoreCase(billEntryDetailsDTO.getDeductibleNonPayableReasonBilling())) ||
						(null != billEntryDetailsDTO.getDeductibleNonPayableReasonFA() && !("").equalsIgnoreCase(billEntryDetailsDTO.getDeductibleNonPayableReasonFA()))
						)
				{
					List<ViewBillRemarks> viewBillRemarksObj = getViewBillRemarksForROD(billEntryDetailsDTO.getReimbursementKey(), billEntryDetailsDTO.getBillClassificationTypeId(), billEntryDetailsDTO.getBillTypeNumber());
					if(null != viewBillRemarksObj && !viewBillRemarksObj.isEmpty())
					{
						for (ViewBillRemarks viewBillRemarks : viewBillRemarksObj) {
							getViewBillRemarks(viewBillRemarks, billEntryDetailsDTO, presenterString);
						}
					}
					else
					{
						ViewBillRemarks viewBillRemarks = null;
						getViewBillRemarks(viewBillRemarks, billEntryDetailsDTO, presenterString);
					}
					//ViewBillRemarks viewBillRemarks = getViewBillRemarks(billEntryDetailsDTO,presenterString);
					//if(null != )

				}
				//viewBillRemarks.
			}
			//viewBillRemarks
		}
		if(null != prePostHospList)
		{
			if(null != prePostHospList && !prePostHospList.isEmpty())
			{
				for (PreHospitalizationDTO preHospitalizationDTO : prePostHospList) {
					if(   
							(null != preHospitalizationDTO.getDeductibleNonPayableReasonBilling() && !("").equalsIgnoreCase(preHospitalizationDTO.getDeductibleNonPayableReasonBilling())) ||
							(null != preHospitalizationDTO.getDeductibleNonPayableReasonFA() && !("").equalsIgnoreCase(preHospitalizationDTO.getDeductibleNonPayableReasonFA()))
							)
					{
						List<ViewBillRemarks> viewBillRemarksObj = getViewBillRemarksForROD(preHospitalizationDTO.getReimbursementKey(), preHospitalizationDTO.getBillClassificationTypeId(), preHospitalizationDTO.getBillTypeNumber());
						if(null != viewBillRemarksObj && !viewBillRemarksObj.isEmpty())
						{
							for (ViewBillRemarks viewBillRemarks : viewBillRemarksObj) {
								getViewBillRemarks(viewBillRemarks, preHospitalizationDTO, presenterString);
							}
						}
						else
						{
							ViewBillRemarks viewBillRemarks = null;
							getViewBillRemarks(viewBillRemarks, preHospitalizationDTO, presenterString);
						}
						/*ViewBillRemarks viewBillRemarks = getViewBillRemarks(preHospitalizationDTO,presenterString);
						entityManager.persist(viewBillRemarks);
						entityManager.flush();*/
					}

				}
			}
		}
	
	}
	
	
	private  ViewBillRemarks getViewBillRemarks(ViewBillRemarks viewBillRemarks,ViewBillSummaryRemarksDTO billEntryDetailsDTO,String presenterString)
	{
		try
		{
			if(null == viewBillRemarks)
			{
			  viewBillRemarks = new ViewBillRemarks();
			}

			Reimbursement reimbursement = getReimbursementByKey(billEntryDetailsDTO.getReimbursementKey());
			
			if(null != reimbursement)
				viewBillRemarks.setReimbursementKey(reimbursement);
			
			
			if(null != billEntryDetailsDTO.getBillClassificationTypeId())
			{
				MasBillClassification billClassification = getMasBillClassificationBasedOnKey(billEntryDetailsDTO.getBillClassificationTypeId());
				viewBillRemarks.setBillClassification(billClassification);
			}
			if(null != billEntryDetailsDTO.getBillTypeNumber())
			{
				MasBillDetailsType masBillDetailsType = getMasBillDetailsType(billEntryDetailsDTO.getBillTypeNumber());
				viewBillRemarks.setBillType(masBillDetailsType);
			}
			viewBillRemarks.setBillingRemarks(billEntryDetailsDTO.getDeductibleNonPayableReasonBilling());
			viewBillRemarks.setFaRemarks(billEntryDetailsDTO.getDeductibleNonPayableReasonFA());
			if(SHAConstants.BILLING.equalsIgnoreCase(presenterString))
			{
				//viewBillRemarks.setBillingDate(new Timestamp(System.currentTimeMillis()));
				viewBillRemarks.setBillingDate(billEntryDetailsDTO.getBillingCompletedDate());
			}
			else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
			{
				//viewBillRemarks.setFaDate((new Timestamp(System.currentTimeMillis())));
				viewBillRemarks.setFaDate(billEntryDetailsDTO.getFaCompletedDate());
			}
			viewBillRemarks.setDeletedFlag("N");
			if(null != viewBillRemarks.getBillKey())
			{
				entityManager.merge(viewBillRemarks);
			}
			else
			{
				entityManager.persist(viewBillRemarks);
			}
			entityManager.flush();
			return viewBillRemarks;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		//return viewBillRemarks;
		
		
	}
	
	

	
	private MasBillClassification getMasBillClassificationBasedOnKey(Long key)
	{
		Query query = entityManager.createNamedQuery("MasBillClassification.findByKey");
		query = query.setParameter("parentKey", key);
		List<MasBillClassification> billClassificationList = query.getResultList();
		if(null != billClassificationList && !billClassificationList.isEmpty())
		{
			entityManager.refresh(billClassificationList.get(0));
			return billClassificationList.get(0);
		}
		return null;
	}
	
	private MasBillDetailsType getMasBillDetailsType(Long key)
	{
		Query query = entityManager.createNamedQuery("MasBillDetailsType.findByKey");
		query = query.setParameter("parentKey", key);
		List <MasBillDetailsType> billTypeList = query.getResultList();
		if(null != billTypeList && !billTypeList.isEmpty())
		{
			entityManager.refresh(billTypeList.get(0));
			return billTypeList.get(0);
		}
		return null;
	}
	
	public List<ViewBillRemarks> getViewBillRemarksForROD(Long rodKey, Long billClassificationKey, Long billTypeKey)
	{
		Query query = entityManager.createNamedQuery("ViewBillRemarks.findByRodBillTypeBillClassification");
		query = query.setParameter("rodKey", rodKey);
		query.setParameter("billClassificationKey", billClassificationKey);
		query.setParameter("billTypeKey",billTypeKey);
		
		List<ViewBillRemarks> viewBillRemarksList = query.getResultList();
		if(null != viewBillRemarksList && !viewBillRemarksList.isEmpty())
		{
			for (ViewBillRemarks viewBillRemarks : viewBillRemarksList) {
				entityManager.refresh(viewBillRemarks);
			}
			
			return viewBillRemarksList;
		}
		return null;
	}
	
	public void loadRRCRequestValues(PreauthDTO preauthDTO,Double sumInsured,String stage)
	{
		//RRCDTO requestRRCDTO = new RRCDTO();
		RRCDTO requestRRCDTO = preauthDTO.getRrcDTO();
		
		if(null != preauthDTO.getClaimDTO().getNewIntimationDto())
		{
			//load policy data
			
			String policyNumber = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber();
			if(null != requestRRCDTO)
			{
				requestRRCDTO.setPolicyNo(policyNumber);
				requestRRCDTO.setIntimationNo(preauthDTO.getNewIntimationDTO().getIntimationId());
				requestRRCDTO.setProductName(preauthDTO.getNewIntimationDTO().getProductName());
				//Duration on hold
				Date admissionDate = preauthDTO.getNewIntimationDTO().getAdmissionDate();
		//			String duration = dbCalculationService.getPolicyAgeing(admissionDate, policyNumber);
				String duration = preauthDTO.getNewIntimationDTO().getPolicyYear();
				
				requestRRCDTO.setDuration(duration);
				requestRRCDTO.setSumInsured(sumInsured);
				
				//load Hospital data
				requestRRCDTO.setHospitalName(preauthDTO.getNewIntimationDTO().getHospitalDto().getName());
				requestRRCDTO.setHospitalCity(preauthDTO.getNewIntimationDTO().getHospitalDto().getCity());
				//if(null != preauthDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals())
				requestRRCDTO.setHospitalZone(preauthDTO.getNewIntimationDTO().getHospitalDto().getHospitalZone());
				requestRRCDTO.setDateOfAdmission(admissionDate);
				if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getDischargeDate())
				requestRRCDTO.setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
				
				//load Insured data
				requestRRCDTO.setInsuredName(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				if(null != preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredAge())
				requestRRCDTO.setInsuredAge(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredAge().longValue());
				if(null != preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredGender() )
				requestRRCDTO.setSex(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getValue());
				
				requestRRCDTO.setClaimType(preauthDTO.getClaimDTO().getClaimTypeValue());
				requestRRCDTO.setProcessingStage(stage);
				if(preauthDTO.getStrUserName() !=null){	
					TmpEmployee tmpEmployee = getEmployeeDetails(preauthDTO.getStrUserName());
					if(null != tmpEmployee)
					{
						/*if(!(null != tmpEmployee.getEmpFirstName()))
						tmpEmployee.setEmpFirstName("");
					else if(!(null != tmpEmployee.getEmpMiddleName()))
						tmpEmployee.setEmpMiddleName("");
					else if(!(null != tmpEmployee.getEmpLastName()))
						tmpEmployee.setEmpLastName("");*/
						StringBuffer strName = new StringBuffer();
						if(null != tmpEmployee.getEmpFirstName())
						{
							strName.append(tmpEmployee.getEmpFirstName());
						}
						if(null != tmpEmployee.getEmpMiddleName())
						{
							if(("").equalsIgnoreCase(strName.toString()))
							{
								strName.append(tmpEmployee.getEmpMiddleName()); 
							}
							else
							{
								strName.append(tmpEmployee.getEmpMiddleName());
							}
						}
						if(null != tmpEmployee.getEmpLastName())
						{
							if(("").equalsIgnoreCase(strName.toString()))
							{
								strName.append(tmpEmployee.getEmpLastName()); 
							}
							else
							{
								strName.append(tmpEmployee.getEmpLastName());
							}
						}

						//requestRRCDTO.setEmployeeName(tmpEmployee.getEmpFirstName()+tmpEmployee.getEmpMiddleName()+tmpEmployee.getEmpLastName());
						requestRRCDTO.setEmployeeName(strName.toString());
						requestRRCDTO.setEmployeeId(tmpEmployee.getEmpId());
						SelectValue empEffort = new SelectValue();
						empEffort.setId(tmpEmployee.getKey());
						empEffort.setValue(tmpEmployee.getEmpId()+"-"+tmpEmployee.getEmpFirstName());
						requestRRCDTO.getEmployeeEffortDTO().setSelEmployeeId(empEffort);
					}
				}
				//requestRRCDTO.setEmployeeNameList(getEmployeeNamesFromMaster());
				requestRRCDTO.setDataSourcesMap(getEmployeeNamesFromMaster());				
				setRequestStageIdForRRC(requestRRCDTO,stage);
				/*ExtraEmployeeEffortDTO extraEffortEmpDTO = new ExtraEmployeeEffortDTO();
					//EmployeeMasterDTO empMasterDTO = new EmployeeMasterDTO();
					//empMasterDTO.setEmployeeName(requestRRCDTO.getEmployeeName());
					extraEffortEmpDTO.setEmployeeId(requestRRCDTO.getEmployeeId());
					//extraEffortEmpDTO.setEmployeeName(requestRRCDTO.getEmployeeName());
					//extraEffortEmpDTO.setEmployeeNameDTO(empMasterDTO);
					
					List<ExtraEmployeeEffortDTO> extraEffortDTOList = new ArrayList<ExtraEmployeeEffortDTO>();
					extraEffortDTOList.add(extraEffortEmpDTO);
					//requestRRCDTO.setEmployeeEffortList(extraEffortDTOList);
					
					//requestRRCDTO.setEmployeeDept();
	*/			
				setQuantumReductionForRRC(requestRRCDTO,preauthDTO,stage);
				preauthDTO.setRrcDTO(requestRRCDTO);
			}
			
			
		}
		
	}
	
	public RRCDTO setRequestStageIdForRRC(RRCDTO requestRRCDTO , String stage)
	{
		if((SHAConstants.PROCESS_PRE_MEDICAL).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
		}
		if((SHAConstants.PROCESS_PREAUTH).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PREAUTH_STAGE);
		}
		else if ((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		}
		else if ((SHAConstants.PROCESS_ENHANCEMENT).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.ENHANCEMENT_STAGE);
		}
		else if ((SHAConstants.PROCESS_WITHDRAW_PREAUTH).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.WITHDRAW_STAGE);
		}
		else if ((SHAConstants.PROCESS_DOWNSIZE_PREAUTH).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.DOWNSIZE_STAGE);
		}
		else if ((SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.DOWNSIZE_STAGE);
		}
		
		else if ((SHAConstants.PROCESS_PED_QUERY).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_QUERY);
		}
		
		else if ((SHAConstants.PROCESS_REJECTION).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PROCESS_REJECTION_STAGE);
		}
		else if ((SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_HOSPITAL_STAGE);
		}
		else if ((SHAConstants.FIELD_VISIT).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.FVR_STAGE_KEY);
		}
		
		else if ((SHAConstants.PED_REQUEST_PROCESS).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		}
		
		else if ((SHAConstants.PED_REQUEST_APPROVER).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		}
		else if ((SHAConstants.ADVISE_ON_PED).equals(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		}
		else if ((SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS).equals(stage))
		{
			/**
			 * To Confirm stage with developer . -- No separate stage available for submit specialist. 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		}
		else if ((SHAConstants.PROCESS_COORDINATOR_REPLY).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.COORDINATOR_PROCESS_REPLY_STAGE);
		}
		
		else if ((SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.SPECIALIST_PROCESS_STAGE);
			//requestRRCDTO.setRequestedStageId(ReferenceTable.COORDINATOR_PROCESS_REPLY_STAGE);
		}
		else if ((SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT).equals(stage))
		{
			/**
			 * To Confirm stage with developer . -- no separate stage available 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		else if ((SHAConstants.PROCESS_INVESTIGATION_INTIATED).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}
		else if ((SHAConstants.ASSIGN_INVESTIGATION_INTIATED).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		//requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		else if ((SHAConstants.ACKNOWLEDGE_INVESTIGATION).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
			//requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);

		//	requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}
		else if ((SHAConstants.ACKNOWLEDGE_DOC_RECEIVED).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		//	requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}
		else if ((SHAConstants.CREATE_ROD).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.CREATE_ROD_STAGE_KEY);
		//	requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}
		else if ((SHAConstants.BILL_ENTRY).equals(stage))
		{
			/**
			 * To Confirm stage with developer . 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.BILL_ENTRY_STAGE_KEY);
		//	requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}
		
		else if((SHAConstants.ADD_ADDITIONAL_DOCUMENTS).equals(stage))
		{
			/**
			 * The stage needs to revisited. Need to check sathish sir for this once.
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		}
		
		else if((SHAConstants.PROCESS_UPLOAD_INVESTIGATION).equals(stage))
		{
			/**
			 * The stage needs to revisited. Need to check sathish sir for this once.
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.UPLOAD_INVESTIGATION_STAGE);

		//	requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		}
		else if((SHAConstants.PROCESS_CLAIM_REGISTRATION).equals(stage))
		{
			/**
			 * The stage needs to revisited. Need to check sathish sir for this once.
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		}
		
		else if((SHAConstants.PROCESS_CONVERT_CLAIM).equals(stage))
		{
			/**
			 * The stage needs to be revisted . Need to check with sathish sir.
			 * 
			 * */
			requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		}
		
		
		
		
		else if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.ZONAL_REVIEW_STAGE);
		}
		else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		
		else if((SHAConstants.BILLING).equalsIgnoreCase(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.BILLING_STAGE);
		}
		else if((SHAConstants.RRC_FINANCIAL).equalsIgnoreCase(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.FINANCIAL_STAGE);
		}
		
		else if((SHAConstants.INITIATE_RRC_REQUEST).equalsIgnoreCase(stage))
		{
			requestRRCDTO.setRequestedStageId(ReferenceTable.RRC_STAGE);
		}
		
		return requestRRCDTO;
	}
	
	private Map<String, Object>  getEmployeeNamesFromMaster()
	{
		BeanItemContainer<SelectValue> employeeNameList = getEmployeeNameFromMaster();
		Map<String, Object> employeeNameMap = new HashMap<String, Object>();
		employeeNameMap.put(SHAConstants.EMPLOYEE_NAME_LIST,employeeNameList);
		employeeNameMap.put(SHAConstants.EMPLOYEE_NAME_ID_MAP , loadEmployeeNameData());
		//employeeNameMap.put(SHAConstants.EMPLOYEE_ID, getEmployeeLoginNameContainer());
		employeeNameMap.put(SHAConstants.EMPLOYEE_ID, getEmployeesFromMaster());
		
		return employeeNameMap;
	}
	
	public BeanItemContainer<SelectValue> getEmployeeNameFromMaster()
	{
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeNameList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey().longValue());
				StringBuffer strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				selectValue.setValue(strName.toString());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}
	
	
	public BidiMap loadEmployeeNameData()
	{
		BidiMap employeeNameMap = new DualHashBidiMap();
		//Map<String,String> employeeNameMap = new HashMap<String, String>();
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		/*BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);*/
		if (!employeeNameList.isEmpty()) {
			//List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (TmpEmployee employee : employeeNameList) {
				StringBuffer strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				employeeNameMap.put(strName.toString() , employee.getEmpId());
			}
			
		}
		return employeeNameMap;
	
	}
	public BeanItemContainer<SelectValue> getEmployeeLoginNameContainer()
	{
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeNameList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey().longValue());
				if(null != employee.getLoginId() && !("").equalsIgnoreCase(employee.getLoginId()))
				{
					selectValue.setValue(employee.getLoginId());
				}
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}
	
	
	public void submitClaimRequestTaskToDBProcedure(PreauthDTO dto, Boolean isZonalReview,
			String outCome,Reimbursement reimbursement,Boolean isWaitForInput) throws Exception {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) dto.getDbOutArray();
		String sendReplyFrom = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if (isZonalReview) {
			/*SubmitProcessClaimRequestZMRTask claimRequestZMRTask = BPMClientContext
					.getClaimRequestZonalReview(dto.getStrUserName(),
							dto.getStrPassword());
			*/
			

			//HumanTask humanTask = dto.getRodHumanTask();
			
			if(outCome != null && (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_ZMR_CANCEL_ROD_STATUS) 
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_ZMR_REFER_TO_BILLENTRY) 
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FA_REFER_TO_BILL_ENTRY_STATUS) )) {
				//PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				//humanTask.setOutcome(outCome);
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, SHAConstants.YES_FLAG);
				//humanTask.setPayload(payloadForCancelROD);
			} else {
				
				//commented by noufel since claim amount in MA approval time should not override.bill entry amount will be consider as the claimed amount
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
				log.error(e.toString());
				e.printStackTrace();
				
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN ZONAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR ZONAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					/*BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					claimRequestZMRTask.execute("claimshead", humanTask);*/

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR IN ZONAL (#*#&*#*#*#*#*#*#");
				}
				
				
			}
		} else {
			/*SubmitProcessClaimRequestTask claimRequestTask = BPMClientContext
					.getClaimRequest(dto.getStrUserName(), dto.getStrPassword());*/

			//HumanTask humanTask = dto.getRodHumanTask();
			
			if(outCome != null && (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS)
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY)
					|| outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FA_REFER_TO_BILL_ENTRY_STATUS))) {
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, SHAConstants.YES_FLAG);
				//IMSSUPPOR-28693
				wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
				/*PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				
				humanTask.setOutcome(outCome);
				humanTask.setPayload(payloadForCancelROD);*/
			} else {

				/*ClaimType claims = new ClaimType();

				ClaimRequestType claimType = null;

				PayloadBOType payloadBO = new PayloadBOType();
				
				CustomerType customer = payloadBO.getCustomer();

				PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
				
				ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();*/
				//if(processActorInfo == null){
					//processActorInfo = new ProcessActorInfoType();
					
				//}
				//processActorInfo.setEscalatedByRole("");
				//processActorInfo.setEscalatedByUser(dto.getStrUserName());
				//payloadBO.setProcessActorInfo(processActorInfo);

				/*if (paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				}
				paymentInfo.setApprovedAmount(dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() != null ? dto
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() : 0d);
				paymentInfo
						.setClaimedAmount(dto.getAmountConsidered() != null ? SHAUtils
								.getDoubleValueFromString(dto.getAmountConsidered())
								: 0d);*/
				//commented by noufel since claim amount in MA approval time should not override.bill entry amount will be consider as the claimed amount
//				Double claimedAmount = dto.getAmountConsidered() != null ? SHAUtils
//						.getDoubleValueFromString(dto.getAmountConsidered())
//						: 0d; 
//				
//				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);

				/*if (payloadBO.getClaimRequest() != null) {
					claimType = payloadBO.getClaimRequest();
				} else {
					claimType = new ClaimRequestType();
				}*/

				if (dto.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
					//claimType.setClientType("MEDICAL");
					//payloadBO.setInvestigation(null);
					//payloadBO.setQuery(null);
				//	FieldVisitType fieldVisit = new FieldVisitType();
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					
					/*if(dto.getNewIntimationDTO().getAdmissionDate() != null){
						String intimDate = SHAUtils.formatIntimationDateValue(dto.getNewIntimationDTO().getAdmissionDate());
						RRCType rrc = payloadBO.getRrc();
						if(rrc == null){
							rrc = new RRCType();
						}
						
						//rrc.setFromDate(intimDate);
						//payloadBO.setRrc(rrc);
					}*/
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
//							wrkFlowMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//							wrkFlowMap.put(SHAConstants.CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							//fieldVisit.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							//claimType.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
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
					/*//claimType.setClientType("MEDICAL");
					//payloadBO.setInvestigation(null);
					//payloadBO.setQuery(null);
					//payloadBO.setFieldVisit(null);
					if (claimType != null) {
						claimType.setReimbReqBy(null);
					}*/
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

				//RODType rodType = new RODType();
				//rodType.setAckNumber(dto.getRodNumber());
				//rodType.setKey(dto.getKey());
				//payloadBO.setRod(rodType);
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					//Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					

				//	ClassificationType classification = payloadBO.getClassification();
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.ZMR_STAGE_NAME);

				/*	if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}*/
					
					//payloadBO.setClassification(classification);
					
				}
				
				wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.MA_STAGE_SOURCE);
				
				
				//humanTask.setPayload(payloadBO);
			}
			//try{
			//claimRequestTask.execute(dto.getStrUserName(), humanTask);
			/*
			if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
				
//				CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//				cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
				
			}*/
			//code added for setting covid priority in wk table by noufel
//			if(reimbursement.getClaim() != null && reimbursement.getClaim().getPriorityEvent() == null){
			if(reimbursement.getBillingAutoAllocIcdFlag() != null && reimbursement.getBillingAutoAllocIcdFlag().equalsIgnoreCase("C")){
				wrkFlowMap.put(SHAConstants.PRIORITY,SHAConstants.COVID);
			}else if(reimbursement.getClaim() != null && reimbursement.getClaim().getPriorityEvent() != null && !reimbursement.getClaim().getPriorityEvent().trim().isEmpty()){
				wrkFlowMap.put(SHAConstants.PRIORITY,reimbursement.getClaim().getPriorityEvent());
			}
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			//dbCalService.initiateTaskProcedure(objArrayForSubmit);
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
			if (dto.getIsPreauthAutoAllocationQ() != null && dto.getIsPreauthAutoAllocationQ()) {
				if(null != wrkFlowMap){
					dbCalService.releaseHoldClaim(Long.parseLong(String.valueOf(wrkFlowMap.get(SHAConstants.WK_KEY))));
				}
				}
			
			/*}catch(Exception e){
				e.printStackTrace();
				log.error(e.toString());
				
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN MA STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				//try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR MA----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);*/
					//BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					//claimRequestTask.execute("claimshead", humanTask);

				/*} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR IN MA (#*#&*#*#*#*#*#*#");
				}*/
			//}
		}
		// HumanTask humanTask = dto.getHumanTask();
	}
	
	public void submitBillingAndFATaskToDBForClaim(PreauthDTO dto,
			Boolean isClaimBilling, String outCome, Reimbursement reimbursement) throws Exception{


		Map<String, Object> wrkFlowMap = (Map<String, Object>) dto.getDbOutArray();
		if (isClaimBilling) {
			/*SubmitProcessClaimBillingTask submitTask = BPMClientContext
					.getClaimBillingTask(dto.getStrUserName(),
							dto.getStrPassword());*/
		
			//HumanTask humanTask = dto.getRodHumanTask();
			if(outCome.equalsIgnoreCase(SHAConstants.OUTCOME_BILLING_CANCEL_ROD)) {
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);	
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG,SHAConstants.YES_FLAG);
				//PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);				
			} else {
			wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
			if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR) ) {

				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.BILLING_CURRENT_QUEUE);
			}else if (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
			{
				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.BILLING_CURRENT_QUEUE);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG,SHAConstants.YES_FLAG);
			}
			
			else if (reimbursement.getStatus().getKey()
					.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {

				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.BILLING_CURRENT_QUEUE);
		
					if (dto.getPreauthDataExtractionDetails()
							.getTreatmentType() != null) {
				
						wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getSpecialityName());
					} else {
						wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, "");
					
					}
					
					/** The below column updated to  identify the send reply cases in parallel flow. jira -GALAXYMAIN-8985**/
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS,SHAConstants.SEND_REPLY_BILLING);
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
			}
				
				if(reimbursement.getStatus() != null && reimbursement.getStatus().getKey() != null){
					//Status status = entityManager.find(Status.class, reimbursement.getStatus().getKey());
					//if(null != status)
					//{
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.BILLING_STAGE_SOURCE_NAME);
					//}
					
				}
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)){
					//Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					//if(null != status)
					//{
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.BILLING_STAGE_SOURCE_NAME);
					//}
					/*if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}*/
//					classification.setSource(stage.getStageName());
				}
				if (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
				{
					Stage  stage = getStageByKey(ReferenceTable.BILLING_STAGE);
					if(null != stage)
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.BILLING_STAGE_SOURCE_NAME);
						//classification.setSource(stage.getStageName());
				}
				
				wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.BILLING_STAGE_SOURCE_NAME);
				
				//payloadBO.setClassification(classification);
				
				Double claimedAmount = 0d;
				if(reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount();
				} 
				if(reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount();
				}
				
				if(reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount();
				}

				/*PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
				if(paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				}*/
					
				/*paymentInfo.setClaimedAmount(dto.getAmountConsidered() != null ? SHAUtils
						.getDoubleValueFromString(dto.getAmountConsidered())
						: 0d);*/
				Double claimedAmt = dto.getAmountConsidered() != null ? SHAUtils
						.getDoubleValueFromString(dto.getAmountConsidered())
						: 0d;
						
						
				if(dto.getUserLimitAmount() != null){
					claimedAmount = SHAUtils.getDoubleFromStringWithComma(dto.getUserLimitAmount());
				}
				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claimedAmount);
				//Double approvedAmt = isClaimBilling ? reimbursement.getBillingApprovedAmount() : reimbursement.getFinancialApprovedAmount();
				// Approved amount variable is not yet decided. Hence keeping this on hold.
				//wrkFlowMap.put(SHAConstants.app, value)
				//paymentInfo.setApprovedAmount(isClaimBilling ? reimbursement.getBillingApprovedAmount() : reimbursement.getFinancialApprovedAmount());
				//payloadBO.setPaymentInfo(paymentInfo);
				
				/*RRCType rrc = payloadBO.getRrc();
				if(rrc == null){
				   rrc = new RRCType();
				   Date currentDate = reimbursement.getCreatedDate();
				   rrc.setToDate(SHAUtils.changeDatetoString(currentDate));
				}
				payloadBO.setRrc(rrc);


				humanTask.setPayload(payloadBO);*/
		}
			try{
			//submitTask.execute(dto.getStrUserName(), humanTask);
				
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
			
			}catch(Exception e){
				e.printStackTrace();
//				log.error(e.toString());
				
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN FINANCIAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR FA APPROVAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
				//	BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					//submitTask.execute("claimshead", humanTask);
					
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);

				} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR (#*#&*#*#*#*#*#*#");
				}
			}
		} else {

			/*SubmitProcessClaimFinancialsTask submitTask = BPMClientContext
					.getClaimFinancialTask(dto.getStrUserName(),
							dto.getStrPassword());*/

			//HumanTask humanTask = dto.getRodHumanTask();
			if(outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FA_CANCEL_ROD_STATUS)) {
				//PayloadBOType payloadForCancelROD = getPayloadForCancelROD(dto, reimbursement.getDocAcknowLedgement());
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, SHAConstants.YES_FLAG);
			//	humanTask.setOutcome(outCome);
				//humanTask.setPayload(payloadForCancelROD);
			} else {
				/*PayloadBOType payloadBO = humanTask.getPayload();
				payloadBO.getClaimRequest().setClientType(null);
				payloadBO.getClaimRequest().setReferTo(null);
				humanTask.setOutcome(outCome);
				ClaimRequestType claimRequestType = payloadBO.getClaimRequest();
				
				CustomerType customer = payloadBO.getCustomer();*/
				wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
				if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS)) {
					/*payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");*/
					//InvestigationType investigation = new InvestigationType();
					if (dto.getInvestigationKey() != null) {
						wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, dto.getInvestigationKey());
						wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.FA_CURRENT_QUEUE);
						/*investigation.setKey(dto.getInvestigationKey());
						payloadBO.setInvestigation(investigation);*/
					}
				} else if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS)) {
					/*payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");
					FieldVisitType fieldVisit = new FieldVisitType();
					*/
					
					Long hospital = reimbursement.getClaim().getIntimation().getHospital();
					
					if(hospital != null){
						Hospitals hospitalByKey = getHospitalByKey(hospital);
						if(hospitalByKey != null){
							
							Long cpuId = hospitalByKey.getCpuId();
							if(cpuId != null){
							TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
//							wrkFlowMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//							//fieldVisit.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//							//claimRequestType.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//							wrkFlowMap.put(SHAConstants.CPU_CODE, tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
							}
						}
					}
					
					/*if(dto.getNewIntimationDTO().getAdmissionDate() != null){
						String intimDate = SHAUtils.formatIntimationDateValue(dto.getNewIntimationDTO().getAdmissionDate());
						RRCType rrcType = new RRCType();
						rrcType.setFromDate(intimDate);
						payloadBO.setRrc(rrcType);
					}*/
					
					if (dto.getFvrKey() != null) {
						wrkFlowMap.put(SHAConstants.FVR_KEY, dto.getFvrKey());
						//fieldVisit.setKey(dto.getFvrKey());
						//payloadBO.setFieldVisit(fieldVisit);
					}
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.FA_CURRENT_QUEUE);
					
				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
					/*claimRequestType.setClientType("MEDICAL");
					payloadBO.setInvestigation(null);
					payloadBO.setFieldVisit(null);
					payloadBO.setQuery(null);*/
					//claimRequestType.setReimbReqBy("FA");
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.FA_CURRENT_QUEUE);
					wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, SHAConstants.PARALLEL_MEDICAL_PENDING);
					//claimRequestType.setResult("MEDICAL");
					//CustomerType custType = payloadBO.getCustomer();
					 {
						if (dto.getPreauthDataExtractionDetails()
								.getTreatmentType() != null) {
							/*custType.setTreatmentType(dto
									.getPreauthDataExtractionDetails()
									.getTreatmentType().getValue());*/
							wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE, dto
									.getPreauthDataExtractionDetails()
									.getTreatmentType().getValue());
						} else {
							//custType.setTreatmentType("");
							wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE,"");
						}
                        if(dto.getSpecialityName() != null){
                        	wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getSpecialityName());
                        	//custType.setSpeciality(dto.getSpecialityName());
                        }
                        
                        /** The below column updated to  identify the send reply cases in parallel flow. jira -GALAXYMAIN-8985**/
    					
    					wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS,SHAConstants.SEND_REPLY_FA);
    					
						//payloadBO.setCustomer(custType);
						
						/*ProcessActorInfoType processActorInfo = payloadBO.getProcessActorInfo();
						if(processActorInfo == null){
							processActorInfo = new ProcessActorInfoType();
							processActorInfo.setEscalatedByUser("");
							processActorInfo.setEscalatedByRole("");
							payloadBO.setProcessActorInfo(processActorInfo);
						}*/
					}

				}
				/**
				 * Added for refer to bill entry screen.
				 * */
				else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY)) {
					/*claimRequestType.setClientType("ACK");
					payloadBO.setInvestigation(null);
					payloadBO.setFieldVisit(null);
					payloadBO.setQuery(null);
					claimRequestType.setReimbReqBy("FA");
					claimRequestType.setResult(SHAConstants.BILL_ENTRY_OUTCOME);*/
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.FA_CURRENT_QUEUE);
					wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG,SHAConstants.YES_FLAG);
					//humanTask.setOutcome(outCome);
				}  
				else if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS)) {
					/*payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("MEDICAL");
					claimRequestType.setReferTo(outCome);
					claimRequestType.setResult("MEDICAL");
					claimRequestType.setReimbReqBy("FA");*/
					wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.FA_CURRENT_QUEUE);

				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
					/*payloadBO.setInvestigation(null);
					payloadBO.setQuery(null);
					payloadBO.setFieldVisit(null);
					claimRequestType.setClientType("POSTMEDICAL");
					QueryType queryType = new QueryType();
					if (dto.getQueryKey() != null) {
						queryType.setKey(dto.getQueryKey());
						queryType.setIsQueryPending(true);
						queryType.setIsQueryReplyReceived(false);
						payloadBO.setQuery(queryType);
					}
					claimRequestType.setResult("QUERY");*/
					
					if (dto.getQueryKey() != null) {
						wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY, dto.getQueryKey());
						//queryType.setKey(dto.getQueryKey());
						//queryType.setIsQueryPending(true);
						//queryType.setIsQueryReplyReceived(false);
						//payloadBO.setQuery(queryType);
					}
					
				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REJECT_STATUS)
						|| reimbursement.getStatus().getKey()
								.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
					//claimRequestType.setClientType("POSTMEDICAL");
				}
				/**
				 * If approve is clicked from FA, the below
				 * attributes are set to null, so that task gets
				 * approved.
				 * */
				else
				{
					//claimRequestType.setResult(null);
					//claimRequestType.setReferTo(null);
				}

				wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.FA_CURRENT_QUEUE);
				/*claimRequestType.setReimbReqBy("FA");
				claimRequestType.setResult(outCome);*/
				// claimRequestType.setOption(outCome);

				/**
				 * 
				 * option is used to decide whether it is medical rebilling
				 * or financial re medical. Hence need to check with yosuva on this.
				 * 
				 * */
				/*
				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
					if (null != claimRequestType.getOption()
							&& SHAConstants.FINANCIAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())
							|| SHAConstants.REMEDICAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())) {
						claimRequestType
								.setOption(SHAConstants.REMEDICAL_REBILLING);
						claimRequestType.setClientType("");
					} else {
						claimRequestType
								.setOption(SHAConstants.FINANCIAL_REMEDICAL);
						claimRequestType.setClientType("");
					}
				}
*/
				/*if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
					if (claimRequestType.getOption() != null
							&& SHAConstants.FINANCIAL_REMEDICAL
									.equalsIgnoreCase(claimRequestType.getOption())
							|| SHAConstants.REMEDICAL_REBILLING
									.equalsIgnoreCase(claimRequestType.getOption())) {
						claimRequestType
								.setOption(SHAConstants.REMEDICAL_REBILLING);
					} else {
						claimRequestType
								.setOption(SHAConstants.FINANCIAL_REBILLING);
					}
				}
*/
				if (dto.getStatusKey().equals(
						ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
						if(dto.getPreauthMedicalDecisionDetails().getSpecialistType() != null){
							wrkFlowMap.put(SHAConstants.SPECIALITY_NAME, dto.getPreauthMedicalDecisionDetails().getSpecialistType().getValue());
						}
						if(null != dto.getStrUserName())
						{
						wrkFlowMap.put(SHAConstants.REFERENCE_USER_ID, dto.getStrUserName());
						}
				}

				
              if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					if(null != stage)
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, stage.getStageName());
					/*if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}*/
				}
              
              if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
            		  || reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){

					Status status = entityManager.find(Status.class, reimbursement.getStatus().getKey());
							wrkFlowMap.put(SHAConstants.STAGE_SOURCE, status.getProcessValue());
					/*if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(status.getProcessValue());
					}*/
					
				}
              
              if (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))
				{
            	  Stage  stage = getStageByKey(ReferenceTable.FINANCIAL_STAGE);
					if(null != stage)
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FINANCIAL_STAGE_SOURCE_NAME);
						
				}
              
              wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FINANCIAL_STAGE_SOURCE_NAME);
              
              // add for common billing 21/01/2021 refer case 
              if(!reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
            	  if((dto.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(dto.getScreenName())) 
            			  || (dto.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(dto.getScreenName()))) {
            		  wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.BILLING_CURRENT_QUEUE);
            	  }
              }
              
             /* Double claimedAmount = 0d;
				if(reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getHospitalizationClaimedAmount();
				} 
				if(reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPreHospitalizationClaimedAmount();
				}
				
				if(reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null) {
					claimedAmount += reimbursement.getDocAcknowLedgement().getPostHospitalizationClaimedAmount();
				}
              
				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,claimedAmount);*/
              /*PaymentInfoType paymentInfo = payloadBO.getPaymentInfo();
              
				if(paymentInfo == null) {
					paymentInfo = new PaymentInfoType();
				} 
				paymentInfo.setClaimedAmount(claimedAmount);
				paymentInfo.setApprovedAmount(isClaimBilling ? reimbursement.getBillingApprovedAmount() : reimbursement.getFinancialApprovedAmount());
				payloadBO.setPaymentInfo(paymentInfo);
              
              
              payloadBO.setClassification(classification);

				humanTask.setPayload(payloadBO);*/
			}
			//try{
			//submitTask.execute(dto.getStrUserName(), humanTask);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
		/*	if(outCome.equalsIgnoreCase(SHAConstants.CANCEL_ROD_OUTCOME)){
				
//				CancelAcknowledgement cancelAcknowledgement = BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,dto.getStrPassword());
//				cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC, humanTask.getPayload());
				
			}*/
			
			/*}catch(Exception e){
				e.printStackTrace();
				log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN FINANCIAL STAGE (#)#)#)#)#)#)%(%^)#)#" + e.toString());
				
				//try {
					log.info("*&*&*&*&*&*&*&*& RESUBMIT THE BPMN TASK FOR FA APPROVAL----------------- *&*&*&*&*&*&"+dto.getNewIntimationDTO().getIntimationId());
				//	BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					//submitTask.execute("claimshead", humanTask);
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					
					DBCalculationService dbCalService = new DBCalculationService();
					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);*/
					
					
				/*} catch(Exception u) {
					log.error("*#*#*#*# SECOND SUBMIT ERROR (#*#&*#*#*#*#*#*#");
				}
				*/
			//}

		}
		if (dto.getIsPreauthAutoAllocationQ() != null && dto.getIsPreauthAutoAllocationQ()) {
		if(null != wrkFlowMap){
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.releaseHoldClaim(Long.parseLong(String.valueOf(wrkFlowMap.get(SHAConstants.WK_KEY))));
		}
		}
	}
	
	private Boolean submitRRCRequestTaskToDB(PreauthDTO preauthDTO,
			Long rrcRequestKey,Claim claimObject) {
		/**
		 * Need to remove hardcoded user name and password, once active
		 * directory is finalized.
		 * */
		try {
			Long workFlowKey = 0l;
			if(null != claimObject)
			{
			Hospitals hospitals = getHospitalById(claimObject.getIntimation().getHospital());		
			
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospitals);
			
			Object[] inputArray = (Object[])arrayListForDBCall[0];
			
			Object[] parameter = new Object[1];
			parameter[0] = inputArray;
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			inputArray[SHAConstants.INDEX_WORK_FLOW_KEY] = workFlowKey; 
			inputArray[SHAConstants.INDEX_CLAIM_TYPE] = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
				if((SHAConstants.ACKNOWLEDGE_DOC_RECEIVED).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || (SHAConstants.CREATE_ROD).equalsIgnoreCase(preauthDTO .getRrcDTO().getProcessingStage())
						|| (SHAConstants.BILL_ENTRY).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()) || (SHAConstants.ADD_ADDITIONAL_DOCUMENTS).equalsIgnoreCase(preauthDTO.getRrcDTO().getProcessingStage()))
				{

					Insured insured = claimObject.getIntimation().getInsured();
					if(null != claimObject && null != claimObject.getIsVipCustomer() && claimObject.getIsVipCustomer().equals(1l)){
						inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
					}
					else if(null != insured && null != insured.getInsuredAge() && insured.getInsuredAge()>60){
						inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
					} else if (claimObject.getClaimPriorityLabel() != null && claimObject.getClaimPriorityLabel().equals("Y")) {
						inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
					}else{
						inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
					}
				
					inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
					//inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.NORMAL;
					
					
					RRCDTO rrcDTO = preauthDTO.getRrcDTO();
					if(rrcDTO.getStageKey() != null){
						Stage stage = entityManager.find(Stage.class, rrcDTO.getStageKey());
						inputArray[SHAConstants.INDEX_STAGE_SOURCE] = stage.getStageName();
					}
					
				}
			
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.RRC_INITIATE_OUTCOME;
			inputArray[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = preauthDTO.getRrcDTO().getRrcRequestNo();
			//rrcType.setRequestNo(preauthDTO.getRrcDTO().getRrcRequestNo());
			/**
			 * rrc type source to be set . Need to check with yosuva on where to set this.  Based on
			 * yosuva input, rrc type source is not required.
			 * */ 
		
			inputArray[SHAConstants.INDEX_RRC_REQUEST_TYPE] = "FRESH";
			inputArray[SHAConstants.INDEX_RRC_REQUEST_KEY] = rrcRequestKey; 
			dbCalculationService.revisedInitiateTaskProcedure(parameter);
			return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return false;
		}
	}
	

	private Boolean submitRRCRequestTaskToDBForCashless(PreauthDTO preauthDTO,
			Long rrcRequestKey,Claim claimObject) {
		/**
		 * Need to remove hardcoded user name and password, once active
		 * directory is finalized.
		 * */
		try {
			/*RRC rrcTask = BPMClientContext.getIntiateRRCRequestTask(
					BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);*/
			Long workFlowKey = 0l;
			if(null != claimObject)
			{
			Hospitals hospitals = getHospitalById(claimObject.getIntimation().getHospital());		
			
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObject, hospitals);
			
			Object[] inputArray = (Object[])arrayListForDBCall[0];
			
			Object[] parameter = new Object[1];
			parameter[0] = inputArray;
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			inputArray[SHAConstants.INDEX_WORK_FLOW_KEY] = workFlowKey; 
			inputArray[SHAConstants.INDEX_CLAIM_TYPE] = SHAConstants.CASHLESS_CLAIM_TYPE;
			// intimationType.setIntimationNumber(intimationNumber);
			//RRCType rrcType = new RRCType();

			inputArray[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = preauthDTO.getRrcDTO().getRrcRequestNo();
			//rrcType.setRequestNo(preauthDTO.getRrcDTO().getRrcRequestNo());
			/**
			 * rrc type source to be set . Need to check with yosuva on where to set this.  Based on
			 * yosuva input, rrc type source is not required.
			 * */ 
			inputArray[SHAConstants.INDEX_RRC_REQUEST_TYPE] = "FRESH";
			inputArray[SHAConstants.INDEX_RRC_REQUEST_KEY] = rrcRequestKey; 

			Insured insured = null;
			if (null != claimObject)
			insured = claimObject.getIntimation().getInsured();
			
			if(null != claimObject && null != claimObject.getIsVipCustomer() && claimObject.getIsVipCustomer().equals(1l)){
				
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
			}
			else if(null != insured && null != insured.getInsuredAge() && insured.getInsuredAge()>60){
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
			} else if (claimObject.getClaimPriorityLabel() != null && claimObject.getClaimPriorityLabel().equals("Y")) {
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
			}else{
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			}
		
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
			inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.NORMAL;
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.RRC_INITIATE_OUTCOME;
			RRCDTO rrcDTO = preauthDTO.getRrcDTO();
			if(rrcDTO.getRequestedStageId()!= null){
				Stage stage = entityManager.find(Stage.class, rrcDTO.getRequestedStageId());
				inputArray[SHAConstants.INDEX_STAGE_SOURCE] = stage.getStageName();
				//classificationType.setSource(stage.getStageName());
			}

			dbCalculationService.revisedInitiateTaskProcedure(parameter);
			
			return true;
			}
			else
			{
				return false;
			}
		}
			catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return false;
		}
	}
	
	
	private Hospitals getHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);

		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}
	
	private MasHospitals getMasHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("MasHospitals.findByKey");
		query.setParameter("key", key);

		List<MasHospitals> resultList = (List<MasHospitals>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}
	
public void submitCloseClaim(){
		
		List<Map<String, Object>> dbTaskForPreauth = getDBTaskForPreauth(SHAConstants.AUTO_CLOSE_CURRENT_QUEUE);
		
		if(dbTaskForPreauth != null){
			SelectValue selected = null;
			Date currentdate = null;
			for (Map<String, Object> map : dbTaskForPreauth) {
				String intimationNo = (String)map.get(SHAConstants.INTIMATION_NO);
				
				String remainderType = (String)map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
				
				Long rodKey = (Long) map.get(SHAConstants.PAYLOAD_ROD_KEY);
				log.info("Auto close claim get task --------------- intimation ------ rod key" + intimationNo +"---------"+ rodKey);
				Claim claim = getClaimsByIntimationNumber(intimationNo);
				
				CloseClaimPageDTO bean = new CloseClaimPageDTO();
				bean.setClaimKey(claim.getKey());
				bean.setCloseRemarks("");
				bean.setClosedProvisionAmt(claim.getCurrentProvisionAmount());
				currentdate = new Date();
				bean.setClosedDate(currentdate);
				selected = new SelectValue();
				selected.setId(2506l);
				selected.setValue("To Be Considered");
				bean.setReasonId(selected);
				
				if(remainderType != null){
					bean.setCloseRemarks(remainderType);
				}else{
					bean.setCloseRemarks("Auto closure");
				}
				bean.setUserName("SYSTEM");
				bean.setIntimationNumber(intimationNo);
				updateClaimClose(bean,currentdate,rodKey);
				updateStageInformation(claim, currentdate);
				
				map.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_AUTO_CLOSE_END);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(map);
				
				dbCalculationService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
		}
		
		
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
		}
		
		
	}
	
private void updateClaimClose(CloseClaimPageDTO bean,Date dateCellValue,Long rodKey) {
		
		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
//----------------------------------------------------Reimbursement Task -----------------------------------------------------------------------------
		
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		
		 List<Map<String, Object>> taskProcedureForCloseClaim = dbCalculationService.getTaskProcedureForCloseClaim(bean.getIntimationNumber());
		 
		 for (Map<String, Object> map : taskProcedureForCloseClaim) {
			 
			Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
			
			if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
			
				Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
				if(reimbursement != null && ! reimbursementList.contains(reimbursement)){
				   reimbursementList.add(reimbursement);
				}
				
			}
		}
		
//		reimbursementList = getReimbursementListForClose(bean.getIntimationNumber());

//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				Reimbursement reimbursement = createRodService.getReimbursementByKey(reimbursementKey);
//				reimbursementList.add(reimbursement);
//			}
//		}
		
		Boolean isClaimLevel = true;
		
		Boolean anyRodActive = isAnyRodActive(bean.getIntimationNumber(),rodKey != null ? rodKey: 0l);
		if(anyRodActive){
			isClaimLevel = false;
		}
		DBCalculationService dbCalculationService = new DBCalculationService();;
			for (Reimbursement reimbursement : reimbursementList) {
				
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
						|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
				
//				dbCalculationService = new DBCalculationService();
				
				dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(),"R");
				
				updateReimbursmentForCloseClaim(bean.getUserName(), reimbursement);
				
				//Reimbursement hospitalizationRod = getHospitalizationRodForCloseClaim(reimbursement.getClaim().getKey(), reimbursement.getKey());
				
				/*if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
						|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					
					if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						if(hospitalizationRod == null){
							if(reimbursement.getClaim().getLatestPreauthKey() != null){
//								dbCalculationService.invokeAccumulatorProcedure(reimbursement.getClaim().getLatestPreauthKey());
							}else{
								Preauth latestPreauth =getLatestPreauthByClaim(reimbursement.getClaim().getKey());
//								dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
							}
						}else{
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}else{
						if(hospitalizationRod != null){
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}
				}*/
				
				dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
				if(! isClaimLevel){
					PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());
					submitCloseClaimForRodLevel(bean,reimbursement);
				}
				
			}else if(reimbursement.getKey().equals(rodKey)){
				dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(),"R");
				
				updateReimbursmentForCloseClaim(bean.getUserName(), reimbursement);
				dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				if(! isClaimLevel){
					PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());
					submitCloseClaimForRodLevel(bean,reimbursement);
				}
			}
		}

		   if(isClaimLevel){
			   PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());
			   submitCloseClaim(bean);
		   }
		   
		   for (Map<String, Object> map : taskProcedureForCloseClaim) {
			   
			   	Long wrkFlowKey = (Long) map.get(SHAConstants.WK_KEY);
			   	String currentQ = (String)map.get(SHAConstants.CURRENT_Q);
			   	String outCome = currentQ+SHAConstants.CLOSE_CLAIM_CURRENT_Q;
			   	
			   	log.info("Auto close claim PRC_SEC_SUBMIT_PULLBACK --------------- wk_key ------ outcome" + wrkFlowKey +"---------"+ outCome);
			   	
			   	Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
			   	if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
			   		
			   		//Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
			   	  String reimbReqBy = (String)map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
				    if(reimbReqBy != null && (reimbReqBy.equalsIgnoreCase(SHAConstants.MA_CURRENT_QUEUE))){
				    	if(! SHAConstants.getNonClosureCurrentQ().contains(currentQ)){
			   		/*if(reimbursement.getStatus().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
							|| reimbursement.getStatus().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){*/
			   			//dbCalculationService = new DBCalculationService();
					   	   dbCalculationService.pullBackSubmitProcedure(wrkFlowKey, outCome, "claimshead");
				    	}
			   		//}
			   	   }else{
			   		    dbCalculationService.pullBackSubmitProcedure(wrkFlowKey, outCome, "claimshead");
			   	   }
				   	
			   	}else{
			   		//dbCalculationService = new DBCalculationService();
				   	dbCalculationService.pullBackSubmitProcedure(wrkFlowKey, outCome, "claimshead");
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
	closeClaim.setClosedProvisionAmt(claim.getCurrentProvisionAmount());
	
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
//    		     lastPreauth.setStage(preauthStage);
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
    entityManager.merge(claim);
    entityManager.flush();
    
    List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
    for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
		DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
		entityManager.persist(documentDetails);
		entityManager.flush();
	}

	//need to implement provision amount;
	
//	PayloadBOType payloadType = new PayloadBOType();
//	IntimationType intimationType = new IntimationType();
//	intimationType.setIntimationNumber(bean.getIntimationNumber());
//	payloadType.setIntimation(intimationType);
//	
//	
//	CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//	
//	PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
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
//			Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
//
//		}
//		
//		SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//	    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
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
	closeClaim.setClosedProvisionAmt(reimbursement.getCurrentProvisionAmt());
	
	Status status = new Status();
	status.setKey(ReferenceTable.CLAIM_CLOSED_STATUS);
	
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
//    		     lastPreauth.setStage(preauthStage);
    		     lastPreauth.setStatus(preauthStatus);
    		     lastPreauth.setModifiedBy(bean.getUserName());
    		     lastPreauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//    		     entityManager.merge(lastPreauth);
//    		     entityManager.flush();
    		     
    		}
    	}
    }
    
//    claim.setStatus(status);
//    claim.setModifiedBy(bean.getUserName());
//    claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//    claim.setCurrentProvisionAmount(0d);
//    claim.setCloseDate(new Timestamp(System.currentTimeMillis()));
//    entityManager.merge(claim);
//    entityManager.flush();
    
    List<UploadDocumentCloseClaimDTO> uploadDocumentDetails = bean.getUploadDocumentDetails();
    for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : uploadDocumentDetails) {
		DocumentDetails documentDetails = uploadDocumentCloseClaimDTO.getDocumentDetails();
		entityManager.persist(documentDetails);
		entityManager.flush();
	}

	//need to implement provision amount;
	
//	PayloadBOType payloadType = new PayloadBOType();
//	IntimationType intimationType = new IntimationType();
//	intimationType.setIntimationNumber(bean.getIntimationNumber());
//	payloadType.setIntimation(intimationType);
//	
//	
//	CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//	
//	PagedTaskList tasks = closeClaimTask.getTasks(BPMClientContext.BPMN_TASK_USER, null, payloadType);
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
//			Reimbursement Reimbursement = updateReimbursmentForCloseClaim(bean.getUserName(), reimbursementKey);
//
//		}
//		
//		SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//
//	    BPMClientContext.setActiveOrDeactive(task, BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
//	}

	
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

//public Preauth getLatestPreauthByClaim(Long claimKey) {
//	Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
//	query.setParameter("claimkey", claimKey);
//	@SuppressWarnings("unchecked")
//	List<Preauth> singleResult = (List<Preauth>) query.getResultList();
//	if(singleResult != null && ! singleResult.isEmpty()) {
//		entityManager.refresh(singleResult.get(0));
//		Preauth preauth = singleResult.get(0);
//		for(int i=0; i <singleResult.size(); i++) {
//			entityManager.refresh(singleResult.get(i));
//			if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
//				entityManager.refresh(singleResult.get(i));
//				preauth = singleResult.get(i);
//				break;
//			} 
//		}
//		return preauth;
//	}
//	
//	return null;
//	
//	
//}

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
		
		if(stageKey != null && statusKey != null){
			
			Stage stage = entityManager.find(Stage.class, stageKey);
			Status status = entityManager.find(Status.class, statusKey);
			reimbursement.setStage(stage);
			reimbursement.setStatus(status);
			reimbursement.setModifiedBy(userName);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
			entityManager.merge(reimbursement);
			entityManager.flush();
		
		}
		
		

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
		
		return hashMap;
		
	}

@SuppressWarnings("unchecked")
public Reimbursement getHospitalizationRodForCloseClaim(Long claimKey,Long reimbursementKey) {

	Query query = entityManager
			.createNamedQuery("Reimbursement.getRodAscendingOrder");
	query.setParameter("claimkey", claimKey);

	List<Reimbursement> reimbursementList = (List<Reimbursement>) query
			.getResultList();

	for (Reimbursement reimbursement2 : reimbursementList) {
		
		if(! reimbursement2.getKey().equals(reimbursementKey)){
			
			if(reimbursement2.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement2.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")
					|| reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag() != null
					&& reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
				return reimbursement2;
			}
		}
		
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

public Boolean isAnyRodActive(String intimationNumber,Long currentReimbursementKey){
	
	Claim claim = getClaimsByIntimationNumber(intimationNumber);
	
	List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claim.getKey());
	if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
		for (Reimbursement reimbursement : previousRODByClaimKey) {
			if(! reimbursement.getKey().equals(currentReimbursementKey) && reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())
					&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && ! (reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
				|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))) {
					return true;					
			}
		}
	}

	return false;
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
	
//	public List<Reimbursement> getPreviousRODByClaimKey(Long claimKey) {
//		Query query = entityManager
//				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
//		query.setParameter("claimKey", claimKey);
//		List<Reimbursement> resultList = query.getResultList();
//		if (!resultList.isEmpty()) {
//			for (Reimbursement reimbursement : resultList) {
//				entityManager.refresh(reimbursement);
//			}
//		}
//		return resultList;
//	}
	
	
public List<Map<String, Object>> getDBTaskForPreauth(String currentQ){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

		DBCalculationService db = new DBCalculationService();
//		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return taskProcedure;
		} 
		return null;
	}

	public Double getBalanceSI(OMPReimbursement reimbursement,OMPClaim claim, String eventCode)
	{
	DBCalculationService dbCalculationService = new DBCalculationService();
	Double sumInsured = 0d;
	Long policyKey = reimbursement.getClaim().getIntimation().getPolicy().getKey();
	
	if(null != reimbursement.getClaim().getIntimation().getInsured().getInsuredId()){
		
		sumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, eventCode);
	}
	Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey , reimbursement.getClaim().getIntimation().getInsured().getKey() , 
			reimbursement.getClaim().getKey(),reimbursement.getKey(), sumInsured,reimbursement.getClaim().getIntimation().getKey(), eventCode);
	Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
	
	return balanceSI;

}
private String getEmployeeNameById(String initiatorId)
{
	List<EmpSecUser> empDetailList;
	Query findByTransactionKey = entityManager.createNamedQuery(
			"EmpSecUser.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
	try{
		empDetailList =(List<EmpSecUser>) findByTransactionKey.getResultList();
		if(empDetailList != null && !empDetailList.isEmpty()){
			entityManager.refresh(empDetailList.get(0));
		}
		
		String empNameWithId = empDetailList.get(0).getEmpId();
		empNameWithId += (empDetailList.get(0).getUserName() != null ? (" - " + empDetailList.get(0).getUserName()) : "");
		
		return empNameWithId;
	}
	catch(Exception e)
	{
		return null;
	}
						
}

public List<OtherBenefitsTableDto> getBenefitTableValues(NewIntimationDto intimationDto,Double insuredSumInsured,Reimbursement reimbursement){
	
	List<OtherBenefitsTableDto> otherBenefitsClaimedAmount = getOtherBenefitsClaimedAmount(reimbursement.getKey());
	List<Long> benefitKeyList = new ArrayList<Long>();
	
	for (OtherBenefitsTableDto otherBenefitsTableDto : otherBenefitsClaimedAmount) {
		if(otherBenefitsTableDto.getBenefitObjId() != null){
			benefitKeyList.add(otherBenefitsTableDto.getBenefitObjId());
		}
	}
	
//	if(otherBenefitsClaimedAmount != null && ! otherBenefitsClaimedAmount.isEmpty()){
//		return otherBenefitsClaimedAmount;
//	}else{
		List<OtherBenefitsTableDto> listOfBenefits = new ArrayList<OtherBenefitsTableDto>();
		listOfBenefits.addAll(otherBenefitsClaimedAmount);
		DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
		List<OtherBenefitsTableDto> otherBebefitsList = dbCalculationService.getOtherBebefitsList(reimbursement.getDocAcknowLedgement().getClaim().getIntimation().getPolicy().getProduct().getKey(), insuredSumInsured.longValue(), 
				docAcknowLedgement.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) ? 401l : 402l);
		
		if(reimbursement.getDocAcknowLedgement().getOtherBenefitsFlag() != null && reimbursement.getDocAcknowLedgement().getOtherBenefitsFlag().equalsIgnoreCase("Y")){
			for (OtherBenefitsTableDto otherBenefitsTableDto : otherBebefitsList) {
				/*if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.emergencyDomestic)){
					if(reimbursement.getDocAcknowLedgement().getOtherBenefitsFlag() != null && reimbursement.getDocAcknowLedgement().getOtherBenefitsFlag().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
						
					}
				}*/
				
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.emergencyDomestic)){
					if(reimbursement.getDocAcknowLedgement().getEmergencyMedicalEvaluation() != null && reimbursement.getDocAcknowLedgement().getEmergencyMedicalEvaluation().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
					}
				}
				
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.compassionateTravel)){
					if(reimbursement.getDocAcknowLedgement().getCompassionateTravel() != null && reimbursement.getDocAcknowLedgement().getCompassionateTravel().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
					}
				}
				
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.repatriationOfMortal)){
					if(reimbursement.getDocAcknowLedgement().getRepatriationOfMortalRemain() != null && reimbursement.getDocAcknowLedgement().getRepatriationOfMortalRemain().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
					}
				}
				
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.treatementForPreferred)){
					if(reimbursement.getDocAcknowLedgement().getPreferredNetworkHospita() != null && reimbursement.getDocAcknowLedgement().getPreferredNetworkHospita().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
					}
				}
				
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.sharedAccomotation) ||
						otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.SHARED_ACCOMODATION)){
					if(reimbursement.getDocAcknowLedgement().getSharedAccomodation() != null && reimbursement.getDocAcknowLedgement().getSharedAccomodation().equalsIgnoreCase("Y")){
						if(! benefitKeyList.contains(otherBenefitsTableDto.getBenefitObjId())){
							listOfBenefits.add(otherBenefitsTableDto);
						}
					}
				}
			}
			
		}
		
		return listOfBenefits;
	//}
	
	
	
}

public List<OtherBenefitsTableDto> getOtherBenefitsClaimedAmount(Long transactionKey){
	
	List<OtherBenefitsTableDto> resultList = new ArrayList<OtherBenefitsTableDto>();
	try{
		Query findByPreauthKey = entityManager.createNamedQuery(
				"BenefitAmountDetails.findByTransactionKey").setParameter("transactionKey",
						transactionKey);

		List<BenefitAmountDetails> benefitList = (List<BenefitAmountDetails>) findByPreauthKey
				.getResultList();

		if (benefitList != null && !benefitList.isEmpty()) {
			OtherBenefitsTableDto benefitDto = null;
			for (BenefitAmountDetails benefitAmountDetails : benefitList) {
				benefitDto = new OtherBenefitsTableDto(benefitAmountDetails);
		        benefitDto.setEnabled(true);
				resultList.add(benefitDto);
			}				
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return resultList;
}


@TransactionAttribute(TransactionAttributeType.REQUIRED)
public Reimbursement submitPAClaimRequest(PreauthDTO preauthDTO) {
	try {
		log.info("Submit Claim Request ---------------" + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NULL INTIMATION"));
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){	
			preauthDTO.setMedicalCompletedDate(new Timestamp(System
					.currentTimeMillis()));
		}
		
		Reimbursement reimbursement = savePAPreauthValues(preauthDTO, false,SHAConstants.CLAIM_REQUEST);

			

		
		if (reimbursement.getClaim() != null) {
			Claim currentClaim = searchByClaimKey(reimbursement.getClaim()
					.getKey());
			if (currentClaim != null) {
//				currentClaim.setStatus(reimbursement.getStatus());
//				currentClaim.setStage(reimbursement.getStage());
				if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
					currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
				}
				currentClaim.setDataOfAdmission(reimbursement.getDateOfAdmission());
				if(reimbursement.getDateOfDischarge() != null){
					currentClaim.setDataOfDischarge(reimbursement.getDateOfDischarge());
				}
				// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
				if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
					currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
					currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
					currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
				}
				
				
				entityManager.merge(currentClaim);
				entityManager.flush();
				log.info("------CurrentClaim------>"+currentClaim+"<------------");
			}
		}

//		String remarksForClaimRequest = getRemarksForClaimRequest(
//				reimbursement.getStatus().getKey(), preauthDTO,
//				reimbursement);
//
//		if (reimbursement.getStatus().getKey()
//				.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
//			reimbursement.setRejectionRemarks(remarksForClaimRequest);
//		} else if (reimbursement.getStatus().getKey()
//				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
//			reimbursement.setApprovalRemarks(remarksForClaimRequest);
//
//		}
		
		

//		Double provisionAmt = 0d;
//		provisionAmt += reimbursement.getApprovedAmount() != null ? reimbursement
//				.getApprovedAmount() : 0d;
//		if (reimbursement.getDocAcknowLedgement() != null) {
//			if (reimbursement.getDocAcknowLedgement()
//					.getPreHospitalizationClaimedAmount() != null) {
//				provisionAmt += reimbursement.getDocAcknowLedgement()
//						.getPreHospitalizationClaimedAmount();
//			}
//			if (reimbursement.getDocAcknowLedgement()
//					.getPostHospitalizationClaimedAmount() != null) {
//				provisionAmt += reimbursement.getDocAcknowLedgement()
//						.getPostHospitalizationClaimedAmount();
//			}
//		}
//
//		List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement
//				.getKey());
//		if (reimbursementBenefitsByRodKey != null) {
//			for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
//				provisionAmt += reimbursementBenefits
//						.getTotalClaimAmountBills() != null ? reimbursementBenefits
//						.getTotalClaimAmountBills() : 0d;
//
//			}
//		}
//		reimbursement.setCurrentProvisionAmt(provisionAmt);
//		entityManager.merge(reimbursement);
//		entityManager.flush();

		List<MedicalVerificationDTO> medicalVerificationTableDTO = preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getMedicalVerificationTableDTO();
		for (MedicalVerificationDTO medicalVerificationDTO : medicalVerificationTableDTO) {
			ClaimVerification verification = new ClaimVerification();
			verification.setReimbursement(reimbursement);
			verification.setKey(medicalVerificationDTO.getKey());
			verification.setStage(reimbursement.getStage());
			verification.setStatus(reimbursement.getStatus());
			verification.setVerificationTypeId(medicalVerificationDTO
					.getDescriptionId());
			verification.setVerificationType("Medical");
			verification.setVerifiedFlag(medicalVerificationDTO
					.getVerifiedFlag());
			verification.setMedicalRemarks(medicalVerificationDTO
					.getRemarks());

			if (verification.getKey() == null) {
				entityManager.persist(verification);
			} else {
				entityManager.merge(verification);
			}
			log.info("------ClaimVerification------>"+verification+"<------------");
		}
		entityManager.flush();

		List<TreatmentQualityVerificationDTO> treatmentVerificationTableDTO = preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getTreatmentVerificationDTO();
		for (TreatmentQualityVerificationDTO dto : treatmentVerificationTableDTO) {
			ClaimVerification verification = new ClaimVerification();
			verification.setReimbursement(reimbursement);
			verification.setKey(dto.getKey());
			verification.setStage(reimbursement.getStage());
			verification.setStatus(reimbursement.getStatus());
			verification.setVerificationTypeId(dto.getDescriptionId());
			verification.setVerificationType("Treatment");
			verification.setVerifiedFlag(dto.getVerifiedFlag());
			verification.setMedicalRemarks(dto.getRemarks());

			if (verification.getKey() == null) {
				entityManager.persist(verification);
			} else {
				entityManager.merge(verification);
			}
			log.info("------ClaimVerification------>"+verification+"<------------");
		}
		entityManager.flush();

		List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO
				.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
			List<FVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2
					.getFvrGradingDTO();
			FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2
					.getKey());
			for (FVRGradingDTO fvrGradingDTO2 : fvrGradingDTO) {

				switch (Integer.valueOf(String.valueOf(fvrGradingDTO2
						.getKey()))) {
				case 8:
					fvrByKey.setPatientVerified(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 9:
					fvrByKey.setDiagnosisVerfied(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 10:
					fvrByKey.setRoomCategoryVerfied(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 11:
					fvrByKey.setTriggerPointsFocused(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 12:
					fvrByKey.setPedVerified(fvrGradingDTO2.getStatusFlag());
					break;
				case 13:
					fvrByKey.setPatientDischarged(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 14:
					fvrByKey.setPatientNotAdmitted(fvrGradingDTO2
							.getStatusFlag());
					break;
				case 15:
					fvrByKey.setOutstandingFvr(fvrGradingDTO2
							.getStatusFlag());
					break;
				default:
					break;
				}
			}

			entityManager.merge(fvrByKey);
			entityManager.flush();
		}

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
				|| reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			ResidualAmount residualAmt = new ResidualAmount();
			residualAmt.setKey(preauthDTO.getResidualAmountDTO().getKey());
			residualAmt.setTransactionKey(reimbursement.getKey());
			residualAmt.setStage(reimbursement.getStage());
			residualAmt.setStatus(reimbursement.getStatus());
			residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
					.getRemarks());
			residualAmt.setAmountConsideredAmount(preauthDTO
					.getResidualAmountDTO().getAmountConsideredAmount());
			residualAmt.setMinimumAmount(preauthDTO.getResidualAmountDTO()
					.getMinimumAmount());
			residualAmt.setCopayAmount(preauthDTO.getResidualAmountDTO()
					.getCopayAmount());
			residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO()
					.getApprovedAmount());
			residualAmt.setCopayPercentage(preauthDTO
					.getResidualAmountDTO().getCopayPercentage());
			residualAmt.setRemarks(preauthDTO.getResidualAmountDTO()
					.getRemarks());
			residualAmt.setNetAmount(preauthDTO.getResidualAmountDTO()
					.getNetAmount() != null ? preauthDTO
					.getResidualAmountDTO().getNetAmount() : 0d);
			residualAmt.setNetApprovedAmount(preauthDTO.getResidualAmountDTO().getNetApprovedAmount() != null ? preauthDTO.getResidualAmountDTO().getNetApprovedAmount() : 0d);
			
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
			log.info("------ResidualAmount------>"+residualAmt+"<------------");
		}
		
		// below line added for saving the values should be zero if rejection is happened.
		if(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)){
			List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = getReimbursementCalculationDetails(preauthDTO.getKey());
			if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
				saveCalculationValues(preauthDTO, reimbursement, /*preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) ? true :*/ false);
			}
		}
		
		// ADD FSD parrallel processing CR2018296
		String outcome = "";
		Boolean isFvrOrInvesInitiated = false;
		
		if(null != preauthDTO.getClaimDTO().getClaimType() && 
				preauthDTO.getClaimDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE)){
			
			Boolean investigationAvailable = getInvestigationPendingForClaim(preauthDTO.getClaimKey(),SHAConstants.TRANSACTION_FLAG_CASHLESS,preauthDTO);
			if(investigationAvailable){
				preauthDTO.setIsParallelInvFvrQuery(investigationAvailable);
			}
		}
		
		 if(null != preauthDTO.getIsParallelInvFvrQuery() && !(preauthDTO.getIsParallelInvFvrQuery())){
			
			outcome = getPAOutcomeForClaimRequest(reimbursement,
					preauthDTO);
		}
		else
		{
			
			isFvrOrInvesInitiated = true;
			
			outcome = getOutcomeForPAClaimRequestWaitForInput(reimbursement,preauthDTO);
		}
		if(null != preauthDTO.getStatusKey() && !(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
			submitFVRGradingDetail(preauthDTO);
		}
		
		if((preauthDTO.getStatusKey().intValue() == (ReferenceTable.CLAIM_REQUEST_REJECT_STATUS).intValue())){
			updateRejectReason(preauthDTO);
			
		}
		/**
		 * Need to implement DB migration for PA
		 */
		submitClaimRequestTaskToDBProcedure(preauthDTO,false,outcome, reimbursement,false);
//		setBPMOutcome(preauthDTO, false, outcome,reimbursement);
		return reimbursement;
	} catch (Exception e) {
		e.printStackTrace();
		Notification.show(e.getMessage());
		log.error(e.toString());
	}
	return null;
}

public Reimbursement savePAPreauthValues(PreauthDTO preauthDTO,
		Boolean isZonalReview,String screenName) {

	ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//	ZonalMedicalReviewMapper.getAllMapValues();
	Reimbursement reimbursement = reimbursementMapper
			.getReimbursement(preauthDTO);
	reimbursement.setActiveStatus(1l);
	Boolean isCancelROD = ReferenceTable.CANCEL_ROD_KEYS.containsKey(preauthDTO.getStatusKey());

	if (preauthDTO.getStatusKey().equals(
			ReferenceTable.FINANCIAL_REJECT_STATUS)) {
		Status status = getStatusByKey(preauthDTO.getStatusKey());
		String strUserName = preauthDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		reimbursement.setModifiedBy(userNameForDB);
		reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		reimbursement.setStatus(status);
	}
	Claim searchByClaimKey2 = searchByClaimKey(preauthDTO.getClaimKey());
	reimbursement.setClaim(searchByClaimKey2);
	Claim currentClaim = null;
	if (null != preauthDTO.getPreauthDataExtractionDetails()
			&& preauthDTO.getPreauthDataExtractionDetails()
					.getNatureOfLoss() != null) {
		reimbursement.setNatureOfLoss(preauthDTO
				.getPreauthDataExtractionDetails().getNatureOfLoss()
				.getId());
	}

	if (null != preauthDTO.getPreauthDataExtractionDetails()
			&& preauthDTO.getPreauthDataExtractionDetails()
					.getCauseOfLoss() != null) {
		reimbursement.setCauseOfLoss(preauthDTO
				.getPreauthDataExtractionDetails().getCauseOfLoss()
				.getId());
	}

	if (null != preauthDTO.getPreauthDataExtractionDetails()
			&& preauthDTO.getPreauthDataExtractionDetails()
					.getCatastrophicLoss() != null) {
		reimbursement.setCatastrophicLoss(preauthDTO
				.getPreauthDataExtractionDetails()
				.getCatastrophicLoss().getId());
	}
	
	if(preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport() != null){
		System.out.println(String.format("Ventilator Support value in PA Preauth Service method: [%s]", preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport()));
		//String VentilatorSupportFlag = preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport().toString().equalsIgnoreCase("true") ? "Y":"N";
		String VentilatorSupportFlag = preauthDTO.getPreauthDataExtractionDetails().getVentilatorSupport()? "Y":"N";
		reimbursement.setVentilatorSupport(VentilatorSupportFlag);
	}

	if (reimbursement.getClaim() != null) {
		currentClaim = searchByClaimKey(reimbursement.getClaim().getKey());
//		currentClaim.setStatus(reimbursement.getStatus());
//		currentClaim.setStage(reimbursement.getStage());
		currentClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		//IMSSUPPOR-31919
		if(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath()!=null){
			currentClaim.setDeathDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath());
		}
		
		entityManager.merge(currentClaim);
		
	}

	if (preauthDTO.getPreauthPreviousClaimsDetails()
			.getAttachToPreviousClaim() != null) {
		Claim searchByClaimKey = searchByClaimKey(preauthDTO
				.getPreauthPreviousClaimsDetails()
				.getAttachToPreviousClaim().getId());
		searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
		entityManager.merge(searchByClaimKey);
		entityManager.flush();
		log.info("------Claim------>"+searchByClaimKey+"<------------");
	}

	if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
			|| reimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		String processflag = "A";
		if (preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null
				&& preauthDTO.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt() != null
				&& preauthDTO.getPreauthDataExtractionDetails()
						.getTotalApprAmt() > preauthDTO
						.getPreauthMedicalDecisionDetails()
						.getInitialTotalApprovedAmt()) {
			processflag = "R";
		}
		reimbursement.setApprovedAmount(preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getInitialTotalApprovedAmt() != null ? preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getInitialTotalApprovedAmt() : 0d);
	}
	if (preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
		if (preauthDTO.getPreauthDataExtractionDetails()
				.getAutoRestoration().toLowerCase().contains("n/a")) {
			reimbursement.setAutomaticRestoration("O");
		} else {
			reimbursement.setAutomaticRestoration(preauthDTO
					.getPreauthDataExtractionDetails().getAutoRestoration()
					.toLowerCase().contains("not") ? "N" : "Y");
		}
	}

	/**
	 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE during
	 * policy refractoring activity. Below code is added for inserting value
	 * in the insured key column.
	 * */
	// reimbursement.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
	
	if(screenName.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)){
		reimbursement = updateZonalReviewRemarks(preauthDTO, reimbursement);
	}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
		reimbursement = updatePAClaimRequestRemarks(preauthDTO, reimbursement,currentClaim);
		if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) 
				&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& (ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& ((reimbursement.getPatientStatus() != null 
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbursement.getPatientStatus().getKey()) 
										|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbursement.getPatientStatus().getKey())))
					|| (preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() != null && !preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeath() && ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))) { 
			
			if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
					!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
				saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), reimbursement);
				if(preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null
						&& !preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()){
					updateLegalHeir(preauthDTO);
				}
			}					
			else{
					saveLegalHeirAndDocumentDetailsFromFA(preauthDTO);
			}	
		}
		
	}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_BILLING)){
		reimbursement = updateClaimBillingRemarks(preauthDTO, reimbursement,currentClaim);
		if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag()) {
			Double billingApprovedAmount = reimbursement.getBillingApprovedAmount();
			billingApprovedAmount += (preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);
			reimbursement.setBillingApprovedAmount(billingApprovedAmount);
		}
	}else if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL)){
		reimbursement = updateClaimFinancialRemarks(preauthDTO,reimbursement,currentClaim);
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS) && preauthDTO.getIsFirstStepRejection()){
			
			reimbursement.setFinancialApprovedAmount(reimbursement.getBillingApprovedAmount());
			reimbursement.setCurrentProvisionAmt(reimbursement.getBillingApprovedAmount());
		}
		
		if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag()) {
			Double financialApprovedAmount = reimbursement.getFinancialApprovedAmount();
			if(preauthDTO.getShouldDetectPremium()) {
				financialApprovedAmount += (preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);
			}
			
			reimbursement.setFinancialApprovedAmount(financialApprovedAmount);
		}else if(preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && preauthDTO.getHospitalizaionFlag()) {
			Double financialApprovedAmount = reimbursement.getFinancialApprovedAmount();
			if(preauthDTO.getShouldDetectPremium()) {
				financialApprovedAmount += (preauthDTO.getPolicyInstalmentPremiumAmt() != null ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0d);
			}
			reimbursement.setFinancialApprovedAmount(financialApprovedAmount);
		}
	}
	
	String strUserName = preauthDTO.getStrUserName();
	String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
	
	if(isCancelROD) {
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
	}
	if (screenName.equals(SHAConstants.CLAIM_BILLING) || screenName.equals(SHAConstants.CLAIM_FINANCIAL)) {
		if(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getKey() != null){
			
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(reimbursement.getDocAcknowLedgement().getKey());
			docAck.setHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getHospitalizaionFlag()));
			docAck.setPreHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPreHospitalizaionFlag()));
			docAck.setPostHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPostHospitalizaionFlag()));
			docAck.setLumpsumAmountFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getLumpSumAmountFlag()));
			docAck.setPartialHospitalisationFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getPartialHospitalizaionFlag()));
			docAck.setHospitalizationRepeatFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getIsHospitalizationRepeat()));
			docAck.setHospitalCashFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getAddOnBenefitsHospitalCash()));
			docAck.setPatientCareFlag(SHAUtils.convertBooleanToStringFlag(preauthDTO.getAddOnBenefitsPatientCare()));

			entityManager.merge(docAck);
			entityManager.flush();
			reimbursement.setDocAcknowLedgement(docAck);
		}
		
	}
	
	if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
	{			
		reimbursement.setAccountNumber(null);			
	}
	else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))
	{			
		
		reimbursement.setPayableAt(null);
	}
	
	if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && 
			null != preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag()){
		
		reimbursement.setWorkPlace(preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag());
	}
	
	
	if (reimbursement.getKey() != null) {
		
		if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
			if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				log.info("<<<<<<<<<<<<<<<<< UNIQUE POLCIY APPROVED AMOUNT IS ZERO. SO STATUS WILL BE CAHNGED AS SETTLED STATUS >>>>>>>>>>>>>>>>>");
				Status status = new Status();
				status.setKey(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
				reimbursement.setStatus(status);
			}
		} 
		
		reimbursement.setModifiedBy(userNameForDB);
		reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		reimbursement.setPremiumAmt(preauthDTO.getUniquePremiumAmount() != null ? preauthDTO.getUniquePremiumAmount() : 0d);
		
		entityManager.merge(reimbursement);
		
	} else {

		String createdBy = preauthDTO.getStrUserName();
		if (preauthDTO.getStrUserName() != null
				&& preauthDTO.getStrUserName().length() > 15) {
			createdBy = SHAUtils.getTruncateString(
					preauthDTO.getStrUserName(), 15);
		}
		reimbursement.setCreatedBy(createdBy);
		entityManager.persist(reimbursement);

	}
	    entityManager.flush();
	    log.info("------Reimbursement------>"+reimbursement+"<------------");
	    if(reimbursement.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)) {
	    	Status status = new Status();
			status.setKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
			reimbursement.setStatus(status);
	    }
	    
	if(reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("y")) {
		isZonalReview = false;
	}
	
	if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_BILLING)){
		
//		entityManager.refresh(reimbursement);
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
			updateReferToMedicalApproverForBilling(preauthDTO, reimbursement,
					userNameForDB);
		}
		
	}
	
	if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
		if(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)){
			
			String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
					.getApproverReply();
			updateMedicalApproverForClaimRequest(preauthDTO, reimbursement, remarks);
		}
	}
	
	if(screenName.equalsIgnoreCase(SHAConstants.CLAIM_FINANCIAL)){
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)){
			
			String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
					.getReasonForRefering();
			referToMedicalForFinancial(preauthDTO, reimbursement, remarks);
		}
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
			String remarks = preauthDTO.getPreauthMedicalDecisionDetails()
					.getFinancialApproverRemarks();
			saveReferToBilling(preauthDTO, reimbursement, remarks);
		}
	}


	preauthDTO.setKey(reimbursement.getKey());

	preauthDTO.getCoordinatorDetails()
			.setPreauthKey(reimbursement.getKey());
	preauthDTO.getCoordinatorDetails().setIntimationKey(
			reimbursement.getClaim().getIntimation().getKey());
	preauthDTO.getCoordinatorDetails().setPolicyKey(
			reimbursement.getClaim().getIntimation().getPolicy().getKey());

	ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = preauthDTO
			.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
	UpdateHospital updateHospital = reimbursementMapper
			.getUpdateHospital(updateHospitalDetails);
//	updateHospital.setPhoneNumber(updateHospitalDetails.gethospitalph);
	if (updateHospital != null) {
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
			coordinator.setPolicy(reimbursement.getClaim().getIntimation()
					.getPolicy());
			coordinator.setIntimation(reimbursement.getClaim()
					.getIntimation());
			coordinator.setTransactionKey(reimbursement.getKey());
			coordinator.setTransactionFlag("R");
			coordinator.setCreatedBy(userNameForDB);
			
			if (isZonalReview) {
				coordinator.setKey(null);
			}

			if (coordinator.getKey() != null) {
//				entityManager.merge(coordinator);
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
		String specialityName = "";
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			if (specialityDTO.getSpecialityType() != null) {
				specialityName += specialityDTO.getSpecialityType()
						.getValue() + ",";
			}
			Speciality speciality = reimbursementMapper
					.getSpeciality(specialityDTO);
			// speciality.setPreauth(preauth);
			speciality.setClaim(reimbursement.getClaim());
			speciality.setStage(reimbursement.getStage());
			speciality.setStatus(reimbursement.getStatus());
			// if(isZonalReview) {
			// speciality.setKey(null);
			// }

			if (speciality.getKey() != null) {
				entityManager.merge(speciality);
			} else {
				entityManager.persist(speciality);
				specialityDTO.setKey(speciality.getKey());
			}
		}
		entityManager.flush();

		preauthDTO.setSpecialityName(specialityName);
	}

	entityManager.clear();

	Map<Long, String> keyMap = new HashMap<Long, String>();
	keyMap.put(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED,
			"coordinatorreply");
	keyMap.put(ReferenceTable.BILLING_REFER_TO_COORDINATOR, "coordinator");
	keyMap.put(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER, "medical");
	keyMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER,
			"billing");
	keyMap.put(ReferenceTable.FINANCIAL_APPROVE_STATUS,
			"financial");
	keyMap.put(ReferenceTable.BILLING_BENEFITS_APPROVED, "benefits");

//	if (reimbursement.getStatus().getKey()
//			.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
//			|| reimbursement.getStatus().getKey()
//					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
//			|| keyMap.containsKey(reimbursement.getStatus().getKey())) {
		List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = preauthDTO
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
			if (diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {

				DiagnosisDetailsTableDTO diagnosisDetailsDTO = diagnosisProcedureTableDTO
						.getDiagnosisDetailsDTO();
				diagnosisDetailsDTO
						.setAmountConsideredAmount(diagnosisProcedureTableDTO
								.getAmountConsidered() != null ? diagnosisProcedureTableDTO
								.getAmountConsidered().doubleValue() : 0d);
				diagnosisDetailsDTO
						.setNetAmount(diagnosisProcedureTableDTO
								.getNetAmount() != null ? diagnosisProcedureTableDTO
								.getNetAmount().doubleValue() : 0d);
				diagnosisDetailsDTO
						.setMinimumAmount(diagnosisProcedureTableDTO
								.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO
								.getMinimumAmountOfAmtconsideredAndPackAmt()
								.doubleValue()
								: 0d);
				diagnosisDetailsDTO
						.setCopayPercentage(SHAUtils
								.getDoubleValueFromString(diagnosisProcedureTableDTO
										.getCoPayPercentage() != null ? diagnosisProcedureTableDTO
										.getCoPayPercentage().getValue()
										: "0"));
				diagnosisDetailsDTO
						.setCopayAmount(diagnosisProcedureTableDTO
								.getCoPayAmount() != null ? diagnosisProcedureTableDTO
								.getCoPayAmount().doubleValue() : 0d);
				diagnosisDetailsDTO
						.setApprovedAmount(diagnosisProcedureTableDTO
								.getMinimumAmount() != null ? diagnosisProcedureTableDTO
								.getMinimumAmount().doubleValue() : 0d);
				diagnosisDetailsDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
				diagnosisDetailsDTO
						.setApproveRemarks(diagnosisProcedureTableDTO
								.getRemarks());
				// diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.getApprovedAmount()
				// != null ? diagnosisDetailsDTO.getApprovedAmount() : 0d) -
				// (diagnosisDetailsDTO.getOldApprovedAmount() != null ?
				// diagnosisDetailsDTO.getOldApprovedAmount() : 0d ));
				diagnosisDetailsDTO
				.setDiffAmount((diagnosisDetailsDTO
						.getOldApprovedAmount() != null ? diagnosisDetailsDTO
						.getOldApprovedAmount() : 0d)
						- (diagnosisDetailsDTO.getNetApprovedAmount() != null ? diagnosisDetailsDTO
								.getNetApprovedAmount() : 0d));
				if(preauthDTO.getIsDirectToBilling() || preauthDTO.getIsDirectToFinancial()) {
					diagnosisDetailsDTO.setDiffAmount(diagnosisDetailsDTO.getNetApprovedAmount() != null ? 0 - diagnosisDetailsDTO
								.getNetApprovedAmount() : 0d);
				}
				
				diagnosisDetailsDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
				diagnosisDetailsDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
				diagnosisDetailsDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());
				
				
			} else if (diagnosisProcedureTableDTO.getProcedureDTO() != null) {
				ProcedureDTO procedureDTO = diagnosisProcedureTableDTO
						.getProcedureDTO();
				procedureDTO
						.setAmountConsideredAmount(diagnosisProcedureTableDTO
								.getAmountConsidered() != null ? diagnosisProcedureTableDTO
								.getAmountConsidered().doubleValue() : 0d);
				procedureDTO
						.setNetAmount(diagnosisProcedureTableDTO
								.getNetAmount() != null ? diagnosisProcedureTableDTO
								.getNetAmount().doubleValue() : 0d);
				procedureDTO
						.setMinimumAmount(diagnosisProcedureTableDTO
								.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? diagnosisProcedureTableDTO
								.getMinimumAmountOfAmtconsideredAndPackAmt()
								.doubleValue()
								: 0d);
				procedureDTO
						.setCopayPercentage(SHAUtils
								.getDoubleValueFromString(diagnosisProcedureTableDTO
										.getCoPayPercentage() != null ? diagnosisProcedureTableDTO
										.getCoPayPercentage().getValue()
										: "0"));
				procedureDTO
						.setCopayAmount(diagnosisProcedureTableDTO
								.getCoPayAmount() != null ? diagnosisProcedureTableDTO
								.getCoPayAmount().doubleValue() : 0d);
				procedureDTO
						.setApprovedAmount(diagnosisProcedureTableDTO
								.getMinimumAmount() != null ? diagnosisProcedureTableDTO
								.getMinimumAmount().doubleValue() : 0d);
				procedureDTO.setNetApprovedAmount(diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ?  diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d);
				procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO
						.getRemarks());
				// procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount()
				// != null ? procedureDTO.getApprovedAmount() : 0d) -
				// (procedureDTO.getOldApprovedAmount() != null ?
				// procedureDTO.getOldApprovedAmount() : 0d ));
				procedureDTO
				.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO
						.getOldApprovedAmount() : 0d)
						- (procedureDTO.getNetApprovedAmount() != null ? procedureDTO
								.getNetApprovedAmount() : 0d));
				
				if(preauthDTO.getIsDirectToBilling() || preauthDTO.getIsDirectToFinancial()) {
					procedureDTO.setDiffAmount(procedureDTO.getNetApprovedAmount() != null ? 0 - procedureDTO
								.getNetApprovedAmount() : 0d);
				}
				
				procedureDTO.setIsAmbChargeFlag(diagnosisProcedureTableDTO.getIsAmbChargeFlag());
				procedureDTO.setAmbulanceCharge(diagnosisProcedureTableDTO.getAmbulanceCharge());
				procedureDTO.setAmtWithAmbulanceCharge(diagnosisProcedureTableDTO.getAmtWithAmbulanceCharge());
			}
		}
//	}
	List<ProcedureDTO> procedureList = preauthDTO
			.getPreauthMedicalProcessingDetails()
			.getProcedureExclusionCheckTableList();
	if (!procedureList.isEmpty()) {
		for (ProcedureDTO procedureDTO : procedureList) {
			Procedure procedure = reimbursementMapper
					.getProcedure(procedureDTO);
			procedure.setDeleteFlag(isCancelROD ? 0l : 1l);
			procedure.setTransactionKey(reimbursement.getKey());
			procedure.setStage(reimbursement.getStage());
			procedure.setStatus(reimbursement.getStatus());

			if (!reimbursement.getStatus().getKey().equals(
					ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
					&& !reimbursement.getStatus().getKey().equals(
							ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
				if (procedureDTO.getCopay() != null) {
					procedure.setCopayPercentage(Double
							.valueOf(procedureDTO.getCopay().getValue()));
				}

			}

			if (procedureDTO.getDiffAmount() == null
					|| procedureDTO.getDiffAmount() == 0) {
				procedure.setProcessFlag("0");
			} else if (procedureDTO.getOldApprovedAmount() != null
					&& procedureDTO.getNetApprovedAmount() != null
					&& procedureDTO.getOldApprovedAmount() > procedureDTO
							.getNetApprovedAmount()) {
				procedure.setProcessFlag("R");
				procedureDTO
						.setDiffAmount((procedureDTO.getOldApprovedAmount() != null ? procedureDTO
								.getOldApprovedAmount() : 0d)
								- (procedureDTO.getNetApprovedAmount() != null ? procedureDTO
										.getNetApprovedAmount() : 0d));
				procedure.setDiffAmount(procedureDTO.getDiffAmount());
			} else if (procedureDTO.getOldApprovedAmount() != null
					&& procedureDTO.getNetApprovedAmount() != null
					&& procedureDTO.getOldApprovedAmount() < procedureDTO
							.getNetApprovedAmount()) {

				procedure.setProcessFlag("A");
			} else {
				procedure.setProcessFlag("F");
			}
			
			if(preauthDTO.getIsDirectToBilling()) {
				procedure.setProcessFlag("F");
			}
			
			Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());
			
			if (reimbursementByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && (reimbursement.getStatus().getKey().equals(
					ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(
							ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
				procedure.setProcessFlag("F");
			}

			if (isZonalReview) {
				procedure.setKey(null);
			}
			if(reimbursement != null && reimbursement.getStatus() != null && ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey()) ) {
				procedure.setProcessFlag("0");
				procedure.setDiffAmount(0d);
			}
			
			
			if(null == procedure.getProcedureID()){
				procedure.setProcedureID(0l);
			}
			
			if (procedure.getKey() != null) {
				procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				procedure.setModifiedBy(userNameForDB);
				entityManager.merge(procedure);
			} else {
				entityManager.persist(procedure);
				procedureDTO.setKey(procedure.getKey());
			}
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
			pedValidation.setDeleteFlag(isCancelROD ? 0l : 1l);
			pedValidation.setTransactionKey(reimbursement.getKey());
			pedValidation.setIntimation(reimbursement.getClaim()
					.getIntimation());
			pedValidation.setPolicy(reimbursement.getClaim()
					.getIntimation().getPolicy());
			pedValidation.setStage(reimbursement.getStage());
			pedValidation.setStatus(reimbursement.getStatus());
			if (isZonalReview) {
				pedValidation.setKey(null);
			}
			pedValidation.setProcessFlag("0");

			if (pedValidation.getDiffAmount() == null
					|| pedValidation.getDiffAmount() == 0) {
				pedValidation.setProcessFlag("0");
			} else if (pedValidationDTO.getOldApprovedAmount() != null
					&& pedValidationDTO.getNetApprovedAmount() != null
					&& pedValidationDTO.getOldApprovedAmount() > pedValidationDTO
							.getNetApprovedAmount()) {
				pedValidation.setProcessFlag("R");
				pedValidationDTO
						.setDiffAmount((pedValidationDTO
								.getOldApprovedAmount() != null ? pedValidationDTO
								.getOldApprovedAmount() : 0d)
								- (pedValidationDTO.getNetApprovedAmount() != null ? pedValidationDTO
										.getNetApprovedAmount() : 0d));
				pedValidation.setDiffAmount(pedValidationDTO
						.getDiffAmount());
			} else if (pedValidationDTO.getOldApprovedAmount() != null
					&& pedValidationDTO.getNetApprovedAmount() != null
					&& pedValidationDTO.getOldApprovedAmount() < pedValidationDTO
							.getNetApprovedAmount()) {
				pedValidation.setProcessFlag("A");
			} else {
				pedValidation.setProcessFlag("F");
			}
			
			if(preauthDTO.getIsDirectToBilling()) {
				pedValidation.setProcessFlag("F");
			}
			
			Reimbursement reimbursementByKey = getReimbursementByKey(reimbursement.getKey());

			if (reimbursementByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && (reimbursement.getStatus().getKey().equals(
					ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
					|| reimbursement.getStatus().getKey().equals(
							ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
				pedValidation.setProcessFlag("F");
			}
			
			if (!reimbursement.getStatus().getKey().equals(
					ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
					&& !reimbursement.getStatus().getKey().equals(
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
			
			if(reimbursement != null && reimbursement.getStatus() != null && ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey()) ) {
				pedValidation.setProcessFlag("0");
				pedValidation.setDiffAmount(0d);
			}

			if (pedValidation.getKey() != null) {
				pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				pedValidation.setModifiedBy(userNameForDB);
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
					if (isZonalReview) {
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

	if (preauthDTO.getStatusKey().equals(
			ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
			|| preauthDTO.getStatusKey().equals(
					ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
		if (reimbursement.getKey() != null) {
			ReimbursementQuery preAuthQuery = new ReimbursementQuery();
			preAuthQuery.setReimbursement(reimbursement);
			preAuthQuery
					.setQueryRemarks(preauthDTO
							.getPreauthMedicalProcessingDetails()
							.getQueryRemarks());
			entityManager.persist(preAuthQuery);
		}
		entityManager.flush();
		entityManager.clear();
	}
	
	

	//saveBillEntryValues(preauthDTO, reimbursement, isCancelROD);
			/**
			 *
			 * 
			 * isCancelROD is set to false, for enhancement R0254.
			 * R0254 - To retain uploaded documents and bill entry values
			 * even if the rod is cancelled. 
			 * 
			 * **/
			saveBillEntryValues(preauthDTO, reimbursement, isCancelROD);

	List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO
			.getDeletedDiagnosis();

	if (deletedDiagnosis != null && !deletedDiagnosis.isEmpty()) {
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
			PedValidation pedValidation = reimbursementMapper
					.getPedValidation(diagnosisDetailsTableDTO);
			pedValidation.setDeleteFlag(0l);
			pedValidation.setStage(reimbursement.getStage());
			pedValidation.setStatus(reimbursement.getStatus());
			pedValidation.setTransactionKey(reimbursement.getKey());
			if (pedValidation.getKey() != null) {
				pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				pedValidation.setModifiedBy(userNameForDB);
				entityManager.merge(pedValidation);
			}
			entityManager.flush();
		}
	}

	List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
	if (deletedProcedure != null && !deletedProcedure.isEmpty()) {
		for (ProcedureDTO procedureDTO : deletedProcedure) {
			Procedure procedure = reimbursementMapper
					.getProcedure(procedureDTO);
			procedure.setTransactionKey(reimbursement.getKey());
			procedure.setStage(reimbursement.getStage());
			procedure.setStatus(reimbursement.getStatus());
			procedure.setDeleteFlag(0l);
			if (procedure.getKey() != null) {
				procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				procedure.setModifiedBy(userNameForDB);
				entityManager.merge(procedure);
			}
			entityManager.flush();
		}
	}

	return reimbursement;

}

private Reimbursement updatePAClaimRequestRemarks(PreauthDTO preauthDTO,Reimbursement reimbursement,Claim claim){
	
	String remarksForClaimRequest = getRemarksForPAClaimRequest(
			reimbursement.getStatus().getKey(), preauthDTO,
			reimbursement);

	if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
		reimbursement.setRejectionRemarks(remarksForClaimRequest);
		reimbursement.setRejectionRemarks2(preauthDTO.getPreauthMedicalDecisionDetails().getRejectionRemarks2()!=null?preauthDTO.getPreauthMedicalDecisionDetails().getRejectionRemarks2() : "");
		
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		reimbursement.setApprovalRemarks(remarksForClaimRequest);
	} else if (reimbursement
			.getStatus()
			.getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
		reimbursement.setCancellationRemarks(remarksForClaimRequest);
		reimbursement.setCancellationReasonId(preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getCancellationReason().getId() : null);
	}
	
	// Provision Amount Related stuff......
	//Double totalClaimedAmount = getPATotalClaimedAmount(reimbursement);
	if(preauthDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
		Double approvedAmount = reimbursement.getApprovedAmount() != null ? reimbursement
				.getApprovedAmount() : 0d;
		
		
		/*<------------ As per requirement Provision Amount changes has been made by Saravana........ ------------------------------> *//////
		if(reimbursement.getClaim().getClaimType() != null) {
			if((reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y") || (reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("Y")))) {
				if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
					//Double prePostTotalClaimedAmount = getPAPrePostTotalClaimedAmount(reimbursement);
					//approvedAmount += prePostTotalClaimedAmount;
					// Added the below line to prevent the provision amount  to exceed the Balance SI
					Double min = Math.min(preauthDTO.getBalanceSI(), approvedAmount);
					reimbursement.setCurrentProvisionAmt(min);
				} else {
					// Lesser of (Amount claimed or Approved amount )
					//Double prePostTotalClaimedAmount = getPAPrePostTotalClaimedAmount(reimbursement);
					//approvedAmount += prePostTotalClaimedAmount;
					//Double min = Math.min(totalClaimedAmount, approvedAmount);
					 Double revisedProvisionAmt = Math.min(approvedAmount,preauthDTO.getBalanceSI());
					reimbursement.setCurrentProvisionAmt(revisedProvisionAmt);
				}
			} else {
				if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
					// Not Applicable for updating current provision amount
				} else {
					// Lesser of (Amount claimed or Approved amount )
					//Double prePostTotalClaimedAmount = getPAPrePostTotalClaimedAmount(reimbursement);
					//approvedAmount += prePostTotalClaimedAmount;
					//Double min = Math.min(totalClaimedAmount, approvedAmount);
					 Double revisedProvisionAmt = Math.min(approvedAmount,preauthDTO.getBalanceSI());
					reimbursement.setCurrentProvisionAmt(revisedProvisionAmt);
//					reimbursement.setCurrentProvisionAmt(min);
				}
			}
			
		}
		/*<---------------------------- Current  Provision amount updates completed ---------------------------->*/
	}
	
	// No updates for provision if it is cancel ROD....
//	reimbursement = getProivsioningForCancelROD(preauthDTO, reimbursement);
	
	if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) ) {
		//IMSSUPPOR-30317 - CASHLESS ROD2 current provision amt not zero after cancel rod- issue 
		if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
			//reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
			Preauth latestPreauthByClaim = getLatestPreauthByClaim(preauthDTO.getClaimKey());
			reimbursement.setCurrentProvisionAmt(latestPreauthByClaim.getTotalApprovalAmount());
		}else{
			reimbursement.setCurrentProvisionAmt(0d);
			reimbursement.setOtherBenefitApprovedAmt(0d);
		}
		
	}
	
	if(preauthDTO.getClaimDTO().getClaimType() != null && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && ReferenceTable.getCancelRODKeys().containsKey(preauthDTO.getStatusKey()) ) {
		if(reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")) {
			reimbursement.setCurrentProvisionAmt(claim.getProvisionAmount());
		}else{
			reimbursement.setCurrentProvisionAmt(0d);
			reimbursement.setOtherBenefitApprovedAmt(0d);
		}
	} 
	
	

	/**
	 * Added as per prakash suggestion. Based on this flag ,
	 * values are calculated in procedure.
	 * 
	 * */
	Reimbursement reimb = getReimbursementByKey(reimbursement.getKey());
	if(null != reimb)
	{
		reimbursement.setProcessClaimType(reimb.getProcessClaimType());
		if(null != reimbursement.getApprovedAmount() && null != reimbursement.getDocAcknowLedgement().getBenifitClaimedAmount())
		{
			Double approvedAmount = Math.min(reimbursement.getApprovedAmount() , reimbursement.getDocAcknowLedgement().getBenifitClaimedAmount());
			if(null != approvedAmount && null != preauthDTO.getBalanceSI())
			{
				Double revisedProvisionAmt = Math.min(approvedAmount,preauthDTO.getBalanceSI());
				reimbursement.setBenApprovedAmt(revisedProvisionAmt);
			}
		}
	}
	return reimbursement;
	
}

public List<FVRGradingMaster> getFVRGradingBySegment(String segment) {
	Query query = entityManager
			.createNamedQuery("FVRGradingMaster.findBySegment").setParameter("segment", segment.toLowerCase());
	List<FVRGradingMaster> singleResult = query.getResultList();
	if (singleResult != null) {
		for (FVRGradingMaster fvrGradingMaster : singleResult) {
			entityManager.refresh(fvrGradingMaster);
		}

	}

	return singleResult;
}


public List<FVRGradingDetail> getFVRGradingDetails(Long fvrKey) {
	Query query = entityManager
			.createNamedQuery("FVRGradingDetail.findByFvrKey").setParameter("fvrKey", fvrKey);
	List<FVRGradingDetail> singleResult = query.getResultList();
	if (singleResult != null) {
		for (FVRGradingDetail fvrGradingMaster : singleResult) {
			entityManager.refresh(fvrGradingMaster);
		}

	}

	return singleResult;
}

public List<FvrTriggerPoint> getFVRTriggerPoints(Long fvrKey) {
	Query query = entityManager
			.createNamedQuery("FvrTriggerPoint.findByKey").setParameter("fvrKey", fvrKey);
	List<FvrTriggerPoint> singleResult = query.getResultList();
	if (singleResult != null) {
		for (FvrTriggerPoint fvrGradingMaster : singleResult) {
			entityManager.refresh(fvrGradingMaster);
		}

	}

	return singleResult;
}


public void submitFVRGradingDetail(PreauthDTO preauthDTO){
	
	List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO
			.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
	if(fvrGradingDetailsDTO != null && !fvrGradingDetailsDTO.isEmpty()){
		for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
			List<NewFVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2
					.getNewFvrGradingDTO();
			FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2
					.getKey());
			if(fvrGradingDTO != null && !fvrGradingDTO.isEmpty()){
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
								gradingDetail.setSegment(fvrGradingDTO2.getSegment());
								gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentC());
								if(fvrGradingDTO2.getFvrSeqNo() != null){
									gradingDetail.setSeqNo(fvrGradingDTO2.getFvrSeqNo());	
								}
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

public FVRGradingDetail getFVRGradingDetailByKey(Long key) {
	Query query = entityManager
			.createNamedQuery("FVRGradingDetail.findByKey").setParameter("key", key);
	FVRGradingDetail singleResult = (FVRGradingDetail) query.getSingleResult();
	
	return singleResult;
}

public Reimbursement getReimbursementObject(String rodNo) {
	Query query = entityManager
			.createNamedQuery("Reimbursement.findRodByNumber");
	query = query.setParameter("rodNumber", rodNo);
	List<Reimbursement> reimbursementObjectList = query.getResultList();
	if (null != reimbursementObjectList
			&& !reimbursementObjectList.isEmpty()) {
		entityManager.refresh(reimbursementObjectList.get(0));
		return reimbursementObjectList.get(0);
	}
	return null;
}


public Boolean isSettledPaymentAvailable(String rodNumber) {
	Query query = entityManager.createNamedQuery(
			"ClaimPayment.findByRodNoOrderByKey").setParameter("rodNumber",
					rodNumber);
	Boolean isAvailable = false;
	@SuppressWarnings("unchecked")
	List<ClaimPayment> paymentList = query.getResultList();
	if(paymentList != null && !paymentList.isEmpty()) {
		ClaimPayment claimPayment = paymentList.get(0);
		if(claimPayment != null && claimPayment.getStatusId() != null && ReferenceTable.PAYMENT_SETTLED.equals(claimPayment.getStatusId().getKey())) {
			isAvailable = true;
		}
	}
	return isAvailable;
}


public PedValidation getDiagnosisByKey(Long diagnosisKey) {	
	

	List<PedValidation> resultList = new ArrayList<PedValidation>();
	
	Query query = entityManager.createNamedQuery("PedValidation.findByKey");
	query.setParameter("primaryKey", diagnosisKey);
	
	resultList = (List<PedValidation>)query.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return resultList.get(0);
	}
    
	return null;

}

public Procedure getProcedureKey(Long diagnosisKey) {	
	

	List<Procedure> resultList = new ArrayList<Procedure>();
	
	Query query = entityManager.createNamedQuery("Procedure.findByKey");
	query.setParameter("primaryKey", diagnosisKey);
	
	resultList = (List<Procedure>)query.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return resultList.get(0);
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


public Long getAssistedReprodTreatment(Long sectionKey) {
	Query query = entityManager
			.createNamedQuery("ClaimSectionCover.findBySectionKey");
	query.setParameter("sectionKey", sectionKey);
	
	Long value = 0l;
	
	List<ClaimSectionCover> reimbBenefits = query
			.getResultList();
	if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
		
		for (ClaimSectionCover claimSectionCover : reimbBenefits) {
			if(claimSectionCover.getCoverValue().equalsIgnoreCase(SHAConstants.ASSISTED_REPRODUCTION_TREATMENT)){
				value = claimSectionCover.getCoverKey();
				break;
			}
		}
		
	}
	return value;
}


@SuppressWarnings("unchecked")
public List<BenefitAmountDetails> getOtherBenefitsClaimedAmountForValidation(Long claimKey) {
	Query query = entityManager
			.createNamedQuery("BenefitAmountDetails.findByTransactionKey");
	query.setParameter("transactionKey", claimKey);
	List<BenefitAmountDetails> resultList = (List<BenefitAmountDetails>) query.getResultList();
	return resultList;
}

@SuppressWarnings("unchecked")
public Reimbursement getHospitalizationRodForFAApproved(Long claimKey,Long reimbursementKey,String rodNumber) {

	Query query = entityManager
			.createNamedQuery("Reimbursement.getRodAscendingOrder");
	query.setParameter("claimkey", claimKey);

	List<Reimbursement> reimbursementList = (List<Reimbursement>) query
			.getResultList();

	for (Reimbursement reimbursement2 : reimbursementList) {
		
		if(! (reimbursement2.getRodNumber().equalsIgnoreCase(rodNumber))){
			if(reimbursement2.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement2.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement2.getStatus().getKey())){
				if(reimbursement2.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement2.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")){
					return reimbursement2;
				}
			}
		}
		
	}
	
	return null;
}

public Boolean submitFVRGradingDetail(FvrReportGradingPageDto preauthDTO, SearchFvrReportGradingTableDto tableDto){
	
	Boolean isValid = Boolean.FALSE;
	try{
		
		List<FvrGradingDetailsDTO> fvrGradingDetailsDTO = preauthDTO
				.getFvrGradingDTO();
		
		if(fvrGradingDetailsDTO != null && !fvrGradingDetailsDTO.isEmpty()){
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO2 : fvrGradingDetailsDTO) {
				List<NewFVRGradingDTO> fvrGradingDTO = fvrGradingDetailsDTO2
						.getNewFvrGradingDTO();
				FieldVisitRequest fvrByKey = getFVRByKey(fvrGradingDetailsDTO2
						.getKey());
				
			if(fvrGradingDTO != null && ! fvrGradingDTO.isEmpty()){
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
									gradingDetail.setSegment(fvrGradingDTO2.getSegment());
									gradingDetail.setGrading(fvrGradingDTO2.getStatusFlagSegmentC());
									if(fvrGradingDTO2.getFvrSeqNo() != null){
										gradingDetail.setSeqNo(fvrGradingDTO2.getFvrSeqNo());	
									}
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
						gradingDetail.setCreatedBy(SHAUtils.getUserNameForDB(tableDto.getUsername()));
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
							
							/*if(fvrByKey.getStatus() != null && (fvrByKey.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR) || fvrByKey.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED)))
							{*/
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
							gradingDetail.setModifiedBy(SHAUtils.getUserNameForDB(tableDto.getUsername()));
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
			}
				if(fvrGradingDetailsDTO2.getIsFVRReceived() != null && fvrGradingDetailsDTO2.getIsFVRReceived()){
					Status status = new Status();
					status.setKey(ReferenceTable.FVR_GRADING_STATUS);
					fvrByKey.setModifiedBy(SHAUtils.getUserNameForDB(tableDto.getUsername()));
					fvrByKey.setModifiedDate(new Timestamp(System
							.currentTimeMillis()));
					fvrByKey.setStatus(status);
					fvrByKey.setFvrGradingDate(new Timestamp(System.currentTimeMillis()));
					if(preauthDTO.getGradingRemarks() != null){
						fvrByKey.setGradingRmrks(preauthDTO.getGradingRemarks());
					}
					entityManager.merge(fvrByKey);
					entityManager.flush();
				}
				
				isValid = Boolean.TRUE;
			}
		}
		
		
	}catch(Exception e){
		return false;
	}
	return isValid;
}

public void resetReimbursementBenefitAmount(Reimbursement reimbursement){
	
	List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement.getKey());
	if(reimbursementBenefitsByRodKey != null){
		for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
			reimbursementBenefits.setTotalClaimAmountBills(0d);
			reimbursementBenefits.setTotalAmount(0d);
			reimbursementBenefits.setPayableAmount(0d);
			entityManager.merge(reimbursementBenefits);
			entityManager.flush();
		}
	}
	
}

	@SuppressWarnings({ "unchecked", "static-access" })
	public List<BillingInternalRemarksTableDTO> getBillingInternalRemarksTrails(Long intimationKey, Long stageKey) {
		int serialNo = 1;
		String claimType = "";
		SimpleDateFormat fromSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat toSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date date;
		
		// Call named query
		Query query = entityManager.createNamedQuery("InternalInfo.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("stageId", stageKey);
		
		// Mapper used
		List<BillingInternalRemarksTableDTO> resultList = (List<BillingInternalRemarksTableDTO>) query.getResultList();
		List<InternalInfo> internalInfoList = (List<InternalInfo>) query.getResultList();
		resultList = BillingInternalRemarksMapper.getInstance().getBillingInternalRemarksTrials(internalInfoList);
		
		for(BillingInternalRemarksTableDTO tableDtoObj : resultList) {
			// Setting the employee name
			TmpEmployee employeeObj = masterService.getEmployeeName(tableDtoObj.getUserId().toLowerCase());
			tableDtoObj.setUserName(employeeObj.getEmpFirstName());
			
			// Setting the serial number
			tableDtoObj.setSerialNo(serialNo);
			serialNo++;
			
			// Setting the created date with format
			if(tableDtoObj.getCreatedDate() != null) {
				try {
					date = fromSDF.parse(tableDtoObj.getCreatedDate());
					tableDtoObj.setCreatedDate(toSDF.format(date));
				} catch (ParseException e) {
					e.getMessage();
				}
			}
			
			// Setting the claim type
			if(tableDtoObj.getClaimType() != null) {
				if(Long.parseLong(tableDtoObj.getClaimType()) == ReferenceTable.CLAIM_TYPE_REIMBURSEMENT_ID) {
					claimType = ReferenceTable.CLAIM_TYPE_REIMBURSEMENT;
				} else if(Long.parseLong(tableDtoObj.getClaimType()) == ReferenceTable.CLAIM_TYPE_CASHLESS_ID) {
					claimType = ReferenceTable.CLAIM_TYPE_CASHLESS;
				}
			}
			tableDtoObj.setClaimType(claimType);
		}
		
		return resultList;
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<FAInternalRemarksTableDTO> getFAInternalRemarksTrails(Long intimationKey, Long stageKey) {
		int serialNo = 1;
		String claimType = "";
		SimpleDateFormat fromSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat toSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date date;
		
		// Call named query
		Query query = entityManager.createNamedQuery("InternalInfo.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("stageId", stageKey);
		
		// Mapper used
		List<FAInternalRemarksTableDTO> resultList = (List<FAInternalRemarksTableDTO>) query.getResultList();
		List<InternalInfo> internalInfoList = (List<InternalInfo>) query.getResultList();
		resultList = FAInternalRemarksMapper.getInstance().getFAInternalRemarksTrials(internalInfoList);
		
		for(FAInternalRemarksTableDTO tableDtoObj : resultList) {
			// Setting the employee name
			//TmpEmployee employeeObj = masterService.getEmployeeName(tableDtoObj.getUserId().toLowerCase());
			//IMSSUPPOR-37324
			TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(tableDtoObj.getUserId().toLowerCase());
			tableDtoObj.setUserName(employeeObj.getEmpFirstName());
			
			// Setting the serial number
			tableDtoObj.setSerialNo(serialNo);
			serialNo++;
			
			// Setting the created date with format
			if(tableDtoObj.getCreatedDate() != null) {
				try {
					date = fromSDF.parse(tableDtoObj.getCreatedDate());
					tableDtoObj.setCreatedDate(toSDF.format(date));
				} catch (ParseException e) {
					e.getMessage();
				}
			}
			
			// Setting the claim type
			if(tableDtoObj.getClaimType() != null) {
				if(Long.parseLong(tableDtoObj.getClaimType()) == ReferenceTable.CLAIM_TYPE_REIMBURSEMENT_ID) {
					claimType = ReferenceTable.CLAIM_TYPE_REIMBURSEMENT;
				} else if(Long.parseLong(tableDtoObj.getClaimType()) == ReferenceTable.CLAIM_TYPE_CASHLESS_ID) {
					claimType = ReferenceTable.CLAIM_TYPE_CASHLESS;
				}
			}
			tableDtoObj.setClaimType(claimType);
		}
		return resultList;
	}
	
public Boolean isFvrOrInvesInitiated(PreauthDTO preauthDto){
	
	Boolean isFvrInvesInitiated = Boolean.FALSE; 	
	
	Query findReimb = entityManager.createNamedQuery(
			"Reimbursement.findRodByNumber").setParameter("rodNumber",
					preauthDto.getRodNumber());		
	   List<Reimbursement> reimbList = findReimb.getResultList();
	   if(null != reimbList && !reimbList.isEmpty()){
		   
		   Reimbursement reimbObj = reimbList.get(0);
		   
	   if(null != reimbObj ){		
	
	Query findFvr = entityManager.createNamedQuery(
			"FieldVisitRequest.findByRodKeyAndStage").setParameter("rodKey",
					reimbObj.getKey());			
	List<Long> stageList = new ArrayList<Long>();
	stageList.add(ReferenceTable.PREAUTH_STAGE);
	stageList.add(ReferenceTable.ENHANCEMENT_STAGE);
	stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
	findFvr.setParameter("stageList", stageList);
	
	   List<FieldVisitRequest> fvrList = findFvr.getResultList();
	   
		Query findInves = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey",
						reimbObj.getClaim().getKey());
		 List<Investigation> invesList = findInves.getResultList();
	   
	   if(null != fvrList && !fvrList.isEmpty()){ 			 				   
		   
		   for (FieldVisitRequest fieldVisit : fvrList) {
			
			   //CORP_LEVEL_CLOSE added for enable approve button in ZMR - 12-Mar-19
			   if(!(ReferenceTable.FVRCANCELLED.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.SKIPFVR.equals(fieldVisit.getStatus().getKey()))
					   && !(ReferenceTable.FVR_REPLY_RECEIVED.equals(fieldVisit.getStatus().getKey())) &&
					   !(ReferenceTable.FVR_GRADING_STATUS.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.FVR_NOT_REQUIRED.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.CORP_LEVEL_CLOSE.equals(fieldVisit.getStatus().getKey())) ){ 
				   
				   isFvrInvesInitiated = Boolean.TRUE;	
				   break;
			   }	
		}		   
		  		 		
		}
	   
	   if(!isFvrInvesInitiated){
		   
	    if(null != invesList && !invesList.isEmpty()){
		   
		  for (Investigation investigation : invesList) {
			  
			 if(null != investigation.getTransactionFlag() && !SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(investigation.getTransactionFlag()))
			  {
				  if( !(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())) &&
						  !(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey())) && 
						  !(ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigation.getStatus().getKey())) &&
						  !(ReferenceTable.INVESTIGATION_GRADING.equals(investigation.getStatus().getKey()))){
					   
					   isFvrInvesInitiated = Boolean.TRUE;	
					   break;
				   }
			  }
		}
		   
		   
	   } 
	 }
	   }
}
	return isFvrInvesInitiated;
	
}



public Boolean initiateFVR(PreauthDTO bean ,String presenterString){
	
	Intimation objIntimation = getIntimationByNo(bean.getNewIntimationDTO().getIntimationId());
	//SearchClaimRegistrationTableDto searchDto = new SearchClaimRegistrationTableDto();
	return initiateFVR(objIntimation,bean,presenterString);
}
public Boolean initiateFVR(Intimation objIntimation, PreauthDTO bean, String presenterString)
{
	try
	{
		FieldVisitRequest fvrRequest = new FieldVisitRequest();

		Query findByIntimationKey = entityManager
				.createNamedQuery("Claim.findByIntimationKey");
		findByIntimationKey = findByIntimationKey.setParameter(
				"intimationKey", objIntimation.getKey());
//		Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
		Claim claim = (Claim) findByIntimationKey.getSingleResult();
		
		
		Stage objStage = new Stage();
		
		
		
		if(null != presenterString && SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString)){
			
			objStage.setKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		}
		else if(null != presenterString && SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString))
		{
			
			objStage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		
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
		fvrRequest.setCreatedBy(bean.getStrUserName());
		//fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
		fvrRequest.setPolicy(objIntimation.getPolicy());
		fvrRequest.setAllocationTo(value);
		fvrRequest.setActiveStatus(1L);
		fvrRequest.setOfficeCode(objIntimation.getPolicy().getHomeOfficeCode());	
		fvrRequest.setTransactionFlag("R");
		fvrRequest.setTransactionKey(bean.getKey());
		fvrRequest.setStatus(fvrStatus);
		fvrRequest.setStage(objStage);		
		fvrRequest.setFvrTriggerPoints(bean
				.getPreauthMedicalDecisionDetails().getFvrTriggerPoints());
		entityManager.persist(fvrRequest);
		entityManager.flush();
		
		Reimbursement reimbursement = getReimbursementByKey(bean.getKey());
		if(null != reimbursement && !(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
				|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))){
			
			reimbursement.setStage(objStage);
			reimbursement.setStatus(fvrStatus);
			entityManager.merge(reimbursement);
			entityManager.flush();
		}
		
//		this.fvrRequest = fvrRequest;
		
		/***
		 * **
		 * the bewlow code will be commented for the purpose of BPMN to DB Migration
		 */
		
//		callReimbursmentFVRTask(fvrRequest, objIntimation, claim, userName);
		
		if (fvrRequest.getKey() != null) {
			bean.setFvrKey(fvrRequest.getKey());
			
			viewFVRService.saveTriggerPoints(fvrRequest,bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
		}
		
		//submitFVRGradingDetail(bean);
		Map<String, Object> workFlowPayload = new WeakHashMap<String, Object>();
		Map<String, Object> workFlowObj = (Map<String, Object>) bean.getDbOutArray();
		DBCalculationService dbService = new DBCalculationService();
		workFlowPayload.putAll(workFlowObj);
				
		if( !workFlowPayload.isEmpty()){	
			Long wkKey = (Long)workFlowObj.get(SHAConstants.WK_KEY);
			workFlowPayload.put(SHAConstants.FVR_KEY, fvrRequest.getKey());
			workFlowPayload.put(SHAConstants.PAYLOAD_FVR_CPU_CODE,fvrRequest.getFvrCpuId());
			workFlowPayload.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY,wkKey);
			//workFlowPayload.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, SHAConstants.PARALLEL_WAITING_FOR_INPUT);
			workFlowPayload.put(SHAConstants.WK_KEY, 0);
			
			if(null != bean && null != bean.getStageKey() && (bean.getStageKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE))){
				
				workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_ZMR_INITIATE_FVR);
				workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
			}
			else
			{
				workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CLAIM_REQUEST_INTIATE_FVR_STATUS);
				workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
			}
			
			//workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_FVR_PROCESS_INIT_INV);
			
			Object[] outObject = SHAUtils.getRevisedObjArrayForSubmit(workFlowPayload);
			dbService.revisedInitiateTaskProcedure(outObject);	
		}
		
//		workFlowObj.put(SHAConstants.LOB_TYPE,claim.getProcessClaimType());
		
		return true;
	}
	catch (Exception e)
	{
		e.printStackTrace();
		return false;
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

public void submitParallelQuery(PreauthDTO bean){
	
	Query findReimb = entityManager.createNamedQuery(
			"Reimbursement.findByKey").setParameter("primaryKey",
					bean.getKey());	
	
	  List<Reimbursement> reimbList = findReimb.getResultList();
	   if(null != reimbList && !reimbList.isEmpty()){
		   
		   Reimbursement reimbursement = reimbList.get(0);
		   
	   if(null != reimbursement ){	
		   
		   	Status status = new Status();
			Stage stage = new Stage();
			status.setKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			stage.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
			reimbursement.setStatus(status);
			reimbursement.setStage(stage);

			if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
				reimbursement.setPatientStatus(new MastersValue());
				reimbursement.getPatientStatus().setKey(bean.getPreauthDataExtractionDetails().getPatientStatus().getId());
				if(bean.getPreauthDataExtractionDetails().getDeathDate() != null) {
					reimbursement.setDateOfDeath(bean.getPreauthDataExtractionDetails().getDeathDate());
					if(bean.getPreauthDataExtractionDetails().getReasonForDeath() != null)
						reimbursement.setDeathReason(bean.getPreauthDataExtractionDetails().getReasonForDeath());
				}
			}
			ReimbursementQuery query = new ReimbursementQuery();
			query
			.setQueryRemarks(bean.getPreauthMedicalDecisionDetails().getQueryRemarks());
			query.setReimbursement(reimbursement);
			query.setStatus(status);
			query.setStage(stage);
			if(bean.getQueryType() != null){
				MastersValue qryTyp = new MastersValue();
				qryTyp.setKey(bean.getQueryType().getId());
				qryTyp.setValue(bean.getQueryType().getValue());
				query.setQryTyp(qryTyp);
			}
			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
							bean.getStrUserName(), 15);
			}
			query.setCreatedBy(createdBy);
			query.setCreatedDate(new Timestamp(System
						.currentTimeMillis()));
			entityManager.persist(query);
			entityManager.flush();
			log.info("------ReimbursementQuery------>"+query+"<------------");
			bean.setQueryKey(query.getKey());
	
			entityManager.merge(reimbursement);
			entityManager.flush();
			
			Object doctorNameList = bean.getPreauthMedicalDecisionDetails().getDoctorName();
			if(null != doctorNameList ){
				List<String> docList = getListFromMultiSelectComponent(doctorNameList);
				if(null != docList && !docList.isEmpty()){
					for (String docName : docList) {
						OpinionValidation opinionValidation = new OpinionValidation();
						opinionValidation.setIntimationNumber(bean.getNewIntimationDTO().getIntimationId());
						Stage stageByKey = getStageByKey(reimbursement.getStage().getKey());
						opinionValidation.setClaimStage(null != stageByKey ? stageByKey.getStageName() : null);
						opinionValidation.setStage(reimbursement.getStage());
						opinionValidation.setStatus(reimbursement.getStatus());
						opinionValidation.setAssignedDocName(docName);
						opinionValidation.setAssignedDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setCpuCode(Long.valueOf(bean.getNewIntimationDTO().getCpuCode()));
						opinionValidation.setOpinionStatus(ReferenceTable.OPINION_VALIDATION_PENDING_KEY);
						opinionValidation.setOpinionStatusDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setCreatedBy(bean.getStrUserName());
						opinionValidation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setUpdatedRemarks(bean.getPreauthMedicalDecisionDetails().getRemarksFromDeptHead());
						opinionValidation.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
						opinionValidation.setUpdatedBy(bean.getStrUserName());
						if(bean.getPreauthMedicalDecisionDetails().getDoctorContainer() != null){
							BeanItemContainer<SpecialSelectValue> doctorContainer = bean.getPreauthMedicalDecisionDetails().getDoctorContainer();
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
			
		}
	  }
	
	   
	   Map<String, Object> workFlowPayload = new WeakHashMap<String, Object>();
		Map<String, Object> workFlowObj = (Map<String, Object>) bean.getDbOutArray();
		DBCalculationService dbService = new DBCalculationService();
		workFlowPayload.putAll(workFlowObj);
				
		if( !workFlowPayload.isEmpty()){	
			Long wkKey = (Long)workFlowObj.get(SHAConstants.WK_KEY);
			workFlowPayload.put(SHAConstants.QUERY_KEY, bean.getQueryKey());
			
			workFlowPayload.put(SHAConstants.PAYLOAD_QUERY_KEY,bean.getQueryKey()); 
			workFlowPayload.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY,wkKey);
		//	workFlowPayload.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_WAITING_FOR_INPUT);
			workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
			workFlowPayload.put(SHAConstants.WK_KEY, 0);
			
				
			workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CLAIM_REQUEST_QUERY_STATUS);
			}
			Object[] outObject = SHAUtils.getRevisedObjArrayForSubmit(workFlowPayload);
			dbService.revisedInitiateTaskProcedure(outObject);
	   
}

public Boolean isFvrOrInvesOrQueryInitiated(PreauthDTO preauthDto){
	
	Boolean isFvrInvesQueryInitiated = Boolean.FALSE; 	
	
	Query findReimb = entityManager.createNamedQuery(
			"Reimbursement.findByKey").setParameter("primaryKey",
					preauthDto.getKey());		
	   List<Reimbursement> reimbList = findReimb.getResultList();
	   if(null != reimbList && !reimbList.isEmpty()){
		   
		   Reimbursement reimbObj = reimbList.get(0);
		   
	   if(null != reimbObj ){		
	
	Query findFvr = entityManager.createNamedQuery(
			"FieldVisitRequest.findByRodKeyAndStage").setParameter("rodKey",
					reimbObj.getKey());			
	List<Long> stageList = new ArrayList<Long>();
	stageList.add(ReferenceTable.PREAUTH_STAGE);
	stageList.add(ReferenceTable.ENHANCEMENT_STAGE);
	stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
	findFvr.setParameter("stageList", stageList);
	
	   List<FieldVisitRequest> fvrList = findFvr.getResultList();
	   
		Query findInves = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey",
						reimbObj.getClaim().getKey());
		 List<Investigation> invesList = findInves.getResultList();
		 
		 Query findQuery = entityManager.createNamedQuery(
					"ReimbursementQuery.findByReimbursement").setParameter("reimbursementKey",
							reimbObj.getKey());
			 List<ReimbursementQuery> queryList = findQuery.getResultList();
			 
	   
	   if(null != fvrList && !fvrList.isEmpty()){ 			 				   
		   
		   for (FieldVisitRequest fieldVisit : fvrList) {
			   
			   if(null != fieldVisit.getFvrProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(fieldVisit.getFvrProceedWithoutReport()))){
				   
				   preauthDto.setProceedWithoutReport(SHAConstants.YES_FLAG);
			   }
			   if(!(ReferenceTable.FVR_REPLY_RECEIVED.equals(fieldVisit.getStatus().getKey())) &&
					   !(ReferenceTable.FVRCANCELLED.equals(fieldVisit.getStatus().getKey())) && 
					   !(ReferenceTable.SKIPFVR.equals(fieldVisit.getStatus().getKey())) &&
					   !(ReferenceTable.FVR_GRADING_STATUS.equals(fieldVisit.getStatus().getKey())) &&
					   !(ReferenceTable.EXECUTIVE_CLOSED.equals(fieldVisit.getStatus().getKey()))&&
					   !(ReferenceTable.CORPORATE_CLOSED.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.FVR_NOT_REQUIRED.equals(fieldVisit.getStatus().getKey()))){ 
				   
				// preauthDto.setIsFvrPending(Boolean.TRUE);
				   preauthDto.setIsParallelInvFvrQuery(Boolean.TRUE);
				   preauthDto.setParallelStatusKey(fieldVisit.getStatus().getKey());
				 return true;
			   }	
			   
			  
		}		   
		  		 		
		}
	   
	    if(null != invesList && !invesList.isEmpty()){
		   
		  for (Investigation investigation : invesList) {
			  
			  if(null != investigation.getInvsProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(investigation.getInvsProceedWithoutReport()))){
				   
				   preauthDto.setProceedWithoutReport(SHAConstants.YES_FLAG);
			   }
			  
			  if(null != preauthDto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey() && 
					  !(ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(preauthDto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey()))
			  {
				  if( !(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey())) &&
						  !(ReferenceTable.INVESTIGATION_GRADING.equals(investigation.getStatus().getKey())) &&
						  !(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())) &&
						  !(ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigation.getStatus().getKey()))){
					   
					// preauthDto.setIsInvsPending(Boolean.TRUE);
					  preauthDto.setIsParallelInvFvrQuery(Boolean.TRUE);
					  preauthDto.setParallelStatusKey(investigation.getStatus().getKey());
					  return true;
				   }
			  }
			  
		}
		   
		   
	   } 
	 
	   if(null != queryList && !queryList.isEmpty()){
		   
		   for (ReimbursementQuery reimbursementQuery : queryList) {
			   
			   if( !(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS.equals(reimbursementQuery.getStatus().getKey())) &&
					   !(ReferenceTable.PARALLEL_QUERY_CANCELLED.equals(reimbursementQuery.getStatus().getKey())) &&
					   !(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS.equals(reimbursementQuery.getStatus().getKey()))&&
					   !(ReferenceTable.FA_QUERY_REPLY_STATUS.equals(reimbursementQuery.getStatus().getKey())) &&
					   !(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS.equals(reimbursementQuery.getStatus().getKey()))){
				  // preauthDto.setIsQueryPending(Boolean.TRUE);
				   preauthDto.setIsParallelInvFvrQuery(Boolean.TRUE);
				   preauthDto.setParallelStatusKey(reimbursementQuery.getStatus().getKey());
				   return true;
			   }
		}
	   }
	   
	 }
}
	return isFvrInvesQueryInitiated;
	
	
}

/*public void updateParallelProcessFlowDetails(PreauthDTO bean,InvesAndQueryAndFvrParallelFlowTableDTO cancelledFvrInvsOrquery){
	
	Map<String, Object> workFlowPayload = new WeakHashMap<String, Object>();
	
	Map<String, Object> workFlowObj = (Map<String, Object>) cancelledFvrInvsOrquery.getDbOutArray();
	
	workFlowPayload.putAll(workFlowObj);
	
	Long investigationKey = (Long) workFlowPayload.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
	Long queryKey = (Long) workFlowPayload.get(SHAConstants.PAYLOAD_QUERY_KEY);
	Long fvrKey = Long.parseLong(String.valueOf( workFlowPayload.get(SHAConstants.FVR_KEY)));
	
	Investigation investigation = getByInvestigationKey(investigationKey);
	ReimbursementQuery reimbursementQuery = getReimbursementQuery(queryKey);
	FieldVisitRequest fieldVisit = getFieldVisitRequestByKey(fvrKey);
	
	if(null != investigation){
		
		if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
	
			investigation.setInvsProceedWithoutReport(SHAConstants.YES_FLAG);
		}

		if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
	
			investigation.setInvsCancelRequest(SHAConstants.YES_FLAG);
		}
>>>>>>> parallelProcessing_R0279

		if(null != cancelledFvrInvsOrquery.getCancelRemarks()){
	
			investigation.setInvsCancelRemarks(cancelledFvrInvsOrquery.getCancelRemarks());
		}

		entityManager.merge(investigation);
	
	}
	if(null != reimbursementQuery){
		
		if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
			
			reimbursementQuery.setQueryCancelRequest(SHAConstants.YES_FLAG);
		}

		if(null != cancelledFvrInvsOrquery.getCancelRemarks()){
	
			reimbursementQuery.setQueryCancelRemarks(cancelledFvrInvsOrquery.getCancelRemarks());
		}

		entityManager.merge(reimbursementQuery);
	}
	
	if(null != fieldVisit){
		
		if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
			
			fieldVisit.setFvrProceedWithoutReport(SHAConstants.YES_FLAG);
		}
	}
	
	
}*/


public Investigation getByInvestigationKey(Long investigaitonKey){
	Query findAll = entityManager.createNamedQuery(
			"Investigation.findByInvestigationKey").setParameter("investigationKey",
					investigaitonKey);
	List<Investigation> investigationList = (List<Investigation>) findAll
			.getResultList();
	if(!investigationList.isEmpty()){
		return investigationList.get(0);
	}else{
		return null;
	}
	
}

public ReimbursementQuery getReimbursementQuery(Long primaryKey) {

	Query query = entityManager
			.createNamedQuery("ReimbursementQuery.findByKey");
	query = query.setParameter("primaryKey", primaryKey);

	List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query.getResultList();
	if(null != reimbursementQuery && !reimbursementQuery.isEmpty()){
	
		return reimbursementQuery.get(0);
	}
	return null;

}



public List<ReimbursementQuery> getCancelledQueryDetails(Long rodKey){
	
	List<Long> statusList = new ArrayList<Long>();
	statusList.add(ReferenceTable.PARALLEL_QUERY_CANCELLED);
	
	Query query = entityManager.createNamedQuery("ReimbursementQuery.findByRodKeyAndStatus");
	query = query.setParameter("reimbursementKey", rodKey);
	query = query.setParameter("statusList", statusList);
	List<ReimbursementQuery> reimbursementQueryList = query.getResultList();
	return reimbursementQueryList;
}

public List<Investigation> getCancelledInvsDetails(Long rodKey){
	
	List<Long> statusList = new ArrayList<Long>();
	statusList.add(ReferenceTable.PARALLEL_INVES_CANCELLED);
	
	Query query = entityManager.createNamedQuery("Investigation.findByTransactionKeyAndStatus");
	query = query.setParameter("transactionKey", rodKey);
	query = query.setParameter("statusList", statusList);
	List<Investigation> reimbursementQueryList = query.getResultList();
	return reimbursementQueryList;
}
public List<InvesAndQueryAndFvrParallelFlowTableDTO> getCancelledFvrInvsQueryDetails(PreauthDTO bean){
	
	List<InvesAndQueryAndFvrParallelFlowTableDTO> listInvsFvrQueryDTO = new ArrayList<InvesAndQueryAndFvrParallelFlowTableDTO>();
	List<ReimbursementQuery> queryList = getCancelledQueryDetails(bean.getKey());
	List<Investigation> invsList = getCancelledInvsDetails(bean.getKey());
	InvesAndQueryAndFvrParallelFlowTableDTO invsFvrQueryDTO = null;
	if((null != queryList && !queryList.isEmpty()) ){
		
		for (ReimbursementQuery query : queryList) {
			invsFvrQueryDTO = new InvesAndQueryAndFvrParallelFlowTableDTO();
			invsFvrQueryDTO.setType(SHAConstants.PARALLEL_QUERY_TYPE);
			invsFvrQueryDTO.setInitiatedDate(SHAUtils.parseDate(query.getCreatedDate()));
			invsFvrQueryDTO.setRemarks(query.getQueryRemarks());
			invsFvrQueryDTO.setStatus(query.getStatus().getProcessValue());
			invsFvrQueryDTO.setCancelRemarks(query.getQueryCancelRemarks());
			listInvsFvrQueryDTO.add(invsFvrQueryDTO);
		}
	}
	
	if((null != invsList && !invsList.isEmpty()) ){
		
		for (Investigation investigation : invsList) {
			invsFvrQueryDTO = new InvesAndQueryAndFvrParallelFlowTableDTO();
			invsFvrQueryDTO.setType(SHAConstants.PARALLEL_INVESTIGATION_TYPE);
			invsFvrQueryDTO.setInitiatedDate(SHAUtils.parseDate(investigation.getCreatedDate()));
			invsFvrQueryDTO.setRemarks(investigation.getTriggerPoints());
			invsFvrQueryDTO.setStatus(investigation.getStatus().getProcessValue());
			invsFvrQueryDTO.setCancelRemarks(investigation.getInvsCancelRemarks());
			listInvsFvrQueryDTO.add(invsFvrQueryDTO);
		}
		
	}
		
	return listInvsFvrQueryDTO;
}

public String getOutcomeForClaimRequestWaitForInput(Reimbursement reimbursement,
		PreauthDTO bean) {

	String outCome = "";
	if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_APPROVE_STATUS;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) || (ReferenceTable.PAYMENT_SETTLED).equals(reimbursement.getStatus().getKey())) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REJECT_STATUS;
	} else if (reimbursement
			.getStatus()
			.getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS;
	}  else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;
			 if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
				 
				 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
			}
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
		 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;/* outcome for esclate reply to be set.**/
	}  else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
		;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS;
	}else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
	}
	else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
	}	
	else
	{
		outCome = SHAConstants.CLAIM_REQUEST_TO_WAIT_FOR_INPUT_OUTCOME;
		updateStageInformation(reimbursement,bean);
	}
	return outCome;
}

public ReimbursementQuery getReplyNotReceivedQueryDetails(Long rodKey){
	
	List<Long> statusList = new ArrayList<Long>();
	statusList.add(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
	statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
	statusList.add(ReferenceTable.PARALLEL_QUERY_CANCELLED);
	
	List<Long> stageList = new ArrayList<Long>();
	stageList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
	
	
	Query query = entityManager.createNamedQuery("ReimbursementQuery.findByReimbKeyAndStatus");
	query = query.setParameter("reimbursementKey", rodKey);
	query = query.setParameter("statusList", statusList);
	query = query.setParameter("stageList", stageList);
	List<ReimbursementQuery> reimbursementQueryList = query.getResultList();
	if(null != reimbursementQueryList && !reimbursementQueryList.isEmpty()){
		
		return reimbursementQueryList.get(0);
	}
	return null;
}


public Void isFvrOrInvInitiatedinZMR(PreauthDTO preauthDto){
	
	Query findReimb = entityManager.createNamedQuery(
			"Reimbursement.findRodByNumber").setParameter("rodNumber",
					preauthDto.getRodNumber());		
	   List<Reimbursement> reimbList = findReimb.getResultList();
	   if(null != reimbList && !reimbList.isEmpty()){
		   
		   Reimbursement reimbObj = reimbList.get(0);
		   
	   if(null != reimbObj ){		
	
	Query findFvr = entityManager.createNamedQuery(
			"FieldVisitRequest.findByRodKeyAndStage").setParameter("rodKey",
					reimbObj.getKey());			
	List<Long> stageList = new ArrayList<Long>();
	stageList.add(ReferenceTable.PREAUTH_STAGE);
	stageList.add(ReferenceTable.ENHANCEMENT_STAGE);
	stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
	stageList.add(ReferenceTable.FINANCIAL_STAGE);
	findFvr.setParameter("stageList", stageList);
	
	   List<FieldVisitRequest> fvrList = findFvr.getResultList();
	   
		Query findInves = entityManager.createNamedQuery(
				"Investigation.findByTransactionKey").setParameter("transactionKey",
						reimbObj.getKey());
		 List<Investigation> invesList = findInves.getResultList();
		 
		 Query findQuery = entityManager.createNamedQuery(
					"ReimbursementQuery.findByReimbursement").setParameter("reimbursementKey",
							reimbObj.getKey());
			 List<ReimbursementQuery> queryList = findQuery.getResultList();
			 
	   
	   if(null != fvrList && !fvrList.isEmpty()){ 			 				   
		   
		   for (FieldVisitRequest fieldVisit : fvrList) {
			   
			if(null != fieldVisit.getStage().getKey() && (ReferenceTable.ZONAL_REVIEW_STAGE.equals(fieldVisit.getStage().getKey()))){
			   if(!(ReferenceTable.FVR_REPLY_RECEIVED.equals(fieldVisit.getStatus().getKey())) &&
					   !(ReferenceTable.FVRCANCELLED.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.SKIPFVR.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.FVR_NOT_REQUIRED.equals(fieldVisit.getStatus().getKey()))){ 
				   
				   preauthDto.setIsFvrInitiatedInZMR(Boolean.TRUE);
			   }	
			}
			   
			   if(null != fieldVisit.getFvrProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(fieldVisit.getFvrProceedWithoutReport()))){
				   
				   preauthDto.setProceedWithoutReport(SHAConstants.YES_FLAG);
			   }
		}		   
		  		 		
		}
	   
	    if(null != invesList && !invesList.isEmpty()){
		   
		  for (Investigation investigation : invesList) {
			  
			  if(null != investigation.getStage().getKey() && (ReferenceTable.ZONAL_REVIEW_STAGE.equals(investigation.getStage().getKey()))){
				  
			  if( !(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey())) &&
					  !(ReferenceTable.INVESTIGATION_GRADING.equals(investigation.getStatus().getKey())) &&
					  !(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())) &&
					  !(ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigation.getStatus().getKey()))){
				   
				  preauthDto.setIsInvsInitiatedInZMR(Boolean.TRUE);
			  }  
				  if(null != investigation.getInvsProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(investigation.getInvsProceedWithoutReport()))){
					   
					   preauthDto.setProceedWithoutReport(SHAConstants.YES_FLAG);
				   }
			   }
		}
		   
		   
	   } 
	 
	 }
}
	return null;
	
	
}

private String getRemarksForPAClaimRequest(Long statusKey, PreauthDTO bean,
		Reimbursement reimbursement) {
	Status status = new Status();
	Stage stage = new Stage();
	String remarks = "";
	switch (statusKey.intValue()) {
	case 87:
		remarks = bean.getPreauthMedicalDecisionDetails()
				.getApproverReply();
//		updateMedicalApproverForClaimRequest(bean, reimbursement, remarks);
		break;
		// parrallel processing 
	/*case 88:
		remarks = bean.getPreauthMedicalDecisionDetails().getQueryRemarks();
		if (!bean.getIsZonalReviewQuery()) {

			status.setKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			stage.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
			reimbursement.setStatus(status);
//			String strUserName = bean.getStrUserName();
//			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//			reimbursement.setModifiedBy(userNameForDB);
//			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(reimbursement);
//			entityManager.flush();

			ReimbursementQuery reimbursementQuery = new ReimbursementQuery();
			reimbursementQuery.setQueryRemarks(remarks);
			reimbursementQuery.setReimbursement(reimbursement);
			reimbursementQuery.setStatus(status);
			reimbursementQuery.setStage(stage);
			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
						bean.getStrUserName(), 15);
			}
			reimbursementQuery.setCreatedBy(createdBy);
			reimbursementQuery.setCreatedDate(new Timestamp(System
					.currentTimeMillis()));
			entityManager.persist(reimbursementQuery);
			entityManager.flush();
			log.info("------ReimbursementQuery------>"+reimbursementQuery+"<------------");
			bean.setQueryKey(reimbursementQuery.getKey());
		} else {
//			ReimbursementQuery reimbursementQuery = getReimbursementQuery(reimbursement
//					.getKey());
			status.setKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			stage.setKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
			reimbursement.setStatus(status);

			ReimbursementQuery query = new ReimbursementQuery();
			query.setQueryRemarks(remarks);
			query.setReimbursement(reimbursement);
			query.setStatus(status);
			query.setStage(stage);
			String createdBy = bean.getStrUserName();
			if (bean.getStrUserName() != null
					&& bean.getStrUserName().length() > 15) {
				createdBy = SHAUtils.getTruncateString(
							bean.getStrUserName(), 15);
			}
			query.setCreatedBy(createdBy);
			query.setCreatedDate(new Timestamp(System
						.currentTimeMillis()));
			entityManager.persist(query);
			entityManager.flush();
			log.info("------ReimbursementQuery------>"+query+"<------------");
			bean.setQueryKey(query.getKey());
//			}
		}
		break;*/
	case 86:
		remarks = bean.getPreauthMedicalDecisionDetails()
				.getEscalationRemarks();
		if (bean.getPreauthMedicalDecisionDetails()
				.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
			bean.getPreauthMedicalDecisionDetails().setReasonForRefering(remarks);
			createSpecialist(bean, reimbursement, reimbursement.getStatus(), reimbursement.getStage());
		}else{
			createEscalate(bean, reimbursement, status, stage, remarks);
		}
		break;
	case 85:
		remarks = bean.getPreauthMedicalDecisionDetails()
				.getEscalateReply();
		createEscalateReply(bean, reimbursement, status, stage, remarks);
		break;
	case 81:
		createCoordinator(bean, reimbursement);
		break;
	case 83:
		createSpecialist(bean, reimbursement, status, stage);
		break;
/*	case 80:
		createInvestigation(bean, reimbursement, status, stage);
		break;
	case 79:
		createInitiateFVR(bean, reimbursement, status, stage);
		break;*/
	case 93:
		status.setKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
		createReimbursementRejection(bean, reimbursement, status);
		remarks = bean.getPreauthMedicalDecisionDetails()
				.getRejectionRemarks();
		
		break;
	case 78:

		remarks = bean.getPreauthMedicalDecisionDetails()
				.getApprovalRemarks();
		break;
		
	case 148:
		remarks = bean.getPreauthMedicalDecisionDetails()
		.getCancelRemarks();
	break;
	
	case 158:
		status.setKey(ReferenceTable.PAYMENT_SETTLED);
		createReimbursementRejection(bean, reimbursement, status);
		remarks = bean.getPreauthMedicalDecisionDetails()
				.getRejectionRemarks();
		break;		

	default:
		break;
	}
	return remarks;
}

public MultipleUploadDocumentDTO updateWaivedQueryDocumentDetails(MultipleUploadDocumentDTO bean){
	
	ReferenceDocument referenceDocument = new ReferenceDocument();
	referenceDocument.setFileName(SHAUtils.getOnlyStrings(bean.getFileName()));
	
	if(SHAConstants.UPLOAD_INVESTGATION_SCREEN.equalsIgnoreCase(bean.getTransactionName())){
		MastersValue fileType = new MastersValue();
		fileType.setKey(ReferenceTable.INVESTIGATION_DOCUMENT_TYPE_KEY);
		referenceDocument.setFileType(fileType);
	}
	referenceDocument.setDocumentToken(bean.getFileToken());
	referenceDocument.setTransactionKey(bean.getTransactionKey());
	referenceDocument.setTransactionType(bean.getTransactionName());
	referenceDocument.setDeletedFlag("N");
	referenceDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	referenceDocument.setCreatedBy(bean.getUsername());
	referenceDocument.setLetterDate(bean.getUploadLetterDate());
	referenceDocument.setNoOfPages(bean.getNoOfPages());
	entityManager.persist(referenceDocument);
	entityManager.flush();
	
	return bean;
}


public void saveEditDocumentValues(MultipleUploadDocumentDTO dto){
	if(dto.getDocKey() != null){
		ReferenceDocument referenceDocByKey = getReferenceDocByKey(dto.getDocKey());
		referenceDocByKey.setLetterDate(dto.getUploadLetterDate());
		referenceDocByKey.setNoOfPages(dto.getNoOfPages());
		referenceDocByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(referenceDocByKey);
		entityManager.flush();
	}
}

public void saveDeletedDocumentValues(MultipleUploadDocumentDTO dto){
	if(dto.getDocKey() != null){
		ReferenceDocument referenceDocByKey = getReferenceDocByKey(dto.getDocKey());
		if(null != referenceDocByKey){
			referenceDocByKey.setDeletedFlag("Y");
			referenceDocByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(referenceDocByKey);
			entityManager.flush();
		}
	}
}

public void updateActiveStatusInWorkFlow(Long wkKey){
	
	String query = "UPDATE IMS_CLS_SEC_WORK_FLOW SET ACTIVE_FLAG = "+ SHAConstants.N_FLAG + "WHERE WK_KEY ="+ wkKey;
	Query nativeQuery = entityManager.createNativeQuery(query);
	nativeQuery.executeUpdate();
}

public void updateFVRNotRequiredDetails(PreauthDTO bean){
	
	Reimbursement reimbObj = getReimbursementByKey(bean.getKey());
	if(null != reimbObj){
		
		MastersValue fvrNotRequires = new MastersValue(); 
		fvrNotRequires.setKey(bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks().getId());
		fvrNotRequires.setValue(bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks().getValue());		
		reimbObj.setFvrNotRequiredRemarks(fvrNotRequires);
		
		if(null != bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredOthersRemarks()){
			reimbObj.setFvrNotRequiredOthersRemarks(bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredOthersRemarks());
		}
		
		entityManager.merge(reimbObj);
		entityManager.flush();
	}
	
}

private String getPAOutcomeForClaimRequest(Reimbursement reimbursement,
		PreauthDTO bean) {

	String outCome = "";
	if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		//outCome = "APPROVE";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_APPROVE_STATUS;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) || (ReferenceTable.PAYMENT_SETTLED).equals(reimbursement.getStatus().getKey())) {
		//outCome = "REJECT";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REJECT_STATUS;
	} else if (reimbursement
			.getStatus()
			.getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
		//outCome = "FVR";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_INTIATE_FVR_STATUS;
		
		// preauthRequest.setKey(reimbursement.getClaim().getKey());
	} else if (reimbursement
			.getStatus()
			.getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
		//outCome = "COORDINATOR";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_QUERY_STATUS;
	}else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;
		/*if (bean != null
				&& bean.getPreauthMedicalDecisionDetails() != null
				&& bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null
				&& bean.getPreauthMedicalDecisionDetails().getEscalateTo()
						.getId() != null) {
			if (bean.getPreauthMedicalDecisionDetails().getEscalateTo()
					.getId().intValue() == ReferenceTable.RMA1) {
				outCome = "RMA1";
			} else if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.RMA2)) {
				outCome = "2";
			} else if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.RMA3)) {
				outCome = "3";
			}
			else if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.RMA4)) {
				outCome = "4";
			}
			else if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.RMA5)) {
				outCome = "5";
			}
			else if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.RMA6)) {
				outCome = "6";
			}*/
			 if (bean.getPreauthMedicalDecisionDetails()
					.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
//				createSpecialist(bean, reimbursement, reimbursement.getStatus(), reimbursement.getStage());
				//outCome = "SPECIALIST";
				 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
			}
		}
	 else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
		 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;/* outcome for esclate reply to be set.**/
	//	outCome = "ESCALATEREPLY"; //***/
	} else if (reimbursement
			.getStatus()
			.getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
		//outCome = "INVESTIGATION";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
	//	outCome = "SENDREPLY";
		//outCome = ;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
		//outCome = "SPECIALIST";
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
	} else if (reimbursement.getStatus().getKey()
			.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
		outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS;
	}
	return outCome;
}

	public List<AssignedInvestigatiorDetails> getInvestigationCountAlert(Long investigationKey){	
	
		Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByInvestigaitonKey")
				.setParameter("investigationkey", investigationKey);
		List<AssignedInvestigatiorDetails> investigationList = query.getResultList();
			if(null != investigationList && !investigationList.isEmpty()){
				return investigationList;
			}
			return null;
	}
	
//IMSSUPPOR-23596
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

public Boolean isPreviousHospAcknowledgment(Long claimKey, Long ackKey) {
    Boolean isExist = Boolean.FALSE;
    List<DocAcknowledgement> previousAckList = getDocAckListByClaim(claimKey);
    if (previousAckList != null && !previousAckList.isEmpty()) {
            for (DocAcknowledgement docAcknowledgement : previousAckList) {
                    if(ackKey != null && !(ackKey.equals(docAcknowledgement.getKey())) && docAcknowledgement.getStatus() != null && ! docAcknowledgement.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) && (docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y"))) {
                            //FA Query approved and canceled.
                            if(docAcknowledgement.getRodKey()!=null){
                                    Reimbursement reimbursement= getReimbursementbyRod(docAcknowledgement.getRodKey());
                                    if(reimbursement != null && reimbursement.getStatus() != null && !((ReferenceTable.CANCEL_ROD_KEYS).containsKey(reimbursement.getStatus().getKey()))){
                                            isExist = Boolean.TRUE;
                                            break;
                                    }
                            }
                            
                    }
            }
    }
    return isExist;
}

/*//IMSSUPPOR-23596
public Boolean isPreviousHospAcknowledgment(Long claimKey, Long ackKey) {
	Boolean isExist = Boolean.FALSE;
	List<DocAcknowledgement> previousAckList = getDocAckListByClaim(claimKey);
	if (previousAckList != null && !previousAckList.isEmpty()) {
		for (DocAcknowledgement docAcknowledgement : previousAckList) {
			if(ackKey != null && !(ackKey.equals(docAcknowledgement.getKey())) && docAcknowledgement.getStatus() != null && ! docAcknowledgement.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS) && (docAcknowledgement.getHospitalisationFlag() != null && docAcknowledgement.getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y"))) {
				isExist = Boolean.TRUE;
				break;
			}
		}
	}
	return isExist;
}*/

@SuppressWarnings("unchecked")
public Boolean getInvestigationPendingForClaim(Long claimKey,String transactionFlag,PreauthDTO bean) {
	boolean isavailable = false;
	
	Query findAll = entityManager.createNamedQuery(
			"Investigation.findByClaimKeyAndTransactionFlag");
			findAll.setParameter("claimKey", claimKey);
			findAll.setParameter("transactionFlag", transactionFlag);
	List<Investigation> investigationList = (List<Investigation>) findAll
			.getResultList(); 
	if(null != investigationList && !investigationList.isEmpty())
	{
		bean.setIsInvsRaised(Boolean.TRUE);
		if(investigationList.get(0).getStatus() != null && 
				!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigationList.get(0).getStatus().getKey()) &&
				!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigationList.get(0).getStatus().getKey()) &&
				!ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigationList.get(0).getStatus().getKey()) &&
				!ReferenceTable.INVESTIGATION_GRADING.equals(investigationList.get(0).getStatus().getKey())){
			isavailable = true;
			return isavailable; 
		}

	}
	return isavailable;
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

public Boolean getJioPolicyDetails(String policyNumber){
	
	 Query query = entityManager.createNamedQuery("StarJioPolicy.findByPolicyNumber");
	 query = query.setParameter("policyNumber", policyNumber);
	 List<StarJioPolicy> resultList = (List<StarJioPolicy>)query.getResultList();		 
	 if(resultList != null && !resultList.isEmpty()) {
		 return true;
	 } 
			
	 return false;
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

	public List<Reimbursement> getReimbursementList(String rodNo) {
	Query query = entityManager
			.createNamedQuery("Reimbursement.findRodByNumber");
	query = query.setParameter("rodNumber", rodNo);
	List<Reimbursement> reimbursementObjectList = query.getResultList();
	if (null != reimbursementObjectList
			&& !reimbursementObjectList.isEmpty()) {
		return reimbursementObjectList;
	}
	return null;
	}

	public Boolean isSettledPaymentAvailableByRodKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByRodKeyOrderByKey");
		query.setParameter("rodKey", rodKey);
		Boolean isAvailable = false;
		@SuppressWarnings("unchecked")
		List<ClaimPayment> paymentList = query.getResultList();
		if (paymentList != null && !paymentList.isEmpty()) {
			ClaimPayment claimPayment = paymentList.get(0);
			if (claimPayment != null
					&& claimPayment.getStatusId() != null
					&& ReferenceTable.PAYMENT_SETTLED.equals(claimPayment
							.getStatusId().getKey())) {
				isAvailable = true;
			}
		}
		return isAvailable;
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateSublimitValues(PreauthDTO preauthDto,String userName) throws Exception{
		
		List<DiagnosisProcedureTableDTO> tableList = preauthDto.getDiagnosisProcedureDtoList();
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
		List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
		Long sublimitId = 0l;
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : tableList) {
			DiagnosisDetailsTableDTO diagDtls = diagnosisProcedureTableDTO.getDiagnosisDetailsDTO();
			if(diagDtls != null && diagDtls.getSublimitApplicableFlag().equalsIgnoreCase("Y")
					&& diagnosisProcedureTableDTO.getIsDiagnosisSublimitChanged()){
				diagnosisList.add(diagDtls);
			}
			ProcedureDTO procedureDTO = diagnosisProcedureTableDTO.getProcedureDTO();
			if(procedureDTO != null && procedureDTO.getSublimitApplicableFlag().equalsIgnoreCase("Y")
					&& diagnosisProcedureTableDTO.getIsProcedureSublimitChanged()){
				procedureList.add(procedureDTO);
			}
		}
		if(diagnosisList != null && !diagnosisList.isEmpty()){
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				/*PedValidation pedValidation = reimbursementMapper
						.getPedValidation(diagnosisDetailsTableDTO);*/
				PedValidation pedValidation = getDiagnosisByKey(diagnosisDetailsTableDTO.getKey());
				if(pedValidation.getSublimitId() != null && !pedValidation.getSublimitId().equals(diagnosisDetailsTableDTO.getSublimitName().getLimitId())){
					dbCalculationService.reimbursementRollBackProc(preauthDto.getKey(),"R");
				}
				pedValidation.setNetApprovedAmount(diagnosisDetailsTableDTO.getNetAmount());
				pedValidation.setSubLimitApplicable(diagnosisDetailsTableDTO.getSublimitApplicableFlag());
				pedValidation.setSublimitId(diagnosisDetailsTableDTO.getSublimitName().getLimitId());
				sublimitId = diagnosisDetailsTableDTO.getSublimitName().getLimitId();
				pedValidation.setProcessFlag("F");
				pedValidation.setSublimitUpdateRemarks(preauthDto.getSublimitUpdateRemarks());
				pedValidation.setModifiedBy(userName);
				pedValidation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(pedValidation);
				entityManager.flush();
			}
			List<PedValidation> diagnosisDtls = getDiagnosisDetailsByIntimationKey(preauthDto.getIntimationKey());
			if(diagnosisDtls != null){
				for (PedValidation pedValidation : diagnosisDtls) {
					if(sublimitId != 0l) {
						pedValidation.setSublimitId(sublimitId);
					}
				}
			}
		} else if(procedureList != null && !procedureList.isEmpty()){
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = getProcedureKey(procedureDTO.getKey());
				if(procedure.getSublimitNameId() != null && !procedure.getSublimitNameId().equals(procedureDTO.getSublimitName().getLimitId())){
					dbCalculationService.reimbursementRollBackProc(preauthDto.getKey(),"R");
				}
				procedure.setNetApprovedAmount(procedureDTO.getNetAmount());
				procedure.setSubLimitApplicable(procedureDTO.getSublimitApplicableFlag());
				procedure.setSublimitNameId(procedureDTO.getSublimitName().getLimitId());
				procedure.setProcessFlag("F");
//				procedure.setSublimitUpdateRemarks(preauthDto.getSublimitUpdateRemarks());
				procedure.setModifiedBy(userName);
				procedure.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(procedure);
				entityManager.flush();
			}
		}
		
		dbCalculationService.invokeReimbursementAccumulatorProcedure(preauthDto.getKey());
		updateSublimitStageInformation(preauthDto);
		
	}
	
	public void updateSublimitStageInformation(PreauthDTO preauthDTO){
		
		StageInformation stgInformation = new StageInformation();
		Reimbursement reimbursement = getReimbursementByKey(preauthDTO.getKey());
		stgInformation.setIntimation(reimbursement.getClaim().getIntimation());				
		stgInformation.setClaimType(reimbursement.getClaim().getClaimType());
		stgInformation.setStage(reimbursement.getStage());
		Status status = new Status();
		status.setKey(ReferenceTable.SUBLIMIT_UPDATE);
		status.setProcessValue(ReferenceTable.SUBLIMIT_UPDATE_VALUE);
		stgInformation.setStatus(status);
		stgInformation.setClaim(reimbursement.getClaim());
		stgInformation.setReimbursement(null !=reimbursement ? reimbursement :null);
		stgInformation.setStatusRemarks(preauthDTO.getSublimitUpdateRemarks());
		stgInformation.setCreatedBy(preauthDTO.getStrUserName());
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		entityManager.persist(stgInformation);
		entityManager.flush();
	}


	public List<PedValidation> getDiagnosisbyPolicyKey(Long policyKey){

		
		Query query = entityManager.createNamedQuery("PedValidation.findByPolicyKey");
		query.setParameter("policyKey", policyKey);
		
		List<PedValidation> resultList = query.getResultList();
		
		if(resultList != null && !resultList.isEmpty()){
			return resultList;
		}
		
		return null;
		
	}
	
	public List<PedValidation> getDiagnosisDetailsByIntimationKey(Long intimationKey){
	
			
			Query query = entityManager.createNamedQuery("PedValidation.findByIntimationKey");
			query.setParameter("intimationKey", intimationKey);
			
			List<PedValidation> resultDiagList = query.getResultList();
			
			if(resultDiagList != null && !resultDiagList.isEmpty()){
				return resultDiagList;
			}
			
			return null;
			
		}
	


	public Boolean isBatchNotCreated(Long rodKey){
		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByRodKeyOrderByKey");
		query.setParameter("rodKey", rodKey);
		Boolean isAvailable = false;
		@SuppressWarnings("unchecked")
		List<ClaimPayment> paymentList = query.getResultList();
		if (paymentList != null && !paymentList.isEmpty()) {
			ClaimPayment claimPayment = paymentList.get(0);
			if(claimPayment != null){
				if(claimPayment.getBatchNumber() == null){
					isAvailable = true;
				}else{
					isAvailable = false;
				}
			}else{
				isAvailable = true;
			}
		}else{
			isAvailable = true;
		}
		return isAvailable;
	}

	public Void isInvPendingInFA(PreauthDTO preauthDto){
		List<Long> stageList = new ArrayList<Long>();
		
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKey");
				findAll.setParameter("claimKey", preauthDto.getClaimDTO().getKey());
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList(); 
		if(null != investigationList && !investigationList.isEmpty())
		{
			for (Investigation investigation : investigationList) {
				if(null != investigation.getKey()){
					CashlessWorkFlow wkObj = getWorkFlowValues(investigation.getKey());
					if(null != wkObj){
						if(SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(wkObj.getReimbReqBy())){
							if(investigation.getStatus() != null && 
									!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) &&
									!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey()) &&
									!ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigation.getStatus().getKey()) &&
									!ReferenceTable.INVESTIGATION_GRADING.equals(investigation.getStatus().getKey())){
								
								preauthDto.setIsInvsPendingInFA(Boolean.TRUE);
							}
						}
					}
				}
			}
		}
		return null;
		
	}
	
	public CashlessWorkFlow getWorkFlowValues(Long invsKey){
		 Query wkQuery = entityManager.createNamedQuery("CashlessWorkFlow.findByInvsKey");
		    wkQuery.setParameter("investigationKey", invsKey);
		    List<CashlessWorkFlow> wk_list = (List<CashlessWorkFlow>)wkQuery.getResultList();
		    if(wk_list != null && ! wk_list.isEmpty()){
		    	return wk_list.get(0);
		    }
		    return null;
	}
	
	public List<AssignedInvestigatiorDetails> getInvsReviewRemarksDetails(PreauthDTO preauthDto){	
		
		List<AssignedInvestigatiorDetails> invsReviewRemarksTableList = new ArrayList<AssignedInvestigatiorDetails>();
		List<AssignedInvestigatiorDetails> mainInvsReviewRemarksTableList = new ArrayList<AssignedInvestigatiorDetails>();
		
		Query query = entityManager.createNamedQuery("Investigation.findByClaimKey");
		query = query.setParameter("claimKey", preauthDto.getClaimDTO().getKey());
		List<Investigation> invsCompletedList = query.getResultList();
		
		if(null != invsCompletedList && !invsCompletedList.isEmpty()){
			for (Investigation investigation : invsCompletedList) {
				List<AssignedInvestigatiorDetails> assignInvsObj = getInvestionNotReviewList(investigation.getKey());				
				if(null != assignInvsObj && !assignInvsObj.isEmpty()){
					invsReviewRemarksTableList.addAll(assignInvsObj);
				}
			}
		}
		if(invsReviewRemarksTableList != null && !invsReviewRemarksTableList.isEmpty() && invsReviewRemarksTableList.size() > 1){
			List<Long> keysList = new ArrayList<Long>();

			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : invsReviewRemarksTableList) {
				keysList.add(assignedInvestigatiorDetails.getKey());
			}
			Long assignmentKey = 0l;
			if (keysList != null && !keysList.isEmpty()) {
				assignmentKey = Collections.max(keysList);
				AssignedInvestigatiorDetails maxKey = getAssignedInvestigByKey(assignmentKey);
				if(maxKey != null)
				mainInvsReviewRemarksTableList.add(maxKey);
			}
			for(AssignedInvestigatiorDetails assignedInvestigatiorDetails : invsReviewRemarksTableList){
				if(assignedInvestigatiorDetails != null && !assignedInvestigatiorDetails.getKey().equals(assignmentKey)){
					mainInvsReviewRemarksTableList.add(assignedInvestigatiorDetails);
				}
			}
			return mainInvsReviewRemarksTableList;
		}
		return invsReviewRemarksTableList;
	}
	public List<AssignedInvestigatiorDetails> getInvestionNotReviewList(Long investigationKey){	
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.INVESTIGATION_GRADING);
		statusList.add(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		
		Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByStatusAndReportFlag")
				.setParameter("investigationkey", investigationKey);
		query = query.setParameter("statusList", statusList);
		List<AssignedInvestigatiorDetails> investigationList = query.getResultList();
			if(null != investigationList && !investigationList.isEmpty()){
				return investigationList;
			}
			return null;
	}
	
	public List<AssignedInvestigatiorDetails> getRepiedInvestigationDetails(Long claimKey){	
		
		List<AssignedInvestigatiorDetails> invsRepliedRemarksTableList = new ArrayList<AssignedInvestigatiorDetails>();
		
		Query query = entityManager.createNamedQuery("Investigation.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Investigation> invsCompletedList = query.getResultList();
		
		if(null != invsCompletedList && !invsCompletedList.isEmpty()){
			for (Investigation investigation : invsCompletedList) {
				List<AssignedInvestigatiorDetails> assignInvsObj = getInvestigationCountAlert(investigation.getKey());				
				if(null != assignInvsObj && !assignInvsObj.isEmpty()){
					invsRepliedRemarksTableList.addAll(assignInvsObj);
				}
			}
		}
		return invsRepliedRemarksTableList;
	}
	//R1256
	private void updateRejectReason(PreauthDTO preauthDto){
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		RejectReason rejReason = new RejectReason();
		rejReason.setTransactionKey(preauthDto.getKey());
		rejReason.setTransactionType("R");
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
	private DiagnosisDetailsTableDTO  setIcdChapterBlock(DiagnosisDetailsTableDTO pedValidationDto){
		
		if(pedValidationDto != null && pedValidationDto.getIcdCode() != null){
				Long icdCodeKey = pedValidationDto.getIcdCode().getId();
				if(icdCodeKey != null){
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
		}
		return pedValidationDto;
		
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
	
	   //IMSSUPPOR-27444
	   public Boolean isAllRODPaymentRejected(Long claimKey) {
			
			Boolean isAllowed = Boolean.TRUE;
			
			List<Reimbursement> previousRODByClaimKey = getLatestReimbursementByClaimKey(claimKey);
			if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
					for (Reimbursement reimbursement : previousRODByClaimKey) {
						if(reimbursement.getStatus() != null  && ! ReferenceTable.PAYMENT_REJECTED.equals(reimbursement.getStatus().getKey())) {
							isAllowed = Boolean.FALSE;
						} 
					}
				
			}else{
				isAllowed = Boolean.FALSE;
			}
			return isAllowed;
		}
	
	public List<Reimbursement> getLatestReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKeyForReconsideration");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (Reimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
	}
	

	private void savePaymentDetailsForNomineeLegalHeir(PreauthDTO preauthDTO, Reimbursement reimbursement) {
		
		if(preauthDTO.getNewIntimationDTO().getNomineeList() != null && !preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()
				&& !isPANomineeDeceased(preauthDTO)) {
			
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
			ClaimPayment claimPayment = null;
			
			List<NomineeDetailsDto> nomineeList = preauthDTO.getNewIntimationDTO().getNomineeList();
			int count = 1 ;
			for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
				
				if(nomineeDetailsDto.isSelectedNominee()) {
			
				claimPayment = mapper.getClaimPaymentObject(preauthDTO);
				
				claimPayment.setSelectCount(count + "/" + preauthDTO.getNewIntimationDTO().getNomineeSelectCount());
				count++;
				claimPayment.setNomineeKey(nomineeDetailsDto.getProposerNomineeKey());
//				claimPayment.setNomineeName(nomineeDetailsDto.getNomineeName());
//				claimPayment.setNomineeRelationship(nomineeDetailsDto.getNomineeRelationship());
				claimPayment.setNomineeName(nomineeDetailsDto.getAppointeeName() != null  && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeDetailsDto.getAppointeeName() : (nomineeDetailsDto.getNomineeName() != null ? nomineeDetailsDto.getNomineeName() : ""));
				claimPayment.setNomineeRelationship(nomineeDetailsDto.getAppointeeName() != null && nomineeDetailsDto.getAppointeeRelationship() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ?  nomineeDetailsDto.getAppointeeRelationship() : (nomineeDetailsDto.getNomineeRelationship() != null ? nomineeDetailsDto.getNomineeRelationship() :""));
				claimPayment.setAccountPreference(nomineeDetailsDto.getPreference());
				claimPayment.setAccountType(nomineeDetailsDto.getAccType());
				claimPayment.setIfscCode(nomineeDetailsDto.getIfscCode());
				claimPayment.setAccountNumber(nomineeDetailsDto.getAccNumber());
				claimPayment.setBranchName(nomineeDetailsDto.getBankBranchName());
				//changed as per Satish Sir suggestion. No change in Payee name for Premia
				claimPayment.setPayeeName(nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeDetailsDto.getAppointeeName() : (nomineeDetailsDto.getNomineeName() != null ? nomineeDetailsDto.getNomineeName() : ""));
				claimPayment.setPayeeRelationship(nomineeDetailsDto.getAppointeeName() != null  && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeDetailsDto.getAppointeeName() : (nomineeDetailsDto.getNomineeName() != null ? nomineeDetailsDto.getNomineeName() : ""));
				
				if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {
					claimPayment.setPayeeName("");
					claimPayment.setPayeeRelationship("");
				}
				
				//claimPayment.setPayeeName(nomineeDetailsDto.getNameAsPerBankAc() != null ? nomineeDetailsDto.getNameAsPerBankAc() : nomineeDetailsDto.getNomineeName());
				claimPayment.setMicrCode(nomineeDetailsDto.getMicrCode());
				//added for bancs policy - 24-01-2020
				if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {
					
					claimPayment.setSourceRiskId(preauthDTO.getNewIntimationDTO().getInsuredPatient().getSourceRiskId());
				}
				claimPayment.setPanNumber(nomineeDetailsDto.getPanNumber());
				claimPayment.setHospitalCode(preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
//				claimPayment.setemailId(nomineeDetailsDto.getEmailId());
				claimPayment.setProposerCode(preauthDTO.getNewIntimationDTO().getPolicy().getProposerCode());
				claimPayment.setProposerName(preauthDTO.getNewIntimationDTO().getPolicy().getProposerFirstName());
				claimPayment.setBankName(nomineeDetailsDto.getBankName());
//				claimPayment.setPayModeChangeReason(nomineeDetailsDto.getpayModeChangeReason());
				
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(nomineeDetailsDto.getPaymentModeId()))
				{
					claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					claimPayment.setAccountNumber(null);
					claimPayment.setBankCode(null);
					claimPayment.setIfscCode(null);
					claimPayment.setBankName(null);
					claimPayment.setBranchName(null);
					claimPayment.setPayableAt(nomineeDetailsDto.getPayableAt());
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(nomineeDetailsDto.getPaymentModeId()))
				{
					
					claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
					claimPayment.setPayableAt(null);
				}
				if(nomineeDetailsDto.getNomineePercent() != null && !nomineeDetailsDto.getNomineePercent().isEmpty()){
					claimPayment.setApprovedAmount( (reimbursement.getFinancialApprovedAmount() * Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0])) / 100d );
					claimPayment.setTotalApprovedAmount(claimPayment.getApprovedAmount());
				}
				
				savePaymentDetails(claimPayment, preauthDTO, reimbursement);
			}
		}	
		
		}
		else if((preauthDTO.getNewIntimationDTO().getNomineeList() == null 
				|| preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty())
				&& preauthDTO.getLegalHeirDTOList() != null
				&& !preauthDTO.getLegalHeirDTOList().isEmpty()) {
			
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
			ClaimPayment claimPayment = null;
			List<LegalHeir> legalList = getListOfLegalHeirByTransactionKey(reimbursement.getKey());
			List<LegalHeirDTO> legalHeirDtoList = new ArrayList<LegalHeirDTO>();
			LegalHeirDTO legalHeirDTO1 = null;
			for (LegalHeir legalHeir : legalList) {
				legalHeirDTO1 = new LegalHeirDTO(legalHeir);
				legalHeirDtoList.add(legalHeirDTO1);
			}
			//List<LegalHeirDTO> legalHeirDtoList = preauthDTO.getLegalHeirDTOList();
			for (LegalHeirDTO legalHeirDTO : legalHeirDtoList) {
				claimPayment = mapper.getClaimPaymentObject(preauthDTO);
				claimPayment.setSelectCount((legalHeirDtoList.indexOf(legalHeirDTO)+1) + "/"+legalHeirDtoList.size());
				/**
			     * added(LegalHeirKey, Address,Pincode )in 11th aug 2020 for bancs legal heir case bugs fixing
			     */
				claimPayment.setLegalHeirKey(legalHeirDTO.getLegalHeirKey());
				claimPayment.setAddressOne(legalHeirDTO.getAddress());
				claimPayment.setPinCode(legalHeirDTO.getPincode());
				
				claimPayment.setLegalHeirName(legalHeirDTO.getHeirName());
				claimPayment.setLegalHeirRelationship(legalHeirDTO.getRelationship() != null ? legalHeirDTO.getRelationship().getValue() : "");
				// Below Condition for Account Preference mandatory for Bancs
				if(legalHeirDTO.getAccountPreference() != null && legalHeirDTO.getAccountPreference().getValue() != null){
					claimPayment.setAccountPreference(legalHeirDTO.getAccountPreference().getValue());
				}
				if(legalHeirDTO.getAccountType() != null){
					claimPayment.setAccountType(legalHeirDTO.getAccountType().getValue());
				}
				claimPayment.setIfscCode(legalHeirDTO.getIfscCode());
				claimPayment.setAccountNumber(legalHeirDTO.getAccountNo() != null && !legalHeirDTO.getAccountNo().isEmpty() ? legalHeirDTO.getAccountNo().toString() : "");
				claimPayment.setPayeeName(legalHeirDTO.getBeneficiaryName());
//				claimPayment.setSourceRiskId(legalHeirDTO.getS);
				//added for bancs policy - 24-01-2020
				if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {
					
					claimPayment.setSourceRiskId(preauthDTO.getNewIntimationDTO().getInsuredPatient().getSourceRiskId());
				}
				claimPayment.setBankName(legalHeirDTO.getBankName());
				claimPayment.setBranchName(legalHeirDTO.getBankBranchName() );
//				claimPayment.setPanNumber(legalHeirDTO.getPanNumber());
				claimPayment.setHospitalCode(preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
//				claimPayment.setemailId(emailId);
				claimPayment.setProposerCode(preauthDTO.getNewIntimationDTO().getPolicy().getProposerCode());
				claimPayment.setProposerName(preauthDTO.getNewIntimationDTO().getPolicy().getProposerFirstName());
//				claimPayment.setPayModeChangeReason(legalHeirDTO.getPaymodeChangeReason());
				if((ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(legalHeirDTO.getPaymentModeId()))
				{
					claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					claimPayment.setAccountNumber(null);
					claimPayment.setBankCode(null);
					claimPayment.setIfscCode(null);
					claimPayment.setBankName(null);
					claimPayment.setBranchName(null);
					claimPayment.setPayableAt(legalHeirDTO.getPayableAt());
				}
				else if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(legalHeirDTO.getPaymentModeId()))
				{
					claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
					claimPayment.setPayableAt(null);
				}
				
				/*if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getPolicySource())) {*/
					if(legalHeirDTO.getSharePercentage() != null){
						claimPayment.setApprovedAmount( (reimbursement.getFinancialApprovedAmount() * legalHeirDTO.getSharePercentage()) / 100d);
					}
				//}
				/*else {   //changed as per Satish Sir suggestion no Change For Premia Policy 
					claimPayment.setApprovedAmount(reimbursement.getFinancialApprovedAmount());
				}*/
				savePaymentDetails(claimPayment, preauthDTO, reimbursement);
			
			}
		}
		
	}
	
	private void savePaymentDetails(ClaimPayment claimPayment, PreauthDTO preauthDTO, Reimbursement reimbursement){
		
		if(reimbursement.getDateOfDeath() != null)
		{
			claimPayment.setDeathDate(reimbursement.getDateOfDeath());
		}
		
		if(reimbursement.getProcessClaimType() == null){
			claimPayment.setProcessClaimType("H");
		}else {
			claimPayment.setProcessClaimType(reimbursement.getProcessClaimType());
		}
		//added for jira IMSSUPPOR-27488 BSI issue
		if(reimbursement.getBenefitsId() != null){
			claimPayment.setBenefitsId(reimbursement.getBenefitsId().getKey());
		}
		if(reimbursement.getBenApprovedAmt() != null){
			claimPayment.setPaBenefitApproveAmount(reimbursement.getBenApprovedAmt());
		}
		if(reimbursement.getUnNamedKey() != null){
			claimPayment.setUnNamedKey(reimbursement.getUnNamedKey());
		}
		
		claimPayment.setBenefitsApprovedAmt(reimbursement.getOtherBenefitApprovedAmt());
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(reimbursement.getCorporateUtilizedAmt() != null && reimbursement.getCorporateUtilizedAmt() >0){
			claimPayment.setCorporateBufferLimit(reimbursement.getCorporateUtilizedAmt() != null ? reimbursement.getCorporateUtilizedAmt().doubleValue() : 0d);
			}
			else if(reimbursement.getWintageBufferUtilAmt() != null && reimbursement.getWintageBufferUtilAmt() >0){
				claimPayment.setCorporateBufferLimit(reimbursement.getWintageBufferUtilAmt().doubleValue());
			}
			else if(reimbursement.getNacBufferUtilAmt() != null && reimbursement.getNacBufferUtilAmt() >0){
				claimPayment.setCorporateBufferLimit(reimbursement.getNacBufferUtilAmt().doubleValue());
			}else{
				claimPayment.setCorporateBufferLimit(0d);
			}
			}
		
		claimPayment.setInstallmentDeductionFlag(SHAConstants.N_FLAG);
		claimPayment.setPremiaWSFlag(SHAConstants.YES_FLAG);
		claimPayment.setTotalApprovedAmount(claimPayment.getApprovedAmount());
		if((preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY))) {
			if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				claimPayment.setPremiumAmt(reimbursement.getPremiumAmt());
				Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
				claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
				claimPayment.setInstallmentDeductionFlag(SHAConstants.YES_FLAG);
				claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
			} 
		}else if((preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG))) {
			if(preauthDTO.getConsolidatedAmtDTO().getAmountPayableAfterPremium() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				claimPayment.setPremiumAmt(reimbursement.getPremiumAmt());
				Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
				claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
				claimPayment.setInstallmentDeductionFlag(SHAConstants.YES_FLAG);
				claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
			} 
		}
		//claimPayment.setRodkey(reimbursement);
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//			claimPayment.setApprovedAmount(preauthDTO.getRevisedProvisionAmount());
			claimPayment.setPremiumAmt(preauthDTO.getShouldDetectPremium() ? preauthDTO.getUniquePremiumAmount() : 0);
			Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
			claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
			claimPayment.setInstallmentDeductionFlag(preauthDTO.getShouldDetectPremium() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
			claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
		}else if(preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && preauthDTO.getHospitalizaionFlag() && preauthDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//			claimPayment.setApprovedAmount(preauthDTO.getRevisedProvisionAmount());
			claimPayment.setPremiumAmt(preauthDTO.getShouldDetectPremium() ? preauthDTO.getPolicyInstalmentPremiumAmt() : 0);
			Double balanceAmt = claimPayment.getApprovedAmount() - claimPayment.getPremiumAmt();
			claimPayment.setTotalApprovedAmount(balanceAmt < 0 ? 0 : balanceAmt);
			claimPayment.setInstallmentDeductionFlag(preauthDTO.getShouldDetectPremium() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
			claimPayment.setPremiaWSFlag(SHAConstants.N_FLAG);
		}
		
//		claimPayment.setReasonForChange(preauthDTO.getPreauthDataExtractionDetails().getReasonForChange());
		
		if(preauthDTO.getNewIntimationDTO().getHospitalDto() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getKey() != null){
			MasHospitals hospitalById = getMasHospitalById(preauthDTO.getNewIntimationDTO().getHospitalDto().getKey());
			claimPayment.setHospital(hospitalById);
		}
		
		if(null != preauthDTO && null != preauthDTO.getNewIntimationDTO().getCpuCode())
		{  
			claimPayment.setCpuCode(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode()));
		}
		
		claimPayment.setFaApprovalDate(new Timestamp(System.currentTimeMillis()));
		
		claimPayment.setCreatedBy(preauthDTO.getStrUserName());
		claimPayment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		DocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
		
		if(null != docAck.getDocumentReceivedFromId())//.equals(ReferenceTable.RECEIVED_FROM_INSURED))
		{
			claimPayment.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
		}
		
		if(("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalisationFlag())
				&& ("N").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getPreHospitalisationFlag())
				&& ("N").equalsIgnoreCase(docAck.getPostHospitalisationFlag()) && ("N").equalsIgnoreCase(docAck.getHospitalCashFlag())
				&& ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
				)
		{
			claimPayment.setLumpsumFlag("Y");
		}
			
			
		claimPayment.setPaymentCpuCode(Long.parseLong(preauthDTO.getNewIntimationDTO().getCpuCode()));
		
		if(null != docAck && null != docAck.getDocumentReceivedFromId() 
				&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) && 
				reimbursement.getClaim() != null && reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
			/**
			 * As per Sathish Sir comments, following lines are commented.
			 */
//				(SHAConstants.YES_FLAG).equalsIgnoreCase(docAck.getHospitalisationFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPartialHospitalisationFlag())
//				&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPreHospitalisationFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPostHospitalisationFlag())
//				&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getHospitalizationRepeatFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getHospitalCashFlag())
//				&& (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getPatientCareFlag()) && (SHAConstants.N_FLAG).equalsIgnoreCase(docAck.getLumpsumAmountFlag())
//				)
		{
			claimPayment.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
		}
		else
		{
			claimPayment.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
			
			Boolean jioPolicyDetails = false;
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				jioPolicyDetails = getJioPolicyDetails(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
			}
			if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && !jioPolicyDetails){
				OrganaizationUnit masBranchOffice = getInsuredOfficeNameByDivisionCode(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());
				if(masBranchOffice != null && masBranchOffice.getCpuCode() != null){
					claimPayment.setPaymentCpuCode(Long.valueOf(masBranchOffice.getCpuCode()));
				}
			}
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
		
		/**
		 * Below code added for saving only benefits approved 
		 * amt in claim payment table. This is done, since prakash
		 * had asked to save it for BSI calculation .
		 * */
		Double benefitsApprovedAmt = 0d;
		if(null != preauthDTO.getPatientCareAmt())
			benefitsApprovedAmt += preauthDTO.getPatientCareAmt();
		if(null !=  preauthDTO.getHospitalCashAmt())
			benefitsApprovedAmt += preauthDTO.getHospitalCashAmt();
		claimPayment.setBenefitsApprovedAmt(benefitsApprovedAmt);
		
		if(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(null != preauthDTO.getPatientCareAmt()){
				claimPayment.setAddOnCoversApprovedAmount(preauthDTO.getHospitalCashAmt());
			}
			
			if(null !=  preauthDTO.getHospitalCashAmt()){
				claimPayment.setOptionalApprovedAmount(preauthDTO.getHospitalCashAmt());
			}
		}
		
		if(null != reimbursement && null != reimbursement.getDocAcknowLedgement())
		{
			claimPayment.setLastAckDate(reimbursement.getDocAcknowLedgement().getDocumentReceivedDate());
			claimPayment.setReconisderFlag(reimbursement.getReconsiderationRequest() != null ? reimbursement.getReconsiderationRequest() : "N");
			if(null != reimbursement.getReconsiderationRequest() && ("Y").equalsIgnoreCase(reimbursement.getReconsiderationRequest()))
				{
					String rodNo = reimbursement.getRodNumber();
					/**
					 * Only if the payment cancellation is Yes, the last ack date will be
					 * overrirdden with the least settled rod for the given rod number.
					 * If no, then the lastest ack date of the rod will be mapped. This is done
					 * already in above code. Hence else if is not required.
					 * */
					if(null != reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() && ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getPaymentCancellationFlag()))
					{
						ClaimPayment objClaimPayment = getSettledClaimPaymentByRODNo(rodNo);
						if(null != objClaimPayment)
						{
							claimPayment.setLastAckDate(objClaimPayment.getLastAckDate());
						}
						else
						{									
							DocAcknowledgement documentAck = getDocumentDetailsByRodKey(reimbursement.getKey());
							if(null != documentAck)
							{
								claimPayment.setLastAckDate(documentAck.getDocumentReceivedDate());
							}
						}
					}
				}
					
		}
		
		if(null != claimPayment.getFaApprovalDate())
		{
			Calendar faDate = Calendar.getInstance();
		      faDate.setTime(claimPayment.getFaApprovalDate());
		      String irdaPlusDays = BPMClientContext.getFAPlusDays();
		      faDate.add(Calendar.DATE, Integer.valueOf(irdaPlusDays));
		      Date faApproveDate = faDate.getTime();
		      faDate.setTimeInMillis(faApproveDate.getTime());
		      long dateDiff = SHAUtils.getDaysBetweenDate(faApproveDate,claimPayment.getLastAckDate());
		      claimPayment.setDelayDays(dateDiff);
		      
		      BPMClientContext bpmClientContext = new BPMClientContext();
		      String irdaTatDays = bpmClientContext.getIRDATATDays();
		      if(null != irdaTatDays && !irdaTatDays.isEmpty() && null != claimPayment.getDelayDays())
		      {
		    	 long exceedsNoOfDays = claimPayment.getDelayDays() - Long.parseLong(irdaTatDays);
		    	 claimPayment.setAllowedDelayDays(exceedsNoOfDays);
		      }
		}
		
		if(null != reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGMCProductList().containsKey(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
			
			List<String> employeeName = dbCalculationService.getGMCEmployeeName(reimbursement.getClaim().getIntimation().getKey());
			if(null != employeeName && !employeeName.isEmpty()){
				claimPayment.setGmcEmployeeName(employeeName.get(1));
			}
		}
		
		claimPayment.setProductName(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getValue());

		Boolean canCreatePayment = true;
		if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d)) {
			canCreatePayment = false;
		}//cr2019184 
		else if((preauthDTO.getPolicyInstalmentFlag() != null && preauthDTO.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) && (preauthDTO.getUniqueDeductedAmount() != null && preauthDTO.getUniqueDeductedAmount() <= 0d) && preauthDTO.getIsPremiumInstAmtEql() != null && preauthDTO.getIsPremiumInstAmtEql()) {
			canCreatePayment = false;
		}
		
		
		if(!canCreatePayment) {
			log.info("???????<<<<<<< AS PER 'BA'TEAM UNIQUE POLICY APPROVED AMOUNT IS ZERO .. SO CLAIM PAYMENT IS SETTED AS PAYMENT STATUS......>>>>>>???????");
			status.setKey(ReferenceTable.PAYMENT_SETTLED);
			claimPayment.setLotNumber("PREMIUM_DEDUCTION");
			claimPayment.setBatchNumber("PREMIUM_DEDUCTION");
			claimPayment.setStatusId(status);
			claimPayment.setPaymentDate(new Date());
		}
		
		if(reimbursement.getClaim().getIntimation().getCpuCode() != null && reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode() != null){
			OrganaizationUnit branchCode = getBranchCode(reimbursement.getClaim().getIntimation().getCpuCode().getCpuCode().toString());
			if(branchCode != null){
				if(null != docAck && null != docAck.getDocumentReceivedFromId() 
						&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					Hospitals hospitalById = getHospitalById(reimbursement.getClaim().getIntimation().getHospital());
					String emailId2 = branchCode.getEmailId2();
					if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
						String val = emailId2.replaceAll("reimb.delhi@starhealth.in", "paayas@starhealth.in");
						emailId2 = val;
					}
					if(emailId2 != null && hospitalById != null && hospitalById.getEmailId() != null){
						emailId2 = hospitalById.getEmailId()+","+emailId2;
					}else if(hospitalById != null && hospitalById.getEmailId() != null){
						emailId2 = hospitalById.getEmailId();
					}
					claimPayment.setZonalMailId(emailId2);
				}
				
			}else{
				/**
				 * As per sathis sir, For payaas policy, default email id is set to be payment email
				 */
				if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
					Hospitals hospitalById = getHospitalById(reimbursement.getClaim().getIntimation().getHospital());
					String emailId2 = "paayas@starhealth.in";
					
					if(emailId2 != null && hospitalById != null && hospitalById.getEmailId() != null){
						emailId2 = hospitalById.getEmailId()+","+emailId2;
					}else if(hospitalById != null && hospitalById.getEmailId() != null){
						emailId2 = hospitalById.getEmailId();
					}
					claimPayment.setZonalMailId(emailId2);
				}
			}
		}
		
		try{
			
			if(null != docAck && null != docAck.getDocumentReceivedFromId() 
					&& docAck.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			
			if(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode() != null){
				String zonalEmailId = "";
				//OrganaizationUnit zonalMailBasedOnBranchType = getZonalMailBasedOnBranchType(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode(), ReferenceTable.BRANCH_TYPE_ID);
				OrganaizationUnit zonalMailBasedOnBranchType = getBranchCode(reimbursement.getClaim().getIntimation().getPolicy().getHomeOfficeCode());
				if(zonalMailBasedOnBranchType != null){
					zonalEmailId = zonalMailBasedOnBranchType.getEmailId2();
					if(preauthDTO.getNewIntimationDTO().getIsPaayasPolicy() != null && preauthDTO.getNewIntimationDTO().getIsPaayasPolicy()){
						String val = zonalEmailId.replaceAll("reimb.delhi@starhealth.in", "paayas@starhealth.in");
						zonalEmailId = val;
					}
				}
				claimPayment.setZonalMailId(zonalEmailId /*+", "+SHAConstants.REIMBURSEMENT_PAYMENT_MAIL_ID*/);
			}
			}
		}catch(Exception e){
			log.info("Issuing office code update issue"+reimbursement.getClaim().getIntimation().getIntimationId());
			e.printStackTrace();
		}
		
		String vnrFlag = null;
		claimPayment.setRodkey(reimbursement.getKey());	
		if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) && 
				((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(preauthDTO.getPreauthDataExtractionDetails().getPaymentModeFlag()))){
			
			vnrFlag = callPaymentFunDeDupProcedure(preauthDTO.getNewIntimationDTO().getIntimationId(),claimPayment.getAccountNumber(),
				    	claimPayment.getIfscCode(),preauthDTO.getPayeeName() != null ? preauthDTO.getPayeeName().toUpperCase():null,
							preauthDTO.getLegalHeirDto().getHeirName() != null ?preauthDTO.getLegalHeirDto().getHeirName().toUpperCase():null);
			
			if(null != vnrFlag){
				claimPayment.setVnrFlag(vnrFlag);
			}
			
		}else{
			claimPayment.setVnrFlag(SHAConstants.VERIFICATION_NOT_REQUIRED);
		}
		if(preauthDTO.getClaimDTO().getLegalClaim() !=null
				&& preauthDTO.getClaimDTO().getLegalClaim().equals("Y")){
			if(preauthDTO.getLegalBillingDTO() !=null
					&& preauthDTO.getLegalBillingDTO().getTotalApprovedAmount() !=null){
				Double approvedAmt = preauthDTO.getLegalBillingDTO().getTotalApprovedAmount().doubleValue();
				claimPayment.setApprovedAmount(approvedAmt);
				claimPayment.setTotalApprovedAmount(approvedAmt);
				claimPayment.setClaimTotAmt(approvedAmt.longValue());
			}		
			claimPayment.setTdsAmt(preauthDTO.getLegalBillingDTO().getTdsAmount());
			claimPayment.setTotalIntAmt(preauthDTO.getLegalBillingDTO().getTotalInterestAmount());
			claimPayment.setClaimTotalnoofdays(preauthDTO.getLegalBillingDTO().getTotalnoofdays());
			claimPayment.setTdsPercentge(preauthDTO.getLegalBillingDTO().getTdsPercentge());
			claimPayment.setCompensationAmt(preauthDTO.getLegalBillingDTO().getCompensation());
			claimPayment.setLegalCost(preauthDTO.getLegalBillingDTO().getCost());
		}
		entityManager.persist(claimPayment);
		entityManager.flush();
		
	
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
			nomineeObj.setVirtualPaymentAddress(nomineeDetailsDto.getMicrCode());
//			nomineeObj.setEffectiveFromDate();
//			nomineeObj.setEffectiveToDate();
			
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
	
	public void saveLegalHeirAndDocumentDetailsFromFA(PreauthDTO rodDTO) {
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		if(rodDTO.getLegalHeirDTOList() != null && !rodDTO.getLegalHeirDTOList().isEmpty()) {
			LegalHeir legalHeir;
			for (LegalHeirDTO legalHeirDto : rodDTO.getLegalHeirDTOList()) {
				legalHeir = new LegalHeir();
				if(legalHeirDto.getLegalHeirKey() != null) {
					legalHeir.setKey(legalHeirDto.getLegalHeirKey());
				}
				
			legalHeir.setLegalHeirName(legalHeirDto.getHeirName());
			legalHeir.setRelationCode(legalHeirDto.getRelationship() != null ? legalHeirDto.getRelationship().getId() : null);
			legalHeir.setRelationDesc(legalHeirDto.getRelationship() != null ? legalHeirDto.getRelationship().getValue() : "");
			legalHeir.setSharePercentage(legalHeirDto.getSharePercentage());
			legalHeir.setAddress(legalHeirDto.getAddress());
			if(legalHeirDto.getAccountType() != null && legalHeirDto.getAccountType().getValue() != null) {
			legalHeir.setAccountType(legalHeirDto.getAccountType().getValue());
			}
			legalHeir.setBeneficiaryName(legalHeirDto.getBeneficiaryName());
			if(legalHeirDto.getAccountNo() != null && !legalHeirDto.getAccountNo().isEmpty()){
				legalHeir.setAccountNo(Long.valueOf(legalHeirDto.getAccountNo()));
			}
			legalHeir.setAccountPreference(legalHeirDto.getAccountPreference() != null ? legalHeirDto.getAccountPreference().getValue() : "");
			legalHeir.setPaymentModeId(legalHeirDto.getPaymentModeId());
			legalHeir.setPayableAt(legalHeirDto.getPayableAt());
			legalHeir.setBankName(legalHeirDto.getBankName());
			legalHeir.setBankBranchName(legalHeirDto.getBankBranchName());
			legalHeir.setIfscCode(legalHeirDto.getIfscCode());
			if (legalHeirDto.getDocumentToken() != null) {
				legalHeir.setUploadFlag(SHAConstants.YES_FLAG);
			} else {
				legalHeir.setUploadFlag(SHAConstants.N_FLAG);
			}
			if (legalHeirDto.getDocumentToken() != null) {
			legalHeir.setDocKey(legalHeirDto.getDocumentToken());
			}
			if(legalHeirDto.getDeleteLegalHeir() != null && legalHeirDto.getDeleteLegalHeir().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				legalHeir.setActiveStatus(SHAConstants.N_FLAG);
			}else{
			legalHeir.setActiveStatus(SHAConstants.YES_FLAG);
			}
			legalHeir.setRodKey(rodDTO.getKey());
//			legalHeir.setIntimationKey(rodDTO.getNewIntimationDTO().getKey());
			if(rodDTO.getNewIntimationDTO().getPolicy() != null){
				legalHeir.setPolicyKey(rodDTO.getNewIntimationDTO().getPolicy().getKey());
			}
			if(rodDTO.getNewIntimationDTO().getInsuredPatient() != null){
				legalHeir.setInsuredKey(rodDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			}
			legalHeir.setPincode(legalHeirDto.getPincode());
			if(legalHeirDto.getLegalHeirKey() == null) {
				legalHeir.setCreatedBy(userNameForDB);
				legalHeir.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(legalHeir);
			}	
			else {
				legalHeir.setModifiedBy(userNameForDB);
				legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(legalHeir);
			}
			entityManager.flush();
			//persisting in document details table
			if(legalHeirDto.getDocumentToken() != null){
			DocumentDetails docDetails = new DocumentDetails();
			docDetails.setDocumentToken(legalHeirDto.getDocumentToken());
			docDetails.setIntimationNumber(rodDTO.getNewIntimationDTO().getIntimationId());
			docDetails.setClaimNumber(rodDTO.getNewIntimationDTO().getClaimNumber());
//			docDetails.setReimbursementNumber(rodDTO.getRodNumberForUploadTbl());
			docDetails.setFileName(legalHeirDto.getFileName());
			docDetails.setDocumentType(SHAConstants.LEGAL_HEIR_CERT);
			docDetails.setRodKey(rodDTO.getKey());
			entityManager.persist(docDetails);
			entityManager.flush();
			}
		}
		}
	}
	
	private void populatePaymentDetails(Reimbursement reimbursement , PreauthDTO preauthDTO)
	{
		if(null != reimbursement && null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails())
		{
			reimbursement.setPayeeEmailId(preauthDTO.getPreauthDataExtractionDetails().getEmailId());
			reimbursement.setAccountNumber(preauthDTO.getPreauthDataExtractionDetails().getAccountNo() != null ? preauthDTO.getPreauthDataExtractionDetails().getAccountNo() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccNumber() : null));
			reimbursement.setAccountPreference(preauthDTO.getPreauthDataExtractionDetails().getAccountPref() != null ? preauthDTO.getPreauthDataExtractionDetails().getAccountPref() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccPreference() : null));
			reimbursement.setAccountType(preauthDTO.getPreauthDataExtractionDetails().getAccType() != null ?  preauthDTO.getPreauthDataExtractionDetails().getAccType() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getAccType() : null));
			reimbursement.setPanNumber(preauthDTO.getPreauthDataExtractionDetails().getPanNo() != null ? preauthDTO.getPreauthDataExtractionDetails().getPanNo() : (preauthDTO.getDto() != null ? preauthDTO.getDto().getPanNumber() : null));
			if(null != preauthDTO.getPreauthDataExtractionDetails().getPayeeName())
				reimbursement.setPayeeName(preauthDTO.getPreauthDataExtractionDetails().getPayeeName().getValue());
			reimbursement.setNameAsPerBankAccount(preauthDTO.getPreauthDataExtractionDetails().getNameAsPerBank());
//			reimbursement.setLegalHeirFirstName(rodDTO.getDocumentDetails().getLegalFirstName());
			reimbursement.setReasonForChange(preauthDTO.getPreauthDataExtractionDetails().getReasonForChange());
			reimbursement.setPayableAt(preauthDTO.getPreauthDataExtractionDetails().getPayableAt());
		}
	}
		
	//added for new product
	public List<MasHospitalCashBenefit> getHospitalBenefitsonProductKey(
			Long productKey) {
		Query query = entityManager
				.createNamedQuery("MasHospitalCashBenefit.findByProductKey");
		query.setParameter("productKey", productKey);
		List<MasHospitalCashBenefit> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (MasHospitalCashBenefit claimVerification : resultList) {
				entityManager.refresh(claimVerification);
			}
		}
		if (!resultList.isEmpty()) {
			return resultList;
		} else {

		}
		return null;
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
	
	public void setQuantumReductionForRRC(RRCDTO requestRRCDTO ,PreauthDTO preauthDTO,String stage){

		List<QuantumReductionDetailsDTO> reductionDetailsDTOs = new ArrayList<QuantumReductionDetailsDTO>();
		QuantumReductionDetailsDTO reductionDetailsDTO = requestRRCDTO.getQuantumReductionDetailsDTO();

		if((SHAConstants.BILLING).equals(stage)
				|| (SHAConstants.FINANCIAL).equals(stage)){	
			if(preauthDTO.getIsReconsiderationRequest() != null && !preauthDTO.getIsReconsiderationRequest()) {
				if(preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && !preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					reductionDetailsDTO.setSettlementAmount(getApprovedAmuntForBillingRRC(preauthDTO));		
				}
			}
		}else if((SHAConstants.CLAIM_REQUEST).equals(stage)){
			reductionDetailsDTO.setPreAuthAmount(Long.parseLong(preauthDTO.getAmountRequested()));
		}
		
		if((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT).equals(stage)
				|| (SHAConstants.PROCESS_ENHANCEMENT).equals(stage)
				|| (SHAConstants.PROCESS_PREAUTH).equals(stage)
				|| (SHAConstants.PROCESS_PRE_MEDICAL).equals(stage)
				|| (SHAConstants.PROCESS_DOWNSIZE_PREAUTH).equals(stage)){
			if(preauthDTO.getStatusKey() == null
					|| !(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
					|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
					|| preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)
					|| preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS))){
				if(reductionDetailsDTO.getSettlementAmount() !=null 
						&& reductionDetailsDTO.getSettlementAmount() == 0){
					Preauth preauth = preauthService.getlastPreauthbyintimationkey(preauthDTO.getNewIntimationDTO().getKey());
					if(preauth !=null){
						if(preauth.getTotalApprovalAmount() !=null){
							reductionDetailsDTO.setSettlementAmount(preauth.getTotalApprovalAmount().longValue());
						}
						if(reductionDetailsDTO.getPreAuthAmount() !=null 
								&& reductionDetailsDTO.getPreAuthAmount() == 0){
							if(preauth.getClaimedAmt() !=null){
								reductionDetailsDTO.setPreAuthAmount(preauth.getClaimedAmt().longValue());
							}	
						}
					}
				}
			}
		}
		if(reductionDetailsDTO.getPreAuthAmount() !=null && reductionDetailsDTO.getSettlementAmount() !=null
				&& (reductionDetailsDTO.getPreAuthAmount() < reductionDetailsDTO.getSettlementAmount())){
			reductionDetailsDTO.setSettlementAmount(reductionDetailsDTO.getPreAuthAmount());
		}
		if(reductionDetailsDTO.getPreAuthAmount() !=null && reductionDetailsDTO.getSettlementAmount() !=null ){
			Long savedAmount = reductionDetailsDTO.getPreAuthAmount() - reductionDetailsDTO.getSettlementAmount();
			reductionDetailsDTO.setSavedAmount(savedAmount);
			requestRRCDTO.setSavedAmount(String.valueOf(savedAmount));
		}
		if(preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList() !=null
				&& !preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			String diagnosisDetails =null;
			for(DiagnosisDetailsTableDTO detailsTableDTO : preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList()){
				if(detailsTableDTO.getDiagnosisName() !=null && detailsTableDTO.getDiagnosisName().getValue() !=null){
					if(diagnosisDetails !=null){
						diagnosisDetails = " ,"+detailsTableDTO.getDiagnosisName().getValue();
					}else{
						diagnosisDetails = detailsTableDTO.getDiagnosisName().getValue();
					} 
				}
			}
			reductionDetailsDTO.setDiagnosis(diagnosisDetails);
		}
		if(preauthDTO.getPreauthDataExtractionDetails().getTreatmentType()!=null &&
				preauthDTO.getPreauthDataExtractionDetails().getTreatmentType().getValue() !=null){
			reductionDetailsDTO.setManagement(preauthDTO.getPreauthDataExtractionDetails().getTreatmentType().getValue());
		}
		if(preauthDTO.getNewIntimationDTO().getHospitalDto() !=null &&
				preauthDTO.getNewIntimationDTO().getHospitalDto().getHospitalType() !=null){
			reductionDetailsDTO.setAnh(anhType(preauthDTO.getNewIntimationDTO().getHospitalDto().getHospitalType().getValue()));
		}
		reductionDetailsDTOs.add(reductionDetailsDTO);
		requestRRCDTO.setQuantumReductionDetailsDTOList(reductionDetailsDTOs);
	}
	
	public SelectValue anhType(String hospitalType){
		SelectValue anhTypeValue = new SelectValue();
		if(hospitalType.equals(SHAConstants.NETWORK_HOSPITAL)){
			anhTypeValue.setId(ReferenceTable.COMMONMASTER_YES);
			anhTypeValue.setValue(SHAConstants.YES);
		}else{
			anhTypeValue.setId(ReferenceTable.COMMONMASTER_NO);
			anhTypeValue.setValue(SHAConstants.No);
		}
		return anhTypeValue;
	}
	
	private Long getApprovedAmuntForBillingRRC(PreauthDTO preauthDTO){
		long approvedAmuntForRRC = 0L;

		approvedAmuntForRRC= preauthDTO.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() +
				preauthDTO.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() +
				preauthDTO.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt();
		return approvedAmuntForRRC;
	}
	
	public BeanItemContainer<SelectValue> getEmployeesFromMaster()
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAllEmployees");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		if (!employeeNameList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeNameList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey().longValue());
				selectValue.setValue(employee.getEmpId()+"-"+employee.getEmpFirstName());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
			SHAUtils.setEmployeeListValue(selectValueContainer);
		}
		/*if(preauthDTO.getNewIntimationDTO() !=null &&
				preauthDTO.getNewIntimationDTO().getKey() !=null){
			Query query = entityManager.createNamedQuery("StageInformation.findEmpIdsByIntimationKey");
			query = query.setParameter("intimationkey", preauthDTO.getNewIntimationDTO().getKey());
			List<String> employeeIds = (List<String>) query.getResultList();
			employeeIds.add(preauthDTO.getStrUserName());
			employeeIds.removeAll(Collections.singleton(null));
			employeeIds.replaceAll(String::toUpperCase);
			if(employeeIds !=null && !employeeIds.isEmpty()){
				Query findAll = entityManager.createNamedQuery("TmpEmployee.findEmpListByEmpIds");
				findAll = findAll.setParameter("empList", employeeIds);
				List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
				if (!employeeNameList.isEmpty()) {
					List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue = null;
					for (TmpEmployee employee : employeeNameList) {
						selectValue = new SelectValue();
						selectValue.setId(employee.getKey().longValue());
						selectValue.setValue(employee.getEmpId()+"-"+employee.getEmpFirstName());
						selectValueList.add(selectValue);
					}
					selectValueContainer.addAll(selectValueList);
				}
			}
		}*/
		return selectValueContainer;
	}
	
	public String callPaymentFunDeDupProcedure(String intimationNo, String accountNo,String ifscCode, String payeeName,
			String legalHierName) {
		
		Query cs = entityManager.createNativeQuery(
				"select FUN_PAYMENT_DE_DUPE (?1,?2,?3,?4,?5) from dual");
		cs.setParameter(1, intimationNo);
		cs.setParameter(2, accountNo);
		cs.setParameter(3, ifscCode);
		cs.setParameter(4, payeeName);
		cs.setParameter(5, legalHierName);
		String result = (String) cs.getSingleResult();
		return result;
	}
	
	public List<SelectValue> employeeNameCriteriaSearchForRRC(String serchValue) {
		
		List<SelectValue> employeeNames = new ArrayList<SelectValue>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TmpEmployee> criteriaQuery = criteriaBuilder.createQuery(TmpEmployee.class);
		Root<TmpEmployee> empRoot = criteriaQuery.from(TmpEmployee.class);
		criteriaQuery.select(empRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		serchValue = serchValue != null ? StringUtils.trim(serchValue).toUpperCase() : "";

		//System.out.println("Query string For Employee RRC		" + serchValue);
		if(serchValue !=null
				&& serchValue.matches(".*\\d.*")){
			predicates.add(criteriaBuilder.like(
					criteriaBuilder.upper(empRoot.<String> get("empId")), "%" + serchValue + "%"));
		}else{
			predicates.add(criteriaBuilder.like(
					criteriaBuilder.upper(empRoot.<String> get("empFirstName")), "%" + serchValue + "%"));
		}
		predicates.add(criteriaBuilder.equal(empRoot.get("activeStatus"),"1"));
		criteriaQuery.orderBy(criteriaBuilder.asc(empRoot.get("empFirstName")));		
		criteriaQuery.select(empRoot).where(predicates.toArray(new Predicate[] {}));
		TypedQuery<TmpEmployee> typedQueryHospitals = entityManager.createQuery(criteriaQuery);
		if(typedQueryHospitals.getResultList() !=null 
				&& !typedQueryHospitals.getResultList().isEmpty()){
			for(TmpEmployee employee : typedQueryHospitals.getResultList()){
				SelectValue emp = new SelectValue();
				emp.setId(employee.getKey());
				emp.setValue(employee.getEmpId()+"-"+employee.getEmpFirstName());
				employeeNames.add(emp);
			}
		}
		return employeeNames;
	}
	
	public List<Reimbursement> getReimbursementByClaimKeForPhysicalDoc(Long claimKey) {
		/*Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);*/
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKeyForReceivedPhyDocs").setParameter("claimKey",
				claimKey);
		List<Reimbursement> rodList = query.getResultList();	
		if(rodList != null && !rodList.isEmpty()){
			for(Reimbursement rodObj : rodList){	
				entityManager.refresh(rodObj);
			}
		}

		return rodList;
	}

	public void generateDocForTopup( DocAcknowledgement docAcknowledgement , ReceiptOfDocumentsDTO bean){

		try {
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber",docAcknowledgement.getClaim().getIntimation().getIntimationId());
			Claim objClaim = getClaimByClaimKey(docAcknowledgement.getClaim().getKey());
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
			dataMap.put("filePath", bean.getDocFilePath());
			dataMap.put("docType", bean.getDocType());
			dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			dataMap.put("createdBy", "XXXXXX");
			SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LegalTaxDeduction getTaxDeductionFromRODKey(Long rodKey)
	{
		Query findAll = entityManager.createNamedQuery("LegalTaxDeduction.findbyrodKey");
		findAll.setParameter("rodKey", rodKey);
		List<LegalTaxDeduction> taxDeductions = (List<LegalTaxDeduction>) findAll.getResultList();	
		if (taxDeductions !=null
				&& !taxDeductions.isEmpty()) {
			return taxDeductions.get(0);
		}
		return null;
	}
	
	public LegalBillingDTO calculateLegalSettlment(LegalBillingDTO billingDTO){
		
		Long totalApproved = 0L;
		if(billingDTO.getAwardAmount() !=null){
			totalApproved += billingDTO.getAwardAmount();
		}if(billingDTO.getCost() !=null){
			totalApproved += billingDTO.getCost();
		}if(billingDTO.getCompensation() !=null){
			totalApproved += billingDTO.getCompensation();
		}if(billingDTO.getInterestApplicable() !=null){
			if(billingDTO.getInterestCurrentClaim() !=null
					&& billingDTO.getInterestOtherClaim() !=null){
				Long intrPay =billingDTO.getInterestCurrentClaim() + billingDTO.getInterestOtherClaim();
				billingDTO.setTotalInterestAmount(intrPay);
				totalApproved+= intrPay;			
			}if(billingDTO.getTdsAmount() !=null){
				totalApproved -= billingDTO.getTdsAmount();
			}
		}
		billingDTO.setTotalApprovedAmount(totalApproved);
		return billingDTO;
	}
	
	public DocAcknowledgement getAcknowledgementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		
		List<DocAcknowledgement> docAcknowledgementList = query.getResultList();
		
		if(docAcknowledgementList != null)
		return docAcknowledgementList.get(0);
		
		return null;
	}
	
	public String getOutcomeForPAClaimRequestWaitForInput(Reimbursement reimbursement,
			PreauthDTO bean) {

		String outCome = "";
		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_APPROVE_STATUS;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) || (ReferenceTable.PAYMENT_SETTLED).equals(reimbursement.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REJECT_STATUS;
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS;
		}  else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;
				 if (bean.getPreauthMedicalDecisionDetails()
						.getEscalateTo().getId().intValue() == (ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)) {
					 
					 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
				}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
			 outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_ESCLATE_CLAIM;/* outcome for esclate reply to be set.**/
		}  else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
			;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_SPECIALIST;
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_CANCEL_ROD_STATUS;
		}else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
		}
		else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY)){
			outCome = SHAConstants.OUTCOME_CLAIM_REQUEST_REFER_TO_BILLENTRY;
		}	
		else
		{
			outCome = SHAConstants.CLAIM_REQUEST_TO_WAIT_FOR_INPUT_OUTCOME;
			updateStageInformation(reimbursement,bean);
		}
		return outCome;
	}
	
	public void submitperauthMaICACProcess(PreauthDTO preauthDTO){
		IcacRequest icacRequest = new IcacRequest();
		if(null != preauthDTO){
			String strUserName = preauthDTO.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);

			Claim currentClaim = searchByClaimKeyValues(preauthDTO.getClaimKey());
			Reimbursement reimbObj = getReimbursementByKey(preauthDTO.getKey());

			if(reimbObj.getKey() != null){
				if(reimbObj != null && null != reimbObj.getKey()){
					reimbObj.setIcacFlag(SHAConstants.YES_FLAG);
					entityManager.merge(reimbObj);
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
				Status status = entityManager.find(Status.class,
						ReferenceTable.CLAIM_REQUEST_ICAC_REQUEST_INITIATED);
				Stage stage = entityManager.find(Stage.class,
						ReferenceTable.CLAIM_REQUEST_STAGE);
				String srcStage = SHAConstants.MA;
				if(srcStage != null && !srcStage.isEmpty()){
					icacRequest.setIcacRequestSource(srcStage);
				}
				icacRequest.setClaimStage(stage);
				icacRequest.setClaimType(currentClaim.getClaimType());
				icacRequest.setStatusId(status);
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
	
	private void updateLegalHeir(PreauthDTO rodDTO){
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		List<LegalHeir> list = getListOfLegalHeirByTransactionKey(rodDTO.getKey());
		if(list != null && !list.isEmpty()){
			for (LegalHeir legalHeir : list) {
				legalHeir.setModifiedBy(userNameForDB);
				legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				legalHeir.setActiveStatus(SHAConstants.N_FLAG);
				entityManager.merge(legalHeir);
				entityManager.flush();
			}
		}
	}
	
	public List<LegalHeir> getListOfLegalHeirByTransactionKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList;
		}
		return null;
		
	}
	
	private Boolean isPANomineeDeceased(PreauthDTO preauthDTO){
		
		if(ReferenceTable.PA_LOB_KEY.equals(preauthDTO.getNewIntimationDTO().getLobId().getId()) && 
				preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null &&
				preauthDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()){
			return  true;
		}
		
		return false;
	}

	// add hospt cash product-076
	private void savehospitalcash(PreauthDTO rodDTO /*ReceiptOfDocumentsDTO rodDTO*/) {
	
		
		Reimbursement reimbursement = getReimbursementObjectByKey(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getRodKey());
		
	
		Double totalAmountPayable = 0d;
		
		 
		if(null != reimbursement)
		{	
			DocAcknowledgement docAck = reimbursement.getDocAcknowLedgement();
			docAck.setHospitalisationFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getPartialHospitalizationFlag());
			docAck.setPreHospitalisationFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getPostHospitalizationFlag());
			docAck.setLumpsumAmountFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getLumpSumAmountFlag());
			docAck.setHospitalCashFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getAddOnBenefitsHospitalCashFlag());
			docAck.setPatientCareFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getAddOnBenefitsPatientCareFlag());
			docAck.setProdHospBenefitFlag(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCashFlag());
			
			entityManager.merge(docAck);
			entityManager.flush();
			entityManager.refresh(docAck);
		}
		
		if(null != rodDTO.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO() && !rodDTO.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO().isEmpty())
		{
			
			Status status = new Status();
		    Double hospitalCashPayableAmt = 0d;
		    Double patientCarePayableAmt = 0d;
			
			UploadDocumentDTO uploadDTO = rodDTO.getReceiptOfDocumentsDTO().getUploadDocumentsDTO();
			if(null != rodDTO.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO() && !rodDTO.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO().isEmpty())
			{
				Stage stage = null;
				for (AddOnBenefitsDTO benefitsDTO : rodDTO.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO()) {
	
					ReimbursementBenefits reimbursementHospBenefits = ProcessClaimRequestBenefitsMapper.getInstance().getAddOnBenefits(benefitsDTO);
					if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
							.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
                      || (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){

						//ReimbursementBenefits reimbursementHospBenefits = ProcessClaimRequestBenefitsMapper.getAddOnBenefits(benefitsDTO);
						ReimbursementBenefits reimbursementBenefits = getReimBenefitsByRODPHCKey(rodDTO.getKey(),benefitsDTO.getPhcBenefitId().longValue());
						
						if(reimbursementBenefits != null && reimbursementBenefits.getKey() != null && benefitsDTO != null){
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


							if (rodDTO.getReceiptOfDocumentsDTO() != null && rodDTO.getReceiptOfDocumentsDTO().getStatusKey() != null)
							{
								if(rodDTO.getReceiptOfDocumentsDTO().getStatusKey().equals(ReferenceTable.BILLING_CANCEL_ROD)
										|| rodDTO.getReceiptOfDocumentsDTO().getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)
										|| rodDTO.getReceiptOfDocumentsDTO().getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))
								{
									reimbursementHospBenefits.setDeletedFlag(SHAConstants.YES_FLAG);
								}
								else
								{
									reimbursementHospBenefits.setDeletedFlag(SHAConstants.N_FLAG);
									reimbursementHospBenefits.setModifiedBy(rodDTO.getStrUserName());
									reimbursementHospBenefits.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								}
							}

							populateBenefitsData(uploadDTO ,  reimbursementHospBenefits  ,  benefitsDTO);

							if(rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails() != null && rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getStatusId() != null && (rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_CANCEL_ROD)
									|| rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) || rodDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getStatusId().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS))){
								reimbursementHospBenefits.setPayableAmount(0d);
								reimbursementHospBenefits.setTotalClaimAmountBills(0d);
							}

							if(null != reimbursementHospBenefits.getPayableAmount()) {
								totalAmountPayable += reimbursementHospBenefits.getPayableAmount();
								hospitalCashPayableAmt += reimbursementHospBenefits.getPayableAmount();
							}
							if(totalAmountPayable > 0 ){
							rodDTO.getReceiptOfDocumentsDTO().setTotalClaimedAmount(totalAmountPayable);
							}

							entityManager.merge(reimbursementHospBenefits);
							entityManager.flush();
							entityManager.clear();
							//entityManager.refresh(reimbursementHospBenefits);
						}
						
					}
				}
			}
			
		}
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

	public List<MasHospitalCashBenefit> getHospitalBenefitsonProductTypeKey(
			Long productTypeId ,String planType) {
		Query query = entityManager
				.createNamedQuery("MasHospitalCashBenefit.findByProductTypeId");
		query.setParameter("productTypeId", productTypeId);
		query.setParameter("planType", planType);
		List<MasHospitalCashBenefit> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (MasHospitalCashBenefit claimVerification : resultList) {
				entityManager.refresh(claimVerification);
			}
		}
		if (!resultList.isEmpty()) {
			return resultList;
		} else {

		}
		return null;
	}
	
	public String getPaymentTemplateNameWithVersion(String docType,Date paymentDate){
		String templateName = null;
		if(paymentDate == null){
				paymentDate = new Date();
		}
		
		String query = "SELECT TEMPLATE_NAME FROM  MAS_LETTER_TEMPLATE WHERE DOCUMENT_TYPE='" 
						+ docType 
						+"' AND TO_DATE('" 
						+ new SimpleDateFormat("dd/MM/yyyy").format(paymentDate) 
						+ "','DD/MM/YYYY') BETWEEN trunc(FROM_DT) AND trunc(TO_DT) AND ACTIVE_FLAG='Y'";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
				
		List<Object> objList = nativeQuery.getResultList();
		
		if(null != objList && !objList.isEmpty())
		{
			templateName = objList.get(0).toString();
		}
		
		return templateName;
	}

	public LegalHeir getlegalHeirListByTransactionKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList.get(0);
		}
		return null;
		
	}

	public List<AssignedInvestigatiorDetails> getInvsReviewRemarksDetailsByClaimKey(Long claimKey){	

		List<AssignedInvestigatiorDetails> invsReviewRemarksTableList = new ArrayList<AssignedInvestigatiorDetails>();

		Query query = entityManager.createNamedQuery("Investigation.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Investigation> invsCompletedList = query.getResultList();

		if(null != invsCompletedList && !invsCompletedList.isEmpty()){
			for (Investigation investigation : invsCompletedList) {
				List<AssignedInvestigatiorDetails> assignInvsObj = getInvestionReviewList(investigation.getKey());				
				if(null != assignInvsObj && !assignInvsObj.isEmpty()){
					invsReviewRemarksTableList.addAll(assignInvsObj);
				}
			}
		}
		return invsReviewRemarksTableList;
	}
	
	public List<AssignedInvestigatiorDetails> getInvestionReviewList(Long investigationKey){	
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.INVESTIGATION_GRADING);
		statusList.add(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		
		Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByStatusAndReviewFlag")
				.setParameter("investigationkey", investigationKey);
		//query = query.setParameter("statusList", statusList);
		List<AssignedInvestigatiorDetails> investigationList = query.getResultList();
			if(null != investigationList && !investigationList.isEmpty()){
				return investigationList;
			}
			return null;
	}
	public void updateCancelRemarks(Reimbursement reimbursement, String remarks,PreauthDTO bean){
		if(null != reimbursement){
			reimbursement.setAutoCancelRemarks(remarks);
			if(null != reimbursement.getKey()){
				entityManager.merge(reimbursement);
				entityManager.flush();
			}
		}
		AutoAllocationCancelRemarks autoAllocCancelRemarks = new AutoAllocationCancelRemarks();
		if(bean.getKey() !=null){
			autoAllocCancelRemarks.setTransactionKey(bean.getKey());
		}else{
			autoAllocCancelRemarks.setTransactionKey(0L);
		}
		autoAllocCancelRemarks.setStageId(ReferenceTable.BILLING_STAGE);
		autoAllocCancelRemarks.setIntimationKey(null != bean.getNewIntimationDTO() ? bean.getNewIntimationDTO().getKey():null);
		autoAllocCancelRemarks.setClaimKey(bean.getClaimDTO().getKey());
		autoAllocCancelRemarks.setTransactionType(SHAConstants.REIMBURSEMENT_CHAR);
		autoAllocCancelRemarks.setCancelRemarks(remarks);
		autoAllocCancelRemarks.setCancelledBy(bean.getStrUserName());
		autoAllocCancelRemarks.setCancelledDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(autoAllocCancelRemarks);
		entityManager.flush();
	
	}
	
 public Boolean getCataractSelectedorNot(List<Long> icdCodeList){
		 Boolean isCataractLimitAvailable = false;
		 Query query = entityManager.createNamedQuery("BillingAutoAllocationIcdCode.findByKeyList");
	 		query.setParameter("keyList", icdCodeList);
	 		
	 		List<BillingAutoAllocationIcdCode> resultList = (List<BillingAutoAllocationIcdCode>)query.getResultList();
	 		if(resultList != null && ! resultList.isEmpty()){
	 			isCataractLimitAvailable = true ;
	 		}
		 return isCataractLimitAvailable;
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
 
 public void updateCancelRemarksForMA(Reimbursement reimbursement, String remarks,PreauthDTO bean){
	 if(null != reimbursement){
		 reimbursement.setAutoCancelMARemarks(remarks);
		 if(null != reimbursement.getKey()){
			 entityManager.merge(reimbursement);
			 entityManager.flush();
		 }
	 }
	 
	 AutoAllocationCancelRemarks autoAllocCancelRemarks = new AutoAllocationCancelRemarks();
		if(bean.getKey() !=null){
			autoAllocCancelRemarks.setTransactionKey(bean.getKey());
		}else{
			autoAllocCancelRemarks.setTransactionKey(0L);
		}
		autoAllocCancelRemarks.setStageId(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
		autoAllocCancelRemarks.setIntimationKey(null != bean.getNewIntimationDTO() ? bean.getNewIntimationDTO().getKey():null);
		autoAllocCancelRemarks.setClaimKey(bean.getClaimDTO().getKey());
		autoAllocCancelRemarks.setTransactionType(SHAConstants.REIMBURSEMENT_CHAR);
		autoAllocCancelRemarks.setCancelRemarks(remarks);
		autoAllocCancelRemarks.setCancelledBy(bean.getStrUserName());
		autoAllocCancelRemarks.setCancelledDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(autoAllocCancelRemarks);
		entityManager.flush();
	 
 }
 public Boolean getCovidSelectedorNot(List<Long> icdCodeList){
	 Boolean isCovidLimitAvailable = false;
	 Query query = entityManager.createNamedQuery("BillingAutoAllocationIcdCode.findByKeyList");
 		query.setParameter("keyList", icdCodeList);
 		
 		List<BillingAutoAllocationIcdCode> resultList = (List<BillingAutoAllocationIcdCode>)query.getResultList();
 		if(resultList != null && ! resultList.isEmpty()){
 			for(BillingAutoAllocationIcdCode detailsTableDTO : resultList){
				if(detailsTableDTO != null && detailsTableDTO.getAutoAllocIcdCodeKey() != null 
						&& (detailsTableDTO.getAutoAllocIcdCodeKey().equals(SHAConstants.COVID_19_ICD_NOT_IDENT_KEY)
								|| detailsTableDTO.getAutoAllocIcdCodeKey().equals(SHAConstants.COVID_19_ICD_IDENT_KEY))){
					isCovidLimitAvailable = true;
					break;
				}else{
					isCovidLimitAvailable = false;
				}
			}
 		}
	 return isCovidLimitAvailable;
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
 
 
 @SuppressWarnings("unchecked")
	public Boolean getSettledByPolicyKey(Long policyKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.getRodKeysByPolicyKey");
		query  = query.setParameter("policykey", policyKey);
		List<Long> rodKeyList = query.getResultList();
		Query pedQuery;
		List<PedValidation> resultList = null;
		
		if(rodKeyList != null && !rodKeyList.isEmpty()){
			List<Long> limitIds = new ArrayList<Long>();
			limitIds.add(17717L);
			limitIds.add(17718L);
			limitIds.add(17719L);
			pedQuery = entityManager.createNamedQuery("PedValidation.findRodSettledByLimitID");
			pedQuery.setParameter("listRodKeys", rodKeyList);
			pedQuery.setParameter("limitIds", limitIds);
			resultList = pedQuery.getResultList();
			if(resultList != null && !resultList.isEmpty()){
				return true;
			}
		}

		return false;
	}
 
 @SuppressWarnings("unchecked")
	public Boolean getNotSettledRodByPolicyKey(Long rodkey ,Long policyKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.getRodNotSetledByPolyKeyAndRodKey");
		query  = query.setParameter("primaryKey", rodkey);
		query  = query.setParameter("policykey", policyKey);
		List<Long> rodKeyList = query.getResultList();
		Query pedQuery;
		List<PedValidation> resultList = null;
		
		if(rodKeyList != null && !rodKeyList.isEmpty()){
			List<Long> limitIds = new ArrayList<Long>();
			limitIds.add(17717L);
			limitIds.add(17718L);
			limitIds.add(17719L);
			pedQuery = entityManager.createNamedQuery("PedValidation.findRodSettledByLimitID");
			pedQuery.setParameter("listRodKeys", rodKeyList);
			pedQuery.setParameter("limitIds", limitIds);
			resultList = pedQuery.getResultList();
			if(resultList != null && !resultList.isEmpty()){
				return true;
			}
		}

		return false;
	}
 
 	public Boolean isAlreadyPreauthApprovedForPostCVC(Long claimKey){

		
		String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CLAIM_KEY =" + claimKey
				+ " AND STATUS_ID IN (22)";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return true;
		}
		return false;
	}
 	
 	public Boolean isAlreadyMAApprovedForPostCVC(Long claimKey){

		
		String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CLAIM_KEY =" + claimKey
				+ " AND STATUS_ID IN  (78,103)";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return true;
		}
		return false;
	}
}