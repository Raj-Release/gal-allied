package com.shaic.claim.lumen.searchcoordinator;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;

@SuppressWarnings("serial")
@ViewInterface(SearchLumenCoordinatorRequestView.class)
public class SearchLumenCoordinatorRequestPresenter extends AbstractMVPPresenter<SearchLumenCoordinatorRequestView>{
	
	public static final String DO_LUMEN_REQ_COORDINATOR_SEARCH = "dolumencoordinatorreqsearch";
	
	@Inject
	LumenDbService lumenService;

	@Override
	public void viewEntered() {
		System.out.println("SearchLumenCoordinatorRequest Presenter Called...");
	}
	
	public void handleSearch(@Observes @CDIEvent(DO_LUMEN_REQ_COORDINATOR_SEARCH) final ParameterDTO parameters) {
		LumenSearchReqFormDTO searchReqFormDTO = (LumenSearchReqFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		view.renderTable(lumenService.getLumenProcessSearchData(searchReqFormDTO,userName,"ProcessCoordinatorSearch"));
	}
	
}
