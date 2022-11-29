package com.shaic.claim.lumen.searchlevelone;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;

@SuppressWarnings("serial")
@ViewInterface(SearchLumenLevelOneRequestView.class)
public class SearchLumenLevelOneRequestPresenter extends AbstractMVPPresenter<SearchLumenLevelOneRequestView>{
	
	public static final String DO_LUMEN_REQ_LEVEL_I_SEARCH = "dolumenlevelOnereqsearch";
	
	@Inject
	LumenDbService lumenService;

	@Override
	public void viewEntered() {
		System.out.println("SearchLumenLevelOneRequest Presenter Called...");
	}
	
	public void handleSearch(@Observes @CDIEvent(DO_LUMEN_REQ_LEVEL_I_SEARCH) final ParameterDTO parameters) {
		LumenSearchReqFormDTO searchReqFormDTO = (LumenSearchReqFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		view.renderTable(lumenService.getLumenProcessSearchData(searchReqFormDTO,userName,"ProcessLevelOneSearch"));
	}
	
}
