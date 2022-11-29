package com.shaic.claim.intimation.unlockompintimation;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.naming.NamingException;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBFormDTO;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBTableDTO;
import com.shaic.domain.ReferenceTable;


@ViewInterface(SearchUnlockOMPIntimationDBView.class)
public class SearchUnlockOMPIntimationDBPresenter extends AbstractMVPPresenter<SearchUnlockOMPIntimationDBView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_OMP_BUTTON_CLICK = "search for unlock omp intimation";
	
	@EJB
	private SearchUnlockOMPIntimationDBService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_OMP_BUTTON_CLICK) final ParameterDTO parameters) throws NamingException {
		
		SearchUnlockIntimationDBFormDTO searchFormDTO = (SearchUnlockIntimationDBFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		Page<SearchUnlockIntimationDBTableDTO> resultPage = searchService.searchForUnlockIntimation(searchFormDTO,userName,passWord);
		List<SearchUnlockIntimationDBTableDTO> unlocklist = resultPage.getPageItems();
		if(unlocklist != null && !unlocklist.isEmpty()){
			for (SearchUnlockIntimationDBTableDTO searchUnlockIntimationDBTableDTO : unlocklist) {
				searchUnlockIntimationDBTableDTO.setLobKey(ReferenceTable.OMP_LOB_KEY);
			}
		}
		
		view.list(resultPage);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

} 

