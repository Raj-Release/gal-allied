package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.legal.processconsumerforum.page.advocatenotice.AdvocateNoticeDTO;
import com.shaic.claim.legal.processconsumerforum.page.advocatenotice.SearchProcessAdvocateNoticeService;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.ConsumerForumDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumService;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalOmbudsman;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;

@Stateless
public class SearchProcessOmbudsmanService {


	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchProcessConsumerForumService consumerForumService;
	
	@EJB
	private SearchProcessAdvocateNoticeService advocateNoticeService;

	public void saveLegalOmbudsman(OmbudsmanDetailsDTO ombudsmanDetailsDTO) {
		
		LegalOmbudsman legalOmbudsman = null; 
		if(ombudsmanDetailsDTO.getIntimationDetailsDTO()!=null && ombudsmanDetailsDTO.getIntimationDetailsDTO().getIntimationNo()!=null){
			legalOmbudsman = getLegalByIntimationNumberAndType(ombudsmanDetailsDTO.getIntimationDetailsDTO().getIntimationNo(),SHAConstants.LEGAL_OMBUDSMAN);
		}
		if(legalOmbudsman == null){
			legalOmbudsman = new LegalOmbudsman();
		}
		legalOmbudsman.setActiveStatus(1L);
		IntimationDetailsDTO intimationDetailsDTO = ombudsmanDetailsDTO.getIntimationDetailsDTO();
		if(intimationDetailsDTO!=null){
			saveIntimationValues(intimationDetailsDTO, legalOmbudsman);
			legalOmbudsman.setClaimKey(intimationDetailsDTO.getClaimKey());
		}
		
		if(ombudsmanDetailsDTO!=null){
			
			if(ombudsmanDetailsDTO.getMovedTo()!=null){
				MastersValue movedTO = masterService.getMaster(ombudsmanDetailsDTO.getMovedTo().getId());
				legalOmbudsman.setMovedTO(movedTO);
				if(movedTO.getValue().equalsIgnoreCase("Consumer Forum")){
					ConsumerForumDTO consumerForumDTO = new ConsumerForumDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7019L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					consumerForumDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					consumerForumService.saveLegalConsumer(consumerForumDTO);
					legalOmbudsman.setActiveStatus(0L);
				}
				if(movedTO.getValue().equalsIgnoreCase("Lawyer/Advocate Notice")){
					AdvocateNoticeDTO advocateNoticeDTO = new AdvocateNoticeDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7019L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					advocateNoticeDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					advocateNoticeService.saveLegalAdvocateNotice(advocateNoticeDTO);
					legalOmbudsman.setActiveStatus(0L);
				}
				
			}
			
			legalOmbudsman.setOmbudLockFlag(ombudsmanDetailsDTO.getIsOmbudsmanClaimLock());
			
			if(ombudsmanDetailsDTO.getIsOmbudsmanClaimLock()!=null){
				if(ombudsmanDetailsDTO.getIsOmbudsmanClaimLock()){
					Claim claimKey = legalOmbudsman.getClaimKey();
					if(claimKey.getLegalFlag()!=null && claimKey.getLegalFlag().equalsIgnoreCase(SHAConstants.N_FLAG)){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}else if(claimKey.getLegalFlag()==null){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}
				}else{
					Claim claimKey = legalOmbudsman.getClaimKey();
					if(claimKey.getLegalFlag()!=null && claimKey.getLegalFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						claimKey.setLegalFlag(SHAConstants.N_FLAG);
						entityManager.merge(claimKey);
					}else if(claimKey.getLegalFlag()==null){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}
					
				}}
			
			
			legalOmbudsman.setComplaintNumber(ombudsmanDetailsDTO.getComplaintNo());
			legalOmbudsman.setComplaintReceiptDate(ombudsmanDetailsDTO.getComplaintRecieptDate());
			if(ombudsmanDetailsDTO.getAddDays()!=null){
				MastersValue addDays = masterService.getMaster(ombudsmanDetailsDTO.getAddDays().getId());
				legalOmbudsman.setAddDaysId(addDays);
			}
			
			if(ombudsmanDetailsDTO.getOmbudsmanOffice()!=null){
				MasOmbudsman ombuMastersValue = masterService.getOmbudsmanDetailsByKey(ombudsmanDetailsDTO.getOmbudsmanOffice().getId());
				legalOmbudsman.setOmbudCpu(ombuMastersValue);
			}
			legalOmbudsman.setOmbudStipulatedDate(ombudsmanDetailsDTO.getStipulateDate());
			legalOmbudsman.setReferToMedical(ombudsmanDetailsDTO.getReferToMedical());
			legalOmbudsman.setReceiptMedicalOpnDate(ombudsmanDetailsDTO.getReceiptOfMedicalOpinion());
		}
		
		DecisionDetailsDto decisionDetailsDto = ombudsmanDetailsDTO.getDecisionDetailsDto();
		if(decisionDetailsDto!=null){
			if(decisionDetailsDto.getDecision()!=null){
				MastersValue decision = masterService.getMaster(decisionDetailsDto.getDecision().getId());
				legalOmbudsman.setOmbudDecisionId(decision);
			}
			legalOmbudsman.setContestHrgdate(decisionDetailsDto.getDateOfHearing());
			legalOmbudsman.setPpDateHearing(decisionDetailsDto.getPostponmentDateofHearing());
			legalOmbudsman.setAdjournmenNxtHearingDate(decisionDetailsDto.getNxtDateOfHearing());
			if(decisionDetailsDto.getHearingStatus()!=null){
				MastersValue hearingStatusId = masterService.getMaster(decisionDetailsDto.getHearingStatus().getId());
				legalOmbudsman.setHearingStatusId(hearingStatusId);
			}
			
			if(decisionDetailsDto.getAwardStatus()!=null){
				MastersValue awardStatusId = masterService.getMaster(decisionDetailsDto.getAwardStatus().getId());
				legalOmbudsman.setAwardStatusId(awardStatusId);
			}
			legalOmbudsman.setAwardReservedDetail(decisionDetailsDto.getAwardReservedDetails());
			legalOmbudsman.setExgratiaAward(decisionDetailsDto.getExgratiaAward());
			legalOmbudsman.setExgratiaAwardAmt(decisionDetailsDto.getExgratiaAwardAmount());
			
			if(decisionDetailsDto.getLostAddDays()!=null){
				MastersValue addDays = masterService.getMaster(decisionDetailsDto.getLostAddDays().getId());
				legalOmbudsman.setLostAddDaysId(addDays);
			}
			
			if(decisionDetailsDto.getWonAddDays()!=null){
				MastersValue addDays = masterService.getMaster(decisionDetailsDto.getWonAddDays().getId());
				legalOmbudsman.setLostAddDaysId(addDays);
			}
			legalOmbudsman.setSelfContainedFlag(decisionDetailsDto.getSelfContainedNote());
			
			legalOmbudsman.setSelfContPreparationDt(decisionDetailsDto.getSelfConNotePreDate());
			legalOmbudsman.setSelfContSubmissionDt(decisionDetailsDto.getSelfConNotePreDate());
			
			legalOmbudsman.setCaseWon(decisionDetailsDto.getCaseWon());
			legalOmbudsman.setClosureDate(decisionDetailsDto.getClosurDate());
			legalOmbudsman.setAwardReceiptDate(decisionDetailsDto.getAwardRecepitDate());
			legalOmbudsman.setCaseWonRemarks(decisionDetailsDto.getCaseWonRemarks());
			legalOmbudsman.setWonInterest(decisionDetailsDto.getInterest());
			legalOmbudsman.setLostInterest(decisionDetailsDto.getInterest());
			legalOmbudsman.setLastDtStatisfactionAwd(decisionDetailsDto.getLastDateForSatisfactionAward());
			
			legalOmbudsman.setCaseLost(decisionDetailsDto.getCaseLost());
			if(decisionDetailsDto.getAnyAwardofOmbudsmanContested()!=null){
			if(decisionDetailsDto.getAnyAwardofOmbudsmanContested()){
				legalOmbudsman.setAwardOmbContested("Y");
			}else{
				legalOmbudsman.setAwardOmbContested("N");
			}}
			legalOmbudsman.setLostAwdRecDate(decisionDetailsDto.getLostAwardRecepitDate());
			
			legalOmbudsman.setLostClosureDate(decisionDetailsDto.getLostAwardClosureDate());
			if(decisionDetailsDto.getReasonForAwards()!=null){
				MastersValue reasonForAwards = masterService.getMaster(decisionDetailsDto.getReasonForAwards().getId());
				legalOmbudsman.setLostRsnAwdId(reasonForAwards);
			}
			
			legalOmbudsman.setLostLastDateStafisfyAwd(decisionDetailsDto.getLostLastDateForSatisfactionAward());
			legalOmbudsman.setLostCaseReason(decisionDetailsDto.getReasonForCaseLost());
			
			legalOmbudsman.setExgratiaAward(decisionDetailsDto.getExgratiaAward());
			
			legalOmbudsman.setExgratiaAwardAmt(decisionDetailsDto.getExgratiaAwardAmount());
			legalOmbudsman.setExgratiaAwardDt(decisionDetailsDto.getExgratiaAwardDate());
			legalOmbudsman.setExgratiaReason(decisionDetailsDto.getReasonForExgratia());
			legalOmbudsman.setAdjournmenNxtHearingDate(decisionDetailsDto.getAdjournmenNxtHearingDate());
			
			if(decisionDetailsDto.getReasonForCompromiseSettlement()!=null){
				MastersValue compromiseSettleRsnId = masterService.getMaster(decisionDetailsDto.getReasonForCompromiseSettlement().getId());
				legalOmbudsman.setCompromiseSettleRsnId(compromiseSettleRsnId);
			}
			
			legalOmbudsman.setCompromiseAmount(decisionDetailsDto.getCompromiseAmount());
			legalOmbudsman.setCompromiseSettleRemark(decisionDetailsDto.getRemarksCompromiseSettlement());
			
			if(decisionDetailsDto.getStatusOfCase()!=null){
				MastersValue statusId = masterService.getMaster(decisionDetailsDto.getStatusOfCase().getId());
				legalOmbudsman.setStatusCaseId(statusId);
			}
			
			if(decisionDetailsDto.getPendingStatus()!=null){
				MastersValue pendingStatus = masterService.getMaster(decisionDetailsDto.getPendingStatus().getId());
				legalOmbudsman.setPendingStatus(pendingStatus);
			}
		}
		if(ombudsmanDetailsDTO.getClaimReconsideration()!=null){
			if(ombudsmanDetailsDTO.getClaimReconsideration()){
				legalOmbudsman.setClaimReconsideration("Y");
			}else{
				legalOmbudsman.setClaimReconsideration("N");
			}
		}
		legalOmbudsman.setReconsiderationReceipt(ombudsmanDetailsDTO.getReconsiderationReceiptDate());
		if(ombudsmanDetailsDTO.getReconsideration()!=null){
			if(ombudsmanDetailsDTO.getReconsideration()){
				legalOmbudsman.setReconsideration("A");
			}else{
				legalOmbudsman.setReconsideration("R");
			}
		}
		legalOmbudsman.setReconAcceptDate(ombudsmanDetailsDTO.getReconsiderationAcceptDate());
		legalOmbudsman.setReconAcceptanceAmount(ombudsmanDetailsDTO.getAcceptanceAmount());
		legalOmbudsman.setReconRejectDate(ombudsmanDetailsDTO.getReconsiderationRejectDate());
		
		if(ombudsmanDetailsDTO.getCancellationOfPolicy()!=null){
			if(ombudsmanDetailsDTO.getCancellationOfPolicy()){
				legalOmbudsman.setCancellationPolicy("Y");
			}else{
				legalOmbudsman.setCancellationPolicy("N");
			}
			
		}
		legalOmbudsman.setRefundProRataPermium(ombudsmanDetailsDTO.getRefundofProrataPremium());
		legalOmbudsman.setProratePremiumRejDate(ombudsmanDetailsDTO.getProrataPremiumRefundDate());
		
		if(ombudsmanDetailsDTO.getReconsiderationCancellationOfPolicy()!=null){
			if(ombudsmanDetailsDTO.getReconsiderationCancellationOfPolicy()){
				legalOmbudsman.setReconCancelPolicy("Y");
			}else{
				legalOmbudsman.setReconCancelPolicy("N");
			}
		}
		legalOmbudsman.setReconCancelPolicyDate(ombudsmanDetailsDTO.getReconsiderationCancelPolicyAcceptDate());
		legalOmbudsman.setAmtRefundByInsured(ombudsmanDetailsDTO.getAmountrefundbytheInsured());
		
		if(ombudsmanDetailsDTO.getGrievanceRequestInitiated()!=null){
			if(ombudsmanDetailsDTO.getGrievanceRequestInitiated()){
				legalOmbudsman.setGrievanceReqIntiated("Y");
			}else{
				legalOmbudsman.setGrievanceReqIntiated("N");
			}
		}
		legalOmbudsman.setGrievanceInitiatedDate(ombudsmanDetailsDTO.getGrievanceInitiatedDate());
		if(ombudsmanDetailsDTO.getGrievanceOutcome()!=null){
			MastersValue grieavanceOutCome = masterService.getMaster(ombudsmanDetailsDTO.getGrievanceOutcome().getId());
			legalOmbudsman.setGrievanceOutcome(grieavanceOutCome);
			
		}
		legalOmbudsman.setGrievanceRemarks(ombudsmanDetailsDTO.getGrievanceRemarks());
		
		legalOmbudsman.setLegalType(SHAConstants.LEGAL_OMBUDSMAN);
		if(legalOmbudsman.getKey()!=null){
			legalOmbudsman.setModifiedBy(ombudsmanDetailsDTO.getUsername());
			legalOmbudsman.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(legalOmbudsman);
		}else{
			legalOmbudsman.setCreatedBy(ombudsmanDetailsDTO.getUsername());
			legalOmbudsman.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(legalOmbudsman);
		}
		entityManager.flush();
		//entityManager.merge(legal);
		//entityManager.flush();
	}

