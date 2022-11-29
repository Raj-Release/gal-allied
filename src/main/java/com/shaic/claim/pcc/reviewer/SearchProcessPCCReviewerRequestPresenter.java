package com.shaic.claim.pcc.reviewer;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;
import com.shaic.claim.pcc.wizard.SearchPccView;

@ViewInterface(SearchPccReviewerView.class)
public class SearchProcessPCCReviewerRequestPresenter extends AbstractMVPPresenter<SearchPccReviewerView> {
	
  /**
	 * 
	 */
	private static final long serialVersionUID = -4135251169373533737L;

   public static final String SEARCH_PCC_REVIEWER_RQUEST = "search_pcc_reviewer_rquest";
	
	@EJB
	private PccReviewerRequestService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PCC_REVIEWER_RQUEST) final ParameterDTO parameters) {
		
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
