package com.shaic.claim.legal.processconsumerforum.page.advocatenotice;


import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.CaseDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.ConsumerForumDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumService;
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.OmbudsmanDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.SearchProcessOmbudsmanService;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalAdvocate;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;

@Stateless
public class SearchProcessAdvocateNoticeService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchProcessOmbudsmanService ombudsmanService;
	
	@EJB
	private SearchProcessConsumerForumService consumerForumService;

	public void saveLegalAdvocateNotice(AdvocateNoticeDTO advocateNoticeDTO) {
		
		LegalAdvocate legalAdvocate = null; 
		if(advocateNoticeDTO.getIntimationDetailsDTO()!=null && advocateNoticeDTO.getIntimationDetailsDTO().getIntimationNo()!=null){
			legalAdvocate = getLegalByIntimationNumberAndType(advocateNoticeDTO.getIntimationDetailsDTO().getIntimationNo(),SHAConstants.LEGAL_ADVOCATE_NOTICE);
		}
		if(legalAdvocate == null){
			legalAdvocate = new LegalAdvocate();
		}
		legalAdvocate.setActiveStatus(1L);
		IntimationDetailsDTO intimationDetailsDTO = advocateNoticeDTO.getIntimationDetailsDTO();
		if(intimationDetailsDTO!=null){
			saveIntimationValues(intimationDetailsDTO, legalAdvocate);
			legalAdvocate.setClaimKey(intimationDetailsDTO.getClaimKey());
		}
		CaseDetailsDTO caseDetailsDTO = advocateNoticeDTO.getCaseDetailsDTO();
		if(caseDetailsDTO!=null){
			legalAdvocate.setAdvocateName(caseDetailsDTO.getAdVocateName());
			legalAdvocate.setAdvnoticeDate(caseDetailsDTO.getAdvocateNoticeDate());
			legalAdvocate.setLimitDate(caseDetailsDTO.getLimitationTime());
			legalAdvocate.setComplainceDate(caseDetailsDTO.getComplainceDate());
			legalAdvocate.setRejectFlag(caseDetailsDTO.getIsStandRejection());
			legalAdvocate.setRepliedDate(caseDetailsDTO.getReplySentDate());
			legalAdvocate.setRejectReason(caseDetailsDTO.getRejectionReason());
			
			legalAdvocate.setSettleDate(caseDetailsDTO.getSettleDate());
			legalAdvocate.setSettleAmt(caseDetailsDTO.getSettleAmt());
			legalAdvocate.setSettleReason(caseDetailsDTO.getSettleReason());
			legalAdvocate.setLegalLockFlag(caseDetailsDTO.getLegalLock());
			if(caseDetailsDTO.getLegalLock()!=null){
				if(caseDetailsDTO.getLegalLock()){
					Claim claimKey = legalAdvocate.getClaimKey();
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
					Claim claimKey = legalAdvocate.getClaimKey();
					if(claimKey.getLegalFlag()!=null && claimKey.getLegalFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						claimKey.setLegalFlag(SHAConstants.N_FLAG);
						entityManager.merge(claimKey);
					}else if(claimKey.getLegalFlag()==null){
						claimKey.setLegalFlag(SHAConstants.YES_FLAG);
						claimKey.setLegalClaim(SHAConstants.YES_FLAG);
						entityManager.merge(claimKey);
					}
					
				}}
			//legalAdvocate.setSettleFlag(caseDetailsDTO.getIsStandRejection());
			legalAdvocate.setNoticerecDate(caseDetailsDTO.getNoticeRecievedDate());
			
			legalAdvocate.setAdvstatus(caseDetailsDTO.getStatus());
			if(caseDetailsDTO.getMovedTo()!=null){
				MastersValue movedTO = masterService.getMaster(caseDetailsDTO.getMovedTo().getId());
				legalAdvocate.setMovedId(movedTO);
				if(movedTO.getValue().equalsIgnoreCase("Ombudsman")){
					OmbudsmanDetailsDTO ombudsmanDetailsDTO = new OmbudsmanDetailsDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7022L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					ombudsmanDetailsDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					ombudsmanService.saveLegalOmbudsman(ombudsmanDetailsDTO);
					legalAdvocate.setActiveStatus(0L);
				}
				if(movedTO.getValue().equalsIgnoreCase("Consumer Forum")){
					ConsumerForumDTO consumerForumDTO = new ConsumerForumDTO();
					SelectValue selectValue= new SelectValue();
					selectValue.setId(7022L);
					intimationDetailsDTO.setReceivedFrom(selectValue);
					consumerForumDTO.setIntimationDetailsDTO(intimationDetailsDTO);
					consumerForumService.saveLegalConsumer(consumerForumDTO);
					legalAdvocate.setActiveStatus(0L);
				}
				
			}
			if(caseDetailsDTO.getPendingValue()!=null){
				MastersValue pendingId = masterService.getMaster(caseDetailsDTO.getPendingValue().getId());
				legalAdvocate.setPendingLevelId(pendingId);
			}
		}
		legalAdvocate.setLegalType(SHAConstants.LEGAL_ADVOCATE_NOTICE);
		if(legalAdvocate.getKey()!=null){
			legalAdvocate.setModifiedBy(advocateNoticeDTO.getUserName());
			legalAdvocate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(legalAdvocate);
		}else{
			legalAdvocate.setCreatedBy(advocateNoticeDTO.getUserName());
			legalAdvocate.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(legalAdvocate);
		}
		entityManager.flush();
		//entityManager.merge(legal);
		//entityManager.flush();
	}

	private LegalAdvocate saveIntimationValues(IntimationDetailsDTO intimationDetailsDTO,
			LegalAdvocate legalAdvocate) {
		legalAdvocate.setIntimationNumber(intimationDetailsDTO.getIntimationNo());
		legalAdvocate.setFinancialYear(intimationDetailsDTO.getFinancialYear());
		legalAdvocate.setPolicyNumber(intimationDetailsDTO.getPolicyNo());
		legalAdvocate.setProvisionAmount(intimationDetailsDTO.getProvisionAmt());
		legalAdvocate.setProductName(intimationDetailsDTO.getProductNo());
		legalAdvocate.setInsuredName(intimationDetailsDTO.getInsuredName());
		if(intimationDetailsDTO.getRepudiation()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getRepudiation().getId());
			legalAdvocate.setRepudiationId(repudiation);
		}
		if(intimationDetailsDTO.getReceivedFrom()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getReceivedFrom().getId());
			legalAdvocate.setReceievedFrom(repudiation);
		}
		return legalAdvocate;
	}

	public LegalAdvocate getLegalByIntimationNumberAndType(String intimationNumber,String legalType) {
		List<LegalAdvocate> resultLegal = null;
		Query findByIntimationNum = entityManager.createNamedQuery("LegalAdvocate.findByIntimationNumberAndType");
		findByIntimationNum.setParameter("intimationNumber", intimationNumber);		
		findByIntimationNum.setParameter("legalType", legalType);

		try {
			resultLegal = (List<LegalAdvocate>) findByIntimationNum.getResultList();
			
			if(resultLegal != null && !resultLegal.isEmpty()){
				return resultLegal.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