	private LegalOmbudsman saveIntimationValues(IntimationDetailsDTO intimationDetailsDTO,
			LegalOmbudsman legalOmbudsman) {
		legalOmbudsman.setIntimationNumber(intimationDetailsDTO.getIntimationNo());
		legalOmbudsman.setFinancialYear(intimationDetailsDTO.getFinancialYear());
		legalOmbudsman.setPolicyNumber(intimationDetailsDTO.getPolicyNo());
		legalOmbudsman.setProvisionAmount(intimationDetailsDTO.getProvisionAmt());
		legalOmbudsman.setProductName(intimationDetailsDTO.getProductNo());
		legalOmbudsman.setInsuredName(intimationDetailsDTO.getInsuredName());
		if(intimationDetailsDTO.getRepudiation()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getRepudiation().getId());
			legalOmbudsman.setRepudiationId(repudiation);
		}
		if(intimationDetailsDTO.getReceivedFrom()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getReceivedFrom().getId());
			legalOmbudsman.setReceievedFrom(repudiation);
		}
		legalOmbudsman.setIssueRejectionDate(intimationDetailsDTO.getIssueRejectionDate());
		legalOmbudsman.setRemarks(intimationDetailsDTO.getRemarks());
		return legalOmbudsman;
	}

	public LegalOmbudsman getLegalByIntimationNumberAndType(String intimationNumber,String legalType) {
		List<LegalOmbudsman> resultLegal = null;
		Query findByIntimationNum = entityManager.createNamedQuery("LegalOmbudsman.findByIntimationNumberAndType");
		findByIntimationNum.setParameter("intimationNumber", intimationNumber);		
		findByIntimationNum.setParameter("legalType", legalType);

		try {
			resultLegal = (List<LegalOmbudsman>) findByIntimationNum.getResultList();
			
			if(resultLegal != null && !resultLegal.isEmpty()){
				return resultLegal.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getContactOmbudsman(SelectValue ombudsmanDetailsDTO) {
		String ombudsmanContact = null;
		if(ombudsmanDetailsDTO.getId()!=null){
			MasOmbudsman ombuMastersValue = masterService.getOmbudsmanDetailsByKey(ombudsmanDetailsDTO.getId());
			if(ombuMastersValue!=null){
		    ombudsmanContact = (ombuMastersValue.getOmbName() + ", "  + ombuMastersValue.getOmbAddress1() + 
					(ombuMastersValue.getOmbAddress2()!=null?", " + ombuMastersValue.getOmbAddress2() : "") + 
					(ombuMastersValue.getOmbAddress3()!=null?", " + ombuMastersValue.getOmbAddress3() : "") + 
					(ombuMastersValue.getOmbAddress4()!=null?", " + ombuMastersValue.getOmbAddress4() : ""));
		}}
		
		return ombudsmanContact;
		
	}




}
