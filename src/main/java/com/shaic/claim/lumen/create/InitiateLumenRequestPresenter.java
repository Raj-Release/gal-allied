package com.shaic.claim.lumen.create;

import java.text.ParseException;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@SuppressWarnings("serial")
@ViewInterface(InitiateLumenRequestView.class)
public class InitiateLumenRequestPresenter extends AbstractMVPPresenter<InitiateLumenRequestView>{
	
	public static final String DO_LUMEN_SEARCH = "dolumensearch";
	public static final String DO_LUMEN_POLICY_SEARCH = "dolumenpolicysearch";
	
	@Inject
	LumenDbService lumenService;
	
	@Inject
	DBCalculationService dbService;

	@Override
	public void viewEntered() {
		System.out.println("InitiateLumenRequest Presenter Called...");
	}
	
	public void handleSearch(@Observes @CDIEvent(DO_LUMEN_SEARCH) final ParameterDTO parameters) {
		LumenSearchFormDTO searchFormDTO = (LumenSearchFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		Page<LumenSearchResultTableDTO> searchResult = lumenService.getSearchResult(searchFormDTO,userName);
		
		List<LumenSearchResultTableDTO> lumenSerachResultList = searchResult.getPageItems();
		
		if(lumenSerachResultList != null && !lumenSerachResultList.isEmpty()) {
			for (LumenSearchResultTableDTO lumenSearchResultTableDTO : lumenSerachResultList) {
				lumenSearchResultTableDTO.setInsuredDeceasedFlag(dbService.getInsuredPatientStatus(lumenSearchResultTableDTO.getClaim().getIntimation().getPolicy().getKey(), lumenSearchResultTableDTO.getClaim().getIntimation().getInsured().getKey()));
			}
		}
		
		view.renderTable(searchResult);
	}
	
	public void handlePolicySearch(@Observes @CDIEvent(DO_LUMEN_POLICY_SEARCH) final ParameterDTO parameters) throws ParseException {
		LumenSearchFormDTO searchFormDTO = (LumenSearchFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		view.renderPolicyTable(lumenService.getPolicySearchResult(searchFormDTO,userName));
	}
}
