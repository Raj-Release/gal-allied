package com.shaic.claim.legal.processconsumerforum.page.consumerforum;


import java.sql.Timestamp;
import java.util.Date;
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
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.OmbudsmanDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.SearchProcessOmbudsmanService;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalConsumer;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.State;
import com.shaic.domain.TmpCPUCode;

@Stateless
public class SearchProcessConsumerForumService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchProcessOmbudsmanService ombudsmanService;
	
	@EJB
	private SearchProcessAdvocateNoticeService advocateNoticeService;

	public void saveLegalConsumer(ConsumerForumDTO consumerForumDTO) {
		
		LegalConsumer legalConsumer = null; 
		if(consumerForumDTO.getIntimationDetailsDTO()!=null && consumerForumDTO.getIntimationDetailsDTO().getIntimationNo()!=null){
			legalConsumer = getLegalByIntimationNumberAndType(consumerForumDTO.getIntimationDetailsDTO().getIntimationNo(),SHAConstants.LEGAL_CONSUMER);
		}
		if(legalConsumer == null){
			legalConsumer = new LegalConsumer();
		}
		legalConsumer.setActiveStatus(1L);
		IntimationDetailsDTO intimationDetailsDTO = consumerForumDTO.getIntimationDetailsDTO();
		if(intimationDetailsDTO!=null){
			saveIntimationValues(intimationDetailsDTO, legalConsumer);
			legalConsumer.setClaimKey(intimationDetailsDTO.getClaimKey());
		}
		CaseDetailsDTO caseDetailsDTO = consumerForumDTO.getCaseDetailsDTO();
		if(caseDetailsDTO!=null){
			legalConsumer.setAdvocateName(caseDetailsDTO.getAdVocateName());
			legalConsumer.setDcdrfRemarks(caseDetailsDTO.getDcdrf());
			legalConsumer.setRecUpdDate(new Date());
			if(caseDetailsDTO.getMovedTo()!=null){
				MastersValue movedTO = masterService.getMaster(caseDetailsDTO.getMovedTo().getId());
				legalConsumer.setMovedTO(movedTO);
				if(movedTO.getValue().equalsIgnoreCase("Ombudsman")){
					OmbudsmanDetailsDTO ombudsmanDetailsDTO = new OmbudsmanDetailsDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7020L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					ombudsmanDetailsDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					ombudsmanService.saveLegalOmbudsman(ombudsmanDetailsDTO);
					legalConsumer.setActiveStatus(0L);
				}
				if(movedTO.getValue().equalsIgnoreCase("Lawyer/Advocate Notice")){
					AdvocateNoticeDTO advocateNoticeDTO = new AdvocateNoticeDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7020L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					advocateNoticeDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					advocateNoticeService.saveLegalAdvocateNotice(advocateNoticeDTO);
					legalConsumer.setActiveStatus(0L);
				}
				
			}

			if(caseDetailsDTO.getZone()!=null){
				Long id = caseDetailsDTO.getZone().getId();
				TmpCPUCode cpuCode = new TmpCPUCode();
				cpuCode.setKey(id);
				//MastersValue zone = masterService.getMaster(caseDetailsDTO.getZone().getId());
				legalConsumer.setZoneId(cpuCode);
			}
			if(caseDetailsDTO.getState()!=null){
				
				Long state = caseDetailsDTO.getState().getId();
				State state2 = new State();
				state2.setKey(state);
				legalConsumer.setStateId(state2);
				/*MastersValue state = masterService.getMaster(caseDetailsDTO.getState().getId());
				if(state!=null){
					legalConsumer.setStateId(state);
				}*/
			}
			legalConsumer.setLegalLockFlag(caseDetailsDTO.getLegalLock());
			if(caseDetailsDTO.getLegalLock()!=null){
				if(caseDetailsDTO.getLegalLock()){
					Claim claimKey = legalConsumer.getClaimKey();
			if(claimKey != null) {
					if(claimKey.getLegalFlag()!=null && claimKey.getLegalFlag().equalsIgnoreCase(SHAConstants.N_FLAG)){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}else if(claimKey.getLegalFlag()==null){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}  }
				}else{
					Claim claimKey = legalConsumer.getClaimKey();
					if(claimKey != null) {
					if(claimKey.getLegalFlag()!=null && claimKey.getLegalFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						claimKey.setLegalFlag(SHAConstants.N_FLAG);
						entityManager.merge(claimKey);
					}else if(claimKey.getLegalFlag()==null){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}
					}
				}
				}
			legalConsumer.setCompClmAmt(caseDetailsDTO.getComplainceClaimedAmt());
			legalConsumer.setComplaintDate(caseDetailsDTO.getComplaintDate());
			legalConsumer.setCcNo(caseDetailsDTO.getCcNo());
		}
		OrderDetailsDTO orderDetailsDTO = consumerForumDTO.getOrderDetailsDTO();
		if(orderDetailsDTO!=null){
			legalConsumer.setReceiptDate(orderDetailsDTO.getDateOfReciept());
			legalConsumer.setOrderDate(orderDetailsDTO.getDateOforder());
			if(orderDetailsDTO.getOrder()!=null){
				MastersValue order = masterService.getMaster(orderDetailsDTO.getOrder().getId());
				legalConsumer.setOrderId(order);
			}else{
				legalConsumer.setOrderId(null);
			}
			legalConsumer.setComplainceDate(orderDetailsDTO.getLimitationOfComplainance());
		}else{
			legalConsumer.setOrderId(null);
		}
		//legalConsumer.setRecUpdRemarks(recUpdRemarks);
		OutOfCourtSettlementDTO outOfCourtSettlmentDTO = consumerForumDTO.getOutOfCourtSettlmentDTO();
		if(outOfCourtSettlmentDTO!=null){
			legalConsumer.setSettledAmount(outOfCourtSettlmentDTO.getSettledAmount());
			legalConsumer.setSettledDate(outOfCourtSettlmentDTO.getSettlementDate());
			legalConsumer.setOfferedAmount(outOfCourtSettlmentDTO.getOfferedAmt());
			legalConsumer.setSavedAmount(outOfCourtSettlmentDTO.getAmtSaved());
			legalConsumer.setOocReason(outOfCourtSettlmentDTO.getReason());
			legalConsumer.setOocSettleFlag(outOfCourtSettlmentDTO.getOutOfCourtSettlement());
			legalConsumer.setConsentLetterFlag(outOfCourtSettlmentDTO.getConsentLetterSent());
			legalConsumer.setLimitofPeriod(outOfCourtSettlmentDTO.getLimitOfPeriod());
		}
		
		legalConsumer.setStateCommFlag(consumerForumDTO.getStateCommission());
		legalConsumer.setNationalCommFlag(consumerForumDTO.getNationalCommission());
		legalConsumer.setMandatoryDepFlag(consumerForumDTO.getMandatoryDeposit());
		legalConsumer.setSupremeCourtFlag(consumerForumDTO.getSupremeCourt());
		legalConsumer.setManDepAmount(consumerForumDTO.getMandatoryAmt());
		legalConsumer.setManDepDate(consumerForumDTO.getMandatoryDate());
		legalConsumer.setManPayeeName(consumerForumDTO.getMandatoryPayeeName());
		if(consumerForumDTO.getMandatoryDepAmtSts()!=null){
			MastersValue depValue = masterService.getMaster(consumerForumDTO.getMandatoryDepAmtSts().getId());
			legalConsumer.setDepAmtStsIs(depValue);
		}
		//legalConsumer.setAdvocateName(consumerForumDTO.getAdvocateName());
		legalConsumer.setFaNumber(consumerForumDTO.getFaNumber());
		if(consumerForumDTO.getState()!=null){
			MastersValue conValue = masterService.getMaster(consumerForumDTO.getState().getId());
			//legalConsumer.setStateId(stateId);
		}
		legalConsumer.setDateHearing(consumerForumDTO.getDateOfHearing());
		legalConsumer.setReplyField(consumerForumDTO.getReplyField());
		if(consumerForumDTO.getStatusOfCase()!=null){
			MastersValue statusOfCase = masterService.getMaster(consumerForumDTO.getStatusOfCase().getId());
			legalConsumer.setStatusCaseId(statusOfCase);
		}
		legalConsumer.setGrndAppealFlag(consumerForumDTO.getGroundforAppealField());
		legalConsumer.setStayStatusFlag(consumerForumDTO.getStatusofStay());
		legalConsumer.setConditionalFlag(consumerForumDTO.getConditionalDeposit());
		legalConsumer.setConDepAmt(consumerForumDTO.getConditionalAmt());
		legalConsumer.setConDepDate(consumerForumDTO.getConditionalDate());
		legalConsumer.setConPayeeName(consumerForumDTO.getConditionalPayeeName());
		if(consumerForumDTO.getConditionalDepAmtSts()!=null){
			MastersValue conValue = masterService.getMaster(consumerForumDTO.getConditionalDepAmtSts().getId());
			legalConsumer.setConAmtStsId(conValue);
		}
		legalConsumer.setAwardFlag(consumerForumDTO.getAwardAgainstus());
		legalConsumer.setAppealFlag(consumerForumDTO.getAppeal());
		if(consumerForumDTO.getAwardReason()!=null){
			legalConsumer.setAwardReasonId(consumerForumDTO.getAwardReason());
		}
		legalConsumer.setAwardAgainstAmt(consumerForumDTO.getAmount());
		legalConsumer.setInterestHistory(consumerForumDTO.getInterestHistory());
		legalConsumer.setLitigationCost(consumerForumDTO.getLitigationCost());
		legalConsumer.setCompensation(consumerForumDTO.getCompensation());
		if(consumerForumDTO.getCaseUpdate()!=null){
			MastersValue caseStatusID = masterService.getMaster(consumerForumDTO.getCaseUpdate().getId());
			legalConsumer.setCaseStatusId(caseStatusID);
		}
		legalConsumer.setUpdcaseFlag(consumerForumDTO.getUpdateOftheCase());
		
		legalConsumer.setWonCase(consumerForumDTO.getWonCase());
		legalConsumer.setWonCaseReason(consumerForumDTO.getReason());
		legalConsumer.setSettlementDate(consumerForumDTO.getSettlementDate());
		legalConsumer.setRecUpdRemarks(consumerForumDTO.getWonRecordLastUpdatedRemarks());
		legalConsumer.setCaseUpdRemarks(consumerForumDTO.getRecordLastUpdatedRemarks());
		legalConsumer.setNextHearingDate(consumerForumDTO.getDateofNextHearing());
		legalConsumer.setFrshPrevHearingDt(consumerForumDTO.getFreshPreviousDateHearing());
		
		legalConsumer.setVakalathFieldDate(consumerForumDTO.getVakalathFieldDate());
		legalConsumer.setAppealPrefferedBy(consumerForumDTO.getAppealPrefferedBy());
		legalConsumer.setAwdAdvocateName(consumerForumDTO.getAdvocateName());
		legalConsumer.setLegalType(SHAConstants.LEGAL_CONSUMER);
		if(legalConsumer.getKey()!=null){
			legalConsumer.setModifiedBy(consumerForumDTO.getUserName());
			legalConsumer.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(legalConsumer);
		}else{
			legalConsumer.setCreatedBy(consumerForumDTO.getUserName());
			legalConsumer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(legalConsumer);
		}
		entityManager.flush();
		//entityManager.merge(legal);
		//entityManager.flush();
	}

	private LegalConsumer saveIntimationValues(IntimationDetailsDTO intimationDetailsDTO,
			LegalConsumer legalConsumer) {
		legalConsumer.setIntimationNumber(intimationDetailsDTO.getIntimationNo());
		legalConsumer.setFinancialYear(intimationDetailsDTO.getFinancialYear());
		legalConsumer.setPolicyNumber(intimationDetailsDTO.getPolicyNo());
		legalConsumer.setProvisionAmount(intimationDetailsDTO.getProvisionAmt());
		legalConsumer.setProductName(intimationDetailsDTO.getProductNo());
		legalConsumer.setInsuredName(intimationDetailsDTO.getInsuredName());
		legalConsumer.setDiagnosis(intimationDetailsDTO.getDiagnosis());
		if(intimationDetailsDTO.getRepudiation()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getRepudiation().getId());
			legalConsumer.setRepudiationId(repudiation);
		}
		if(intimationDetailsDTO.getReceivedFrom()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getReceivedFrom().getId());
			legalConsumer.setReceievedFrom(repudiation);
		}

		return legalConsumer;
	}

	public LegalConsumer getLegalByIntimationNumberAndType(String intimationNumber,String legalType) {
		List<LegalConsumer> resultLegal = null;
		Query findByIntimationNum = entityManager.createNamedQuery("LegalConsumer.findByIntimationNumberAndType");
		findByIntimationNum.setParameter("intimationNumber", intimationNumber);		
		findByIntimationNum.setParameter("legalType", legalType);

		try {
			resultLegal = (List<LegalConsumer>) findByIntimationNum.getResultList();
			
			if(resultLegal != null && !resultLegal.isEmpty()){
				return resultLegal.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
