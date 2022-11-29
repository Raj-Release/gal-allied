package com.shaic.claim.intimation.viewdetails.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.intimation.search.SearchIntimationFormDto;

@SuppressWarnings("serial")
@ViewInterface(SearchViewDetailView.class)
public class SearchViewDetailPresenter extends AbstractMVPPresenter<SearchViewDetailView>{
	
	public static final String RESET_SEARCH_VIEW = "Reset Search Fields for view details"; 
	public static final String SUBMIT_SEARCH = "Search Submit for view details";
	public static final String DISABLE_SEARCH_FIELDS = "Disable Search Filters for view details";
	public static final String EDIT_INTIMATION_SCREEN = "Edit Intimation Screen for view details";
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
	}
	
	protected void showSearchViewDetailIntimationTable(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		view.showSearchViewDetailIntimationTable((SearchIntimationFormDto) parameters.getPrimaryParameter());
		//		view.showSearchIntimationTable((Map<String,Object>) parameters.getPrimaryParameter());
	}
	
	protected void showSearchIntimation(@Observes @CDIEvent(RESET_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetSearchIntimationView();
		System.out.println("view Entered called");
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
