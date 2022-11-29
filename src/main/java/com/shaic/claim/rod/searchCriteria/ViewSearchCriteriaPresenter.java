package com.shaic.claim.rod.searchCriteria;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;





@ViewInterface(ViewSearchCriteriaView.class)
public class ViewSearchCriteriaPresenter extends AbstractMVPPresenter<ViewSearchCriteriaView> {

	public static final String SEARCH_BUTTON_CLICK = "Search Ceiteria";
	
	@EJB
	private ViewSearchCriteriaService viewSearchCriteriaService;
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		try
		{
		ViewSearchCriteriaFormDTO searchFormDTO = (ViewSearchCriteriaFormDTO) parameters
				.getPrimaryParameter();
		

			view.list(viewSearchCriteriaService.search(searchFormDTO));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
