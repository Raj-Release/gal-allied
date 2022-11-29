package com.shaic.claim.pcc.zonalMedicalHead;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;

@SuppressWarnings("serial")
@ViewInterface(SearchPccZonalMedicalHeadView.class)
public class SearchProcessPCCZonalmedicalHeadPresenter extends
		AbstractMVPPresenter<SearchPccZonalMedicalHeadView> {

	
    public static final String SEARCH_PCC_ZONAL_MEDICAL_HEAD = "search_pcc_zonal_medical_head";
	
	@EJB
	private ZonalMedicalHeadRequestService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PCC_ZONAL_MEDICAL_HEAD) final ParameterDTO parameters) {
		
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
