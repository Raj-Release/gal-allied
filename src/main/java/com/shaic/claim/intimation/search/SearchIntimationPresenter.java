package com.shaic.claim.intimation.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(SearchIntimationView.class)
public class SearchIntimationPresenter extends AbstractMVPPresenter<SearchIntimationView> {
	
	
	public static final String RESET_SEARCH_VIEW = "Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "Edit Intimation Screen";
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
	}
	
	protected void showSearchIntimationTable(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		view.showSearchIntimationTable((SearchIntimationFormDto) parameters.getPrimaryParameter());
		//		view.showSearchIntimationTable((Map<String,Object>) parameters.getPrimaryParameter());
	}
	
	protected void showSearchIntimation(@Observes @CDIEvent(RESET_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetSearchIntimationView();
	}
	
	protected void hideSearchfields(@Observes @CDIEvent(DISABLE_SEARCH_FIELDS) final ParameterDTO parameters) {
		view.hideSearchFields((String)parameters.getPrimaryParameter());
	}
	
	protected void editIntimationDetails(@Observes @CDIEvent(EDIT_INTIMATION_SCREEN) final ParameterDTO parameters) {
	
//	  IntimationsDto a_intimationDto = (IntimationsDto) parameters.getPrimaryParameter();
//	  
//	  
//	  Page.getCurrent().setUriFragment("!" + MenuItemBean.NEW_INTIMATION + "/" + a_intimationDto.getPolicyDto().getPolicySysId()+"/"+a_intimationDto.getKey());
//	  Page.getCurrent().reload();
	}
	
}
