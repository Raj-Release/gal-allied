package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.LegalOmbudsman;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;

@ViewInterface(SearchProcessOmbudsmanView.class)
public class SearchProcessOmbudsmanPresenter extends AbstractMVPPresenter<SearchProcessOmbudsmanView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchProcessOmbudsmanService ombudsmanService;
	
	public static final String LEGAL_SAVE_OMBUDSMAN = "legal_save_ombudsman";
	
	public static final String POPULATE_FIELD_VALUES_OMBUDSMAN = "Populate Field Values ombudsman";

	protected static final String LEGAL_CONTACT_OMBUDSMAN = "legal contact ombudsman";
	
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
	public void save(@Observes @CDIEvent(LEGAL_SAVE_OMBUDSMAN) final ParameterDTO parameters) {
		OmbudsmanDetailsDTO ombudsmanDetailsDTO = (OmbudsmanDetailsDTO) parameters.getPrimaryParameter();
		ombudsmanService.saveLegalOmbudsman(ombudsmanDetailsDTO);
		view.buildSuccessLayout();
		//System.out.println(consumerForumDTO);
	}
	
	@SuppressWarnings("static-access")
	public void getContact(@Observes @CDIEvent(LEGAL_CONTACT_OMBUDSMAN) final ParameterDTO parameters) {
		SelectValue ombudsmanDetailsDTO = (SelectValue) parameters.getPrimaryParameter();
		String contactOmbudsman = ombudsmanService.getContactOmbudsman(ombudsmanDetailsDTO);
		if(contactOmbudsman!=null){
			view.setOmbudsmanContact(contactOmbudsman);
		}
		//System.out.println(consumerForumDTO);
	}
	
	
	@SuppressWarnings("unchecked")
	public void populateFieldValues(@Observes @CDIEvent(POPULATE_FIELD_VALUES_OMBUDSMAN) final ParameterDTO parameters) {
		SearchProcessConsumerForumTableDTO dto  = (SearchProcessConsumerForumTableDTO) parameters.getPrimaryParameter();
		
		if (dto != null) {
			Claim claimsByIntimationNumber = claimService.getClaimsByIntimationNumber(dto.getIntimationNumber());
			LegalOmbudsman legalOmbudsman =ombudsmanService.getLegalByIntimationNumberAndType(dto.getIntimationNumber() , SHAConstants.LEGAL_OMBUDSMAN);
			String diagnosisName = getDiagnosisName(claimsByIntimationNumber);
			if(claimsByIntimationNumber != null) {
				view.populateFiledValues(claimsByIntimationNumber,legalOmbudsman, diagnosisName);
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
