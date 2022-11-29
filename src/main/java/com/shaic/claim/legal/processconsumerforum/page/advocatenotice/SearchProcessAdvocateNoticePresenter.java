package com.shaic.claim.legal.processconsumerforum.page.advocatenotice;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.LegalAdvocate;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;


@ViewInterface(SearchProcessAdvocateNoticeView.class)
public class SearchProcessAdvocateNoticePresenter extends AbstractMVPPresenter<SearchProcessAdvocateNoticeView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchProcessAdvocateNoticeService advocateNoticeService;
	
	public static final String LEGAL_SAVE_ADVOCATE_NOTICE = "legal_save_advocate_notice";
	
	public static final String POPULATE_FIELD_VALUES_ADVOCATE_NOTICE = "Populate Field Values advocate notice";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private ReimbursementService  reimbursementService;
	
	@EJB
	private PreauthService preauthService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	public void save(@Observes @CDIEvent(LEGAL_SAVE_ADVOCATE_NOTICE) final ParameterDTO parameters) {
		AdvocateNoticeDTO advocateNoticeDTO = (AdvocateNoticeDTO) parameters.getPrimaryParameter();
		advocateNoticeService.saveLegalAdvocateNotice(advocateNoticeDTO);
		view.buildSuccessLayout();
		//System.out.println(consumerForumDTO);
	}
	
	@SuppressWarnings("unchecked")
	public void populateFieldValues(@Observes @CDIEvent(POPULATE_FIELD_VALUES_ADVOCATE_NOTICE) final ParameterDTO parameters) {
		SearchProcessConsumerForumTableDTO dto  = (SearchProcessConsumerForumTableDTO) parameters.getPrimaryParameter();
		
		if (dto != null) {
			Claim claimsByIntimationNumber = claimService.getClaimsByIntimationNumber(dto.getIntimationNumber());
			LegalAdvocate legalAdvocate =advocateNoticeService.getLegalByIntimationNumberAndType(dto.getIntimationNumber() , SHAConstants.LEGAL_ADVOCATE_NOTICE);
			String diagnosisName = getDiagnosisName(claimsByIntimationNumber);
			if(claimsByIntimationNumber != null) {
				view.populateFiledValues(claimsByIntimationNumber,legalAdvocate,diagnosisName);
			}
			
		}
	}
	
	private String getDiagnosisName(Claim claimsByIntimationNumber) {
		String diagnosisForPreauthByKey=null;
		List<Reimbursement> reimbursementByClaimKey = reimbursementService.getReimbursementByClaimKey(claimsByIntimationNumber.getKey());
		if(reimbursementByClaimKey!=null && reimbursementByClaimKey.size()>0){
			Reimbursement reimbursement = reimbursementByClaimKey.get(0);
			if(reimbursement!=null){
				Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(reimbursement.getKey());
				diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursementByKey.getKey());
				//legalOmbudsman.setDiagnosis(diagnosisForPreauthByKey);
				if(diagnosisForPreauthByKey!=null){
					Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimsByIntimationNumber.getKey());
					if(latestPreauthByClaim!=null){
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(latestPreauthByClaim.getKey());
						//legalOmbudsman.setDiagnosis(diagnosisForPreauthByKey);
					}
				}
			}
		}else{
			Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimsByIntimationNumber.getKey());
			if(latestPreauthByClaim!=null){
				 diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(latestPreauthByClaim.getKey());
			}
		}
		return diagnosisForPreauthByKey;
	}
}
