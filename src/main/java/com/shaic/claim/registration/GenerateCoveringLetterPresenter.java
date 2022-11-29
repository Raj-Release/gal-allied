package com.shaic.claim.registration;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.GenerateCoveringLetterService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@SuppressWarnings("serial")
@ViewInterface(GenerateLetterView.class)
public class GenerateCoveringLetterPresenter extends AbstractMVPPresenter<GenerateLetterView> {
		
	public static final String RESET_CLAIM_SEARCH_VIEW = "Reset Claim Search Fields";
	public static final String INITIATE_SEARCH = "Initiate Search";
	public static final String CANCEL_CLAIM_LETTER_GENERATION = "Cancel Claim Covering Letter";
	public static final String SUBMIT_COVERING_LETTER_SEARCH = "Submit Claim Covering letter Search";
	public static final String VIEW_CLAIM_COVERINGLETTER = "View Claim Covering Letter";
	public static final String GENERATE_LETTER = "Generate Covering Letter";
	public static final String SET_BPM_OUT_COME = "set BPM Out Come";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private GenerateCoveringLetterService generateCoverinLetterSrevice;
	

	@Override
	public void viewEntered() {
	
		
	} 
	
	protected void initiateSearch(@Observes @CDIEvent(INITIATE_SEARCH) final ParameterDTO parameters) {
		view.doSearch();
	}
	
	protected void setBpmOutCome(@Observes @CDIEvent(SET_BPM_OUT_COME) final ParameterDTO parameters) {
		claimService.setOutComeForCoveringLetterTask(parameters.getPrimaryParameter());
	}
	
	protected void showClaimSearchViewCoveringLetter(@Observes @CDIEvent(MenuItemBean.GENERATE_COVERINGLETTER) final ParameterDTO parameters) {
//		view.showSearchCoveringLetterView("CoveringLetter");
		view.initView();
	}
	protected void showClaimSearchViewforRejectionLetter(@Observes @CDIEvent(MenuItemBean.GENERATE_REJECTIONLETTER) final ParameterDTO parameters) {
		view.showSearchCoveringLetterView("RejectionLetter");
	}
	
	protected void showClaimSearchTable(@Observes @CDIEvent(SUBMIT_COVERING_LETTER_SEARCH) final ParameterDTO parameters) {
		Map<String,Object> queryFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		
//		queryFilter.put("lobType", SHAConstants.HEALTH_LOB_FLAG);
		queryFilter.put("lob",SHAConstants.HEALTH_LOB);
		GenerateCoveringLetterSearchDto searchDto = new GenerateCoveringLetterSearchDto();
		searchDto.setQueryFilter(queryFilter);
		searchDto.setPageable(queryFilter != null && queryFilter.containsKey("pageable") ? (Pageable)queryFilter.get("pageable") : null );
		Page<GenerateCoveringLetterSearchTableDto> coveringLetterTableContainer = generateCoverinLetterSrevice.getClaimsToGenerateCoveringLetter(searchDto);
		
		List<GenerateCoveringLetterSearchTableDto> searchResultList = coveringLetterTableContainer.getPageItems();
		
		if(coveringLetterTableContainer != null && !searchResultList.isEmpty()){
			for (GenerateCoveringLetterSearchTableDto generateCoveringLetterSearchTableDto : searchResultList) {
				Intimation intimation = intimationService.getIntimationByKey(generateCoveringLetterSearchTableDto.getClaimDto().getNewIntimationDto().getKey());
				NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);
				generateCoveringLetterSearchTableDto.getClaimDto().setNewIntimationDto(newIntimationDto);
			}
			
		}
		
		view.showClaimSearchTable(coveringLetterTableContainer);
	}
	
	protected void cancelClaimCoveringLetter(@Observes @CDIEvent(CANCEL_CLAIM_LETTER_GENERATION) final ParameterDTO parameters) {
		Claim a_claim = (Claim) parameters.getPrimaryParameter();
		view.cancelCoveringLetter(a_claim);
	}
	
	protected void resetSerchClaimCoveringLetter(@Observes @CDIEvent(RESET_CLAIM_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetClaimSearchCoveringLetterGeneration();
	}
	
	protected void showGenrateCoveringLetterDetailView(@Observes @CDIEvent(VIEW_CLAIM_COVERINGLETTER) final ParameterDTO parameters) {
		
		//GenerateCoveringLetterSearchTableDto tableDTO = (GenerateCoveringLetterSearchTableDto) parameters.getPrimaryParameter();
		/*Claim objClaim = claimService.getClaimByKey(tableDTO.getClaimDto().getKey());
		 if(null != objClaim)
		 {
			 if((SHAConstants.YES_FLAG).equalsIgnoreCase(objClaim.getLegalFlag()))
				{
				 	tableDTO.setIsLegal(true);
				}
		 }*/
		 view.showGenrateCoveringLetterDetailView(parameters);
//		  Page.getCurrent().setUriFragment("!" + CLICK_GENERATE_COVERING_LETTER_VIEW + "/" + a_claimDto.getKey());
//		  Page.getCurrent().reload();
	}
	
	protected void genrateCoveringLetter(@Observes @CDIEvent(GENERATE_LETTER) final ParameterDTO parameters) {
		 ClaimDto a_claimDto = (ClaimDto) parameters.getPrimaryParameter();
		 
		 view.generateCoveringLetter(a_claimDto);
//		  Page.getCurrent().setUriFragment("!" + CLICK_GENERATE_COVERING_LETTER_VIEW + "/" + a_claimDto.getKey());
//		  Page.getCurrent().reload();
	}
	
}
