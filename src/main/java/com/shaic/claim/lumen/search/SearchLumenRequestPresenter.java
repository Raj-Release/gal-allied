package com.shaic.claim.lumen.search;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;

@SuppressWarnings("serial")
@ViewInterface(SearchLumenRequestView.class)
public class SearchLumenRequestPresenter extends AbstractMVPPresenter<SearchLumenRequestView>{
	
	public static final String DO_LUMEN_REQ_SEARCH = "dolumenreqsearch";
	public static final String SHOW_VIEW_LUMEN_TRIALS = "showlumentrialview";
	
	@Inject
	LumenDbService lumenService;

	@Inject
	DBCalculationService dbService;
	@Override
	public void viewEntered() {
		System.out.println("SearchLumenRequest Presenter Called...");
	}
	
	public void handleSearch(@Observes @CDIEvent(DO_LUMEN_REQ_SEARCH) final ParameterDTO parameters) {
		LumenSearchReqFormDTO searchReqFormDTO = (LumenSearchReqFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		Page<LumenRequestDTO> lumenSearchData = lumenService.getLumenSearchData(searchReqFormDTO,userName);
		
		if(lumenSearchData != null 
				&& lumenSearchData.getPageItems() != null 
				&& !lumenSearchData.getPageItems().isEmpty()) {
			List<LumenRequestDTO> lumenResultList = lumenSearchData.getPageItems();
			for (LumenRequestDTO iterableDto : lumenResultList) {
				iterableDto.setPatientStatus(dbService.getInsuredPatientStatus(iterableDto.getIntimation().getPolicy().getKey(), iterableDto.getIntimation().getInsured().getKey()));
			}
		}
		
		view.renderTable(lumenSearchData);
	}
	
	public void showLumenTrailsPopup(@Observes @CDIEvent(SHOW_VIEW_LUMEN_TRIALS) final ParameterDTO parameters) {
		LumenRequestDTO searchReqDTO = (LumenRequestDTO) parameters.getPrimaryParameter();
		view.showLumenTrails(searchReqDTO);
	}
}
