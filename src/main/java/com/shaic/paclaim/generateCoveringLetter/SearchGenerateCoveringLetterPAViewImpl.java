package com.shaic.paclaim.generateCoveringLetter;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.MVPView;

import com.shaic.arch.table.Page;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@UIScoped
@CDIView(value = MenuItemBean.SEARCH_GENERATE_PA_COVERING_LETTER)
public class SearchGenerateCoveringLetterPAViewImpl extends AbstractMVPView implements SearchGenerateCoveringLetterPAView, View {

		 
	@Inject private Instance<SearchGenerateCoveringLetterPA> generatePACoveringLetterUIInstance;
	
	private SearchGenerateCoveringLetterPA generatePACoveringLetterUI;
    
	@Inject private Instance<MVPView> views;
	
	@EJB IntimationService intimationService;
	@EJB ClaimService claimService;
	@EJB PolicyService policyService;
	
		@PostConstruct
		public void initView() {
			addStyleName("view");
			setSizeFull();
			generatePACoveringLetterUI = generatePACoveringLetterUIInstance.get();
			generatePACoveringLetterUI.init();
			setCompositionRoot(generatePACoveringLetterUI);
			resetView();
			resetSearchResultTableValues();
		}

		@Override
		public void showClaimSearchTable(Page<GenerateCoveringLetterSearchTableDto> resultTableList) {
			
			if(null != resultTableList && resultTableList.getPageItems() != null &&  !resultTableList.getPageItems().isEmpty()){
				generatePACoveringLetterUI.showSearchResultTable(resultTableList);
				
			}
			else{
				
				generatePACoveringLetterUI.showInformation("No Result Found for Search");

			 return;
		   }
		}

		@Override
		public void resetView() {
			if(generatePACoveringLetterUI != null)
			{
				generatePACoveringLetterUI.init();
			}	
			generatePACoveringLetterUI.resetValues();
		}

		@Override
		public void doSearch() {
			
			Map<String,Object> filters = generatePACoveringLetterUI.getSearchFilter();
			
			filters.put(BPMClientContext.USERID,UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			filters.put(BPMClientContext.PASSWORD,UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());
			filters.put(BPMClientContext.USER_OBJECT, UI.getCurrent().getSession().getAttribute(BPMClientContext.USER_OBJECT));
			filters.put("pageable",generatePACoveringLetterUI.getGenCovLetterSearchTable().getPageable());
	        
			fireViewEvent(SearchGeneratePACoveringLetterPresenter.SUBMIT_PA_COVERING_LETTER_SEARCH,filters);
				
		}

		@Override
		public void resetSearchResultTableValues() {

			generatePACoveringLetterUI.resetComponents();
			generatePACoveringLetterUI.getGenCovLetterSearchTable().getPageable().setPageNumber(1);
			
			generatePACoveringLetterUI.getGenCovLetterSearchTable().removeRow();
			
		}
		
		
}
