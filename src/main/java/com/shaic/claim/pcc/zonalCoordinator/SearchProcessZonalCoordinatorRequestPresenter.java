package com.shaic.claim.pcc.zonalCoordinator;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;
import com.shaic.claim.pcc.wizard.SearchPccView;

@ViewInterface(SearchPccZonalCoordinatorView.class)
public class SearchProcessZonalCoordinatorRequestPresenter extends AbstractMVPPresenter<SearchPccZonalCoordinatorView> {
	
public static final String SEARCH_ZONAL_COORDINATOR_RQUEST = "search_zonal_coordinator_rquest";
	
	@EJB
	private ZonalCoordinatorRequestService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_ZONAL_COORDINATOR_RQUEST) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestFormDTO searchFormDTO = (SearchProcessPCCRequestFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
