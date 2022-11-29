package com.shaic.claim.cvc.postprocess;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.cvc.SearchCVCFormDTO;
import com.shaic.claim.cvc.SearchCVCService;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;





@ViewInterface(SearchPostProcessCVCView.class)
public class SearchPostClaimProcessCVCPresenter extends AbstractMVPPresenter<SearchPostProcessCVCView>{

private static final long serialVersionUID = -5504472929540762973L;
	
	
	public static final String SEARCH_BUTTON_CLICK_POST_PROCESS = "search_for_CVC_Post_Process_Claim";
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PreauthService preauthService;
	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_POST_PROCESS) final ParameterDTO parameters) {
		
		SearchCVCFormDTO searchFormDTO = (SearchCVCFormDTO) parameters.getPrimaryParameter();		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		SearchCVCTableDTO search = new SearchCVCTableDTO();
		Intimation intimation = intimationService.getIntimationByNo(searchFormDTO.getIntimationNo());
		if(intimation != null && intimation.getProcessClaimType() != null 
				&& intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
			Claim claim = intimationService.getClaimforIntimation(intimation.getKey());
			if(claim != null){
			String[]  splitIntm = intimation.getIntimationId().split("/");
			String year = splitIntm[1];
			search.setIntimationKey(intimation.getKey());
			search.setUsername(userName);
			if(year.equals(searchFormDTO.getYear())){
				if(claim != null){
					search.setClaimKey(claim.getKey());
					if(claim.getClaimType().getKey().equals(401l)){
						search.setClaimType("C");
					}else {
						search.setClaimType("R");
					}
					Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
					if(reimbursement == null){
						Preauth preauth = preauthService.getLatestPreauthByClaimKey(claim.getKey());
						Boolean isPreauthEnhancementApproved = reimbursementService.isAlreadyPreauthApprovedForPostCVC(claim.getKey());
						if(preauth != null && isPreauthEnhancementApproved){
							search.setTransactionKey(preauth.getKey());
							search.setClaimType("C");
						} else {
							search.setIntimationKey(null);
						}
					}else {
						Boolean isMAApproved = reimbursementService.isAlreadyMAApprovedForPostCVC(claim.getKey());
						if(isMAApproved){
							search.setTransactionKey(reimbursement.getKey());
							search.setClaimType("R");
						} else {
							search.setIntimationKey(null);
						}
					}
				}
			} else {
				search.setIntimationKey(null);
			}
		} else {
			search.setIntimationKey(null);
		}
			
		}
		/*if(intimation == null){
			searchFormDTO.setIntimationK;
		}*/
//		SearchCVCTableDTO search = cvcSearchService.search(searchFormDTO,userName,passWord);
				
		view.list(search); 
	}
	

	@Override
	public void viewEntered() {

	}
}
