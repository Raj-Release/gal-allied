package com.shaic.claim.rod.citySearchCriteria;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;



@ViewInterface(CitySearchCriteriaView.class)
public class CitySearchCriteriaPresenter  extends AbstractMVPPresenter<CitySearchCriteriaView>{
	
	@EJB
	private CitySearchCriteriaService viewSearchCriteriaService;

	public static final String PAYABLE_SEARCH_CRITERIA = "Search Payable Ceiteria";
	
	
	public void searchClick(
			@Observes @CDIEvent(PAYABLE_SEARCH_CRITERIA) final ParameterDTO parameters) {
		try
		{
		String name = (String) parameters
				.getPrimaryParameter();
	

			view.setTableValues(viewSearchCriteriaService.search(name != null ? name.toUpperCase() : ""));
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
